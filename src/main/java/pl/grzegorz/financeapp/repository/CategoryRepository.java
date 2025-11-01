package pl.grzegorz.financeapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.grzegorz.financeapp.entity.Category;
import pl.grzegorz.financeapp.entity.User;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
}
