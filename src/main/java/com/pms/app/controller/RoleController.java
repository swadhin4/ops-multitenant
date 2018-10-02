package com.pms.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPLoginVO;
import com.pms.jpa.entities.RoleStatus;
import com.pms.web.service.UserService;
import com.pms.web.util.RestResponse;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/manage")
	public String getRoles(){
		return "role.entry";
	}

	
	@RequestMapping(value = "/getstatusroleids", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestResponse> getRoleStatusMapping(final HttpSession session){
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		try {
		//Authentication springAuthentication = SecurityContextHolder.getContext().getAuthentication();
		//AuthorizedUserDetails authUser2 = (AuthorizedUserDetails)springAuthentication.getPrincipal();
		List<RoleStatus> getRoleStatus = new ArrayList<RoleStatus>();
		LoginUser loginUser=getCurrentLoggedinUser(session);
		SPLoginVO spLoginVO = (SPLoginVO) session.getAttribute("savedsp");
		if(loginUser!=null){
			logger.info("Getting role status for Loggedin User : "+ loginUser.getUsername());
			getRoleStatus = userService.getRoleStatus(loginUser);
		}else if(!StringUtils.isEmpty(spLoginVO.getSpUsername()) && spLoginVO.isValidated()){
			logger.info("Getting role status for service provider :"+ spLoginVO.getSpUsername());
			//getRoleStatus = userService.getRoleStatusByRoleId(spLoginVO.getRoleId());
		}
		List<Long> StatusIDs= new ArrayList<Long>();
	//	getRoleStatus.stream().peek(e -> StatusIDs.add(e.getRoleId()));
		if(!getRoleStatus.isEmpty()){
			
			for(RoleStatus roleStatus : getRoleStatus){
				StatusIDs.add(roleStatus.getStatusId());
			}
			logger.info("Status IDs: "+ StatusIDs);
		}
		
		response.setObject(StatusIDs);
		response.setStatusCode(200); 
		responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
		}catch(Exception e) {
			e.printStackTrace();
		response.setStatusCode(500);
		response.setMessage("Error Occured While Fetching Status Roles");
		responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.BAD_GATEWAY);
		}
		return responseEntity;
	}

}
