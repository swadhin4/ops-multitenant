package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.ServiceProviderVO;

public class TenantsDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(TenantsDAO.class);
	public TenantsDAO(String tenantDB) {
		ConnectionManager.getInstance(tenantDB);
	}

	public boolean insertExtSPDetails(ServiceProviderVO serviceProviderVO, String dbName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			 @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_EXTSP_TENANT, 
		            Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, serviceProviderVO.getSpUserName());
				ps.setString(2, serviceProviderVO.getEmail());
				ps.setString(3, dbName);
				return ps;		
			}
		}, key);
		 	LOGGER.info("Saved EXTSP {} with id {}.", serviceProviderVO.getSpUserName(), key.getKey());
		 	if(key.getKey().intValue()>0){
		 		return true;
		 	}else{
		 		return false;
		 	}
	}
	public boolean insertRegisteredSPDetails(int spTenantId, String registerdSPEmail) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			 @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_REGSP_TENANT, 
		            Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, spTenantId);
				ps.setString(2, registerdSPEmail);
				return ps;		
			}
		}, key);
		 	LOGGER.info("Saved Registered SP {} with id {}.", registerdSPEmail, key.getKey());
		 	if(key.getKey().intValue()>0){
		 		return true;
		 	}else{
		 		return false;
		 	}
	}
	
	
	public boolean insertCustomerDetails(int customerTenantId, String companyCode, String customerEmail) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			 @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_CUSTOMER_TENANT_MAPPING, 
		            Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, customerTenantId);
				ps.setString(2, companyCode);
				ps.setString(3, customerEmail);
				return ps;		
			}
		}, key);
		 	LOGGER.info("Saved Customer {} with id {}.", customerEmail, key.getKey());
		 	if(key.getKey().intValue()>0){
		 		return true;
		 	}else{
		 		return false;
		 	}
	}
}
