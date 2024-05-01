package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.collections.FXCollections;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.util.stream.Collectors;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import org.example.entities.User;
import org.example.service.UserService;

public class Dashboard {
    @FXML
    private TextField searchField;
    @FXML
    private GridPane grid;
    @FXML
    private Pagination pagination;

    UserService us = new UserService();

    @FXML
    public void initialize() {
        grid.getChildren().clear();
        displayg("");
    }

    @FXML
    void refresh(ActionEvent event) {
        grid.getChildren().clear();
        displayg("");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Dashboard.fxml"));
        Parent adminRoot = loader.load();

        Scene adminScene = new Scene(adminRoot);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(adminScene);
        window.show();
    }

    private void displayg(String searchText) {
        ///////////////////////////////////////////////////////////////
        ObservableList<User> l2 = FXCollections.observableArrayList();
        ResultSet resultSet2;
        if (searchText.isEmpty()) {
            resultSet2 = us.Getall(); // Retrieve all users if search text is empty
        } else {
            resultSet2 = us.searchUsers(searchText); // Retrieve filtered users based on search text
        }
        l2.clear();
        User pppp = new User();
        l2.add(pppp);
        int column = 0;
        int row = 2;
        if (l2.size() > 0) {

        }
        try {
            while (resultSet2.next()) {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/User.fxml"));
                try {
                    AnchorPane anchorPane = fxmlLoader.load();
                    UserC itemController = fxmlLoader.getController();
                    int id=resultSet2.getInt("id");
                    String mail=resultSet2.getString("email");
                    String pass=resultSet2.getString("password");
                    String ln=resultSet2.getString("name");
                    String fn=resultSet2.getString("prenom");
                    int tel=resultSet2.getInt("tel");
                    int isbanned= resultSet2.getInt("is_banned");
                    User ppppp = new User(id,mail,"",pass,ln,fn,tel,isbanned);
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

    @FXML
    private void search() {
        String searchText = searchField.getText().toLowerCase(); // Convert to lowercase for case-insensitive search

        // Clear the grid before populating with search results
        grid.getChildren().clear();

        // Call displayg() with the search text
        displayg(searchText);


    }


}

