package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationRequest;
import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationResponse;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UpdateReservationRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController implements IReservationController {

    private final ReservationUseCase useCase;

    public ReservationController(ReservationUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> create(@Valid @RequestBody ReservationRequest request) {
        Reservation created = useCase.create(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, toResponse(created)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateReservationRequest request
    ) {
        Reservation existing = useCase.findById(id);
        Reservation updated = useCase.update(id, toDomainUpdate(existing, request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ReservationResponse>> updateByQuery(
            @RequestParam(required = false) UUID id,
            @Valid @RequestBody UpdateReservationRequest request
    ) {
        UUID resolvedId = resolveReservationId(id, "update");
        Reservation existing = useCase.findById(resolvedId);
        Reservation updated = useCase.update(resolvedId, toDomainUpdate(existing, request));
        return ResponseEntity.ok(ApiResponse.success(toResponse(updated)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully."));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> deleteByQuery(@RequestParam(required = false) UUID id) {
        UUID resolvedId = resolveReservationId(id, "delete");
        useCase.delete(resolvedId);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationResponse>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (id != null) {
            return ResponseEntity.ok(ApiResponse.success(toResponse(useCase.findById(id))));
        }

        List<Reservation> data = (date == null) ? useCase.findAll() : useCase.findByDate(date);
        return ResponseEntity.ok(ApiResponse.success(data.stream().map(this::toResponse).toList()));
    }

    private Reservation toDomain(ReservationRequest request) {
        return new Reservation(
                null,
                request.userId(),
                request.facilityId(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.status() == null ? com.sanfran.community.domain.model.entity.ReservationStatus.PENDING : com.sanfran.community.domain.model.entity.ReservationStatus.valueOf(request.status())
        );
    }

    private Reservation toDomainUpdate(Reservation existing, UpdateReservationRequest request) {
        return new Reservation(
                null,
                existing.userId(),
                existing.facilityId(),
                request.date(),
                request.startTime(),
                request.endTime(),
                request.status() == null ? existing.status() : com.sanfran.community.domain.model.entity.ReservationStatus.valueOf(request.status())
        );
    }

    private ReservationResponse toResponse(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.userId(),
                reservation.facilityId(),
                reservation.date(),
                reservation.startTime(),
                reservation.endTime(),
                reservation.status() == null ? "PENDING" : reservation.status().name()
        );
    }

    private UUID resolveReservationId(UUID id, String operation) {
        if (id == null) {
            throw new ValidationException("id", "Provide id query parameter to " + operation + " a reservation.");
        }
        return id;
    }
}
