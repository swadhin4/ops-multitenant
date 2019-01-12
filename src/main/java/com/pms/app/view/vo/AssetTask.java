package com.pms.app.view.vo;

import java.io.Serializable;
import java.util.Date;

public class AssetTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6328518344093529771L;
	private Long taskId;
	private Long assetId;
	private String taskName;
	private String taskDesc;
	private String planStartDate;
	private String planEndDate;
	private String taskAssignedTo;
	private String taskStatus;
	private String resComments;
	private String createdBy;
	private String modifiedBy;
	private String createdDate;
	private Date plannedStartDate;
	private Date plannedComplDate;
	private int status;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public Long getAssetId() {
		return assetId;
	}
	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDesc() {
		return taskDesc;
	}
	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}
	public String getPlanStartDate() {
		return planStartDate;
	}
	public void setPlanStartDate(String planStartDate) {
		this.planStartDate = planStartDate;
	}
	public String getPlanEndDate() {
		return planEndDate;
	}
	public void setPlanEndDate(String planEndDate) {
		this.planEndDate = planEndDate;
	}
	public String getTaskAssignedTo() {
		return taskAssignedTo;
	}
	public void setTaskAssignedTo(String taskAssignedTo) {
		this.taskAssignedTo = taskAssignedTo;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getResComments() {
		return resComments;
	}
	public void setResComments(String resComments) {
		this.resComments = resComments;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Date getPlannedStartDate() {
		return plannedStartDate;
	}
	public void setPlannedStartDate(Date plannedStartDate) {
		this.plannedStartDate = plannedStartDate;
	}
	public Date getPlannedComplDate() {
		return plannedComplDate;
	}
	public void setPlannedComplDate(Date plannedComplDate) {
		this.plannedComplDate = plannedComplDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((assetId == null) ? 0 : assetId.hashCode());
		result = prime * result + ((taskId == null) ? 0 : taskId.hashCode());
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
		AssetTask other = (AssetTask) obj;
		if (assetId == null) {
			if (other.assetId != null)
				return false;
		} else if (!assetId.equals(other.assetId))
			return false;
		if (taskId == null) {
			if (other.taskId != null)
				return false;
		} else if (!taskId.equals(other.taskId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "AssetTask [taskId=" + taskId + ", assetId=" + assetId + ", taskName=" + taskName + ", taskDesc="
				+ taskDesc + ", planStartDate=" + planStartDate + ", planEndDate=" + planEndDate + ", taskAssignedTo="
				+ taskAssignedTo + ", taskStatus=" + taskStatus + ", resComments=" + resComments + ", createdBy="
				+ createdBy + "]";
	}

	
}
