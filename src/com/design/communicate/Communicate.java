package com.design.communicate;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.design.servlets.SMSServlet;
import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

public interface Communicate {

	public static final String ACCOUNT_SID = "ACa5505abc27a7d474f55d817367c57f45";
	public static final String AUTH_TOKEN = "11685b45e64715afef26e26781f5ad98";
	
	public static void sendText (String text) {
    	TwilioRestClient client = new TwilioRestClient(ACCOUNT_SID, AUTH_TOKEN);
    	 
        // Build a filter for the MessageList
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("Body", text));
        params.add(new BasicNameValuePair("To", SMSServlet.from));
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
