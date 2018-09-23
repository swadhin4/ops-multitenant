package com.pms.app.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pms.app.constants.CommonConstants;

@ControllerAdvice
public class PMSTechnicalExceptionHandler {

	private static final Logger LOGGER = Logger.getLogger(PMSTechnicalExceptionHandler.class.getName());



	@ExceptionHandler({PMSTechnicalException.class})
	@ResponseBody
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public PMSServiceFault serviceError(HttpServletResponse httpRes,PMSTechnicalException exception) {

		httpRes.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		exception.getWsaFault().setErrorCode(CommonConstants.TECHNICAL_ERROR); 
		exception.getWsaFault().setStatus(CommonConstants.ERROR_STATUS);
		exception.getWsaFault().setType(CommonConstants.ERROR_TYP_CRICTICAL);
		//exception.getWsaFault().setUserMessage(exception.get);

		LOGGER.log(Level.SEVERE, "PMSTechnicalExceptionHandler Caught in Handler -"+exception.getMessage()+"-"+exception.getCause());

		return exception.getWsaFault();
	}	

}
