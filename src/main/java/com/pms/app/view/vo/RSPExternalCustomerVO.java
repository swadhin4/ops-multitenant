package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pms.app.exception.Required;

public class RSPExternalCustomerVO implements Serializable{

	private static final long serialVersionUID = -611333737320816640L;
	private Long customerId;
	@Required
	private String customerName;
	@Required
	private String companyCode;
	@Required
	private String primaryContactEmail;
	@Required
	private String primaryContactNumber;
	private String secondaryContactEmail;
	private String secondaryContactNumber;
	private Long regionId;
	private String regionName;
	private Long countryId;
	private String countryName;
	private String slaDescription;
	private int status;
	private List<RSPExternalSLADetailVO> slaListVOList=new ArrayList<RSPExternalSLADetailVO>();
	
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getPrimaryContactEmail() {
		return primaryContactEmail;
	}
	public void setPrimaryContactEmail(String primaryContactEmail) {
		this.primaryContactEmail = primaryContactEmail;
	}
	public String getPrimaryContactNumber() {
		return primaryContactNumber;
	}
	public void setPrimaryContactNumber(String primaryContactNumber) {
		this.primaryContactNumber = primaryContactNumber;
	}
	public String getSecondaryContactEmail() {
		return secondaryContactEmail;
	}
	public void setSecondaryContactEmail(String secondaryContactEmail) {
		this.secondaryContactEmail = secondaryContactEmail;
	}
	public String getSecondaryContactNumber() {
		return secondaryContactNumber;
	}
	public void setSecondaryContactNumber(String secondaryContactNumber) {
		this.secondaryContactNumber = secondaryContactNumber;
	}
	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	public String getRegionName() {
		return regionName;
	}
	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}
	public Long getCountryId() {
		return countryId;
	}
	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public String getSlaDescription() {
		return slaDescription;
	}
	public void setSlaDescription(String slaDescription) {
		this.slaDescription = slaDescription;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<RSPExternalSLADetailVO> getSlaListVOList() {
		return slaListVOList;
	}
	public void setSlaListVOList(List<RSPExternalSLADetailVO> slaListVOList) {
		this.slaListVOList = slaListVOList;
	}
	@Override
	public String toString() {
		return "RSPExternalCustomerVO [customerId=" + customerId + ", customerName=" + customerName + ", companyCode="
				+ companyCode + ", primaryContactEmail=" + primaryContactEmail + ", primaryContactNumber="
				+ primaryContactNumber + ", secondaryContactEmail=" + secondaryContactEmail
				+ ", secondaryContactNumber=" + secondaryContactNumber + ", regionId=" + regionId + ", regionName="
				+ regionName + ", countryId=" + countryId + ", countryName=" + countryName + ", slaDescription="
				+ slaDescription + ", status=" + status + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyCode == null) ? 0 : companyCode.hashCode());
		result = prime * result + ((customerId == null) ? 0 : customerId.hashCode());
		result = prime * result + ((primaryContactEmail == null) ? 0 : primaryContactEmail.hashCode());
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
		RSPExternalCustomerVO other = (RSPExternalCustomerVO) obj;
		if (companyCode == null) {
			if (other.companyCode != null)
				return false;
		} else if (!companyCode.equals(other.companyCode))
			return false;
		if (customerId == null) {
			if (other.customerId != null)
				return false;
		} else if (!customerId.equals(other.customerId))
			return false;
		if (primaryContactEmail == null) {
			if (other.primaryContactEmail != null)
				return false;
		} else if (!primaryContactEmail.equals(other.primaryContactEmail))
			return false;
		return true;
	}
	
	

}
