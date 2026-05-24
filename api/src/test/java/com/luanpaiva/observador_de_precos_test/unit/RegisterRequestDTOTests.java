package com.luanpaiva.observador_de_precos_test.unit;

import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegisterRequestDTO Validation Tests")
class RegisterRequestDTOTests {

    private Validator validator;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    @DisplayName("Should create valid RegisterRequestDTO with all fields")
    void testCreateValidRegisterRequestDTO() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                "senha123456"
        );

        assertThat(dto).isNotNull();
        assertThat(dto.name()).isEqualTo("João Silva");
        assertThat(dto.email()).isEqualTo("joao@example.com");
        assertThat(dto.password()).isEqualTo("senha123456");
    }

    @Test
    @DisplayName("Should validate successfully with valid data")
    void testValidationSuccessfulWithValidData() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Maria Santos",
                "maria@example.com",
                "senhaSegura123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void testValidationFailsWhenNameIsBlank() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "",
                "valid@example.com",
                "password123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")))
                .isTrue();
    }

    @Test
    @DisplayName("Should fail validation when name is null")
    void testValidationFailsWhenNameIsNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                null,
                "valid@example.com",
                "password123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name")))
                .isTrue();
    }

    @Test
    @DisplayName("Should fail validation when name exceeds 120 characters")
    void testValidationFailsWhenNameTooLong() {
        String longName = "a".repeat(121);
        RegisterRequestDTO dto = new RegisterRequestDTO(
                longName,
                "valid@example.com",
                "password123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    @DisplayName("Should fail validation when email is blank")
    void testValidationFailsWhenEmailIsBlank() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "",
                "password123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")))
                .isTrue();
    }

    @Test
    @DisplayName("Should fail validation when email is invalid format")
    void testValidationFailsWhenEmailInvalidFormat() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "not-an-email",
                "password123"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Should accept valid email formats")
    void testValidationAcceptsValidEmailFormats() {
        String[] validEmails = {
                "user@example.com",
                "user.name@example.com",
                "user+tag@example.co.uk",
                "user123@subdomain.example.com"
        };

        for (String email : validEmails) {
            RegisterRequestDTO dto = new RegisterRequestDTO(
                    "User Name",
                    email,
                    "password123"
            );

            Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

            assertThat(violations)
                    .as("Email should be valid: " + email)
                    .isEmpty();
        }
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void testValidationFailsWhenPasswordIsBlank() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                ""
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(2);
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")))
                .isTrue();
    }

    @Test
    @DisplayName("Should fail validation when password is null")
    void testValidationFailsWhenPasswordIsNull() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                null
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(1);
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")))
                .isTrue();
    }

    @Test
    @DisplayName("Should fail validation when password is less than 6 characters")
    void testValidationFailsWhenPasswordTooShort() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                "12345"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Should accept password with exactly 6 characters")
    void testValidationAcceptsPasswordWithSixCharacters() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                "123456"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail validation when password exceeds 255 characters")
    void testValidationFailsWhenPasswordTooLong() {
        String longPassword = "a".repeat(256);
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                longPassword
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations)
                .isNotEmpty()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password"));
    }

    @Test
    @DisplayName("Should fail validation when multiple fields are invalid")
    void testValidationFailsWhenMultipleFieldsInvalid() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "",
                "invalid-email",
                "short"
        );

        Set<ConstraintViolation<RegisterRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).size().isGreaterThanOrEqualTo(3);
    }

    @Test
    @DisplayName("Should access DTO fields using record accessors")
    void testDTORecordAccessors() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Test User",
                "test@example.com",
                "password123"
        );

        assertThat(dto.name()).isEqualTo("Test User");
        assertThat(dto.email()).isEqualTo("test@example.com");
        assertThat(dto.password()).isEqualTo("password123");
    }
}
