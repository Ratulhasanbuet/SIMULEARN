package com.example.simulearn;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MenuController {

    @FXML
    private HBox cardHBox;
    @FXML
    public void initialize() {
        for (Node node : cardHBox.getChildren()) {
            if (node instanceof Button btn) {
                // Optionally scale on hover
                btn.setOnMouseEntered(e -> btn.setScaleX(1.05));
                btn.setOnMouseEntered(e -> btn.setScaleY(1.05));
                btn.setOnMouseExited(e -> {
                    btn.setScaleX(1);
                    btn.setScaleY(1);
                });
            }
        }

    }

    @FXML
    private void biology(ActionEvent event) {

        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/biologyMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");

        }
    }

    @FXML
    private void chemistry(ActionEvent event) {

        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/chemistryMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    private void math(ActionEvent event) {

        try {
            // Load FXML correctly
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/mathMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");

        }
    }

    @FXML
    private void physics(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/physicsMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }


}



