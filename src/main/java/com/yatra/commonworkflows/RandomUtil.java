
package com.yatra.commonworkflows;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.mifmif.common.regex.Generex;

public class RandomUtil {

	public static long generateRandom() throws InterruptedException{
		Thread.sleep(1000);
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		Date date = new Date();
		long randomNo =Long.parseLong(dateFormat.format(date));
		return randomNo;
	}
	public static String generateRandomEmailId() throws InterruptedException{
		return "yatraguest"+generateRandom()+"@gmail.com";
	}
	public static int generateRandomIndex(int count){
		 Random randomGenerator = new Random();
		 return randomGenerator.nextInt(count);
	}
	 public static String randomString()
	    {
	        String alphabet= "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        String randomString = "";
	        Random random = new Random();
	        int randomLen = 1+random.nextInt(5);
	        for (int i = 0; i < randomLen; i++) {
	            char c = alphabet.charAt(random.nextInt(26));
	            randomString+=c;
	        }
	        return randomString;
	    }
	 
	 public static String randomPassport(){
		 String regex= "[a-zA-Z]{2}[0-9]{7}";
		 Generex generex = new Generex(regex);
		 String randomStr = generex.random();
		 return randomStr;
	 }
	 public static String getTime(){
		 Date date = new Date();
		 return Long.toString(date.getTime());
	 }
	 
	 public static String getTimeHoursMinutes(int hrs)
	 {
		 int mins = LocalDateTime.now().getMinute();
		 int hours;
		 if(hrs!=0)
			 hours = LocalDateTime.now().plusHours(hrs).getHour();
		 else
			 hours = LocalDateTime.now().getHour();
		 if(mins<10){
			 String currentTime = hours +":"+ "0"+mins;
			 return currentTime;
		 }
		 else{
			 String currentTime = hours +":"+ mins;
			 return currentTime;
		 }
	 }
	 public static String getDate(int days)
	 {
		 int year, month, day;
		 String date = null;
		 if(days>=0)
		 {
			 year = LocalDateTime.now().plusDays(days).getYear();
			 month = LocalDateTime.now().plusDays(days).getMonthValue();
			 day = LocalDateTime.now().plusDays(days).getDayOfMonth();
			 date = day+"/"+month+"/"+year;
		 }
		 else
		 {
			 year = LocalDateTime.now().minusDays(days).getYear();
			 month = LocalDateTime.now().minusDays(days).getMonthValue();
			 day = LocalDateTime.now().minusDays(days).getDayOfMonth();
			 date = day+"/"+month+"/"+year;
		 }
		 return date;
		 
	 }
	 public static HashMap<String, String> createMapper(){
		 HashMap<String, String> mapper = new HashMap<String , String>();
			mapper.put("insurance", "INSURANCE");
			mapper.put("bus", "BUS");
			mapper.put("car", "CAR");
			mapper.put("dom-hotels", "DOMHTL");
			mapper.put("visa", "VISA");
			mapper.put("int-flights", "INTAIR");
			mapper.put("dom-flights", "DOMAIR");
			mapper.put("trains", "TRAIN");
			mapper.put("int-hotels", "INTHTL");
			return mapper;
	 }
	 public static String getTimeCategory(String time){
		 int t= Integer.parseInt(time.split(":")[0]);
		 String category = null;
		 if ( t< 12 )  
		 { 
			 category= "Morning"; 
		 } 
		 else  /* Hour is from noon to 5pm (actually to 5:59 pm) */
		 if ( t >= 12 && t <= 17 ) 
		 { 
			 category= "Afternoon"; 
		 } 
		 else  /* the hour is after 5pm, so it is between 6pm and midnight */
		 if (  t > 17 && t <= 24 ) 
		 { 
			 category= "Evening"; 
		 }
		 return category;
	 }
}

