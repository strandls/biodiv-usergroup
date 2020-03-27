package com.strandls.userGroup.pojo;

import java.util.List;

import com.strandls.activity.pojo.MailData;

public class UserGroupMappingCreateData {

	private MailData mailData;
	private List<Long> userGroups;

	/**
	 * 
	 */
	public UserGroupMappingCreateData() {
		super();
	}

	/**
	 * @param mailData
	 * @param userGroups
	 */
	public UserGroupMappingCreateData(MailData mailData, List<Long> userGroups) {
		super();
		this.mailData = mailData;
		this.userGroups = userGroups;
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

}
