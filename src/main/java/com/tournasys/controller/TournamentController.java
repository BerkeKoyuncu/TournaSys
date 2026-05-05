package com.tournasys.controller;

import com.tournasys.model.Tournament;
import com.tournasys.service.TournamentService;
import com.tournasys.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class TournamentController {

    @FXML private NavbarController navbarController;

    @FXML private TextField nameField;
    @FXML private ComboBox<String> typeBox;
    @FXML private TableView<Tournament> table;
    @FXML private Button createButton;
    @FXML private Button deleteButton;
    @FXML private VBox operationCard;
    @FXML private Label subtitleLabel;

    @FXML private TableColumn<Tournament, Integer> idColumn;
    @FXML private TableColumn<Tournament, String> nameColumn;
    @FXML private TableColumn<Tournament, String> typeColumn;
    @FXML private TableColumn<Tournament, String> statusColumn;

    private final TournamentService tournamentService = new TournamentService();

    @FXML
    public void initialize() {
        if (navbarController != null) {
            navbarController.setActivePage("tournaments");
        }

        typeBox.setItems(FXCollections.observableArrayList("League", "Knockout"));

        idColumn.setCellValueFactory(new PropertyValueFactory<>("tournamentId"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        applyRolePermissions();
        loadTournaments();
    }

    @FXML
    private void handleCreateTournament() {
        try {
            String name = nameField.getText();
            String type = typeBox.getValue();

            int managerId = SessionManager.isLoggedIn()
                    ? SessionManager.getCurrentUserId()
                    : 1;

            tournamentService.createTournament(name, type, managerId);

            clearFields();
            loadTournaments();

            showInfo("Tournament created successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleDeleteTournament() {
        try {
            Tournament selectedTournament = table.getSelectionModel().getSelectedItem();

            if (selectedTournament == null) {
                throw new IllegalArgumentException("Please select a tournament.");
            }

            boolean deleted = tournamentService.deleteTournament(selectedTournament.getTournamentId());

            if (!deleted) {
                throw new IllegalArgumentException("Tournament could not be deleted.");
            }

            loadTournaments();
            showInfo("Tournament deleted successfully.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        clearFields();
    }

    private void loadTournaments() {
        table.setItems(FXCollections.observableArrayList(
                tournamentService.getAllTournaments()
        ));
    }

    private void clearFields() {
        nameField.clear();
        typeBox.setValue(null);
    }

    private void applyRolePermissions() {
        boolean manager = SessionManager.isManager();
        operationCard.setVisible(manager);
        operationCard.setManaged(manager);
        subtitleLabel.setText(manager ? "Create and manage tournaments." : "View available tournaments.");
        nameField.setDisable(!manager);
        typeBox.setDisable(!manager);
        createButton.setDisable(!manager);
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
