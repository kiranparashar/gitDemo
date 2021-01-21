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
import net.sf.json.JSONObject;

@SuppressWarnings({"rawtypes","unused"})
public class TestOtherAPI extends BaseTest {
	CommonFunctions cfObj = new CommonFunctions();
	
	@Test(dataProvider = "TestData" , dataProviderClass = DataProviderUtil.class)	
	public boolean TestotherAPIMehtod(List data) throws Exception {

		testdata=TestDataGenerator.getHashMap(data);	
		System.out.println(testdata);
		boolean result = false;
		try{			
			String TestCaseMethod = testdata.get("TestName").toString();
			String TestCaseDescription = testdata.get("TestDescription").toString()+"-"+ testdata.get("Domain").toString()+"-"+ testdata.get("TripType").toString();
			
			ExtentReporterUtil.startTest(TestCaseMethod, TestCaseDescription);
			Log.testCaseInfo(CommonFunctions.logTestData(testdata));
			
			new FlightAPI(testdata);
			
			switch(TestCaseMethod){
			case "GetFares":	
				result=FlightAPI.getFares();
				break;
			
			case "GetAllPax":	
				result = FlightAPI.loginInFlow();
				if (FlightAPI.responseCode != 200)
					return result = false;
				
				result= result & FlightAPI.getAllPax();
				break;
				
			case "TrackTravellerPage":	
				result=  FlightAPI.flightSearch();
				if (FlightAPI.responseCode != 200)
					return result = false;
				
				result = result & FlightAPI.flightPricing();
				if (FlightAPI.responseCode != 200)
					return result = false;
				
				result = result & FlightAPI.trackTravellerPage();
				break;
				
			case "GetCardType":
				result= FlightAPI.getCardType();
				break;
			}
			
			
		}catch(Exception e){
			result=false;
			Log.exception(e);
		}
		finally {
			Assert.assertEquals(result, true);
			Log.endTestCase();
		}
		return result;
	}
	
}

