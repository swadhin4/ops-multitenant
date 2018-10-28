package com.pms.app.view.vo;

import java.util.Date;

public class ServiceProviderUserAccessVO {
	private int accessId;

	private int userId;

	private int spCustId;

	private Date createdOn;

	private String createdBy;
	
	private boolean deleted;

	/**
	 * @return the accessId
	 */
	public int getAccessId() {
		return accessId;
	}

	/**
	 * @param accessId the accessId to set
	 */
	public void setAccessId(int accessId) {
		this.accessId = accessId;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * @return the spCustId
	 */
	public int getSpCustId() {
		return spCustId;
	}

	/**
	 * @param spCustId the spCustId to set
	 */
	public void setSpCustId(int spCustId) {
		this.spCustId = spCustId;
	}

	/**
	 * @return the createdOn
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

	/**
	 * @param createdOn the createdOn to set
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

}
