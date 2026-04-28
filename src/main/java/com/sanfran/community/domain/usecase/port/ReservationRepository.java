package com.sanfran.community.domain.usecase.port;

import com.sanfran.community.domain.model.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Optional<Reservation> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<Reservation> findAll();

    List<Reservation> findByDate(LocalDate date);

    boolean existsConflict(UUID facilityId, LocalDate date, LocalTime startTime, LocalTime endTime, UUID excludedReservationId);
}
