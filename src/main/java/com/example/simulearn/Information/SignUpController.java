package com.example.simulearn.Information;

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
        // ── Send verification code FIRST (before saving to DB) ───────────
        showSuccess("Sending verification code...");

        String verificationCode = EmailService.generateVerificationCode();
        boolean sent = EmailService.sendVerificationCode(email, verificationCode);

        if (!sent) {
            showError("Failed to send verification email. Please check your email address.");
            return;
        }

        // ── Save verification code in DB ──────────────────
        DatabaseHelper.saveVerificationCode(email, verificationCode);

        // ── Go to verification page, pass credentials for saving AFTER verify ─
        goToVerification(event, email, username, password);
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

    // Pass password too so VerificationController can save to DB after verify
    private void goToVerification(ActionEvent event, String email, String username, String password) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Verification.fxml"));
            Parent root = loader.load();
            VerificationController controller = loader.getController();
            controller.initializeSignUp(email, username, password);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            showError("Could not load verification page.");
        }
    }
}