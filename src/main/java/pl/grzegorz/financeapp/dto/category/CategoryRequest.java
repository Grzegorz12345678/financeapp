package pl.grzegorz.financeapp.dto.category;

public record CategoryRequest(
        String name,
        String type,
        Long userId
) {}

