package com.pms.jpa.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="pm_asset_subcategory1")
public class AssetRepairType implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5088724621482326274L;
	
	@Id	
	@Column(name="subcategory1_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long subCategoryId1;
	
	@Column(name="asset_category_id")
	private Long assetCategoryId;
	
	@Column(name="asset_subcategory1")
	private String assetSubcategory1;

	public Long getSubCategoryId1() {
		return subCategoryId1;
	}

	public void setSubCategoryId1(Long subCategoryId1) {
		this.subCategoryId1 = subCategoryId1;
	}

	public Long getAssetCategoryId() {
		return assetCategoryId;
	}

	public void setAssetCategoryId(Long assetCategoryId) {
		this.assetCategoryId = assetCategoryId;
	}

	
	public String getAssetSubcategory1() {
		return assetSubcategory1;
	}

	public void setAssetSubcategory1(String assetSubcategory1) {
		this.assetSubcategory1 = assetSubcategory1;
	}

	@Override
	public String toString() {
		return "AssetRepairType [subCategoryId1=" + subCategoryId1 + ", assetCategoryId=" + assetCategoryId
				+ ", assetSubcategory1=" + assetSubcategory1 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subCategoryId1 == null) ? 0 : subCategoryId1.hashCode());
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
		AssetRepairType other = (AssetRepairType) obj;
		if (subCategoryId1 == null) {
			if (other.subCategoryId1 != null)
				return false;
		} else if (!subCategoryId1.equals(other.subCategoryId1))
			return false;
		return true;
	}

}
