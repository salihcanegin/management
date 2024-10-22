package com.company.management.configuration.exception;


import java.io.Serial;

public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1501317669042329287L;

    private final String messageKey;

    public BaseException(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getMessageKey() {
        return messageKey;
    }
}
