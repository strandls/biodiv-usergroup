/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupAddMemebr {

	private List<Long> founderList;
	private List<Long> moderatorList;
	private List<Long> memberList;

	/**
	 * 
	 */
	public UserGroupAddMemebr() {
		super();
	}

	/**
	 * @param founderList
	 * @param moderatorList
	 * @param memberList
	 */
	public UserGroupAddMemebr(List<Long> founderList, List<Long> moderatorList, List<Long> memberList) {
		super();
		this.founderList = founderList;
		this.moderatorList = moderatorList;
		this.memberList = memberList;
	}

	public List<Long> getFounderList() {
		return founderList;
	}

	public void setFounderList(List<Long> founderList) {
		this.founderList = founderList;
	}

	public List<Long> getModeratorList() {
		return moderatorList;
	}

	public void setModeratorList(List<Long> moderatorList) {
		this.moderatorList = moderatorList;
	}

	public List<Long> getMemberList() {
		return memberList;
	}

	public void setMemberList(List<Long> memberList) {
		this.memberList = memberList;
	}

}
