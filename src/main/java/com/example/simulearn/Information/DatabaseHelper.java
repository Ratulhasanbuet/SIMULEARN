package com.example.simulearn.Information;

import com.example.simulearn.Session;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class DatabaseHelper {

    private static final String DB_DIR = System.getProperty("user.home") + File.separator + ".simulearn";
    public static final String DB_PATH = DB_DIR + File.separator + "simulearn.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    // ── Singleton connection ──────────────────────────────
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            // Make sure the folder exists
            new File(DB_DIR).mkdirs();
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(true);
        }
        return connection;
    }

    // ── Create tables on first run ────────────────────────
    public static void initialize() {
        createMessagesTable();
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    username   TEXT    NOT NULL UNIQUE,
                    email      TEXT    NOT NULL UNIQUE,
                    password   TEXT    NOT NULL,
                    created_at TEXT    DEFAULT (datetime('now'))
                );
                """;

        String createRemember = """
                CREATE TABLE IF NOT EXISTS remember_tokens (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    user_id    INTEGER NOT NULL,
                    token      TEXT    NOT NULL UNIQUE,
                    created_at TEXT    DEFAULT (datetime('now')),
                    FOREIGN KEY (user_id) REFERENCES users(id)
                );
                """;

        String createVerificationCodes = """
                CREATE TABLE IF NOT EXISTS verification_codes (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    email      TEXT    NOT NULL,
                    code       TEXT    NOT NULL,
                    created_at TEXT    DEFAULT (datetime('now')),
                    expires_at TEXT    NOT NULL
                );
                """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createRemember);
            stmt.execute(createVerificationCodes);
        } catch (SQLException e) {
            System.out.println("DB init error: " + e.getMessage());
        }
    }

    // ── Hash password with SHA-256 ────────────────────────
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    // ── Register new user ─────────────────────────────────
    public static boolean registerUser(String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            ps.setString(2, email.trim().toLowerCase());
            ps.setString(3, hashPassword(password));
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            // UNIQUE constraint → username or email already exists
            return false;
        }
    }

    // ── Validate login ────────────────────────────────────
    // Returns username on success, null on failure
    public static String loginUser(String usernameOrEmail, String password) {
        String sql = """
                SELECT username FROM users
                WHERE (username = ? OR email = ?) AND password = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usernameOrEmail.trim());
            ps.setString(2, usernameOrEmail.trim().toLowerCase());
            ps.setString(3, hashPassword(password));
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("username");
        } catch (SQLException e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }

    // ── Check if username already taken ──────────────────
    public static boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username.trim());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── Check if email already taken ─────────────────────
    public static boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── Get email from username ──────────────────────────
    public static String getUserEmail(String username) {
        String sql = "SELECT email FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("email");
        } catch (SQLException e) {
            System.out.println("Get email error: " + e.getMessage());
        }
        return null;
    }

    // ── Save remember-me token ────────────────────────────
    public static void saveRememberToken(String username, String token) {
        String getUserId = "SELECT id FROM users WHERE username = ?";
        String insert = "INSERT OR REPLACE INTO remember_tokens (user_id, token) VALUES (?, ?)";
        try (Connection conn = getConnection()) {
            PreparedStatement ps1 = conn.prepareStatement(getUserId);
            ps1.setString(1, username);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int userId = rs.getInt("id");
                PreparedStatement ps2 = conn.prepareStatement(insert);
                ps2.setInt(1, userId);
                ps2.setString(2, token);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Remember token error: " + e.getMessage());
        }
    }

    // ── Get username from remember token ──────────────────
    public static String getUserFromToken(String token) {
        String sql = """
                SELECT u.username FROM users u
                JOIN remember_tokens rt ON u.id = rt.user_id
                WHERE rt.token = ?
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("username");
        } catch (SQLException e) {
            System.out.println("Token lookup error: " + e.getMessage());
        }
        return null;
    }

    // ── Delete remember token (on logout) ─────────────────
    public static void deleteRememberToken(String token) {
        String sql = "DELETE FROM remember_tokens WHERE token = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, token);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Token delete error: " + e.getMessage());
        }
    }

    // ── Verification Code Management ──────────────────────
    public static void saveVerificationCode(String email, String code) {
        // Generate expiry time (10 minutes from now)
        String sql = """
                INSERT INTO verification_codes (email, code, expires_at)
                VALUES (?, ?, datetime('now', '+10 minutes'))
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Verification code save error: " + e.getMessage());
        }
    }

    // ── Verify code and delete it ────────────────────────
    public static boolean verifyCode(String email, String code) {
        String sql = """
                SELECT 1 FROM verification_codes
                WHERE email = ? AND code = ? AND datetime('now') < expires_at
                """;
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, code);
            if (ps.executeQuery().next()) {
                // Delete the used code
                deleteVerificationCode(email, code);
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Verification code check error: " + e.getMessage());
        }
        return false;
    }

    // ── Delete verification code ─────────────────────────
    private static void deleteVerificationCode(String email, String code) {
        String sql = "DELETE FROM verification_codes WHERE email = ? AND code = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            ps.setString(2, code);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Verification code delete error: " + e.getMessage());
        }
    }

    // ── Clean expired verification codes ──────────────────
    public static void cleanExpiredCodes() {
        String sql = "DELETE FROM verification_codes WHERE datetime('now') >= expires_at";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Clean expired codes error: " + e.getMessage());
        }
    }
    public static java.util.List<String> getAllUsernames() {
        java.util.List<String> usernames = new java.util.ArrayList<>();
        String sql = "SELECT username FROM users ORDER BY username ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("username");

                // Skip current logged-in user
                if (!username.equals(SessionManager.getCurrentUser())) {
                    usernames.add(username);
                }
            }

        } catch (SQLException e) {
            System.out.println("Get all usernames error: " + e.getMessage());
        }

        return usernames;
    }
    // In DatabaseHelper.java
    public static void createMessagesTable() {
        String sql = """
        CREATE TABLE IF NOT EXISTS messages (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            sender TEXT NOT NULL,
            receiver TEXT NOT NULL,
            text TEXT NOT NULL,
            timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
            is_read INTEGER DEFAULT 0
        )
    """;
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveMessage(String sender, String receiver, String text) {
        String sql = "INSERT INTO messages (sender, receiver, text) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.setString(3, text);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getMessagesBetween(String user1, String user2) {
        System.out.println("=== getMessagesBetween ===");
        System.out.println("user1: " + user1);
        System.out.println("user2: " + user2);

        String sql = """
        SELECT sender, text, timestamp FROM messages
        WHERE (sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?)
        ORDER BY timestamp ASC
    """;
        List<String[]> messages = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user1); ps.setString(2, user2);
            ps.setString(3, user2); ps.setString(4, user1);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(new String[]{rs.getString("sender"), rs.getString("text"), rs.getString("timestamp")});
            }
            System.out.println("rows returned: " + messages.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static void markMessagesAsRead(String sender, String receiver) {
        String sql = "UPDATE messages SET is_read = 1 WHERE sender = ? AND receiver = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sender);
            ps.setString(2, receiver);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getUnreadCount(String receiver, String sender) {
        String sql = "SELECT COUNT(*) FROM messages WHERE receiver = ? AND sender = ? AND is_read = 0";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, receiver);
            ps.setString(2, sender);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
