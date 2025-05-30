package org.example.qc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/gui/RoleSelectionView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 350);
            stage.setTitle("BelSign Login");

            // Tilføj globalt stylesheet
            scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
