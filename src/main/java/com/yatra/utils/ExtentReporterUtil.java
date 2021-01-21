package com.yatra.utils;

import java.io.File;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.testng.ITestResult;
import org.testng.SkipException;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ExtentReporterUtil {

	public static ExtentTest test = null;
	public static ExtentReports report = null;

	private static File configFile = new File(System.getProperty("user.dir") + File.separator + "src/main/resources/ReportConfig.xml");

	public static ExtentTest startTest(String testName, String description) throws FileNotFoundException {

		try {
			if (report == null) {
				File reportFilePath = new File(
						System.getProperty("user.dir") + File.separator + "test-output/AutomationReport.html");
				report = new ExtentReports(reportFilePath.toString(), true);
				if (configFile.exists()) {
					report.loadConfig(configFile);
				}
			}

			if (test == null) {
				test = report.startTest(testName);
				test.setDescription(description);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return test;
	}
	
	public static  void endTest() {
		report.endTest(test);
		test = null;
	}
	
	
	public static ExtentTest getTest() {
			return test;
	}
	
	public static void info(String message) {
		getTest().log(LogStatus.INFO, message);
	}

	
	public static void debug(String event) {
		getTest().log(LogStatus.UNKNOWN, event);
	}

	
	public static void pass(String passMessage) {
		getTest().log(LogStatus.PASS, "<font color=\"green\">" + passMessage + "</font>");
	}

	
	public static void fail(String failMessage) {
		getTest().log(LogStatus.FAIL, "<font color=\"red\">" + failMessage + "</font>");
	}

	
	public static void skip(String message) {
		getTest().log(LogStatus.SKIP, "<font color=\"orange\">" + message + "</font>");
	}

	
	public static void logStackTrace(Throwable t) {
		if (t instanceof Error) {
			getTest().log(LogStatus.ERROR, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		} else if (t instanceof SkipException) {
			getTest().log(LogStatus.SKIP, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		} else {
			getTest().log(LogStatus.FATAL, "<div class=\"stacktrace\">" + ExceptionUtils.getStackTrace(t) + "</div>");
		}
	}

	
	public static void updateTestResults(ITestResult result){
		try{
				if (result.getStatus()==ITestResult.FAILURE) {
					ExtentReporterUtil.fail("<b>Test is Failed</b>");
					
				}
				if(result.getStatus()==ITestResult.SUCCESS){
					ExtentReporterUtil.pass("<b>Test is Passed</b>");
				}
				if(result.getStatus()==ITestResult.SKIP){
					ExtentReporterUtil.pass("Test is Skipped");
				}
			}finally{
					ExtentReporterUtil.endTest();
			}
		}
	
}