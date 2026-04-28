package com.sanfran.community.domain.model.exception;

public final class BusinessRuleException extends RuntimeException implements DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
