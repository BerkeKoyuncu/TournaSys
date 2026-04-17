package com.tournasys.controller;

import com.tournasys.service.AuthenticationService;
import com.tournasys.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> roleBox;

    private final AuthenticationService authenticationService = new AuthenticationService();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleBox.getValue();

        try {
            authenticationService.register(username, password, role);
            showInformation("Registration Successful", "User account created successfully.");
            SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
        } catch (IllegalArgumentException e) {
            showError("Registration Error", e.getMessage());
        } catch (Exception e) {
            showError("Unexpected Error", "Something went wrong during registration.");
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLogin() {
        SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}