package com.pms.app.view.vo;

import java.io.Serializable;

public class FinDelReqBodyVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long ticketID;
	
	private Long costId;
	
	private String costName;
	
	private float cost;
	
	private Long chargeBack;
	
	private Long billable;
	
	private boolean isDeleteCheck;

	public Long getCostId() {
		return costId;
	}

	public void setCostId(Long costId) {
		this.costId = costId;
	}

	public Long getTicketID() {
		return ticketID;
	}

	public void setTicketID(Long ticketID) {
		this.ticketID = ticketID;
	}

	public Long getChargeBack() {
		return chargeBack;
	}

	public void setChargeBack(Long chargeBack) {
		this.chargeBack = chargeBack;
	}

	public Long getBillable() {
		return billable;
	}

	public void setBillable(Long billable) {
		this.billable = billable;
	}

	public float getCost() {
		return cost;
	}

	public void setCost(float cost) {
		this.cost = cost;
	}

	public boolean isDeleteCheck() {
		return isDeleteCheck;
	}

	public void setDeleteCheck(boolean isDeleteCheck) {
		this.isDeleteCheck = isDeleteCheck;
	}
	
	public String getCostName() {
		return costName;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}

	@Override
	public String toString() {
		return "FinReqBodyVO [costId=" + costId + ", ticketID=" + ticketID + ", chargeBack=" + chargeBack
				+ ", billable=" + billable + ", cost=" + cost + ", isDeleteCheck=" + isDeleteCheck + "]";
	}

}
