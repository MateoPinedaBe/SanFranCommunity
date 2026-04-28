package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationRequest(
        @Schema(description = "Identifier of the user that creates the reservation")
        @NotNull UUID userId,
        @Schema(description = "Identifier of the reserved facility")
        @NotNull UUID facilityId,
        @Schema(description = "Reservation date", example = "2026-05-10")
        @NotNull LocalDate date,
        @Schema(description = "Reservation start time", example = "09:00:00")
        @NotNull LocalTime startTime,
        @Schema(description = "Reservation end time", example = "11:00:00")
        @NotNull LocalTime endTime
) {
}
