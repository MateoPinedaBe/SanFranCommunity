package com.sanfran.community.domain.model.vo;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DomainValidatorsTest {

    @Test
    void validateUserShouldRejectInvalidEmail() {
        User user = new User(null, "Ana Maria", "CC12345", "invalid", "Password123", "ADMIN", "OWNER");

        assertThatThrownBy(() -> DomainValidators.validateUser(user))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email format is invalid.");
    }

    @Test
    void validateFacilityShouldRejectInvalidCapacity() {
        Facility facility = new Facility(null, "Salon", "Descripcion", "https://example.com/salon.jpg", 0);

        assertThatThrownBy(() -> DomainValidators.validateFacility(facility))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Capacity must be between 1 and 10000.");
    }

    @Test
    void validateReservationShouldRejectPastDate() {
        Reservation reservation = new Reservation(
            null,
            java.util.UUID.randomUUID(),
            java.util.UUID.randomUUID(),
            LocalDate.now().minusDays(1),
            LocalTime.NOON,
            LocalTime.NOON.plusHours(1)
        );

        assertThatThrownBy(() -> DomainValidators.validateReservation(reservation))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Date must not be in the past.");
    }

    @Test
    void normalizeRoleShouldUppercaseAndTrim() {
        assertThat(DomainValidators.normalizeRole(" resident ")).isEqualTo("RESIDENT");
        assertThat(DomainValidators.normalizeSubRole(" owner ")).isEqualTo("OWNER");
    }
}