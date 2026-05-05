package com.tournasys.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String DEFAULT_URL = "jdbc:sqlite:tournasys.db";

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
        String url = System.getProperty("tournasys.db.url", DEFAULT_URL);
        Connection connection = DriverManager.getConnection(url);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
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

        String createTournamentsTable = """
                CREATE TABLE IF NOT EXISTS tournaments (
                    tournament_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    type TEXT NOT NULL,
                    status TEXT NOT NULL,
                    manager_id INTEGER
                );
                """;

        String createTeamsTable = """
                CREATE TABLE IF NOT EXISTS teams (
                    team_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    tournament_id INTEGER NOT NULL,
                    FOREIGN KEY (tournament_id) REFERENCES tournaments(tournament_id)
                );
                """;

        String createMatchesTable = """
                CREATE TABLE IF NOT EXISTS matches (
                    match_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    match_date TEXT NOT NULL,
                    home_score INTEGER DEFAULT 0,
                    away_score INTEGER DEFAULT 0,
                    status TEXT NOT NULL,
                    tournament_id INTEGER NOT NULL,
                    home_team_id INTEGER NOT NULL,
                    away_team_id INTEGER NOT NULL,
                    FOREIGN KEY (tournament_id) REFERENCES tournaments(tournament_id),
                    FOREIGN KEY (home_team_id) REFERENCES teams(team_id),
                    FOREIGN KEY (away_team_id) REFERENCES teams(team_id)
                );
                """;

        String createStandingsTable = """
                CREATE TABLE IF NOT EXISTS standings (
                    standing_id INTEGER PRIMARY KEY AUTOINCREMENT,
                    tournament_id INTEGER NOT NULL,
                    team_id INTEGER NOT NULL,
                    played INTEGER DEFAULT 0,
                    won INTEGER DEFAULT 0,
                    drawn INTEGER DEFAULT 0,
                    lost INTEGER DEFAULT 0,
                    points INTEGER DEFAULT 0,
                    FOREIGN KEY (tournament_id) REFERENCES tournaments(tournament_id),
                    FOREIGN KEY (team_id) REFERENCES teams(team_id),
                    UNIQUE(tournament_id, team_id)
                );
                """;

        String insertDefaultUser = """
                INSERT OR IGNORE INTO users (user_id, username, password_hash, role)
                VALUES (1, 'berke', '170842', 'manager');
                """;

        String migrateDefaultUserPassword = """
                UPDATE users
                SET password_hash = '170842'
                WHERE username = 'berke' AND password_hash = '1234';
                """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(createUsersTable);
            statement.execute(createTournamentsTable);
            statement.execute(createTeamsTable);
            statement.execute(createMatchesTable);
            statement.execute(createStandingsTable);
            statement.execute(insertDefaultUser);
            statement.execute(migrateDefaultUserPassword);

            System.out.println("Database initialized successfully.");

        } catch (SQLException e) {
            System.out.println("Database initialization failed.");
            e.printStackTrace();
        }
    }
}
