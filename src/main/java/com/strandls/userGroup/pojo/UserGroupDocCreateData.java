/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDocCreateData {

	private Long documentId;
	private List<Long> userGroupIds;

	/**
	 * 
	 */
	public UserGroupDocCreateData() {
		super();
	}

	/**
	 * @param documentId
	 * @param userGroupIds
	 */
	public UserGroupDocCreateData(Long documentId, List<Long> userGroupIds) {
		super();
		this.documentId = documentId;
		this.userGroupIds = userGroupIds;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

	public List<Long> getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(List<Long> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

}
