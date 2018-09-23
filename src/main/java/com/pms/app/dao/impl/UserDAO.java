package com.pms.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.jpa.entities.UserModel;


@Repository
public class UserDAO {

	public UserDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}
	
	public UserModel getUserDetails(String username){
		final UserModel savedUser = new UserModel();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		return jdbcTemplate.query(AppConstants.USER_ROLE_QUERY, new Object[]{username}, new ResultSetExtractor<UserModel>() {
			@Override
			public UserModel extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<String> roleList = new ArrayList<String>();
				if (rs.next()) {
                	savedUser.setUserId(rs.getLong("user_id"));
                	savedUser.setFirstName(rs.getString("first_name"));
                	savedUser.setLastName(rs.getString("last_name"));
                	savedUser.setEmailId(rs.getString("email_id"));
                	savedUser.setPassword(rs.getString("password"));
                	savedUser.setEnabled(rs.getInt("enabled"));
                	savedUser.setRoleId(rs.getLong("role_id"));
                	roleList.add(rs.getString("role_name"));
                	savedUser.setSysPassword(rs.getString("sys_password"));
                	savedUser.setCompanyId(rs.getLong("company_id"));
                	savedUser.setCompanyName(rs.getString("company_name"));
                	savedUser.setRoleNameList(roleList);
                }
                return savedUser;
			}
		});
	}
	
}
