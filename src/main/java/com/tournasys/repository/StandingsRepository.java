package com.tournasys.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.model.StandingRow;
import com.tournasys.model.Team;

public class StandingsRepository {

    public void saveOrUpdateStanding(int tournamentId, StandingRow row) {
        String sql = """
                INSERT INTO standings
                (tournament_id, team_id, played, won, drawn, lost, points)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                ON CONFLICT(tournament_id, team_id)
                DO UPDATE SET
                    played = excluded.played,
                    won = excluded.won,
                    drawn = excluded.drawn,
                    lost = excluded.lost,
                    points = excluded.points
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);
            stmt.setInt(2, row.getTeam().getTeamId());
            stmt.setInt(3, row.getPlayed());
            stmt.setInt(4, row.getWon());
            stmt.setInt(5, row.getDrawn());
            stmt.setInt(6, row.getLost());
            stmt.setInt(7, row.getPoints());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Standing save/update error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<StandingRow> findByTournamentId(int tournamentId) {
        List<StandingRow> rows = new ArrayList<>();

        String sql = """
                SELECT 
                    s.team_id,
                    t.name,
                    s.played,
                    s.won,
                    s.drawn,
                    s.lost,
                    s.points
                FROM standings s
                JOIN teams t ON s.team_id = t.team_id
                WHERE s.tournament_id = ?
                ORDER BY s.points DESC, s.won DESC, t.name ASC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);

            try (ResultSet rs = stmt.executeQuery()) {
                int position = 1;

                while (rs.next()) {
                    Team team = new Team(
                            rs.getInt("team_id"),
                            rs.getString("name")
                    );

                    team.setStats(
                            rs.getInt("played"),
                            rs.getInt("won"),
                            rs.getInt("drawn"),
                            rs.getInt("lost"),
                            rs.getInt("points")
                    );

                    rows.add(new StandingRow(position++, team));
                }
            }

        } catch (SQLException e) {
            System.out.println("Standing find error: " + e.getMessage());
            e.printStackTrace();
        }

        return rows;
    }

    public boolean deleteByTournamentId(int tournamentId) {
        String sql = "DELETE FROM standings WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Standing delete error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteByTeamId(int teamId) {
        String sql = "DELETE FROM standings WHERE team_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Standing delete by team error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
