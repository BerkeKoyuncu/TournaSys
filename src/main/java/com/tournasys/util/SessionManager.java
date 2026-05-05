package com.tournasys.util;

import com.tournasys.model.User;

public class SessionManager {

    private static User currentUser;

    private SessionManager() {
    }

    public static void login(User user) {
        currentUser = user;
    }

    public static void logout() {
        currentUser = null;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isManager() {
        return currentUser != null && "manager".equalsIgnoreCase(currentUser.getRole());
    }

    public static boolean isPlayer() {
        return currentUser != null && "player".equalsIgnoreCase(currentUser.getRole());
    }

    public static int getCurrentUserId() {
        if (currentUser == null) {
            throw new IllegalStateException("No user is currently logged in.");
        }

        return currentUser.getUserId();
    }

    public static void requireManager() {
        if (!isManager()) {
            throw new SecurityException("Only managers can perform this operation.");
        }
    }
}
