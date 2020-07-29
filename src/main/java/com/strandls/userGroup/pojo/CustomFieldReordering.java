/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldReordering {
	private Long cfId;
	private Long displayOrder;

	/**
	 * 
	 */
	public CustomFieldReordering() {
		super();
	}

	/**
	 * @param cfId
	 * @param displayOrder
	 */
	public CustomFieldReordering(Long cfId, Long displayOrder) {
		super();
		this.cfId = cfId;
		this.displayOrder = displayOrder;
	}

	public Long getCfId() {
		return cfId;
	}

	public void setCfId(Long cfId) {
		this.cfId = cfId;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

}
