package com.pms.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.pms.app.constants.AppConstants;
import com.pms.app.dao.CustomDAO;
import com.pms.app.view.vo.AssetVO;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;

public class CustomDAOImpl implements CustomDAO {

	private final static Logger LOGGER = LoggerFactory.getLogger(CustomDAOImpl.class);

	@Autowired  
	private DataSource dataSource;
	
	@Autowired  
	private JdbcTemplate jdbcTemplate;
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	
	@Override
	public List<AssetRepairType> getAssetRepairTypeByAssetCategoryId(Long assetCategoryId) throws Exception {
		LOGGER.info("Inside CustomDAOImpl .. getAssetRepairTypeByAssetCategoryId");
		LOGGER.info("Getting list of asset repair types ");
		List<AssetRepairType>  repairTypes = jdbcTemplate.query(AppConstants.ASSET_REPAIRTYPE_QUERY, new Object[]{assetCategoryId},new RowMapper<AssetRepairType>(){

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

	@Override
	public List<AssetSubRepairType> getAssetSubRepairTypeByAssetRepairType(Long assetSubCategoryid) throws Exception {
		LOGGER.info("Inside CustomDAOImpl .. getAssetRepairTypeByAssetCategoryId");
		LOGGER.info("Getting list of asset repair types ");
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

	@Override
	public AssetVO getAssetSubcategoryDetails(Long categoryId, Long subCategoryId1,  Long assetId)
			throws Exception {
		
		List<AssetVO> resultAssetVO = jdbcTemplate.query(AppConstants.ASSET_SUBCATEGORY_DETAILS_QUERY, new Object[]{categoryId, subCategoryId1,  assetId}, new RowMapper<AssetVO>(){
				@Override
				public AssetVO mapRow(ResultSet rs, int arg1) throws SQLException {
					AssetVO assetVO = new AssetVO();
					assetVO.setAssetId(rs.getLong(1));
					assetVO.setCategoryId(rs.getLong(2));
					assetVO.setAssetCategoryName(rs.getString(3));
					assetVO.setSubCategoryId1(rs.getLong(4));
					assetVO.setAssetSubcategory1(rs.getString(5));
					return assetVO;
				}
	    });
		return resultAssetVO.isEmpty()?null:resultAssetVO.get(0);
	}

	@Override
	public String getTenantName(String email) throws Exception {
		return "opstenant2";
	}
}
