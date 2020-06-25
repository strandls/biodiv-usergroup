/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupHabitatCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4462096853634308841L;
	private Long habitatId;
	private Long userGroupId;

	public Long getHabitatId() {
		return habitatId;
	}

	public void setHabitatId(Long habitatId) {
		this.habitatId = habitatId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

}
