package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.entities.Program;
import edu.esprit.services.ServiceCoach;
import edu.esprit.services.ServiceProgram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;


public class ModifierProgram implements Initializable {
    @FXML
    private TextField TFduree;

    @FXML
    private ComboBox<String> coachName;

    @FXML
    private ComboBox<String> typeProgram;
    private ServiceCoach serviceCoach;
    @FXML
    private DatePicker startDate;
    private ServiceProgram sp = new ServiceProgram();
    private Program program;

    @FXML
    void modifierProgramAction(ActionEvent event) {
        String selectedCoachName = coachName.getValue();
        if (selectedCoachName != null) {
            int coachId = serviceCoach.getCoachIdByName(selectedCoachName);
            Coach coach = serviceCoach.getOneByID(coachId);
            if (coachId != -1) {
                String duree = TFduree.getText();
                String type = typeProgram.getValue();
                LocalDate selectedDate = startDate.getValue();
                if (selectedDate != null) {
                    // Mettre à jour les détails du programme
                    program.setCoach(coach);
                    program.setType(type);
                    program.setDuree(duree);
                    program.setStartDate(Date.valueOf(selectedDate));
                    // Appeler la méthode de modification du service de programme
                    sp.modifier(program);
                    // Afficher un message de succès
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Le programme a été modifié avec succès !");
                    alert.setTitle("Modification réussie");
                    alert.show();
                } else {
                    // Afficher un message si aucune date n'est sélectionnée
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Veuillez sélectionner une date de début.");
                    alert.setTitle("Date de début manquante");
                    alert.show();
                }
            } else {
                // Afficher un message si le coach sélectionné est invalide
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Aucun coach correspondant au nom sélectionné n'a été trouvé.");
                alert.setTitle("Coach invalide");
                alert.show();
            }
        } else {
            // Afficher un message si aucun coach n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Veuillez sélectionner un coach.");
            alert.setTitle("Coach manquant");
            alert.show();
        }
    }
    public void setData(Program program) {
        this.program = program;
        if (program != null) {
            // Remplir les champs avec les données du programme
            coachName.setValue(program.getCoach().getName());
            typeProgram.setValue(program.getType());
            TFduree.setText(program.getDuree());
            startDate.setValue(new Date(program.getStartDate().getTime()).toLocalDate());
        }
    }

    public void setServiceCoach(ServiceCoach serviceCoach) {
        this.serviceCoach = serviceCoach;
    }

    public void setServiceProgram(ServiceProgram serviceProgram) {
        this.sp = serviceProgram;
    }

    @FXML
    void navigatetoAfficherProgramAction(ActionEvent event) {
        try {
            // Redirection vers la vue AfficherProgram
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherProgram.fxml"));
            startDate.getScene().setRoot(root);
        } catch (IOException e) {
            // Gérer les erreurs de chargement de la vue
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de la redirection vers la vue AfficherProgram : " + e.getMessage());
            alert.setTitle("Erreur de redirection");
            alert.show();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialiser le service Coach
        serviceCoach = new ServiceCoach();

        // Récupérer tous les noms des coachs depuis la base de données
        Set<String> coachNames = serviceCoach.getAllCoachNames();

        // Convertir le set de noms de coachs en une liste observable
        ObservableList<String> coachNamesList = FXCollections.observableArrayList(coachNames);

        // Définir les noms des coachs dans le ComboBox coachName
        coachName.setItems(coachNamesList);
        ObservableList<String> typesList = FXCollections.observableArrayList(
                "Programme de flexibilité et de mobilité",
                "Prise de Masse",
                "Perte de poids"
        );
        typeProgram.setItems(typesList);
    }
}