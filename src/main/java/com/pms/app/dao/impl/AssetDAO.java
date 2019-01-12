package com.pms.app.dao.impl;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.AssetTask;
import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.jpa.entities.Asset;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;
import com.pms.web.util.ApplicationUtil;

@Repository
public class AssetDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(AssetDAO.class);

	public AssetDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	public List<AssetVO> findBySiteIdIn(Set<Long> siteIdList, String viewType) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("siteIds", siteIdList);
		parameters.addValue("siteIds", siteIdList);
		/*if(viewType.equalsIgnoreCase("RSP")){
			parameters.addValue("viewType", "RSP");
		}
		else if(viewType.equalsIgnoreCase("EXT")){
			parameters.addValue("viewType", "EXT");
		}*/
		
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
	
	public List<AssetSubRepairType> getAssetSubRepairTypeByAssetRepairType(Long assetSubCategoryid) throws Exception {
		LOGGER.info("Inside AssetDAO .. getAssetSubRepairTypeByAssetRepairType");
		LOGGER.info("Getting list of asset sub repair types ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetSubRepairType>  subRepairTypes = jdbcTemplate.query(AppConstants.ASSET_SUBREPAIRTYPE_QUERY, new Object[]{assetSubCategoryid},new RowMapper<AssetSubRepairType>(){

			@Override
			public AssetSubRepairType mapRow(ResultSet rs, int arg1) throws SQLException {
				AssetSubRepairType assetSubRepairType = new AssetSubRepairType();
				assetSubRepairType.setSubCategoryId2(rs.getLong(1));
				assetSubRepairType.setAssetSubcategory2(rs.getString(2));
				return assetSubRepairType;
			}
		});
		LOGGER.info("Exit CustomDAOImpl .. getAssetRepairTypeByAssetCategoryId");
		return subRepairTypes;
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
		assetVO.setSpType(rs.getString("sp_type"));
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
			if(!StringUtils.isEmpty(rs.getString("sp_type")) && rs.getString("sp_type").equalsIgnoreCase("RSP")){
				assetVO.setServiceProviderId(rs.getLong("rsp_id"));
				assetVO.setSpType("RSP");
				assetVO.setSpHelpDeskEmail(rs.getString("rsp_help_deskemail"));
				assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("rsp_name"))==true?null:rs.getString("rsp_name"));
			}
			else if(!StringUtils.isEmpty(rs.getString("sp_type")) && rs.getString("sp_type").equalsIgnoreCase("EXT")){
				assetVO.setServiceProviderId(rs.getLong("sp_id"));
				assetVO.setSpType("EXT");
				assetVO.setSpHelpDeskEmail(rs.getString("help_desk_email"));
				assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("sp_name"))==true?null:rs.getString("sp_name"));
			}
			
			
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
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate(AppConstants.ASSET_CREATE_QUERY, 
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
                    		
                    		if(asset.getSpType().equalsIgnoreCase("RSP")){
                    			ps.setNull(7, Types.NULL);
                    			ps.setLong(8, asset.getServiceProviderId());
                    		}else{
                    			ps.setLong(7, asset.getServiceProviderId());
                    			ps.setNull(8, Types.NULL);
                    		}
                    		ps.setString(9, asset.getSpType());
                    		ps.setString(10, StringUtils.isEmpty(asset.getImagePath())==true?null:asset.getImagePath());
                    		ps.setString(11, StringUtils.isEmpty(asset.getDocumentPath())==true?null:asset.getDocumentPath());
                    		ps.setDate(12,ApplicationUtil.getSqlDate(asset.getDateCommissioned()));
                    		ps.setDate(13, null==asset.getDateDeComissioned()?null:ApplicationUtil.getSqlDate(asset.getDateDeComissioned()));
                    		ps.setString(14, asset.getContent());
                    		ps.setLong(15, asset.getSiteId());
                    		ps.setString(16, asset.getIsAssetElectrical());
                    		ps.setString(17, asset.getIsPWSensorAttached());
                    		ps.setString(18,  asset.getPwSensorNumber());
                    		ps.setString(19, asset.getAssetDescription());
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
            		if(asset.getSpType().equalsIgnoreCase("RSP")){
            			ps.setNull(11, Types.NULL);
            			ps.setLong(12, asset.getServiceProviderId());
            		}
            		else if(asset.getSpType().equalsIgnoreCase("EXT")){
            			ps.setLong(11, asset.getServiceProviderId());
            			ps.setNull(12, Types.NULL);
            		}
            		ps.setString(13,asset.getSpType());
            		ps.setDate(14,ApplicationUtil.getSqlDate(asset.getDateCommissioned()));
            		ps.setDate(15, null==asset.getDateDeComissioned()?null:ApplicationUtil.getSqlDate(asset.getDateDeComissioned()));
            		ps.setString(16, asset.getIsAssetElectrical());
            		ps.setString(17, asset.getIsPWSensorAttached());
            		if(StringUtils.isEmpty(asset.getPwSensorNumber())){
            			ps.setNull(18,  Types.NULL);
            		}
            		else{
            			ps.setString(18,  asset.getPwSensorNumber());
            		}
            		ps.setLong(19, asset.getSubCategoryId1());
            		ps.setDate(20, ApplicationUtil.getSqlDate(new Date()));
            		ps.setString(21,user.getUsername());
            		ps.setLong(22, asset.getAssetId());
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

	
	public List<AssetVO> findAssetBySiteId(Long siteId, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetVO> assetList =  jdbcTemplate.query(AppConstants.SITE_ASSET_LIST_QUERY, new Object[] { siteId }, new ResultSetExtractor<List<AssetVO>>() {
			@Override
			public List<AssetVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<AssetVO> assetList = new ArrayList<AssetVO>();
						while (rs.next()) {
							if (user.getUserType().equalsIgnoreCase("SP")) {
									AssetVO assetVO = new AssetVO();
									assetVO.setSiteId(siteId);
									assetVO.setAssetId(rs.getLong("asset_id"));
									assetVO.setAssetName(rs.getString("asset_name"));
									assetVO.setAssetCode(rs.getString("asset_code"));
									assetVO.setAssetType(rs.getString("asset_type"));
									if(!StringUtils.isEmpty(rs.getString("rsp_id"))){
										assetVO.setSpCode(rs.getString("sp_code"));
										assetVO.setServiceProviderId(rs.getLong("rsp_id"));
										assetVO.setSpHelpDeskEmail(rs.getString("help_desk_email"));
										assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("rsp_name")) == true? null : rs.getString("rsp_name"));
									}
									/*else if(!StringUtils.isEmpty(rs.getString("sp_id"))){
										assetVO.setServiceProviderId(rs.getLong("sp_id"));
										assetVO.setSpHelpDeskEmail(rs.getString("sp_help_deskemail"));
										assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("sp_name")) == true? null : rs.getString("sp_name"));
									}*/
									assetVO.setSpType(rs.getString("sp_type"));
									assetVO.setCategoryId(rs.getLong("category_id"));
									assetVO.setAssetCategoryName(rs.getString("category_name"));
									assetVO.setSubCategoryId1(rs.getLong("subcategory1_id"));
									if (assetVO.getSubCategoryId1() != null) {
										assetVO.setAssetSubcategory1(rs.getString("asset_subcategory1"));
									}
									assetList.add(assetVO);
								
							} else if (user.getUserType().equalsIgnoreCase("USER")) {
							
									AssetVO assetVO = new AssetVO();
									assetVO.setSiteId(siteId);
									assetVO.setAssetId(rs.getLong("asset_id"));
									assetVO.setAssetName(rs.getString("asset_name"));
									assetVO.setAssetCode(rs.getString("asset_code"));
									assetVO.setAssetType(rs.getString("asset_type"));
									if(!StringUtils.isEmpty(rs.getString("rsp_id"))){
										assetVO.setSpCode(rs.getString("sp_code"));
										assetVO.setServiceProviderId(rs.getLong("rsp_id"));
										assetVO.setSpHelpDeskEmail(rs.getString("help_desk_email"));
										assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("rsp_name")) == true? null : rs.getString("rsp_name"));
									}
									else if(!StringUtils.isEmpty(rs.getString("sp_id"))){
										assetVO.setServiceProviderId(rs.getLong("sp_id"));
										assetVO.setSpHelpDeskEmail(rs.getString("help_desk_email"));
										assetVO.setServiceProviderName(StringUtils.isEmpty(rs.getString("sp_name")) == true? null : rs.getString("sp_name"));
									}
									assetVO.setSpType(rs.getString("sp_type"));
									assetVO.setCategoryId(rs.getLong("category_id"));
									assetVO.setAssetCategoryName(rs.getString("category_name"));
									assetVO.setSubCategoryId1(rs.getLong("subcategory1_id"));
									if (assetVO.getSubCategoryId1() != null) {
										assetVO.setAssetSubcategory1(rs.getString("asset_subcategory1"));
									}
									assetList.add(assetVO);
								
							}

						}
					return assetList;
				}
			});
		return assetList;
	}

	public AssetTask saveAssetTask(AssetTask assetTask, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_ASSET_TASK_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, assetTask.getAssetId());
	        ps.setString(2, assetTask.getTaskName());
    		ps.setString(3, assetTask.getTaskDesc());
    		ps.setDate(4,	ApplicationUtil.getSqlDate(assetTask.getPlannedStartDate()));
    		ps.setDate(5,   ApplicationUtil.getSqlDate(assetTask.getPlannedComplDate()));
    		ps.setString(6, assetTask.getTaskAssignedTo());
    		ps.setString(7, assetTask.getTaskStatus());
    		ps.setString(8, assetTask.getResComments());
    		ps.setString(9, user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved Asset Task {} with taskId {}.", assetTask.getTaskName(), key.getKey());
	    assetTask.setTaskId(key.getKey().longValue());
	    return assetTask;
	}

	public List<AssetTask> findAssetTaskByAsset(Long assetId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<AssetTask> assetTaskList =  jdbcTemplate.query(AppConstants.ASSET_TASK_LIST_QUERY, new Object[] { assetId }, new ResultSetExtractor<List<AssetTask>>() {
			@Override
			public List<AssetTask> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<AssetTask> assetTaskList = new ArrayList<AssetTask>();
						while (rs.next()) {
							AssetTask assetTask = new AssetTask();
							assetTask.setTaskId(rs.getLong("task_id"));
							assetTask.setTaskName(rs.getString("task_name"));
							assetTask.setTaskDesc(rs.getString("task_desc"));
							assetTask.setPlanStartDate(ApplicationUtil.getDateStringFromSQLDate(rs.getString("planned_start_date")));
							assetTask.setPlanEndDate(ApplicationUtil.getDateStringFromSQLDate(rs.getString("planned_end_date")));
							assetTask.setTaskAssignedTo(rs.getString("task_assigned_to"));
							assetTask.setTaskStatus(rs.getString("task_status"));
							assetTask.setCreatedDate(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_date")));
							assetTask.setResComments(rs.getString("res_comment"));
							assetTaskList.add(assetTask);
						}
			return assetTaskList;
		    }
		
	   });
		return assetTaskList;
	}
	
	public int updateAssetTask(AssetTask assetTask , LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int updatedRow = jdbcTemplate.update(AppConstants.ASSET_TASK_UPDATE_QUERY,  new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, assetTask.getTaskName());
         		ps.setString(2, assetTask.getTaskDesc());
         		ps.setDate(3,	ApplicationUtil.getSqlDate(assetTask.getPlannedStartDate()));
         		ps.setDate(4,   ApplicationUtil.getSqlDate(assetTask.getPlannedComplDate()));
         		ps.setString(5, assetTask.getTaskAssignedTo());
         		ps.setString(6, assetTask.getTaskStatus());
         		ps.setString(7, assetTask.getResComments());
         		ps.setString(8, user.getUsername());
         		ps.setLong(9, assetTask.getTaskId());
            }

        });
		return updatedRow;
           
    }
}
