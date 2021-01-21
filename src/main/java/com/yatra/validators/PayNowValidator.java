package com.yatra.validators;

import java.util.HashMap;
import com.yatra.commonworkflows.FlightAPI;
import com.yatra.utils.CommonFunctions;
import com.yatra.utils.Constants;
import com.yatra.utils.Log;
import net.sf.json.JSONObject;

public class PayNowValidator {

	public static boolean validatePayNowApi(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;

		try {
			result = result & Log.assertError(response.getString("responseStatus").equals("SUCCESS"),
					"responseStatus is SUCCESS", "responseStatus is not SUCCESS");

			String rurl = response.getJSONObject("redirectMap").getString("rurl");
			result = result & Log.assertError(!rurl.isEmpty(), "rurl is - " + rurl, "rurl is null");

			int payNowResponseAmount = Integer
					.parseInt(response.getJSONObject("redirectMap").getString("amount").split("\\.")[0]);
			int diff = FlightAPI.payableDispayedAmount - payNowResponseAmount;

			Log.message("Payable Displayed Amount: " + FlightAPI.payableDispayedAmount);
			Log.message("Paynow response/Bank page Amount: " + payNowResponseAmount);

			if (CommonFunctions.getPaymentServer().contains("rfs")) {
				result = result & Log.assertError(payNowResponseAmount==1, "Payable amount is fine on RFS",
						"Payable amount is should be Rs. 1 on RFS");
			} else {
				result = result & Log.assertError((diff == 0 || (diff < 5 && diff > -5)), "Payable amount is fine",
						"Payable amount is not maching with bank amount");
			}
			result = result & Log.assertError(!response.getJSONObject("redirectMap").getString("mob").isEmpty(),
					"mob is - " + response.getJSONObject("redirectMap").getString("mob"), "mob is null");
			result = result & Log.assertError(
					response.getJSONObject("redirectMap").getString("origin").equalsIgnoreCase(testdata.get("Origin")),
					"Origin is - " + response.getJSONObject("redirectMap").getString("origin"),
					"Origin is null/not correct");
			result = result & Log.assertError(
					!response.getJSONObject("redirectMap").getString("destination")
							.isEmpty(),
					"Destination is - " + response.getJSONObject("redirectMap").getString("destination"),
					"destination is null");
			result = result & Log.assertError(!response.getJSONObject("redirectMap").getString("flightClass").isEmpty(),
					"flightClass is - " + response.getJSONObject("redirectMap").getString("flightClass"),
					"flightClass is null");
			result = result & Log.assertError(
					response.getJSONObject("redirectMap").getString("product").equalsIgnoreCase(FlightAPI.productCode),
					"product is - " + response.getJSONObject("redirectMap").getString("product"),
					"product is incorrect");
			result = result & Log.assertError(
					response.getJSONObject("redirectMap").getString("email").equalsIgnoreCase(Constants.EMAIL_ID),
					"email is - " + response.getJSONObject("redirectMap").getString("email"), "email is incorrect");
			result = result & Log.assertError(
					response.getJSONObject("redirectMap").getString("super_pnr").equals(FlightAPI.superPnr),
					"super_pnr is - " + response.getJSONObject("redirectMap").getString("super_pnr"),
					"super_pnr is incorrect");

			result = result
					& Log.assertError(response.getJSONObject("redirectMap").getString("merchant").equals("yatra"),
							"merchant is - " + response.getJSONObject("redirectMap").getString("merchant"),
							"merchant is incorrect");
			result = result & Log.assertError(!response.getJSONObject("redirectMap").getString("ttid").isEmpty(),
					"ttid is - " + response.getJSONObject("redirectMap").getString("ttid"), "ttid is null");
			result = result
					& Log.assertError(!response.getJSONObject("redirectMap").getString("flightNumber").isEmpty(),
							"flightNumber is - " + response.getJSONObject("redirectMap").getString("flightNumber"),
							"flightNumber is null");
			result = result & Log.assertError(!response.getJSONObject("redirectMap").getString("tdate").isEmpty(),
					"tdate is - " + response.getJSONObject("redirectMap").getString("tdate"), "tdate is null");
			result = result & Log.assertError(!response.getJSONObject("redirectMap").getString("s2sUrl").isEmpty(),
					"s2sUrl is - " + response.getJSONObject("redirectMap").getString("s2sUrl"), "s2sUrl is null");

			String tripType = testdata.get("TripType").equalsIgnoreCase("ONEWAY") ? "O" : "R";
			result = result
					& Log.assertError(response.getJSONObject("redirectMap").getString("tripType").equals(tripType),
							"tripType is - " + response.getJSONObject("redirectMap").getString("tripType"),
							"tripType is incorrect");

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}

		return result;
	}

}
