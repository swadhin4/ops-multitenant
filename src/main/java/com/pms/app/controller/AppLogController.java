package com.pms.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handles requests for the application home page.
 */
@Controller
@RequestMapping(value = "/server")
public class AppLogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppLogController.class);

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/app/log", method = RequestMethod.GET, produces=MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String logWindow(final HttpServletRequest request, HttpServletRequest response) throws IOException {

		String serverLogPath = System.getProperty("catalina.base");
		serverLogPath=serverLogPath.replaceAll("\\\\","/");
		LOGGER.info("Log Path : " +serverLogPath );
		Path path = FileSystems.getDefault().getPath(serverLogPath+"/logs/pms/pms.log"); 
		StringBuilder logContent = new StringBuilder(); 
		logContent.append("<pre>"); 
		try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);) { 
			String line = null; 
			while ((line = reader.readLine()) != null) { 
				logContent.append(line).append("<br/>"); 
			} 
		} catch (IOException x) { 
			// Take care of that 
		} 
		logContent.append("</pre>"); 
		return logContent.toString();
	}


}
