package com.company.presentation.rest;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.AbstractRequestLoggingFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class CustomLoggingFilter extends AbstractRequestLoggingFilter {

    public CustomLoggingFilter() {
        this.setIncludeClientInfo(true);
        this.setIncludeQueryString(true);
        this.setIncludeHeaders(true);
        this.setIncludePayload(true);
    }

    private static byte[] toByteArray(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        log.info(message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        // Do nothing.
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        String url = request.getRequestURI();
        if (url == null) {
            return false;
        }

        return log.isInfoEnabled();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();

        boolean isFirstRequest = !isAsyncDispatch(request);
        if ((!isFirstRequest) || (!this.shouldLog(request))) {
            filterChain.doFilter(request, response);
            return;
        }

        HttpServletRequest requestToUse = request;
        String contentType = request.getContentType();
        if ((this.isIncludePayload()) && (contentType != null)
            && (contentType.toLowerCase().contains("application/json"))) {
            byte[] body = toByteArray(request);
            requestToUse = new CachedHttpServletRequestWrapper(request, body);
        }

        ContentCachingResponseWrapper responseToUse = new ContentCachingResponseWrapper(response);
        try {
            String message = this.createMessage(requestToUse, "Request [", "]");
            this.beforeRequest(requestToUse, message);

            filterChain.doFilter(requestToUse, responseToUse);
        } finally {
            try {
                this.completeResponse(requestToUse, responseToUse, startTime);
            } finally {
                responseToUse.copyBodyToResponse();
            }
        }
    }

    @Override
    protected String getMessagePayload(HttpServletRequest request) {
        if (!(request instanceof CachedHttpServletRequestWrapper)) {
            return "[**NOT SUPPORT**]";
        }

        CachedHttpServletRequestWrapper wrapper = (CachedHttpServletRequestWrapper) request;
        if (wrapper.body.length == 0) {
            return null;
        }

        String payload = new String(wrapper.body, StandardCharsets.UTF_8);
        return StringUtils.hasText(payload) ? payload : null;
    }

    protected void completeResponse(HttpServletRequest request, HttpServletResponse response, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        String message = this.initCompleteMessage(request, response, duration);
        log.info(message);
    }

    protected String initCompleteMessage(HttpServletRequest request, HttpServletResponse response, long duration) {
        StringBuilder msg = new StringBuilder();
        msg.append("Response [");
        msg.append(request.getMethod()).append(' ').append(request.getRequestURI());

        msg.append(", status=").append(response.getStatus());

        if (this.isIncludePayload()) {
            String payload = this.initResponseMessagePayload(response);
            if (payload != null) {
                msg.append(", payload=").append(payload);
            }
        }

        msg.append(", duration=").append(duration).append("ms");
        msg.append("]");
        return msg.toString();
    }

    protected String initResponseMessagePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(
                response, ContentCachingResponseWrapper.class);
        if (wrapper == null) {
            return null;
        }

        byte[] buf = wrapper.getContentAsByteArray();
        String payload = new String(buf, StandardCharsets.UTF_8);

        return StringUtils.hasText(payload) ? payload : null;
    }

    private static class CachedHttpServletRequestWrapper extends HttpServletRequestWrapper {

        private final byte[] body;

        CachedHttpServletRequestWrapper(HttpServletRequest request, byte[] body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new CachedServletInputStream(this.body);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(this.getInputStream(), this.getCharacterEncoding()));
        }
    }

    private static class CachedServletInputStream extends ServletInputStream {

        private final byte[] body;

        private int cursor;

        private CachedServletInputStream(byte[] body) {
            this.body = body;
            this.cursor = 0;
        }

        @Override
        public boolean isFinished() {
            return (this.body.length <= this.cursor);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            // Do nothing.
        }

        @Override
        public int read() throws IOException {
            if (this.isFinished()) {
                return -1;
            }

            return this.body[this.cursor++];
        }
    }
}
