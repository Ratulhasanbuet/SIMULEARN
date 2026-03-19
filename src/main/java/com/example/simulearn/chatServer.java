package com.example.simulearn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class chatServer {

    // Thread-safe map of online clients
    private static Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Chat server started on port 5000...");

        while (true) {
            Socket socket = serverSocket.accept();
            new ClientHandler(socket).start();
        }
    }

    static class ClientHandler extends Thread {
        Socket socket;
        BufferedReader in;
        PrintWriter out;
        String username;

        ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                // 1️⃣ Read username from client
                username = in.readLine();

                // Optional: validate username in DB here
                if (username == null || username.isEmpty()) {
                    socket.close();
                    return;
                }

                clients.put(username, out);
                broadcastUserList();

                System.out.println(username + " joined the chat.");

                String msg;
                while ((msg = in.readLine()) != null) {

                    String[] parts = msg.split("\\|", 4);
                    if (parts[0].equals("SEND")) {
                        String sender = parts[1];
                        String receiver = parts[2];
                        String text = parts[3];

                        PrintWriter receiverOut = clients.get(receiver);
                        if (receiverOut != null) {
                            receiverOut.println("MSG|" + sender + "|" + text);
                        } else {
                            // Optionally store in DB for offline delivery
                            System.out.println("User " + receiver + " is offline. Message not sent.");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // Remove client on disconnect
                if (username != null) {
                    clients.remove(username);
                    broadcastUserList();
                    System.out.println(username + " left the chat.");
                }
                try {
                    socket.close();
                } catch (Exception ignored) {}
            }
        }

        // Broadcast current online users to all clients
        private void broadcastUserList() {
            String userList = String.join(",", clients.keySet());
            for (PrintWriter pw : clients.values()) {
                pw.println("USERLIST|" + userList);
            }
        }
    }
}
