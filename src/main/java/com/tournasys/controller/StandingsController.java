package com.tournasys.controller;

import com.tournasys.model.StandingRow;
import com.tournasys.model.Tournament;
import com.tournasys.service.StandingsService;
import com.tournasys.service.TournamentService;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class StandingsController {

    @FXML private NavbarController navbarController;
    @FXML private ComboBox<Tournament> tournamentBox;
    @FXML private TableView<StandingRow> standingsTable;

    @FXML private TableColumn<StandingRow, Integer> posColumn;
    @FXML private TableColumn<StandingRow, String> teamColumn;
    @FXML private TableColumn<StandingRow, Integer> playedColumn;
    @FXML private TableColumn<StandingRow, Integer> wonColumn;
    @FXML private TableColumn<StandingRow, Integer> drawColumn;
    @FXML private TableColumn<StandingRow, Integer> lostColumn;
    @FXML private TableColumn<StandingRow, Integer> pointsColumn;

    private final StandingsService standingsService = new StandingsService();
    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {
        if (navbarController != null) {
            navbarController.setActivePage("standings");
        }

        posColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getPosition())
        );
        teamColumn.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(
                        cell.getValue().getTeam().getName()
                )
        );
        playedColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getPlayed())
        );
        wonColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getWon())
        );
        drawColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getDrawn())
        );
        lostColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getLost())
        );
        pointsColumn.setCellValueFactory(cell ->
                new ReadOnlyObjectWrapper<>(cell.getValue().getPoints())
        );

        setupTournamentBox();
        tournamentBox.setItems(FXCollections.observableArrayList(
                tournamentService.getAllTournaments()
        ));

        tournamentBox.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldTournament, newTournament) -> loadStandings()
        );

        if (!tournamentBox.getItems().isEmpty()) {
            tournamentBox.getSelectionModel().selectFirst();
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

    private void loadStandings() {
        try {
            Tournament t = tournamentBox.getValue();
            if (t == null) {
                standingsTable.getItems().clear();
                return;
            }

            standingsTable.setItems(FXCollections.observableArrayList(
                    standingsService.getStandings(t.getTournamentId())
            ));
            standingsTable.refresh();
        } catch (Exception e) {
            standingsTable.getItems().clear();
            showError(e.getMessage());
        }
    }

    private String formatTournament(Tournament tournament) {
        return tournament.getName() + " - " + tournament.getType() + " - " + tournament.getStatus();
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}
