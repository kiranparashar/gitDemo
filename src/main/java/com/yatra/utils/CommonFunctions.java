package com.yatra.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommonFunctions {

	public static String getControllerServer() {
		try {
			String propValue = System.getenv("ControllerServer");
			if (propValue != null)
				return propValue;
			else 
				return ReadProperties.readProperty("ControllerServer");

		} catch (Exception e) {
			Log.exception(e);
		}
		return null;
	}
	
	

	public static String getPaymentServer() {
		String propValue = System.getenv("PaymentServer");
		if (propValue != null)
			return propValue;
		else
			return ReadProperties.readProperty("PaymentServer");
	}

	public static HashMap<String, String> logTestData(HashMap<String, String> testdata) {
		HashMap<String, String> hashMap = new LinkedHashMap<String, String>(testdata);

		if (testdata.get("TripType").equalsIgnoreCase("ONEWAY")) {
			hashMap.put("DepartureDate",
					DateGenerator.generateDateAfterDays(Integer.parseInt(testdata.get("DepartureDate"))));
			hashMap.remove("ReturnDate");
		} else {
			hashMap.put("DepartureDate",
					DateGenerator.generateDateAfterDays(Integer.parseInt(testdata.get("DepartureDate"))));
			hashMap.put("ReturnDate",
					DateGenerator.generateDateAfterDays(Integer.parseInt(testdata.get("ReturnDate"))));

		}

		return hashMap;
	}

	public static void renameLogFile() throws FileNotFoundException {
		try {
			String logPath = System.getProperty("user.dir") + File.separator + "logs/";

			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
			Calendar cal = Calendar.getInstance();

			String sReportDate = dateFormat.format(cal.getTime()).replaceAll(":", "");

			File dir = new File(logPath);
			File[] files = dir.listFiles();
			if (files == null || files.length == 0) {
				System.out.println("No Log file exist on given path: " + logPath);
			} else {
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().contains("CurrentTestLogs.log")) {
						File oldfile = new File(logPath + "CurrentTestLogs.log");
						Thread.sleep(500);
						File newfile = new File(logPath + "logs_" + sReportDate + ".log");
						Thread.sleep(500);
						oldfile.renameTo(newfile);
					}
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
