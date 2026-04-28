package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.ReservationJpaEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ReservationJpaRepositoryIntegrationTest {

    private static final UUID SEEDED_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID SEEDED_FACILITY_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @Autowired
    private ReservationJpaRepository reservationJpaRepository;

    @Test
    void existsConflictShouldReturnTrueForOverlappingReservation() {
        ReservationJpaEntity entity = new ReservationJpaEntity();
        entity.setUserId(SEEDED_USER_ID);
        entity.setFacilityId(SEEDED_FACILITY_ID);
        entity.setDate(LocalDate.now().plusDays(2));
        entity.setStartTime(LocalTime.of(9, 0));
        entity.setEndTime(LocalTime.of(11, 0));
        reservationJpaRepository.save(entity);

        boolean result = reservationJpaRepository.existsConflict(
                SEEDED_FACILITY_ID,
                entity.getDate(),
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                null
        );

        assertThat(result).isTrue();
    }

    @Test
    void existsConflictShouldReturnFalseWhenOnlyCurrentReservationIsExcluded() {
        ReservationJpaEntity entity = new ReservationJpaEntity();
        entity.setUserId(SEEDED_USER_ID);
        entity.setFacilityId(SEEDED_FACILITY_ID);
        entity.setDate(LocalDate.now().plusDays(3));
        entity.setStartTime(LocalTime.of(14, 0));
        entity.setEndTime(LocalTime.of(16, 0));
        ReservationJpaEntity saved = reservationJpaRepository.save(entity);

        boolean result = reservationJpaRepository.existsConflict(
                SEEDED_FACILITY_ID,
                saved.getDate(),
                LocalTime.of(14, 30),
                LocalTime.of(15, 30),
                saved.getId()
        );

        assertThat(result).isFalse();
    }
}