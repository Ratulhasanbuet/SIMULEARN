package com.example.simulearn.Information;

import com.example.simulearn.Session;

import java.io.*;
import java.util.Properties;

public class SessionManager {

    private static final String TOKEN_FILE =
            System.getProperty("user.home") + File.separator +
                    ".simulearn" + File.separator + "remember.properties";

    private static String currentUser = null;

    public static void setCurrentUser(String username) {
        currentUser = username;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static void logout() {
        String token = loadToken();
        if (token != null) {
            DatabaseHelper.deleteRememberToken(token);
            clearToken();
        }
        currentUser = null;
    }

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
        } catch (IOException e) {
            return null;
        }
    }

    public static void clearToken() {
        new File(TOKEN_FILE).delete();
    }

    public static String tryAutoLogin() {

        String token = loadToken();
        if (token == null) return null;
        String username = DatabaseHelper.getUserFromToken(token);
        if (username != null) {
            setCurrentUser(username);
            Session.username = username;
        } else {
            clearToken();
        }
        return username;
    }

    public static String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}