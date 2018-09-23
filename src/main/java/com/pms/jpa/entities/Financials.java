package com.pms.jpa.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "pm_cust_ticket_financials")
public class Financials implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="cost_id",unique=true, nullable=false)
	private Long Id;
	
	@Column(name="ticket_id",unique=false, nullable=false)
	private Long ticketId;
	
	@Column(name="cost_name")
	private String CostName;
	
	@Column(name="cost")
	private BigDecimal cost;
	
	@Column(name="charge_back")
	private String ChargeBack;
	
	@Column(name="billable")
	private String Billable;
	
	@Column(name="created_on")
	private Date CreatedOn = new Date();
	
	@Column(name="created_by")
	private String CreatedBy;
	
	@Column(name="modified_by")
	private String ModifiedBy;
	
	@Column(name="modified_on")
	private Date ModifiedOn;
	
	@Column(name="version")
	@Version
	private int Version ;

	
	public Financials(){
		
	}

	public Financials( Long ticketId, String costName, BigDecimal cost, String chargeBack, String billable) {
		super();
		this.ticketId = ticketId;
		this.CostName = costName;
		this.cost = cost;
		this.ChargeBack = chargeBack;
		this.Billable = billable;
	}

	public Financials(Long id, Long ticketId, String costName, BigDecimal cost, String chargeBack, String billable) {
		super();
		Id = id;
		this.ticketId = ticketId;
		CostName = costName;
		this.cost = cost;
		ChargeBack = chargeBack;
		Billable = billable;
	}
	public Long getId() {
		return Id;
	}


	public void setId(Long id) {
		Id = id;
	}


	public Long getTicketId() {
		return ticketId;
	}

	public void setTicketId(Long ticketId) {
		this.ticketId = ticketId;
	}

	public String getCostName() {
		return CostName;
	}

	public void setCostName(String costName) {
		CostName = costName;
	}

	
	public String getChargeBack() {
		return ChargeBack;
	}

	public void setChargeBack(String chargeBack) {
		ChargeBack = chargeBack;
	}

	public String getBillable() {
		return Billable;
	}

	public void setBillable(String billable) {
		Billable = billable;
	}



	public String getCreatedBy() {
		return CreatedBy;
	}

	public void setCreatedBy(String createdBy) {
		CreatedBy = createdBy;
	}

	public String getModifiedBy() {
		return ModifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		ModifiedBy = modifiedBy;
	}


	public Date getCreatedOn() {
		return CreatedOn;
	}

	public void setCreatedOn(Date createdOn) {
		CreatedOn = createdOn;
	}

	public Date getModifiedOn() {
		return ModifiedOn;
	}

	public void setModifiedOn(Date modifiedOn) {
		ModifiedOn = modifiedOn;
	}

	
	public int getVersion() {
		return Version;
	}


	public void setVersion(int version) {
		Version = version;
	}


	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "Financials [Id=" + Id + ", ticketId=" + ticketId + ", CostName=" + CostName + ", cost=" + cost
				+ ", ChargeBack=" + ChargeBack + ", Billable=" + Billable + ", CreatedOn=" + CreatedOn + ", CreatedBy="
				+ CreatedBy + ", ModifiedBy=" + ModifiedBy + ", ModifiedOn=" + ModifiedOn + ", Version=" + Version
				+ "]";
	}

	
}
