package com.yatra.testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.yatra.commonworkflows.BaseTest;
import com.yatra.commonworkflows.FlightAPI;
import com.yatra.dataproviders.DataProviderUtil;
import com.yatra.dataproviders.TestDataGenerator;
import com.yatra.utils.CommonFunctions;
import com.yatra.utils.ExtentReporterUtil;
import com.yatra.utils.Log;

@SuppressWarnings("rawtypes")
public class FlightPricingTest extends BaseTest{
	
	@Test(dataProvider = "TestData" , dataProviderClass = DataProviderUtil.class ,groups={"FlightPricing"})	
	public boolean flightPricing(List data) throws Exception {
		testdata=TestDataGenerator.getHashMap(data);	
		boolean result=false;
		try{			
			String TestCaseMethod = testdata.get("TestName").toString();
			String TestCaseDescription = testdata.get("TestDescription").toString()+"-"+ testdata.get("Domain").toString()+"-"+ testdata.get("TripType").toString();
			
			ExtentReporterUtil.startTest(TestCaseMethod, TestCaseDescription);
			Log.testCaseInfo(CommonFunctions.logTestData(testdata));
			
			new FlightAPI(testdata);
			result=  FlightAPI.flightSearch();
			if (FlightAPI.responseCode != 200)
				return result = false;
			
			result =  result & FlightAPI.flightPricing();
			
		}
		catch(Exception e){
			Log.exception(e);
		}
		finally {
			Assert.assertEquals(result, true);
			Log.endTestCase();
		}
		return result;
	}
}
