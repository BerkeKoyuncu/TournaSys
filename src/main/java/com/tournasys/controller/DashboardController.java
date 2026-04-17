package com.tournasys.controller;

import com.tournasys.util.SceneManager;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    private NavbarController navbarController;

    @FXML
    public void initialize() {
        navbarController.setActivePage("dashboard");
    }

    @FXML
    private void goToTournaments() {
        SceneManager.switchScene("/com/tournasys/fxml/tournament-view.fxml");
    }

    @FXML
    private void goToTeams() {
        SceneManager.switchScene("/com/tournasys/fxml/team-view.fxml");
    }

    @FXML
    private void goToMatches() {
        SceneManager.switchScene("/com/tournasys/fxml/match-view.fxml");
    }
}