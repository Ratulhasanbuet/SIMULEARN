package com.example.simulearn;

import com.example.simulearn.Information.SessionManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class chatClient {

    private Socket socket;
    public BufferedReader in;
    private PrintWriter out;

    public chatClient() throws Exception {
        // Connect to server
        String serverIP = "192.168.0.105"; // Server's LAN IP
        Socket socket = new Socket(serverIP, 5000);
        //socket = new Socket("localhost", 5000);

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Send current username (from remember-me / session)
        out.println(SessionManager.getCurrentUser());

        new Thread(this::listenToServer).start();
    }

    // Send message to another user
    public void sendMessage(String receiver, String text) {
        out.println("SEND|" + SessionManager.getCurrentUser() + "|" + receiver + "|" + text);
    }

    // Listener to handle messages (optional backend processing)
    private void listenToServer() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                // Example: you can log messages or forward to controller
                if (line.startsWith("MSG|")) {
                    String[] parts = line.split("\\|", 3);
                    String sender = parts[1];
                    String text = parts[2];
                    System.out.println(sender + ": " + text);
                }
                // USERLIST messages can now be ignored here
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Disconnect client
    public void disconnect() {
        try {
            socket.close();
        } catch (Exception ignored) {}
    }
}