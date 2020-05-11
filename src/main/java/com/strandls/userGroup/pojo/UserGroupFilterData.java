/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.Date;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupFilterData {

	private Long observationId;
	private Double latitude;
	private Double longitude;
	private Date createdOnDate;
	private Date observedOnDate;
	private Long taxonomyId;

	/**
	 * 
	 */
	public UserGroupFilterData() {
		super();
	}

	/**
	 * @param observationId
	 * @param latitude
	 * @param longitude
	 * @param createdOnDate
	 * @param observedOnDate
	 * @param taxonomyId
	 */
	public UserGroupFilterData(Long observationId, Double latitude, Double longitude, Date createdOnDate,
			Date observedOnDate, Long taxonomyId) {
		super();
		this.observationId = observationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdOnDate = createdOnDate;
		this.observedOnDate = observedOnDate;
		this.taxonomyId = taxonomyId;
	}

	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Date getCreatedOnDate() {
		return createdOnDate;
	}

	public void setCreatedOnDate(Date createdOnDate) {
		this.createdOnDate = createdOnDate;
	}

	public Date getObservedOnDate() {
		return observedOnDate;
	}

	public void setObservedOnDate(Date observedOnDate) {
		this.observedOnDate = observedOnDate;
	}

	public Long getTaxonomyId() {
		return taxonomyId;
	}

	public void setTaxonomyId(Long taxonomyId) {
		this.taxonomyId = taxonomyId;
	}

}
