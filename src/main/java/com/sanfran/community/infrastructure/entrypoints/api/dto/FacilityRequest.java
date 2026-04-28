package com.sanfran.community.infrastructure.entrypoints.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FacilityRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotBlank @Size(max = 500) String description,
        @NotBlank String imageUrl,
        @NotNull @Min(1) @Max(10000) Integer capacity
) {
}
