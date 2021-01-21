package com.yatra.validators;

import java.util.HashMap;
import com.yatra.commonworkflows.FlightAPI;
import com.yatra.utils.Log;
import net.sf.json.JSONObject;

public class GetPaymentInfoValidator {

	public static boolean validateGetPaymentInfoApi(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;

		try {
			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result & Log.assertError(!response.getString("sessionId").isEmpty(),
					"sessionId is - " + response.getString("sessionId"), "sessionId is null");

			result = result & Log.assertError(response.getString("interationType").equals("PaymentInfoDetails"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));

			result = result & Log.assertError(response.getJSONObject("response").get("merchantCode").equals("yatra"),
					"merchantCode is - yatra", "merchantCode is inocorrect");
			result = result & Log.assertError(
					response.getJSONObject("response").get("productCode").equals(FlightAPI.productCode),
					"productCode is - " + response.getJSONObject("response").get("productCode"),
					"productCode is null/inocorrect");

			int payopsCount = response.getJSONObject("response").getJSONObject("paymentOptionJSON")
					.getJSONArray("options").length();
			if (payopsCount == 0) {
				Log.fail("Payment options array is empty");
				return result = false;
			}
			for (int j = 0; j < payopsCount; j++) {
				result = result & Log.assertError(
						!response.getJSONObject("response").getJSONObject("paymentOptionJSON").getJSONArray("options")
								.getJSONObject(j).getString("displayText").isEmpty(),
						(j + 1) + " payment option - "
								+ response.getJSONObject("response").getJSONObject("paymentOptionJSON")
										.getJSONArray("options").getJSONObject(j).getString("displayText"),
						(j + 1) + " payment option is null");
				
				result = result & Log.assertError(
						!response.getJSONObject("response").getJSONObject("paymentOptionJSON").getJSONArray("options")
								.getJSONObject(j).getString("code").isEmpty(),
						(j + 1) + " payment option code - "
								+ response.getJSONObject("response").getJSONObject("paymentOptionJSON")
										.getJSONArray("options").getJSONObject(j).getString("code"),
						(j + 1) + " payment option code is null");
				
			}

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}

		return result;
	}

}
