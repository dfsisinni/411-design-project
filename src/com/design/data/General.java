package com.design.data;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public interface General {

	public static void googleKnowledge (String body) {
		String query = "https://kgsearch.googleapis.com/v1/entities:search?query="
				+ body + "&key=AIzaSyAjXKpbYwL4CFbXVtNbLKKE9cOrlrsI05Q&limit=1&indent=True";
		
		String output = ".\n";
		
		URL url = null;
		
		try {
			url = new URL(query);
			InputStream is = null;
			try {
				is = url.openStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			JsonReader json = Json.createReader(is);
			JsonObject obj = json.readObject();
			
			//obj.getJsonArray("itemListElement").getJsonObject(0)
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
