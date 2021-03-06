package com.pms.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.pms.app.exception.PMSTechnicalException;
import com.pms.app.view.vo.CompanyAttachments;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.service.FileIntegrationService;
import com.pms.web.service.TicketService;
import com.pms.web.util.RestResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private FileIntegrationService fileIntegrationService;
	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private AwsIntegrationService awsIntegrationService;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(final Locale locale, final ModelMap model,final HttpSession session) {
		try{
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if(loginUser==null){
			return "index";
		}
		else{
			model.put("message", loginUser.getMessage());
			session.invalidate();
		}
		}catch(PMSTechnicalException pmse) {
			LOGGER.error(pmse.getMessage());
			return "redirect:/login?status="+pmse.getMessage();
		}catch(Exception e){
			e.printStackTrace();
			return "index";
		}
		return "index";
	}
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(final Locale locale, final ModelMap model,
			final HttpServletRequest request, @RequestParam(value="status") String status) {
		if(StringUtils.isEmpty(status)){
			model.put("message", "");
		}
		return "login";
	}
	@RequestMapping(value = "/login/{message}", method = RequestMethod.GET)
	public String login(final Locale locale, final ModelMap model,
			final HttpServletRequest request, @PathVariable(value="message") String message) {
		if(StringUtils.isEmpty(message)){
			model.put("message", "");
		}else{
			model.put("message", "Invalid Username or password");
		}
		/*model.put("message", "Invalid Username or password");
		model.put("user", 0);*/
		return "login";
	}
	
	@RequestMapping(value = "/tenants", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> login() {
		RestResponse responseData = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.NO_CONTENT);
		/*List<Tenant> tenantList = tenantService.getAllTenants();
		if(!tenantList.isEmpty()){
			responseData.setStatusCode(200);
			responseData.setObject(tenantList);
			responseEntity=new ResponseEntity<RestResponse>(responseData, HttpStatus.OK);
		}else{
			responseData.setStatusCode(500);
			responseEntity=new ResponseEntity<RestResponse>(responseData, HttpStatus.NOT_FOUND);
		}*/
		return responseEntity;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(final Locale locale, final ModelMap model,final HttpServletRequest request) {
		return "register";
	}
	@RequestMapping(value = "/awsexplorer", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> awsExplorer(final HttpServletRequest request) {
		
		List<CompanyAttachments> companyAttachmentList = awsIntegrationService.listBucketObjects();
		RestResponse responseData = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.NO_CONTENT);
		if(!companyAttachmentList.isEmpty()){
			responseData.setStatusCode(200);
			responseData.setObject(companyAttachmentList);
			responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
		}
		return responseEntity;
	}
	@RequestMapping(value = "/appdashboard", method = RequestMethod.GET)
	public String loginSuccess(final Locale locale, final Model model, final HttpSession session) {
		LOGGER.info("Validating currently logged in user.");
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if(loginUser!=null) {
			if(StringUtils.isEmpty(loginUser.getUsername())) {
				return "redirect:/";
			}else {
				LOGGER.info("Logged in User : " + loginUser.getUsername());
				model.addAttribute("user", loginUser);
				if(!StringUtils.isEmpty(loginUser.getSysPassword())){
					if(loginUser.getUserType().equalsIgnoreCase("USER") && loginUser.getSysPassword().equalsIgnoreCase("YES")){
						return "redirect:/user/profile";
					}else if(loginUser.getUserType().equalsIgnoreCase("SP") && loginUser.getSysPassword().equalsIgnoreCase("YES")){
						return "redirect:/user/profile";
					}
					else if(loginUser.getUserType().equalsIgnoreCase("EXTSP") && loginUser.getSysPassword().equalsIgnoreCase("NO")){
						return "redirect:/user/extsp/incident/details";
					}else{
						session.setAttribute("usercode", loginUser.getCompany().getCompanyCode());
						return "home";
					}
				  }else{
					    model.addAttribute("message", "System password is empty");
						return "redirect:/";
				  }
				}
		}else {
			model.addAttribute("message", "Invalid username or password");
			return "redirect:/";
		}
	}

	@RequestMapping(value = "/selected/file/download", method = RequestMethod.GET)
	public void getSelectedSiteFileImage(@RequestParam(value="keyname") String keyName,final HttpSession session,
			final HttpServletResponse response) {
		LOGGER.info("Inside HomeController .. getSelectedSiteFileImage");
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				if(StringUtils.isNotEmpty(keyName)){
				RestResponse responseData = fileIntegrationService.getFileLocation(loginUser.getCompany(),keyName);
				if(responseData.getStatusCode()==200){
					setResponseWithFile(responseData.getMessage(), response);
				}else{
					LOGGER.info("Unable to download file");
				 }
				}

			} catch (Exception e) {
				LOGGER.info("Exception while getting site image", e);

			}
		}

		LOGGER.info("Exit HomeController .. getSelectedSiteFile");
	}
	
	@RequestMapping(value = "/file/attachement/delete/{feature}/{fileIds}/{type}", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> deleteFileAttached(@PathVariable(value="feature") String feature, 
			@PathVariable(value="fileIds") String fileIds,@PathVariable(value="type") String type,final HttpSession session,
			final HttpServletResponse response) {
		LOGGER.info("Inside HomeController .. deleteFileAttached");
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse responseData = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.NO_CONTENT);
		if(loginUser!=null){
			try {
				
				if(StringUtils.isNotEmpty(feature)){
					if(feature.equalsIgnoreCase("SITE")){
						String [] siteId = fileIds.split(",");
						responseData = fileIntegrationService.deleteFile(loginUser.getDbName(),Long.parseLong(siteId[0]), null,null,null,null);
						responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
					}
					else if(feature.equalsIgnoreCase("LICENSE")){
						String [] licenseIds = fileIds.split(",");
						List<Long> licenseList = new ArrayList<Long>();
						for(String licenseId:licenseIds){
							licenseList.add(Long.parseLong(licenseId));
						}
						responseData = null;//fileIntegrationService.deleteFile(null, licenseList,null,null,null);
						responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
					}
					else if(feature.equalsIgnoreCase("ASSET")){
						String [] assetId = fileIds.split(",");
						if(StringUtils.isNotBlank(type)){
							responseData = null;//fileIntegrationService.deleteFile(null, null,Long.parseLong(assetId[0]),null,type);	
						}
						responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
					}
					else if(feature.equalsIgnoreCase("INCIDENT")){
						TicketVO sessionTicketVO = (TicketVO) session.getAttribute("selectedTicket");
						String [] incidentIds = fileIds.split(",");
						List<Long> ticketAttachementIds = new ArrayList<Long>();
						for(String attachement:incidentIds){
							ticketAttachementIds.add(Long.parseLong(attachement));
						}
						responseData = fileIntegrationService.deleteFile(loginUser.getDbName(),null, null,null,ticketAttachementIds,null);
						if(responseData.getStatusCode()==200){
							
						}
						LOGGER.info("Updating incident image path");
						List<TicketAttachment> fileAttachments = ticketService.findByTicketId(sessionTicketVO.getTicketId(), loginUser, sessionTicketVO);
						List<TicketAttachment> fileAttachmentList = new ArrayList<TicketAttachment>();
						if(fileAttachments==null){
							LOGGER.info("No Ticket Attachment for "+ sessionTicketVO.getTicketNumber());
						}else{
							
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							for (TicketAttachment ticketAttachment : fileAttachments) {
								ticketAttachment.setCreatedDate(formatter.format(ticketAttachment.getCreatedOn()));
								fileAttachmentList.add(ticketAttachment);
							}
						}
						sessionTicketVO.getAttachments().clear();
						sessionTicketVO.setAttachments(fileAttachmentList);
						session.setAttribute("selectedTicket", sessionTicketVO);
						responseData.setStatusCode(200);
						responseData.setObject(sessionTicketVO);
						responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
							
						}
					}
					
					
			} catch (Exception e) {
				LOGGER.info("Exception while getting site image", e);
				responseData.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(responseData,HttpStatus.OK);
			}
		}
		
		LOGGER.info("Exit HomeController .. deleteFileAttached");
		return responseEntity;
	}
}
