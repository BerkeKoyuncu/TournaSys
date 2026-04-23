package com.tournasys.model;

public class StandingRow {
    private Team team;
    private int position;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int points;

    public StandingRow(Team team) {
        this.team = team;
        this.played = 0;
        this.won = 0;
        this.drawn = 0;
        this.lost = 0;
        this.points = 0;
    }
    // İleride buraya galibiyet ekleme (addWin) gibi metotlar ekleyeceğim
}