package com.example.simulearn.Information;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class VerificationController {

    @FXML
    private TextField codeField1;
    @FXML
    private TextField codeField2;
    @FXML
    private TextField codeField3;
    @FXML
    private TextField codeField4;
    @FXML
    private TextField codeField5;
    @FXML
    private TextField codeField6;

    @FXML
    private Label messageLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Button verifyButton;
    @FXML
    private Button resendButton;

    private String userEmail;
    private String username;
    private String pendingPassword;
    private boolean isSignUp = false;
    private long resendCooldownEnd = 0;

    public void initialize(String email, String username, boolean isSignUp) {
        this.userEmail = email;
        this.username = username;
        this.isSignUp = isSignUp;

        if (emailLabel != null) {
            emailLabel.setText("Verification code has been sent to: " + email);
        }
        setupCodeFields();
    }

    public void initializeSignUp(String email, String username, String password) {
        this.userEmail = email;
        this.username = username;
        this.pendingPassword = password;
        this.isSignUp = true;

        if (emailLabel != null) {
            emailLabel.setText("Verification code has been sent to: " + email);
        }
        setupCodeFields();
    }

    private void setupCodeFields() {
        TextField[] fields = {codeField1, codeField2, codeField3, codeField4, codeField5, codeField6};

        for (int i = 0; i < fields.length; i++) {
            final int index = i;
            fields[i].textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.length() > 1) {
                    fields[index].setText(newVal.substring(0, 1));
                }
                if (!newVal.matches("[0-9]*")) {
                    fields[index].setText(oldVal);
                }
                if (newVal.length() == 1 && index < fields.length - 1) {
                    fields[index + 1].requestFocus();
                }
            });
        }
    }

    @FXML
    void handleVerify(ActionEvent event) {
        String code = codeField1.getText() + codeField2.getText() + codeField3.getText() +
                codeField4.getText() + codeField5.getText() + codeField6.getText();

        if (code.length() != 6) {
            showError("Please enter all 6 digits of the verification code.");
            return;
        }

        if (!DatabaseHelper.verifyCode(userEmail, code)) {
            showError("Invalid or expired verification code. Please try again.");
            return;
        }

        if (isSignUp && pendingPassword != null) {

            boolean saved = DatabaseHelper.registerUser(username, userEmail, pendingPassword);
            if (!saved) {
                showError("Could not complete registration. Username or email may already exist.");
                return;
            }
        }

        SessionManager.setCurrentUser(username);
        showSuccess("Email verified successfully! Redirecting...");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
        goToHome(event);
    }

    @FXML
    void handleResend(ActionEvent event) {
        long currentTime = System.currentTimeMillis();

        if (currentTime < resendCooldownEnd) {
            long secondsLeft = (resendCooldownEnd - currentTime) / 1000;
            showError("Please wait " + secondsLeft + " seconds before resending.");
            return;
        }

        String newCode = EmailService.generateVerificationCode();
        boolean sent = EmailService.sendVerificationCode(userEmail, newCode);

        if (sent) {
            DatabaseHelper.saveVerificationCode(userEmail, newCode);
            resendCooldownEnd = currentTime + 60000;
            showSuccess("New verification code sent to " + userEmail);
            clearCodeFields();
        } else {
            showError("Failed to send verification code. Please check your email settings.");
        }
    }

    @FXML
    void handleBackToLogin(ActionEvent event) {
        try {
            String fxmlFile = isSignUp
                    ? "/com/example/simulearn/SignUp.fxml"
                    : "/com/example/simulearn/Login.fxml";
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (java.io.IOException e) {
            showError("Could not go back.");
        }
    }

    private void showError(String msg) {
        messageLabel.setStyle("-fx-text-fill: #f87171;");
        messageLabel.setText(msg);
    }

    private void showSuccess(String msg) {
        messageLabel.setStyle("-fx-text-fill: #4ade80;");
        messageLabel.setText(msg);
    }

    private void clearCodeFields() {
        codeField1.clear();
        codeField2.clear();
        codeField3.clear();
        codeField4.clear();
        codeField5.clear();
        codeField6.clear();
        codeField1.requestFocus();
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