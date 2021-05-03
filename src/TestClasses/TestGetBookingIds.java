package TestClasses;
import static org.junit.Assert.*;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.junit.Ignore;
import org.junit.Test;

import Links.FilesPaths;
import Links.URLs;
import Utils.JSONUtils;
import enums.HTTPMethod;
import enums.HTTPRequestsContentTypes;
import requestHandling.HandleRequestReponse;
import requestHandling.RestClientHandler;

public class TestGetBookingIds {

	@Test
	public void getBookingID_AllInfo() throws Exception{
		/*
		 * In this method I will only test whether the API is giving back a response or not.
		 * */
		// 1. connect to server and open connection (get HttpURLConnection object)
		String query = "";
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection = RestClientHandler.connectServer(URL, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection.getResponseCode());
		assertTrue("unable to connect to webservice", connection.getResponseCode() == 200);
		// 3. reading response using input stream
		String response = RestClientHandler.readResponse(connection);
		System.out.println(response);
		assertTrue("Data is empty", !response.equals(""));
	}
	
	@Test
	public void getBookingID_SpecificFirstname() throws Exception{
		/*
		 * In this method I will be creating a new booking to retrieve its bookingid and then I will be 
		 * 		testing getUserID for that bookingid.
		 * */
		// **************************************************************
		// STEP 1
		// 1. Open Connection --- HttpURLConnection
		HttpURLConnection connection1 = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String requestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.getByFirstNameFile);
		// 3. Post Request
		RestClientHandler.sendPost(connection1, requestJSONObject, HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		String response1 = RestClientHandler.readResponse(connection1);
		// 5. convert String to JSON
		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response1);
		String expected_id = jsonObject.get("bookingid").toString();
		// **************************************************************
		// 1. connect to server and open connection (get HttpURLConnection object)
		
		String query = "/?firstname=Abedtester&lastname=willitwork";
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection2 = RestClientHandler.connectServer(URL, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection2.getResponseCode());
		assertTrue("unable to connect to webservice", connection2.getResponseCode() == 200);
		// 3. reading response using input stream
		String response2 = RestClientHandler.readResponse(connection2);
		JSONArray jsonArr = (JSONArray) JSONUtils.convertStringToJSON(response2);
		boolean found_the_id = false;
		// 4. looping over the array of the bookngids' find if the id showed or now
		for(int i = 0 ; i < jsonArr.size() ; i++ ) {
			String actual_id = ((JSONObject)jsonArr.get(i)).get("bookingid").toString();
			if(actual_id.equals(expected_id))
				found_the_id = true;
		}
		assertTrue("id was not found", found_the_id);
	}
	
	@Test
	public void getBookingID_SpecificID() throws Exception{
		/*
		 * In this method I will be creating a new booking to retrieve its bookingid and then I will be 
		 * 		testing getUserID for that bookingid.
		 * */
		// **************************************************************
		// STEP 1
		// 1. Open Connection --- HttpURLConnection
		HttpURLConnection connection1 = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String requestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.getByFirstNameFile);
		// 3. Post Request
		RestClientHandler.sendPost(connection1, requestJSONObject, HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		String response1 = RestClientHandler.readResponse(connection1);
		// 5. convert String to JSON
		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response1);
		String expected_id = jsonObject.get("bookingid").toString();
		// **************************************************************
		// 1. connect to server and open connection (get HttpURLConnection object)
		
		String query = "/"+expected_id;
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection2 = RestClientHandler.connectServer(URL, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		RestClientHandler.sendGet(connection2, "", HTTPRequestsContentTypes.JSON);
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection2.getResponseCode());
		assertTrue("unable to connect to webservice", connection2.getResponseCode() == 200);
		// 3. reading response using input stream
		String response2 = RestClientHandler.readResponse(connection2);
		JSONObject responseJSON = (JSONObject) JSONUtils.convertStringToJSON(response2); 
		assertTrue(jsonObject.get("booking").equals(responseJSON));
	}
	
	@Test
	public void getBookingID_MAXID() throws Exception{
		String query = "/44444";
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection2 = RestClientHandler.connectServer(URL, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		RestClientHandler.sendGet(connection2, "", HTTPRequestsContentTypes.JSON);
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection2.getResponseCode());
		assertTrue("Someone picked a big ID", connection2.getResponseCode() == 404);
	}
}
