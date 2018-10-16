package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.FinancialVO;
import com.pms.jpa.entities.Financials;

public interface FinancialService {

	List<Financials>  findByTicketId(Long TicketId);
	boolean deleteById(Long costid);
	List<Financials> save(List<Financials> fin, String username);
	List<Financials> saveFinancials(List<FinUpdReqBodyVO> finVOList, String username);
	
	List<Financials> saveAndUpdate(List<FinancialVO> finVO, String username);
	
}
