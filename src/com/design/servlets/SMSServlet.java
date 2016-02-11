package com.design.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            processQuery(request.getParameter("Body"));
    	}

    }
    
    //uses ibm natural language classifier
    //directs code to correct data source
    public void processQuery (String body) {
    	NaturalLanguageClassifier service = new NaturalLanguageClassifier();
    	service.setUsernameAndPassword("ddb1cffa-dcf7-449d-8c7e-21a613543e12", "qVLlAd0MdnP5");

    	Classification classification = service.classify("c7fa4ax22-nlc-278", body);
    	List <ClassifiedClass> confidence = classification.getClasses(); //list of classes
    	
    	double largest = classification.getTopConfidence(); //get largest
    	boolean success = true;
    	int secondLargest; //used to keep track of 2nd highest confidence if difference is < 30% between highest & 2nd highest

    	//assigns initial value to 2nd largest
    	if (classification.getTopClass().equals(confidence.get(0).getName())) {
    		secondLargest = 1;
    	} else {
    		secondLargest = 0;
    	}
    	
    	System.out.println(largest);
    	//loop through confidence classes
    	for (int i = 0; i < confidence.size(); i++) {
    		
    		if (!confidence.get(i).getName().equals(classification.getTopClass())) {
    			if (largest - 0.30 < confidence.get(i).getConfidence()) {
        			success = false;
        		}
        		
        		if (confidence.get(i).getConfidence() > confidence.get(secondLargest).getConfidence()) {
        			secondLargest = i;
        		}
    		}
    		
    		
    	}
    	
    	if (success) { //if successful map to correct data source
    		System.out.println(classification.getTopClass());
    		if (classification.getTopClass().equals("directions")) {
    			Maps.googleMaps(body);
    		} else if (classification.getTopClass().equals("math")) {
    			Wolfram.wolframAlpha(body);
    		} else if (classification.getTopClass().equals("weather")) {
    			Weather.weather(body);
    		}
    	} else { //otherwise unsuccessful
    		unsuccessful(classification.getTopClass(), confidence.get(secondLargest).getName());
    	}
    }
    
    public void unsuccessful(String top, String second) {
    	System.out.println("unsuccessful");
    }

}
