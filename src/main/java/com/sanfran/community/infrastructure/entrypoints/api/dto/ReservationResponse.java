package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record ReservationResponse(
        @Schema(description = "Unique reservation identifier")
        UUID id,
        @Schema(description = "Identifier of the user that owns the reservation")
        UUID userId,
        @Schema(description = "Identifier of the reserved facility")
        UUID facilityId,
        @Schema(description = "Reservation date")
        LocalDate date,
        @Schema(description = "Reservation start time")
        LocalTime startTime,
        @Schema(description = "Reservation end time")
        LocalTime endTime
        ,
        @Schema(description = "Reservation status")
        String status
) {
}