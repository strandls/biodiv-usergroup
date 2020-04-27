/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */
@Entity
@Table(name = "user_group")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9177200176991398786L;

	private Long id;
	private Boolean allowUserToJoin;
	private String description;
	private String domianName;
	private Date foundedOn;
	private String homePage;
	private String icon;
	private Boolean isDeleted;
	private String name;
	private Double neLatitude;
	private Double neLongitude;
	private Double swLatitude;
	private Double swLongitude;
	private String theme;
	private Long visitCount;
	private String webAddress;
	private Long languageId;
	private Boolean sendDigestMail;
	private Date startDate;
	private String filterRule;
	private String newFilterRule;

	/**
	 * 
	 */
	public UserGroup() {
		super();
	}

	/**
	 * @param id
	 * @param allowUserToJoin
	 * @param description
	 * @param domianName
	 * @param foundedOn
	 * @param homePage
	 * @param icon
	 * @param isDeleted
	 * @param name
	 * @param neLatitude
	 * @param neLongitude
	 * @param swLatitude
	 * @param swLongitude
	 * @param theme
	 * @param visitCount
	 * @param webAddress
	 * @param languageId
	 * @param sendDigestMail
	 * @param startDate
	 * @param filterRule
	 * @param newFilterRule
	 */
	public UserGroup(Long id, Boolean allowUserToJoin, String description, String domianName, Date foundedOn,
			String homePage, String icon, Boolean isDeleted, String name, Double neLatitude, Double neLongitude,
			Double swLatitude, Double swLongitude, String theme, Long visitCount, String webAddress, Long languageId,
			Boolean sendDigestMail, Date startDate, String filterRule, String newFilterRule) {
		super();
		this.id = id;
		this.allowUserToJoin = allowUserToJoin;
		this.description = description;
		this.domianName = domianName;
		this.foundedOn = foundedOn;
		this.homePage = homePage;
		this.icon = icon;
		this.isDeleted = isDeleted;
		this.name = name;
		this.neLatitude = neLatitude;
		this.neLongitude = neLongitude;
		this.swLatitude = swLatitude;
		this.swLongitude = swLongitude;
		this.theme = theme;
		this.visitCount = visitCount;
		this.webAddress = webAddress;
		this.languageId = languageId;
		this.sendDigestMail = sendDigestMail;
		this.startDate = startDate;
		this.filterRule = filterRule;
		this.newFilterRule = newFilterRule;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "allow_users_to_join")
	public Boolean getAllowUserToJoin() {
		return allowUserToJoin;
	}

	public void setAllowUserToJoin(Boolean allowUserToJoin) {
		this.allowUserToJoin = allowUserToJoin;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "domain_name")
	public String getDomianName() {
		return domianName;
	}

	public void setDomianName(String domianName) {
		this.domianName = domianName;
	}

	@Column(name = "founded_on")
	public Date getFoundedOn() {
		return foundedOn;
	}

	public void setFoundedOn(Date foundedOn) {
		this.foundedOn = foundedOn;
	}

	@Column(name = "home_page")
	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "is_deleted")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "ne_latitude")
	public Double getNeLatitude() {
		return neLatitude;
	}

	public void setNeLatitude(Double neLatitude) {
		this.neLatitude = neLatitude;
	}

	@Column(name = "ne_longitude")
	public Double getNeLongitude() {
		return neLongitude;
	}

	public void setNeLongitude(Double neLongitude) {
		this.neLongitude = neLongitude;
	}

	@Column(name = "sw_latitude")
	public Double getSwLatitude() {
		return swLatitude;
	}

	public void setSwLatitude(Double swLatitude) {
		this.swLatitude = swLatitude;
	}

	@Column(name = "sw_longitude")
	public Double getSwLongitude() {
		return swLongitude;
	}

	public void setSwLongitude(Double swLongitude) {
		this.swLongitude = swLongitude;
	}

	@Column(name = "theme")
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Column(name = "visit_count")
	public Long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}

	@Column(name = "webaddress")
	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	@Column(name = "language_id")
	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	@Column(name = "send_digest_mail")
	public Boolean getSendDigestMail() {
		return sendDigestMail;
	}

	public void setSendDigestMail(Boolean sendDigestMail) {
		this.sendDigestMail = sendDigestMail;
	}

	@Column(name = "stat_start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "filter_rule")
	public String getFilterRule() {
		return filterRule;
	}

	public void setFilterRule(String filterRule) {
		this.filterRule = filterRule;
	}

	@Column(name = "new_filter_rule", columnDefinition = "TEXT")
	public String getNewFilterRule() {
		return newFilterRule;
	}

	public void setNewFilterRule(String newFilterRule) {
		this.newFilterRule = newFilterRule;
	}

}
