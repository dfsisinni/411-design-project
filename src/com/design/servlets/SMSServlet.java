/**
 * This servlet deals with SMS queries
 * Shayn test01
 */
package com.design.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.design.communicate.ProcessUser;
import com.design.data.Maps;
import com.design.data.Weather;
import com.design.data.Wolfram;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

@WebServlet("/sms")
public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	public static String from;
	
    public SMSServlet() {
        super();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if (request.getParameter("From") != null) {
    		//System.out.println(request.getParameter("From"));
            //System.out.println("Query: " + request.getParameter("Body"));
    		from = request.getParameter("From");
    		from = "+12896683263";
            //processQuery(request.getParameter("Body"));
            ProcessUser.userExists(from);
    	}

    }
    
    // Uses IBM natural language classifier
    // Directs code to correct data source
    public void processQuery (String body) {
    	NaturalLanguageClassifier service = new NaturalLanguageClassifier();
    	service.setUsernameAndPassword("6e7a6f54-5d89-4454-8f59-1ba52696f989", "TvAzv6xg9Up8");

    	Classification classification = service.classify("c7e487x21-nlc-11193", body);
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
    	
    	if (success) { // If successful map to correct data source
    		System.out.println(classification.getTopClass());
    		if (classification.getTopClass().equals("directions")) {
    			Maps.googleMaps(body);
    		} else if (classification.getTopClass().equals("math")) {
    			Wolfram.wolframAlpha(body);
    		} else if (classification.getTopClass().equals("weather")) {
    			Weather.weather(body);
    		}
    	} else { // Otherwise unsuccessful
    		unsuccessful(classification.getTopClass(), confidence.get(secondLargestI).getName());
    	}
    }
    
    public void unsuccessful(String top, String second) {
    	System.out.println("unsuccessful");
    }

}
