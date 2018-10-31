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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.UserModel;


@Repository
public class SPUserDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(SPUserDAO.class);

	public SPUserDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}
	
	public UserModel getUserDetails(String username){
		final UserModel savedUser = new UserModel();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		return jdbcTemplate.query(AppConstants.SP_USER_ROLE_QUERY, new Object[]{username}, new ResultSetExtractor<UserModel>() {
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
                	savedUser.setCompanyId(rs.getLong("sp_cid"));
                	savedUser.setCompanyCode(rs.getString("sp_code"));
                	savedUser.setCompanyName(rs.getString("sp_cname"));
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

	public UserVO saveNewUser(AppUserVO appUserVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_NEW_USER_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	    	ps.setString(1, appUserVO.getFirstName());
            ps.setString(2, appUserVO.getLastName());
            ps.setString(3, appUserVO.getEmail());
            ps.setString(4, appUserVO.getEmail());
            ps.setLong(5,  Long.parseLong(appUserVO.getPhoneNo()));
            ps.setString(6, appUserVO.getGeneratedPassword());
            ps.setLong(7, user.getCompany().getCompanyId());
            if(appUserVO.getIsEnabled().equalsIgnoreCase("true")){
            	 ps.setInt(8, 1);
			}else{
				 ps.setInt(8, 0);
			}
           
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved  user {} with id {}.", appUserVO.getFirstName(), key.getKey());
	    appUserVO.setUserId(key.getKey().longValue());
	    UserVO userVO = new UserVO();
	    userVO.setPasswordGenerated(appUserVO.getGeneratedPassword());
	    userVO.setUserId(key.getKey().longValue());
	    userVO.setEmailId(appUserVO.getEmail());
	    userVO.setExists(false);
	    return userVO;
	}

	public Long saveUserRole(UserVO savedUserVO) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_NEW_USER_ROLE_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	    	ps.setLong(1, savedUserVO.getUserId());
            ps.setLong(2, savedUserVO.getRoleId());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved  user role {} with id {}.", savedUserVO.getUserId(), key.getKey());
	    Long roleId=key.getKey().longValue();
	    return roleId;
	}

	public int updateUserStatus(AppUserVO appUserVO) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.UPDATE_USER_STATUS);
	            ps.setInt(1, Integer.parseInt(appUserVO.getIsEnabled()));
	            ps.setLong(2, appUserVO.getUserId());
	            return ps;
		      }
		      });
		 	LOGGER.info("Update user status with  {}.", appUserVO.getIsEnabled());
		 	return updated;
	}

	public List<UserVO> getAllSPUsers(Long companyId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<UserVO> userList = jdbcTemplate.query(AppConstants.SP_USER_LIST_QUERY, new Object[] {companyId}, new ResultSetExtractor<List<UserVO>>(){
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
					userVO.setUserType("SP");
					userList.add(userVO);
				}
				return userList;
			}
		});
		return userList;
	}

	public  UserModel getExtUserDetails(String username) {
		final UserModel savedUser = new UserModel();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		return jdbcTemplate.query(AppConstants.EXT_SP_USER_DETAIL_QUERY, new Object[]{username}, new ResultSetExtractor<UserModel>() {
			@Override
			public UserModel extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
                	savedUser.setUserId(rs.getLong("sp_id"));
                	savedUser.setSpId(rs.getLong("sp_id"));
                	savedUser.setSpUsername(rs.getString("sp_username"));
                	savedUser.setEmailId(rs.getString("sp_username"));
                	savedUser.setPassword(rs.getString("access_key"));
                	savedUser.setFirstName("");
                	savedUser.setLastName(rs.getString("sp_name"));
                	savedUser.setSpName(rs.getString("sp_name"));
                	savedUser.setSysPassword("NO");
                	savedUser.setCompanyId(rs.getLong("customer_id"));
                	savedUser.setEnabled(1);
                	savedUser.setCompanyName(rs.getString("company_name"));
                	savedUser.setCompanyCode(rs.getString("company_code"));
                	savedUser.setUserType("EXTSP");
                	String role = "ROLE_SP_EXTERNAL";
                	savedUser.setRoleId(6l);
                	savedUser.getRoleNameList().add(role);
                }
                return savedUser;
			}
		});
	}
	
}
