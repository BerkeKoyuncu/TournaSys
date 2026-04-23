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

    
    // Maça skor girmek için kullanılacak metot
    public void setScore(int home, int away) {
        this.homeScore = home;
        this.awayScore = away;
        this.status = "Played"; // Skor girilince maç durumu güncellenir
    }

    // Skorları karşılaştırıp kazananı döndüren metot
    public Team getWinner() {
        if (this.homeScore > this.awayScore) {
            return this.homeTeam;
        } else if (this.awayScore > this.homeScore) {
            return this.awayTeam;
        } else {
            return null; // Beraberlik durumu
        }
    }
    
}