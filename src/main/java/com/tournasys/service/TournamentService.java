package com.tournasys.service;

import java.util.List;
import java.util.stream.Collectors;

import com.tournasys.model.Match;
import com.tournasys.model.StandingRow;
import com.tournasys.model.Team;
import com.tournasys.model.Tournament;
import com.tournasys.repository.MatchRepository;
import com.tournasys.repository.StandingsRepository;
import com.tournasys.repository.TeamRepository;
import com.tournasys.repository.TournamentRepository;
import com.tournasys.util.SessionManager;

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
        SessionManager.requireManager();

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
        for (Match match : matches) {
            Team homeTeam = tournament.findTeamById(match.getHomeTeam().getTeamId());
            Team awayTeam = tournament.findTeamById(match.getAwayTeam().getTeamId());

            if (homeTeam == null || awayTeam == null) {
                continue;
            }

            Match tournamentMatch = new Match(
                    match.getMatchId(),
                    match.getMatchDate(),
                    homeTeam,
                    awayTeam
            );

            if (match.isCompleted()) {
                tournamentMatch.setScore(match.getHomeScore(), match.getAwayScore());
            }

            tournament.getMatches().add(tournamentMatch);
        }

        return tournament;
    }

    public List<Tournament> getAllTournaments() {
        return tournamentRepository.findAll();
    }

    public boolean deleteTournament(int tournamentId) {
        SessionManager.requireManager();

        matchRepository.deleteByTournamentId(tournamentId);
        standingsRepository.deleteByTournamentId(tournamentId);
        teamRepository.deleteByTournamentId(tournamentId);
        return tournamentRepository.deleteTournament(tournamentId);
    }

    public void generateSchedule(int tournamentId) {
        SessionManager.requireManager();

        Tournament tournament = getTournamentById(tournamentId);

        tournament.generateSchedule();

        matchRepository.deleteByTournamentId(tournamentId);
        standingsRepository.deleteByTournamentId(tournamentId);

        for (Match match : tournament.getMatches()) {
            matchRepository.saveMatch(match, tournamentId);
        }

        for (StandingRow row : tournament.getStandings().getRows()) {
            standingsRepository.saveOrUpdateStanding(tournamentId, row);
        }

        tournamentRepository.updateStatus(tournamentId, tournament.getStatus());
    }

    public void updateMatchScore(int tournamentId, int matchId, int homeScore, int awayScore) {
        SessionManager.requireManager();

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        Tournament tournamentBeforeUpdate = getTournamentById(tournamentId);
        if ("Knockout".equalsIgnoreCase(tournamentBeforeUpdate.getType()) && homeScore == awayScore) {
            throw new IllegalArgumentException("Knockout matches cannot end in a draw.");
        }

        boolean updated = matchRepository.updateScore(matchId, homeScore, awayScore);

        if (!updated) {
            throw new IllegalArgumentException("Match score could not be updated.");
        }

        Tournament tournament = getTournamentById(tournamentId);
        if ("Knockout".equalsIgnoreCase(tournament.getType())) {
            addNextKnockoutRoundIfNeeded(tournament, tournamentId);
            tournament = getTournamentById(tournamentId);
        }

        tournament.updateStandings();

        for (StandingRow row : tournament.getStandings().getRows()) {
            standingsRepository.saveOrUpdateStanding(tournamentId, row);
        }

        if (tournament.isCompleted()) {
            tournamentRepository.updateStatus(tournamentId, "Completed");
        } else {
            tournamentRepository.updateStatus(tournamentId, "In Progress");
        }
    }

    public List<Match> getMatchesByTournamentId(int tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

    public List<StandingRow> getStandingsByTournamentId(int tournamentId) {
        return standingsRepository.findByTournamentId(tournamentId);
    }

    private void addNextKnockoutRoundIfNeeded(Tournament tournament, int tournamentId) {
        List<Match> matches = tournament.getMatches();

        if (matches.isEmpty()) {
            return;
        }

        int roundStart = 0;
        int roundSize = tournament.getTeams().size() / 2;

        while (roundSize > 0 && roundStart + roundSize < matches.size()) {
            roundStart += roundSize;
            roundSize /= 2;
        }

        if (roundSize <= 0 || roundStart + roundSize != matches.size()) {
            return;
        }

        List<Match> currentRound = matches.subList(roundStart, matches.size());
        if (currentRound.stream().anyMatch(match -> !match.isCompleted())) {
            return;
        }

        List<Team> winners = currentRound.stream()
                .map(Match::getWinner)
                .collect(Collectors.toList());

        if (winners.size() <= 1) {
            return;
        }

        if (winners.size() % 2 != 0) {
            throw new IllegalStateException("Cannot create next knockout round with an odd number of winners.");
        }

        int existingMatchCount = matches.size();
        for (int i = 0; i < winners.size(); i += 2) {
            Match nextMatch = new Match(
                    existingMatchCount + (i / 2) + 1,
                    java.time.LocalDateTime.now().plusDays(existingMatchCount + (i / 2) + 1),
                    winners.get(i),
                    winners.get(i + 1)
            );
            matchRepository.saveMatch(nextMatch, tournamentId);
        }
    }
}
