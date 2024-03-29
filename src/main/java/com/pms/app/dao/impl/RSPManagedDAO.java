package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.constants.RSPCustomerConstants;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.RSPExternalCustomerVO;
import com.pms.app.view.vo.RSPExternalSLADetailVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.util.ApplicationUtil;

@Repository
public class RSPManagedDAO {
	
	private final static Logger LOGGER = LoggerFactory.getLogger(RSPManagedDAO.class);
	
	public RSPManagedDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	public List<Region> findRspRegions() {
		LOGGER.info("Inside RSPManagedDAO .. findRegions");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Region> regionList = jdbcTemplate.query(RSPCustomerConstants.RSP_REGION_LIST_QUERY,
				new ResultSetExtractor<List<Region>>() {
					@Override
					public List<Region> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<Region> rspRegionList = new ArrayList<Region>();
						while (rs.next()) {
						Region region = new Region();
						region.setRegionId(rs.getLong("region_id"));
						region.setRegionCode(rs.getString("region_code"));
						region.setRegionName(rs.getString("region_name"));
						rspRegionList.add(region);
						}
						return rspRegionList;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. findRegions");
		return regionList;
	}

	public List<Country> findCountrybyRegion(Long regionId) {
		LOGGER.info("Inside RSPManagedDAO .. findRegions");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Country> regionCountryList = jdbcTemplate.query(RSPCustomerConstants.RSP_COUNTRY_LIST_QUERY, new Object[]{regionId},
				new ResultSetExtractor<List<Country>>() {
					@Override
					public List<Country> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<Country> countryList = new ArrayList<Country>();
						while (rs.next()) {
						Country country = new Country();
						country.setCountryId(rs.getLong("country_id"));
						country.setCountryCode(rs.getString("country_code"));
						country.setCountryName(rs.getString("country_name"));
						countryList.add(country);
						}
						return countryList;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. findRegions");
		return regionCountryList;
	}

	public RSPExternalCustomerVO saveExternalCustomer(RSPExternalCustomerVO externalCustomerVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(RSPCustomerConstants.SAVE_RSP_EXTERNAL_CUSTOMER_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setString(1, externalCustomerVO.getCustomerName());
	    	ps.setString(2, externalCustomerVO.getCompanyCode());
            ps.setLong(3, externalCustomerVO.getCountryId());
            ps.setString(4, externalCustomerVO.getPrimaryContactEmail());
            ps.setString(5, externalCustomerVO.getSecondaryContactEmail());
            ps.setLong(6, Long.parseLong(externalCustomerVO.getPrimaryContactNumber()));
            if(!StringUtils.isEmpty(externalCustomerVO.getSecondaryContactNumber())){
            	ps.setLong(7, Long.parseLong(externalCustomerVO.getSecondaryContactNumber()));
            }else{
            	ps.setNull(7, Types.NULL);
            }
            ps.setString(8, externalCustomerVO.getSlaDescription());
            ps.setString(9, user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved  External Customer {} with Customer ID {}.", externalCustomerVO.getCustomerName(), key.getKey());
	    externalCustomerVO.setCustomerId(key.getKey().longValue());
	    return externalCustomerVO;
	}

	public int saveOrUpdateExternalCustomerSLAList(List<RSPExternalSLADetailVO> slaListVOList, LoginUser loginUser, String mode, Long externalCustomerId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int recordData=0;
		if(mode.equalsIgnoreCase("ADD")){
			final String query = RSPCustomerConstants.INSERT_EXTERNAL_CUSTOMER_SLA_LIST;
		    int[] insertedRows = jdbcTemplate.batchUpdate(query,new BatchPreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	            	RSPExternalSLADetailVO externalCustSLA = slaListVOList.get(i);
	            	ps.setLong(1, externalCustomerId);
	                ps.setLong(2, externalCustSLA.getPriorityId());
	                ps.setInt(3, externalCustSLA.getDuration());
	                ps.setString(4, externalCustSLA.getUnit());
	                ps.setString(5, loginUser.getUsername());
	                
	            }

	            @Override
	            public int getBatchSize() {
	                return slaListVOList.size();
	            }
	       });
		    recordData = insertedRows.length;
		}
		else if(mode.equalsIgnoreCase("UPDATE")){
			final String query  = RSPCustomerConstants.UPDATE_EXTERNAL_CUSTOMER_SLA_LIST;
		    int[] updatedRows = jdbcTemplate.batchUpdate(query,new BatchPreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps, int i) throws SQLException {
	            	RSPExternalSLADetailVO externalCustSLA = slaListVOList.get(i);
	                ps.setInt(1, externalCustSLA.getDuration());
	                ps.setString(2, externalCustSLA.getUnit());
	                ps.setLong(3, externalCustSLA.getSlaId());
	                ps.setLong(4, externalCustomerId);
	            }

	            @Override
	            public int getBatchSize() {
	                return slaListVOList.size();
	            }
	       });
		    recordData = updatedRows.length;
		}
    
        return recordData;
	}

	public List<RSPExternalCustomerVO> getExternalCustomers() {
		LOGGER.info("Inside RSPManagedDAO .. getExternalCustomers");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<RSPExternalCustomerVO> customerList = jdbcTemplate.query(RSPCustomerConstants.RSP_EXTERNAL_CUSTOMER_LIST_QUERY,
				new ResultSetExtractor<List<RSPExternalCustomerVO>>() {
					@Override
					public List<RSPExternalCustomerVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<RSPExternalCustomerVO> custList = new ArrayList<RSPExternalCustomerVO>();
						while (rs.next()) {
						RSPExternalCustomerVO rspExternalCustomerVO = new RSPExternalCustomerVO();
						rspExternalCustomerVO.setCustomerId(rs.getLong("cust_cid"));
						rspExternalCustomerVO.setCustomerName(rs.getString("cust_name"));
						rspExternalCustomerVO.setCompanyCode(rs.getString("cust_code"));
						rspExternalCustomerVO.setPrimaryContactEmail(rs.getString("p_email"));
						rspExternalCustomerVO.setPrimaryContactNumber(String.valueOf(rs.getLong("p_contact_no")));
						rspExternalCustomerVO.setCountryId(rs.getLong("country_id"));
						rspExternalCustomerVO.setCountryName(rs.getString("country_name"));
						rspExternalCustomerVO.setRegionId(rs.getLong("region_id"));
						rspExternalCustomerVO.setRegionName(rs.getString("region_name"));
						rspExternalCustomerVO.setSecondaryContactEmail(rs.getString("s_email"));
						rspExternalCustomerVO.setSecondaryContactNumber(rs.getString("s_contact_no"));
						rspExternalCustomerVO.setSlaDescription(rs.getString("sla_desc"));
						custList.add(rspExternalCustomerVO);
						}
						return custList;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. getExternalCustomers");
		return customerList;
	}

	public List<RSPExternalSLADetailVO> getExternalCustomerSLA(Long extCustId) {
		LOGGER.info("Inside RSPManagedDAO .. getExternalCustomerSLA");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<RSPExternalSLADetailVO> slaList = jdbcTemplate.query(RSPCustomerConstants.RSP_EXTERNAL_CUSTOMER_SLA_LIST_QUERY, new Object[]{extCustId},
				new ResultSetExtractor<List<RSPExternalSLADetailVO>>() {
					@Override
					public List<RSPExternalSLADetailVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<RSPExternalSLADetailVO> custSLAList = new ArrayList<RSPExternalSLADetailVO>();
						while (rs.next()) {
						RSPExternalSLADetailVO rspExternalSLAVO = new RSPExternalSLADetailVO();
						rspExternalSLAVO.setSlaId(rs.getLong("sla_id"));
						rspExternalSLAVO.setPriorityId(rs.getLong("priority_id"));
						rspExternalSLAVO.setPriority(rs.getString("description"));
						rspExternalSLAVO.setUnit(rs.getString("unit"));
						rspExternalSLAVO.setDuration(rs.getInt("duration"));
						custSLAList.add(rspExternalSLAVO);
						}
						return custSLAList;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. getExternalCustomerSLA");
		return slaList;
	}

	public int updateExternalCustomer(RSPExternalCustomerVO externalCustomerVO, LoginUser loginUser) {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
	        int updatedRow = jdbcTemplate.update(RSPCustomerConstants.UPDATE_RSP_EXTERNAL_CUSTOMER_QUERY, new PreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	        		ps.setString(1, externalCustomerVO.getCustomerName());
	        		ps.setString(2, externalCustomerVO.getCompanyCode());
	        		ps.setLong(3, externalCustomerVO.getCountryId());
	        		ps.setString(4, externalCustomerVO.getPrimaryContactEmail());
	        		ps.setString(5, externalCustomerVO.getSecondaryContactEmail() );
	        		ps.setLong(6, Long.parseLong(externalCustomerVO.getPrimaryContactNumber()));
	        		if(StringUtils.isEmpty(externalCustomerVO.getSecondaryContactNumber())){
	        			ps.setLong(7,Types.NULL);
	        		}
	        		else{
	        			ps.setLong(7,Long.parseLong(externalCustomerVO.getSecondaryContactNumber()));
	        		}
	        		ps.setString(8, externalCustomerVO.getSlaDescription());
	        		ps.setLong(9, externalCustomerVO.getCustomerId());
	            }

	        });
			return updatedRow;
	           
	    }

	public RSPExternalCustomerVO getExternalCustomer(RSPExternalCustomerVO externalCustomerVO, LoginUser loginUser) {
		LOGGER.info("Inside RSPManagedDAO .. getExternalCustomer");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		RSPExternalCustomerVO savedCustomer = jdbcTemplate.query(RSPCustomerConstants.SELECT_RSP_EXTERNAL_CUSTOMER_QUERY, new Object[]{externalCustomerVO.getCompanyCode(), externalCustomerVO.getPrimaryContactEmail()},
				new ResultSetExtractor<RSPExternalCustomerVO>() {
					@Override
					public RSPExternalCustomerVO extractData(ResultSet rs) throws SQLException, DataAccessException {
						RSPExternalCustomerVO rspExternalSLAVO = new RSPExternalCustomerVO();
						if(rs.next()) {
							rspExternalSLAVO.setCustomerId(rs.getLong("cust_cid"));
							rspExternalSLAVO.setCustomerName(rs.getString("cust_name"));
							rspExternalSLAVO.setCompanyCode(rs.getString("cust_code"));
							rspExternalSLAVO.setPrimaryContactEmail(rs.getString("p_email"));
						}
						return rspExternalSLAVO;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. getExternalCustomer");
		return savedCustomer;
	}

	public int updateFurtherSLA(Long customerId, String slaDescription) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		  int updatedRow = jdbcTemplate.update(RSPCustomerConstants.UPDATE_EXTERNAL_CUSTOMER_SLA_DESCRIPTION, new PreparedStatementSetter() {
	            @Override
	            public void setValues(PreparedStatement ps) throws SQLException {
	        		ps.setString(1,slaDescription);
	        		ps.setLong(2, customerId);
	            }

	        });
			return updatedRow;
		
	}
	
	public List<TicketVO> getExtCustomericketsBySPcode(String spcode, Long extCustId) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketVOs = jdbcTemplate.query(RSPCustomerConstants.EXT_CUSTOMER_TICKETS_QUERY,
				new Object[] { spcode , extCustId }, new ResultSetExtractor<List<TicketVO>>() {
					@Override
					public List<TicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<TicketVO> ticketVOList = new ArrayList<TicketVO>();
						while (rs.next()) {
							TicketVO ticketVO = new TicketVO();
							ticketVO.setTicketId(rs.getLong("id"));
							ticketVO.setTicketNumber(rs.getString("ticket_number"));
							ticketVO.setTicketTitle(rs.getString("ticket_title"));
							ticketVO.setSiteId(rs.getLong("site_id"));
							ticketVO.setSiteName(rs.getString("site_name"));
							ticketVO.setAssetId(rs.getLong("asset_id"));
							ticketVO.setAssetName(rs.getString("asset_name"));
							ticketVO.setRaisedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
							ticketVO.setSla(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate")));
							ticketVO.setAssignedSP(rs.getString("sp_name"));
							ticketVO.setStatusId(rs.getLong("status_id"));
							ticketVO.setStatus(rs.getString("status"));
							ticketVO.setRaisedBy(rs.getString("created_by"));

							ticketVOList.add(ticketVO);
						}
						return ticketVOList;
					}
				});
		return ticketVOs == null ? Collections.emptyList() : ticketVOs;
	}
}
