package com.tournasys.exception;

public class DuplicateTeamException extends Exception {

    public DuplicateTeamException() {
        super("Team already exists in the tournament.");
    }

    public DuplicateTeamException(String message) {
        super(message);
    }
}