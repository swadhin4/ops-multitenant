package com.pms.web.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pms.app.constants.AppConstants;
import com.pms.app.constants.UserType;
import com.pms.app.dao.impl.AssetDAO;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.AssetTask;
import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.jpa.entities.Asset;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;
import com.pms.web.service.AssetService;
import com.pms.web.util.RestResponse;

@Service("assetService")
public class AssetServiceImpl implements AssetService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AssetServiceImpl.class);


	private AssetDAO getAssetDAO(String dbName) {
		return new AssetDAO(dbName);
	}

	private SiteDAO getSiteDAO(String dbName) {
		return new SiteDAO(dbName);
	}

	/*
	 * @Autowired private AssetCategoryRepo assetCategoryRepo;
	 * 
	 * @Autowired private AssetLocationRepo assetLocationRepo;
	 * 
	 * @Autowired private ServiceProviderRepo serviceProviderRepo;
	 * 
	 * @Autowired private UserSiteAccessRepo userSiteAccessRepo;
	 * 
	 * @Autowired private FileIntegrationService fileIntegrationService;
	 */

	@Override
	@Transactional
	public List<AssetVO> findAllAsset(LoginUser user, final String viewtype) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. findAllAsset");
		LOGGER.info("Getting Asset List for logged in user : " + user.getFirstName() + "" + user.getLastName());
		SiteDAO siteDAO = getSiteDAO(user.getDbName());
		List<CreateSiteVO> siteList = null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			 if(!StringUtils.isEmpty(viewtype)){
				 siteList = siteDAO.getExtCustUserSiteList(user.getUsername(), Long.parseLong(viewtype));
			 }else{
				 throw new Exception("Please select the customer");
			 }
			 
		}else{
			 siteList = siteDAO.getSiteList(user.getUsername());
		}
		
		Set<Long> siteIdList = new HashSet<Long>();
		for (CreateSiteVO siteVO : siteList) {
			siteIdList.add(siteVO.getSiteId());
		}
		List<AssetVO> siteAssetList = getAssetDAO(user.getDbName()).findBySiteIdIn(siteIdList, viewtype);
		LOGGER.info("Total Assets for user : " + siteAssetList.size());

		LOGGER.info("Exit AssetServiceImpl .. findAllAsset");
		return siteAssetList == null ? Collections.emptyList() : siteAssetList;
	}

	@Override
	public AssetVO findAssetById(LoginUser user, Long assetid) {
		LOGGER.info("Inside AssetServiceImpl .. findAssetById");
		AssetVO assetVO = null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			assetVO = getAssetDAO(user.getDbName()).getExtCustAssetDetails(assetid);
			List<AssetTask> taskList = getAssetDAO(user.getDbName()).findAssetTaskByAsset(assetVO.getAssetId());
			if(!taskList.isEmpty()){
				assetVO.setTaskList(taskList);
			}
		}else{
			assetVO = getAssetDAO(user.getDbName()).getAssetDetails(assetid);
			List<AssetTask> taskList = getAssetDAO(user.getDbName()).findAssetTaskByAsset(assetVO.getAssetId());
			if(!taskList.isEmpty()){
				assetVO.setTaskList(taskList);
			}
		}
		
		LOGGER.info("Exit AssetServiceImpl .. findAssetById");
		return assetVO;
	}
	@Override
	public List<AssetVO> findAssetBySiteId(LoginUser user,Long siteId) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. findAssetsBySite");
		List<AssetVO> assetList = null;
		List<AssetVO> rspAssignedAssetList = new ArrayList<AssetVO>();
		List<AssetVO> customerAssetList = new ArrayList<AssetVO>();
		synchronized (siteId) {
			assetList = getAssetDAO(user.getDbName()).findAssetBySiteId(siteId, user);
			for(AssetVO asset: assetList){
				if(asset.getSpType().equalsIgnoreCase("RSP") && 
						user.getCompany().getCompanyCode().equalsIgnoreCase(asset.getSpCode())){
					rspAssignedAssetList.add(asset);
				}else{
					customerAssetList.add(asset);
				}
			}
		}
		if(user.getUserType().equalsIgnoreCase("USER")){
			LOGGER.info("Listing Assets for Customer of both EXTERNAL and REGISTERED for Site id : "+ siteId);
			LOGGER.info("Asset assigned to registered SP : "+ rspAssignedAssetList.size());
			LOGGER.info("Asset assigned to Externa SP : "+ customerAssetList.size());
			for(AssetVO asset: rspAssignedAssetList){
				customerAssetList.add(asset);
			}
			LOGGER.info("Total Assets for Customer : "+ customerAssetList.size());
			return customerAssetList == null?Collections.emptyList():customerAssetList;
		}else{
			LOGGER.info("Asset assigned to registered SP : "+ rspAssignedAssetList.size());
			return rspAssignedAssetList == null?Collections.emptyList():rspAssignedAssetList;
		}
		
	}

	@Override
	public List<AssetCategory> getAllAssetCategories(LoginUser user) throws Exception {
		List<AssetCategory> assetCategories = getAssetDAO(user.getDbName()).findAssetCategories();
		List<AssetCategory> tempCategories = assetCategories;
		AssetCategory removedAssetCategory = null;
		for (AssetCategory assetCategory : tempCategories) {
			if (assetCategory.getAssetCategoryName().equalsIgnoreCase("Other")) {
				removedAssetCategory = assetCategory;
				assetCategories.remove(assetCategory);
				break;
			}
		}
		if (removedAssetCategory != null) {
			assetCategories.add(removedAssetCategory);
		}
		return assetCategories == null ? Collections.emptyList() : assetCategories;
	}

	@Override
	public List<AssetLocation> getAllAssetLocations(LoginUser user) throws Exception {
		List<AssetLocation> assetLocations = getAssetDAO(user.getDbName()).findAssetLocations();
		AssetLocation removedAssetLocation = null;
		for (AssetLocation assetLocation : assetLocations) {
			if (assetLocation.getLocationName().equalsIgnoreCase("Other")) {
				removedAssetLocation = assetLocation;
				assetLocations.remove(assetLocation);
				break;
			}
		}
		if (removedAssetLocation != null) {
			assetLocations.add(removedAssetLocation);
		}
		return assetLocations == null ? Collections.emptyList() : assetLocations;
	}

	@Override
	public List<AssetRepairType> findAssetRepairTypeBy(LoginUser user, Long assetCategoryId) throws Exception {
		return getAssetDAO(user.getDbName()).getAssetRepairTypeByAssetCategoryId(assetCategoryId);
	}
	
	@Override
	public List<AssetSubRepairType> findAssetSubRepairTypeBy(LoginUser user,Long assetSubCategoryid) throws Exception {
		return getAssetDAO(user.getDbName()).getAssetSubRepairTypeByAssetRepairType(assetSubCategoryid);
	}


	@Override
	public List<ServiceProviderVO> findSPByCompanyIdIn(LoginUser user) {
		List<ServiceProviderVO> serviceProviderList = getAssetDAO(user.getDbName())
				.findSPByCompanyId(user.getCompany().getCompanyId());
		return serviceProviderList;
	}

	@Override
	@Transactional
	public RestResponse saveOrUpdateAsset(AssetVO assetVO, LoginUser user) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. saveOrUpdateAsset");
		List<AssetVO> assetVOList = new ArrayList<AssetVO>();
		RestResponse response = new RestResponse();
		List<Asset> assetList = new ArrayList<Asset>();
		boolean isAssetAvailable = false;
		Set<Long> uniqueSites = new HashSet<>(assetVO.getSites());
		LOGGER.info("No sites selected for asset : " + uniqueSites);
		if (uniqueSites.size() == 0) {
			LOGGER.info("No sites selected for asset : " + assetVO.getAssetName());
		} else {
			if (assetVO.getAssetId() == null) {
				assetList = getAssetDAO(user.getDbName()).findByAssetCodeAndSiteIdInAndDelFlag(assetVO.getAssetCode(),assetVO.getSites(), 0);
				// assetList=
				// assetRepo.findByAssetCodeAndSiteIdInAndDelFlag(assetVO.getAssetCode(),
				// assetVO.getSites(), 0);

				if (assetList.isEmpty()) {
					LOGGER.info("No asset found for asset code : " + assetVO.getAssetCode() + " and for sites :"
							+ assetVO.getSites());
					isAssetAvailable = false;
				} else {
					isAssetAvailable = true;
				}
				if (!isAssetAvailable) {
					assetVOList = saveAssetsforMultipleSites(assetVO, user, assetVOList);
					if (!assetVOList.isEmpty()) {
						response.setStatusCode(200);
						response.setMode("SAVING");
					}

				} else {
					LOGGER.info("Asset already exists for any of these sites : " + assetVO.getSites());
					response.setStatusCode(204);
				}
			} else {

				assetList = getAssetDAO(user.getDbName()).findByAssetCodeAndSiteIdInAndDelFlag(assetVO.getAssetCode(),assetVO.getSites(), 0);
				if (assetList.isEmpty()) {
					LOGGER.info("No asset found for asset code : " + assetVO.getAssetCode() + " and for sites :"+ assetVO.getSites());
					isAssetAvailable = false;
				} else {
					isAssetAvailable = true;
				}
				if (!isAssetAvailable) {
					assetVOList = updateAssetsforMultipleSites(null, assetVO, user, assetVOList);
					if (assetVOList.isEmpty()) {
						LOGGER.info("Asset already exists for site " + assetList.get(0).getSiteId());
						response.setStatusCode(204);
					} else {
						response.setStatusCode(200);
						response.setMode("UPDATING");
					}
				} else {
					assetVOList = updateAssetsforMultipleSites(assetList.get(0), assetVO, user, assetVOList);
					if (assetVOList.isEmpty()) {
						LOGGER.info("Asset already exists for site " + assetList.get(0).getSiteId());
						response.setStatusCode(204);
					} else {
						response.setStatusCode(200);
						response.setMode("UPDATING");
					}
				}

			}
		}

		LOGGER.info("Exit AssetServiceImpl .. saveOrUpdateAsset");
		return response;

	}

	private List<AssetVO> saveAssetsforMultipleSites(AssetVO assetVO, LoginUser user, List<AssetVO> assetVOList) {
		List<Asset> savedAssetList = new ArrayList<Asset>();
		for (Long site : assetVO.getSites()) {
			Asset asset = new Asset();
			BeanUtils.copyProperties(assetVO, asset);
			asset.setSiteId(site);
			asset.setCreatedBy(user.getUsername());
			// savedAssetVO = saveOrUpdateAssetSites(assetVO, asset,
			// assetVOList);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			if (!StringUtils.isEmpty(assetVO.getCommisionedDate())) {
				Date commDate;
				try {
					commDate = formatter.parse(assetVO.getCommisionedDate());
					asset.setDateCommissioned(commDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}

			if (!StringUtils.isEmpty(assetVO.getDeCommissionedDate())) {
				Date deCommDate;
				try {
					deCommDate = formatter.parse(assetVO.getDeCommissionedDate());
					asset.setDateDeComissioned(deCommDate);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (assetVO.getAssetType().equalsIgnoreCase("E")) {
				LOGGER.info("Validating  Asset Electrical and Power sensor attached");
				if (!StringUtils.isEmpty(assetVO.getIsAssetElectrical())) {
					if (assetVO.getIsAssetElectrical().equalsIgnoreCase("YES")) {
						LOGGER.info("Asset is electrical");
						asset.setIsAssetElectrical(assetVO.getIsAssetElectrical());
						if (assetVO.getIsPWSensorAttached().equalsIgnoreCase("YES")) {
							LOGGER.info("Asset has power sensor attached");
							asset.setIsPWSensorAttached(assetVO.getIsPWSensorAttached());

							if (!StringUtils.isEmpty(assetVO.getPwSensorNumber())) {
								LOGGER.info("Asset has power sensor number");
								asset.setPwSensorNumber(assetVO.getPwSensorNumber());
							} else {
								LOGGER.info("Asset power sensor must not be empty");
								throw new RuntimeException("Asset power sensor should not be empty.");
							}
						} else {
							LOGGER.info("Asset has no power sensor attached");
							asset.setIsPWSensorAttached("NO");
							asset.setPwSensorNumber("");
						}
					} else {
						LOGGER.info("Asset is not electical");
						asset.setIsAssetElectrical("NO");
						asset.setIsPWSensorAttached("NO");
						asset.setPwSensorNumber("");
					}

				}
			}
			asset.setCategoryId(assetVO.getCategoryId());
			asset.setSubCategoryId1(assetVO.getSubCategoryId1());
			asset.setSpType(assetVO.getSpType());
			asset.setExtCustId(assetVO.getExtCustId());
			savedAssetList.add(asset);

			/*
			 * Asset savedAsset = getAssetDAO(user.getDbName()).saveAsset(asset,
			 * assetVO, user); if (savedAsset.getAssetId() != null) {
			 * //BeanUtils.copyProperties(savedAsset, savedAssetVO);
			 * savedAssetVO.setAssetId(savedAsset.getAssetId());
			 * assetVOList.add(savedAssetVO); LOGGER.info(
			 * "Asset created successfully for site " + savedAsset.getAssetId()
			 * + " / " + asset.getSiteId()); savedAssetList.add(savedAsset); }
			 */

			/*
			 * if(assetVO.getSites().size()==1){
			 * if(assetVO.getAssetType().equalsIgnoreCase("E")){
			 * if(!StringUtils.isEmpty(assetVO.getAssetImage().
			 * getBase64ImageString())){ LOGGER.info("Asset Image uploading..");
			 * assetVO = uploadAssetFiles(assetVO, user, "IMAGE",
			 * user.getCompany(), savedAssetList.get(0)); } }
			 * 
			 * if(!StringUtils.isEmpty(assetVO.getAssetDoc().
			 * getBase64ImageString())){ LOGGER.info(
			 * "Asset Document uploading.."); assetVO =
			 * uploadAssetFiles(assetVO, user, "DOC", user.getCompany(),
			 * savedAssetList.get(0)); }
			 * 
			 * }
			 */

		}
		try {
			int rspExtCustInserted=0;
			int rspCustInserted=0;
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				rspExtCustInserted = getAssetDAO(user.getDbName()).saveExtCustAssetInBatch(savedAssetList, assetVO, user);
				if(rspExtCustInserted>0){
					assetVOList.add(assetVO);
				}
			}
			else{
				rspCustInserted = getAssetDAO(user.getDbName()).saveAssetInBatch(savedAssetList, assetVO, user);
				if(rspCustInserted>0){
					assetVOList.add(assetVO);
				}
			}
			
		} catch (Exception e) {
			assetVOList.clear();
			e.printStackTrace();
		}
		return assetVOList;
	}

	private List<AssetVO> updateAssetsforMultipleSites(Asset savedAsset, AssetVO assetVO, LoginUser user,
			List<AssetVO> assetVOList) {
		AssetVO savedAssetVO = null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			savedAssetVO = getAssetDAO(user.getDbName()).getExtCustAssetDetails(assetVO.getAssetId());
		}else{
			savedAssetVO= getAssetDAO(user.getDbName()).getAssetDetails(assetVO.getAssetId());
		}
		
		Asset asset = new Asset();
		if (savedAsset == null) {
			LOGGER.info("New Asset code is updating for asset : " + assetVO.getAssetName() + " and site :"+ assetVO.getSiteId());
			asset.setAssetId(savedAssetVO.getAssetId());
			asset.setAssetCode(savedAssetVO.getAssetCode());
			assetVOList = updateAssetForSite(assetVO, user, assetVOList, asset);
		} else if (savedAsset.getAssetCode().equalsIgnoreCase(savedAssetVO.getAssetCode()) && savedAsset.getSiteId().equals(savedAssetVO.getSiteId())) {
			LOGGER.info("Found same asset Code and Site ID, so updating the same");
			asset.setAssetId(savedAssetVO.getAssetId());
			asset.setAssetCode(savedAssetVO.getAssetCode());
			assetVOList = updateAssetForSite(assetVO, user, assetVOList, asset);

		} else {
			LOGGER.info("Asset code already exists.");
		}
		return assetVOList == null ? Collections.emptyList() : assetVOList;
	}

	private List<AssetVO> updateAssetForSite(AssetVO assetVO, LoginUser user, List<AssetVO> assetVOList, Asset asset) {

		BeanUtils.copyProperties(assetVO, asset);
		asset.setModifiedBy(user.getUsername());
		asset.setModifiedDate(new Date());
		// assetVO = saveOrUpdateAssetSites(assetVO, asset, assetVOList);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!StringUtils.isEmpty(assetVO.getCommisionedDate())) {
			Date commDate;
			try {
				commDate = formatter.parse(assetVO.getCommisionedDate());
				asset.setDateCommissioned(commDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		if (!StringUtils.isEmpty(assetVO.getDeCommissionedDate())) {
			Date deCommDate;
			try {
				deCommDate = formatter.parse(assetVO.getDeCommissionedDate());
				asset.setDateDeComissioned(deCommDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if (assetVO.getAssetType().equalsIgnoreCase("E")) {
			LOGGER.info("Validating  Asset Electrical and Power sensor attached");
			if (!StringUtils.isEmpty(assetVO.getIsAssetElectrical())) {
				if (assetVO.getIsAssetElectrical().equalsIgnoreCase("YES")) {
					LOGGER.info("Asset is electrical");
					asset.setIsAssetElectrical(assetVO.getIsAssetElectrical());
					if (assetVO.getIsPWSensorAttached().equalsIgnoreCase("YES")) {
						LOGGER.info("Asset has power sensor attached");
						asset.setIsPWSensorAttached(assetVO.getIsPWSensorAttached());

						if (!StringUtils.isEmpty(assetVO.getPwSensorNumber())) {
							LOGGER.info("Asset has power sensor number");
							asset.setPwSensorNumber(assetVO.getPwSensorNumber());
						} else {
							LOGGER.info("Asset power sensor must not be empty");
							throw new RuntimeException("Asset power sensor should not be empty.");
						}
					} else {
						LOGGER.info("Asset has no power sensor attached");
						asset.setIsPWSensorAttached("NO");
						asset.setPwSensorNumber("");
					}
				} else {
					LOGGER.info("Asset is not electical");
					asset.setIsAssetElectrical("NO");
					asset.setIsPWSensorAttached("NO");
					asset.setPwSensorNumber("");
				}

			}
		}
		asset.setCategoryId(assetVO.getCategoryId());
		asset.setSubCategoryId1(assetVO.getSubCategoryId1());
		assetVO.getSites().add(assetVO.getSiteId());
		asset.setSpType(assetVO.getSpType());

	/*	if (assetVO.getAssetType().equalsIgnoreCase("E")) {
			if (!StringUtils.isEmpty(assetVO.getAssetImage().getBase64ImageString())) {
				assetVO = uploadAssetFiles(assetVO, user, "IMAGE", user.getCompany(), asset);

			}
		}

		if (!StringUtils.isEmpty(assetVO.getAssetDoc().getBase64ImageString())) {
			assetVO = uploadAssetFiles(assetVO, user, "DOC", user.getCompany(), asset);
		}*/
		if (org.apache.commons.lang3.StringUtils.isNotBlank(assetVO.getImagePath())) {
			asset.setImagePath(assetVO.getImagePath());
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(assetVO.getDocumentPath())) {
			asset.setDocumentPath(assetVO.getDocumentPath());
		}
		try {
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				int i = getAssetDAO(user.getDbName()).updateExtCustAsset(asset, assetVO, user);
				if(i>0){
					assetVOList.add(assetVO);
				}
			}else{
				int i = getAssetDAO(user.getDbName()).updateAsset(asset, assetVO, user);
				if(i>0){
					assetVOList.add(assetVO);
				}
			}
			
		} catch (Exception e) {
			assetVOList.clear();
			e.printStackTrace();
		}
		return assetVOList;
	}
	

	@Override
	@Transactional
	public AssetVO deleteAsset(AssetVO assetVO, LoginUser user) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. deleteAsset");
		try {
			int deletedRows = getAssetDAO(user.getDbName()).deleteAsset(assetVO.getAssetId());
			if(deletedRows>0){
				assetVO.setDelFlag(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return assetVO;
	}

	@Override
	public AssetTask saveOrUpdateAssetTask(AssetTask assetTask, LoginUser loginUser) throws Exception {
		LOGGER.info("Inside AssetServiceImpl .. saveOrUpdateAssetTask");
		AssetTask savedAssetTask =null;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		if (!StringUtils.isEmpty(assetTask.getPlanStartDate())) {
			Date startDate;
			Date endDate;
			try {
				startDate = formatter.parse(assetTask.getPlanStartDate());
				endDate = formatter.parse(assetTask.getPlanEndDate());
				assetTask.setPlannedStartDate(startDate);
				assetTask.setPlannedComplDate(endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(assetTask.getTaskId()==null){
			savedAssetTask = getAssetDAO(loginUser.getDbName()).saveAssetTask(assetTask, loginUser);
			savedAssetTask.setStatus(200);
		}
		else if(assetTask.getTaskId()!=null){
			int updated = getAssetDAO(loginUser.getDbName()).updateAssetTask(assetTask, loginUser);
			if(updated>0){
				savedAssetTask=assetTask;
				savedAssetTask.setStatus(202);
			}
		}
		LOGGER.info("Exit AssetServiceImpl .. saveOrUpdateAssetTask");
		
		return savedAssetTask;
	}

	@Override
	public List<AssetVO> findAssetList(LoginUser loginUser, final String custCompanyCode) throws Exception {
		List<AssetVO> assetList = new ArrayList<AssetVO>();
		if(loginUser.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			assetList = getAssetDAO(loginUser.getDbName()).findAssetList(loginUser.getCompany().getCompanyId(), custCompanyCode, AppConstants.RSP_ASSIGNED_ASSET_LIST_QUERY);
		}
		return assetList==null?Collections.emptyList():assetList;
	}

	
}
