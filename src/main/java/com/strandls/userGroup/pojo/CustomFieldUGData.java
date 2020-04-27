/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldUGData {

	private Long customFieldId;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private Boolean allowedParticipation;

	/**
	 * 
	 */
	public CustomFieldUGData() {
		super();
	}

	/**
	 * @param customFieldId
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param allowedParticipation
	 */
	public CustomFieldUGData(Long customFieldId, String defaultValue, Integer displayOrder, Boolean isMandatory,
			Boolean allowedParticipation) {
		super();
		this.customFieldId = customFieldId;
		this.defaultValue = defaultValue;
		this.displayOrder = displayOrder;
		this.isMandatory = isMandatory;
		this.allowedParticipation = allowedParticipation;
	}

	public Long getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
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
