package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.constants.RSPCustomerConstants;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.RSPExternalCustomerVO;
import com.pms.app.view.vo.RSPExternalSLADetailVO;
import com.pms.app.view.vo.SiteLicenceVO;
import com.pms.app.view.vo.UserVO;
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
	        ps.setString(1, externalCustomerVO.getCompanyName());
	    	ps.setString(2, externalCustomerVO.getCompanyCode());
            ps.setLong(3, externalCustomerVO.getCountryId());
            ps.setString(4, externalCustomerVO.getPrimaryContactEmail());
            ps.setString(5, externalCustomerVO.getSecondaryContactEmail());
            ps.setLong(6, Long.parseLong(externalCustomerVO.getPrimaryContactNumber()));
            ps.setLong(7, Long.parseLong(externalCustomerVO.getSecondaryContactNumber()));
            ps.setString(8, externalCustomerVO.getSlaDescription());
            ps.setString(9, user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved  External Customer {} with Customer ID {}.", externalCustomerVO.getCompanyName(), key.getKey());
	    externalCustomerVO.setCustomerId(key.getKey().longValue());
	    return externalCustomerVO;
	}

	public int saveOrUpdateExternalCustomerSLAList(List<RSPExternalSLADetailVO> slaListVOList, LoginUser loginUser, String mode, Long externalCustomerId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String query = "";
		if(mode.equalsIgnoreCase("ADD")){
			query = RSPCustomerConstants.INSERT_EXTERNAL_CUSTOMER_SLA_LIST;
		}
		else{
			query = RSPCustomerConstants.UPDATE_EXTERNAL_CUSTOMER_SLA_LIST; 
		}
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
        return insertedRows.length;
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
						rspExternalCustomerVO.setCompanyName(rs.getString("cust_name"));
						rspExternalCustomerVO.setCompanyCode(rs.getString("cust_code"));
						rspExternalCustomerVO.setPrimaryContactEmail(rs.getString("p_email"));
						rspExternalCustomerVO.setPrimaryContactNumber(String.valueOf(rs.getLong("p_contact_no")));
						rspExternalCustomerVO.setCountryId(rs.getLong("country_id"));
						rspExternalCustomerVO.setCountryName(rs.getString("country_name"));
						rspExternalCustomerVO.setRegionId(rs.getLong("region_id"));
						rspExternalCustomerVO.setRegionName(rs.getString("region_name"));
						rspExternalCustomerVO.setSecondaryContactEmail(rs.getString("s_email"));
						rspExternalCustomerVO.setSecondaryContactNumber(rs.getString("s_contact_no"));
						custList.add(rspExternalCustomerVO);
						}
						return custList;
					}
				});
		LOGGER.info("Exit RSPManagedDAO .. getExternalCustomers");
		return customerList;
	}
}
