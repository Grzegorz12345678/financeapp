package pl.grzegorz.financeapp.dto.transaction;

import pl.grzegorz.financeapp.dto.category.CategorySimpleResponse;
import pl.grzegorz.financeapp.dto.user.UserSimpleResponse;

import java.math.BigDecimal;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        String description,
        String date,
        CategorySimpleResponse category,
        UserSimpleResponse user
) {}

