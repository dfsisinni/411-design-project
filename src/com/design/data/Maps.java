/**
 * The Maps Module, to handle all location based data
 */
package com.design.data;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public interface Maps {

	 public static void googleMaps (String query) {
	    	System.out.println("Google Maps!");
	 }
	 
	 
	 private static String [] (String query) {
		 query=query.toLowerCase();
		 String junkarray[]= {" how ", " directions ", " direction "," go "," starting "," at "};
		 for(int i=0; i<junkarray.length; i++)
		 {
			 query=query.replace(junkarray[i], "");
		 }
		 String brokearray[]= query.split(" ");
		 
		 List <Integer> toList = new ArrayList<Integer>();
		 List <Integer> fromList = new ArrayList<Integer>();
		 
		 
		 
		 return null; 	 
	 	}
	 
	 
	 public static String [] getLatLong (String text) {
	    	String key = "AIzaSyAjXKpbYwL4CFbXVtNbLKKE9cOrlrsI05Q";
	    	String query = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + text + "&types=geocode&key=" + key;
	    	
	    	
	    	URL url = null;
			try {
				url = new URL(query);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String lat = null;
			String lon = null;
			
			try {
				InputStream is = url.openStream();
				JsonReader json = Json.createReader(is);
				JsonObject obj = json.readObject();
				String place = obj.getJsonArray("predictions").getJsonObject(0).getJsonString("place_id").getString();
				
				query = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + place + "&key=" + key;
				url = new URL(query);
				
				is = url.openStream();
				json = Json.createReader(is);
				obj = json.readObject().getJsonObject("result").getJsonObject("geometry").getJsonObject("location");
				
				lat = String.valueOf(obj.getJsonNumber("lat"));
				lon = String.valueOf(obj.getJsonNumber("lng"));
				
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}	
			
			
			String [] arr = {lat, lon};
			return arr;
	    }
	
}
