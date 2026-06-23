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
        // Ensure status default
        Reservation r = reservation.status() == null ? new Reservation(
                reservation.id(), reservation.userId(), reservation.facilityId(), reservation.date(), reservation.startTime(), reservation.endTime(), com.sanfran.community.domain.model.entity.ReservationStatus.PENDING
        ) : reservation;
        DomainValidators.validateReservation(r);
        ensureReferencesExist(r);
        ensureNoConflict(r, null);
        return repository.save(r);
    }

    public Reservation update(UUID id, Reservation reservation) {
        if (id == null) {
            throw new ValidationException("id", "El ID no debe ser nulo.");
        }

        DomainValidators.validateReservation(reservation);
    ensureReferencesExist(reservation);
    ensureNoConflict(reservation, id);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("La reserva no se encontró en la base de datos."));
    return repository.save(new Reservation(
        id,
        reservation.userId(),
        reservation.facilityId(),
        reservation.date(),
        reservation.startTime(),
        reservation.endTime(),
        reservation.status()
    ));
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "El ID no debe ser nulo.");
        }
        if (!repository.existsById(id)) {
            throw new NotFoundException("La reserva no se encontró en la base de datos.");
        }
        repository.deleteById(id);
    }

    public Reservation findById(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "El ID no debe ser nulo.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("La reserva no se encontró en la base de datos."));
    }

    public List<Reservation> findAll() {
        return repository.findAll();
    }

    public List<Reservation> findByDate(LocalDate date) {
        if (date == null) {
            throw new ValidationException("date", "La fecha no debe ser nula.");
        }
        return repository.findByDate(date);
    }

    private void ensureReferencesExist(Reservation reservation) {
        if (!userRepository.existsById(reservation.userId())) {
            throw new NotFoundException("El usuario no se encontró en la base de datos.");
        }

        if (!facilityRepository.existsById(reservation.facilityId())) {
            throw new NotFoundException("La instalación no se encontró en la base de datos.");
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
            throw new BusinessRuleException("La instalación ya está reservada para el rango de horario seleccionado.");
        }
    }
}
