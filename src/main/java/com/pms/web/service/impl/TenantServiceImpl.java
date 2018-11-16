package com.pms.web.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.jpa.entities.Tenant;
import com.pms.web.service.TenantService;

@Service("tenantService")
public class TenantServiceImpl implements TenantService {

	public TenantServiceImpl() {
		super();
	}
	
	
	@Override
	public List<Tenant> getAllTenants() {
		ConnectionManager connectionManager = new ConnectionManager();
		DataSource  dataSource = ConnectionManager.getTenantDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Tenant> tenantList = jdbcTemplate.query("select * from tenants", new BeanPropertyRowMapper(Tenant.class));
		System.out.println("Total Tenants : " + tenantList.size());
		try {
			connectionManager.getTenantConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tenantList == null ? Collections.EMPTY_LIST:tenantList;
	}
	
	@Override
	public Tenant getTenantDB(String username, String type) {
		ConnectionManager connectionManager = new ConnectionManager();
		DataSource  dataSource = ConnectionManager.getTenantDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Tenant tenant =null;
		if(type.equalsIgnoreCase("SP")){
			 tenant = (Tenant) jdbcTemplate.queryForObject(AppConstants.SP_USER_TENANT, new Object[]{username}, new BeanPropertyRowMapper(Tenant.class));
		}else if(type.equalsIgnoreCase("USER")){
			 tenant = (Tenant) jdbcTemplate.queryForObject(AppConstants.USER_TENANT,new Object[]{username}, new BeanPropertyRowMapper(Tenant.class));
		}
		else if(type.equalsIgnoreCase("EXTSP")){
			 tenant = (Tenant) jdbcTemplate.queryForObject(AppConstants.EXT_SP_USER_TENANT,new Object[]{username}, new BeanPropertyRowMapper(Tenant.class));
		}
		try {
			connectionManager.getTenantConnection().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tenant;
	}
}
