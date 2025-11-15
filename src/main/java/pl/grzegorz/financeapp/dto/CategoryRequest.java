package pl.grzegorz.financeapp.dto;

public record CategoryRequest(
        String name,
        String type,
        Long userId
) {}

