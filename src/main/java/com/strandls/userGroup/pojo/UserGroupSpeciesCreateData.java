/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class UserGroupSpeciesCreateData {

	public List<Long> userGroupIds;

	/**
	 * 
	 */
	public UserGroupSpeciesCreateData() {
		super();
	}

	/**
	 * @param userGroupIds
	 */
	public UserGroupSpeciesCreateData(List<Long> userGroupIds) {
		super();
		this.userGroupIds = userGroupIds;
	}

	public List<Long> getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(List<Long> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

}
