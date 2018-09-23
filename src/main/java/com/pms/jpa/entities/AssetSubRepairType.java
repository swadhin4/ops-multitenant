package com.pms.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="pm_asset_subcategory2")
public class AssetSubRepairType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5088724621482326274L;
	
	@Id	
	@Column(name="subcategory2_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long subCategoryId2;
	
	@Column(name="subcategory1_id")
	private Long assetSubCategoryId;
	
	@Column(name="subcategory2_name")
	private String assetSubcategory2;


	public Long getSubCategoryId2() {
		return subCategoryId2;
	}

	public void setSubCategoryId2(Long subCategoryId2) {
		this.subCategoryId2 = subCategoryId2;
	}

	public Long getAssetSubCategoryId() {
		return assetSubCategoryId;
	}

	public void setAssetSubCategoryId(Long assetSubCategoryId) {
		this.assetSubCategoryId = assetSubCategoryId;
	}


	public String getAssetSubcategory2() {
		return assetSubcategory2;
	}

	public void setAssetSubcategory2(String assetSubcategory2) {
		this.assetSubcategory2 = assetSubcategory2;
	}

	@Override
	public String toString() {
		return "AssetSubRepairType [subCategoryId2=" + subCategoryId2 + ", assetSubCategoryId=" + assetSubCategoryId
				+ ", assetSubcategory2=" + assetSubcategory2 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subCategoryId2 == null) ? 0 : subCategoryId2.hashCode());
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
		AssetSubRepairType other = (AssetSubRepairType) obj;
		if (subCategoryId2 == null) {
			if (other.subCategoryId2 != null)
				return false;
		} else if (!subCategoryId2.equals(other.subCategoryId2))
			return false;
		return true;
	}



	
	
}
