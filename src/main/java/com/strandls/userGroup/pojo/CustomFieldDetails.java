/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldDetails {

	private CustomFields customFields;
	private List<CustomFieldValues> cfValues;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private Boolean allowedParticipation;

	/**
	 * 
	 */
	public CustomFieldDetails() {
		super();
	}

	/**
	 * @param customFields
	 * @param cfValues
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param allowedParticipation
	 */
	public CustomFieldDetails(CustomFields customFields, List<CustomFieldValues> cfValues, String defaultValue,
			Integer displayOrder, Boolean isMandatory, Boolean allowedParticipation) {
		super();
		this.customFields = customFields;
		this.cfValues = cfValues;
		this.defaultValue = defaultValue;
		this.displayOrder = displayOrder;
		this.isMandatory = isMandatory;
		this.allowedParticipation = allowedParticipation;
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	public List<CustomFieldValues> getCfValues() {
		return cfValues;
	}

	public void setCfValues(List<CustomFieldValues> cfValues) {
		this.cfValues = cfValues;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getAllowedParticipation() {
		return allowedParticipation;
	}

	public void setAllowedParticipation(Boolean allowedParticipation) {
		this.allowedParticipation = allowedParticipation;
	}

}
