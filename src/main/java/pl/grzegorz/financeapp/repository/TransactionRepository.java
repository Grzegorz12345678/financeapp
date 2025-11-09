package pl.grzegorz.financeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.grzegorz.financeapp.entity.Transaction;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.entity.Category;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUser(User user);
    List<Transaction> findByCategory(Category category);
    List<Transaction> findByUserIdAndDateBetween(Long userId, LocalDate start, LocalDate end);
}
