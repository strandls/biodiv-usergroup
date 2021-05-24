package com.strandls.userGroup.pojo;

import java.io.Serializable;

public class UserGroupDataTableCompositeKey implements Serializable {

	private static final long serialVersionUID = 239849114389298578L;
	private Long userGroupId;
	private Long dataTableId;

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getDataTableId() {
		return dataTableId;
	}

	public void setDataTableId(Long dataTableId) {
		this.dataTableId = dataTableId;
	}

}
