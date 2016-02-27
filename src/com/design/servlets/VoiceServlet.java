package com.design.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.design.communicate.ProcessUser;
import com.twilio.sdk.verbs.Gather;
import com.twilio.sdk.verbs.Record;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

/**
 * Servlet implementation class VoiceServlet
 */
@WebServlet("/voice")
public class VoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VoiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	
	 public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
	    	if (request.getParameter("From") != null) {
	    		
	    		System.out.println("Entering Voice Servlet");
	    		
	    		TwiMLResponse twiml = new TwiMLResponse();
	    		
	    		Gather gather = new Gather();
	    		gather.setAction("/handle-key");
	    		gather.setNumDigits(1);
	    		gather.setMethod("POST");
	    		
	    		Say say = new Say("Input Query");
	    		Say sayInGather = new Say("Time to Gather");
	    		
	    		try {
	    			gather.append(sayInGather);
	    			twiml.append(say);
	    			twiml.append(gather);
	    		} catch (TwiMLException ex) {
	    			ex.printStackTrace();
	    		}

	            
	            response.setContentType("application/xml");
	            response.getWriter().println(twiml.toXML());

	    	}
	    	

	    }
}
