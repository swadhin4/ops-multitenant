/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import com.pms.app.view.vo.IncidentImageVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.web.service.EmailService;
import com.pms.web.service.TicketService;
import com.pms.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/incident")
@Controller
public class IncidentController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(IncidentController.class);
	@Autowired
	private TicketService ticketSerice;
	@Autowired
	private EmailService emailService;

	/*

	
	@Autowired
	private ServiceProviderService serviceProviderService;

	@Autowired
	private SPEscalationLevelRepo spEscalationRepo;

	@Autowired
	private TicketAttachmentRepo ticketAttachmentRepo;

	@Autowired
	private FinancialService finService;*/

	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
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
	}

	@RequestMapping(value = "/details/create", method = RequestMethod.GET)
	public String incidentDetailsCreate(final Locale locale, final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			model.put("user", loginUser);
			if (loginUser.getSysPassword().equalsIgnoreCase("YES")) {
				return "redirect:/user/profile";
			} else {
				model.put("mode", "NEW");
				session.setAttribute("imageList", null);
				return "incident.create";
			}
		} else {
			return "redirect:/login";
		}
	}

	
	
	@RequestMapping(value = "/details/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
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
	}

	@RequestMapping(value = "/details/view", method = RequestMethod.GET)
	public String incidentDetailsView(final ModelMap model, final HttpServletRequest request,
			final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
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
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> listAllTickets(HttpSession session) {
		List<TicketVO> tickets = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				tickets = ticketSerice.getAllCustomerTickets(loginUser);
				if (tickets.isEmpty()) {
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
					return responseEntity;
				} else {
					response.setStatusCode(200);
					response.setObject(tickets);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else {
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
	
	
	@RequestMapping(value = "/ticketcategories", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<TicketCategory>> listAllTicketCategories(HttpSession session) {
		List<TicketCategory> categories = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if(loginUser!=null){
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
	

	@RequestMapping(value = "/status/{category}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<Status>> listAllOpenTickets(HttpSession session, @PathVariable (value="category") final String category) {
		List<Status> statusList = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if(loginUser!=null){
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

	
	@RequestMapping(value = "/priority/sla/{spId}/{categoryId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getPriorityAndSLA(@PathVariable(value = "spId") Long spId,
			@PathVariable(value = "categoryId") Long categoryId, final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser != null) {
			try {
				TicketPrioritySLAVO ticketPrioritySLAVO = ticketSerice.getTicketPriority(spId, categoryId, loginUser);
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
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
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
				savedTicketVO = ticketSerice.saveOrUpdate(ticketVO, loginUser);
					if (savedTicketVO.getTicketId() != null && savedTicketVO.getMessage().equalsIgnoreCase("CREATED")) {
						response.setStatusCode(200);
						response.setObject(savedTicketVO);
						response.setMessage("New Incident created successfully");
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					} else if (savedTicketVO.getTicketId() != null
							&& savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")) {
						response.setStatusCode(200);
						response.setObject(savedTicketVO);
						response.setMessage("Incident updated successfully");
						responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
					}
				
				

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while creating an incident");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}

			if (response.getStatusCode() == 200 && savedTicketVO.getMessage().equalsIgnoreCase("CREATED")) {
				savedTicketVO.setCreatedUser(loginUser.getFirstName()+" "+loginUser.getLastName());
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
			/*
				 * else if(response.getStatusCode()==200 &&
				 * savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){
				 * 
				 * } /*else if(response.getStatusCode()==200 &&
				 * savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){
				 * 
				 * 
				 * } /*else if(response.getStatusCode()==200 &&
				 * savedTicketVO.getMessage().equalsIgnoreCase("UPDATED")){
				 * 
				 * try { emailResponse =
				 * emailService.successTicketCreationSPEmail(savedTicketVO,
				 * "UPDATED"); } catch (Exception e) { logger.info(
				 * "Exception in sending incident update mail", e); } }
				 */
		}
		else {
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

		}else {
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
		}else {
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
				//SelectedTicketVO selectedTicketVO = ticketSerice.getSelectedTicket(ticketVO.getTicketId(),loginUser);
			    session.setAttribute("selectedTicket", ticketVO);
				response.setStatusCode(200);
				response.setObject(ticketVO);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			}else {
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
					selectedTicketVO = ticketSerice.getSelectedTicket(selectedTicketVO.getTicketId(),loginUser);
					/*ServiceProviderVO serviceProviderVO = serviceProviderService.findServiceProvider(selectedTicketVO.getAssignedTo());
					if (StringUtils.isNotBlank(serviceProviderVO.getHelpDeskEmail())) {
						selectedTicketVO.setAssignedSPEmail(serviceProviderVO.getHelpDeskEmail());
					}
					List<EscalationLevelVO> escalationLevelVOs = serviceProviderVO.getEscalationLevelList();
					if (escalationLevelVOs.isEmpty()) {

					} else {
						List<EscalationLevelVO> finalEscalationList = new ArrayList<EscalationLevelVO>();
						for (EscalationLevelVO escalationVO : escalationLevelVOs) {
							TicketEscalationVO ticketEscalationVO = ticketSerice.getEscalationStatus(selectedTicketVO.getTicketId(), escalationVO.getEscId());
							EscalationLevelVO tempEscalationVO = new EscalationLevelVO();
							if (ticketEscalationVO.getCustEscId() != null) {
								tempEscalationVO.setStatus("Escalated");
							}
							tempEscalationVO.setEscId(escalationVO.getEscId());
							tempEscalationVO.setEscalationEmail(escalationVO.getEscalationEmail());
							tempEscalationVO.setEscalationLevel(escalationVO.getEscalationLevel());
							tempEscalationVO.setLevelId(escalationVO.getLevelId());
							tempEscalationVO.setServiceProdviderId(escalationVO.getServiceProdviderId());
							tempEscalationVO.setEscalationPerson(escalationVO.getEscalationPerson());
							finalEscalationList.add(tempEscalationVO);
						}

						selectedTicketVO.setEscalationLevelList(finalEscalationList);
					}
					List<CustomerSPLinkedTicketVO> customerLinkedTickets = ticketSerice.getAllLinkedTickets(selectedTicketVO.getTicketId());
					if (!customerLinkedTickets.isEmpty()) {
						selectedTicketVO.setLinkedTickets(customerLinkedTickets);
					}

					List<TicketCommentVO> selectedTicketComments = ticketSerice.getTicketComments(selectedTicketVO.getTicketId());
					if (!selectedTicketComments.isEmpty()) {
						selectedTicketVO.setTicketComments(selectedTicketComments);
					}

					List<TicketAttachment> fileAttachments = ticketAttachmentRepo.findByTicketNumber(selectedTicketVO.getTicketNumber());
					if (fileAttachments == null) {
						logger.info("Not Ticket Attachment for " + selectedTicketVO.getTicketNumber());
					} else {
						if (fileAttachments.isEmpty()) {
							logger.info("Not Ticket Attachment for " + selectedTicketVO.getTicketNumber());
						} else {
							List<TicketAttachment> fileAttachmentList = new ArrayList<TicketAttachment>();
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
							for (TicketAttachment ticketAttachment : fileAttachments) {
								ticketAttachment.setCreatedDate(formatter.format(ticketAttachment.getCreatedOn()));
								fileAttachmentList.add(ticketAttachment);
							}
							selectedTicketVO.setAttachments(fileAttachmentList);
						}
					}
					selectedTicketVO.setLinkedTickets(selectedTicketVO.getLinkedTickets());
					// selectedTicketVO.getEscalationLevelList().clear();
					selectedTicketVO.setEscalationLevelList(selectedTicketVO.getEscalationLevelList());
					selectedTicketVO.setTicketComments(selectedTicketVO.getTicketComments());
					// Changes added by ankit for Financials Data
					//List<Financials> finacialsList = finService.findByTicketId(selectedTicketVO.getTicketId());
					//selectedTicketVO.setFinancialList(finacialsList);
					logger.info("these are financials====>>> " + selectedTicketVO.getFinancialList());
					// Changes end here
*/					session.setAttribute("selectedTicket", selectedTicketVO);
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
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}
	@RequestMapping(value = "/file/attachments/{ticketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getTicketAttachments(@PathVariable(value="ticketId") Long ticketId ,HttpSession session) throws Exception {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		if (loginUser != null) {
			TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
			List<TicketAttachment> fileAttachments = ticketSerice.findByTicketId(ticketId, loginUser);
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
}
