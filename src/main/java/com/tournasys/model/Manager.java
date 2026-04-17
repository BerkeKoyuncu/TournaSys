package com.tournasys.model;

public class Manager extends User {

    public Manager() {
        super();
    }

    public Manager(int userId, String username, String passwordHash) {
        super(userId, username, passwordHash, "manager");
    }
}