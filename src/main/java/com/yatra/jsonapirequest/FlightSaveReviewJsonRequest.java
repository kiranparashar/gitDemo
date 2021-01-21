package com.yatra.jsonapirequest;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.lang3.RandomStringUtils;
import com.yatra.commonworkflows.FlightAPI;
import com.yatra.utils.Constants;
import com.yatra.utils.DateGenerator;

public class FlightSaveReviewJsonRequest {

	public ArrayList<AddOnParams> addOnParams = new ArrayList<AddOnParams>();
	public AdvancedPricing advancedPricing = new AdvancedPricing();
	public DiscountParams discountParams = new DiscountParams();
	public GlobalParams globalParams;
	public HotelCrossSellParams hotelCrossSellParams;
	public ProductParams productParams;
	public PromoParams promoParams;
	public TotalBreakup totalBreakup;
	public ArrayList<TravellerParams> travellerParams = new ArrayList<TravellerParams>();
	public UserParams userParams;

	public FlightSaveReviewJsonRequest(HashMap<String, String> testdata, HashMap<String,String> otherdata) {

		AddOnParams addOnParamsObj;
		addOnParamsObj = new AddOnParams("insurance");
		addOnParams.add(addOnParamsObj);
		
		globalParams = new GlobalParams(testdata, otherdata);
		hotelCrossSellParams = new HotelCrossSellParams();
		productParams = new ProductParams(testdata, otherdata);
		promoParams = new PromoParams();
		totalBreakup = new TotalBreakup();
		
		TravellerParams travellerParamsObj;
		int j=1;
		for (int i = 0; i < Integer.parseInt(testdata.get("Adult")); i++) {
			String paxCategory = "ADT";
			int paxId=j;
			travellerParamsObj = new TravellerParams(testdata, paxId, paxCategory);
			travellerParams.add(travellerParamsObj);
			j++;
		}
		for (int i = 0; i < Integer.parseInt(testdata.get("Child")); i++) {
			String paxCategory = "CHD";
			int paxId=j;
			travellerParamsObj = new TravellerParams(testdata, paxId, paxCategory);
			travellerParams.add(travellerParamsObj);
			j++;
		}
		for (int i = 0; i < Integer.parseInt(testdata.get("Infant")); i++) {
			String paxCategory = "INF";
			int paxId=j;
			travellerParamsObj = new TravellerParams(testdata, paxId, paxCategory);
			travellerParams.add(travellerParamsObj);
			j++;
		}
		
		userParams = new UserParams(otherdata);
	}
}

class AddOnParams {
	public String addonId;
	public String addonLabel;
	public String addonType;
	public String nomineeName;
	
	public AddOnParams(String insurance){
		this.addonId = "01";
		this.addonLabel = "Insurance";
		this.addonType = "insurance";
		this.nomineeName="";
	}
}

class AdvancedPricing {
}

class DiscountParams {
}

class GlobalParams {
	public String aDTcreator = "";
	public String changeFlightUrl = "com.yatra.flights.activity.InternationalFlightSearchResultsActivity";
	public String channel = "b2c";
	public String ebs_accountId;   
	public String ebs_sessionId ; 
	public String isPartial = "false";
	public String pricingId;
	public String product = "flight";
	public String prq = "";
	public String searchId;
	public String superPnr ;
	public String url = "";
	public String ftype="";
	public String org="";
	public String rurl="";
	
	GlobalParams(HashMap<String, String> testData, HashMap<String,String> otherdata) {
		this.ebs_accountId = otherdata.get("ebs_accountId");
		this.ebs_sessionId = otherdata.get("ebs_sessionId");
		this.pricingId = otherdata.get("pricingId");
		this.searchId = otherdata.get("searchId");
		this.superPnr = otherdata.get("superPnr");
		ftype = (testData.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R");
		org = testData.get("Origin");
		rurl = "https://secure.yatra.com/ccwebapp/mobile/flight/"
				+ FlightAPI.productCode
				+ "/responsePaymentHandler.htm?sessionId\u003de107b19c83e8f66715045934074255302";
	}
}

class HotelCrossSellParams {
	public String hotelBookingRequestJSON="";
	public String isHotelCrosssellBooking="false";
	
}

class ProductParams {
	public String amountDisp ;
	public String displayMarkup="0";
	public String tripType ;
	
	ProductParams(HashMap<String, String> testdata, HashMap<String, String> otherdata){
		this.amountDisp = otherdata.get("amountDisp");
		this.tripType = (testdata.get("TripType").toString().equalsIgnoreCase("ONEWAY") ? "O" : "R");
	}
}

class PromoParams{	
}

class TotalBreakup {
	public Baggage baggage = new Baggage();
	public BeingHuman beingHuman = new BeingHuman();
	public Insurance insurance = new Insurance();;
	public Markup markup = new Markup();;
	public Meals meals = new Meals();
	public Others others = new Others();;
	public Seats seats = new Seats();;

}

class Baggage{
	public int amount=0;
	public String label="Baggage Charges";
}

class BeingHuman{
	public int amount=0;
	public String label="Being Human Contribution";
}

class Insurance{
	public int amount=FlightAPI.insuranceAmount;
	public String label="Travel Insurance";
}

class Markup{
	public int amount=0;
	public String label="Markup";
}

class Meals{
	public int amount=0;
	public String label ="Meals Charges";
}

class Seats{
	public int amount=0;
	public String label ="Seats Charges";
}

class Others{
	public int amount=0;
	public String label ="Others Charges";
}

class UserParams{
	public AdditionalContact  additionalContact = new AdditionalContact();
	public String emailId = Constants.EMAIL_ID;
	public String firstName;
	public String lastName;
	public String mobileNo = Constants.MOBILE_NUMBER;
	public String mobileNoISD = "91";
	public String title = "Mr";
	public String userId;
	
	public UserParams(HashMap<String, String> otherdata){
		this.firstName=otherdata.get("firstName");
		this.lastName =otherdata.get("lastName");
		this.userId = otherdata.get("userId");
	}
}
class AdditionalContact{
	public String email=Constants.EMAIL_ID;
	public String mobile = Constants.MOBILE_NUMBER;
}



class TravellerParams{
	public int paxID;
	public SsrDetails ssrDetails = new SsrDetails();
	public TravellerDetails travellerDetails;
	
	public TravellerParams(HashMap<String,String> testData, int paxNumber, String paxCategory){
	 this.paxID=paxNumber;
	 travellerDetails = new TravellerDetails(paxNumber,paxCategory);
	}

}	

class SsrDetails{
	public ArrayList<SsrBaggageDetails> ssrBaggageDetails = new ArrayList<SsrBaggageDetails>();
	public ArrayList<SsrMealDetails> ssrMealDetails = new ArrayList<SsrMealDetails>();
	public ArrayList<SsrOtherDetails> ssrOtherDetails = new ArrayList<SsrOtherDetails>();
	public ArrayList<SsrSeatDetails> ssrSeatDetails = new ArrayList<SsrSeatDetails>();
}

class SsrBaggageDetails{
}

class SsrMealDetails{
}

class SsrOtherDetails{
}

class SsrSeatDetails{
}


class TravellerDetails{
	public String dateOfBirth="";
	public String firstName="Test";
	public FrequentFlyer frequentFlyer = new FrequentFlyer();
	public int id;
	public String lastName=RandomStringUtils.random(5,"abcdefghijklmnopqrstuvwxyz");
	public String middleName = "";
	public String paxClass;
	public String passengerClass;
	public Passport passport = new Passport();
	public String title = "Mr";
	
	public TravellerDetails(int paxId, String paxCat){
	this.paxClass= (paxCat.equalsIgnoreCase("ADT")?"Adult": (paxCat.equalsIgnoreCase("CHD")?"Child":"Infant"));
	this.passengerClass = paxCat;
	if(paxCat.equalsIgnoreCase("CHD")){
		this.title="Master";
	}else if (paxCat.equalsIgnoreCase("INF")){
		this.title="Master";
		this.dateOfBirth=DateGenerator.generateDateAfetrMonths(-2);
	}
	this.id=paxId;
	}
	
}

class FrequentFlyer{
}

class Passport{
	public String expiryDate="";
	public String issuingCountryCode="";
	public String issuingCountryName="";
	public String nationality="";
	public String number = "";
	
}


