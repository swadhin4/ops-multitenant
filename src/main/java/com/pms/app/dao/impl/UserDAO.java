package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.UserModel;


@Repository
public class UserDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(UserDAO.class);

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
                	savedUser.setCompanyCode(rs.getString("company_code"));
                	savedUser.setCompanyName(rs.getString("company_name"));
                	savedUser.setRoleNameList(roleList);
                }
                return savedUser;
			}
		});
	}

	public List<RoleStatus> getRoleStatus(Long roleId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<RoleStatus> roleStatusList = jdbcTemplate.query(AppConstants.USER_ROLE_STATUS_MAPPING, new Object[] {roleId}, new ResultSetExtractor<List<RoleStatus>>(){
			List<RoleStatus> roleStatusMap = new ArrayList<RoleStatus>();
			@Override
			public List<RoleStatus> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					RoleStatus roleStatus = new RoleStatus();
					roleStatus.setId(rs.getLong("role_status_map_id"));
					roleStatus.setRoleId(rs.getLong("role_id"));
					roleStatus.setStatusId(rs.getLong("status_id"));
					roleStatusMap.add(roleStatus);
				}
				return roleStatusMap;
			}
		});
		return roleStatusList;
	}

	public List<UserVO> getAllUsers(Long companyId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<UserVO> userList = jdbcTemplate.query(AppConstants.USER_LIST_QUERY, new Object[] {companyId}, new ResultSetExtractor<List<UserVO>>(){
			List<UserVO> userList = new ArrayList<UserVO>();
			@Override
			public List<UserVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					UserVO userVO = new UserVO();
					userVO.setUserId(rs.getLong("user_id"));
					userVO.setFirstName(rs.getString("first_name"));
					userVO.setLastName(rs.getString("last_name"));
					userVO.setEmailId(rs.getString("email_id"));
					userVO.setPhoneNo(rs.getString("phone"));
					userVO.setRoleId(rs.getLong("role_id"));
					userVO.setRoleName(rs.getString("description"));
					userVO.setEnabled(rs.getInt("enabled"));
					userVO.setCompanyName(rs.getString("company_name"));
					userList.add(userVO);
				}
				return userList;
			}
		});
		return userList;
	}

	public List<Role> getAllRoles() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Role> roleList = jdbcTemplate.query(AppConstants.USER_ROLE_LIST_QUERY, new ResultSetExtractor<List<Role>>(){
			List<Role> roleList = new ArrayList<Role>();
			@Override
			public List<Role> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					Role role = new Role();
					role.setRoleId(rs.getLong("role_id"));
					role.setDescription(rs.getString("role_desc"));
					roleList.add(role);
				}
				return roleList;
			}
		});
		return roleList;
	}

	public UserVO updateRole(UserVO userVO) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		 jdbcTemplate.update(new PreparedStatementCreator() {
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.UPDATE_USER_ROLE);
	            ps.setLong(1, userVO.getRoleSelected().getRoleId());
	            ps.setLong(2, userVO.getUserId());
	            return ps;
		      }
		      });
		 	LOGGER.info("Update user {} with role id  {}.", userVO.getUserName(),  userVO.getRoleId());
		    return userVO;   
	}
	
}
