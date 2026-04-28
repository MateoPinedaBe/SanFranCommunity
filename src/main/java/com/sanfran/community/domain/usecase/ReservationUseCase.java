package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.model.vo.DomainValidators;
import com.sanfran.community.domain.usecase.port.ReservationRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ReservationUseCase {

    private final ReservationRepository repository;

    public ReservationUseCase(ReservationRepository repository) {
        this.repository = repository;
    }

    public Reservation create(Reservation reservation) {
        DomainValidators.validateReservation(reservation);
        return repository.save(reservation);
    }

    public Reservation update(UUID id, Reservation reservation) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }

        DomainValidators.validateReservation(reservation);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The Reservation was not found in the database."));

        return repository.save(new Reservation(id, reservation.date(), reservation.startTime(), reservation.endTime()));
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        if (!repository.existsById(id)) {
            throw new NotFoundException("The Reservation was not found in the database.");
        }
        repository.deleteById(id);
    }

    public Reservation findById(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The Reservation was not found in the database."));
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public List<Reservation> findByDate(LocalDate date) {
        if (date == null) {
            throw new ValidationException("date", "Date must not be null.");
        }
        return repository.findByDate(date);
    }
}
