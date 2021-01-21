package com.yatra.validators;

import java.util.HashMap;

import com.yatra.utils.Log;

import net.sf.json.JSONObject;

public class LoginInFlowValidator {

	public static boolean validateLoginInFlowApi(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;

		try {
			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result
					& Log.assertError(!response.getString("sessionId").isEmpty(),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null");

			result = result & Log.assertError(response.getString("interationType").equals("Login"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));

			result = result & Log.assertError(!response.getJSONObject("response").getString("authToken").isEmpty(),
					"authToken is - " + response.getJSONObject("response").getString("authToken"), "authToken is null");

			result = result
					& Log.assertError(!response.getJSONObject("response").getString("encryptedMobileNo").isEmpty(),
							"encryptedMobileNo is - "
									+ response.getJSONObject("response").getString("encryptedMobileNo"),
							"encryptedMobileNo is null");
			result = result
					& Log.assertError(!response.getJSONObject("response").getString("encryptedEmailId").isEmpty(),
							"encryptedEmailId is - " + response.getJSONObject("response").getString("encryptedEmailId"),
							"encryptedEmailId is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("firstName").isEmpty(),
					"firstName is - "
							+ response.getJSONObject("response").getJSONObject("userDetails").getString("firstName"),
					"firstName is null");

			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("firstName").isEmpty(),
					"firstName is - "
							+ response.getJSONObject("response").getJSONObject("userDetails").getString("firstName"),
					"firstName is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("lastName").isEmpty(),
					"lastName is - "
							+ response.getJSONObject("response").getJSONObject("userDetails").getString("lastName"),
					"lastName is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("mobileNo").isEmpty(),
					"mobileNo is - "
							+ response.getJSONObject("response").getJSONObject("userDetails").getString("mobileNo"),
					"mobileNo is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("isdCode").isEmpty(),
					"isdCode is - "
							+ response.getJSONObject("response").getJSONObject("userDetails").getString("isdCode"),
					"isdCode is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("email").isEmpty(),
					"email is - " + response.getJSONObject("response").getJSONObject("userDetails").getString("email"),
					"email is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONObject("userDetails").getString("title").isEmpty(),
					"title is - " + response.getJSONObject("response").getJSONObject("userDetails").getString("title"),
					"title is null");
			result = result & Log.assertError(
					response.getJSONObject("response").getJSONObject("userDetails").getInt("userId") > 0,
					"userId is - " + response.getJSONObject("response").getJSONObject("userDetails").getInt("userId"),
					"userId is null");
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

}
