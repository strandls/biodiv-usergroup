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
	private String cfValue;
	private Integer displayOrder;
	private String fieldType;

	/**
	 * 
	 */
	public CustomFieldData() {
		super();
	}

	/**
	 * @param cfId
	 * @param cfName
	 * @param cfValue
	 * @param displayOrder
	 * @param fieldType
	 */
	public CustomFieldData(Long cfId, String cfName, String cfValue, Integer displayOrder, String fieldType) {
		super();
		this.cfId = cfId;
		this.cfName = cfName;
		this.cfValue = cfValue;
		this.displayOrder = displayOrder;
		this.fieldType = fieldType;
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

	public String getCfValue() {
		return cfValue;
	}

	public void setCfValue(String cfValue) {
		this.cfValue = cfValue;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}
