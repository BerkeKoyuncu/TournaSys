package com.tournasys.app;

import com.tournasys.config.DatabaseConnection;
import com.tournasys.util.SceneManager;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection.initializeDatabase();

        primaryStage.setTitle("TournaSys");
        loadApplicationIcons(primaryStage);
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

    private void loadApplicationIcons(Stage stage) {
        int[] iconSizes = {16, 32, 64, 128, 256, 512};

        for (int size : iconSizes) {
            String iconPath = "/com/tournasys/ico/icon-" + size + ".png";
            try {
                stage.getIcons().add(new Image(getClass().getResourceAsStream(iconPath)));
            } catch (Exception e) {
                System.out.println("Application icon could not be loaded: " + iconPath);
            }
        }
    }
}
