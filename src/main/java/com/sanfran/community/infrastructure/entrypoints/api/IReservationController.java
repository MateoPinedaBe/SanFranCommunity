package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationResponse;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UpdateReservationRequest;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.UUID;

public interface IReservationController {
    ResponseEntity<ApiResponse<ReservationResponse>> create(ReservationRequest request);

    ResponseEntity<ApiResponse<ReservationResponse>> update(UUID id, UpdateReservationRequest request);

    ResponseEntity<ApiResponse<ReservationResponse>> updateByQuery(UUID id, UpdateReservationRequest request);

    ResponseEntity<ApiResponse<String>> delete(UUID id);

    ResponseEntity<ApiResponse<String>> deleteByQuery(UUID id);

    ResponseEntity<ApiResponse<ReservationResponse>> findById(UUID id);

    ResponseEntity<ApiResponse<?>> findAll(UUID id, LocalDate date);
}
