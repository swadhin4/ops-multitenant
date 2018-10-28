/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
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
	
/*	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> createNewServiceProvider(final Locale locale, final ModelMap model,
			@RequestBody final ServiceProviderVO serviceProviderVO, final HttpSession session) {
		logger.info("Inside ServiceProviderController .. createNewServiceProvider");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			ServiceProviderVO savedServiceProvider =null;
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
				try {
					emailService.successSaveSPEmail(savedServiceProvider, loginUser);
				} catch (Exception e) {
					logger.info("Exception while sending email for service provider upon "+ savedServiceProvider.getOption(), e);
				}
			}
		}
		else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit SiteController .. createNewServiceProvider");
		return responseEntity;
	}*/



	@RequestMapping(value = "/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllServiceProvider(final HttpSession session) {
		logger.info("Inside ServiceProviderController .. listAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOs = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				serviceProviderVOs = serviceProviderService.findAllServiceProvider(loginUser);
				if (serviceProviderVOs.isEmpty()) {
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}else{
					Collections.sort(serviceProviderVOs, ServiceProviderVO.COMPARE_BY_SPNAME);
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
		logger.info("Inside ServiceProviderController .. listAllServiceProvider");
		return responseEntity;
	}

	/*@RequestMapping(value = "/list/by/{customerId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> serviceProviderByCompany(@PathVariable (value="customerId") Long customerId,final HttpSession session) {
		logger.info("Inside ServiceProviderController .. serviceProviderByCompany");
		List<ServiceProviderVO> serviceProviderVOs = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				serviceProviderVOs = serviceProviderService.findServiceProviderByCustomer(customerId);
				if (serviceProviderVOs.isEmpty()) {
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}else{
					Collections.sort(serviceProviderVOs, ServiceProviderVO.COMPARE_BY_SPNAME);
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
		logger.info("Inside ServiceProviderController .. serviceProviderByCompany");
		return responseEntity;
	}*/
}
