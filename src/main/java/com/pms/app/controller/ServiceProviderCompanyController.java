package com.pms.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.UserVO;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.util.RestResponse;

@RequestMapping(value = "/serviceprovidercompany")
@Controller
public class ServiceProviderCompanyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderCompanyController.class);

	@Autowired
	private ServiceProviderService serviceProviderService;

	@RequestMapping(value = "/newuser", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestResponse> createUser(@RequestBody final SPUserVo spUserVo, final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				spUserVo.setCreatedBy(loginUser.getUsername());
				String insertedRows = serviceProviderService.createServiceProviderUser(spUserVo, loginUser);
				response.setStatusCode(200);
				response.setObject(insertedRows);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "/updatecustomers/{selectedspuserid}", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateUser(@RequestBody final List<CustomerVO> customerVOList,
			@PathVariable(value="selectedspuserid") Long selectedSPUserId, final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				String insertedRows = serviceProviderService.updateServiceProviderCustomers(customerVOList,selectedSPUserId, loginUser);
				response.setStatusCode(200);
				response.setObject(insertedRows);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "/getusers", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getAllUsersWithRoleandCustomers(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				List<UserVO> serviceUsers = serviceProviderService.getAllUsersWithRole(loginUser);
				response.setStatusCode(200);
				response.setObject(serviceUsers);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/getcustomers", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getCustomerForSP(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				List<CustomerVO> customerList = serviceProviderService.getAllCustomers(loginUser);
				response.setStatusCode(200);
				response.setObject(customerList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "/selectedcustomer/{spuserid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getCustomerForSelectedUser(final HttpSession session, @PathVariable(value="spuserid") Long spuserid) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				List<CustomerVO> customerList = serviceProviderService.getCustomerForSelectedUser(loginUser, spuserid);
				response.setStatusCode(200);
				response.setObject(customerList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}
}
