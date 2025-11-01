package pl.grzegorz.financeapp.entity;

import jakarta.persistence.*;
import pl.grzegorz.financeapp.CategoryType;

@Entity
public class Category {
    @Id @GeneratedValue
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private CategoryType type; // INCOME, EXPENSE

    @ManyToOne
    private User user;
}
