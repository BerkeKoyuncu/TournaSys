package com.tournasys.controller;

import javafx.fxml.FXML;

public class MatchController {

    @FXML
    private NavbarController navbarController;

    @FXML
    public void initialize() {
        navbarController.setActivePage("matches");
    }
}