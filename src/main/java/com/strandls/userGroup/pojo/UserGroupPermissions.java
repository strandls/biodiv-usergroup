/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupPermissions {

	private List<UserGroupMemberRole> userMemberRole;
	private List<UserGroupMemberRole> userFeatureRole;

	/**
	 * 
	 */
	public UserGroupPermissions() {
		super();
	}

	/**
	 * @param userMemberRole
	 * @param userFeatureRole
	 */
	public UserGroupPermissions(List<UserGroupMemberRole> userMemberRole, List<UserGroupMemberRole> userFeatureRole) {
		super();
		this.userMemberRole = userMemberRole;
		this.userFeatureRole = userFeatureRole;
	}

	public List<UserGroupMemberRole> getUserMemberRole() {
		return userMemberRole;
	}

	public void setUserMemberRole(List<UserGroupMemberRole> userMemberRole) {
		this.userMemberRole = userMemberRole;
	}

	public List<UserGroupMemberRole> getUserFeatureRole() {
		return userFeatureRole;
	}

	public void setUserFeatureRole(List<UserGroupMemberRole> userFeatureRole) {
		this.userFeatureRole = userFeatureRole;
	}

}
