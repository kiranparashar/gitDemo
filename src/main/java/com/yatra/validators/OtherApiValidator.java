package com.yatra.validators;

import java.util.HashMap;

import com.yatra.utils.Log;

import net.sf.json.JSONObject;

public class OtherApiValidator {

	
	public static boolean dummy(JSONObject response, HashMap<String, String> testdata){
		boolean result=true;
		try{
			
			
		}catch(Exception e){
			result=false;
			Log.exception(e);
		}
		
		return result;
	}
	
	
	public static boolean validateGetFaresApi(JSONObject response, HashMap<String, String> testdata){
		boolean result=true;
		try{
			
			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result
					& Log.assertError(!response.getString("sessionId").isEmpty(),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null");

			result = result & Log.assertError(response.getString("interationType").equals("FlightMonthlyFareData"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));
			
//			result = result & Log.assertError(!response.getJSONObject("response").getJSONObject("depart").isEmpty(), "depart calendar is fine", "depart calendar is empty");
//			if(testdata.get("TripType").equalsIgnoreCase("ROUNDTRIP")){
//				result = result & Log.assertError(!response.getJSONObject("response").getJSONObject("return").isEmpty(), "return calendar is fine", "return calendar is empty");
//			}
			
		}catch(Exception e){
			result=false;
			Log.exception(e);
		}
		
		return result;
	}
	
	public static boolean validateGetAllPaxApi(JSONObject response, HashMap<String, String> testdata){
		boolean result=true;
		try{
			
			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result
					& Log.assertError(!response.getString("sessionId").isEmpty(),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null");

			result = result & Log.assertError(response.getString("interationType").equals("GetAllPax"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));
			
			result = result
					& Log.assertError(!response.getJSONObject("response").getString("code").isEmpty(),
							"code is - " + response.getJSONObject("response").getString("code"), "code is null");
			
			int paxListCount = response.getJSONObject("response").getJSONArray("PaxDetailsWOs").length();
			if(paxListCount==0){
				Log.fail("Pax list is empty");
				return result=false;
			}
			int index = (int)(Math.random()*(paxListCount-1));
			
			result = result & Log.assertError(!response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("title").isEmpty(),
					"title is - " + response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("title"), "title is null");
			
			result = result & Log.assertError(!response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("firstName").isEmpty(),
					"firstName is - " + response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("firstName"), "firstName is null");
			
			result = result & Log.assertError(!response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("lastName").isEmpty(),
					"lastName is - " + response.getJSONObject("response").getJSONArray("PaxDetailsWOs").getJSONObject(index).getString("lastName"), "lastName is null");
			
			
		}catch(Exception e){
			result=false;
			Log.exception(e);
		}
		
		return result;
	}
	
	
	public static boolean validateGetCardTypeApi(JSONObject response, HashMap<String, String> testdata){
		boolean result=true;
		try{
			result = result & Log.assertError(response.getInt("resCode") == 200, "API Response Code is 200",
					"API Response Code is " + response.getInt("resCode"));

			result = result & Log.assertError(response.getString("resMessage").equals("Success"),
					"Response message is Success", "Response message is not Success");

			result = result & Log.assertError(!response.getString("interactionId").isEmpty(),
					"InteractionId is - " + response.getString("interactionId"), "InteractionId is null");

			result = result
					& Log.assertError(!response.getString("sessionId").isEmpty(),
							"sessionId is - " + response.getString("sessionId"), "sessionId is null");

			result = result & Log.assertError(response.getString("interationType").equals("CardType"),
					"interationType is fine - " + response.getString("interationType"),
					"interationType is incorrect - " + response.getString("interationType"));
			
			result = result
					& Log.assertError(!response.getJSONObject("response").getString("cardType").isEmpty(),
							"cardType is - " + response.getJSONObject("response").getString("cardType"), "cardType is null");
			
		}catch(Exception e){
			result=false;
			Log.exception(e);
		}
		
		return result;
	}
	
}
