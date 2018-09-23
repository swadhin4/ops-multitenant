package com.pms.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.Notification;
import com.pms.web.util.RestResponse;

@RestController

public class NotificationController extends BaseController{


	@MessageMapping("/hello")
	@SendTo("/topic/userlogin")
	public RestResponse notifyLogin(final HttpSession session) throws Exception {
		Thread.sleep(1000); // simulated delay
		LoginUser loggedInUser = getCurrentLoggedinUser(session);
		RestResponse response= new RestResponse();
		if(loggedInUser!=null){
			Notification notification = new Notification();
			notification.setSubject("User Login Details");
			notification.setDescription("The user currently login to app is "+ loggedInUser.getEmail());
			response.setStatusCode(200);
			response.setObject(notification);
		}
		return response;
	}

}
