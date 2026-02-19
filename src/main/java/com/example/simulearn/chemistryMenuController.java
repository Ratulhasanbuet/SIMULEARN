package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class chemistryMenuController {
    @FXML
    public void initialize() {

    }

    @FXML

    public void onRealMoleculeButtonClicked(ActionEvent event) throws IOException {
        // Load FXML correctly
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/realMolecule.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1920, 1080);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
