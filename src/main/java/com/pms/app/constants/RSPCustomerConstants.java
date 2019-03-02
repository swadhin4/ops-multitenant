package com.pms.app.constants;

public class RSPCustomerConstants {

	
	public static final String RSP_REGION_LIST_QUERY = "select * from sp_region";

	public static final String RSP_COUNTRY_LIST_QUERY = "select * from sp_country where region_id=?";

	public static final String SAVE_RSP_EXTERNAL_CUSTOMER_QUERY = "insert into sp_ext_customers (cust_name, cust_code, country_id,"
			+ " p_email, s_email, p_contact_no, s_contact_no, sla_desc, created_by, created_date) "
			+ " values(?,?,?,?,?,?,?,?,?,NOW())";
	
	public static final String UPDATE_RSP_EXTERNAL_CUSTOMER_QUERY = "update sp_ext_customers set cust_name=?, cust_code=?, country_id=?,"
			+ "p_email=?, s_email=?, p_contact_no=?, s_contact_no=?, sla_desc=? where cust_cid=?";
	
	public static final String SELECT_RSP_EXTERNAL_CUSTOMER_QUERY = "select * from sp_ext_customers where cust_code=? and p_email=?";
	
	public static final String RSP_EXTERNAL_CUSTOMER_LIST_QUERY ="select e.cust_cid, e.cust_name, "
			+ " e.cust_code,e.country_id, sc.country_name, sc.region_id, sr.region_name, e.p_email, e.s_email, e.s_contact_no, "
			+ " e.p_contact_no, e.sla_desc  FROM sp_ext_customers e " 
			+ " LEFT JOIN sp_country sc ON sc.country_id = e.country_id"
			+ " LEFT JOIN sp_region sr ON sr.region_id = sc.region_id";
	

	public static final String INSERT_EXTERNAL_CUSTOMER_SLA_LIST = "insert into sp_ext_cust_sla(ext_cust_id, priority_id, duration, unit, created_by, created_on) "
			+ " values (?,?,?,?,?,NOW())";
	
	public static final String UPDATE_EXTERNAL_CUSTOMER_SLA_LIST = "update sp_ext_cust_sla set  duration=?, unit=? where sla_id=? and ext_cust_id=?";

	public static final String RSP_EXTERNAL_CUSTOMER_SLA_LIST_QUERY = "select e.sla_id, e.priority_id, e.duration, e.unit,"
			+ " p.description from sp_ext_cust_sla e LEFT JOIN pm_ticket_priority p "
			+ " ON p.priority_id = e.priority_id where e.ext_cust_id=?";
	
	public static final String INSERT_EXT_CUSTOMER_SITE_CREATE="INSERT INTO pm_site (site_name, site_owner, operator_id, district_id, area_id,"
			+ "  cluster_id, elec_id_no, site_number1, site_number2, attachment_path,sales_area_size, brand_id, brand_name, contact_name, email,  "
			+ "  latitude, longitude,  primary_contact_number, alt_contact_number, site_address1, site_address2, site_address3, site_address4, post_code,   "
			+ "  created_by, created_at) "
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
	
	
	public static final String UPDATE_EXT_CUSTOMER_SITE_QUERY="UPDATE  pm_site set site_name=?, site_owner=?, operator_id=?, district_id=?, area_id=?,"
			+ "  cluster_id=?, elec_id_no=?, site_number1=?, site_number2=?, attachment_path=?,sales_area_size=?, brand_id=?, brand_name=?, contact_name=?, email=?,  "
			+ "  latitude=?, longitude=?,  primary_contact_number=?, alt_contact_number=?, site_address1=?, site_address2=?, site_address3=?, site_address4=?, post_code=?,   "
			+ "  modified_by=?, modified_date=NOW() where site_id=?";
	
	
	public static final String EXT_CUST_SP_USER_ACCESS = "insert into pm_user_access(user_id, site_id, created_date, created_by) "
			+ " values(?,?, NOW(),?)";
	
	
	public static final String EXT_CUST_USER_SITE_ACCESS_QUERY="select ps.site_id, ps.site_name, ps.district_id, pd.district_name,"
			+ " ps.area_id, pa.area_name, ps.cluster_id, pc.cluster_name, sec.cust_cid as company_id,sec.cust_name as company_name, "
			+ " pcon.country_id, pcon.country_name, ps.site_owner,ps.brand_id, ps.brand_name  from pm_site ps "
			+ " left join pm_district pd on pd.district_id=ps.district_id left join pm_area pa on pa.area_id = ps.area_id "
			+ " left join pm_cluster pc on pc.cluster_id = ps.cluster_id left join sp_country pcon on pcon.country_id = pd.country_id "
			+ " inner join pm_user_access pua on pua.site_id=ps.site_id inner join sp_users pu on pu.user_id = pua.user_id "
			+ " left join sp_ext_customers sec on sec.cust_cid = ps.operator_id WHERE pu.email_id = ? and sec.cust_cid=?";
	
	public static final String EXT_CUST_SITE_DETAILS_QUERY = "select s.site_id, s.site_name, s.site_code, s.site_owner,s.elec_id_no,"
			+ " s.brand_id, s.brand_name, d.district_id, d.district_name, d.area_id, a.area_name, d.cluster_id, c.cluster_name,s.contact_name, s.email, "
			+ " s.primary_contact_number, s.alt_contact_number, s.site_address1, s.site_address2, s.site_address3, s.site_address4, "
			+ " s.latitude, s.longitude, s.site_number1, s.site_number2, s.attachment_path, s.sales_area_size  from pm_site s "
			+ " left JOIN pm_district d on s.district_id=d.district_id  left JOIN pm_area a on a.area_id=s.area_id "
			+ " left JOIN pm_cluster c on s.cluster_id=c.cluster_id where s.site_id=?";
}
