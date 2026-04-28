package com.sanfran.community.domain.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EntityTests {

    @Test
    void userRecordShouldPreserveValues() {
        UUID id = UUID.randomUUID();
        User user = new User(id, "Ana", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");

        assertThat(user.id()).isEqualTo(id);
        assertThat(user.email()).isEqualTo("ana@test.com");
        assertThat(user.role()).isEqualTo("ADMIN");
    }

    @Test
    void facilityRecordShouldPreserveValues() {
        UUID id = UUID.randomUUID();
        Facility facility = new Facility(id, "Salon", "Descripcion", "https://example.com/salon.jpg", 50);

        assertThat(facility.id()).isEqualTo(id);
        assertThat(facility.capacity()).isEqualTo(50);
    }

    @Test
    void reservationRecordShouldPreserveValues() {
        UUID id = UUID.randomUUID();
        Reservation reservation = new Reservation(
                id,
                UUID.randomUUID(),
                UUID.randomUUID(),
                LocalDate.now().plusDays(1),
                LocalTime.NOON,
                LocalTime.NOON.plusHours(1)
        );

        assertThat(reservation.id()).isEqualTo(id);
        assertThat(reservation.userId()).isNotNull();
        assertThat(reservation.endTime()).isAfter(reservation.startTime());
    }
}