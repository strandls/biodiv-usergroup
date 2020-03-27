/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldCreateData {

	private String name;
	private String dataType;
	private String fieldType;
	private String iconURL;
	private String notes;
	private String units;

//	userGroup specific Settings

	private Long userGroupId;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private Boolean allowedParticipation;

//	values
	private List<CustomFieldValuesCreateData> values;

	/**
	 * 
	 */
	public CustomFieldCreateData() {
		super();
	}

	/**
	 * @param name
	 * @param dataType
	 * @param fieldType
	 * @param iconURL
	 * @param notes
	 * @param units
	 * @param userGroupId
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param allowedParticipation
	 * @param values
	 */
	public CustomFieldCreateData(String name, String dataType, String fieldType, String iconURL, String notes,
			String units, Long userGroupId, String defaultValue, Integer displayOrder, Boolean isMandatory,
			Boolean allowedParticipation, List<CustomFieldValuesCreateData> values) {
		super();
		this.name = name;
		this.dataType = dataType;
		this.fieldType = fieldType;
		this.iconURL = iconURL;
		this.notes = notes;
		this.units = units;
		this.userGroupId = userGroupId;
		this.defaultValue = defaultValue;
		this.displayOrder = displayOrder;
		this.isMandatory = isMandatory;
		this.allowedParticipation = allowedParticipation;
		this.values = values;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
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

	public List<CustomFieldValuesCreateData> getValues() {
		return values;
	}

	public void setValues(List<CustomFieldValuesCreateData> values) {
		this.values = values;
	}

}
