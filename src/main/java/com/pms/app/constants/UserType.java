package com.pms.app.constants;

public enum UserType {
	
	LOGGEDIN_USER_CUSTOMER("USER"),
	
	LOGGEDIN_USER_EXTSP("EXTSP"),
	
	LOGGEDIN_USER_RSP("SP")
	;
	

	private final String userType;

	UserType(final String s) { userType = s; }


	public String getUserType() {
		return userType;
	}


	@Override
	public String toString() { return this.name(); }

}
