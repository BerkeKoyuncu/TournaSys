package com.tournasys.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.tournasys.exception.DuplicateTeamException;
import com.tournasys.interfaces.Manageable;
import com.tournasys.interfaces.Schedulable;

public class Tournament implements Schedulable, Manageable {
    private final int tournamentId;
    private String name;
    private String type;
    private String status;
    private final List<Team> teams;
    private final List<Match> matches;
    private final Standings standings;

    public Tournament(int tournamentId, String name, String type, String status) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.standings = new Standings();
    }

    @Override
    public void addTeam(Team team) throws DuplicateTeamException {
        if (team == null) {
            throw new IllegalArgumentException("Team cannot be null");
        }

        for (Team existingTeam : teams) {
            if (existingTeam.getName().equalsIgnoreCase(team.getName())) {
                throw new DuplicateTeamException("Team already exists in this tournament: " + team.getName());
            }
        }

        teams.add(team);
        standings.recalculate(teams);
    }

    @Override
    public boolean removeTeam(int teamId) {
        boolean removed = teams.removeIf(team -> team.getTeamId() == teamId);

        if (removed) {
            matches.removeIf(match ->
                    match.getHomeTeam().getTeamId() == teamId ||
                    match.getAwayTeam().getTeamId() == teamId
            );

            standings.recalculate(teams);
        }

        return removed;
    }

    @Override
    public void generateSchedule() {
        matches.clear();

        if (teams.size() < 2) {
            throw new IllegalStateException("At least 2 teams required to generate schedule.");
        }

        if ("Knockout".equalsIgnoreCase(type)) {
            generateKnockoutRound(teams, LocalDateTime.now().plusDays(1));
        } else {
            generateLeagueSchedule();
        }

        status = "Scheduled";
    }

    private void generateLeagueSchedule() {
        int matchId = 1;
        LocalDateTime startDate = LocalDateTime.now().plusDays(1);

        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Match match = new Match(
                        matchId,
                        startDate.plusDays(matchId - 1),
                        teams.get(i),
                        teams.get(j)
                );

                matches.add(match);
                matchId++;
            }
        }
    }

    private void generateKnockoutRound(List<Team> roundTeams, LocalDateTime startDate) {
        if (roundTeams.size() % 2 != 0) {
            throw new IllegalStateException("Knockout tournaments require an even number of teams.");
        }

        int matchId = matches.size() + 1;

        for (int i = 0; i < roundTeams.size(); i += 2) {
            matches.add(new Match(
                    matchId,
                    startDate.plusDays(matchId - 1),
                    roundTeams.get(i),
                    roundTeams.get(i + 1)
            ));

            matchId++;
        }
    }

    public void updateStandings() {
        for (Team team : teams) {
            team.resetStats();
        }

        for (Match match : matches) {
            if (!match.isCompleted()) {
                continue;
            }

            Team homeTeam = match.getHomeTeam();
            Team awayTeam = match.getAwayTeam();

            if (match.isDraw()) {
                homeTeam.recordDraw();
                awayTeam.recordDraw();
            } else if (match.getHomeScore() > match.getAwayScore()) {
                homeTeam.recordWin();
                awayTeam.recordLoss();
            } else {
                awayTeam.recordWin();
                homeTeam.recordLoss();
            }
        }

        standings.recalculate(teams);
    }

    public Team findTeamById(int teamId) {
        return teams.stream()
                .filter(t -> t.getTeamId() == teamId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Match> getSchedule() {
        return matches;
    }

    @Override
    public boolean isScheduleGenerated() {
        return !matches.isEmpty();
    }

    public boolean isCompleted() {
        return !matches.isEmpty() && matches.stream().allMatch(Match::isCompleted);
    }

    public int getTournamentId() {
        return tournamentId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public Standings getStandings() {
        return standings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
