package com.tournasys.model;

import java.util.List;

public class Player extends User {

    public Player(int userId, String username, String passwordHash) {
        super(userId, username, passwordHash, "player");
    }

    public List<Match> viewFixtures(Tournament tournament) {
        return tournament.getMatches();
    }

    public Standings viewStandings(Tournament tournament) {
        return tournament.getStandings();
    }
}