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
@Table(name = "custom_fields_group_37")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldUG37 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7621167420836878598L;
	private Long observationId;
	private String cf_14501638;
	private String cf_14501655;
	private String cf_14501656;
	private String cf_14501657;
	private String cf_14501658;
	private String cf_14501659;
	private String cf_14501660;
	private String cf_14501661;

	@Id
	@Column(name = "observation_id")
	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

	@Column(name = "cf_14501638")
	public String getCf_14501638() {
		return cf_14501638;
	}

	public void setCf_14501638(String cf_14501638) {
		this.cf_14501638 = cf_14501638;
	}

	@Column(name = "cf_14501655")
	public String getCf_14501655() {
		return cf_14501655;
	}

	public void setCf_14501655(String cf_14501655) {
		this.cf_14501655 = cf_14501655;
	}

	@Column(name = "cf_14501656")
	public String getCf_14501656() {
		return cf_14501656;
	}

	public void setCf_14501656(String cf_14501656) {
		this.cf_14501656 = cf_14501656;
	}

	@Column(name = "cf_14501657")
	public String getCf_14501657() {
		return cf_14501657;
	}

	public void setCf_14501657(String cf_14501657) {
		this.cf_14501657 = cf_14501657;
	}

	@Column(name = "cf_14501658")
	public String getCf_14501658() {
		return cf_14501658;
	}

	public void setCf_14501658(String cf_14501658) {
		this.cf_14501658 = cf_14501658;
	}

	@Column(name = "cf_14501659")
	public String getCf_14501659() {
		return cf_14501659;
	}

	public void setCf_14501659(String cf_14501659) {
		this.cf_14501659 = cf_14501659;
	}

	@Column(name = "cf_14501660")
	public String getCf_14501660() {
		return cf_14501660;
	}

	public void setCf_14501660(String cf_14501660) {
		this.cf_14501660 = cf_14501660;
	}

	@Column(name = "cf_14501661")
	public String getCf_14501661() {
		return cf_14501661;
	}

	public void setCf_14501661(String cf_14501661) {
		this.cf_14501661 = cf_14501661;
	}

}
