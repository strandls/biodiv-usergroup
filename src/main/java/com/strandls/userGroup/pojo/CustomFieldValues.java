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
@Table(name = "custom_field_values")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldValues implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6046908308080401523L;
	private Long id;
	private Long customFieldId;
	private String values;
	private Long authorId;
	private String iconURL;
	private String notes;

	/**
	 * 
	 */
	public CustomFieldValues() {
		super();
	}

	/**
	 * @param id
	 * @param customFieldId
	 * @param values
	 * @param authorId
	 * @param iconURL
	 * @param notes
	 */
	public CustomFieldValues(Long id, Long customFieldId, String values, Long authorId, String iconURL, String notes) {
		super();
		this.id = id;
		this.customFieldId = customFieldId;
		this.values = values;
		this.authorId = authorId;
		this.iconURL = iconURL;
		this.notes = notes;
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

	@Column(name = "custom_field_id")
	public Long getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}

	@Column(name = "value")
	public String getValues() {
		return values;
	}

	public void setValues(String values) {
		this.values = values;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "icon_url")
	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
