/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.PasswordVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.UserModel;
import com.pms.web.service.EmailService;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.service.UserService;
import com.pms.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/user")
@Controller
public class UserController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = "application/json")
	public String userHome(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("USER")) {
			boolean isSessionEnabled = request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled + "" + request.getSession().getId());
			model.put("user", loginUser);
			return "user.list";
		} else if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			boolean isSessionEnabled = request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled + "" + request.getSession().getId());
			model.put("user", loginUser);
			return "sp.user.list";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET, produces = "application/json")
	public String userProfile(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			model.put("user", loginUser);
			return "user.profile";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String userDetails(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("USER")) {
			model.put("user", loginUser);
			return "user.details";
		} else if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "sp.user.details";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/logged", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> loggedInUserDetail(final ModelMap model, final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			AppUserVO appUserVO = new AppUserVO();
			response.setStatusCode(200);
			appUserVO.setUserId(loginUser.getUserId());
			appUserVO.setEmail(loginUser.getUsername());
			appUserVO.setFirstName(loginUser.getFirstName());
			appUserVO.setLastName(loginUser.getLastName());
			appUserVO.setPhoneNo(loginUser.getPhoneNo());
			// appUserVO.setPhoneNo(loginUser.getPhoneNo());
			appUserVO.setRole(loginUser.getUserRoles().get(0).getRole());
			// Country country =
			// countryService.findCountry(loginUser.getCompany().getCountryId());
			appUserVO.setCompany(loginUser.getCompany());
			response.setObject(appUserVO);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> userList(final HttpServletRequest request, final HttpSession session) {
		logger.info("Inside UserController - userList");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				List<UserVO> userList = new ArrayList<UserVO>();
				if (loginUser.getUserType().equalsIgnoreCase("USER")) {
					userList = userService.findALLUsers(loginUser.getCompany().getCompanyId(), loginUser);
				} else if (loginUser.getUserType().equalsIgnoreCase("SP")) {
					userList = serviceProviderService.findALLSPUsers(loginUser.getCompany().getCompanyId(), loginUser);
				}
				if (userList.size() > 0) {
					response.setStatusCode(200);
					response.setObject(userList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(404);
					response.setMessage("No user available ");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(500);
				response.setMessage("Exception while getting user list ");
				logger.error("Exception while getting user list", e);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - userList");
		return responseEntity;
	}

	@RequestMapping(value = "/roles", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> userRoles(final HttpSession session) {
		logger.info("Inside UserController - userRoles");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser user = getCurrentLoggedinUser(session);
		if (user != null) {

			try {
				List<Role> roles = userService.findAllRoles(user);
				if (!roles.isEmpty()) {
					response.setStatusCode(200);
					response.setObject(roles);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(500);
				response.setMessage("Exception while getting role list ");
				logger.error("Exception while getting role list", e);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
		}
		logger.info("Exit UserController - userRoles");
		return responseEntity;
	}

	@RequestMapping(value = "/role/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateUserRoles(@RequestBody final UserVO userVO, final HttpSession session) {
		logger.info("Inside UserController - updateUserRoles");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser user = (LoginUser) session.getAttribute("loginUser");
		if (user != null) {
			try {
				logger.info("Updating roles for " + userVO.getFirstName() + "" + userVO.getLastName());
				UserVO savedUser = userService.updateRoles(userVO, user);
				if (savedUser != null) {
					response.setStatusCode(200);
					response.setMessage("User role updated successfully");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(201);
					response.setMessage("Unable to update role");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				response.setMessage("Exception while updating role");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - updateUserRoles");
		return responseEntity;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> saveNewUser(final HttpSession session, @RequestBody final AppUserVO appUserVO) {
		logger.info("Inside UserController - saveNewUser");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		UserVO userVO = null;
		LoginUser user = (LoginUser) session.getAttribute("loginUser");
		if (user != null) {
			if (!StringUtils.isEmpty(appUserVO.getEmail())) {
				try {
					appUserVO.setCompany(user.getCompany());
					userVO = userService.saveUser(appUserVO, user);
					if (userVO.isExists()) {
						response.setStatusCode(204);
						response.setMessage("User with \"" + appUserVO.getEmail() + "\" already exists.");
					} else {
						if(userVO.getStatus()==204){
							response.setMessage("Unable to map user to tenant ");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}else{
							response.setStatusCode(200);
							response.setMessage("User with \"" + appUserVO.getEmail() + "\" registered successfully.");
							appUserVO.setGeneratedPassword(userVO.getPasswordGenerated());
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
							final AppUserVO savedUser = appUserVO;
							TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
							theExecutor.execute(new Runnable() {
								@Override
								public void run() {
									logger.info("Email thread started : " + Thread.currentThread().getName());
									try {
										emailService.sendSuccessSaveEmail(savedUser.getEmail(), appUserVO);
										logger.info("Email for register user sent successfully");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
										logger.info("Email for register user did not sent.");
									}
								}
							});
						}
						
					
					}
				} catch (Exception e) {
					response.setStatusCode(500);
					response.setMessage("Exception while saving new user.");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					logger.error("Exception while saving new user", e);
				}
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your login session has expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - saveNewUser");
		return responseEntity;
	}

	@RequestMapping(value = "/enableordisable", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody RestResponse changeStatus(@RequestBody final AppUserVO appUserVO, final HttpSession session) {
		logger.info("Inside UserController - changeStatus");
		RestResponse response = new RestResponse();
		LoginUser user = getCurrentLoggedinUser(session);
		try {
			if (user != null) {
				response = userService.updateStatus(appUserVO, user);
				if (response.getStatusCode() == 200) {
					response.setStatusCode(200);
					response.setMessage(response.getMessage());
				} else {
					response.setStatusCode(204);
					response.setMessage(response.getMessage());
				}
			} else {
				response.setStatusCode(404);
				response.setMessage("Your current session is expired. Please login again");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception while updating user account status");
		}
		logger.info("Exit UserController - changeStatus");
		return response;
	}

	@RequestMapping(value = "/extsp/incident/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model, HttpSession session) {
		LoginUser user = (LoginUser) session.getAttribute("loginUser");
		if (user != null) {
			try {
				model.put("savedsp", user);
				return "sp.incident.details";
			} catch (Exception e) {
				e.printStackTrace();
				return "redirect:/";
			}
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/passwordchange", method = RequestMethod.POST, produces = "application/json")
	public @ResponseBody RestResponse updateUserPassword(@RequestBody final PasswordVO passwordVO, final HttpSession session) {
		logger.info("Inside UserController - updateUserPassword" );
		RestResponse response=new RestResponse();
		LoginUser user=getCurrentLoggedinUser(session);
		try{
			if(user!=null){
				response  = userService.changePassword(passwordVO, user);
				if(response.getStatusCode()==200){
					response.setStatusCode(200);
					response.setMessage("Password changed successfully.");
					session.invalidate();
				}else{
					response.setStatusCode(204);
					response.setMessage(response.getMessage());
				}
			}else {
				response.setStatusCode(404);
				response.setMessage("Your current session is expired. Please login again");
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception while changing the password");
		}
		logger.info("Exit UserController - updateUserPassword" );
		return response;
	}

	
	@RequestMapping(value = "/profile/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateProfile(@RequestBody final AppUserVO appUserVO, final HttpSession session) {
		logger.info("Inside UserController - updateProfile" );
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response=new RestResponse();
		LoginUser user=getCurrentLoggedinUser(session);
		try{
			if(user!=null){
				response  = userService.updateProfile(appUserVO, user);
				if(response.getStatusCode()==200){
					response.setStatusCode(200);
					response.setMessage(response.getMessage());
					UserModel updatedUser = (UserModel) response.getObject();
					user.setFirstName(updatedUser.getFirstName());
					user.setLastName(updatedUser.getLastName());
					user.setPhoneNo(String.valueOf(updatedUser.getPhoneNo()));
					session.setAttribute("loginUser", user);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(204);
					response.setMessage(response.getMessage());
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			}else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		}catch(Exception e){
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception while updating user profile");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}
		logger.info("Exit UserController - updateProfile" );
		return responseEntity;
	}
	
	@RequestMapping(value = "/site/access/{siteId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse>  getUserSiteAccess(final ModelMap model,final HttpSession session,
			@PathVariable(value="siteId") Long siteId ) {
		logger.info("Inside UserController - getUserSiteAccess" );
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser=getCurrentLoggedinUser(session);

		if (loginUser!=null) {
			try {
				List<UserVO> userSiteAccessList = userService.getUserSiteAccess(siteId,loginUser);
				List<UserVO> userSiteWithoutAccessList = userService.getUserSiteWithoudAccess(siteId,loginUser);
				response.setStatusCode(200);
				response.setObject(userSiteAccessList);
				response.setObject2(userSiteWithoutAccessList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				response.setStatusCode(500);
				response.setMessage("Exception while getting user site access list");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				e.printStackTrace();
			}

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - getUserSiteAccess" );
		return responseEntity;
	}
	@RequestMapping(value = "/assign/site/{userId}/{siteId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse>  assignUserToSite(@PathVariable(value="userId") Long userId,
			@PathVariable(value="siteId") Long siteId,final HttpSession session) {
		logger.info("Inside UserController - assignUserToSite" );
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			try {
				boolean isUpdated= userService.assignUserToSite(userId, siteId, loginUser);
				if(isUpdated){
					response.setStatusCode(200);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} catch (Exception e) {
				response.setStatusCode(500);
				response.setMessage("Exception while assign user to site");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				e.printStackTrace();
			}

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - assignUserToSite" );
		return responseEntity;
	}


	@RequestMapping(value = "/revoke/site/{accessId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse>  revokeAccessToSite(@PathVariable(value="accessId") Long accessId,
			final HttpSession session) {
		logger.info("Inside UserController - revokeAccessToSite" );
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			try {
				userService.removeUserAccessFromSite(accessId, loginUser);
				logger.info("Access revoked from the user " );
				response.setStatusCode(200);
				response.setMessage("Site Access revoked from the User.");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				
			} catch (Exception e) {
				response.setStatusCode(500);
				response.setMessage("Exception while revoking access from user.");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				e.printStackTrace();
			}

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - revokeAccessToSite" );
		return responseEntity;
	}
}
