/**
 * The Wolfram Module, deals with queries relevant to Wolfram Alpha
 * Specifically, this module handles:
 * 
 * @author Shayaan Syed Ali
 * @version Feb 26, 2016
 */
package com.design.data;

import com.design.communicate.Communicate;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
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

	public static void wolframAlpha(String queryStr)
	{
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
		}
		
		// Error case
		if (queryResult.isError())
		{
			System.out.println("Query error: " + queryResult.getErrorCode() + ": " + queryResult.getErrorMessage());
			// WIP: Handles error from Wolfram querying
		}
		else if (!queryResult.isSuccess())
		{
			System.out.println("Query not successful");
			// WIP: Handles failure of Wolfram to return result for query
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
					WASubpod[] subpods = pods[podI].getSubpods();
					for (WASubpod subpod : subpods)
					{
						result.append(subpod.getTitle());
						result.append(": ");
						result.append(subpod.toString());
						result.append('\n');
					}
				}
			}
			
			// Include pods which must always be included at the end (if they exist)
			// WIP
		}

			


    	 // Send results through Communicate Module
    	 Communicate.sendText(result.toString());
    	 
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