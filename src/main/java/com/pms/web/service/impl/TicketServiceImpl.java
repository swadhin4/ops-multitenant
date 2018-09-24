package com.pms.web.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.pms.app.dao.impl.IncidentDAO;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketCategory;
import com.pms.web.service.TicketService;

@Service("ticketService")
public class TicketServiceImpl implements TicketService {
	

	private final static Logger LOGGER = LoggerFactory.getLogger(TicketServiceImpl.class);
	

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
	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser loginUser) throws Exception {
		Long newIncidentNumber = null;
		Long lastIncidentNumber = 900l;
		newIncidentNumber = lastIncidentNumber + 1;
		String ticketNumber = "IN" +  String.format("%08d", newIncidentNumber);
		LOGGER.info("Ticket Number Generated : " + ticketNumber);
		customerTicket.setTicketNumber(ticketNumber);
		
		return getIncidentDAO(loginUser.getDbName()).saveIncident(customerTicket, loginUser);
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
