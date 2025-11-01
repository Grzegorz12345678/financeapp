package pl.grzegorz.financeapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id @GeneratedValue
    private Long id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;

    @ManyToOne
    private Category category;
    @ManyToOne
    private User user;
}

