package com.tournasys.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.model.Tournament;

public class TournamentRepository {

    public int saveTournament(Tournament tournament, int managerId) {
        String sql = "INSERT INTO tournaments (name, type, status, manager_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, tournament.getName());
            stmt.setString(2, tournament.getType());
            stmt.setString(3, tournament.getStatus());
            stmt.setInt(4, managerId);

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public Tournament findById(int tournamentId) {
        String sql = "SELECT * FROM tournaments WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Tournament(
                            rs.getInt("tournament_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Tournament> findAll() {
        List<Tournament> tournaments = new ArrayList<>();
        String sql = "SELECT * FROM tournaments ORDER BY tournament_id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                tournaments.add(new Tournament(
                        rs.getInt("tournament_id"),
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tournaments;
    }

    public boolean deleteTournament(int tournamentId) {
        String sql = "DELETE FROM tournaments WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean updateStatus(int tournamentId, String status) {
        String sql = "UPDATE tournaments SET status = ? WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, tournamentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
