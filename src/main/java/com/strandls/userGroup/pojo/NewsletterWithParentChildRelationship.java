package com.strandls.userGroup.pojo;

import java.util.List;

public class NewsletterWithParentChildRelationship {

	private Long id;
	private String title;
	private Long parentId;
	private Integer displayOrder;
	private List<NewsletterWithParentChildRelationship> childs;

	public NewsletterWithParentChildRelationship() {
		super();
	}

	public NewsletterWithParentChildRelationship(Long id, String title, Long parentId, Integer displayOrder,
			List<NewsletterWithParentChildRelationship> childs) {
		super();
		this.id = id;
		this.title = title;
		this.parentId = parentId;
		this.displayOrder = displayOrder;
		this.childs = childs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public List<NewsletterWithParentChildRelationship> getChilds() {
		return childs;
	}

	public void setChilds(List<NewsletterWithParentChildRelationship> childs) {
		this.childs = childs;
	}
}
