package com.pms.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.pms.app.exception.CustomGenericException;
import com.pms.web.util.RestResponse;

@Controller
public class MainExceptionController {

	@ExceptionHandler(CustomGenericException.class)
	public RestResponse handleCustomException(final CustomGenericException ex) {

		return null;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(final Exception ex) {

		return null;
	}

}
