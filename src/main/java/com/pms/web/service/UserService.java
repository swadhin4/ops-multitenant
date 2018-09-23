/*
 * Copyright (C) 2013 , Inc. All rights reserved 
 */
package com.pms.web.service;

import java.util.List;

import org.springframework.security.core.Authentication;

import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.User;
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

	UserVO saveUser(AppUserVO appUserVO) throws Exception;

	List<UserVO> findALLUsers(Long companyId) throws Exception;

	AuthorizedUserDetails getAuthorizedUser(Authentication springAuthentication) throws Exception;

	RestResponse changePassword(PasswordVO passwordVO, LoginUser user);

	RestResponse updateRole(AppUserVO appUserVO, LoginUser user) throws Exception;

	RestResponse updateStatus(AppUserVO appUserVO, String isEnabled) throws Exception ;

	int checkUserAvailibility(String email)  throws Exception ;

	RestResponse resetNewPassword(String email, String newPassword) throws Exception ;

	RestResponse resetForgotPassword(String email, String newPassword) throws Exception;

	RestResponse updateProfile(AppUserVO appUserVO) throws Exception;
}
