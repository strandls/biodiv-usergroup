/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_species_group")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupSpeciesGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6436983258689514465L;
	private Long userGroupId;
	private Long speciesGroupId;

	@Id
	@Column(name = "user_group_species_groups_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "species_group_id")
	public Long getSpeciesGroupId() {
		return speciesGroupId;
	}

	public void setSpeciesGroupId(Long speciesGroupId) {
		this.speciesGroupId = speciesGroupId;
	}

}
