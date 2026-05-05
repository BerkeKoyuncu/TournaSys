package com.tournasys.controller;

import com.tournasys.util.SceneManager;
import com.tournasys.util.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class NavbarController {

    @FXML
    private Button dashboardButton;

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

    public void setActivePage(String page) {
        clearActiveState();

        switch (page.toLowerCase()) {
            case "dashboard":
                dashboardButton.getStyleClass().add("nav-button-active");
                break;
            case "tournaments":
                tournamentsButton.getStyleClass().add("nav-button-active");
                break;
            case "teams":
                teamsButton.getStyleClass().add("nav-button-active");
                break;
            case "matches":
                matchesButton.getStyleClass().add("nav-button-active");
                break;
            case "standings":
                standingsButton.getStyleClass().add("nav-button-active");
                break;
            case "bracket":
                bracketButton.getStyleClass().add("nav-button-active");
                break;
            default:
                break;
        }
    }

    private void clearActiveState() {
        dashboardButton.getStyleClass().remove("nav-button-active");
        tournamentsButton.getStyleClass().remove("nav-button-active");
        teamsButton.getStyleClass().remove("nav-button-active");
        matchesButton.getStyleClass().remove("nav-button-active");
        standingsButton.getStyleClass().remove("nav-button-active");
        bracketButton.getStyleClass().remove("nav-button-active");
    }

    @FXML
    public void goDashboard(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/dashboard-view.fxml");
    }

    @FXML
    public void goTournaments(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/tournament-view.fxml");
    }

    @FXML
    public void goTeams(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/team-view.fxml");
    }

    @FXML
    public void goMatches(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/match-view.fxml");
    }

    @FXML
    public void goStandings(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/standings-view.fxml");
    }

    @FXML
    public void goBracket(ActionEvent event) {
        SceneManager.switchScene("/com/tournasys/fxml/bracket-view.fxml");
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();

        SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
    }
}
