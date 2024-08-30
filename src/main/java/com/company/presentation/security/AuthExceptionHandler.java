package com.company.presentation.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthExceptionHandler implements AuthenticationEntryPoint {

    // 401 unauthorized
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
    // move to ExceptionHandler with AccessDeniedException
    // 403 Forbidden
    //    @Override
    //    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception)
    //            throws IOException, ServletException {
    //
    //        String message = "Access is denied";
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 10;
    //
    //        ErrorResponse errorResponse = new ErrorResponse(message, detailMessage, code, exception);
    //
    //        addErrorResponseToBodyResponse(errorResponse, response, HttpServletResponse.SC_FORBIDDEN);
    //    }


}
