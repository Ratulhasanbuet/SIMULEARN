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

public class LoginController {

    @FXML private TextField     usernameField;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox      rememberMe;
    @FXML private Label         errorLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String input    = usernameField.getText().trim();
        String password = passwordField.getText();

        // ── Basic validation ──────────────────────────────
        if (input.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        // ── Attempt login ─────────────────────────────────
        String username = DatabaseHelper.loginUser(input, password);

        if (username == null) {
            showError("Invalid username/email or password.");
            return;
        }

        // ── Success ───────────────────────────────────────
        SessionManager.setCurrentUser(username);

        if (rememberMe.isSelected()) {
            String token = SessionManager.generateToken();
            DatabaseHelper.saveRememberToken(username, token);
            SessionManager.saveToken(token);
        }

        goToHome(event);
    }

    @FXML
    void openSignUp(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/SignUp.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            showError("Could not open Sign Up page.");
        }
    }

    @FXML
    void goHome(ActionEvent event) {
        goToHome(event);
    }

    // ── Helpers ───────────────────────────────────────────
    private void showError(String msg) {
        errorLabel.setStyle("-fx-text-fill: #f87171;");
        errorLabel.setText(msg);
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
