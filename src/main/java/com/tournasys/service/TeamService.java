package com.tournasys.service;

import java.util.List;

import com.tournasys.exception.DuplicateTeamException;
import com.tournasys.model.Team;
import com.tournasys.model.Tournament;
import com.tournasys.repository.MatchRepository;
import com.tournasys.repository.StandingsRepository;
import com.tournasys.repository.TeamRepository;
import com.tournasys.repository.TournamentRepository;
import com.tournasys.util.SessionManager;

public class TeamService {
    private final TeamRepository teamRepository;
    private final TournamentRepository tournamentRepository;
    private final MatchRepository matchRepository;
    private final StandingsRepository standingsRepository;

    public TeamService() {
        this.teamRepository = new TeamRepository();
        this.tournamentRepository = new TournamentRepository();
        this.matchRepository = new MatchRepository();
        this.standingsRepository = new StandingsRepository();
    }

    public int addTeamToTournament(int tournamentId, String teamName) throws DuplicateTeamException {
        SessionManager.requireManager();

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
        int teamId = teamRepository.saveTeam(team, tournamentId);

        if (teamId <= 0) {
            throw new IllegalStateException("Team could not be saved.");
        }

        resetTournamentProgress(tournamentId);
        return teamId;
    }

    public List<Team> getTeamsByTournamentId(int tournamentId) {
        return teamRepository.findByTournamentId(tournamentId);
    }

    public boolean deleteTeam(int teamId) {
        SessionManager.requireManager();

        Integer tournamentId = teamRepository.findTournamentIdByTeamId(teamId);

        matchRepository.deleteByTeamId(teamId);
        standingsRepository.deleteByTeamId(teamId);
        boolean deleted = teamRepository.deleteTeam(teamId);

        if (deleted && tournamentId != null) {
            resetTournamentProgress(tournamentId);
        }

        return deleted;
    }

    private void resetTournamentProgress(int tournamentId) {
        matchRepository.deleteByTournamentId(tournamentId);
        standingsRepository.deleteByTournamentId(tournamentId);
        tournamentRepository.updateStatus(tournamentId, "Created");
    }
}
