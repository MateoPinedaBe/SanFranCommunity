package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.ReservationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

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

            @Query("""
                select count(r)
                from ReservationJpaEntity r
                where r.userId = :userId
                  and (r.status is null or r.status <> 'CANCELLED')
                """)
            long countActiveByUserId(@Param("userId") UUID userId);

            @Query("""
                select count(r)
                from ReservationJpaEntity r
                where r.facilityId = :facilityId
                  and (r.status is null or r.status <> 'CANCELLED')
                """)
            long countActiveByFacilityId(@Param("facilityId") UUID facilityId);

            @Modifying
            @Transactional
            @Query("""
                            delete from ReservationJpaEntity r
                            where r.userId = :userId
                                and (r.status is null or r.status = 'CANCELLED')
                            """)
            void deleteCancelledByUserId(@Param("userId") UUID userId);

            @Modifying
            @Transactional
            @Query("""
                            delete from ReservationJpaEntity r
                            where r.facilityId = :facilityId
                                and (r.status is null or r.status = 'CANCELLED')
                            """)
            void deleteCancelledByFacilityId(@Param("facilityId") UUID facilityId);
}
