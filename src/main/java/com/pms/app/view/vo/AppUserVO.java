package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Role;

public class AppUserVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1402291382333587542L;
	private Long userId;
	private String firstName;
	private String lastName;
	private String email;
	private String isEnabled;
	private Role role;
	private Company company;
	private String generatedPassword;
	private String phoneNo;
	private List<CustomerVO> customerList = new ArrayList<CustomerVO>();
	private String userType;
	public AppUserVO() {
		super();
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(final String email) {
		this.email = email;
	}

	public String getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(final String isEnabled) {
		this.isEnabled = isEnabled;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(final Role role) {
		this.role = role;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(final Company company) {
		this.company = company;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getGeneratedPassword() {
		return generatedPassword;
	}
	public void setGeneratedPassword(String generatedPassword) {
		this.generatedPassword = generatedPassword;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public List<CustomerVO> getCustomerList() {
		return customerList;
	}
	public void setCustomerList(List<CustomerVO> customerList) {
		this.customerList = customerList;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		AppUserVO other = (AppUserVO) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	
}
