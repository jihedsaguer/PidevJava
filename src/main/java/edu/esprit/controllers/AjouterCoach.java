package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.services.ServiceCoach;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AjouterCoach {
    @FXML
    private TextField TFimg;

    @FXML
    private TextField TFmail;

    @FXML
    private TextField TFname;

    @FXML
    private ImageView img;

    private final ServiceCoach sc = new ServiceCoach();

    @FXML
    void initialize() {
        TFimg.setDisable(true);
    }

    // Méthode pour vérifier la validité de l'adresse e-mail
    private boolean isValidEmailAddress(String email) {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @FXML
    void ajouterCoachAction(ActionEvent event) {
        String name = TFname.getText();
        String mail = TFmail.getText();
        String imgPath = TFimg.getText();

        // Vérification des champs vides et de l'e-mail valide
        if (name.isEmpty() || mail.isEmpty() || imgPath.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Veuillez remplir tous les champs.");
            alert.showAndWait();
        } else if (!isValidEmailAddress(mail)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Adresse e-mail invalide.");
            alert.showAndWait();
        } else {
            try {
                sc.ajouter(new Coach(name, mail, imgPath));
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setContentText("Coach ajouté avec succès.");
                alert.show();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("SQL Exception");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    @FXML
    void navigatetoAfficherCoachAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoach.fxml"));
            TFname.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void upload(ActionEvent event) {
        FileChooser fileChooser=new FileChooser();
        File file=fileChooser.showOpenDialog(img.getScene().getWindow());
        if(file!=null){
            TFimg.setText(file.getName());
            Image image=new Image(file.toURI().toString());
            img.setImage(image);
        }
    }
}
