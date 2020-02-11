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
	private Long customFieldId;
	private Long customFieldValueId;
	private String value;

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
	 * @param customFieldId
	 * @param customFieldValueId
	 * @param value
	 */
	public ObservationCustomField(Long id, Long authorId, Long observationId, Long customFieldId,
			Long customFieldValueId, String value) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.observationId = observationId;
		this.customFieldId = customFieldId;
		this.customFieldValueId = customFieldValueId;
		this.value = value;
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

	@Column(name = "value", columnDefinition = "TEXT")
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
