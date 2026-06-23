package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.infrastructure.entrypoints.api.dto.UpdateUserRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UserRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UserResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface IUserController {
    ResponseEntity<ApiResponse<UserResponse>> create(UserRequest request);

    ResponseEntity<ApiResponse<UserResponse>> update(UUID id, UpdateUserRequest request);

    ResponseEntity<ApiResponse<UserResponse>> updateByQuery(UUID id, String names, UpdateUserRequest request);

    ResponseEntity<ApiResponse<String>> delete(UUID id);

    ResponseEntity<ApiResponse<String>> deleteByQuery(UUID id, String names);

    ResponseEntity<ApiResponse<UserResponse>> findById(UUID id);

    ResponseEntity<ApiResponse<?>> findAll(UUID id, String names, String email);
}
