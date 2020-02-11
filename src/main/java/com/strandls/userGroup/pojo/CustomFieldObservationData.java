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
	private List<CustomFieldData> customFieldData;

	/**
	 * 
	 */
	public CustomFieldObservationData() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param customFieldData
	 */
	public CustomFieldObservationData(Long userGroupId, List<CustomFieldData> customFieldData) {
		super();
		this.userGroupId = userGroupId;
		this.customFieldData = customFieldData;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public List<CustomFieldData> getCustomFieldData() {
		return customFieldData;
	}

	public void setCustomFieldData(List<CustomFieldData> customFieldData) {
		this.customFieldData = customFieldData;
	}

}
