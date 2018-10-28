package com.pms.web.service.security;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private final String extraParam;
	public CustomAuthenticationToken(Object principal,
			Object credentials,
			String extraParam) {
		super(principal, credentials);
		this.extraParam= extraParam;
	}
	public CustomAuthenticationToken(Object principal,
			Object credentials,
			String extraParam,
			GrantedAuthority[] authorities) {

		super(principal, credentials, Arrays.asList(authorities));
		this.extraParam= extraParam;
	}
	public CustomAuthenticationToken(Object principal,
			Object credentials,
			String isLdap,
			Collection<? extends GrantedAuthority> authorities, String extraParam) {
		super(principal, credentials, authorities);
		this.extraParam = extraParam;
	}
	/**
	 * @return the extraParam
	 */
	public String getExtraParam() {
		return extraParam;
	}
}