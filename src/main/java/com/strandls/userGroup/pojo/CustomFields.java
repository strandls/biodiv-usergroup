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
@Table(name = "custom_fields")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFields implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4600810338054043149L;
	private Long id;
	private Long authorId;
	private String name;
	private String dataType;
	private String fieldType;
	private String units;
	private String iconURL;
	private String notes;

	/**
	 * 
	 */
	public CustomFields() {
		super();
	}

	/**
	 * @param id
	 * @param authorId
	 * @param name
	 * @param dataType
	 * @param fieldType
	 * @param units
	 * @param iconURL
	 * @param notes
	 */
	public CustomFields(Long id, Long authorId, String name, String dataType, String fieldType, String units,
			String iconURL, String notes) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.name = name;
		this.dataType = dataType;
		this.fieldType = fieldType;
		this.units = units;
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

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "data_type")
	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Column(name = "field_type")
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "units")
	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
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
