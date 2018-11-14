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

}
