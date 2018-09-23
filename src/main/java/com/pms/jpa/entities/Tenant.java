package com.pms.jpa.entities;

import java.io.Serializable;

public class Tenant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7600884721775705191L;
	private int tenant_id;
	private String user_email;
	private String company_code;
	private String db_name;
	
	public int getTenant_id() {
		return tenant_id;
	}
	public void setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
	}
	public String getUser_email() {
		return user_email;
	}
	public void setUser_email(String user_email) {
		this.user_email = user_email;
	}
	public String getCompany_code() {
		return company_code;
	}
	public void setCompany_code(String company_code) {
		this.company_code = company_code;
	}
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	@Override
	public String toString() {
		return "Tenant [tenant_id=" + tenant_id + ", user_email=" + user_email + ", company_code=" + company_code
				+ ", db_name=" + db_name + "]";
	}
	
	
	
	
}
