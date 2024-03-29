/*
 * Copyright (C) 2013 , Inc. All rights reserved 
 */
package com.pms.web.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.UserSiteAccessVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.User;
import com.pms.jpa.entities.UserModel;
import com.pms.jpa.entities.UserSiteAccess;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.RestResponse;

public interface UserService {

	User save(User user);

	List<User> findALL();

	List<Role> listRoles();

	User retrieve(Long userId);

	User update(User user);

	User findByUserName(String userName);

	public User findByEmail(String email);

	User getCurrentLoggedinUser();

	UserVO updateRoles(UserVO userVO,LoginUser user);

	UserVO saveUser(AppUserVO appUserVO, LoginUser user) throws Exception;

	List<UserVO> findALLUsers(Long companyId, LoginUser user) throws Exception;

	AuthorizedUserDetails getAuthorizedUser(Authentication springAuthentication) throws Exception;

	RestResponse changePassword(PasswordVO passwordVO, LoginUser user);

	RestResponse updateRole(AppUserVO appUserVO, LoginUser user) throws Exception;

	RestResponse updateStatus(AppUserVO appUserVO, LoginUser user) throws Exception ;

	int checkUserAvailibility(String email)  throws Exception ;

	RestResponse updateProfile(AppUserVO appUserVO, LoginUser user) throws Exception;

	List<RoleStatus> getRoleStatus(LoginUser user) throws Exception;

	List<RoleStatus> getRoleStatusByRoleId(Long roleId) throws Exception;

	List<Role> findAllRoles(LoginUser user) throws Exception;

	RestResponse resetNewPassword(String email, String type, String newPassword) throws Exception;
	
	RestResponse resetForgotPassword(String email, String type, String newPassword) throws Exception;

	List<UserVO> getUserSiteAccess(Long userId, LoginUser user) throws Exception;

	List<UserVO> getUserSiteWithoudAccess(Long siteId, LoginUser user) throws Exception;

	boolean assignUserToSite(Long userId, Long siteId, LoginUser loginUser);

	void removeUserAccessFromSite(Long accessId, LoginUser loginUser);

	UserModel getTicketCreationUser(LoginUser user, final String raisedBy);
}
