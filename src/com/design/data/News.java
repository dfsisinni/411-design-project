package com.design.data;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.design.communicate.Communicate;
import com.design.communicate.ProcessUser;
import com.design.persistence.*;
import com.design.servlets.SMSServlet;

public class News {
	public News(){
	
	}
	
	public static void getNews(Queries qu){
		
		String query = qu.getQuery();
		String type = qu.getType();
		
		String finalMessage = "";
		
		String publisher;
		
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
			
			int num = 3;
			
			if (type.equals("sms")) {
				if (results.size() > 5) {
					num = 5;
				} else {
					num = results.size();
				}
			}
			publisher = results.getJsonObject(0).getJsonString("publisher").getString();
			
			for (int i = 0; i < num; i++) {		
				finalMessage += (i + 1) + ". ";
				finalMessage += results.getJsonObject(i).getJsonString("titleNoFormatting").getString();
				finalMessage += "\n";
			}
			
			qu.setSuccessful(true);
			qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			finalMessage = null;
			publisher = null;
			qu.setSuccessful(false);
			qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
		}
		
		ProcessUser.persistNews(qu, publisher, finalMessage);
	}

}
