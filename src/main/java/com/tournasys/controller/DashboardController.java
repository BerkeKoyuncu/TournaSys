package com.tournasys.controller;

import com.tournasys.util.SceneManager;
import com.tournasys.util.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardController {

    @FXML
    private NavbarController navbarController;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Button tournamentsButton;

    @FXML
    private Button teamsButton;

    @FXML
    private Button matchesButton;

    @FXML
    private Button standingsButton;

    @FXML
    private Button bracketButton;

    @FXML
    public void initialize() {

        if (!SessionManager.isLoggedIn()) {
            SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
            return;
        }

        navbarController.setActivePage("dashboard");
        applyRoleText();
    }

    private void applyRoleText() {
        if (SessionManager.isManager()) {
            subtitleLabel.setText("Manage your tournaments efficiently.");
            tournamentsButton.setText("Create Tournament");
            teamsButton.setText("Manage Teams");
            matchesButton.setText("Schedule Matches");
            standingsButton.setText("View Standings");
            bracketButton.setText("View Bracket");
            return;
        }

        subtitleLabel.setText("View tournaments, fixtures, and standings.");
        tournamentsButton.setText("View Tournaments");
        teamsButton.setText("View Teams");
        matchesButton.setText("View Matches");
        standingsButton.setText("View Standings");
        bracketButton.setText("View Bracket");
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

    @FXML
    private void goToStandings() {
        SceneManager.switchScene("/com/tournasys/fxml/standings-view.fxml");
    }

    @FXML
    private void goToBracket() {
        SceneManager.switchScene("/com/tournasys/fxml/bracket-view.fxml");
    }
}
