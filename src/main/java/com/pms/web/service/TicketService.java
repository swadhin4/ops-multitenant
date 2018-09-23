package com.pms.web.service;

import java.util.List;

import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.TicketCategory;


public interface TicketService {

	public List<TicketVO> getAllCustomerTickets(LoginUser loginUser) throws Exception;
	
	public List<TicketCategory> getTicketCategories(LoginUser loginUser) throws Exception;
	
	public TicketPrioritySLAVO getTicketPriority(Long serviceProviderID, Long ticketCategoryId, LoginUser loginUser) throws Exception;
	
	/*public TicketVO saveOrUpdate(TicketVO customerTicket, LoginUser user, SPLoginVO savedLoginVO) throws PMSServiceException;

	public CustomerTicket saveOrUpdate(CustomerTicket customerTicket) throws Exception;

	public List<CustomerTicketVO> getOpenCustomerTickets() throws Exception;

	public List<CustomerTicket> getTicketsByStatus(Long statusId) throws Exception;

	public List<CustomerTicket> getOpenTicketsBySite(Long siteId) throws Exception;


	public CustomerTicketVO getCustomerTicket(Long ticktId) throws Exception;

	

	public TicketVO getSelectedTicket(Long ticketId) throws Exception;
	
	

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
		public List<TicketVO> getRelatedTickets(Long ticketId,Long siteId) throws Exception;
	
	//Added By Ankit for SP Related Tickets
	public List<TicketVO> getSPRelatedTickets(Long ticketId,Long siteId, Long spId) throws Exception;		*/

}
