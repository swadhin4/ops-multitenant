package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SiteLicenceVO;
import com.pms.app.view.vo.SiteSubmeterVO;


public interface SiteService {

	public List<CreateSiteVO> getSiteList(LoginUser user) throws Exception;
	

	public CreateSiteVO saveSite(CreateSiteVO siteVO, LoginUser user) throws Exception;
	
	public CreateSiteVO findSiteBySiteId(Long siteId, LoginUser user) throws Exception;

	public List<SiteLicenceVO> findLicenseBySiteId(Long siteId, LoginUser user) throws Exception;

	public List<Integer> saveSiteLicense(List<SiteLicenceVO> siteLicenseVOList, Long siteId, LoginUser user)  throws Exception;
	
	public int saveSiteSubmeter(List<SiteSubmeterVO> siteSubmeterVOList, Long siteId, LoginUser user, String mode)  throws Exception;

	public int saveSiteOperatingTimings(CreateSiteVO createSiteVO, Long siteId, LoginUser loginUser, String mode) throws Exception;

	public CreateSiteVO updateSite(CreateSiteVO createSiteVO, LoginUser loginUser) throws Exception;


	public int updateSiteLicense(List<SiteLicenceVO> siteLicenseVOList, Long siteId, LoginUser user) throws Exception;


	public List<CreateSiteVO> getSPSiteList(String rspcode, String custDBName, LoginUser user) throws Exception;


	public List<CreateSiteVO> getSiteListForCompany(LoginUser user, String custcompCode) throws Exception;

}
