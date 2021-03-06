package com.pms.app.view.vo;

import java.io.Serializable;

public class CustomerSPLinkedTicketVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5921604434243227656L;
	private Long id;
	private String spLinkedTicket;
	private String custTicketNumber;
	private String custTicketId;
	private String rspTicketId;
	private String rspTicketNumber;
	private String closedFlag;
	private String closeTime;
	private String createdBy;
	private String createdOn;
	private String modifiedOn;
	private String modifiedBy;
	private String spType;
	private String isValidLink; 
	private Long statusId;
	private String linkedTicketStatus;
	
	private Long rspTicketLongId;
	private Long linkedCTticketId;
	private Long linkedRspTicketId;
	private String linkedTicketType;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSpLinkedTicket() {
		return spLinkedTicket;
	}
	public void setSpLinkedTicket(String spLinkedTicket) {
		this.spLinkedTicket = spLinkedTicket;
	}
	public String getCustTicketNumber() {
		return custTicketNumber;
	}
	public void setCustTicketNumber(String custTicketNumber) {
		this.custTicketNumber = custTicketNumber;
	}
	public String getCustTicketId() {
		return custTicketId;
	}
	public void setCustTicketId(String custTicketId) {
		this.custTicketId = custTicketId;
	}
	public String getClosedFlag() {
		return closedFlag;
	}
	public void setClosedFlag(String closedFlag) {
		this.closedFlag = closedFlag;
	}
	public String getCloseTime() {
		return closeTime;
	}
	public void setCloseTime(String closeTime) {
		this.closeTime = closeTime;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}
	public String getModifiedOn() {
		return modifiedOn;
	}
	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getSpType() {
		return spType;
	}
	public void setSpType(String spType) {
		this.spType = spType;
	}
	public String getRspTicketId() {
		return rspTicketId;
	}
	public void setRspTicketId(String rspTicketId) {
		this.rspTicketId = rspTicketId;
	}
	public String getRspTicketNumber() {
		return rspTicketNumber;
	}
	public void setRspTicketNumber(String rspTicketNumber) {
		this.rspTicketNumber = rspTicketNumber;
	}
	public String getIsValidLink() {
		return isValidLink;
	}
	public void setIsValidLink(String isValidLink) {
		this.isValidLink = isValidLink;
	}
	public Long getStatusId() {
		return statusId;
	}
	public void setStatusId(Long statusId) {
		this.statusId = statusId;
	}
	public String getLinkedTicketStatus() {
		return linkedTicketStatus;
	}
	public void setLinkedTicketStatus(String linkedTicketStatus) {
		this.linkedTicketStatus = linkedTicketStatus;
	}
	public Long getRspTicketLongId() {
		return rspTicketLongId;
	}
	public void setRspTicketLongId(Long rspTicketLongId) {
		this.rspTicketLongId = rspTicketLongId;
	}
	public Long getLinkedCTticketId() {
		return linkedCTticketId;
	}
	public void setLinkedCTticketId(Long linkedCTticketId) {
		this.linkedCTticketId = linkedCTticketId;
	}
	public Long getLinkedRspTicketId() {
		return linkedRspTicketId;
	}
	public void setLinkedRspTicketId(Long linkedRspTicketId) {
		this.linkedRspTicketId = linkedRspTicketId;
	}
	public String getLinkedTicketType() {
		return linkedTicketType;
	}
	public void setLinkedTicketType(String linkedTicketType) {
		this.linkedTicketType = linkedTicketType;
	}
	@Override
	public String toString() {
		return "CustomerSPLinkedTicketVO [id=" + id + ", spLinkedTicket=" + spLinkedTicket + ", custTicketNumber="
				+ custTicketNumber + ", custTicketId=" + custTicketId + ", closedFlag=" + closedFlag + ", closeTime="
				+ closeTime + ", createdBy=" + createdBy + ", createdOn=" + createdOn + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((custTicketId == null) ? 0 : custTicketId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		CustomerSPLinkedTicketVO other = (CustomerSPLinkedTicketVO) obj;
		if (custTicketId == null) {
			if (other.custTicketId != null)
				return false;
		} else if (!custTicketId.equals(other.custTicketId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
		
	
}
