/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDocumentCompositeKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8595157945964107902L;
	private Long userGroupId;
	private Long documentId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

}
