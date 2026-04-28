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
    private static final Pattern HTTP_URL_PATTERN = Pattern.compile("https?://.+");

    private static final Set<String> VALID_ROLES = Set.of("ADMIN", "RESIDENT", "STAFF");
    private static final Set<String> VALID_SUB_ROLES = Set.of("OWNER", "TENANT", "GUEST");

    private DomainValidators() {
    }

    public static void validateUser(User user) {
        if (user == null) {
            throw new ValidationException("user", "User data must not be null.");
        }

        if (user.names() == null || user.names().isBlank()) {
            throw new ValidationException("names", "Name is required.");
        }
        if (user.names().length() < 2 || user.names().length() > 100) {
            throw new ValidationException("names", "Name must be between 2 and 100 characters.");
        }

        if (user.idDocument() == null || user.idDocument().isBlank()) {
            throw new ValidationException("idDocument", "ID document is required.");
        }
        if (!DOCUMENT_PATTERN.matcher(user.idDocument()).matches()) {
            throw new ValidationException("idDocument", "ID document must be alphanumeric between 5 and 20 characters.");
        }

        if (user.email() == null || user.email().isBlank()) {
            throw new ValidationException("email", "Email is required.");
        }
        if (!EMAIL_PATTERN.matcher(user.email()).matches()) {
            throw new ValidationException("email", "Email format is invalid.");
        }

        if (user.password() == null || user.password().isBlank()) {
            throw new ValidationException("password", "Password is required.");
        }
        if (user.password().length() < 8) {
            throw new ValidationException("password", "Password must be at least 8 characters long.");
        }

        if (user.role() == null || user.role().isBlank()) {
            throw new ValidationException("role", "Role is required.");
        }
        if (!VALID_ROLES.contains(normalizeKey(user.role()))) {
            throw new BusinessRuleException("Role must be one of: ADMIN, RESIDENT, STAFF.");
        }

        if (user.subRole() == null || user.subRole().isBlank()) {
            throw new ValidationException("subRole", "Sub-role is required.");
        }
        if (!VALID_SUB_ROLES.contains(normalizeKey(user.subRole()))) {
            throw new BusinessRuleException("Sub-role must be one of: OWNER, TENANT, GUEST.");
        }
    }

    public static void validateFacility(Facility facility) {
        if (facility == null) {
            throw new ValidationException("facility", "Facility data must not be null.");
        }

        if (facility.name() == null || facility.name().isBlank()) {
            throw new ValidationException("name", "Name is required.");
        }
        if (facility.name().length() < 2 || facility.name().length() > 100) {
            throw new ValidationException("name", "Name must be between 2 and 100 characters.");
        }

        if (facility.description() == null || facility.description().isBlank()) {
            throw new ValidationException("description", "Description is required.");
        }
        if (facility.description().length() > 500) {
            throw new ValidationException("description", "Description must not exceed 500 characters.");
        }

        if (facility.imageUrl() == null || facility.imageUrl().isBlank()) {
            throw new ValidationException("imageUrl", "Image URL is required.");
        }
        if (!HTTP_URL_PATTERN.matcher(facility.imageUrl()).matches()) {
            throw new ValidationException("imageUrl", "Image URL must start with http:// or https://.");
        }

        if (facility.capacity() == null) {
            throw new ValidationException("capacity", "Capacity is required.");
        }
        if (facility.capacity() < 1 || facility.capacity() > 10000) {
            throw new BusinessRuleException("Capacity must be between 1 and 10000.");
        }
    }

    public static void validateReservation(Reservation reservation) {
        if (reservation == null) {
            throw new ValidationException("reservation", "Reservation data must not be null.");
        }

        if (reservation.userId() == null) {
            throw new ValidationException("userId", "User is required.");
        }

        if (reservation.facilityId() == null) {
            throw new ValidationException("facilityId", "Facility is required.");
        }

        if (reservation.date() == null) {
            throw new ValidationException("date", "Date is required.");
        }
        if (reservation.date().isBefore(LocalDate.now())) {
            throw new BusinessRuleException("Date must not be in the past.");
        }

        if (reservation.startTime() == null) {
            throw new ValidationException("startTime", "Start time is required.");
        }
        if (reservation.endTime() == null) {
            throw new ValidationException("endTime", "End time is required.");
        }
        if (!reservation.endTime().isAfter(reservation.startTime())) {
            throw new BusinessRuleException("End time must be after start time.");
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
