package com.tournasys.model;

import java.time.LocalDateTime;

import com.tournasys.interfaces.Scorable;

public class Match implements Scorable {
    private final int matchId;
    private LocalDateTime matchDate;
    private final Team homeTeam;
    private final Team awayTeam;
    private int homeScore;
    private int awayScore;
    private String status;

    public Match(int matchId, LocalDateTime matchDate, Team homeTeam, Team awayTeam) {
        if (homeTeam == null || awayTeam == null) {
            throw new IllegalArgumentException("Match teams cannot be null.");
        }

        if (homeTeam.equals(awayTeam)) {
            throw new IllegalArgumentException("A team cannot play against itself.");
        }

        this.matchId = matchId;
        this.matchDate = matchDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.status = "Scheduled";
    }

    @Override
    public void setScore(int homeScore, int awayScore) {

        if ("Completed".equals(status)) {
            throw new IllegalStateException("Match already completed.");
        }

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.status = "Completed";
    }

    public Team getWinner() {
        if (!"Completed".equals(status)) {
            return null;
        }

        if (homeScore > awayScore) {
            return homeTeam;
        } else if (awayScore > homeScore) {
            return awayTeam;
        }

        return null;
    }

    public boolean isDraw() {
        return "Completed".equals(status) && homeScore == awayScore;
    }

    @Override
    public boolean isCompleted() {
        return "Completed".equals(status);
    }

    public int getMatchId() {
        return matchId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public String getStatus() {
        return status;
    }
}
