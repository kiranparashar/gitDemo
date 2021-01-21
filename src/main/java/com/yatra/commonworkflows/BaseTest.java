package com.yatra.commonworkflows;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.apache.log4j.LogManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;

import com.yatra.utils.CommonFunctions;
import com.yatra.utils.ExtentReporterUtil;

public class BaseTest {
	
	public HashMap<String , String> testdata = new HashMap<String , String>();


	@AfterMethod
	public void updateExtentReport(ITestResult result) throws Exception {
		ExtentReporterUtil.updateTestResults(result);
			
	}
	
	@AfterSuite
	public void closeExtentReport() {
		try {
			if (ExtentReporterUtil.report != null) {
				ExtentReporterUtil.report.flush();
				ExtentReporterUtil.report.close();
			}
			LogManager.shutdown();
			CommonFunctions.renameLogFile();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
