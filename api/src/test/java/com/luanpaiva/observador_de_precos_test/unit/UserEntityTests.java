package com.luanpaiva.observador_de_precos_test.unit;

import com.luanpaiva.observador_de_precos.modules.users.entity.User;
import com.luanpaiva.observador_de_precos.modules.users.enums.UserRole;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Entity Tests")
class UserEntityTests {

    @Test
    @DisplayName("Should create a user with all fields using builder")
    void testCreateUserWithBuilder() {
        UUID userId = UUID.randomUUID();
        String name = "João Silva";
        UserRole role = UserRole.USER;
        String email = "joao@example.com";
        String password = "hashed_password_123";
        LocalDateTime createdAt = LocalDateTime.now();

        User user = User.builder()
                .id(userId)
                .name(name)
                .email(email)
                .role(role)
                .password(password)
                .createdAt(createdAt)
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getCreatedAt()).isEqualTo(createdAt);
        assertThat(user.getRole()).isEqualTo(role);
    }

    @Test
    @DisplayName("Should create a user with no-args constructor")
    void testCreateUserWithNoArgsConstructor() {
        User user = new User();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isNull();
        assertThat(user.getEmail()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getRole()).isNull();
    }

    @Test
    @DisplayName("Should get username from email using UserDetails interface")
    void testGetUsernameReturnsEmail() {
        String email = "maria@example.com";
        User user = User.builder()
                .name("Maria")
                .email(email)
                .role(UserRole.USER)
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();

        String username = user.getUsername();

        assertThat(username).isEqualTo(email);
    }

    @Test
    @DisplayName("Should return ROLE_USER authority")
    void testGetAuthoritiesReturnsRoleAuthority() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password123")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        var authorities = user.getAuthorities();

        assertThat(authorities).hasSize(1);

        assertThat(authorities.iterator().next().getAuthority())
                .isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Should set user properties using setters")
    void testSetUserProperties() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        String name = "Carlos";
        String email = "carlos@example.com";
        String password = "encoded_password";
        UserRole role = UserRole.ADMIN;
        LocalDateTime now = LocalDateTime.now();

        user.setId(userId);
        user.setName(name);
        user.setEmail(email);
        user.setRole(role);
        user.setPassword(password);
        user.setCreatedAt(now);

        assertThat(user.getId()).isEqualTo(userId);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getCreatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should be able to update user email")
    void testUpdateUserEmail() {
        User user = User.builder()
                .name("Ana")
                .email("ana.old@example.com")
                .password("password123")
                .createdAt(LocalDateTime.now())
                .build();

        user.setEmail("ana.new@example.com");

        assertThat(user.getEmail()).isEqualTo("ana.new@example.com");
    }

    @Test
    @DisplayName("Should compare two users with same id")
    void testUserEqualityBasedOnId() {
        UUID userId = UUID.randomUUID();
        User user1 = User.builder()
                .id(userId)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(userId)
                .name("User2")
                .email("user2@example.com")
                .password("password2")
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(user1.getId()).isEqualTo(user2.getId());
    }
}
