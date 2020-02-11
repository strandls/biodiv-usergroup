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
@Table(name = "custom_fields_group_18")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldUG18 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2694922592741621106L;
	private Long observationId;
	private String cf5;
	private String cf6;

	@Id
	@Column(name = "observation_id")
	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

	@Column(name = "cf_5")
	public String getCf5() {
		return cf5;
	}

	public void setCf5(String cf5) {
		this.cf5 = cf5;
	}

	@Column(name = "cf_6")
	public String getCf6() {
		return cf6;
	}

	public void setCf6(String cf6) {
		this.cf6 = cf6;
	}

}
