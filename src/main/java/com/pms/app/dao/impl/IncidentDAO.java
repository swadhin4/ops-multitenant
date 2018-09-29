package com.pms.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
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
import org.springframework.util.StringUtils;

import com.mysql.jdbc.Statement;
import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Asset;
import com.pms.jpa.entities.ServiceProvider;
import com.pms.jpa.entities.Status;
import com.pms.jpa.entities.TicketAttachment;
import com.pms.jpa.entities.TicketCategory;
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
		
		return ticketVO;
	}
	public TicketPrioritySLAVO getSPSLADetails(Long serviceProviderID, Long ticketCategoryId) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		TicketPrioritySLAVO ticketPriority= jdbcTemplate.query(AppConstants.TICKET_PRIORITY_SP_SLA_QUERY, new Object[]{serviceProviderID, ticketCategoryId}, new ResultSetExtractor<TicketPrioritySLAVO>() {
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
	public Long getLastIncidentCreated() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		Long lastTicketId = jdbcTemplate.query(AppConstants.LAST_INCIDENT_NUMBER_QUERY, new ResultSetExtractor<Long>() {
			
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
	public TicketVO saveIncident (TicketVO ticketVO, LoginUser user){
		LOGGER.info("IncidentDAO -- saveIncident -- Save Incident details using Procedure: ");
		if(ticketVO.getTicketId()==null){
			//JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
			/*SimpleJdbcCall insertJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspCreateCustIncident");			SqlParameterSource map = createNewIncidentParam(ticketVO, user);
			Map<String, Object> out = insertJdbcCall.execute(map);
			Map<String, String> resultMap1 = ApplicationUtil.extractResult(out);
			ticketVO.setTicketId(Long.parseLong(resultMap1.get("@ticketId")));
			ticketVO.setStatusCode(Integer.parseInt(resultMap1.get("@isError")));*/
			try {
				ticketVO = insertTicketData(ticketVO,user);
				LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketTitle() +" with ID : "+ ticketVO.getTicketId());
				if(ticketVO.getTicketId()!=null ){
					LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
					ticketVO.setStatusCode(200);
					ticketVO.setMessage("Created");
				}else{
					ticketVO.setTicketId(null);
					LOGGER.info("Unable to create incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}else if(ticketVO.getTicketId()!=null){
			//ticketVO.setMessage("Updated");
			LOGGER.info("Incident updated for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
		}
		
		LOGGER.info("Exit -- IncidentDAO -- saveIncident-- Save Incident details using Procedure: ");
		return ticketVO;

	}
	
	private TicketVO insertTicketData(TicketVO ticketVO, LoginUser user) {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		KeyHolder key = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	      @Override
	      public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	        final PreparedStatement ps = connection.prepareStatement(AppConstants.INSERT_TICKET_QUERY, 
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
	    String updateSLa = updateSlaDueDate(ticketVO.getTicketNumber(), ticketVO.getDuration(), ticketVO.getUnit());
	    ticketVO.setSla(updateSLa);
	    return ticketVO;
	}
	public String updateSlaDueDate(String ticketNumber, Integer slaDuration, String slaUnit) {
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getDataSource());
		SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("usp_update_sla_duedate");
		 SqlParameterSource in = new MapSqlParameterSource()
				 .addValue("p_ticket_number", ticketNumber)
				 .addValue("p_sla_duration", slaDuration)
				 .addValue("p_sla_unit", slaUnit)
		 			.addValue("out_sla_due_date", "@out_sla_due_date");
		 
	      Map<String, Object> out = jdbcCall.execute(in);
	      String slaDueDate = (String)out.get("out_sla_due_date");
	      return slaDueDate;
	      
	}
	private SqlParameterSource createNewIncidentParam(TicketVO ticketVO, LoginUser user) {
		SqlParameterSource map = new MapSqlParameterSource()
				.addValue("ticketNumber", ticketVO.getTicketNumber())
				.addValue("ticketTitle", ticketVO.getTicketTitle())
				.addValue("ticketDesc", ticketVO.getDescription())
				.addValue("siteId", ticketVO.getSiteId())
				.addValue("assetId", ticketVO.getAssetId())
				.addValue("assetCatId",ticketVO.getAssetCategoryId())
				.addValue("assetSubCat1Id", ticketVO.getSubCategoryId1())
				.addValue("assetSubCat2Id", ticketVO.getSubCategoryId2())
				.addValue("spId", ticketVO.getAssignedTo())
				.addValue("ticketCatId", ticketVO.getCategoryId())
				.addValue("ticketPriority",ticketVO.getPriorityDescription())
				.addValue("issueStartTime", ticketVO.getTicketStartTime())
				.addValue("slaDuration", ticketVO.getDuration())
				.addValue("slaUnit", ticketVO.getUnit())
				.addValue("statusId", ticketVO.getStatusId())
				.addValue("createdBy", user.getUsername())
				.addValue("dbName", user.getDbName())
				.addValue("ticketId", "@ticketId")
				.addValue("isError", "@isError");
				return map;
	}
	
	private int createTicketUsingProc(TicketVO ticketVO, LoginUser user)
            throws Exception {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
        int updatedRows = jdbcTemplate.update("call tenants.uspCreateCustIncident(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                new PreparedStatementSetter() {

                    @Override
                    public void setValues(PreparedStatement ps) throws SQLException {
                    	ps.setString(1, ticketVO.getTicketNumber());
                        ps.setString(2, ticketVO.getTicketTitle());
                        ps.setString(3, ticketVO.getDescription());
                        ps.setLong(4, ticketVO.getSiteId());
                        ps.setLong(5, ticketVO.getAssetId());
                        ps.setLong(6, ticketVO.getAssetCategoryId());
                        ps.setLong(7, ticketVO.getSubCategoryId1());
                        ps.setLong(8, ticketVO.getSubCategoryId2());
                        ps.setLong(9, ticketVO.getAssignedTo());
                        ps.setLong(10,  ticketVO.getCategoryId());
                        ps.setString(11, ticketVO.getPriorityDescription());
                        ps.setString(12, ticketVO.getTicketStartTime());
                        ps.setLong(13, ticketVO.getDuration());
                        ps.setString(14, ticketVO.getUnit());
                        ps.setLong(15, ticketVO.getStatusId());
                        ps.setString(16,  user.getUsername());
                        ps.setString(17,  user.getDbName());
                      //  ps.setString(18, "@isError");
                    }


                });
        return updatedRows;
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
}
