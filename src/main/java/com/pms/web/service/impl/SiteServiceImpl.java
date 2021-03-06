package com.pms.web.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.pms.app.constants.UserType;
import com.pms.app.dao.impl.SiteDAO;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SiteDeliveryVO;
import com.pms.app.view.vo.SiteLicenceVO;
import com.pms.app.view.vo.SiteOperationVO;
import com.pms.app.view.vo.SiteSubmeterVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.web.service.FileIntegrationService;
import com.pms.web.service.SiteService;



@Service("siteService")
public class SiteServiceImpl implements SiteService{

	private final static Logger LOGGER = LoggerFactory.getLogger(SiteServiceImpl.class);
	private static final String SUFFIX = "/";
	
	private SiteDAO getSiteDAO(String dbName) {
		return new SiteDAO(dbName);
	}
	@Autowired
	private FileIntegrationService fileIntegrationService;
	public SiteServiceImpl() {
		super();
	}
	

	@Override
	public List<CreateSiteVO> getSiteList(LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		List<CreateSiteVO> siteList = new ArrayList<CreateSiteVO>();
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
			siteList = siteDAO.getSiteList(user.getUsername());
		}
		return siteList;
	}

	@Override
	public List<CreateSiteVO> getExtCustSiteList(LoginUser user, Long extCustId) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		List<CreateSiteVO> siteList = new ArrayList<CreateSiteVO>();
		 if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			siteList = siteDAO.getExtCustUserSiteList(user.getUsername(), extCustId);
		}
		return siteList;
	}
	
	@Override
	public List<CreateSiteVO> getSiteListForCompany(LoginUser user, String custCode) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		List<CreateSiteVO> siteList = siteDAO.getSiteListByCompany(custCode);
		return siteList;
	}
	@Override
	public List<CreateSiteVO> getSPSiteList(String rspcode, String custDBName, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(custDBName);
		List<CreateSiteVO> siteList = siteDAO.getSPSiteList(rspcode, user);
		return siteList;
		
	}

	@Override
	@Transactional
	public CreateSiteVO saveSite(CreateSiteVO siteVO, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		CreateSiteVO savedSiteVO = null;
		if(siteVO.getSiteId()==null){
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
				savedSiteVO = siteDAO.saveSite(siteVO, user);
			}
			else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				savedSiteVO = siteDAO.saveRSPExternalCustomerSite(siteVO, user);
				if(savedSiteVO.getSiteId()!=null){
					LOGGER.info("Updating RSP external customer site access");
					int updatedRow = siteDAO.updateRSPUserExtCustSiteAccess(user, savedSiteVO.getSiteId());		
					if(updatedRow>0){
						LOGGER.info("External Customer Site access updated for user :" + user.getUsername());
					}
				}
			}
			String siteFileAttachment = getSiteFileAttachment(savedSiteVO, user);
			if(!StringUtils.isEmpty(siteFileAttachment)){
				siteVO.setFileInput(siteFileAttachment);
				int updatedFile = siteDAO.updateAttachmentSite(savedSiteVO, user);
			}
		}
		return savedSiteVO;
	}
	
	@Override
	@Transactional
	public List<Integer> saveSiteLicense(List<SiteLicenceVO> siteLicenseVOList, Long siteId, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		List<Integer> savedLicenseRecords = new ArrayList<Integer>();
		int licenseRecordsInserted=0;
		int licenseRecordsUpdated=0;
		if(siteId!=null){
			List<SiteLicenceVO> newLicenseList = new ArrayList<SiteLicenceVO>();
			List<SiteLicenceVO> existingLicenseList = new ArrayList<SiteLicenceVO>();
			if(!siteLicenseVOList.isEmpty()){
				for(SiteLicenceVO siteLicense : siteLicenseVOList){
					if(siteLicense.getLicenseId()==null){
						newLicenseList.add(siteLicense);
					}else if(siteLicense.getLicenseId()!=null){
						existingLicenseList.add(siteLicense);
					}
				}
			}
			if(!newLicenseList.isEmpty()){
				existingLicenseList.clear();
				for(SiteLicenceVO existingLicense : newLicenseList){
					if(!StringUtils.isEmpty(existingLicense.getAttachment())){
						String siteLicenseAttachment = getLicensFileAttachment(existingLicense, user);
						existingLicense.setAttachment(siteLicenseAttachment);
					}
				}
				licenseRecordsInserted = siteDAO.insertSiteLicenseBatch(siteId,newLicenseList, user);
				savedLicenseRecords.add(licenseRecordsInserted);
			}else{
				savedLicenseRecords.add(0);
			}
			if(!existingLicenseList.isEmpty()){
				int count=0;
				newLicenseList.clear();
				for(SiteLicenceVO existingLicense : existingLicenseList){
					if(!StringUtils.isEmpty(existingLicense.getAttachment())){
						count++;
						String siteLicenseAttachment = getLicensFileAttachment(existingLicense, user);
						existingLicense.setAttachment(siteLicenseAttachment);
					}
				}
				
				licenseRecordsUpdated = siteDAO.updateSiteLicenseBatch(siteId,existingLicenseList, user);
				savedLicenseRecords.add(licenseRecordsUpdated);
			
			}else{
				savedLicenseRecords.add(0);
			}
		}
		return savedLicenseRecords;
	}
	

	@Override
	@Transactional
	public int updateSiteLicense(List<SiteLicenceVO> siteLicenseVOList, Long siteId, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		int licenseRecordsUpdated=0;
		if(siteId!=null){
			licenseRecordsUpdated = siteDAO.updateSiteLicenseBatch(siteId, siteLicenseVOList, user);
		}
		return licenseRecordsUpdated;
	}

	@Override
	@Transactional
	public int saveSiteOperatingTimings(CreateSiteVO createSiteVO, Long siteId, LoginUser user, String mode) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		int siteOperation=0;
		int siteDelivery=0;
		if(siteId!=null){
			//siteOperation = siteDAO.insertSiteOperatingBatch(siteId,createSiteVO.getSiteOperation(), user);
			//siteDelivery=siteDAO.insertSiteDeliveryBatch(siteId,createSiteVO.getSiteDelivery(), user);
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
				siteOperation = siteDAO.saveOrUpdateOeratingTimings(siteId,createSiteVO.getSiteOperation(),createSiteVO.getSiteDelivery(), user, mode);
			}
			else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				siteOperation = siteDAO.saveOrUpdateOeratingTimings(siteId,createSiteVO.getSiteOperation(),createSiteVO.getSiteDelivery(), user, mode);
			}
			
		}
		return siteOperation+siteDelivery;
	}
	
	@Override
	@Transactional
	public int saveSiteSubmeter(List<SiteSubmeterVO> siteSubmeterVOList, Long siteId, LoginUser user, String mode) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		List<Integer> savedSubmeterVORecords = new ArrayList<Integer>();
		int subMeterRecordsInserted=0;
		if(siteId!=null){
			List<SiteSubmeterVO> newSubmeterList = new ArrayList<SiteSubmeterVO>();
			List<SiteSubmeterVO> existingSubmeterList = new ArrayList<SiteSubmeterVO>();
			if(!siteSubmeterVOList.isEmpty()){
				for(SiteSubmeterVO siteSubmeter : siteSubmeterVOList){
					if(siteSubmeter.getSubMeterId()==null){
						newSubmeterList.add(siteSubmeter);
					}else if(siteSubmeter.getSubMeterId()!=null){
						existingSubmeterList.add(siteSubmeter);
					}
				}
			}
			if(!newSubmeterList.isEmpty()){
				subMeterRecordsInserted = siteDAO.saveOrUpdateSubmeterBatch(siteId,newSubmeterList, user,"ADD");
				savedSubmeterVORecords.add(subMeterRecordsInserted);
			}else{
				savedSubmeterVORecords.add(0);
			}
			if(!existingSubmeterList.isEmpty()){
				subMeterRecordsInserted = siteDAO.saveOrUpdateSubmeterBatch(siteId,existingSubmeterList, user,"UPDATE");
				savedSubmeterVORecords.add(subMeterRecordsInserted);
			}else{
				savedSubmeterVORecords.add(0);
			}
		}
		return subMeterRecordsInserted;
	}
	
	@Override
	public CreateSiteVO findSiteBySiteId(Long siteId, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		CreateSiteVO savedSiteVO =null;
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			savedSiteVO  = siteDAO.getSiteDetails(siteId);
			List<SiteOperationVO> salesTimeList = siteDAO.getSiteSalesTimings(siteId);
			if(!salesTimeList.isEmpty()){
				savedSiteVO.setSiteOperation(salesTimeList);
			}else{
				LOGGER.info("No Sales timing found for selected Site :" + savedSiteVO.getSiteName());
			}
			List<SiteDeliveryVO> deliveryTimeList = siteDAO.getSiteDeliveryTimings(siteId);
			if(!deliveryTimeList.isEmpty()){
				savedSiteVO.setSiteDelivery(deliveryTimeList);
			}else{
				LOGGER.info("No Delivery timing found for selected Site :" + savedSiteVO.getSiteName());
			}
		}
		else{
			savedSiteVO  = siteDAO.getSiteDetails(siteId);
		List<SiteLicenceVO> licenseList = siteDAO.getSiteLicense(siteId);
		if(!licenseList.isEmpty()){
			savedSiteVO.setSiteLicense(licenseList);
		}else{
			LOGGER.info("No license found for selected Site :" + savedSiteVO.getSiteName());
		}
		List<SiteOperationVO> salesTimeList = siteDAO.getSiteSalesTimings(siteId);
		if(!salesTimeList.isEmpty()){
			savedSiteVO.setSiteOperation(salesTimeList);
		}else{
			LOGGER.info("No Sales timing found for selected Site :" + savedSiteVO.getSiteName());
		}
		List<SiteDeliveryVO> deliveryTimeList = siteDAO.getSiteDeliveryTimings(siteId);
		if(!deliveryTimeList.isEmpty()){
			savedSiteVO.setSiteDelivery(deliveryTimeList);
		}else{
			LOGGER.info("No Delivery timing found for selected Site :" + savedSiteVO.getSiteName());
		}
		
		List<SiteSubmeterVO> submeterList = siteDAO.getSiteSubmeterList(siteId);
		if(!submeterList.isEmpty()){
			savedSiteVO.setSiteSubmeter(submeterList);
		}else{
			LOGGER.info("No Submeter details found for selected Site :" + savedSiteVO.getSiteName());
		}
		}
		return savedSiteVO;
	}

	@Override
	public List<SiteLicenceVO> findLicenseBySiteId(Long siteId, LoginUser user) throws Exception {
		return null;
	}

	@Override
	@Transactional
	public CreateSiteVO updateSite(CreateSiteVO siteVO, LoginUser user) throws Exception {
		SiteDAO siteDAO=getSiteDAO(user.getDbName());
		CreateSiteVO savedSiteVO = null;
		if(siteVO.getSiteId()!=null){
			if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_CUSTOMER.getUserType())){
				savedSiteVO = siteDAO.saveSite(siteVO, user);
			}
			else if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
				int updatedRow = siteDAO.updateRSPExternalCustomerSite(siteVO, user);
				if(updatedRow>0){
					savedSiteVO = siteVO;
					savedSiteVO.setStatus(200);
				}
			}
			String siteFileAttachment = getSiteFileAttachment(siteVO, user);
			if(!StringUtils.isEmpty(siteFileAttachment)){
				siteVO.setFileInput(siteFileAttachment);
				int updatedFile = siteDAO.updateAttachmentSite(siteVO, user);
			}
		}
		return savedSiteVO;
	}


	private String getSiteFileAttachment(CreateSiteVO siteVO, LoginUser user) throws IOException {
		String siteFileKey = null;
		if(!StringUtils.isEmpty(siteVO.getFileInput())){
			UploadFile siteFile = new UploadFile();
			siteFile.setSiteId(siteVO.getSiteId());
			siteFile.setFileExtension(siteVO.getFileExtension());
			siteFile.setBase64ImageString(siteVO.getFileInput());
			siteFileKey = fileIntegrationService.siteFileUpload(user,siteVO, siteFile, user.getCompany());
		}
		return siteFileKey;
	}

	private String getLicensFileAttachment(SiteLicenceVO existingLicense, LoginUser user) throws IOException {
		String siteFileKey = null;
		if(!StringUtils.isEmpty(existingLicense.getAttachment())){
			UploadFile licenseFile = new UploadFile();
			licenseFile.setFileName(existingLicense.getLicenseName());
			licenseFile.setLicenseId(existingLicense.getLicenseId());
			licenseFile.setBase64ImageString(existingLicense.getAttachment());
			licenseFile.setFileExtension("pdf");
			siteFileKey = fileIntegrationService.siteLicenseFileUpload(user,licenseFile,  user.getCompany());
		}
		return siteFileKey;
	}



}
