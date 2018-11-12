package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;

public class ServiceProviderVO implements Serializable {

	private static final long serialVersionUID = -926067993624964198L;
	private Long serviceProviderId;
	private String code;
	private String name;
	private String spUserName;
	private String email;
	private String helpDeskNumber;
	private String helpDeskEmail;
	private String companyName;
	private String companyCode;
	private String countryName;
	private Region region = new Region();
	private Country country = new Country();
	private String additionalDetails;
	private List<SLADetailsVO> slaListVOList= new ArrayList<SLADetailsVO>();
	private List<EscalationLevelVO> escalationLevelList = new ArrayList<EscalationLevelVO>();
	private String slaDescription;
	private String accessKey;
	private int status;
	private String message;
	private String option;
	private List<String> tabs=new ArrayList<String>();
	private int version;

	public Long getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(Long serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpUserName() {
		return spUserName;
	}
	public void setSpUserName(String spUserName) {
		this.spUserName = spUserName;
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
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	public String getAdditionalDetails() {
		return additionalDetails;
	}
	public void setAdditionalDetails(String additionalDetails) {
		this.additionalDetails = additionalDetails;
	}
	public List<SLADetailsVO> getSlaListVOList() {
		return slaListVOList;
	}
	public void setSlaListVOList(List<SLADetailsVO> slaListVOList) {
		this.slaListVOList = slaListVOList;
	}
	public List<EscalationLevelVO> getEscalationLevelList() {
		return escalationLevelList;
	}
	public void setEscalationLevelList(List<EscalationLevelVO> escalationLevelList) {
		this.escalationLevelList = escalationLevelList;
	}
	public String getAccessKey() {
		return accessKey;
	}
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
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
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

	public static Comparator<ServiceProviderVO> COMPARE_BY_SPNAME = new Comparator<ServiceProviderVO>() {
        public int compare(ServiceProviderVO one, ServiceProviderVO other) {
            return one.name.compareTo(other.name);
        }
    };

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
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public List<String> getTabs() {
		return tabs;
	}
	public void setTabs(List<String> tabs) {
		this.tabs = tabs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((serviceProviderId == null) ? 0 : serviceProviderId.hashCode());
		result = prime * result + ((spUserName == null) ? 0 : spUserName.hashCode());
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
		ServiceProviderVO other = (ServiceProviderVO) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (serviceProviderId == null) {
			if (other.serviceProviderId != null)
				return false;
		} else if (!serviceProviderId.equals(other.serviceProviderId))
			return false;
		if (spUserName == null) {
			if (other.spUserName != null)
				return false;
		} else if (!spUserName.equals(other.spUserName))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ServiceProviderVO [serviceProviderId=" + serviceProviderId + ", code=" + code + ", name=" + name
				+ ", spUserName=" + spUserName + ", email=" + email + ", helpDeskNumber=" + helpDeskNumber
				+ ", helpDeskEmail=" + helpDeskEmail + ", region=" + region + ", country=" + country
				+ ", additionalDetails=" + additionalDetails + ", status=" + status + ", message=" + message
				+ ", option=" + option + "]";
	}
		
}
