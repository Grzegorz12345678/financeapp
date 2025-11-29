package pl.grzegorz.financeapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.grzegorz.financeapp.dto.transaction.TransactionRequest;
import pl.grzegorz.financeapp.dto.transaction.TransactionResponse;
import pl.grzegorz.financeapp.entity.Transaction;
import pl.grzegorz.financeapp.service.TransactionService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping
    public TransactionResponse create(@RequestBody TransactionRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<TransactionResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

