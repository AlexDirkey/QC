package org.example.qc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(Main.class.getResource("/LoginView.fxml"));
        System.out.println(Main.class.getClassLoader().getResource("LoginView.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/gui/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BelSign Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}