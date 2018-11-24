package com.pms.app.view.vo;

import java.io.Serializable;

public class RegisteredSPSLA implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8071383330694790999L;
	
	private Long rslaId;
	private Long rspId;
	private Long priorityId;
	private int duration;
	private String unit;
	public Long getRslaId() {
		return rslaId;
	}
	public void setRslaId(Long rslaId) {
		this.rslaId = rslaId;
	}
	public Long getRspId() {
		return rspId;
	}
	public void setRspId(Long rspId) {
		this.rspId = rspId;
	}
	public Long getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Long priorityId) {
		this.priorityId = priorityId;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	
}
