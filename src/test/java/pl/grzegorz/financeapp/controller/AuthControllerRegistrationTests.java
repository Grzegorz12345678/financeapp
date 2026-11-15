package pl.grzegorz.financeapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.grzegorz.financeapp.entity.User;
import pl.grzegorz.financeapp.repository.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerRegistrationTests {

    private static final String TEST_JWT_SECRET = createTestSecret();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @DynamicPropertySource
    static void configureJwtSecret(DynamicPropertyRegistry registry) {
        registry.add("jwt.secret", () -> TEST_JWT_SECRET);
    }

    @Test
    void registersUserWithEncodedPasswordAndIgnoresSubmittedRole() throws Exception {
        String username = uniqueValue("register-user");
        String email = uniqueValue("register") + "@example.com";
        String rawPassword = "A-test-password-123";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson(username, email, rawPassword, "ADMIN")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist())
                .andExpect(jsonPath("$.role").doesNotExist());

        User saved = userRepository.findByUsername(username).orElseThrow();
        assertEquals("USER", saved.getRole());
        assertNotEquals(rawPassword, saved.getPassword());
        assertTrue(passwordEncoder.matches(rawPassword, saved.getPassword()));
    }

    @Test
    void returnsConflictWhenUsernameIsAlreadyTaken() throws Exception {
        String username = uniqueValue("duplicate-user");
        saveUser(username, uniqueValue("existing") + "@example.com");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson(username, uniqueValue("new") + "@example.com", "password")))
                .andExpect(status().isConflict())
                .andExpect(content().string(not(containsString("uk_users_"))))
                .andExpect(content().string(not(containsString("constraint"))));
    }

    @Test
    void returnsConflictWhenEmailIsAlreadyTaken() throws Exception {
        String email = uniqueValue("existing") + "@example.com";
        saveUser(uniqueValue("existing-user"), email);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson(uniqueValue("new-user"), email, "password")))
                .andExpect(status().isConflict());
    }

    @Test
    void rejectsInvalidRegistrationInput() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationJson("", "not-an-email", "")))
                .andExpect(status().isBadRequest());
    }

    private String registrationJson(String username, String email, String password) throws Exception {
        return registrationJson(username, email, password, null);
    }

    private String registrationJson(String username, String email, String password, String role) throws Exception {
        Map<String, String> body = role == null
                ? Map.of("username", username, "email", email, "password", password)
                : Map.of("username", username, "email", email, "password", password, "role", role);
        return objectMapper.writeValueAsString(body);
    }

    private void saveUser(String username, String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode("existing-password"));
        user.setRole("USER");
        userRepository.saveAndFlush(user);
    }

    private String uniqueValue(String prefix) {
        return prefix + "-" + UUID.randomUUID();
    }

    private static String createTestSecret() {
        byte[] key = new byte[32];
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
