package com.pms.app.view.vo;

public class SPLoginVO {
	
	private Long spId;
	
	private String email;
	
	private String spUsername;
	
	private String accessCode;
	
	private boolean isValidated;
	
	private String role;
	
	private String roleName;
	
	private Long roleId;
	
	private String spName;
	

	public SPLoginVO() {
		super();
	}

	public Long getSpId() {
		return spId;
	}

	public void setSpId(Long spId) {
		this.spId = spId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAccessCode() {
		return accessCode;
	}

	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	public boolean isValidated() {
		return isValidated;
	}

	public void setValidated(boolean isValidated) {
		this.isValidated = isValidated;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSpName() {
		return spName;
	}

	public void setSpName(String spName) {
		this.spName = spName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getSpUsername() {
		return spUsername;
	}

	public void setSpUsername(String spUsername) {
		this.spUsername = spUsername;
	}
	
	
}
