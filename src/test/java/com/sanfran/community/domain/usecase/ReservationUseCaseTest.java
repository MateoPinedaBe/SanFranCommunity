package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.usecase.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class ReservationUseCaseTest {

    @Mock
    private ReservationRepository repository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FacilityRepository facilityRepository;

    private ReservationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReservationUseCase(repository, userRepository, facilityRepository);
    }

    @Test
    void createShouldRejectPastDate() {
        Reservation reservation = new Reservation(
                null,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now().minusDays(1),
                LocalTime.NOON,
                LocalTime.NOON.plusHours(1)
        );

        assertThatThrownBy(() -> useCase.create(reservation))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Date must not be in the past.");
    }

    @Test
    void createShouldRejectWhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        Reservation reservation = new Reservation(null, userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));

        when(userRepository.existsById(userId)).thenReturn(false);

        assertThatThrownBy(() -> useCase.create(reservation))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The User was not found in the database.");
    }

    @Test
    void createShouldRejectTimeOverlapForFacility() {
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        Reservation reservation = new Reservation(null, userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));

        when(userRepository.existsById(userId)).thenReturn(true);
        when(facilityRepository.existsById(facilityId)).thenReturn(true);
        when(repository.existsConflict(facilityId, reservation.date(), reservation.startTime(), reservation.endTime(), null)).thenReturn(true);

        assertThatThrownBy(() -> useCase.create(reservation))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("The facility is already reserved for the selected time range.");
    }

    @Test
    void findByDateShouldRejectNullDate() {
        assertThatThrownBy(() -> useCase.findByDate(null))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Date must not be null.");
    }
}