/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldValuesData {

	private String fieldTextData;
	private CustomFieldValues singleCategoricalData;
	private List<CustomFieldValues> multipleCategoricalData;
	private String minRange;
	private String maxRange;

	/**
	 * 
	 */
	public CustomFieldValuesData() {
		super();
	}

	/**
	 * @param fieldTextData
	 * @param singleCategoricalData
	 * @param multipleCategoricalData
	 * @param minRange
	 * @param maxRange
	 */
	public CustomFieldValuesData(String fieldTextData, CustomFieldValues singleCategoricalData,
			List<CustomFieldValues> multipleCategoricalData, String minRange, String maxRange) {
		super();
		this.fieldTextData = fieldTextData;
		this.singleCategoricalData = singleCategoricalData;
		this.multipleCategoricalData = multipleCategoricalData;
		this.minRange = minRange;
		this.maxRange = maxRange;
	}

	public String getFieldTextData() {
		return fieldTextData;
	}

	public void setFieldTextData(String fieldTextData) {
		this.fieldTextData = fieldTextData;
	}

	public CustomFieldValues getSingleCategoricalData() {
		return singleCategoricalData;
	}

	public void setSingleCategoricalData(CustomFieldValues singleCategoricalData) {
		this.singleCategoricalData = singleCategoricalData;
	}

	public List<CustomFieldValues> getMultipleCategoricalData() {
		return multipleCategoricalData;
	}

	public void setMultipleCategoricalData(List<CustomFieldValues> multipleCategoricalData) {
		this.multipleCategoricalData = multipleCategoricalData;
	}

	public String getMinRange() {
		return minRange;
	}

	public void setMinRange(String minRange) {
		this.minRange = minRange;
	}

	public String getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(String maxRange) {
		this.maxRange = maxRange;
	}

}
