package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FacilityRequest(
        @Schema(description = "Facility display name", example = "Salon Social")
        @NotBlank @Size(min = 2, max = 100) String name,
        @Schema(description = "Short description of the facility", example = "Espacio para eventos y reuniones")
        @NotBlank @Size(max = 500) String description,
        @Schema(description = "Public image URL for the facility", example = "https://example.com/salon.jpg")
        @NotBlank String imageUrl,
        @Schema(description = "Maximum allowed capacity", example = "100")
        @NotNull @Min(1) @Max(10000) Integer capacity
) {
}
