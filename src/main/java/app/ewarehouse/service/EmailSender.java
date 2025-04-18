package app.ewarehouse.service;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {

	
	 public static void main() {
	        // SMTP configuration
	        final String smtpHost = "email.csmpl.com";
	        final int smtpPort = 587;
	        final String username = "raj.sender@csmpl.com";
	        final String password = "Csmpl@3105";

	        // Email details
	        String from = username;
	        //String to = "anand.kumar@csm.tech";// Replace with the recipient's email address
	        String to = "warehouse@csmpl.com";
	        String subject = "Test Email";
	        String body = "This is a test email sent from Java using SMTP.";

	        // SMTP server properties
	        Properties properties = new Properties();
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", smtpHost);
	        properties.put("mail.smtp.port", smtpPort);

	        // Create a session with authentication
	        Session session = Session.getInstance(properties, new Authenticator() {
	            @Override
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(username, password);
	            }
	        });

	        try {
	            // Create a message
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(from));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	            message.setSubject(subject);
	            message.setText(body);

	            // Send the message
	            Transport.send(message);

	            System.out.println("Email sent successfully!");
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    }
}
