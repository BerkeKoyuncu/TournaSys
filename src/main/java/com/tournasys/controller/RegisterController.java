package com.tournasys.controller;

import com.tournasys.service.AuthenticationService;
import com.tournasys.util.SceneManager;
import com.tournasys.util.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private ComboBox<String> roleBox;

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    public void initialize() {
        
        if (!SessionManager.isLoggedIn()) {
        SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
        return;
        }

        roleBox.getItems().addAll("manager", "player");
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
}