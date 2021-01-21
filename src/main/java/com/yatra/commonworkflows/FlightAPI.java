package com.yatra.commonworkflows;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yatra.jsonapirequest.FlightSaveReviewJsonRequest;
import com.yatra.utils.CommonFunctions;
import com.yatra.utils.Constants;
import com.yatra.utils.DateGenerator;
import com.yatra.utils.HttpConnectionUtil;
import com.yatra.utils.Log;
import com.yatra.utils.StringUtil;
import com.yatra.validators.FlightPriceValidator;
import com.yatra.validators.FlightSearchValidator;
import com.yatra.validators.GetPaymentInfoValidator;
import com.yatra.validators.LoginInFlowValidator;
import com.yatra.validators.OtherApiValidator;
import com.yatra.validators.PayNowValidator;
import com.yatra.validators.SaveReviewDetailsValidator;

import joptsimple.internal.Strings;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class FlightAPI {

	public CommonFunctions cfObj = new CommonFunctions();

	private static LinkedHashMap<String, String> searchParams = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, String> pricingParams = new LinkedHashMap<String, String>();
	private static HashMap<String, String> testdata;
	private static HttpConnectionUtil con = new HttpConnectionUtil();

	public static int responseCode;
	private static JSONObject searchResponse = null;
	private static JSONArray sectorResults = null;
	public static JSONObject pricingResponse = null;
	private static JSONObject getPaymentInfoResponse = null;
	public static JSONObject loginResponse = null;
	private static JSONObject saveReviewDetailsResponse = null;
	private static JSONObject payNowResponse = null;
	private static JSONObject getPromoListResponse = null;
	private static JSONObject validatePromoCodeResponse = null;
	private static JSONObject getFaresResponse = null;
	private static JSONObject getAllPaxResponse = null;
	private static JSONObject getCardTypeResponse = null;
	private static JSONObject trackTravellerPageResponse = null;

	private static int owFlightsCount;
	private static int rtFlightsCount;
	private static int owFlightIndex;
	private static int rtFlightIndex;
	private static String supplierCode;
	private static String flightTypeCSV;

	public static LinkedHashMap<String, String> params = new LinkedHashMap<String, String>();
	static List<NameValuePair> formdata = new ArrayList<NameValuePair>();

	public static String pricingId;
	public static String superPnr;
	private static String url;
	public static String searchId;
	public static String sessionId;
	public static String authToken = "";

	public static int promoAmount = 0;
	public static int insuranceAmount = 0;
	public static String promoCode = "";
	public static int payableDispayedAmount = 0;
	public static int eCash = 0;

	public static int instantDiscount = 0;
	public static int eCashDiscount = 0;

	public static String productCode;
	public static String flightIdCSV = "";

	public FlightAPI(HashMap<String, String> testdata) {
		FlightAPI.testdata = testdata;

		if (testdata.get("Platform").toString().equalsIgnoreCase("android")) {
			FlightAPI.productCode = (testdata.get("Domain").toString().equalsIgnoreCase("DOM") ? "mdomandroid"
					: "mintandroid");
		} else {
			FlightAPI.productCode = (testdata.get("Domain").toString().equalsIgnoreCase("DOM") ? "mdomios" : "mintios");
		}

		HttpConnectionUtil.responseCode = 0;
	}

	public static boolean flightSearch() throws Exception {
		boolean result = false;
		responseCode = 111;
		try {
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode
					+ "/getFlightsFromPresto.htm";

			searchParams.put("deviceId", "e107b19c83e8f667");
			searchParams.put("appVersion", testdata.get("AppVersion"));
			searchParams.put("osVersion", testdata.get("OsVersion"));
			searchParams.put("domain", testdata.get("Domain"));
			searchParams.put("tripType", testdata.get("TripType"));
			searchParams.put("noOfAdults", testdata.get("Adult"));
			searchParams.put("noOfChildren", testdata.get("Child"));
			searchParams.put("noOfInfants", testdata.get("Infant"));
			searchParams.put("travelClass", testdata.get("Class"));

			if (testdata.get("TripType").equalsIgnoreCase("ONEWAY")) {
				searchParams.put("tripList[0].origin", testdata.get("Origin"));
				searchParams.put("tripList[0].destination", testdata.get("Destination"));
				String departureDate = DateGenerator
						.generateDateAfterDays(Integer.parseInt(testdata.get("DepartureDate")));
				searchParams.put("tripList[0].departureDate", departureDate);
			} else {
				searchParams.put("tripList[0].origin", testdata.get("Origin"));
				searchParams.put("tripList[0].destination", testdata.get("Destination"));
				searchParams.put("tripList[0].departureDate",
						DateGenerator.generateDateAfterDays(Integer.parseInt(testdata.get("DepartureDate"))));

				searchParams.put("tripList[1].origin", testdata.get("Destination"));
				searchParams.put("tripList[1].destination", testdata.get("Origin"));

				String returnDate = DateGenerator.generateDateAfterDays(Integer.parseInt(testdata.get("ReturnDate")));
				searchParams.put("tripList[1].departureDate", returnDate);
			}

			if (testdata.get("Domain").equalsIgnoreCase("INT")) {
				searchParams.put("firstTimeLocation", "false");
				searchParams.put("expectedRespTime", "90000");
			}

			searchResponse = con.getCall(url, searchParams, "Flight Search");
			Log.message("Flight search response :  " + searchResponse);

			responseCode = searchResponse.getInt("resCode");
			if (searchResponse == null || responseCode!= 200){
				Log.fail("API response is not successful");
				return result = false;
			}

			result = FlightSearchValidator.validate(searchResponse, testdata);

			searchId = searchResponse.getString("interactionId");
			sessionId = searchResponse.getString("sessionId");

			if (testdata.get("Domain").equalsIgnoreCase("DOM")) {
				sectorResults = searchResponse.getJSONObject("response").getJSONObject("searchResults")
						.getJSONArray("sectorResults");
			} else {
				sectorResults = searchResponse.getJSONObject("response").getJSONObject("searchResults")
						.getJSONArray("flightCombination");
			}

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}

		return result;
	}

	/**
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws ClientProtocolException
	 *
	 */
	public static boolean flightPricing() throws ClientProtocolException, IOException, InterruptedException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode
					+ "/v4/getFlightPrice.htm";

			//attempt pricing 3 times until get success
			for (int i = 0; i < 3; i++) {
				generateFlightPricingParams();
				pricingResponse = con.postFormDataCall(url, formdata, "Flight Pricing");
				Log.message("Flight pricing attempt: " + (i + 1));
				Log.message("The Pricing Response is:- " + pricingResponse);

				responseCode = pricingResponse.getInt("resCode");
				if (pricingResponse == null || responseCode != 200) {
					if (i == 2) {
						Log.fail("Flight Pricing API response is not successful");
						return result = false;
					} else {
						Log.message("Flight Pricing API response is not successful");
					}
				} else {
					Log.message("Flight pricing is successful");
					break;
				}
			}
			
			result = FlightPriceValidator.verifyFlightPricing(pricingResponse, testdata);

			pricingId = pricingResponse.getJSONObject("response").getString("pricingId");
			superPnr = pricingResponse.getJSONObject("response").getString("superPnr");

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	private static List<NameValuePair> generateFlightPricingParams(){
		String flightPrice = null;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode
					+ "/v4/getFlightPrice.htm";

			switch (testdata.get("Domain")) {
			case "DOM":
				switch (testdata.get("TripType")) {
				case "ONEWAY":
					owFlightsCount = sectorResults.getJSONObject(0).getJSONArray("flights").length();
					owFlightIndex = (int) (Math.random() * (owFlightsCount - 1));
					// owFlightIndex=0;

					flightIdCSV = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
							.getString("flightId");
					flightPrice = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
							.getString("totalFare");
					supplierCode = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
							.getString("fltSupplierId");
					flightTypeCSV = "nf";
					break;

				case "ROUNDTRIP":
					// OW flight data
					owFlightsCount = sectorResults.getJSONObject(0).getJSONArray("flights").length();
					owFlightIndex = (int) (Math.random() * (owFlightsCount - 1));
					// owFlightIndex=0;

					flightIdCSV = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
							.getString("flightId");
					String sc1 = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
							.getString("fltSupplierId");

					// RT flight data
					rtFlightsCount = sectorResults.getJSONObject(1).getJSONArray("flights").length();
					rtFlightIndex = (int) (Math.random() * (rtFlightsCount - 1));
					// rtFlightIndex=0;

					flightIdCSV = flightIdCSV + "," + sectorResults.getJSONObject(1).getJSONArray("flights")
							.getJSONObject(rtFlightIndex).getString("flightId");
					String sc2 = sectorResults.getJSONObject(1).getJSONArray("flights").getJSONObject(rtFlightIndex)
							.getString("fltSupplierId");

					if (sc1.equalsIgnoreCase(sc2))
						supplierCode = sc1;
					else
						supplierCode = sc1 + "," + sc2;

					flightPrice = getReturnFlightsPrice(sc1, sc2, flightIdCSV);
					break;
				}
				formdata.add(new BasicNameValuePair("deviceId", "e107b19c83e8f667"));
				formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
				formdata.add(new BasicNameValuePair("emailId", ""));
				formdata.add(new BasicNameValuePair("FlightIDCSV_Supplier", flightIdCSV));
				formdata.add(new BasicNameValuePair("flightIdCSV", flightIdCSV));
				formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
				formdata.add(new BasicNameValuePair("ajx", "true"));
				formdata.add(new BasicNameValuePair("flightPrice", flightPrice));
				formdata.add(new BasicNameValuePair("searchId", searchId));
				formdata.add(new BasicNameValuePair("sessionId", sessionId));
				formdata.add(new BasicNameValuePair("sc", supplierCode));
				formdata.add(new BasicNameValuePair("flightTypeCSV", flightTypeCSV));
				break;

			case "INT":
				int index = (int) (Math.random() * (sectorResults.length() - 1));
				// index =0;
				flightIdCSV = sectorResults.getJSONObject(index).getString("flightIdCSV");
				flightPrice = sectorResults.getJSONObject(index).getString("totalFare");
				supplierCode = sectorResults.getJSONObject(index).getString("fltSupplierId");

				flightTypeCSV = "nf";
				if (testdata.get("TripType").equalsIgnoreCase("ROUNDTRIP"))
					flightTypeCSV = "nf,nf";

				formdata.add(new BasicNameValuePair("deviceId", "e107b19c83e8f667"));
				formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
				formdata.add(new BasicNameValuePair("emailId", ""));
				formdata.add(new BasicNameValuePair("flightIdCSV", flightIdCSV));
				formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
				formdata.add(new BasicNameValuePair("searchId", searchId));
				formdata.add(new BasicNameValuePair("flightPrice", flightPrice));
				formdata.add(new BasicNameValuePair("ajx", "true"));
				formdata.add(new BasicNameValuePair("sessionId", sessionId));
				formdata.add(new BasicNameValuePair("flightTypeCSV", flightTypeCSV));
				formdata.add(new BasicNameValuePair("sc", supplierCode));
				break;
			}
		
		}catch(Exception e){
			Log.exception(e);
		}
		
		return formdata;
	}
	
	
	private static String getReturnFlightsPrice(String owSuppCode, String rtSuppCode, String flightIdCSV) {
		String flightPrice = null;
		long owSpecialFare = 0;
		long rtSpecialFare = 0;
		long owNormalFare = 0;
		long rtNormalFare = 0;
		String airlineType = null;

		try {
			airlineType = sectorResults.getJSONObject(0).getJSONArray("flights").getJSONObject(owFlightIndex)
					.getString("specialFarePairing");

			switch (airlineType) {
			case "ONE":
				if (!owSuppCode.equalsIgnoreCase(rtSuppCode)) {
					String owflightPrice = sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(owFlightIndex).getString("totalFare");
					String rtflightPrice = sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(rtFlightIndex).getString("totalFare");

					flightPrice = Integer.toString(Integer.parseInt(owflightPrice) + Integer.parseInt(rtflightPrice));
					flightTypeCSV = "nf,nf";

				} else {
					owNormalFare = Integer.parseInt(sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(owFlightIndex).getString("totalFare"));
					owSpecialFare = Integer.parseInt(sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(owFlightIndex).getString("discountedtotalFare"));

					rtNormalFare = Integer.parseInt(sectorResults.getJSONObject(1).getJSONArray("flights")
							.getJSONObject(rtFlightIndex).getString("totalFare"));
					rtSpecialFare = Integer.parseInt(sectorResults.getJSONObject(1).getJSONArray("flights")
							.getJSONObject(rtFlightIndex).getString("discountedtotalFare"));

					flightPrice = Long.toString((owSpecialFare < owNormalFare ? owSpecialFare : owNormalFare)
							+ (rtSpecialFare < rtNormalFare ? rtSpecialFare : rtNormalFare));
					flightTypeCSV = (owSpecialFare < owNormalFare ? "sf" : "nf") + ","
							+ (rtSpecialFare < rtNormalFare ? "sf" : "nf");
				}

				break;

			// GDS fare
			case "BOTH":
				if (!owSuppCode.equalsIgnoreCase(rtSuppCode)) {
					String owflightPrice = sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(owFlightIndex).getString("totalFare");
					String rtflightPrice = sectorResults.getJSONObject(0).getJSONArray("flights")
							.getJSONObject(rtFlightIndex).getString("totalFare");

					flightPrice = Integer.toString(Integer.parseInt(owflightPrice) + Integer.parseInt(rtflightPrice));
					flightTypeCSV = "nf,nf";
				} else {

					boolean specialFare = false;
					flightIdCSV = flightIdCSV.replaceAll(",", "");

					int totalSpecialFareCombinations = searchResponse.getJSONObject("response")
							.getJSONObject("searchResults").getJSONObject("fareDetailsSR")
							.getJSONArray("flightCombinationDetail").length();

					for (int i = 0; i < totalSpecialFareCombinations - 1; i++) {
						String flightCombinationId = searchResponse.getJSONObject("response")
								.getJSONObject("searchResults").getJSONObject("fareDetailsSR")
								.getJSONArray("flightCombinationDetail").getJSONObject(i)
								.getString("flightCombinationId");
						if (flightCombinationId.equalsIgnoreCase(flightIdCSV)) {
							flightPrice = searchResponse.getJSONObject("response").getJSONObject("searchResults")
									.getJSONObject("fareDetailsSR").getJSONArray("flightCombinationDetail")
									.getJSONObject(i).getString("totalFare");
							flightTypeCSV = "sf,sf";
							specialFare = true;
							break;
						}
					}
					if (!specialFare) {
						owNormalFare = Integer.parseInt(sectorResults.getJSONObject(0).getJSONArray("flights")
								.getJSONObject(owFlightIndex).getString("totalFare"));

						rtNormalFare = Integer.parseInt(sectorResults.getJSONObject(1).getJSONArray("flights")
								.getJSONObject(rtFlightIndex).getString("totalFare"));
						flightPrice = Long.toString(owNormalFare + rtNormalFare);
						flightTypeCSV = "nf,nf";
					}
				}
				break;
			}
		} catch (Exception e) {
			Log.exception(e);
		}
		return flightPrice;
	}

	public static boolean loginInFlow() throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/common/mcommonandroid/login.htm";

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("authProvider", "YATRA"));
			formdata.add(new BasicNameValuePair("email", Constants.EMAIL_ID));
			formdata.add(new BasicNameValuePair("deviceId", "e107b19c83e8f667"));
			formdata.add(new BasicNameValuePair("password", Constants.PASSWORD));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));

			loginResponse = con.postFormDataCall(url, formdata, "Login");
			Log.message("Login API Response: " + loginResponse);

			responseCode = loginResponse.getInt("resCode");
			if (loginResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}

			result = LoginInFlowValidator.validateLoginInFlowApi(loginResponse, testdata);

			authToken = loginResponse.getJSONObject("response").getString("authToken");

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean getPaymentInfo() throws ClientProtocolException, IOException, InterruptedException {
		boolean result = false;
		responseCode=111;
		try {

			formdata.clear();

			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/common/" + productCode
					+ "/getPaymentInfoV2.htm";

			String ylp_max = pricingResponse.getJSONObject("response").getString("ylp_max");
			String bookingAmount = pricingResponse.getJSONObject("response").getJSONObject("fareDetails")
					.getString("totalFareWithConvenienceFee");

			formdata.add(new BasicNameValuePair("superPnr", superPnr));
			formdata.add(new BasicNameValuePair("deviceId", "e107b19c83e8f667"));
			formdata.add(new BasicNameValuePair("pricingId", "e107b19c83e8f667"));
			formdata.add(new BasicNameValuePair("previous_wallet_id", ""));
			formdata.add(new BasicNameValuePair("versionNo", "20170201"));
			formdata.add(new BasicNameValuePair("uuid", ""));
			formdata.add(new BasicNameValuePair("ylp_max", ylp_max));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("bookingAmt", bookingAmount));
			formdata.add(new BasicNameValuePair("ttid", superPnr));
			formdata.add(new BasicNameValuePair("cancelRedeemedAmount", ""));
			formdata.add(new BasicNameValuePair("merchantCode", "yatra"));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("productCode", productCode));
			formdata.add(new BasicNameValuePair("client", "APP"));
			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("cust_email", Constants.EMAIL_ID));
			formdata.add(new BasicNameValuePair("ssoToken", authToken));

			getPaymentInfoResponse = con.postFormDataCall(url, formdata, "GetPaymentInfo");
			Log.message("The GetPaymentInfo Response is: " + getPaymentInfoResponse);

			responseCode = getPaymentInfoResponse.getInt("resCode");
			if (getPaymentInfoResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}
			
			result = GetPaymentInfoValidator.validateGetPaymentInfoApi(getPaymentInfoResponse, testdata);
			
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean saveReviewDetails() throws ClientProtocolException, IOException, InterruptedException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode
					+ "/saveFlightReviewDetails.htm";
			String reviewJson = generateReviewJson();

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("superPnr", superPnr));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("reviewJson", reviewJson));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("pricingId", pricingId));

			saveReviewDetailsResponse = con.postFormDataCall(url, formdata, "SaveReivewDetails");
			Log.message("SaveReviewDetails Response is: " + saveReviewDetailsResponse);

			responseCode = saveReviewDetailsResponse.getInt("resCode");
			if (saveReviewDetailsResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}

			result = SaveReviewDetailsValidator.validateSaveReviewDetailsApi(saveReviewDetailsResponse, testdata);
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	private static String generateReviewJson() throws JsonProcessingException {
		String reviewJson = null;
		reviewJson = StringUtil.generateRequestString(new FlightSaveReviewJsonRequest(testdata, getSaveReviewData()));
		return reviewJson;
	}

	private static HashMap<String, String> getSaveReviewData() {
		HashMap<String, String> data = new HashMap<String, String>();

		int totalDispAmount = 0;
		int totalFare = 0;
		try {

			if (testdata.get("Insurance").equalsIgnoreCase("Yes")) {
				boolean insuranceData = pricingResponse.getJSONObject("response").getJSONObject("fareDetails")
						.has("insuranceFareData");
				if (insuranceData)
					insuranceAmount = pricingResponse.getJSONObject("response").getJSONObject("fareDetails")
							.getJSONObject("insuranceFareData").getInt("amount");
				else
					Log.error("Insurance fare details is missing in pricing response");
			}

			totalFare = Integer.parseInt(
					pricingResponse.getJSONObject("response").getJSONObject("fareDetails").getString("totalFare"));

			totalDispAmount = totalFare + insuranceAmount - promoAmount;

			data.put("superPnr", superPnr);
			data.put("ebs_accountId", pricingResponse.getJSONObject("response").getString("ebs_accountId"));
			data.put("ebs_sessionId", pricingResponse.getJSONObject("response").getString("ebs_sessionid"));
			data.put("isPartial", "false");
			data.put("pricingId", pricingId);
			data.put("searchId", searchId);
			data.put("amountDisp", Integer.toString(totalDispAmount));
			data.put("displayMarkup", "0 ");
			data.put("insuranceAmount", Integer.toString(insuranceAmount));
			data.put("sessionId", sessionId);

			if (testdata.get("Login").equalsIgnoreCase("Yes")) {
				data.put("userId",
						loginResponse.getJSONObject("response").getJSONObject("userDetails").getString("userId"));
				data.put("firstName",
						loginResponse.getJSONObject("response").getJSONObject("userDetails").getString("firstName"));
				data.put("lastName",
						loginResponse.getJSONObject("response").getJSONObject("userDetails").getString("lastName"));
			} else {
				data.put("userId", "guest");
				data.put("firstName", "guest");
				data.put("lastName", "");
			}
		} catch (Exception e) {
			Log.exception(e);
		}
		return data;
	}

	public static boolean payNow() throws ClientProtocolException, IOException, InterruptedException {
		boolean result = false;
		try {
			formdata.clear();
			url = CommonFunctions.getPaymentServer() + "/PaySwift/payNow";

			int totalAmountWithConvFee = Integer.parseInt(pricingResponse.getJSONObject("response")
					.getJSONObject("fareDetails").getString("totalFareWithConvenienceFee"));

			if (testdata.get("Login").equalsIgnoreCase("Yes") && testdata.get("RedeemeCash").equalsIgnoreCase("Yes")) {
				eCash = getPaymentInfoResponse.getJSONObject("response").getJSONObject("QbCardsWalletInfo")
						.getInt("ecash");
			}

			eCash = eCash > 50 ? Constants.REDEEME_ECASH : eCash;

			payableDispayedAmount = totalAmountWithConvFee + insuranceAmount - promoAmount - eCash;

			formdata.add(new BasicNameValuePair("superPnr", superPnr));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("amountDisplayed", Integer.toString(payableDispayedAmount)));
			formdata.add(new BasicNameValuePair("otherDiscountingStatus", "false"));
			formdata.add(new BasicNameValuePair("discount", "0.0"));
			formdata.add(new BasicNameValuePair("uuid", pricingId));
			formdata.add(new BasicNameValuePair("email", Constants.EMAIL_ID));
			formdata.add(new BasicNameValuePair("cybersourceFingerprintId", "yatravcybse107b19c83e8f667"));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("ttid", superPnr));
			formdata.add(new BasicNameValuePair("cardDiscountingStatus", "false"));
			formdata.add(new BasicNameValuePair("promoCode", promoCode));
			formdata.add(new BasicNameValuePair("bypass_fingerprint", "No"));
			formdata.add(new BasicNameValuePair("paymentOptionParameters", getPaymentParams()));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("paymentMode", "FULL"));
			formdata.add(new BasicNameValuePair("client", "APP"));
			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("amountToRedeem", Integer.toString(eCash)));
			formdata.add(new BasicNameValuePair("discountingStatus", "false"));
			formdata.add(new BasicNameValuePair("promoType", "")); // Need to
																	// pass
			formdata.add(new BasicNameValuePair("mob", Constants.MOBILE_NUMBER));
			formdata.add(new BasicNameValuePair("product", productCode));
			formdata.add(new BasicNameValuePair("ssoToken", authToken));

			payNowResponse = con.postFormDataCall(url, formdata, "PayNow");
			Log.message("The payNow Response is: " + payNowResponse);

			if (payNowResponse == null){
				Log.fail("API response is not successful");
				return result = false;
			}

			result = PayNowValidator.validatePayNowApi(payNowResponse, testdata);
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;

	}

	private static String getPaymentParams() {
		String paymentParams = "";
		List<String> params = new LinkedList<String>();

		try {
			switch (testdata.get("PaymentMethod").toUpperCase()) {

			case "CREDITCARD":
				params.add("payop=cc");
				params.add("ctype=" + Constants.CARD_TYPE);
				params.add("isCardInternational=false");
				params.add("saveQBCard=true");
				params.add("cno=" + Constants.CARD_NUMBER);
				params.add("cardholder_name=Rati");
				params.add("address_check_rdo=Domestic");
				params.add("ccsc=123");
				params.add("cexpm=01");
				params.add("cexpy=2022");
				break;
			case "DEBITCARD":
				params.add("payop=dc");
				params.add("ctype=" + Constants.CARD_TYPE);
				params.add("isCardInternational=false");
				params.add("saveQBCard=true");
				params.add("cno=" + Constants.CARD_NUMBER);
				params.add("cardholder_name=Rati");
				params.add("address_check_rdo=Domestic");
				params.add("ccsc=123");
				params.add("cexpm=01");
				params.add("cexpy=2022");
				break;
			case "NETBANKING":
				params.add("payop=nb");
				params.add("bankCode=" + Constants.NETBANK);
				params.add("prBank=" + Constants.NETBANK);
				break;
			case "UPI":
				params.add("payop=upi");
				break;
			case "EMIOPTIONS":
				params.add("payop=emi");
				params.add("ctype=" + Constants.CARD_TYPE);
				params.add("isEmiPayment=true");
				params.add("isCardInternational=false");
				params.add("emiBank=" + Constants.EMIBANK);
				params.add("emiTenure=" + Constants.EMITTENURE);
				params.add(Constants.EMIBANK + "=" + Constants.EMITTENURE);
				params.add("TncAgree=checked");
				params.add("cno=" + Constants.CARD_NUMBER);
				params.add("cardholder_name=Rati");
				params.add("emiBank_select=" + Constants.EMIBANK);
				params.add("address_check_rdo=Domestic");
				params.add("ccsc=123");
				params.add("cexpm=01");
				params.add("cexpy=2022");
				break;
			case "WALLETS":
				params.add("payop=mw");
				params.add("bankCode=MBK");
				params.add("walletService=MBK");
				break;
			case "STOREDCARDS":
				
				Object obj = getPaymentInfoResponse.getJSONObject("response")
						.getJSONObject("QbCardsWalletInfo").getJSONObject("qbCards").get("quickBookCards");
				if(obj.equals(null) ){
					Log.fail("Not getting any saved card");
					break;
				}
					
				params.add("payop=qb");
				params.add("isCardInternational=" + getPaymentInfoResponse.getJSONObject("response")
						.getJSONObject("QbCardsWalletInfo").getJSONObject("qbCards").getJSONArray("quickBookCards")
						.getJSONObject(0).getString("cardAccessType"));
				params.add("saveQBCard=true");
				params.add("cardid=" + getPaymentInfoResponse.getJSONObject("response")
						.getJSONObject("QbCardsWalletInfo").getJSONObject("qbCards").getJSONArray("quickBookCards")
						.getJSONObject(0).getString("cardId"));
				params.add("cardType=" + getPaymentInfoResponse.getJSONObject("response")
						.getJSONObject("QbCardsWalletInfo").getJSONObject("qbCards").getJSONArray("quickBookCards")
						.getJSONObject(0).getString("cardTypeLabel"));
				params.add("cardBrand=" + getPaymentInfoResponse.getJSONObject("response")
						.getJSONObject("QbCardsWalletInfo").getJSONObject("qbCards").getJSONArray("quickBookCards")
						.getJSONObject(0).getString("cardBrand"));
				params.add("cno=" + getPaymentInfoResponse.getJSONObject("response").getJSONObject("QbCardsWalletInfo")
						.getJSONObject("qbCards").getJSONArray("quickBookCards").getJSONObject(0)
						.getString("cardNumber"));
				params.add("ccsc=123");
				break;
			}

			params.add("tdate=" + DateGenerator.generateDateAfterDays("yyyy-MM-dd hh:mm:ss",
					Integer.parseInt(testdata.get("DepartureDate")))); // set
																		// onward
																		// date
			params.add("origin=" + testdata.get("Origin"));
			params.add("destination=" + testdata.get("Destination"));
			params.add("tripType=" + (testdata.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R"));
			params.add("product=" + productCode);
			params.add("merchant=yatra");

			paymentParams = Strings.join(params, "|");
		} catch (Exception e) {
			Log.exception(e);
		}
		return paymentParams;
	}

	public static boolean getPromoList() throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();

			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/common/" + productCode
					+ "/getPromoCodeList2.htm";

			String totalFare = pricingResponse.getJSONObject("response").getJSONObject("fareDetails")
					.getString("totalFare");

			formdata.add(new BasicNameValuePair("productType", "AIR"));
			formdata.add(new BasicNameValuePair("lob", "Flight"));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("pid", pricingId));
			formdata.add(new BasicNameValuePair("isdCode", "91"));
			formdata.add(new BasicNameValuePair("mobile", Constants.MOBILE_NUMBER));
			formdata.add(new BasicNameValuePair("email", Constants.EMAIL_ID));
			formdata.add(new BasicNameValuePair("searchId", searchId));
			formdata.add(new BasicNameValuePair("promoSource", "YT"));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("promoContext", "REVIEW"));
			formdata.add(new BasicNameValuePair("flightType",
					testdata.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R"));
			formdata.add(new BasicNameValuePair("flightIdCSV", flightIdCSV));
			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("ref", superPnr));
			formdata.add(new BasicNameValuePair("purchaseAmount", totalFare));
			formdata.add(new BasicNameValuePair("channel", "B2C"));

			getPromoListResponse = con.postFormDataCall(url, formdata, "GetPromoList");
			Log.message("GetPromoList API response: " + getPromoListResponse);

			responseCode = getPromoListResponse.getInt("resCode");
			if (getPromoListResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}

			int totalPromos = getPromoListResponse.getJSONObject("response").getJSONArray("promoList").length();
			Log.message("Total avalilable promo code for selected flight: " + totalPromos);

			if (totalPromos == 0)
				Log.error("No Promo code availble for selected flight");
			else
				result = true;
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean validatePromoCode(String promoName) throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();

			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode
					+ "/v3/validatePromoCode.htm";

			if (promoName == null || promoName == "") {
				int totalPromo = getPromoListResponse.getJSONObject("response").getJSONArray("promoList").length();
				int index = (int) Math.random() * (totalPromo - 1);
				promoCode = getPromoListResponse.getJSONObject("response").getJSONArray("promoList")
						.getJSONObject(index).getString("promoCode");
			} else {
				promoCode = promoName;
			}

			String totalFare = pricingResponse.getJSONObject("response").getJSONObject("fareDetails")
					.getString("totalFare");

			formdata.add(new BasicNameValuePair("superPnr", superPnr));
			formdata.add(new BasicNameValuePair("mobileNo", Constants.MOBILE_NUMBER));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("UserId", ""));
			formdata.add(new BasicNameValuePair("pricingId", pricingId));
			formdata.add(new BasicNameValuePair("isdCode", "91"));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("promoCode", promoCode)); // get
			formdata.add(new BasicNameValuePair("totalAmount", totalFare));
			formdata.add(new BasicNameValuePair("modelNumber", "MotoG3"));
			formdata.add(new BasicNameValuePair("companyName", "motorola"));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("promoContext", "REVIEW"));
			formdata.add(new BasicNameValuePair("flightType",
					testdata.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R"));
			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));

			Log.message("Applied Promocode: " + promoCode);

			validatePromoCodeResponse = con.postFormDataCall(url, formdata, "Validate PromoCode");
			Log.message("Validate PromoCode API response: " + validatePromoCodeResponse);

			responseCode = validatePromoCodeResponse.getInt("resCode");
			if (validatePromoCodeResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}

			instantDiscount = Integer.parseInt(validatePromoCodeResponse.getJSONObject("response")
					.getJSONObject("newResponse").getString("promoDiscnt"));
			eCashDiscount = Integer.parseInt(validatePromoCodeResponse.getJSONObject("response")
					.getJSONObject("newResponse").getString("ecashdiscnt"));

			if (instantDiscount > 0 && eCashDiscount == 0) {
				String instantDiscountMsg = validatePromoCodeResponse.getJSONObject("response")
						.getJSONObject("newResponse").getString("promoDiscntMsg");
				Log.message(instantDiscountMsg);
				result = true;
			} else if (instantDiscount == 0 && eCashDiscount > 0) {
				String eCashDiscountMsg = validatePromoCodeResponse.getJSONObject("response")
						.getJSONObject("newResponse").getString("ecashDiscntMsg");
				Log.message(eCashDiscountMsg);
				result = true;
			} else if (instantDiscount == 0 && eCashDiscount == 0) {
				Log.error("Not get any discount");
			} else if (instantDiscount > 0 && eCashDiscount > 0) {
				Log.message("Seems you got Both (cash & eCash)  discount");
				result = true;
			}

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean getFares() throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/flight/" + productCode + "/getFares.htm";

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("origin", testdata.get("Origin")));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("destination", testdata.get("Destination")));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("flightType",
					testdata.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R"));

			getFaresResponse = con.postFormDataCall(url, formdata, "Get Fares");
			Log.message("Get Fares API response: " + getFaresResponse);

			responseCode = getFaresResponse.getInt("resCode");
			if (getFaresResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}
			
			result = OtherApiValidator.validateGetFaresApi(getFaresResponse, testdata);
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean getAllPax() throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/common/" + productCode + "/getAllPax.htm";

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("ssoToken", authToken));

			getAllPaxResponse = con.postFormDataCall(url, formdata, "Get All Pax");
			Log.message("Get All Pax API response: " + getAllPaxResponse);

			responseCode = getAllPaxResponse.getInt("resCode");
			if (getAllPaxResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}
			
			result = OtherApiValidator.validateGetAllPaxApi(getAllPaxResponse, testdata);
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean trackTravellerPage() throws IOException {
		boolean result = false;
		responseCode=111;
		try {

			formdata.clear();
			url = CommonFunctions.getControllerServer()
					+ "/ccwebapp/mobile/flight2/mdomandroid/async/air-pay-book-service/" + promoCode
					+ "/review/track-traveller-page.htm";

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("superPnr", superPnr));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("pricingId", pricingId));

			trackTravellerPageResponse = con.postFormDataCall(url, formdata, "Track Traveller Page");
			Log.message("Track Traveller Page API response: " + trackTravellerPageResponse);

			responseCode = trackTravellerPageResponse.getInt("resCode");
			if (trackTravellerPageResponse != null && responseCode == 200)
				result = true;

		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

	public static boolean getCardType() throws IOException {
		boolean result = false;
		responseCode=111;
		try {
			formdata.clear();
			url = CommonFunctions.getControllerServer() + "/ccwebapp/mobile/common/" + productCode + "/getCardType.htm";

			formdata.add(new BasicNameValuePair("osVersion", testdata.get("OsVersion")));
			formdata.add(new BasicNameValuePair("deviceId", Constants.DEVICEID));
			formdata.add(new BasicNameValuePair("sessionId", sessionId));
			formdata.add(new BasicNameValuePair("appVersion", testdata.get("AppVersion")));
			formdata.add(new BasicNameValuePair("cno", Constants.CARD_NUMBER));

			getCardTypeResponse = con.postFormDataCall(url, formdata, "Get Card Type");
			Log.message("Get Card Type API response: " + getCardTypeResponse);

			responseCode = getCardTypeResponse.getInt("resCode");
			if (getCardTypeResponse == null || responseCode != 200){
				Log.fail("API response is not successful");
				return result = false;
			}
			
			result = OtherApiValidator.validateGetCardTypeApi(getCardTypeResponse, testdata);
			
		} catch (Exception e) {
			result = false;
			Log.exception(e);
		}
		return result;
	}

}
