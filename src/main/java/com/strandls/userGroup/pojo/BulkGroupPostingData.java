/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class BulkGroupPostingData {

	private List<Long> userGroupList;
	private List<UserGroupObvFilterData> ugObvFilterDataList;

	public BulkGroupPostingData() {
		super();
	}

	public BulkGroupPostingData(List<Long> userGroupList, List<UserGroupObvFilterData> ugObvFilterDataList) {
		super();
		this.userGroupList = userGroupList;
		this.ugObvFilterDataList = ugObvFilterDataList;
	}

	public List<Long> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Long> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public List<UserGroupObvFilterData> getUgObvFilterDataList() {
		return ugObvFilterDataList;
	}

	public void setUgObvFilterDataList(List<UserGroupObvFilterData> ugObvFilterDataList) {
		this.ugObvFilterDataList = ugObvFilterDataList;
	}

}
