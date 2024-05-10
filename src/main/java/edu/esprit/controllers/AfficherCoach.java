package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.services.ServiceCoach;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;

public class AfficherCoach implements Initializable {

    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;

    private final ServiceCoach sc = new ServiceCoach();
    Set<Coach> coachSet = sc.getAll();
    List<Coach> coachList = new ArrayList<>(coachSet);
    @FXML
    private TextField tfrecherche;

    @FXML
    private ComboBox<String> cbtri;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        affichergrid(coachList);
        recherche_avance();
        cbtri.getItems().setAll("Name","Email");
    }
    @FXML
    void tri(ActionEvent event) {
        affichergrid(new ArrayList<>(sc.triParCritere(cbtri.getValue())));
    }
    private void affichergrid(List<Coach> coachList){
        grid.getChildren().clear();
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < coachList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/CoachComponent.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                CoachComponent itemController = fxmlLoader.getController(); // Utilisez ReclamationItemComponentController
                itemController.setData(coachList.get(i));

                if (column == 1) {
                    column = 0;
                    row++;
                }

                grid.add(anchorPane, column++, row); //(child,column,row)
                //set grid width
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    void recherche_avance(){
        ObservableList<Coach> data= FXCollections.observableArrayList(sc.getAll());
        FilteredList<Coach> filteredList=new FilteredList<>(data,c->true);
        tfrecherche.textProperty().addListener((observable,oldValue,newValue)->{
            filteredList.setPredicate(c->{
                if(newValue.isEmpty() || newValue==null){
                    return true;
                } else if (c.getName().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else if (c.getEmail().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                } else{
                    return false;
                }
            });
            affichergrid(filteredList);
        });
    }
    @FXML
    void gotoProgramme(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherProgram.fxml"));
            tfrecherche.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
    @FXML
    void gotoajouter(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterCoach.fxml"));
            tfrecherche.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}