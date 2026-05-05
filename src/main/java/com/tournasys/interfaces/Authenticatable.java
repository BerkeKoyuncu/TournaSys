package com.tournasys.interfaces;

import com.tournasys.model.User;

public interface Authenticatable {

    User login(String username, String password);

    void register(String username, String password, String role);
}