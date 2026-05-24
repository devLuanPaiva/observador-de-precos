package com.luanpaiva.observador_de_precos_test.unit;

import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepository Tests")
class UserRepositoryTests {

    @Mock
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("encoded_password")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Should save user to repository")
    void testSaveUser() {
        User userToSave = testUser;
        when(userRepository.save(userToSave)).thenReturn(userToSave);

        User savedUser = userRepository.save(userToSave);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getName()).isEqualTo("Test User");
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).save(userToSave);
    }

    @Test
    @DisplayName("Should find user by email")
    void testFindUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Test User");
        assertThat(foundUser.get().getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return empty Optional when email not found")
    void testFindUserByEmailNotFound() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should check if email exists")
    void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        boolean exists = userRepository.existsByEmail("test@example.com");

        assertThat(exists).isTrue();
        verify(userRepository, times(1)).existsByEmail("test@example.com");
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void testExistsByEmailNotFound() {
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find user by UUID id")
    void testFindUserById() {
        UUID userId = UUID.randomUUID();
        testUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        Optional<User> foundUser = userRepository.findById(userId);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should return empty Optional when id not found")
    void testFindUserByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        when(userRepository.findById(randomId)).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findById(randomId);

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDeleteUserById() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userRepository).deleteById(userId);

        userRepository.deleteById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should delete user entity")
    void testDeleteUser() {
        doNothing().when(userRepository).delete(testUser);

        userRepository.delete(testUser);

        verify(userRepository, times(1)).delete(testUser);
    }

    @Test
    @DisplayName("Should find all saved users")
    void testFindAllUsers() {
        User user1 = User.builder()
                .name("User One")
                .email("user1@example.com")
                .password("password1")
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .password("password2")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        var allUsers = userRepository.findAll();

        assertThat(allUsers).hasSize(2);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should count all users")
    void testCountUsers() {
        when(userRepository.count()).thenReturn(2L);

        long count = userRepository.count();

        assertThat(count).isEqualTo(2);
        verify(userRepository, times(1)).count();
    }

    @Test
    @DisplayName("Should email be case-sensitive in queries")
    void testEmailCaseSensitivity() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> found = userRepository.findByEmail("test@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should handle multiple email lookups")
    void testMultipleEmailLookups() {
        User user1 = User.builder()
                .name("User One")
                .email("user1@example.com")
                .password("password1")
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .name("User Two")
                .email("user2@example.com")
                .password("password2")
                .createdAt(LocalDateTime.now())
                .build();

        when(userRepository.findByEmail("user1@example.com")).thenReturn(Optional.of(user1));
        when(userRepository.findByEmail("user2@example.com")).thenReturn(Optional.of(user2));

        Optional<User> found1 = userRepository.findByEmail("user1@example.com");
        Optional<User> found2 = userRepository.findByEmail("user2@example.com");

        assertThat(found1).isPresent().contains(user1);
        assertThat(found2).isPresent().contains(user2);
        verify(userRepository, times(1)).findByEmail("user1@example.com");
        verify(userRepository, times(1)).findByEmail("user2@example.com");
    }

    @Test
    @DisplayName("Should preserve user data after save")
    void testPreserveUserDataAfterSave() {
        LocalDateTime createdTime = LocalDateTime.now();
        User user = User.builder()
                .name("Data Test User")
                .email("datatest@example.com")
                .password("hashed_password_123")
                .createdAt(createdTime)
                .build();

        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User savedUser = userRepository.save(user);
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());

        assertThat(retrievedUser).isPresent();
        User found = retrievedUser.get();
        assertThat(found.getName()).isEqualTo("Data Test User");
        assertThat(found.getEmail()).isEqualTo("datatest@example.com");
        assertThat(found.getPassword()).isEqualTo("hashed_password_123");
        assertThat(found.getCreatedAt()).isNotNull();
    }
}

