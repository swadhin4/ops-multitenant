/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/company")
@Controller
public class CompanyController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(CompanyController.class);


}
