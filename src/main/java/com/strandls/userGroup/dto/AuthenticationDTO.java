package com.strandls.userGroup.dto;

import com.strandls.user.pojo.UserDTO;

public class AuthenticationDTO {
	
	private UserDTO credentials;
	private Long groupId;
	
	public UserDTO getCredentials() {
		return credentials;
	}
	public void setCredentials(UserDTO credentials) {
		this.credentials = credentials;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
	@Override
	public String toString() {
		return "AuthenticationDTO [credentials=" + credentials + ", groupId=" + groupId + "]";
	}

}
