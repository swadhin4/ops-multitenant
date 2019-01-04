/*
 */
package com.pms.app.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import com.pms.app.exception.PMSTechnicalException;
import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.UserRole;
import com.pms.web.service.security.AuthorizedUserDetails;
import com.pms.web.util.CredentialUtils;
import com.pms.web.util.ServiceUtil;

/**
 * The Class BaseController.
 *
 */
public class BaseController {

	private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

	public String getCurrentLoggedinUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		 if(StringUtils.isNotBlank(username)){
			return username;
		}
		return username;
	}
	
	private AuthorizedUserDetails authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		String username = getCurrentLoggedinUserName();
		String password = String.valueOf(auth.getCredentials());

		logger.info("username:" + username);
		logger.info("password:" + password); // Don't log passwords in real app

		// 1. Use the username to load the data for the user, including authorities and password.
		// 2. Check the passwords match (should use a hashed password here).
		AuthorizedUserDetails userDetails = null;//(AuthorizedUserDetails) userAuthorizationService.loadUserByUsername(username);
		try {
			boolean isPasswordValidated = CredentialUtils.validatePassword(password, userDetails.getUser().getPassword());
			if (isPasswordValidated) {

				// 3. Preferably clear the password in the user object before storing in authentication object
				userDetails.eraseCredentials();

				// 4. Return an authenticated token, containing user data and authorities  
				userDetails.getUser().setPassword("");
			}
			else{
				throw new BadCredentialsException("Bad Credentials");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.info("Exception while validating password", e);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			logger.info("Exception while validating key for the password", e);
		}
		return userDetails ;
	}

	/**
	 * Gets the current loggedin user.
	 *
	 * @return the current loggedin user
	 */
	public LoginUser getCurrentLoggedinUser( final HttpSession session) throws PMSTechnicalException{
		LoginUser loginUser=(LoginUser) session.getAttribute("loginUser");
		
		String username = getCurrentLoggedinUserName();
		if (!StringUtils.isNotBlank(username)){
		//	logger.info("User session is expired or not started yet.");
			throw new PMSTechnicalException("User session is expired. Please re-login");
		}else if (loginUser !=null){
			return loginUser;
		}
		else{
			if (StringUtils.isNotBlank(username)) {
				 if(username.equalsIgnoreCase("anonymousUser")){
					 loginUser = null;
					 throw new PMSTechnicalException("Invalid user credentials. Please re-login");
				 }else{
					Authentication springAuthentication = SecurityContextHolder.getContext().getAuthentication();
					AuthorizedUserDetails authUser = (AuthorizedUserDetails) springAuthentication.getPrincipal() ;
					try {
						//authUser = null;//userService.getAuthorizedUser(springAuthentication);
						if(authUser.getUser()!=null) {
							if(authUser.getUser().getEnabled() == 0){
								logger.info("User account is locked");
								loginUser=new LoginUser();
								loginUser.setLoginStatus(404);
								loginUser.setMessage("Please verify your credentials. Account is either locked or not created. Please contact administrator.");
								//throw new PMSDBException(CommonConstants.UNAUTHORIZED_E1006, CommonConstants.USER_ACCOUNT_DISABLED);
							}else{
								loginUser=new LoginUser();
								logger.info("User found for " + username);
								loginUser.setUserId(authUser.getUser().getUserId());
								loginUser.setFirstName(authUser.getUser().getFirstName());
								loginUser.setLastName(authUser.getUser().getLastName());
								loginUser.setUsername(authUser.getUser().getEmailId());
								loginUser.setSysPassword(authUser.getUser().getSysPassword());
								loginUser.setPhoneNo(String.valueOf(authUser.getUser().getPhoneNo()));
								loginUser.setDbName(authUser.getUser().getDbName());
								loginUser.setTenantId(authUser.getUser().getTenantId());
								//loginUser.setDbName("probmng30");
								List<String> roles = authUser.getUser().getRoleNameList();
								Role role = new Role();
								role.setRoleId(authUser.getUser().getRoleId());
								role.setRoleName(roles.get(0));
								UserRole userRole = new UserRole(); 
								userRole.setRole(role);
								List<UserRole> userRoles = new ArrayList<UserRole>(1);
								userRoles.add(userRole);
								loginUser.setUserRoles(userRoles);
								loginUser.getCompany().setCompanyId(authUser.getUser().getCompanyId());
								loginUser.getCompany().setCompanyName(authUser.getUser().getCompanyName());
								loginUser.getCompany().setCompanyCode(authUser.getUser().getCompanyCode());
								
								loginUser.getCompany().setCountryId(authUser.getUser().getCountryId());
								loginUser.getCompany().setCountryName(authUser.getUser().getCountryName());
								
								loginUser.setUserType(authUser.getUser().getUserType());
								if(authUser.getUser().getUserType().equalsIgnoreCase("EXTSP")){
									loginUser.setSpId(authUser.getUser().getSpId());
								}
								else if (authUser.getUser().getUserType().equalsIgnoreCase("SP")){
									loginUser.setSpDbName(authUser.getUser().getDbName());
								}
								//loginUser.setCompany(authUser.getUser().getCompany());
								//loginUser.setPhoneNo(String.valueOf(authUser.getUser().getPhone()));
							//	UserRole loggedInUserRole = userRole;
								/*logger.info("Getting list of persmission for the user role : "+ loggedInUserRole.getRole().getRoleName());
								try{
									List<AppFeature> featureAccessList = rolePermissionService.getUserFeatureAccess(loggedInUserRole);
									if(!featureAccessList.isEmpty()){
										loginUser.setFeatureList(featureAccessList);
									}

								}catch(Exception e){
									logger.error("Exception while getting user details ", e);
								}*/
								session.setAttribute("loginUser", loginUser);
								session.setAttribute("loggedInUserDB", loginUser.getDbName());
							}
						}else{
							AuthorizedUserDetails authUser2 = (AuthorizedUserDetails)springAuthentication.getPrincipal();
							System.out.println(authUser2.isAccountNonLocked());
						}
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				 }
				}

			}
		return loginUser;
	}


	public void setResponseWithFile(final String fileName, final HttpServletResponse response) throws IOException {

		File file = new File(fileName);
		response.setContentType(ServiceUtil.getImageMimeType(file.getName()));
		response.setContentLength((int) file.length());
		response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		BufferedInputStream input = null;

		try {
			input = new BufferedInputStream(new FileInputStream(file));
			IOUtils.copy(input, response.getOutputStream());
			response.flushBuffer();
		} catch (Exception e) {
			logger.debug("Exception while file cop", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException ignore) {}
			}
		}
	}




}
