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

public class DistrictDAO {

	public DistrictDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}

	public DistrictVO findByCountryId(Long countryId, String username) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		DistrictVO distrcitVO =  jdbcTemplate.query(AppConstants.USER_DISTRICT_QUERY, new Object[]{countryId}, new ResultSetExtractor<DistrictVO>() {
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
}
