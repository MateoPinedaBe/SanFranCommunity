package com.sanfran.community.domain.model.exception;

public sealed interface DomainException permits ValidationException, BusinessRuleException, NotFoundException {
}
