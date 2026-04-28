package com.sanfran.community.domain.model.exception;

public final class ValidationException extends RuntimeException implements DomainException {

    private final String field;

    public ValidationException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
