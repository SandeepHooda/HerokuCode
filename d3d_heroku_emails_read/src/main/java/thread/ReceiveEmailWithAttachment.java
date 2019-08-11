package thread;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mangodb.MangoDB;
import secreat.Key;
import thread.vo.Settings;
import thread.vo.VisionRequest;
import vo.HumanDetectVO;
import vo.ResponseVO;
 
/**
 * This class is used to receive email with attachment.
 * @author codesjava
 */
public class ReceiveEmailWithAttachment { 
	private static String USER_AGENT ="Mozilla/5.0";
	private static final String falseAlarmSubject = "False alarm";
	 private static final String pop3Host = "pop.gmail.com";//change accordingly
	 private static final String mailStoreType = "pop3";	
	 private static  final String userName =Key.userName;
	 private static  final String password = Key.password;
	private static  Properties props = new Properties();
	static {
		 
		    props.put("mail.store.protocol", "pop3");
		    props.put("mail.pop3.host", pop3Host);
		    props.put("mail.pop3.port", "995");
		    props.put("mail.pop3.starttls.enable", "true");
	}
public static final void sendEmail(String subject, String Body) {
	Properties props_send = new Properties();
	props_send.put("mail.smtp.auth", "true");
	props_send.put("mail.smtp.starttls.enable", "true");
	props_send.put("mail.smtp.host", "smtp.gmail.com");
	props_send.put("mail.smtp.port", "587");
	
	Session session = Session.getInstance(props_send,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(userName, password);
				}
			  });
	
	 try {

			Message message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			message.setFrom(new InternetAddress("foscamnotificationsandeep@gmail.com","Camera Detect Human"));
			
			message.setRecipients(Message.RecipientType.TO,		InternetAddress.parse("foscamnotificationsandeep@gmail.com"));
			
		
			message.setSubject(subject);
				 message.setText(Body); 
		Transport.send(message); 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}  
	  
}
 public static List<HumanDetectVO> receiveEmail(String pop3Host,
    String mailStoreType, String userName, String password){
	 boolean humanFound = false;
	 List<HumanDetectVO> imageScanner = new ArrayList<HumanDetectVO>();
	
    //Set properties
  
 
    // Get the Session object.
    Session session = Session.getInstance(props);
 
    try {
        //Create the POP3 store object and connect to the pop store.
	Store store = session.getStore("pop3s");
	store.connect(pop3Host, userName, password);
 
	//Create the folder object and open it in your mailbox.
	Folder emailFolder = store.getFolder("INBOX");
	emailFolder.open(Folder.READ_ONLY);
 
	//Retrieve the messages from the folder object.
	Message[] messages = emailFolder.getMessages();
	System.out.println("Total Message" + messages.length);
 
	//Iterate the messages
	for (int i = 0; i < messages.length; i++) {
	   Message message = messages[i];
	   Address[] toAddress = 
             message.getRecipients(Message.RecipientType.TO);
	     System.out.println("---------------------------------");  
	    
	     System.out.println("Time : "+message.getSentDate());
	    
 
	     
	     //Iterate multiparts
          try {
        	  
          }catch(Exception e) {
        	  e.printStackTrace();
          }
          Multipart multipart = null;
          try {
        	  if (message.getContent() != null && message.getContent() instanceof Multipart)
        	  multipart = (Multipart) message.getContent();
          }catch(Exception e) {
        	  e.printStackTrace();
          }
	    
	     if (null != multipart) {
	    	 for(int k = 0; k < multipart.getCount(); k++){
	  	       BodyPart bodyPart = multipart.getBodyPart(k);  
	  	       String fileName = bodyPart.getFileName();  
	  	       System.out.println( "Orignal File name "+fileName);
	  	       if (null != fileName) {
	  	    	   if (fileName.toLowerCase().indexOf("gb2312") != -1) {   
	  	               fileName = MimeUtility.decodeText(fileName);   
	  	           } 
	  		       System.out.println( "File name after decoding "+fileName);
	  	       }
	             
	  	      
	  	       InputStream stream =    (InputStream) bodyPart.getInputStream();  
	  	      ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
	  	     
	   
	  	       byte[] buffer = new byte[1024];
	  			int len;

	  			// read bytes from the input stream and store them in buffer
	  			while ((len = stream.read(buffer)) != -1) {
	  				// write bytes from the buffer into output stream
	  				byteArray.write(buffer, 0, len);
	  			}
	  	       
	  			if (fileName != null && !falseAlarmSubject.equals(message.getSubject())){//ignore this email as it was sent by me 
	  				System.out.println(" checking human .");
		  	        // Close the file 
		  			byteArray.close(); 
		  			if (!humanFound) {
		  				humanFound = findHuman(byteArray.toByteArray());//If Human is found in one of the immage then no need to check in other images
		  			}
		  			HumanDetectVO imageResult = new HumanDetectVO();
		  			
		  			imageResult.setHasHuman( humanFound);
		  			imageResult.setFileName(fileName+" Date = "+ message.getSentDate());
		  			imageScanner.add(imageResult);
		  			
	  		     }
	  			
	  				 
	  			//}
	  	        
	  	      } 
	     }
	      
	   }
 
	   //close the folder and store objects
	   emailFolder.close(false);
	   store.close();
	} catch (NoSuchProviderException e) {
		e.printStackTrace();
	} catch (MessagingException e){
		e.printStackTrace();
	} catch (Exception e) {
	       e.printStackTrace();
	}
    
    return imageScanner;
    
    }
 //
//HTTP POST request
	private static boolean findHuman( byte[] bytesArray ) throws Exception {
		String settingsJson = MangoDB.getDocumentWithQuery("sanhoo-home-security", "settings", "0", true, null, null);
		Gson  json = new Gson();
		Settings settings = (Settings)json.fromJson(settingsJson, new TypeToken<Settings>() {}.getType());
		if (!settings.isValue()) {
			//No need to check google api to figure out it is human or not. any movement is good enough
			System.out.println("mango DD switch is off for \"sanhoo-home-security\", \"settings\" No need to check google api to figure out it is human or not. any movement is good enough");
			return true;
		}
		
		System.out.println(" Using google vision API to find human ");
		String url = "https://vision.googleapis.com/v1/images:annotate?key="; 
		
		VisionRequest visionRequest = VisionRequest.getRequestVo();
		String base64Content = Base64.getEncoder().encodeToString(bytesArray);
		visionRequest.getRequests().get(0).getImage().setContent(base64Content);
		
		String visionRequestJson =  json.toJson(visionRequest, new TypeToken<VisionRequest>() {}.getType());
				    
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setUseCaches(false);
		con.setDoOutput(true);
	
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		con.setRequestProperty(
			    "Content-Type", "application/json" );
		
		
		DataOutputStream request = new DataOutputStream(
				con.getOutputStream());

			request.writeBytes(visionRequestJson);
			
		
			request.flush();
			request.close();

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		 //System.out.println(" response from google "+response);
		  ResponseVO responseVO = (ResponseVO) json.fromJson(response.toString(), new TypeToken<ResponseVO>() {}.getType());
		//print result
		  boolean personFound = responseVO.isPersonFound();
		System.out.println("Person found "+personFound);
	
		
		return personFound;

	}
	
	
	// HTTP GET request
		private static void notifyHumanActivity() throws Exception {

			String url = "http://sanhoo-home-security.appspot.com/HumanDetected?deviceID=3";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");

			//add request header
			con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());

		}
		
 public static void main(String[] args) {
 
 

	 
	  try {
		  System.out.println(" Read email");
		  //call receiveEmail
		  List<HumanDetectVO> imageScanResult = receiveEmail(pop3Host, mailStoreType, userName, password);
		  
		  
		  boolean humanFound  = false;
		  Set<String> falseAlarm = new HashSet<String>();
		  if (null != imageScanResult && imageScanResult.size() > 0 ) {
			 
			  for (HumanDetectVO result: imageScanResult) {
				  if (!result.isHasHuman()) {
					  falseAlarm.add(result.getFileName());
				  }else {
					  humanFound = true;
				  }
			  }
				  
			 
		  }
		  
		  if (humanFound) {
			  System.out.println(" make a phone call ");
			  notifyHumanActivity();
		  }
		 if (falseAlarm.size() > 0) {
			 sendEmail(falseAlarmSubject, "Human not found "+falseAlarm);
		 }
		
	} catch (Exception e) {
		
		e.printStackTrace();
	}

 
 }
}