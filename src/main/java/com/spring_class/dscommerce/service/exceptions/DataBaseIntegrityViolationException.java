package com.spring_class.dscommerce.service.exceptions;

public class DataBaseIntegrityViolationException extends RuntimeException {

    public DataBaseIntegrityViolationException(String message) {
        super(message);
    }

    public DataBaseIntegrityViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
