/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupHomePage {

	private Boolean showGallery;
	private Boolean showStats;
	private Boolean showRecentObservation;
	private Boolean showGridMap;
	private Boolean showPartners;
	private Stats stats;

	/**
	 * 
	 */
	public UserGroupHomePage() {
		super();
	}

	/**
	 * @param showGallery
	 * @param showStats
	 * @param showRecentObservation
	 * @param showGridMap
	 * @param showPartners
	 * @param stats
	 */
	public UserGroupHomePage(Boolean showGallery, Boolean showStats, Boolean showRecentObservation, Boolean showGridMap,
			Boolean showPartners, Stats stats) {
		super();
		this.showGallery = showGallery;
		this.showStats = showStats;
		this.showRecentObservation = showRecentObservation;
		this.showGridMap = showGridMap;
		this.showPartners = showPartners;
		this.stats = stats;
	}

	public Boolean getShowGallery() {
		return showGallery;
	}

	public void setShowGallery(Boolean showGallery) {
		this.showGallery = showGallery;
	}

	public Boolean getShowStats() {
		return showStats;
	}

	public void setShowStats(Boolean showStats) {
		this.showStats = showStats;
	}

	public Boolean getShowRecentObservation() {
		return showRecentObservation;
	}

	public void setShowRecentObservation(Boolean showRecentObservation) {
		this.showRecentObservation = showRecentObservation;
	}

	public Boolean getShowGridMap() {
		return showGridMap;
	}

	public void setShowGridMap(Boolean showGridMap) {
		this.showGridMap = showGridMap;
	}

	public Boolean getShowPartners() {
		return showPartners;
	}

	public void setShowPartners(Boolean showPartners) {
		this.showPartners = showPartners;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

}
