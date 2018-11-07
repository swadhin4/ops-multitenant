package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Role;

public class UserVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1177890956231725360L;
	private Long userId;
	private String userName;
	private String firstName;
	private String lastName;
	private String emailId;
	private Map<Long,String> roles=new HashMap<Long,String>();
	private List<String> roleNames = new ArrayList<String>();
	private List<Long> roleIds=new ArrayList<Long>();
	private Long roleId;
	private String roleName;
	private String companyName;
	private String createdAt;
	private boolean isExists;
	private Role roleSelected;
	private Company company = new Company();
	private String passwordGenerated;
	private int enabled;
	private String phoneNo;
	private String userType;
	private ServiceProviderUserRoleVO userRole;
	//private List<ServiceProviderUserAccessVO> customers;
	private List<CustomerVO> customers;
	private int status;
	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(final String userName) {
		this.userName = userName;
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

	public void setRoles(final Map<Long, String> roles) {
		this.roles = roles;
	}
	public List<Long> getRoleIds() {
		return roleIds;
	}
	public void setRoleIds(final List<Long> roleIds) {
		this.roleIds = roleIds;
	}
	public Map<Long, String> getRoles() {
		return roles;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(final String emailId) {
		this.emailId = emailId;
	}
	public List<String> getRoleNames() {
		return roleNames;
	}
	public void setRoleNames(final List<String> roleNames) {
		this.roleNames = roleNames;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt;
	}
	public boolean isExists() {
		return isExists;
	}
	public void setExists(final boolean isExists) {
		this.isExists = isExists;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(final Company company) {
		this.company = company;
	}
	public String getPasswordGenerated() {
		return passwordGenerated;
	}
	public void setPasswordGenerated(String passwordGenerated) {
		this.passwordGenerated = passwordGenerated;
	}
	public int getEnabled() {
		return enabled;
	}
	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Role getRoleSelected() {
		return roleSelected;
	}
	public void setRoleSelected(Role roleSelected) {
		this.roleSelected = roleSelected;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public ServiceProviderUserRoleVO getUserRole() {
		return userRole;
	}
	public void setUserRole(ServiceProviderUserRoleVO userRole) {
		this.userRole = userRole;
	}
	public List<CustomerVO> getCustomers() {
		return customers;
	}
	public void setCustomers(List<CustomerVO> customers) {
		this.customers = customers;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserVO other = (UserVO) obj;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}



}
