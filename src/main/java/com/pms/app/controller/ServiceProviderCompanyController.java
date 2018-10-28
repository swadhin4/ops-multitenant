package com.pms.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

	@RequestMapping(value = "/updateuser", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateUser(@RequestBody final SPUserVo spUserVo, final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser =  getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				spUserVo.setModifiedBy(loginUser.getUsername());
				String insertedRows = serviceProviderService.updateServiceProviderUser(spUserVo, loginUser);
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
				List<UserVO> serviceUsers = serviceProviderService.getAllUsersWithRoleAndCustomers(loginUser);
				response.setStatusCode(200);
				response.setObject(serviceUsers);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

}
