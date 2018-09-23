package com.pms.app.exception;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import com.pms.app.constants.CommonConstants;

public class BaseExceptionHandler implements ExceptionMapper<Exception>{

	private static final Logger LOGGER = Logger.getLogger(BaseExceptionHandler.class.getName());

	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	
	public Response toResponse(Exception exception) {
		PMSServiceFault wsFault = new PMSServiceFault();

		ResponseBuilder respBuilder = null;
		respBuilder = Response.status(Status.INTERNAL_SERVER_ERROR);

		wsFault.setErrorCode(CommonConstants.GENERIC_ERROR);
		wsFault.setStatus(CommonConstants.ERROR_STATUS);
		wsFault.setType(CommonConstants.ERROR_TYP_SYSTEM);
		wsFault.setErrorMessage(exception.getMessage());
		wsFault.setUserMessage(CommonConstants.COMMON_EXCEPTION_USER_MSG);


		LOGGER.log(Level.SEVERE, "Exception Caught in Handler - "+exception);

		return respBuilder.entity(wsFault).build();
	}
	

}
