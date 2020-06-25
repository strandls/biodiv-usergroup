/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupSpeciesGroupCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 239849114389298578L;
	private Long userGroupId;
	private Long speciesGroupId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getSpeciesGroupId() {
		return speciesGroupId;
	}

	public void setSpeciesGroupId(Long speciesGroupId) {
		this.speciesGroupId = speciesGroupId;
	}

}
