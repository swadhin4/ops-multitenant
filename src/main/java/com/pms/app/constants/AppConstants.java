package com.pms.app.constants;

public class AppConstants {

	public static final String PASSWORD_RESET_LINK="/forgot/app/password/reset/link/{token}";
	
	public static String email="";
	
	public static final String SP_USER_TENANT="select t.tenant_id tenant_id, t.sp_user_email user_email, st.db_name db_name from sp_user_mapping t "
			+ " inner join sp_tenants st on st.tenant_id = t.tenant_id and  t.sp_user_email=?";
	
	public static final String USER_TENANT="select * from tenants where user_email=?";
	
	
	public static final String ASSET_SUBREPAIRTYPE_QUERY = "select subcategory2_id, subcategory2_name from pm_asset_subcategory2 where "
			+ " subcategory1_id = ? order by subcategory2_name";
	
	public static final String ASSET_SUBCATEGORY_DETAILS_QUERY = "SELECT a.asset_id, ac.category_id,ac.category_name,asc1.subcategory1_id,asc1.asset_subcategory1 "
			+ " FROM pm_asset a " 
			+ " LEFT JOIN pm_asset_category ac ON a.category_id = ac.category_id "
			+ " LEFT JOIN pm_asset_subcategory1 asc1 ON asc1.subcategory1_id = a.subcategory1_id "
			+ " WHERE a.category_id = ? AND a.subcategory1_id = ?  and a.asset_id=? ";
	
	public static final String USER_ROLE_QUERY ="select u.user_id, u.first_name, u.last_name, u.email_id, u.password,u.enabled, "
			+ " r.role_id, r.role_name, r.role_desc, u.sys_password, pc.company_id,pc.company_code, pc.company_name "
			+ " from pm_users u inner join pm_user_role ur  INNER join pm_role r on ur.role_id=r.role_id "
			+ " inner  join pm_company pc where u.company_id=pc.company_id and u.user_id=ur.user_id and u.email_id = ?";
	
	public static final String SP_USER_ROLE_QUERY ="select u.user_id, u.first_name, u.last_name, u.email_id, u.password,u.enabled, "
			+ " r.role_id, r.role_name, r.role_desc, u.sys_password, pc.sp_cid,pc.sp_code, pc.sp_cname "
			+ " from sp_users u inner join sp_user_role ur  INNER join sp_role r on ur.role_id=r.role_id "
			+ " inner  join sp_company pc where u.sp_id=pc.sp_cid and u.user_id=ur.user_id and u.email_id =  ?";
	
	public static final String USER_ROLE_STATUS_MAPPING="select role_status_map_id, role_id, status_id from pm_role_status_mapping where role_id=?";
	
	public static final String USER_SITE_ACCESS_QUERY="select ps.site_id, ps.site_name, ps.district_id, pd.district_name,"
			+ " ps.area_id, pa.area_name, ps.cluster_id, pc.cluster_name, pu.company_id,pcomp.company_name, "
			+ " pcon.country_id, pcon.country_name, ps.site_owner,ps.brand_id, ps.brand_name  from pm_site ps "
			+ " left join pm_district pd on pd.district_id=ps.district_id left join pm_area pa on pa.area_id = ps.area_id "
			+ " left join pm_cluster pc on pc.cluster_id = ps.cluster_id left join pm_country pcon on pcon.country_id = pd.country_id "
			+ " inner join pm_user_access pua on pua.site_id=ps.site_id inner join pm_users pu on pu.user_id = pua.user_id "
			+ " left join pm_company pcomp on pcomp.company_id = pu.company_id WHERE pu.email_id = ?";
	
	public static final String USER_DISTRICT_QUERY = "SELECT dist.district_id,dist.district_name from pm_district dist "
			+ " INNER JOIN pm_company com ON com.country_id = dist.country_id WHERE com.company_id=?";
	
	public static final String USER_AREA_QUERY = "SELECT ar.area_id, ar.area_name from pm_area ar "
			+ " INNER JOIN pm_district dist ON dist.district_id = ar.dist_id WHERE ar.dist_id=?";
	
	public static final String USER_CLUSTER_QUERY = "SELECT cl.cluster_id,cl.cluster_name FROM pm_cluster cl "
			+ " INNER JOIN pm_area ar ON ar.area_id = cl.area_id WHERE cl.district_id=? and ar.area_id = ?";
	
	
	public static final String SITE_INSERT_QUERY = "insert into pm_site (site_name,site_code, district_id,area_id,cluster_id "
			+ " site_owner,brand_id,brand_name,operator_id,attachment_path,site_address1,site_address2,site_address3,site_address4 "
			+ " post_code,latitude,longitude,contact_name,primary_contact_number,alt_contact_number,email,elec_id_no,site_number1 "
			+ " site_number2, sales_area_size, created_by, version) values "
			+ " (:siteName,:siteCode,:districtId,:areaId,:clusterId,:siteOwner,:brandId,:brandName,:operatorId,:attachmentPath "
			+ " ,:addressLine1 ,:addressLine2,:addressLine3,:addressLine4,:zipCode,:siteLat,:siteLong,:siteContactName,:pContactNumber,:sContactNumber"
			+ " ,:siteEmail,:elecId,:siteNumber1,:siteNumber2,:salesArea,:createdBy,0)";
	
	public static final String SITE_DETAILS_QUERY = "select s.site_id, s.site_name, s.site_code, s.site_owner,s.elec_id_no,"
			+ " s.brand_id, s.brand_name, d.district_name, a.area_name, c.cluster_name,s.contact_name, s.email, "
			+ " s.primary_contact_number, s.alt_contact_number, s.site_address1, s.site_address2, s.site_address3, s.site_address4, "
			+ " s.latitude, s.longitude, s.site_number1, s.site_number2, s.attachment_path  from pm_site s "
			+ " left JOIN pm_district d on s.district_id=d.district_id  left JOIN pm_area a on a.area_id=s.area_id "
			+ " left JOIN pm_cluster c on s.cluster_id=c.cluster_id where s.site_id=?";

	public static final String SITE_LICENSE_QUERY = "select license_id, license_name,start_date, end_date, attachment_path from pm_sitelicense where site_id=?";
	
	public static final String SITE_OPERATING_SALES_TIMINGS_QUERY = "select op_id, day_of_week,op_start_time, op_close_time from pm_site_sales_op_time where site_id=?";
	
	public static final String SITE_OPERATING_DELIVERY_TIMINGS_QUERY = "select op_id, day_of_week,op_start_time, op_close_time from pm_site_delivery_op_time where site_id=?";
	
	public static final String INSERT_SALES_TIMINGS_QUERY = "insert into pm_site_sales_op_time (site_id, day_of_week, op_start_time, op_close_time) value (?,?,?,?)";
	
	public static final String INSERT_DELIVERY_TIMINGS_QUERY = "insert into pm_site_delivery_op_time (site_id, day_of_week, op_start_time, op_close_time) value (?,?,?,?)";
	
	public static final String UPDATE_SALES_TIMINGS_QUERY = "update pm_site_sales_op_time set  day_of_week=?, op_start_time=?, op_close_time = ? where op_id=? ";
	
	public static final String UPDATE_DELIVERY_TIMINGS_QUERY = "update pm_site_delivery_op_time set  day_of_week=?, op_start_time=?, op_close_time = ? where op_id=? ";
	
	
	public static final String SITE_SUBMETER_QUERY = "select submeter_id, submeter_number,submeter_user from pm_site_submeter where site_id=?";
	
	public static final String INSERT_SITE_LICENCE_QUERY="insert into pm_sitelicense (site_id, license_name, start_date, end_date, attachment_path,created_by) "
			+ " values(?,?,?,?,?,?)";
	public static final String INSERT_SUBMETER_SITE_QUERY="insert into pm_site_submeter (site_id, submeter_number,submeter_user,created_by) "
			+ " values(?,?,?,?)";
	
	public static final String UPDATE_SUBMETER_SITE_QUERY="update pm_site_submeter set site_id=?, submeter_number =?,submeter_user=? "
			+ " where submeter_id=?";
	
	public static final String ASSET_LIST_QUERY="select ps.asset_id, ps.asset_code, ps.asset_name, p.site_name, p.site_id, ps.model_number, "
			+ " ps.category_id, pc.category_name, ps.location_id, pl.location_name from pm_asset ps "
			+ " left OUTER join pm_asset_category pc on pc.category_id=ps.category_id "
			+ " left OUTER join pm_asset_location pl on pl.location_id=ps.location_id "
			+ " left outer join pm_site p on p.site_id=ps.site_id "
			+ " where ps.site_id in (:siteIds) ";

	public static final String ASSET_DETAILS_QUERY = "SELECT pa.asset_id,pa.asset_name, "
			+ " pa.asset_code, pa.model_number, pa.category_id,pac.category_name, pa.subcategory1_id,pas.asset_subcategory1, "
			+ " pa.sp_id,psp.sp_name,psp.help_desk_email, pa.location_id,pal.location_name, pa.date_commissioned, pa.date_decomissioned, "
			+ " pa.site_id,ps.site_name, pa.content, pa.is_asset_electrical, pa.is_pw_sensor_attached, pa.pw_sensor_number, "
			+ " pa.image_path, pa.document_path, pa.asset_desc, pa.del_flag, pac.asset_type, ps.site_owner,ps.contact_name, ps.email "
			+ " FROM pm_asset pa LEFT JOIN pm_asset_category pac ON pa.category_id=pac.category_id "
			+ " LEFT JOIN pm_asset_subcategory1 pas ON pa.subcategory1_id=pas.subcategory1_id "
			+ " LEFT JOIN pm_service_provider psp ON pa.sp_id=psp.sp_id LEFT JOIN pm_asset_location pal ON "
			+ " pa.location_id=pal.location_id LEFT JOIN pm_site ps ON pa.site_id=ps.site_id "
			+ " WHERE pa.asset_id=?";
	
	public static final String ASSET_CATEGORY_LIST ="select category_id, category_name, asset_type from pm_asset_category";
	
	public static final String ASSET_LOCATION_LIST ="select location_id, location_name from pm_asset_location";
	
	public static final String ASSET_REPAIRTYPE_QUERY = "select subcategory1_id, asset_subcategory1 from pm_asset_subcategory1 where "
			+ " asset_category_id = ? order by asset_subcategory1";
	
	public static final String EXTERNAL_SERVICE_PROVIDER_LIST = "select p.sp_id, p.sp_name, p.sp_email, 'E'"
			+ " from pm_service_provider p left outer join pm_company pc on pc.company_id = p.customer_id where p.customer_id=?";
	
	public static final String EXTERNAL_SERVICE_PROVIDER_QUERY = "select * from pm_service_provider p where p.sp_id=?";
	
	
	public static final String FIND_DUPLICATE_ASSET_QUERY = "select * from pm_asset where asset_code = :assetCode and site_id in (:siteIds ) and del_flag=0";
	
	
	public static final String ASSET_UPDATE_QUERY = "update pm_asset set asset_code=?, site_id=?, asset_name=?, asset_desc=?, "
			+ " model_number=?, category_id=?, content=?, location_id=?, image_path=?,document_path=?, sp_id=?, "
			+ " date_commissioned=?, date_decomissioned=?, is_asset_electrical=?, is_pw_sensor_attached=?, "
			+ " pw_sensor_number=?, subcategory1_id=?, modified_date=?,modified_by=? where asset_id=?";

	public static final String ASSET_DELETE_QUERY ="UPDATE pm_asset set del_flag=1 where asset_id=?";
	
	
	public static final String TICKET_LIST_QUERY="select pct.id, pct.ticket_number,pct.ticket_title,pct.site_id, ps.site_name,pct.asset_id, pa.asset_name, pct.created_on, "
			+ " pct.sla_duedate, psp.sp_id, psp.sp_name, pct.status_id, pst.status, pst.description from pm_cust_ticket pct "
			+ " left outer join pm_site ps on ps.site_id=pct.site_id left outer join pm_asset pa on pa.asset_id=pct.asset_id "
			+ " left outer join pm_service_provider psp on psp.sp_id=pa.sp_id left outer join pm_status pst "
			+ " on pst.status_id=pct.status_id where pct.site_id in (:siteIds) ";
	
	public static final String EXTSP_TICKET_LIST_QUERY="select pct.id, pct.ticket_number,pct.ticket_title,pct.site_id, ps.site_name,pct.asset_id, pa.asset_name, pct.created_on, "
			+ " pct.sla_duedate, psp.sp_id, psp.sp_name, pct.status_id, pst.status, pst.description from pm_cust_ticket pct "
			+ " left outer join pm_site ps on ps.site_id=pct.site_id left outer join pm_asset pa on pa.asset_id=pct.asset_id "
			+ " left outer join pm_service_provider psp on psp.sp_id=pa.sp_id left outer join pm_status pst "
			+ " on pst.status_id=pct.status_id where pct.assigned_to = ? ";
	
	public static final String SITE_ASSET_LIST_QUERY="select ps.asset_id, ps.asset_code, ps.asset_name, pc.asset_type, "
			+ " psp.sp_id, psp.sp_name, psp.help_desk_email,pc.category_id, pc.category_name,pas.subcategory1_id, pas.asset_subcategory1 from pm_asset ps "
			+ " left OUTER join pm_asset_category pc on pc.category_id=ps.category_id "
			+ " LEFT OUTER JOIN pm_asset_subcategory1 pas ON pas.subcategory1_id = ps.subcategory1_id"
			+ " left OUTER join pm_service_provider psp on psp.sp_id = ps.sp_id "
			+ " where ps.site_id = ? ";
	public static final String TICKET_CATEGORY_QUERY ="select * from pm_ticket_category ";
	
	public static final String TICKETS_STATUS_QUERY ="select * from pm_status where category=?";
	
	public static final String TICKET_PRIORITY_SP_SLA_QUERY = "select ps.settings_id, ps.category_id, ptc.id, ptc.ticket_category, ptp.priority_id, "
			+ " ptp.description, psp.sp_id, psp.sp_name, pss.duration, pss.unit from pm_ct_priority_settings ps "
			+ " left outer join pm_ticket_category ptc on ptc.id = ps.category_id "
			+ " left outer join pm_ticket_priority ptp on ptp.priority_id = ps.priority_id "
			+ " left outer join pm_sp_sla pss on pss.priority_id=ps.priority_id "
			+ " left outer join pm_service_provider psp on psp.sp_id=pss.sp_id where pss.sp_id=? and ptc.id=?";
	
	public static final String INSERT_TICKET_QUERY = "insert into pm_cust_ticket(ticket_number, ticket_title,ticket_desc, status_id,ticket_category,"
			+ " site_id,asset_id,asset_category_id,asset_subcategory1_id,asset_subcategory2_id,priority,assigned_to, "
			+ " ticket_starttime, created_by, created_on) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())";
	
	public static final String UPDATE_TICKET_QUERY = "update pm_cust_ticket set ticket_title=?, ticket_desc=?, "
			+ " sla_duedate = ?, ticket_category=?, status_id=?,priority=?,close_code=?,close_note=?,closed_on=?,"
			+ "  is_rootcause_resolved=?,service_restoration_ts=?, closed_by=?,"
			+ " modified_by=?, modified_on=? where id=?";
	
	public static final String LAST_INCIDENT_NUMBER_QUERY="select id from pm_cust_ticket order by id desc limit 1"; 
	
	public static final String INSERT_TICKET_ATTACHMENT_QUERY = "insert into pm_cust_ticket_attachment(ticket_id,ticket_number, attachment_path, "
			+ "created_by,created_on) values(?,?,?,?,NOW())";
	
	public static final String TICKET_SELECTED_QUERY = "SELECT "
+" ct.id, ct.ticket_number, ct.ticket_title, "
+" ct.ticket_desc, "
+" ct.status_id,sts.`status`,sts.description, "
+" ct.ticket_category as ticket_category_id,tc.ticket_category, "
+" ct.site_id,st.site_name,st.site_number1,st.site_number2,st.primary_contact_number,"
+ " st.site_address1,st.site_address2,st.site_address3,st.site_address4,st.post_code, "
+" ct.asset_id,ast.asset_name,ast.asset_code,ast.model_number, "
+" ct.asset_category_id,ac.category_name, "
+" ct.asset_subcategory1_id,ac1.asset_subcategory1, "
+" ct.asset_subcategory2_id,ac2.subcategory2_name, "
+" ct.priority, ct.sla_duedate, ct.ticket_starttime, "
+" ct.service_restoration_ts, ct.close_code,clc.closed_code_desc,"
+" ct.is_rootcause_resolved, ct.close_note, ct.assigned_to,sp.sp_name, "
+" ct.closed_by,pu.first_name,pu.last_name,pu.phone, "
+" ct.closed_on,  ct.created_by,  ct.created_on  FROM pm_cust_ticket ct "
+" LEFT JOIN pm_site st ON ct.site_id = st.site_id "
+" LEFT JOIN pm_asset ast ON ct.asset_id = ast.asset_id "
+" LEFT JOIN pm_asset_category ac ON ct.asset_category_id = ac.category_id "
+" LEFT JOIN pm_asset_subcategory1 ac1 ON ct.asset_subcategory1_id = ac1.subcategory1_id "
+" LEFT JOIN pm_asset_subcategory2 ac2 ON ct.asset_subcategory2_id = ac2.subcategory2_id "
+" LEFT JOIN pm_ticket_category tc ON ct.ticket_category = tc.id "
+" LEFT JOIN pm_status sts ON ct.status_id = sts.status_id "
+" LEFT JOIN pm_service_provider sp ON ct.assigned_to = sp.sp_id "
+" LEFT JOIN pm_closecode clc ON ct.close_code = clc.closed_code "
+" LEFT JOIN pm_users pu ON ct.created_by = pu.email_id "
+" WHERE ct.id = ?";

	public static final String TICKET_ATTACHMENTS = "select id, ticket_number, attachment_path from pm_cust_ticket_attachment where ticket_id=?";

	public  static final String INSERT_TICKET_COMMENT_QUERY = "INSERT into pm_cust_ticket_comment(ticket_id,custticket_number,comment, created_by, created_date) "
			+ " values(?,?,?,?,NOW())";

	public static final String TICKET_COMMENTS = "select * from pm_cust_ticket_comment where ticket_id=?";

	public static final String TICKET_HISTORY = "select * from pm_ct_historic_activities where ticket_number=? order by ts desc";
	
	public static final String EXT_SP_LINKED_TICKETS = "select * from pm_cust_sp_ticketmapping where cust_ticket_id=? and del_flag=0";
	
	public static final String RELATED_TICKETS_QUERY = "select ct.id, ct.ticket_number, ct.ticket_title, ct.status_id, "
			+ " sts.`status`, ct.site_id,st.site_name,ct.sla_duedate, "
			+ " ct.asset_id,ast.asset_name, ct.created_on, ct.assigned_to, "
			+ " sp.sp_name FROM pm_cust_ticket ct "
			+" LEFT JOIN pm_site st ON ct.site_id = st.site_id "
			+" LEFT JOIN pm_status sts ON ct.status_id = sts.status_id "
			+" LEFT JOIN pm_asset ast ON ct.asset_id = ast.asset_id "
			+" LEFT JOIN pm_service_provider sp ON ct.assigned_to = sp.sp_id "
			+" where  ct.id <> ? and ct.site_id = ?";
	
	public static final String INSERT_TICKET_MAPPING_QUERY= "INSERT INTO pm_cust_sp_ticketmapping "
			+ " (cust_ticket_id,customer_ticket_no,spticket_no,closed_flag,closed_time,created_by,"
			+ " created_on,del_flag) VALUES (?,?,?,?,?,?,NOW(),0)";

	public static final String STATUS_CHANGED_LINKED_TICKET = "UPDATE pm_cust_sp_ticketmapping set closed_flag='CLOSED', "
			+ "closed_time=NOW(), modified_by=?, modified_on=NOW() where id=? ";
	
	public static final String DELETE_LINKED_TICKET = "UPDATE pm_cust_sp_ticketmapping set del_flag=1, "
			+ " modified_by=?, modified_on=NOW() where id=? ";

	public static final String INSERT_TICKET_ESCALATION_QUERY = "INSERT INTO pm_ct_escalations "
			+ " (ticket_id, ticket_number, esc_level, escalated_by,esc_id, escalated_date)"
			+ " values (?,?,?,?,?,NOW())";
	
	public static final String TICKET_ESCALATIONS = "select * from pm_ct_escalations where ticket_id=?";
	
	public static final String TICKET_BY_ESCID = "select * from pm_ct_escalations where ticket_id=? and esc_id=?";
	
	public static final String SP_ESCALATIONS_QUERY= "select * from pm_sp_escalation_levels where sp_id=?";
	
	public static final String TICKET_FINANCE_SELECT_QUERY= "select * from pm_cust_ticket_financials where ticket_id=?";
	
	public static final String TICKET_FINANCE_BY_ID= "select * from pm_cust_ticket_financials where cost_id=?";
	public static final String DELETE_TICKET_FINANCE_BY_ID= "delete from pm_cust_ticket_financials where cost_id=?";
	
	public static final String TICKET_FINANCE_INSERT_QUERY= "INSERT into pm_cust_ticket_financials "
			+ " (ticket_id, cost_name, cost, charge_back, billable, created_by) values(?, ?,?,?, ?,?)";
	
	public static final String TICKET_FINANCE_UPDATE_QUERY= "UPDATE  pm_cust_ticket_financials "
			+  " set cost_name=?, cost=?, charge_back=?, billable=?, modified_by=?, modified_on=NOW() where cost_id=?";
	
	public static final String USER_LIST_QUERY= "SELECT u.user_id,u.first_name,u.last_name,u.email_id,u.phone,"
			+ " ur.role_id,r.role_desc as description,u.enabled, pc.company_name from pm_users u "
			+ " LEFT JOIN pm_user_role ur ON u.user_id = ur.user_id LEFT JOIN pm_role r ON r.role_id = ur.role_id"
			+ " LEFT JOIN pm_company pc on pc.company_id=u.company_id WHERE u.company_id = ?";
	
	public static final String SP_USER_LIST_QUERY= "SELECT u.user_id,u.first_name,u.last_name,u.email_id,u.phone,"
			+ " ur.role_id,r.role_desc as description,u.enabled, pc.sp_cname company_name from sp_users u "
			+ " LEFT JOIN sp_user_role ur ON u.user_id = ur.user_id LEFT JOIN sp_role r ON r.role_id = ur.role_id"
			+ " LEFT JOIN sp_company pc on pc.sp_cid=u.sp_id WHERE u.sp_id = ?";

	public static final String USER_ROLE_LIST_QUERY = "select * from pm_role";
	
	public static final String SP_USER_ROLE_LIST_QUERY = "select * from sp_role";
	
	public static final String UPDATE_USER_ROLE = "UPDATE pm_user_role set role_id=?  where user_id=?";
	
	public static final String UPDATE_USER_STATUS = "UPDATE pm_users set enabled=?  where user_id=?";
	
	public static final String INSERT_NEW_USER_QUERY = "INSERT into pm_users(first_name, last_name, email_id, login_name, phone, password, company_id, enabled, created_date, sys_password, version) "
			+ " values(?,?,?,?,?, ?,?,?,NOW(),'YES',0)";
	
	public static final String INSERT_NEW_USER_ROLE_QUERY = "INSERT into pm_user_role(user_id, role_id)  values(?,?)";
	
	public static final String INSERT_SP_NEW_USER_ROLE_QUERY = "INSERT into sp_user_role(user_id, role_id)  values(?,?)";
	
	public static final String INSERT_SERVICEPROVIDER_USER_QUERY = "insert into sp_users (sp_id,first_name,last_name,email_id,password,sys_password,phone,enabled,created_date,created_by) "
			+ " values(?,?,?,?,?,'NO',?,?,NOW(),?)";
	public static final String SERVICEPROVIDER_USERBY_USERNAME_QUERY = "select user_id,sp_id,first_name,last_name,email_id,phone,enabled,created_by,created_date,modified_by,modified_date from sp_users where email_id=?";
	
	public static final String SERVICEPROVIDER_USERS_QUERY = "select user_id,sp_id,first_name,last_name,email_id,phone,enabled,created_by,created_date,modified_by,modified_date from sp_users";
	
	public static final String SERVICEPROVIDER_USERS_ROLE_QUERY = "select user_role_id,user_id,role_id,created_by,created_date,modified_by,modified_date from sp_user_role where user_id=?";
	
	/*public static final String SERVICEPROVIDER_USERS_CUSTOMERS_QUERY = "select access_id,user_id,sp_cust_id,created_on,created_by from sp_user_access where user_id=? and del_flag=0";*/
	
	public static final String SERVICEPROVIDER_USERS_CUSTOMERS_QUERY = "select sp_cust_id,customer_code,customer_name,country_name, access_id, del_flag from vw_user_cust_mapping where user_id=?";
	
	public static final String SP_ALL_CUSTOMERS_QUERY = "select sp.sp_cust_id, spc.sp_cname, spc.sp_code, sp.customer_code, sp.customer_name, sp.cust_country_id, "
			+ " c.country_code, c.country_name from sp_customers sp left join sp_country c"
			+ " on c.country_id = sp.cust_country_id left join sp_company spc on spc.sp_cid=sp.sp_id"
			+ " where sp.sp_id=? and spc.sp_code=?";
	
	public static final String INSERT_SERVICEPROVIDER_USER_ROLE_QUERY = "insert into sp_user_role (user_id,role_id,created_date,created_by) "
			+ " values(?,?,?,?)";
	public static final String INSERT_SERVICEPROVIDER_USER_ACCESS_QUERY = "insert into sp_user_access (user_id,sp_cust_id,del_flag,created_on,created_by) "
			+ " values(?,?,0,NOW(),?)";
	public static final String UPDATE_SERVICEPROVIDER_USER_QUERY = "update sp_users set sp_id=?, first_name=?, last_name=?, email_id=?, user_password=?, phone=?, enabled=?, modified_date=?, modified_by=? where user_id=? ";
	
	public static final String UPDATE_SERVICEPROVIDER_USER_ROLE_QUERY = "update sp_user_role set role_id=?, modified_by=?, modified_date=? where user_id=? ";
	
	public static final String DELETE_SERVICEPROVIDER_USER_ACCESS_QUERY = "update sp_user_access set del_flag=1 where sp_cust_id=? ";

	public static final String EXT_SP_USER_TENANT="select sp_username as username, sp_email as user_email, db_name from ext_user_sp_mapping where sp_username=?";
	
	public static final String EXT_SP_USER_DETAIL_QUERY = "	select s.sp_id, s.sp_username, s.sp_email, s.sp_name,s.access_key, s.customer_id, pc.company_name, pc.company_code"
			+ " from pm_service_provider s left outer join pm_company pc on pc.company_id=s.customer_id"
			+ " where s.sp_username= ?";

	public static final String EXT_SP_CREATE_QUERY = "insert into pm_service_provider (sp_username, sp_name,sp_code, country_id, sp_desc, sp_email,customer_id,"
			+ "created_date, created_by,help_desk_number, help_desk_email,access_key,sla_description) values (?,?,?,?,?,?,?,NOW(),?,?,?,?,?)";
	
	
	public static final String INSERT_EXT_SP_TENANAT_MAP_QUERY= "insert into ext_user_sp_mapping(sp_username,sp_email,db_name) values(?,?,?)";

	public static final String REGION_LIST_QUERY = "select * from pm_region";

	public static final String COUNTRY_LIST_QUERY = "select * from pm_country where region_id=?";
	
	public static final String INSERT_SP_ESCALATIONS_QUERY  = "insert into pm_sp_escalation_levels(sp_id, esc_level, esc_person,esc_email,created_by,created_on)"
			+ " values(?,?,?,?,?,NOW())";
	
	public static final String INSERT_SP_SLA_QUERY  = "insert into pm_sp_sla(sp_id, priority_id, duration,unit,created_by,created_on)"
			+ " values(?,?,?,?,?,NOW())";
	
	public static final String LAST_SP_ID_QUERY="select sp_id from pm_service_provider order by sp_id desc limit 1";
	
	public static final String EXT_SP_DETAILS_QUERY ="select s.sp_id, s.sp_username, s.sp_email, s.sp_name,s.access_key, s.customer_id, pc.company_name, pc.company_code"
			+ " from pm_service_provider s left outer join pm_company pc on pc.company_id=s.customer_id"
			+ " where s.sp_id= ?";
	
	public static final String EXT_SP_UPDATE_QUERY = "update pm_service_provider set sp_name = ?,sp_code=?, country_id=?, sp_desc=?, sp_email=?,customer_id=?,"
			+ "modified_date=NOW(), modified_by=?,help_desk_number=?, help_desk_email=?,sla_description=? where sp_id=?";

	
	public static final String UPDATE_SP_ESCALATIONS_QUERY  = "update pm_sp_escalation_levels set esc_person=?,esc_email=? where esc_id=? and sp_id=?";

	
	public static final String UPDATE_SP_SLA_QUERY  = "update pm_sp_sla set duration=?,unit=? where sla_id=? and sp_id=?" ;
	
	
	public static final String EXT_SERVICE_PROVIDER_LIST = "select ps.sp_id, ps.sp_name, ps.sp_code, ps.sp_email,ps.sp_desc, "
			+ " ps.help_desk_number, ps.help_desk_email, ps.sla_description ,ps.country_id, pc.company_name, pc.company_code, po.country_name ,"
			+ " pr.region_id, pr.region_name from pm_service_provider ps "
			+ " left join pm_company pc on pc.company_id=ps.customer_id "
			+ " left join pm_country po on po.country_id=ps.country_id "
			+ " left join pm_region pr on po.region_id=pr.region_id order by sp_name";
	
	public static final String EXT_SERVICE_PROVIDER_INFO = "select ps.sp_id, ps.sp_name, ps.sp_code, ps.sp_email,ps.sp_desc, "
			+ " ps.help_desk_number, ps.help_desk_email, ps.sla_description, ps.country_id, pc.company_name, pc.company_code, po.country_name ,"
			+ " pr.region_id, pr.region_name from pm_service_provider ps "
			+ " left join pm_company pc on pc.company_id=ps.customer_id "
			+ " left join pm_country po on po.country_id=ps.country_id "
			+ " left join pm_region pr on po.region_id=pr.region_id where sp_id = ? ";
	
	public static final String EXT_SP_ESCALATIONS = "select esc_id, esc_level, esc_person, esc_email from pm_sp_escalation_levels where sp_id=?";
	
	public static final String EXT_SP_PRIORITY = "select sla_id, priority_id, duration, unit from pm_sp_sla where sp_id=?";
}
