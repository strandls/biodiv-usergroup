/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupIbp {

	private String name;
	private String webAddress;
	

	/**
	 * @param name
	 * @param webAddress
	 */
	public UserGroupIbp(String name, String webAddress) {
		super();
		this.name = name;
		this.webAddress = webAddress;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

}
