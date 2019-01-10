package com.pms.web.service;

import java.io.IOException;
import java.util.List;

import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UploadFile;
import com.pms.jpa.entities.Company;
import com.pms.web.util.RestResponse;

public interface FileIntegrationService {

	public String siteFileUpload(LoginUser user, CreateSiteVO siteVO, UploadFile siteFile, Company company)  throws IOException;
	
	public String siteLicenseFileUpload(LoginUser user, UploadFile siteFile, Company company)  throws IOException;
	
	public String siteIncidentFileUpload(List<UploadFile> fileList,TicketVO customerTicketVO, Company company, String folderLocation, String uploadedBy)  throws IOException;
	
	/*public AssetVO siteAssetFileUpload(AssetVO assetVO,UploadFile assetFile, Company company, String type)  throws IOException;*/
	
	public String createIncidentFolder(String incidentNumber, Company company) throws IOException;
	
	public RestResponse getFileLocation(Company company, String keyName) throws Exception;
	
	public RestResponse deleteFile(String dbName, Long siteId, List<Long> licenseIdList, Long assetId, List<Long> incidentList, String keyName) throws Exception;
	
}
