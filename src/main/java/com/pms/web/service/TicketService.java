package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.FinUpdReqBodyVO;
import com.pms.app.view.vo.FinancialVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.SPEscalationLevels;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;


public interface TicketService {

	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser, String assignedTo) throws Exception;
	
	public List<TicketCategory> getTicketCategories(LoginUser loginUser) throws Exception;
	
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId, String assetType, LoginUser loginUser) throws Exception;

	public List<Status> getStatusByCategory(LoginUser loginUser, String category) throws Exception;
	public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser use) throws Exception;

	public String updateSlaDate(String ticketNumber, int duration, String unit, LoginUser loginUser) throws Exception;

	public TicketVO getSelectedTicket(Long ticketId,LoginUser loginUser) throws Exception;

	public List<TicketAttachment> findByTicketId(Long ticketId, LoginUser loginUser) throws Exception;

	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser loginUser) throws Exception;

	public List<TicketCommentVO> getTicketComments(Long ticketId, LoginUser loginUser) throws Exception;

	public List<TicketHistoryVO> getTicketHistory(String ticketNumber, LoginUser loginUser) throws Exception;

	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicketId, LoginUser loginUser) throws Exception;
	
	public List<TicketVO> getRelatedTickets(Long ticketId, Long siteId, LoginUser loginUser) throws Exception;

	public int changeLinkedTicketStatus(Long linkedTicket,  LoginUser loginUser) throws Exception;

	public int deleteLinkedTicket(Long linkedTicket, LoginUser loginUser) throws Exception;

	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket,
			LoginUser user) throws Exception;

	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) throws Exception;

	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId, LoginUser user);

	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId, LoginUser user, String spType) throws Exception;

	public List<Financials> saveFinancials(List<FinUpdReqBodyVO> finVOList, LoginUser user) throws Exception;

	List<Financials> saveAndUpdate(List<FinancialVO> financialVOList, LoginUser user) throws Exception;

	List<Financials> save(List<Financials> finList, LoginUser user) throws Exception;

	List<Financials> findFinanceByTicketId(Long ticketId, LoginUser user) throws Exception;

	List<TicketVO> getAllSPTickets(LoginUser loginUser) throws Exception;

	public boolean deleteFinanceCostById(Long costId, LoginUser user) throws Exception;

	public List<TicketVO> getTicketsForSP(LoginUser loginUser, String ticketCreatedBy, String custDBName) throws Exception;

	public EscalationLevelVO getSPEscalationLevels(Long escId, LoginUser loginUser, String ticketAssignedType) throws Exception;
	
	/*public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser user, SPLoginVO savedLoginVO) throws PMSServiceException;

	public CustomerTicket saveOrUpdate(CustomerTicket customerTicket) throws Exception;

	public List<CustomerTicketVO> getOpenCustomerTickets() throws Exception;

	public List<CustomerTicket> getTicketsByStatus(Long statusId) throws Exception;

	public List<CustomerTicket> getOpenTicketsBySite(Long siteId) throws Exception;


	public CustomerTicketVO getCustomerTicket(Long ticktId) throws Exception;

	

	
	
	

	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) throws Exception;

	public CustomerSPLinkedTicketVO saveLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, LoginUser loginUser) throws Exception;

	public List<CustomerSPLinkedTicketVO> getAllLinkedTickets(Long custTicket) throws Exception;

	public CustomerSPLinkedTicket deleteLinkedTicket(Long linkedTicketId, String email);

	public List<TicketEscalationVO> getAllEscalationLevels(Long ticketId);

	public TicketEscalationVO getEscalationStatus(Long ticketId, Long escId);
	
	public List<TicketHistoryVO> getTicketHistory(Long ticketId);

	public CustomerSPLinkedTicketVO changeLinkedTicketStatus(Long linkedTicket, String status, String updatedBy) throws Exception;
	
	public List<TicketCommentVO> getTicketComments(Long ticketId);
	
	public TicketCommentVO saveTicketComment(TicketCommentVO ticketCommentVO, LoginUser user) throws Exception;

	public List<TicketVO> getCustomerTicketsBySP(Long spId) throws Exception;

	public CustomerSPLinkedTicketVO saveSPLinkedTicket(Long custTicket, String custTicketNumber, String linkedTicket, String spEmail) throws Exception;

	public TicketCommentVO saveSPTicketComment(TicketCommentVO ticketCommentVO, SPLoginVO savedLoginVO) throws Exception;
	
	//Added By Ankit for Related Tickets
		
	
	//Added By Ankit for SP Related Tickets
	public List<TicketVO> getSPRelatedTickets(Long ticketId,Long siteId, Long spId) throws Exception;		*/

}
