package com.pms.jpa.entities;

import java.io.Serializable;

public class SPTenant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7600884721775705191L;
	private int tenant_id;
	private String sp_user_email;
	private String sp_company_code;
	private String db_name;
	private Long sp_country_id;
	public int getTenant_id() {
		return tenant_id;
	}
	public void setTenant_id(int tenant_id) {
		this.tenant_id = tenant_id;
	}
	public String getSp_user_email() {
		return sp_user_email;
	}
	public void setSp_user_email(String sp_user_email) {
		this.sp_user_email = sp_user_email;
	}
	public String getSp_company_code() {
		return sp_company_code;
	}
	public void setSp_company_code(String sp_company_code) {
		this.sp_company_code = sp_company_code;
	}
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	public Long getSp_country_id() {
		return sp_country_id;
	}
	public void setSp_country_id(Long sp_country_id) {
		this.sp_country_id = sp_country_id;
	}
	@Override
	public String toString() {
		return "SPTenant [tenant_id=" + tenant_id + ", sp_user_email=" + sp_user_email + ", db_name=" + db_name + "]";
	}
	
	
	
	
}
