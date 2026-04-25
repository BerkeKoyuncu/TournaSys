package com.tournasys.service;

import java.util.List;

import com.tournasys.model.Match;
import com.tournasys.repository.MatchRepository;

public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService() {
        this.matchRepository = new MatchRepository();
    }

    public List<Match> getMatchesByTournament(int tournamentId) {
        return matchRepository.findByTournamentId(tournamentId);
    }

    public void updateScore(int matchId, int homeScore, int awayScore) {

        if (homeScore < 0 || awayScore < 0) {
            throw new IllegalArgumentException("Scores cannot be negative.");
        }

        boolean success = matchRepository.updateScore(matchId, homeScore, awayScore);

        if (!success) {
            throw new IllegalArgumentException("Match not found or update failed.");
        }
    }

    public boolean deleteMatch(int matchId) {
        return matchRepository.deleteMatch(matchId);
    }
}