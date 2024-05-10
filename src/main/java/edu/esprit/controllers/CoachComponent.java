package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.services.ServiceCoach;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class CoachComponent {

    @FXML
    private Label TFemail;

    @FXML
    private ImageView img;

    @FXML
    private Label TFname;
    private Coach coach;
    ServiceCoach serviceCoach = new ServiceCoach();
    public void setData(Coach coach){
        this.coach = coach;
        TFname.setText(coach.getName());
        TFemail.setText(coach.getEmail());
        try {
            File file = new File(getClass().getResource("/img/"+coach.getImg()).toURI());

            if (file.exists()) {
                img.setImage(new Image(file.toURI().toString()));
            }
        } catch (Exception e) {
            System.out.println("default img");
        }

    }

    @FXML
    void modifierButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierCoach.fxml"));
            Parent root = loader.load();
            ModifierCoach controller = loader.getController();
            controller.setServiceCoach(serviceCoach);
            controller.setData(coach);
            TFname.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void supprimerButton(ActionEvent event) {
        if (coach != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce coach ?");
            confirmationAlert.setTitle("Confirmation de suppression");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer la réclamation
                serviceCoach.supprimer(coach.getId());

                // Afficher une alerte pour informer l'utilisateur que la réclamation a été supprimée avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("coach a été supprimée avec succès.");
                alert.setTitle("coach supprimée");
                alert.show();

                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/AfficherCoach.fxml"));
                    TFname.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception si la redirection échoue
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                    errorAlert.setTitle("Erreur de redirection");
                    errorAlert.show();
                }
            }
        } else {
            // Afficher un message d'erreur si la réclamation est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer ce coach car aucune coach n'est sélectionnée.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }
}
