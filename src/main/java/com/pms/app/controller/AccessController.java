package com.pms.app.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * The Class AccessController.
 *
 */
@Controller
public class AccessController extends BaseController{

	private static final Logger LOGGER = LoggerFactory.getLogger(AccessController.class);


	/**
	 * Denied.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/denied", method = RequestMethod.GET)
	public String denied() {
		return "denied";
	}

	/**
	 * Login failure.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/login/failure", method = RequestMethod.GET)
	public String loginFailure(final ModelMap model, HttpSession session) {
		return "redirect:/";
	}

	/**
	 * Logout session time out.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(final HttpServletRequest request,
			final HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if ( session != null) {
		String sessionId = session.getId();
		session.invalidate();
		Cookie[] cookies = request.getCookies();
	    for (Cookie cookie : cookies) {
	      if (sessionId.equalsIgnoreCase(cookie.getValue())) {
	        cookie.setMaxAge(0);
	        cookie.setValue(null);
	        cookie.setDomain(request.getServerName());
	        cookie.setPath(request.getServletContext().getContextPath() + "/");
	      //  cookie.setSecure(request.isSecure());
	        response.addCookie(cookie);
	        break;
	      }
	    }
		}
		
		return "redirect:/";

	}

	/**
	 * Logout session time out.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/logout/session", method = RequestMethod.GET)
	public String logoutSessionTimeOut(final Model model) {
		String message = "";
		model.addAttribute("message", message);
		return "redirect:/";
	}

	/**
	 * Logout session time out.
	 *
	 * @param model
	 *            the model
	 * @return the string
	 */
	@RequestMapping(value = "/logout/maxSession", method = RequestMethod.GET)
	public String logoutMaxSession(final Model model) {
		String message = "User already logged into the system.";
		model.addAttribute("message", message);
		return "redirect:/";
	}

	/**
	 * Session time out.
	 *
	 * @return the string
	 */
	@RequestMapping(value = "/sessiontimeout", method = RequestMethod.GET)
	public String sessionTimeOut() {
		return "sessiontimeout";
	}


/*	@RequestMapping(value = "/validatelogin", method = RequestMethod.POST)
	public String validateLogin(final ModelMap model,final HttpServletRequest request) {
		User appUser = null;
		String validLoginUrl="redirect:/";
		if (StringUtils.isEmpty(request.getParameter("username"))) {

		} else {
			appUser = userService.findByUserName(request.getParameter("username"));
			if(appUser!=null) {
				if (appUser.getEmailId().equalsIgnoreCase(request.getParameter("username"))
						&& appUser.getPassword().equalsIgnoreCase(
								request.getParameter("password"))) {
					LoginUser loginUser=new LoginUser();
					loginUser.setUsername(appUser.getEmailId());
					loginUser.setUserRoles(appUser.getUserRoles());

					request.getSession().setAttribute("loginUser", loginUser);
					validLoginUrl="redirect:/dashboard";
				}else {
					request.getSession().setAttribute("error", "Invalid Username and Password");
					validLoginUrl="redirect:/";
				}
			}else {
				request.getSession().setAttribute("error", "Invalid Username and Password");
				validLoginUrl="redirect:/";
			}
		}

		return validLoginUrl;
	}*/
}