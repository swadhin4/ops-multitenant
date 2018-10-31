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
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.pms.app.dao.impl.IncidentDAO;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.FinancialVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SelectedTicketVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.jpa.entities.TicketComment;
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
	public List<TicketVO> getAllSPTickets(LoginUser loginUser) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - getAllSPTickets");
		LOGGER.info("Getting ticket List for logged in SP user : "+  loginUser.getLastName());
		List<TicketVO> customerTicketList = getIncidentDAO(loginUser.getDbName()).findTicketsAssignedToSP(loginUser.getSpId());
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		LOGGER.info("Exit TicketServiceImpl - getAllSPTickets");
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
		TicketVO incidentVO = null;;
		if(customerTicket.getTicketId()==null && customerTicket.getMode().equalsIgnoreCase("NEW")){
			incidentVO = customerTicket;
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
			incidentVO.setTicketNumber(ticketNumber);
		}else if(customerTicket.getTicketId()!=null && customerTicket.getMode().equalsIgnoreCase("UPDATE")){
			incidentVO = getSelectedTicket(customerTicket.getTicketId(), loginUser);
			BeanUtils.copyProperties(customerTicket, incidentVO);
			LOGGER.info("Updated Ticket : " +  incidentVO);
			
		}
		if(!StringUtils.isEmpty(customerTicket.getMode())){
			if(!customerTicket.getMode().equalsIgnoreCase("IMAGEUPLOAD")){
				ServiceProvider serviceProvider = getIncidentDAO(loginUser.getDbName()).getTicketServiceProvider(incidentVO);
				CreateSiteVO site = getSiteDAO(loginUser.getDbName()).getSiteDetails(incidentVO.getSiteId());
				incidentVO.setServiceProvider(serviceProvider);
				incidentVO.setSite(site);
			}
			else{
				incidentVO = customerTicket; 
			}
			
			incidentVO.setTicketStartTime(ApplicationUtil.makeSQLDateFromString(customerTicket.getTicketStartTime()));
			incidentVO = getIncidentDAO(loginUser.getDbName()).saveOrUpdateIncident(incidentVO, loginUser);
		}
		String folderLocation = createIncidentFolder(incidentVO.getTicketNumber(), loginUser, null);
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

	@Override
	@Transactional
	public TicketVO getSelectedTicket(Long ticketId, LoginUser loginUser) throws Exception {
		SelectedTicketVO selectedTicket = getIncidentDAO(loginUser.getDbName()).getSelectedTicket(ticketId);
		TicketVO ticketVO = new TicketVO();
		List<String> addressList = new ArrayList<String>(4);
		ticketVO.setTicketId(selectedTicket.getId()); 
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
		ticketVO.setSiteAddress(finalAddress + ", " +selectedTicket.getPhone() +", "+ selectedTicket.getPost_code() );
		ticketVO.setSiteContact(String.valueOf(selectedTicket.getPrimary_contact_number()));
		
		ticketVO.setAssetId(selectedTicket.getAsset_id());
		ticketVO.setAssetCategoryId(selectedTicket.getAsset_category_id());
		ticketVO.setAssetCategoryName(selectedTicket.getCategory_name());
		ticketVO.setAssetName(selectedTicket.getAsset_name());
		ticketVO.setAssetCode(selectedTicket.getAsset_code());
		ticketVO.setAssetSubCategory1(selectedTicket.getAsset_subcategory1());
		ticketVO.setAssetSubCategory2(selectedTicket.getSubcategory2_name());
		
		ticketVO.setCategoryId(selectedTicket.getTicket_category_id());
		ticketVO.setCategoryName(selectedTicket.getTicket_category());
		ticketVO.setPriorityDescription(selectedTicket.getPriority());
		ticketVO.setSla(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getSla_duedate()));
		ticketVO.setTicketStartTime(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getTicket_starttime()));
		ticketVO.setServiceRestorationTime(selectedTicket.getService_restoration_ts());
		ticketVO.setCloseCode(selectedTicket.getClose_code()==null?null:Long.parseLong(selectedTicket.getClose_code()));
		ticketVO.setClosedNote(selectedTicket.getClosed_code_desc());
		ticketVO.setAssignedTo(selectedTicket.getAssigned_to());
		ticketVO.setAssignedSP(selectedTicket.getSp_name());
		ticketVO.setClosedBy(selectedTicket.getClosed_by());
		ticketVO.setClosedOn(selectedTicket.getClosed_on());
		ticketVO.setCreatedUser(selectedTicket.getFirst_name()+" "+selectedTicket.getLast_name());
		ticketVO.setRaisedBy(selectedTicket.getCreated_by());
		ticketVO.setCreatedOn(ApplicationUtil.makeDateStringFromSQLDate(selectedTicket.getCreated_on()));
		ticketVO.setStatusId(selectedTicket.getStatus_id());
		ticketVO.setStatusDescription(selectedTicket.getDescription());
		ticketVO.setStatus(selectedTicket.getStatus());
		ticketVO.setSlaPercent(getSLAPercent(ticketVO));
		List<TicketCommentVO> ticketComments = getTicketComments(ticketVO.getTicketId(), loginUser);
		List<EscalationLevelVO> escalationLevelVOs = getTicketEscalationList(selectedTicket.getAssigned_to(), loginUser);
		ticketVO.setTicketComments(ticketComments);
		ticketVO.setEscalationLevelList(escalationLevelVOs);
		return ticketVO;
	}
	

	private double getSLAPercent(TicketVO customerTicket) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		double slaPercent=0.0;
		try{
		Date slaDueDate = simpleDateFormat.parse(customerTicket.getSla());
		Date creationTime = simpleDateFormat.parse(customerTicket.getCreatedOn());
		
		if(customerTicket.getStatusId().intValue() != 15){
			Date currentDateTime = new Date();
			long numeratorDiff = currentDateTime.getTime() - creationTime.getTime(); 
			long denominatorDiff = slaDueDate.getTime() - creationTime.getTime();
		
			
			double numValue = TimeUnit.MINUTES.convert(numeratorDiff, TimeUnit.MILLISECONDS);
			double deNumValue = TimeUnit.MINUTES.convert(denominatorDiff, TimeUnit.MILLISECONDS);
			if (deNumValue != 0){
				slaPercent = Math.round((numValue / deNumValue) * 100);
			}	
			return slaPercent;
		}else{
			if(customerTicket.getServiceRestorationTime()!=null){
				Date restoredTime = simpleDateFormat.parse(customerTicket.getServiceRestorationTime());
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
		}catch(ParseException e){
			e.printStackTrace();
		}
		return 0;
	}

	
	
	@Override
	public List<TicketAttachment> findByTicketId(Long ticketId, LoginUser loginUser) throws Exception {
		List<TicketAttachment> selectedTicketAttachments = getIncidentDAO(loginUser.getDbName()).getTicketAttachments(ticketId);
		return selectedTicketAttachments==null?Collections.emptyList():selectedTicketAttachments;
	}
	
	@Override
	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser user) throws Exception {
			TicketComment ticketComment = new TicketComment();
			ticketComment.setCreatedBy(user.getUsername());
			ticketComment.setCustTicketNumber(ticketCommentVO.getTicketNumber());
			ticketComment.setTicketId(ticketCommentVO.getTicketId());
			ticketComment.setComment(ticketCommentVO.getComment());
			ticketComment = getIncidentDAO(user.getDbName()).saveTicketComment(ticketComment, user);
			
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
	public List<TicketCommentVO> getTicketComments(Long ticketId, LoginUser user) {
		List<TicketCommentVO> commentListVO = getIncidentDAO(user.getDbName()).getTicketComments(ticketId);
		return commentListVO == null?Collections.EMPTY_LIST:commentListVO;
	}

	@Override
	public List<TicketHistoryVO> getTicketHistory(String ticketNumber, LoginUser user) throws Exception {
		List<TicketHistoryVO> ticketHistoryList = getIncidentDAO(user.getDbName()).getTicketHistory(ticketNumber);
		return ticketHistoryList == null?Collections.EMPTY_LIST:ticketHistoryList;
	}
	
	@Override
	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, LoginUser user) throws Exception {
		LOGGER.info("Inside TicketServiceImpl - saveLinkedTicket");
		CustomerSPLinkedTicketVO  customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
		customerSPLinkedTicketVO.setCustTicketId(String.valueOf(custTicket));
		customerSPLinkedTicketVO.setCustTicketNumber(custTicketNumber);
		customerSPLinkedTicketVO.setSpLinkedTicket(linkedTicket);
		customerSPLinkedTicketVO.setClosedFlag("OPEN");
		CustomerSPLinkedTicketVO savedLinkedTicket = getIncidentDAO(user.getDbName()).saveLinkedTicket(customerSPLinkedTicketVO, user);
		LOGGER.info("Exit TicketServiceImpl - saveLinkedTicket");
		return savedLinkedTicket;
	}

	@Override
	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicketId, LoginUser loginUser) throws Exception {
		List<CustomerSPLinkedTicketVO> customerSPLinkedTickets = getIncidentDAO(loginUser.getDbName()).findByCustTicketIdAndDelFlag(custTicketId);
		return customerSPLinkedTickets== null?Collections.EMPTY_LIST:customerSPLinkedTickets;
	}
	
	@Override
	public int changeLinkedTicketStatus(Long linkedTicket, LoginUser loginUser) throws Exception {
		int linkedStatus =  getIncidentDAO(loginUser.getDbName()).changeLinkedTicketStatus(linkedTicket, loginUser);
		return linkedStatus;
	}

	@Override
	public int deleteLinkedTicket(Long linkedTicket, LoginUser loginUser) throws Exception {
		int linkedStatus =  getIncidentDAO(loginUser.getDbName()).deleteLinkedTicket(linkedTicket, loginUser);
		return linkedStatus;
	}
	
	@Override
	public List<TicketVO> getRelatedTickets(Long ticketId, Long siteId, LoginUser loginUser) throws Exception {
		List<TicketVO> customerTicketList = getIncidentDAO(loginUser.getDbName()).findRelatedTickets(ticketId, siteId);
		return customerTicketList == null?Collections.EMPTY_LIST:customerTicketList;
	}

	private List<EscalationLevelVO> getTicketEscalationList(Long spAssignedTo, LoginUser loginUser) {
		List<EscalationLevelVO> escalationLevels =	getIncidentDAO(loginUser.getDbName()).getSPEscalation(spAssignedTo,loginUser);		
		return escalationLevels == null?Collections.EMPTY_LIST:escalationLevels;
	}
	

	@Override
	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) {
		LOGGER.info("Inside TicketServiceImpl - saveTicketEscalations");
		TicketEscalationVO savedTicketEscVO =  getIncidentDAO(user.getDbName()).saveTicketEscalations(ticketEscalationLevel,user);
		
		LOGGER.info("Exit TicketServiceImpl - saveTicketEscalations");
		return savedTicketEscVO;
	}
	
	@Override
	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId, LoginUser user ) {
		List<TicketEscalationVO> escalatedTickets =  getIncidentDAO(user.getDbName()).getAllEscalations(ticketId);
		return escalatedTickets == null?Collections.EMPTY_LIST:escalatedTickets;
	}


	@Override
	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId, LoginUser user) {
		TicketEscalationVO savedTicketEscVO = getIncidentDAO(user.getDbName()).findByTicketIdAndEscLevelId(ticketId, escId);
		savedTicketEscVO.setEscalationStatus("Escalated");
		return savedTicketEscVO;
	}
	@Override
	public List<Financials> findFinanceByTicketId(Long ticketId, LoginUser user) {
		try {
			return getIncidentDAO(user.getDbName()).getTicketFinancials(ticketId,user);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
	public List<Financials> save(List<Financials> finList, LoginUser user) {
		List<Financials> financials=new ArrayList<Financials>();
		try {
			for (Financials fin : finList) {
				if (fin.getId() != null) {
					Financials savedFinancial = getIncidentDAO(user.getDbName()).getTicketFinanceById(fin.getId(),user);
					org.springframework.beans.BeanUtils.copyProperties(fin, savedFinancial);
					savedFinancial.setModifiedOn(new Date());
					savedFinancial.setModifiedBy(user.getUsername());
					Financials editedFinance = getIncidentDAO(user.getDbName()).updateFinance(savedFinancial, user);
					if(editedFinance!=null){
						financials.add(editedFinance);
					}
				} else {
					fin.setCreatedBy(user.getUsername());
					Financials savedFinance= getIncidentDAO(user.getDbName()).saveFinance(fin, user);
					if(savedFinance!=null){
						financials.add(savedFinance);
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("ERROR while updating Financials ", e);
		}
		return financials == null ? Collections.EMPTY_LIST:financials;
	}

	@Override
	@Transactional
	public List<Financials> saveAndUpdate(List<FinancialVO> financialVOList, LoginUser user) {
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
			List<Financials> savedFinanceList= save(financials, user);
			return savedFinanceList == null ? Collections.EMPTY_LIST:savedFinanceList;
	}

	@Override
	public List<Financials> saveFinancials(List<FinUpdReqBodyVO> finVOList, LoginUser user) {
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
		List<Financials> savedFinanceList = save(financialList, user);
		return savedFinanceList == null ? Collections.EMPTY_LIST:savedFinanceList;
	}

}
