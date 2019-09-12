/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupIbp {

	private Long id;
	private String name;
	private String webAddress;

	/**
	 * @param id
	 * @param name
	 * @param webAddress
	 */
	public UserGroupIbp(Long id, String name, String webAddress) {
		super();
		this.id = id;
		this.name = name;
		this.webAddress = webAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
