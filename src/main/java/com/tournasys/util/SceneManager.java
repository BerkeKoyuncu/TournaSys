package com.tournasys.util;

import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    private static final double DEFAULT_WIDTH = 1000;
    private static final double DEFAULT_HEIGHT = 650;

    private SceneManager() {
    }

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;
    }

    public static void switchScene(String fxmlPath) {
        if (stage == null) {
            throw new IllegalStateException("Stage is not set. Call SceneManager.setStage(stage) first.");
        }

        try {
            URL resource = SceneManager.class.getResource(fxmlPath);

            if (resource == null) {
                throw new IllegalArgumentException("FXML not found: " + fxmlPath);
            }

            double currentX = stage.getX();
            double currentY = stage.getY();
            double currentWidth = stage.getWidth();
            double currentHeight = stage.getHeight();

            if (currentWidth <= 0 || Double.isNaN(currentWidth)) {
                currentWidth = DEFAULT_WIDTH;
            }

            if (currentHeight <= 0 || Double.isNaN(currentHeight)) {
                currentHeight = DEFAULT_HEIGHT;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Scene scene = new Scene(root, currentWidth, currentHeight);

            stage.setScene(scene);
            stage.setWidth(currentWidth);
            stage.setHeight(currentHeight);

            if (!Double.isNaN(currentX)) {
                stage.setX(currentX);
            }

            if (!Double.isNaN(currentY)) {
                stage.setY(currentY);
            }

            stage.show();

        } catch (Exception e) {
            System.out.println("Failed to load FXML: " + fxmlPath);
            e.printStackTrace();
        }
    }
}