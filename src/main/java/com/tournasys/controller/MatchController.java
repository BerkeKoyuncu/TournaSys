package com.tournasys.controller;

import com.tournasys.model.Match;
import com.tournasys.model.Tournament;
import com.tournasys.service.MatchService;
import com.tournasys.service.TournamentService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class MatchController {

    @FXML private ComboBox<Tournament> tournamentBox;
    @FXML private TableView<Match> matchTable;

    @FXML private TableColumn<Match, Integer> idColumn;
    @FXML private TableColumn<Match, String> homeColumn;
    @FXML private TableColumn<Match, String> awayColumn;
    @FXML private TableColumn<Match, Integer> homeScoreColumn;
    @FXML private TableColumn<Match, Integer> awayScoreColumn;
    @FXML private TableColumn<Match, String> statusColumn;

    @FXML private TextField homeScoreField;
    @FXML private TextField awayScoreField;

    private final MatchService matchService = new MatchService();
    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<>("matchId"));
        homeColumn.setCellValueFactory(cell -> 
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getHomeTeam().getName()
                )
        );
        awayColumn.setCellValueFactory(cell -> 
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getAwayTeam().getName()
                )
        );
        homeScoreColumn.setCellValueFactory(new PropertyValueFactory<>("homeScore"));
        awayScoreColumn.setCellValueFactory(new PropertyValueFactory<>("awayScore"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        tournamentBox.setItems(FXCollections.observableArrayList(tournamentService.getAllTournaments()));

        tournamentBox.setOnAction(e -> loadMatches());
    }

    @FXML
    private void handleGenerateSchedule() {
        try {
            Tournament t = tournamentBox.getValue();
            if (t == null) throw new IllegalArgumentException("Select tournament");

            tournamentService.generateSchedule(t.getTournamentId());
            loadMatches();

            showInfo("Schedule created");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleUpdateScore() {
        try {
            Match selected = matchTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                throw new IllegalArgumentException("Select a match");
            }

            int home = Integer.parseInt(homeScoreField.getText());
            int away = Integer.parseInt(awayScoreField.getText());

            matchService.updateScore(selected.getMatchId(), home, away);

            Tournament t = tournamentBox.getValue();
            tournamentService.updateMatchScore(
                    t.getTournamentId(),
                    selected.getMatchId(),
                    home,
                    away
            );

            loadMatches();

            showInfo("Score updated");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void loadMatches() {
        Tournament t = tournamentBox.getValue();
        if (t == null) return;

        matchTable.setItems(FXCollections.observableArrayList(
                matchService.getMatchesByTournament(t.getTournamentId())
        ));
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}