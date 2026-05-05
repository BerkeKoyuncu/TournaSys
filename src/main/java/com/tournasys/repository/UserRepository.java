package com.tournasys.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.model.Manager;
import com.tournasys.model.Player;
import com.tournasys.model.User;

public class UserRepository {

    public User findByUsername(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    int id = rs.getInt("user_id");
                    String uname = rs.getString("username");
                    String password = rs.getString("password_hash");
                    String role = rs.getString("role");

                    switch (role.toLowerCase()) {
                        case "manager":
                            return new Manager(id, uname, password);

                        case "player":
                            return new Player(id, uname, password);

                        default:
                            throw new RuntimeException("Unknown role: " + role);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void saveUser(User user) {
        String query = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user.");
        }
    }
}