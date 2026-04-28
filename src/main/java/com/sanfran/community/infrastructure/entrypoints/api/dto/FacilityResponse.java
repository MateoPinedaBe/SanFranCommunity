package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record FacilityResponse(
        @Schema(description = "Unique facility identifier")
        UUID id,
        @Schema(description = "Facility display name")
        String name,
        @Schema(description = "Short description of the facility")
        String description,
        @Schema(description = "Public image URL for the facility")
        String imageUrl,
        @Schema(description = "Maximum allowed capacity")
        Integer capacity
) {
}