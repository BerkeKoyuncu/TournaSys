package com.tournasys.service;

import com.tournasys.exception.AuthenticationException;
import com.tournasys.model.User;
import com.tournasys.repository.UserRepository;

public class AuthenticationService {

    private final UserRepository userRepository = new UserRepository();

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new AuthenticationException("User not found.");
        }

        if (!user.getPasswordHash().equals(password)) {
            throw new AuthenticationException("Wrong password.");
        }

        return user;
    }

    public void register(String username, String password, String role) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty.");
        }

        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }

        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role must be selected.");
        }

        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            throw new IllegalArgumentException("This username is already taken.");
        }

        User newUser = new User(0, username, password, role);
        userRepository.saveUser(newUser);
    }
}