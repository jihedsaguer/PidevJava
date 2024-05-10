package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.services.ServiceCoach;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;

public class ModifierCoach {
    @FXML
    private TextField TFimg;

    @FXML
    private TextField TFmail;

    @FXML
    private TextField TFname;
    private Coach coach;
    @FXML
    private ImageView img;

    private ServiceCoach sc = new ServiceCoach();


    @FXML
    void modifierCoachAction(ActionEvent event) {
        if (coach != null && sc != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir modifier ce coach ?");
            confirmationAlert.setTitle("Confirmation de modification");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Mettre à jour les données de la réclamation avec les valeurs des champs de texte
                coach.setName(TFname.getText());
                coach.setEmail(TFmail.getText());
                coach.setImg(TFimg.getText());

                try {
                    // Appeler la méthode de modification du service de réclamation
                    sc.modifier(coach);

                    // Afficher un message de succès
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setContentText("coach modifiée avec succès !");
                    successAlert.setTitle("Modification réussie");
                    successAlert.show();

                    // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoach.fxml"));
                        TFname.getScene().setRoot(root);
                    } catch (IOException e) {
                        // Gérer l'exception si la redirection échoue
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setContentText("Une erreur s'est produite lors du coach.");
                        errorAlert.setTitle("Erreur de redirection");
                        errorAlert.show();
                    }
                } catch (Exception e) {
                    // Afficher un message d'erreur en cas d'échec de la modification
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Erreur lors de la modification du coach : " + e.getMessage());
                    errorAlert.setTitle("Erreur de modification");
                    errorAlert.show();
                }
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null ou si le service de réclamation est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de modifier le coach car aucun coach n'est sélectionnée");
            errorAlert.setTitle("Erreur de modification");
            errorAlert.show();
        }
    }
    public void setData(Coach coach) {
        this.coach = coach;
        if (coach != null) {
            TFname.setText(coach.getName());
            TFmail.setText(coach.getEmail());
            TFimg.setText(coach.getImg());
            try {
                File file = new File(getClass().getResource("/img/"+coach.getImg()).toURI());

                if (file.exists()) {
                    img.setImage(new Image(file.toURI().toString()));
                }
            } catch (Exception e) {
                System.out.println("default img");
            }
            TFimg.setDisable(true);
            }
        }

    public void setServiceCoach(ServiceCoach serviceCoach) {
        this.sc = serviceCoach;
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
