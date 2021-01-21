package com.yatra.utils;

import java.util.HashMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.testng.Assert;

public class Log {
	public static Logger logger;
	public static String className;

	public static Logger getLogger() {
		Logger logger = null;
		Class<?> testClass = null;

		String className = new Exception().getStackTrace()[2].getClassName();
		try {
			testClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		logger = Logger.getLogger(testClass);
		return logger;
	}

	public static void message(String message) {
		getLogger().log(Log.class.getName(), Level.INFO, message, null);
		ExtentReporterUtil.info(message);
	}

	public static void pass(String message) {
		getLogger().log(Log.class.getName(), Level.INFO, message, null);
		ExtentReporterUtil.pass(message);
	}
	
	public static void error(String message) {
		getLogger().log(Log.class.getCanonicalName(), Level.ERROR, message, null);
		ExtentReporterUtil.fail(message);
	}

	public static String callerClass() {
		return className = new Exception().getStackTrace()[2].getClassName();
	}

	public static void testCaseInfo(HashMap<String, String> testData) {
		getLogger().info("------------------Test: " + testData.get("TestDescription") + "--------------------");
		getLogger().info("TestData: "+testData);
		ExtentReporterUtil.info(testData.toString());
	}

	public static void endTestCase() {
		getLogger().info("----------------" + "END TEST" + "------------------");
	}

	public static void exception(Exception e) {
		String eMessage = e.getMessage();

		if (eMessage != null && eMessage.contains("\n")) {
			eMessage = eMessage.substring(0, eMessage.indexOf("\n"));
		}
		getLogger().log(callerClass(), Level.FATAL, eMessage, null);
		ExtentReporterUtil.fail(eMessage);
	}

	public static void assertThat(boolean status, String passmsg, String failmsg) {
		if (!status) {
			Log.fail(failmsg);
		} else {
			Log.message(passmsg);
		}
	}

	public static boolean assertError(boolean status, String passmsg, String failmsg){
		if (!status) {
			Log.error(failmsg);
		} else {
				Log.pass(passmsg);
		}
		return status;
	}
	
	public static void fail(String description) {
		getLogger().error(description);
		ExtentReporterUtil.fail(description);
		Assert.fail(description);
	}

}