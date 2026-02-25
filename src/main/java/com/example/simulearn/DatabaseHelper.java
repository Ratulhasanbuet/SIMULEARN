package com.example.simulearn;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.File;

public class DatabaseHelper {

    private static final String DB_DIR  = System.getProperty("user.home") + File.separator + ".simulearn";
    private static final String DB_PATH = DB_DIR + File.separator + "simulearn.db";
    private static final String URL     = "jdbc:sqlite:" + DB_PATH;

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

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createUsers);
            stmt.execute(createRemember);
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
        } catch (SQLException e) { return false; }
    }

    // ── Check if email already taken ─────────────────────
    public static boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email.trim().toLowerCase());
            return ps.executeQuery().next();
        } catch (SQLException e) { return false; }
    }

    // ── Save remember-me token ────────────────────────────
    public static void saveRememberToken(String username, String token) {
        String getUserId = "SELECT id FROM users WHERE username = ?";
        String insert    = "INSERT OR REPLACE INTO remember_tokens (user_id, token) VALUES (?, ?)";
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
}
