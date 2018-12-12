package com.pms.app.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SelectedTicketVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
import com.pms.jpa.entities.TicketComment;
import com.pms.web.util.ApplicationUtil;
@Repository
public class IncidentDAO {
	private final static Logger LOGGER = LoggerFactory.getLogger(IncidentDAO.class);

	public IncidentDAO(String userConfig) {
		ConnectionManager.getInstance(userConfig);
	}
	public List<TicketVO> findTicketsBySiteIdIn(Set<Long> siteIdList) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("siteIds", siteIdList);
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.TICKET_LIST_QUERY, parameters,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	
	public List<TicketCategory> findTicketCategories() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketCategory> ticketCategoryList = jdbcTemplate.query(AppConstants.TICKET_CATEGORY_QUERY, new RowMapper<TicketCategory>() {
			@Override
			public TicketCategory mapRow(ResultSet rs, int arg1) throws SQLException {
				return toTicketategoryList(rs);
			}
		});
		return ticketCategoryList;
	}
	
	protected TicketCategory toTicketategoryList(ResultSet rs) throws SQLException  {
		TicketCategory ticketCategory = new TicketCategory();
		ticketCategory.setId(rs.getLong("id"));
		ticketCategory.setTicketCategory(rs.getString("ticket_category"));
		ticketCategory.setDescription(rs.getString("description"));
		return ticketCategory;
	}
	private TicketVO toTicketList(ResultSet rs) throws SQLException {
		DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		TicketVO ticketVO = new TicketVO();
		ticketVO.setTicketId(rs.getLong("id"));
		ticketVO.setTicketNumber(rs.getString("ticket_number"));
		ticketVO.setTicketTitle(rs.getString("ticket_title"));
		ticketVO.setSiteId(rs.getLong("site_id"));
		ticketVO.setSiteName(rs.getString("site_name"));
		ticketVO.setAssetId(rs.getLong("asset_id"));
		ticketVO.setAssetName(rs.getString("asset_name"));
		ticketVO.setRaisedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
		String slaDueDate = ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate"));
		ticketVO.setSla(slaDueDate);
		ticketVO.setAssignedTo(rs.getLong("sp_id"));
		ticketVO.setAssignedSP(rs.getString("sp_name"));
		ticketVO.setStatus(rs.getString("status"));
		ticketVO.setStatusDescription(rs.getString("description"));
		ticketVO.setStatusId(rs.getLong("status_id"));
		return ticketVO;
	}
	public TicketPrioritySLAVO getSPSLADetails(Long serviceProviderID, Long ticketCategoryId, String spType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String spTicketPriorityQuery=null;
		if(spType.equalsIgnoreCase("SP")){
			spTicketPriorityQuery = AppConstants.TICKET_PRIORITY_RSP_SLA_QUERY;
		}
			else {
				spTicketPriorityQuery = AppConstants.TICKET_PRIORITY_SP_SLA_QUERY;
			}
		TicketPrioritySLAVO ticketPriority = jdbcTemplate.query(spTicketPriorityQuery, new Object[]{serviceProviderID, ticketCategoryId}, new ResultSetExtractor<TicketPrioritySLAVO>() {
			@Override
			public TicketPrioritySLAVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				TicketPrioritySLAVO  ticketPrioritySLAVO = new TicketPrioritySLAVO(); 
				if(rs.next()){
				int duration = rs.getInt("duration");
				String unit =  rs.getString("unit");
				Date slaDate=null;
				if(unit.equalsIgnoreCase("hours")){
					slaDate = DateUtils.addHours(new Date(), duration);
				}else if(unit.equalsIgnoreCase("days")){
					slaDate = DateUtils.addDays(new Date(), duration);
				}
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
					ticketPrioritySLAVO.setPriorityId(rs.getLong("priority_id"));
					ticketPrioritySLAVO.setPriorityName(rs.getString("description"));
					ticketPrioritySLAVO.setServiceProviderId(rs.getLong("sp_id"));
					ticketPrioritySLAVO.setServiceProviderName(rs.getString("sp_name"));
					ticketPrioritySLAVO.setTicketCategoryId(rs.getLong("id"));
					ticketPrioritySLAVO.setTicketSlaDueDate(simpleDateFormat.format(slaDate));
					ticketPrioritySLAVO.setDuration(duration);
					ticketPrioritySLAVO.setUnits(unit);
					}
				return ticketPrioritySLAVO;
				}
			} );
		return ticketPriority;
	}
	public List<Status> getStatusByCategory(String category) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Status> ticketStatusList = jdbcTemplate.query(AppConstants.TICKETS_STATUS_QUERY, new Object[]{category}, new ResultSetExtractor<List<Status>>() {
			@Override
			public List<Status> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Status> statusList = new ArrayList<Status>();
				while(rs.next()){
					Status  status = new Status();
					status.setStatusId(rs.getLong("status_id"));
					status.setStatus(rs.getString("status"));
					status.setDescription(rs.getString("description"));
					status.setCategory(rs.getString("category"));
					statusList.add(status);
				}
				return statusList;
			}
	  });
		return ticketStatusList;
	}
	public Long getLastIncidentCreated(String criteriea) {
		String lastIncidentQuery=null;
		if(criteriea.equalsIgnoreCase("USER")){
			lastIncidentQuery = AppConstants.LAST_INCIDENT_NUMBER_QUERY;
		}
		else if(criteriea.equalsIgnoreCase("SP")){
			lastIncidentQuery = AppConstants.LAST_SP_INCIDENT_NUMBER_QUERY;
		}
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Long lastTicketId = jdbcTemplate.query(lastIncidentQuery, new ResultSetExtractor<Long>() {
			
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				Long lastTicket = 0l;
				if(rs.next()){
					lastTicket = rs.getLong("id");
				}
				return lastTicket;
			}
		});
		return lastTicketId;
	}
	
	public TicketVO saveOrUpdateIncident (final TicketVO ticketVO, LoginUser user){
		LOGGER.info("IncidentDAO -- saveOrUpdateIncident -- Save Incident details : ");
		if(ticketVO.getTicketId()==null){
			try {
				TicketVO savedTicketVO =null;
				if(user.getUserType().equalsIgnoreCase("USER")){
				savedTicketVO = insertTicketData(ticketVO,user);
				}else if(user.getUserType().equalsIgnoreCase("SP")){
					savedTicketVO = insertSPTicketData(ticketVO,user);
				}
				if(savedTicketVO.getTicketId()!=null ){
					LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
					savedTicketVO.setStatusCode(200);
					savedTicketVO.setMessage("Created");
				}else{
					LOGGER.info("Unable to create incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		}else if(ticketVO.getTicketId()!=null){
			try {
				TicketVO savedTicketVO = updateTicketData(ticketVO,user);
				if(savedTicketVO.getTicketId()!=null ){
					LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
					LOGGER.info("Status of the incident updated to : " +  ticketVO.getStatus());
					LOGGER.info("Incident Modified by : " +  ticketVO.getModifiedBy());
					savedTicketVO.setStatusCode(200);
					savedTicketVO.setMessage("Updated");
				}else{
					LOGGER.info("Unable to update incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			LOGGER.info("Incident updated for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
		}
		
		LOGGER.info("Exit -- IncidentDAO -- saveOrUpdateIncident-- Save Incident details using Procedure: ");
		return ticketVO;

	}
	
	private TicketVO insertSPTicketData(TicketVO ticketVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_SP_TICKET_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	    	ps.setString(1, ticketVO.getTicketNumber());
            ps.setString(2, ticketVO.getTicketTitle());
            ps.setString(3, ticketVO.getDescription());
            ps.setLong(4, ticketVO.getStatusId());
            ps.setLong(5,  ticketVO.getCategoryId());
            ps.setLong(6, ticketVO.getSiteId());
            ps.setLong(7, ticketVO.getAssetId());
            ps.setLong(8, ticketVO.getAssetCategoryId());
            ps.setLong(9, ticketVO.getSubCategoryId1());
            ps.setLong(10, ticketVO.getSubCategoryId2());
            ps.setString(11, ticketVO.getPriorityDescription());
            ps.setLong(12, ticketVO.getAssignedTo());
            ps.setString(13, ticketVO.getRspAssignedAgent());
            ps.setString(14, ticketVO.getTicketStartTime());
            ps.setString(15,  user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer ticket {} with id {}.", ticketVO.getTicketNumber(), key.getKey());
	    ticketVO.setTicketId(key.getKey().longValue());
	    String updateSLa = updateSlaDueDate(ticketVO.getTicketNumber(), ticketVO.getDuration(), ticketVO.getUnit(), "SP");
	    ticketVO.setSla(updateSLa);
	    return ticketVO;
	}
	private TicketVO updateTicketData(TicketVO ticketVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		 jdbcTemplate.update(new PreparedStatementCreator() {
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.UPDATE_CUST_TICKET_QUERY);
	            ps.setString(1, ticketVO.getTicketTitle());
	            ps.setString(2, ticketVO.getDescription());
	            ps.setString(3,  ApplicationUtil.makeSQLDateFromString(ticketVO.getSla()));
	            ps.setLong(4,  ticketVO.getCategoryId());
	            ps.setLong(5, ticketVO.getStatusId());
	            ps.setString(6, ticketVO.getPriorityDescription());
	            ps.setLong(7, ticketVO.getStatusId().intValue()==15?ticketVO.getCloseCode():0l);
	            ps.setString(8, StringUtils.isEmpty(ticketVO.getCloseNote())==true?null:ticketVO.getCloseNote());
	            ps.setDate(9, ticketVO.getStatusId().intValue()==13?ApplicationUtil.getSqlDate(new Date()):null);
	            ps.setInt(10, ticketVO.getIsRootcauseResolved());
	            ps.setDate(11, ticketVO.getStatusId().intValue()==15?ApplicationUtil.getSqlDate(new Date()):null);
	            ps.setString(12, ticketVO.getStatusId().intValue()==13?user.getUsername():ticketVO.getClosedBy()); //closed by if status is closed
	            ps.setString(13,  user.getUsername());//Modified by username
	            ps.setDate(14,  ApplicationUtil.getSqlDate(new Date()));//Modified date
	            ps.setLong(15, ticketVO.getTicketId());
		        return ps;
		      }
		    });
		 	LOGGER.info("Update customer ticket {} with id {}.", ticketVO.getTicketNumber(), ticketVO.getTicketId());
		    ticketVO.setTicketId(ticketVO.getTicketId());
		    ticketVO.setModifiedBy(user.getUsername());
		    return ticketVO;
	}
	private TicketVO insertTicketData(TicketVO ticketVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_CUST_TICKET_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	    	ps.setString(1, ticketVO.getTicketNumber());
            ps.setString(2, ticketVO.getTicketTitle());
            ps.setString(3, ticketVO.getDescription());
            ps.setLong(4, ticketVO.getStatusId());
            ps.setLong(5,  ticketVO.getCategoryId());
            ps.setLong(6, ticketVO.getSiteId());
            ps.setLong(7, ticketVO.getAssetId());
            ps.setLong(8, ticketVO.getAssetCategoryId());
            ps.setLong(9, ticketVO.getSubCategoryId1());
            ps.setLong(10, ticketVO.getSubCategoryId2());
            ps.setString(11, ticketVO.getPriorityDescription());
            ps.setLong(12, ticketVO.getAssignedTo());
            ps.setString(13, ticketVO.getTicketStartTime());
            ps.setString(14,  user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer ticket {} with id {}.", ticketVO.getTicketNumber(), key.getKey());
	    ticketVO.setTicketId(key.getKey().longValue());
	    String updateSLa = updateSlaDueDate(ticketVO.getTicketNumber(), ticketVO.getDuration(), ticketVO.getUnit(), "CUST");
	    ticketVO.setSla(updateSLa);
	    return ticketVO;
	}
	public String updateSlaDueDate(String ticketNumber, Integer slaDuration, String slaUnit, String ticketType) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("usp_update_sla_duedate");
		 SqlParameterSource in = new MapSqlParameterSource()
				 .addValue("p_ticket_number", ticketNumber)
				 .addValue("p_sla_duration", slaDuration)
				 .addValue("p_sla_unit", slaUnit)
				 .addValue("ticket_type", ticketType)
		 		 .addValue("out_sla_due_date", "@out_sla_due_date");
		 
	      Map<String, Object> out = jdbcCall.execute(in);
	      String slaDueDate = (String)out.get("out_sla_due_date");
	      return slaDueDate;
	      
	}
	
	public int insertOrUpdateTicketAttachments(List<TicketAttachment> ticketAttachments) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate(AppConstants.INSERT_TICKET_ATTACHMENT_QUERY , 
                new BatchPreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                    	TicketAttachment attachement = ticketAttachments.get(i);
                    		ps.setLong(1, attachement.getTicketId());
                    		ps.setString(2, attachement.getTicketNumber());
                    		ps.setString(3, attachement.getAttachmentPath());
                    		ps.setString(4, attachement.getCreatedBy());
                    }

                    @Override
                    public int getBatchSize() {
                        return ticketAttachments.size();
                    }
                });
        return insertedRows.length;
	}
	public ServiceProvider getTicketServiceProvider(TicketVO customerTicket) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProvider serviceProvider = jdbcTemplate.query(AppConstants.EXTERNAL_SERVICE_PROVIDER_QUERY,new Object[]{customerTicket.getAssignedTo()}, new ResultSetExtractor<ServiceProvider>(){
			@Override
			public ServiceProvider extractData(ResultSet rs) throws SQLException, DataAccessException {
				ServiceProvider  sp = new ServiceProvider();
				if(rs.next()){
					sp.setServiceProviderId(rs.getLong("sp_id"));
					sp.setSpUsername(rs.getString("sp_username"));
					sp.setAccessKey(rs.getString("access_key"));
					sp.setHelpDeskEmail(rs.getString("help_desk_email"));
					sp.setName(rs.getString("sp_name"));
					sp.setSlaDescription(rs.getString("sla_description"));
				}
				return sp;
			}
		});
		return serviceProvider;
	}
	public SelectedTicketVO getSelectedTicket(Long ticketId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SelectedTicketVO selectedTicketVO = (SelectedTicketVO) jdbcTemplate.queryForObject(AppConstants.TICKET_SELECTED_QUERY, new Object[] { ticketId }, new BeanPropertyRowMapper(SelectedTicketVO.class));
		return selectedTicketVO;
	}
	public List<TicketAttachment> getTicketAttachments(Long ticketId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketAttachment> ticketAttachments = jdbcTemplate.query(AppConstants.TICKET_ATTACHMENTS,new Object[]{ticketId}, new ResultSetExtractor<List<TicketAttachment>>(){
			@Override
			public List<TicketAttachment> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketAttachment> attachments = new ArrayList<TicketAttachment>();
				while(rs.next()){
					TicketAttachment ticketAttachment = new TicketAttachment();
					ticketAttachment.setAttachmentId(rs.getLong("id"));
					ticketAttachment.setTicketNumber(rs.getString("ticket_number"));
					ticketAttachment.setAttachmentPath(rs.getString("attachment_path"));
					attachments.add(ticketAttachment);
				}
				return attachments;
			}
		
		});
		return ticketAttachments;
	}
	public TicketComment saveTicketComment(TicketComment ticketComment, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_TICKET_COMMENT_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, ticketComment.getTicketId());
	        ps.setString(2, ticketComment.getCustTicketNumber());
	    	ps.setString(3, ticketComment.getComment());
            ps.setString(4,  user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer ticket comment {} with id {}.", ticketComment.getCustTicketNumber(), key.getKey());
	    ticketComment.setCommentId(key.getKey().longValue());
		return ticketComment;
	}
	
	public List<TicketCommentVO> getTicketComments(Long ticketId){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketCommentVO> ticketComments = jdbcTemplate.query(AppConstants.TICKET_COMMENTS,new Object[]{ticketId}, new ResultSetExtractor<List<TicketCommentVO>>(){
			@Override
			public List<TicketCommentVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketCommentVO> comments = new ArrayList<TicketCommentVO>();
				while(rs.next()){
					TicketCommentVO comment = new TicketCommentVO();
					comment.setCommentId(rs.getLong("comment_id"));
					comment.setComment(rs.getString("comment"));
					comment.setCreatedBy(rs.getString("created_by"));
					comment.setCreatedDate(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_date")));
					comments.add(comment);
				}
				return comments;
			}
		});
		return ticketComments;
		
	}
	public List<TicketHistoryVO> getTicketHistory(String ticketNumber) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketHistoryVO> ticketHistory = jdbcTemplate.query(AppConstants.TICKET_HISTORY,new Object[]{ticketNumber}, new ResultSetExtractor<List<TicketHistoryVO>>(){
			@Override
			public List<TicketHistoryVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketHistoryVO> historyList = new ArrayList<TicketHistoryVO>();
				while(rs.next()){
					TicketHistoryVO history = new TicketHistoryVO();
					history.setHistoryId(rs.getLong("history_id"));
					history.setTicketNumber(rs.getString("ticket_number"));
					history.setAction(rs.getString("action").charAt(0));
					history.setMessage(rs.getString("message"));
					history.setColumnName(rs.getString("column_name"));
					SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
					history.setTimeStamp(simpleDateFormat.format(rs.getDate("ts")));
					history.setValueBefore(rs.getString("value_before"));
					history.setValueAfter(rs.getString("value_after"));
					history.setWho(rs.getString("who"));
					historyList.add(history);
				}
				return historyList;
			}
		});
		return ticketHistory;
	}
	public List<CustomerSPLinkedTicketVO> findByCustTicketIdAndDelFlag(Long custTicketId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerSPLinkedTicketVO> spLinkedTickets = jdbcTemplate.query(AppConstants.EXT_SP_LINKED_TICKETS,new Object[]{custTicketId}, new ResultSetExtractor<List<CustomerSPLinkedTicketVO>>(){
			@Override
			public List<CustomerSPLinkedTicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<CustomerSPLinkedTicketVO> linkedTickets = new ArrayList<CustomerSPLinkedTicketVO>();
				while(rs.next()){
					CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
					customerSPLinkedTicketVO.setId(rs.getLong("id"));
					customerSPLinkedTicketVO.setSpLinkedTicket(rs.getString("spticket_no"));
					customerSPLinkedTicketVO.setCustTicketId(rs.getString("cust_ticket_id"));
					customerSPLinkedTicketVO.setCustTicketNumber(rs.getString("customer_ticket_no"));
					customerSPLinkedTicketVO.setClosedFlag(rs.getString("closed_flag"));
					linkedTickets.add(customerSPLinkedTicketVO);
				}
				return linkedTickets;
			}
			
		});
		return spLinkedTickets;
	}
	
	public List<TicketVO> findRelatedTickets(Long custTicketId, Long siteId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.RELATED_TICKETS_QUERY,new Object[]{custTicketId, siteId}, new ResultSetExtractor<List<TicketVO>>(){
			@Override
			public List<TicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketVO> relatedTickets = new ArrayList<TicketVO>();
				while(rs.next()){
					TicketVO tckt = new TicketVO();
					tckt.setTicketId(rs.getLong("id"));
					tckt.setTicketNumber(rs.getString("ticket_number"));
					tckt.setSiteId(rs.getLong("site_id"));
					tckt.setSiteName(rs.getString("site_name")); 
					tckt.setAssetName(rs.getString("asset_name"));
					tckt.setTicketTitle(rs.getString("ticket_title"));
					tckt.setCreatedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
					tckt.setSla(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate")));
					tckt.setStatus(rs.getString("status"));
					tckt.setAssignedSP(rs.getString("sp_name"));
					relatedTickets.add(tckt);
				}
				return relatedTickets;
			}
			
		});
		return ticketList;
	}
	public List<TicketVO> findSPRelatedTickets(Long ticketId, Long siteId, Long spId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.SP_RELATED_TICKETS_QUERY,new Object[]{ticketId, siteId, spId}, new ResultSetExtractor<List<TicketVO>>(){
			@Override
			public List<TicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketVO> relatedTickets = new ArrayList<TicketVO>();
				while(rs.next()){
					TicketVO tckt = new TicketVO();
					tckt.setTicketId(rs.getLong("id"));
					tckt.setTicketNumber(rs.getString("ticket_number"));
					tckt.setSiteId(rs.getLong("site_id"));
					tckt.setSiteName(rs.getString("site_name")); 
					tckt.setAssetName(rs.getString("asset_name"));
					tckt.setTicketTitle(rs.getString("ticket_title"));
					tckt.setCreatedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
					tckt.setSla(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate")));
					tckt.setStatus(rs.getString("status"));
					tckt.setAssignedSP(rs.getString("sp_name"));
					relatedTickets.add(tckt);
				}
				return relatedTickets;
			}
			
		});
		return ticketList;
	}
	public CustomerSPLinkedTicketVO saveLinkedTicket(CustomerSPLinkedTicketVO customerSPLinkedTicketVO, LoginUser user ){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_TICKET_MAPPING_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	    	ps.setLong(1, Long.parseLong(customerSPLinkedTicketVO.getCustTicketId()));
            ps.setString(2, customerSPLinkedTicketVO.getCustTicketNumber());
            ps.setString(3, customerSPLinkedTicketVO.getSpLinkedTicket());
            ps.setString(4, customerSPLinkedTicketVO.getClosedFlag());
            ps.setString(5,  StringUtils.isEmpty(customerSPLinkedTicketVO.getCloseTime())==true?null:customerSPLinkedTicketVO.getCloseTime());
            ps.setString(6,  user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer linked ticket {} with id {}.", customerSPLinkedTicketVO.getSpLinkedTicket(), key.getKey());
	    customerSPLinkedTicketVO.setId(key.getKey().longValue());
	    return customerSPLinkedTicketVO;
	}
	
	public int changeLinkedTicketStatus(Long linkedTicket, LoginUser user) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRow = jdbcTemplate.update(AppConstants.STATUS_CHANGED_LINKED_TICKET, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
	            ps.setLong(2, linkedTicket);
			}
		});
		return updatedRow;
	}
	
	public int deleteLinkedTicket(Long linkedTicket, LoginUser user) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRow = jdbcTemplate.update(AppConstants.DELETE_LINKED_TICKET, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
	            ps.setLong(2, linkedTicket);
			}
		});
		return updatedRow;
	}
	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_TICKET_ESCALATION_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, ticketEscalationLevel.getTicketId());
            ps.setString(2,ticketEscalationLevel.getTicketNumber());
            ps.setString(3,  "Level " +ticketEscalationLevel.getEscLevelDesc());
            ps.setString(4, user.getUsername());
            ps.setLong(5,  ticketEscalationLevel.getEscId());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer escalation for ticket {} with id {}.", ticketEscalationLevel.getTicketNumber(), key.getKey());
	    ticketEscalationLevel.setCustEscId(key.getKey().longValue());
	    ticketEscalationLevel.setEscalationStatus("Escalated");
	    return ticketEscalationLevel;
	}
	public List<TicketEscalationVO> getAllEscalations(Long ticketId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketEscalationVO> ticketList = jdbcTemplate.query(AppConstants.TICKET_ESCALATIONS,new Object[]{ticketId}, new ResultSetExtractor<List<TicketEscalationVO>>(){
			@Override
			public List<TicketEscalationVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<TicketEscalationVO> escTickets = new ArrayList<TicketEscalationVO>();
				while(rs.next()){
					TicketEscalationVO ticketEscalationVO = new TicketEscalationVO();
					ticketEscalationVO.setCustEscId(rs.getLong("ct_esc_id"));
					ticketEscalationVO.setTicketId(rs.getLong("ticket_id"));
					ticketEscalationVO.setTicketNumber(rs.getString("ticket_number"));
					ticketEscalationVO.setEscLevelDesc(rs.getString("esc_level"));
					ticketEscalationVO.setEscalatedBy(rs.getString("escalated_by"));
					escTickets.add(ticketEscalationVO);
				}
				return escTickets;
			}
			
		});
		return ticketList;
	}
	public TicketEscalationVO findByTicketIdAndEscLevelId(Long ticketId, Long escId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		TicketEscalationVO escVO = jdbcTemplate.query(AppConstants.TICKET_BY_ESCID,new Object[]{ticketId, escId}, new ResultSetExtractor<TicketEscalationVO>(){
			@Override
			public TicketEscalationVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				TicketEscalationVO ticketEscalationVO = new TicketEscalationVO();
				if(rs.next()){
					ticketEscalationVO.setCustEscId(rs.getLong("ct_esc_id"));
					ticketEscalationVO.setTicketId(rs.getLong("ticket_id"));
					ticketEscalationVO.setTicketNumber(rs.getString("ticket_number"));
					ticketEscalationVO.setEscLevelDesc(rs.getString("esc_level"));
					ticketEscalationVO.setEscalatedBy(rs.getString("escalated_by"));
				}
				return ticketEscalationVO;
			  }
			});
		return escVO;
	}
	public List<EscalationLevelVO> getSPEscalation(Long spAssignedTo, LoginUser loginUser) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<EscalationLevelVO> escList = jdbcTemplate.query(AppConstants.SP_ESCALATIONS_QUERY,new Object[]{spAssignedTo}, new ResultSetExtractor<List<EscalationLevelVO>>(){
			@Override
			public List<EscalationLevelVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<EscalationLevelVO> escVals = new ArrayList<EscalationLevelVO>();
				while(rs.next()){
					EscalationLevelVO ticketEscalationVO = new EscalationLevelVO();
					ticketEscalationVO.setEscId(rs.getLong("esc_id"));
					ticketEscalationVO.setServiceProdviderId(rs.getLong("sp_id"));
					ticketEscalationVO.setEscalationLevel(rs.getString("esc_level"));
					ticketEscalationVO.setEscalationEmail(rs.getString("esc_email"));
					ticketEscalationVO.setEscalationPerson(rs.getString("esc_person"));
					escVals.add(ticketEscalationVO);
				}
				return escVals;
			}
		});
		return escList;
	}
	
	public List<Financials> getTicketFinancials(Long ticketId, LoginUser loginUser) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Financials> financialVOList = jdbcTemplate.query(AppConstants.TICKET_FINANCE_SELECT_QUERY,new Object[]{ticketId}, new ResultSetExtractor<List<Financials>>(){
			@Override
			public List<Financials> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Financials> financeList = new ArrayList<Financials>();
				while(rs.next()){
					Financials financeVO = new Financials();
					financeVO.setId(rs.getLong("cost_id"));
					financeVO.setTicketId(rs.getLong("ticket_id"));
					financeVO.setCostName(rs.getString("cost_name"));
					financeVO.setCost(new BigDecimal(rs.getString("cost")));
					financeVO.setChargeBack(rs.getString("charge_back"));
					financeVO.setBillable(rs.getString("billable"));
					financeVO.setCreatedBy(rs.getString("created_by"));
					financeList.add(financeVO);
				}
				return financeList;
			}
		});
		return financialVOList == null?Collections.emptyList():financialVOList;
	}
	
	public Financials getTicketFinanceById(Long costId, LoginUser loginUser) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Financials financeObj = jdbcTemplate.query(AppConstants.TICKET_FINANCE_BY_ID,new Object[]{costId}, new ResultSetExtractor<Financials>(){
			@Override
			public Financials extractData(ResultSet rs) throws SQLException, DataAccessException {
				Financials financeVO = new Financials();
				if(rs.next()){
					financeVO.setId(rs.getLong("cost_id"));
					financeVO.setTicketId(rs.getLong("ticket_id"));
					financeVO.setCostName(rs.getString("cost_name"));
					financeVO.setCost(new BigDecimal(rs.getString("cost")));
					financeVO.setChargeBack(rs.getString("charge_back"));
					financeVO.setBillable(rs.getString("billable"));
					financeVO.setCreatedBy(rs.getString("created_by"));
				}
				return financeVO;
			}
		});
		return financeObj ;
	}

	public Financials saveFinance(Financials financialVO, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.TICKET_FINANCE_INSERT_QUERY, 
	            Statement.RETURN_GENERATED_KEYS);
	        ps.setLong(1, financialVO.getTicketId());
    		ps.setString(2, financialVO.getCostName());
    		ps.setDouble(3, financialVO.getCost().doubleValue());
    		ps.setString(4, financialVO.getChargeBack());
    		ps.setString(5, financialVO.getBillable());
    		ps.setString(6, user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved Finance ticket {} with id {}.", financialVO.getCost(), key.getKey());
	    financialVO.setId(key.getKey().longValue());
	    return financialVO;
    }
	
	public Financials updateFinance(Financials financeVO, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int updatedRows = jdbcTemplate.update(AppConstants.TICKET_FINANCE_UPDATE_QUERY,     new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                		ps.setString(1, financeVO.getCostName());
                		ps.setDouble(2, financeVO.getCost().doubleValue());
                		ps.setString(3, financeVO.getChargeBack());
                		ps.setString(4, financeVO.getBillable());
                		ps.setString(5, user.getUsername());
                		ps.setLong(6, financeVO.getId());
                }
                });
        return updatedRows>0?financeVO:null;
    }
	public List<TicketVO> findTicketsAssignedToSP(Long spId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.EXTSP_TICKET_LIST_QUERY, new Object[]{spId} ,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	public boolean deleteFinanceCostById(Long costId, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRow = jdbcTemplate.update(AppConstants.DELETE_TICKET_FINANCE_BY_ID,new Object[]{costId});
		if(updatedRow>0){
			return true;
		}else{
			return false;
		}
		
	}
	
}
