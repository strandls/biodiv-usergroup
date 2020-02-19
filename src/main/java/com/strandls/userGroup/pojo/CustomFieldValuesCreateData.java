/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldValuesCreateData {

	private String value;
	private String iconURL;
	private String notes;

	/**
	 * 
	 */
	public CustomFieldValuesCreateData() {
		super();
	}

	/**
	 * @param value
	 * @param iconURL
	 * @param notes
	 */
	public CustomFieldValuesCreateData(String value, String iconURL, String notes) {
		super();
		this.value = value;
		this.iconURL = iconURL;
		this.notes = notes;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

}
