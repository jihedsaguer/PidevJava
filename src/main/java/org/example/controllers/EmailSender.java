package org.example.controllers;

import javax.mail.*;
import javax.mail.internet.*;

import java.util.Properties;

public class EmailSender {
    public static void main(String[] args) {
        // Email configuration
        String host = "smtp.google.com"; // Your SMTP server host
        String port = "587"; // Your SMTP server port
        String username = "jihedsaguer127@gmail.com"; // Your email address
        String password = "nrqngbrnngpvcgmi"; // Your email password

        // Sender and recipient information
        String from = "jihedsaguer127@gmail.com";
        String to = "jihedsaguer127@gmail.com";

        // Email content
        String subject = "Test Email";
        String body = "This is a test email sent from Java.";

        // Set properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set sender and recipient
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set subject and body
            message.setSubject(subject);
            message.setText(body);

            // Send email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
