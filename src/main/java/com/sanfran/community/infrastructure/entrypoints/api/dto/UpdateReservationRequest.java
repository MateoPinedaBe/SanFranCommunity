package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationRequest(
        @Schema(description = "Reservation date", example = "2026-05-10")
        @NotNull LocalDate date,
        @Schema(description = "Reservation start time", example = "09:00:00")
        @NotNull LocalTime startTime,
        @Schema(description = "Reservation end time", example = "11:00:00")
        @NotNull LocalTime endTime,
        @Schema(description = "Reservation status", example = "PENDING")
        String status
) {
}
