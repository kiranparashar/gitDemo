
package com.yatra.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateGenerator {

	public static String generateDateAfterDays(String dateFormat, int days){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		cal.add(Calendar.DATE, days);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	
	public static String generateDate(int days){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.YEAR, days);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	public static String generateDateAfetrMonths(int months){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.MONTH, months);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	public static String generateDateAfterYears(int years){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.DAY_OF_YEAR, years);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	public static String generateDateAfterDays(int days){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.DATE, days);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	public static String generateDate4(int days){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		cal.add(Calendar.ALL_STYLES, days);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	public static String getDateString(int days){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
		cal.add(Calendar.DATE, days);
        return simpleDateFormat.format(cal.getTime()).toString();
	}
	

}

