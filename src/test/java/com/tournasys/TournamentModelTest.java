package com.tournasys;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.tournasys.exception.DuplicateTeamException;
import com.tournasys.model.Match;
import com.tournasys.model.Team;
import com.tournasys.model.Tournament;

public class TournamentModelTest {

    @Test
    void addTeam_shouldAddTeamToTournament() throws DuplicateTeamException {
        Tournament tournament = new Tournament(1, "School Cup", "League", "Created");

        tournament.addTeam(new Team(1, "Team A"));

        assertEquals(1, tournament.getTeams().size());
        assertEquals("Team A", tournament.getTeams().get(0).getName());
    }

    @Test
    void addTeam_shouldThrowExceptionWhenDuplicateTeamAdded() throws DuplicateTeamException {
        Tournament tournament = new Tournament(1, "School Cup", "League", "Created");

        tournament.addTeam(new Team(1, "Team A"));

        assertThrows(DuplicateTeamException.class, () ->
                tournament.addTeam(new Team(2, "Team A"))
        );
    }

    @Test
    void generateSchedule_shouldCreateMatches() throws DuplicateTeamException {
        Tournament tournament = new Tournament(1, "School Cup", "League", "Created");

        tournament.addTeam(new Team(1, "Team A"));
        tournament.addTeam(new Team(2, "Team B"));
        tournament.addTeam(new Team(3, "Team C"));

        tournament.generateSchedule();

        assertEquals(3, tournament.getMatches().size());
        assertEquals("Scheduled", tournament.getStatus());
    }

    @Test
    void updateStandings_shouldCalculatePointsCorrectly() throws DuplicateTeamException {
        Tournament tournament = new Tournament(1, "School Cup", "League", "Created");

        Team teamA = new Team(1, "Team A");
        Team teamB = new Team(2, "Team B");

        tournament.addTeam(teamA);
        tournament.addTeam(teamB);
        tournament.generateSchedule();

        Match match = tournament.getMatches().get(0);
        Team expectedWinner = match.getHomeTeam();

        match.setScore(2, 1);
        tournament.updateStandings();

        assertEquals(3, expectedWinner.getPoints());
        assertEquals(expectedWinner.getName(), tournament.getStandings().getLeader().getTeam().getName());
    }
}