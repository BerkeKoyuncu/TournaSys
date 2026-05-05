package com.tournasys.app;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.util.SceneManager;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection.initializeDatabase();

        primaryStage.setTitle("TournaSys");
        primaryStage.setWidth(1000);
        primaryStage.setHeight(650);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(650);

        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/com/tournasys/fxml/login-view.fxml");
        primaryStage.setTitle("TournaSys");

        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}