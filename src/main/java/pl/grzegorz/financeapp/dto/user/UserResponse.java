package pl.grzegorz.financeapp.dto.user;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        List<CategorySimpleResponse> categories,
        List<TransactionSimpleResponse> transactions
) {}


