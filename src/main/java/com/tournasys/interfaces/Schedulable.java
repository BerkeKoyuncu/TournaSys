package com.tournasys.interfaces;

import java.util.List;

import com.tournasys.model.Match;

public interface Schedulable {

    void generateSchedule();

    List<Match> getSchedule();

    boolean isScheduleGenerated();
}