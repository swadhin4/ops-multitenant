package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.DistrictVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.jpa.entities.Area;
import com.pms.jpa.entities.Cluster;
import com.pms.jpa.entities.Region;


public interface DistrictService {

	public List<DistrictVO> findDistrictByCountry(Long countryId, LoginUser user) throws Exception;
	
	public List<Area> findAreaByDistrict(Long districtId, LoginUser user) throws Exception;
	
	public List<Cluster> findClusterByArea(Long districtId, Long areaId, LoginUser user) throws Exception;

	List<Region> findAllRegions(LoginUser user) throws Exception;

}
