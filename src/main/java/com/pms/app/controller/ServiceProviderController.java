/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.EmailService;
import com.pms.web.service.ServiceProviderService;
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
		}
		else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit SiteController .. createNewServiceProvider");
		return responseEntity;
	}



	@RequestMapping(value = "/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllServiceProvider(final HttpSession session) {
		logger.info("Inside ServiceProviderController .. listAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOs = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				serviceProviderVOs = serviceProviderService.findSPList(loginUser);
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
	@RequestMapping(value = "/info/{spId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> serviceProviderInfo(@PathVariable (value="spId") Long spId,final HttpSession session) {
		logger.info("Inside ServiceProviderController .. serviceProviderInfo");
		
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				ServiceProviderVO serviceProviderVO = serviceProviderService.findServiceProviderInfo(spId,loginUser);
					response.setStatusCode(200);
					response.setObject(serviceProviderVO);
					responseEntity = new  ResponseEntity<RestResponse>(response, HttpStatus.OK);
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
}
