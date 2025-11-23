package pl.grzegorz.financeapp.service;

import org.springframework.stereotype.Service;
import pl.grzegorz.financeapp.CategoryType;
import pl.grzegorz.financeapp.dto.category.CategoryRequest;
import pl.grzegorz.financeapp.dto.category.CategoryResponse;
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

    public CategoryResponse createCategory(CategoryRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();
        category.setName(request.name());
        category.setType(CategoryType.valueOf(request.type()));
        category.setUser(user);

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(
                saved.getId(),
                saved.getName(),
                saved.getType().name(),
                saved.getUser().getId()
        );
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getType().name(),
                        c.getUser().getId()
                ))
                .toList();
    }

    public Optional<CategoryResponse> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(c -> new CategoryResponse(
                        c.getId(),
                        c.getName(),
                        c.getType().name(),
                        c.getUser().getId()
                ));
    }
}



