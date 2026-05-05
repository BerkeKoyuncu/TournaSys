package com.tournasys.controller;

import com.tournasys.model.StandingRow;
import com.tournasys.model.Tournament;
import com.tournasys.service.StandingsService;
import com.tournasys.service.TournamentService;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class StandingsController {

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

        posColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        teamColumn.setCellValueFactory(cell ->
                new javafx.beans.property.SimpleStringProperty(
                        cell.getValue().getTeam().getName()
                )
        );
        playedColumn.setCellValueFactory(new PropertyValueFactory<>("played"));
        wonColumn.setCellValueFactory(new PropertyValueFactory<>("won"));
        drawColumn.setCellValueFactory(new PropertyValueFactory<>("drawn"));
        lostColumn.setCellValueFactory(new PropertyValueFactory<>("lost"));
        pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        tournamentBox.setItems(FXCollections.observableArrayList(
                tournamentService.getAllTournaments()
        ));

        tournamentBox.setOnAction(e -> loadStandings());
        
    }

    private void loadStandings() {
        Tournament t = tournamentBox.getValue();
        if (t == null) return;

        standingsTable.setItems(FXCollections.observableArrayList(
                standingsService.getStandings(t.getTournamentId())
        ));
    }
}