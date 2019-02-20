package com.pms.app.view.vo;

import java.io.Serializable;

public class RSPExternalSLADetailVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5445179091916308155L;
	private Long slaId;
	private Long priorityId;
	private int duration;
	private String unit;
	private String priority;
	public Long getSlaId() {
		return slaId;
	}
	public void setSlaId(Long slaId) {
		this.slaId = slaId;
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
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	@Override
	public String toString() {
		return "RSPExternalSLADetailVO [slaId=" + slaId + ", priorityId=" + priorityId + ", duration=" + duration
				+ ", unit=" + unit + ", priority=" + priority + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((priority == null) ? 0 : priority.hashCode());
		result = prime * result + ((slaId == null) ? 0 : slaId.hashCode());
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
		RSPExternalSLADetailVO other = (RSPExternalSLADetailVO) obj;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (slaId == null) {
			if (other.slaId != null)
				return false;
		} else if (!slaId.equals(other.slaId))
			return false;
		return true;
	}
	
	

}
