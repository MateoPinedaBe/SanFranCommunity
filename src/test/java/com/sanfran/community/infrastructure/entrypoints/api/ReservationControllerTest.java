package com.sanfran.community.infrastructure.entrypoints.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.usecase.ReservationUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.ReservationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservationControllerTest {

    private MockMvc mockMvc;
    private ReservationUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        useCase = mock(ReservationUseCase.class);
        objectMapper = new ObjectMapper().findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(new ReservationController(useCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createShouldReturn201() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        ReservationRequest request = new ReservationRequest(userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));
        Reservation reservation = new Reservation(UUID.randomUUID(), userId, facilityId, request.date(), request.startTime(), request.endTime());
        when(useCase.create(any(Reservation.class))).thenReturn(reservation);

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.userId").value(userId.toString()))
            .andExpect(jsonPath("$.data.facilityId").value(facilityId.toString()));
    }

    @Test
    void findAllShouldReturn200() throws Exception {
        when(useCase.findAll()).thenReturn(List.of(
            new Reservation(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1))
        ));

        mockMvc.perform(get("/api/v1/reservations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data[0].userId").exists())
                .andExpect(jsonPath("$.data[0].startTime").value("12:00:00"));
    }

    @Test
    void findByIdShouldAcceptQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        Reservation reservation = new Reservation(id, userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));
        when(useCase.findById(id)).thenReturn(reservation);

        mockMvc.perform(get("/api/v1/reservations").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.userId").value(userId.toString()))
                .andExpect(jsonPath("$.data.facilityId").value(facilityId.toString()));
    }

    @Test
    void createShouldReturn422WhenReservationConflicts() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        ReservationRequest request = new ReservationRequest(userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));

        when(useCase.create(any(Reservation.class)))
                .thenThrow(new BusinessRuleException("The facility is already reserved for the selected time range."));

        mockMvc.perform(post("/api/v1/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.data.error").value("Business Rule Violation"))
                .andExpect(jsonPath("$.data.message").value("The facility is already reserved for the selected time range."));
    }

    @Test
    void updateShouldAcceptIdQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID facilityId = UUID.randomUUID();
        ReservationRequest request = new ReservationRequest(userId, facilityId, LocalDate.now().plusDays(1), LocalTime.NOON, LocalTime.NOON.plusHours(1));
        Reservation updated = new Reservation(id, userId, facilityId, request.date(), request.startTime(), request.endTime());

        when(useCase.update(eq(id), any(Reservation.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/reservations")
                        .param("id", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    void deleteShouldAcceptIdQueryParam() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/reservations").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("Reservation deleted successfully."));

        verify(useCase).delete(id);
    }
}