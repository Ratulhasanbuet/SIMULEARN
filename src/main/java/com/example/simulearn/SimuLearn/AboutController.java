package com.example.simulearn.SimuLearn;

import com.example.simulearn.Information.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AboutController {

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
    void goHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to go back to home.");
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
            System.out.println("Failed to open Login page.");
        }
    }

    @FXML
    private void about(ActionEvent event) {

    }

    @FXML
    private void exit(ActionEvent event) {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}