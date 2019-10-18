/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupObservationCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9105275561972582923L;
	private Long userGroupId;
	private Long observationId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

}
