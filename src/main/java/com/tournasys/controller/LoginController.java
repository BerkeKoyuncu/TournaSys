package com.tournasys.controller;

import com.tournasys.model.User;
import com.tournasys.service.AuthenticationService;
import com.tournasys.util.SceneManager;
import com.tournasys.util.SessionManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User user = authService.login(username, password);

            SessionManager.login(user);

            SceneManager.switchScene("/com/tournasys/fxml/dashboard-view.fxml");

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void goToRegister() {
        SceneManager.switchScene("/com/tournasys/fxml/register-view.fxml");
    }

    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }
}