/**
 * 
 */
package com.pms.app.constants;

public enum TicketUpdateType {

	UPDATE_FOR_EXTSP_TICKETS("EXTSP"),
	
	UPDATE_FOR_RSP_ASSIGNED_TICKETS("RSP"),
	
	UPDATE_FOR_CUSTOMER_TICKETS("EXTSP"),
	
	UPDATE_BY_RSP_FOR_CUSTOMER_TICKET("CUSTOMER"),
	
	UPDATE_BY_RSP_FOR_COMPANY_TICKET("RSP"),
	
	UPDATE_BY_RSP_FOR_EXTCUST_TICKET("EXTCUST");
	

	private final String updateType;

	TicketUpdateType(final String s) { updateType = s; }


	public String getUpdateType() {
		return updateType;
	}


	@Override
	public String toString() { return this.name(); }
}
