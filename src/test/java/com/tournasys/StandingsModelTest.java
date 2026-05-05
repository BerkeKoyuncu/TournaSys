package com.tournasys;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import com.tournasys.model.Standings;
import com.tournasys.model.Team;

public class StandingsModelTest {

    @Test
    void recalculate_shouldSortTeamsByPoints() {
        Team teamA = new Team(1, "Team A");
        Team teamB = new Team(2, "Team B");

        teamA.recordWin();
        teamB.recordDraw();

        Standings standings = new Standings();
        standings.recalculate(List.of(teamB, teamA));

        assertEquals("Team A", standings.getLeader().getTeam().getName());
        assertEquals(3, standings.getLeader().getPoints());
    }

    @Test
    void getLeader_shouldReturnNullWhenEmpty() {
        Standings standings = new Standings();

        assertNull(standings.getLeader());
    }
}