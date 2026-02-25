package com.example.simulearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML private TextField     usernameField;
    @FXML private TextField     emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private CheckBox      rememberMe;
    @FXML private Label         messageLabel;

    @FXML
    void handleSignUp(ActionEvent event) {
        String username  = usernameField.getText().trim();
        String email     = emailField.getText().trim();
        String password  = passwordField.getText();
        String confirm   = confirmPasswordField.getText();

        // ── Validation ────────────────────────────────────
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }
        if (username.length() < 3) {
            showError("Username must be at least 3 characters.");
            return;
        }
        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return;
        }
        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }
        if (!password.equals(confirm)) {
            showError("Passwords do not match.");
            return;
        }
        if (DatabaseHelper.usernameExists(username)) {
            showError("Username already taken. Please choose another.");
            return;
        }
        if (DatabaseHelper.emailExists(email)) {
            showError("An account with this email already exists.");
            return;
        }

        // ── Register ──────────────────────────────────────
        boolean success = DatabaseHelper.registerUser(username, email, password);

        if (!success) {
            showError("Registration failed. Please try again.");
            return;
        }

        // ── Auto-login after sign up ──────────────────────
        SessionManager.setCurrentUser(username);

        if (rememberMe.isSelected()) {
            String token = SessionManager.generateToken();
            DatabaseHelper.saveRememberToken(username, token);
            SessionManager.saveToken(token);
        }

        goToHome(event);
    }

    @FXML
    void goToLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            showError("Could not open Login page.");
        }
    }

    @FXML
    void goHome(ActionEvent event) {
        goToHome(event);
    }

    // ── Helpers ───────────────────────────────────────────
    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #f87171;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #4ade80;");
        messageLabel.setText(msg);
    }

    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Menu.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Failed to go home: " + e.getMessage());
        }
    }
}
