package org.george.hybridcolumnar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Input {
	
	public static Integer parseInt(String field) {
		Integer number;
		try {
			number = Integer.parseInt(field.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
	public static Long parseLong(String field) {
		Long number;
		try {
			number = Long.parseLong(field.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
	public static Float parseFloat(String field) {
		Float number;
		try {
			number = Float.parseFloat(field.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
	public static Double parseDouble(String field) {
		Double number;
		try {
			number = Double.parseDouble(field.trim());
		} catch (NumberFormatException e) {
			number = null;
		}
		return number;
	}
	
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
	
	public static Date parseDate(String dateField) {
		return parseDate("MM/dd/yyyy", dateField);
	}
	
	public static Comparable<?> parse(Datatype datatype, String field) {
		switch (datatype) {
			case INTEGER:
				return parseInt(field);
			case LONG:
				return parseLong(field);
			case FLOAT:
				return parseFloat(field);
			case DOUBLE:
				return parseDouble(field);
			case DATE:
				return parseDate(field);
			default:
				return field.trim();
		}
	}
	
}
