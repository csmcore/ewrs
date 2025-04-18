/**
 * Created BY :K Priyamabda
	Created On :01-Feb-2022
 */
package app.ewarehouse.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;


/**
 * @author priyambadap
 *
 */
public class ComplainTokenGenerator {
	
	private ComplainTokenGenerator() {}
	
	private static final Logger logger = LoggerFactory.getLogger(ComplainTokenGenerator.class);
	public static String getRandomNumberString() {
		
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd;
		int number = 0;
		try {
			rnd = SecureRandom.getInstanceStrong();
			 number = rnd.nextInt(999999);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("error ComplainTokenGenerator :: getRandomNumberString() !!", e);
		}
		// this will convert any number sequence into 6 character.
		return String.format("%08d", number + new Date().getTime() / 1000);
		
	}
	
	
public static String getOtpNumberString() {
		
		// It will generate 6 digit random Number.
		// from 0 to 999999
		Random rnd;
		int number = 0;
		try {
			rnd = SecureRandom.getInstanceStrong();
			 number = rnd.nextInt(999999);
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("error ComplainTokenGenerator :: getRandomNumberString() !!", e);
		}
		// this will convert any number sequence into 6 character.
		return ""+number;
		
	}
	
}
