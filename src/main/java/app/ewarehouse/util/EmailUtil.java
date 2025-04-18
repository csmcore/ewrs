package app.ewarehouse.util;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import app.ewarehouse.dto.Mail;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;



public  class EmailUtil {
	
	public static final String JAVAX_NET_SSL_SSL_SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
	public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_SMTP_PORT2 = "mail.smtp.port";
	public static final String MAIL_SMTP_SOCKET_FACTORY_CLASS = "mail.smtp.socketFactory.class";
	public static final String MAIL_SMTP_SOCKET_FACTORY_PORT2 = "mail.smtp.socketFactory.port";
	public static final String MAIL_SMTP_SSL_CHECKSERVERIDENTITY = "mail.smtp.ssl.checkserveridentity";
	public static final String HOST = "smtp.gmail.com";
	public static final String MAIL_SMTP_PORT = "465";
	public static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "465";
//	public static final String USERNAME = "uiidptestmail@gmail.com";
//	public static final String CREDE = "ggibwtywofsxjwin";
//	public static final String FROM = "uiidptestmail@gmail.com";
	
	public static final String USERNAME = "ewrsinfo@gmail.com";
	public static final String CREDE = "rfecgkalleyyqbyl";
	public static final String FROM = "ewrsinfo@gmail.com";
	
//	public static final String HOST = "email.csmpl.com";
//	public static final String MAIL_SMTP_PORT = "587";
//	public static final String MAIL_SMTP_SOCKET_FACTORY_PORT = "587";
//	public static final String USERNAME = "raj.sender@csmpl.com";
//	public static final String CREDE = "Csmpl@3105";
//	public static final String FROM = "raj.sender@csmpl.com";
	static Logger log=LoggerFactory.getLogger(EmailUtil.class);
	
	
	public static void sendMail(Mail mail) {
		
		Properties prop = new Properties();
		
		prop.put(MAIL_SMTP_HOST, HOST);
		prop.put(MAIL_SMTP_AUTH, "true");
		prop.put(MAIL_SMTP_PORT2, MAIL_SMTP_PORT);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT2, MAIL_SMTP_PORT);
		prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
		prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY, true);
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
		
		
		Session session = Session.getInstance(prop, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USERNAME, CREDE);
			}
		});
		
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.getMailTo()));
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(mail.getMailCc()));
			message.setSubject(mail.getMailSubject());
			message.setContent(mail.getTemplate(), "text/html");

			Transport.send(message);

			

		} catch (MessagingException e) {
			
		}
	}
	


	public  static Boolean  sendMail(String subject,String text,String toMail) {

	
		
    Thread emailthread=null;
		
	Properties prop = new Properties();
		 prop.put(MAIL_SMTP_HOST, HOST);
	     prop.put(MAIL_SMTP_PORT2, MAIL_SMTP_PORT);
	     prop.put(MAIL_SMTP_AUTH, "true");
	     prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT2, MAIL_SMTP_SOCKET_FACTORY_PORT);
	     prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
	     prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY, true);
	     prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
	     //Enabled (TLS / AUTO / STARTTLS)
	     Session session = Session.getInstance(prop,
	             new Authenticator() {
	                 @Override
	                 protected PasswordAuthentication getPasswordAuthentication() {
	                     return new PasswordAuthentication(USERNAME, CREDE);
	                 }
	             });

	     try {
	    	 
	    	  emailthread=new Thread(()->{
	    		 
	    		 try {
		    		 Message message = new MimeMessage(session);
			         message.setFrom(new InternetAddress(FROM));
			         message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toMail));
			         message.setSubject(subject);
			         message.setContent(text, "text/html");
			         Transport.send(message);
			        
		    	 }
		    	 catch(Exception e) {
		    		 log.info("Inside Email Util class Exception");		    		
		    	 }
	    		 
	    	 });
	    	  
	    	 emailthread.start();
	    	// return "Mail send successfull To "+toMail+ "\n\n\n if you don't find mail in inbox then kindly check your junk or spam files.";

	    	 return true;
	         

	     } catch (Exception e) {
	    	 log.info("Inside Email Util class Exception");
	    	 return false;
	     }
	 }
	
	
	
public static void  sendME() {


     Properties prop = new Properties();
	 prop.put(MAIL_SMTP_HOST, HOST);
     prop.put(MAIL_SMTP_PORT2, "465");
     prop.put(MAIL_SMTP_AUTH, "true");
     prop.put(MAIL_SMTP_SOCKET_FACTORY_PORT2, "465");
     prop.put(MAIL_SMTP_SOCKET_FACTORY_CLASS, JAVAX_NET_SSL_SSL_SOCKET_FACTORY);
	 prop.put(MAIL_SMTP_SSL_CHECKSERVERIDENTITY,true); //to resolve the sonarqube vulnerability **gurpreet
     Session session = Session.getInstance(prop,
             new Authenticator() {
                 @Override
                 protected PasswordAuthentication getPasswordAuthentication() {
                     return new PasswordAuthentication(USERNAME, CREDE);
                 }
             });

     try {

         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("from@gmail.com"));
         message.setRecipients(
                 Message.RecipientType.TO,
                 InternetAddress.parse("haraprasad.moharana@csm.tech")
         );
         message.setSubject("Testing Gmail SSL");
         message.setText("Dear Mail Crawler,"
                 + "\n\n Please do not spam my email!");

         Transport.send(message);

     } catch (MessagingException e) {
    	 
     }
 }


	public String buildString(int n) {
		String result = "";
		for (int i = 0; i < n; i++) {
			result += i; // Creates new String objects each time
		}
		return result;
	}
	public List<String> getList() {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			list.add(new String("Item")); // Unnecessary object creation
		}
		return list;
	}





}