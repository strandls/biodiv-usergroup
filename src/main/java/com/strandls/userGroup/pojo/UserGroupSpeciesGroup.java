/**
 * 
 */
package com.strandls.userGroup.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_species_group")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserGroupSpeciesGroupCompositeKey.class)
public class UserGroupSpeciesGroup {
	private Long userGroupId;
	private Long speciesGroupId;

	/**
	 * 
	 */
	public UserGroupSpeciesGroup() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param speciesGroupId
	 */
	public UserGroupSpeciesGroup(Long userGroupId, Long speciesGroupId) {
		super();
		this.userGroupId = userGroupId;
		this.speciesGroupId = speciesGroupId;
	}

	@Id
	@Column(name = "user_group_species_groups_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "species_group_id")
	public Long getSpeciesGroupId() {
		return speciesGroupId;
	}

	public void setSpeciesGroupId(Long speciesGroupId) {
		this.speciesGroupId = speciesGroupId;
	}

}
