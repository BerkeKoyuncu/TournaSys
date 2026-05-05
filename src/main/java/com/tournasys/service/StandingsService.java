package com.tournasys.service;

import java.util.List;

import com.tournasys.model.StandingRow;
import com.tournasys.model.Tournament;
import com.tournasys.repository.StandingsRepository;

public class StandingsService {

    private final StandingsRepository standingsRepository;
    private final TournamentService tournamentService;

    public StandingsService() {
        this.standingsRepository = new StandingsRepository();
        this.tournamentService = new TournamentService();
    }

    public List<StandingRow> getStandings(int tournamentId) {
        Tournament tournament = tournamentService.getTournamentById(tournamentId);
        tournament.updateStandings();

        List<StandingRow> rows = tournament.getStandings().getRows();
        for (StandingRow row : rows) {
            standingsRepository.saveOrUpdateStanding(tournamentId, row);
        }

        return rows;
    }

    public void clearStandings(int tournamentId) {
        boolean success = standingsRepository.deleteByTournamentId(tournamentId);

        if (!success) {
            System.out.println("No standings found to delete.");
        }
    }
}
