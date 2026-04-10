package com.tournasys.model;

import java.time.LocalDateTime;

public class Match {
    private int matchId;
    private LocalDateTime matchDate;
    private Team homeTeam;
    private Team awayTeam;
    private int homeScore;
    private int awayScore;
    private String status;

    public Match(int matchId, LocalDateTime matchDate, Team homeTeam, Team awayTeam) {
        this.matchId = matchId;
        this.matchDate = matchDate;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.status = "Scheduled";
    }
}
