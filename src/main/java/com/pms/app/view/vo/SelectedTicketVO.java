package com.pms.app.view.vo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class SelectedTicketVO implements Serializable {

	private static final long serialVersionUID = -6205454701524949740L;
	@Id
	private Long id;
	private String ticket_number;
	private String ticket_title;
	private String ticket_desc;
	private Long status_id;
	private String status;
	private String description;
	private Long ticket_category_id;
	private String ticket_category;
	private Long site_id;
	private String site_name;
	private Long site_number1;
	private Long site_number2;
	private Long primary_contact_number;
	private String site_address1;
	private String site_address2;
	private String site_address3;
	private String site_address4;
	private String post_code;
	private Long asset_id;
	private String asset_name;
	private String asset_code;
	private String model_number;
	private Long asset_category_id;
	private String category_name;
	private Long asset_subcategory1_id;
	private String asset_subcategory1;
	private Long asset_subcategory2_id;
	private String subcategory2_name;
	private String priority;
	private String sla_duedate;
	private String ticket_starttime;
	private String service_restoration_ts;
	private String close_code;
	private String closed_code_desc;
	private String is_rootcause_resolved;
	private String close_note;
	private Long assigned_to;
	private Long rassigned_to;
	private String rsp_name;
	private String sp_name;
	private String closed_by;
	private String first_name;
	private String last_name;
	private String phone;
	private String closed_on;
	private String created_by;
	private String created_on;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTicket_number() {
		return ticket_number;
	}
	public void setTicket_number(String ticket_number) {
		this.ticket_number = ticket_number;
	}
	public String getTicket_title() {
		return ticket_title;
	}
	public void setTicket_title(String ticket_title) {
		this.ticket_title = ticket_title;
	}
	public String getTicket_desc() {
		return ticket_desc;
	}
	public void setTicket_desc(String ticket_desc) {
		this.ticket_desc = ticket_desc;
	}
	public Long getStatus_id() {
		return status_id;
	}
	public void setStatus_id(Long status_id) {
		this.status_id = status_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Long getTicket_category_id() {
		return ticket_category_id;
	}
	public void setTicket_category_id(Long ticket_category_id) {
		this.ticket_category_id = ticket_category_id;
	}
	public String getTicket_category() {
		return ticket_category;
	}
	public void setTicket_category(String ticket_category) {
		this.ticket_category = ticket_category;
	}
	public Long getSite_id() {
		return site_id;
	}
	public void setSite_id(Long site_id) {
		this.site_id = site_id;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}
	public Long getSite_number1() {
		return site_number1;
	}
	public void setSite_number1(Long site_number1) {
		this.site_number1 = site_number1;
	}
	public Long getSite_number2() {
		return site_number2;
	}
	public void setSite_number2(Long site_number2) {
		this.site_number2 = site_number2;
	}
	public Long getPrimary_contact_number() {
		return primary_contact_number;
	}
	public void setPrimary_contact_number(Long primary_contact_number) {
		this.primary_contact_number = primary_contact_number;
	}
	public String getSite_address1() {
		return site_address1;
	}
	public void setSite_address1(String site_address1) {
		this.site_address1 = site_address1;
	}
	public String getSite_address2() {
		return site_address2;
	}
	public void setSite_address2(String site_address2) {
		this.site_address2 = site_address2;
	}
	public String getSite_address3() {
		return site_address3;
	}
	public void setSite_address3(String site_address3) {
		this.site_address3 = site_address3;
	}
	public String getSite_address4() {
		return site_address4;
	}
	public void setSite_address4(String site_address4) {
		this.site_address4 = site_address4;
	}
	public String getPost_code() {
		return post_code;
	}
	public void setPost_code(String post_code) {
		this.post_code = post_code;
	}
	public Long getAsset_id() {
		return asset_id;
	}
	public void setAsset_id(Long asset_id) {
		this.asset_id = asset_id;
	}
	public String getAsset_name() {
		return asset_name;
	}
	public void setAsset_name(String asset_name) {
		this.asset_name = asset_name;
	}
	public String getAsset_code() {
		return asset_code;
	}
	public void setAsset_code(String asset_code) {
		this.asset_code = asset_code;
	}
	public String getModel_number() {
		return model_number;
	}
	public void setModel_number(String model_number) {
		this.model_number = model_number;
	}
	public Long getAsset_category_id() {
		return asset_category_id;
	}
	public void setAsset_category_id(Long asset_category_id) {
		this.asset_category_id = asset_category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public Long getAsset_subcategory1_id() {
		return asset_subcategory1_id;
	}
	public void setAsset_subcategory1_id(Long asset_subcategory1_id) {
		this.asset_subcategory1_id = asset_subcategory1_id;
	}
	public String getAsset_subcategory1() {
		return asset_subcategory1;
	}
	public void setAsset_subcategory1(String asset_subcategory1) {
		this.asset_subcategory1 = asset_subcategory1;
	}
	public Long getAsset_subcategory2_id() {
		return asset_subcategory2_id;
	}
	public void setAsset_subcategory2_id(Long asset_subcategory2_id) {
		this.asset_subcategory2_id = asset_subcategory2_id;
	}
	public String getSubcategory2_name() {
		return subcategory2_name;
	}
	public void setSubcategory2_name(String subcategory2_name) {
		this.subcategory2_name = subcategory2_name;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getSla_duedate() {
		return sla_duedate;
	}
	public void setSla_duedate(String sla_duedate) {
		this.sla_duedate = sla_duedate;
	}
	public String getTicket_starttime() {
		return ticket_starttime;
	}
	public void setTicket_starttime(String ticket_starttime) {
		this.ticket_starttime = ticket_starttime;
	}
	public String getService_restoration_ts() {
		return service_restoration_ts;
	}
	public void setService_restoration_ts(String service_restoration_ts) {
		this.service_restoration_ts = service_restoration_ts;
	}
	public String getClose_code() {
		return close_code;
	}
	public void setClose_code(String close_code) {
		this.close_code = close_code;
	}
	public String getClosed_code_desc() {
		return closed_code_desc;
	}
	public void setClosed_code_desc(String closed_code_desc) {
		this.closed_code_desc = closed_code_desc;
	}
	public String getIs_rootcause_resolved() {
		return is_rootcause_resolved;
	}
	public void setIs_rootcause_resolved(String is_rootcause_resolved) {
		this.is_rootcause_resolved = is_rootcause_resolved;
	}
	public String getClose_note() {
		return close_note;
	}
	public void setClose_note(String close_note) {
		this.close_note = close_note;
	}
	public Long getAssigned_to() {
		return assigned_to;
	}
	public void setAssigned_to(Long assigned_to) {
		this.assigned_to = assigned_to;
	}
	public String getSp_name() {
		return sp_name;
	}
	public void setSp_name(String sp_name) {
		this.sp_name = sp_name;
	}
	public String getClosed_by() {
		return closed_by;
	}
	public void setClosed_by(String closed_by) {
		this.closed_by = closed_by;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getClosed_on() {
		return closed_on;
	}
	public void setClosed_on(String closed_on) {
		this.closed_on = closed_on;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_on() {
		return created_on;
	}
	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}
	public Long getRassigned_to() {
		return rassigned_to;
	}
	public void setRassigned_to(Long rassigned_to) {
		this.rassigned_to = rassigned_to;
	}
	public String getRsp_name() {
		return rsp_name;
	}
	public void setRsp_name(String rsp_name) {
		this.rsp_name = rsp_name;
	}
	@Override
	public String toString() {
		return "SelectedTicketVO [id=" + id + ", ticket_number=" + ticket_number + ", ticket_title=" + ticket_title
				+ ", ticket_desc=" + ticket_desc + ", status_id=" + status_id + ", status=" + status + ", description="
				+ description + ", ticket_category_id=" + ticket_category_id + ", ticket_category=" + ticket_category
				+ ", site_id=" + site_id + ", site_name=" + site_name + ", site_number1=" + site_number1
				+ ", site_number2=" + site_number2 + ", primary_contact_number=" + primary_contact_number
				+ ", site_address1=" + site_address1 + ", site_address2=" + site_address2 + ", site_address3="
				+ site_address3 + ", site_address4=" + site_address4 + ", post_code=" + post_code + ", asset_id="
				+ asset_id + ", asset_name=" + asset_name + ", asset_code=" + asset_code + ", model_number="
				+ model_number + ", asset_category_id=" + asset_category_id + ", category_name=" + category_name
				+ ", asset_subcategory1_id=" + asset_subcategory1_id + ", asset_subcategory1=" + asset_subcategory1
				+ ", asset_subcategory2_id=" + asset_subcategory2_id + ", subcategory2_name=" + subcategory2_name
				+ ", priority=" + priority + ", sla_duedate=" + sla_duedate + ", ticket_starttime=" + ticket_starttime
				+ ", service_restoration_ts=" + service_restoration_ts + ", close_code=" + close_code
				+ ", closed_code_desc=" + closed_code_desc + ", is_rootcause_resolved=" + is_rootcause_resolved
				+ ", close_note=" + close_note + ", assigned_to=" + assigned_to + ", sp_name=" + sp_name
				+ ", closed_by=" + closed_by + ", first_name=" + first_name + ", last_name=" + last_name + ", phone="
				+ phone + ", closed_on=" + closed_on + ", created_by=" + created_by + ", created_on=" + created_on
				+ "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((ticket_number == null) ? 0 : ticket_number.hashCode());
		result = prime * result + ((ticket_title == null) ? 0 : ticket_title.hashCode());
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
		SelectedTicketVO other = (SelectedTicketVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (ticket_number == null) {
			if (other.ticket_number != null)
				return false;
		} else if (!ticket_number.equals(other.ticket_number))
			return false;
		if (ticket_title == null) {
			if (other.ticket_title != null)
				return false;
		} else if (!ticket_title.equals(other.ticket_title))
			return false;
		return true;
	}
	
	
	
}
	
