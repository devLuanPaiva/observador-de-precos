package com.luanpaiva.observador_de_precos_test.unit;

import com.luanpaiva.observador_de_precos.modules.auth.controller.AuthController;
import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CONFLICT;


@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Register Tests")
class AuthControllerRegisterTests {

    @Mock
    private AuthService authService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(authService);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("validRegistrationRequests")
    @DisplayName("Should call register service with valid DTOs")
    void testRegisterCallsServiceSuccessfully(String ignoredDisplayName, RegisterRequestDTO dto) {
        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        authController.register(dto);

        verify(authService, times(1)).register(dto);
    }

    static Stream<Arguments> validRegistrationRequests() {
        return Stream.of(
                Arguments.of("Should call register service with valid DTO", new RegisterRequestDTO(
                        "João Silva",
                        "joao@example.com",
                        "senha123456"
                )),
                Arguments.of("Should register user successfully with valid data", new RegisterRequestDTO(
                        "Maria Santos",
                        "maria@example.com",
                        "senhaSegura123"
                )),
                Arguments.of("Should accept register with minimal valid data", new RegisterRequestDTO(
                        "U",
                        "u@example.com",
                        "123456"
                ))
        );
    }

    @Test
    @DisplayName("Should throw exception when AuthService throws conflict exception")
    void testRegisterFailsWhenEmailExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Test User",
                "existing@example.com",
                "password123"
        );

        doThrow(new ResponseStatusException(CONFLICT, "Email já cadastrado"))
                .when(authService).register(any(RegisterRequestDTO.class));

        assertThatThrownBy(() -> authController.register(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email já cadastrado");
    }

    @Test
    @DisplayName("Should pass correct data to service")
    void testControllerPassesCorrectDataToService() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Service Test",
                "service@example.com",
                "password123"
        );

        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        authController.register(dto);

        verify(authService).register(
                new RegisterRequestDTO(
                        "Service Test",
                        "service@example.com",
                        "password123"
                )
        );
    }

    @Test
    @DisplayName("Should handle multiple registration requests")
    void testMultipleRegistrationRequests() {
        RegisterRequestDTO dto1 = new RegisterRequestDTO(
                "User One",
                "user1@example.com",
                "password123"
        );

        RegisterRequestDTO dto2 = new RegisterRequestDTO(
                "User Two",
                "user2@example.com",
                "password456"
        );

        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        authController.register(dto1);
        authController.register(dto2);

        verify(authService, times(2)).register(any(RegisterRequestDTO.class));
    }

    @Test
    @DisplayName("Should not catch exceptions from service")
    void testControllerDoesNotCatchServiceException() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Test",
                "test@example.com",
                "password123"
        );

        ResponseStatusException serviceException = 
                new ResponseStatusException(CONFLICT, "Email already exists");
        doThrow(serviceException).when(authService).register(any(RegisterRequestDTO.class));

        assertThatThrownBy(() -> authController.register(dto))
                .isEqualTo(serviceException);
    }

    @Test
    @DisplayName("Should accept different valid email formats")
    void testRegisterWithDifferentEmailFormats() {
        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        RegisterRequestDTO dto1 = new RegisterRequestDTO("User1", "user1@example.com", "password123");
        RegisterRequestDTO dto2 = new RegisterRequestDTO("User2", "user.name@example.com", "password123");
        RegisterRequestDTO dto3 = new RegisterRequestDTO("User3", "user+tag@example.co.uk", "password123");

        authController.register(dto1);
        authController.register(dto2);
        authController.register(dto3);

        verify(authService, times(3)).register(any(RegisterRequestDTO.class));
    }

    @Test
    @DisplayName("Should pass DTO to service without modification")
    void testControllerDoesNotModifyDTO() {
        RegisterRequestDTO originalDto = new RegisterRequestDTO(
                "Original User",
                "original@example.com",
                "password123"
        );

        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        authController.register(originalDto);

        assertThat(originalDto.name()).isEqualTo("Original User");
        assertThat(originalDto.email()).isEqualTo("original@example.com");
        assertThat(originalDto.password()).isEqualTo("password123");
    }

    @Test
    @DisplayName("Should verify service is only called once per registration")
    void testServiceCalledOncePerRegistration() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Test User",
                "test@example.com",
                "password123"
        );

        doNothing().when(authService).register(any(RegisterRequestDTO.class));

        authController.register(dto);

        verify(authService, times(1)).register(any(RegisterRequestDTO.class));
        verify(authService, times(1)).register(dto);
    }
}

