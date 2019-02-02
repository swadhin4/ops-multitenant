/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.util.Collections;
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

import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.EmailService;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.service.SiteService;
import com.pms.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/serviceprovider")
@Controller
public class ServiceProviderController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderController.class);


	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private SiteService siteService;

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String userDetails(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				return "service.provider";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/rsp/incident/create", method = RequestMethod.GET)
	public String rspIncidentPage(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.addAttribute("mode","NEW");	
			model.put("user", loginUser);
			return "serviceprovider.incident.create";
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/rsp/incident/update", method = RequestMethod.GET)
	public String rspIncidentUpdatePage(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.addAttribute("mode","EDIT");	
			model.put("user", loginUser);
			return "serviceprovider.incident.update";
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/rsp/site/list/{custdb}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<CreateSiteVO>> listAllRSPSites(HttpSession session, @PathVariable (value="custdb") final String custDB) {
		logger.info("Inside ServiceProviderController -- listAllRSPSites");
		List<CreateSiteVO> sitesList = null;
		try {
			LoginUser user= getCurrentLoggedinUser(session);
			if(user!=null){
				if(StringUtils.isNotBlank(custDB)){
				sitesList = siteService.getSPSiteList(user.getCompany().getCompanyCode(),custDB, user);
				if (sitesList.isEmpty()) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
					// You many decide to return HttpStatus.NOT_FOUND
				}else{
					Collections.sort(sitesList, CreateSiteVO.COMPARE_BY_SITENAME);
				}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Exit ServiceProviderController -- listAllRSPSites");
		return new ResponseEntity<List<CreateSiteVO>>(sitesList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/resetpassword/{spid}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse>  resetServiceProviderPassword(final HttpServletRequest request,
			@PathVariable(value="spid") Long spId,
			final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser!=null) {
			try {
				if(loginUser.getUserType().equalsIgnoreCase("USER")){
				ServiceProviderVO updatedServiceProvider= serviceProviderService.resetPassword(spId, loginUser);
					if(updatedServiceProvider.getOption().equalsIgnoreCase("PWDUPDATED")){
						response.setStatusCode(200);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}else{
						response.setStatusCode(401);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.BAD_REQUEST);
					}
					
					if(response.getStatusCode()==200 ){
						logger.info("Sending Password reset Email to "+ updatedServiceProvider.getHelpDeskEmail());
						final ServiceProviderVO updatedSP = updatedServiceProvider;
						TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
						theExecutor.execute(new Runnable() {
							@Override
							public void run() {
								logger.info("Email thread started : " + Thread.currentThread().getName());
								try {
									emailService.successExtSPPasswordReset(updatedSP, loginUser);
								} catch (Exception e) {
									logger.info("Exception while sending email for Password reset for EXTSP");
									e.printStackTrace();
								}
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/rsp/active/user/list/{custcode}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> activeUserList(final HttpServletRequest request, final HttpSession session, @PathVariable(value="custcode") String custcode) {
		logger.info("Inside UserController - userList" );
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			try {
					logger.info("Getting RSP  Agents from : " + loginUser.getSpDbName() +" for "+ loginUser.getUsername());
					List<UserVO> userList = serviceProviderService.findALLActiveSPUsers(custcode, loginUser);
					if(userList.size()>0){
						response.setStatusCode(200);
						response.setObject(userList);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}else{
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

		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - userList" );
		return responseEntity;
	}
	
	@RequestMapping(value = "/user/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> userList(final HttpServletRequest request, final HttpSession session) {
		logger.info("Inside UserController - userList" );
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			try {
				List<UserVO> userList = serviceProviderService.findALLSPUsers(loginUser.getCompany().getCompanyId(), loginUser);
				if(userList.size()>0){
					response.setStatusCode(200);
					response.setObject(userList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
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

		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit UserController - userList" );
		return responseEntity;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> createNewServiceProvider(final Locale locale, final ModelMap model,
			@RequestBody final ServiceProviderVO serviceProviderVO, final HttpSession session) {
		logger.info("Inside ServiceProviderController .. createNewServiceProvider");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		ServiceProviderVO savedServiceProvider =null;
		if(loginUser!=null){
			try {

				logger.info("Create New ServiceProvider : "+ serviceProviderVO);
				savedServiceProvider = serviceProviderService.saveServiceProvider(serviceProviderVO,loginUser);
				if(savedServiceProvider.getStatus()==200){
					response.setStatusCode(200);
					response.setObject(savedServiceProvider);
					response.setMessage(savedServiceProvider.getMessage());
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					if(response.getStatusCode()==200 && savedServiceProvider.getOption().equalsIgnoreCase("CREATED")){
						final ServiceProviderVO savedSP = savedServiceProvider;
						TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
						theExecutor.execute(new Runnable() {
							@Override
							public void run() {
								logger.info("Email thread started : " + Thread.currentThread().getName());
								try {
									emailService.successSaveSPEmail(savedSP, loginUser);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
					}
				}else{
					response.setStatusCode(204);
					response.setMessage(savedServiceProvider.getMessage());
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				logger.info("Exception while creating service provider", e);
					response.setStatusCode(500);
					response.setMessage("Error occured while saving service provider ");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			}
			

			
		}
		else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit SiteController .. createNewServiceProvider");
		return responseEntity;
	}



	@RequestMapping(value = "/list/{spType}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllServiceProvider(final HttpSession session, @PathVariable(value="spType")  String spType) {
		logger.info("Inside ServiceProviderController .. listAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOs = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
					String spTypeSelected = null;
					if(!StringUtils.isEmpty(spType) && spType.equalsIgnoreCase("RSP")){
						serviceProviderVOs = serviceProviderService.findSPList(loginUser, spType);
					}
					else if(!StringUtils.isEmpty(spType) && spType.equalsIgnoreCase("EXT")){
						serviceProviderVOs = serviceProviderService.findSPList(loginUser, spType);
					}
					else if(!StringUtils.isEmpty(spType) && spType.equalsIgnoreCase("ALL")){
						serviceProviderVOs = serviceProviderService.findAllSPList(loginUser);
					}
					if (serviceProviderVOs.isEmpty()) {
						response.setStatusCode(404);
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
					}else{
						//Collections.sort(serviceProviderVOs, ServiceProviderVO.COMPARE_BY_SPNAME);
						response.setStatusCode(200);
						response.setObject(serviceProviderVOs);
						responseEntity = new  ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
			} catch (Exception e) {
				logger.info("Exception in getting service provider list", e);
				response.setMessage("Exception while getting service provider list");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			}
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit ServiceProviderController .. listAllServiceProvider");
		return responseEntity;
	}

	

	@RequestMapping(value = "/regions", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Region>> listAllRegions(final HttpSession session) {
		List<Region> regions = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if(loginUser!=null){
				regions = serviceProviderService.findAllRegions(loginUser);
				if (regions.isEmpty()) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception getting region list", e);
		}

		return new ResponseEntity<List<Region>>(regions, HttpStatus.OK);
	}


	@RequestMapping(value = "/country/{regionId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Country>> listAllCountries(final HttpSession session,@PathVariable (value="regionId") final Long regionId ) {
		List<Country> countryList = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if(loginUser!=null){
			countryList = serviceProviderService.findCountryByRegion(regionId, loginUser);
			if (countryList.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception getting region list", e);
		}

		return new ResponseEntity<List<Country>>(countryList, HttpStatus.OK);
	}
	@RequestMapping(value = "/info/{spId}/{spViewType}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> serviceProviderInfo(@PathVariable (value="spId") Long spId,
			@PathVariable (value="spViewType") String spViewType,final HttpSession session) {
		logger.info("Inside ServiceProviderController .. serviceProviderInfo");
		
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				ServiceProviderVO serviceProviderVO = serviceProviderService.findServiceProviderInfo(spId,loginUser, spViewType);
				 if(serviceProviderVO.getServiceProviderId()!=null){
					response.setStatusCode(200);
					response.setObject(serviceProviderVO);
					responseEntity = new  ResponseEntity<RestResponse>(response, HttpStatus.OK);
				 }else{
					response.setStatusCode(404);
					response.setMessage("Service Provider Details not available");
					responseEntity = new  ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND); 
				 }
			} catch (Exception e) {
				logger.info("Exception in getting service provider list", e);
				response.setMessage("Exception while getting service provider list");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			}
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Inside ServiceProviderController .. serviceProviderInfo");
		return responseEntity;
	}
	
	@RequestMapping(value = "/rsp/access/customer/{rspId}/{accessValue}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> accessOrRevokeAccess(HttpSession session, 
			@PathVariable (value="rspId") final Long rspId,
			@PathVariable (value="accessValue") final String accessValue) {
		logger.info("Inside ServiceProviderController -- accessOrRevokeAccess");
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		try {
			LoginUser user= getCurrentLoggedinUser(session);
			if(user!=null){
				int updated=0;
				if(!StringUtils.isEmpty(accessValue) && accessValue.equalsIgnoreCase("Y")){
					updated = serviceProviderService.grantOrRevokeAccess(rspId,user,"N");
				  if(updated==200){
					  response.setStatusCode(200);
					  response.setMessage("Access has been revoked");
					  responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				  }
				}
				else if(!StringUtils.isEmpty(accessValue) && accessValue.equalsIgnoreCase("N")){
					   updated = serviceProviderService.grantOrRevokeAccess(rspId,user,"Y");
					  if(updated==200){
						  response.setStatusCode(200);
						  response.setMessage("Access has been granted");
						  responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
						  if(updated==200){
								TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
								theExecutor.execute(new Runnable() {
									@Override
									public void run() {
										logger.info("Email thread started : " + Thread.currentThread().getName());
										try {
											emailService.accessGrantedRSPEmail(rspId, user);
										} catch (Exception e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
							}
					  }
				}else{
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
			
			}
		} catch (Exception e) {
			e.printStackTrace();
			  response.setStatusCode(500);
			  response.setMessage("Exception while updating access details");
			  responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
		}
		logger.info("Exit ServiceProviderController -- accessOrRevokeAccess");
		return responseEntity;
	}
	
}
