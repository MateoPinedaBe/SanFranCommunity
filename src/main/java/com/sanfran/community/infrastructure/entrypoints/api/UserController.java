package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UpdateUserRequest;
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
public class UserController implements IUserController {

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
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateUserRequest request) {
        User updated = useCase.update(id, toDomain(request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<UserResponse>> updateByQuery(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String names,
            @Valid @RequestBody UpdateUserRequest request
    ) {
        UUID resolvedId = resolveUserIdByQuery(id, names, "update");
        User updated = useCase.update(resolvedId, toDomain(request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully."));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteByQuery(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String names
    ) {
        UUID resolvedId = resolveUserIdByQuery(id, names, "delete");
        useCase.delete(resolvedId);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String names,
            @RequestParam(required = false) String email
    ) {
        if (id != null) {
            return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
        }

        if (email != null && !email.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success(useCase.findByEmail(email).stream().map(this::toResponse).toList()));
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

    private User toDomain(UpdateUserRequest request) {
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

    private UUID resolveUserIdByQuery(UUID id, String names, String operation) {
        if (id != null) {
            return id;
        }

        if (names == null || names.isBlank()) {
            throw new ValidationException("id", "Provide either id or names query parameter to " + operation + " a user.");
        }

        String normalizedName = names.trim();
        List<User> matches = useCase.findByNames(normalizedName).stream()
                .filter(user -> user.names() != null && user.names().equalsIgnoreCase(normalizedName))
                .toList();

        if (matches.isEmpty()) {
            throw new NotFoundException("The User was not found in the database.");
        }

        if (matches.size() > 1) {
            throw new BusinessRuleException("Multiple users match the provided name. Use id to " + operation + " a specific user.");
        }

        return matches.getFirst().id();
    }
}
