package com.sanfran.community.domain.model.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record Reservation(
        UUID id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {
}
