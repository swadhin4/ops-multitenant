package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.ServiceProviderDAOImpl;
import com.pms.app.dao.impl.UserDAO;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.User;
import com.pms.web.service.UserService;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.QuickPasswordEncodingGenerator;
import com.pms.web.util.RandomUtils;
import com.pms.web.util.RestResponse;

@Service("userService")
public class UserServiceImpl implements UserService {
	

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
	private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

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
		UserVO savedUserVO	= getUserDAO(user.getDbName()).updateRole(userVO);
		return savedUserVO;
	}

	@Override
	@Transactional
	public UserVO saveUser(AppUserVO appUserVO, LoginUser user) throws Exception {
		String generatedRawPassword = RandomUtils.randomAlphanumeric(8);
		String encryptedPassword = QuickPasswordEncodingGenerator.encodePassword(generatedRawPassword);
		appUserVO.setGeneratedPassword(encryptedPassword);
		UserVO savedUserVO = null;
		if(user.getUserType().equalsIgnoreCase("USER")){
			savedUserVO	= getUserDAO(user.getDbName()).saveNewUser(appUserVO, user);
			if(savedUserVO.getUserId()!=null){
				savedUserVO.setRoleId(appUserVO.getRole().getRoleId());
				Long roleId = getUserDAO(user.getDbName()).saveUserRole(savedUserVO);
				if(roleId>0){
					LOGGER.info("User created with role");
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
					LOGGER.info("SP User created with role");
				}else{
					LOGGER.info("SP User not created with role");
				}
				List<CustomerVO> customerList = appUserVO.getCustomerList();
				
				if(!customerList.isEmpty()){
					Integer recordsMapped = getServiceProviderDAOImpl(user.getDbName()).createServiceProviderUserAccess(customerList, user);
					if(recordsMapped > 0 ){
						savedUserVO.setStatus(200);
					}
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse changePassword(PasswordVO passwordVO, LoginUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse updateRole(AppUserVO appUserVO, LoginUser user) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse updateStatus(AppUserVO appUserVO, LoginUser user) throws Exception {
		int updated = getUserDAO(user.getDbName()).updateUserStatus(appUserVO);
		RestResponse response = new RestResponse();
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
	public RestResponse resetNewPassword(String email, String newPassword) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse resetForgotPassword(String email, String newPassword) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RestResponse updateProfile(AppUserVO appUserVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
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

}
