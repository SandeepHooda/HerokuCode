package com.phone.call;

import java.util.LinkedHashMap;

import com.phone.Key;
import com.plivo.helper.api.client.RestAPI;

public class Call {

	public static void makeACall(String phoneNo, String messageUrl, String fromPhoneNumber) throws Exception {
		if (null == fromPhoneNumber) {
			fromPhoneNumber = "1111111111";
		}
		System.out.println("messageUrl="+messageUrl+" phone no "+phoneNo);
		String auth_id = Key.ID;
        String auth_token = Key.key;
        RestAPI api = new RestAPI(auth_id, auth_token, "v1");

        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("to",phoneNo); // The phone numer to which the all has to be placed
        parameters.put("from",fromPhoneNumber); // The phone number to be used as the caller id

        // answer_url is the URL invoked by Plivo when the outbound call is answered
        // and contains instructions telling Plivo what to do with the call
        parameters.put("answer_url",messageUrl/*"https://s3.amazonaws.com/static.plivo.com/answer.xml"*/);
        parameters.put("answer_method","GET"); // method to invoke the answer_url

       
        
            // Make an outbound call and print the response
            com.plivo.helper.api.response.call.Call resp = api.makeCall(parameters);
            if (resp.serverCode != 201 || resp.error != null) {
            	throw new Exception("Couln't place call");
            }
            System.out.println(resp);
       
	}
	public static void makeACall(String phoneNo, String messageUrl) throws Exception {
		makeACall( phoneNo, messageUrl, null);
	}
	
}
