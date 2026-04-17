package com.tournasys.controller;

import javafx.fxml.FXML;

public class TeamController {

    @FXML
    private NavbarController navbarController;

    @FXML
    public void initialize() {
        navbarController.setActivePage("teams");
    }
}