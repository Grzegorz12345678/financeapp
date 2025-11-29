package pl.grzegorz.financeapp.service;

import org.springframework.stereotype.Service;
import pl.grzegorz.financeapp.dto.category.CategorySimpleResponse;
import pl.grzegorz.financeapp.dto.transaction.TransactionRequest;
import pl.grzegorz.financeapp.dto.transaction.TransactionResponse;
import pl.grzegorz.financeapp.dto.user.UserSimpleResponse;
import pl.grzegorz.financeapp.entity.Category;
import pl.grzegorz.financeapp.entity.Transaction;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.CategoryRepository;
import pl.grzegorz.financeapp.repository.TransactionRepository;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public TransactionResponse create(TransactionRequest req) {

        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository.findById(req.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Transaction t = new Transaction();
        t.setAmount(req.amount());
        t.setDescription(req.description());
        t.setDate(LocalDate.parse(req.date()));
        t.setUser(user);
        t.setCategory(category);

        Transaction saved = transactionRepository.save(t);
        return toResponse(saved);
    }

    public List<TransactionResponse> getAll() {
        return transactionRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<TransactionResponse> getById(Long id) {
        return transactionRepository.findById(id)
                .map(this::toResponse);
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmount(),
                t.getDescription(),
                t.getDate().toString(),
                new CategorySimpleResponse(
                        t.getCategory().getId(),
                        t.getCategory().getName(),
                        t.getCategory().getType().name()
                ),
                new UserSimpleResponse(
                        t.getUser().getId(),
                        t.getUser().getUsername(),
                        t.getUser().getEmail()
                )
        );
    }
}


