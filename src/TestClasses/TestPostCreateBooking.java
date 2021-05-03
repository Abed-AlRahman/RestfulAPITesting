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

public class TestPostCreateBooking {

	@Test
	public void testCreatBooking() throws Exception {
		/*
		 * This method sends a complete JSON object, then it sends another request with the
		 *  id of the created object.
		 *  Both should be Equal. 
		 * */
		
		// 1. Open Connection --- HttpURLConnection
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.NewBookingJSONFile);
		
		JSONObject expected = (JSONObject) JSONUtils.convertStringToJSON(resquestJSONObject);
		// 3. Post Request
		RestClientHandler.sendPost(connection, resquestJSONObject, HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		String response = RestClientHandler.readResponse(connection);
		// 5. convert String to JSON
		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response);
		String expected_id = jsonObject.get("bookingid").toString();
		
		// 6. validation data jsonObject==response
		// (https://restful-booker.herokuapp.com/booking/id)
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
	public void testCreatBookingMissingAField() throws Exception {
		/*
		 * This send a JSON object missing one field. The response should be 
		 * Internal server error ( 500 ) because all the fields are required.
		 * */
		// 1. Open Connection --- HttpURLConnection
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.NewBookingJSONFile);
		
		JSONObject expected = (JSONObject) JSONUtils.convertStringToJSON(resquestJSONObject);
		expected.remove("firstname");
		// 3. Post Request
		RestClientHandler.sendPost(connection, expected.toJSONString(), HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		assertTrue(connection.getResponseCode() == 500);
		
	}

}
