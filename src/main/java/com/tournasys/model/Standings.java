package com.tournasys.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Standings {
    private final List<StandingRow> rows;

    public Standings() {
        this.rows = new ArrayList<>();
    }

    public void recalculate(List<Team> teams) {
        rows.clear();

        List<Team> sortedTeams = new ArrayList<>(teams);

        sortedTeams.sort(
                Comparator.comparingInt(Team::getPoints).reversed()
                        .thenComparing(Comparator.comparingInt(Team::getWon).reversed())
                        .thenComparing(Team::getName)
        );

        int position = 1;

        for (Team team : sortedTeams) {
            rows.add(new StandingRow(position++, team));
        }
    }

    public void clear() {
        rows.clear();
    }

    public StandingRow getLeader() {
        if (rows.isEmpty()) {
            return null;
        }

        return rows.get(0);
    }

    public void displayTable() {
        System.out.println("Pos | Team | P | W | D | L | Pts");

        for (StandingRow row : rows) {
            System.out.println(
                    row.getPosition() + " | " +
                    row.getTeam().getName() + " | " +
                    row.getPlayed() + " | " +
                    row.getWon() + " | " +
                    row.getDrawn() + " | " +
                    row.getLost() + " | " +
                    row.getPoints()
            );
        }
    }

    public List<StandingRow> getRows() {
        return rows;
    }
}
