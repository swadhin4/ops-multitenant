package com.pms.web.service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pms.app.config.ConnectionManager;
import com.pms.jpa.entities.Tenant;
import com.pms.web.service.TenantService;

public class TenantServiceImpl implements TenantService {

	@Override
	public List<Tenant> getAllTenants() {
		ConnectionManager connectionManager = new ConnectionManager();
		DataSource  dataSource = ConnectionManager.getTenantDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Tenant> tenantList = jdbcTemplate.query("select * from tenants", new BeanPropertyRowMapper(Tenant.class));
		System.out.println("Totala Tenants : " + tenantList.size());
		try {
			connectionManager.getTenantConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tenantList == null ? Collections.EMPTY_LIST:tenantList;
	}
	
	@Override
	public Tenant getTenantDB(String username) {
		ConnectionManager connectionManager = new ConnectionManager();
		DataSource  dataSource = ConnectionManager.getTenantDataSource();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		Tenant tenant = (Tenant) jdbcTemplate.queryForObject("select * from tenants where user_email='"+username+"'", new BeanPropertyRowMapper(Tenant.class));
		System.out.println("Totala Tenants : " + tenant.getDb_name());
		try {
			connectionManager.getTenantConnection().close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tenant;
	}

}
