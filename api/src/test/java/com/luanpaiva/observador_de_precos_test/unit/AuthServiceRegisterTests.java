package com.luanpaiva.observador_de_precos_test.unit;

import com.luanpaiva.observador_de_precos.modules.auth.dto.RegisterRequestDTO;
import com.luanpaiva.observador_de_precos.modules.auth.service.AuthService;
import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.repository.UserRepository;
import com.luanpaiva.observador_de_precos.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("AuthService Register Tests")
@ExtendWith(MockitoExtension.class)
class AuthServiceRegisterTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(userRepository, passwordEncoder, jwtService);
    }

    @Test
    @DisplayName("Should register a user successfully with valid data")
    void testRegisterUserSuccessfully() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "joao@example.com",
                "senha123456");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(dto.password())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto);

        verify(userRepository, times(1)).existsByEmail("joao@example.com");
        verify(passwordEncoder, times(1)).encode("senha123456");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void testRegisterFailsWhenEmailExists() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "João Silva",
                "existing@example.com",
                "senha123456");

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(dto))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Email já cadastrado");

        verify(userRepository, times(1)).existsByEmail("existing@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should encode password before saving user")
    void testPasswordEncodingBeforeSave() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Maria Santos",
                "maria@example.com",
                "mySecurePassword");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode("mySecurePassword")).thenReturn("$2a$10$encoded_hash");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("$2a$10$encoded_hash");
        assertThat(savedUser.getPassword()).isNotEqualTo("mySecurePassword");
    }

    @Test
    @DisplayName("Should save user with correct name and email")
    void testUserSavedWithCorrectNameAndEmail() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Carlos Eduardo",
                "carlos@example.com",
                "password123");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getName()).isEqualTo("Carlos Eduardo");
        assertThat(savedUser.getEmail()).isEqualTo("carlos@example.com");
    }

    @Test
    @DisplayName("Should not interact with JwtService during registration")
    void testJwtServiceNotUsedDuringRegistration() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "Test User",
                "test@example.com",
                "password123");

        when(userRepository.existsByEmail(dto.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto);

        verify(jwtService, never()).generateAccessToken(any(), any(), any(), any());
        verify(jwtService, never()).generateRefreshToken(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Should handle case-sensitive email checks")
    void testEmailCheckIsCaseSensitive() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "User",
                "test@example.com",
                "password123");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto);

        verify(userRepository).existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should throw conflict exception with correct HTTP status")
    void testExceptionHasCorrectHttpStatus() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "User",
                "duplicate@example.com",
                "password123");

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        try {
            authService.register(dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode().value()).isEqualTo(409);
        }
    }

    @Test
    @DisplayName("Should verify email existence before processing registration")
    void testEmailExistenceCheckIsFirstOperation() {
        RegisterRequestDTO dto = new RegisterRequestDTO(
                "User",
                "test@example.com",
                "password123");

        when(userRepository.existsByEmail(dto.email())).thenReturn(true);

        try {
            authService.register(dto);
        } catch (ResponseStatusException e) {
            assertThat(e.getStatusCode().value()).isEqualTo(409);
        }

        verify(userRepository).existsByEmail("test@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should register multiple users with different emails")
    void testRegisterMultipleUsersWithDifferentEmails() {
        RegisterRequestDTO dto1 = new RegisterRequestDTO(
                "User One",
                "user1@example.com",
                "password123");
        RegisterRequestDTO dto2 = new RegisterRequestDTO(
                "User Two",
                "user2@example.com",
                "password456");

        when(userRepository.existsByEmail(dto1.email())).thenReturn(false);
        when(userRepository.existsByEmail(dto2.email())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        authService.register(dto1);
        authService.register(dto2);

        verify(userRepository, times(2)).save(any(User.class));
        verify(userRepository).existsByEmail("user1@example.com");
        verify(userRepository).existsByEmail("user2@example.com");
    }
}
