package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationUseCaseTest {

    @Mock
    private ReservationRepository repository;

    private ReservationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReservationUseCase(repository);
    }

    @Test
    void createShouldRejectPastDate() {
        Reservation reservation = new Reservation(null, LocalDate.now().minusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));

        assertThatThrownBy(() -> useCase.create(reservation))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Date must not be in the past.");
    }

    @Test
    void findByDateShouldRejectNullDate() {
        assertThatThrownBy(() -> useCase.findByDate(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Date must not be null.");
    }
}