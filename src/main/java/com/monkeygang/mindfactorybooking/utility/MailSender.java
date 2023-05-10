package com.monkeygang.mindfactorybooking.utility;


import java.io.*;
import java.util.Properties;

import com.monkeygang.mindfactorybooking.BookingApplication;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


public class MailSender {
//Lukas er sej


    public static void sendTestMail() throws IOException {

        // Recipient's email
        String to = "lukhol01@easv365.dk";

        // Sender's email
        String from = "MindFactory@ecco.dk";

        String host = "smtp-relay.sendinblue.com";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.ssl.enable", "no");
        properties.put("mail.smtp.starttls.enable", "yes");
        properties.put("mail.smtp.auth", "true");

        File file = new File(BookingApplication.class.getResource("").getPath() + "SendinBluePassword");

        BufferedReader br = new BufferedReader(new FileReader(file));

        String password = br.readLine();


        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication("mindfactorybyecco.noreply@gmail.com", password);

            }

        });

        // Used to debug SMTP issues
        session.setDebug(true);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("MindFactory Booking report");

            // Now set the actual message
            message.setText("This is actual message");


            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("I sent this from my Java app!");

// Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            String filename = BookingApplication.class.getResource("calendar-view.fxml").getPath() + "HelloWorld.pdf";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);



            System.out.println("sending...");
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }

    }

}



