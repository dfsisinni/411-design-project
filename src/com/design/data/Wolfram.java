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
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;

// This was originally an interface, it may be better as a class or it may not be
public class Wolfram
{

	public static void wolframAlpha(String queryStr)
	{
    	StringBuilder result = new StringBuilder(".\n");
    	
    	// Basic engine setup
    	WAEngine engine = new WAEngine();
    	engine.setAppID("7AHUTR-UV58KYXA8Q");
    	 
    	// Query set up using parameter
    	WAQuery query = engine.createQuery();
    	query.setInput(queryStr);
    	
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
		else if (queryResult.isSuccess())
		{
			System.out.println();
			// WIP: Handles failure of Wolfram to find
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