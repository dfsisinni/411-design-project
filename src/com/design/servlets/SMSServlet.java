/**
 * This servlet deals with SMS queries
 */

package com.design.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.design.communicate.ProcessUser;
import com.design.data.Maps;
import com.design.data.News;
import com.design.data.Weather;
import com.design.data.Wolfram;
import com.design.persistence.Queries;
import com.design.persistence.Users;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

@WebServlet("/sms")
public class SMSServlet extends HttpServlet {
	
	public double queryTime;
	
	private static final long serialVersionUID = 1L;
	
	private static Users user = null;

	
	public static String from;
	
    public SMSServlet() {
        super();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	queryTime = System.currentTimeMillis();
    	if (request.getParameter("From") != null) {
    		//System.out.println(request.getParameter("From"));
            //System.out.println("Query: " + request.getParameter("Body"));
    		from = request.getParameter("From");
    		from = "+12896683263";
            processQuery(request.getParameter("Body"));
            user = ProcessUser.userExists(from);
            
            System.out.println(request.getParameter("Body"));
    	}

    }
    
    // Uses IBM natural language classifier
    // Directs code to correct data source
    public void processQuery (String body) {

    	NaturalLanguageClassifier service = new NaturalLanguageClassifier();
    	service.setUsernameAndPassword("6e7a6f54-5d89-4454-8f59-1ba52696f989", "TvAzv6xg9Up8");

    	Classification classification = service.classify("c7fa4ax22-nlc-10514", body);
    	List <ClassifiedClass> confidence = classification.getClasses(); // List of classes
    	
    	double largest = classification.getTopConfidence(); // Get largest
    	boolean success = true;
    	int secondLargestI; // Used to keep track of index of 2nd highest confidence if difference is < 30% between highest & 2nd highest

    	// Assigns initial value to 2nd largest
    	if (classification.getTopClass().equals(confidence.get(0).getName())) {
    		secondLargestI = 1;
    	} else {
    		secondLargestI = 0;
    	}
    	
    	System.out.println(largest);
    	// Loop through confidence classes
    	for (int i = 0; i < confidence.size(); i++) {
    		
    		if (!confidence.get(i).getName().equals(classification.getTopClass())) {
    			if (largest - 0.30 < confidence.get(i).getConfidence()) {
        			success = false;
        		}
        		
        		if (confidence.get(i).getConfidence() > confidence.get(secondLargestI).getConfidence()) {
        			secondLargestI = i;
        		}
    		}
    		
    	}
    	
    	Queries query = new Queries();
		query.setConfidence(classification.getTopConfidence());
		query.setDirections(null);
		query.setNews(null);
		query.setPhone(user);
		query.setQuery(body);
		query.setTime(new Date());
		query.setType("sms");
    	
    	if (success) { // If successful map to correct data source
    		System.out.println(classification.getTopClass());
    		if (classification.getTopClass().equals("directions")) {
    			query.setClass1("directions");
    			Maps.googleMaps(query);
    		} else if (classification.getTopClass().equals("math")) {
    			query.setClass1("math");
    			Wolfram.wolframAlpha(query);
    		} else if (classification.getTopClass().equals("weather")) {
    			query.setClass1("weather");
    			Weather.weather(query);
    		} else if (classification.getTopClass().equals("news")) {
    			query.setClass1("news");
    			News.getNews(query);
    		}
    		else
    		{
    			Wolfram.wolframAlpha(body);
    		}
    	} else { // Otherwise unsuccessful
    		query.setClass1("");
    		query.setSuccessful(false);
    		
    	}
    }
    

}
