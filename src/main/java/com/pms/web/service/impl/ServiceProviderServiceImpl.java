package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.pms.app.constants.AppConstants;
import com.pms.app.constants.UserType;
import com.pms.app.dao.impl.DistrictDAO;
import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.dao.impl.TenantsDAO;
import com.pms.app.exception.PMSDBException;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SLADetailsVO;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.AssetService;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.util.QuickPasswordEncodingGenerator;
import com.pms.web.util.RandomUtils;

@Service("serviceProviderService")
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);
	
	@Autowired
	private AssetService assetService;
	
	private SPUserDAO getSPUserDAO(String dbName) {
		return new SPUserDAO(dbName);
	}
	private ServiceProviderDAOImpl getServiceProviderDAOImpl(String dbName) {
		return new ServiceProviderDAOImpl(dbName);
	}
	
	private DistrictDAO getDistrictDAO(String dbName){
		return new DistrictDAO(dbName);
		
	}
	private TenantsDAO getTenantsDAO(String dbName){
		return new TenantsDAO(dbName);
		
	}
	
	@Override
	public List<ServiceProviderVO> findAllServiceProvider(LoginUser user) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		logger.info("Getting Service Provider List for logged in user : " + user.getFirstName() + " " + user.getLastName());
		List<ServiceProviderVO> serviceProviderList = assetService.findSPByCompanyIdIn(user);
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderList == null ? Collections.emptyList() : serviceProviderList;
	}

	@Override
	public List<ServiceProviderVO> findServiceProviderByCustomer(Long customerId) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOList = new ArrayList<ServiceProviderVO>();
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderVOList == null ? Collections.emptyList() : serviceProviderVOList;
	}

	@Override
	public List<UserVO> findALLSPUsers(Long companyId, LoginUser loginUser) throws Exception {
		List<UserVO> userList =  getSPUserDAO(loginUser.getSpDbName()).getAllSPUsers(companyId);
		return userList==null?Collections.emptyList():userList;
	}

	
	@Override
	public String createServiceProviderUser(SPUserVo sPUserVo, LoginUser user) throws Exception {
		// TODO Auto-generated method stub
		ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(user.getDbName());
		serviceProviderDAOImpl.insertServiceProviderUser(sPUserVo);
		SPUserVo spuserVO = serviceProviderDAOImpl.getServiceProviderUserByEmail(sPUserVo);
		spuserVO.setRoleId(sPUserVo.getRoleId());
		serviceProviderDAOImpl.createServiceProviderUserRole(spuserVO, user);
		spuserVO.setCustomers(sPUserVo.getCustomers());
		//serviceProviderDAOImpl.createServiceProviderUserAccess(spuserVO, user);

		return "success";
	}

	@Override
	public String updateServiceProviderCustomers(List<CustomerVO> customerList, Long selectedSPUserId, LoginUser user) throws Exception {
		ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(user.getDbName());
		//serviceProviderDAOImpl.updatetServiceProviderUser(sPUserVo);
	//	serviceProviderDAOImpl.updateServiceProviderUserRole(sPUserVo, user);
		int affectedRows = serviceProviderDAOImpl.updateServiceProviderUserAccess(customerList, selectedSPUserId, user);
		if(affectedRows>0){
		return "success";
		}else{
			return "failure";
		}
	}

	@Override
	public List<UserVO> getAllUsersWithRole(LoginUser user) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(user.getDbName());
		List<UserVO> spusers = serviceProviderDAOImpl.getAllSPUsers(user.getCompany().getCompanyId());
		for (UserVO sPUserVo : spusers) {
			sPUserVo.setUserRole(serviceProviderDAOImpl.getUserRoleByUserID(String.valueOf(sPUserVo.getUserId())));
			//sPUserVo.setCustomers(serviceProviderDAOImpl.getCustomersByUserID(String.valueOf(sPUserVo.getUserId())));
		}
		return spusers;
	}
	@Override
	public List<CustomerVO> getCustomerForSelectedUser(LoginUser loginUser, Long spuserid) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(loginUser.getDbName());
		return serviceProviderDAOImpl.getCustomersBySelectedSPUser(String.valueOf(spuserid));
	}
	
	@Override
	public List<CustomerVO> getAllCustomers(LoginUser loginUser) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(loginUser.getDbName());
		return serviceProviderDAOImpl.getCustomersBySPID(String.valueOf(loginUser.getUserId()), loginUser.getCompany().getCompanyCode());
	}
	@Override
	public ServiceProviderVO saveServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser loginUser) throws PMSDBException {
		final SPUserDAO spDAO = getSPUserDAO(loginUser.getDbName());
		ServiceProviderVO savedSP = new ServiceProviderVO();
		if(serviceProviderVO.getServiceProviderId()==null){
			Long lastSPId = getSPUserDAO(loginUser.getDbName()).getLastSPCreated();
			Long newSPId =null;
			if(lastSPId == 0){
				newSPId = 1l;
			}else{
				newSPId = lastSPId + 1;
			}
			newSPId = lastSPId + 1;
			serviceProviderVO.setSpUserName("SP-00"+loginUser.getCompany().getCompanyId()+newSPId);
			final String rawPassword = RandomUtils.randomAlphanumeric(6);
			serviceProviderVO.setAccessKey(rawPassword);
			savedSP = spDAO.getUniqueServiceProvider(serviceProviderVO, loginUser);
			if(savedSP.getServiceProviderId()==null){
				savedSP = spDAO.saveServiceProvider(serviceProviderVO, loginUser);
				int escalationRecored = 	spDAO.saveEscalalationLevels( savedSP, serviceProviderVO.getEscalationLevelList(), loginUser, AppConstants.INSERT_SP_ESCALATIONS_QUERY);
				int slaRecored =	spDAO.saveSLADetails( savedSP, serviceProviderVO.getSlaListVOList(), loginUser, AppConstants.INSERT_SP_SLA_QUERY);
				if(escalationRecored > 0  && slaRecored > 0){
					savedSP.setStatus(200);
					savedSP.setOption("CREATED");
					savedSP.setAccessKey(rawPassword);
					boolean isExtSPCreated = insertExternalSPTenants(serviceProviderVO, loginUser.getDbName());
					if(isExtSPCreated){
						savedSP.setTenantMapped(true);
					}
				}
			}else{
				throw new PMSDBException("202", "Duplicate company email exists");
			}
		}else{
			if(StringUtils.isNotEmpty(serviceProviderVO.getSpDbName()) && StringUtils.isNotEmpty(serviceProviderVO.getAccessGranted())){
				logger.info("Updating Registered Service Provider Details");
				int updated = spDAO.updateRegisteredServiceProvider(serviceProviderVO, loginUser);
				if(updated > 0 ){
					logger.info("Updating Registered Service provider Escalation and SLA details");
					int updatedEscalationRecords = 	spDAO.updateEscalalationLevels( serviceProviderVO, serviceProviderVO.getEscalationLevelList(), loginUser, AppConstants.UPDATE_RSP_ESCALATIONS_QUERY);
					int slaUpdatedRecored =	spDAO.updateSLADetails( serviceProviderVO, serviceProviderVO.getSlaListVOList(), loginUser, AppConstants.UPDATE_RSP_SLA_QUERY);
					if(updatedEscalationRecords > 0  && slaUpdatedRecored > 0){
						savedSP.setStatus(200);
						savedSP.setOption("UPDATED");
					}
				}
			}else {
				logger.info("Updating External Service Provider Details");
				int updated = spDAO.updateServiceProvider(serviceProviderVO, loginUser);
				if(updated > 0 ){
					logger.info("Updating Externa Service provider Escalation and SLA details");
					int updatedEscalationRecords = 	spDAO.updateEscalalationLevels( serviceProviderVO, serviceProviderVO.getEscalationLevelList(), loginUser, AppConstants.UPDATE_SP_ESCALATIONS_QUERY);
					int slaUpdatedRecored =	spDAO.updateSLADetails( serviceProviderVO, serviceProviderVO.getSlaListVOList(), loginUser, AppConstants.UPDATE_SP_SLA_QUERY);
					if(updatedEscalationRecords > 0  && slaUpdatedRecored > 0){
						savedSP.setStatus(200);
						savedSP.setOption("UPDATED");
					}
				}
			}
		}
		return savedSP;
	}
	private boolean insertExternalSPTenants(ServiceProviderVO serviceProviderVO, String dbName) {
		TenantsDAO tenantDAO = getTenantsDAO("tenants");
		return tenantDAO.insertExtSPDetails(serviceProviderVO,dbName);
	}
	@Override
	public List<Region> findAllRegions(LoginUser loginUser) throws Exception {
		final DistrictDAO districtDAO = getDistrictDAO(loginUser.getDbName());
		List<Region> regionList = districtDAO.findRegionList();
		return regionList == null ? Collections.emptyList():regionList;
	}
	@Override
	public List<Country> findCountryByRegion(Long regionId, LoginUser loginUser) throws Exception {
		final DistrictDAO districtDAO = getDistrictDAO(loginUser.getDbName());
		List<Country> countryList = districtDAO.findCountryByRegion(regionId);
		return countryList == null ? Collections.emptyList():countryList;
	}
	@Override
	public List<ServiceProviderVO> findSPList(LoginUser user, final String spType) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findSPList");
		final SPUserDAO spDAO = getSPUserDAO(user.getDbName());
		List<ServiceProviderVO> selectedOptionSPList = spType.equalsIgnoreCase("EXT")?spDAO.findSPList():spDAO.findRSPList();
		logger.info("Exit ServiceProviderServiceImpl -- findSPList");
		return selectedOptionSPList == null ? Collections.emptyList() : selectedOptionSPList;
	}
	
	@Override
	public List<ServiceProviderVO> findAllSPList(LoginUser user) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findSPList");
		final SPUserDAO spDAO = getSPUserDAO(user.getDbName());
		List<ServiceProviderVO> allSPList = new ArrayList<ServiceProviderVO>();
		List<ServiceProviderVO> externalSPList = spDAO.findSPList();
		List<ServiceProviderVO> registeredSPList = spDAO.findRSPList();
		for(ServiceProviderVO extSP:externalSPList){
			allSPList.add(extSP);
		}
		for(ServiceProviderVO rspSP:registeredSPList){
			allSPList.add(rspSP);
		}
		
		logger.info("Exit ServiceProviderServiceImpl -- findSPList");
		return allSPList == null ? Collections.emptyList() : allSPList;
	}
	
	@Override
	public ServiceProviderVO findServiceProviderInfo(Long spId, LoginUser loginUser, String spViewType) throws Exception {
		
		ServiceProviderVO serviceProvider = null;
		if(!StringUtils.isEmpty(spViewType) && spViewType.equalsIgnoreCase("EXT")){
			 serviceProvider = getSPUserDAO(loginUser.getDbName()).findSPDetails(spId, spViewType);
			List<EscalationLevelVO> escalationLevelList =getSPUserDAO(loginUser.getDbName()).getEscalationDetails(spId, spViewType);
			List<SLADetailsVO> slaListVOList = getSPUserDAO(loginUser.getDbName()).getSLADetails(spId, spViewType);
			serviceProvider.setEscalationLevelList(escalationLevelList);
			serviceProvider.setSlaListVOList(slaListVOList);
		}
		else if(!StringUtils.isEmpty(spViewType) && spViewType.equalsIgnoreCase("RSP")){
			serviceProvider = getSPUserDAO(loginUser.getDbName()).findSPDetails(spId, spViewType);
			List<EscalationLevelVO> escalationLevelList =getSPUserDAO(loginUser.getDbName()).getEscalationDetails(spId, spViewType);
			List<SLADetailsVO> slaListVOList = getSPUserDAO(loginUser.getDbName()).getSLADetails(spId, spViewType);
			serviceProvider.setEscalationLevelList(escalationLevelList);
			serviceProvider.setSlaListVOList(slaListVOList);
		}
		return serviceProvider;
	}
	
	@Override
	public List<CustomerVO> getCustomerCountryForloggedInUser(LoginUser loginUser, Long spuserid) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(loginUser.getSpDbName());
		return serviceProviderDAOImpl.getCustomerCountryForloggedInUser(spuserid, loginUser.getCompany().getCompanyCode());
	}


	public List<TicketVO> getCustomerTickets(String spcode, String custDBName, LoginUser user) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(custDBName);
		List<TicketVO> tickets = serviceProviderDAOImpl.getCustomerTicketsBySPcode(spcode, user.getUserId());
		return tickets;
	}
	@Override
	public ServiceProviderVO resetPassword(Long spId, LoginUser user ) throws Exception {
		final ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(user.getDbName());
		boolean isUpdated=false;
		ServiceProviderVO  serviceProviderVO = null;
		synchronized (serviceProviderDAOImpl) {
			serviceProviderVO  = serviceProviderDAOImpl.findSPPasswordDetails(spId);
			logger.info("Resetting Password for "+ serviceProviderVO.getName());
			String defaultPassword = "mkp006";
			int updatedRows = serviceProviderDAOImpl.resetPassword(QuickPasswordEncodingGenerator.encodePassword(defaultPassword), spId);
			if(updatedRows>0){
				isUpdated=true;
				logger.info("Password updated successfully");
				serviceProviderVO.setAccessKey(defaultPassword);
				serviceProviderVO.setOption("PWDUPDATED");
			}
		}
		return serviceProviderVO;
	}
	@Override
	public List<UserVO> findALLActiveSPUsers(String customerCode, LoginUser loginUser) throws Exception {
		List<UserVO> userList =null;
		if(customerCode.equalsIgnoreCase("SPUSERS") && loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			userList =  getSPUserDAO(loginUser.getDbName()).getSPAgentUsers(loginUser);
		}else{
			userList =  getSPUserDAO(loginUser.getSpDbName()).getAllActiveUsers(customerCode, loginUser.getUserRoles().get(0).getRole().getRoleId());
		}
		return userList==null?Collections.emptyList():userList;
	}
	@Override
	public int grantOrRevokeAccess(Long rspId, LoginUser loginUser, String grantOrRevokeVal) throws Exception {
		logger.info("Getting RSP access Details from Customer DB");
		ServiceProviderVO registeredSPVO = getSPUserDAO(loginUser.getDbName()).findSPDetails(rspId, "RSP");
		int isUpdated = getSPUserDAO(loginUser.getDbName()).setAccessValue(rspId, grantOrRevokeVal);
		logger.info("Registered SP access_granted value changed to {}, by  {}" + grantOrRevokeVal, loginUser.getUsername());
		int isQueryExectued = 0;
		if(isUpdated>0){
			if(grantOrRevokeVal.equalsIgnoreCase("N")){
				registeredSPVO.setAccessGranted(grantOrRevokeVal);
				logger.info("Getting RSP mapping details from RSP Tenant DB to validate RSP by Customer Code and customer DB Name");
				ServiceProviderVO rspCustomerMappedVO = getSPUserDAO(registeredSPVO.getSpDbName()).findRSPCustomerDetailsByCode(loginUser.getCompany().getCompanyCode(), loginUser.getDbName());
				if(rspCustomerMappedVO!=null){
					logger.info("Record Found. Deleting the Mapping of Customer and RSP from RSP Tenant DB");
					int spUserAccessDel= getSPUserDAO(registeredSPVO.getSpDbName()).deleteRspCustomerAccessRecord(rspCustomerMappedVO);
					int deleted = getSPUserDAO(registeredSPVO.getSpDbName()).deleteRspCustomerRecord(rspCustomerMappedVO);
					if(deleted > 0){
						isQueryExectued = 200;
					}
				}
			}
			else if(grantOrRevokeVal.equalsIgnoreCase("Y")){
				registeredSPVO.setAccessGranted(grantOrRevokeVal);
				logger.info("Getting RSP company details by RSP CODE from RSP Tenant DB to validate RSP SPID to Map customer details");
				ServiceProviderVO rspOpsTenant = getSPUserDAO(registeredSPVO.getSpDbName()).findRSPDetailsByCode(registeredSPVO.getCode());
				if(rspOpsTenant!=null){
					logger.info("Inserting new mapping record for Customer and RSP in RSP Tenant DB");
					ServiceProviderVO updatedRSPMap = getSPUserDAO(registeredSPVO.getSpDbName()).insertRspCustomerRecord(loginUser, rspOpsTenant.getRspOpsTenantSpId());
					if(updatedRSPMap.getRspCustId()!=null){
						isQueryExectued = 200;
					}
				}
			}
		}
		return isQueryExectued;
	}
}
