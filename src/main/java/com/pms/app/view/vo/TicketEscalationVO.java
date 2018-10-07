package com.pms.app.view.vo;

import java.io.Serializable;

public class TicketEscalationVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4395687233186874682L;
	private Long custEscId;
	private Long escId;
	private Long ticketId;
	private String ticketNumber;
	private String escLevelDesc;
	private String escalatedBy;
	private String escalatedDate;
	private String escalationStatus;
	private TicketVO ticketData;
	
	public TicketEscalationVO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getCustEscId() {
		return custEscId;
	}
	public void setCustEscId(Long custEscId) {
		this.custEscId = custEscId;
	}
	public Long getEscId() {
		return escId;
	}
	public void setEscId(Long escId) {
		this.escId = escId;
	}
	public Long getTicketId() {
		return ticketId;
	}
	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}
	public String getTicketNumber() {
		return ticketNumber;
	}
	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}
	
	public String getEscalatedBy() {
		return escalatedBy;
	}
	public void setEscalatedBy(String escalatedBy) {
		this.escalatedBy = escalatedBy;
	}
	public String getEscalatedDate() {
		return escalatedDate;
	}
	public void setEscalatedDate(String escalatedDate) {
		this.escalatedDate = escalatedDate;
	}
	public String getEscLevelDesc() {
		return escLevelDesc;
	}
	public void setEscLevelDesc(String escLevelDesc) {
		this.escLevelDesc = escLevelDesc;
	}
	public String getEscalationStatus() {
		return escalationStatus;
	}
	public void setEscalationStatus(String escalationStatus) {
		this.escalationStatus = escalationStatus;
	}
	public TicketVO getTicketData() {
		return ticketData;
	}
	public void setTicketData(TicketVO ticketData) {
		this.ticketData = ticketData;
	}
	@Override
	public String toString() {
		return "TicketEscalationVO [escId=" + escId + ", ticketId=" + ticketId + ", ticketNumber=" + ticketNumber
				+ ", escLevelDesc=" + escLevelDesc + ", escalatedBy=" + escalatedBy + ", escalatedDate=" + escalatedDate
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custEscId == null) ? 0 : custEscId.hashCode());
		result = prime * result + ((escId == null) ? 0 : escId.hashCode());
		result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TicketEscalationVO other = (TicketEscalationVO) obj;
		if (custEscId == null) {
			if (other.custEscId != null)
				return false;
		} else if (!custEscId.equals(other.custEscId))
			return false;
		if (escId == null) {
			if (other.escId != null)
				return false;
		} else if (!escId.equals(other.escId))
			return false;
		if (ticketId == null) {
			if (other.ticketId != null)
				return false;
		} else if (!ticketId.equals(other.ticketId))
			return false;
		return true;
	}
	

}
