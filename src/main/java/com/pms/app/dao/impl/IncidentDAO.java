package com.pms.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.pms.app.config.ConnectionManager;
import com.pms.app.constants.AppConstants;
import com.pms.app.view.vo.CreateSiteVO;
import com.pms.app.view.vo.LoginUser;
import com.pms.app.view.vo.TicketPrioritySLAVO;
import com.pms.app.view.vo.TicketVO;
import com.pms.jpa.entities.Status;
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
		try{
		String createdDate = df2.format(df.parse(rs.getString("created_on")));
		ticketVO.setRaisedOn(createdDate);
		String slaDueDate = df2.format(df.parse(rs.getString("sla_duedate")));
		ticketVO.setSla(slaDueDate);
		}catch(ParseException e){
			e.printStackTrace();
		}
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
					ticketPrioritySLAVO.setPriorityName(rs.getString("priority"));
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
	
	public TicketVO saveIncident (TicketVO ticketVO, LoginUser user){
		LOGGER.info("IncidentDAO -- saveIncident -- Save Incident details using Procedure: ");
		JdbcTemplate jdbcTemplate = new JdbcTemplate(ConnectionManager.getTenantDataSource());
		if(ticketVO.getTicketId()==null){
			SimpleJdbcCall insertJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("uspCreateCustIncident");
			SqlParameterSource map = createNewIncidentParam(ticketVO, user);
			Map<String, Object> out = insertJdbcCall.execute(map);
			Map<String, String> resultMap1 = ApplicationUtil.extractResult(out);
			ticketVO.setTicketId(Long.parseLong(resultMap1.get("@ticketId")));
			LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketTitle() +" with ID : "+ ticketVO.getTicketId());
			if(ticketVO.getTicketId()!=null){
				LOGGER.info("Incident created for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
				ticketVO.setMessage("Created");
			}else{
				LOGGER.info("Unable to create incident for "+  user.getUsername() + "in name of "+ ticketVO.getTicketTitle());
			}
		}else if(ticketVO.getTicketId()!=null){
			//ticketVO.setMessage("Updated");
			LOGGER.info("Incident updated for "+  user.getUsername() + "/ Incident : "+ ticketVO.getTicketNumber() +" with ID : "+ ticketVO.getTicketId());
		}
		
		LOGGER.info("Exit -- IncidentDAO -- saveIncident-- Save Incident details using Procedure: ");
		return ticketVO;

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
				.addValue("ticketPriorityId",ticketVO.getPriorityId())
				.addValue("ticketSLA", ticketVO.getSla())
				.addValue("issueStartTime", ticketVO.getTicketStartTime())
				.addValue("statusId", ticketVO.getStatusId())
				.addValue("createdBy", user.getUsername())
				.addValue("dbName", user.getDbName())
				.addValue("ticketId", "@ticketId")
				.addValue("isError", "@isError");
				return map;
	}
}
