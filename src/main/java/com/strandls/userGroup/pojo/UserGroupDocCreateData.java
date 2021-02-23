/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

import com.strandls.activity.pojo.MailData;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDocCreateData {

	private Long documentId;
	private List<Long> userGroupIds;
	private MailData mailData;

	/**
	 * 
	 */
	public UserGroupDocCreateData() {
		super();
	}

	/**
	 * @param documentId
	 * @param userGroupIds
	 * @param mailData
	 */
	public UserGroupDocCreateData(Long documentId, List<Long> userGroupIds, MailData mailData) {
		super();
		this.documentId = documentId;
		this.userGroupIds = userGroupIds;
		this.mailData = mailData;
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

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

}
