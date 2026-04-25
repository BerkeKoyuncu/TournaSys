package com.tournasys.service;

import java.util.List;

import com.tournasys.model.StandingRow;
import com.tournasys.repository.StandingsRepository;

public class StandingsService {

    private final StandingsRepository standingsRepository;

    public StandingsService() {
        this.standingsRepository = new StandingsRepository();
    }

    public List<StandingRow> getStandings(int tournamentId) {
        return standingsRepository.findByTournamentId(tournamentId);
    }

    public void clearStandings(int tournamentId) {
        boolean success = standingsRepository.deleteByTournamentId(tournamentId);

        if (!success) {
            System.out.println("No standings found to delete.");
        }
    }
}