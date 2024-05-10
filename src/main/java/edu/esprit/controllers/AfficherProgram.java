package edu.esprit.controllers;

import com.itextpdf.layout.Document;
import edu.esprit.entities.Coach;
import edu.esprit.entities.Program;
import edu.esprit.services.ServiceCoach;
import edu.esprit.services.ServiceProgram;
import edu.esprit.utils.PdfGenerator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class AfficherProgram implements Initializable {
    @FXML
    private ScrollPane scroll;
    @FXML
    private GridPane grid;

    private final ServiceProgram sp = new ServiceProgram();
    Set<Program> programSet = sp.getAll();
    List<Program> programList = new ArrayList<>(programSet);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        affichergrid(programList);
    }
    private void affichergrid(List<Program> programList){
        int column = 0;
        int row = 1;
        try {
            for (int i = 0; i < programList.size(); i++) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ProgramComponent.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();

                ProgramComponent itemController = fxmlLoader.getController(); // Utilisez ReclamationItemComponentController
                itemController.setData(programList.get(i));

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
    @FXML
    void pdf(ActionEvent event) {
        String date= LocalDate.now().toString();
        String path="C:\\fittttcoach\\fitconnect\\src\\main\\resources\\pdf\\Program"+date+".pdf";
        Document doc=PdfGenerator.createPDF(path);
        doc.add(PdfGenerator.generateProgramsTablePdf());
        doc.close();
    }

    @FXML
    void stat(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/stat-program.fxml"));
            Scene scene=new Scene(root);
            Stage stage=new Stage();
            stage.setScene(scene);
            stage.show();
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
            Parent root = FXMLLoader.load(getClass().getResource("/AjouterProgram.fxml"));
            grid.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void gotocoach(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoach.fxml"));
            grid.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}
