package com.yatra.validators;

import java.util.HashMap;
import com.yatra.utils.DateGenerator;
import com.yatra.utils.Log;
import net.sf.json.JSONObject;

public class FlightSearchValidator {
	private static int resCode;
	private static String resMessage;
	private static String interactionId;
	private static String pollingId;
	public static String sessionId;
	private static String interationType;
	private static String originCity;
	private static String destinationCity;
	private static String yatraAirlineCode;
	private static String marketingAirlineCode;
	private static String airlineCode;
	private static String airlineName;
	private static String arrivalTime;
	private static String departureDate;
	private static String departureTime;
	private static String duration;
	private static String fareType;
	private static String totalFarePerAdult;
	private static String flightId;
	private static String baggage;
	private static int totalNoOfResults = 0;
	private static String totalFare;
	private static String fltSupplierId;
	private static String flightCode;
	private static String classtype;
	public static int eCash;

	public static boolean validate(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;

		try {
			resCode = response.getInt("resCode");
			result = result
					& Log.assertError(resCode == 200, "API Response Code is 200", "API Response Code is " + resCode);

			resMessage = response.getString("resMessage");
			result = result & Log.assertError(resMessage.equals("Success"), "Response message is Success",
					"Response message is not Success");

			interactionId = response.getString("interactionId");
			result = result & Log.assertError(!interactionId.isEmpty(), "InteractionId is - " + interactionId,
					"InteractionId is null");

			pollingId = response.getString("pollingId");
			result = result & Log.assertError(!pollingId.isEmpty(), "pollingId is - " + pollingId, "pollingId is null");

			sessionId = response.getString("sessionId");
			result = result & Log.assertError(!sessionId.isEmpty(), "sessionId is - " + sessionId, "sessionId is null");

			interationType = response.getString("interationType");
			result = result & Log.assertError(interationType.equals("FlightSearch"), "interationType is fine",
					"interationType is incorrect");

			int index = 0;
			originCity = response.getJSONObject("response").getJSONObject("request").getJSONArray("tripList")
					.getJSONObject(index).getString("origin");
			result = result & Log.assertError(originCity.equals(testdata.get("Origin")), "Origin city is fine",
					"Origin city is incorrect in response");

			destinationCity = response.getJSONObject("response").getJSONObject("request").getJSONArray("tripList")
					.getJSONObject(index).getString("destination");
			result = result & Log.assertError(destinationCity.equals(testdata.get("Destination")),
					"Destination city is fine", "Destination city is incorrect in response");

			switch (testdata.get("Domain").toString()) {

			case "DOM":
				int i = 0;
				int loopCount = (testdata.get("TripType").equalsIgnoreCase("ONEWAY") ? 1 : 2);
				while (i < loopCount) {
					totalNoOfResults = response.getJSONObject("response").getJSONObject("searchResults")
							.getJSONArray("sectorResults").getJSONObject(i).getJSONArray("flights").length();

					int selectedFlightIndex = (int) (Math.random() * (totalNoOfResults - 1));

					if (i == 0) {
						Log.message("Validating - Onward Flight details");
						result = result & Log.assertError(totalNoOfResults > 0,
								"Total onward flight results are " + totalNoOfResults,
								"Onward Flight results count is 0");
					} else {
						Log.message("Validating - Return Flight details");
						result = result & Log.assertError(totalNoOfResults > 0,
								"Total Return flight results are " + totalNoOfResults,
								"Return Flight results count is 0");

					}

					Log.message("Selected flight index: " + selectedFlightIndex);

					JSONObject selectedFlightObj = response.getJSONObject("response").getJSONObject("searchResults")
							.getJSONArray("sectorResults").getJSONObject(0).getJSONArray("flights")
							.getJSONObject(selectedFlightIndex);

					departureTime = selectedFlightObj.getString("departureTime");
					result = result & Log.assertError(!departureTime.isEmpty(), "Departure time is - " + departureTime,
							"Departure time is null");

					arrivalTime = selectedFlightObj.getString("arrivalTime");
					result = result & Log.assertError(!arrivalTime.isEmpty(), "Arrival time is - " + arrivalTime,
							"Arrival time is null");

					duration = selectedFlightObj.getString("duration");
					result = result & Log.assertError((!duration.isEmpty() && !duration.equals("00:00")),
							"duration time is - " + duration, "duration time is null");

					totalFare = selectedFlightObj.getString("totalFare");
					result = result & Log.assertError(!totalFare.isEmpty(), "Total fare is - " + totalFare,
							"Total fare is null");

					totalFarePerAdult = selectedFlightObj.getString("totalFarePerAdult");
					result = result & Log.assertError(!totalFarePerAdult.isEmpty(),
							"Total fare per adult is - " + totalFarePerAdult, "Total fare per adult is null");

					baggage = selectedFlightObj.getString("baggage");
					result = result & Log.assertError(!baggage.isEmpty(), "baggage is - " + baggage, "baggage is null");

					fltSupplierId = selectedFlightObj.getString("fltSupplierId");
					result = result & Log.assertError(!fltSupplierId.isEmpty(), "fltSupplierId is - " + fltSupplierId,
							"fltSupplierId is null");

					flightId = selectedFlightObj.getString("flightId");
					result = result
							& Log.assertError(!flightId.isEmpty(), "flightId is - " + flightId, "flightId is null");

					airlineCode = selectedFlightObj.getString("airlineCode");
					result = result & Log.assertError(!airlineCode.isEmpty(), "airlineCode is - " + airlineCode,
							"airlineCode is null");

					flightCode = selectedFlightObj.getString("flightCode");
					result = result & Log.assertError(!flightCode.isEmpty(), "flightCode is - " + flightCode,
							"flightCode is null");

					marketingAirlineCode = selectedFlightObj.getString("marketingAirlineCode");
					result = result & Log.assertError(!marketingAirlineCode.isEmpty(),
							"marketingAirlineCode is - " + marketingAirlineCode, "marketingAirlineCode is null");

					yatraAirlineCode = selectedFlightObj.getString("yatraAirlineCode");
					result = result & Log.assertError(!yatraAirlineCode.isEmpty(),
							"yatraAirlineCode is - " + yatraAirlineCode, "yatraAirlineCode is null");

					airlineName = selectedFlightObj.getString("airlineName");
					result = result & Log.assertError(!airlineName.isEmpty(), "airlineName is - " + airlineName,
							"airlineName is null");

					fareType = selectedFlightObj.getString("fareType");
					result = result
							& Log.assertError(!fareType.isEmpty(), "fareType is - " + fareType, "fareType is null");

					classtype = selectedFlightObj.getString("classtype");
					result = result
							& Log.assertError(!classtype.isEmpty(), "classtype is - " + classtype, "classtype is null");

					eCash = selectedFlightObj.getInt("eCash");
					result = result & Log.assertError(eCash > 50 && eCash < 1000, "eCash is fine - " + eCash,
							"eCash seems incorrect");

					result = result & filterVerification(response, testdata);

					i++;
				}
				break;
			case "INT":
				
				break;
			}

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}

		return result;
	}

	private static boolean filterVerification(JSONObject response, HashMap<String, String> testdata) {
		boolean result = true;
		int i = 0;
		try {
			int loopCount = (testdata.get("TripType").equalsIgnoreCase("ONEWAY") ? 1 : 2);

			while (i < loopCount) {
				JSONObject filterDetails = response.getJSONObject("response").getJSONObject("searchResults")
						.getJSONArray("sectorResults").getJSONObject(i).getJSONObject("filterDetails");

				result = result & Log.assertError(
						filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getInt("minValue") > 0,
						"RangeFilter: minValue>0,  amount: "
								+ filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getInt("minValue"),
						"RangeFilter: minValue=0");
				result = result & Log.assertError(
						filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getInt("maxValue") > 0,
						"RangeFilter: maxValue>0,  amount: "
								+ filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getInt("maxValue"),
						"RangeFilter: maxValue=0");
				result = result & Log.assertError(
						!filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getString("filterName").isEmpty(),
						"RangeFilter: filterName is - "
								+ filterDetails.getJSONArray("rangeFilters").getJSONObject(0).getString("filterName"),
						"RangeFilter: filterName is null");

				result = result & Log.assertError(
						!filterDetails.getJSONArray("timeRangeFilters").getJSONObject(0).getString("filterName")
								.isEmpty(),
						"timeRangeFilters: filterName is - " + filterDetails.getJSONArray("timeRangeFilters")
								.getJSONObject(0).getString("filterName"),
						"timeRangeFilters: filterName is null");

				int timeFilterCount = filterDetails.getJSONArray("timeRangeFilters").getJSONObject(0)
						.getJSONArray("timeRanges").length();
				result = result & Log.assertError(timeFilterCount > 0, "timeRanges filter array is not blank",
						"timeRanges filter array is blank");

				for (int j = 0; j < timeFilterCount; j++) {
					result = result & Log.assertError(
							!filterDetails.getJSONArray("timeRangeFilters").getJSONObject(0).getJSONArray("timeRanges")
									.getJSONObject(j).getString("label").isEmpty(),
							"timeRangeFilters: time label is - " + filterDetails.getJSONArray("timeRangeFilters")
									.getJSONObject(0).getJSONArray("timeRanges").getJSONObject(j).getString("label"),
							"timeRangeFilters: time label is null");
				}

				result = result & Log.assertError(
						!filterDetails.getJSONArray("multiSelectFilters").getJSONObject(0).getString("filterName")
								.isEmpty(),
						"multiSelectFilters: filterName is - " + filterDetails.getJSONArray("multiSelectFilters")
								.getJSONObject(0).getString("filterName"),
						"multiSelectFilters: filterName is null");

				int filterAirlineCount = filterDetails.getJSONArray("multiSelectFilters").getJSONObject(0)
						.getJSONArray("filterValues").length();
				result = result & Log.assertError(filterAirlineCount > 0, "Airline filter array is not blank",
						"Airline filter array is blank");

				for (int j = 0; j < filterAirlineCount; j++) {
					result = result & Log.assertError(
							!filterDetails.getJSONArray("multiSelectFilters").getJSONObject(0)
									.getJSONArray("filterValues").getJSONObject(j).getString("label").isEmpty(),
							"multiSelectFilters: airline label is - " + filterDetails.getJSONArray("multiSelectFilters")
									.getJSONObject(0).getJSONArray("filterValues").getJSONObject(j).getString("label"),
							"multiSelectFilters: airline label is null");
				}

				int filterBooleanTabCount = filterDetails.getJSONArray("booleanTabFilters").length();
				result = result & Log.assertError(filterBooleanTabCount > 0, "booleanTabFilters array is not blank",
						"booleanTabFilters array is blank");

				for (int j = 0; j < filterBooleanTabCount; j++) {
					result = result & Log.assertError(
							!filterDetails.getJSONArray("booleanTabFilters").getJSONObject(j).getString("label")
									.isEmpty(),
							"booleanTabFilters :" + (j + 1) + " filter label is - "
									+ filterDetails.getJSONArray("booleanTabFilters").getJSONObject(j)
											.getString("label"),
							"booleanTabFilters :" + (j + 1) + " filter label is null");
				}

				i++;
			}
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

}
