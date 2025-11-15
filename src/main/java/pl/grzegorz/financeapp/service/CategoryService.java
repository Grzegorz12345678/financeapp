package pl.grzegorz.financeapp.service;

import org.springframework.stereotype.Service;
import pl.grzegorz.financeapp.CategoryType;
import pl.grzegorz.financeapp.dto.CategoryRequest;
import pl.grzegorz.financeapp.entity.Category;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.CategoryRepository;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(CategoryRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();
        category.setName(request.name());
        category.setType(CategoryType.valueOf(request.type()));
        category.setUser(user);

        return categoryRepository.save(category);
    }

    public Optional<Category> updateCategory(Long id, CategoryRequest request) {
        return categoryRepository.findById(id).map(category -> {
            category.setName(request.name());
            category.setType(CategoryType.valueOf(request.type()));
            User user = userRepository.findById(request.userId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            category.setUser(user);
            return categoryRepository.save(category);
        });
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}


