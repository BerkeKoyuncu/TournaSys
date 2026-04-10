package com.tournasys.model;

import com.tournasys.interfaces.Schedulable;
import java.util.ArrayList;
import java.util.List;

public class Tournament implements Schedulable {
    private int tournamentId;
    private String name;
    private String type;
    private String status;
    private List<Team> teams;
    private List<Match> matches;

    public Tournament(int tournamentId, String name, String type, String status) {
        this.tournamentId = tournamentId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    @Override
    public void generateSchedule() {
        // TODO: Implement scheduling logic
    }
}
