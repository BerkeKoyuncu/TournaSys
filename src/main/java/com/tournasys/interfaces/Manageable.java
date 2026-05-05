package com.tournasys.interfaces;

import com.tournasys.exception.DuplicateTeamException;
import com.tournasys.model.Team;

public interface Manageable {

    void addTeam(Team team) throws DuplicateTeamException;

    boolean removeTeam(int teamId);
}