package com.pms.app.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pms.app.view.vo.LoginUser;
import com.pms.web.util.RestResponse;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping("/auth")
public class AuthController extends BaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@RequestMapping(value = "/user/access", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public RestResponse authorizeUserAccess(final HttpSession session) {
		LOGGER.info("Authorizing currently logged in user to access user details page.");
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();

		if(loginUser!=null) {
			if(StringUtils.isEmpty(loginUser.getUsername())) {
				response.setStatusCode(404);
				response.setMessage("User not found");
			}else {
				try {
					response.setLoggedInUserMail(loginUser.getUsername());
					response.setObject(loginUser);
					response.setStatusCode(200);
				} catch (Exception e) {
					response.setStatusCode(500);
					LOGGER.error("Exception while authorization", e);
				}
			}
		}else {
			response.setStatusCode(404);
			response.setMessage("User is not logged in");
		}

		return response;
	}

	@RequestMapping(value = "/site/access", method = RequestMethod.GET, produces="application/json")
	@ResponseBody
	public RestResponse authorizeSiteSuccess(final HttpSession session) {
		LOGGER.info("Authorizing currently logged in user to access site details page.");
		LoginUser loginUser=getCurrentLoggedinUser(session);
		RestResponse response = new RestResponse();
		if(loginUser!=null) {
			if(StringUtils.isEmpty(loginUser.getUsername())) {
				response.setStatusCode(404);
				response.setMessage("User not found");
			}else {
				try {
					response.setLoggedInUserMail(loginUser.getUsername());
					response.setStatusCode(200);
				} catch (Exception e) {
					response.setStatusCode(500);
					LOGGER.error("Exception while authorization", e);
				}
			}
		}else {
			response.setStatusCode(404);
			response.setMessage("User is not logged in");
		}
		return response;
	}
}
