package com.pms.web.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.User;
import com.pms.web.service.UserService;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.RestResponse;

@Service("userService")
public class UserServiceImpl implements UserService {
	

	public UserServiceImpl() {
		super();
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserVO saveUser(AppUserVO appUserVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserVO> findALLUsers(Long companyId) throws Exception {
		// TODO Auto-generated method stub
		return null;
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
	public RestResponse updateStatus(AppUserVO appUserVO, String isEnabled) throws Exception {
		// TODO Auto-generated method stub
		return null;
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

}
