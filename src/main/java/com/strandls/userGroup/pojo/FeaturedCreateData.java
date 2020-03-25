package com.strandls.userGroup.pojo;

import com.strandls.activity.pojo.MailData;

public class FeaturedCreateData {

	private FeaturedCreate featuredCreate;
	private MailData mailData;

	/**
	 * 
	 */
	public FeaturedCreateData() {
		super();
	}

	/**
	 * @param featuredCreate
	 * @param mailData
	 */
	public FeaturedCreateData(FeaturedCreate featuredCreate, MailData mailData) {
		super();
		this.featuredCreate = featuredCreate;
		this.mailData = mailData;
	}

	public FeaturedCreate getFeaturedCreate() {
		return featuredCreate;
	}

	public void setFeaturedCreate(FeaturedCreate featuredCreate) {
		this.featuredCreate = featuredCreate;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

}
