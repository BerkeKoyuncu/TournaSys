package com.tournasys.service;

import java.util.List;

import com.tournasys.exception.DuplicateTeamException;
import com.tournasys.model.Team;
import com.tournasys.model.Tournament;
import com.tournasys.repository.TeamRepository;
import com.tournasys.repository.TournamentRepository;

public class TeamService {
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;

    public TeamService() {
        this.teamRepository = new TeamRepository();
        this.tournamentRepository = new TournamentRepository();
    }

    public int addTeamToTournament(int tournamentId, String teamName) throws DuplicateTeamException {
        if (teamName == null || teamName.isBlank()) {
            throw new IllegalArgumentException("Team name cannot be empty.");
        }

        Tournament tournament = tournamentRepository.findById(tournamentId);

        if (tournament == null) {
            throw new IllegalArgumentException("Tournament not found.");
        }

        List<Team> existingTeams = teamRepository.findByTournamentId(tournamentId);

        for (Team existingTeam : existingTeams) {
            if (existingTeam.getName().equalsIgnoreCase(teamName)) {
                throw new DuplicateTeamException("Team already exists: " + teamName);
            }
        }

        Team team = new Team(0, teamName);
        return teamRepository.saveTeam(team, tournamentId);
    }

    public List<Team> getTeamsByTournamentId(int tournamentId) {
        return teamRepository.findByTournamentId(tournamentId);
    }

    public boolean deleteTeam(int teamId) {
        return teamRepository.deleteTeam(teamId);
    }
}