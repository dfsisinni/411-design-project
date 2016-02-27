package com.design.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    	String recordingUrl = request.getParameter("RecordingUrl");
    }

}