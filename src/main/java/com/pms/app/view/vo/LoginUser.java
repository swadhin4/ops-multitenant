package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.pms.jpa.entities.AppFeature;
import com.pms.jpa.entities.Company;
import com.pms.jpa.entities.Site;
import com.pms.jpa.entities.UserRole;

public class LoginUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6018974143388625606L;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private Long userId;
	private Long employeeId;
	private Long spId;
	private List<AppFeature> featureList = new ArrayList<AppFeature>();
	private List<String> accessLevelList = new ArrayList<String>();
	private String sysPassword;
	private Company company = new Company();
	private Site site;
	private List<UserRole> userRoles=new ArrayList<UserRole>();
	private int loginStatus;
	private String message;
	private String phoneNo;
	private String dbName;
	private String userType;

	public String getUsername() {
		return username;
	}
	public void setUsername(final String username) {
		this.username = username;
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
	public List<UserRole> getUserRoles() {
		return userRoles;
	}
	public void setUserRoles(final List<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(final Long userId) {
		this.userId = userId;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(final Long employeeId) {
		this.employeeId = employeeId;
	}
	public List<AppFeature> getFeatureList() {
		return featureList;
	}
	public void setFeatureList(final List<AppFeature> featureList) {
		this.featureList = featureList;
	}
	public List<String> getAccessLevelList() {
		return accessLevelList;
	}
	public void setAccessLevelList(final List<String> accessLevelList) {
		this.accessLevelList = accessLevelList;
	}
	public String getSysPassword() {
		return sysPassword;
	}
	public void setSysPassword(final String sysPassword) {
		this.sysPassword = sysPassword;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(final Company company) {
		this.company = company;
	}
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	public int getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(int loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "LoginUser [username=" + username + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", email=" + email + ", userId="
				+ userId + ", featureList=" + featureList
				+ ", accessLevelList=" + accessLevelList + ", userRoles="
				+ userRoles + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		LoginUser other = (LoginUser) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}


		
}
