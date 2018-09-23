package com.pms.web.service;

import java.util.List;

import com.pms.jpa.entities.Tenant;


public interface TenantService {

	public List<Tenant> getAllTenants();

	public Tenant getTenantDB(String username);
}
