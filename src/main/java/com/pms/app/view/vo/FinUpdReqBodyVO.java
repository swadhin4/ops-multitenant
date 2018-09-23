package com.pms.app.view.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class FinUpdReqBodyVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long costId;
	
	private Long ticketID;
	
	private String costName;
	
	private BigDecimal cost = new BigDecimal("0.00");
	
	private String chargeBack;
	
	private String billable;
	
	private boolean isDeleteCheck;
	
	private boolean isEdited;
	
	
	public FinUpdReqBodyVO(Long costId, Long ticketID, String costName, BigDecimal cost, String chargeBack, String billable) {
		super();
		this.costId = costId;
		this.ticketID = ticketID;
		this.costName = costName;
		this.cost = cost;
		this.chargeBack = chargeBack;
		this.billable = billable;
	}


	public Long getTicketID() {
		return ticketID;
	}


	public void setTicketID(Long ticketID) {
		this.ticketID = ticketID;
	}


	public Long getCostId() {
		return costId;
	}


	public void setCostId(Long costId) {
		this.costId = costId;
	}


	public String getCostName() {
		return costName;
	}


	public void setCostName(String costName) {
		this.costName = costName;
	}


	public BigDecimal getCost() {
		return cost;
	}


	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}


	public String getChargeBack() {
		return chargeBack;
	}


	public void setChargeBack(String chargeBack) {
		this.chargeBack = chargeBack;
	}


	public String getBillable() {
		return billable;
	}


	public void setBillable(String billable) {
		this.billable = billable;
	}



	public boolean isDeleteCheck() {
		return isDeleteCheck;
	}


	public void setDeleteCheck(boolean isDeleteCheck) {
		this.isDeleteCheck = isDeleteCheck;
	}


	public boolean isEdited() {
		return isEdited;
	}


	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
	

	
}
