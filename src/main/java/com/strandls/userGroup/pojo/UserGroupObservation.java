/**
 * 
 */
package com.strandls.userGroup.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_observations")
public class UserGroupObservation {

	private Long userGroupId;
	private Long observationId;

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "observation_id")
	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

}
