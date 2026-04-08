package com.example.simulearn;

import com.example.simulearn.Information.SessionManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.example.simulearn.Information.DatabaseHelper.DB_PATH;

public class chatClient {

    private Socket socket;
    public BufferedReader in;
    private PrintWriter out;

    public chatClient() throws Exception {
        String serverIP = "localhost";

        System.out.println("Connecting to server...");

        this.socket = new Socket(serverIP, 5000);

        System.out.println("Connected!");

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        out.println(SessionManager.getCurrentUser());

        new Thread(this::listenToServer).start();
    }

    public static void main(String[] args) {
        try {
            chatClient client = new chatClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String receiver, String text) {
        out.println("SEND|" + SessionManager.getCurrentUser() + "|" + receiver + "|" + text);
    }


    private void listenToServer() {
        try {
            String line;
            while ((line = in.readLine()) != null) {

                if (line.startsWith("MSG|")) {
                    String[] parts = line.split("\\|", 3);
                    String sender = parts[1];
                    String text = parts[2];
                    System.out.println(sender + ": " + text);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void disconnect() {
        try {
            socket.close();
        } catch (Exception ignored) {
        }
    }
}