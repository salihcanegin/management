package com.company.management.configuration.exception;

import java.io.Serial;

public class NotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = -6901638879182754875L;

    public static final int ERROR_CODE = 10300;

    public NotFoundException(String messageKey) {
        super(messageKey);
    }
}
