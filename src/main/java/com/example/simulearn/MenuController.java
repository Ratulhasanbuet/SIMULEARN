package com.example.simulearn;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MenuController {

    @FXML private HBox  cardHBox;
    @FXML private HBox  loggedOutBox;
    @FXML private HBox  loggedInBox;
    @FXML private Label usernameLabel;
    @FXML private Label userAvatarLabel;

    @FXML
    public void initialize() {

        // ── Show correct navbar state based on login ──────
        String user = SessionManager.getCurrentUser();

        if (user != null && !user.isEmpty()) {
            // Logged in — show user chip
            loggedOutBox.setVisible(false);
            loggedOutBox.setManaged(false);
            loggedInBox.setVisible(true);
            loggedInBox.setManaged(true);

            usernameLabel.setText(user);
            // First letter of username as avatar
            userAvatarLabel.setText(String.valueOf(user.charAt(0)).toUpperCase());
        } else {
            // Not logged in — show login button
            loggedOutBox.setVisible(true);
            loggedOutBox.setManaged(true);
            loggedInBox.setVisible(false);
            loggedInBox.setManaged(false);
        }

        // ── Card hover scale ──────────────────────────────
        for (Node node : cardHBox.getChildren()) {
            if (node instanceof Button btn) {
                btn.setOnMouseEntered(e -> {
                    btn.setScaleX(1.05);
                    btn.setScaleY(1.05);
                });
                btn.setOnMouseExited(e -> {
                    btn.setScaleX(1);
                    btn.setScaleY(1);
                });
            }
        }
    }

    // ── Logout ────────────────────────────────────────────
    @FXML
    private void logout(ActionEvent event) {
        SessionManager.logout();
        // Reload this page so navbar resets to logged-out
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

    // ── Login ─────────────────────────────────────────────
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
            System.out.println("Failed to open Login page.");
        }
    }

    // ── About ─────────────────────────────────────────────
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
            System.out.println("Failed to open About page.");
        }
    }

    // ── Subject pages ─────────────────────────────────────
    @FXML
    private void biology(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/biologyMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    private void chemistry(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/chemistryMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }

    @FXML
    private void math(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/mathMenu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
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
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to open the window.");
        }
    }
}
