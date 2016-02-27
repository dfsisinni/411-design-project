package com.design.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.design.communicate.ProcessUser;
import com.twilio.sdk.verbs.Record;
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
	    		TwiMLResponse twiml = new TwiMLResponse();
	    	    Record record = new Record();
	            record.setMaxLength(30);
	            // You may need to change this to point to the location of your
	            // servlet 
	            record.setAction("/handle-recording");
	            try {
	                twiml.append(record);
	            } catch (TwiMLException e) {
	                e.printStackTrace();
	            }

	    	}
	    	System.out.println("here");

	    }
}
