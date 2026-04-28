package com.sanfran.community.infrastructure.drivenadapters.jpa;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.ReservationJpaEntity;
import com.sanfran.community.infrastructure.drivenadapters.jpa.repository.ReservationJpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository repository;

    public ReservationRepositoryAdapter(ReservationJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaEntity entity = toEntity(reservation);
        ReservationJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Reservation> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Reservation> findByDate(LocalDate date) {
        return repository.findByDate(date).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsConflict(
            UUID facilityId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime,
            UUID excludedReservationId
    ) {
        return repository.existsConflict(facilityId, date, startTime, endTime, excludedReservationId);
    }

    private ReservationJpaEntity toEntity(Reservation reservation) {
        ReservationJpaEntity entity = new ReservationJpaEntity();
        entity.setId(reservation.id());
        entity.setUserId(reservation.userId());
        entity.setFacilityId(reservation.facilityId());
        entity.setDate(reservation.date());
        entity.setStartTime(reservation.startTime());
        entity.setEndTime(reservation.endTime());
        return entity;
    }

    private Reservation toDomain(ReservationJpaEntity entity) {
        return new Reservation(
                entity.getId(),
            entity.getUserId(),
            entity.getFacilityId(),
                entity.getDate(),
                entity.getStartTime(),
                entity.getEndTime()
        );
    }
}
