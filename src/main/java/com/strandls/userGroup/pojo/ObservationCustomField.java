/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;
import java.util.Date;

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
@Table(name = "observation_custom_field")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ObservationCustomField implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3708196921054966277L;
	private Long id;
	private Long authorId;
	private Long observationId;
	private Long userGroupId;
	private Long customFieldId;
	private Long customFieldValueId;
	private Date createdOn;
	private Date lastModified;
	private String valueString;
	private Double valueNumeric;
	private Date valueDate;

	/**
	 * 
	 */
	public ObservationCustomField() {
		super();
	}

	/**
	 * @param id
	 * @param authorId
	 * @param observationId
	 * @param userGroupId
	 * @param customFieldId
	 * @param customFieldValueId
	 * @param createdOn
	 * @param lastModified
	 * @param valueString
	 * @param valueNumeric
	 * @param valueDate
	 */
	public ObservationCustomField(Long id, Long authorId, Long observationId, Long userGroupId, Long customFieldId,
			Long customFieldValueId, Date createdOn, Date lastModified, String valueString, Double valueNumeric,
			Date valueDate) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.observationId = observationId;
		this.userGroupId = userGroupId;
		this.customFieldId = customFieldId;
		this.customFieldValueId = customFieldValueId;
		this.createdOn = createdOn;
		this.lastModified = lastModified;
		this.valueString = valueString;
		this.valueNumeric = valueNumeric;
		this.valueDate = valueDate;
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

	@Column(name = "observation_id")
	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
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

	@Column(name = "custom_field_value_id")
	public Long getCustomFieldValueId() {
		return customFieldValueId;
	}

	public void setCustomFieldValueId(Long customFieldValueId) {
		this.customFieldValueId = customFieldValueId;
	}

	@Column(name = "created_on")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "last_modified")
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	@Column(name = "value_string", columnDefinition = "TEXT")
	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	@Column(name = "value_numeric")
	public Double getValueNumeric() {
		return valueNumeric;
	}

	public void setValueNumeric(Double valueNumeric) {
		this.valueNumeric = valueNumeric;
	}

	@Column(name = "value_date")
	public Date getValueDate() {
		return valueDate;
	}

	public void setValueDate(Date valueDate) {
		this.valueDate = valueDate;
	}

}
