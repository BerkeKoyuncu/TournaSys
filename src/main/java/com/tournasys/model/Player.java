package com.tournasys.model;

public class Player extends User {

    public Player() {
        super();
    }

    public Player(int userId, String username, String passwordHash) {
        super(userId, username, passwordHash, "player");
    }
}