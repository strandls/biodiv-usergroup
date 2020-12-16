/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class ReorderingHomePage {

	private Long galleryId;
	private Long displayOrder;

	/**
	 * 
	 */
	public ReorderingHomePage() {
		super();
	}

	/**
	 * @param galleryId
	 * @param displayOrder
	 */
	public ReorderingHomePage(Long galleryId, Long displayOrder) {
		super();
		this.galleryId = galleryId;
		this.displayOrder = displayOrder;
	}

	public Long getGalleryId() {
		return galleryId;
	}

	public void setGalleryId(Long galleryId) {
		this.galleryId = galleryId;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

}
