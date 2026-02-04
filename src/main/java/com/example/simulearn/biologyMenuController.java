package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class biologyMenuController {


    @FXML
    public void initialize() {

    }
    @FXML
    void ligationbuttonclicked(ActionEvent event){
        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/ligation.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");

        }
    }

}
