package com.pms.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pms.app.constants.AppConstants;
import com.pms.app.constants.RSPCustomerConstants;
import com.pms.app.constants.TicketUpdateType;
import com.pms.app.constants.UserType;
import com.pms.app.dao.impl.IncidentDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.FinancialVO;
import com.pms.app.view.vo.IncidentTask;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.SelectedTicketVO;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.jpa.entities.TicketComment;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.service.TicketService;
import com.pms.web.util.ApplicationUtil;
import com.pms.web.util.RandomUtils;
import com.pms.web.util.RestResponse;

@Service("ticketService")
public class TicketServiceImpl implements TicketService {
	

	private final static Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);
	
	@Autowired
	private AwsIntegrationService awsIntegrationService;
	

	private SiteDAO getSiteDAO(String dbName) {
		return new SiteDAO(dbName);
	}
	
	private IncidentDAO getIncidentDAO(String dbName) {
		return new IncidentDAO(dbName);
	}
	
	private ServiceProviderDAOImpl getServiceProviderDAOImpl(String dbName) {
		return new ServiceProviderDAOImpl(dbName);
	}
	
	
	@Override
	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser, final String assignedTo) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllCustomerTickets");
		LOGGER.info("Getting ticket List for logged in user : "+  loginUser.getFirstName() + "" + loginUser.getLastName());
		List<TicketVO> customerTicketList = new ArrayList<TicketVO>();
		if(assignedTo.equalsIgnoreCase("EXT")){
			SiteDAO siteDAO = getSiteDAO(loginUser.getDbName());
			List<CreateSiteVO> siteList = siteDAO.getSiteList(loginUser.getUsername());
			Set<Long> siteIdList = new HashSet<Long>();
			for (CreateSiteVO siteVO : siteList) {
				siteIdList.add(siteVO.getSiteId());
			}
			customerTicketList = getIncidentDAO(loginUser.getDbName()).findTicketsBySiteIdIn(siteIdList, assignedTo);
		}else{
			customerTicketList = getIncidentDAO(loginUser.getDbName()).findTicketsBySiteIdIn(null, assignedTo);
		}
		
		LOGGER.info("Exit TicketServiceImpl - getAllCustomerTickets");
		return customerTicketList == null?Collections.emptyList():customerTicketList;
	}
	
	@Override
	public List<TicketVO> getAllSPTickets(LoginUser loginUser) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllSPTickets");
		LOGGER.info("Getting ticket List for logged in SP user : "+  loginUser.getLastName());
		List<TicketVO> customerTicketList = getIncidentDAO(loginUser.getDbName()).findTicketsAssignedToExternalSP(loginUser.getSpId());
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		LOGGER.info("Exit TicketServiceImpl - getAllSPTickets");
		return customerTicketList == null?Collections.emptyList():customerTicketList;
	}
	
	@Override
	public List<TicketVO> getTicketsForSP(LoginUser loginUser, String ticketsCreatedBy, String custDB) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getTicketsForSP");
		LOGGER.info("Getting ticket List for logged in SP user : "+  loginUser.getLastName());
		List<TicketVO> customerTicketList = null;
				if(loginUser.getUserType().equalsIgnoreCase("SP") && ticketsCreatedBy.equalsIgnoreCase("CUSTOMER")){
					ServiceProviderVO serviceProviderVO = getIncidentDAO(custDB).getRegisteredSPDetails(loginUser.getCompany().getCompanyCode());
					customerTicketList = getIncidentDAO(custDB).findTicketsAssignedToRSP(serviceProviderVO.getServiceProviderId());
				}
					
				else if(loginUser.getUserType().equalsIgnoreCase("SP") && ticketsCreatedBy.equalsIgnoreCase("RSP")){
					customerTicketList = getIncidentDAO(custDB).findTicketsCreatedBy(loginUser.getCompany().getCompanyCode());
					
				}else if(loginUser.getUserType().equalsIgnoreCase("USER")){
					ServiceProviderVO serviceProviderVO = getIncidentDAO(custDB).getRegisteredSPDetails(ticketsCreatedBy);
					customerTicketList = getIncidentDAO(custDB).findTicketsAssignedToRSP(serviceProviderVO.getServiceProviderId());
				}
		
		LOGGER.info("Exit TicketServiceImpl - getTicketsForSP");
		return customerTicketList == null?Collections.emptyList():customerTicketList;
	}


	@Override
	public List<TicketCategory> getTicketCategories(LoginUser loginUser) throws Exception {
		List<TicketCategory> ticketCategories = getIncidentDAO(loginUser.getDbName()).findTicketCategories();
		return ticketCategories == null?Collections.emptyList():ticketCategories;
	}

	@Override
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId, String assetType, LoginUser loginUser) throws Exception {
		TicketPrioritySLAVO  ticketPriority = getIncidentDAO(loginUser.getDbName()).getSPSLADetails(serviceProviderID, ticketCategoryId,assetType, loginUser.getUserType());
		return ticketPriority;
	}

	@Override
	public List<Status> getStatusByCategory(LoginUser loginUser, String category) throws Exception {
		List<Status> statusList = getIncidentDAO(loginUser.getDbName()).getStatusByCategory(category);
		return statusList==null?Collections.emptyList():statusList;
	}
	
	@Override
	public String updateSlaDate(String ticketNumber, int duration, String unit, LoginUser loginUser) throws Exception {
		return  getIncidentDAO(loginUser.getDbName()).updateSlaDueDate(ticketNumber, duration, unit, "");
	}

	@Override
	@Transactional
	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser loginUser) throws Exception {
		TicketVO incidentVO = null;;
		LOGGER.info("Mode of Ticket Creation : " + customerTicket.getMode() +" by "+ loginUser.getUserType());
		if(customerTicket.getTicketId()==null && customerTicket.getMode().equalsIgnoreCase("NEW")){
			incidentVO = customerTicket;
			String ticketNumber=null;
			Long lastIncidentNumber = getIncidentDAO(loginUser.getDbName()).getLastIncidentCreated(loginUser.getUserType(), customerTicket.getTicketAssignedType());
			Long newIncidentNumber =null;
			if(lastIncidentNumber==null || lastIncidentNumber == 0){
				newIncidentNumber = 1l;
				lastIncidentNumber = 0l;
			}else{
				newIncidentNumber = lastIncidentNumber + 1;
			}
			newIncidentNumber = lastIncidentNumber + 1;
			if(loginUser.getUserType().equalsIgnoreCase("USER")){
				ticketNumber = "IN" +  String.format("%08d", newIncidentNumber);
			}
			else if(loginUser.getUserType().equalsIgnoreCase("SP") && !customerTicket.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
				 ticketNumber = "SP" +  String.format("%08d", newIncidentNumber);
			}
			else if(loginUser.getUserType().equalsIgnoreCase("SP") && customerTicket.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
				 ticketNumber = "EXT" +  String.format("%07d", newIncidentNumber);
			}
			LOGGER.info("Ticket Number Generated for {} is {}  "+ loginUser.getUserType() , ticketNumber);
			incidentVO.setTicketNumber(ticketNumber);
			
			CreateSiteVO site = getSiteDAO(loginUser.getDbName()).getSiteDetails(incidentVO.getSiteId());
			incidentVO.setSite(site);
			incidentVO.setTicketStartTime(ApplicationUtil.makeSQLDateFromString(customerTicket.getTicketStartTime()));
			//Change the logic if the incident is created by Customer or Registered Service Provider
			incidentVO = getIncidentDAO(loginUser.getDbName()).saveOrUpdateIncident(incidentVO, loginUser);
			if(incidentVO.getMessage().equalsIgnoreCase("CREATED")  ){
				LOGGER.info("Creating Incident Folder for : " +  incidentVO.getTicketNumber());
				if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) &&
						customerTicket.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
					incidentVO.setRspCustMappedCompanyId(customerTicket.getRspCustMappedCompanyId());
					incidentVO.setRspCustMappedCompanyName(customerTicket.getRspCustMappedCompanyName());
					incidentVO.setRspCustMappedCompanyCode(customerTicket.getRspCustMappedCompanyCode());
				}else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && 
						customerTicket.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
					incidentVO = getRSPCreatedSelectedTicket(customerTicket.getTicketId(), loginUser, TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType());
					incidentVO.setRspCustMappedCompanyId(customerTicket.getRspCustMappedCompanyId());
					incidentVO.setRspCustMappedCompanyName(customerTicket.getRspCustMappedCompanyName());
					incidentVO.setRspCustMappedCompanyCode(customerTicket.getRspCustMappedCompanyCode());
				}
				String folderLocation = createIncidentFolder(incidentVO, loginUser, null);
				incidentVO = imageUploadFeature(loginUser, incidentVO, folderLocation);
				incidentVO.setMessage("CREATED");
			}
		}else if(customerTicket.getTicketId()!=null && customerTicket.getMode().equalsIgnoreCase("UPDATE")){
			if(loginUser.getUserType().equalsIgnoreCase("SP") && customerTicket.getTicketAssignedType().equalsIgnoreCase("RSP")){
				incidentVO = getRSPCreatedSelectedTicket(customerTicket.getTicketId(), loginUser, "RSP");
			}else if(loginUser.getUserType().equalsIgnoreCase("SP") && customerTicket.getTicketAssignedType().equalsIgnoreCase("CUSTOMER")){
				incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
			}else if(loginUser.getUserType().equalsIgnoreCase("SP") && customerTicket.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
				incidentVO = getRSPCreatedSelectedTicket(customerTicket.getTicketId(), loginUser,customerTicket.getTicketAssignedType());
			}else{
				incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
			}
			BeanUtils.copyProperties(customerTicket, incidentVO);
			CreateSiteVO site = getSiteDAO(loginUser.getDbName()).getSiteDetails(incidentVO.getSiteId());
			incidentVO.setSite(site);
			incidentVO.setTicketStartTime(ApplicationUtil.makeSQLDateFromString(customerTicket.getTicketStartTime()));
			//Change the logic if the incident is created by Customer or Registered Service Provider
			incidentVO = getIncidentDAO(loginUser.getDbName()).saveOrUpdateIncident(incidentVO, loginUser);
			LOGGER.info("Updated Ticket : " +  incidentVO);
			incidentVO.setMessage("UPDATED");
		}
		
		else if(customerTicket.getMode().equalsIgnoreCase("IMAGEUPLOAD")){
			/*ServiceProvider serviceProvider = getIncidentDAO(loginUser.getDbName()).getTicketServiceProvider(incidentVO);
			incidentVO.setServiceProvider(serviceProvider);*/
			if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) &&
					customerTicket.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				incidentVO = getRSPCreatedSelectedTicket(customerTicket.getTicketId(), loginUser, TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType());
				incidentVO.setRspCustMappedCompanyId(customerTicket.getRspCustMappedCompanyId());
				incidentVO.setRspCustMappedCompanyName(customerTicket.getRspCustMappedCompanyName());
				incidentVO.setRspCustMappedCompanyCode(customerTicket.getRspCustMappedCompanyCode());
			}else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && 
					customerTicket.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
				incidentVO = getRSPCreatedSelectedTicket(customerTicket.getTicketId(), loginUser, TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType());
				incidentVO.setRspCustMappedCompanyId(customerTicket.getRspCustMappedCompanyId());
				incidentVO.setRspCustMappedCompanyName(customerTicket.getRspCustMappedCompanyName());
				incidentVO.setRspCustMappedCompanyCode(customerTicket.getRspCustMappedCompanyCode());
			}
			else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
				incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
			}
			else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
				incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
			}
			incidentVO.setIncidentImageList(customerTicket.getIncidentImageList());
			String folderLocation = createIncidentFolder(incidentVO, loginUser, null);
			incidentVO = imageUploadFeature(loginUser, incidentVO, folderLocation);
			
		}
		return incidentVO;
	}

	private TicketVO imageUploadFeature(LoginUser loginUser, TicketVO incidentVO, String folderLocation) {
		if(StringUtils.isNotEmpty(folderLocation)){
			if(!incidentVO.getIncidentImageList().isEmpty()){
				boolean isUploaded = uploadIncidentImages(incidentVO, loginUser,null, folderLocation, loginUser.getUsername());
				if(isUploaded){
					incidentVO.setFileUploaded(isUploaded);
					incidentVO.setMessage("UPDATED");
				}else{
					incidentVO.setFileUploaded(isUploaded);
				}
			}
		}
		return incidentVO; 
	}


	private String createIncidentFolder(TicketVO incidentVO, LoginUser user, Company spSiteCompany) {
		LOGGER.info("Inside TicketServiceImpl .. createIncidentFolder");
		String incidentFolderLocation="";
		try {
			if(spSiteCompany!=null){
			 incidentFolderLocation = createIncidentFolder(incidentVO.getTicketNumber(), spSiteCompany) ;
			}
			if(user!=null){
				if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && !incidentVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST") ){
					LOGGER.info("RSP Mapped Customer Code to Create Folder : "+ incidentVO.getRspCustMappedCompanyCode());
					 user.getCompany().setCompanyId(incidentVO.getRspCustMappedCompanyId());
					 user.getCompany().setCompanyName(incidentVO.getRspCustMappedCompanyName());
					 user.getCompany().setCompanyCode(incidentVO.getRspCustMappedCompanyCode());
					incidentFolderLocation = createIncidentFolder(incidentVO.getTicketNumber(),user.getCompany()) ;
				}
				else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && incidentVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST") ){
					LOGGER.info("RSP Externa Customer to Create Folder : "+ incidentVO.getRspCustMappedCompanyCode());
					incidentFolderLocation = createIncidentFolder(incidentVO.getTicketNumber(),user.getCompany()) ;
				}
				else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
					LOGGER.info("Customer Code to Create Folder : " +  user.getCompany().getCompanyCode());
					incidentFolderLocation = createIncidentFolder(incidentVO.getTicketNumber(),user.getCompany()) ;
				}
				 //fileIntegrationService.createIncidentFolder(ticketNumber, user.getCompany());
			}
			if(StringUtils.isNotEmpty(incidentFolderLocation)){
				LOGGER.info("Incident Folder Created..");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		LOGGER.info("Exit TicketServiceImpl .. createIncidentFolder");
		return incidentFolderLocation;
	}
	
	public String createIncidentFolder(String incidentNumber, Company company) throws IOException {
		LOGGER.info("Inside TicketServiceImpl .. createIncidentFolder");
		String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
		String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
		LOGGER.info("Incident Folder Location : "+  fileUploadLocation);
		String directoryName = incidentNumber;
		 File uploadDirectory = new File(fileUploadLocation+"\\"+company.getCompanyCode()+"\\incident\\"+directoryName);
		 File downloadDirectory = new File(fileDownloadLocation+"\\"+company.getCompanyCode()+"\\incident\\"+directoryName);
		    if (! uploadDirectory.exists()){
		        if(uploadDirectory.mkdir()){
		        	 LOGGER.info("Incident Upload Folder created : "+  uploadDirectory.getPath());
		        	 downloadDirectory.mkdir();
		        	 LOGGER.info("Incident Download Folder created : "+  downloadDirectory.getPath());
		        }else{
		        	LOGGER.info("Unable to create incident directory");
		        }
		       
		    }else{
		    	LOGGER.info("Directory already exists");
		    }
		LOGGER.info("Exit TicketServiceImpl .. createIncidentFolder");
		return uploadDirectory.getPath();
	}


	private boolean uploadIncidentImages(TicketVO customerTicketVO, LoginUser loginUser, Company company, String folderLocation, String uploadedBy) {
		LOGGER.info("Inside TicketServiceImpl .. uploadIncidentImages");
		boolean isUploaded =false;
		try {
			if (loginUser != null) {
				List<TicketAttachment> ticketAttachments = siteIncidentFileUpload(
						customerTicketVO.getIncidentImageList(), customerTicketVO, loginUser.getCompany(),
						folderLocation, uploadedBy);
				if (loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())
						&& customerTicketVO.getTicketAssignedType()
								.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())) {
					if (!ticketAttachments.isEmpty()) {
						int attachmenentSaved = getIncidentDAO(loginUser.getDbName())
								.insertOrUpdateTicketAttachments(ticketAttachments, AppConstants.RSP_INSERT_TICKET_ATTACHMENT_QUERY);
						if (attachmenentSaved > 0) {
							isUploaded = true;
						}
					}
				}
				else if (loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())
						&& customerTicketVO.getTicketAssignedType()
								.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())) {
					if (!ticketAttachments.isEmpty()) {
						int attachmenentSaved = getIncidentDAO(loginUser.getDbName())
								.insertOrUpdateTicketAttachments(ticketAttachments, AppConstants.INSERT_TICKET_ATTACHMENT_QUERY);
						if (attachmenentSaved > 0) {
							isUploaded = true;
						}
					}
				}
				else if (loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())) {
					if (!ticketAttachments.isEmpty()) {
						int attachmenentSaved = getIncidentDAO(loginUser.getDbName())
								.insertOrUpdateTicketAttachments(ticketAttachments, AppConstants.INSERT_TICKET_ATTACHMENT_QUERY);
						if (attachmenentSaved > 0) {
							isUploaded = true;
						}
					}
				}
			}
			if(company!=null){
				List<TicketAttachment> ticketAttachments = siteIncidentFileUpload(customerTicketVO.getIncidentImageList(), customerTicketVO, company, folderLocation, uploadedBy);
				int attachmenentSaved=getIncidentDAO(loginUser.getDbName()).insertOrUpdateTicketAttachments(ticketAttachments, AppConstants.INSERT_TICKET_ATTACHMENT_QUERY);
				if(attachmenentSaved>0){
					isUploaded = true;
				}
			}
		} catch (IOException e) {
			LOGGER.info("Exception while uploading to s3", e);
			isUploaded =false;
			
		}
		
		LOGGER.info("Exit TicketServiceImpl .. uploadIncidentImages");
		return isUploaded;
	}

	private List<TicketAttachment> siteIncidentFileUpload(List<UploadFile> incidentImageList, TicketVO customerTicketVO, Company company,
			String folderLocation, String uploadedBy) throws IOException {
			LOGGER.info("Inside FileIntegrationServiceImpl .. siteIncidentFileUpload");
			Map<Path,String> incidentKeyMap = new HashMap<Path, String>();
			List<TicketAttachment> ticketAttachmentList = new ArrayList<TicketAttachment>();
			for(UploadFile attachment : incidentImageList){
				String base64Image = "";
				String fileKey="";
				Path destinationFile = null;
				String generatedFileName="";
				//String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
					base64Image = attachment.getBase64ImageString().split(",")[1];
					generatedFileName=attachment.getFile()+"_"+Calendar.getInstance().getTimeInMillis()+"."+attachment.getFileExtension().toLowerCase();
					destinationFile = Paths.get(folderLocation+"\\"+generatedFileName);
					fileKey=company.getCompanyCode()+"/incident/"+customerTicketVO.getTicketNumber()+"/"+generatedFileName;
				if(StringUtils.isEmpty(base64Image)){
					LOGGER.info("No Image or Document selected" );
				}else{
					byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
					try {
						Files.write(destinationFile, imageBytes);
						LOGGER.info("Saving image to location : "+ destinationFile.toString() );
						incidentKeyMap.put(destinationFile, fileKey);
						TicketAttachment ticketAttachment = new TicketAttachment();
						ticketAttachment.setAttachmentPath(fileKey);
						ticketAttachment.setTicketId(customerTicketVO.getTicketId());
						ticketAttachment.setTicketNumber(customerTicketVO.getTicketNumber());
						ticketAttachment.setCreatedBy(uploadedBy);
						ticketAttachmentList.add(ticketAttachment);
					} catch (IOException e) {
						LOGGER.info("Unable to upload incident files ", e );
					}
				}
			}
			
			for (Map.Entry<Path, String> pathEntry : incidentKeyMap.entrySet()) {
				LOGGER.info("Uploading file to S3 : "+ pathEntry.getValue());
				RestResponse response = pushToAwsS3(pathEntry.getKey(),  pathEntry.getValue());
			}

			LOGGER.info("Exit FileIntegrationServiceImpl .. siteIncidentFileUpload");
			return ticketAttachmentList;
		
	}
	private RestResponse pushToAwsS3(Path destinationFile, String fileKey) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		RestResponse response = new RestResponse();
		@SuppressWarnings("deprecation")
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		try{
			List<Bucket> bucketList = s3client.listBuckets();
			if(bucketList != null){
				String bucketName="malay-first-s3-bucket-pms-test";
				awsIntegrationService.uploadObject(new PutObjectRequest(bucketName, fileKey, destinationFile.toFile()).withCannedAcl(CannedAccessControlList.Private), s3client);
				response.setStatusCode(200);
			}else{
				response.setStatusCode(503);
				response.setMessage("AWS Service Not Available");
			}
		}catch(Exception e){
			LOGGER.info("AWS service not available ", e.getMessage() );
			response.setStatusCode(503);
			response.setMessage("AWS Service Not Available");
		}
		return response;
	}

	@Override
	@Transactional
	public TicketVO getSelectedTicket(Long ticketId, LoginUser loginUser) throws Exception {
		LOGGER.info("Getting ticket details for Customer created by Customer ");
		SelectedTicketVO selectedTicket = null;
		TicketVO ticketVO = new TicketVO();
		List<String> addressList = new ArrayList<String>(4);
		selectedTicket= getIncidentDAO(loginUser.getDbName()).getSelectedTicket(ticketId);
		ticketVO.setTicketId(selectedTicket.getId()); 
		ticketVO = getTicketDetails(loginUser, selectedTicket, ticketVO, addressList);
		ticketVO.setRaisedUser(Long.parseLong(selectedTicket.getPhone()));
		return ticketVO;
	}
	
	@Override
	@Transactional
	public TicketVO getRSPCreatedSelectedTicket(Long ticketId, LoginUser loginUser, final String operationType) throws Exception {
		LOGGER.info("Getting ticket details for RSP created by RSP ");
		SelectedTicketVO selectedTicket = null;
		TicketVO ticketVO = new TicketVO();
		List<String> addressList = new ArrayList<String>(4);
		selectedTicket= getIncidentDAO(loginUser.getDbName()).getRSPSelectedTicket(ticketId, operationType);
		ticketVO.setTicketId(selectedTicket.getId()); 
	/*	if(StringUtils.isEmpty(loginUser.getSpDbName())){
			LOGGER.info("Getting RSP User details for the ticket created ");
			SPUserVo spUserVo = new SPUserVo(); 
			spUserVo.setUserEmail(selectedTicket.getCreated_by());
			spUserVo = getServiceProviderDAOImpl(loginUser.getSpDbName()).getServiceProviderUserByEmail(spUserVo);
			selectedTicket.setFirst_name(spUserVo.getFirstName()+" "+spUserVo.getLastName());
			ticketVO.setCreatedUser(spUserVo.getFirstName()+" "+spUserVo.getLastName());
			ticketVO.setRaisedUser(spUserVo.getUserPhone());
		}
		else{
			ticketVO.setCreatedUser(selectedTicket.getFirst_name()+" "+selectedTicket.getLast_name());
			//ticketVO.setRaisedUser(Long.parseLong(selectedTicket.getPhone()));
		}*/
		ticketVO.setTicketId(selectedTicket.getId()); 
		ticketVO = getTicketDetails(loginUser, selectedTicket, ticketVO, addressList);
		return ticketVO;
	}

	private TicketVO getTicketDetails(LoginUser loginUser, SelectedTicketVO selectedTicket, TicketVO ticketVO,
			List<String> addressList) {
		ticketVO.setCreatedUser(selectedTicket.getFirst_name()+" "+selectedTicket.getLast_name());
		ticketVO.setTicketTitle(selectedTicket.getTicket_title());
		ticketVO.setTicketNumber(selectedTicket.getTicket_number());
		ticketVO.setDescription(selectedTicket.getTicket_desc());
		
		ticketVO.setSiteId(selectedTicket.getSite_id());
		ticketVO.setSiteName(selectedTicket.getSite_name());	
		ticketVO.setSiteNumber1(String.valueOf(selectedTicket.getSite_number1()));
		ticketVO.setSiteNumber2(selectedTicket.getSite_number2()==null?null:String.valueOf(selectedTicket.getSite_number2()));
		if(StringUtils.isNotEmpty(selectedTicket.getSite_address1())){
			addressList.add(selectedTicket.getSite_address1());
		}
		if(StringUtils.isNotEmpty(selectedTicket.getSite_address2())){
			addressList.add(selectedTicket.getSite_address2());
		}
		if(StringUtils.isNotEmpty(selectedTicket.getSite_address3())){
			addressList.add(selectedTicket.getSite_address3());
		}
		if(StringUtils.isNotEmpty(selectedTicket.getSite_address4())){
			addressList.add(selectedTicket.getSite_address4());
		}
		String finalAddress = StringUtils.join(addressList,", ");
		
		if(StringUtils.isNotEmpty(selectedTicket.getPost_code())){
			ticketVO.setSiteAddress(finalAddress + ", " +selectedTicket.getPrimary_contact_number() +", "+selectedTicket.getPost_code() );
		}
		else{
		ticketVO.setSiteAddress(finalAddress + ", " +selectedTicket.getPrimary_contact_number());
		}
		ticketVO.setSiteContact(String.valueOf(selectedTicket.getPrimary_contact_number()));
		ticketVO.setSiteOwner(selectedTicket.getSite_owner());
		ticketVO.setEmail(selectedTicket.getEmail());
		ticketVO.setAssetId(selectedTicket.getAsset_id());
		ticketVO.setAssetCategoryId(selectedTicket.getAsset_category_id());
		ticketVO.setAssetCategoryName(selectedTicket.getCategory_name());
		ticketVO.setAssetName(selectedTicket.getAsset_name());
		ticketVO.setAssetCode(selectedTicket.getAsset_code());
		ticketVO.setAssetModel(selectedTicket.getModel_number());
		ticketVO.setAssetSubCategory1(selectedTicket.getAsset_subcategory1());
		ticketVO.setAssetSubCategory2(selectedTicket.getSubcategory2_name());
		ticketVO.setAssetCommissionedDate(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getDate_commissioned()));
		ticketVO.setCategoryId(selectedTicket.getTicket_category_id());
		ticketVO.setCategoryName(selectedTicket.getTicket_category());
		ticketVO.setPriorityDescription(selectedTicket.getPriority());
		ticketVO.setSla(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getSla_duedate()));
		ticketVO.setTicketStartTime(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getTicket_starttime()));
		ticketVO.setServiceRestorationTime(selectedTicket.getService_restoration_ts());
		ticketVO.setCloseCode(selectedTicket.getClose_code()==null?null:Long.parseLong(selectedTicket.getClose_code()));
		ticketVO.setClosedNote(selectedTicket.getClosed_code_desc());
		if(selectedTicket.getAssigned_to()!=null){
			LOGGER.info("Getting ticket details assigned to External SP");
			ticketVO.setTicketAssignedType("EXT");
			ticketVO.setAssignedTo(selectedTicket.getAssigned_to());
			ticketVO.setAssignedSP(selectedTicket.getSp_name());
			List<EscalationLevelVO> escalationLevelVOs = getTicketEscalationList(selectedTicket.getAssigned_to(), loginUser, AppConstants.SP_ESCALATIONS_QUERY);
			ticketVO.setEscalationLevelList(escalationLevelVOs);
			
		}
		
		else {
			LOGGER.info("Getting ticket details assigned to Registered SP");
			ticketVO.setTicketAssignedType("RSP");
			ticketVO.setAssignedTo(selectedTicket.getRassigned_to());
			ticketVO.setAssignedSP(selectedTicket.getRsp_name());
			List<EscalationLevelVO> escalationLevelVOs = null;// getTicketEscalationList(selectedTicket.getRassigned_to(), loginUser, AppConstants.RSP_ESCALATIONS_QUERY);
			ticketVO.setEscalationLevelList(escalationLevelVOs);
			
		}
		
		//List<TicketCommentVO> ticketComments = getTicketComments(ticketVO.getTicketId(), loginUser);
		//ticketVO.setTicketComments(ticketComments);
		ticketVO.setClosedBy(selectedTicket.getClosed_by());
		ticketVO.setClosedOn(selectedTicket.getClosed_on());
		
		ticketVO.setRaisedBy(selectedTicket.getCreated_by());

		ticketVO.setCreatedOn(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getCreated_on()));
		ticketVO.setStatusId(selectedTicket.getStatus_id());
		ticketVO.setStatusDescription(selectedTicket.getDescription());
		ticketVO.setStatus(selectedTicket.getStatus());
		ticketVO.setSlaPercent(getSLAPercent(ticketVO));
		return ticketVO;
	}
	

	private double getSLAPercent(TicketVO customerTicket) {
		synchronized (customerTicket) {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
			double slaPercent = 0.0;
			try {
				Date slaDueDate = simpleDateFormat.parse(customerTicket.getSla());
				Date creationTime = simpleDateFormat.parse(customerTicket.getCreatedOn());

				if (customerTicket.getStatusId().intValue() != 15) {
					Date currentDateTime = new Date();
					long numeratorDiff = currentDateTime.getTime() - creationTime.getTime();
					long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();

					double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
					double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
					if (deNumValue != 0) {
						slaPercent = Math.round((numValue / deNumValue) * 100);
					}
					return slaPercent;
				} else {
					if (customerTicket.getServiceRestorationTime() != null) {
						Date restoredTime = simpleDateFormat.parse(customerTicket.getServiceRestorationTime());
						long numeratorDiff = restoredTime.getTime() - creationTime.getTime();
						long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();

						double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
						double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
						if (deNumValue != 0) {
							slaPercent = Math.round((numValue / deNumValue) * 100);
						}
					}
					return slaPercent;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return 0;
		}
	}

	
	
	@Override
	public List<TicketAttachment> findByTicketId(Long ticketId, LoginUser loginUser, final TicketVO ticketVO) throws Exception {
		List<TicketAttachment> selectedTicketAttachments = new ArrayList<TicketAttachment>();
		if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) &&
				ticketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
			selectedTicketAttachments = getIncidentDAO(loginUser.getDbName()).getTicketAttachments(ticketId, AppConstants.RSP_TICKET_ATTACHMENTS);
		}
		if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) &&
				ticketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
			selectedTicketAttachments = getIncidentDAO(loginUser.getDbName()).getTicketAttachments(ticketId, AppConstants.TICKET_ATTACHMENTS);
		}
		if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) ){
			selectedTicketAttachments = getIncidentDAO(loginUser.getDbName()).getTicketAttachments(ticketId, AppConstants.TICKET_ATTACHMENTS);
		}
		if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
			selectedTicketAttachments = getIncidentDAO(loginUser.getDbName()).getTicketAttachments(ticketId, AppConstants.TICKET_ATTACHMENTS);
		}

		return selectedTicketAttachments==null?Collections.emptyList():selectedTicketAttachments;
	}
	
	@Override
	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser user, String ticketAssignedType) throws Exception {
			TicketComment ticketComment = new TicketComment();
			ticketComment.setCreatedBy(user.getUsername());
			ticketComment.setCustTicketNumber(ticketCommentVO.getTicketNumber());
			ticketComment.setTicketId(ticketCommentVO.getTicketId());
			ticketComment.setComment(ticketCommentVO.getComment());
			
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				 if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
					 ticketComment = getIncidentDAO(user.getDbName()).saveTicketComment(ticketComment, user, AppConstants.INSERT_RSP_TICKET_COMMENT_QUERY);
				 }
				 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
					 ticketComment = getIncidentDAO(user.getDbName()).saveTicketComment(ticketComment, user, AppConstants.INSERT_TICKET_COMMENT_QUERY);
				 }
				 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
					 ticketComment = getIncidentDAO(user.getDbName()).saveTicketComment(ticketComment, user, AppConstants.INSERT_TICKET_COMMENT_QUERY);
				 }
			}else{
				ticketComment = getIncidentDAO(user.getDbName()).saveTicketComment(ticketComment, user, AppConstants.INSERT_TICKET_COMMENT_QUERY);
			}
			
			if(ticketComment.getCommentId()!=null){
				ticketCommentVO.setTicketNumber(ticketComment.getCustTicketNumber());
				ticketCommentVO.setTicketId(ticketComment.getTicketId());
				ticketCommentVO.setCommentId(ticketComment.getCommentId());
				ticketCommentVO.setComment(ticketComment.getComment());
				ticketCommentVO.setCreatedBy(ticketComment.getCreatedBy());
				SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
				ticketCommentVO.setCreatedDate(simpleDateFormat.format(ticketComment.getCreatedDate()));
			}
		return ticketCommentVO;
		
	}
	
	@Override
	public List<TicketCommentVO> getTicketComments(Long ticketId, LoginUser user, String ticketAssignedType) {
		List<TicketCommentVO> commentListVO = new ArrayList<TicketCommentVO>();
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			 if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				commentListVO = getIncidentDAO(user.getDbName()).getTicketComments(ticketId, AppConstants.RSP_TICKET_COMMENTS);
			 }
			 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				 commentListVO = getIncidentDAO(user.getDbName()).getTicketComments(ticketId, AppConstants.TICKET_COMMENTS);
			 }
			 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
				 commentListVO = getIncidentDAO(user.getDbName()).getTicketComments(ticketId, AppConstants.TICKET_COMMENTS);
			 }
		}else{
			commentListVO = getIncidentDAO(user.getDbName()).getTicketComments(ticketId, AppConstants.TICKET_COMMENTS);
		}
		return commentListVO == null?Collections.emptyList():commentListVO;
	}

	@Override
	public List<TicketHistoryVO> getTicketHistory(String ticketNumber, LoginUser user) throws Exception {
		List<TicketHistoryVO> ticketHistoryList = getIncidentDAO(user.getDbName()).getTicketHistory(ticketNumber);
		return ticketHistoryList == null?Collections.emptyList():ticketHistoryList;
	}
	
	@Override
	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, 
			String linkedTicket, LoginUser user, String spMappingType, Long rspAssignedTo) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - saveLinkedTicket");
		CustomerSPLinkedTicketVO  customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
		try{
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){	
			if(spMappingType.equalsIgnoreCase("EXT")){
				customerSPLinkedTicketVO.setCustTicketId(String.valueOf(custTicket));
				customerSPLinkedTicketVO.setCustTicketNumber(custTicketNumber);
				customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicket);
				customerSPLinkedTicketVO.setClosedFlag("OPEN");
				customerSPLinkedTicketVO.setSpType("EXT");
				customerSPLinkedTicketVO = getIncidentDAO(user.getDbName()).saveLinkedTicket(customerSPLinkedTicketVO, user, AppConstants.INSERT_EXT_TICKET_MAPPING_QUERY);
				customerSPLinkedTicketVO.setIsValidLink("VALID");
			}
			else if(spMappingType.equalsIgnoreCase("RSP")){
				TicketVO ticketVo = getIncidentDAO(user.getDbName()).findRSPTicket(linkedTicket, rspAssignedTo);
				if(StringUtils.isNotEmpty(ticketVo.getTicketNumber())){
					customerSPLinkedTicketVO.setCustTicketId(String.valueOf(custTicket));
					customerSPLinkedTicketVO.setCustTicketNumber(custTicketNumber);
					customerSPLinkedTicketVO.setRspTicketId(String.valueOf(ticketVo.getTicketId()));
					customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicket);
					customerSPLinkedTicketVO.setClosedFlag("OPEN");
					customerSPLinkedTicketVO.setSpType("RSP");
					customerSPLinkedTicketVO = getIncidentDAO(user.getDbName()).saveLinkedTicket(customerSPLinkedTicketVO, user, AppConstants.INSERT_RSP_TICKET_MAPPING_QUERY);
					customerSPLinkedTicketVO.setIsValidLink("VALID");
				}
			}
		}
		else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
			if(spMappingType.equalsIgnoreCase("EXT")){
				customerSPLinkedTicketVO.setCustTicketId(String.valueOf(custTicket));
				customerSPLinkedTicketVO.setCustTicketNumber(custTicketNumber);
				customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicket);
				customerSPLinkedTicketVO.setClosedFlag("OPEN");
				customerSPLinkedTicketVO.setSpType("EXT");
				customerSPLinkedTicketVO = getIncidentDAO(user.getDbName()).saveLinkedTicket(customerSPLinkedTicketVO, user, AppConstants.INSERT_EXT_TICKET_MAPPING_QUERY);
			}
		}
		
	
		}catch(Exception e){
			//e.printStackTrace();
			if(e.getMessage().contains("Duplicate")){
				LOGGER.info("Duplicate Entry Customer and Link Ticket");
				customerSPLinkedTicketVO.setIsValidLink("DUPLICATE");
			}
		}
		
		LOGGER.info("Exit TicketServiceImpl - saveLinkedTicket");
		return customerSPLinkedTicketVO;
	}
	
	@Override
	public CustomerSPLinkedTicketVO saveRSPLinkedTicket(Long parentRspTicketId, Long linkedTicketId,
			String linkedTicketType, String linkedTicketNumber, LoginUser user) throws Exception {
			CustomerSPLinkedTicketVO  customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
			if (user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())) {
				LOGGER.info("Saving RSP Customer Linked Ticket ");
				customerSPLinkedTicketVO.setRspTicketLongId(parentRspTicketId);
				if(linkedTicketType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
					customerSPLinkedTicketVO.setLinkedCTticketId(linkedTicketId);
					customerSPLinkedTicketVO.setLinkedTicketType("CT");
					customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicketNumber);
					customerSPLinkedTicketVO = getIncidentDAO(user.getDbName()).saveRSPLinkedTicket(customerSPLinkedTicketVO, user,
							AppConstants.INSERT_RSP_CUST_LINKED_TICKET_QUERY);
				}
				else if(linkedTicketType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
					LOGGER.info("Saving RSP Company Linked Ticket ");
					customerSPLinkedTicketVO.setLinkedRspTicketId(linkedTicketId);
					customerSPLinkedTicketVO.setLinkedTicketType("SP");
					customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicketNumber);
					customerSPLinkedTicketVO = getIncidentDAO(user.getDbName()).saveRSPLinkedTicket(customerSPLinkedTicketVO, user,
							AppConstants.INSERT_RSP_LINKED_TICKET_QUERY);
				}
				
				if(customerSPLinkedTicketVO.getId()!=null){
					customerSPLinkedTicketVO.setStatusId(200l);
				}
			}

		return customerSPLinkedTicketVO;
		
	}

	@Override
	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicketId, LoginUser loginUser) throws Exception {
		List<CustomerSPLinkedTicketVO> customerSPLinkedTickets = getIncidentDAO(loginUser.getDbName()).findByCustTicketIdAndDelFlag(custTicketId);
		return customerSPLinkedTickets== null?Collections.emptyList():customerSPLinkedTickets;
	}
	
	@Override
	public int changeLinkedTicketStatus(Long linkedTicket, LoginUser loginUser) throws Exception {
		int linkedStatus =  getIncidentDAO(loginUser.getDbName()).changeLinkedTicketStatus(linkedTicket, loginUser);
		return linkedStatus;
	}

	@Override
	public int deleteLinkedTicket(Long linkedTicket, LoginUser user) throws Exception {
		int linkedStatus = 0;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) ||  
				user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
			 linkedStatus =  getIncidentDAO(user.getDbName()).deleteLinkedTicket(linkedTicket, user);
		}
		else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			 if("RSP".equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				 linkedStatus =  getIncidentDAO(user.getDbName()).deleteRSPLinkedTicket(linkedTicket, user);
			 }
			 else if("CUSTOMER".equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				 linkedStatus =  getIncidentDAO(user.getDbName()).deleteRSPLinkedTicket(linkedTicket, user);
			 }
		}
		return linkedStatus;
	}
	
	@Override
	public List<TicketVO> getRelatedTickets(Long ticketId, Long siteId, LoginUser loginUser, String ticketAssignedType) throws Exception {
		List<TicketVO> customerTicketList = null;
		if(loginUser.getUserType().equalsIgnoreCase("USER")){
			customerTicketList = getIncidentDAO(loginUser.getDbName()).findRelatedTickets(ticketId, siteId);
		}
		else if(loginUser.getUserType().equalsIgnoreCase("EXTSP")){
			customerTicketList = getIncidentDAO(loginUser.getDbName()).findSPRelatedTickets(ticketId, siteId, loginUser.getSpId(), AppConstants.SP_RELATED_TICKETS_QUERY);
		}
		else if(loginUser.getUserType().equalsIgnoreCase("SP")){
			 if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				 customerTicketList = getIncidentDAO(loginUser.getDbName()).findSPRelatedTickets(ticketId, siteId, loginUser.getCompany().getCompanyId(), AppConstants.RSP_CUSTOMER_RELATED_TICKETS_QUERY);
			 }
			 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				 customerTicketList = getIncidentDAO(loginUser.getDbName()).findSPRelatedTickets(ticketId, siteId, loginUser.getCompany().getCompanyId(), AppConstants.RSP_COMPANY_RELATED_TICKETS_QUERY);
			 }
			 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
				 customerTicketList = getIncidentDAO(loginUser.getDbName()).findSPRelatedTickets(ticketId, siteId, loginUser.getCompany().getCompanyId(), RSPCustomerConstants.RSP_EXT_CUSTOMER_RELATED_TICKETS_QUERY);
			 }
		}
		return customerTicketList == null?Collections.emptyList():customerTicketList;
	}

	private List<EscalationLevelVO> getTicketEscalationList(Long spAssignedTo, LoginUser loginUser, final String spEscalationQuery) {
		List<EscalationLevelVO> escalationLevels =	getIncidentDAO(loginUser.getDbName()).getSPEscalation(spAssignedTo,loginUser, spEscalationQuery);		
		return escalationLevels == null?Collections.emptyList():escalationLevels;
	}
	

	@Override
	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) {
		LOGGER.info("Inside TicketServiceImpl - saveTicketEscalations");
		TicketEscalationVO savedTicketEscVO = null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) ||
				user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType()) ){
			if(ticketEscalationLevel.getTicketData().getTicketAssignedType().equalsIgnoreCase("EXT")){
				LOGGER.info("Updating Escalation for External SP");
				savedTicketEscVO =  getIncidentDAO(user.getDbName()).saveTicketEscalations(ticketEscalationLevel,user, AppConstants.INSERT_TICKET_ESCALATION_QUERY);
			}
			else if(ticketEscalationLevel.getTicketData().getTicketAssignedType().equalsIgnoreCase("RSP")){
				LOGGER.info("Updating Escalation for Registered SP in PM_CT_ESCALATION ");
				savedTicketEscVO =  getIncidentDAO(user.getDbName()).saveTicketEscalations(ticketEscalationLevel,user, AppConstants.INSERT_RSP_TICKET_ESCALATION_QUERY);
			}
		}
		else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			if(ticketEscalationLevel.getTicketData().getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				LOGGER.info("Updating Escalation for Registered SP in PM_CT_ESCALATION ");
				savedTicketEscVO =  getIncidentDAO(user.getDbName()).saveTicketEscalations(ticketEscalationLevel,user, AppConstants.INSERT_RSP_TICKET_ESCALATION_QUERY);
			}
			else if(ticketEscalationLevel.getTicketData().getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				LOGGER.info("Updating Escalation for Registered SP");
				savedTicketEscVO =  getIncidentDAO(user.getDbName()).saveTicketEscalations(ticketEscalationLevel,user, AppConstants.INSERT_RSP_COMPANY_TICKET_ESCALATION_QUERY);
			}
		}
		 
		
		LOGGER.info("Exit TicketServiceImpl - saveTicketEscalations");
		return savedTicketEscVO;
	}
	
	@Override
	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId, LoginUser user ) {
		List<TicketEscalationVO> escalatedTickets =  getIncidentDAO(user.getDbName()).getAllEscalations(ticketId);
		return escalatedTickets == null?Collections.emptyList():escalatedTickets;
	}


	@Override
	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId, LoginUser user, final String spType) {
		TicketEscalationVO savedTicketEscVO =null;
		 if(spType.equalsIgnoreCase("EXT")){
			 savedTicketEscVO = getIncidentDAO(user.getDbName()).findByTicketIdAndEscLevelId(ticketId, escId, AppConstants.TICKET_BY_ESCID);
		 }
		 
		 else  if(spType.equalsIgnoreCase("RSP")){
			 savedTicketEscVO = getIncidentDAO(user.getDbName()).findByTicketIdAndEscLevelId(ticketId, escId, AppConstants.TICKET_BY_RSP_ESCID);
		 }
		 else  if(spType.equalsIgnoreCase("EXTCUST")){
			 savedTicketEscVO = getIncidentDAO(user.getDbName()).findByTicketIdAndEscLevelId(ticketId, escId, AppConstants.TICKET_BY_RSP_ESCID);
		 }
		savedTicketEscVO.setEscalationStatus("Escalated");
		return savedTicketEscVO;
	}
	@Override
	public List<Financials> findFinanceByTicketId(Long ticketId, LoginUser user, final String ticketAssignedType) {
		List<Financials> financials=new ArrayList<Financials>();
		try {
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) ||  
					user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
				financials = getIncidentDAO(user.getDbName()).getTicketFinancials(ticketId,user, AppConstants.TICKET_FINANCE_SELECT_QUERY);
			}
			else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				 if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
					 financials =getIncidentDAO(user.getDbName()).getTicketFinancials(ticketId,user, AppConstants.TICKET_FINANCE_SELECT_QUERY);
				 }
				 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
					 financials =getIncidentDAO(user.getDbName()).getTicketFinancials(ticketId,user, AppConstants.RSP_TICKET_FINANCE_SELECT_QUERY);
				 }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return financials;
	}

/*	
	@Override
	public boolean deleteById(Long costid) {
		try {
			finRepo.delete(costid);
			return true;
		} catch (Exception e) {
			logger.error(
					"ERROR while Deleting Financials with CostId--> " + costid + " , Probably it was not found!!!! ");
			return false;
		}

	}*/

	@Override
	@Transactional
	public List<Financials> save(List<Financials> finList, LoginUser user, final String ticketAssignedType) {
		List<Financials> financials=new ArrayList<Financials>();
		try {
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) || 
					user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
				financials = customerTicketFinanceOperation(finList, user, financials);
		 }else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			 if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				 financials = customerTicketFinanceOperation(finList, user, financials);
			 }
			 else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				 for (Financials fin : finList) {
						if (fin.getId() != null) {
							Financials savedFinancial = getIncidentDAO(user.getDbName()).getTicketFinanceById(fin.getId(),user, AppConstants.RSP_TICKET_FINANCE_BY_ID);
							org.springframework.beans.BeanUtils.copyProperties(fin, savedFinancial);
							savedFinancial.setModifiedOn(new Date());
							savedFinancial.setModifiedBy(user.getUsername());
							Financials editedFinance = getIncidentDAO(user.getDbName()).updateFinance(savedFinancial, user, AppConstants.RSP_TICKET_FINANCE_UPDATE_QUERY);
							if(editedFinance!=null){
								financials.add(editedFinance);
							}
						} else {
							fin.setCreatedBy(user.getUsername());
							Financials savedFinance= getIncidentDAO(user.getDbName()).saveFinance(fin, user, AppConstants.RSP_TICKET_FINANCE_INSERT_QUERY);
							if(savedFinance!=null){
								financials.add(savedFinance);
							}
						}

					}
			 }
		 }
		} catch (Exception e) {
			LOGGER.error("ERROR while updating Financials ", e);
		}
		return financials == null ? Collections.emptyList():financials;
	}

	private List<Financials> customerTicketFinanceOperation(List<Financials> finList, LoginUser user, List<Financials> financials)
			throws Exception {
		for (Financials fin : finList) {
			if (fin.getId() != null) {
				Financials savedFinancial = getIncidentDAO(user.getDbName()).getTicketFinanceById(fin.getId(),user, AppConstants.TICKET_FINANCE_BY_ID);
				org.springframework.beans.BeanUtils.copyProperties(fin, savedFinancial);
				savedFinancial.setModifiedOn(new Date());
				savedFinancial.setModifiedBy(user.getUsername());
				Financials editedFinance = getIncidentDAO(user.getDbName()).updateFinance(savedFinancial, user, AppConstants.TICKET_FINANCE_UPDATE_QUERY);
				if(editedFinance!=null){
					financials.add(editedFinance);
				}
			} else {
				fin.setCreatedBy(user.getUsername());
				Financials savedFinance= getIncidentDAO(user.getDbName()).saveFinance(fin, user, AppConstants.TICKET_FINANCE_INSERT_QUERY);
				if(savedFinance!=null){
					financials.add(savedFinance);
				}
			}

		}
		return financials == null ? Collections.emptyList():financials;
	}

	@Override
	@Transactional
	public List<Financials> saveAndUpdate(List<FinancialVO> financialVOList, LoginUser user, String ticketAssignedType) {
			List<Financials> financials=new ArrayList<Financials>();
			for(FinancialVO finVO : financialVOList){
				Financials fin = new Financials();
				if(!StringUtils.isEmpty(finVO.getCostId())) {
					if(finVO.getIsEdited().equalsIgnoreCase("true")){
						fin.setId(Long.parseLong(finVO.getCostId()));
						fin.setCost(finVO.getCost());
						fin.setBillable(finVO.getBillable());
						fin.setChargeBack(finVO.getChargeBack());
						fin.setCreatedBy(user.getUsername());
						fin.setCostName(finVO.getCostName());
						fin.setTicketId(finVO.getTicketID());
						financials.add(fin);	
					}
				}
				else if(StringUtils.isEmpty(finVO.getCostId())) {
					fin.setCost(finVO.getCost());
					fin.setBillable(finVO.getBillable());
					fin.setChargeBack(finVO.getChargeBack());
					fin.setCreatedBy(user.getUsername());
					fin.setCostName(finVO.getCostName());
					fin.setTicketId(finVO.getTicketID());
					financials.add(fin);	
				}
				
			}
			List<Financials> savedFinanceList= save(financials, user, ticketAssignedType);
			return savedFinanceList == null ?Collections.emptyList():savedFinanceList;
	}

	@Override
	public List<Financials> saveFinancials(List<FinUpdReqBodyVO> finVOList, LoginUser user, String ticketAssignedType) {
		List<Financials> financialList = new ArrayList<Financials>();
		finVOList.forEach(FinupdVO -> {
			if (FinupdVO.getCostId()!=null) {
				if(FinupdVO.isEdited()){
				Financials fin = new Financials(FinupdVO.getCostId(),FinupdVO.getTicketID(), FinupdVO.getCostName(),
						FinupdVO.getCost(), String.valueOf(FinupdVO.getChargeBack()),String.valueOf(FinupdVO.getBillable()));
				financialList.add(fin);
				}

			} else {
				Financials fin = new Financials( FinupdVO.getTicketID(), FinupdVO.getCostName(),
						FinupdVO.getCost(), String.valueOf(FinupdVO.getChargeBack()),
						String.valueOf(FinupdVO.getBillable()));
				fin.setCreatedOn(new Date());
				financialList.add(fin);
			}
		});
		List<Financials> savedFinanceList = save(financialList, user, ticketAssignedType);
		return savedFinanceList == null ?Collections.emptyList():savedFinanceList;
	}

	@Override
	public boolean deleteFinanceCostById(Long costId, LoginUser user, String ticketAssignedType) throws Exception {
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) || 
				user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
			return getIncidentDAO(user.getDbName()).deleteFinanceCostById(costId,user, AppConstants.DELETE_TICKET_FINANCE_BY_ID);
		}
		else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
					return getIncidentDAO(user.getDbName()).deleteFinanceCostById(costId,user, AppConstants.RSP_DELETE_TICKET_FINANCE_BY_ID);
				}
				else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
					return getIncidentDAO(user.getDbName()).deleteFinanceCostById(costId,user, AppConstants.DELETE_TICKET_FINANCE_BY_ID);
				}
			}
		return false;
	}

	@Override
	public EscalationLevelVO getSPEscalationLevels(Long escId, LoginUser user, String ticketAssignedType)	throws Exception {
		EscalationLevelVO escLevelInfo = null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType()) ||  
				user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
			if(ticketAssignedType.equalsIgnoreCase("EXT")){
				escLevelInfo = getIncidentDAO(user.getDbName()).getEscalationLevelBy(escId, AppConstants.EXT_ESCALATION_BY_ESCID);
			}
			else if(ticketAssignedType.equalsIgnoreCase("RSP")){
				escLevelInfo = getIncidentDAO(user.getDbName()).getEscalationLevelBy(escId, AppConstants.RSP_ESCALATION_BY_ESCID);
			}
		}else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				escLevelInfo = getIncidentDAO(user.getDbName()).getEscalationLevelBy(escId, AppConstants.RSP_ESCALATION_BY_ESCID);
			/*else if(ticketAssignedType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
				escLevelInfo = getIncidentDAO(user.getDbName()).getEscalationLevelBy(escId, AppConstants.EXT_ESCALATION_BY_ESCID);
			}*/
		}
		return escLevelInfo;
	}

	@Override
	public List<TicketVO> getSuggestedTicketForAsset(LoginUser loginUser,Long assetId) throws Exception {
		List<TicketVO> rspSuggestedTicket = getIncidentDAO(loginUser.getDbName()).findRSPSuggestedTickets(assetId);
		return rspSuggestedTicket == null?Collections.emptyList():rspSuggestedTicket;
	}

	@Override
	public List<TicketVO> getRSPSuggestedTicketForAsset(LoginUser loginUser,Long assetId, TicketVO ticketVO) throws Exception {
		List<TicketVO> rspSuggestedTicket = getIncidentDAO(loginUser.getDbName()).findRSPReferenceTickets(assetId, loginUser.getCompany().getCompanyId(), ticketVO.getTicketNumber());
		for(TicketVO suggestedTicket: rspSuggestedTicket){
			suggestedTicket.setTicketAssignedType("RSP");
		}
		return rspSuggestedTicket == null?Collections.emptyList():rspSuggestedTicket;
	}

	@Override
	public List<TicketVO> getCustomerSuggestedTicketForAsset(LoginUser loginUser,Long assetId) throws Exception {
		List<TicketVO> customerSuggestedTicket = getIncidentDAO(loginUser.getDbName()).findCustomerSuggestedTickets(assetId, loginUser.getCompany().getCompanyId());
		for(TicketVO suggestedTicket: customerSuggestedTicket){
			suggestedTicket.setTicketAssignedType("CUSTOMER");
		}
		return customerSuggestedTicket == null?Collections.emptyList():customerSuggestedTicket;
	}

	@Override
	public List<CustomerSPLinkedTicketVO> getRSPLinkedTickets(LoginUser loginUser, Long parentTicketId, String linkedTicketType)
			throws Exception {
		List<CustomerSPLinkedTicketVO> customerSPLinkedTickets = new ArrayList<CustomerSPLinkedTicketVO>();
		if(linkedTicketType.equalsIgnoreCase("CT")){
			customerSPLinkedTickets =  getIncidentDAO(loginUser.getDbName()).findLinkedTicketForRSP(parentTicketId, linkedTicketType, AppConstants.RSP_CUSTOMER_LINKED_TICKETS);
		}
		else if(linkedTicketType.equalsIgnoreCase("SP")){
			customerSPLinkedTickets =  getIncidentDAO(loginUser.getDbName()).findLinkedTicketForRSP(parentTicketId, linkedTicketType, AppConstants.RSP_COMPANY_LINKED_TICKETS);
		}
		
		return customerSPLinkedTickets== null?Collections.emptyList():customerSPLinkedTickets;
	}

	@Override
	public IncidentTask saveRspTicketTask(IncidentTask incidentTask, LoginUser user, String ticketIncidentType) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!StringUtils.isEmpty(incidentTask.getPlanStartDate()) && !StringUtils.isEmpty(incidentTask.getPlanEndDate())) {
			Date startDate;
			Date endDate;
			try {
				startDate = formatter.parse(incidentTask.getPlanStartDate());
				endDate = formatter.parse(incidentTask.getPlanEndDate());
				incidentTask.setPlannedStartDate(startDate);
				incidentTask.setPlannedComplDate(endDate);
				if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
					if(ticketIncidentType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
						if(incidentTask.getTaskId()==null){
							incidentTask.setTaskNumber("INCTK-"+RandomUtils.randomIntger(7));
							incidentTask = getIncidentDAO(user.getDbName()).saveRspIncidentTask(incidentTask, user, AppConstants.INSERT_RSP_INCIDENT_TASK_QUERY);
						}
						else if(incidentTask.getTaskId()!=null){
							int updatedRows = getIncidentDAO(user.getDbName()).updateRspIncidentTask(incidentTask, user, AppConstants.UPDATE_RSP_INCIDENT_TASK_QUERY);
							if(updatedRows>0){
								incidentTask.setStatus(202);
							}
						}
					}else if(ticketIncidentType.equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
						if(incidentTask.getTaskId()==null){
							incidentTask.setTaskNumber("EXTINCTK-"+RandomUtils.randomIntger(7));
							incidentTask = getIncidentDAO(user.getDbName()).saveRspIncidentTask(incidentTask, user, AppConstants.INSERT_RSP_INCIDENT_TASK_QUERY);
						}
						else if(incidentTask.getTaskId()!=null){
							int updatedRows = getIncidentDAO(user.getDbName()).updateRspIncidentTask(incidentTask, user, AppConstants.UPDATE_RSP_INCIDENT_TASK_QUERY);
							if(updatedRows>0){
								incidentTask.setStatus(202);
							}
						}
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		return incidentTask;
	}

	@Override
	public List<IncidentTask> getIncidentTaskList(LoginUser user, Long ticketId, TicketVO selectedTicketVO)
			throws Exception {
		List<IncidentTask> incidentTaskList = new ArrayList<IncidentTask>();
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			if(selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
				incidentTaskList = getIncidentDAO(user.getDbName()).getRSPIncidentTasks(ticketId, AppConstants.RSP_INCIDENT_TASK_LIST_QUERY);
			}else if (selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
				incidentTaskList = getIncidentDAO(user.getDbName()).getRSPIncidentTasks(ticketId, AppConstants.RSP_INCIDENT_TASK_LIST_QUERY);
			}
		}
		return incidentTaskList== null?Collections.emptyList():incidentTaskList;
	}

	
	

}
