package edu.esprit.tests;

import edu.esprit.entities.Coach;
import edu.esprit.services.ServiceCoach;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterProgram.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Gestion Coach");
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}