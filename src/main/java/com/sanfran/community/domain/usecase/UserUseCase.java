package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.model.vo.DomainValidators;
import com.sanfran.community.domain.usecase.port.UserRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.sanfran.community.domain.usecase.IUserUseCase;

import java.util.Locale;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class UserUseCase implements IUserUseCase {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ReservationRepository reservationRepository;

    public UserUseCase(UserRepository repository, PasswordEncoder passwordEncoder, ReservationRepository reservationRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
    }

    public User create(User user) {
        User normalizedUser = normalized(user);
        DomainValidators.validateUser(normalizedUser);
        ensureUniqueConstraints(normalizedUser, null);
        return repository.save(normalizedUser);
    }

    public User update(UUID id, User user) {
        if (id == null) {
            throw new ValidationException("id", "El ID no debe ser nulo.");
        }

        User existing = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("El usuario no se encontró en la base de datos."));

        // If password is null, keep the existing password
        User userWithPassword = new User(
                user.id(),
                user.names(),
                user.idDocument(),
                user.email(),
                user.password() == null ? existing.password() : user.password(),
                user.role(),
                user.subRole()
        );

        User normalizedUser = normalized(userWithPassword);
        DomainValidators.validateUser(normalizedUser);
        ensureUniqueConstraints(normalizedUser, id);

        return repository.save(new User(
            id,
            normalizedUser.names(),
            normalizedUser.idDocument(),
            normalizedUser.email(),
            user.password() == null ? existing.password() : normalizedUser.password(),
            normalizedUser.role(),
            normalizedUser.subRole()
        ));
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        if (!repository.existsById(id)) {
            throw new NotFoundException("The User was not found in the database.");
        }
        long active = reservationRepository.countActiveByUserId(id);
        if (active > 0) {
            throw new BusinessRuleException("No se puede eliminar el usuario porque tiene reservas activas.");
        }

        // Remove cancelled reservations referencing this user to avoid FK constraint
        reservationRepository.deleteCancelledByUserId(id);
        repository.deleteById(id);
    }

    public User findById(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The User was not found in the database."));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public List<User> findByNames(String names) {
        if (names == null || names.isBlank()) {
            throw new ValidationException("names", "Name must not be blank.");
        }
        return repository.findByNamesContainingIgnoreCase(names);
    }

    public List<User> findByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("email", "Email must not be blank.");
        }
        return repository.findByEmailIgnoreCase(email.trim().toLowerCase(Locale.ROOT))
                .map(List::of)
                .orElse(List.of());
    }

    private void ensureUniqueConstraints(User user, UUID currentId) {
        repository.findByEmailIgnoreCase(user.email())
                .filter(existing -> !Objects.equals(existing.id(), currentId))
                .ifPresent(existing -> {
                    throw new ValidationException("email", "Email already exists.");
                });

        repository.findByIdDocumentIgnoreCase(user.idDocument())
                .filter(existing -> !Objects.equals(existing.id(), currentId))
                .ifPresent(existing -> {
                    throw new ValidationException("idDocument", "ID document already exists.");
                });
    }

    private User normalized(User user) {
        if (user == null) {
            return null;
        }

        return new User(
                user.id(),
            user.names() == null ? null : user.names().trim(),
            user.idDocument() == null ? null : user.idDocument().trim(),
            user.email() == null ? null : user.email().trim().toLowerCase(Locale.ROOT),
                user.password() == null ? null : passwordEncoder.encode(user.password()),
                DomainValidators.normalizeRole(user.role()),
                DomainValidators.normalizeSubRole(user.subRole())
        );
    }
}
