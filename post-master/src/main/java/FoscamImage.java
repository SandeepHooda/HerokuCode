import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import com.email.EmailAddess;
import com.email.EmailVO;
import com.fasterxml.jackson.databind.ObjectMapper;

import thread.Worker;

import org.apache.commons.io.IOUtils;


/**
 * Servlet implementation class Welcome
 */

public class FoscamImage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FoscamImage() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	/*Thread t = new Thread(new Worker());
    	t.setDaemon(false);
    	t.start();
    	System.out.println(" I have asked the worker to do your work");*/
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("Content length  :: "+request.getContentLength());
		byte[] imageBytes ;  //IOUtils.toByteArray(request.getInputStream());;
		
		
		InputStream inputStream = request.getInputStream(); 
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int totalBytes = 0;
		try {
			 if (inputStream != null) {  
					

		            byte[] charBuffer = new byte[1024];  
		            int bytesRead = -1;  

		            while ((bytesRead = inputStream.read(charBuffer)) > 0) { 
		            	totalBytes +=bytesRead;
		            	System.out.println("Bytes read  : "+bytesRead+ " total so far "+totalBytes);
		            	bos.write(charBuffer, 0, bytesRead);
		            }  
		        } 
		}catch(Exception e) {
			//e.printStackTrace();//It prints end of file error whioch is unnecessary
		}
		
		
		 bos.flush();
		 bos.close();
		 imageBytes = bos.toByteArray();
	   
		 String data = new String(imageBytes);
		 System.out.println(data.indexOf("Connection: close"));
		 
		System.out.println("Image size : "+imageBytes.length);
		imageBytes = Arrays.copyOfRange(imageBytes, (data.indexOf("Connection: close") +"Connection: close".length()+4 ), imageBytes.length);
		

		System.out.println("Image size : "+imageBytes.length);
		
		final String username = "";
		final String password = ""; 

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			message.setFrom(new InternetAddress("foscamnotificationsandeep@gmail.com","Gate Keeper"));
			
			message.setRecipients(Message.RecipientType.TO,		InternetAddress.parse("foscamnotificationsandeep@gmail.com"));
			
		
			message.setSubject("Gatekeeper detected a motion");
			messageBodyPart.setContent("<H1>Image </H1><img src=\"cid:image\">","text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);
			
				MimeBodyPart attachPart = new MimeBodyPart();
				 
				
				 ByteArrayDataSource dSource = new ByteArrayDataSource(imageBytes, "image/jpeg");
				 attachPart.setFileName("Image.jpg");
				 attachPart.setDataHandler(new DataHandler(dSource));
				 multipart.addBodyPart(attachPart);
			

			
			message.setContent(multipart);
			
			Transport.send(message);

			System.out.println(" Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	

}
