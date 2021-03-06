/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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

import com.pms.app.constants.TicketUpdateType;
import com.pms.app.constants.UserType;
import com.pms.app.exception.PMSTechnicalException;
import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.IncidentImageVO;
import com.pms.app.view.vo.IncidentTask;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.jpa.entities.UserModel;
import com.pms.web.service.EmailService;
import com.pms.web.service.TicketService;
import com.pms.web.service.UserService;
import com.pms.web.util.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/incident")
@Controller
@Api(value = "Incident Service")
public class IncidentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);
	@Autowired
	private TicketService ticketSerice;
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				model.put("user", loginUser);
				if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
					return "redirect:/user/profile";
				} else {
					return "incident.details";
				}
			} else {
				return "redirect:/login";
			}
		} catch (PMSTechnicalException e) {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/details/create", method = RequestMethod.GET)
	public String incidentDetailsCreate(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				model.put("user", loginUser);
				if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
					return "redirect:/user/profile";
				} else {
					model.put("mode", "NEW");
					session.setAttribute("imageList", null);
					AssetVO selectedAssetVO = (AssetVO) session.getAttribute("assetVO");
					model.put("assetVO", selectedAssetVO);
					return "incident.create";
				}
			} else {
				return "redirect:/login";
			}
		} catch (PMSTechnicalException e) {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/create/page", method = RequestMethod.GET)
	public String incidentCreatePage(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				model.put("user", loginUser);
				if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
					return "redirect:/user/profile";
				} else {
					model.put("mode", "NEW");
					model.put("assetVO", null);
					session.setAttribute("imageList", null);
					return "incident.create";
				}
			} else {
				return "redirect:/login";
			}
		} catch (PMSTechnicalException e) {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/details/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser.getSysPassword() != null) {
				model.put("user", loginUser);
				if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
					return "redirect:/user/profile";
				} else {
					model.put("mode", "EDIT");
					return "incident.update";
				}
			} else {
				return "redirect:/login";
			}
		} catch (PMSTechnicalException e) {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/details/view", method = RequestMethod.GET)
	public String incidentDetailsView(final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		try {
			if (loginUser != null) {
				model.put("user", loginUser);
				if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
					return "redirect:/user/profile";
				} else {
					model.put("mode", "EDIT");
					return "incident.view";
				}
			} else {
				return "redirect:/login";
			}
		} catch (PMSTechnicalException e) {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/list/{assignedTo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> listAllTickets(final HttpSession session,
			@PathVariable(value = "assignedTo") final String assignedTo) {
		List<TicketVO> tickets = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				tickets = ticketSerice.getAllCustomerTickets(loginUser, assignedTo);
				if (tickets.isEmpty()) {
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
					return responseEntity;
				} else {
					response.setStatusCode(200);
					response.setObject(tickets);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			logger.info("Exception in getting ticket list response", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/ticketcategories", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<TicketCategory>> listAllTicketCategories(HttpSession session) {
		List<TicketCategory> categories = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				categories = ticketSerice.getTicketCategories(loginUser);
				if (categories.isEmpty()) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
					// You many decide to return HttpStatus.NOT_FOUND
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<TicketCategory>>(categories, HttpStatus.OK);
	}

	@RequestMapping(value = "/status/{category}/{custdb}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Status>> listAllOpenTickets(HttpSession session,
			@PathVariable(value = "category") final String category,
			@PathVariable(value = "custdb") final String custdb) {
		List<Status> statusList = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				if (!custdb.equalsIgnoreCase("NA")) {
					loginUser.setDbName(custdb);
				}
				statusList = ticketSerice.getStatusByCategory(loginUser, category);
				if (statusList.isEmpty()) {
					return new ResponseEntity(HttpStatus.NO_CONTENT);
					// You many decide to return HttpStatus.NOT_FOUND
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<Status>>(statusList, HttpStatus.OK);
	}

	@RequestMapping(value = "/priority/sla/{spId}/{categoryId}/{sptype}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getPriorityAndSLA(@PathVariable(value = "spId") Long spId,
			@PathVariable(value = "categoryId") Long categoryId, @PathVariable(value = "sptype") String sptype,
			final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				TicketPrioritySLAVO ticketPrioritySLAVO = ticketSerice.getTicketPriority(spId, categoryId, sptype,loginUser);
				if (ticketPrioritySLAVO.getPriorityId() != null) {
					response.setStatusCode(200);
					response.setObject(ticketPrioritySLAVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while getting Priority");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);

			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	@ApiOperation(value = "Create Incident", notes = "Creation a new Incident", response = IncidentController.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Incident Created successfully") })
	public ResponseEntity<RestResponse> createNewIncident(final Locale locale, final ModelMap model,
			@RequestBody final TicketVO ticketVO, final HttpSession session) {
		logger.info("Inside IncidentController .. createNewIncident");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		TicketVO savedTicketVO = null;
		RestResponse emailResponse = null;
		if (loginUser != null) {
			try {
				logger.info("TicektVO : " + ticketVO);
				String selectedCustLocation = (String) session.getAttribute("custLocation");
				if (!StringUtils.isEmpty(selectedCustLocation)) {
					loginUser.setDbName(selectedCustLocation);
				}
				logger.info("Saving ticket to : " + loginUser.getDbName() + " created by : " + loginUser.getUserType());
				savedTicketVO = ticketSerice.saveOrUpdate(ticketVO, loginUser);
				if (ticketVO.getMode().equalsIgnoreCase("NEW")
						&& savedTicketVO.getMessage().equalsIgnoreCase("CREATED")) {
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("New Incident created successfully");
					String loginUserDB = (String) session.getAttribute("loggedInUserDB");
				//	loginUser.setDbName(loginUserDB);
					//session.setAttribute("loginUser", loginUser);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else if (ticketVO.getMode().equalsIgnoreCase("UPDATE")
						&& savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")) {
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("Incident updated successfully");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else if (ticketVO.getMode().equalsIgnoreCase("IMAGEUPLOAD")
						&& savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")) {
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("Incident images uploaded successfully");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while creating an incident");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}

			if (response.getStatusCode() == 200 && savedTicketVO.getMessage().equalsIgnoreCase("CREATED")) {
				savedTicketVO.setCreatedUser(loginUser.getFirstName() + " " + loginUser.getLastName());
				savedTicketVO.setCreatedBy(loginUser.getUsername());
				final TicketVO ticketVOs = savedTicketVO;
				TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
				theExecutor.execute(new Runnable() {
					@Override
					public void run() {
						logger.info("Email thread started : " + Thread.currentThread().getName());
						try {
							emailService.successTicketCreationSPEmail(ticketVOs, "CREATED",
									loginUser.getCompany().getCompanyName());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		logger.info("Exit SiteController .. createNewIncident");
		return responseEntity;
	}

	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> imageUpload(final ModelMap model, HttpServletRequest request,
			final HttpSession session, @RequestBody IncidentImageVO incidentImageVO) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		Map<Integer, IncidentImageVO> imageList = (Map<Integer, IncidentImageVO>) session.getAttribute("imageList");
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);

		if (loginUser != null) {

			if (imageList == null) {
				imageList = new HashMap<Integer, IncidentImageVO>();
				response.setCalculatedVal(incidentImageVO.getFileSize());
				imageList.put(incidentImageVO.getImgPos(), incidentImageVO);
				session.setAttribute("imageList", imageList);
			} else {
				imageList = (Map<Integer, IncidentImageVO>) session.getAttribute("imageList");
				for (Map.Entry<Integer, IncidentImageVO> imageEntry : imageList.entrySet()) {
					if (imageEntry.getKey().equals(incidentImageVO.getImgPos())) {
						IncidentImageVO imageVO = imageEntry.getValue();
						imageVO.setBase64ImageString(incidentImageVO.getBase64ImageString());
						imageVO.setFile(incidentImageVO.getFile());
						imageVO.setFileExtension(incidentImageVO.getFileExtension());
						imageVO.setFileName(incidentImageVO.getFileName());
						imageVO.setIncidentImgId(incidentImageVO.getIncidentImgId());
						imageVO.setFileSize(incidentImageVO.getFileSize());
						imageList.put(incidentImageVO.getImgPos(), incidentImageVO);
						break;
					}
				}
				imageList.put(incidentImageVO.getImgPos(), incidentImageVO);
				session.setAttribute("imageList", imageList);
			}

			response.setObject(imageList);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);

		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/image/remove", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> imageDelete(final ModelMap model, HttpServletRequest request,
			final HttpSession session, @RequestBody IncidentImageVO incidentImageVO) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		Map<Integer, IncidentImageVO> imageList = (Map<Integer, IncidentImageVO>) session.getAttribute("imageList");
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			imageList.remove(incidentImageVO.getImgPos(), incidentImageVO);
			session.setAttribute("imageList", imageList);
			response.setObject(imageList);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/ticket/{ticketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getSelectedTicket(@PathVariable(value = "ticketId") Long ticketId,
			HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				TicketVO selectedTicket = null;
				if(loginUser.getUserType().equalsIgnoreCase("SP")){
					String selectedCustomerDB = (String) session.getAttribute("selectedCustomerDB");
					loginUser.setDbName(selectedCustomerDB);
					String ticketsOf = (String) session.getAttribute("ticketsBy");
					if(ticketsOf.equalsIgnoreCase("CUSTOMER")){
						selectedTicket  = ticketSerice.getSelectedTicket(ticketId, loginUser);
					}
					else if(ticketsOf.equalsIgnoreCase("RSP")){
						selectedTicket  = ticketSerice.getRSPCreatedSelectedTicket(ticketId, loginUser, ticketsOf);
					}
					else if(ticketsOf.equalsIgnoreCase("EXTCUST")){
						loginUser.setDbName(loginUser.getSpDbName());
						selectedTicket  = ticketSerice.getRSPCreatedSelectedTicket(ticketId, loginUser, ticketsOf);
					}
				}
				else{
					selectedTicket  = ticketSerice.getSelectedTicket(ticketId, loginUser);
				}
				if (selectedTicket.getTicketId() != null) {
					response.setStatusCode(200);
					response.setObject(selectedTicket);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while getting ticket");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);

			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/selected/ticket", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> getSelectedTicket(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @RequestBody TicketVO ticketVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				
				if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
					logger.info("Loggedin user is Customer pointing to its DB");
				}
				else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && !ticketVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
					//loginUser.setSpDbName(loginUser.getDbName());
					logger.info("Loggedin user is RSP pointing to Customer DB : " + ticketVO.getCustomerDB() );
					loginUser.setDbName(ticketVO.getCustomerDB());
					session.setAttribute("selectedCustomerDB", ticketVO.getCustomerDB());
				}
				else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType()) && ticketVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
					logger.info("Loggedin user is RSP pointing to own DB : " + loginUser.getDbName() );
				}
				/*if (loginUser.getUserType().equalsIgnoreCase("SP")) {
					loginUser.setDbName(ticketVO.getCustomerDB());
					session.setAttribute("selectedCustomerDB", ticketVO.getCustomerDB());
				}*/
				//TicketVO selectedTicketVO = ticketSerice.getSelectedTicket(ticketVO.getTicketId(), loginUser);
				session.setAttribute("selectedTicket", ticketVO);
				response.setStatusCode(200);
				response.setObject(ticketVO);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception in getting ticket response", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/session/ticket/update", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> incidentSessionTicket(ModelMap model, HttpServletRequest request,
			HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			model.put("user", loginUser);
			TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
			try {
				if (selectedTicketVO != null) {
					if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
						logger.info("Getting selected ticket from customer DB");
						selectedTicketVO = ticketSerice.getSelectedTicket(selectedTicketVO.getTicketId(), loginUser);
						if (selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_FOR_RSP_ASSIGNED_TICKETS.getUpdateType())){
							logger.info("Getting suggested tickets from RSP TICKETS -- table");
							List<TicketVO> rspSuggestedTickets = ticketSerice.getSuggestedTicketForAsset(loginUser,
									selectedTicketVO.getAssetId());
							if (!rspSuggestedTickets.isEmpty()) {
								response.setObject2(rspSuggestedTickets);
							}
						}
						selectedTicketVO = getSelectedTicketEscalations(loginUser, selectedTicketVO);
						List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),	loginUser, selectedTicketVO.getTicketAssignedType());
						selectedTicketVO.setFinancialList(finacialsList);
					}
					else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_EXTSP.getUserType())){
						logger.info("Getting selected ticket from customer DB");
						selectedTicketVO = ticketSerice.getSelectedTicket(selectedTicketVO.getTicketId(), loginUser);
						selectedTicketVO = getSelectedTicketEscalations(loginUser, selectedTicketVO);
						List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),	loginUser, selectedTicketVO.getTicketAssignedType());
						selectedTicketVO.setFinancialList(finacialsList);
					}
					else if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
						String custDBName = (String) session.getAttribute("selectedCustomerDB");
						if (!StringUtils.isEmpty(custDBName)){
						logger.info("Getting selected ticket from RSP DB");
							if (selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())) {
								loginUser.setDbName(custDBName);
								selectedTicketVO = ticketSerice.getRSPCreatedSelectedTicket(selectedTicketVO.getTicketId(),	loginUser, TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType());
								selectedTicketVO = getSelectedTicketEscalations(loginUser, selectedTicketVO);
								selectedTicketVO.setTicketAssignedType(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType());
								List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),	loginUser, selectedTicketVO.getTicketAssignedType());
								selectedTicketVO.setFinancialList(finacialsList);
							} else if (selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())) {
								loginUser.setDbName(custDBName);
								selectedTicketVO = ticketSerice.getSelectedTicket(selectedTicketVO.getTicketId(),loginUser);
								selectedTicketVO = getSelectedTicketEscalations(loginUser, selectedTicketVO);
								selectedTicketVO.setTicketAssignedType(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType());
								List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),	loginUser, selectedTicketVO.getTicketAssignedType());
								selectedTicketVO.setFinancialList(finacialsList);
							}
						}else {
						logger.info("Getting selected ticket from RSP External Customer ");
							if (selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())) {
								selectedTicketVO = ticketSerice.getRSPCreatedSelectedTicket(selectedTicketVO.getTicketId(),	loginUser, TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType());
								UserModel extUser = userService.getTicketCreationUser(loginUser, selectedTicketVO.getRaisedBy());
								if(extUser!=null){
									selectedTicketVO.setCreatedUser(extUser.getFirstName() + " " +  extUser.getLastName());
									selectedTicketVO.setRaisedUser(extUser.getPhoneNo());
								}
								//selectedTicketVO = getSelectedTicketEscalations(loginUser, selectedTicketVO);
								selectedTicketVO.setTicketAssignedType(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType());
								//List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),	loginUser, selectedTicketVO.getTicketAssignedType());
								//selectedTicketVO.setFinancialList(finacialsList);
							} 
						}
					}
					
					session.setAttribute("selectedTicket", selectedTicketVO);
					response.setStatusCode(200);
					response.setObject(selectedTicketVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
				logger.info("Exception in getting ticket response", e);
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	private TicketVO getSelectedTicketEscalations(LoginUser loginUser, TicketVO selectedTicketVO) throws Exception {
		List<EscalationLevelVO> escalationLevelVOs = selectedTicketVO.getEscalationLevelList();
		if (selectedTicketVO.getEscalationLevelList().isEmpty()) {
			logger.info("No Escalation Levels ");
		} else {
			List<EscalationLevelVO> finalEscalationList = new ArrayList<EscalationLevelVO>();
			for (EscalationLevelVO escalationVO : escalationLevelVOs) {
				TicketEscalationVO ticketEscalationVO = ticketSerice.getEscalationStatus(
						selectedTicketVO.getTicketId(), escalationVO.getEscId(), loginUser,
						selectedTicketVO.getTicketAssignedType());
				EscalationLevelVO tempEscalationVO = new EscalationLevelVO();
				if (ticketEscalationVO.getCustEscId() != null) {
					tempEscalationVO.setStatus("Escalated");
				}
				tempEscalationVO.setEscId(escalationVO.getEscId());
				tempEscalationVO.setEscalationEmail(escalationVO.getEscalationEmail());
				tempEscalationVO.setEscalationLevel(escalationVO.getEscalationLevel());
				tempEscalationVO.setServiceProdviderId(escalationVO.getServiceProdviderId());
				tempEscalationVO.setEscalationPerson(escalationVO.getEscalationPerson());
				finalEscalationList.add(tempEscalationVO);
			}

			selectedTicketVO.setEscalationLevelList(finalEscalationList);
		}
		return selectedTicketVO;
	}

	@RequestMapping(value = "/file/attachments/{ticketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getTicketAttachments(@PathVariable(value = "ticketId") Long ticketId,
			HttpSession session) throws Exception {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
			String custDBName = (String) session.getAttribute("selectedTicketDB");
			List<TicketAttachment> fileAttachments = new ArrayList<TicketAttachment>();
			if (StringUtils.isEmpty(custDBName)) {
				fileAttachments = ticketSerice.findByTicketId(ticketId, loginUser, selectedTicketVO);
			} else {
				loginUser.setDbName(custDBName);
				fileAttachments = ticketSerice.findByTicketId(ticketId, loginUser, selectedTicketVO);
			}
			if (fileAttachments == null) {
				logger.info("Not Ticket Attachment for " + ticketId);
			} else {
				if (fileAttachments.isEmpty()) {
					logger.info("Not Ticket Attachment for " + ticketId);
				} else {
					List<TicketAttachment> fileAttachmentList = new ArrayList<TicketAttachment>();
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					for (TicketAttachment ticketAttachment : fileAttachments) {
						ticketAttachment.setCreatedDate(formatter.format(ticketAttachment.getCreatedOn()));
						fileAttachmentList.add(ticketAttachment);
					}
					selectedTicketVO.setAttachments(fileAttachmentList);
					response.setStatusCode(200);
					response.setObject(selectedTicketVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}
		}
		return responseEntity;
	}

	@RequestMapping(value = "/comment/save", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> comment(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @RequestBody TicketCommentVO ticketCommentVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				TicketCommentVO savedComment = ticketSerice.saveTicketComment(ticketCommentVO, loginUser, selectedTicketVO.getTicketAssignedType());
				if (savedComment.getCommentId() != null) {
					response.setStatusCode(200);
					response.setObject(savedComment);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while saving comment", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/comment/list/{ticketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> commentLint(final ModelMap model,
			@PathVariable(value = "ticketId") Long ticketId, final HttpServletRequest request,
			final HttpSession session) throws Exception {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
			List<TicketCommentVO> selectedTicketComments = ticketSerice.getTicketComments(ticketId, loginUser, selectedTicketVO.getTicketAssignedType());
			if (!selectedTicketComments.isEmpty()) {
				response.setStatusCode(200);
				response.setObject(selectedTicketComments);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} else {
				response.setStatusCode(404);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/history/{ticketnumber}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> incidentSessionTicket(final ModelMap model,
			@PathVariable(value = "ticketnumber") String ticketnumber, final HttpServletRequest request,
			final HttpSession session) throws Exception {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			model.put("user", loginUser);
			List<TicketHistoryVO> selectedTicketHistory = ticketSerice.getTicketHistory(ticketnumber, loginUser);
			if (!selectedTicketHistory.isEmpty()) {
				response.setStatusCode(200);
				response.setObject(selectedTicketHistory);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} else {
				response.setStatusCode(404);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}
		} else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}

	@RequestMapping(value = "/linkedticket/{ticketId}/{ticketNumber}/{linkedticket}/{spTicketMapType}/{spAssignedTo}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> linked(final ModelMap model, HttpServletRequest request, HttpSession session,
			@PathVariable(value = "ticketId") Long ticketId, @PathVariable(value = "ticketNumber") String ticketNumber,
			@PathVariable(value = "linkedticket") String linkedTicket,
			@PathVariable(value = "spTicketMapType") String spTicketMapType,
			@PathVariable(value = "spAssignedTo") Long rspAssginedTo) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				
				List<CustomerSPLinkedTicketVO> spLinkedTickets = ticketSerice.getAllLinkedTickets(ticketId, loginUser);
				boolean isSPTicketLinked = false;
				if(spLinkedTickets.isEmpty()){
					isSPTicketLinked = true;
				}else{
					for(CustomerSPLinkedTicketVO spLinkedTicket: spLinkedTickets){
						if(spLinkedTicket.getSpLinkedTicket().equalsIgnoreCase(linkedTicket)){
							isSPTicketLinked = false;
							break;
						}else{
							isSPTicketLinked = true;
						}
					}
				}	
				if(isSPTicketLinked){
					CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveLinkedTicket(ticketId, ticketNumber,linkedTicket, loginUser, spTicketMapType, rspAssginedTo);
					if (savedTicketLinked.getId() != null) {
						response.setStatusCode(200);
						response.setObject(savedTicketLinked);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
				}else{
					response.setStatusCode(204);
					response.setMessage("Ticket number is already linked.");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}

		return responseEntity;
	}
	

	@RequestMapping(value = "/linkedticket/list/{custTicketId}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> listLinkedTickets(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @PathVariable(value = "custTicketId") Long custTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				List<CustomerSPLinkedTicketVO> linkedTickets = ticketSerice.getAllLinkedTickets(custTicketId,
						loginUser);
				response.setStatusCode(200);
				TicketVO ticketVO = (TicketVO) session.getAttribute("selectedTicket");
				ticketVO.getLinkedTickets().clear();
				ticketVO.setLinkedTickets(linkedTickets);
				session.setAttribute("selectedTicket", ticketVO);
				response.setObject(ticketVO);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while getting listLinkedTickets", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/linkedticket/status/{linkedTicket}/{status}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> changeStatusLinkedTicket(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @PathVariable(value = "linkedTicket") Long linkedTicket) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				int statusChanged = ticketSerice.changeLinkedTicketStatus(linkedTicket, loginUser);
				if (statusChanged == 1) {
					response.setStatusCode(200);
					TicketVO ticketVO = (TicketVO) session.getAttribute("selectedTicket");
					ticketVO.getLinkedTickets().clear();
					List<CustomerSPLinkedTicketVO> linkedTickets = ticketSerice.getAllLinkedTickets(ticketVO.getTicketId(),
							loginUser);
					ticketVO.setLinkedTickets(linkedTickets);
					session.setAttribute("selectedTicket", ticketVO);
					response.setObject(ticketVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while getting listLinkedTickets", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/linkedticket/delete/{linkedTicketId}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> deleteLinkedTicket(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @PathVariable(value = "linkedTicketId") Long linkedTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO sessionTicket = (TicketVO) session.getAttribute("selectedTicket");
				String custDBName = (String) session.getAttribute("selectedCustomerDB");
				if (StringUtils.isNotEmpty(custDBName)) {
					loginUser.setDbName(custDBName);
				}
				int deletedTicket = ticketSerice.deleteLinkedTicket(linkedTicketId, loginUser);
				if (deletedTicket > 0) {
					response.setStatusCode(200);
					response.setMessage("Linked Ticket has been deleted successfully.");
					List<CustomerSPLinkedTicketVO> customerLinkedTickets = sessionTicket.getLinkedTickets();
					boolean isAvailable = false;
					CustomerSPLinkedTicketVO foundTicket = null;
					for (CustomerSPLinkedTicketVO custTicket : customerLinkedTickets) {
						if (custTicket.getId().equals(linkedTicketId)) {
							foundTicket = custTicket;
							isAvailable = true;
							break;
						}
					}
					if (isAvailable) {
						isAvailable = customerLinkedTickets.remove(foundTicket);
					}
					else {
						response.setObject(customerLinkedTickets);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while deleting", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/escalate", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> escalate(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @RequestBody TicketEscalationVO ticketEscalationLevels) {
		logger.info("Inside IncidentController .. escalate");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		TicketEscalationVO savedTicketEscalation = null;
		LoginUser loginUser = getCurrentLoggedinUser(session);
		try {
			if (loginUser != null) {
				savedTicketEscalation = ticketSerice.saveTicketEscalations(ticketEscalationLevels, loginUser);
				if (savedTicketEscalation.getCustEscId() != null) {
					response.setStatusCode(200);
					response.setObject(savedTicketEscalation);
					response.setMessage("Incident " + savedTicketEscalation.getTicketNumber() + "escalated to "
							+ savedTicketEscalation.getEscLevelDesc());
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}
		EscalationLevelVO spEscalationLevel = null;
		if (response.getStatusCode() == 200) {
			List<String> escCCMailList = new ArrayList<String>(0);
			List<EscalationLevelVO> escalationLevelVOs = ticketEscalationLevels.getTicketData().getEscalationLevelList();

			if (escalationLevelVOs.size() == 0) {
				logger.info("No escalation list available");
			} else {
				logger.info("Escalation Level list : " + escalationLevelVOs.size());
				try {
					spEscalationLevel = ticketSerice.getSPEscalationLevels(savedTicketEscalation.getEscId(), loginUser,
							ticketEscalationLevels.getTicketData().getTicketAssignedType());
					for (EscalationLevelVO escalatedTicket : escalationLevelVOs) {
						if (StringUtils.isNotBlank(escalatedTicket.getStatus()) 
								&& !escalatedTicket.getEscalationEmail().equalsIgnoreCase(spEscalationLevel.getEscalationEmail())) {
							escCCMailList.add(escalatedTicket.getEscalationEmail());
						}
					}

				} catch (Exception e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}

				logger.info("escCCMailList :" + escCCMailList);
			}
			if(spEscalationLevel.getEscId()!=null){
			final EscalationLevelVO spEscLevel = spEscalationLevel;
			final TicketEscalationVO savedTicketEsc = savedTicketEscalation;
			TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
			TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
			theExecutor.execute(new Runnable() {

				@Override
				public void run() {
					logger.info("Email thread started : " + Thread.currentThread().getName());
					if (spEscLevel != null) {
						String ccEscList = "";
						if (!escCCMailList.isEmpty()) {
							ccEscList = StringUtils.join(escCCMailList, ',');
							logger.info("Escalation CC List : " + ccEscList);
							try {
								emailService.successEscalationLevel(selectedTicketVO, spEscLevel,
										ccEscList, savedTicketEsc.getEscLevelDesc());
							} catch (Exception e) {
								logger.info("Exception while sending email", e);
							}
						} else {
							logger.info("Escalation To List : " + spEscLevel.getEscalationEmail());
							logger.info("Escalation CC list is empty");
							try {
								emailService.successEscalationLevel(selectedTicketVO, spEscLevel,
										ccEscList, savedTicketEsc.getEscLevelDesc());
							} catch (Exception e) {
								logger.info("Exception while sending email", e);
							}

						}

					}

				}
			});
			}
		}

		logger.info("Exit IncidentController .. escalate");
		return responseEntity;
	}

	@RequestMapping(value = "/updateFinancial", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> updateFinacials(final HttpSession session, @RequestBody final String finVO) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		LoginUser loginUser = getCurrentLoggedinUser(session);
		try {
			if (loginUser != null) {
				List<FinUpdReqBodyVO> finVOList = getFinancialVOList(finVO);
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				List<Financials> financials = ticketSerice.saveFinancials(finVOList, loginUser, selectedTicketVO.getTicketAssignedType());
				if (!financials.isEmpty()) {
					selectedTicketVO.setFinancialList(financials);
					response.setStatusCode(200);
					response.setObject(financials);
					response.setMessage("Cost Item(s) Updated successfully");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(204);
					response.setMessage("Unable to update the cost Item(s) for selected ticket.");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.CONFLICT);
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			logger.info("Exception while updating financial data.", e);
		}
		return responseEntity;
	}

	private List<FinUpdReqBodyVO> getFinancialVOList(final String finVO) {
		JSONArray jsonArray = new JSONArray(finVO);
		List<FinUpdReqBodyVO> finVOList = new ArrayList<FinUpdReqBodyVO>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject explrObject = jsonArray.getJSONObject(i);
			Long costId = null;
			if (explrObject.has("costId")) {
				if (!StringUtils.isEmpty(String.valueOf(explrObject.get("costId")))) {
					costId = Long.parseLong(String.valueOf(explrObject.get("costId")));
				}
			}
			FinUpdReqBodyVO fins = new FinUpdReqBodyVO(costId, explrObject.getLong("ticketID"),
					explrObject.getString("costName"), new BigDecimal(explrObject.getDouble("cost")),
					explrObject.getString("chargeBack"), explrObject.getString("billable"));
			if (explrObject.has("isDeleteCheck")) {
				if (explrObject.getBoolean("isDeleteCheck")) {
					fins.setDeleteCheck(explrObject.getBoolean("isDeleteCheck"));
				}
			}
			if (explrObject.has("isEdited")) {
				if (explrObject.getBoolean("isEdited")) {
					fins.setEdited(explrObject.getBoolean("isEdited"));
				}
			}
			finVOList.add(fins);
		}
		return finVOList;
	}

	@RequestMapping(value = "/deleteFinancial", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> deleteFinacials(@RequestBody final String finVO, final HttpSession session) {
		RestResponse apr = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		try {
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				JSONArray jsonArray = new JSONArray(finVO);
				int successCount = 0;
				int failureCount = 0;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject explrObject = jsonArray.getJSONObject(i);
					if (explrObject.getBoolean("isDeleteCheck") == true) {
						boolean resp;
						resp = ticketSerice.deleteFinanceCostById(explrObject.getLong("costId"), loginUser, selectedTicketVO.getTicketAssignedType());
						if (resp == true) {
							successCount++;
						} else {
							failureCount++;
						}
					}
				}
				if (successCount > 0) {
					apr.setStatusCode(200);
					apr.setMessage("Selected Cost Items deleted successfully");
					responseEntity = new ResponseEntity<RestResponse>(apr, HttpStatus.OK);
				} else if (failureCount > 0) {
					apr.setStatusCode(500);
					apr.setMessage("Selected Cost Items were not deleted successfully, please try again");
					responseEntity = new ResponseEntity<RestResponse>(apr, HttpStatus.EXPECTATION_FAILED);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			apr.setStatusCode(500);
			apr.setMessage("JSON Data Parsing Exception");
			responseEntity = new ResponseEntity<RestResponse>(apr, HttpStatus.EXPECTATION_FAILED);
		} catch (Exception e) {
			e.printStackTrace();
			apr.setStatusCode(500);
			apr.setMessage("Exception failed.Please try again");
			responseEntity = new ResponseEntity<RestResponse>(apr, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/suggestion/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getRSPSuggestionList(HttpSession session) throws Exception {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				String custDBName = (String) session.getAttribute("selectedCustomerDB");
				if (StringUtils.isNotEmpty(custDBName)) {
					loginUser.setDbName(custDBName);
					List<TicketVO> rspReferenceTickets = ticketSerice.getRSPSuggestedTicketForAsset(loginUser,
							selectedTicketVO.getAssetId(), selectedTicketVO);
					List<TicketVO> customerReferenceTickets = ticketSerice.getCustomerSuggestedTicketForAsset(loginUser,
							selectedTicketVO.getAssetId());
					if (!rspReferenceTickets.isEmpty()) {
						response.setObject(rspReferenceTickets);
					}
					if (!customerReferenceTickets.isEmpty()) {
						response.setObject2(customerReferenceTickets);
					}
					response.setStatusCode(200);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				} else {
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.EXPECTATION_FAILED);
				}

			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception failed.Please try again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	@RequestMapping(value = "/rsp/linkedticket/save/{parentTicketId}/{linkedTicketId}/{linkedTicketType}/{linkedTicketNumber}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> linked(final ModelMap model, HttpServletRequest request, HttpSession session,
			@PathVariable(value = "parentTicketId") Long parentTicketId, 
			@PathVariable(value = "linkedTicketId") Long linkedTicketId,
			@PathVariable(value = "linkedTicketType") String linkedTicketType,
			@PathVariable(value = "linkedTicketNumber") String linkedTicketNumber) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveRSPLinkedTicket(parentTicketId, linkedTicketId, linkedTicketType, linkedTicketNumber, loginUser);
				if (savedTicketLinked.getId() != null) {
					response.setStatusCode(200);
					response.setMessage("Ticket number "+ linkedTicketNumber +" linked successfully" );
					response.setObject(savedTicketLinked);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
				/*List<CustomerSPLinkedTicketVO> spLinkedTickets = ticketSerice.getRSPAllLinkedTickets(parentTicketId, loginUser, linkedTicketType);
				boolean isSPTicketLinked = false;
				if(spLinkedTickets.isEmpty()){
					isSPTicketLinked = true;
				}else{
					for(CustomerSPLinkedTicketVO spLinkedTicket: spLinkedTickets){
						if(spLinkedTicket.getSpLinkedTicket().equalsIgnoreCase(linkedTicketNumber)){
							isSPTicketLinked = false;
							break;
						}else{
							isSPTicketLinked = true;
						}
					}
				}	
				if(isSPTicketLinked){
					CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveRSPLinkedTicket(parentTicketId, linkedTicketId, linkedTicketType, loginUser);
					if (savedTicketLinked.getId() != null) {
						response.setStatusCode(200);
						response.setObject(savedTicketLinked);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
				}else{
					response.setStatusCode(204);
					response.setMessage("Ticket number is already linked.");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}*/
			} else {
				response.setStatusCode(204);
				response.setMessage("Ticket number is already linked.");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			logger.info("Exception while escalations", e);
		}

		return responseEntity;
	}

	@RequestMapping(value = "/rsp/linkedticket/list/{parentTicketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getRSPLinkedTickedtist(HttpSession session,@PathVariable(value = "parentTicketId") Long parentTicketId) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				String custDBName = (String) session.getAttribute("selectedCustomerDB");
				if (StringUtils.isNotEmpty(custDBName)) {
					loginUser.setDbName(custDBName);
					if(selectedTicketVO.getTicketId().equals(parentTicketId)){
						String linkedTicketType=null;
						if(selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_CUSTOMER_TICKET.getUpdateType())){
							linkedTicketType="CT";
						}
						if(selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_COMPANY_TICKET.getUpdateType())){
							linkedTicketType="SP";
						}
						List<CustomerSPLinkedTicketVO> rspLinkedTickets = ticketSerice.getRSPLinkedTickets(loginUser,parentTicketId,linkedTicketType);
						if(!rspLinkedTickets.isEmpty()){
							response.setStatusCode(200);
							response.setObject(rspLinkedTickets);
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
					}
				} else {
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.EXPECTATION_FAILED);
				}

			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception failed.Please try again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/rsp/incident/task/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> saveRspIncidentTask(HttpSession session,@RequestBody  IncidentTask incidentTask) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				if(selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
					if(selectedTicketVO.getTicketId().equals(incidentTask.getTicketId())){
						incidentTask.setTicketNumber(selectedTicketVO.getTicketNumber());
						IncidentTask savedIncidentTask = ticketSerice.saveRspTicketTask(incidentTask, loginUser, "EXTCUST");
						if(savedIncidentTask.getStatus()==200){
							response.setStatusCode(200);
							response.setObject(savedIncidentTask);
							response.setMessage("Incident Task has been saved succesfully");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
						else if(savedIncidentTask.getStatus()==202){
							response.setStatusCode(200);
							response.setObject(savedIncidentTask);
							response.setMessage("Incident Task has been updated succesfully");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
					}
				}else{
				String custDBName = (String) session.getAttribute("selectedCustomerDB");
				if (StringUtils.isNotEmpty(custDBName)) {
					loginUser.setDbName(custDBName);
					if(selectedTicketVO.getTicketId().equals(incidentTask.getTicketId())){
						incidentTask.setTicketNumber(selectedTicketVO.getTicketNumber());
						IncidentTask savedIncidentTask = ticketSerice.saveRspTicketTask(incidentTask, loginUser, "RSP");
						if(savedIncidentTask.getStatus()==200){
							response.setStatusCode(200);
							response.setObject(savedIncidentTask);
							response.setMessage("Incident Task has been saved succesfully");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
						else if(savedIncidentTask.getStatus()==202){
							response.setStatusCode(200);
							response.setObject(savedIncidentTask);
							response.setMessage("Incident Task has been updated succesfully");
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
					}
				} else {
					response.setStatusCode(204);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.EXPECTATION_FAILED);
				}
			 }
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception failed.Please try again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/rsp/task/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getRSPIncidentTaskList(HttpSession session)  {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				if(selectedTicketVO.getTicketAssignedType().equalsIgnoreCase(TicketUpdateType.UPDATE_BY_RSP_FOR_EXTCUST_TICKET.getUpdateType())){
					List<IncidentTask> incidentTaskList = ticketSerice.getIncidentTaskList(loginUser,selectedTicketVO.getTicketId(), selectedTicketVO);
					if(!incidentTaskList.isEmpty()){
						response.setStatusCode(200);
						response.setObject(incidentTaskList);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
				}else{
					String custDBName = (String) session.getAttribute("selectedCustomerDB");
					if (StringUtils.isNotEmpty(custDBName)) {
						loginUser.setDbName(custDBName);
						List<IncidentTask> incidentTaskList = ticketSerice.getIncidentTaskList(loginUser,selectedTicketVO.getTicketId(), selectedTicketVO);
						if(!incidentTaskList.isEmpty()){
							response.setStatusCode(200);
							response.setObject(incidentTaskList);
							responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
						}
					} else {
						response.setStatusCode(204);
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.EXPECTATION_FAILED);
					}
				}
			} else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		}catch (Exception e) {
			e.printStackTrace();
			response.setStatusCode(500);
			response.setMessage("Exception failed.Please try again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
}
