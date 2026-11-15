package pl.grzegorz.financeapp.service;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import pl.grzegorz.financeapp.dto.user.RegisterRequest;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserServiceRegistrationTests {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, passwordEncoder);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
    }

    @ParameterizedTest
    @ValueSource(strings = {"uk_users_username", "UK_USERS_EMAIL"})
    void mapsOnlyKnownRegistrationUniqueConstraintsToConflict(String constraintName) {
        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(dataIntegrityViolation(constraintName));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.register(new RegisterRequest("new-user", "new@example.com", "password")));

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("Username or email is already in use", exception.getReason());
        assertFalse(exception.getReason().contains("constraint"));
    }

    @Test
    void doesNotMapOtherIntegrityViolationsToConflictOrExposeDatabaseDetails() {
        when(userRepository.saveAndFlush(any(User.class)))
                .thenThrow(dataIntegrityViolation("fk_transactions_user_id"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.register(new RegisterRequest("new-user", "new@example.com", "password")));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        assertEquals("Registration could not be completed", exception.getReason());
        assertFalse(exception.getReason().contains("fk_transactions_user_id"));
    }

    private DataIntegrityViolationException dataIntegrityViolation(String constraintName) {
        ConstraintViolationException cause = new ConstraintViolationException(
                "database-specific detail", new SQLException("database-specific detail"),
                "insert into users", constraintName);
        return new DataIntegrityViolationException("database-specific detail", cause);
    }
}
