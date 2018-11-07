package com.example.team19.kindlr;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Created by Joshua on 11/4/18.
 */

public class EmailNotifier {
    private static String USER_NAME = "chaowang310";  // GMail user name (just the part before "@gmail.com")
    private static String PASSWORD = "310sucks"; // GMail password
    private static String RECIPIENT;

//    public static void main(String[] args) {
//        String from = USER_NAME;
//        String pass = PASSWORD;
//        RECIPIENT = "joshualh@usc.edu";
//        String[] to = { RECIPIENT }; // list of recipient email addresses
//        String subject = "Java send mail example";
//        String body = "Welcome to JavaMail!";
//
//        sendFromGMail(subject, body);
//        System.out.println("sent");
//    }

    public EmailNotifier(String recipient)
    {
        RECIPIENT = recipient;
    }

    public void sendFromGMail(String subject, String body) {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = { RECIPIENT };
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (AddressException ae) {
            ae.printStackTrace();
        }
        catch (MessagingException me) {
            me.printStackTrace();
        }
    }

}

