/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.app.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.pms.app.view.vo.AssetVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.AssetCategory;
import com.pms.jpa.entities.AssetLocation;
import com.pms.jpa.entities.AssetRepairType;
import com.pms.jpa.entities.AssetSubRepairType;
import com.pms.web.service.AssetService;
import com.pms.web.util.RestResponse;

/**
 * The Class UserController.
 *
 */
@RequestMapping(value = "/asset")
@Controller
public class AssetController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(AssetController.class);

	
	@Autowired
	private AssetService assetService;



	@RequestMapping(value = "/home", method = RequestMethod.GET, produces = "application/json")
	public String userHome(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			boolean isSessionEnabled=request.isRequestedSessionIdValid();
			System.out.println(isSessionEnabled +""+ request.getSession().getId());
			model.put("user", loginUser);
			return "asset.list";
		} else {
			return "redirect:/login";
		}
	}


	@RequestMapping(value = "/details", method = RequestMethod.GET)
	public String assetPage(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				return "asset.details";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/equipment/create", method = RequestMethod.GET)
	public String equipmentCreate(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "NEW");
				return "asset.equipment";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/service/create", method = RequestMethod.GET)
	public String serviceCreate(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "NEW");
				session.setAttribute("imageList", null);
				return "asset.service";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/equipment/update", method = RequestMethod.GET)
	public String equipmentUpdate(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "EDIT");
				session.setAttribute("imageList", null);
				return "asset.equipmentupdate";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/service/update", method = RequestMethod.GET)
	public String serviceUpdate(final Locale locale, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("mode", "EDIT");
				session.setAttribute("imageList", null);
				return "asset.serviceupdate";
			}
		} else {
			return "redirect:/login";
		}
	}
	
	@RequestMapping(value = "/info/{siteId}", method = RequestMethod.GET)
	public String assetSitePage(@PathVariable(value="siteId") String siteId, final ModelMap model,
			final HttpServletRequest request, final HttpSession session) {
		LoginUser loginUser=getCurrentLoggedinUser(session);
		if (loginUser!=null) {
			model.put("user", loginUser);
			if(loginUser.getSysPassword().equalsIgnoreCase("YES")){
				return "redirect:/user/profile";
			}else{
				model.put("siteId", siteId);
				return "asset.details";
			}
		} else {
			return "redirect:/login";
		}
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllAssets(final HttpSession session) {
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				List<AssetVO> assets = assetService.findAllAsset(loginUser);
				if (assets.isEmpty()) {
					response.setStatusCode(404);
					response.setMessage("No assets available");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}else{
					response.setStatusCode(200);
					response.setObject(assets);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			logger.info("Exception in getting asset list response", e);
			response.setStatusCode(500);
			response.setMessage("Exception occurred while getting asset list");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return responseEntity;
	}
	
	
	@RequestMapping(value = "/info/{assetId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getAssetInfo(@PathVariable(value="assetId") Long assetId, final HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				AssetVO assetVO = assetService.findAssetById(loginUser,assetId);
				if (assetVO.getAssetId()!=null) {
					response.setStatusCode(200);
					session.setAttribute("assetVO", assetVO);
					response.setObject(assetVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else{
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
				
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<AssetCategory>> listAllAssetCategories( final HttpSession session) {
		List<AssetCategory> assetCategories = null;
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
			assetCategories = assetService.getAllAssetCategories(loginUser);
			if (assetCategories.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<AssetCategory>>(assetCategories, HttpStatus.OK);
	}
	@RequestMapping(value = "/category/repairtype/{assetCategoryid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getAssetRepairType(@PathVariable(value="assetCategoryid") Long assetCategoryId, final HttpSession session) {
		logger.info("Inside AssetController .. getAssetRepairType");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
			List<AssetRepairType> assetRepairTypeVOList = assetService.findAssetRepairTypeBy(loginUser, assetCategoryId);
				if (assetRepairTypeVOList.isEmpty()) {
					response.setStatusCode(404);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}else{
					response.setStatusCode(200);
					response.setObject(assetRepairTypeVOList);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else{
				
			}
		} catch (Exception e) {
			logger.info("Exception while getting asset repair list ", e);
			e.printStackTrace();
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}
		
		logger.info("Exit AssetController .. getAssetRepairType");
		return responseEntity;
	}
	
	@RequestMapping(value = "/category/subrepairtype/{assetSubCategoryid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getAssetSubRepairType(final HttpSession session, @PathVariable(value="assetSubCategoryid") Long assetSubCategoryid) {
		logger.info("Inside AssetController .. getAssetSubRepairType");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
			List<AssetSubRepairType> assetSubRepairTypeVOList = assetService.findAssetSubRepairTypeBy(loginUser, assetSubCategoryid);
			if (assetSubRepairTypeVOList.isEmpty()) {
				response.setStatusCode(404);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}else{
				response.setStatusCode(200);
				response.setObject(assetSubRepairTypeVOList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			}
			}
		} catch (Exception e) {
			logger.info("Exception while getting asset repair list ", e);
			e.printStackTrace();
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}
		
		logger.info("Exit AssetController .. getAssetSubRepairType");
		return responseEntity;
	}
	

	@RequestMapping(value = "/locations", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<AssetLocation>> listAllAssetLocations(final HttpSession session) {
		List<AssetLocation> assetLocations= null;
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
			assetLocations = assetService.getAllAssetLocations(loginUser);
			if (assetLocations.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<AssetLocation>>(assetLocations, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestResponse> createNewAsset(final Locale locale, final ModelMap model,
			@RequestBody final AssetVO assetVO, final HttpSession session) {
		logger.info("Inside AssetController .. createNewAsset");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		LoginUser loginUser = getCurrentLoggedinUser(session);
		if(loginUser!=null){
			try {
				logger.info("AssetVO : "+ assetVO);
				response = assetService.saveOrUpdateAsset(assetVO, loginUser);
				if(response.getStatusCode()==200){
					if(response.getMode().equals("SAVING")){
						response.setStatusCode(200);
						response.setMessage("Asset created successfully");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
					}
					else if(response.getMode().equals("UPDATING")){
						response.setStatusCode(200);
						response.setMessage("Asset updated successfully");
						responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.OK);
						}
				}else{
					response.setStatusCode(204);
					response.setMessage("Asset code already exists for selected site");
					responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);
				}

			} catch (Exception e) {
				logger.info("Exception in getting response", e);
				response.setMessage("Exception while creating an asset");
				response.setStatusCode(500);
				responseEntity = new ResponseEntity<RestResponse>(response,HttpStatus.NOT_FOUND);

			}
		}else {
			response.setStatusCode(401);
			response.setMessage("Your current session is expired. Please login again");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
		}

		logger.info("Exit AssetController .. createNewAsset");
		return responseEntity;
	}
	
	
	@RequestMapping(value = "/delete/{assetId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> deleteAsset(@PathVariable(value="assetId") Long assetId, HttpSession session) {
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
		try {
			LoginUser loginUser = getCurrentLoggedinUser(session);
			if(loginUser!=null){
				AssetVO assetVO = assetService.findAssetById(loginUser,assetId);
				assetVO = assetService.deleteAsset(assetVO, loginUser);
				if (assetVO.getAssetId()!=null) {
					response.setStatusCode(200);
					response.setObject(assetVO);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else{
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}

		return responseEntity;
	}
	@RequestMapping(value = "/list/{siteId}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> listAllAssetsBySite(final HttpSession session, @PathVariable (value="siteId") Long siteId) {
		logger.info("Inside AssetController..listAllAssetsBySite");
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(HttpStatus.NO_CONTENT);
		RestResponse response = new RestResponse();
		try {
			LoginUser loginUser=getCurrentLoggedinUser(session);
			if (loginUser!=null) {
				List<AssetVO> assets = null;
				String custDbName = (String) session.getAttribute("customerLocation");
				if(StringUtils.isNotBlank(custDbName)){
					loginUser.setDbName(custDbName);
					assets  = assetService.findAssetBySiteId(loginUser,siteId);
				}else{
					assets  = assetService.findAssetBySiteId(loginUser,siteId);
				}
				if (assets.isEmpty()) {
					response.setStatusCode(404);
					response.setMessage("No assets available");
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
				}else{
					response.setStatusCode(200);
					response.setObject(assets);
					responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
				}
			}else {
				response.setStatusCode(401);
				response.setMessage("Your current session is expired. Please login again");
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			logger.info("Exception in getting asset list response", e);
			response.setStatusCode(500);
			response.setMessage("Exception occurred while getting asset list");
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		logger.info("Exit  AssetController..listAllAssetsBySite");
		return responseEntity;
	}


	
	/*

	
	

	@RequestMapping(value = "/locations", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<List<AssetLocation>> listAllAssetLocations() {
		List<AssetLocation> assetLocations= null;
		try {
			assetLocations = assetService.getAllAssetLocations();
			if (assetLocations.isEmpty()) {
				return new ResponseEntity(HttpStatus.NO_CONTENT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<List<AssetLocation>>(assetLocations, HttpStatus.OK);
	}
	

	
	@RequestMapping(value = "/category/subrepairtype/{assetSubCategoryid}", method = RequestMethod.GET,produces="application/json")
	public ResponseEntity<RestResponse> getAssetSubRepairType(@PathVariable(value="assetSubCategoryid") Long assetSubCategoryid) {
		logger.info("Inside AssetController .. getAssetSubRepairType");
		RestResponse response = new RestResponse();
		ResponseEntity<RestResponse> responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NO_CONTENT);
		try {
			List<AssetSubRepairType> assetSubRepairTypeVOList = assetService.findAssetSubRepairTypeBy(assetSubCategoryid);
			if (assetSubRepairTypeVOList.isEmpty()) {
				response.setStatusCode(404);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
			}else{
				response.setStatusCode(200);
				response.setObject(assetSubRepairTypeVOList);
				responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.info("Exception while getting asset repair list ", e);
			e.printStackTrace();
			responseEntity = new ResponseEntity<RestResponse>(response, HttpStatus.NOT_FOUND);
		}
		
		logger.info("Exit AssetController .. getAssetSubRepairType");
		return responseEntity;
	}*/
	
}
