package pl.grzegorz.financeapp.dto.user;

public record UserSimpleResponse(
        Long id,
        String username,
        String email
) {}

