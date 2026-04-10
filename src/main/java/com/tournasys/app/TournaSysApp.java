package com.tournasys.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class TournaSysApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("TournaSys - Initial Project Scaffold");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("TournaSys");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
