package com.tournasys.controller;

import com.tournasys.model.Team;
import com.tournasys.model.Tournament;
import com.tournasys.service.TeamService;
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

public class TeamController {

    @FXML private NavbarController navbarController;

    @FXML private TextField teamNameField;
    @FXML private ComboBox<Tournament> tournamentBox;
    @FXML private TableView<Team> teamTable;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private VBox operationCard;
    @FXML private Label subtitleLabel;

    @FXML private TableColumn<Team, Integer> teamIdColumn;
    @FXML private TableColumn<Team, String> teamNameColumn;

    private final TeamService teamService = new TeamService();
    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {
        if (navbarController != null) {
            navbarController.setActivePage("teams");
        }

        teamIdColumn.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        teamNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        setupTournamentBox();
        loadTournaments();
        applyRolePermissions();

        tournamentBox.setOnAction(event -> loadTeams());
    }

    @FXML
    private void handleAddTeam() {
        try {
            Tournament selectedTournament = tournamentBox.getValue();

            if (selectedTournament == null) {
                throw new IllegalArgumentException("Please select a tournament.");
            }

            teamService.addTeamToTournament(
                    selectedTournament.getTournamentId(),
                    teamNameField.getText()
            );

            teamNameField.clear();
            loadTeams();

            showInfo("Team added successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTeam() {
        try {
            Team selectedTeam = teamTable.getSelectionModel().getSelectedItem();

            if (selectedTeam == null) {
                throw new IllegalArgumentException("Please select a team.");
            }

            boolean deleted = teamService.deleteTeam(selectedTeam.getTeamId());

            if (!deleted) {
                throw new IllegalArgumentException("Team could not be deleted.");
            }

            loadTeams();
            showInfo("Team deleted successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        teamNameField.clear();
    }

    private void loadTournaments() {
        tournamentBox.setItems(FXCollections.observableArrayList(
                tournamentService.getAllTournaments()
        ));
    }

    private void loadTeams() {
        Tournament selectedTournament = tournamentBox.getValue();

        if (selectedTournament == null) {
            teamTable.getItems().clear();
            return;
        }

        teamTable.setItems(FXCollections.observableArrayList(
                teamService.getTeamsByTournamentId(selectedTournament.getTournamentId())
        ));
    }

    private void setupTournamentBox() {
        tournamentBox.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });

        tournamentBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Tournament item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName());
            }
        });
    }

    private void applyRolePermissions() {
        boolean manager = com.tournasys.util.SessionManager.isManager();
        operationCard.setVisible(manager);
        operationCard.setManaged(manager);
        subtitleLabel.setText(manager ? "Create and manage teams." : "View teams by tournament.");
        teamNameField.setDisable(!manager);
        addButton.setDisable(!manager);
        deleteButton.setDisable(!manager);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
