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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.design.communicate.Communicate;

public class Maps {

	 public static void googleMaps (String query) {
		 String [] results = detDirections(query);
		 getAPIResults(results); 
	 }
	 
	 private static void getAPIResults (String [] parameters) {
		 String query = "https://maps.googleapis.com/maps/api/directions/json?origin=" + parameters[0] +"&"
		 		+ "destination=" + parameters[1] + "&key=AIzaSyBbcq6id_NE2X_M-Fr7vXqCV6DLLYcDZ78";
		 query = query.replace(" ", "+");
		 
		 URL url = null;
		 try {
			url = new URL(query);
		 } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
			
		 try {
			InputStream is = url.openStream();
			JsonReader json = Json.createReader(is);
			JsonObject obj = json.readObject();	
			
			JsonArray arr = null;
			
			try {
				arr = obj.getJsonArray("routes").getJsonObject(0).getJsonArray("legs").getJsonObject(0).getJsonArray("steps");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
			
			
			String directions = "";
			
			for (int i = 0; i < arr.size(); i++) {
				directions += (i + 1) +". ";
				directions += arr.getJsonObject(i).getJsonString("html_instructions").getString();
				directions += "\n";
			}
			
			String [] dumbPatterns = {"\u003cb\u003e", "\u003c/b\u003e"};
			for (int i=0; i < dumbPatterns.length; i++)
			{
				directions = directions.replace(dumbPatterns[i], "");
			}
			
			directions = directions.replace("<div style=\"font-size:0.9em\">", " ");
			directions = directions.replace("</div>", "");
			
			
			System.out.println(directions);
			
			Communicate.sendText(directions);
			
		 } catch (Exception ex) {
			ex.printStackTrace();
		 }
	 }
	 
	 
	 private static String [] detDirections(String query)
	 {
		 String [] returnVariable = {"", ""};
		 
		 query=query.toLowerCase();
		 String junkarray[]= {" how ", " directions ", " direction "," go "," starting "," at ", " in ", " get "};
		 for(int i=0; i<junkarray.length; i++)
		 {
			 query=query.replace(junkarray[i], " ");
		 }
		 String brokenArray[]= query.split(" ");
                 
                 for (int i = 0; i < junkarray.length; i++) {
                     if (brokenArray[0].equals(junkarray[i].substring(1, junkarray[i].length() - 1))) {
                         brokenArray[0] = "";
                     }
                 }
		 
		 List <Integer> toList = new ArrayList<Integer>();
		 List <Integer> fromList = new ArrayList<Integer>();
		 
		 for (int i=0;i<brokenArray.length;i++)
		 {
			 if (brokenArray[i].equals("to"))
			 {
				toList.add(i);
			 }
			 else if (brokenArray[i].equals("from"))
			 {
				 fromList.add(i);
			 }
		 }
		 
		 
		 if (toList.size() == 0)
		 {
			 if (fromList.size() == 0) 
			 {
				 //Communicate.sendText("Unable to parse your directions query. Please try rephrasing.");
				 returnVariable[0] = "";
				 returnVariable[1] = "";
			 } 
			 else
			 {
				int from = fromList.get(fromList.size() - 1);
				for (int i = 0; i < from; i++) 
				{
					returnVariable[1] += brokenArray[i] +" ";
				}
				for (int i = from + 1; i < brokenArray.length; i++) 
				{
					returnVariable[0] += brokenArray[i] + " ";
				}
			 }
		 }
		 else
		 {
			 if(fromList.size()==0)
			 {
				 int to = toList.get(toList.size() - 1);
					for (int i = 0; i < to; i++) 
					{
						returnVariable[0] += brokenArray[i] +" ";
					}
					for (int i = to + 1; i < brokenArray.length; i++) 
					{
						returnVariable[1] += brokenArray[i] + " ";
					}
			 }
			 else
			 {
				int to = toList.get(toList.size() - 1);
				int from = fromList.get(fromList.size() - 1);
				
				if (from < to)
				{
					for (int i=from+1; i<to ; i++)
					{
						returnVariable[0] += brokenArray[i] +" ";
					}
					for (int i=to+1; i<brokenArray.length ; i++)
					{
						returnVariable[1] += brokenArray[i] +" ";
					}	
				}
				else
				{
					for (int i=to+1; i<from; i++)
					{
						returnVariable[1] += brokenArray[i] +" ";
					}
					for (int i=from+1; i<brokenArray.length; i++)
					{
						returnVariable[0] += brokenArray[i] +" ";
					}
				}
			 }
		 }
		 
                 return returnVariable;
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
