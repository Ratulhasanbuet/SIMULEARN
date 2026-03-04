package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class physicsMenuController {

    @FXML
    private HBox loggedOutBox;
    @FXML
    private HBox loggedInBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label userAvatarLabel;

    @FXML
    public void initialize() {
        String user = SessionManager.getCurrentUser();
        if (user != null && !user.isEmpty()) {
            loggedOutBox.setVisible(false);
            loggedOutBox.setManaged(false);
            loggedInBox.setVisible(true);
            loggedInBox.setManaged(true);
            usernameLabel.setText(user);
            userAvatarLabel.setText(String.valueOf(user.charAt(0)).toUpperCase());
        } else {
            loggedOutBox.setVisible(true);
            loggedOutBox.setManaged(true);
            loggedInBox.setVisible(false);
            loggedInBox.setManaged(false);
        }
    }


    @FXML
    void onHomeButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to go home.");
        }
    }

    @FXML
    private void about(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/About.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open About.");
        }
    }

    @FXML
    private void login(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open Login.");
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        SessionManager.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Logout failed.");
        }
    }

    @FXML
    private void exit(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


    @FXML
    void onBuoyancyClick(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Buoyancy.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    void onLogicCircuitClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogicCircuit.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 1920, 1080));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    void onProjectileButtonClicked(ActionEvent event) throws java.io.IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Projectile.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1200, 700);
        scene.getStylesheets().add(getClass().getResource("/com/example/simulearn/Projectile.css").toExternalForm());
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        ProjectileController controller = loader.getController();
        controller.onSceneReady(scene, stage);
        stage.setTitle("🏀 Projectile Simulator");
        stage.setMinWidth(900);
        stage.setMinHeight(600);
        stage.setScene(scene);
        stage.show();
    }
}