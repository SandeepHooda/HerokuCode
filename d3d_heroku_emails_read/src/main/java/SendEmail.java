import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

import com.email.EmailAddess;
import com.email.EmailVO;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Servlet implementation class Welcome
 */

public class SendEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendEmail() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String name= request.getParameter("name");
    	String toAddress = "sonu.hooda@gmail.com";
    	if (null == name) {
    		name = "Sandeep";
    	}
    	String message ="Hello "+name;
    	String subject = "Motion detected by Pi Cam";
    	String fromAddress ="personal.reminder.notification@gmail.com";
    	
    	
    	EmailVO emalVO = new EmailVO();
		
		emalVO.setSubject(subject);
		
		emalVO.setHtmlContent(message+" <br/><br/>"
				+ "https://sanhoo-home-security.appspot.com/");
		EmailAddess from = new EmailAddess();
		if (fromAddress.indexOf("@") > 0) {
			fromAddress = fromAddress.substring(0, fromAddress.indexOf("@"));
			fromAddress = fromAddress.replace(".", " ").replaceAll("_", " ");
		}
		from.setLabel(fromAddress);
		from.setAddress(emalVO.getUserName());
		
		List<EmailAddess> receipients = new ArrayList<EmailAddess>();
		EmailAddess to = new EmailAddess();
		to.setAddress(toAddress);
		emalVO.setFromAddress(from);
		receipients.add(to);
		emalVO.setToAddress(receipients);
	        sendEmail(emalVO,response);
    	
    }
    private void sendEmail(EmailVO email, HttpServletResponse response) throws IOException {
    	final String username = "personal.reminder.notification@gmail.com";
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
			message.setFrom(new InternetAddress(email.getFromAddress().getAddress(),email.getFromAddress().getLabel()));
			if(email.getToAddress() != null && email.getToAddress().size() > 0) {
				for (EmailAddess address:email.getToAddress()  ) {
					
					/*if (!"sonu.hooda@gmail.com".equalsIgnoreCase(address.getAddress())) {
						
						System.out.println("Unauthorized email To address "+address.getAddress());
						email.setHtmlContent("This email is for "+address.getAddress()+" Subject:::: "+email.getSubject()+"   Body::: "+email.getHtmlContent());
						System.out.println("un authorized email "+email.getHtmlContent());
						address.setAddress("sonu.hooda@gmail.com");
					}*/
					
					message.setRecipients(Message.RecipientType.TO,		InternetAddress.parse(address.getAddress()));
					
				}
				
			}
			/*if(email.getCcAddress() != null && email.getCcAddress().size() > 0) {
				for (EmailAddess address:email.getCcAddress()  ) {
					message.setRecipients(Message.RecipientType.CC,		InternetAddress.parse(address.getAddress()));
				}
				
			}*/
			/*if(email.getBccAddress() != null && email.getBccAddress().size() > 0) {
				for (EmailAddess address:email.getBccAddress()  ) {
					message.setRecipients(Message.RecipientType.BCC,		InternetAddress.parse(address.getAddress()));
				}
				
			}*/
			
			message.setSubject(email.getSubject());
			messageBodyPart.setContent(email.getHtmlContent(),"text/html; charset=utf-8");
			multipart.addBodyPart(messageBodyPart);
			if (email.getBase64Attachment() != null) {
				MimeBodyPart attachPart = new MimeBodyPart();
				 MimeBodyPart filePart = new MimeBodyPart();
				 byte[] fileData = DatatypeConverter.parseBase64Binary(email.getBase64Attachment());
				 ByteArrayDataSource dSource = new ByteArrayDataSource(fileData, "text/*");
				 attachPart.setFileName(email.getAttachmentName());
				 attachPart.setDataHandler(new DataHandler(dSource));
				 multipart.addBodyPart(attachPart);
			}

			
			message.setContent(multipart);
			
			Transport.send(message);

			response.getWriter().print("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		 BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
	        String json = "";
	        StringBuffer sb = new StringBuffer();
	        while((json = br.readLine() ) != null) {
	        	sb.append(json);
	        	
	        }
	        
	        ObjectMapper objectMapper = new ObjectMapper();
	        EmailVO email = objectMapper.readValue(sb.toString(), EmailVO.class);  
	        sendEmail(email,response);
	        
			}

	

}
