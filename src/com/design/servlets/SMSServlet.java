package com.design.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;

/**
 * Servlet implementation class SMSServlet
 */
@WebServlet("/sms")
public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SMSServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if (request.getParameter("From") != null) {
    		//System.out.println(request.getParameter("From"));
            //System.out.println("Query: " + request.getParameter("Body"));
            processQuery(request.getParameter("Body"));
    	}

    }
    
    //uses ibm natural language classifier
    //directs code to correct data source
    public void processQuery (String body) {
    	NaturalLanguageClassifier service = new NaturalLanguageClassifier();
    	service.setUsernameAndPassword("e3e61635-8a28-4e45-91e3-8b9e03cfedd3", "RuR5llwakcJr");

    	Classification classification = service.classify("563C46x20-nlc-361", body);
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
    	
    	//loop through confidence classes
    	for (int i = 0; i < confidence.size(); i++) {
    		if (largest - 0.0 < confidence.get(i).getConfidence()) {
    			success = false;
    		}
    		
    		if (confidence.get(i).getConfidence() > confidence.get(secondLargest).getConfidence()) {
    			secondLargest = i;
    		}
    	}
    	
    	if (success) { //if successful map to correct data source
    		System.out.println(classification.getTopClass());
    		if (classification.getTopClass().equals("\" directions\"")) {
    			googleMaps(body);
    		} else if (classification.getTopClass().equals("\" math\"")) {
    			wolframAlpha(body);
    		}
    	} else { //otherwise unsuccessful
    		unsuccessful(classification.getTopClass(), confidence.get(secondLargest).getName());
    	}
    }
    
    public void unsuccessful(String top, String second) {
    	
    }
    
    public void wolframAlpha(String query) {
    	System.out.println("Wolfram Alpha!");
    }
    
    public void googleMaps (String query) {
    	System.out.println("Google Maps!");
    }

}
