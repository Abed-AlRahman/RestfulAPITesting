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

public class TestDeleteBooking {

	@Test
	public void testDeleteValidID() throws Exception{
		
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

		
		HttpURLConnection connection2 = RestClientHandler.connectServer(URLs.Autho, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject2 = JSONUtils.readJSONObjectFromFile(FilesPaths.authFile);
				
		RestClientHandler.sendPost(connection2, resquestJSONObject2, HTTPRequestsContentTypes.JSON);

		String response2 = RestClientHandler.readResponse(connection2);

		JSONObject jsonObject2 = (JSONObject) JSONUtils.convertStringToJSON(response2);
		String expected_token = jsonObject2.get("token").toString();

		String query = "/" + expected_id;
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection3 = RestClientHandler.connectServer(URL, HTTPMethod.DELETE,
				HTTPRequestsContentTypes.JSON);
		
		connection3.setRequestProperty("Cookie", "token=" + expected_token);
		RestClientHandler.sendDelete(connection3, "", HTTPRequestsContentTypes.JSON);

		System.out.println(connection3.getResponseCode());
		assertTrue(connection3.getResponseCode() == 201);
		
	}
	
	@Test
	public void testDeleteInvalidID() throws Exception{
				
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.Autho, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.authFile);
				
		RestClientHandler.sendPost(connection, resquestJSONObject, HTTPRequestsContentTypes.JSON);

		String response = RestClientHandler.readResponse(connection);

		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response);
		String expected_token = jsonObject.get("token").toString();

		String query = "/10000";
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection2 = RestClientHandler.connectServer(URL, HTTPMethod.DELETE,
				HTTPRequestsContentTypes.JSON);
		
		connection2.setRequestProperty("Cookie", "token=" + expected_token);
		RestClientHandler.sendDelete(connection2, "", HTTPRequestsContentTypes.JSON);

		System.out.println(connection2.getResponseCode());
		
		assertTrue(connection2.getResponseCode() == 405);
	}
	
	@Test
	public void testDeleteNoToken() throws Exception{
				
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.Autho, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.authFile);
				
		RestClientHandler.sendPost(connection, resquestJSONObject, HTTPRequestsContentTypes.JSON);

		String response = RestClientHandler.readResponse(connection);

		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response);
		String expected_token = jsonObject.get("token").toString();

		String query = "/10000";
		String URL = URLs.BOOKING + query;
		HttpURLConnection connection2 = RestClientHandler.connectServer(URL, HTTPMethod.DELETE,
				HTTPRequestsContentTypes.JSON);
		
		RestClientHandler.sendDelete(connection2, "", HTTPRequestsContentTypes.JSON);

		System.out.println(connection2.getResponseCode());
		
		assertTrue(connection2.getResponseCode() == 403);
	}

}
