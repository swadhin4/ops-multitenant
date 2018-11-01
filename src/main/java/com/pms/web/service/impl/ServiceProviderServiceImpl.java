package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
import com.pms.web.service.AssetService;
import com.pms.web.service.ServiceProviderService;

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
	
	
	@Override
	@Transactional
	public List<ServiceProviderVO> findAllServiceProvider(LoginUser user) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		logger.info("Getting Service Provider List for logged in user : " + user.getFirstName() + " " + user.getLastName());
		List<ServiceProviderVO> serviceProviderList = assetService.findSPByCompanyIdIn(user);
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderList == null ? Collections.EMPTY_LIST : serviceProviderList;
	}

	@Override
	public List<ServiceProviderVO> findServiceProviderByCustomer(Long customerId) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOList = new ArrayList<ServiceProviderVO>();
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderVOList == null ? Collections.EMPTY_LIST : serviceProviderVOList;
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
		serviceProviderDAOImpl.createServiceProviderUserAccess(spuserVO, user);

		return "success";
	}

	@Override
	public String updateServiceProviderUser(SPUserVo sPUserVo, LoginUser user) throws Exception {
		// TODO Auto-generated method stub
		ServiceProviderDAOImpl serviceProviderDAOImpl = getServiceProviderDAOImpl(user.getDbName());
		serviceProviderDAOImpl.updatetServiceProviderUser(sPUserVo);
		serviceProviderDAOImpl.updateServiceProviderUserRole(sPUserVo, user);
		serviceProviderDAOImpl.updateServiceProviderUserAccess(sPUserVo, user);

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
	

}
