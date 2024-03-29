package com.pms.app.constants;

public class RSPCustomerConstants {

	
	public static final String RSP_REGION_LIST_QUERY = "select * from sp_region";
	
	public static final String UPDATE_USER_PASSWORD = "update sp_users set password=?, sys_password='NO' where email_id=?";
	
	public static final String FORGOT_USER_PASSWORD = "update sp_users set password=?, sys_password='YES' where email_id=?";
	
	public static final String CHECK_UNIQUE_SP_USER_PHONE = "select email_id, phone from sp_users where phone=?";
	
	public static final String UPDATE_SP_USER_PROFILE = "update sp_users set first_name=?, last_name=?, phone=? where email_id=?";

	public static final String RSP_COUNTRY_LIST_QUERY = "select * from sp_country where region_id=?";

	public static final String SAVE_RSP_EXTERNAL_CUSTOMER_QUERY = "insert into sp_ext_customers (cust_name, cust_code, country_id,"
			+ " p_email, s_email, p_contact_no, s_contact_no, sla_desc, created_by, created_date) "
			+ " values(?,?,?,?,?,?,?,?,?,NOW())";
	
	public static final String UPDATE_RSP_EXTERNAL_CUSTOMER_QUERY = "update sp_ext_customers set cust_name=?, cust_code=?, country_id=?,"
			+ "p_email=?, s_email=?, p_contact_no=?, s_contact_no=?, sla_desc=? where cust_cid=?";
	
	public static final String SELECT_RSP_EXTERNAL_CUSTOMER_QUERY = "select * from sp_ext_customers where cust_code=? and p_email=?";
	
	public static final String RSP_EXTERNAL_CUSTOMER_LIST_QUERY ="select e.cust_cid, e.cust_name, "
			+ " e.cust_code,e.country_id, sc.country_name, sc.region_id, sr.region_name, e.p_email, "
			+ " e.s_email, e.s_contact_no, e.p_contact_no, e.sla_desc  FROM sp_ext_customers e " 
			+ " LEFT JOIN sp_country sc ON sc.country_id = e.country_id"
			+ " LEFT JOIN sp_region sr ON sr.region_id = sc.region_id";

	public static final String INSERT_EXTERNAL_CUSTOMER_SLA_LIST = "insert into sp_ext_cust_sla(ext_cust_id, priority_id, duration, unit, created_by, created_on) "
			+ " values (?,?,?,?,?,NOW())";
	
	public static final String UPDATE_EXTERNAL_CUSTOMER_SLA_LIST = "update sp_ext_cust_sla set  duration=?, unit=? where sla_id=? and ext_cust_id=?";
	
	public static final String UPDATE_EXTERNAL_CUSTOMER_SLA_DESCRIPTION = "update sp_ext_customers set  sla_desc=? where cust_cid=?";

	public static final String RSP_EXTERNAL_CUSTOMER_SLA_LIST_QUERY = "select e.sla_id, e.priority_id, e.duration, e.unit,"
			+ " p.description from sp_ext_cust_sla e LEFT JOIN pm_ticket_priority p "
			+ " ON p.priority_id = e.priority_id where e.ext_cust_id=?";
	
	public static final String INSERT_EXT_CUSTOMER_SITE_CREATE="INSERT INTO pm_site (site_name, site_owner, operator_id, district_id, area_id,"
			+ "  cluster_id, elec_id_no, site_number1, site_number2, attachment_path,sales_area_size, brand_id, brand_name, contact_name, email,  "
			+ "  latitude, longitude,  primary_contact_number, alt_contact_number, site_address1, site_address2, site_address3, site_address4, post_code,   "
			+ "  created_by, created_date) "
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
	
	public static final String ASSET_CREATE_QUERY = "INSERT INTO pm_asset "
			+ " (asset_name,asset_code,model_number, location_id, category_id,subcategory1_id,rsp_id,sp_type,"
			+ " image_path, document_path,   date_commissioned, date_decomissioned, content, site_id,  "
			+ " is_asset_electrical,  is_pw_sensor_attached, pw_sensor_number,   asset_desc,  "
			+ "  created_by,  del_flag, version) "
			+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, 0, 0)";
	
	public static final String ASSET_UPDATE_QUERY = "update pm_asset set asset_code=?, site_id=?, asset_name=?, asset_desc=?, "
			+ " model_number=?, category_id=?, content=?, location_id=?, image_path=?,document_path=?,  date_commissioned=?, "
			+ " date_decomissioned=?, is_asset_electrical=?, is_pw_sensor_attached=?, "
			+ " pw_sensor_number=?, subcategory1_id=?, modified_date=NOW(),modified_by=? where asset_id=?";
	
	public static final String ASSET_DETAILS_QUERY = "SELECT pa.asset_id,pa.asset_name, "
			+ " pa.asset_code, pa.model_number, pa.category_id,pac.category_name, pa.subcategory1_id,pas.asset_subcategory1, "
			+ " pa.rsp_id, psrp.sp_cname sp_name, pa.sp_type, ps.email rsp_help_deskemail, pa.location_id, "
			+ " pal.location_name, pa.date_commissioned, pa.date_decomissioned, "
			+ " pa.site_id,ps.site_name, pa.content, pa.is_asset_electrical, pa.is_pw_sensor_attached, pa.pw_sensor_number, "
			+ " pa.image_path, pa.document_path, pa.asset_desc, pa.del_flag, pac.asset_type, ps.site_owner,ps.contact_name, ps.email "
			+ " FROM pm_asset pa LEFT JOIN pm_asset_category pac ON pa.category_id=pac.category_id "
			+ " LEFT JOIN pm_asset_subcategory1 pas ON pa.subcategory1_id=pas.subcategory1_id "
			+ " LEFT JOIN sp_company psrp on pa.rsp_id=psrp.sp_cid "
			+ " LEFT JOIN pm_asset_location pal ON "
			+ " pa.location_id=pal.location_id LEFT JOIN pm_site ps ON pa.site_id=ps.site_id "
			+ " WHERE pa.asset_id=?";
	
	public static final String EXT_CUSTOMER_TICKETS_QUERY = "select pct.id, pct.ticket_number,pct.site_id,pct.asset_id,pct.ticket_title,pct.created_on,pct.created_by,pct.sla_duedate, "
			+ " pct.status_id,prs.sp_cname sp_name,ps.site_name,pa.asset_name,pst.status from pm_cust_ticket pct "
			+ " left join sp_company prs on prs.sp_cid=pct.rassigned_to "
			+ "  left join pm_site ps on pct.site_id=ps.site_id "
			+ " left join pm_asset pa on pct.asset_id=pa.asset_id "
			+ " left join sp_ext_customers sec on  sec.cust_cid=ps.operator_id "
			+ " left join pm_status pst on pct.status_id=pst.status_id where prs.sp_code=? and sec.cust_cid=?";
	
	public static final String RSP_USER_LIST_QUERY = " select su.user_id,su.first_name,su.last_name,su.email_id from sp_users su ";
	
	public static final String RSP_EXT_CUST_ASSET_LIST_QUERY="select ps.asset_id, ps.asset_code, ps.asset_name, pc.asset_type, "
			+ " ps.sp_type, ps.rsp_id, spc.sp_cname rsp_name, spc.sp_code, "
			+ " sec.p_email help_desk_email, pc.category_id, pc.category_name,pas.subcategory1_id, pas.asset_subcategory1 "
			+ " from pm_asset ps left OUTER join pm_asset_category pc on pc.category_id=ps.category_id "
			+ " LEFT OUTER JOIN pm_asset_subcategory1 pas ON pas.subcategory1_id = ps.subcategory1_id"
		 	+ " LEFT JOIN sp_company spc on spc.sp_cid=ps.rsp_id "
		 	+ " LEFT JOIN pm_site psite on ps.site_id=psite.site_id "
		 	+ " LEFT JOIN sp_ext_customers sec on sec.cust_cid=psite.operator_id "
			+ " where ps.site_id = ? ";
	
	public static final String TICKET_PRIORITY_RSP_EXTCUST_SLA_QUERY = "select ps.settings_id, ps.category_id, ptc.id, ptc.ticket_category, ptp.priority_id, "
			+ " ptp.description, psp.cust_cid sp_id, psp.cust_name sp_name, pss.duration, pss.unit from pm_ct_priority_settings ps "
			+ " left outer join pm_ticket_category ptc on ptc.id = ps.category_id "
			+ " left outer join pm_ticket_priority ptp on ptp.priority_id = ps.priority_id "
			+ " left outer join sp_ext_cust_sla pss on pss.priority_id=ps.priority_id "
			+ " left outer join sp_ext_customers psp on psp.cust_cid=pss.ext_cust_id where pss.ext_cust_id=? and ptc.id=?";
	
	public static final String LAST_SP_EXTCUST_INCIDENT_NUMBER_QUERY="select id from pm_cust_ticket order by id desc limit 1";
	
	public static final String INSERT_SP_EXTCUST_TICKET_QUERY = "insert into pm_cust_ticket(ticket_number, ticket_title,ticket_desc, status_id,ticket_category,"
			+ " site_id,asset_id,asset_category_id,asset_subcategory1_id,asset_subcategory2_id,priority,rassigned_to, assigned_agent, "
			+ " ticket_starttime, created_by, created_on) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
	
	public static final String RSP_EXTCUST_TICKET_SELECTED_QUERY = "SELECT "
			+" ct.id, ct.ticket_number, ct.ticket_title, "
			+" ct.ticket_desc, "
			+" ct.status_id,sts.`status`,sts.description, "
			+" ct.ticket_category as ticket_category_id,tc.ticket_category, "
			+" ct.site_id,st.site_name,st.site_number1,st.site_number2,st.primary_contact_number,"
			+ " st.site_address1,st.site_address2,st.site_address3,st.site_address4,st.post_code, st.site_owner, st.email,"
			+" ct.asset_id,ast.asset_name,ast.asset_code,ast.model_number,ast.date_commissioned, "
			+" ct.asset_category_id,ac.category_name, "
			+" ct.asset_subcategory1_id,ac1.asset_subcategory1, "
			+" ct.asset_subcategory2_id,ac2.subcategory2_name, "
			+" ct.priority, ct.sla_duedate, ct.ticket_starttime, "
			+" ct.service_restoration_ts, ct.close_code,clc.closed_code_desc,"
			+" ct.is_rootcause_resolved, ct.close_note, ct.rassigned_to,ct.assigned_agent, sp.sp_cname rsp_name, "
			+" ct.closed_by, ct.closed_on,  ct.created_by,  ct.created_on  FROM pm_cust_ticket ct "
			+" LEFT JOIN pm_site st ON ct.site_id = st.site_id "
			+" LEFT JOIN pm_asset ast ON ct.asset_id = ast.asset_id "
			+" LEFT JOIN pm_asset_category ac ON ct.asset_category_id = ac.category_id "
			+" LEFT JOIN pm_asset_subcategory1 ac1 ON ct.asset_subcategory1_id = ac1.subcategory1_id "
			+" LEFT JOIN pm_asset_subcategory2 ac2 ON ct.asset_subcategory2_id = ac2.subcategory2_id "
			+" LEFT JOIN pm_ticket_category tc ON ct.ticket_category = tc.id "
			+" LEFT JOIN pm_status sts ON ct.status_id = sts.status_id "
			+" LEFT JOIN sp_company sp ON ct.rassigned_to = sp.sp_cid "
			+" LEFT JOIN pm_closecode clc ON ct.close_code = clc.closed_code "
			+" WHERE ct.id = ?";
	
	public static final String RSP_EXT_CUSTOMER_RELATED_TICKETS_QUERY = "select ct.id, ct.ticket_number, ct.ticket_title, ct.status_id, "
			+ " sts.`status`, ct.site_id,st.site_name,ct.sla_duedate, "
			+ " ct.asset_id,ast.asset_name, ct.created_on, ct.rassigned_to, "
			+ " sp.sp_cname sp_name FROM pm_cust_ticket ct "
			+" LEFT JOIN pm_site st ON ct.site_id = st.site_id "
			+" LEFT JOIN pm_status sts ON ct.status_id = sts.status_id "
			+" LEFT JOIN pm_asset ast ON ct.asset_id = ast.asset_id "
			+" LEFT JOIN sp_company sp ON ct.rassigned_to = sp.sp_cid "
			+" where  ct.id <> ? and ct.site_id = ? and ct.rassigned_to = ?";
}
