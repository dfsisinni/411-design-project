/**
 * The Wolfram Module, deals with queries relevant to Wolfram Alpha
 * Specifically, this module handles:
 * 
 * @author Shayaan Syed Ali
 * @version Feb 27, 2016
 */
package com.design.data;

import com.design.communicate.Communicate;
import com.design.communicate.ProcessUser;
import com.design.persistence.Queries;
import com.design.servlets.SMSServlet;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

// This was originally an interface, it may be better as a class or it may not be
public class Wolfram
{
//	static int INPUT_POD_ID = 
	static int NO_PODS_TO_INCL = 3;
//	static int alwaysInclIDs = {}; // POD IDs to always include, if they exist for a query

	public static boolean wolframAlpha(Queries qu)
	{
    	String queryStr = qu.getQuery();
		StringBuilder result = new StringBuilder(".\n");
    	
    	// Basic engine setup
    	WAEngine engine = new WAEngine();
    	engine.setAppID("7AHUTR-UV58KYXA8Q");
    	 
    	// Set up query with necessary parameters
    	WAQuery query = engine.createQuery();
    	query.setInput(queryStr);
    	// query.addIncludePodID(arg0);
    	
    	// Query retrieval and error handling
    	WAQueryResult queryResult = null;
		try
		{
			queryResult = engine.performQuery(query);
		}
		catch (WAException e)
		{
			// WIP: Handle WA engine errors
			e.printStackTrace(); // Might not want this; need to generate a response
			qu.setSuccessful(false);
    		qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
    		ProcessUser.persistWolfram(qu);
    		return false;
		}
		
		// Error case
		if (queryResult.isError())
		{
			qu.setSuccessful(false);
    		qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
    		ProcessUser.persistWolfram(qu);
    		return false;
		}
		else if (!queryResult.isSuccess())
		{
			qu.setSuccessful(false);
    		qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
    		ProcessUser.persistWolfram(qu);
    		return false;
		}
		// Response generation
		else
		{
			// To do:
			// 1. Determine what the query is relevant to
			// 2. Organize the query as needed (if needed)
			// 3. Set up assumptions (as needed)
			// 4. Get WA engine response
			// 5. Sort through response
			// 6. Generate result with parts of response

			// Alt method:
			// Include first few pods in output
			// Have some pods, which if they exist for this entry, are always included
			
			// Retrieve result pods
			WAPod[] pods = queryResult.getPods();
			
			// Result generation
			
			// Include pods which must always be included first (if they exist)
			// WIP
			// First few pods included
			int podI = 0;
			if (pods[podI].getID().equals("Input")) // Exclude 'Input interpretation' pod
			{
				podI++;
			}
			for (/* int podI = 0 */ ; podI < NO_PODS_TO_INCL && podI < pods.length; podI++)
			{
				if (!pods[podI].isError())
				{
					result.append(pods[podI].getTitle());
					result.append(": \n");
					WASubpod[] subpods = pods[podI].getSubpods();
					for (WASubpod subpod : subpods)
					{
						// Iterate through elements of the subpod and include plaintext elements
						for (Object element : subpod.getContents())
						{
							if (element instanceof WAPlainText)
							{
								result.append(((WAPlainText) element).getText());
							}
						}
						result.append('\n');
					}
				}
				else
				{
					podI--;
				}
			}
			
			// Include pods which must always be included at the end (if they exist)
			// WIP
		}

			


    	 // Send results through Communicate Module
		qu.setSuccessful(true);
		qu.setResponseTime(((double) System.currentTimeMillis() - SMSServlet.queryTime)/1000);
		ProcessUser.persistWolfram(qu, result.toString());
		
		return true;
    	 
	} // wolframAlpha method
		
} // Wolfram class
    	 
    	// Dane's old code (in 'Response generation' else block
//			outerloop:
//
//				for (WAPod pod : queryResult.getPods()) {
//
//					if (!pod.isError()) {
//
// 				 for (WASubpod subpod : pod.getSubpods()) {
// 					 for (Object element : subpod.getContents()) {
// 						 if (element instanceof WAPlainText) {
// 							  
// 							 if (body.toLowerCase().contains("derivative") || body.toLowerCase().contains("deriv")) {
// 								 if (((WAPlainText) element).getText().contains("d/dx")) {
// 									 result +=  ((WAPlainText) element).getText();
// 									 if (body.toLowerCase().contains("integral") || body.toLowerCase().contains("integrate") ||
// 											 body.toLowerCase().contains("derivative")) {
// 										 break outerloop;
// 									 }
// 									 
// 								 }
// 							 } else {
// 								 if (!((WAPlainText) element).getText().contains("Plot")) {
//     								 System.out.println(((WAPlainText) element).getText());  
//     								 result += ((WAPlainText) element).getText();
//     								 if (body.toLowerCase().contains("integral") || body.toLowerCase().contains("integrate") ||
// 											 body.toLowerCase().contains("derivative")) {
// 										 break outerloop;
// 									 }
//     							 }
// 							 }
// 						 }
// 					 }
// 				 }
// 			 }
// 		 }
 	 
//Dane's old code (right before 'Communicate.sendtext(result);' line
// 	 result = result.replace("+", " + ");
// 	 result = result.replace("-", " - ");
// 	 result = result.replace("constant", "C");
// 	 result = result.replace("=", "\n=");