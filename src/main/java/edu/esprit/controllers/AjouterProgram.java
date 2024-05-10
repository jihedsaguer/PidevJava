package edu.esprit.controllers;

import edu.esprit.entities.Coach;
import edu.esprit.entities.Program;
import edu.esprit.services.ServiceCoach;
import edu.esprit.services.ServiceProgram;
import edu.esprit.utils.EmailSender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Set;
import java.sql.Date;


public class AjouterProgram {
    @FXML
    private TextField TFduree;

    @FXML
    private ComboBox<String> coachName;

    @FXML
    private ComboBox<String> typeProgram;

    @FXML
    private DatePicker startDate;

    private ServiceCoach serviceCoach;
    private ServiceProgram sp = new ServiceProgram();

    @FXML
    public void initialize() {
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

    @FXML
    void ajouterProgramAction(ActionEvent event) {
        String selectedCoachName = coachName.getValue();
        // Vérifier si un coach est sélectionné
        if (selectedCoachName != null) {
            // Récupérer l'ID du coach à partir de son nom
            int coachId = serviceCoach.getCoachIdByName(selectedCoachName);
            Coach coach = serviceCoach.getOneByID(coachId);
            // Si l'ID du coach est valide (différent de -1), ajouter le programme
            if (coachId != -1) {
                // Récupérer d'autres informations nécessaires pour ajouter le programme
                String duree = TFduree.getText();
                String type = typeProgram.getValue(); // Récupérer le type de programme
                LocalDate selectedDate = startDate.getValue(); // Récupérer la date sélectionnée

                // Vérifier si tous les champs sont remplis
                if (duree.isEmpty() || type == null || selectedDate == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setContentText("Veuillez remplir tous les champs.");
                    alert.showAndWait();
                } else {
                    // Vérifier si la date sélectionnée est antérieure à la date actuelle
                    if (selectedDate.isBefore(LocalDate.now())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Veuillez sélectionner une date future.");
                        alert.showAndWait();
                    } else {
                        // Convertir la date en java.sql.Date
                        Date startDateSql = Date.valueOf(selectedDate);

                        // Ajouter ici le code pour ajouter un programme en utilisant coachId, duree, type et startDateSql
                        try {
                            sp.ajouter(new Program(coach, type, duree, startDateSql));
                            EmailSender.sendEmail(coach.getEmail(), "Attribution de nouveau programme", "Bonjour " + coach.getName() + ",un nouveau programme vous a été attribué.");
                            // Assumant également que vous affichez un message de succès ou d'échec après l'ajout du programme
                            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                            successAlert.setTitle("Succès");
                            successAlert.setContentText("Programme ajouté avec succès !");
                            successAlert.show();
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                // Afficher un message d'erreur si aucun coach correspondant au nom sélectionné n'est trouvé
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("Aucun coach correspondant au nom sélectionné n'a été trouvé.");
                alert.showAndWait();
            }
        } else {
            // Afficher un message d'erreur si aucun coach n'est sélectionné
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Aucun coach n'a été sélectionné.");
            alert.showAndWait();
        }
    }


    @FXML
    void navigatetoAfficherProgramAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/AfficherProgram.fxml"));
            TFduree.getScene().setRoot(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Sorry");
            alert.setTitle("Error");
            alert.show();
        }
    }
}
