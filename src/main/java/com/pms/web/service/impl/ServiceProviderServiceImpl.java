package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.app.dao.impl.DistrictDAO;
import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.dao.impl.TenantsDAO;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SLADetailsVO;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.AssetService;
import com.pms.web.service.ServiceProviderService;
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
	@Transactional
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
		List<UserVO> userList =  getSPUserDAO(loginUser.getDbName()).getAllSPUsers(companyId);
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
		serviceProviderDAOImpl.updateServiceProviderUserAccess(customerList, selectedSPUserId, user);

		return "success";
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
	@Transactional
	public ServiceProviderVO saveServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser loginUser) {
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
			savedSP = spDAO.saveServiceProvider(serviceProviderVO, loginUser);
			if(savedSP.getServiceProviderId()!=null){
				int escalationRecored = 	spDAO.saveEscalalationLevels( savedSP, serviceProviderVO.getEscalationLevelList(), loginUser);
				int slaRecored =	spDAO.saveSLADetails( savedSP, serviceProviderVO.getSlaListVOList(), loginUser);
				if(escalationRecored > 0  && slaRecored > 0){
					savedSP.setStatus(200);
					savedSP.setOption("CREATED");
					savedSP.setAccessKey(rawPassword);
					boolean isExtSPCreated = insertExternalSPTenants(serviceProviderVO, loginUser.getDbName());
					if(isExtSPCreated){
						savedSP.setTenantMapped(true);
					}
				}
			}
		}else{
			int updated = spDAO.updateServiceProvider(serviceProviderVO, loginUser);
			if(updated > 0 ){
				int updatedEscalationRecords = 	spDAO.updateEscalalationLevels( serviceProviderVO, serviceProviderVO.getEscalationLevelList(), loginUser);
				int slaUpdatedRecored =	spDAO.updateSLADetails( serviceProviderVO, serviceProviderVO.getSlaListVOList(), loginUser);
				if(updatedEscalationRecords > 0  && slaUpdatedRecored > 0){
					savedSP.setStatus(200);
				}
			}
		}
		return savedSP;
	}
	private boolean insertExternalSPTenants(ServiceProviderVO serviceProviderVO, String dbName) {
		return getTenantsDAO("tenants").insertExtSPDetails(serviceProviderVO,dbName);
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
	public List<ServiceProviderVO> findSPList(LoginUser user) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findSPList");
		final SPUserDAO spDAO = getSPUserDAO(user.getDbName());
		List<ServiceProviderVO> serviceProviderList = spDAO.findSPList();
		logger.info("Exit ServiceProviderServiceImpl -- findSPList");
		return serviceProviderList == null ? Collections.emptyList() : serviceProviderList;
	}
	@Override
	public ServiceProviderVO findServiceProviderInfo(Long spId, LoginUser loginUser) throws Exception {
		ServiceProviderVO serviceProvider = getSPUserDAO(loginUser.getDbName()).findSPDetails(spId);
		List<EscalationLevelVO> escalationLevelList =getSPUserDAO(loginUser.getDbName()).getEscalationDetails(spId);
		List<SLADetailsVO> slaListVOList = getSPUserDAO(loginUser.getDbName()).getSLADetails(spId);
		serviceProvider.setEscalationLevelList(escalationLevelList);
		serviceProvider.setSlaListVOList(slaListVOList);
		return serviceProvider;
	}
	

}
