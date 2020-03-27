/**
 * 
 */
package com.strandls.userGroup.pojo;

import com.strandls.activity.pojo.MailData;

/**
 * @author Abhishek Rudra
 *
 */
public class ObservationLatLon {

	private Long observationId;
	private Double latitude;
	private Double longitude;
	private MailData mailData;

	/**
	 * 
	 */
	public ObservationLatLon() {
		super();
	}

	/**
	 * @param observationId
	 * @param latitude
	 * @param longitude
	 * @param mailData
	 */
	public ObservationLatLon(Long observationId, Double latitude, Double longitude, MailData mailData) {
		super();
		this.observationId = observationId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.mailData = mailData;
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

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

	

}
