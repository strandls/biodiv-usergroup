package com.strandls.userGroup.pojo;

import com.strandls.user.pojo.UserIbp;

public class InvitaionMailData {

	private UserIbp inviterObject;
	private String inviteeName;
	private String inviteeEmail;
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
	 * @param inviterObject
	 * @param inviteeName
	 * @param inviteeEmail
	 * @param userGroup
	 * @param role
	 * @param token
	 */
	public InvitaionMailData(UserIbp inviterObject, String inviteeName, String inviteeEmail, UserGroupIbp userGroup,
			String role, String token) {
		super();
		this.inviterObject = inviterObject;
		this.inviteeName = inviteeName;
		this.inviteeEmail = inviteeEmail;
		this.userGroup = userGroup;
		this.role = role;
		this.token = token;
	}

	public UserIbp getInviterObject() {
		return inviterObject;
	}

	public void setInviterObject(UserIbp inviterObject) {
		this.inviterObject = inviterObject;
	}

	public String getInviteeName() {
		return inviteeName;
	}

	public void setInviteeName(String inviteeName) {
		this.inviteeName = inviteeName;
	}

	public String getInviteeEmail() {
		return inviteeEmail;
	}

	public void setInviteeEmail(String inviteeEmail) {
		this.inviteeEmail = inviteeEmail;
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

}
