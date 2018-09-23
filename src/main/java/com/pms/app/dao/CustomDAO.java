package com.pms.app.dao;

import java.util.List;

import com.pms.app.view.vo.AssetVO;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;

public interface CustomDAO {

	public List<AssetRepairType> getAssetRepairTypeByAssetCategoryId(Long assetCategoryId) throws Exception;

	public List<AssetSubRepairType> getAssetSubRepairTypeByAssetRepairType(Long assetSubCategoryid) throws Exception;
	
	public AssetVO getAssetSubcategoryDetails(Long categoryId, Long subCategoryId1,  Long assetId) throws Exception;
	
	public String getTenantName(String email) throws Exception;
}
