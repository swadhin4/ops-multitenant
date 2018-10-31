package com.pms.app.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.dao.ServiceProviderDAO;
import com.pms.app.view.vo.CustomerVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SPUserVo;
import com.pms.app.view.vo.ServiceProviderUserAccessVO;
import com.pms.app.view.vo.ServiceProviderUserRoleVO;
import com.pms.app.view.vo.UserVO;
import com.pms.web.util.QuickPasswordEncodingGenerator;

public class ServiceProviderDAOImpl implements ServiceProviderDAO {
	public ServiceProviderDAOImpl(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	@Override
	public String insertServiceProviderUser(SPUserVo sPUserVo) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Integer insertedRows = jdbcTemplate.update(AppConstants.INSERT_SERVICEPROVIDER_USER_QUERY,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, sPUserVo.getSpId());
						ps.setString(2, sPUserVo.getFirstName());
						ps.setString(3, sPUserVo.getLastName());
						ps.setString(4, sPUserVo.getUserEmail());
						ps.setString(5, QuickPasswordEncodingGenerator.encodePassword(sPUserVo.getUserPassword()));
						ps.setString(6, "NO");
						ps.setLong(7, sPUserVo.getUserPhone());
						ps.setLong(8, sPUserVo.getEnabled());
						ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
						ps.setString(10, sPUserVo.getCreatedBy());

					}
				});

		return insertedRows.toString();
	}

	@Override
	public String updatetServiceProviderUser(SPUserVo sPUserVo) throws Exception {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Integer insertedRows = jdbcTemplate.update(AppConstants.UPDATE_SERVICEPROVIDER_USER_QUERY,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, sPUserVo.getSpId());
						ps.setString(2, sPUserVo.getFirstName());
						ps.setString(3, sPUserVo.getLastName());
						ps.setString(4, sPUserVo.getUserEmail());
						ps.setString(5, QuickPasswordEncodingGenerator.encodePassword(sPUserVo.getUserPassword()));
						ps.setLong(6, sPUserVo.getUserPhone());
						ps.setLong(7, sPUserVo.getEnabled());
						ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
						ps.setString(9, sPUserVo.getModifiedBy());
						ps.setLong(10, sPUserVo.getUserId());

					}
				});
		return insertedRows.toString();
	}

	@Override
	public SPUserVo getServiceProviderUserByEmail(SPUserVo sPUserVo) throws Exception {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SPUserVo spUserVo = jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERBY_USERNAME_QUERY,
				new Object[] { sPUserVo.getUserEmail() }, new ResultSetExtractor<SPUserVo>() {
					@Override
					public SPUserVo extractData(ResultSet rs) throws SQLException, DataAccessException {
						SPUserVo spuserVo = new SPUserVo();
						while (rs.next()) {
							spuserVo.setUserId(rs.getInt("user_id"));
							spuserVo.setSpId(rs.getInt("sp_id"));
							spuserVo.setFirstName(rs.getString("first_name"));
							spuserVo.setLastName(rs.getString("last_name"));
							spuserVo.setUserEmail(rs.getString("user_email"));
							spuserVo.setUserPhone(rs.getLong("user_phone"));
							spuserVo.setEnabled(rs.getByte("enabled"));
							spuserVo.setCreatedBy(rs.getString("created_by"));
							spuserVo.setCreatedDate(rs.getDate("created_date"));
							spuserVo.setModifiedBy(rs.getString("modified_by"));
							spuserVo.setModifiedDate(rs.getDate("modified_date"));
						}
						return spuserVo;
					}
				});
		return spUserVo;
	}

	@Override
	public String createServiceProviderUserRole(SPUserVo sPUserVo, LoginUser loginUser) throws Exception {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Integer insertedRows = jdbcTemplate.update(AppConstants.INSERT_SERVICEPROVIDER_USER_ROLE_QUERY,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, sPUserVo.getUserId());
						ps.setString(2, sPUserVo.getRoleId());
						ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						ps.setString(4, loginUser.getUsername());

					}
				});
		return insertedRows.toString();
	}

	@Override
	public String createServiceProviderUserAccess(SPUserVo useraccessvo, LoginUser loginUser) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int[] insertedRows = jdbcTemplate.batchUpdate(AppConstants.INSERT_SERVICEPROVIDER_USER_ACCESS_QUERY,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ServiceProviderUserAccessVO serviceProviderUserAccessVO = useraccessvo.getCustomers().get(i);
						ps.setInt(1, useraccessvo.getUserId());
						ps.setInt(2, serviceProviderUserAccessVO.getSpCustId());
						ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						ps.setString(4, loginUser.getUsername());
					}

					@Override
					public int getBatchSize() {
						return useraccessvo.getCustomers().size();
					}
				});
		return insertedRows.toString();
	}

	@Override
	public String updateServiceProviderUserRole(SPUserVo sPUserVo, LoginUser loginUser) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Integer insertedRows = jdbcTemplate.update(AppConstants.UPDATE_SERVICEPROVIDER_USER_ROLE_QUERY,
				new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, sPUserVo.getRoleId());
						ps.setString(2, loginUser.getUsername());
						ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						ps.setInt(4, sPUserVo.getUserId());

					}
				});
		return insertedRows.toString();
	}

	@Override
	public String updateServiceProviderUserAccess(SPUserVo useraccessvo, LoginUser loginUser) throws Exception {
		// TODO Auto-generated method stub
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		for (ServiceProviderUserAccessVO serviceProviderUserAccessVO : useraccessvo.getCustomers()) {
			if (serviceProviderUserAccessVO.getAccessId() != 0 && serviceProviderUserAccessVO.isDeleted() == true) {
				jdbcTemplate.update(AppConstants.DELETE_SERVICEPROVIDER_USER_ACCESS_QUERY,
						new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1, serviceProviderUserAccessVO.getSpCustId());
							}
						});

			} else if (serviceProviderUserAccessVO.getAccessId() == 0) {
				jdbcTemplate.update(AppConstants.INSERT_SERVICEPROVIDER_USER_ACCESS_QUERY,
						new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(1, useraccessvo.getUserId());
								ps.setInt(2, serviceProviderUserAccessVO.getSpCustId());
								ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
								ps.setString(4, loginUser.getUsername());
							}
						});
			}
		}
		return "success";
	}

/*	@Override
	public List<SPUserVo> getAllUsers() throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SPUserVo> sPUserVos =  jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERS_QUERY, new Object[]{}, new ResultSetExtractor<List<SPUserVo>>() {
			@Override
			public List<SPUserVo> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<SPUserVo> sPUserVoList = new ArrayList<SPUserVo>();
				while (rs.next()) {
					SPUserVo spuserVo = new SPUserVo();
					spuserVo.setUserId(rs.getInt("user_id"));
					spuserVo.setSpId(rs.getInt("sp_id"));
					spuserVo.setFirstName(rs.getString("first_name"));
					spuserVo.setLastName(rs.getString("last_name"));
					spuserVo.setUserEmail(rs.getString("email_id"));
					spuserVo.setUserPhone(rs.getLong("phone"));
					spuserVo.setEnabled(rs.getByte("enabled"));
					spuserVo.setCreatedBy(rs.getString("created_by"));
					spuserVo.setCreatedDate(rs.getDate("created_date"));
					spuserVo.setModifiedBy(rs.getString("modified_by"));
					spuserVo.setModifiedDate(rs.getDate("modified_date"));
					
					sPUserVoList.add(spuserVo);
                }
				return sPUserVoList;
			}
		});
		return sPUserVos;
	}*/
	@Override
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

	@Override
	public ServiceProviderUserRoleVO getUserRoleByUserID(String userId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderUserRoleVO serviceProviderUserRoleVO = jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERS_ROLE_QUERY,
				new Object[] { userId }, new ResultSetExtractor<ServiceProviderUserRoleVO>() {
					@Override
					public ServiceProviderUserRoleVO extractData(ResultSet rs) throws SQLException, DataAccessException {
						ServiceProviderUserRoleVO serviceProviderUserRoleVO = new ServiceProviderUserRoleVO();
						while (rs.next()) {
							serviceProviderUserRoleVO.setUserAccessId(rs.getInt("user_role_id"));
							serviceProviderUserRoleVO.setUserId(rs.getInt("user_id"));
							serviceProviderUserRoleVO.setUserRoleId(rs.getInt("role_id"));
							serviceProviderUserRoleVO.setCreatedDate(rs.getDate("created_date"));
							serviceProviderUserRoleVO.setCreatedBy(rs.getString("created_by"));
							serviceProviderUserRoleVO.setModifiedBy(rs.getString("modified_by"));
							serviceProviderUserRoleVO.setModifiedDate(rs.getDate("modified_date"));
						}
						return serviceProviderUserRoleVO;
					}
				});
		return serviceProviderUserRoleVO;
	}

	/*@Override
	public List<ServiceProviderUserAccessVO> getCustomersByUserID(String userId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<ServiceProviderUserAccessVO> serviceProviderUserAccessVOs =  jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERS_CUSTOMERS_QUERY, new Object[]{userId}, new ResultSetExtractor<List<ServiceProviderUserAccessVO>>() {
			@Override
			public List<ServiceProviderUserAccessVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<ServiceProviderUserAccessVO> serviceProviderUserAccessVOList = new ArrayList<ServiceProviderUserAccessVO>();
				while (rs.next()) {
					ServiceProviderUserAccessVO serviceProviderUserAccessVO = new ServiceProviderUserAccessVO();
					serviceProviderUserAccessVO.setAccessId(rs.getInt("access_id"));
					serviceProviderUserAccessVO.setUserId(rs.getInt("user_id"));
					serviceProviderUserAccessVO.setSpCustId(rs.getInt("sp_cust_id"));
					serviceProviderUserAccessVO.setCreatedOn(rs.getDate("created_on"));
					serviceProviderUserAccessVO.setCreatedBy(rs.getString("created_by"));
					
					serviceProviderUserAccessVOList.add(serviceProviderUserAccessVO);
                }
				return serviceProviderUserAccessVOList;
			}
		});
		return serviceProviderUserAccessVOs;
	}*/
	
	@Override
	public List<CustomerVO> getCustomersByUserID(String userId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerVO> customerVOs = jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERS_CUSTOMERS_QUERY,
				new Object[] { userId }, new ResultSetExtractor<List<CustomerVO>>() {
					@Override
					public List<CustomerVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
						while (rs.next()) {
							CustomerVO customerVO = new CustomerVO();
							customerVO.setCustomerCode(rs.getString("customer_code"));
							customerVO.setCustomerName(rs.getString("customer_name"));
							customerVO.setCustomerName(rs.getString("country_name"));
							if (rs.getInt("del_flag") == 1) {
								customerVO.setSelected(true);
							} else {
								customerVO.setSelected(false);
							}

							customerVOList.add(customerVO);
						}
						return customerVOList;
					}
				});
		return customerVOs;
	}

}
