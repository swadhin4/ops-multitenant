package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.RSPExternalCustomerVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;

public interface RSPMangedService {
	
	public List<Region> getRegionList(LoginUser user) throws Exception;

	public List<Country> getCountryList(LoginUser loginUser, Long regionId) throws Exception;

	public RSPExternalCustomerVO saveExternalCustomer(RSPExternalCustomerVO externalCustomerVO, LoginUser loginUser) throws Exception;

	public List<RSPExternalCustomerVO> getExternalCustomers(LoginUser loginUser)  throws Exception;

}
