package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.model.vo.DomainValidators;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.usecase.port.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class ReservationUseCase {

    private final ReservationRepository repository;
    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;

    public ReservationUseCase(
            ReservationRepository repository,
            UserRepository userRepository,
            FacilityRepository facilityRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.facilityRepository = facilityRepository;
    }

    public Reservation create(Reservation reservation) {
        DomainValidators.validateReservation(reservation);
        ensureReferencesExist(reservation);
        ensureNoConflict(reservation, null);
        return repository.save(reservation);
    }

    public Reservation update(UUID id, Reservation reservation) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }

        DomainValidators.validateReservation(reservation);
    ensureReferencesExist(reservation);
    ensureNoConflict(reservation, id);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The Reservation was not found in the database."));

    return repository.save(new Reservation(
        id,
        reservation.userId(),
        reservation.facilityId(),
        reservation.date(),
        reservation.startTime(),
        reservation.endTime()
    ));
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

    private void ensureReferencesExist(Reservation reservation) {
        if (!userRepository.existsById(reservation.userId())) {
            throw new NotFoundException("The User was not found in the database.");
        }

        if (!facilityRepository.existsById(reservation.facilityId())) {
            throw new NotFoundException("The Facility was not found in the database.");
        }
    }

    private void ensureNoConflict(Reservation reservation, UUID excludedReservationId) {
        boolean conflict = repository.existsConflict(
                reservation.facilityId(),
                reservation.date(),
                reservation.startTime(),
                reservation.endTime(),
                excludedReservationId
        );

        if (conflict) {
            throw new BusinessRuleException("The facility is already reserved for the selected time range.");
        }
    }
}
