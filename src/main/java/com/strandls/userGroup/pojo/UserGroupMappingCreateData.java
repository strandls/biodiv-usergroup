package com.strandls.userGroup.pojo;

import java.util.List;

import com.strandls.activity.pojo.MailData;

public class UserGroupMappingCreateData {

	private MailData mailData;
	private List<Long> userGroups;
	private UserGroupObvFilterData ugFilterData;

	public UserGroupMappingCreateData() {
		super();
	}

	public UserGroupMappingCreateData(MailData mailData, List<Long> userGroups, UserGroupObvFilterData ugFilterData) {
		super();
		this.mailData = mailData;
		this.userGroups = userGroups;
		this.ugFilterData = ugFilterData;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

	public List<Long> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<Long> userGroups) {
		this.userGroups = userGroups;
	}

	public UserGroupObvFilterData getUgFilterData() {
		return ugFilterData;
	}

	public void setUgFilterData(UserGroupObvFilterData ugFilterData) {
		this.ugFilterData = ugFilterData;
	}

}
