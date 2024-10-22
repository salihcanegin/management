package com.company.management.configuration.exception;

import java.io.Serial;

public class AlreadyExistException extends BaseException {

    @Serial
    private static final long serialVersionUID = -7501637879182754874L;

    public static final int ERROR_CODE = 10200;

    public AlreadyExistException(String messageKey) {
        super(messageKey);
    }
}
