package com.tournasys.controller;

import com.tournasys.exception.AuthenticationException;
import com.tournasys.model.User;
import com.tournasys.service.AuthenticationService;
import com.tournasys.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private final AuthenticationService authService = new AuthenticationService();

    @FXML
    private void handleLogin() {
        try {
            String username = usernameField.getText();
            String password = passwordField.getText();

            User user = authService.login(username, password);

            System.out.println("Logged in as: " + user.getUsername());
            SceneManager.switchScene("/com/tournasys/fxml/dashboard-view.fxml");

        } catch (AuthenticationException e) {
            showError(e.getMessage());
        } catch (Exception e) {
            showError("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRegister() {
        SceneManager.switchScene("/com/tournasys/fxml/register-view.fxml");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Authentication Failed");
        alert.setContentText(message);
        alert.showAndWait();
    }
}