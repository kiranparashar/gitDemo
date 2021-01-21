package com.yatra.utils;

import java.io.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import com.yatra.commonworkflows.ExecutionTimer;

import net.sf.json.JSONObject;

@SuppressWarnings("rawtypes")
public class HttpConnectionUtil {

	private HttpGet httpGet;
	private HttpPost httpPost;
	private HttpResponse response=null;
//	private HttpClient client;
	private CloseableHttpClient client;
	private String line = "";
	private StringBuffer result=null;
	public String ssoToken;
	private URIBuilder builder ;
	public static int responseCode=0;
	
	
	public StringBuffer getResult(HttpResponse response) throws IOException{
		result = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		while ((line = reader.readLine()) != null){
			result.append(line); 
		}
		return result;
	}
	
	// Get Method
	public JSONObject getCall(String url, LinkedHashMap<String, String> testdata, String description)
			throws ClientProtocolException, IOException, InterruptedException {
		JSONObject apiResponse;
		try {
			client = createAcceptSelfSignedCertificateClient();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			Log.exception(e);
		}

		getSetUp(url, testdata);
		long startTime = ExecutionTimer.startTime();
		response = client.execute(httpGet);
		Log.message("Time taken by " + description + " API : " + ExecutionTimer.elapsedTime(startTime) + " seconds");
		validateApi(response, description);
		result = getResult(response);
		String res = result.toString().trim();

		apiResponse = new JSONObject(res.substring(res.indexOf('{')));
		if(apiResponse==null || responseCode!=200){
			Log.fail("API Response is NOT successful");
		}
		
		return apiResponse;
	}

	public void getSetUp(String url , LinkedHashMap<String, String> testdata) throws ClientProtocolException, IOException{
		
		builder = new URIBuilder();
		builder.setPath(url);
		Set entrySet = testdata.entrySet();
		Iterator it= entrySet.iterator();
		while(it.hasNext()){
			Map.Entry param = (Map.Entry)it.next();
			builder.setParameter(param.getKey().toString(), 
					param.getValue().toString().equals("null")?"":param.getValue().toString());
		}
		Log.message("API URL :  "+ builder);
		httpGet = new HttpGet( builder.toString() );
		httpGet.addHeader("Content-Type", "application/json");
//		client = HttpClientBuilder.create().build();
	}
	
	// Post Call
	public JSONObject postFormDataCall(String url , List<NameValuePair> formdata 
			, String description) throws IOException{
		JSONObject apiResponse;
		try {
			client = createAcceptSelfSignedCertificateClient();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			Log.exception(e);
		}
		
		Log.message(description +" API URL: "+url);
		Log.message(description +" API Params: "+formdata);
		
		postSetUpForFormData(url, formdata);
		long startTime = ExecutionTimer.startTime();
		response = client.execute(httpPost);
		if(!description.contains("-"))
			Log.message("Time taken by "+description+" API is "+ExecutionTimer.elapsedTime(startTime)+" seconds");
		validateApi(response, description);
		result = getResult(response);
		
		apiResponse = new JSONObject(result.toString());
		if(apiResponse==null || responseCode!=200){
			Log.fail("API Response is NOT successful");
		}
		return  new JSONObject(result.toString());
		
	}
	
	public void postSetUpForFormData(String url , List<NameValuePair> formdata){
		httpPost= new HttpPost(url);
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formdata , Consts.UTF_8);
		httpPost.setEntity(entity);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
//		client = HttpClientBuilder.create().build();
	}
	
	
	public void postSetUp(String url, LinkedHashMap<String , String> params) throws UnsupportedEncodingException{
		builder = new URIBuilder();
		httpPost= new HttpPost( url);
		Log.message("API URL :  "+ httpPost);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
		
		Set entrySet = params.entrySet();
		Iterator it= entrySet.iterator();
		while(it.hasNext()){
			Map.Entry param = (Map.Entry)it.next();
			builder.setParameter(param.getKey().toString(), 
					param.getValue().toString().equals("null")?"":param.getValue().toString());
		}
		System.out.println(builder.toString());
		
		client = HttpClientBuilder.create().build();
	}
	
	
	public JSONObject postCall(String url , LinkedHashMap<String , String> params, String description) throws ClientProtocolException, IOException, InterruptedException{
		postSetUp(url, params);
		long startTime = ExecutionTimer.startTime();
		response = client.execute(httpPost);
		Log.message("Time taken by "+description+" API :  "+ExecutionTimer.elapsedTime(startTime)+" seconds");
		validateApi(response, description);
		result = getResult(response);
		return  new JSONObject(result.toString());
	}

	public void validateApi(HttpResponse response , String description){
		responseCode = response.getStatusLine().getStatusCode();
		Log.assertThat(responseCode==200
				, description +" API successfully invoked"
				, description +" API is down , status code = "
						+ response.getStatusLine().getStatusCode()
						+"Status "+response.getStatusLine().getReasonPhrase()
				);
	}

	public String getURL(String url ,LinkedHashMap<String, String> testdata) throws ClientProtocolException, IOException, InterruptedException
	{
		URIBuilder builder = new URIBuilder();
		builder.setPath(ReadProperties.getProperty("TripUrlOnline")+url);
		Set entrySet = testdata.entrySet();
		Iterator it= entrySet.iterator();
		while(it.hasNext()){
			Map.Entry param = (Map.Entry)it.next();
			builder.setParameter(param.getKey().toString(), 
					param.getValue().toString().equals("null")?"":param.getValue().toString());
		}	 	
		return builder.toString();
	}

	
	private static CloseableHttpClient createAcceptSelfSignedCertificateClient()
            throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        // use the TrustSelfSignedStrategy to allow Self Signed Certificates
        SSLContext sslContext = SSLContextBuilder
                .create()
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        // we can optionally disable hostname verification. 
        // if you don't want to further weaken the security, you don't have to include this.
        HostnameVerifier allowAllHosts = new NoopHostnameVerifier();
        
        // create an SSL Socket Factory to use the SSLContext with the trust self signed certificate strategy
        // and allow all hosts verifier.
        SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext, allowAllHosts);
        
        // finally create the HttpClient using HttpClient factory methods and assign the ssl socket factory
        return HttpClients
                .custom()
                .setSSLSocketFactory(connectionFactory)
                .build();
    }
	
}
