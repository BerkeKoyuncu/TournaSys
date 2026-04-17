package com.tournasys.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class SceneManager {

    private static Stage stage;

    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath) {
        try {
            URL resource = SceneManager.class.getResource(fxmlPath);

            if (resource == null) {
                throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            }

            double currentX = stage.getX();
            double currentY = stage.getY();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            if (currentWidth <= 0 || currentHeight <= 0) {
                currentWidth = DEFAULT_WIDTH;
                currentHeight = DEFAULT_HEIGHT;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Scene scene = new Scene(root, currentWidth, currentHeight);
            stage.setScene(scene);

            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);
            stage.setX(currentX);
            stage.setY(currentY);

        } catch (Exception e) {
            System.out.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}