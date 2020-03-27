/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_cf_mapping")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupCustomFieldMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7236264523162316728L;

	private Long id;
	private Long authorId;
	private Long userGroupId;
	private Long customFieldId;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private Boolean allowedParticipation;

	/**
	 * 
	 */
	public UserGroupCustomFieldMapping() {
		super();
	}

	/**
	 * @param id
	 * @param authorId
	 * @param userGroupId
	 * @param customFieldId
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param allowedParticipation
	 */
	public UserGroupCustomFieldMapping(Long id, Long authorId, Long userGroupId, Long customFieldId,
			String defaultValue, Integer displayOrder, Boolean isMandatory, Boolean allowedParticipation) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.userGroupId = userGroupId;
		this.customFieldId = customFieldId;
		this.defaultValue = defaultValue;
		this.displayOrder = displayOrder;
		this.isMandatory = isMandatory;
		this.allowedParticipation = allowedParticipation;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "custom_field_id")
	public Long getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}

	@Column(name = "deafult_value")
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Column(name = "display_order")
	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	@Column(name = "is_mandatory")
	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	@Column(name = "allowed_participation")
	public Boolean getAllowedParticipation() {
		return allowedParticipation;
	}

	public void setAllowedParticipation(Boolean allowedParticipation) {
		this.allowedParticipation = allowedParticipation;
	}

}
