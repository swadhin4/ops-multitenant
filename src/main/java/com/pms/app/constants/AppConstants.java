package com.pms.app.constants;

public class AppConstants {

	public static final String PASSWORD_RESET_LINK="/forgot/app/password/reset/link/{token}";
	
	public static String email="";
	
	
	
	public static final String ASSET_SUBREPAIRTYPE_QUERY = "select subcategory2_id, subcategory2_name from pm_asset_subcategory2 where "
			+ " subcategory1_id = ? order by subcategory2_name";
	
	public static final String ASSET_SUBCATEGORY_DETAILS_QUERY = "SELECT a.asset_id, ac.category_id,ac.category_name,asc1.subcategory1_id,asc1.asset_subcategory1 "
			+ " FROM pm_asset a " 
			+ " LEFT JOIN pm_asset_category ac ON a.category_id = ac.category_id "
			+ " LEFT JOIN pm_asset_subcategory1 asc1 ON asc1.subcategory1_id = a.subcategory1_id "
			+ " WHERE a.category_id = ? AND a.subcategory1_id = ?  and a.asset_id=? ";
	
	public static final String USER_ROLE_QUERY ="select u.user_id, u.first_name, u.last_name, u.email_id, u.password,u.enabled, "
			+ " r.role_id, r.role_name, r.role_desc, u.sys_password, pc.company_id, pc.company_name "
			+ " from pm_users u inner join pm_user_role ur  INNER join pm_role r on ur.role_id=r.role_id "
			+ " inner  join pm_company pc where u.company_id=pc.company_id and u.user_id=ur.user_id and u.email_id = ?";
	
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
			+ " pa.sp_id,psp.sp_name, pa.location_id,pal.location_name, pa.date_commissioned, pa.date_decomissioned, "
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
	
	public static final String SITE_ASSET_LIST_QUERY="select ps.asset_id, ps.asset_code, ps.asset_name, pc.asset_type, "
			+ " psp.sp_id, psp.sp_name, pc.category_id, pc.category_name,pas.subcategory1_id, pas.asset_subcategory1 from pm_asset ps "
			+ " left OUTER join pm_asset_category pc on pc.category_id=ps.category_id "
			+ " LEFT OUTER JOIN pm_asset_subcategory1 pas ON pas.subcategory1_id = ps.subcategory1_id"
			+ " left OUTER join pm_service_provider psp on psp.sp_id = ps.sp_id "
			+ " where ps.site_id = ? ";
	public static final String TICKET_CATEGORY_QUERY ="select * from pm_ticket_category ";
	
	public static final String TICKETS_STATUS_QUERY ="select * from pm_status where category=?";
	
	public static final String TICKET_PRIORITY_SP_SLA_QUERY = "select ps.settings_id, ps.category_id, ptc.id, ptc.ticket_category, ptp.priority_id, "
			+ " ptp.priority, psp.sp_id, psp.sp_name, pss.duration, pss.unit from pm_ct_priority_settings ps "
			+ " left outer join pm_ticket_category ptc on ptc.id = ps.category_id "
			+ " left outer join pm_ticket_priority ptp on ptp.priority_id = ps.priority_id "
			+ " left outer join pm_sp_sla pss on pss.priority_id=ps.priority_id "
			+ " left outer join pm_service_provider psp on psp.sp_id=pss.sp_id where pss.sp_id=? and ptc.id=?";
}
