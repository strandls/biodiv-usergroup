/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class UserGroupSpeciesCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5685653363535582991L;
	private Long userGroupId;
	private Long speciesId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Long speciesId) {
		this.speciesId = speciesId;
	}

}
