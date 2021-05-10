/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_documents")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserGroupDocumentCompositeKey.class)
public class UserGroupDocument implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2436948398388938905L;
	private Long userGroupId;
	private Long documentId;

	/**
	 * 
	 */
	public UserGroupDocument() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param documentId
	 */
	public UserGroupDocument(Long userGroupId, Long documentId) {
		super();
		this.userGroupId = userGroupId;
		this.documentId = documentId;
	}

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "document_id")
	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}

}
