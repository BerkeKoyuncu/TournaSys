package com.tournasys.model;

import com.tournasys.exception.DuplicateTeamException;

public class Manager extends User {

    public Manager(int userId, String username, String passwordHash) {
        super(userId, username, passwordHash, "manager");
    }

    public Tournament createTournament(int tournamentId, String name, String type) {
        return new Tournament(tournamentId, name, type, "Created");
    }

    public void manageTeams(Tournament tournament, Team team) throws DuplicateTeamException {
        tournament.addTeam(team);
    }

    public void scheduleMatches(Tournament tournament) {
        tournament.generateSchedule();
    }

    public void enterScores(Match match, int homeScore, int awayScore) {
        match.setScore(homeScore, awayScore);
    }
}