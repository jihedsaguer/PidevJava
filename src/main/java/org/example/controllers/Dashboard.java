package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.net.URL;
import javafx.scene.control.ScrollPane;

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
    @FXML

    // Total number of users
    private int totalUsers = 50 ;/* Get the total number of users */;

    // Number of users per page
    private int usersPerPage = 5;

    UserService us = new UserService();

    @FXML
    public void initialize() {
        int totalPages = (int) Math.ceil((double) totalUsers / usersPerPage);

        // Set the number of pages for pagination
        pagination.setPageCount(totalPages);

        // Set up the event handler for pagination
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            changePage(newIndex.intValue());

        });
           pagination.setCurrentPageIndex(0);
        // Display the initial page
        changePage(0);
        grid.getChildren().clear();
        displayg("");
    }
    private void changePage(int pageIndex) {
        // Calculate the start and end index of users for the current page
        int startIndex = pageIndex * usersPerPage;
        int endIndex = Math.min(startIndex + usersPerPage, totalUsers);

        // Clear existing content from the GridPane
        grid.getChildren().clear();

        // Populate the GridPane with users from startIndex to endIndex
        displayUsers(startIndex, endIndex);
    }
    private void displayUsers(int startIndex, int endIndex) {
        // Clear the GridPane
        grid.getChildren().clear();

        // Retrieve users from the database based on the start and end index
        ResultSet resultSet = us.getUsersInRange(startIndex, endIndex);

        try {
            int row = 0;
            int column = 0;

            // Set padding and margin for user cards
            Insets cardInsets = new Insets(5);
            int cardSpacing = 10;

            // Loop through the result set and populate the GridPane with user data
            while (resultSet.next()) {
                // Load the UserCard.fxml and controller
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/User.fxml"));
                AnchorPane userCard = loader.load();
                UserC controller = loader.getController();

                // Populate user data
                User user = new User(
                        resultSet.getInt("id"),
                        resultSet.getString("email"),
                        resultSet.getString("roles"),
                        resultSet.getString("password"),
                        resultSet.getString("name"),
                        resultSet.getString("prenom"),
                        resultSet.getInt("tel"),

                        resultSet.getInt("is_banned")
                );

                System.out.println(user);

                // Set user data in the controller
                controller.setUser(user);



                // Set spacing between user cards

                // Add the user card to the GridPane
                grid.add(userCard, column++, row);

                // Increment row after adding user card
                if (column == 3) { // Adjust the column count as needed
                    column = 0;
                    row++;
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void refresh(ActionEvent event) {
        grid.getChildren().clear();
        displayg("");
    }

    @FXML
    void logout(ActionEvent event) throws IOException {
        System.out.println("logging out");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface.fxml"));
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

