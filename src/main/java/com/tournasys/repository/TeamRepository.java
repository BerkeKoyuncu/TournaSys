package com.tournasys.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.model.Team;

public class TeamRepository {

    public int saveTeam(Team team, int tournamentId) {
        String sql = "INSERT INTO teams (name, tournament_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, team.getName());
            stmt.setInt(2, tournamentId);

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

    public List<Team> findByTournamentId(int tournamentId) {
        List<Team> teams = new ArrayList<>();
        String sql = "SELECT * FROM teams WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    teams.add(new Team(
                            rs.getInt("team_id"),
                            rs.getString("name")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return teams;
    }

    public Integer findTournamentIdByTeamId(int teamId) {
        String sql = "SELECT tournament_id FROM teams WHERE team_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("tournament_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean deleteTeam(int teamId) {
        String sql = "DELETE FROM teams WHERE team_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteByTournamentId(int tournamentId) {
        String sql = "DELETE FROM teams WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
