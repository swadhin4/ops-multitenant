package com.pms.web.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.pms.app.mail.EmailTemplate;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.web.service.EmailService;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.util.RestResponse;

import freemarker.template.Configuration;

@Service("emailService")
public class EmailServiceImpl implements EmailService {

	private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	
	@Autowired
	private ServiceProviderService serviceProviderService;

	/*@Autowired
	private ServiceProviderRepo serviceProviderRepo;
	*/
	/*@Autowired
	private SiteRepo siteRepo;*/
	// Supply your SMTP credentials below. Note that your SMTP credentials are different from your AWS credentials.
	static final String SMTP_USERNAME = "AKIAICCJYOHTH5YPIG2A";  // Replace with your SMTP username.
	static final String SMTP_PASSWORD = "AlWwkzbX1QLknEOv26QxqGytYRUvwxvCdqfTN4Gl/Zlp";  // Replace with your SMTP password.

	// Amazon SES SMTP host name. This example uses the US West (Oregon) Region.
	static final String HOST = "email-smtp.us-west-2.amazonaws.com";
	
	
	@Autowired
    private Configuration freemarkerConfig;

	@Override
	public void sendEmail(final EmailTemplate emailTemplate) throws MailException { 

	}

	
	
	@Override
	@Async
	public RestResponse sendEmail(final String customerMailId, final String message)
			throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);

		//Christopher Gruen <c.gruen@hotmail.de>,Christopher Gruen <c.gruen@novazure.com>
		String toMailIds ="c.gruen@hotmail.de"; // Send Email to Chris in PROD
		//String toMailIds ="swadhin4@gmail.com";
		//String ccMailIds="malay18@gmail.com";//malay18@gmail.com,ranjankiitbbsr@gmail.com,shibasishmohanty@gmail.com";
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		//mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailIds ));
		mimeMessage.setSubject("Customer request for Demo","utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			
			Map < String, Object > model = new HashMap < String, Object > ();
	        model.put("customer", customerMailId);
	        model.put("message", "message");
	        model.put("regards", customerMailId);
			
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			/*messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear Chris,<br><br>");
			messageContent.append("A request for free demo has been raised by the customer \""+ customerMailId +"\". Request details are mentioned below:");
			messageContent.append("</td></tr><tr>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("<br><br><b>Customer's Message </b><br><br>"+ message);
			messageContent.append("</td></tr></table>");*/

			MimeBodyPart mbp1 = new MimeBodyPart();
			try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			//mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			//Template t = freemarkerConfig.getTemplate("email-template.ftl");
	        //String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, mail.getModel());
			mbp1.setContent(getContentFromTemplate(model, "request-demo.txt"), "text/html; charset=utf-8" );
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(200);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();        	
		}
		return response;
	}

	@Override
	public RestResponse sendSuccessSaveEmail(String emailId, AppUserVO appUserVO)
			throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds =appUserVO.getEmail(); // Send Email to Chris in PROD
		//	String ccMailIds="swadhin4@gmail.com,malay18@gmail.com,ranjankiitbbsr@gmail.com,sibasishmohanty@gmail.com,cadentive.digital@gmail.com";
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		//mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailIds ));
		mimeMessage.setSubject("Your account is created for OPS365 Application ","utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear "+appUserVO.getFirstName() +" " +appUserVO.getLastName()+",<br><br>");
			messageContent.append("The administrator has granted you access to PMS Platform. Please find your credentials below:");
			messageContent.append("</td></tr>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("<br><br><b>Username</b> : "+ appUserVO.getEmail() +"<br>");
			messageContent.append("<br><br><b>Password</b> : "+ appUserVO.getGeneratedPassword() +"<br>");
			messageContent.append("</td></tr>");
			messageContent.append("<tr><td align='left'><b>INSTRUCTIONS</b> <br>");
			messageContent.append("Step 1: Login to the application using the above credentials. <br>");
			messageContent.append("Step 2: Navigate to the Security Settings Tab in the Profile Page. <br>");
			messageContent.append("Step 3: Change your password and re-login to the application.");
			messageContent.append("</td></tr></table>");

			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(200);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();        	
		}
		return response;
	}

	
	@Override
	public RestResponse successSaveSPEmail(ServiceProviderVO serviceProviderVO, LoginUser loginUser)
			throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds =serviceProviderVO.getHelpDeskEmail(); // Send Email to Service Provider Helpdesk email in PROD
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		//mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailIds ));
		mimeMessage.setSubject("You have been registered in OPS365 Application ","utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear "+serviceProviderVO.getName()+",<br><br>");
			messageContent.append(loginUser.getFirstName()+" "+ loginUser.getLastName() +" from company "+ loginUser.getCompany().getCompanyName() + " has granted you access to his incident and ticket database.<br>");
			messageContent.append("In the future you/your helpdesk may receive emails which will provide you with incident details if damage to an asset has been logged or a service has been interrupted on your customer´s site.");
			messageContent.append("</td></tr>");
			messageContent.append("<tr><td align='left'><br>You will be able to enter the application under: ");
			messageContent.append("<br><br><b>Application link</b> : http://52.25.66.108:8585/login <br>");
			messageContent.append("<br>Click on 'Login' on the top right corner and click on 'External User Login' tab.");
			messageContent.append("<br>Please sign in using your username and access key.");
			messageContent.append("<br><br><b>Username</b> : "+ serviceProviderVO.getSpUserName() +"<br>");
			messageContent.append("<br><b>Access Key</b> : "+ serviceProviderVO.getAccessKey() +"<br>");
			messageContent.append("</td></tr>");
			messageContent.append("</tr><td><br>For further questions and comments on how to handle this application please contact: info@sigmasurge.com | www.sigmasurge.com</td></tr>");
			messageContent.append("</td></tr></table>");

			 
			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(100);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();  
			
		}
		return response;
	}

	@Override
	@Async
	public void successTicketCreationSPEmail(TicketVO savedticketVO, String creationStatus, String company) throws Exception {
		// Create a Properties object to contain connection configuration information.
		ServiceProvider serviceProvider = savedticketVO.getServiceProvider();
		//Site site = siteRepo.findOne(savedticketVO.getSiteId());
		CreateSiteVO site =  savedticketVO.getSite();
		final RestResponse response = new RestResponse();
		
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds =serviceProvider.getHelpDeskEmail(); //serviceProvider.getHelpDeskEmail(); // Send Email to Service Provider Helpdesk email in PROD
		//String toMailIds = "malay18@gmail.com"; 
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		//mimeMessage.setFrom(new InternetAddress("swadhin4@gmail.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		//mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("malay18@gmail.com"));
		mimeMessage.setSubject("Incident Number : "+ savedticketVO.getTicketNumber() +" created for "+ savedticketVO.getSiteName() +" / Status : "+savedticketVO.getStatus(),"utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			logger.info("Attempting to send an email through the Amazon SES SMTP interface...");
			Map < String, Object > model = new HashMap < String, Object > ();
			model.put("company", company==null?"":company);
	        model.put("serviceProvider", serviceProvider.getName()==null?"":serviceProvider.getName());
	        model.put("siteName", savedticketVO.getSiteName()==null?"":savedticketVO.getSiteName());
	        model.put("siteOwner", site.getSiteOwner()==null?"":site.getSiteOwner());
	        model.put("siteBrand", site.getBrandName()==null?"":site.getBrandName());
	        model.put("siteIdNumber1", site.getSiteNumber1()==null?"":site.getSiteNumber1().toString());
	        model.put("siteIdNumber2", site.getSiteNumber2()==null?"":site.getSiteNumber2().toString());
	        model.put("siteContactName", site.getContactName()==null?"":site.getContactName());
	        model.put("siteContactEmail", site.getEmail()==null?"":site.getEmail());
	        model.put("siteContactPhone", site.getPrimaryContact()==null?"":site.getPrimaryContact().toString());
	        model.put("contactName", savedticketVO.getCreatedUser()==null?"":savedticketVO.getCreatedUser());
	        model.put("contactEmail", savedticketVO.getCreatedBy()==null?"":savedticketVO.getCreatedBy());
	        model.put("ticketNumber", savedticketVO.getTicketNumber()==null?"":savedticketVO.getTicketNumber());
	        model.put("ticketCategory", savedticketVO.getCategoryName()==null?"":savedticketVO.getCategoryName());
	        model.put("assetCategory", savedticketVO.getAssetCategoryName()==null?"":savedticketVO.getAssetCategoryName());
	        model.put("assetComponentType", savedticketVO.getAssetSubCategory1()==null?"":savedticketVO.getAssetSubCategory1());
	        model.put("assetSubComponentType", savedticketVO.getAssetSubCategory2()==null?"":savedticketVO.getAssetSubCategory2());
	        model.put("ticketPriority", savedticketVO.getPriorityDescription()==null?"":savedticketVO.getPriorityDescription());
	        model.put("issueStartDate", savedticketVO.getTicketStartTime()==null?"":savedticketVO.getTicketStartTime());
	        model.put("resolutionDate", savedticketVO.getSla()==null?"":savedticketVO.getSla());
	        model.put("currentStatus", savedticketVO.getStatus()==null?"":savedticketVO.getStatus());
	        model.put("ticketName", savedticketVO.getTicketTitle()==null?"":savedticketVO.getTicketTitle());
	        model.put("assetImpacted", savedticketVO.getAssetName()==null?"":savedticketVO.getAssetName());
	        model.put("ticketDescription", savedticketVO.getDescription()==null?"":savedticketVO.getDescription());
	        model.put("username", serviceProvider.getSpUsername());
	       // model.put("secretKey", serviceProvider.getAccessKey());
			

			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/html; charset=\"utf-8\""); 
			mbp1.setContent( getContentFromTemplate(model, "incident-create.html"), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();        	
		}
		
		
	}

	@Override
	public RestResponse sendForgotPasswordEmail(String email, String defaultPwd) throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		if(StringUtils.isNotBlank(email)){
		
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds = email; // Send Email to Service Provider Helpdesk email in PROD
		//String toMailIds = "swadhin4@gmail.com"; 
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		//mimeMessage.setFrom(new InternetAddress("swadhin4@gmail.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		//mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse("malay18@gmail.com"));
		mimeMessage.setSubject("Your password for OPS365 application is reset ","utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear "+email+",<br><br>");
			messageContent.append("No worries!! If you forgot your password, we are ready to assist you in reseting your password" );
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Please use the default password provided below to login to the application. <br>");
			messageContent.append("<br> Default Password : <b>"+ defaultPwd +"</b>");
			messageContent.append("<br><span style='color:red'>NOTE :  Default password is only for login. You have to change your password to access the application.</span>");
			messageContent.append("<td></tr></table>");

			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(202);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			if(response.getStatusCode()==0){
				response.setStatusCode(500);
				response.setMessage("Email cannot be sent");
			}else if(response.getStatusCode()==200){
				response.setMessage("Password Reset email sent successfully");
			}
			transport.close();        	
		}
		}
		return response;
	}
	
	
	@Override
	public RestResponse successExtSPPasswordReset(ServiceProviderVO updatedSPUpdate, LoginUser loginUser) throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		if(StringUtils.isNotBlank(updatedSPUpdate.getEmail())){
		
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds = updatedSPUpdate.getEmail(); // Send Email to Service Provider Helpdesk email in PROD
		String ccMailIds = updatedSPUpdate.getHelpDeskEmail(); 
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		//mimeMessage.setFrom(new InternetAddress("swadhin4@gmail.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailIds));
		mimeMessage.setSubject("Your password for OPS365 application is reset ","utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear "+updatedSPUpdate.getName()+",<br><br>");
			messageContent.append("Your password reset has been initiated and is reset by "+ loginUser.getFirstName() +" " +loginUser.getLastName() );
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Please use the new password provided below to login to the application. <br>");
			messageContent.append("<br> New Password : <b>"+ updatedSPUpdate.getAccessKey() +"</b>");
			messageContent.append("<td></tr></table>");

			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(202);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			if(response.getStatusCode()==0){
				response.setStatusCode(500);
				response.setMessage("Email cannot be sent");
			}else if(response.getStatusCode()==200){
				response.setMessage("Password Reset email sent successfully");
			}
			transport.close();        	
		}
		}
		return response;
	}
	
	
	
	@Override
	@Async
	public RestResponse successEscalationLevel(TicketVO savedticketVO, EscalationLevelVO spEscalationLevel, String ccLevelEmail, String escaltedlevel) throws Exception {
		// Create a Properties object to contain connection configuration information.
		final RestResponse response = new RestResponse();
		ServiceProvider serviceProvider = new ServiceProvider();//serviceProviderRepo.findOne(savedticketVO.getAssignedTo());
		Properties props = System.getProperties();
		props.put("mail.transport.protocol", "smtps");
		props.put("mail.smtp.port", 25); 

		// Set properties indicating that we want to use STARTTLS to encrypt the connection.
		// The SMTP session will begin on an unencrypted connection, and then the client
		// will issue a STARTTLS command to upgrade to an encrypted connection.
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.starttls.required", "true");

		// Create a Session object to represent a mail session with the specified properties. 
		Session session = Session.getDefaultInstance(props);


		// Create a message with the specified information. 
		final MimeMessage mimeMessage = new MimeMessage(session);
		String toMailIds =spEscalationLevel.getEscalationEmail(); // Send Email to Service Provider Helpdesk email in PROD
		//String toMailIds = "swadhin4@gmail.com"; 
		mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
		//mimeMessage.setFrom(new InternetAddress("swadhin4@gmail.com"));
		mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
		if(StringUtils.isNotBlank(ccLevelEmail)){
		mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccLevelEmail));
		}
		mimeMessage.setSubject("Incident Number : "+ savedticketVO.getTicketNumber() +" has been escalated to level "+ spEscalationLevel.getEscalationLevel(),"utf-8");

		// Create a transport.        
		Transport transport = session.getTransport();

		// Send the message.
		try
		{
			System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
			StringBuilder messageContent = new StringBuilder();
			messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("Dear "+spEscalationLevel.getEscalationPerson()+",<br><br>");
			messageContent.append("<tr><td align='left'>");
			messageContent.append("<b><u>Ticket Primary Details</u><b><br>");
			messageContent.append("<ul>");
			messageContent.append("<li>Site : "+savedticketVO.getSiteName()+"</li>");
			messageContent.append("<li>Ticket Number : "+savedticketVO.getTicketNumber()+"</li>");
			messageContent.append("<li>Ticket Category : "+savedticketVO.getCategoryName()+"</li>");
			messageContent.append("<li>Issue Started on : "+savedticketVO.getTicketStartTime()+"</li>");
			messageContent.append("<li>Sla Date : "+savedticketVO.getSla()+"</li>");
			messageContent.append("<li>Status: "+savedticketVO.getStatus()+"</li>");
			messageContent.append("</ul>");
			messageContent.append("</td></tr>");
			messageContent.append("<tr><td align='left'>");
			/*messageContent.append("<br><br><b>Username</b> : "+ serviceProvider.getSpUsername() +"<br>");
			messageContent.append("<br><br><b>Access Key</b> : "+ serviceProvider.getAccessKey() +"<br>");
			*/messageContent.append("<br><br><b>Application link</b> : http://52.25.66.108:8585/login <br>");
			if(savedticketVO.getTicketAssignedType().equalsIgnoreCase("EXT")){
				messageContent.append("<br>NOTE: Select 'External User ' tab and sign in using your username and password.");
			}
			else if(savedticketVO.getTicketAssignedType().equalsIgnoreCase("RSP")){
				messageContent.append("<br>NOTE: Select 'Registered User' tab and sign in using your username and password.");
			}
			messageContent.append("</td></tr></table>");
			messageContent.append("</table>");

			MimeBodyPart mbp1 = new MimeBodyPart();
			/*try {
				mbp1.setDataHandler(new DataHandler(
						new ByteArrayDataSource(message.toString(), "text/html")));
			} catch (IOException e1) {
				e1.printStackTrace();
			}*/
			mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
			mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
			mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

			// create the second message part
			/*MimeBodyPart mbp2 = new MimeBodyPart();

			// attach the file to the message
			try {
			mbp2.attachFile(fileName);
			    } catch (IOException e) {
			   e.printStackTrace();
			}*/

			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp1);
			//mp.addBodyPart(mbp2);
			mimeMessage.setContent(mp, "text/html");

			// Connect to Amazon SES using the SMTP username and password you specified above.
			if (!transport.isConnected()){//make sure the connection is alive
				transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			}

			// Send the email.O
			mimeMessage.setSentDate(new Date());
			// Send the email.
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			System.out.println("Email sent!");
			response.setStatusCode(200);
		}
		catch (Exception ex) {
			response.setStatusCode(500);
			logger.error("The email was not sent.", ex);
		}
		finally
		{
			// Close and terminate the connection.
			transport.close();        	
		}
		
		return response;
	}

	 public String getContentFromTemplate(Map < String, Object > model, String mailTemplate) {
	        StringBuffer content = new StringBuffer();
	        try {
	            content.append(FreeMarkerTemplateUtils
	                .processTemplateIntoString(freemarkerConfig.getTemplate(mailTemplate), model));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return content.toString();
	    }



	@Override
	public RestResponse accessGrantedRSPEmail(Long rspId, LoginUser user) throws Exception {
			
		// Create a Properties object to contain connection configuration information.
				final RestResponse response = new RestResponse();
				final ServiceProviderVO serviceProviderVO = serviceProviderService.findServiceProviderInfo(rspId, user, "RSP");
				Properties props = System.getProperties();
				props.put("mail.transport.protocol", "smtps");
				props.put("mail.smtp.port", 25); 

				// Set properties indicating that we want to use STARTTLS to encrypt the connection.
				// The SMTP session will begin on an unencrypted connection, and then the client
				// will issue a STARTTLS command to upgrade to an encrypted connection.
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.starttls.required", "true");

				// Create a Session object to represent a mail session with the specified properties. 
				Session session = Session.getDefaultInstance(props);


				// Create a message with the specified information. 
				final MimeMessage mimeMessage = new MimeMessage(session);
				String toMailIds = serviceProviderVO.getHelpDeskEmail(); // Send Email to Service Provider Helpdesk email in PROD
				String ccMailIds = serviceProviderVO.getEmail(); 
				mimeMessage.setFrom(new InternetAddress("c.gruen@novazure.com"));
				//mimeMessage.setFrom(new InternetAddress("swadhin4@gmail.com"));
				mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toMailIds ));
				if(StringUtils.isNotBlank(ccMailIds)){
				mimeMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccMailIds));
				}
				if(serviceProviderVO.getAccessGranted().equalsIgnoreCase("Y")){
					mimeMessage.setSubject("Access Granted for Customer Database : "+user.getCompany().getCompanyName(),"utf-8");
				}
				
				

				// Create a transport.        
				Transport transport = session.getTransport();

				// Send the message.
				try
				{
					System.out.println("Attempting to send an email through the Amazon SES SMTP interface...");
					StringBuilder messageContent = new StringBuilder();
					messageContent.append("<table width='90%' border='0' cellspacing='0' cellpadding='0'>");
					messageContent.append("<tr><td align='left'>");
					messageContent.append("Dear "+serviceProviderVO.getEmail()+",<br><br>");
					messageContent.append("<tr><td align='left'>");
					messageContent.append(user.getFirstName()+" "+user.getLastName() +" has granted you access to its incident database.<br>");
					messageContent.append("Please make sure that your agents are assigned for this customer to manage incidents." );				
					messageContent.append("</td></tr>");
					messageContent.append("<tr><td align='left'>");
					messageContent.append("<br><br><b>Application link</b> : http://52.25.66.108:8585/login <br>");
					messageContent.append("<br>NOTE: Select 'Registered User Login' tab and sign in as Service Provider using your username and password.");
					messageContent.append("</td></tr></table>");
					messageContent.append("</table>");

					MimeBodyPart mbp1 = new MimeBodyPart();
					
					mbp1.setHeader("Content-Type","text/plain; charset=\"utf-8\""); 
					mbp1.setContent( messageContent.toString(), "text/html; charset=utf-8" ); 
					mbp1.setHeader("Content-Transfer-Encoding", "quoted-printable");

					// create the second message part
					/*MimeBodyPart mbp2 = new MimeBodyPart();

					// attach the file to the message
					try {
					mbp2.attachFile(fileName);
					    } catch (IOException e) {
					   e.printStackTrace();
					}*/

					Multipart mp = new MimeMultipart();
					mp.addBodyPart(mbp1);
					//mp.addBodyPart(mbp2);
					mimeMessage.setContent(mp, "text/html");

					// Connect to Amazon SES using the SMTP username and password you specified above.
					if (!transport.isConnected()){//make sure the connection is alive
						transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
					}

					// Send the email.O
					mimeMessage.setSentDate(new Date());
					// Send the email.
					transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
					System.out.println("Email sent!");
					response.setStatusCode(200);
				}
				catch (Exception ex) {
					response.setStatusCode(500);
					logger.error("The email was not sent.", ex);
				}
				finally
				{
					// Close and terminate the connection.
					transport.close();        	
				}
				
				return response;
	}
	
	
}
