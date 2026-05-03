package com.tournasys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.exception.AuthenticationException;
import com.tournasys.model.User;
import com.tournasys.service.AuthenticationService;

public class AuthenticationServiceTest {

    @Test
    void registerAndLogin_shouldReturnUser() {
        DatabaseConnection.initializeDatabase();

        AuthenticationService authService = new AuthenticationService();

        String username = "testuser_" + System.currentTimeMillis();

        authService.register(username, "1234", "manager");

        User user = authService.login(username, "1234");

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals("manager", user.getRole());
    }

    @Test
    void login_shouldRejectWrongPassword() {
        DatabaseConnection.initializeDatabase();

        AuthenticationService authService = new AuthenticationService();

        String username = "wrongpass_" + System.currentTimeMillis();

        authService.register(username, "1234", "player");

        assertThrows(AuthenticationException.class, () ->
                authService.login(username, "wrong-password")
        );
    }

    @Test
    void register_shouldRejectDuplicateUsername() {
        DatabaseConnection.initializeDatabase();

        AuthenticationService authService = new AuthenticationService();

        String username = "duplicate_" + System.currentTimeMillis();

        authService.register(username, "1234", "manager");

        assertThrows(IllegalArgumentException.class, () ->
                authService.register(username, "1234", "manager")
        );
    }
}