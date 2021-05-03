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

public class TestGetRestAPI {

	@Ignore
	@Test
	public void TestRestClientHandler() throws IOException {
		// 1. connect to server and open connection (get HttpURLConnection object)
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection.getResponseCode());
		assertTrue("unable to connect to webservice", connection.getResponseCode() == 200);
		// 3. reading response using input stream
		String response = RestClientHandler.readResponse(connection);
		System.out.println(response);
		assertTrue("Data is empty", !response.equals(""));

	}
	@Ignore
	@Test
	public void testCreatBooking() throws Exception {
		// 1. Open Connection --- HttpURLConnection
		HttpURLConnection connection = RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.NewBookingJSONFile);
		
		JSONObject lol = (JSONObject) JSONUtils.convertStringToJSON(resquestJSONObject);
		// 3. Post Request
		RestClientHandler.sendPost(connection, resquestJSONObject, HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		String response = RestClientHandler.readResponse(connection);
		System.out.println(response);
		// 5. convert String to JSON
		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response);
//		jsonObject = ((JSONObject) jsonObject.get("booking"));
//		String bookingid = jsonObject.get("bookingid").toString();
//		String additionalneeds = ((JSONObject) jsonObject.get("booking")).get("additionalneeds").toString();
//
//		String checkin = ((JSONObject) (((JSONObject) jsonObject.get("booking")).get("bookingdates"))).get("checkin")
//				.toString();
//		System.out.println("bookingid=" + bookingid);
//		System.out.println("additionalneeds=" + additionalneeds);
//		System.out.println("checkin=" + checkin);
		System.out.println("it's me" + lol.get("additionalneeds").toString());
//		jsonObject.remove("bookingid");
//		System.out.println(jsonObject.remove(jsonObject.get("\"bookingid\"")));
		System.out.println(jsonObject);
		System.out.println(lol.equals(jsonObject));
//		lol.equals(jsonObject);
		// 6. validation data jsonObject==response
		// (https://restful-booker.herokuapp.com/booking/id)
		System.out.println(connection.getResponseCode());
	}

	@Ignore
	@Test
	public void testUpdateUser() throws Exception {
		// 1. Open Connection --- HttpURLConnection
		String url = URLs.ReqResAPI+"2";
		//url = url.replace("userID", "2");
		System.out.println(url);
		HttpURLConnection connection = RestClientHandler.connectServer(url, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		// 2. Prepare Json Object
		String resquestJSONObject = JSONUtils.readJSONObjectFromFile(FilesPaths.UpdateUserJSONFile);
		System.out.println(resquestJSONObject);
		// 3. Post Request
		RestClientHandler.sendPut(connection, resquestJSONObject, HTTPRequestsContentTypes.JSON);
		// 4. Reading Response
		System.out.println(connection.getResponseCode());
		String response = RestClientHandler.readResponse(connection);
		System.out.println(response);
	}
	
	@Test
	public void FilterByNameExist() throws IOException , Exception{
		
		// get url with exist id 
		String url = URLs.BOOKING+"?firstname=Mark&lastname=Jackson";
		System.out.println(url);

		
		// 1. connect to server and open connection (get HttpURLConnection object)
		HttpURLConnection connection = RestClientHandler.connectServer(url, HTTPMethod.GET,
				HTTPRequestsContentTypes.JSON);
		
		// 2. validate if the connection is successfully openned
		System.out.println("connection.getResponseCode() : " + connection.getResponseCode());
		assertTrue("unable to connect to webservice", connection.getResponseCode() == 200);
		
		// 3. reading response using input stream
		String response = RestClientHandler.readResponse(connection);
		System.out.println(response);

		// 5. convert String to JSON
//		JSONObject jsonObject = (JSONObject) JSONUtils.convertStringToJSON(response);
//		JSONArray jsonArr = (JSONArray) JSONUtils.convertStringToJSON(response);
//		JSONObject a = (JSONObject)jsonArr.get(0);
//		System.out.println(a.get("bookingid"));

		assertTrue("Name Not Valid", false);
}


//	@Test
//	public void TestPostData() throws Exception
//	{
//		//1. Get HttpURLConnection
//		HttpURLConnection connection= RestClientHandler.connectServer(URLs.BOOKING, HTTPMethod.POST , HTTPRequestsContentTypes.JSON);
//		
//		//2. Read JSON file		
//		String json=JSONUtils.readJSONObjectFromFile(FilesPaths.CreatBookingJSONFile);
//		
//		//3.sent post request
//		RestClientHandler.sendPost(connection, json, HTTPRequestsContentTypes.JSON);
//		
//		//4. Read Response
//		String response=RestClientHandler.readResponse(connection);
//		System.out.println(response);
//		//5. convert string to json object 
//		JSONObject obj= (JSONObject) JSONUtils.convertStringToJSON(response);
//		System.out.println(obj.get("bookingid"));
//	}

}
