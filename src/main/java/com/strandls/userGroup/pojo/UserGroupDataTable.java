package com.strandls.userGroup.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "user_group_data_tables")
@IdClass(UserGroupDataTableCompositeKey.class)
public class UserGroupDataTable {

	private Long userGroupId;
	private Long dataTableId;

	/**
	 * 
	 */
	public UserGroupDataTable() {
		super();
	}

	/**
	 * 
	 * @param userGroupId
	 * @param dataTableId
	 */
	public UserGroupDataTable(Long userGroupId, Long dataTableId) {
		super();
		this.userGroupId = userGroupId;
		this.dataTableId = dataTableId;
	}

	@Id
	@Column(name = "data_table_id")
	public Long getDataTableId() {
		return dataTableId;
	}

	public void setDataTableId(Long dataTableId) {
		this.dataTableId = dataTableId;
	}

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

}
