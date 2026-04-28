package com.sanfran.community.infrastructure.entrypoints.api;

import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationRequest;
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
public class ReservationController {

    private final ReservationUseCase useCase;

    public ReservationController(ReservationUseCase useCase) {
        this.useCase = useCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Reservation>> create(@Valid @RequestBody ReservationRequest request) {
        Reservation created = useCase.create(toDomain(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(201, created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Reservation>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReservationRequest request
    ) {
        Reservation updated = useCase.update(id, toDomain(request));
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        useCase.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Reservation>> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(useCase.findById(id)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Reservation>>> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<Reservation> data = (date == null) ? useCase.findAll() : useCase.findByDate(date);
        return ResponseEntity.ok(ApiResponse.success(data));
    }

    private Reservation toDomain(ReservationRequest request) {
        return new Reservation(
                null,
                request.date(),
                request.startTime(),
                request.endTime()
        );
    }
}
