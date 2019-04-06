package com.pms.app.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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

import com.pms.app.exception.PMSServiceException;
import com.pms.app.exception.PMSTechnicalException;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.RSPExternalCustomerVO;
import com.pms.app.view.vo.RSPExternalSLADetailVO;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.RSPMangedService;
import com.pms.web.service.ServiceProviderService;
import com.pms.web.service.TicketService;
import com.pms.web.util.RestResponse;

@RequestMapping(value = "/serviceprovidercompany")
@Controller
public class ServiceProviderCompanyController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderCompanyController.class);

	@Autowired
	private ServiceProviderService serviceProviderService;
	
	
	@Autowired
	private TicketService ticketSerice;
	
	@Autowired
	private RSPMangedService rspManagedService;
	
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
	@RequestMapping(value = "/set/customer/{dbName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> setRspSelectedCustomerDBName(final HttpServletRequest request,
			final HttpSession session, @PathVariable(value = "dbName") String custDBName) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			session.setAttribute("selectedCustomerDB", custDBName);
			response.setStatusCode(200);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
		} else {
			response.setStatusCode(404);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_ACCEPTABLE);
		}
		return responseEntity;
	}
	@RequestMapping(value = "/externalcustomers", method = RequestMethod.GET)
	public String getExternalCustomers(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "serviceprovider.externalcustomers";
		} else {
			return "redirect:/login";
		}
	}
	@RequestMapping(value = "/ext/customer/incident/create", method = RequestMethod.GET)
	public String createExtCustomerIncident(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			model.put("mode", "NEW");
			model.put("assetVO", null);
			session.setAttribute("imageList", null);
			return "sp.externalcustomer.incident.create";
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/ext/customer/incident/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("savedsp", loginUser);
			model.put("user", loginUser);
				model.put("mode", "EDIT");
				return "sp.externalcustomer.incident.update";
		} else {
			return "redirect:/";
		}
	}
	@RequestMapping(value = "/externalcustomers/incidents", method = RequestMethod.GET)
	public String getExternalCustomersIncidents(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		try{
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "sp.externalcustomers.incidents";
		} else {
			return "redirect:/login";
		}
		}catch(PMSTechnicalException e){
			return "redirect:/login";
		}
	}
	@RequestMapping(value = "/externalcustomers/sites", method = RequestMethod.GET)
	public String getExternalCustomersSites(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "sp.externalcustomers.sites";
		} else {
			return "redirect:/login";
		}
	}
	
	
	@RequestMapping(value = "/externalcustomers/assets", method = RequestMethod.GET)
	public String getExternalCustomersAssets(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null && loginUser.getUserType().equalsIgnoreCase("SP")) {
			model.put("user", loginUser);
			return "sp.externalcustomers.assets";
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
					logger.info("Getting RSP  customers from : " + loginUser.getSpDbName() +" for "+ loginUser.getUsername());
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

	@RequestMapping(value = "/customers/tickets/{spcode}/{custDBName}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getCustomerTickets(final HttpSession session,
			@PathVariable(value = "spcode") String spcode,
			@PathVariable(value = "custDBName") String custDBName) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		
		if (loginUser != null) {
			try {
				session.setAttribute("ticketsBy", "CUSTOMER");
				String spCode = (String) session.getAttribute("usercode");
				if(!StringUtils.isEmpty(spCode)){
					List<TicketVO> ticketList = serviceProviderService.getCustomerTickets(spcode,custDBName, loginUser);
					if(!ticketList.isEmpty()){
						response.setResponseType(custDBName);
						response.setStatusCode(200);
						//session.setAttribute("selectedCustomerDB", custDBName);
						response.setObject(ticketList);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
					else{
						response.setStatusCode(404);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
					}
				}else{
					response.setStatusCode(404);
					response.setMessage("Registered Service Provider Code is missing");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/list/tickets/{custDBName}/{ticketsBy}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getRSPCreatedTickets(final HttpSession session,	
			@PathVariable(value = "custDBName") String custDBName, @PathVariable(value = "ticketsBy") String ticketsBy) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		
		if (loginUser != null) {
			try {
				session.setAttribute("ticketsBy", ticketsBy);
				String spCode = (String) session.getAttribute("usercode");
				if(!StringUtils.isEmpty(spCode)){
					loginUser.getCompany().setCompanyCode(spCode);
				}
				List<TicketVO> ticketList = ticketSerice.getTicketsForSP(loginUser, ticketsBy, custDBName);
				if(!ticketList.isEmpty()){
					response.setResponseType(custDBName);
					response.setStatusCode(200);
					response.setObject(ticketList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
				else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/regions", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllRegions(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		List<Region> regions = null;
		if (loginUser != null) {
				try {
				regions = rspManagedService.getRegionList(loginUser);
				if(!regions.isEmpty()){
					response.setStatusCode(200);
					response.setObject(regions);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Exception getting region list", e);
			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/country/region/{regionId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listCountryByRegions(final HttpSession session, @PathVariable(value = "regionId") Long regionId) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		List<Country> countryList = null;
		if (loginUser != null) {
				try {
					countryList = rspManagedService.getCountryList(loginUser, regionId);
				if(!countryList.isEmpty()){
					response.setStatusCode(200);
					response.setObject(countryList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Exception getting country list", e);
			}
		}
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/save/ext/customer", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> saveExternalCustomer(final Locale locale, final ModelMap model,
			@RequestBody final RSPExternalCustomerVO externalCustomerVO, final HttpSession session) {
		logger.info("Inside ServiceProviderCompanyController .. saveExternalCustomer");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RSPExternalCustomerVO savedExtCustomer=null;
		if(loginUser!=null){
			try {

				logger.info("Save External ServiceProvider : "+ externalCustomerVO);
				savedExtCustomer = rspManagedService.saveExternalCustomer(externalCustomerVO,loginUser);
				if(savedExtCustomer.getStatus()==200){
					response.setStatusCode(200);
					response.setObject(savedExtCustomer);
					response.setMessage("External Customer saved successfully.");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					if(response.getStatusCode()==200){/*
						final RSPExternalCustomerVO savedSP = savedExtCustomer;
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
					*/}
				}
				else if (savedExtCustomer.getStatus()==202){
					response.setStatusCode(200);
					response.setObject(savedExtCustomer);
					response.setMessage("External Customer updated successfully.");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
				else{
					response.setStatusCode(204);
					response.setMessage("Duplicate Customer found ");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.EXPECTATION_FAILED);
				}
			} catch (Exception e) {
					response.setStatusCode(500);
					if(e.getLocalizedMessage().contains("Duplicate")){
						logger.info("Duplicate Customer found ");
						response.setStatusCode(204);
						response.setMessage("Duplicate Customer found ");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.EXPECTATION_FAILED);
					}else{
						logger.info("Error occured while saving  external customer", e);
						response.setMessage("Error occured while saving  external customer ");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
					}
			}
			

			
		}
		else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit ServiceProviderCompanyController .. saveExternalCustomer");
		return responseEntity;
	}
	
	@RequestMapping(value = "/ext/customer/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getExternalCustomers(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		List<RSPExternalCustomerVO> customerList = null;
		if (loginUser != null) {
				try {
					loginUser.setDbName(loginUser.getSpDbName());
					customerList = rspManagedService.getExternalCustomers(loginUser);
				if(!customerList.isEmpty()){
					response.setStatusCode(200);
					response.setObject(customerList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Exception getting customerList", e);
			}
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/ext/customer/sla/list/{extCustId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getExternalCustomerSLA(final HttpSession session, @PathVariable(value="extCustId") Long extCustId) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		List<RSPExternalSLADetailVO> customerSLAList = null;
		if (loginUser != null) {
				try {
					customerSLAList = rspManagedService.getExternalCustomerSLA(loginUser, extCustId);
				if(!customerSLAList.isEmpty()){
					response.setStatusCode(200);
					response.setObject(customerSLAList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}else{
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.error("Exception getting customerList", e);
			}
		}
		return responseEntity;
	}
	

	@RequestMapping(value = "/update/ext/customer/sla", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> updateExtCustSLA(final Locale locale, final ModelMap model,
			@RequestBody final RSPExternalCustomerVO externalCustomerVO, final HttpSession session) {
		logger.info("Inside ServiceProviderCompanyController .. updateExtCustSLA");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RSPExternalCustomerVO savedExtCustomer=null;
		if(loginUser!=null){
			try {

				logger.info("Update External ServiceProvider SLA: "+ externalCustomerVO);
				savedExtCustomer = rspManagedService.updateExtCustSLA(externalCustomerVO,loginUser);
				if(savedExtCustomer.getStatus()==200){
					response.setStatusCode(200);
					response.setObject(savedExtCustomer);
					response.setMessage("SLA Details updated for customer : "+ externalCustomerVO.getCustomerName());
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}
				else{
					response.setStatusCode(204);
					response.setMessage("Unable to update the sla details");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.EXPECTATION_FAILED);
				}
			} catch (Exception e) {
					response.setStatusCode(500);
						logger.info("Error occured while saving  external customer", e);
						response.setMessage("Error occured while saving  external customer ");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			

			
		}
		else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit ServiceProviderCompanyController .. updateExtCustSLA");
		return responseEntity;
	}
	
	@RequestMapping(value = "/ext/customer/incident/list/{extCustId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getExternalCustomerIncidents(final HttpSession session, @PathVariable(value="extCustId") Long extCustId) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		List<TicketVO> ticketVOList = null;
		if (loginUser != null) {
				try {
					session.setAttribute("ticketsBy", "EXTCUST");
					ticketVOList = rspManagedService.getExternalCustomerIncidents(loginUser, extCustId);
					if(!ticketVOList.isEmpty()){
						response.setStatusCode(200);
						response.setObject(ticketVOList);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}else{
						response.setStatusCode(404);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
					}
				} catch (Exception e) {
					e.printStackTrace();
					response.setStatusCode(500);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
					logger.error("Exception getting customerList", e);
				}
		}
		return responseEntity;
	}
}
