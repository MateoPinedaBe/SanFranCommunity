package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, UUID> {
    List<ReservationJpaEntity> findByDate(LocalDate date);

        @Query("""
                        select count(r) > 0
                        from ReservationJpaEntity r
                        where r.facilityId = :facilityId
                            and r.date = :date
                            and (:excludedReservationId is null or r.id <> :excludedReservationId)
                            and r.startTime < :endTime
                                and r.endTime > :startTime
                                and (r.status is null or r.status <> 'CANCELLED')
                        """)
        boolean existsConflict(
                        @Param("facilityId") UUID facilityId,
                        @Param("date") LocalDate date,
                        @Param("startTime") LocalTime startTime,
                        @Param("endTime") LocalTime endTime,
                        @Param("excludedReservationId") UUID excludedReservationId
        );
}
