package com.tournasys.model;

public class StandingRow {
    private int position;
    private final Team team;
    private final int played;
    private final int won;
    private final int drawn;
    private final int lost;
    private final int points;

    public StandingRow(int position, Team team) {
        this.position = position;
        this.team = team;
        this.played = team.getPlayed();
        this.won = team.getWon();
        this.drawn = team.getDrawn();
        this.lost = team.getLost();
        this.points = team.getPoints();
    }

    public int getPosition() {
        return position;
    }

    public Team getTeam() {
        return team;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getDrawn() {
        return drawn;
    }

    public int getLost() {
        return lost;
    }

    public int getPoints() {
        return points;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
