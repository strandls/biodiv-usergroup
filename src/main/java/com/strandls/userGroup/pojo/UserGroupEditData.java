/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupEditData {

	private Boolean allowUserToJoin;
	private String description;
	private String homePage;
	private String icon;
	private String domainName;
	private String name;
	private Double neLatitude;
	private Double neLongitude;
	private Double swLatitude;
	private Double swLongitude;
	private String theme;
	private Long languageId;
	private Boolean sendDigestMail;
	private String newFilterRule;

	/**
	 * 
	 */
	public UserGroupEditData() {
		super();
	}

	/**
	 * @param allowUserToJoin
	 * @param description
	 * @param homePage
	 * @param icon
	 * @param domainName
	 * @param name
	 * @param neLatitude
	 * @param neLongitude
	 * @param swLatitude
	 * @param swLongitude
	 * @param theme
	 * @param languageId
	 * @param sendDigestMail
	 * @param newFilterRule
	 */
	public UserGroupEditData(Boolean allowUserToJoin, String description, String homePage, String icon,
			String domainName, String name, Double neLatitude, Double neLongitude, Double swLatitude,
			Double swLongitude, String theme, Long languageId, Boolean sendDigestMail, String newFilterRule) {
		super();
		this.allowUserToJoin = allowUserToJoin;
		this.description = description;
		this.homePage = homePage;
		this.icon = icon;
		this.domainName = domainName;
		this.name = name;
		this.neLatitude = neLatitude;
		this.neLongitude = neLongitude;
		this.swLatitude = swLatitude;
		this.swLongitude = swLongitude;
		this.theme = theme;
		this.languageId = languageId;
		this.sendDigestMail = sendDigestMail;
		this.newFilterRule = newFilterRule;
	}

	public Boolean getAllowUserToJoin() {
		return allowUserToJoin;
	}

	public void setAllowUserToJoin(Boolean allowUserToJoin) {
		this.allowUserToJoin = allowUserToJoin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNeLatitude() {
		return neLatitude;
	}

	public void setNeLatitude(Double neLatitude) {
		this.neLatitude = neLatitude;
	}

	public Double getNeLongitude() {
		return neLongitude;
	}

	public void setNeLongitude(Double neLongitude) {
		this.neLongitude = neLongitude;
	}

	public Double getSwLatitude() {
		return swLatitude;
	}

	public void setSwLatitude(Double swLatitude) {
		this.swLatitude = swLatitude;
	}

	public Double getSwLongitude() {
		return swLongitude;
	}

	public void setSwLongitude(Double swLongitude) {
		this.swLongitude = swLongitude;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public Boolean getSendDigestMail() {
		return sendDigestMail;
	}

	public void setSendDigestMail(Boolean sendDigestMail) {
		this.sendDigestMail = sendDigestMail;
	}

	public String getNewFilterRule() {
		return newFilterRule;
	}

	public void setNewFilterRule(String newFilterRule) {
		this.newFilterRule = newFilterRule;
	}

}
