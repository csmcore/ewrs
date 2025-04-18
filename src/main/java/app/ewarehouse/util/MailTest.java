package app.ewarehouse.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

import app.ewarehouse.dto.Mail;

public class MailTest {

	public static void main(String[] args) {
		//String otpno = ComplainTokenGenerator.getOtpNumberString();

		 String otpmessage = "\n\n This is test Mail ";

		  Mail mail = new Mail(); mail.setMailSubject("Demo Mail.");
		  mail.setContentType("text/html");

		  //mail.setMailCc("uiidptestmail@gmail.com"); // CC mail id
		  mail.setTemplate(otpmessage); mail.setMailTo("gurpreet.sran1990@gmail.com");
		  EmailUtil.sendMail(mail);

		
	//	System.out.println(generateUniqueId(2));
		
		

	}
	
//	 public  static String generateUniqueId() {
//	        // Format: YYYYMMDDHHmmssSSS (Date + Time + Milliseconds)
//	        String dateTime = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//
//	        // Generate a 4-digit random number for extra uniqueness
//	        int randomNumber = ThreadLocalRandom.current().nextInt(1000, 10000);
//
//	        return dateTime + randomNumber;
//	    }
	 public static String generateUniqueId(int digits) {
	        // Get current date & time in YYYYMMDDHHmmss format
	        String dateTime = new SimpleDateFormat("yyyyMMdd").format(new Date());

	        // Generate a random number with the specified number of digits
	        int min = (int) Math.pow(10, digits - 1); // Minimum value (e.g., 100000 for 6 digits)
	        int max = (int) Math.pow(10, digits) - 1; // Maximum value (e.g., 999999 for 6 digits)
	        int randomNumber = ThreadLocalRandom.current().nextInt(min, max + 1);

	        // Combine date-time and random number
	        return dateTime + randomNumber;
	    }

}
