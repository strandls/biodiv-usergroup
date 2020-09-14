/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class GroupHomePageData {

	private Boolean showGallery;
	private Boolean showStats;
	private Boolean showRecentObservation;
	private Boolean showGridMap;
	private Boolean showPartners;
	private Boolean showDesc;
	private String description;
	private Stats stats;
	private List<GroupGallerySlider> gallerySlider;

	/**
	 * 
	 */
	public GroupHomePageData() {
		super();
	}

	/**
	 * @param showGallery
	 * @param showStats
	 * @param showRecentObservation
	 * @param showGridMap
	 * @param showPartners
	 * @param showDesc
	 * @param description
	 * @param stats
	 * @param gallerySlider
	 */
	public GroupHomePageData(Boolean showGallery, Boolean showStats, Boolean showRecentObservation, Boolean showGridMap,
			Boolean showPartners, Boolean showDesc, String description, Stats stats,
			List<GroupGallerySlider> gallerySlider) {
		super();
		this.showGallery = showGallery;
		this.showStats = showStats;
		this.showRecentObservation = showRecentObservation;
		this.showGridMap = showGridMap;
		this.showPartners = showPartners;
		this.showDesc = showDesc;
		this.description = description;
		this.stats = stats;
		this.gallerySlider = gallerySlider;
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

	public Boolean getShowDesc() {
		return showDesc;
	}

	public void setShowDesc(Boolean showDesc) {
		this.showDesc = showDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Stats getStats() {
		return stats;
	}

	public void setStats(Stats stats) {
		this.stats = stats;
	}

	public List<GroupGallerySlider> getGallerySlider() {
		return gallerySlider;
	}

	public void setGallerySlider(List<GroupGallerySlider> gallerySlider) {
		this.gallerySlider = gallerySlider;
	}

}
