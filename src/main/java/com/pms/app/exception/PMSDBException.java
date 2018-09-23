package com.pms.app.exception;

import java.io.Serializable; 


public class PMSDBException extends Exception implements Serializable {

	private static final long serialVersionUID = -1L;
	private PMSServiceFault wsaFault;
	
	public PMSDBException() {
		super();
	}

	public PMSDBException(String message, Exception exception) {
		super(exception.getMessage());
		this.wsaFault = new PMSServiceFault(com.pms.app.constants.CommonConstants.GENERIC_ERROR, exception.getMessage());        
	}

	public PMSDBException(String errorCode,String message) {
		super(message);
		this.wsaFault = new PMSServiceFault(errorCode, message);
	}

	public PMSDBException(String errorCode) {
		super();
		this.wsaFault = new PMSServiceFault(errorCode);
	}

	public PMSDBException(String type,String errorCode,String status,String message,String userMsg,String[] errors ) {
		super(message);
		this.wsaFault = new PMSServiceFault(type,errorCode, status,message , userMsg, errors);
	}

	public PMSDBException(String type,String errorCode,String status,String message,String userMsg) {
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
