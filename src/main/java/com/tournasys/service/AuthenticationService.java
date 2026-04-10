package com.tournasys.service;

import com.tournasys.exception.AuthenticationException;
import com.tournasys.model.User;

public class AuthenticationService {

    public User login(String username, String password) throws AuthenticationException {
        throw new AuthenticationException("Login logic is not implemented yet.");
    }

    public void logout(User user) {
        // TODO: Implement logout logic
    }
}
