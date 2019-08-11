package com.phone.text;

import java.util.LinkedHashMap;
import java.util.logging.Logger;



import com.phone.Key;
import com.plivo.helper.api.client.RestAPI;
import com.plivo.helper.api.response.call.Call;
import com.plivo.helper.api.response.message.MessageResponse;
import com.plivo.helper.exception.PlivoException;

public class SMS {
	
	public static void sendText(String phoneNo, String text) throws Exception {
		System.out.println("Sending SMS to  "+phoneNo );
		RestAPI api = new RestAPI(Key.ID, Key.key, "v1");

        LinkedHashMap<String, String> parameters = new LinkedHashMap<String, String>();
        parameters.put("src", "1111111111"); // Sender's phone number with country code
        parameters.put("dst", phoneNo); // Receiver's phone number with country code
        parameters.put("text", text); // Your SMS text message
       

        // Send Unicode text
       
        parameters.put("url", "https://remind-me-on.appspot.com/TextStatus"); // The URL to which with the status of the message is sent
        parameters.put("method", "GET"); // The method used to call the url

       
            // Send the message
        	System.out.println("Sending SMS to in try catch "+phoneNo );
            MessageResponse msgResponse = api.sendMessage(parameters);
            System.out.println("Sending SMS msgResponse "+msgResponse );
            // Print the response
            System.out.println(msgResponse);
            // Print the Api ID
            System.out.println("Api ID : " + msgResponse.apiId);
            // Print the Response Message
            System.out.println("Message : " + msgResponse.message);

            if (msgResponse.serverCode == 202 && msgResponse.error == null) {
                // Print the Message UUID
                System.out.println("Message UUID : " + msgResponse.messageUuids.get(0).toString());
            } else {
                System.out.println(msgResponse.error);
                throw new Exception("Couln't place call");
            }
        
	}

}

