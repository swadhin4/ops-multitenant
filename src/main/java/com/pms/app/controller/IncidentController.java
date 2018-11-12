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

import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.IncidentImageVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.SPEscalationLevels;
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
	 * 
	 * 
	 * @Autowired private ServiceProviderService serviceProviderService;
	 * 
	 * @Autowired private SPEscalationLevelRepo spEscalationRepo;
	 * 
	 * @Autowired private TicketAttachmentRepo ticketAttachmentRepo;
	 * 
	 * @Autowired private FinancialService finService;
	 */

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

	@RequestMapping(value = "/status/{category}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<Status>> listAllOpenTickets(HttpSession session,
			@PathVariable(value = "category") final String category) {
		List<Status> statusList = null;
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
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
		} else {
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
				if (ticketVO.getMode().equalsIgnoreCase("NEW")
						&& savedTicketVO.getMessage().equalsIgnoreCase("CREATED")) {
					response.setStatusCode(200);
					response.setObject(savedTicketVO);
					response.setMessage("New Incident created successfully");
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

	@RequestMapping(value = "/selected/ticket", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> getSelectedTicket(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @RequestBody TicketVO ticketVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				// SelectedTicketVO selectedTicketVO =
				// ticketSerice.getSelectedTicket(ticketVO.getTicketId(),loginUser);
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
					selectedTicketVO = ticketSerice.getSelectedTicket(selectedTicketVO.getTicketId(), loginUser);
					List<EscalationLevelVO> escalationLevelVOs = selectedTicketVO.getEscalationLevelList();
					if (escalationLevelVOs.isEmpty()) {

					} else {
						List<EscalationLevelVO> finalEscalationList = new ArrayList<EscalationLevelVO>();
						for (EscalationLevelVO escalationVO : escalationLevelVOs) {
							TicketEscalationVO ticketEscalationVO = ticketSerice.getEscalationStatus(
									selectedTicketVO.getTicketId(), escalationVO.getEscId(), loginUser);
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
					/*
					 * ServiceProviderVO serviceProviderVO =
					 * serviceProviderService.findServiceProvider(
					 * selectedTicketVO.getAssignedTo()); if
					 * (StringUtils.isNotBlank(serviceProviderVO.
					 * getHelpDeskEmail())) {
					 * selectedTicketVO.setAssignedSPEmail(serviceProviderVO.
					 * getHelpDeskEmail()); }
					 * 
					 * List<CustomerSPLinkedTicketVO> customerLinkedTickets =
					 * ticketSerice.getAllLinkedTickets(selectedTicketVO.
					 * getTicketId()); if (!customerLinkedTickets.isEmpty()) {
					 * selectedTicketVO.setLinkedTickets(customerLinkedTickets);
					 * }
					 * 
					 * List<TicketCommentVO> selectedTicketComments =
					 * ticketSerice.getTicketComments(selectedTicketVO.
					 * getTicketId()); if (!selectedTicketComments.isEmpty()) {
					 * selectedTicketVO.setTicketComments(selectedTicketComments
					 * ); }
					 * 
					 * List<TicketAttachment> fileAttachments =
					 * ticketAttachmentRepo.findByTicketNumber(selectedTicketVO.
					 * getTicketNumber()); if (fileAttachments == null) {
					 * logger.info("Not Ticket Attachment for " +
					 * selectedTicketVO.getTicketNumber()); } else { if
					 * (fileAttachments.isEmpty()) { logger.info(
					 * "Not Ticket Attachment for " +
					 * selectedTicketVO.getTicketNumber()); } else {
					 * List<TicketAttachment> fileAttachmentList = new
					 * ArrayList<TicketAttachment>(); SimpleDateFormat formatter
					 * = new SimpleDateFormat("dd-MM-yyyy HH:mm"); for
					 * (TicketAttachment ticketAttachment : fileAttachments) {
					 * ticketAttachment.setCreatedDate(formatter.format(
					 * ticketAttachment.getCreatedOn()));
					 * fileAttachmentList.add(ticketAttachment); }
					 * selectedTicketVO.setAttachments(fileAttachmentList); } }
					 * selectedTicketVO.setLinkedTickets(selectedTicketVO.
					 * getLinkedTickets()); //
					 * selectedTicketVO.getEscalationLevelList().clear();
					 * selectedTicketVO.setEscalationLevelList(selectedTicketVO.
					 * getEscalationLevelList());
					 * selectedTicketVO.setTicketComments(selectedTicketVO.
					 * getTicketComments()); // Changes added by ankit for
					 * Financials Data //List<Financials> finacialsList =
					 * finService.findByTicketId(selectedTicketVO.getTicketId())
					 * ; //selectedTicketVO.setFinancialList(finacialsList);
					 * logger.info("these are financials====>>> " +
					 * selectedTicketVO.getFinancialList()); // Changes end here
					 * 
					 */
					List<Financials> finacialsList = ticketSerice.findFinanceByTicketId(selectedTicketVO.getTicketId(),
							loginUser);
					selectedTicketVO.setFinancialList(finacialsList);
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

	@RequestMapping(value = "/file/attachments/{ticketId}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getTicketAttachments(@PathVariable(value = "ticketId") Long ticketId,
			HttpSession session) throws Exception {
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

	@RequestMapping(value = "/comment/save", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> comment(final ModelMap model, final HttpServletRequest request,
			final HttpSession session, @RequestBody TicketCommentVO ticketCommentVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				TicketCommentVO savedComment = ticketSerice.saveTicketComment(ticketCommentVO, loginUser);
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
			List<TicketCommentVO> selectedTicketComments = ticketSerice.getTicketComments(ticketId, loginUser);
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

	@RequestMapping(value = "/linkedticket/{custticket}/{custticketnumber}/{linkedticket}", method = RequestMethod.GET)
	public ResponseEntity<RestResponse> linked(final ModelMap model, HttpServletRequest request, HttpSession session,
			@PathVariable(value = "custticket") Long custTicket,
			@PathVariable(value = "custticketnumber") String custTicketNumber,
			@PathVariable(value = "linkedticket") String linkedTicket) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				CustomerSPLinkedTicketVO savedTicketLinked = ticketSerice.saveLinkedTicket(custTicket, custTicketNumber,
						linkedTicket, loginUser);
				if (savedTicketLinked.getId() != null) {
					response.setStatusCode(200);
					response.setObject(savedTicketLinked);
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
					ticketVO.setLinkedTickets(ticketVO.getLinkedTickets());
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
				int deletedTicket = ticketSerice.deleteLinkedTicket(linkedTicketId, loginUser);
				if (deletedTicket == 1) {
					response.setStatusCode(200);
					TicketVO sessionTicket = (TicketVO) session.getAttribute("selectedTicket");
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
					boolean isRemoved = false;
					if (isAvailable) {
						isRemoved = customerLinkedTickets.remove(foundTicket);
					}
					if (isRemoved) {
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
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
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
/*		if (response.getStatusCode() == 200) {
			List<String> escCCMailList = new ArrayList<String>(0);
			SPEscalationLevels spEscalationLevel = null;
			List<EscalationLevelVO> escalationLevelVOs = ticketEscalationLevels.getTicketData()	.getEscalationLevelList();

			if (escalationLevelVOs.size() == 0) {
				logger.info("No escalation list available");
			} else {
				logger.info("Escalation Level list : " + escalationLevelVOs.size());
				spEscalationLevel = spEscalationRepo.findOne(savedTicketEscalation.getEscId());
				TicketVO selectedTicketVO = (TicketVO) session.getAttribute("selectedTicket");
				for (EscalationLevelVO escalatedTicket : selectedTicketVO.getEscalationLevelList()) {
					if (StringUtils.isNotBlank(escalatedTicket.getStatus())) {
						escCCMailList.add(escalatedTicket.getEscalationEmail());
					}

				}

				logger.info("escCCMailList :" + escCCMailList);
			}
			final SPEscalationLevels spEscLevel = spEscalationLevel;
			final TicketEscalationVO savedTicketEsc = savedTicketEscalation;
			TaskExecutor theExecutor = new SimpleAsyncTaskExecutor();
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
								emailService.successEscalationLevel(ticketEscalationLevels.getTicketData(), spEscLevel,
										ccEscList, savedTicketEsc.getEscLevelDesc());
							} catch (Exception e) {
								logger.info("Exception while sending email", e);
							}
						} else {
							logger.info("Escalation To List : " + spEscLevel.getEscalationEmail());
							logger.info("Escalation CC list is empty");
							try {
								emailService.successEscalationLevel(ticketEscalationLevels.getTicketData(), spEscLevel,
										ccEscList, savedTicketEsc.getEscLevelDesc());
							} catch (Exception e) {
								logger.info("Exception while sending email", e);
							}

						}

					} else {
						logger.info("No ticket escalated for SP");
					}

				}
			});

		}*/

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
				List<Financials> financials = ticketSerice.saveFinancials(finVOList, loginUser);
				if (!financials.isEmpty()) {
					TicketVO sessionTicket = (TicketVO) session.getAttribute("selectedTicket");
					sessionTicket.setFinancialList(financials);
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
				JSONArray jsonArray = new JSONArray(finVO);
				int successCount = 0;
				int failureCount = 0;
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject explrObject = jsonArray.getJSONObject(i);
					if (explrObject.getBoolean("isDeleteCheck") == true) {
						boolean resp;
						resp = ticketSerice.deleteFinanceCostById(explrObject.getLong("costId"), loginUser);
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
}
