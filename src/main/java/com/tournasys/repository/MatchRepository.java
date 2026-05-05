package com.tournasys.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.model.Match;
import com.tournasys.model.Team;

public class MatchRepository {

    public int saveMatch(Match match, int tournamentId) {
        String sql = """
                INSERT INTO matches
                (match_date, home_score, away_score, status, tournament_id, home_team_id, away_team_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, match.getMatchDate().toString());
            stmt.setInt(2, match.getHomeScore());
            stmt.setInt(3, match.getAwayScore());
            stmt.setString(4, match.getStatus());
            stmt.setInt(5, tournamentId);
            stmt.setInt(6, match.getHomeTeam().getTeamId());
            stmt.setInt(7, match.getAwayTeam().getTeamId());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }

        } catch (SQLException e) {
            System.out.println("Match save error: " + e.getMessage());
            e.printStackTrace();
        }

        return -1;
    }

    public List<Match> findByTournamentId(int tournamentId) {
        List<Match> matches = new ArrayList<>();

        String sql = """
                SELECT 
                    m.match_id,
                    m.match_date,
                    m.home_score,
                    m.away_score,
                    m.status,
                    ht.team_id AS home_team_id,
                    ht.name AS home_team_name,
                    at.team_id AS away_team_id,
                    at.name AS away_team_name
                FROM matches m
                JOIN teams ht ON m.home_team_id = ht.team_id
                JOIN teams at ON m.away_team_id = at.team_id
                WHERE m.tournament_id = ?
                ORDER BY m.match_date ASC, m.match_id ASC
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Team homeTeam = new Team(
                            rs.getInt("home_team_id"),
                            rs.getString("home_team_name")
                    );

                    Team awayTeam = new Team(
                            rs.getInt("away_team_id"),
                            rs.getString("away_team_name")
                    );

                    Match match = new Match(
                            rs.getInt("match_id"),
                            LocalDateTime.parse(rs.getString("match_date")),
                            homeTeam,
                            awayTeam
                    );

                    if ("Completed".equals(rs.getString("status"))) {
                        match.setScore(
                                rs.getInt("home_score"),
                                rs.getInt("away_score")
                        );
                    }

                    matches.add(match);
                }
            }

        } catch (SQLException e) {
            System.out.println("Match find error: " + e.getMessage());
            e.printStackTrace();
        }

        return matches;
    }

    public boolean updateScore(int matchId, int homeScore, int awayScore) {
        String sql = """
                UPDATE matches
                SET home_score = ?, away_score = ?, status = ?
                WHERE match_id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, homeScore);
            stmt.setInt(2, awayScore);
            stmt.setString(3, "Completed");
            stmt.setInt(4, matchId);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Match score update error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteMatch(int matchId) {
        String sql = "DELETE FROM matches WHERE match_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, matchId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Match delete error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteByTournamentId(int tournamentId) {
        String sql = "DELETE FROM matches WHERE tournament_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, tournamentId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Match delete by tournament error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public boolean deleteByTeamId(int teamId) {
        String sql = "DELETE FROM matches WHERE home_team_id = ? OR away_team_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, teamId);
            stmt.setInt(2, teamId);
            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Match delete by team error: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }
}
