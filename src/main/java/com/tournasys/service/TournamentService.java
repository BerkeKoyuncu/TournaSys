package com.tournasys.service;

import java.util.List;

import com.tournasys.model.Match;
import com.tournasys.model.StandingRow;
import com.tournasys.model.Team;
import com.tournasys.model.Tournament;
import com.tournasys.repository.MatchRepository;
import com.tournasys.repository.StandingsRepository;
import com.tournasys.repository.TeamRepository;
import com.tournasys.repository.TournamentRepository;

public class TournamentService {
    private final TournamentRepository tournamentRepository;
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final StandingsRepository standingsRepository;

    public TournamentService() {
        this.tournamentRepository = new TournamentRepository();
        this.teamRepository = new TeamRepository();
        this.matchRepository = new MatchRepository();
        this.standingsRepository = new StandingsRepository();
    }

    public int createTournament(String name, String type, int managerId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Tournament name cannot be empty.");
        }

        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Tournament type cannot be empty.");
        }

        Tournament tournament = new Tournament(0, name, type, "Created");
        return tournamentRepository.saveTournament(tournament, managerId);
    }

    public Tournament getTournamentById(int tournamentId) {
        Tournament tournament = tournamentRepository.findById(tournamentId);

        if (tournament == null) {
            throw new IllegalArgumentException("Tournament not found.");
        }

        List<Team> teams = teamRepository.findByTournamentId(tournamentId);
        for (Team team : teams) {
            try {
                tournament.addTeam(team);
            } catch (Exception e) {
                System.out.println("Team load error: " + e.getMessage());
            }
        }

        List<Match> matches = matchRepository.findByTournamentId(tournamentId);
        tournament.getMatches().addAll(matches);

        return tournament;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public boolean deleteTournament(int tournamentId) {
        standingsRepository.deleteByTournamentId(tournamentId);
        return tournamentRepository.deleteTournament(tournamentId);
    }

    public void generateSchedule(int tournamentId) {
        Tournament tournament = getTournamentById(tournamentId);

        tournament.generateSchedule();

        for (Match match : tournament.getMatches()) {
            matchRepository.saveMatch(match, tournamentId);
        }
    }

    public void updateMatchScore(int tournamentId, int matchId, int homeScore, int awayScore) {
        boolean updated = matchRepository.updateScore(matchId, homeScore, awayScore);

        if (!updated) {
            throw new IllegalArgumentException("Match score could not be updated.");
        }

        Tournament tournament = getTournamentById(tournamentId);
        tournament.updateStandings();

        for (StandingRow row : tournament.getStandings().getRows()) {
            standingsRepository.saveOrUpdateStanding(tournamentId, row);
        }
    }

    public List<Match> getMatchesByTournamentId(int tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

    public List<StandingRow> getStandingsByTournamentId(int tournamentId) {
        return standingsRepository.findByTournamentId(tournamentId);
    }
}