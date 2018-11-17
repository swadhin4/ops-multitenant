package com.pms.app.view.vo;

public class CustomerVO {

	private Long customerId;

	private String email;

	private String message;

	private boolean isRegistered = false;

	private String customerCode;
	private String customerName;
	private String countryName;
	private String custDBName;
	
	public String getCustDBName() {
		return custDBName;
	}

	public void setCustDBName(String custDBName) {
		this.custDBName = custDBName;
	}

	private boolean selected;
	private Long accessId;
	private boolean isDelFlagEnabled;
	
	private Long userId;
	private Long countryId;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(final Long customerId) {
		this.customerId = customerId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public boolean isRegistered() {
		return isRegistered;
	}

	public void setRegistered(final boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Long getAccessId() {
		return accessId;
	}

	public void setAccessId(Long accessId) {
		this.accessId = accessId;
	}

	public boolean isDelFlagEnabled() {
		return isDelFlagEnabled;
	}

	public void setDelFlagEnabled(boolean isDelFlagEnabled) {
		this.isDelFlagEnabled = isDelFlagEnabled;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Override
	public String toString() {
		return "CustomerVO [customerId=" + customerId + ", email=" + email + ", message=" + message + ", isRegistered="
				+ isRegistered + ", customerCode=" + customerCode + ", customerName=" + customerName + ", countryName="
				+ countryName + ", selected=" + selected + ", accessId=" + accessId + ", isDelFlagEnabled="
				+ isDelFlagEnabled + "]";
	}

	
	
	
}
