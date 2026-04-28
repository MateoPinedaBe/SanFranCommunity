package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.usecase.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserUseCaseTest {

    @Mock
    private UserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new UserUseCase(repository, passwordEncoder);
    }

    @Test
    void createShouldNormalizeAndPersistUser() {
        User input = new User(null, " Ana Maria ", " CC12345 ", " ANA@TEST.COM ", "Password123", "resident", "owner");
        User saved = new User(UUID.randomUUID(), "Ana Maria", "CC12345", "ana@test.com", "encoded-password", "RESIDENT", "OWNER");

        when(repository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.empty());
        when(repository.findByIdDocumentIgnoreCase("CC12345")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenReturn(saved);

        User result = useCase.create(input);

        assertThat(result.email()).isEqualTo("ana@test.com");
        assertThat(result.role()).isEqualTo("RESIDENT");
        verify(repository).save(new User(null, "Ana Maria", "CC12345", "ana@test.com", "encoded-password", "RESIDENT", "OWNER"));
    }

    @Test
    void createShouldRejectDuplicateEmail() {
        User input = new User(null, "Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");
        User existing = new User(UUID.randomUUID(), "Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");

        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");
        when(repository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> useCase.create(input))
                .isInstanceOf(ValidationException.class)
                .hasMessage("Email already exists.");
    }

    @Test
    void updateShouldThrowWhenUserDoesNotExist() {
        UUID id = UUID.randomUUID();
        User input = new User(null, "Ana Maria", "CC12345", "ana@test.com", "Password123", "ADMIN", "OWNER");

        when(passwordEncoder.encode("Password123")).thenReturn("encoded-password");
        when(repository.findByEmailIgnoreCase("ana@test.com")).thenReturn(Optional.empty());
        when(repository.findByIdDocumentIgnoreCase("CC12345")).thenReturn(Optional.empty());
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.update(id, input))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The User was not found in the database.");
    }
}