package com.tournasys.model;

public class Team {
    private final int teamId;
    private String name;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int points;

    public Team(int teamId, String name) {
        this.teamId = teamId;
        this.name = name;
        this.played = 0;
        this.won = 0;
        this.drawn = 0;
        this.lost = 0;
        this.points = 0;
    }

    public void setStats(int played, int won, int drawn, int lost, int points) {
        this.played = played;
        this.won = won;
        this.drawn = drawn;
        this.lost = lost;
        this.points = points;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Team other)) {
            return false;
        }

        return teamId > 0 && other.teamId > 0 && teamId == other.teamId;
    }

    @Override
    public int hashCode() {
        return teamId > 0 ? Integer.hashCode(teamId) : System.identityHashCode(this);
    }

    public void recordWin() {
        played++;
        won++;
        points += 3;
    }

    public void recordDraw() {
        played++;
        drawn++;
        points += 1;
    }

    public void recordLoss() {
        played++;
        lost++;
    }

    public void resetStats() {
        played = 0;
        won = 0;
        drawn = 0;
        lost = 0;
        points = 0;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " (" + points + " pts)";
    }
}
