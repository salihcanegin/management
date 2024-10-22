package com.company.management.configuration.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {

    private final MessageSource messageSource;

    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestError> alreadyExistExceptionHandler(AlreadyExistException exception) {
        final String message = getMessage(exception.getMessageKey());
        return getErrorResponse(HttpStatus.BAD_REQUEST, message, AlreadyExistException.ERROR_CODE);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestError> notFoundExceptionHandler(NotFoundException exception) {
        final String message = getMessage(exception.getMessageKey());
        return getErrorResponse(HttpStatus.NOT_FOUND, message, NotFoundException.ERROR_CODE);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<RestError> businessExceptionHandler(BusinessException exception) {
        final String message = getMessage(exception.getMessageKey());
        return getErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY, message, BusinessException.ERROR_CODE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestError> methodArgumentNotValidHandler(MethodArgumentNotValidException exception) {
        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        final String errorMessage = fieldErrors.stream()
                .map(this::convertFieldErrorMessage)
                .collect(Collectors.joining(","));
        return getErrorResponse(HttpStatus.BAD_REQUEST, errorMessage);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestError> defaultHandler(Exception exception) {
        final String message = getMessage("error.occurred");
        return getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestError> defaultHandler(AuthenticationException exception) {
        final String message = getMessage("login.unsuccessful");
        return getErrorResponse(HttpStatus.UNAUTHORIZED, message);
    }

    private String convertFieldErrorMessage(FieldError fieldError) {
        String messageKey = String.format("%s.%s.%s",
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getCode())
                .replace(" ", ".");
        return getMessage(messageKey);
    }

    private String getMessage(String messageKey, String... params) {
        return messageSource.getMessage(messageKey, params, LocaleContextHolder.getLocale());
    }

    private ResponseEntity<RestError> getErrorResponse(HttpStatus httpStatus, String message) {
        final RestError error = new RestError(
                httpStatus.value(),
                message,
                httpStatus
        );
        return new ResponseEntity<>(error, httpStatus);
    }

    private ResponseEntity<RestError> getErrorResponse(HttpStatus httpStatus, String message, int errorCode) {
        final RestError error = new RestError(
                errorCode,
                message,
                httpStatus
        );
        return new ResponseEntity<>(error, httpStatus);
    }
}
