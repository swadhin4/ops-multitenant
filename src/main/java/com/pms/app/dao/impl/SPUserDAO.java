package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.constants.RSPCustomerConstants;
import com.pms.app.constants.TicketPriorityEnum;
import com.pms.app.view.vo.AppUserVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SLADetailsVO;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.UserVO;
import com.pms.jpa.entities.Role;
import com.pms.jpa.entities.RoleStatus;
import com.pms.jpa.entities.TicketPriority;
import com.pms.jpa.entities.UserModel;
import com.pms.web.util.QuickPasswordEncodingGenerator;


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

	public UserVO saveNewSPUser(AppUserVO appUserVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_SERVICEPROVIDER_USER_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, user.getCompany().getCompanyId());
	    	ps.setString(2, appUserVO.getFirstName());
            ps.setString(3, appUserVO.getLastName());
            ps.setString(4, appUserVO.getEmail());
            ps.setString(5, appUserVO.getGeneratedPassword());
            ps.setLong(6,  Long.parseLong(appUserVO.getPhoneNo()));
            if(appUserVO.getIsEnabled().equalsIgnoreCase("true")){
            	 ps.setInt(7, 1);
			}else{
				 ps.setInt(7, 0);
			}
            ps.setString(8, user.getEmail());
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
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_SP_NEW_USER_ROLE_QUERY, 
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

	public ServiceProviderVO saveServiceProvider(ServiceProviderVO serviceProviderVO,LoginUser user ) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator(){
			 @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.EXT_SP_CREATE_QUERY, 
		            Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, serviceProviderVO.getSpUserName());
				ps.setString(2, serviceProviderVO.getName());
				ps.setString(3,serviceProviderVO.getCode());
				ps.setLong(4,serviceProviderVO.getCountry().getCountryId());
				ps.setString(5,serviceProviderVO.getAdditionalDetails());
				ps.setString(6, serviceProviderVO.getEmail());
				ps.setLong(7, user.getCompany().getCompanyId());
				ps.setString(8, user.getUsername());
				if(StringUtils.isNotBlank(serviceProviderVO.getHelpDeskNumber())){
					ps.setLong(9,Long.parseLong(serviceProviderVO.getHelpDeskNumber()));
				}else{
					ps.setString(9,serviceProviderVO.getHelpDeskNumber());
				}
				ps.setString(10, serviceProviderVO.getHelpDeskEmail());
				ps.setString(11, QuickPasswordEncodingGenerator.encodePassword(serviceProviderVO.getAccessKey()));
				ps.setString(12, serviceProviderVO.getSlaDescription());
				return ps;		
			}
		}, key);
		 	LOGGER.info("Saved SP {} with id {}.", serviceProviderVO.getName(), key.getKey());
		 	serviceProviderVO.setServiceProviderId(key.getKey().longValue());
		 	return serviceProviderVO;
		}
	public ServiceProviderVO getUniqueServiceProvider(ServiceProviderVO serviceProviderVO,LoginUser user ) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderVO uniqueSP = jdbcTemplate.query(AppConstants.EXT_SP_USER_DETAIL_QUERY, new Object[]{serviceProviderVO.getEmail()}, new ResultSetExtractor<ServiceProviderVO>() {
			@Override
			public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				ServiceProviderVO sp= new ServiceProviderVO();
				if (rs.next()) {
					sp.setServiceProviderId(rs.getLong("sp_id"));
					sp.setName(rs.getString("sp_name"));
					sp.setCode(rs.getString("sp_code"));
                }
			 return sp;
			}
		});
		return uniqueSP;
	}
	
	public int updateServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
			
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.EXT_SP_UPDATE_QUERY);
		        ps.setString(1, serviceProviderVO.getName());
				ps.setString(2,serviceProviderVO.getCode());
				ps.setLong(3,serviceProviderVO.getCountry().getCountryId());
				ps.setString(4,serviceProviderVO.getAdditionalDetails());
				ps.setString(5, serviceProviderVO.getEmail());
				ps.setLong(6, user.getCompany().getCompanyId());
				ps.setString(7,user.getUsername());
				ps.setString(8,serviceProviderVO.getHelpDeskNumber());
				ps.setString(9, serviceProviderVO.getHelpDeskEmail());
				ps.setString(10, serviceProviderVO.getSlaDescription());
				ps.setLong(11, serviceProviderVO.getServiceProviderId());
	            return ps;
		        }
		      });
		 	LOGGER.info("Update External SP status with  {}.",  serviceProviderVO.getName());
		 	return updated;
	}
	
	public int updateRegisteredServiceProvider(ServiceProviderVO serviceProviderVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
			
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.RSP_UPDATE_QUERY);
				ps.setString(1,serviceProviderVO.getAdditionalDetails());
				ps.setString(2,user.getUsername());
				ps.setString(3,serviceProviderVO.getHelpDeskNumber());
				ps.setString(4, serviceProviderVO.getHelpDeskEmail());
				ps.setString(5, serviceProviderVO.getSlaDescription());
				ps.setLong(6, serviceProviderVO.getServiceProviderId());
	            return ps;
		        }
		      });
		 	LOGGER.info("Update Registered SP status with  {}.",  serviceProviderVO.getName());
		 	return updated;
	}

	

	
	public int saveEscalalationLevels(ServiceProviderVO savedSP, List<EscalationLevelVO> escalationLevelList, LoginUser loginUser, final String spEscalationInsertQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int[] updatedRows = jdbcTemplate.batchUpdate(spEscalationInsertQuery,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	EscalationLevelVO escalation = escalationLevelList.get(i);
                        ps.setLong(1, savedSP.getServiceProviderId());
                        ps.setLong(2, Long.parseLong(escalation.getEscalationLevel()));
                        ps.setString(3, escalation.getEscalationPerson());
                        ps.setString(4, escalation.getEscalationEmail());
                        ps.setString(5, loginUser.getUsername());
                    }

                    @Override
                    public int getBatchSize() {
                        return escalationLevelList.size();
                    }
                });
        return updatedRows.length;
		
	}
	public int updateEscalalationLevels(ServiceProviderVO savedSP, List<EscalationLevelVO> escalationLevelList,
			LoginUser loginUser, final String spEscalationQuery) {
		List<EscalationLevelVO> escListWithIds = new ArrayList<EscalationLevelVO>();
		List<EscalationLevelVO> escListWithOutIds = new ArrayList<EscalationLevelVO>();
		 int insertedEscList = 0;
		for(EscalationLevelVO escObj  : escalationLevelList){
			if(escObj.getEscId()!=null){
				escListWithIds.add(escObj);
			}else{
				escListWithOutIds.add(escObj);
			}
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate(spEscalationQuery ,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	EscalationLevelVO escalation = escListWithIds.get(i);
                        ps.setString(1, escalation.getEscalationPerson());
                        ps.setString(2, escalation.getEscalationEmail());
                        ps.setLong(3, escalation.getEscId());
                        ps.setLong(4, savedSP.getServiceProviderId());
                    }

                    @Override
                    public int getBatchSize() {
                        return escListWithIds.size();
                    }
                });
          if(escListWithOutIds.size()>0 && StringUtils.isEmpty(savedSP.getSpDbName())){
        	  insertedEscList = saveEscalalationLevels(savedSP, escListWithOutIds, loginUser, AppConstants.INSERT_SP_ESCALATIONS_QUERY);
          }
          else{
        	  insertedEscList = saveEscalalationLevels(savedSP, escListWithOutIds, loginUser, AppConstants.INSERT_RSP_ESCALATIONS_QUERY);
          }
        return insertedEscList+insertedRows.length;
	}
	public int saveSLADetails(ServiceProviderVO savedSP, List<SLADetailsVO> slaListVOList, LoginUser loginUser, final String spSLAInsertQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int[] insertedRows = jdbcTemplate.batchUpdate(spSLAInsertQuery,
			       new BatchPreparedStatementSetter() {

	                    @Override
	                    public void setValues(PreparedStatement ps, int i) throws SQLException {
	                    	SLADetailsVO slaDetail = slaListVOList.get(i);
	                        ps.setLong(1, savedSP.getServiceProviderId());
	                        ps.setLong(2, slaDetail.getTicketPriority().getPriorityId());
	                        ps.setInt(3, Integer.parseInt(slaDetail.getDuration()));
	                        ps.setString(4, slaDetail.getUnit());
	                        ps.setString(5, loginUser.getUsername());
	                    }

	                    @Override
	                    public int getBatchSize() {
	                        return slaListVOList.size();
	                    }
	                });
	        return insertedRows.length;
		
	}
	
	public int updateSLADetails(ServiceProviderVO savedSP, List<SLADetailsVO> slaListVOList, LoginUser loginUser, final String spSLAQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<SLADetailsVO> slaListWithIds = new ArrayList<SLADetailsVO>();
		List<SLADetailsVO> slaListWithOutIds = new ArrayList<SLADetailsVO>();
		 int insertedSlaList = 0;
		for(SLADetailsVO slaDetails  : slaListVOList){
			if(slaDetails.getSlaId()!=null){
				slaListWithIds.add(slaDetails);
			}else{
				slaListWithOutIds.add(slaDetails);
			}
		}
        int[] updatedRows = jdbcTemplate.batchUpdate(spSLAQuery ,
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	SLADetailsVO slaDetail = slaListVOList.get(i);
                        ps.setInt(1, Integer.parseInt(slaDetail.getDuration()));
                        ps.setString(2, slaDetail.getUnit());
                        ps.setLong(3, slaDetail.getSlaId());
                        ps.setLong(4, savedSP.getServiceProviderId());
                    }

                    @Override
                    public int getBatchSize() {
                        return slaListWithIds.size();
                    }
                });
        if(slaListWithOutIds.size()>0 && StringUtils.isEmpty(savedSP.getSpDbName())){
        	insertedSlaList = saveSLADetails(savedSP, slaListWithOutIds, loginUser, AppConstants.INSERT_SP_SLA_QUERY);
        }
        else{
        	insertedSlaList = saveSLADetails(savedSP, slaListWithOutIds, loginUser, AppConstants.INSERT_RSP_SLA_QUERY);
        }
        return   insertedSlaList+updatedRows.length;
	}
	
	public Long getLastSPCreated() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Long lastSPId = jdbcTemplate.query(AppConstants.LAST_SP_ID_QUERY, new ResultSetExtractor<Long>() {
			
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				Long lastSP = 0l;
				if(rs.next()){
					lastSP = rs.getLong("sp_id");
				}
				return lastSP;
			}
		});
		return lastSPId;
	}

	public List<ServiceProviderVO> findSPList() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<ServiceProviderVO> externalSpList = jdbcTemplate.query(AppConstants.EXT_SERVICE_PROVIDER_LIST,  new ResultSetExtractor<List<ServiceProviderVO>>(){
			List<ServiceProviderVO> spList = new ArrayList<ServiceProviderVO>();
			@Override
			public List<ServiceProviderVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					ServiceProviderVO spProviderVO = new ServiceProviderVO();
					spProviderVO.setServiceProviderId(rs.getLong("sp_id"));
					spProviderVO.setName(rs.getString("sp_name"));
					spProviderVO.setCode(rs.getString("sp_code"));
					spProviderVO.setAdditionalDetails("sp_desc");
					spProviderVO.setEmail(rs.getString("sp_email"));
					spProviderVO.setHelpDeskNumber(rs.getString("help_desk_number"));
					spProviderVO.setHelpDeskEmail(rs.getString("help_desk_email"));
					spProviderVO.setSlaDescription(rs.getString("sla_description"));
					spProviderVO.getCountry().setCountryId(rs.getLong("country_id"));
					spProviderVO.getCountry().setCountryName(rs.getString("country_name"));
					spProviderVO.getRegion().setRegionId(rs.getLong("region_id"));
					spProviderVO.getRegion().setRegionName(rs.getString("region_name"));
					spProviderVO.setCompanyCode(rs.getString("company_code"));
					spProviderVO.setCountryName(rs.getString("country_name"));
					spList.add(spProviderVO);
				}
				return spList;
			}
		});
		return externalSpList;
	}
	public List<ServiceProviderVO> findRSPList() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<ServiceProviderVO> registeredSpList = jdbcTemplate.query(AppConstants.RSP_SERVICE_PROVIDER_LIST,  new ResultSetExtractor<List<ServiceProviderVO>>(){
			List<ServiceProviderVO> spList = new ArrayList<ServiceProviderVO>();
			@Override
			public List<ServiceProviderVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					ServiceProviderVO spProviderVO = new ServiceProviderVO();
					spProviderVO.setServiceProviderId(rs.getLong("sp_id"));
					spProviderVO.setName(rs.getString("sp_name"));
					spProviderVO.setCode(rs.getString("sp_code"));
					spProviderVO.setAdditionalDetails("sp_desc");
					spProviderVO.setEmail(rs.getString("sp_email"));
					spProviderVO.setHelpDeskNumber(rs.getString("help_desk_number"));
					spProviderVO.setHelpDeskEmail(rs.getString("help_desk_email"));
					spProviderVO.setSlaDescription(rs.getString("sla_description"));
					spProviderVO.getCountry().setCountryId(rs.getLong("country_id"));
					spProviderVO.getCountry().setCountryName(rs.getString("country_name"));
					spProviderVO.getRegion().setRegionId(rs.getLong("region_id"));
					spProviderVO.getRegion().setRegionName(rs.getString("region_name"));
					spProviderVO.setCountryName(rs.getString("country_name"));
					spProviderVO.setAccessGranted(rs.getString("access_granted"));
					spProviderVO.setSpDbName(rs.getString("sp_db_name"));
					
					spList.add(spProviderVO);
				}
				return spList;
			}
		});
		return registeredSpList;
	}
	public ServiceProviderVO findSPDetails(Long spId, String spViewType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String spQuery=null;
		if(spViewType.equalsIgnoreCase("EXT")){
			spQuery = AppConstants.EXT_SERVICE_PROVIDER_INFO;
		}
		else if(spViewType.equalsIgnoreCase("RSP")){
			spQuery = AppConstants.RSP_SERVICE_PROVIDER_INFO;
		}
		ServiceProviderVO spProviderVO= new ServiceProviderVO();
			jdbcTemplate.query(spQuery, new Object[]{ spId }, 
				new ResultSetExtractor<ServiceProviderVO>(){
			@Override
			public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					
					spProviderVO.setServiceProviderId(rs.getLong("sp_id"));
					spProviderVO.setName(rs.getString("sp_name"));
					spProviderVO.setCode(rs.getString("sp_code"));
					spProviderVO.setAdditionalDetails(rs.getString("sp_desc"));
					spProviderVO.setEmail(rs.getString("sp_email"));
					spProviderVO.setHelpDeskNumber(rs.getString("help_desk_number"));
					spProviderVO.setHelpDeskEmail(rs.getString("help_desk_email"));
					spProviderVO.setSlaDescription(rs.getString("sla_description"));
					if(spViewType.equalsIgnoreCase("EXT")){
						spProviderVO.setCompanyCode(rs.getString("company_code"));
						spProviderVO.setCompanyName(rs.getString("company_name"));
					}
					else if(spViewType.equalsIgnoreCase("RSP")){
						spProviderVO.setCompanyCode(rs.getString("sp_code"));
						spProviderVO.setCompanyName(rs.getString("sp_name"));
						spProviderVO.setAccessGranted(rs.getString("access_granted"));
						spProviderVO.setSpDbName(rs.getString("sp_db_name"));
					}
					spProviderVO.getCountry().setCountryId(rs.getLong("country_id"));
					spProviderVO.getCountry().setCountryName(rs.getString("country_name"));
					spProviderVO.getRegion().setRegionId(rs.getLong("region_id"));
					spProviderVO.getRegion().setRegionName(rs.getString("region_name"));
					
				}
				return spProviderVO;
			}
		 });
		return spProviderVO;
	}

	public List<EscalationLevelVO> getEscalationDetails(Long spId, String spViewType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String spQuery=null;
		if(spViewType.equalsIgnoreCase("EXT")){
			spQuery = AppConstants.EXT_SP_ESCALATIONS;
		}
		else if(spViewType.equalsIgnoreCase("RSP")){
			spQuery = AppConstants.RSP_ESCALATIONS_QUERY;
		}
		List<EscalationLevelVO> spEscList = jdbcTemplate.query(spQuery, new Object[] {spId}, new ResultSetExtractor<List<EscalationLevelVO>>(){
			List<EscalationLevelVO> spEscList = new ArrayList<EscalationLevelVO>();
			@Override
			public List<EscalationLevelVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					EscalationLevelVO  spEscalation = new EscalationLevelVO();
					spEscalation.setEscId(rs.getLong("esc_id"));
					spEscalation.setEscalationEmail(rs.getString("esc_email"));
					spEscalation.setEscalationLevel(String.valueOf(rs.getLong("esc_level")));
					spEscalation.setEscalationPerson(rs.getString("esc_person"));
					spEscList.add(spEscalation);
				}
				return spEscList;
			}
		});
		return spEscList==null?Collections.emptyList():spEscList;
	}
	
	public List<SLADetailsVO> getSLADetails(Long spId, String spViewType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String spQuery=null;
		if(spViewType.equalsIgnoreCase("EXT")){
			spQuery =  AppConstants.EXT_SPSLA_PRIORITY;
		}
		else if(spViewType.equalsIgnoreCase("RSP")){
			spQuery =  AppConstants.RSP_SLA_PRIORITY;
		}
		List<SLADetailsVO> spSLAList = jdbcTemplate.query( spQuery, new Object[] {spId}, new ResultSetExtractor<List<SLADetailsVO>>(){
			List<SLADetailsVO> spSLAList = new ArrayList<SLADetailsVO>();
			@Override
			public List<SLADetailsVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					SLADetailsVO  spSLa = new SLADetailsVO();
					spSLa.setSlaId(rs.getLong("sla_id"));
					spSLa.setDuration(String.valueOf(rs.getInt("duration")));
					spSLa.setUnit(rs.getString("unit"));
					TicketPriority ticketPriority = new TicketPriority();
					ticketPriority.setPriorityId(rs.getLong("priority_id"));
					if(ticketPriority.getPriorityId()!=null){
						if(ticketPriority.getPriorityId().intValue() == 1){
							ticketPriority.setDescription(com.pms.app.constants.TicketPriorityEnum.CRITICAL.name());
						}
						else if(ticketPriority.getPriorityId().intValue() == 2){
							ticketPriority.setDescription(TicketPriorityEnum.HIGH.name());
						}
						else if(ticketPriority.getPriorityId().intValue() == 3){
							ticketPriority.setDescription(TicketPriorityEnum.MEDIUM.name());
						}
						else if(ticketPriority.getPriorityId().intValue() == 4){
							ticketPriority.setDescription(TicketPriorityEnum.LOW.name());
						}
						spSLa.setTicketPriority(ticketPriority);
					}
					spSLAList.add(spSLa);
				}
				return spSLAList;
			}
		});
		return spSLAList == null?Collections.emptyList():spSLAList;
	}

	public List<UserVO> getAllActiveUsers(String customerCode, Long roleId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<UserVO> userList = jdbcTemplate.query(AppConstants.RSP_USER_LIST_QUERY, new Object[] {customerCode, roleId}, new ResultSetExtractor<List<UserVO>>(){
			List<UserVO> userList = new ArrayList<UserVO>();
			@Override
			public List<UserVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					UserVO userVO = new UserVO();
					userVO.setUserId(rs.getLong("user_id"));
					userVO.setFirstName(rs.getString("first_name"));
					userVO.setLastName(rs.getString("last_name"));
					userVO.setEmailId(rs.getString("email_id"));
					userVO.setUserType("SP");
					userList.add(userVO);
				}
				return userList;
			}
		});
		return userList;
	}

	public int updateRSPEscalalationLevels(ServiceProviderVO serviceProviderVO,	List<EscalationLevelVO> escalationLevelList, LoginUser loginUser) {
		
		return 0;
	}

	public int setAccessValue(Long rspId, String grantOrRevokeVal) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.CUST_SP_ACCESS_VALUE_UPDATE);
			ps.setString(1,grantOrRevokeVal);
			ps.setLong(2,rspId);
            return ps;
	        }
	      });
	 	return updated;
	}

	public ServiceProviderVO findRSPCustomerDetailsByCode(String companyCode, final String custMappedDbName) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderVO spProviderVO= new ServiceProviderVO();
		jdbcTemplate.query(AppConstants.RSP_CUSTOMER_MAPPED_SP_QUERY, new Object[]{ companyCode, custMappedDbName }, 
			new ResultSetExtractor<ServiceProviderVO>(){
		@Override
		public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
			if(rs.next()){
				spProviderVO.setRspCustId(rs.getLong("sp_cust_id"));
				spProviderVO.setRspOpsTenantSpId(rs.getLong("sp_id"));
				spProviderVO.setCustomerCode(rs.getString("customer_code"));
				spProviderVO.setCustDbName(rs.getString("cust_db_name"));
			}
			return spProviderVO;
		}
	 });
	return spProviderVO;
	}

	public int deleteRspCustomerRecord(ServiceProviderVO rspOpsTenant) throws SQLException {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.RSP_CUSTOMER_DELETE_QUERY);
			ps.setLong(1,rspOpsTenant.getRspCustId());
            return ps;
	        }
	      });
	 	return updated;
	}


	public ServiceProviderVO findRSPDetailsByCode(final String rspCode) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderVO spProviderVO= new ServiceProviderVO();
		jdbcTemplate.query(AppConstants.RSP_OPSTENAT_DETAILS_BYCODE, new Object[]{ rspCode }, 
			new ResultSetExtractor<ServiceProviderVO>(){
		@Override
		public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
			if(rs.next()){
				spProviderVO.setRspOpsTenantSpId(rs.getLong("sp_cid"));
				spProviderVO.setCustomerCode(rs.getString("sp_code"));
			}
			return spProviderVO;
		}
	 });
	return spProviderVO;
	}
	
	public ServiceProviderVO insertRspCustomerRecord(LoginUser loginUser, Long rspId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
		ServiceProviderVO serviceProviderVO = new ServiceProviderVO();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.RSP_CUSTOMER_MAPPING_INSERT, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, rspId);
	    	ps.setString(2, loginUser.getCompany().getCompanyCode());
            ps.setString(3, loginUser.getCompany().getCompanyName());
            ps.setLong(4, loginUser.getCompany().getCountryId());
            ps.setString(5, loginUser.getDbName());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved  user company {} with id {}.", loginUser.getCompany().getCompanyName(), key.getKey());
	    Long spCustId=key.getKey().longValue();
	    if(spCustId!=null){
	    	serviceProviderVO.setRspCustId(spCustId);
	    	serviceProviderVO.setCustomerCode(loginUser.getCompany().getCompanyCode());
	    	serviceProviderVO.setCustDbName(loginUser.getDbName());
	    }
	    return serviceProviderVO;
	}

	public int deleteRspCustomerAccessRecord(ServiceProviderVO rspCustomerMappedVO) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updated = jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.RSP_CUSTOMER_RSP_USERACCESS__QUERY);
			ps.setLong(1,rspCustomerMappedVO.getRspCustId());
            return ps;
	        }
	      });
	 	return updated;
	}

	public List<UserVO> getSPAgentUsers(LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<UserVO> userList = jdbcTemplate.query(RSPCustomerConstants.RSP_USER_LIST_QUERY,  new ResultSetExtractor<List<UserVO>>(){
			List<UserVO> userList = new ArrayList<UserVO>();
			@Override
			public List<UserVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while(rs.next()){
					UserVO userVO = new UserVO();
					userVO.setUserId(rs.getLong("user_id"));
					userVO.setFirstName(rs.getString("first_name"));
					userVO.setLastName(rs.getString("last_name"));
					userVO.setEmailId(rs.getString("email_id"));
					userVO.setUserType("RSP");
					userList.add(userVO);
				}
				return userList;
			}
		});
		return userList;
	}

}
