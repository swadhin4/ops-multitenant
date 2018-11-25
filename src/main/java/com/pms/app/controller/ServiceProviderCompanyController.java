package com.pms.app.controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.util.RestResponse;

@RequestMapping(value = "/serviceprovidercompany")
@Controller
public class ServiceProviderCompanyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderCompanyController.class);

	@Autowired
	private ServiceProviderService serviceProviderService;
	
	@RequestMapping(value = "/customers", method = RequestMethod.GET)
	public String userDetails(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "serviceprovider.customers";
		} else {
			return "redirect:/login";
		}
	}
	
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
				if(insertedRows.equalsIgnoreCase("success")){
					response.setStatusCode(200);
					response.setMessage("Customer mapping updated successfully");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
				else if (insertedRows.equalsIgnoreCase("failure")){
					response.setStatusCode(204);
					response.setMessage("Unable to Map Customers for selected service provider");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				response.setMessage("Exception while mapping customers");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
	
	@RequestMapping(value = "/mappedcustomers", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getCustomerAndCountryForLOggedinUser(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);

		if (loginUser != null) {
			try {
				List<CustomerVO> customerList = serviceProviderService.getCustomerCountryForloggedInUser(loginUser,	loginUser.getUserId());
				response.setStatusCode(200);
				response.setObject(customerList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "/customers/tickets/{custcode}/{custDBName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getCustomerTickets(final HttpSession session,
			@PathVariable(value = "custcode") String custcode,
			@PathVariable(value = "custDBName") String custDBName) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		
		if (loginUser != null) {
			try {
				List<TicketVO> ticketList = serviceProviderService.getCustomerTickets(custcode,custDBName);
				response.setStatusCode(200);
				response.setObject(ticketList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}
}
