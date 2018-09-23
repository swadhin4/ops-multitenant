package com.pms.app.view.vo;

import java.io.Serializable;

public class SiteLicenceVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3445885064403395180L;
	private Long licenseId;
	private String licenseName;
	private String validfrom;
	private String validto;
	private String attachment;
	private int status;


	public Long getLicenseId() {
		return licenseId;
	}
	public void setLicenseId(final Long licenseId) {
		this.licenseId = licenseId;
	}
	public String getLicenseName() {
		return licenseName;
	}
	public void setLicenseName(final String licenseName) {
		this.licenseName = licenseName;
	}
	public String getValidfrom() {
		return validfrom;
	}
	public void setValidfrom(final String validfrom) {
		this.validfrom = validfrom;
	}
	public String getValidto() {
		return validto;
	}
	public void setValidto(final String validto) {
		this.validto = validto;
	}
	public String getAttachment() {
		return attachment;
	}
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "SiteLicenceVO [licenseId=" + licenseId + ", licenseName="
				+ licenseName + ", validfrom=" + validfrom + ", validto="
				+ validto + "]";
	}

}
