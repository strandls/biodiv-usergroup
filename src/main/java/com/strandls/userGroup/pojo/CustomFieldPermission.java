/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldPermission {

	private Long userGroupId;
	private List<Long> allowedCfId;

	/**
	 * 
	 */
	public CustomFieldPermission() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param allowedCfId
	 */
	public CustomFieldPermission(Long userGroupId, List<Long> allowedCfId) {
		super();
		this.userGroupId = userGroupId;
		this.allowedCfId = allowedCfId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public List<Long> getAllowedCfId() {
		return allowedCfId;
	}

	public void setAllowedCfId(List<Long> allowedCfId) {
		this.allowedCfId = allowedCfId;
	}

}
