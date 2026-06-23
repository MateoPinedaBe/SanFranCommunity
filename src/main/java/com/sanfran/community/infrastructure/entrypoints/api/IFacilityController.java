package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface IFacilityController {
    ResponseEntity<ApiResponse<FacilityResponse>> create(FacilityRequest request);

    ResponseEntity<ApiResponse<FacilityResponse>> update(UUID id, FacilityRequest request);

    ResponseEntity<ApiResponse<FacilityResponse>> updateByQuery(UUID id, String name, FacilityRequest request);

    ResponseEntity<ApiResponse<String>> delete(UUID id);

    ResponseEntity<ApiResponse<String>> deleteByQuery(UUID id, String name);

    ResponseEntity<ApiResponse<FacilityResponse>> findById(UUID id);

    ResponseEntity<ApiResponse<?>> findAll(UUID id, String name);
}
