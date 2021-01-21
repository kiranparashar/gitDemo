package com.yatra.validators;

import java.util.HashMap;
import com.yatra.utils.Log;
import net.sf.json.JSONObject;

public class SaveReviewDetailsValidator {

	public static boolean validateSaveReviewDetailsApi(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;

		try {

			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result
					& Log.assertError(response.getString("sessionId").equalsIgnoreCase(FlightSearchValidator.sessionId),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null / incorrect");

			result = result & Log.assertError(response.getString("interationType").equals("SaveFlightReviewDetails"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));

			result = result
					& Log.assertError(response.getString("sessionId").equalsIgnoreCase(FlightSearchValidator.sessionId),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null / incorrect");

			result = result & Log.assertError(
					response.getJSONObject("response").getString("pricingId").equals(FlightPriceValidator.pricingId),
					"pricingId is - " + response.getJSONObject("response").getString("pricingId"),
					"pricingId is null / incorrect");

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

}
