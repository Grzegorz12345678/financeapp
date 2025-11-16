package pl.grzegorz.financeapp.dto.user;

import java.math.BigDecimal;

public record TransactionSimpleResponse(
        Long id,
        BigDecimal amount,
        String description,
        String date
) {}

