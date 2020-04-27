package com.strandls.userGroup.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */

@Entity
@Table(name = "user_group_invitaion")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupInvitation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1663810459714154970L;
	private Long id;
	private Long inviterId;
	private Long inviteeId;
	private Long userGroupId;
	private Long roleId;
	private String email;

	/**
	 * 
	 */
	public UserGroupInvitation() {
		super();
	}

	/**
	 * @param id
	 * @param inviterId
	 * @param inviteeId
	 * @param userGroupId
	 * @param roleId
	 * @param email
	 */
	public UserGroupInvitation(Long id, Long inviterId, Long inviteeId, Long userGroupId, Long roleId, String email) {
		super();
		this.id = id;
		this.inviterId = inviterId;
		this.inviteeId = inviteeId;
		this.userGroupId = userGroupId;
		this.roleId = roleId;
		this.email = email;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "inviter_id")
	public Long getInviterId() {
		return inviterId;
	}

	public void setInviterId(Long inviterId) {
		this.inviterId = inviterId;
	}

	@Column(name = "invitee_id")
	public Long getInviteeId() {
		return inviteeId;
	}

	public void setInviteeId(Long inviteeId) {
		this.inviteeId = inviteeId;
	}

	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Column(name = "role_id")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "email")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((inviteeId == null) ? 0 : inviteeId.hashCode());
		result = prime * result + ((inviterId == null) ? 0 : inviterId.hashCode());
		result = prime * result + ((roleId == null) ? 0 : roleId.hashCode());
		result = prime * result + ((userGroupId == null) ? 0 : userGroupId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserGroupInvitation other = (UserGroupInvitation) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (inviteeId == null) {
			if (other.inviteeId != null)
				return false;
		} else if (!inviteeId.equals(other.inviteeId))
			return false;
		if (inviterId == null) {
			if (other.inviterId != null)
				return false;
		} else if (!inviterId.equals(other.inviterId))
			return false;
		if (roleId == null) {
			if (other.roleId != null)
				return false;
		} else if (!roleId.equals(other.roleId))
			return false;
		if (userGroupId == null) {
			if (other.userGroupId != null)
				return false;
		} else if (!userGroupId.equals(other.userGroupId))
			return false;
		return true;
	}

}
