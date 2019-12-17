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
	private String icon;
	private String webAddress;

	/**
	 * 
	 */
	public UserGroupIbp() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param icon
	 * @param webAddress
	 */
	public UserGroupIbp(Long id, String name, String icon, String webAddress) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

}
