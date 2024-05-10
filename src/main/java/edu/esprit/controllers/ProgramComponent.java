package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.entities.Program;
import edu.esprit.services.ServiceCoach;
import edu.esprit.services.ServiceProgram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.Optional;

public class ProgramComponent {
    @FXML
    private Label TFcoachName;

    @FXML
    private Label TFdate;

    @FXML
    private Label TFduree;

    @FXML
    private Label TFtypeProgram;
    private Program program;
    ServiceProgram serviceProgram = new ServiceProgram();
    public void setData(Program program){
        this.program = program;
        TFcoachName.setText(program.getCoach().getName());
        TFtypeProgram.setText(program.getType());
        TFduree.setText(program.getDuree());
        TFdate.setText(String.valueOf(program.getStartDate()));
    }

    @FXML
    void modifierButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierProgramme.fxml"));
            Parent root = loader.load();
            ModifierProgram controller = loader.getController();
            controller.setServiceProgram(serviceProgram);
            controller.setData(program);
            TFdate.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }

    @FXML
    void supprimerButton(ActionEvent event) {
        if (program != null) {
            // Créer une boîte de dialogue de confirmation
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce programme ?");
            confirmationAlert.setTitle("Confirmation de suppression");

            // Afficher la boîte de dialogue et attendre la réponse de l'utilisateur
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            // Vérifier si l'utilisateur a cliqué sur le bouton OK
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Supprimer le programme
                serviceProgram.supprimer(program.getId());

                // Afficher une alerte pour informer l'utilisateur que le programme a été supprimé avec succès
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Le programme a été supprimé avec succès.");
                alert.setTitle("Programme supprimé");
                alert.show();

                // Vous pouvez ajouter du code ici pour mettre à jour l'interface utilisateur en conséquence,
                // Rediriger l'utilisateur vers la vue précédente (par exemple, la liste des réclamations)
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/AfficherProgram.fxml"));
                    TFdate.getScene().setRoot(root);
                } catch (IOException e) {
                    // Gérer l'exception si la redirection échoue
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setContentText("Une erreur s'est produite lors de la redirection.");
                    errorAlert.setTitle("Erreur de redirection");
                    errorAlert.show();
                }
                // comme rafraîchir la liste des programmes affichés, etc.
            }
        } else {
            // Afficher un message d'erreur si le programme est null
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Impossible de supprimer ce programme car aucun programme n'est sélectionné.");
            errorAlert.setTitle("Erreur de suppression");
            errorAlert.show();
        }
    }

}
