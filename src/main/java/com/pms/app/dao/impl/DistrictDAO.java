package com.pms.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.DistrictVO;
import com.pms.jpa.entities.Area;
import com.pms.jpa.entities.Cluster;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;

public class DistrictDAO {

	public DistrictDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	public DistrictVO findByCountryId(Long countryId, String username, final String districtQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		DistrictVO distrcitVO =  jdbcTemplate.query(districtQuery, new Object[]{countryId}, new ResultSetExtractor<DistrictVO>() {
			@Override
			public DistrictVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				DistrictVO districtVO = new DistrictVO();
				if(rs.next()){
				
				districtVO.setDistrictId(rs.getLong("district_id"));
				districtVO.setDistrictName(rs.getString("district_name"));
				}
				return districtVO;
			}
		});
		return distrcitVO;
	}

	public List<Area> findAreaByDistrictId(Long districtId, String username) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Area> areaList =  jdbcTemplate.query(AppConstants.USER_AREA_QUERY, new Object[]{districtId}, new ResultSetExtractor<List<Area>>() {
			@Override
			public List<Area> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Area> areas = new ArrayList<Area>();
				while(rs.next()){
					Area area = new Area();
					area.setAreaId(rs.getLong("area_id"));
					area.setAreaName(rs.getString("area_name"));
					areas.add(area);
				}
				return areas;
			}
		});
		return areaList;
	}

	public List<Cluster> findClusterByAreaId(Long districtId, Long areaId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Cluster> clusterList =  jdbcTemplate.query(AppConstants.USER_CLUSTER_QUERY, new Object[]{districtId,areaId}, new ResultSetExtractor<List<Cluster>>() {
			@Override
			public List<Cluster> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Cluster> clusters = new ArrayList<Cluster>();
				while(rs.next()){
					Cluster cluster = new Cluster();
					cluster.setClusterID(rs.getLong("cluster_id"));
					cluster.setClusterName(rs.getString("cluster_name"));
					clusters.add(cluster);
				}
				return clusters;
			}
		});
		return clusterList;
	}

	public List<Region> findRegionList() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Region> regionList =  jdbcTemplate.query(AppConstants.REGION_LIST_QUERY, new ResultSetExtractor<List<Region>>() {
			@Override
			public List<Region> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Region> regionList = new ArrayList<Region>();
				while(rs.next()){
					Region region = new Region();
					region.setRegionId(rs.getLong("region_id"));
					region.setRegionCode(rs.getString("region_code"));
					region.setRegionName(rs.getString("region_name"));
					regionList.add(region);
				}
				return regionList;
			}
		});
		return regionList;
 
	}

	public List<Country> findCountryByRegion(Long regionId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Country> countryList =  jdbcTemplate.query(AppConstants.COUNTRY_LIST_QUERY, new Object [] {regionId}, new ResultSetExtractor<List<Country>>() {
			@Override
			public List<Country> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Country> countryList = new ArrayList<Country>();
				while(rs.next()){
					Country country = new Country();
					country.setCountryId(rs.getLong("country_id"));
					country.setCountryCode(rs.getString("country_code"));
					country.setCountryName(rs.getString("country_name"));
					countryList.add(country);
				}
				return countryList;
			}
		});
		return countryList;
	}
}
