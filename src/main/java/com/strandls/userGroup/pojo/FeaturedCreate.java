/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class FeaturedCreate {

	private String notes;
	private Long objectId;
	private String objectType;
	private List<Long> userGroup;
	private Long languageId;

	/**
	 * 
	 */
	public FeaturedCreate() {
		super();
	}

	/**
	 * @param notes
	 * @param objectId
	 * @param objectType
	 * @param userGroup
	 * @param languageId
	 */
	public FeaturedCreate(String notes, Long objectId, String objectType, List<Long> userGroup, Long languageId) {
		super();
		this.notes = notes;
		this.objectId = objectId;
		this.objectType = objectType;
		this.userGroup = userGroup;
		this.languageId = languageId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public List<Long> getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(List<Long> userGroup) {
		this.userGroup = userGroup;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

}
