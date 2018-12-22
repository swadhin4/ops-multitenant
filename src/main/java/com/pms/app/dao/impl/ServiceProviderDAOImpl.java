package com.pms.app.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
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
import com.pms.app.view.vo.ServiceProviderUserRoleVO;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.app.view.vo.UserVO;
import com.pms.web.util.ApplicationUtil;
import com.pms.web.util.QuickPasswordEncodingGenerator;
import com.pms.web.util.RestResponse;

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
	public int createServiceProviderUserAccess(List<CustomerVO> customerList , UserVO savedUserVO, LoginUser loginUser) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int[] insertedRows = jdbcTemplate.batchUpdate(AppConstants.INSERT_SERVICEPROVIDER_USER_ACCESS_QUERY,
				new BatchPreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						CustomerVO spUserAccessVO = customerList.get(i);
						ps.setLong(1, savedUserVO.getUserId());
						ps.setLong(2, spUserAccessVO.getCustomerId());
						ps.setString(3, loginUser.getUsername());
					}

					@Override
					public int getBatchSize() {
						return customerList.size();
					}
				});
		return insertedRows.length;
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

	/*@Override
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
	}*/

	@Override
	public int updateServiceProviderUserAccess(List<CustomerVO> customerList, Long selectedSPUserId, LoginUser loginUser) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRows=0;
		int insertedRows=0;
		for (CustomerVO spUserAccessVO : customerList) {
			if (spUserAccessVO.getAccessId()!=null  ) {
				updatedRows = jdbcTemplate.update(AppConstants.UPDATE_SERVICEPROVIDER_USER_ACCESS_QUERY,
						new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setLong(1, spUserAccessVO.getValue().equalsIgnoreCase("ONE")?1:0 );
								ps.setLong(2, spUserAccessVO.getCustomerId());
								ps.setLong(3, spUserAccessVO.getAccessId());
							}
						});

			} else if (spUserAccessVO.getAccessId() == null ) {
				insertedRows = jdbcTemplate.update(AppConstants.INSERT_SERVICEPROVIDER_USER_ACCESS_QUERY,
						new PreparedStatementSetter() {

							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setLong(1, selectedSPUserId);
								ps.setLong(2, spUserAccessVO.getCustomerId());
								ps.setString(3, loginUser.getUsername());
							}
						});
			}
		}
		return updatedRows+insertedRows ;
	
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
	public List<CustomerVO> getCustomersBySPID(String userId, String spCode) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerVO> customerVOs = jdbcTemplate.query(AppConstants.SP_ALL_CUSTOMERS_QUERY,
				new Object[] { userId, spCode }, new ResultSetExtractor<List<CustomerVO>>() {
					@Override
					public List<CustomerVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
						while (rs.next()) {
							CustomerVO customerVO = new CustomerVO();
							customerVO.setCustomerId(rs.getLong("sp_cust_id"));
							customerVO.setCustomerCode(rs.getString("customer_code"));
							customerVO.setCustomerName(rs.getString("customer_name"));
							customerVO.setCountryName(rs.getString("country_name"));
							customerVO.setSelected(false);
							customerVOList.add(customerVO);
						}
						return customerVOList;
					}
				});
		return customerVOs == null?Collections.emptyList():customerVOs;
	}
	
	@Override
	public List<CustomerVO> getCustomersBySelectedSPUser(String spId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerVO> customerVOs = jdbcTemplate.query(AppConstants.SERVICEPROVIDER_USERS_CUSTOMERS_QUERY,
				new Object[] { spId }, new ResultSetExtractor<List<CustomerVO>>() {
					@Override
					public List<CustomerVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
						while (rs.next()) {
							CustomerVO customerVO = new CustomerVO();
							customerVO.setCustomerId(rs.getLong("sp_cust_id"));
							customerVO.setCustomerCode(rs.getString("customer_code"));
							customerVO.setCustomerName(rs.getString("customer_name"));
							customerVO.setCountryName(rs.getString("country_name"));
							if(StringUtils.isNotBlank(rs.getString("del_flag")) && rs.getInt("del_flag")==0){
								customerVO.setSelected(true);
								customerVO.setAccessId(rs.getLong("access_id"));
								customerVO.setDelFlagEnabled(true); 
							}else if(StringUtils.isNotBlank(rs.getString("del_flag")) && rs.getInt("del_flag")==1){
								customerVO.setAccessId(rs.getLong("access_id"));
								customerVO.setSelected(false);
								customerVO.setDelFlagEnabled(false); 
							}else {
								customerVO.setSelected(false);
								customerVO.setDelFlagEnabled(false); 
							}
							customerVOList.add(customerVO);
						}
						return customerVOList;
					}
				});
		return customerVOs == null?Collections.emptyList():customerVOs;
	}
	@Override
	public List<CustomerVO> getCustomerCountryForloggedInUser(Long loggedUserId, String spCode) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerVO> customerVOs = jdbcTemplate.query(AppConstants.LOGGEDUSER_CUSTOMER_COUNTRY_QUERY,
				new Object[] { loggedUserId }, new ResultSetExtractor<List<CustomerVO>>() {
					@Override
					public List<CustomerVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<CustomerVO> customerVOList = new ArrayList<CustomerVO>();
						while (rs.next()) {
							CustomerVO customerVO = new CustomerVO();
							customerVO.setUserId(loggedUserId);
							customerVO.setCustomerId(rs.getLong("sp_cust_id"));
							customerVO.setCustomerCode(rs.getString("customer_code"));
							customerVO.setCustomerName(rs.getString("customer_name"));
							customerVO.setCountryId(rs.getLong("cust_country_id"));
							customerVO.setCountryName(rs.getString("country_name"));
							customerVO.setCustDBName(rs.getString("cust_db_name"));
							customerVO.setSpCode(spCode);
							customerVOList.add(customerVO);
						}
						return customerVOList;
					}
				});
		return customerVOs == null ? Collections.emptyList() : customerVOs;
	}


	@Override
	public List<String> getCustomerDBServiceProviderCode(String custcode) throws Exception {
		List<String> custdtls = new ArrayList<String>();
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Map<String, Object>> rows = jdbcTemplate
				.queryForList(AppConstants.SERVICEPROVIDER_CUSTOMERDB_BY_CUSTOMERCODE_QUERY, new Object[] { custcode });
		if (rows != null && !rows.isEmpty()) {
			for (Map row : rows) {
				custdtls.add((String) row.get("cust_db_name"));
				custdtls.add((String) row.get("sp_code"));
			}
		}
		return custdtls;
	}


	@Override
	public List<TicketVO> getCustomerTicketsBySPcode(String spcode, Long spUserId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketVOs = jdbcTemplate.query(AppConstants.CUSTOMER_TICKETS_BY_SERVICEPROVIDERCODE_QUERY,
				new Object[] { spcode  }, new ResultSetExtractor<List<TicketVO>>() {
					@Override
					public List<TicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<TicketVO> ticketVOList = new ArrayList<TicketVO>();
						while (rs.next()) {
							TicketVO ticketVO = new TicketVO();
							ticketVO.setTicketNumber(rs.getString("ticket_number"));
							ticketVO.setTicketTitle(rs.getString("ticket_title"));
							ticketVO.setSiteId(rs.getLong("site_id"));
							ticketVO.setSiteName(rs.getString("site_name"));
							ticketVO.setAssetId(rs.getLong("asset_id"));
							ticketVO.setAssetName(rs.getString("asset_name"));
							ticketVO.setRaisedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
							ticketVO.setSla(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate")));
							ticketVO.setAssignedSP(rs.getString("sp_name"));
							ticketVO.setStatus(rs.getString("status"));

							ticketVOList.add(ticketVO);
						}
						return ticketVOList;
					}
				});
		return ticketVOs == null ? Collections.emptyList() : ticketVOs;
	}

	public int resetPassword(String encodePassword, Long extSPId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		RestResponse response = new RestResponse();
		int updatedRows = jdbcTemplate.update(AppConstants.EXT_SP_PASSWORD_RESET_QUERY,
				new PreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1, encodePassword);
						ps.setLong(2, extSPId);
					}
				});
		return  updatedRows;
	}

	public ServiceProviderVO findSPPasswordDetails(Long spId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderVO spProviderVO= new ServiceProviderVO();
		jdbcTemplate.query(AppConstants.EXT_SERVICE_PROVIDER_INFO, new Object[]{ spId }, 
			new ResultSetExtractor<ServiceProviderVO>(){
		@Override
		public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
			if(rs.next()){
				spProviderVO.setServiceProviderId(rs.getLong("sp_id"));
				spProviderVO.setName(rs.getString("sp_name"));
				spProviderVO.setCode(rs.getString("sp_code"));
				spProviderVO.setEmail(rs.getString("sp_email"));
				spProviderVO.setHelpDeskEmail(rs.getString("help_desk_email"));
				spProviderVO.setAccessKey(rs.getString("accessKey"));
			}
			return spProviderVO;
		}
	 });
	return spProviderVO;
	}

	
}
