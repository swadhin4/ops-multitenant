package com.pms.app.view.vo;

import java.math.BigDecimal;

public class FinancialVO {

		private String costId;
		
		private Long ticketID;
		
		private String costName;
		
		private BigDecimal cost = new BigDecimal("0.00");
		
		private String chargeBack;
		
		private String billable;
		
		private String CreatedBy;
		
		private String isDeleteCheck;
		
		private String isEdited;

		
		public String getCostId() {
			return costId;
		}

		public void setCostId(String costId) {
			this.costId = costId;
		}

		
		public Long getTicketID() {
			return ticketID;
		}

		public void setTicketID(Long ticketID) {
			this.ticketID = ticketID;
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

		public String getCreatedBy() {
			return CreatedBy;
		}

		public void setCreatedBy(String createdBy) {
			CreatedBy = createdBy;
		}

		public String getIsDeleteCheck() {
			return isDeleteCheck;
		}

		public void setIsDeleteCheck(String isDeleteCheck) {
			this.isDeleteCheck = isDeleteCheck;
		}

		public String getIsEdited() {
			return isEdited;
		}

		public void setIsEdited(String isEdited) {
			this.isEdited = isEdited;
		}

			
}
