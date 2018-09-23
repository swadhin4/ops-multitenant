/*
 * Copyright (C) 2013 , Inc. All rights reserved
 */
package com.pms.web.service.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.jpa.entities.User;
import com.pms.jpa.entities.UserRole;
import com.pms.web.service.UserService;

/**
 * The Class CustomUserDetailServiceImpl.
 *
 */
@Service("customUserDetailService")
@Transactional(readOnly = true)
public class CustomUserDetailServiceImpl implements UserDetailsService {

	/** The user service. */
	@Autowired
	private UserService userService;

	/**
	 * Load user by username.
	 *
	 * @param username
	 *          the username
	 * @return the user details
	 * @throws UsernameNotFoundException
	 *           the username not found exception
	 * @see org.springframework.security.core.userdetails.UserDetailsService#
	 *      loadUserByUsername(java.lang.String)
	 */
	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

		try {
			User user = userService.findByUserName(username);
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			CustomUserDetails customUserDetails =
					new CustomUserDetails(user.getEmailId(), user.getPassword(), true, accountNonExpired,
							credentialsNonExpired, accountNonLocked, getAuthorities(user.getUserRoles()));
			return customUserDetails;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * Gets the authorities.
	 *
	 * @param userRoleList
	 *          the user role
	 * @return the authorities
	 */
	private Collection<? extends GrantedAuthority> getAuthorities(final List<UserRole> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for(UserRole role:roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole().getRoleName()));
		}

		return authorities;
	}

}
