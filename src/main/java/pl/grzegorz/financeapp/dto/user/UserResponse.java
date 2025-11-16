package pl.grzegorz.financeapp.dto.user;

import pl.grzegorz.financeapp.entity.Category;
import pl.grzegorz.financeapp.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String email,
        List<Category> categories,
        List<Transaction> transactions

) {}

