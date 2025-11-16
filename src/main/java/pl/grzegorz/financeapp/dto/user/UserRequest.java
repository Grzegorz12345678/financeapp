package pl.grzegorz.financeapp.dto.user;

public record UserRequest(
        String username,
        String email,
        String password
) {}

