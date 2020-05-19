/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class BulkGroupUnPostingData {

	private List<Long> userGroupList;
	private List<Long> observationList;

	public BulkGroupUnPostingData() {
		super();
	}

	public BulkGroupUnPostingData(List<Long> userGroupList, List<Long> observationList) {
		super();
		this.userGroupList = userGroupList;
		this.observationList = observationList;
	}

	public List<Long> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Long> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public List<Long> getObservationList() {
		return observationList;
	}

	public void setObservationList(List<Long> observationList) {
		this.observationList = observationList;
	}

}
