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
@Table(name = "custom_field")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4475656445845117742L;
	private Long id;
	private Long version;
	private Boolean allowedMultiple;
	private Long authorId;
	private String dataType;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private String name;
	private String notes;
	private String options;
	private Long userGroupId;
	private Boolean allowedPaticipation;

	/**
	 * 
	 */
	public CustomField() {
		super();
	}

	/**
	 * @param id
	 * @param version
	 * @param allowedMultiple
	 * @param authorId
	 * @param dataType
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param name
	 * @param notes
	 * @param options
	 * @param userGroupId
	 * @param allowedPaticipation
	 */
	public CustomField(Long id, Long version, Boolean allowedMultiple, Long authorId, String dataType,
			String defaultValue, Integer displayOrder, Boolean isMandatory, String name, String notes, String options,
			Long userGroupId, Boolean allowedPaticipation) {
		super();
		this.id = id;
		this.version = version;
		this.allowedMultiple = allowedMultiple;
		this.authorId = authorId;
		this.dataType = dataType;
		this.defaultValue = defaultValue;
		this.displayOrder = displayOrder;
		this.isMandatory = isMandatory;
		this.name = name;
		this.notes = notes;
		this.options = options;
		this.userGroupId = userGroupId;
		this.allowedPaticipation = allowedPaticipation;
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

	@Column(name = "version")
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(name = "allowed_multiple")
	public Boolean getAllowedMultiple() {
		return allowedMultiple;
	}

	public void setAllowedMultiple(Boolean allowedMultiple) {
		this.allowedMultiple = allowedMultiple;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "data_type")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "default_value")
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

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name = "options")
	public String getOptions() {
		return options;
	}

	public void setOptions(String options) {
		this.options = options;
	}

	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "allowed_participation")
	public Boolean getAllowedPaticipation() {
		return allowedPaticipation;
	}

	public void setAllowedPaticipation(Boolean allowedPaticipation) {
		this.allowedPaticipation = allowedPaticipation;
	}

}
