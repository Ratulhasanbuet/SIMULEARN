package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;


public class physicsMenuController {
    @FXML

    public void initialize() {

    }
    @FXML
    void onBuoyancyClick(ActionEvent event)
    {
        try {
            URL fxmlLocation = getClass().getResource("com/example/simulearn/Buoyancy.fxml");
            System.out.println("FXML location: " + fxmlLocation);
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Buoyancy.fxml"));
            Parent root = loader.load();
            //System.out.println(getClass().getResource("/com/example/simulearn/Buoyancy.fxml"));

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root , 1920,1080);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();

        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");

        }
    }
}
