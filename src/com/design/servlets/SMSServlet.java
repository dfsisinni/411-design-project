package com.design.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

@WebServlet("/sms")
public class SMSServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String ACCOUNT_SID = "ACa5505abc27a7d474f55d817367c57f45";
	public static final String AUTH_TOKEN = "11685b45e64715afef26e26781f5ad98";

	
	private String from;
	
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
    
    public void wolframAlpha(String body) {
    	String result = "";
    	
    	 WAEngine engine = new WAEngine();
    	 engine.setAppID("7AHUTR-UV58KYXA8Q");
    	 
    	 WAQuery query = engine.createQuery();
    	 query.setInput(body);
    	 
    	 WAQueryResult queryResult = null;
		try {
			queryResult = engine.performQuery(query);
		} catch (WAException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	 
    	 if (queryResult.isError()) {
    		 
    	 } else {
    		 for (WAPod pod : queryResult.getPods()) {
    			 if (!pod.isError()) {
    				 for (WASubpod subpod : pod.getSubpods()) {
    					 for (Object element : subpod.getContents()) {
    						 if (element instanceof WAPlainText) {
    							  
    							 if (body.toLowerCase().contains("derivative") || body.toLowerCase().contains("deriv")) {
    								 if (((WAPlainText) element).getText().contains("d/dx")) {
    									 result +=  ((WAPlainText) element).getText();
    								 }
    							 } else {
    								 if (!((WAPlainText) element).getText().contains("Plot")) {
        								 System.out.println(((WAPlainText) element).getText());  
        								 result += ((WAPlainText) element).getText();
        							 }
    							 }
    						 }
    					 }
    				 }
    			 }
    		 }
    	 }
    	 sendText(result);
    }
    
    public void googleMaps (String query) {
    	System.out.println("Google Maps!");
    }
    
    public void sendText (String text) {
    	TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
    	 
        // Build a filter for the MessageList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", text));
        params.add(new BasicNameValuePair("To", from));
        params.add(new BasicNameValuePair("From", "+12892721224"));
     
        MessageFactory messageFactory = client.getAccount().getMessageFactory();
        Message message = null;
		try {
			message = messageFactory.create(params);
		} catch (TwilioRestException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(message.getSid());
    }

}
