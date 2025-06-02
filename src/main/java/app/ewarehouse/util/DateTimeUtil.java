package app.ewarehouse.util;

import java.util.Date;

public class DateTimeUtil {

	public static long timeDiff(Date date1,Date date2, String mode) {
		
		 long differenceInMilliSeconds=0;
		   // Calculating the difference in milliseconds 
	     //differenceInMilliSeconds = Math.abs(date2.getTime() - date1.getTime()); 
	     
	     differenceInMilliSeconds= date1.getTime()-date2.getTime() ;
		 if("Seconds".equalsIgnoreCase(mode)) {
			 // Calculating the difference in Seconds 
//		    long differenceInSeconds = (differenceInMilliSeconds / 1000) % 60; 
		    long differenceInSeconds = differenceInMilliSeconds / 1000;

		    return differenceInSeconds;
		}
		else if("Minutes".equalsIgnoreCase(mode)) {
			 // Calculating the difference in Minutes 
			long differenceInMinutes = differenceInMilliSeconds / (60 * 1000);
//		    long differenceInMinutes = (differenceInMilliSeconds / (60 * 1000)) % 60;
		    return differenceInMinutes;
		}
		else if("Hours".equalsIgnoreCase(mode)) {
			 // Calculating the difference in Hours 
		    long differenceInHours = (differenceInMilliSeconds / (60 * 60 * 1000)) % 24; 
		    return differenceInHours;
		}else if ("Days".equalsIgnoreCase(mode)) {
			 // Calculating the difference in Days
			 long differenceInDays = differenceInMilliSeconds / (24 * 60 * 60 * 1000); // 1 day = 24 hours * 60 minutes * 60 seconds * 1000 milliseconds
			 return differenceInDays;
		 }else if ("Years".equalsIgnoreCase(mode)) {
			 // Calculating the difference in Days
			 long differenceInYears = differenceInMilliSeconds / (365L*24 * 60 * 60 * 1000);
			 return differenceInYears;
		 }
		 else {
			 return differenceInMilliSeconds;
		}
		
		
	}
	
	

 

   

    

   
	
}
