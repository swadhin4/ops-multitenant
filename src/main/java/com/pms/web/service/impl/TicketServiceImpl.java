package com.pms.web.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pms.app.dao.impl.IncidentDAO;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.Site;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.web.service.AwsIntegrationService;
import com.pms.web.service.TicketService;
import com.pms.web.util.ApplicationUtil;

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
	
	@Override
	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllCustomerTickets");
		LOGGER.info("Getting ticket List for logged in user : "+  loginUser.getFirstName() + "" + loginUser.getLastName());
		SiteDAO siteDAO = getSiteDAO(loginUser.getDbName());
		List<CreateSiteVO> siteList = siteDAO.getSiteList(loginUser.getUsername());
		Set<Long> siteIdList = new HashSet<Long>();
		for (CreateSiteVO siteVO : siteList) {
			siteIdList.add(siteVO.getSiteId());
		}
		List<TicketVO> customerTicketList = getIncidentDAO(loginUser.getDbName()).findTicketsBySiteIdIn(siteIdList);
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		LOGGER.info("Exit TicketServiceImpl - getAllCustomerTickets");
		return customerTicketList == null?Collections.emptyList():customerTicketList;
	}

	@Override
	public List<TicketCategory> getTicketCategories(LoginUser loginUser) throws Exception {
		List<TicketCategory> ticketCategories = getIncidentDAO(loginUser.getDbName()).findTicketCategories();
		return ticketCategories == null?Collections.emptyList():ticketCategories;
	}

	@Override
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId, LoginUser loginUser) throws Exception {
		TicketPrioritySLAVO  ticketPriority = getIncidentDAO(loginUser.getDbName()).getSPSLADetails(serviceProviderID, ticketCategoryId);
		return ticketPriority;
	}

	@Override
	public List<Status> getStatusByCategory(LoginUser loginUser, String category) throws Exception {
		List<Status> statusList = getIncidentDAO(loginUser.getDbName()).getStatusByCategory(category);
		return statusList==null?Collections.emptyList():statusList;
	}
	
	@Override
	public String updateSlaDate(String ticketNumber, int duration, String unit, LoginUser loginUser) throws Exception {
		return  getIncidentDAO(loginUser.getDbName()).updateSlaDueDate(ticketNumber, duration, unit);
	}

	@Override
	@Transactional
	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser loginUser) throws Exception {
		Long lastIncidentNumber = getIncidentDAO(loginUser.getDbName()).getLastIncidentCreated();
		Long newIncidentNumber =null;
		if(lastIncidentNumber == 0){
			newIncidentNumber = 1l;
		}else{
			newIncidentNumber = lastIncidentNumber + 1;
		}
		newIncidentNumber = lastIncidentNumber + 1;
		String ticketNumber = "IN" +  String.format("%08d", newIncidentNumber);
		LOGGER.info("Ticket Number Generated : " + ticketNumber);
		customerTicket.setTicketNumber(ticketNumber);
		customerTicket.setTicketStartTime(ApplicationUtil.makeSQLDateFromString(customerTicket.getTicketStartTime()));
		customerTicket = getIncidentDAO(loginUser.getDbName()).saveIncident(customerTicket, loginUser);
		if(customerTicket.getTicketId()!=null && customerTicket.getMessage().equalsIgnoreCase("CREATED")){
			ServiceProvider serviceProvider = getIncidentDAO(loginUser.getDbName()).getTicketServiceProvider(customerTicket);
			CreateSiteVO site = getSiteDAO(loginUser.getDbName()).getSiteDetails(customerTicket.getSiteId());
			customerTicket.setServiceProvider(serviceProvider);
			customerTicket.setSite(site);
			String folderLocation = createIncidentFolder(customerTicket.getTicketNumber(), loginUser, null);
			if(StringUtils.isNotEmpty(folderLocation)){
				if(!customerTicket.getIncidentImageList().isEmpty()){
					boolean isUploaded = uploadIncidentImages(customerTicket, loginUser,null, folderLocation, loginUser.getUsername());
					customerTicket.setFileUploaded(isUploaded);
				}
			}
		}
		
		return customerTicket;
	}


	private String createIncidentFolder(String ticketNumber, LoginUser user, Company spSiteCompany) {
		LOGGER.info("Inside TicketServiceImpl .. createIncidentFolder");
		String incidentFolderLocation="";
		try {
			if(spSiteCompany!=null){
			 incidentFolderLocation = createIncidentFolder(ticketNumber, spSiteCompany) ;
			}
			if(user!=null){
				incidentFolderLocation = createIncidentFolder(ticketNumber, user.getCompany()) ; //fileIntegrationService.createIncidentFolder(ticketNumber, user.getCompany());
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


	private boolean uploadIncidentImages(TicketVO customerTicketVO, LoginUser user, Company company, String folderLocation, String uploadedBy) {
		LOGGER.info("Inside TicketServiceImpl .. uploadIncidentImages");
		boolean isUploaded =false;
		try {
			if(user!=null){
				List<TicketAttachment> ticketAttachments = siteIncidentFileUpload(customerTicketVO.getIncidentImageList(), customerTicketVO, user.getCompany(), folderLocation, uploadedBy);
				if(!ticketAttachments.isEmpty()){
					int attachmenentSaved=getIncidentDAO(user.getDbName()).insertOrUpdateTicketAttachments(ticketAttachments);
					if(attachmenentSaved>0){
						isUploaded = true;
					}
				}
				
			}
			if(company!=null){
				List<TicketAttachment> ticketAttachments = siteIncidentFileUpload(customerTicketVO.getIncidentImageList(), customerTicketVO, company, folderLocation, uploadedBy);
				int attachmenentSaved=getIncidentDAO(user.getDbName()).insertOrUpdateTicketAttachments(ticketAttachments);
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
				pushToAwsS3(pathEntry.getKey(),  pathEntry.getValue());
			}

			LOGGER.info("Exit FileIntegrationServiceImpl .. siteIncidentFileUpload");
			return ticketAttachmentList;
		
	}
	private void pushToAwsS3(Path destinationFile, String fileKey) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIAJZTA6BYNTESWQWBQ","YWzhoGSfC1ADDT+xHzvAsvf/wyMlSl71TexLLg8t");
		@SuppressWarnings("deprecation")
		AmazonS3 s3client = new AmazonS3Client(credentials);
		s3client.setRegion(com.amazonaws.regions.Region.getRegion(Regions.US_WEST_2));
		String bucketName="malay-first-s3-bucket-pms-test";
		awsIntegrationService.uploadObject(new PutObjectRequest(bucketName, fileKey, destinationFile.toFile()).withCannedAcl(CannedAccessControlList.Private), s3client);
	}
	
	
	/*private TicketVO getSelectedTicketDetails(SimpleDateFormat simpleDateFormat, CustomerTicket customerTicket) {
		List<String> fullAddress = new ArrayList<String>();
		TicketVO tempCustomerTicketVO = new TicketVO();
		tempCustomerTicketVO.setTicketId(customerTicket.getId());
		tempCustomerTicketVO.setTicketNumber(customerTicket.getTicketNumber());
		tempCustomerTicketVO.setTicketTitle(customerTicket.getTicketTitle());
		tempCustomerTicketVO.setDescription(customerTicket.getTicketDesc());
		tempCustomerTicketVO.setSiteId(customerTicket.getSite().getSiteId());
		tempCustomerTicketVO.setSiteName(customerTicket.getSite().getSiteName());
		tempCustomerTicketVO.setAssetId(customerTicket.getAsset().getAssetId());
		tempCustomerTicketVO.setAssetName(customerTicket.getAsset().getAssetName());
		tempCustomerTicketVO.setAssetCode(customerTicket.getAsset().getAssetCode());
		
		if(customerTicket.getSite().getPrimaryContact()!=null){
			tempCustomerTicketVO.setSiteContact(customerTicket.getSite().getPrimaryContact().toString());
			}
			if(customerTicket.getSite().getSiteNumberOne()!=null){
			tempCustomerTicketVO.setSiteNumber1(customerTicket.getSite().getSiteNumberOne().toString());
			}
			if(customerTicket.getSite().getSiteNumberTwo()!=null){
			tempCustomerTicketVO.setSiteNumber2(customerTicket.getSite().getSiteNumberTwo().toString());
			}
			
			if(customerTicket.getAsset().getModelNumber()!=null){
			tempCustomerTicketVO.setAssetModel(customerTicket.getAsset().getModelNumber());
			}
		
		tempCustomerTicketVO.setCategoryId(customerTicket.getTicketCategory().getId());
		tempCustomerTicketVO.setCategoryName(customerTicket.getTicketCategory().getDescription());
		if(customerTicket.getAssetCategory()!=null){
			tempCustomerTicketVO.setAssetCategoryName(customerTicket.getAssetCategory().getAssetCategoryName());
			tempCustomerTicketVO.setAssetSubCategory1(customerTicket.getAssetRepairType().getAssetSubcategory1());
			tempCustomerTicketVO.setAssetSubCategory2(customerTicket.getAssetSubRepairType().getAssetSubcategory2());
		}
		tempCustomerTicketVO.setAssignedSP(customerTicket.getAssignedTo().getName());
		tempCustomerTicketVO.setAssignedTo(customerTicket.getAssignedTo().getServiceProviderId());
		tempCustomerTicketVO.setPriorityDescription(customerTicket.getPriority());
		tempCustomerTicketVO.setStatusId(customerTicket.getStatus().getStatusId());
		tempCustomerTicketVO.setStatus(customerTicket.getStatus().getStatus());
		if(StringUtils.isNotEmpty(customerTicket.getStatus().getDescription())){
			tempCustomerTicketVO.setStatusDescription(customerTicket.getStatus().getDescription());
			}
		tempCustomerTicketVO.setRaisedOn(simpleDateFormat.format(customerTicket.getCreatedOn()));
		tempCustomerTicketVO.setRaisedBy(customerTicket.getCreatedBy());
		tempCustomerTicketVO.setTicketStartTime(simpleDateFormat.format(customerTicket.getTicketStarttime()));
		tempCustomerTicketVO.setSla(simpleDateFormat.format(customerTicket.getSlaDuedate()));
		tempCustomerTicketVO.setCloseCode(customerTicket.getCloseCode());
		tempCustomerTicketVO.setClosedNote(customerTicket.getCloseNote());
		
		if(!StringUtils.isEmpty(customerTicket.getSite().getSiteAddress1())){
			fullAddress.add(customerTicket.getSite().getSiteAddress1());
		}
		if(!StringUtils.isEmpty(customerTicket.getSite().getSiteAddress2())){
			fullAddress.add(customerTicket.getSite().getSiteAddress2());
		}
		if(!StringUtils.isEmpty(customerTicket.getSite().getSiteAddress3())){
			fullAddress.add(customerTicket.getSite().getSiteAddress3());
		}
		if(!StringUtils.isEmpty(customerTicket.getSite().getSiteAddress4())){
			fullAddress.add(customerTicket.getSite().getSiteAddress4());
		}
		if(!StringUtils.isEmpty(customerTicket.getSite().getPostCode())){
			fullAddress.add(customerTicket.getSite().getPostCode());
		}
		
		String finalAddress = org.apache.commons.lang3.StringUtils.join(fullAddress,",");
		tempCustomerTicketVO.setSiteAddress(finalAddress);
		
		if(StringUtils.isNotBlank(customerTicket.getClosedBy())){
			tempCustomerTicketVO.setClosedBy(customerTicket.getClosedBy());
		}
		
		if(customerTicket.getClosedOn()!=null){
		tempCustomerTicketVO.setClosedOn(simpleDateFormat.format(customerTicket.getClosedOn()));
		}
		
		if(customerTicket.getCloseNote()!=null){
			tempCustomerTicketVO.setClosedNote(customerTicket.getCloseNote());
		}
		if(customerTicket.getModifiedOn()!=null){
			tempCustomerTicketVO.setModifiedOn(simpleDateFormat.format(customerTicket.getModifiedOn()));
		}
		
		if(StringUtils.isNotBlank(customerTicket.getModifiedBy())){
			tempCustomerTicketVO.setModifiedBy(customerTicket.getModifiedBy());
		}
		
		if(customerTicket.getServiceRestorationTime()!=null){
			tempCustomerTicketVO.setServiceRestorationTime(simpleDateFormat.format(customerTicket.getServiceRestorationTime()));
		}
		
		if(user!=null){
		tempCustomerTicketVO.setRaisedUser(user.getPhone());
		tempCustomerTicketVO.setCreatedUser(user.getFirstName() +" "+ user.getLastName());
		}
		double slaPercentage = getSLAPercent(customerTicket);
		tempCustomerTicketVO.setSlaPercent(slaPercentage);
		return tempCustomerTicketVO;
	}

	private double getSLAPercent(CustomerTicket customerTicket) {
		Date slaDueDate = customerTicket.getSlaDuedate();
		Date creationTime = customerTicket.getCreatedOn();
		
		if(customerTicket.getStatus().getStatusId().intValue() != 15){
			Date currentDateTime = new Date();
			long numeratorDiff = currentDateTime.getTime() - creationTime.getTime(); 
			long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
			double slaPercent=0.0;
			
			double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
			double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
			if (deNumValue != 0){
				slaPercent = Math.round((numValue / deNumValue) * 100);
			}	
			return slaPercent;
		}else{
			double slaPercent =0.0; 
			if(customerTicket.getServiceRestorationTime()!=null){
				Date restoredTime = customerTicket.getServiceRestorationTime();
				long numeratorDiff = restoredTime.getTime() - creationTime.getTime(); 
				long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
				
				double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
				double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
				if (deNumValue != 0){
					slaPercent = Math.round((numValue / deNumValue) * 100);
				}
			}
			return slaPercent;
		}
	}
*/
	

}
