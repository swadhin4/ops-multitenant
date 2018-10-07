package com.pms.app.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SiteDeliveryVO;
import com.pms.app.view.vo.SiteLicenceVO;
import com.pms.app.view.vo.SiteOperationVO;
import com.pms.app.view.vo.SiteSubmeterVO;
import com.pms.web.util.ApplicationUtil;

@Repository
public class SiteDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(SiteDAO.class);
	
	public SiteDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}
	
	public List<CreateSiteVO> getSiteList(String username){
		LOGGER.info("SiteDAO -- getSiteList -- Getting Site List for User : "+ username);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CreateSiteVO> siteList =  jdbcTemplate.query(AppConstants.USER_SITE_ACCESS_QUERY, new Object[]{username}, new ResultSetExtractor<List<CreateSiteVO>>() {
			@Override
			public List<CreateSiteVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<CreateSiteVO> siteList = new ArrayList<CreateSiteVO>();
				while (rs.next()) {
					CreateSiteVO siteVO = new CreateSiteVO();
					siteVO.setSiteId(rs.getLong("site_id"));
					siteVO.setSiteName(rs.getString("site_name"));
					siteVO.setDistrictId(rs.getLong("district_id"));
					siteVO.setDistrictName(rs.getString("district_name"));
					siteVO.setAreaId(rs.getLong("area_id"));
					siteVO.setAreaName(rs.getString("area_name"));
					siteVO.setClusterId(rs.getLong("cluster_id"));
					siteVO.setClusterName(rs.getString("cluster_name"));
					siteVO.setCountryId(rs.getLong("country_id"));
					siteVO.setCountryName(rs.getString("country_name"));
					siteVO.setCompanyId(rs.getLong("company_id"));
					siteVO.setCompanyName(rs.getString("company_name"));
					siteVO.setSiteOwner(rs.getString("site_owner"));
					siteVO.setOwner(rs.getString("site_owner"));
					siteVO.setBrandId(rs.getLong("brand_id"));
					siteVO.setBrandName(rs.getString("brand_name"));
					siteList.add(siteVO);
                }
				LOGGER.info("SiteDAO -- getSiteList -- Total Site List : "+ siteList.size());
				return siteList;
			}
		});
		return siteList==null?Collections.EMPTY_LIST:siteList;
	}
	
	public CreateSiteVO getSiteDetails(Long siteId){
		LOGGER.info("SiteDAO -- getSiteDetails -- Getting Site details for selcted site : "+ siteId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		CreateSiteVO siteVO =  jdbcTemplate.query(AppConstants.SITE_DETAILS_QUERY, new Object[]{siteId}, new ResultSetExtractor<CreateSiteVO>() {
			@Override
			public CreateSiteVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<CreateSiteVO> siteList = new ArrayList<CreateSiteVO>();
				CreateSiteVO siteVO = new CreateSiteVO();
				if (rs.next()) {
					siteVO.setSiteId(rs.getLong("site_id"));
					siteVO.setSiteName(rs.getString("site_name"));
					siteVO.setDistrictName(rs.getString("district_name"));
					siteVO.setAreaName(rs.getString("area_name"));
					siteVO.setClusterName(rs.getString("cluster_name"));
					siteVO.setSiteOwner(rs.getString("site_owner"));
					siteVO.setOwner(rs.getString("site_owner"));
					siteVO.setBrandId(rs.getLong("brand_id"));
					siteVO.setBrandName(rs.getString("brand_name"));
					siteVO.setElectricityId(rs.getString("elec_id_no"));
					siteVO.setContactName(rs.getString("contact_name"));
					siteVO.setEmail(rs.getString("email"));
					siteVO.setPrimaryContact(rs.getString("primary_contact_number"));
					siteVO.setSiteAddress1(rs.getString("site_address1"));
					siteVO.setSiteAddress2(rs.getString("site_address2"));
					siteVO.setSiteAddress3(rs.getString("site_address3"));
					siteVO.setSiteAddress4(rs.getString("site_address4"));
					siteVO.setSecondaryContact(rs.getString("alt_contact_number"));
					siteVO.setLatitude(rs.getString("latitude"));
					siteVO.setLongitude(rs.getString("longitude"));
					if(rs.getLong("site_number1")>0){
						siteVO.setSiteNumber1(String.valueOf(rs.getLong("site_number1")));
					}
					if(rs.getLong("site_number2")>0){
						siteVO.setSiteNumber2(String.valueOf(rs.getLong("site_number2")));
					}
                }
				return siteVO;
			}
		});
		return siteVO;
	}
	
	public CreateSiteVO saveSite(CreateSiteVO siteVO, LoginUser user){
		LOGGER.info("SiteDAO -- saveSite -- Save site details using Procedure: ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		if(siteVO.getSiteId()==null){
			SimpleJdbcCall insertJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspInsertSiteDetails");
			SqlParameterSource map = createNewSiteParam(siteVO, user);
			Map<String, Object> out = insertJdbcCall.execute(map);
			Map<String, String> resultMap1 = extractResult(out);
			siteVO.setSiteId(Long.parseLong(resultMap1.get("@siteId")));
			//siteVO.setStatus(Integer.parseInt(resultMap1.get("@isError")));	
			LOGGER.info("Site created for "+  user.getUsername() + "/ Site : "+ siteVO.getSiteName() +" with ID : "+ siteVO.getSiteId());
			if(siteVO.getSiteId()!=null){
				insertJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspUserSiteAccess");
				SqlParameterSource map2 = new MapSqlParameterSource()
						.addValue("userId", user.getUserId())
				.addValue("siteId", siteVO.getSiteId())
				.addValue("createdBy",user.getUsername())
				.addValue("dbName", user.getDbName())
				.addValue("isError", "@isError");
				Map<String, Object> out2 =  insertJdbcCall.execute(map2);
				LOGGER.info("Updating SiteUserAccess table for new site created by : "+  user.getUsername() + "/ Site : "+ siteVO.getSiteName());
				Map<String, String> resultMap2 = extractResult(out2);
				if(Integer.parseInt(resultMap2.get("@isError"))==200){
				siteVO.setStatus(201);	
				}
			}else{
				LOGGER.info("Unable to create site for "+  user.getUsername() + "in name of "+ siteVO.getSiteName());
			}
		}else if(siteVO.getSiteId()!=null){
			SimpleJdbcCall updateJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspUpdateSiteDetails");
			SqlParameterSource map = updateExistingSiteParam(siteVO, user);
			Map<String, Object> out = updateJdbcCall.execute(map);
			Map<String, String> resultMap1 = extractResult(out);
			siteVO.setStatus(Integer.parseInt(resultMap1.get("@isError")));
			siteVO.setStatus(200);	
			LOGGER.info("Site updated for "+  user.getUsername() + "/ Site : "+ siteVO.getSiteName() +" with ID : "+ siteVO.getSiteId());
		}
		
		LOGGER.info("Exit -- SiteDAO -- saveSite -- Save site details using Procedure: ");
		return siteVO;

	}

	private SqlParameterSource updateExistingSiteParam(CreateSiteVO siteVO, LoginUser user) {
		SqlParameterSource map = new MapSqlParameterSource()
				.addValue("siteId", siteVO.getSiteId())
				.addValue("siteName", siteVO.getSiteName())
				.addValue("siteOwner", StringUtils.isEmpty(siteVO.getOwner())==true?null:siteVO.getOwner())
				.addValue("operatorId", user.getCompany().getCompanyId())
				.addValue("districtId", siteVO.getDistrictId())
				.addValue("areaId", siteVO.getAreaId())
				.addValue("clusterId", siteVO.getClusterId())
				.addValue("elecId", siteVO.getElectricityId())
				.addValue("siteNumber1", Long.parseLong(siteVO.getSiteNumber1()))
				.addValue("siteNumber2", StringUtils.isEmpty(siteVO.getSiteNumber2())==true?null:Long.parseLong(siteVO.getSiteNumber2()))
				.addValue("attachmentPath", siteVO.getSiteAttachments().isEmpty()==true?null:siteVO.getSiteAttachments().get(0))
				.addValue("salesArea", StringUtils.isEmpty(siteVO.getSalesAreaSize())==true?null:siteVO.getSalesAreaSize())
				.addValue("brandId", siteVO.getBrandId())
				.addValue("brandName", siteVO.getBrandName())
				.addValue("siteContactName", siteVO.getContactName())
				.addValue("siteEmail", siteVO.getEmail())
				.addValue("siteLat", StringUtils.isEmpty(siteVO.getLatitude())==true?null:siteVO.getLatitude())
				.addValue("siteLong", StringUtils.isEmpty(siteVO.getLongitude())==true?null:siteVO.getLongitude())
				.addValue("pContactNumber", StringUtils.isEmpty(siteVO.getPrimaryContact())==true?null:Long.parseLong(siteVO.getPrimaryContact()))
				.addValue("sContactNumber",  StringUtils.isEmpty(siteVO.getSecondaryContact())==true?null:Long.parseLong(siteVO.getSecondaryContact()))
				.addValue("addressLine1", siteVO.getSiteAddress1())
				.addValue("addressLine2", siteVO.getSiteAddress2())
				.addValue("addressLine3", siteVO.getSiteAddress3())
				.addValue("addressLine4", siteVO.getSiteAddress4())
				.addValue("zipCode", siteVO.getZipCode())
				.addValue("dbName", user.getDbName())
				.addValue("modifiedBy", user.getUsername())
				.addValue("isError", "@isError");
				return map;
	}

	private SqlParameterSource createNewSiteParam(CreateSiteVO siteVO, LoginUser user) {
		SqlParameterSource map = new MapSqlParameterSource().addValue("siteName", siteVO.getSiteName())
		.addValue("siteOwner", StringUtils.isEmpty(siteVO.getOwner())==true?null:siteVO.getOwner())
		.addValue("operatorId", user.getCompany().getCompanyId())
		.addValue("districtId", siteVO.getDistrictId())
		.addValue("areaId", siteVO.getAreaId())
		.addValue("clusterId", siteVO.getClusterId())
		.addValue("elecId", siteVO.getElectricityId())
		.addValue("siteNumber1", Long.parseLong(siteVO.getSiteNumber1()))
		.addValue("siteNumber2", StringUtils.isEmpty(siteVO.getSiteNumber2())==true?null:Long.parseLong(siteVO.getSiteNumber2()))
		.addValue("attachmentPath", siteVO.getSiteAttachments().isEmpty()==true?null:siteVO.getSiteAttachments().get(0))
		.addValue("salesArea", StringUtils.isEmpty(siteVO.getSalesAreaSize())==true?null:siteVO.getSalesAreaSize())
		.addValue("brandId", siteVO.getBrandId())
		.addValue("brandName", siteVO.getBrandName())
		.addValue("siteContactName", siteVO.getContactName())
		.addValue("siteEmail", siteVO.getEmail())
		.addValue("siteLat", StringUtils.isEmpty(siteVO.getLatitude())==true?null:siteVO.getLatitude())
		.addValue("siteLong", StringUtils.isEmpty(siteVO.getLongitude())==true?null:siteVO.getLongitude())
		.addValue("pContactNumber", StringUtils.isEmpty(siteVO.getPrimaryContact())==true?null:Long.parseLong(siteVO.getPrimaryContact()))
		.addValue("sContactNumber",  StringUtils.isEmpty(siteVO.getSecondaryContact())==true?null:Long.parseLong(siteVO.getSecondaryContact()))
		.addValue("addressLine1", siteVO.getSiteAddress1())
		.addValue("addressLine2", siteVO.getSiteAddress2())
		.addValue("addressLine3", siteVO.getSiteAddress3())
		.addValue("addressLine4", siteVO.getSiteAddress4())
		.addValue("zipCode", siteVO.getZipCode())
		.addValue("dbName", user.getDbName())
		.addValue("createdBy", user.getUsername())
		.addValue("siteId", "@siteId")
		.addValue("isError", "@isError");
		return map;
	}
	
	public List<SiteLicenceVO> getSiteLicense(Long siteId){
		LOGGER.info("SiteDAO -- getSiteDetails -- Getting Site license details for selected site : "+ siteId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SiteLicenceVO>  siteLicenseVOList =  jdbcTemplate.query(AppConstants.SITE_LICENSE_QUERY, new Object[]{siteId}, new ResultSetExtractor<List<SiteLicenceVO>>() {
			@Override
			public List<SiteLicenceVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SiteLicenceVO> licenseList = new ArrayList<SiteLicenceVO>();
				DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				while (rs.next()) {
					SiteLicenceVO siteLicenceVO = new SiteLicenceVO();
					siteLicenceVO.setLicenseId(rs.getLong("license_id"));
					siteLicenceVO.setLicenseName(rs.getString("license_name"));
					String fromD =df.format(rs.getDate("start_date"));
					String toD =df.format(rs.getDate("end_date"));
					siteLicenceVO.setValidfrom(fromD);
					siteLicenceVO.setValidto(toD);
					siteLicenceVO.setAttachment(rs.getString("attachment_path"));
					licenseList.add(siteLicenceVO);
                }
				return licenseList;
			}
		});
		return siteLicenseVOList == null ? Collections.emptyList():siteLicenseVOList;
	}
	
	public List<SiteOperationVO> getSiteSalesTimings(Long siteId){
		LOGGER.info("SiteDAO -- getSiteDetails -- Getting Site Sales timing details for selected site : "+ siteId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SiteOperationVO>  siteSalesTimeList =  jdbcTemplate.query(AppConstants.SITE_OPERATING_SALES_TIMINGS_QUERY, new Object[]{siteId}, new ResultSetExtractor<List<SiteOperationVO>>() {
			@Override
			public List<SiteOperationVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SiteOperationVO> salesTimeList = new ArrayList<SiteOperationVO>();
				while (rs.next()) {
					SiteOperationVO siteSalesTime = new SiteOperationVO();
					siteSalesTime.setOpId(rs.getLong("op_id"));
					siteSalesTime.setDays(rs.getString("day_of_week"));
					siteSalesTime.setFrom(rs.getString("op_start_time"));
					siteSalesTime.setTo(rs.getString("op_close_time"));
					salesTimeList.add(siteSalesTime);
                }
				return salesTimeList;
			}
		});
		return siteSalesTimeList == null ? Collections.emptyList():siteSalesTimeList;
	}
	
	
	public List<SiteDeliveryVO> getSiteDeliveryTimings(Long siteId){
		LOGGER.info("SiteDAO -- getSiteDetails -- Getting Site Delivery timing details for selected site : "+ siteId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SiteDeliveryVO>  siteDeliveryTimeList =  jdbcTemplate.query(AppConstants.SITE_OPERATING_DELIVERY_TIMINGS_QUERY, new Object[]{siteId}, new ResultSetExtractor<List<SiteDeliveryVO>>() {
			@Override
			public List<SiteDeliveryVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SiteDeliveryVO> deliveryTimeList = new ArrayList<SiteDeliveryVO>();
				while (rs.next()) {
					SiteDeliveryVO siteDeliveryTime = new SiteDeliveryVO();
					siteDeliveryTime.setOpId(rs.getLong("op_id"));
					siteDeliveryTime.setDays(rs.getString("day_of_week"));
					siteDeliveryTime.setFrom(rs.getString("op_start_time"));
					siteDeliveryTime.setTo(rs.getString("op_close_time"));
					deliveryTimeList.add(siteDeliveryTime);
                }
				return deliveryTimeList;
			}
		});
		return siteDeliveryTimeList == null ? Collections.emptyList():siteDeliveryTimeList;
	}
	
	
	public List<SiteSubmeterVO> getSiteSubmeterList(Long siteId) {
		LOGGER.info("SiteDAO -- getSiteDetails -- Getting Site Submeter  details for selected site : "+ siteId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SiteSubmeterVO>  siteSubmeterList =  jdbcTemplate.query(AppConstants.SITE_SUBMETER_QUERY, new Object[]{siteId}, new ResultSetExtractor<List<SiteSubmeterVO>>() {
			@Override
			public List<SiteSubmeterVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SiteSubmeterVO> submeterList = new ArrayList<SiteSubmeterVO>();
				while (rs.next()) {
					SiteSubmeterVO siteSubmeterVO = new SiteSubmeterVO();
					siteSubmeterVO.setSubMeterId(rs.getLong("submeter_id"));
					siteSubmeterVO.setSubMeterNumber(rs.getString("submeter_number"));
					siteSubmeterVO.setSubMeterUser(rs.getString("submeter_user"));
					submeterList.add(siteSubmeterVO);
                }
				return submeterList;
			}
		});
		return siteSubmeterList == null ? Collections.emptyList():siteSubmeterList;
	}

	
	
	public SiteLicenceVO saveLicenseDetails(Long siteId, SiteLicenceVO siteLicence, LoginUser user) {
		LOGGER.info("SiteDAO -- saveSiteLicense -- Save site license using Procedure: ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspInsertSiteLicense");
		SqlParameterSource map = new MapSqlParameterSource()
		 .addValue("siteId", siteId)
		.addValue("licenseName", StringUtils.isEmpty(siteLicence.getLicenseName())==true?null:siteLicence.getLicenseName())
		.addValue("startDate", siteLicence.getValidfrom())
		.addValue("endDate", siteLicence.getValidto())
		.addValue("attachmentPath", StringUtils.isEmpty(siteLicence.getAttachment())==true?null:siteLicence.getAttachment())
		.addValue("createdBy", user.getUsername())
		.addValue("dbName", user.getDbName())
		.addValue("licenseId", "@licenseId")
		.addValue("isError", "@isError");
		jdbcCall.compile();
		Map<String, Object> out = jdbcCall.execute(map);
		Map<String, String> resultMap1 = extractResult(out);
		siteLicence.setLicenseId(Long.parseLong(resultMap1.get("@licenseId")));
		siteLicence.setStatus(Integer.parseInt(resultMap1.get("@isError")));
		return siteLicence;	
	}
	
	public int insertSiteLicenseBatch(Long siteId, List<SiteLicenceVO> siteLicenceVOList, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int[] insertedRows = jdbcTemplate.batchUpdate("call tenants.uspInsertSiteLicense(?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteLicenceVO siteLicence = siteLicenceVOList.get(i);
                    	try{
                        ps.setLong(1, siteId);
                        ps.setString(2, StringUtils.isEmpty(siteLicence.getLicenseName())==true?null:siteLicence.getLicenseName());
                        Date startDate= simpleDateFormat.parse(siteLicence.getValidfrom());
                        Date endDate= simpleDateFormat.parse(siteLicence.getValidto());
                        ps.setDate(3, ApplicationUtil.getSqlDate(startDate));
                        ps.setDate(4, ApplicationUtil.getSqlDate(endDate));
                        ps.setString(5, StringUtils.isEmpty(siteLicence.getAttachment())==true?null:siteLicence.getAttachment());
                        ps.setString(6, user.getUsername());
                        ps.setString(7, user.getDbName());
                    	}catch(ParseException p){
                    		p.printStackTrace();
                    	}
                        
                    }

                    @Override
                    public int getBatchSize() {
                        return siteLicenceVOList.size();
                    }
                });
        return insertedRows.length;
    }
	
	public int updateSiteLicenseBatch(Long siteId, List<SiteLicenceVO> siteLicenceVOList, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        int[] updatedRows = jdbcTemplate.batchUpdate("call tenants.uspUpdateSiteLicense(?,?,?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteLicenceVO siteLicence = siteLicenceVOList.get(i);
                    	try{
                    	ps.setLong(1, siteLicence.getLicenseId());
                        ps.setLong(2, siteId);
                        ps.setString(3, StringUtils.isEmpty(siteLicence.getLicenseName())==true?null:siteLicence.getLicenseName());
                        Date startDate= simpleDateFormat.parse(siteLicence.getValidfrom());
                        Date endDate= simpleDateFormat.parse(siteLicence.getValidto());
                        ps.setDate(4, ApplicationUtil.getSqlDate(startDate));
                        ps.setDate(5, ApplicationUtil.getSqlDate(endDate));
                        ps.setString(6, StringUtils.isEmpty(siteLicence.getAttachment())==true?null:siteLicence.getAttachment());
                        ps.setString(7, user.getUsername());
                        ps.setString(8, user.getDbName());
                    	}catch(ParseException p){
                    		p.printStackTrace();
                    	}
                      //  ps.setString(8, "@licenseId");
                       // ps.setString(9, "@isError");
                        
                    }

                    @Override
                    public int getBatchSize() {
                        return siteLicenceVOList.size();
                    }
                });
        return updatedRows.length;
    }
	
	public int saveOrUpdateOeratingTimings(Long siteId, List<SiteOperationVO> siteOperation,List<SiteDeliveryVO> siteDeliveryList, LoginUser user, String mode) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String query1 = "";
		String query2 = "";
		if(mode.equalsIgnoreCase("ADD")){
			query1 = AppConstants.INSERT_SALES_TIMINGS_QUERY;
			query2 = AppConstants.INSERT_DELIVERY_TIMINGS_QUERY;
		}else{
			query1 = AppConstants.UPDATE_SALES_TIMINGS_QUERY;
			query2 = AppConstants.UPDATE_DELIVERY_TIMINGS_QUERY;
		}
        int[] insertedRows = jdbcTemplate.batchUpdate(query1,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteOperationVO siteOperationVO = siteOperation.get(i);
                    	if(mode.equalsIgnoreCase("ADD")){
	                        ps.setLong(1, siteId);
	                        ps.setString(2, siteOperationVO.getDays());
	                        ps.setString(3, siteOperationVO.getFrom());
	                        ps.setString(4, siteOperationVO.getTo());
                    	}else{
                    		ps.setString(1, siteOperationVO.getDays());
 	                        ps.setString(2, siteOperationVO.getFrom());
 	                        ps.setString(3, siteOperationVO.getTo());
 	                        ps.setLong(4, siteOperationVO.getOpId());
                    	}
                    }

                    @Override
                    public int getBatchSize() {
                        return siteOperation.size();
                    }
                });
        int[] deliveryRows = jdbcTemplate.batchUpdate(query2,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteDeliveryVO siteDeliveryVO = siteDeliveryList.get(i);
                    	if(mode.equalsIgnoreCase("ADD")){
	                        ps.setLong(1, siteId);
	                        ps.setString(2, siteDeliveryVO.getDays());
	                        ps.setString(3, siteDeliveryVO.getFrom());
	                        ps.setString(4, siteDeliveryVO.getTo());
                    	}else{
                    		ps.setString(1, siteDeliveryVO.getDays());
 	                        ps.setString(2, siteDeliveryVO.getFrom());
 	                        ps.setString(3, siteDeliveryVO.getTo());
 	                        ps.setLong(4, siteDeliveryVO.getOpId());
                    	}
                    }

                    @Override
                    public int getBatchSize() {
                        return siteDeliveryList.size();
                    }
                });
        return insertedRows.length + deliveryRows.length;
	}
	
	public int saveOrUpdateSubmeterBatch(Long siteId, List<SiteSubmeterVO> siteSubmterVOList, LoginUser user, String mode)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String query = "";
		if(mode.equalsIgnoreCase("ADD")){
			query = AppConstants.INSERT_SUBMETER_SITE_QUERY;
		}
		else{
			query = AppConstants.UPDATE_SUBMETER_SITE_QUERY;
		}
        int[] insertedRows = jdbcTemplate.batchUpdate(query,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteSubmeterVO siteSubmeter = siteSubmterVOList.get(i);
                        ps.setLong(1, siteId);
                        ps.setString(2, StringUtils.isEmpty(siteSubmeter.getSubMeterNumber())==true?null:siteSubmeter.getSubMeterNumber());
                        ps.setString(3, StringUtils.isEmpty(siteSubmeter.getSubMeterUser())==true?null:siteSubmeter.getSubMeterUser());
                        if(mode.equalsIgnoreCase("ADD")){
                        	ps.setString(4, user.getUsername());
                        }else{
                        	ps.setLong(4, siteSubmeter.getSubMeterId());
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return siteSubmterVOList.size();
                    }
                });
        return insertedRows.length;
    }
	public int insertSubmeterBatch(Long siteId, List<SiteSubmeterVO> siteSubmterVOList, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate("call tenants.uspInsertSiteSubmeter(?,?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteSubmeterVO siteSubmeter = siteSubmterVOList.get(i);
                        ps.setLong(1, siteSubmeter.getSubMeterId());
                        ps.setLong(2, siteId);
                        ps.setString(3, StringUtils.isEmpty(siteSubmeter.getSubMeterNumber())==true?null:siteSubmeter.getSubMeterNumber());
                        ps.setString(4, StringUtils.isEmpty(siteSubmeter.getSubMeterUser())==true?null:siteSubmeter.getSubMeterUser());
                        ps.setString(5, user.getUsername());
                        ps.setString(6, user.getDbName());
                    }

                    @Override
                    public int getBatchSize() {
                        return siteSubmterVOList.size();
                    }
                });
        return insertedRows.length;
    }

	private Map<String, String> extractResult(Map<String, Object> out) {
		String result1 =  out.get("#result-set-1").toString();
		result1 = result1.replaceAll("\\[", "").replaceAll("\\]","");
		result1 = result1.replaceAll("\\{", "").replaceAll("\\}","");
		    System.out.println(result1);
		    String[] keyValuePairs = result1.split(",");              //split the string to creat key-value pairs
		    Map<String,String> resultMap = new HashMap<>();               

		    for(String pair : keyValuePairs)                        //iterate over the pairs
		    {
		        String[] entry = pair.split("=");                   //split the pairs to get key and value 
		        resultMap.put(entry[0].trim(), entry[1].trim());          //add them to the hashmap and trim whitespaces
		    }
		return resultMap;
	}

	public int insertSiteOperatingBatch(Long siteId, List<SiteOperationVO> siteOperation, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate("call tenants.uspInsertSiteSalesTime(?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteOperationVO siteOperationVO = siteOperation.get(i);
                        ps.setLong(1, siteId);
                        ps.setString(2, siteOperationVO.getDays());
                        ps.setString(3, siteOperationVO.getFrom());
                        ps.setString(4, siteOperationVO.getTo());
                        ps.setString(5, user.getDbName());
                    }

                    @Override
                    public int getBatchSize() {
                        return siteOperation.size();
                    }
                });
        return insertedRows.length;
	}

	public int insertSiteDeliveryBatch(Long siteId, List<SiteDeliveryVO> siteDeliveryList, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate("call tenants.uspInsertSiteDeliveryTime(?,?,?,?,?)",
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SiteDeliveryVO siteDeliveryVO = siteDeliveryList.get(i);
                        ps.setLong(1, siteId);
                        ps.setString(2, siteDeliveryVO.getDays());
                        ps.setString(3, siteDeliveryVO.getFrom());
                        ps.setString(4, siteDeliveryVO.getTo());
                        ps.setString(5, user.getDbName());
                    }

                    @Override
                    public int getBatchSize() {
                        return siteDeliveryList.size();
                    }
                });
        return insertedRows.length;
	}

	
	
}
