package com.company.presentation.rest;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    // Default exception
    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception exception) {
        log.error(exception);
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Not found url handler
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException exception,
                                                                   HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(exception);
        return new ResponseEntity<>(exception.getMessage(), status);
    }

    //    // Not support HTTP Method
    //    @Override
    //    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
    //            HttpRequestMethodNotSupportedException exception, HttpHeaders headers, HttpStatusCode status,
    //            WebRequest request) {
    //        String message = getMessageFromHttpRequestMethodNotSupportedException(exception);
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 3;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, exception);
    //
    //        log.error(detailMessage, exception);
    //        return new ResponseEntity<>(response, status);
    //    }
    //
    //    private String getMessageFromHttpRequestMethodNotSupportedException(
    //            HttpRequestMethodNotSupportedException exception) {
    //        String message = exception.getMethod() + " method is not supported for this request. Supported methods are";
    //        for (HttpMethod method : exception.getSupportedHttpMethods()) {
    //            message += " " + method;
    //        }
    //
    //        return message;
    //    }
    //
    //    // Not support media type
    //    @Override
    //    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException exception,
    //                                                                     HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    //        String message = getMessageFromHttpMediaTypeNotSupportedException(exception);
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 4;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, exception);
    //
    //        log.error(detailMessage, exception);
    //        return new ResponseEntity<>(response, status);
    //    }
    //
    //    private String getMessageFromHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException exception) {
    //        String message = exception.getContentType() + " media type is not supported. Supported media types are ";
    //        for (MediaType method : exception.getSupportedMediaTypes()) {
    //            message += method + ", ";
    //        }
    //        return message.substring(0, message.length() - 2);
    //    }
    //
    //    // missing parameter
    //    @Override
    //    protected ResponseEntity<Object> handleMissingServletRequestParameter(
    //            MissingServletRequestParameterException exception, HttpHeaders headers, HttpStatusCode status,
    //            WebRequest request) {
    //        String message = exception.getParameterName() + " parameter is missing";
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 5;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, exception);
    //
    //        return new ResponseEntity<>(response, status);
    //    }
    //
    //    // wrong parameter type
    //    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    //    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exception) {
    //
    //        String message = exception.getName() + " should be of type " + exception.getRequiredType().getName();
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 6;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, new Exception());
    //
    //        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    //    }
    //
    //    // bean validation
    //    @Override
    //    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,
    //                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
    //        String message = "Validation: Parameters is error!";
    //        String detailMessage = exception.getLocalizedMessage();
    //        // error
    //        Map<String, String> errors = new HashMap<>();
    //        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
    //            String fieldName = ((FieldError) error).getField();
    //            String errorMessage = error.getDefaultMessage();
    //            errors.put(fieldName, errorMessage);
    //        }
    //        int code = 7;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, errors);
    //
    //        return new ResponseEntity<>(response, status);
    //    }
    //
    //    // bean validation
    //    @SuppressWarnings("rawtypes")
    //    @ExceptionHandler(ConstraintViolationException.class)
    //    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
    //        String message = "Validation: Parameters is error!";
    //        String detailMessage = exception.getLocalizedMessage();
    //        // error
    //        Map<String, String> errors = new HashMap<>();
    //        for (ConstraintViolation violation : exception.getConstraintViolations()) {
    //            String fieldName = violation.getPropertyPath().toString();
    //            String errorMessage = violation.getMessage();
    //            errors.put(fieldName, errorMessage);
    //        }
    //        int code = 8;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, errors);
    //
    //        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    //    }
    //
    //    // 401 unauthorized
    //    @ExceptionHandler(BadCredentialsException.class)
    //    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
    //        String message = "Authentication Failed";
    //        String detailMessage = exception.getLocalizedMessage();
    //        int code = 9;
    //
    //        ErrorResponse response = new ErrorResponse(message, detailMessage, code, exception);
    //
    //        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    //    }

}
