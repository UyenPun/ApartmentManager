package com.company.domain.exception;

public class ResidentServiceException extends RuntimeException {

    public ResidentServiceException(String message) {
        super(message);
    }

    public ResidentServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
