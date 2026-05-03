package com.tournasys;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.tournasys.model.Match;
import com.tournasys.model.Team;

public class MatchModelTest {

    @Test
    void setScore_shouldCompleteMatch() {
        Team home = new Team(1, "Home");
        Team away = new Team(2, "Away");

        Match match = new Match(1, LocalDateTime.now(), home, away);
        match.setScore(3, 1);

        assertTrue(match.isCompleted());
        assertEquals(home, match.getWinner());
        assertFalse(match.isDraw());
    }

    @Test
    void setScore_shouldRejectNegativeScores() {
        Team home = new Team(1, "Home");
        Team away = new Team(2, "Away");

        Match match = new Match(1, LocalDateTime.now(), home, away);

        assertThrows(IllegalArgumentException.class, () ->
                match.setScore(-1, 2)
        );
    }

    @Test
    void constructor_shouldRejectSameTeam() {
        Team team = new Team(1, "Team A");

        assertThrows(IllegalArgumentException.class, () ->
                new Match(1, LocalDateTime.now(), team, team)
        );
    }
}