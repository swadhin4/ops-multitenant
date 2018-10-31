package com.pms.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.IncidentImageVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPLoginVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.web.service.TicketService;
import com.pms.web.service.UserService;
import com.pms.web.util.RestResponse;

@RequestMapping(value = "/sp")
@Controller
public class ServiceProviderLoginController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderLoginController.class);
	
	

	@Autowired
	private TicketService ticketSerice;
	
	
	@Autowired
	protected UserService userService;
	
	
	
	
	@RequestMapping(value = "/session/user", method = RequestMethod.GET, produces="application/json")
	public ResponseEntity<RestResponse> loggedInSP(final HttpServletRequest request, final HttpSession session) {
		logger.info("Inside ServiceProviderLoginController .. loggedInSP");
	
		LoginUser loginUser = getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
		if (loginUser!=null) {
			response.setStatusCode(200);
			response.setObject(loginUser);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
		} else {
			response.setStatusCode(404);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
		}
		}catch (Exception e) {
			response.setStatusCode(500);
			logger.info("Exception in getting service provider details", e);
		}
		
		logger.info("Exit ServiceProviderLoginController .. loggedInSP");
		return responseEntity;
	}
	
	
	
	@RequestMapping(value = "/incident/details", method = RequestMethod.GET)
	public String incidentDetails(final Locale locale, final ModelMap model,
			final HttpServletRequest request, HttpSession session) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO!=null) {
			model.put("savedsp", savedLoginVO);
			session.setAttribute("imageList", null);
			return "sp.incident.details";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/incident/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllTickets(final HttpSession session) {
		List<TicketVO> tickets = null;
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if (loginUser != null) {
				tickets = ticketSerice.getAllSPTickets(loginUser);
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


	@RequestMapping(value = "/incident/details/update", method = RequestMethod.GET)
	public String incidentDetailsUpdate(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("savedsp", loginUser);
				model.put("mode", "EDIT");
				return "sp.incident.update";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/incident/details/view", method = RequestMethod.GET)
	public String incidentDetailsView(final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		if (savedLoginVO.getSpId()!=null) {
			model.put("savedsp", savedLoginVO);
				model.put("mode", "EDIT");
				return "sp.incident.view";
		} else {
			return "redirect:/";
		}
	}
	
	
	@RequestMapping(value = "/selected/ticket", method = RequestMethod.POST)
	public ResponseEntity<RestResponse> getSelectedTicket(final ModelMap model, HttpServletRequest request, 
			 HttpSession session, @RequestBody TicketVO ticketVO) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
			LoginUser savedLoginVO=getCurrentLoggedinUser(session);
			if (savedLoginVO!=null) {
					ticketVO = ticketSerice.getSelectedTicket(ticketVO.getTicketId(), savedLoginVO);
				 	session.setAttribute("selectedTicket", ticketVO);
					response.setStatusCode(200);
					response.setObject(ticketVO);
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
				}else {
					response.setStatusCode(401);
					response.setMessage("Your current session is expired. Please login again");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
				}
		} catch (Exception e) {
			response.setStatusCode(500);
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
			logger.info("Exception in getting ticket response", e);
		}

		return responseEntity;
	}
	
	
	@RequestMapping(value = "/image/upload", method = RequestMethod.POST, produces="application/json")
	public ResponseEntity<RestResponse> imageUpload(final ModelMap model, HttpServletRequest request, 
			final HttpSession session, @RequestBody IncidentImageVO incidentImageVO) {
		SPLoginVO savedLoginVO=(SPLoginVO)session.getAttribute("savedsp");
		RestResponse response = new RestResponse();
		Map<Integer, IncidentImageVO> imageList = (Map<Integer, IncidentImageVO>)session.getAttribute("imageList"); 
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
	
		if (savedLoginVO!=null) {
			if(imageList==null){
				imageList = new HashMap<Integer, IncidentImageVO>();
				response.setCalculatedVal(incidentImageVO.getFileSize());
				imageList.put(incidentImageVO.getImgPos(), incidentImageVO);
				session.setAttribute("imageList", imageList);
			}else{
				imageList = (Map<Integer, IncidentImageVO>)session.getAttribute("imageList"); 
				for(Map.Entry<Integer, IncidentImageVO> imageEntry:imageList.entrySet()){
					if(imageEntry.getKey().equals(incidentImageVO.getImgPos())){
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
			responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
			
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}
		return responseEntity;
	}
	
	


}
