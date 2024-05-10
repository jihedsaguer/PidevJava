package edu.esprit.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String mailto,String subject,String msg){
        final String EMAIL="mohamed24025287@gmail.com";
        final String PASS="rhiy ggbe yrbs qobl";
        Properties properties=new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.port","587");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        properties.put("mail.smtp.ssl.trust","smtp.gmail.com");
        Session session=Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL,PASS);
            }
        });
        try{
            Message message=new MimeMessage(session);
            message.setFrom(new InternetAddress(EMAIL));
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(mailto));
            message.setSubject(subject);
            message.setText(msg);
            Transport.send(message);
            System.out.println("Done");
        }catch (MessagingException e){
            System.out.println(e.getMessage());
        }
    }
}