package org.example.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class chooseforget {
    @FXML
    void forgetemail(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forgetEmail.fxml"));
        Parent signInRoot = loader.load();
        Scene signInScene = new Scene(signInRoot);

        // Create a new stage for the forget password window
        Stage forgetPwdStage = new Stage();
        forgetPwdStage.initModality(Modality.APPLICATION_MODAL); // Set modality to APPLICATION_MODAL
        forgetPwdStage.setScene(signInScene);
        forgetPwdStage.showAndWait();

    }

    @FXML
    void forgetnum(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/forget.fxml"));
        Parent signInRoot = loader.load();
        Scene signInScene = new Scene(signInRoot);

        // Create a new stage for the forget password window
        Stage forgetPwdStage = new Stage();
        forgetPwdStage.initModality(Modality.APPLICATION_MODAL); // Set modality to APPLICATION_MODAL
        forgetPwdStage.setScene(signInScene);
        forgetPwdStage.showAndWait();

    }
}
