package com.example.simulearn;

import com.example.simulearn.Information.DatabaseHelper;
import com.example.simulearn.Information.SessionManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

import static com.example.simulearn.Information.DatabaseHelper.getAllUsernames;

public class chatController {

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane chatScrollPane;

    @FXML
    private TextArea messageField;

    @FXML
    private ListView<String> userList;

    @FXML
    private Button sendButton;

    private String currentChatUser;

    @FXML
    public void initialize() {
        String user = SessionManager.tryAutoLogin();

        //if (user != null) {
            userList.getItems().clear();
            userList.getItems().addAll(getAllUsernames());
        //}
        // Auto-select first user if list is not empty
        if (!userList.getItems().isEmpty()) {
            userList.getSelectionModel().selectFirst();
            currentChatUser = userList.getItems().get(0);
        }

        // Disable send button if no user is selected
        sendButton.setDisable(currentChatUser == null);

        // Listen for selection changes
        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentChatUser = newVal;
            sendButton.setDisable(newVal == null);
            chatBox.getChildren().clear(); // optional: clear previous chat
        });

        // Start listening for incoming messages
        startListening();
    }

    private void startListening() {
        new Thread(() -> {
            try {
                String msg;
                while ((msg = Session.client.in.readLine()) != null) {
                    String[] parts = msg.split("\\|", 3);
                    if (parts[0].equals("MSG")) {
                        String sender = parts[1];
                        String text = parts[2];

                        Platform.runLater(() -> receiveMessage(sender, text));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void receiveMessage(String sender, String text) {
        // Only display message if it's for the currently selected chat
        if (sender.equals(currentChatUser)) {
            addMessage(sender, text, false);
        } else {
            // Optional: highlight user in list to indicate unread message
        }
    }

    private void addMessage(String sender, String text, boolean isMine) {
        Label label = new Label(sender+": \n"+text);
        label.setWrapText(true);
        label.setStyle("-fx-padding:8; -fx-background-radius:10;" +
                (isMine ? "-fx-background-color:#d6eaf8;" : "-fx-background-color:#f1f2f6;"));

        VBox container = new VBox(label);
        container.setStyle("-fx-alignment: " + (isMine ? "CENTER-RIGHT" : "CENTER-LEFT") + ";");

        chatBox.getChildren().add(container);

        // Scroll to bottom
        chatScrollPane.layout();
        chatScrollPane.setVvalue(1.0);
    }

    @FXML
    private void onBackClicked(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/simulearn/Menu.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    private void onAttachClicked() {
        // Implement file attachment later
    }

    @FXML
    private void onSendClicked() {
        if (currentChatUser == null) return; // no user selected

        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        // Send message via client
        if (Session.client != null) {
            Session.client.sendMessage(currentChatUser, text);
        }

        // Display in UI
        addMessage("Me", text, true);

        messageField.clear();
    }
}
