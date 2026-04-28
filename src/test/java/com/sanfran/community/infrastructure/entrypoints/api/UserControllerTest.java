package com.sanfran.community.infrastructure.entrypoints.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.usecase.UserUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.UserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest {

    private MockMvc mockMvc;
    private UserUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        useCase = mock(UserUseCase.class);
        objectMapper = new ObjectMapper().findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(useCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createShouldReturn201AndHidePassword() throws Exception {
        UUID id = UUID.randomUUID();
        User saved = new User(id, "Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");
        UserRequest request = new UserRequest("Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");

        when(useCase.create(any(User.class))).thenReturn(saved);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.email").value("ana@test.com"))
                .andExpect(jsonPath("$.data.password").doesNotExist());
    }

    @Test
    void findAllShouldReturn200() throws Exception {
        when(useCase.findAll()).thenReturn(List.of(
                new User(UUID.randomUUID(), "Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER")
        ));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data[0].names").value("Ana Maria"));
    }

    @Test
    void createShouldReturn422WhenBusinessRuleFails() throws Exception {
        UserRequest request = new UserRequest("Ana Maria", "CC12345", "ana@test.com", "Password123", "INVALID", "OWNER");
        when(useCase.create(any(User.class))).thenThrow(new BusinessRuleException("Role must be one of: ADMIN, RESIDENT, STAFF."));

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.status").value(422))
            .andExpect(jsonPath("$.data.error").value("Business Rule Violation"))
            .andExpect(jsonPath("$.data.message").value("Role must be one of: ADMIN, RESIDENT, STAFF."));
    }
}