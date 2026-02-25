package com.example.simulearn;

import java.io.*;
import java.util.Properties;

/**
 * Handles the current login session and persists the
 * "Remember Me" token to a local file so the user stays
 * logged in between app restarts.
 */
public class SessionManager {

    private static final String TOKEN_FILE =
        System.getProperty("user.home") + File.separator +
        ".simulearn" + File.separator + "remember.properties";

    // ── In-memory current user ────────────────────────────
    private static String currentUser = null;

    public static void setCurrentUser(String username) { currentUser = username; }
    public static String getCurrentUser()              { return currentUser; }
    public static boolean isLoggedIn()                 { return currentUser != null; }

    public static void logout() {
        String token = loadToken();
        if (token != null) {
            DatabaseHelper.deleteRememberToken(token);
            clearToken();
        }
        currentUser = null;
    }

    // ── Remember-Me token persistence ─────────────────────
    public static void saveToken(String token) {
        try {
            File f = new File(TOKEN_FILE);
            f.getParentFile().mkdirs();
            Properties props = new Properties();
            props.setProperty("token", token);
            try (FileOutputStream fos = new FileOutputStream(f)) {
                props.store(fos, null);
            }
        } catch (IOException e) {
            System.out.println("Could not save token: " + e.getMessage());
        }
    }

    public static String loadToken() {
        File f = new File(TOKEN_FILE);
        if (!f.exists()) return null;
        try (FileInputStream fis = new FileInputStream(f)) {
            Properties props = new Properties();
            props.load(fis);
            return props.getProperty("token");
        } catch (IOException e) { return null; }
    }

    public static void clearToken() {
        new File(TOKEN_FILE).delete();
    }

    /**
     * Call this at app startup — auto-logs in if a valid
     * remember-me token exists on disk.
     * Returns the username if auto-login succeeded, else null.
     */
    public static String tryAutoLogin() {
        String token = loadToken();
        if (token == null) return null;
        String username = DatabaseHelper.getUserFromToken(token);
        if (username != null) {
            setCurrentUser(username);
        } else {
            clearToken(); // stale token
        }
        return username;
    }

    /** Generate a simple unique token */
    public static String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
