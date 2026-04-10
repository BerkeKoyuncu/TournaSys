package com.tournasys.model;

public class Team {
    private int teamId;
    private String name;
    private int played;
    private int won;
    private int drawn;
    private int lost;
    private int points;

    public Team(int teamId, String name) {
        this.teamId = teamId;
        this.name = name;
    }

    public int getTeamId() {
        return teamId;
    }

    public String getName() {
        return name;
    }
}
