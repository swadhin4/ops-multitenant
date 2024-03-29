package com.pms.app.dao.impl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
import com.pms.app.constants.RSPCustomerConstants;
import com.pms.app.view.vo.CustomerSPLinkedTicketVO;
import com.pms.app.view.vo.EscalationLevelVO;
import com.pms.app.view.vo.IncidentTask;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.SelectedTicketVO;
import com.pms.app.view.vo.ServiceProviderVO;
import com.pms.app.view.vo.TicketCommentVO;
import com.pms.app.view.vo.TicketEscalationVO;
import com.pms.app.view.vo.TicketHistoryVO;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Financials;
import com.pms.jpa.entities.IncidentReport;
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
	public List<TicketVO> findTicketsBySiteIdIn(Set<Long> siteIdList, String assignedTo) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketList = new ArrayList<TicketVO>();
		if(assignedTo.equalsIgnoreCase("EXT")){
			MapSqlParameterSource parameters = new MapSqlParameterSource();
			parameters.addValue("siteIds", siteIdList);
			 ticketList = jdbcTemplate.query(AppConstants.CUST_EXT_TICKET_LIST_QUERY, parameters,
					new RowMapper<TicketVO>() {
						@Override
						public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
							return toTicketList(rs);
						}
			});
		}
		else if(assignedTo.equalsIgnoreCase("RSP")){
		 ticketList = jdbcTemplate.query(AppConstants.CUST_RSP_TICKET_LIST_QUERY,
					new RowMapper<TicketVO>() {
						@Override
						public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
							return toTicketList(rs);
						}
			});
		}
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
		/*DateFormat df = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");*/
		TicketVO ticketVO = new TicketVO();
		ticketVO.setTicketId(rs.getLong("id"));
		ticketVO.setTicketNumber(rs.getString("ticket_number"));
		ticketVO.setTicketTitle(rs.getString("ticket_title"));
		ticketVO.setDescription(rs.getString("ticket_desc"));
		ticketVO.setSiteId(rs.getLong("site_id"));
		ticketVO.setSiteName(rs.getString("site_name"));
		ticketVO.setAssetId(rs.getLong("asset_id"));
		ticketVO.setAssetModel(rs.getString("model_number"));
		ticketVO.setAssetName(rs.getString("asset_name"));
		ticketVO.setRaisedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
		String slaDueDate = ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate"));
		ticketVO.setSla(slaDueDate);
		ticketVO.setAssignedTo(rs.getLong("sp_id"));
		ticketVO.setAssignedSP(rs.getString("sp_name"));
		ticketVO.setStatus(rs.getString("status"));
		ticketVO.setStatusDescription(rs.getString("description"));
		ticketVO.setStatusId(rs.getLong("status_id"));
		ticketVO.setRaisedBy(rs.getString("created_by"));
		return ticketVO;
	}
	public TicketPrioritySLAVO getSPSLADetails(Long serviceProviderID, Long ticketCategoryId, String spType, String userType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		String spTicketPriorityQuery=null;
		if(userType.equalsIgnoreCase("SP") && !spType.equalsIgnoreCase("EXTCUST")){
			spTicketPriorityQuery = AppConstants.TICKET_PRIORITY_RSP_SLA_QUERY;
		}
		else if(userType.equalsIgnoreCase("SP") && spType.equalsIgnoreCase("EXTCUST")){
			spTicketPriorityQuery = RSPCustomerConstants.TICKET_PRIORITY_RSP_EXTCUST_SLA_QUERY;
		}
		else if(userType.equalsIgnoreCase("USER") && spType.equalsIgnoreCase("RSP")){
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
	public Long getLastIncidentCreated(String criteriea, String ticketAssignedType) {
		String lastIncidentQuery=null;
		if(criteriea.equalsIgnoreCase("USER")){
			lastIncidentQuery = AppConstants.LAST_INCIDENT_NUMBER_QUERY;
		}
		else if(criteriea.equalsIgnoreCase("SP") && !ticketAssignedType.equalsIgnoreCase("EXTCUST")){
			lastIncidentQuery = AppConstants.LAST_SP_INCIDENT_NUMBER_QUERY;
		}
		else if(criteriea.equalsIgnoreCase("SP") && ticketAssignedType.equalsIgnoreCase("EXTCUST")){
			lastIncidentQuery = RSPCustomerConstants.LAST_SP_EXTCUST_INCIDENT_NUMBER_QUERY;
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
			TicketVO savedTicketVO =null;
			try {
				if(user.getUserType().equalsIgnoreCase("USER")){
					savedTicketVO = insertTicketData(ticketVO,user);
				}else if(user.getUserType().equalsIgnoreCase("SP") && !ticketVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
					// Verify the Ticket Creator of SP company or SP Agent Id for rassignedto Column
					ServiceProviderVO spCompany = getRegisteredSPDetails(user.getCompany().getCompanyCode());
					ticketVO.setAssignedTo(spCompany.getServiceProviderId());
					if(ticketVO.getTicketAssignedType().equalsIgnoreCase("RSP")){
						savedTicketVO = insertSPTicketData(ticketVO,user, AppConstants.INSERT_SP_TICKET_QUERY, "SP");
						savedTicketVO.setTicketAssignedType("RSP");
					}
					if(ticketVO.getTicketAssignedType().equalsIgnoreCase("CUSTOMER")){
						savedTicketVO = insertSPTicketData(ticketVO,user, AppConstants.INSERT_SP_TICKET_QUERY, "SP");
						savedTicketVO.setTicketAssignedType("CUSTOMER");
					}
				}
				else if(user.getUserType().equalsIgnoreCase("SP") && ticketVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
						ticketVO.setAssignedTo(ticketVO.getAssignedTo());
						savedTicketVO = insertSPTicketData(ticketVO,user, RSPCustomerConstants.INSERT_SP_EXTCUST_TICKET_QUERY, "EXTCUST");
						savedTicketVO.setTicketAssignedType("EXTCUST");
				}
				if(savedTicketVO.getTicketId()!=null ){
					LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
					savedTicketVO.setStatusCode(200);
					savedTicketVO.setMessage("Created");
				}else{
					LOGGER.info("Unable to create incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
				}
			} catch (Exception e) {
				LOGGER.error("Exception caused in ticket creation - saveOrUpdateIncident()" );
				e.printStackTrace();
				savedTicketVO.setStatusCode(500);
				savedTicketVO.setMessage("Error in ticket creation");
			}
		
		}else if(ticketVO.getTicketId()!=null){
			TicketVO savedTicketVO = null;
			try {
				if(user.getUserType().equalsIgnoreCase("USER") || user.getUserType().equalsIgnoreCase("EXTSP")){
						savedTicketVO = updateTicketData(ticketVO,user);
					}else if(user.getUserType().equalsIgnoreCase("SP") && ticketVO.getTicketAssignedType().equalsIgnoreCase("RSP")){
						savedTicketVO = updateSPTicketData(ticketVO,user);
					}
					else if(user.getUserType().equalsIgnoreCase("SP") && ticketVO.getTicketAssignedType().equalsIgnoreCase("CUSTOMER")){
						savedTicketVO = updateTicketData(ticketVO,user);
					}
					else if(user.getUserType().equalsIgnoreCase("SP") && ticketVO.getTicketAssignedType().equalsIgnoreCase("EXTCUST")){
						ticketVO.setAssignedTo(ticketVO.getAssignedTo());
						savedTicketVO = updateTicketData(ticketVO,user);
						savedTicketVO.setTicketAssignedType("EXTCUST");
				}
				if(savedTicketVO.getTicketId()!=null ){
					LOGGER.info("Status of the incident updated to : " +  ticketVO.getStatus());
					LOGGER.info("Incident Modified by : " +  ticketVO.getModifiedBy());
					savedTicketVO.setStatusCode(200);
					savedTicketVO.setMessage("Updated");
					LOGGER.info("Incident updated for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
				}else{
					LOGGER.info("Unable to update incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
				}
			} catch (Exception e) {
				LOGGER.error("Exception caused in ticket updation - saveOrUpdateIncident()" );
				savedTicketVO.setStatusCode(500);
				savedTicketVO.setMessage("Unable to update ticket");
				e.printStackTrace();
			}
			
		}
		
		LOGGER.info("Exit -- IncidentDAO -- saveOrUpdateIncident-- Save Incident details : ");
		return ticketVO;

	}
	
	private TicketVO insertSPTicketData(TicketVO ticketVO, LoginUser user, final String ticketCreationQuery, final String ticketType) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(ticketCreationQuery, 
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
	    String updateSLa = updateSlaDueDate(ticketVO.getTicketNumber(), ticketVO.getDuration(), ticketVO.getUnit(), ticketType);
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
	            if(ticketVO.getStatusId().intValue()==15){
	            	 ps.setLong(7, ticketVO.getCloseCode());
	            	  ps.setDate(11, ApplicationUtil.getSqlDate(new Date()));
	            }
	            else{
	            	ps.setNull(7, Types.NULL);
	            	 ps.setNull(11, Types.NULL);
	            }
	            
	            if(StringUtils.isEmpty(ticketVO.getCloseNote())){
	            	ps.setNull(8, Types.NULL);
	            }
	            else{
	            	  ps.setString(8, ticketVO.getCloseNote());
	            }
	            if(ticketVO.getStatusId().intValue()==13){
	            	 ps.setDate(9,ApplicationUtil.getSqlDate(new Date()));
	            }
	            else{
	            	ps.setNull(9, Types.NULL);
	            }
	            ps.setInt(10, ticketVO.getIsRootcauseResolved());
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
	
	private TicketVO updateSPTicketData(TicketVO ticketVO, LoginUser user){

		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		 jdbcTemplate.update(new PreparedStatementCreator() {
		      @Override
		      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
		        final PreparedStatement ps = connection.prepareStatement(AppConstants.UPDATE_RSP_TICKET_QUERY);
	            ps.setString(1, ticketVO.getTicketTitle());
	            ps.setString(2, ticketVO.getDescription());
	            ps.setString(3,  ApplicationUtil.makeSQLDateFromString(ticketVO.getSla()));
	            ps.setLong(4,  ticketVO.getCategoryId());
	            ps.setLong(5, ticketVO.getStatusId());
	            ps.setString(6, ticketVO.getPriorityDescription());
	            if(ticketVO.getStatusId().intValue()==15){
	            	 ps.setLong(7, ticketVO.getCloseCode());
	            	  ps.setDate(11, ApplicationUtil.getSqlDate(new Date()));
	            }
	            else{
	            	ps.setNull(7, Types.NULL);
	            	 ps.setNull(11, Types.NULL);
	            }
	            
	            if(StringUtils.isEmpty(ticketVO.getCloseNote())){
	            	ps.setNull(8, Types.NULL);
	            }
	            else{
	            	  ps.setString(8, ticketVO.getCloseNote());
	            }
	            if(ticketVO.getStatusId().intValue()==13){
	            	 ps.setDate(9,ApplicationUtil.getSqlDate(new Date()));
	            }
	            else{
	            	ps.setNull(9, Types.NULL);
	            }
	            ps.setInt(10, ticketVO.getIsRootcauseResolved());
	            ps.setString(12, ticketVO.getStatusId().intValue()==13?user.getUsername():ticketVO.getClosedBy()); //closed by if status is closed
	            ps.setString(13,  user.getUsername());//Modified by username
	            ps.setDate(14,  ApplicationUtil.getSqlDate(new Date()));//Modified date
	            ps.setLong(15, ticketVO.getTicketId());
		        return ps;
		      }
		    });
		 	LOGGER.info("Update RSP ticket {} with id {}.", ticketVO.getTicketNumber(), ticketVO.getTicketId());
		    ticketVO.setTicketId(ticketVO.getTicketId());
		    ticketVO.setModifiedBy(user.getUsername());
		    return ticketVO;
	
	}
	private TicketVO insertTicketData(TicketVO ticketVO, LoginUser user) {
		LOGGER.info("Tickey creating for :"+ user.getUsername() + " for asset SP type : "+ ticketVO.getAsstSpType());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	    	  String ticketQuery = AppConstants.INSERT_CUST_TICKET_QUERY;
	    	  if(ticketVO.getAsstSpType().equalsIgnoreCase("RSP")){
	    		  ticketQuery = AppConstants.INSERT_CUST_TICKET_RSP_ASSIGNED_QUERY;
	    	  }
	    	 
	        final PreparedStatement ps = connection.prepareStatement(ticketQuery, 
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
	
	public int insertOrUpdateTicketAttachments(List<TicketAttachment> ticketAttachments, final String ticketAttachmentQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int[] insertedRows = jdbcTemplate.batchUpdate(ticketAttachmentQuery , 
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
	public SelectedTicketVO getSelectedTicket(Long ticketId ) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SelectedTicketVO selectedTicketVO = (SelectedTicketVO) jdbcTemplate.queryForObject(AppConstants.TICKET_SELECTED_QUERY, new Object[] { ticketId }, new BeanPropertyRowMapper(SelectedTicketVO.class));
		return selectedTicketVO;
	}
	public SelectedTicketVO getRSPSelectedTicket(Long ticketId, final String ticketAssignedType ) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SelectedTicketVO selectedTicketVO = null;
		if(ticketAssignedType.equalsIgnoreCase("EXTCUST")){
			selectedTicketVO = (SelectedTicketVO) jdbcTemplate.queryForObject(RSPCustomerConstants.RSP_EXTCUST_TICKET_SELECTED_QUERY, new Object[] { ticketId }, new BeanPropertyRowMapper(SelectedTicketVO.class));
		}else{
			selectedTicketVO = (SelectedTicketVO) jdbcTemplate.queryForObject(AppConstants.RSP_TICKET_SELECTED_QUERY, new Object[] { ticketId }, new BeanPropertyRowMapper(SelectedTicketVO.class));
		}
		return selectedTicketVO;
	}
	public List<TicketAttachment> getTicketAttachments(Long ticketId, final String ticketAttachmentQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketAttachment> ticketAttachments = jdbcTemplate.query(ticketAttachmentQuery,new Object[]{ticketId}, new ResultSetExtractor<List<TicketAttachment>>(){
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
	public TicketComment saveTicketComment(TicketComment ticketComment, LoginUser user, final String insertTicketCommentQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(insertTicketCommentQuery, 
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
	
	public List<TicketCommentVO> getTicketComments(Long ticketId,  final String listTicketCommentQuery){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketCommentVO> ticketComments = jdbcTemplate.query(listTicketCommentQuery,new Object[]{ticketId}, new ResultSetExtractor<List<TicketCommentVO>>(){
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
					customerSPLinkedTicketVO.setSpType(rs.getString("sptype"));
					if(StringUtils.isNotEmpty(String.valueOf(rs.getLong("rsp_ticket_id")))){
						customerSPLinkedTicketVO.setRspTicketId(String.valueOf(rs.getLong("rsp_ticket_id")));
						customerSPLinkedTicketVO.setStatusId(rs.getLong("status_id"));
						customerSPLinkedTicketVO.setLinkedTicketStatus(rs.getString("description"));
					}
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
					tckt.setStatusId(rs.getLong("status_id"));
					tckt.setStatus(rs.getString("status"));
					tckt.setAssignedSP(rs.getString("sp_name"));
					relatedTickets.add(tckt);
				}
				return relatedTickets;
			}
			
		});
		return ticketList;
	}
	public List<TicketVO> findSPRelatedTickets(Long ticketId, Long siteId, Long spId, final String ticketRelatedQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketList = jdbcTemplate.query(ticketRelatedQuery,new Object[]{ticketId, siteId, spId}, new ResultSetExtractor<List<TicketVO>>(){
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
					tckt.setStatusId(rs.getLong("status_id"));
					tckt.setStatus(rs.getString("status"));
					tckt.setAssignedSP(rs.getString("sp_name"));
					relatedTickets.add(tckt);
				}
				return relatedTickets;
			}
			
		});
		return ticketList;
	}
	public CustomerSPLinkedTicketVO saveLinkedTicket(CustomerSPLinkedTicketVO customerSPLinkedTicketVO, LoginUser user, final String insertSPMappingQuery ){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(insertSPMappingQuery, 
	            Statement.RETURN_GENERATED_KEYS);
	        if(customerSPLinkedTicketVO.getSpType().equalsIgnoreCase("EXT")){
		    	ps.setLong(1, Long.parseLong(customerSPLinkedTicketVO.getCustTicketId()));
	            ps.setString(2, customerSPLinkedTicketVO.getCustTicketNumber());
	            ps.setString(3, customerSPLinkedTicketVO.getSpLinkedTicket());
	            ps.setString(4, customerSPLinkedTicketVO.getSpType());
	            ps.setString(5, customerSPLinkedTicketVO.getClosedFlag());
	            ps.setString(6,  StringUtils.isEmpty(customerSPLinkedTicketVO.getCloseTime())==true?null:customerSPLinkedTicketVO.getCloseTime());
	            ps.setString(7,  user.getUsername());
	        }
	        else if(customerSPLinkedTicketVO.getSpType().equalsIgnoreCase("RSP")){
	        	ps.setLong(1, Long.parseLong(customerSPLinkedTicketVO.getCustTicketId()));
	            ps.setString(2, customerSPLinkedTicketVO.getCustTicketNumber());
	            ps.setString(3, customerSPLinkedTicketVO.getRspTicketId());
	            ps.setString(4, customerSPLinkedTicketVO.getSpLinkedTicket());
	            ps.setString(5, customerSPLinkedTicketVO.getSpType());
	            ps.setString(6, customerSPLinkedTicketVO.getClosedFlag());
	            ps.setString(7,  StringUtils.isEmpty(customerSPLinkedTicketVO.getCloseTime())==true?null:customerSPLinkedTicketVO.getCloseTime());
	            ps.setString(8,  user.getUsername());
	        }
           
           
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved customer linked ticket {} with id {}.", customerSPLinkedTicketVO.getSpLinkedTicket(), key.getKey());
	    customerSPLinkedTicketVO.setId(key.getKey().longValue());
	    return customerSPLinkedTicketVO;
	}
	
	public CustomerSPLinkedTicketVO saveRSPLinkedTicket(CustomerSPLinkedTicketVO customerSPLinkedTicketVO, LoginUser user, final String insertSPMappingQuery ){
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(insertSPMappingQuery, 
	            Statement.RETURN_GENERATED_KEYS);
	        if(customerSPLinkedTicketVO.getLinkedTicketType().equalsIgnoreCase("CT")){
		    	ps.setLong(1, customerSPLinkedTicketVO.getRspTicketLongId());
	            ps.setLong(2, customerSPLinkedTicketVO.getLinkedCTticketId());
	            ps.setString(3, customerSPLinkedTicketVO.getSpLinkedTicket());
	            ps.setString(4, customerSPLinkedTicketVO.getLinkedTicketType());
	            ps.setString(5,  user.getUsername());
	        }
	        else if(customerSPLinkedTicketVO.getLinkedTicketType().equalsIgnoreCase("SP")){
	        	ps.setLong(1, customerSPLinkedTicketVO.getRspTicketLongId());
	            ps.setLong(2, customerSPLinkedTicketVO.getLinkedRspTicketId());
	            ps.setString(3, customerSPLinkedTicketVO.getSpLinkedTicket());
	            ps.setString(4, customerSPLinkedTicketVO.getLinkedTicketType());
	            ps.setString(5,  user.getUsername());
	        }
           
           
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
		int updatedRow = jdbcTemplate.update(AppConstants.DELETE_CUST_LINKED_TICKET, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, user.getUsername());
	            ps.setLong(2, linkedTicket);
			}
		});
		return updatedRow;
	}
	
	public int deleteRSPLinkedTicket(Long linkedTicket, LoginUser user) throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRow = jdbcTemplate.update(AppConstants.DELETE_RSP_LINKED_TICKET, new PreparedStatementSetter(){
			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
	            ps.setLong(1, linkedTicket);
			}
		});
		return updatedRow;
	}
	public TicketEscalationVO saveTicketEscalations(TicketEscalationVO ticketEscalationLevel, LoginUser user,final String spTypeEscaltionQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(spTypeEscaltionQuery, 
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
	public TicketEscalationVO findByTicketIdAndEscLevelId(Long ticketId, Long escId, final String spEscTypeQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		TicketEscalationVO escVO = jdbcTemplate.query(spEscTypeQuery,new Object[]{ticketId, escId}, new ResultSetExtractor<TicketEscalationVO>(){
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
	public List<EscalationLevelVO> getSPEscalation(Long spAssignedTo, LoginUser loginUser, final String spTicketEscQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<EscalationLevelVO> escList = jdbcTemplate.query(spTicketEscQuery,new Object[]{spAssignedTo}, new ResultSetExtractor<List<EscalationLevelVO>>(){
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
	
	public List<Financials> getTicketFinancials(Long ticketId, LoginUser loginUser, final String financeQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<Financials> financialVOList = jdbcTemplate.query(financeQuery,new Object[]{ticketId}, new ResultSetExtractor<List<Financials>>(){
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
	
	public Financials getTicketFinanceById(Long costId, LoginUser loginUser, final String financeQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Financials financeObj = jdbcTemplate.query(financeQuery,new Object[]{costId}, new ResultSetExtractor<Financials>(){
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

	public Financials saveFinance(Financials financialVO, LoginUser user, final String financeQuery)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(financeQuery, 
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
	
	public Financials updateFinance(Financials financeVO, LoginUser user, final String financeQuery)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int updatedRows = jdbcTemplate.update(financeQuery,     new PreparedStatementSetter() {
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
	public List<TicketVO> findTicketsAssignedToExternalSP(Long spId) {
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
	public boolean deleteFinanceCostById(Long costId, LoginUser user, final String financeQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		int updatedRow = jdbcTemplate.update(financeQuery,new Object[]{costId});
		if(updatedRow>0){
			return true;
		}else{
			return false;
		}
		
	}
	public List<TicketVO> findTicketsCreatedBy(String spCode) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<TicketVO> ticketVOs = jdbcTemplate.query(AppConstants.RSP_TICKET_CREATED_LIST_QUERY,
				new Object[] { spCode  }, new ResultSetExtractor<List<TicketVO>>() {
					@Override
					public List<TicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<TicketVO> ticketVOList = new ArrayList<TicketVO>();
						while (rs.next()) {
							TicketVO ticketVO = new TicketVO();
							ticketVO.setTicketId(rs.getLong("id"));
							ticketVO.setTicketNumber(rs.getString("ticket_number"));
							ticketVO.setTicketTitle(rs.getString("ticket_title"));
							ticketVO.setSiteId(rs.getLong("site_id"));
							ticketVO.setSiteName(rs.getString("site_name"));
							ticketVO.setAssetId(rs.getLong("asset_id"));
							ticketVO.setAssetName(rs.getString("asset_name"));
							ticketVO.setRaisedOn(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_on")));
							ticketVO.setSla(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("sla_duedate")));
							ticketVO.setAssignedSP(rs.getString("sp_name"));
							ticketVO.setStatusId(rs.getLong("status_id"));
							ticketVO.setStatus(rs.getString("status"));

							ticketVOList.add(ticketVO);
						}
						return ticketVOList;
					}
				});
		return ticketVOs == null ? Collections.emptyList() : ticketVOs;
	}
	
	
	public List<TicketVO> findTicketsAssignedToRSP(Long spId) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("spId", spId);
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.RSP_CUST_TICKET_LIST_QUERY, parameters,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	public ServiceProviderVO getRegisteredSPDetails(String spCode) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		ServiceProviderVO serviceProviderVO = jdbcTemplate.query(AppConstants.RSP_DETAIL_QUERY,
				new Object[] { spCode }, new ResultSetExtractor<ServiceProviderVO>() {
					@Override
					public ServiceProviderVO extractData(ResultSet rs) throws SQLException, DataAccessException {
						ServiceProviderVO serviceProviderVO = new ServiceProviderVO();
						while (rs.next()) {
							serviceProviderVO.setServiceProviderId(rs.getLong("sp_id"));
							serviceProviderVO.setCompanyCode(rs.getString("sp_code"));
							serviceProviderVO.setName(rs.getString("sp_name"));
						}
						return serviceProviderVO;
					}
				});
		return serviceProviderVO;
		
	}	
	
	public IncidentReport getIncidentReportDetails(String ticketNumber) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		IncidentReport incidentReport = jdbcTemplate.query(AppConstants.INCIDENT_REPORT_QUERY,
				new Object[] { ticketNumber }, new ResultSetExtractor<IncidentReport>() {
					@Override
					public IncidentReport extractData(ResultSet rs) throws SQLException, DataAccessException {
						IncidentReport incidentRecord = new IncidentReport();
						while (rs.next()) {
							incidentRecord.setTimeToRaiseTicket(rs.getString("time_to_raise_ticket"));;
							incidentRecord.setCustomerTicketNumber(rs.getString("customer_ticket"));
						}
						return incidentRecord;
					}
				});
		return incidentReport;
		
	}
	public List<TicketVO> findRSPSuggestedTickets(Long assetId) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("assetId", assetId);
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.RSP_TICKET_SUGGESTED_LIST_QUERY, parameters,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	
	public List<TicketVO> findCustomerSuggestedTickets(Long assetId, Long rspId) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("assetId", assetId);
		parameters.addValue("rspId", rspId);
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.CUSTOMER_TICKET_SUGGESTED_LIST_QUERY, parameters,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	
	public List<TicketVO> findRSPReferenceTickets(Long assetId, Long rspId, String parentTicketNumber) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("assetId", assetId);
		parameters.addValue("rspId", rspId);
		parameters.addValue("parentTicketNumber", parentTicketNumber);
		List<TicketVO> ticketList = jdbcTemplate.query(AppConstants.RSP_TICKET_REFERENCE_LIST_QUERY, parameters,
				new RowMapper<TicketVO>() {
					@Override
					public TicketVO mapRow(ResultSet rs, int arg1) throws SQLException {
						return toTicketList(rs);
					}
				});
		return ticketList;
	}
	
	
	public TicketVO findRSPTicket(String linkedTicket, Long rspAssignedTo) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		TicketVO ticket =  jdbcTemplate.query(AppConstants.VALIDATE_TICKET_RSPID_QUERY, new Object[] { linkedTicket,  rspAssignedTo}, new ResultSetExtractor<TicketVO>() {
			@Override
			public TicketVO extractData(ResultSet rs) throws SQLException, DataAccessException {
				TicketVO incidentRecord = new TicketVO();
				if (rs.next()) {
					incidentRecord.setTicketId(rs.getLong("id"));
					incidentRecord.setTicketNumber(rs.getString("ticket_number"));
				}
				return incidentRecord;
			}
		});
		return ticket;
		
	}
	public List<TicketAttachment> findByAttachmentIdIn(List<Long> incidentAttachementIds) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("attachmentIds", incidentAttachementIds);
		List<TicketAttachment> incidentAttachments =  jdbcTemplate.query(AppConstants.INCIDENT_ATTACHMENT_LIST,parameters, new RowMapper<TicketAttachment>() {
			@Override
			public TicketAttachment mapRow(ResultSet rs, int arg1) throws SQLException {
				TicketAttachment ticketAttachment = new TicketAttachment();
				ticketAttachment.setAttachmentId(rs.getLong("id")); 
				ticketAttachment.setTicketNumber(rs.getString("ticket_number"));
				ticketAttachment.setAttachmentPath(rs.getString("attachment_path"));
				return ticketAttachment;
			}
		});
		return incidentAttachments;
	}
	public int deleteAttachmentById(List<Long> attachmentIds) {
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(ConnectionManager.getDataSource());
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("attachmentIds", attachmentIds);
        int updatedRows = jdbcTemplate.update(AppConstants.INCIDENT_ATTACHMENT_DELETE_QUERY,parameters);
        return updatedRows;		
	}
	public EscalationLevelVO getEscalationLevelBy(Long escId, String escLevelQuery) {
			JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
			EscalationLevelVO escLevelVO = jdbcTemplate.query(escLevelQuery,new Object[]{escId}, new ResultSetExtractor<EscalationLevelVO>(){
				@Override
				public EscalationLevelVO extractData(ResultSet rs) throws SQLException, DataAccessException {
					EscalationLevelVO ticketEscalationVO = new EscalationLevelVO();
					if(rs.next()){
						
						ticketEscalationVO.setEscId(rs.getLong("esc_id"));
						ticketEscalationVO.setServiceProdviderId(rs.getLong("sp_id"));
						ticketEscalationVO.setEscalationLevel(rs.getString("esc_level"));
						ticketEscalationVO.setEscalationEmail(rs.getString("esc_email"));
						ticketEscalationVO.setEscalationPerson(rs.getString("esc_person"));
						
					}
					return ticketEscalationVO;
				}
			});
			return escLevelVO;
		}
	public List<CustomerSPLinkedTicketVO> findLinkedTicketForRSP(Long parentTicketId, String linkedTicketType, final String rspLinkedTicketQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<CustomerSPLinkedTicketVO> spLinkedTickets = jdbcTemplate.query(rspLinkedTicketQuery,new Object[]{parentTicketId}, new ResultSetExtractor<List<CustomerSPLinkedTicketVO>>(){
			@Override
			public List<CustomerSPLinkedTicketVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<CustomerSPLinkedTicketVO> linkedTickets = new ArrayList<CustomerSPLinkedTicketVO>();
				while(rs.next()){
					CustomerSPLinkedTicketVO customerSPLinkedTicketVO = new CustomerSPLinkedTicketVO();
					customerSPLinkedTicketVO.setId(rs.getLong("id"));
					customerSPLinkedTicketVO.setRspTicketLongId(rs.getLong("rsp_ticket_id"));
					customerSPLinkedTicketVO.setSpLinkedTicket(rs.getString("linked_ticket_number"));
					customerSPLinkedTicketVO.setLinkedTicketType(rs.getString("linked_ticket_type"));
					if(rs.getString("linked_ticket_type").equalsIgnoreCase("CT")){
						customerSPLinkedTicketVO.setLinkedCTticketId(rs.getLong("linked_ct_ticket_id"));
					}
					else if(rs.getString("linked_ticket_type").equalsIgnoreCase("SP")){
						customerSPLinkedTicketVO.setLinkedRspTicketId(rs.getLong("linked_rsp_ticket_id"));
					}
					customerSPLinkedTicketVO.setStatusId(rs.getLong("status_id"));
					customerSPLinkedTicketVO.setLinkedTicketStatus(rs.getString("description"));
					linkedTickets.add(customerSPLinkedTicketVO);
				}
				return linkedTickets;
			}
			
		});
		return spLinkedTickets;
	}
	
	public IncidentTask saveRspIncidentTask(IncidentTask incidentTask, LoginUser user,final String incidentTaskQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(incidentTaskQuery, 
	            Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, incidentTask.getTicketId());
            ps.setString(2,incidentTask.getTaskName());
            ps.setString(3, incidentTask.getTicketNumber() );
            ps.setString(4, incidentTask.getTaskNumber() );
            ps.setString(5, incidentTask.getTaskDesc() );
            ps.setDate(6,	ApplicationUtil.getSqlDate(incidentTask.getPlannedStartDate()));
    		ps.setDate(7,   ApplicationUtil.getSqlDate(incidentTask.getPlannedComplDate()));
    		ps.setString(8, incidentTask.getTaskAssignedTo());
    		ps.setString(9, incidentTask.getTaskStatus());
    		ps.setString(10, incidentTask.getResComments());
    		ps.setString(11, user.getUsername());
	        return ps;
	      }
	    }, key);
	    LOGGER.info("Saved task details for ticket {} with task id {}.", incidentTask.getTicketNumber(), key.getKey());
	    if(key.getKey()!=null){
	    	incidentTask.setTaskId(key.getKey().longValue());
	    	incidentTask.setStatus(200);
	    }
	    
	    return incidentTask;
	}
	public List<IncidentTask> getRSPIncidentTasks(Long ticketId, final String rspIncidentTaskListQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		List<IncidentTask> rspIncidentTaskList = jdbcTemplate.query(rspIncidentTaskListQuery,new Object[]{ticketId}, new ResultSetExtractor<List<IncidentTask>>(){
			@Override
			public List<IncidentTask> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<IncidentTask> incidentTasks = new ArrayList<IncidentTask>();
				while(rs.next()){
					IncidentTask incidentTask = new IncidentTask();
					incidentTask.setTaskId(rs.getLong("task_id"));
					incidentTask.setTaskName(rs.getString("task_name"));
					incidentTask.setTaskNumber(rs.getString("task_number"));
					incidentTask.setTaskDesc(rs.getString("task_desc"));
					incidentTask.setPlanStartDate(ApplicationUtil.getDateStringFromSQLDate(rs.getString("planned_start_date")));
					incidentTask.setPlanEndDate(ApplicationUtil.getDateStringFromSQLDate(rs.getString("planned_end_date")));
					incidentTask.setTaskAssignedTo(rs.getString("task_assigned_to"));
					incidentTask.setTaskStatus(rs.getString("task_status"));
					incidentTask.setCreatedDate(ApplicationUtil.makeDateStringFromSQLDate(rs.getString("created_date")));
					incidentTask.setCreatedBy(rs.getString("created_by"));
					incidentTask.setResComments(rs.getString("res_comment"));
					incidentTasks.add(incidentTask);
				}
				return incidentTasks;
			}
			
		});
		return rspIncidentTaskList;
	}
	public int updateRspIncidentTask(IncidentTask incidentTask, LoginUser user,
			String updateRspIncidentTaskQuery) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
        int updatedRow = jdbcTemplate.update(updateRspIncidentTaskQuery,  new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, incidentTask.getTaskName());
         		ps.setString(2, incidentTask.getTaskDesc());
         		ps.setDate(3,	ApplicationUtil.getSqlDate(incidentTask.getPlannedStartDate()));
         		ps.setDate(4,   ApplicationUtil.getSqlDate(incidentTask.getPlannedComplDate()));
         		ps.setString(5, incidentTask.getTaskAssignedTo());
         		ps.setString(6, incidentTask.getTaskStatus());
         		ps.setString(7, incidentTask.getResComments());
         		ps.setString(8, user.getUsername());
         		ps.setLong(9, incidentTask.getTaskId());
            }

        });
		return updatedRow;
	}
	
}
