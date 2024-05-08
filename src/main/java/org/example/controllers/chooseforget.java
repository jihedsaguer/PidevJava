package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.service.UserService;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;
import java.util.Properties;
import java.util.Random;

public class chooseforget {

    private int code;
    UserService us = new UserService();
    private String mail;
    @FXML
    private Button btnenvoyer;

    @FXML
    private TextField tf_email;

    @FXML
    void forgetemail(ActionEvent event) {
        if(Objects.equals(btnenvoyer.getText(), "Suivant"))
        {
            if(Integer.parseInt(tf_email.getText())==this.code)
            {
                tf_email.setText("");
                tf_email.setPromptText("Entrer le nouveau mdp");
                btnenvoyer.setText("Modifier");
            }
        } else if (Objects.equals(btnenvoyer.getText(), "Modifier")) {
            us.modifierMdp(this.mail , tf_email.getText() );
        } else {
// Get the email address from the text field
            String recipientEmail = tf_email.getText();

            // Generate a random verification code
            String verificationCode = generateVerificationCode();
            this.code =Integer.parseInt(verificationCode);
            // Email configuration
            String host = "smtp.gmail.com"; // Your SMTP server host
            String port = "587"; // Your SMTP server port
            String username = "jihedsaguer127@gmail.com"; // Your email address
            String password = "kmhsnptwphjvoacj"; // Your email password

            // Email content
            String subject = "Verification Code for Password Reset";
            String body = "Your verification code is: " + verificationCode;

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
                message.setFrom(new InternetAddress(username));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));

                // Set subject and body
                message.setSubject(subject);
                message.setText(body);

                // Send email
                Transport.send(message);

                System.out.println("Email sent successfully!");
                this.mail=tf_email.getText();
                tf_email.setText("");
                tf_email.setPromptText("Entrer le code");
                btnenvoyer.setText("Suivant");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

    private String generateVerificationCode() {
        // Generate a random 6-digit verification code
        Random random = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }
}
