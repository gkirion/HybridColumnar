package org.george.hybridcolumnar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Input {

	public static Date parseDate(String dateFormat, String dateField) {
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		Date date;
		try {
			date = df.parse(dateField.trim());
		} catch (ParseException e) {
			date = null;
		}
		return date;
	}
	
	public static Integer parseInt(String intField) {
		Integer number;
		try {
			number = Integer.parseInt(intField.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
	public static Double parseDouble(String doubleField) {
		Double number;
		try {
			number = Double.parseDouble(doubleField.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
}
