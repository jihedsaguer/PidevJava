package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.example.entities.Event;
import org.example.service.EventService;
import org.example.tools.DBconnexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardEvent {
    @FXML
    private PieChart pieChart;


    @FXML
    private AnchorPane rootPane;


    @FXML
    void tocreateevent(ActionEvent event) {
        pn_addevent.toFront();
        initialize(); // Appel de la méthode initialize() après le changement de vue
    }

    @FXML
    void toeventlist(ActionEvent event) {
        pn_eventlist.toFront();
        initialize(); // Appel de la méthode initialize() après le changement de vue
    }




    @FXML
    void handleStatisticsGeneration(ActionEvent event) {
        try {
            // 1. Récupérer les données des événements et des participations depuis la base de données
            // Par exemple, vous pouvez utiliser des requêtes SQL pour obtenir ces données
            Connection connection = DBconnexion.getInstance().getCnx();
            String query = "SELECT e.nom AS event_nom, COUNT(p.event_id) AS nb_participants " +
                    "FROM event e LEFT JOIN participation p ON e.id = p.event_id " +
                    "GROUP BY e.nom";
            ResultSet resultSet = connection.createStatement().executeQuery(query);

            // 2. Calculer le nombre de participations pour chaque événement
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String eventName = resultSet.getString("event_nom");
                int participantsCount = resultSet.getInt("nb_participants");
                // 3. Créer une liste d'objets PieChart.Data
                pieChartData.add(new PieChart.Data(eventName, participantsCount));
            }
            // Modifier la position du PieChart
            pieChart.setLayoutX(-40); // Remplacez nouvelleValeurX par la valeur x désirée
            pieChart.setLayoutY(210); // Remplacez nouvelleValeurY par la valeur y désirée

            // 4. Ajouter les données au PieChart pour les afficher
            pieChart.setData(pieChartData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private GridPane grid;

    @FXML
    private Pane pn_eventlist;

    @FXML
    private Pane pn_addevent;

    @FXML
    private DatePicker tf_date;

    @FXML
    private TextArea tf_desc;

    @FXML
    private TextField tf_lieu;

    @FXML
    private TextField tf_nom;

    @FXML
    private TextField tf_lan;

    @FXML
    private TextField tf_lon;

    EventService es = new EventService();

    @FXML
    void titre(ActionEvent event) {
        grid.getChildren().clear();
        triet();
    }

    @FXML
    void date(ActionEvent event) {
        grid.getChildren().clear();
        tried();
    }

    @FXML
    void addevent(ActionEvent event) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();
        if (tf_nom.getText().isEmpty() || tf_desc.getText().isEmpty() ||tf_lieu.getText().isEmpty() ||tf_date.getValue().isBefore(currentDate)||tf_date.getValue()==null) {
            // Afficher un message d'alerte
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Champs manquants");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez remplir tous les champs !");
            alert.showAndWait();
            return;
        }
        String description = tf_desc.getText();
        String nom = tf_nom.getText();
        String lieu = tf_lieu.getText();
        String date = String.valueOf(tf_date.getValue());
        Event p = new Event(nom,description,"default",lieu,date,Double.parseDouble(tf_lan.getText()),Double.parseDouble(tf_lon.getText()));
        es.ajouterEntite(p);
        tf_nom.clear();
        tf_desc.clear();
        tf_lieu.clear();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Valider");
        alert.setHeaderText(null);
        alert.setContentText("Evenement ajouter !");
        alert.showAndWait();

        pn_eventlist.toFront();
    }

    @FXML
    public void initialize() {
        grid.getChildren().clear();
        displayg();
    }

    @FXML
    void refresh(ActionEvent event) {
        grid.getChildren().clear();
        displayg();
    }



    private void displayg() {
        ///////////////////////////////////////////////////////////////
        ObservableList<Event> l2 = FXCollections.observableArrayList();
        ResultSet resultSet2 = es.Getall();
        l2.clear();
        Event pppp = new Event();
        l2.add(pppp);
        int column = 0;
        int row = 2;
        if (l2.size() > 0) {

        }
        try {
            while (resultSet2.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    EventC itemController = fxmlLoader.getController();
                    int id=resultSet2.getInt("id");
                    String nom =resultSet2.getString("nom");
                    String decsription=resultSet2.getString("description");
                    String lieu=resultSet2.getString("lieu");
                    String date=resultSet2.getString("date");
                    double lat=resultSet2.getDouble("lat");
                    double lon=resultSet2.getDouble("long");
                    Event ppppp = new Event(id,nom,decsription,"",date,lieu,lat,lon);
                    itemController.setData(ppppp);
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
                } catch (IOException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void triet() {
        ///////////////////////////////////////////////////////////////
        ObservableList<Event> l2 = FXCollections.observableArrayList();
        ResultSet resultSet2 = es.Sortt();
        l2.clear();
        Event pppp = new Event();
        l2.add(pppp);
        int column = 0;
        int row = 2;
        if (l2.size() > 0) {

        }
        try {
            while (resultSet2.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    EventC itemController = fxmlLoader.getController();
                    int id=resultSet2.getInt("id");
                    String nom =resultSet2.getString("nom");
                    String decsription=resultSet2.getString("description");
                    String lieu=resultSet2.getString("lieu");
                    String date=resultSet2.getString("date");
                    double lat=resultSet2.getDouble("lat");
                    double lon=resultSet2.getDouble("long");
                    Event ppppp = new Event(id,nom,decsription,"",date,lieu,lat,lon);
                    itemController.setData(ppppp);
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
                } catch (IOException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void tried() {
        ///////////////////////////////////////////////////////////////
        ObservableList<Event> l2 = FXCollections.observableArrayList();
        ResultSet resultSet2 = es.Sortd();
        l2.clear();
        Event pppp = new Event();
        l2.add(pppp);
        int column = 0;
        int row = 2;
        if (l2.size() > 0) {

        }
        try {
            while (resultSet2.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/Event.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    EventC itemController = fxmlLoader.getController();
                    int id=resultSet2.getInt("id");
                    String nom =resultSet2.getString("nom");
                    String decsription=resultSet2.getString("description");
                    String lieu=resultSet2.getString("lieu");
                    String date=resultSet2.getString("date");
                    double lat=resultSet2.getDouble("lat");
                    double lon=resultSet2.getDouble("long");
                    Event ppppp = new Event(id,nom,decsription,"",date,lieu,lat,lon);
                    itemController.setData(ppppp);
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
                } catch (IOException ex) {
                    Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}



