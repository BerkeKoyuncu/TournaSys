package com.tournasys.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class TournamentController {

    @FXML
    private NavbarController navbarController;

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> typeBox;

    @FXML
    private TableView<?> table;

    @FXML
    public void initialize() {
        navbarController.setActivePage("tournaments");
    }
}