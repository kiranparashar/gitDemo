package com.yatra.validators;

import java.util.HashMap;

import com.yatra.utils.Log;

import net.sf.json.JSONObject;

public class FlightPriceValidator {

	private static String interactionId;
	private static String interationType;
	private static String sessionId;
	private static int resCode;
	private static String resMessage;

	public static int totalFare;
	public static int totalFareWithConvenienceFee;
	public static String pricingId;

	public static boolean verifyFlightPricing(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;
		
		try{
		resCode = response.getInt("resCode");
		result = result
				& Log.assertError(resCode == 200, "API Response Code is 200", "API Response Code is " + resCode);

		resMessage = response.getString("resMessage");
		result = result & Log.assertError(resMessage.equals("Success"), "Response message is Success",
				"Response message is not Success");

		interactionId = response.getString("interactionId");
		result = result & Log.assertError(!interactionId.isEmpty(), "InteractionId is - " + interactionId,
				"InteractionId is null");

		sessionId = response.getString("sessionId");
		result = result & Log.assertError(sessionId.equalsIgnoreCase(FlightSearchValidator.sessionId),
				"sessionId is - " + sessionId, "sessionId is null / incorrect");

		interationType = response.getString("interationType");
		result = result & Log.assertError(interationType.equals("FlightPricing"),
				"interationType is fine - " + interationType, "interationType is incorrect - " + interationType);

		// result = result & Log.assertError(, "","");
		result = result & Log.assertError(!response.getJSONObject("response").getString("ebs_sessionid").isEmpty(),
				"ebs_sessionid is - " + response.getJSONObject("response").getString("ebs_sessionid"),
				"ebs_sessionid is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("yatraMiles").isEmpty(),
				"yatraMiles is - " + response.getJSONObject("response").getString("yatraMiles"), "yatraMiles is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("supplierCode").isEmpty(),
				"supplierCode is - " + response.getJSONObject("response").getString("supplierCode"),
				"supplierCode is null");
		
		pricingId = response.getJSONObject("response").getString("pricingId");
		result = result & Log.assertError(!pricingId.isEmpty(),
				"pricingId is - " +pricingId, "pricingId is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("superPnr").isEmpty(),
				"superPnr is - " + response.getJSONObject("response").getString("superPnr"), "superPnr is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("searchId").isEmpty(),
				"searchId is - " + response.getJSONObject("response").getString("searchId"), "searchId is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("ebs_accountId").isEmpty(),
				"ebs_accountId is - " + response.getJSONObject("response").getString("ebs_accountId"),
				"ebs_accountId is null");
		result = result & Log.assertError(!response.getJSONObject("response").getString("ylp_max").isEmpty(),
				"ylp_max is - " + response.getJSONObject("response").getString("ylp_max"), "ylp_max is null");
		result = result & Log.assertError(
				response.getJSONObject("response").getInt("eCash") >= FlightSearchValidator.eCash,
				"eCash is - " + response.getJSONObject("response").getInt("eCash"), "eCash is null / incorrect");
//		result = result & Log.assertError(response.getJSONObject("response").getBoolean("showGst"),
//				"showGst is - " + response.getJSONObject("response").getBoolean("showGst"), "showGst is false");
//		result = result & Log.assertError(response.getJSONObject("response").getBoolean("gstRePrice"),
//				"gstRePrice is - " + response.getJSONObject("response").getBoolean("gstRePrice"),
//				"gstRePrice is false");
		result = result & Log.assertError(
				!response.getJSONObject("response").getJSONObject("convenienceFee").getString("amount").isEmpty(),
				"ConvenienceFee total amount is - "
						+ response.getJSONObject("response").getJSONObject("convenienceFee").getString("amount"),
				"ConvenienceFee total amount is null");
		result = result & Log.assertError(
				!response.getJSONObject("response").getJSONObject("convenienceFee").getString("perPaxAmount").isEmpty(),
				"ConvenienceFee per pax amount is - "
						+ response.getJSONObject("response").getJSONObject("convenienceFee").getString("perPaxAmount"),
				"ConvenienceFee per pax amount is null");

		totalFare = response.getJSONObject("response").getJSONObject("fareDetails").getInt("totalFare");

		totalFareWithConvenienceFee = response.getJSONObject("response").getJSONObject("fareDetails")
				.getInt("totalFareWithConvenienceFee");
		result = result & Log.assertError(totalFareWithConvenienceFee > 0,
				"totalFareWithConvenienceFee is -" + totalFareWithConvenienceFee, "totalFareWithConvenienceFee is 0");

		int fareBreakupCount = response.getJSONObject("response").getJSONObject("fareDetails")
				.getJSONArray("fareBreakup").length();
		result = result & Log.assertError(fareBreakupCount >= 2, "Fare breakup array is not empty",
				"Fare breakup array is empty");

		int toalFareInBreakupSection = response.getJSONObject("response").getJSONObject("fareDetails")
				.getJSONArray("fareBreakup").getJSONObject(0).getInt("amount");

		int totalOfAllBreakup = 0;
		for (int j = 1; j < fareBreakupCount; j++) {
			totalOfAllBreakup = totalOfAllBreakup + response.getJSONObject("response").getJSONObject("fareDetails")
					.getJSONArray("fareBreakup").getJSONObject(j).getInt("amount");
		}

		result = result & Log.assertError(
				(totalFare == toalFareInBreakupSection && toalFareInBreakupSection == totalOfAllBreakup),
				"Total fare is fine (as per breakup) - " + totalFare, "Total fare seems incorrect");

		try {
			int insuranceAmount = response.getJSONObject("response").getJSONObject("fareDetails")
					.getJSONObject("insuranceFareData").getInt("amount");
			result = result & Log.assertError(insuranceAmount > 0, "Insurance amount is - " + insuranceAmount,
					"Insurance seems incorrect");
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}

		int legInfoCount = response.getJSONObject("response").getJSONArray("legInfos").length();
		for (int j = 0; j < legInfoCount; j++) {
			result = result
					& Log.assertError(
							!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
									.getString("baggage").isEmpty(),
							"Baggage in " + (j + 1)
									+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
											.getJSONObject(j).getString("baggage"),
							"Baggage in " + (j + 1) + " leg is null");
			result = result
					& Log.assertError(
							!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
									.getString("flightId").isEmpty(),
							"flightId in " + (j + 1)
									+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
											.getJSONObject(j).getString("flightId"),
							"flightId in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getString("departureCity").isEmpty(),
					"departureCity in " + (j + 1)
							+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
									.getJSONObject(j).getString("departureCity"),
					"departureCity in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getString("arrivalCity").isEmpty(),
					"arrivalCity in " + (j + 1)
							+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
									.getJSONObject(j).getString("arrivalCity"),
					"arrivalCity in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getString("departureDate").isEmpty(),
					"departureDate in " + (j + 1)
							+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
									.getJSONObject(j).getString("departureDate"),
					"departureDate in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getString("arrivalDate").isEmpty(),
					"arrivalCity in " + (j + 1)
							+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
									.getJSONObject(j).getString("arrivalDate"),
					"arrivalDate in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getString("travelClass").isEmpty(),
					"travelClass in " + (j + 1)
							+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
									.getJSONObject(j).getString("travelClass"),
					"travelClass in " + (j + 1) + " leg is null");
			result = result
					& Log.assertError(
							!response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
									.getString("travelClass").isEmpty(),
							"fareType in " + (j + 1)
									+ " leg is - " + response.getJSONObject("response").getJSONArray("legInfos")
											.getJSONObject(j).getString("fareType"),
							"fareType in " + (j + 1) + " leg is null");
			result = result & Log.assertError(
					response.getJSONObject("response").getJSONArray("legInfos").getJSONObject(j)
							.getBoolean("displayFareRule"),
					"displayFareRule in " + (j + 1) + " leg is - true ",
					"displayFareRule in " + (j + 1) + " leg is false");
		}

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

}
