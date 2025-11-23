package pl.grzegorz.financeapp.dto.user;

import pl.grzegorz.financeapp.dto.category.CategorySimpleResponse;
import pl.grzegorz.financeapp.dto.transaction.TransactionSimpleResponse;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        List<CategorySimpleResponse> categories,
        List<TransactionSimpleResponse> transactions
) {}


