package com.pms.web.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.web.service.AssetService;
import com.pms.web.service.ServiceProviderService;

@Service("serviceProviderService")
public class ServiceProviderServiceImpl implements ServiceProviderService {

	private static final Logger logger = LoggerFactory.getLogger(ServiceProviderServiceImpl.class);
	@Autowired
	private AssetService assetService;

	@Override
	@Transactional
	public List<ServiceProviderVO> findAllServiceProvider(LoginUser user) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		logger.info("Getting Service Provider List for logged in user : " + user.getFirstName() + " " + user.getLastName());
		List<ServiceProviderVO> serviceProviderList = assetService.findSPByCompanyIdIn(user);
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderList == null ? Collections.EMPTY_LIST : serviceProviderList;
	}

	@Override
	public List<ServiceProviderVO> findServiceProviderByCustomer(Long customerId) throws Exception {
		logger.info("Inside ServiceProviderServiceImpl -- findAllServiceProvider");
		List<ServiceProviderVO> serviceProviderVOList = new ArrayList<ServiceProviderVO>();
		logger.info("Exit ServiceProviderServiceImpl -- findAllServiceProvider");
		return serviceProviderVOList == null ? Collections.EMPTY_LIST : serviceProviderVOList;
	}

	

}
