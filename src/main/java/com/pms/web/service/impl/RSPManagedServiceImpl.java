package com.pms.web.service.impl;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pms.app.dao.impl.RSPManagedDAO;
import com.pms.app.exception.RequiredFieldException;
import com.pms.app.exception.Validator;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.RSPExternalCustomerVO;
import com.pms.app.view.vo.RSPExternalSLADetailVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Country;
import com.pms.jpa.entities.Region;
import com.pms.web.service.RSPMangedService;

@Service("rspManagedService")
public class RSPManagedServiceImpl implements RSPMangedService {
	
	private static final Logger logger = LoggerFactory.getLogger(RSPManagedServiceImpl.class);
	private RSPManagedDAO getRspManagedDAO(String dbName) {
		return new RSPManagedDAO(dbName);
	}
	@Override
	public List<Region> getRegionList(LoginUser user) throws Exception {
		return getRspManagedDAO(user.getDbName()).findRspRegions();
	}
	@Override
	public List<Country> getCountryList(LoginUser user, Long regionId) throws Exception {
		return  getRspManagedDAO(user.getDbName()).findCountrybyRegion(regionId);
	}
	@Override
	@Transactional
	public RSPExternalCustomerVO saveExternalCustomer(RSPExternalCustomerVO externalCustomerVO, LoginUser loginUser)
			throws Exception {
		 if(externalCustomerVO.getCustomerId()==null){
			 logger.info("Saving External Customer for :"+ loginUser.getUserType());
			 boolean isValid=validateEntity(externalCustomerVO);
			 if(isValid){
				 externalCustomerVO = getRspManagedDAO(loginUser.getDbName()).saveExternalCustomer(externalCustomerVO, loginUser);
				 if(externalCustomerVO.getCustomerId()!=null){
					int recordsInserted = getRspManagedDAO(loginUser.getDbName()).saveOrUpdateExternalCustomerSLAList(externalCustomerVO.getSlaListVOList(), loginUser, "ADD", externalCustomerVO.getCustomerId());
					if(recordsInserted>0){
						externalCustomerVO.setStatus(200);
						
						logger.info("External Customer Details Saved");
					}
					
				 }
			 }
			 
		 }
		 else if(externalCustomerVO.getCustomerId()!=null){
			 logger.info("Updating External Customer for :"+ loginUser.getUserType());
			 boolean isValid=validateEntity(externalCustomerVO);
			 if(isValid){
				 RSPExternalCustomerVO savedCustomer =  getRspManagedDAO(loginUser.getDbName()).getExternalCustomer(externalCustomerVO, loginUser);
				 if(savedCustomer.getCustomerId()!=null &&  savedCustomer.getCustomerId().equals(externalCustomerVO.getCustomerId())){
					 int updatedRow = getRspManagedDAO(loginUser.getDbName()).updateExternalCustomer(externalCustomerVO, loginUser);
					 if(updatedRow>0){
						 externalCustomerVO.setStatus(202);
						 logger.info("External Customer Details Updated");
					 }
				 }else  if(savedCustomer.getCustomerId()==null){
					 int updatedRow = getRspManagedDAO(loginUser.getDbName()).updateExternalCustomer(externalCustomerVO, loginUser);
					 if(updatedRow>0){
						 externalCustomerVO.setStatus(202);
						 logger.info("External Customer Details Updated");
					 }
				 }else  {
						 externalCustomerVO.setStatus(204);
						 logger.info("Duplicate External Customer Details found");
				 }
			 }
		 }
		return externalCustomerVO;
	}
	private boolean validateEntity(RSPExternalCustomerVO externalCustomerVO) {
		boolean isValid=false;
		try {
			isValid = Validator.validateForNulls(externalCustomerVO);
			logger.info("Validated Successfully");
		} catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | RequiredFieldException e) {
			e.printStackTrace();
			logger.info("Unable to validate the Object.");
		}
		return isValid;
	}
	@Override
	public List<RSPExternalCustomerVO> getExternalCustomers(LoginUser loginUser) throws Exception {
		return  getRspManagedDAO(loginUser.getDbName()).getExternalCustomers();
	}
	@Override
	public List<RSPExternalSLADetailVO> getExternalCustomerSLA(LoginUser loginUser, Long extCustId) throws Exception {
		return getRspManagedDAO(loginUser.getDbName()).getExternalCustomerSLA(extCustId);
	}
	@Override
	@Transactional
	public RSPExternalCustomerVO updateExtCustSLA(RSPExternalCustomerVO externalCustomerVO, LoginUser loginUser)
			throws Exception {
		int recordsUpdated = getRspManagedDAO(loginUser.getDbName()).saveOrUpdateExternalCustomerSLAList(externalCustomerVO.getSlaListVOList(), loginUser, "UPDATE", externalCustomerVO.getCustomerId());
		if(recordsUpdated>0){
			externalCustomerVO.setStatus(200);
			int slaUpdated = getRspManagedDAO(loginUser.getDbName()).updateFurtherSLA(externalCustomerVO.getCustomerId(), externalCustomerVO.getSlaDescription());
			if(slaUpdated>0){
				externalCustomerVO.setStatus(200);
			}
		}
		return externalCustomerVO;
	}
	@Override
	@Transactional
	public List<TicketVO> getExternalCustomerIncidents(LoginUser loginUser, Long extCustId) throws Exception {
		List<TicketVO> extCustTickets = getRspManagedDAO(loginUser.getDbName()).getExtCustomericketsBySPcode(loginUser.getCompany().getCompanyCode(), extCustId);
		return extCustTickets == null?Collections.emptyList():extCustTickets;
	}

}
