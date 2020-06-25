/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.Date;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupFilterDate {

	private Date fromDate;
	private Date toDate;

	/**
	 * 
	 */
	public UserGroupFilterDate() {
		super();
	}

	/**
	 * @param fromDate
	 * @param toDate
	 */
	public UserGroupFilterDate(Date fromDate, Date toDate) {
		super();
		this.fromDate = fromDate;
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}
