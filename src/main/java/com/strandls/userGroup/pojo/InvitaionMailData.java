package com.strandls.userGroup.pojo;

import com.strandls.user.pojo.UserIbp;

public class InvitaionMailData {

	private UserIbp user;
	private String emailId;
	private UserGroupIbp userGroup;
	private String role;
	private String token;

	/**
	 * 
	 */
	public InvitaionMailData() {
		super();
	}

	/**
	 * @param user
	 * @param emailId
	 * @param userGroup
	 * @param role
	 * @param token
	 */
	public InvitaionMailData(UserIbp user, String emailId, UserGroupIbp userGroup, String role, String token) {
		super();
		this.user = user;
		this.emailId = emailId;
		this.userGroup = userGroup;
		this.role = role;
		this.token = token;
	}

	public UserIbp getUser() {
		return user;
	}

	public void setUser(UserIbp user) {
		this.user = user;
	}

	public UserGroupIbp getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroupIbp userGroup) {
		this.userGroup = userGroup;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

}
