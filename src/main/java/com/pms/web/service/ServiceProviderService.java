package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;

public interface ServiceProviderService {

	/*public ServiceProviderVO saveServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser loginUser) throws Exception;

	public ServiceProviderVO findServiceProvider(Long serviceProviderId) throws Exception;*/
	
	public List<ServiceProviderVO> findSPList(LoginUser user, final String spType) throws Exception;

	public List<ServiceProviderVO> findAllServiceProvider(LoginUser user) throws Exception;

	public List<ServiceProviderVO> findServiceProviderByCustomer(Long customerId) throws Exception;

	public List<UserVO> findALLSPUsers(Long companyId, LoginUser loginUser) throws Exception;

	/*public boolean deleteServiceProvider() throws Exception;

	public SPLoginVO validateServiceProvider(String email, String accessCode) throws Exception;*/
	
	public String createServiceProviderUser(SPUserVo sPUserVo, LoginUser user) throws Exception;

	public String updateServiceProviderCustomers(List<CustomerVO> customerVOList, Long selectedSPUserId, LoginUser user) throws Exception;
	
	public List<UserVO> getAllUsersWithRole(LoginUser user) throws Exception;

	public List<CustomerVO> getCustomerForSelectedUser(LoginUser loginUser, Long spuserid) throws Exception;

	List<CustomerVO> getAllCustomers(LoginUser loginUser) throws Exception;

	public ServiceProviderVO saveServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser loginUser);

	public List<Region> findAllRegions(LoginUser loginUser) throws Exception;

	public List<Country> findCountryByRegion(Long regionId, LoginUser loginUser) throws Exception;

	public ServiceProviderVO findServiceProviderInfo(Long spId, LoginUser loginUser) throws Exception;
	
	public List<CustomerVO> getCustomerCountryForloggedInUser(LoginUser loginUser, Long spuserid) throws Exception;
	
	public List<TicketVO> getCustomerTickets(String custcode,String custDBName, LoginUser loginUser) throws Exception;

	public ServiceProviderVO resetPassword(Long spId, LoginUser loginUser) throws Exception;

	public List<UserVO> findALLActiveSPUsers(String customerCode, LoginUser loginUser) throws Exception;

}