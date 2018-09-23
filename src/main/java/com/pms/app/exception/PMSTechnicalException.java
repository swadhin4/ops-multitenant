package com.pms.app.exception;

import java.io.Serializable;

public class PMSTechnicalException extends RuntimeException implements Serializable {


	private static final long serialVersionUID = 1L;
	private PMSServiceFault wsaFault;

	public PMSTechnicalException() {
		super();

	}
	public PMSTechnicalException(String message, Throwable cause) {
		super(message, cause);

		this.wsaFault = new PMSServiceFault(message, cause.getMessage());
	}
	
	public PMSTechnicalException(String message, Throwable cause, String userMsg) {
		super(message, cause);

		this.wsaFault = new PMSServiceFault(message, cause.getMessage(), userMsg);
	}
	
	public PMSTechnicalException(String message) {
		super(message);

	}
	public PMSTechnicalException(Throwable cause) {
		super(cause);

	}
	public PMSServiceFault getWsaFault() {
		return wsaFault;
	}
	public void setWsaFault(PMSServiceFault wsaFault) {
		this.wsaFault = wsaFault;
	}


}
