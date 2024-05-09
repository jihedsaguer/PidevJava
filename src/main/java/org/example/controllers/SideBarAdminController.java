package org.example.controllers;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SideBarAdminController implements Initializable {

    @FXML
    private VBox menuVBox;

    @FXML
    private Pane contenuPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisez votre contrôleur ici si nécessaire

    }

    @FXML
    private void afficherOption1() {
        // Chargez le contenu de l'option 1 dans la zone de droite
        loadContent("/Dashboard.fxml");

    }

    @FXML
    private void afficherOption2() {
        // Chargez le contenu de l'option 2 dans la zone de droite
        loadContent("/DashboardEvent.fxml");
    }

    private void loadContent(String fxmlFileName) {
        try {
            AnchorPane content = FXMLLoader.load(getClass().getResource(fxmlFileName));
            contenuPane.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
