/**
 * The Wolfram Module, deals with queries relevant to Wolfram Alpha
 */
package com.design.data;

import com.design.communicate.Communicate;
import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

public interface Wolfram {

	public static void wolframAlpha(String body) {
    	String result = ".\n";
    	
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
    		 outerloop:
    		 
    		 for (WAPod pod : queryResult.getPods()) {
    			 if (!pod.isError()) {
    				 
    				
    				 
    				 for (WASubpod subpod : pod.getSubpods()) {
    					 for (Object element : subpod.getContents()) {
    						 if (element instanceof WAPlainText) {
    							  
    							 if (body.toLowerCase().contains("derivative") || body.toLowerCase().contains("deriv")) {
    								 if (((WAPlainText) element).getText().contains("d/dx")) {
    									 result +=  ((WAPlainText) element).getText();
    									 if (body.toLowerCase().contains("integral") || body.toLowerCase().contains("integrate") ||
    											 body.toLowerCase().contains("derivative")) {
    										 break outerloop;
    									 }
    									 
    								 }
    							 } else {
    								 if (!((WAPlainText) element).getText().contains("Plot")) {
        								 System.out.println(((WAPlainText) element).getText());  
        								 result += ((WAPlainText) element).getText();
        								 if (body.toLowerCase().contains("integral") || body.toLowerCase().contains("integrate") ||
    											 body.toLowerCase().contains("derivative")) {
    										 break outerloop;
    									 }
        							 }
    							 }
    						 }
    					 }
    				 }
    			 }
    		 }
    	 }
    	 
    	 result = result.replace("+", " + ");
    	 result = result.replace("-", " - ");
    	 result = result.replace("constant", "C");
    	 result = result.replace("=", "\n=");
    	 
    	 Communicate.sendText(result);
    }
	
}
