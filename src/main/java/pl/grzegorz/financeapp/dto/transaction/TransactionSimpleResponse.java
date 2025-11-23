package pl.grzegorz.financeapp.dto.transaction;

import java.math.BigDecimal;

public record TransactionSimpleResponse(
        Long id,
        BigDecimal amount,
        String description,
        String date
) {}

