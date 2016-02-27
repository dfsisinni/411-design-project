package com.design.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.twilio.sdk.verbs.Play;
import com.twilio.sdk.verbs.Say;
import com.twilio.sdk.verbs.TwiMLException;
import com.twilio.sdk.verbs.TwiMLResponse;

/**
 * Servlet implementation class HandleRecording
 */
@WebServlet("/handle-recording")
public class HandleRecording extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HandleRecording() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	System.out.println("Handling Recording");
    	
    	 String recordingUrl = request.getParameter("RecordingUrl");
         TwiMLResponse twiml = new TwiMLResponse();
         if (recordingUrl != null) {
             try {
                 twiml.append(new Say("Thanks for howling... take a listen to what you howled."));
                 twiml.append(new Play(recordingUrl));
                 twiml.append(new Say("Goodbye"));
             } catch (TwiMLException e) {
                 e.printStackTrace();
             }
         } else {
             response.sendRedirect("/twiml");
             return;
         }
  
         response.setContentType("application/xml");
         response.getWriter().print(twiml.toXML());
    }

}