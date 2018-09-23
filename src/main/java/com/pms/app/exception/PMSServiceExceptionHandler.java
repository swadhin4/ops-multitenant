
package com.pms.app.exception;


import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pms.app.constants.CommonConstants;

@ControllerAdvice
public class PMSServiceExceptionHandler  {

	private static final Logger LOGGER = LoggerFactory.getLogger(PMSServiceExceptionHandler.class.getName());


	@ExceptionHandler({PMSServiceException.class})
	@ResponseBody
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public PMSServiceFault serviceError(HttpServletResponse httpRes,PMSServiceException exception) {
		System.out.println("serviceError called");
		
		String errorCode = exception.getWsaFault().getErrorCode();

		if(errorCode.equalsIgnoreCase(CommonConstants.NO_DATA_FOUND_E1003)
				) {
			httpRes.setStatus(HttpStatus.OK.value());
		} 
		else if(errorCode.equalsIgnoreCase(CommonConstants.MISSING_INPUT_ERROR_E1001)
				|| errorCode.equalsIgnoreCase(CommonConstants.DATA_VALIDATION_ERROR_E1002)
				|| errorCode.equalsIgnoreCase(CommonConstants.DATE_FORMAT_ERROR_E1008)        		
				){

			httpRes.setStatus(HttpStatus.BAD_REQUEST.value());
		}
		else if(errorCode.equalsIgnoreCase(CommonConstants.ERROR_INVALID_PARAM_COMBINATION_E1005)){
			httpRes.setStatus(HttpStatus.FORBIDDEN.value());
		}
		else if(errorCode.equalsIgnoreCase(CommonConstants.UNAUTHORIZED_E1006)){
			httpRes.setStatus(HttpStatus.UNAUTHORIZED.value());
		}

		else {
			httpRes.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		LOGGER.info("PMSServiceException Caught in Handler - " + exception.getWsaFault().getErrorCode()+"-"+exception.getWsaFault().getErrorMessage()+"-"+exception.getWsaFault().getUserMessage());
		System.out.println("From handler : "+exception.getWsaFault());
		return exception.getWsaFault();
	}

}
