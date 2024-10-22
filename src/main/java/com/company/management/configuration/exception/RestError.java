package com.company.management.configuration.exception;

import org.springframework.http.HttpStatus;

public class RestError {
    private final int code;
    private final String message;
    private final HttpStatus status;

    public RestError(int code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}