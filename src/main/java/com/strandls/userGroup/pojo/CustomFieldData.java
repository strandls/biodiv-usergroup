/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldData {

	private Long cfId;
	private String cfName;
	private String dataType;
	private String fieldType;
	private String cfIconUrl;
	private String cfNotes;
	private String defaultValue;
	private String units;
	private Integer displayOrder;
	private Boolean allowedParticipation;
	private CustomFieldValuesData customFieldValues;

	/**
	 * 
	 */
	public CustomFieldData() {
		super();
	}

	/**
	 * @param cfId
	 * @param cfName
	 * @param dataType
	 * @param fieldType
	 * @param cfIconUrl
	 * @param cfNotes
	 * @param defaultValue
	 * @param units
	 * @param displayOrder
	 * @param allowedParticipation
	 * @param customFieldValues
	 */
	public CustomFieldData(Long cfId, String cfName, String dataType, String fieldType, String cfIconUrl,
			String cfNotes, String defaultValue, String units, Integer displayOrder, Boolean allowedParticipation,
			CustomFieldValuesData customFieldValues) {
		super();
		this.cfId = cfId;
		this.cfName = cfName;
		this.dataType = dataType;
		this.fieldType = fieldType;
		this.cfIconUrl = cfIconUrl;
		this.cfNotes = cfNotes;
		this.defaultValue = defaultValue;
		this.units = units;
		this.displayOrder = displayOrder;
		this.allowedParticipation = allowedParticipation;
		this.customFieldValues = customFieldValues;
	}

	public Long getCfId() {
		return cfId;
	}

	public void setCfId(Long cfId) {
		this.cfId = cfId;
	}

	public String getCfName() {
		return cfName;
	}

	public void setCfName(String cfName) {
		this.cfName = cfName;
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

	public String getCfIconUrl() {
		return cfIconUrl;
	}

	public void setCfIconUrl(String cfIconUrl) {
		this.cfIconUrl = cfIconUrl;
	}

	public String getCfNotes() {
		return cfNotes;
	}

	public void setCfNotes(String cfNotes) {
		this.cfNotes = cfNotes;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getAllowedParticipation() {
		return allowedParticipation;
	}

	public void setAllowedParticipation(Boolean allowedParticipation) {
		this.allowedParticipation = allowedParticipation;
	}

	public CustomFieldValuesData getCustomFieldValues() {
		return customFieldValues;
	}

	public void setCustomFieldValues(CustomFieldValuesData customFieldValues) {
		this.customFieldValues = customFieldValues;
	}

}
