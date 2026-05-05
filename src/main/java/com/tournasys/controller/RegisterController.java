package com.tournasys.controller;

import com.tournasys.service.AuthenticationService;
import com.tournasys.util.SceneManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleBox;
    @FXML private Label usernameHint;
    @FXML private Label passwordHint;

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    public void initialize() {
        roleBox.getItems().setAll("Manager", "Player");
        bindHint(usernameField, usernameHint);
        bindHint(passwordField, passwordHint);
    }

    @FXML
    private void handleRegister() {
        try {
            authService.register(
                    usernameField.getText(),
                    passwordField.getText(),
                    roleBox.getValue()
            );

            showInfo("User registered successfully!");

            SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void goToLogin() {
        SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }

    private void bindHint(TextField field, Label hint) {
        field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
            hint.setVisible(isFocused);
            hint.setManaged(isFocused);
        });
    }
}
