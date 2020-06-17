/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

import com.strandls.user.pojo.UserIbp;

/**
 * @author Abhishek Rudra
 *
 */
public class AdministrationList {
	
	private List<UserIbp> founderList;
	private List<UserIbp> moderatorList;
	/**
	 * 
	 */
	public AdministrationList() {
		super();
	}
	/**
	 * @param founderList
	 * @param moderatorList
	 */
	public AdministrationList(List<UserIbp> founderList, List<UserIbp> moderatorList) {
		super();
		this.founderList = founderList;
		this.moderatorList = moderatorList;
	}
	public List<UserIbp> getFounderList() {
		return founderList;
	}
	public void setFounderList(List<UserIbp> founderList) {
		this.founderList = founderList;
	}
	public List<UserIbp> getModeratorList() {
		return moderatorList;
	}
	public void setModeratorList(List<UserIbp> moderatorList) {
		this.moderatorList = moderatorList;
	}
	
	

}
