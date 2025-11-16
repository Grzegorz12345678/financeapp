package pl.grzegorz.financeapp.dto.category;

public record CategoryResponse(
        Long id,
        String name,
        String type,
        Long userId
) {}

