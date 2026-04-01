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
        refreshUserListWithUnread();

        sendButton.setDisable(true);

        userList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            currentChatUser = newVal;
            sendButton.setDisable(newVal == null);

            if (newVal != null) {
                String cleanName = newVal.replaceAll("\\s*\\(\\d+\\)$", "").trim();
                loadChatHistory(cleanName);
                DatabaseHelper.markMessagesAsRead(cleanName, SessionManager.getCurrentUser());
                refreshUserListWithUnread();
            }
        });

        // Single selectFirst — the listener above handles loadChatHistory
        if (!userList.getItems().isEmpty()) {
            userList.getSelectionModel().selectFirst();
            currentChatUser = userList.getItems().get(0);
            sendButton.setDisable(false);
        }

        startListening();
    }
    private void loadChatHistory(String otherUser) {
        String me = SessionManager.getCurrentUser();
        System.out.println("=== loadChatHistory ===");
        System.out.println("me: " + me);
        System.out.println("otherUser: " + otherUser);

        List<String[]> history = DatabaseHelper.getMessagesBetween(me, otherUser);
        System.out.println("messages found: " + history.size());

        chatBox.getChildren().clear();
        for (String[] msg : history) {
            String sender = msg[0];
            String text = msg[1];
            boolean isMine = sender.equals(me);
            addMessage(isMine ? "Me" : sender, text, isMine);
        }
    }
    private void refreshUserListWithUnread() {
        String me = SessionManager.getCurrentUser();
        userList.getItems().clear();

        for (String username : getAllUsernames()) {
            int unread = DatabaseHelper.getUnreadCount(me, username);
            String display = unread > 0 ? username + " (" + unread + ")" : username;
            userList.getItems().add(display);
        }
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
        String activeChatUser = currentChatUser != null
                ? currentChatUser.replaceAll("\\s*\\(\\d+\\)$", "").trim()
                : null;

        if (sender.equals(activeChatUser)) {
            addMessage(sender, text, false);
        } else {
            // Refresh list to show new unread count
            Platform.runLater(this::refreshUserListWithUnread);
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
    private void onSendClicked() {
        if (currentChatUser == null) return;

        // Strip unread badge before sending
        String receiver = currentChatUser.replaceAll("\\s*\\(\\d+\\)$", "").trim();

        String text = messageField.getText().trim();
        if (text.isEmpty()) return;

        if (Session.client != null) {
            Session.client.sendMessage(receiver, text);
        }

        addMessage("Me", text, true);
        messageField.clear();
    }
}
