package com.pms.app.exception;

import java.io.Serializable;

import com.pms.app.constants.CommonConstants; 


public class PMSServiceException extends Exception implements Serializable {

	private static final long serialVersionUID = -1L;
	private PMSServiceFault wsaFault;
	
	public PMSServiceException() {
		super();
	}

	public PMSServiceException(String message, Exception exception) {
		super(exception.getMessage());
		this.wsaFault = new PMSServiceFault(CommonConstants.GENERIC_ERROR, exception.getMessage());        
	}

	public PMSServiceException(String errorCode,String message) {
		super(message);
		this.wsaFault = new PMSServiceFault(errorCode, message);
	}
	
	public PMSServiceException(String errorCode,String message, String userMsg) {
		super(message);
		this.wsaFault = new PMSServiceFault(errorCode, message, userMsg);
	}

	public PMSServiceException(String errorCode) {
		super();
		this.wsaFault = new PMSServiceFault(errorCode);
	}

	public PMSServiceException(String type,String errorCode,String status,String message,String userMsg,String[] errors ) {
		super(message);
		this.wsaFault = new PMSServiceFault(type,errorCode, status,message , userMsg, errors);
	}

	public PMSServiceException(String type,String errorCode,String status,String message,String userMsg) {
		super(message);
		this.wsaFault = new PMSServiceFault(type,errorCode, status,message , userMsg);
	}

	public PMSServiceFault getWsaFault() {
		return wsaFault;
	}

	public void setWsaFault(PMSServiceFault wsaFault) {
		this.wsaFault = wsaFault;
	}
}
