package com.tournasys.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:tournasys.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("SQLite JDBC driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver could not be loaded.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
        String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password_hash TEXT NOT NULL,
                    role TEXT NOT NULL
                );
                """;

        String insertDefaultUser = """
                INSERT OR IGNORE INTO users (user_id, username, password_hash, role)
                VALUES (1, 'berke', '1234', 'manager');
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createUsersTable);
            statement.execute(insertDefaultUser);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization failed.");
            e.printStackTrace();
        }
    }
}