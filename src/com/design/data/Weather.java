package com.design.data;

import java.io.IOException;

import org.apache.commons.lang3.text.WordUtils;
import org.bitpipeline.lib.owm.OwmClient;
import org.bitpipeline.lib.owm.WeatherData;
import org.bitpipeline.lib.owm.WeatherForecastResponse;
import org.bitpipeline.lib.owm.WeatherStatusResponse;
import org.json.JSONException;

import com.design.communicate.Communicate;

public interface Weather {

	public static void weather (String text) {
    	text = text.toLowerCase();
    	text = text.replace("weather", "");
    	text = text.replace(" in ", "");
    	
    	if (text.toLowerCase().contains("london")  && !text.toLowerCase().contains("uk")) {
    		text = "London,CA";
    	}
    	text = text.trim();
    	
    	if (text.contains(" ")) {
    		latLonWeatherSearch(text);
    	} else {
    		normalWeatherSearch(text);	
    	}
			
    }
    
    public static void latLonWeatherSearch (String text) {
    	String output = "";
    	text = text.replace(" ", "+");
		String [] loc = Maps.getLatLong(text);
		
		if (loc[0] != null && loc[1] != null) {
			OwmClient owm = new OwmClient ();
	    	WeatherStatusResponse currentWeather = null;
	    	
	    	WeatherForecastResponse forecast = null;
	    
			try {
				currentWeather=owm.currentWeatherAroundPoint(Float.parseFloat(loc[0]), Float.parseFloat(loc[1]), 10);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
	    	if (currentWeather.hasWeatherStatus ()) {
	    	    WeatherData weather = currentWeather.getWeatherStatus ().get (0);
	    	    System.out.println("Code " + currentWeather.getCode());
	    	    if (text.equals("London,CA")) {
	    	    	text = "London, ON";
	    	    }
	    	    
	    	    text = text.replace("+", " ");
	    	    output += "Current Temperature in " + WordUtils.capitalize(text) + ": " + Math.round(weather.getTemp() - 273) + " °C, ";
	    	    if (weather.getWindSpeed() > 0) {
	    	    	output += "Wind Speed: " + weather.getWindSpeed() + " km/hr @ " + weather.getWind().getDeg() + "° ";
	    	    }
	    	    
	    	    if (weather.getRain() > 0) {
	    	    	output += "\n Raining at a rate of " + weather.getPrecipitation() + " mm/h. Expected Accumulation: " + weather.getRainObj().getToday() + " mm. ";
	    	    } else if (weather.getSnow() > 0) {
	    	    	output += "\n Snowing at a rate of " + weather.getPrecipitation() + " cm/h. Expected Accumulation: " + weather.getSnowObj().getToday() + " cm.";
	    	    }
	    	    
	    	  
	    	  
	    	}
	    	
	    	System.out.println(output);
	    	
	    	if (output.length() != 0) {
	    		Communicate.sendText(output);
	    	} else {
	    		Communicate.sendText("Weather not found for specified location.");
	    	}
	    	
		} else {
			Communicate.sendText("Weather not found for specified location.");
		}

	}
    
    
    public static void normalWeatherSearch (String orig) {
    	String text = orig;
    	String output = "";
    	OwmClient owm = new OwmClient ();
    	WeatherStatusResponse currentWeather = null;
    	
    	WeatherForecastResponse forecast = null;
    
		try {
			if (text.contains(",")) {
				String [] textArr = text.split(",");
				textArr[0] = textArr[0].trim();
				textArr[1] = textArr[1].trim();
				textArr[0] = textArr[0].replace(" ", "");
				System.out.println("Input:" + textArr[0]);
				currentWeather = owm.currentWeatherAtCity(textArr[0], textArr[1]);
			} else {
				text = text.trim();
				text = text.replace(" ", "");
				System.out.println("Input:" + text);
				currentWeather = owm.currentWeatherAtCity(text);
			}
			forecast = owm.forecastWeatherAtCity(text);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	if (currentWeather.hasWeatherStatus ()) {
    	    WeatherData weather = currentWeather.getWeatherStatus ().get (0);
    	    System.out.println("Code " + currentWeather.getCode());
    	    if (text.equals("London,CA")) {
    	    	text = "London, ON";
    	    }
    	    
    	    output += "Current Temperature in " + WordUtils.capitalize(text) + ": " + Math.round(weather.getTemp() - 273) + " °C, ";
    	    if (weather.getWindSpeed() > 0) {
    	    	output += "Wind Speed: " + weather.getWindSpeed() + " km/hr @ " + weather.getWind().getDeg() + "°, ";
    	    }
    	    
    	    if (weather.getRain() > 0) {
    	    	output += "\n Raining at a rate of " + weather.getPrecipitation() + " mm/h. Expected Accumulation: " + weather.getRainObj().getToday() + " mm. ";
    	    } else if (weather.getSnow() > 0) {
    	    	output += "\n Snowing at a rate of " + weather.getPrecipitation() + " cm/h. Expected Accumulation: " + weather.getSnowObj().getToday() + " cm.";
    	    }
    	    
    	    output += "Description: " + weather.getWeatherConditions().get(0).getDescription();
    	}
    	
    	System.out.println(output);
    	
    	if (output.length() != 0) {
    		Communicate.sendText(output);
    	} else {
    		Communicate.sendText("Weather not found for specified location.");
    	}
    }
    
	
}