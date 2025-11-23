package pl.grzegorz.financeapp.service;

import org.springframework.stereotype.Service;
import pl.grzegorz.financeapp.dto.user.CategorySimpleResponse;
import pl.grzegorz.financeapp.dto.user.TransactionSimpleResponse;
import pl.grzegorz.financeapp.dto.user.UserRequest;
import pl.grzegorz.financeapp.dto.user.UserResponse;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserResponse createUser(UserRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setRole("USER");

        User saved = userRepository.save(user);
        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getCategories().stream()
                        .map(c -> new CategorySimpleResponse(
                                    c.getId(),
                                    c.getName(),
                                    c.getType().name())).toList(),

                saved.getTransactions().stream()
                        .map(t -> new TransactionSimpleResponse(
                                t.getId(),
                                t.getAmount(),
                                t.getDescription(),
                                t.getDate().toString()
                        )).toList());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserResponse(
                        u.getId(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getCategories().stream()
                                .map(c -> new CategorySimpleResponse(
                                        c.getId(),
                                        c.getName(),
                                        c.getType().name()
                                ))
                                .toList(),
                        u.getTransactions().stream()
                                .map(t -> new TransactionSimpleResponse(
                                        t.getId(),
                                        t.getAmount(),
                                        t.getDescription(),
                                        t.getDate().toString()
                                ))
                                .toList()
                ))
                .toList();
    }


//    public Optional<UserResponse> getUser(Long id) {
//        return userRepository.findById(id)
//                .map(u -> new UserResponse(u.getId(), u.getUsername(), u.getEmail(), u.getCategories(),u.getTransactions()));
//    }
}
