package com.pms.app.dao;

import java.util.List;

import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderUserRoleVO;
import com.pms.app.view.vo.UserVO;

public interface ServiceProviderDAO {

	public String insertServiceProviderUser(SPUserVo sPUserVo) throws Exception;

	public String updatetServiceProviderUser(SPUserVo sPUserVo) throws Exception;

	public SPUserVo getServiceProviderUserByEmail(SPUserVo sPUserVo) throws Exception;

	public String createServiceProviderUserRole(SPUserVo sPUserVo, LoginUser loginUser) throws Exception;

	public int createServiceProviderUserAccess(List<CustomerVO> customerList, LoginUser loginUser) throws Exception;

	public String updateServiceProviderUserRole(SPUserVo sPUserVo, LoginUser loginUser) throws Exception;

	public String updateServiceProviderUserAccess(SPUserVo useraccessvo, LoginUser loginUser) throws Exception;
	
	public int[] updateServiceProviderUserAccess(List<CustomerVO> customerList,Long selectedSPUserId, LoginUser loginUser) throws Exception;

	//public List<SPUserVo> getAllUsers() throws Exception;
	
	public List<UserVO> getAllSPUsers(Long companyId) throws Exception;

	public ServiceProviderUserRoleVO getUserRoleByUserID(String userId) throws Exception;

	public List<CustomerVO> getCustomersBySPID(String userId, String spCode) throws Exception;

	public List<CustomerVO> getCustomersBySelectedSPUser(String spId) throws Exception;


}
