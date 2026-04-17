package com.tournasys.controller;

import javafx.fxml.FXML;

public class StandingsController {

    @FXML
    private NavbarController navbarController;

    @FXML
    public void initialize() {
        navbarController.setActivePage("standings");
    }
}