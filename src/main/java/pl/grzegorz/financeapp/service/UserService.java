package pl.grzegorz.financeapp.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.grzegorz.financeapp.dto.category.CategorySimpleResponse;
import pl.grzegorz.financeapp.dto.transaction.TransactionSimpleResponse;
import pl.grzegorz.financeapp.dto.user.RegisterRequest;
import pl.grzegorz.financeapp.dto.user.UserRequest;
import pl.grzegorz.financeapp.dto.user.UserResponse;
import pl.grzegorz.financeapp.dto.user.UserSimpleResponse;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponse createUser(UserRequest request) {
        return toResponse(saveUser(request.username(), request.email(), request.password()));
    }

    public UserSimpleResponse register(RegisterRequest request) {
        User saved = saveUser(request.username(), request.email(), request.password());
        return new UserSimpleResponse(saved.getId(), saved.getUsername(), saved.getEmail());
    }

    private User saveUser(String username, String email, String rawPassword) {
        boolean usernameTaken = userRepository.findByUsername(username).isPresent();
        boolean emailTaken = userRepository.findByEmail(email).isPresent();
        if (usernameTaken || emailTaken) {
            throw registrationConflict();
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER");

        try {
            return userRepository.saveAndFlush(user);
        }
        catch (DataIntegrityViolationException exception) {
            if (isRegistrationUniqueConstraintViolation(exception)) {
                throw registrationConflict();
            }
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Registration could not be completed", exception);
        }
    }

    private boolean isRegistrationUniqueConstraintViolation(DataIntegrityViolationException exception) {
        for (Throwable cause = exception; cause != null && cause.getCause() != cause; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException constraintViolation) {
                String constraintName = constraintViolation.getConstraintName();
                if ("uk_users_username".equalsIgnoreCase(constraintName)
                        || "uk_users_email".equalsIgnoreCase(constraintName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private ResponseStatusException registrationConflict() {
        return new ResponseStatusException(HttpStatus.CONFLICT,
                "Username or email is already in use");
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<UserResponse> getUser(Long id) {
        return userRepository.findById(id)
                .map(this::toResponse);
    }

    private UserResponse toResponse(User u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getCategories().stream()
                        .map(c -> new CategorySimpleResponse(
                                c.getId(),
                                c.getName(),
                                c.getType().name()))
                        .toList(),
                u.getTransactions().stream()
                        .map(t -> new TransactionSimpleResponse(
                                t.getId(),
                                t.getAmount(),
                                t.getDescription(),
                                t.getDate().toString()
                        ))
                        .toList()
        );
    }
}
