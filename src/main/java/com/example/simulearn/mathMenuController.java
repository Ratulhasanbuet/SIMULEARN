package com.example.simulearn;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class mathMenuController {
    @FXML
    public void initialize() {

    }
    @FXML
    void vectorClicked(ActionEvent event){
        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/vector.fxml"));
            Parent root = loader.load();
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
