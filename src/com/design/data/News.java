package com.design.data;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.design.communicate.Communicate;

public class News {
	public News(){
	
	}
	
	public static void getNews(String query, String type){
		
		query=query.replace(" ", "+");
		
		URL url = null;
		try {
			url = new URL("https://ajax.googleapis.com/ajax/services/search/news?v=1.0&q="+query);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			InputStream is = url.openStream();
			JsonReader json = Json.createReader(is);
			JsonObject obj = json.readObject();
			JsonArray results = obj.getJsonObject("responseData").getJsonArray("results");
			
			String finalMessage = "";
			
			int num = 3;
			
			if (type.equals("sms")) {
				if (results.size() > 5) {
					num = 5;
				} else {
					num = results.size();
				}
			}
			
			for (int i = 0; i < num; i++) {
				finalMessage += (i + 1) + ". ";
				finalMessage += results.getJsonObject(i).getJsonString("titleNoFormatting").getString();
				finalMessage += "\n";
			}
			
			if (type.equals("sms")) {
				Communicate.sendText(finalMessage);
			} else {
				
			}
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
