package com.pms.app.dao.impl;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.jpa.entities.Asset;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.web.util.ApplicationUtil;

@Repository
public class AssetDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(AssetDAO.class);

	public AssetDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	public List<AssetVO> findBySiteIdIn(Set<Long> siteIdList) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("siteIds", siteIdList);
		Object[] siteArray = siteIdList.toArray();
		List<AssetVO> assetVOList = jdbcTemplate.query(AppConstants.ASSET_LIST_QUERY, parameters,
				new RowMapper<AssetVO>() {
					@Override
					public AssetVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toAssetVO(rs);
					}
				});
		return assetVOList;
	}

	public AssetVO getAssetDetails(Long assetId) {
		LOGGER.info("AssetDAO -- getAssetDetails -- Getting Asset details for selected asset : " + assetId);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		AssetVO assetVO = jdbcTemplate.query(AppConstants.ASSET_DETAILS_QUERY, new Object[] { assetId },
				new ResultSetExtractor<AssetVO>() {
					@Override
					public AssetVO extractData(ResultSet rs) throws SQLException, DataAccessException {
						return getAssetDetails(rs);
					}
				});
		return assetVO;
	}

	public List<AssetCategory> findAssetCategories() {
		LOGGER.info("Inside AssetDAO .. findAssetCategories");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetCategory> assetCategoryList = jdbcTemplate.query(AppConstants.ASSET_CATEGORY_LIST,
				new ResultSetExtractor<List<AssetCategory>>() {
					@Override
					public List<AssetCategory> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<AssetCategory> categoryList = new ArrayList<AssetCategory>();
						while (rs.next()) {
							AssetCategory assetCategory = new AssetCategory();
							assetCategory.setAssetCategoryId(rs.getLong("category_id"));
							assetCategory.setAssetCategoryName(rs.getString("category_name"));
							assetCategory.setAssetType(rs.getString("asset_type"));
							categoryList.add(assetCategory);
						}
						return categoryList;
					}
				});
		LOGGER.info("Exit AssetDAO .. findAssetCategories");
		return assetCategoryList;
	}

	public List<AssetRepairType> getAssetRepairTypeByAssetCategoryId(Long assetCategoryId) throws Exception {
		LOGGER.info("Inside AssetDAO .. getAssetRepairTypeByAssetCategoryId");
		LOGGER.info("Getting list of asset repair types ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetRepairType> repairTypes = jdbcTemplate.query(AppConstants.ASSET_REPAIRTYPE_QUERY,
				new Object[] { assetCategoryId }, new RowMapper<AssetRepairType>() {

					@Override
					public AssetRepairType mapRow(ResultSet rs, int arg1) throws SQLException {
						AssetRepairType assetRepairType = new AssetRepairType();
						assetRepairType.setSubCategoryId1(rs.getLong(1));
						assetRepairType.setAssetSubcategory1(rs.getString(2));
						return assetRepairType;
					}

				});
		LOGGER.info("Exit CustomDAOImpl .. getAssetRepairTypeByAssetCategoryId");
		return repairTypes;
	}

	public List<AssetLocation> findAssetLocations() {
		LOGGER.info("Inside AssetDAO .. findAssetLocations");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetLocation> assetLocationList = jdbcTemplate.query(AppConstants.ASSET_LOCATION_LIST,
				new ResultSetExtractor<List<AssetLocation>>() {
					@Override
					public List<AssetLocation> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<AssetLocation> locationList = new ArrayList<AssetLocation>();
						while (rs.next()) {
							AssetLocation assetLocation = new AssetLocation();
							assetLocation.setLocationId(rs.getLong("location_id"));
							assetLocation.setLocationName(rs.getString("location_name"));
							locationList.add(assetLocation);
						}
						return locationList;
					}
				});
		LOGGER.info("Exit AssetDAO .. findAssetLocations");
		return assetLocationList;
	}

	private AssetVO toAssetVO(ResultSet rs) throws SQLException {
		AssetVO assetVO = new AssetVO();
		assetVO.setAssetId(rs.getLong("asset_id"));
		assetVO.setAssetCode(rs.getString("asset_code"));
		assetVO.setAssetName(rs.getString("asset_name"));
		assetVO.setModelNumber(rs.getString("model_number"));
		assetVO.setSiteId(rs.getLong("site_id"));
		assetVO.setSiteName(rs.getString("site_name"));
		assetVO.setCategoryId(rs.getLong("category_id"));
		assetVO.setAssetCategoryName(rs.getString("category_name"));
		assetVO.setLocationId(rs.getLong("location_id"));
		assetVO.setLocationName(rs.getString("location_name"));
		return assetVO;
	}
	
	
	private AssetVO getAssetDetails(ResultSet rs) throws SQLException {
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		AssetVO assetVO = new AssetVO();
		if (rs.next()) {
			assetVO.setAssetId(rs.getLong("asset_id"));
			assetVO.setAssetCode(rs.getString("asset_code"));
			assetVO.setAssetName(rs.getString("asset_name"));
			assetVO.setModelNumber(rs.getString("model_number"));
			assetVO.setSiteId(rs.getLong("site_id"));
			assetVO.setSiteName(rs.getString("site_name"));
			assetVO.setAssetType(rs.getString("asset_type"));
			assetVO.setCategoryId(rs.getLong("category_id"));
			assetVO.setAssetCategoryName(rs.getString("category_name"));
			assetVO.setLocationId(rs.getLong("location_id"));
			assetVO.setLocationName(rs.getString("location_name"));
			assetVO.setAssetDescription(rs.getString("asset_desc"));
			assetVO.setContent(rs.getString("content"));
			assetVO.setImagePath(rs.getString("image_path"));
			assetVO.setDocumentPath(rs.getString("document_path"));
			assetVO.setServiceProviderId(rs.getLong("sp_id"));
			assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("sp_name"))==true?null:rs.getString("sp_name"));
			String commissionedDate = df.format(rs.getDate("date_commissioned"));
			String deComissionedDate = null==rs.getDate("date_decomissioned")?null:df.format(rs.getDate("date_decomissioned"));
			assetVO.setCommisionedDate(commissionedDate);
			assetVO.setDeCommissionedDate(deComissionedDate);
			assetVO.setIsAssetElectrical(rs.getString("is_asset_electrical"));
			assetVO.setIsPWSensorAttached(rs.getString("is_pw_sensor_attached"));
			assetVO.setPwSensorNumber(rs.getString("pw_sensor_number"));
			assetVO.setSubCategoryId1(rs.getLong("subcategory1_id"));
			assetVO.setAssetSubcategory1(rs.getString("asset_subcategory1"));
			assetVO.setDelFlag(rs.getInt("del_flag"));
			assetVO.setSiteOwner(rs.getString("site_owner"));
			assetVO.setContactName(rs.getString("contact_name"));
			assetVO.setEmail(rs.getString("email"));
		}
		return assetVO;
	}

	public List<ServiceProviderVO> findSPByCompanyId(Long companyId) {
		LOGGER.info("Inside AssetDAO .. findSPByCompanyId");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<ServiceProviderVO> serviceProviderList =  jdbcTemplate.query(AppConstants.EXTERNAL_SERVICE_PROVIDER_LIST, new Object[] { companyId }, new ResultSetExtractor<List<ServiceProviderVO>>() {
			@Override
			public List<ServiceProviderVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ServiceProviderVO> spList = new ArrayList<ServiceProviderVO>();
				while(rs.next()){
					ServiceProviderVO serviceProvider = new ServiceProviderVO();
					serviceProvider.setServiceProviderId(rs.getLong("sp_id"));
					serviceProvider.setName(rs.getString("sp_name"));
					serviceProvider.setEmail(rs.getString("sp_email"));
					spList.add(serviceProvider);
					}
					return spList;
				}
			});
		return serviceProviderList;

	}

	public List<Asset> findByAssetCodeAndSiteIdInAndDelFlag(String assetCode, List<Long> sites, int i) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("siteIds", sites);
		parameters.addValue("assetCode", assetCode);
		List<Asset> assetVOList = jdbcTemplate.query(AppConstants.FIND_DUPLICATE_ASSET_QUERY, parameters,
				new RowMapper<Asset>() {
					@Override
					public Asset mapRow(ResultSet rs, int arg1) throws SQLException {
						return toAsset(rs);
					}
				});
		return assetVOList;
	}

	private Asset toAsset(ResultSet rs) throws SQLException {
		Asset assetVO = new Asset();
		assetVO.setAssetId(rs.getLong("asset_id"));
		assetVO.setAssetCode(rs.getString("asset_code"));
		assetVO.setAssetName(rs.getString("asset_name"));
		assetVO.setModelNumber(rs.getString("model_number"));
		assetVO.setSiteId(rs.getLong("site_id"));
		return assetVO;
	}

	public int saveAssetInBatch(List<Asset> assetList,AssetVO assetVO, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate("call tenants.uspAddAsset(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", 
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	Asset asset = assetList.get(i);
                    		ps.setString(1, StringUtils.isEmpty(asset.getAssetName())==true?null:asset.getAssetName());
                    		ps.setString(2, StringUtils.isEmpty(asset.getAssetCode())==true?null:asset.getAssetCode());
                    		ps.setString(3, StringUtils.isEmpty(asset.getModelNumber())==true?null:asset.getModelNumber());
                    		ps.setLong(4,  asset.getLocationId());
                    		ps.setLong(5, asset.getCategoryId());
                    		ps.setLong(6, asset.getSubCategoryId1());
                    		ps.setLong(7, asset.getServiceProviderId());
                    		ps.setString(8, StringUtils.isEmpty(asset.getImagePath())==true?null:asset.getImagePath());
                    		ps.setString(9, StringUtils.isEmpty(asset.getDocumentPath())==true?null:asset.getDocumentPath());
                    		ps.setDate(10,ApplicationUtil.getSqlDate(asset.getDateCommissioned()));
                    		ps.setDate(11, null==asset.getDateDeComissioned()?null:ApplicationUtil.getSqlDate(asset.getDateDeComissioned()));
                    		ps.setString(12, asset.getContent());
                    		ps.setLong(13, asset.getSiteId());
                    		ps.setString(14, asset.getIsAssetElectrical());
                    		ps.setString(15, asset.getIsPWSensorAttached());
                    		ps.setString(16,  asset.getPwSensorNumber());
                    		ps.setString(17, asset.getAssetDescription());
                    		ps.setInt(18,asset.getDelFlag());
                    		ps.setString(19,user.getDbName());
                    		ps.setString(20, user.getUsername());
                    		
                    }

                    @Override
                    public int getBatchSize() {
                        return assetList.size();
                    }
                });
        return insertedRows.length;
    }
	
	
	public int updateAsset(Asset asset ,AssetVO assetVO, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int updatedRow = jdbcTemplate.update(AppConstants.ASSET_UPDATE_QUERY, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
            		ps.setString(1, StringUtils.isEmpty(asset.getAssetCode())==true?null:asset.getAssetCode());
            		ps.setLong(2, asset.getSiteId());
            		ps.setString(3, StringUtils.isEmpty(asset.getAssetName())==true?null:asset.getAssetName());
            		ps.setString(4, asset.getAssetDescription());
            		ps.setString(5, StringUtils.isEmpty(asset.getModelNumber())==true?null:asset.getModelNumber());
            		ps.setLong(6, asset.getCategoryId());
            		ps.setString(7, asset.getContent());
            		ps.setLong(8,  asset.getLocationId());
            		ps.setString(9, StringUtils.isEmpty(asset.getImagePath())==true?null:asset.getImagePath());
            		ps.setString(10, StringUtils.isEmpty(asset.getDocumentPath())==true?null:asset.getDocumentPath());
            		ps.setLong(11, asset.getServiceProviderId());
            		ps.setDate(12,ApplicationUtil.getSqlDate(asset.getDateCommissioned()));
            		ps.setDate(13, null==asset.getDateDeComissioned()?null:ApplicationUtil.getSqlDate(asset.getDateDeComissioned()));
            		ps.setString(14, asset.getIsAssetElectrical());
            		ps.setString(15, asset.getIsPWSensorAttached());
            		ps.setString(16,  asset.getPwSensorNumber());
            		ps.setLong(17, asset.getSubCategoryId1());
            		ps.setDate(18, ApplicationUtil.getSqlDate(new Date()));
            		ps.setString(19,user.getUsername());
            		ps.setLong(20, asset.getAssetId());
            }

        });
		return updatedRow;
           
    }

	public int deleteAsset(Long assetId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		  int updatedRow = jdbcTemplate.update(AppConstants.ASSET_DELETE_QUERY, new PreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	            		ps.setLong(1, assetId);
	            }
	        });
		  return updatedRow;
	}

	public List<AssetVO> findAssetBySiteId(Long siteId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetVO> assetList =  jdbcTemplate.query(AppConstants.SITE_ASSET_LIST_QUERY, new Object[] { siteId }, new ResultSetExtractor<List<AssetVO>>() {
			@Override
			public List<AssetVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<AssetVO> assetList = new ArrayList<AssetVO>();
				while(rs.next()){
					AssetVO assetVO = new AssetVO();
					assetVO.setSiteId(siteId);
					assetVO.setAssetId(rs.getLong("asset_id"));
					assetVO.setAssetName(rs.getString("asset_name"));
					assetVO.setAssetCode(rs.getString("asset_code"));
					assetVO.setAssetType(rs.getString("asset_type"));
					assetVO.setServiceProviderId(rs.getLong("sp_id"));
					assetVO.setServiceProviderName(rs.getString("sp_name"));
					assetVO.setCategoryId(rs.getLong("category_id"));
					assetVO.setAssetCategoryName(rs.getString("category_name"));
					assetVO.setSubCategoryId1(rs.getLong("subcategory1_id"));
					if(assetVO.getSubCategoryId1()!=null){
						assetVO.setAssetSubcategory1(rs.getString("asset_subcategory1"));
					}
					assetList.add(assetVO);
					}
					return assetList;
				}
			});
		return assetList;
	}
}
