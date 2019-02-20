package com.pms.app.constants;

public class RSPCustomerConstants {

	
	public static final String RSP_REGION_LIST_QUERY = "select * from sp_region";

	public static final String RSP_COUNTRY_LIST_QUERY = "select * from sp_country where region_id=?";

	public static final String SAVE_RSP_EXTERNAL_CUSTOMER_QUERY = "insert into sp_ext_customers (cust_name, cust_code, country_id,"
			+ " p_email, s_email, p_contact_no, s_contact_no, sla_desc, created_by, created_date) "
			+ " values(?,?,?,?,?,?,?,?,?,NOW())";
	
	public static final String RSP_EXTERNAL_CUSTOMER_LIST_QUERY ="select e.cust_cid, e.cust_name, "
			+ " e.cust_code,e.country_id, sc.country_name, sc.region_id, sr.region_name, e.p_email, e.s_email, e.s_contact_no, "
			+ " e.p_contact_no  FROM sp_ext_customers e " 
			+ " LEFT JOIN sp_country sc ON sc.country_id = e.country_id"
			+ " LEFT JOIN sp_region sr ON sr.region_id = sc.region_id";
	

	public static final String INSERT_EXTERNAL_CUSTOMER_SLA_LIST = "insert into sp_ext_cust_sla(ext_cust_id, priority_id, duration, unit, created_by, created_on) "
			+ " values (?,?,?,?,?,NOW())";
	
	public static final String UPDATE_EXTERNAL_CUSTOMER_SLA_LIST = "update sp_ext_cust_sla set priority_id=?, duration=?, unit=? where ext_cust_id=?";
	
}
