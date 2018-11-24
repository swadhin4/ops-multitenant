package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RegisteredServiceProviderVO implements Serializable {

	private static final long serialVersionUID = -926067993624964198L;
	private Long rspId;
	private String rspName;
	private String email;
	private String helpDeskNumber;
	private String helpDeskEmail;
	private String companyName;
	private String companyCode;
	private String countryId;
	private String countryName;
	private String rspDescription;
	private String slaDescription;
	private String customerId;
	private List<RegisteredSPSLA> registeredSLAList = new ArrayList<RegisteredSPSLA>();
	private int status;
	private String message;
	private String option;
	private int version;
	public Long getRspId() {
		return rspId;
	}
	public void setRspId(Long rspId) {
		this.rspId = rspId;
	}
	public String getRspName() {
		return rspName;
	}
	public void setRspName(String rspName) {
		this.rspName = rspName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHelpDeskNumber() {
		return helpDeskNumber;
	}
	public void setHelpDeskNumber(String helpDeskNumber) {
		this.helpDeskNumber = helpDeskNumber;
	}
	public String getHelpDeskEmail() {
		return helpDeskEmail;
	}
	public void setHelpDeskEmail(String helpDeskEmail) {
		this.helpDeskEmail = helpDeskEmail;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCountryId() {
		return countryId;
	}
	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getRspDescription() {
		return rspDescription;
	}
	public void setRspDescription(String rspDescription) {
		this.rspDescription = rspDescription;
	}
	public String getSlaDescription() {
		return slaDescription;
	}
	public void setSlaDescription(String slaDescription) {
		this.slaDescription = slaDescription;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	public List<RegisteredSPSLA> getRegisteredSLAList() {
		return registeredSLAList;
	}
	public void setRegisteredSLAList(List<RegisteredSPSLA> registeredSLAList) {
		this.registeredSLAList = registeredSLAList;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((rspId == null) ? 0 : rspId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegisteredServiceProviderVO other = (RegisteredServiceProviderVO) obj;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (rspId == null) {
			if (other.rspId != null)
				return false;
		} else if (!rspId.equals(other.rspId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "RegisteredServiceProviderVO [rspId=" + rspId + ", rspName=" + rspName + ", email=" + email
				+ ", helpDeskNumber=" + helpDeskNumber + ", helpDeskEmail=" + helpDeskEmail + ", companyName="
				+ companyName + ", companyCode=" + companyCode + ", countryId=" + countryId + ", countryName="
				+ countryName + ", rspDescription=" + rspDescription + ", slaDescription=" + slaDescription
				+ ", customerId=" + customerId + ", status=" + status + "]";
	}

		
}
