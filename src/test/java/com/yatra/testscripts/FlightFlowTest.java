package com.yatra.testscripts;

import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.yatra.commonworkflows.BaseTest;
import com.yatra.commonworkflows.FlightAPI;
import com.yatra.dataproviders.DataProviderUtil;
import com.yatra.dataproviders.TestDataGenerator;
import com.yatra.utils.CommonFunctions;
import com.yatra.utils.ExtentReporterUtil;
import com.yatra.utils.HttpConnectionUtil;
import com.yatra.utils.Log;
import net.sf.json.JSONObject;

@SuppressWarnings({ "rawtypes", "unused" })
public class FlightFlowTest extends BaseTest {
	CommonFunctions cfObj = new CommonFunctions();

	@Test(dataProvider = "TestData", dataProviderClass = DataProviderUtil.class)
	public boolean flightFlow(List data) throws Exception {
		System.out.println(data);
		testdata = TestDataGenerator.getHashMap(data);
		boolean result = false;
		try {
			String TestCaseMethod = testdata.get("TestName").toString();
			String TestCaseDescription = testdata.get("TestDescription").toString() + "-"
					+ testdata.get("Domain").toString() + "-" + testdata.get("TripType").toString();

			ExtentReporterUtil.startTest(TestCaseMethod, TestCaseDescription);
			Log.testCaseInfo(CommonFunctions.logTestData(testdata));

			new FlightAPI(testdata);
			
			result = FlightAPI.flightSearch();
			if (FlightAPI.responseCode != 200)
				return result = false;

			result = result & FlightAPI.flightPricing();
			if (FlightAPI.responseCode != 200)
				return result = false;

			if (testdata.get("Login").equalsIgnoreCase("Yes")) {
				result = result & FlightAPI.loginInFlow();
				if (FlightAPI.responseCode != 200)
					return result = false;
			}

			result = result & FlightAPI.getPaymentInfo();
			if (FlightAPI.responseCode != 200)
				return result = false;

			result = result & FlightAPI.saveReviewDetails();
			if (FlightAPI.responseCode != 200)
				return result = false;

			result = result & FlightAPI.payNow();

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
			Log.exception(e);
		} finally {
			Assert.assertEquals(result, true);
			Log.endTestCase();
		}
		return result;
	}
	
}
