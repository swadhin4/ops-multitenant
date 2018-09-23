package com.pms.app.exception;

import java.io.Serializable;
import java.util.Arrays;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@XmlRootElement
@JsonPropertyOrder({"status", "errorCode", "errorMessage"})
@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class PMSServiceFault implements Serializable {


	private static final long serialVersionUID = -8979390207199719926L;

	private String status ;
	private String errorCode;
	private String errorMessage;
	private String type ;			 									 // EDS - fuse 6.0 Refactoring
	private String userMessage;                                			 // EDS - fuse 6.0 Refactoring
	private String[] errors ;  											 // EDS - fuse 6.0 Refactoring

	public PMSServiceFault() {
		super();
	}

	public PMSServiceFault(String errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	
	public PMSServiceFault(String errorCode, String errorMessage, String userMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.userMessage = userMsg;
	}
	

	public PMSServiceFault(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public PMSServiceFault(String errorCode,String message,String userMsg, String[] errors) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.userMessage = userMsg;
		this.errors = errors;

	}

	public PMSServiceFault(String type,String errorCode,String status,String message,String userMsg, String[] errors) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.userMessage = userMsg;
		this.errors = errors;
		this.type = type;
		this.status = status;

	}

	public PMSServiceFault(String type,String errorCode,String status,String message,String userMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = message;
		this.userMessage = userMsg;
		this.type = type;
		this.status = status;
	}

	@XmlElement(name = "status")
	@JsonProperty("status")
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlElement(name = "errorCode")
	@JsonProperty("errorCode")
	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@XmlElement(name = "errorMsg")
	@JsonProperty("errorMsg")
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	// EDS - fuse 6.0 Refactoring start

	@XmlElement(name = "userMsg")
	@JsonProperty("userMsg")
	public String getUserMessage() {
		return userMessage;
	}

	public void setUserMessage(String userMessage) {
		this.userMessage = userMessage;
	}

	@XmlElement(name = "type")
	@JsonProperty("type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "errors")
	@JsonProperty("errors")
	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	@Override
	public String toString() {
		return "PMSServiceFault [status=" + status + ", errorCode="
				+ errorCode + ", errorMessage=" + errorMessage + ", type="
				+ type + ", userMessage=" + userMessage + ", errors="
				+ Arrays.toString(errors) + "]";
	}
}
