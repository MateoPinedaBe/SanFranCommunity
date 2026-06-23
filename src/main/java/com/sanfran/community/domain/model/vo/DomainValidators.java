package com.sanfran.community.domain.model.vo;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.model.entity.Reservation;
import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.ValidationException;

import java.util.Locale;
import java.time.LocalDate;
import java.util.Set;
import java.util.regex.Pattern;

public final class DomainValidators {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern DOCUMENT_PATTERN = Pattern.compile("[A-Za-z0-9]{5,20}");
    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("(https?://.+|data:image/.+;base64,.+)");

    private static final Set<String> VALID_ROLES = Set.of("ADMIN", "RESIDENT", "STAFF");
    private static final Set<String> VALID_SUB_ROLES = Set.of("OWNER", "TENANT", "GUEST");

    private DomainValidators() {
    }

    public static void validateUser(User user) {
        if (user == null) {
            throw new ValidationException("user", "Los datos del usuario no deben ser nulos.");
        }

        if (user.names() == null || user.names().isBlank()) {
            throw new ValidationException("names", "El nombre es obligatorio.");
        }
        if (user.names().length() < 2 || user.names().length() > 100) {
            throw new ValidationException("names", "El nombre debe tener entre 2 y 100 caracteres.");
        }

        if (user.idDocument() == null || user.idDocument().isBlank()) {
            throw new ValidationException("idDocument", "El documento de identificación es obligatorio.");
        }
        if (!DOCUMENT_PATTERN.matcher(user.idDocument()).matches()) {
            throw new ValidationException("idDocument", "El documento debe ser alfanumérico y tener entre 5 y 20 caracteres.");
        }

        if (user.email() == null || user.email().isBlank()) {
            throw new ValidationException("email", "El correo electrónico es obligatorio.");
        }
        if (!EMAIL_PATTERN.matcher(user.email()).matches()) {
            throw new ValidationException("email", "El formato del correo electrónico es inválido.");
        }

        if (user.password() == null || user.password().isBlank()) {
            throw new ValidationException("password", "La contraseña es obligatoria.");
        }
        if (user.password().length() < 8) {
            throw new ValidationException("password", "La contraseña debe tener al menos 8 caracteres.");
        }

        if (user.role() == null || user.role().isBlank()) {
            throw new ValidationException("role", "El rol es obligatorio.");
        }
        if (!VALID_ROLES.contains(normalizeKey(user.role()))) {
            throw new BusinessRuleException("El rol debe ser uno de: ADMIN, RESIDENT, STAFF.");
        }

        if (user.subRole() == null || user.subRole().isBlank()) {
            throw new ValidationException("subRole", "El sub-rol es obligatorio.");
        }
        if (!VALID_SUB_ROLES.contains(normalizeKey(user.subRole()))) {
            throw new BusinessRuleException("El sub-rol debe ser uno de: OWNER, TENANT, GUEST.");
        }
    }

    public static void validateFacility(Facility facility) {
        if (facility == null) {
            throw new ValidationException("facility", "Los datos de la instalación no deben ser nulos.");
        }

        if (facility.name() == null || facility.name().isBlank()) {
            throw new ValidationException("name", "El nombre es obligatorio.");
        }
        if (facility.name().length() < 2 || facility.name().length() > 100) {
            throw new ValidationException("name", "El nombre debe tener entre 2 y 100 caracteres.");
        }

        if (facility.description() == null || facility.description().isBlank()) {
            throw new ValidationException("description", "La descripción es obligatoria.");
        }
        if (facility.description().length() > 500) {
            throw new ValidationException("description", "La descripción no debe exceder los 500 caracteres.");
        }

        if (facility.imageUrl() == null || facility.imageUrl().isBlank()) {
            throw new ValidationException("imageUrl", "La URL de la imagen es obligatoria.");
        }
        if (!HTTP_URL_PATTERN.matcher(facility.imageUrl()).matches()) {
            throw new ValidationException("imageUrl", "La URL de la imagen debe comenzar con http://, https:// o ser una URL válida de data:image;base64.");
        }

        if (facility.capacity() == null) {
            throw new ValidationException("capacity", "La capacidad es obligatoria.");
        }
        if (facility.capacity() < 1 || facility.capacity() > 10000) {
            throw new BusinessRuleException("La capacidad debe estar entre 1 y 10000.");
        }
    }

    public static void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new ValidationException("reservation", "Los datos de la reserva no deben ser nulos.");
        }

        if (reservation.userId() == null) {
            throw new ValidationException("userId", "El usuario es obligatorio.");
        }

        if (reservation.facilityId() == null) {
            throw new ValidationException("facilityId", "La instalación es obligatoria.");
        }

        if (reservation.date() == null) {
            throw new ValidationException("date", "La fecha es obligatoria.");
        }
        if (reservation.date().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("La fecha no puede ser anterior a hoy.");
        }

        if (reservation.startTime() == null) {
            throw new ValidationException("startTime", "La hora de inicio es obligatoria.");
        }
        if (reservation.endTime() == null) {
            throw new ValidationException("endTime", "La hora de fin es obligatoria.");
        }
        if (!reservation.endTime().isAfter(reservation.startTime())) {
            throw new BusinessRuleException("La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (reservation.status() == null) {
            throw new ValidationException("status", "El estado de la reserva es obligatorio.");
        }
    }

    public static String normalizeRole(String value) {
        return normalizeKey(value);
    }

    public static String normalizeSubRole(String value) {
        return normalizeKey(value);
    }

    private static String normalizeKey(String value) {
        return value == null ? null : value.trim().toUpperCase(Locale.ROOT);
    }
}
