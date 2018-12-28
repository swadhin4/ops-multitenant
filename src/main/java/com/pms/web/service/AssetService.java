package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;
import com.pms.web.util.RestResponse;


public interface AssetService {

	public List<AssetVO> findAllAsset(LoginUser user, String viewtype) throws Exception;
	
	public AssetVO findAssetById(LoginUser user, Long assetid);
	
	public List<AssetCategory> getAllAssetCategories(LoginUser user) throws Exception;
	
	public List<AssetLocation> getAllAssetLocations(LoginUser user) throws Exception;
	
	public List<AssetRepairType> findAssetRepairTypeBy(LoginUser user, Long assetCategoryId) throws Exception;

	public List<ServiceProviderVO> findSPByCompanyIdIn(LoginUser user);
	
	public RestResponse saveOrUpdateAsset(AssetVO assetVO, LoginUser loginUser) throws Exception;
	
	public AssetVO deleteAsset(AssetVO assetVO, LoginUser loginUser) throws Exception;

	public List<AssetVO> findAssetBySiteId(LoginUser loginUser, Long siteId) throws Exception;

	public List<AssetSubRepairType> findAssetSubRepairTypeBy(LoginUser user, Long assetSubCategoryid) throws Exception;

	/*public List<AssetVO> findAssetsBySite(Long siteId) throws Exception;

	

	public AssetVO findAssetByModelNumber(String modelNumber) throws Exception; 

	

	

	*/

}
