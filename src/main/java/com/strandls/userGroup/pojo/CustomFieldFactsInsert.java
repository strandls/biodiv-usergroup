/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomFieldFactsInsert {

	private Long userGroupId;
	private Long customFieldId;
	private Long observationId;
//	single categorical
	private Long singleCategorical;
//	multiple categorical
	private List<Long> multipleCategorical;

//	Range Data
	private String minValue;
	private String maxValue;

//	Field text Box
	private String textBoxValue;

	/**
	* 
	*/
	public CustomFieldFactsInsert() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param customFieldId
	 * @param observationId
	 * @param singleCategorical
	 * @param multipleCategorical
	 * @param minValue
	 * @param maxValue
	 * @param textBoxValue
	 */
	public CustomFieldFactsInsert(Long userGroupId, Long customFieldId, Long observationId, Long singleCategorical,
			List<Long> multipleCategorical, String minValue, String maxValue, String textBoxValue) {
		super();
		this.userGroupId = userGroupId;
		this.customFieldId = customFieldId;
		this.observationId = observationId;
		this.singleCategorical = singleCategorical;
		this.multipleCategorical = multipleCategorical;
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.textBoxValue = textBoxValue;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getCustomFieldId() {
		return customFieldId;
	}

	public void setCustomFieldId(Long customFieldId) {
		this.customFieldId = customFieldId;
	}

	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}

	public Long getSingleCategorical() {
		return singleCategorical;
	}

	public void setSingleCategorical(Long singleCategorical) {
		this.singleCategorical = singleCategorical;
	}

	public List<Long> getMultipleCategorical() {
		return multipleCategorical;
	}

	public void setMultipleCategorical(List<Long> multipleCategorical) {
		this.multipleCategorical = multipleCategorical;
	}

	public String getMinValue() {
		return minValue;
	}

	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}

	public String getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}

	public String getTextBoxValue() {
		return textBoxValue;
	}

	public void setTextBoxValue(String textBoxValue) {
		this.textBoxValue = textBoxValue;
	}

}
