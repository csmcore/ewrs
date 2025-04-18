package app.ewarehouse.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateTimeUtility {
	public static Date getDateAndTime(long timeStamp) {
		return new Date(timeStamp);
	}

	public static String getTodayDate() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(AdminPanelConstant.DATE_FORMAT);
		return formatter.format(date);
	}
	
	public static String getDate(SimpleDateFormat sdf, long millisecond) {
		Date date = new Date(millisecond);
		return sdf.format(date);
	}

	public static long getMilliSecond(String strDate, SimpleDateFormat sdf) {
		long millisecond = 0;
		try {
			Date date = sdf.parse(strDate);
			millisecond = date.getTime();
		} catch (ParseException e) {
			log.error("DateTimeUtility:getMilliSecond()",e);
		}
		return millisecond;
	}
	
	public static long convertLocalDateIntoMilliSecond(LocalDate date) {
		long millisecond = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
		return millisecond;
	}
	
	public static LocalDate convertMillisecondIntoLocalDate(long millisecond) {
		LocalDate date = Instant.ofEpochMilli(millisecond).atZone(ZoneId.systemDefault()).toLocalDate();
		return date;
	}
	
	
	
	public static String convertDateFormatYYYYToDD(String indate) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    String formateddate=LocalDate.parse(indate, formatter).format(formatter2);
		return formateddate;
	}
	
	
	
	
	public static LocalDate convertDateFormatDD_MM_YY(String inputDate) {

		LocalDate formattedDate=null;
	    try {
	         

	         DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Define date format
	         formattedDate = LocalDate.parse(inputDate, formatter);
	         
	          // Convert to Date
	         
	     } catch (Exception e) {
	         e.getMessage();
	     }
		return formattedDate;
	}
	
	
}
