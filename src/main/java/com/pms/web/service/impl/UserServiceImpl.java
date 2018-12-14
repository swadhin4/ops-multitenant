package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.dao.impl.TenantsDAO;
import com.pms.app.dao.impl.UserDAO;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.UserSiteAccessVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.Tenant;
import com.pms.jpa.entities.User;
import com.pms.jpa.entities.UserModel;
import com.pms.web.service.TenantService;
import com.pms.web.service.UserService;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.QuickPasswordEncodingGenerator;
import com.pms.web.util.RestResponse;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl() {
		super();
	}
	
	private UserDAO getUserDAO(String dbName) {
		return new UserDAO(dbName);
	}
	private SPUserDAO getSPUserDAO(String dbName) {
		return new SPUserDAO(dbName);
	}
	private ServiceProviderDAOImpl getServiceProviderDAOImpl(String dbName) {
		return new ServiceProviderDAOImpl(dbName);
	}
	private TenantsDAO getTenantsDAO(String dbName){
		return new TenantsDAO(dbName);
		
	}
	@Autowired
	private TenantService tenantService;
	
	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> findALL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Role> listRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User retrieve(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User findByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User getCurrentLoggedinUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserVO updateRoles(UserVO userVO, LoginUser user) {
		UserVO savedUserVO = null;
		if(user.getUserType().equalsIgnoreCase("USER")){
			savedUserVO = getUserDAO(user.getDbName()).updateRole(userVO);
		}
		else if(user.getUserType().equalsIgnoreCase("SP")){
			savedUserVO = getUserDAO(user.getDbName()).updateSPRole(userVO);
		}
		return savedUserVO;
	}

	@Override
	@Transactional
	public UserVO saveUser(AppUserVO appUserVO, LoginUser user) throws Exception {
		String generatedRawPassword = "mkp006";//RandomUtils.randomAlphanumeric(8);
		String encryptedPassword = QuickPasswordEncodingGenerator.encodePassword(generatedRawPassword);
		appUserVO.setGeneratedPassword(encryptedPassword);
		UserVO savedUserVO = null;
		if(user.getUserType().equalsIgnoreCase("USER")){
			savedUserVO	= getUserDAO(user.getDbName()).saveNewUser(appUserVO, user);
			if(savedUserVO.getUserId()!=null){
				savedUserVO.setRoleId(appUserVO.getRole().getRoleId());
				Long roleId = getUserDAO(user.getDbName()).saveUserRole(savedUserVO);
				if(roleId>0){
					LOGGER.info("Customer  {} created with role {}", appUserVO.getFirstName(), roleId);
					savedUserVO.setStatus(200);
					boolean isCustomerCreated = getTenantsDAO("tenants").insertCustomerDetails(user.getTenantId(),user.getCompany().getCompanyCode(), savedUserVO.getEmailId());
					if(isCustomerCreated){
						savedUserVO.setStatus(200);
						LOGGER.info("Customer {} tenant mapping created ", savedUserVO.getFirstName());
					}else{
						LOGGER.info("Unable to map customer {} ", savedUserVO.getFirstName(),"Login Denied" );
						savedUserVO.setStatus(204);
					}
				}else{
					LOGGER.info("User not created with role");
				}
			}
		}else if(user.getUserType().equalsIgnoreCase("SP")){
			savedUserVO	= getSPUserDAO(user.getDbName()).saveNewSPUser(appUserVO, user);
			if(savedUserVO.getUserId()!=null){
				savedUserVO.setRoleId(appUserVO.getRole().getRoleId());
				Long roleId = getSPUserDAO(user.getDbName()).saveUserRole(savedUserVO);
				if(roleId>0){
					LOGGER.info("SP  {} created with role {}", savedUserVO.getEmailId(), roleId);
					savedUserVO.setStatus(200);
					boolean isSPUserCreated = getTenantsDAO("tenants").insertRegisteredSPDetails(user.getTenantId(), savedUserVO.getEmailId());
					if(isSPUserCreated){
						savedUserVO.setStatus(200);
						LOGGER.info("SP {} tenant mapping created ", savedUserVO.getEmailId());
						List<CustomerVO> customerList = appUserVO.getCustomerList();
						if(!customerList.isEmpty()){
							Integer recordsMapped = getServiceProviderDAOImpl(user.getDbName()).createServiceProviderUserAccess(customerList, savedUserVO, user);
							if(recordsMapped > 0 ){
								savedUserVO.setStatus(200);
							}
						}
					}else{
						LOGGER.info("Unable to map SP User. SP User can not login");
						savedUserVO.setStatus(204);
					}
				}else{
					LOGGER.info("SP User not created with role");
				}
				
			}
		}
		return savedUserVO;
	}

	@Override
	@Transactional
	public List<UserVO> findALLUsers(Long companyId, LoginUser user) throws Exception {
		List<UserVO> userList =  getUserDAO(user.getDbName()).getAllUsers(companyId);
		return userList==null?Collections.emptyList():userList;
	}

	@Override
	public AuthorizedUserDetails getAuthorizedUser(Authentication springAuthentication) throws Exception {
		return null;
	}

	@Override
	public RestResponse changePassword(PasswordVO passwordVO, LoginUser user) {
		LOGGER.info("Inside UserServiceImpl ... changePassword");
		RestResponse response = new RestResponse();
		if(user.getUserId()!=null){
			UserModel loggedInUser = getUserDAO(user.getDbName()).getUserDetails(user.getUsername());
			final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String existingPassword = passwordVO.getOldPassword();
			String dbPassword       = loggedInUser.getPassword();

			if (passwordEncoder.matches(existingPassword, dbPassword)) {
				LOGGER.info("Old password match existing password.");
				String newEncodedPassword = QuickPasswordEncodingGenerator.encodePassword(passwordVO.getConfirmPassword());
				passwordVO.setNewPassword(newEncodedPassword);
				int updated = getUserDAO(user.getDbName()).changePassword(passwordVO, user.getUsername());
				if(updated>0){
					LOGGER.info("New password saved successfully.");
					response.setStatusCode(200);
				}else{
					response.setStatusCode(204);
					response.setMessage("Your new password could not be validated");
				}
			} else {
				response.setStatusCode(205);
				LOGGER.info("Old password did not match.");
				response.setMessage("Old password is not valid");

			}

		}
		LOGGER.info("Exit UserServiceImpl ... changePassword");
		return response;
	}

	@Override
	public RestResponse updateRole(AppUserVO appUserVO, LoginUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse updateStatus(AppUserVO appUserVO, LoginUser user) throws Exception {
		int updated=0;
		RestResponse response = new RestResponse();
		if(user.getUserType().equalsIgnoreCase("USER")){
			updated =   getUserDAO(user.getDbName()).updateUserStatus(appUserVO);
		}
		else if(user.getUserType().equalsIgnoreCase("SP")){
			updated =   getUserDAO(user.getDbName()).updateSPUserStatus(appUserVO);
		}
		
		if(updated==1 && Integer.parseInt(appUserVO.getIsEnabled())==0){
			response.setStatusCode(200);
			response.setCalculatedVal(updated);
			response.setMessage("User account disabled successfully");
		}else if(updated==1  && Integer.parseInt(appUserVO.getIsEnabled())==1){
			response.setStatusCode(200);
			response.setCalculatedVal(updated);
			response.setMessage("User account enabled successfully");
		}
		return response;
	}

	@Override
	public int checkUserAvailibility(String email) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RestResponse resetNewPassword(String email, String type, String newPassword) throws Exception{
		LOGGER.info("Inside UserServiceImpl ... resetNewPassword");
		RestResponse response = new RestResponse();
		try{
			Tenant tenant = tenantService.getTenantDB(email, type);
			UserModel user = getUserDAO(tenant.getDb_name()).getUserDetails(email);
			if(user.getUserId()!=null){
				LOGGER.info("Reseting new password for user :"+ user.getEmailId());
				String newEncodedPassword = QuickPasswordEncodingGenerator.encodePassword(newPassword);
				user.setPassword(newEncodedPassword);
				PasswordVO passwordVO = new PasswordVO();
				passwordVO.setNewPassword(newEncodedPassword);
				UserModel userModel = getUserDAO(tenant.getDb_name()).getUserDetails(email);
				int updated = getUserDAO(tenant.getDb_name()).changePassword(passwordVO, email);
				if(updated>0 && userModel.getPassword().equalsIgnoreCase(newEncodedPassword))
					LOGGER.info("New Password reset successfully.");
					response.setStatusCode(200);
				}else{
					response.setStatusCode(204);
					response.setMessage("Your new password could not be validated");
				}
		}catch(Exception e){
			response.setStatusCode(500);
			LOGGER.error("Exception while resetting new password", e);
		}

		LOGGER.info("Exit UserServiceImpl ... resetNewPassword");
		return response;
	}
	
	@Override
	public RestResponse resetForgotPassword(String email, String type, String newPassword) throws Exception{
		LOGGER.info("Inside UserServiceImpl ... resetForgotPassword");
		RestResponse response = new RestResponse();
		try{
			UserModel user =null;
			Tenant tenant =null;
			if(type.equalsIgnoreCase("1")){
			tenant = tenantService.getTenantDB(email, "USER");
			user = getUserDAO(tenant.getDb_name()).getUserDetails(email);
			}
			else if(type.equalsIgnoreCase("2")){
				 tenant = tenantService.getTenantDB(email, "SP");
				 user = getUserDAO(tenant.getDb_name()).getUserDetails(email);
				}
			if(user.getUserId()!=null){
				LOGGER.info("Reseting new password for user :"+ user.getEmailId());
				String newEncodedPassword = QuickPasswordEncodingGenerator.encodePassword(newPassword);
				int update = getUserDAO(tenant.getDb_name()).updatePassword(newEncodedPassword, email);
				if(update > 0 )
					LOGGER.info("New Password reset successfully.");
					response.setStatusCode(200);
					response.setMessage(newPassword);
				}else{
					response.setStatusCode(204);
					response.setMessage("Your new password could not be validated");
				}
		}catch(Exception e){
			response.setStatusCode(500);
			LOGGER.error("Exception while resetting forgot password", e);
		}

		LOGGER.info("Exit UserServiceImpl ... resetForgotPassword");
		return response;
	}

	@Override
	public RestResponse updateProfile(AppUserVO appUserVO, LoginUser user) throws Exception {
		UserModel siteUser = getUserDAO(user.getDbName()).getUserDetails(user.getUsername());
		LOGGER.info("Inside UserServiceImpl ... updateProfile");
		RestResponse response = new RestResponse();
		List<UserModel> userModelList = getUserDAO(user.getDbName()).checkUniquePhoneForUser(Long.parseLong(appUserVO.getPhoneNo()));
		LOGGER.info("Checking existing user with same phone number");
		if(userModelList.isEmpty()){
			LOGGER.info("No other user available with same number");
			int updated = getUserDAO(user.getDbName()).updateUserProfile(appUserVO, user);
				if(updated>0){
					response.setStatusCode(200);
					response.setObject(siteUser);
					response.setMessage("Profile updated successfully");
				}
		}
		else if(userModelList.size()>0){
			for(UserModel phoneUsers : userModelList){
				if(phoneUsers.getEmailId().equalsIgnoreCase(siteUser.getEmailId())){
					int updated = getUserDAO(user.getDbName()).updateUserProfile(appUserVO, user);
					if(updated>0){
						response.setStatusCode(200);
						response.setObject(siteUser);
						response.setMessage("Profile updated successfully");
					}
				 break;
				}
			}
		}
		LOGGER.info("Exit UserServiceImpl ... updateProfile");
		return response;
	}


	@Override
	public List<RoleStatus> getRoleStatus(LoginUser user) {
		try {
			System.out.println("Role ID --- > "+user.getUserRoles().get(0));
			List<RoleStatus> roleStatus = getUserDAO(user.getDbName()).getRoleStatus(user.getUserRoles().get(0).getRole().getRoleId());
			return roleStatus;
		} catch (Exception e) {
			LOGGER.error("Error Occured while fetching Role Status mapping");
			e.printStackTrace();
			return null;
		}

	}
	
	@Override
	public List<RoleStatus> getRoleStatusByRoleId(final Long roleId) {
		try {
			System.out.println("Role ID --- > "+roleId);
			//List<RoleStatus> roleStatus = getUserDAO(dbName).getRoleStatus(roleId);
			//return roleStatus;
		} catch (Exception e) {
			LOGGER.error("Error Occured while fetching Role Status mapping");
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public List<Role> findAllRoles(LoginUser user) throws Exception {
		List<Role> roleList = new ArrayList<Role>();
		 if( user.getUserType().equalsIgnoreCase("USER")){
			 roleList =	 getUserDAO(user.getDbName()).getAllRoles();
		 }else if( user.getUserType().equalsIgnoreCase("SP")) {
			 roleList =  getUserDAO(user.getDbName()).getAllSPRoles();
		 }
		return roleList==null?Collections.emptyList():roleList;
	}

	@Override
	public List<UserVO> getUserSiteAccess(Long siteId, LoginUser user) throws Exception {
		List<UserVO> userList = getUserDAO(user.getDbName()).getUserWithSiteAccess(siteId);
		return userList;
		
	}
	@Override
	public List<UserVO> getUserSiteWithoudAccess(Long siteId, LoginUser user) throws Exception {
		List<UserVO> userList = getUserDAO(user.getDbName()).getUserWithoutSiteAccess(siteId);
		return userList;
		
	}

	@Override
	public boolean assignUserToSite(Long userId, Long siteId, LoginUser user) {
		int updated = getUserDAO(user.getDbName()).grantAccess(userId, siteId);
		return updated>0?true:false;
	}

	@Override
	public void removeUserAccessFromSite(Long accessId, LoginUser user) {
		 getUserDAO(user.getDbName()).revokeAccess(accessId);
	}

}
