package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IReservationUseCase {
    Reservation create(Reservation reservation);

    Reservation update(UUID id, Reservation reservation);

    void delete(UUID id);

    Reservation findById(UUID id);

    List<Reservation> findAll();

    List<Reservation> findByDate(LocalDate date);
}
