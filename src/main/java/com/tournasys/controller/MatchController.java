package com.tournasys.controller;

import com.tournasys.model.Match;
import com.tournasys.model.Tournament;
import com.tournasys.service.MatchService;
import com.tournasys.service.TournamentService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class MatchController {

    @FXML private NavbarController navbarController;
    @FXML private ComboBox<Tournament> tournamentBox;
    @FXML private TableView<Match> matchTable;

    @FXML private TableColumn<Match, Integer> idColumn;
    @FXML private TableColumn<Match, String> homeColumn;
    @FXML private TableColumn<Match, String> awayColumn;
    @FXML private TableColumn<Match, String> homeScoreColumn;
    @FXML private TableColumn<Match, String> awayScoreColumn;
    @FXML private TableColumn<Match, String> statusColumn;

    @FXML private TextField homeScoreField;
    @FXML private TextField awayScoreField;
    @FXML private Button generateScheduleButton;
    @FXML private Button updateScoreButton;
    @FXML private VBox operationCard;
    @FXML private Label subtitleLabel;

    private final MatchService matchService = new MatchService();
    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {
        if (navbarController != null) {
            navbarController.setActivePage("matches");
        }

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
        homeScoreColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().isCompleted()
                                ? String.valueOf(cell.getValue().getHomeScore())
                                : "-"
                )
        );
        awayScoreColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().isCompleted()
                                ? String.valueOf(cell.getValue().getAwayScore())
                                : "-"
                )
        );
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        setupTournamentBox();
        tournamentBox.setItems(FXCollections.observableArrayList(tournamentService.getAllTournaments()));
        applyRolePermissions();
        tournamentBox.setOnAction(e -> loadMatches());
        matchTable.getSelectionModel().selectedItemProperty().addListener((obs, oldMatch, newMatch) -> {
            if (newMatch == null) {
                homeScoreField.clear();
                awayScoreField.clear();
                return;
            }

            if (newMatch.isCompleted()) {
                homeScoreField.setText(String.valueOf(newMatch.getHomeScore()));
                awayScoreField.setText(String.valueOf(newMatch.getAwayScore()));
            } else {
                homeScoreField.clear();
                awayScoreField.clear();
            }
        });

        if (!tournamentBox.getItems().isEmpty()) {
            tournamentBox.getSelectionModel().selectFirst();
            loadMatches();
        }
    }

    @FXML
    private void handleGenerateSchedule() {
        try {
            Tournament t = tournamentBox.getValue();
            if (t == null) throw new IllegalArgumentException("Select tournament");

            tournamentService.generateSchedule(t.getTournamentId());
            refreshTournamentSelection(t.getTournamentId());
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

            int home = parseScore(homeScoreField.getText(), "Home score");
            int away = parseScore(awayScoreField.getText(), "Away score");

            Tournament t = tournamentBox.getValue();
            if (t == null) {
                throw new IllegalArgumentException("Select tournament");
            }

            tournamentService.updateMatchScore(
                    t.getTournamentId(),
                    selected.getMatchId(),
                    home,
                    away
            );

            loadMatches();
            homeScoreField.clear();
            awayScoreField.clear();

            showInfo("Score updated");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void loadMatches() {
        Tournament t = tournamentBox.getValue();
        if (t == null) {
            matchTable.getItems().clear();
            return;
        }

        matchTable.setItems(FXCollections.observableArrayList(
                matchService.getMatchesByTournament(t.getTournamentId())
        ));
    }

    private int parseScore(String value, String fieldName) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException(fieldName + " must be a whole number.");
        }
    }

    private void setupTournamentBox() {
        tournamentBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatTournament(item));
            }
        });

        tournamentBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : formatTournament(item));
            }
        });
    }

    private void refreshTournamentSelection(int tournamentId) {
        tournamentBox.setItems(FXCollections.observableArrayList(tournamentService.getAllTournaments()));
        tournamentBox.getItems().stream()
                .filter(tournament -> tournament.getTournamentId() == tournamentId)
                .findFirst()
                .ifPresent(tournament -> tournamentBox.getSelectionModel().select(tournament));
    }

    private String formatTournament(Tournament tournament) {
        return tournament.getName() + " - " + tournament.getType() + " - " + tournament.getStatus();
    }

    private void applyRolePermissions() {
        boolean manager = com.tournasys.util.SessionManager.isManager();
        operationCard.setVisible(manager);
        operationCard.setManaged(manager);
        subtitleLabel.setText(manager ? "Schedule matches and enter scores." : "View tournament fixtures and results.");
        homeScoreField.setDisable(!manager);
        awayScoreField.setDisable(!manager);
        generateScheduleButton.setDisable(!manager);
        updateScoreButton.setDisable(!manager);
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
