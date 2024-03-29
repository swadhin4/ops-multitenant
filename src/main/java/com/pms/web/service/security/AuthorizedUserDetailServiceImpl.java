/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.service.security;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.pms.app.constants.AppConstants;
import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.UserDAO;
import com.pms.jpa.entities.Tenant;
import com.pms.jpa.entities.UserModel;
import com.pms.web.service.TenantService;
import com.pms.web.util.ApplicationUtil;

/**
 * The Class CustomUserDetailServiceImpl.
 *
 */
@Service("userAuthorizationService")
@Transactional(readOnly = true)
public class AuthorizedUserDetailServiceImpl implements  UserDetailsService {
	private final static Logger LOGGER = LoggerFactory.getLogger(AuthorizedUserDetailServiceImpl.class);
	@Autowired
	private TenantService tenantService;
	
	 @Autowired
	 private HttpServletRequest request;
	 
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		try {
			request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
			String userType = request.getParameter("usertype");
			System.out.println(userType);
			UserModel user = null;
			if (userType.equals("1")) {
				user = getUserDetails(username, "USER");
				boolean isCompanyRegistered = validateCompanyRegistration(user.getCompanyCode());
				if(isCompanyRegistered){
					user.setRegistered(isCompanyRegistered);
				}
				
			} else if (userType.equals("2")) {
				user = getUserDetails(username, "SP");
			} else if (userType.equalsIgnoreCase("EXTSP")) {
				user = getUserDetails(username, "EXTSP");
			}
			if (user == null) {
				throw new UsernameNotFoundException("Username not found");
			} else {
				boolean accountNonExpired = true;
				boolean credentialsNonExpired = true;
				boolean accountNonLocked = true;
				if (userType.equals("1")) {
					user.setUserType("USER");
				} else if (userType.equals("2")) {
					user.setUserType("SP");
				} else if (userType.equalsIgnoreCase("EXTSP")) {
					user.setUserType("EXTSP");
				}

				AuthorizedUserDetails authorizedUserDetails = new AuthorizedUserDetails(user.getEmailId(),
						user.getPassword(), true, accountNonExpired, credentialsNonExpired, accountNonLocked,
						getAuthorities(user.getRoleNameList()));
				authorizedUserDetails.setUser(user);
				return authorizedUserDetails;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}


	private boolean validateCompanyRegistration(String companyCode) {
		boolean isPresent=false;
		String companyPath = System.getProperty("catalina.base");
		companyPath=companyPath.replaceAll("\\\\","/");
		LOGGER.info("companyPath : " +companyPath );
		File folder = FileSystems.getDefault().getPath(companyPath+"/uploads/malay-first-s3-bucket-pms-test").toFile();
		if(folder!=null){
		String[] folders = folder.list();
        for (String folderName : folders){
        	if(companyCode.equalsIgnoreCase(folderName)){
        		isPresent=true;
        		break;
        	}
        }
        if(!isPresent){
        	String fileUploadLocation = ApplicationUtil.getServerUploadLocation();
    		String fileDownloadLocation = ApplicationUtil.getServerDownloadLocation();
    		String directoryName = companyCode;
    		 File uploadDirectory = new File(fileUploadLocation+"\\"+directoryName);
    		 File downloadDirectory = new File(fileDownloadLocation+"\\"+directoryName);
    		    if (! uploadDirectory.exists()){
    		        if(uploadDirectory.mkdir()){
    		        	 downloadDirectory.mkdir();
    		        	 LOGGER.info("Company code directory created : "+  uploadDirectory.getPath());
    		        	 File siteDir = new File(fileUploadLocation+"\\"+directoryName+"\\site");
    		        	 if(siteDir.mkdir()){
    		        	 File licenseDir = new File(fileUploadLocation+"\\"+directoryName+"\\site\\license");
    		        	 licenseDir.mkdir();
    		        	 }
    		        	 File siteDir2 = new File(fileDownloadLocation+"\\"+directoryName+"\\site");
    		        	 siteDir2.mkdir();
    		        	 if(siteDir2.mkdir()){
        		        	 File licenseDir2 = new File(fileDownloadLocation+"\\"+directoryName+"\\site\\license");
        		        	 licenseDir2.mkdir();
        		        	 }
    		        	 File assetDirectory = new File(fileUploadLocation+"\\"+directoryName+"\\asset");
    		        	 assetDirectory.mkdir();
    		        	 File assetDirectory2 = new File(fileDownloadLocation+"\\"+directoryName+"\\asset");
    		        	 assetDirectory2.mkdir();
    		        	 File uploadIncidentDirectory = new File(fileUploadLocation+"\\"+directoryName+"\\incident");
    		        	 uploadIncidentDirectory.mkdir();
    		        	 File downloadIncDic = new File(fileDownloadLocation+"\\"+directoryName+"\\incident");
    		        	 downloadIncDic.mkdir();
    		        	 isPresent=true;
    		        }else{
    		        	LOGGER.info("Unable to create code directory");
    		        	isPresent=false;
    		        }
    		       
    		    }else{
    		    	LOGGER.info("Directory already exists");
    		    }
    		    
        }}
	   return isPresent;
	}


	private UserModel getUserDetails(String username, String type) {
		Tenant tenant = tenantService.getTenantDB(username, type);
		UserModel appUser=null;
		if(type.equalsIgnoreCase("USER")){
			UserDAO userDAO = new UserDAO(tenant.getDb_name());
			UserModel user = userDAO.getUserDetails(username, AppConstants.USER_ROLE_QUERY);
			appUser = user;
		}
		else if(type.equalsIgnoreCase("SP")){
			SPUserDAO spUserDAO = new SPUserDAO(tenant.getDb_name());
			UserModel regSPUser = spUserDAO.getUserDetails(username);
			appUser = regSPUser;
		}
		else if(type.equalsIgnoreCase("EXTSP")){
			SPUserDAO spUserDAO = new SPUserDAO(tenant.getDb_name());
			UserModel extSPUser = spUserDAO.getExtUserDetails(username);
			appUser = extSPUser;
		}
		appUser.setDbName(tenant.getDb_name());
		appUser.setTenantId(tenant.getTenant_id());
		return appUser;
	}



	private Properties getProperties() {
		Properties prop = new Properties();
		InputStream is = null;
		try {
		    is = this.getClass().getResourceAsStream("/system-config.properties");
		    prop.load(is);
		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		return prop;
	}

	

	/**
	 * Gets the authorities.
	 *
	 * @param userRoleList
	 *            the user role
	 * @return the authorities
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(final List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
			authorities.add(new SimpleGrantedAuthority(roles.get(0)));

		return authorities;
	}


}
