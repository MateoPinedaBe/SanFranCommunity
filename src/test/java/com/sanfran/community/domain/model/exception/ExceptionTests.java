package com.sanfran.community.domain.model.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionTests {

    @Test
    void validationExceptionShouldStoreFieldAndMessage() {
        ValidationException exception = new ValidationException("email", "Email is invalid");

        assertThat(exception.getField()).isEqualTo("email");
        assertThat(exception).hasMessage("Email is invalid");
    }

    @Test
    void businessRuleExceptionShouldStoreMessage() {
        BusinessRuleException exception = new BusinessRuleException("Business rule violated");

        assertThat(exception).hasMessage("Business rule violated");
    }

    @Test
    void notFoundExceptionShouldStoreMessage() {
        NotFoundException exception = new NotFoundException("Resource not found");

        assertThat(exception).hasMessage("Resource not found");
    }
}