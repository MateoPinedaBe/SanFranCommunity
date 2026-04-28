package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityResponse;
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
@RequestMapping("/api/v1/facilities")
public class FacilityController {

    private final FacilityUseCase useCase;

    public FacilityController(FacilityUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<FacilityResponse>> create(@Valid @RequestBody FacilityRequest request) {
        Facility created = useCase.create(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, toResponse(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<FacilityResponse>> update(@PathVariable UUID id, @Valid @RequestBody FacilityRequest request) {
        Facility updated = useCase.update(id, toDomain(request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Facility deleted successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<FacilityResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String name
    ) {
        if (id != null) {
            return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
        }

        List<Facility> data = (name == null || name.isBlank()) ? useCase.findAll() : useCase.findByName(name);
        return ResponseEntity.ok(ApiResponse.success(data.stream().map(this::toResponse).toList()));
    }

    private Facility toDomain(FacilityRequest request) {
        return new Facility(
                null,
                request.name(),
                request.description(),
                request.imageUrl(),
                request.capacity()
        );
    }

    private FacilityResponse toResponse(Facility facility) {
        return new FacilityResponse(
                facility.id(),
                facility.name(),
                facility.description(),
                facility.imageUrl(),
                facility.capacity()
        );
    }
}
