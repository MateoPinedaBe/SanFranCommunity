package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UserRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserUseCase useCase;

    public UserController(UserUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserRequest request) {
        User created = useCase.create(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, toResponse(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable UUID id, @Valid @RequestBody UserRequest request) {
        User updated = useCase.update(id, toDomain(request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String names
    ) {
        if (id != null) {
            return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
        }

        List<User> data = (names == null || names.isBlank()) ? useCase.findAll() : useCase.findByNames(names);
        return ResponseEntity.ok(ApiResponse.success(data.stream().map(this::toResponse).toList()));
    }

    private User toDomain(UserRequest request) {
        return new User(
                null,
                request.names(),
                request.idDocument(),
                request.email(),
                request.password(),
                request.role(),
                request.subRole()
        );
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.id(),
                user.names(),
                user.idDocument(),
                user.email(),
                user.role(),
                user.subRole()
        );
    }
}
