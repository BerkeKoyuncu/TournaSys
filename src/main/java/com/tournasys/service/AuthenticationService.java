package com.tournasys.service;

import com.tournasys.exception.AuthenticationException;
import com.tournasys.interfaces.Authenticatable;
import com.tournasys.model.Manager;
import com.tournasys.model.Player;
import com.tournasys.model.User;
import com.tournasys.repository.UserRepository;

public class AuthenticationService implements Authenticatable {

    private static final String USERNAME_PATTERN = "^[A-Za-z0-9_]{3,20}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S{8,}$";

    private final UserRepository userRepository = new UserRepository();

    private String hashPassword(String password) {
        return Integer.toHexString(password.hashCode());
    }

    @Override
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        String hashedPassword = hashPassword(password);

        if (!user.getPasswordHash().equals(hashedPassword)) {
            throw new AuthenticationException("Wrong password.");
        }

        return user;
    }

    @Override
    public void register(String username, String password, String role) {

        validateUsername(username);
        validatePassword(password);

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must be selected.");
        }

        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("This username is already taken.");
        }

        String hashedPassword = hashPassword(password);

        User newUser = switch (role.toLowerCase()) {
            case "manager" -> new Manager(0, username, hashedPassword);
            case "player" -> new Player(0, username, hashedPassword);
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };

        userRepository.saveUser(newUser);
    }

    private void validateUsername(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (!username.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException(
                    "Username must be 3-20 characters and use only letters, numbers, or underscore."
            );
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (!password.matches(PASSWORD_PATTERN)) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters and include a letter, number, and special character."
            );
        }
    }
}
