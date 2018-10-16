package com.pms.app.view.vo;

import java.io.Serializable;

public class SiteSubmeterVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7119802620415614013L;

	private Long subMeterId;

	private String subMeterNumber;

	private String subMeterUser;

	public Long getSubMeterId() {
		return subMeterId;
	}

	public void setSubMeterId(final Long subMeterId) {
		this.subMeterId = subMeterId;
	}

	public String getSubMeterNumber() {
		return subMeterNumber;
	}

	public void setSubMeterNumber(final String subMeterNumber) {
		this.subMeterNumber = subMeterNumber;
	}

	public String getSubMeterUser() {
		return subMeterUser;
	}

	public void setSubMeterUser(final String subMeterUser) {
		this.subMeterUser = subMeterUser;
	}

	@Override
	public String toString() {
		return "SiteSubmeterVO [subMeterId=" + subMeterId + ", subMeterNumber="
				+ subMeterNumber + ", subMeterUser=" + subMeterUser + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subMeterId == null) ? 0 : subMeterId.hashCode());
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
		SiteSubmeterVO other = (SiteSubmeterVO) obj;
		if (subMeterId == null) {
			if (other.subMeterId != null)
				return false;
		} else if (!subMeterId.equals(other.subMeterId))
			return false;
		return true;
	}


}
