package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pms.app.constants.AppConstants;
import com.pms.app.constants.UserType;
import com.pms.app.dao.impl.DistrictDAO;
import com.pms.app.view.vo.DistrictVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.Area;
import com.pms.jpa.entities.Cluster;
import com.pms.jpa.entities.Region;
import com.pms.web.service.DistrictService;



@Service("districtService")
public class DistrictServiceImpl implements DistrictService{

	private DistrictDAO getDistrictDAO(String dbName) {
		return new DistrictDAO(dbName);
	}
	
	@Override
	public List<DistrictVO> findDistrictByCountry(final Long countryId, LoginUser user) throws Exception {
		List<DistrictVO> districtVOList = new ArrayList<DistrictVO>();
		DistrictDAO districtDAO=getDistrictDAO(user.getDbName());
		String districtQuery="";
		if(user.getUserType().equalsIgnoreCase(UserType.LOGGEDIN_USER_RSP.getUserType())){
			districtQuery=AppConstants.SP_DISTRICT_QUERY;
		}else{
			districtQuery=AppConstants.USER_DISTRICT_QUERY;
		}
		DistrictVO districtVO = 	districtDAO.findByCountryId(countryId, user.getUsername(), districtQuery);
		districtVOList.add(districtVO);
		return districtVOList ==null?Collections.emptyList():districtVOList;
	}

	@Override
	public List<Area> findAreaByDistrict(Long districtId, LoginUser user) throws Exception {
		DistrictDAO districtDAO=getDistrictDAO(user.getDbName());
		List<Area> areaList = 	districtDAO.findAreaByDistrictId(districtId, user.getUsername());
		return areaList ==null?Collections.emptyList():areaList;
	}

	@Override
	public List<Cluster> findClusterByArea(Long districtId, Long areaId, LoginUser user) throws Exception {
		DistrictDAO districtDAO=getDistrictDAO(user.getDbName());
		List<Cluster> clusterList = 	districtDAO.findClusterByAreaId(districtId, areaId);
		return clusterList ==null?Collections.emptyList():clusterList;
	}
	@Override
	public List<Region> findAllRegions(LoginUser user) throws Exception {
		List<Region> regionList = getDistrictDAO(user.getDbName()).findRegionList();
		return  regionList== null? Collections.emptyList():regionList;
	}

}
