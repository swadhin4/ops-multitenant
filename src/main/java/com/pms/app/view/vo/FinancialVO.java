package com.pms.app.view.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class FinancialVO implements Serializable{

		/**
	 * 
	 */
	private static final long serialVersionUID = 2088751500409682110L;

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

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((costId == null) ? 0 : costId.hashCode());
			result = prime * result + ((ticketID == null) ? 0 : ticketID.hashCode());
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
			FinancialVO other = (FinancialVO) obj;
			if (costId == null) {
				if (other.costId != null)
					return false;
			} else if (!costId.equals(other.costId))
				return false;
			if (ticketID == null) {
				if (other.ticketID != null)
					return false;
			} else if (!ticketID.equals(other.ticketID))
				return false;
			return true;
		}

			
}
