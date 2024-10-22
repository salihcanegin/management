package com.company.management.configuration.exception;

import java.io.Serial;

public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = -1901637879182754871L;

    public static final int ERROR_CODE = 10500;

    public BusinessException(String messageKey) {
        super(messageKey);
    }
}
