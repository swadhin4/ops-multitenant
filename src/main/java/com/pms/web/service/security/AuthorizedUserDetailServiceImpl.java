/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.service.security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

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

import com.pms.app.dao.impl.SPUserDAO;
import com.pms.app.dao.impl.UserDAO;
import com.pms.jpa.entities.Tenant;
import com.pms.jpa.entities.UserModel;
import com.pms.web.service.TenantService;

/**
 * The Class CustomUserDetailServiceImpl.
 *
 */
@Service("userAuthorizationService")
@Transactional(readOnly = true)
public class AuthorizedUserDetailServiceImpl implements  UserDetailsService {

	@Autowired
	private TenantService tenantService;
	
	 @Autowired
	 private HttpServletRequest request;
	 
	
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		try {
		     request  = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		    String userType= request.getParameter("usertype");
		    System.out.println(userType);
		    UserModel user=null;
		    if(userType.equals("1")){
			 user = getUserDetails(username, "USER");
			
		    }else{
		    	user = getUserDetails(username, "SP");
		    }
			if (user == null) {
				throw new UsernameNotFoundException("Username not found");
			} else {
				  if(userType.equals("1")){
					user.setUserType("USER");
				    }else{
				    user.setUserType("SP");
				   }
				boolean accountNonExpired = true;
				boolean credentialsNonExpired = true;
				boolean accountNonLocked = true;
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



	private UserModel getUserDetails(String username, String type) {
		Tenant tenant = tenantService.getTenantDB(username, type);
		UserModel appUser=null;
		if(type.equalsIgnoreCase("USER")){
			UserDAO userDAO = new UserDAO(tenant.getDb_name());
			UserModel user = userDAO.getUserDetails(username);
			appUser = user;
		}
		else if(type.equalsIgnoreCase("SP")){
			SPUserDAO spUserDAO = new SPUserDAO(tenant.getDb_name());
			UserModel user = spUserDAO.getUserDetails(username);
			appUser = user;
		}
		appUser.setDbName(tenant.getDb_name());
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
