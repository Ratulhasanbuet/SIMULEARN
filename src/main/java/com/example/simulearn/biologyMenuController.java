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
            Scene scene = new Scene(root , 1920,1080);
            scene.getStylesheets().add(
                    getClass().getResource("biologyMenu.css").toExternalForm()
            );
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");

        }
    }

}
