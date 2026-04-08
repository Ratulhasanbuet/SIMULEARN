package com.example.simulearn;

import com.example.simulearn.Information.DatabaseHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.simulearn.Information.DatabaseHelper.DB_PATH;

public class chatServer {


    private static Map<String, PrintWriter> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws Exception {
        DatabaseHelper.createMessagesTable();
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


                username = in.readLine();


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

                        System.out.println("=== SERVER RECEIVED SEND ===");
                        System.out.println("sender: [" + sender + "]");
                        System.out.println("receiver: [" + receiver + "]");
                        System.out.println("text: [" + text + "]");

                        DatabaseHelper.saveMessage(sender, receiver, text);

                        System.out.println("=== CHECKING DB CONTENT ===");
                        try (var conn = DatabaseHelper.getConnection();
                             var stmt = conn.createStatement();
                             var rs = stmt.executeQuery("SELECT sender, receiver, text FROM messages")) {

                            while (rs.next()) {
                                System.out.println(
                                        "[" + rs.getString("sender") + "] -> [" +
                                                rs.getString("receiver") + "] : " +
                                                rs.getString("text")
                                );
                            }
                        }

                        System.out.println("=== END DB CHECK ===");

                        PrintWriter receiverOut = clients.get(receiver);
                        if (receiverOut != null) {
                            receiverOut.println("MSG|" + sender + "|" + text);
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                if (username != null) {
                    clients.remove(username);
                    broadcastUserList();
                    System.out.println(username + " left the chat.");
                }
                try {
                    socket.close();
                } catch (Exception ignored) {
                }
            }
        }


        private void broadcastUserList() {
            String userList = String.join(",", clients.keySet());
            for (PrintWriter pw : clients.values()) {
                pw.println("USERLIST|" + userList);
            }
        }
    }
}