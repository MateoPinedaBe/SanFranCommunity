package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {
    List<ReservationJpaEntity> findByDate(LocalDate date);
}
