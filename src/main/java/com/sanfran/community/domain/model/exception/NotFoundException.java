package com.sanfran.community.domain.model.exception;

public final class NotFoundException extends RuntimeException implements DomainException {

    public NotFoundException(String message) {
        super(message);
    }
}
