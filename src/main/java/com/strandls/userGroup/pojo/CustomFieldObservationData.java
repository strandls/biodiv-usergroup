/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldObservationData {

	private Long userGroupId;
	private List<CustomFieldData> customField;

	/**
	 * 
	 */
	public CustomFieldObservationData() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param customField
	 */
	public CustomFieldObservationData(Long userGroupId, List<CustomFieldData> customField) {
		super();
		this.userGroupId = userGroupId;
		this.customField = customField;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public List<CustomFieldData> getCustomField() {
		return customField;
	}

	public void setCustomField(List<CustomFieldData> customField) {
		this.customField = customField;
	}

}
