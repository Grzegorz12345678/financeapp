package pl.grzegorz.financeapp.dto.transaction;

import java.math.BigDecimal;

public record TransactionRequest(
        BigDecimal amount,
        String description,
        String date,
        Long categoryId,
        Long userId
) {}

