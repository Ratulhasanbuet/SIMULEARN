package com.example.simulearn.Information;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;
import java.util.Random;

/**
 * EmailService handles sending verification codes to user emails.
 * Configure your email credentials in the config file or environment variables.
 */
public class EmailService {

    // Gmail configuration - you can modify these
    private static final String SENDER_EMAIL = "simulearn0@gmail.com";
    private static final String SENDER_PASSWORD = "vfzrpkrjlurovewa"; // spaces ছাড়া
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    /**
     * Send verification code to user email
     */
    public static boolean sendVerificationCode(String recipientEmail, String verificationCode) {
        try {
            // Setup mail properties
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.starttls.required", "true");
            props.put("mail.smtp.connectiontimeout", "5000");
            props.put("mail.smtp.timeout", "5000");

            // Create session with authentication
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });

            session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                }
            });
            session.setDebug(true); // <-- enables debug output

            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("SimuLearn Email Verification Code");

            // Create HTML email body
            String emailBody =
                    "<html>" +
                            "<head>" +
                            "    <style>" +
                            "        body { font-family: DM Sans Medium, sans-serif; color: #333; }" +
                            "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
                            "        .header { background-color: #2ecc71; color: white; padding: 20px; text-align: center; border-radius: 5px 5px 0 0; }" +
                            "        .content { background-color: #f9f9f9; padding: 20px; border: 1px solid #ddd; }" +
                            "        .code-box { background-color: #e8f5e9; padding: 15px; text-align: center; border-radius: 5px; margin: 20px 0; }" +
                            "        .code { font-size: 32px; font-weight: bold; color: #2ecc71; letter-spacing: 5px; }" +
                            "        .footer { background-color: #f0f0f0; padding: 10px; text-align: center; font-size: 12px; border-radius: 0 0 5px 5px; }" +
                            "    </style>" +
                            "</head>" +
                            "<body>" +
                            "    <div class=\"container\">" +
                            "        <div class=\"header\">" +
                            "            <h1>SimuLearn</h1>" +
                            "            <p>Email Verification</p>" +
                            "        </div>" +
                            "        <div class=\"content\">" +
                            "            <h2>Welcome to SimuLearn!</h2>" +
                            "            <p>Thank you for signing up. To complete your registration, please enter the verification code below:</p>" +
                            "            <div class=\"code-box\">" +
                            "                <div class=\"code\">" + verificationCode + "</div>" +
                            "            </div>" +
                            "            <p><strong>This code will expire in 10 minutes.</strong></p>" +
                            "            <p>If you did not request this code, please ignore this email.</p>" +
                            "        </div>" +
                            "        <div class=\"footer\">" +
                            "            <p>&copy; 2026 SimuLearn. All rights reserved.</p>" +
                            "        </div>" +
                            "    </div>" +
                            "</body>" +
                            "</html>";

            message.setContent(emailBody, "text/html; charset=utf-8");

            // Send email
            Transport.send(message);
            System.out.println("Verification email sent to: " + recipientEmail);
            return true;

        } catch (MessagingException e) {
            System.out.println("Failed to send verification email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Generate a random 6-digit verification code
     */
    public static String generateVerificationCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}