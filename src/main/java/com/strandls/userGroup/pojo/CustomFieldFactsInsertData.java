package com.strandls.userGroup.pojo;

import com.strandls.activity.pojo.MailData;

public class CustomFieldFactsInsertData {

	private CustomFieldFactsInsert factsCreateData;
	private MailData mailData;

	/**
	 * 
	 */
	public CustomFieldFactsInsertData() {
		super();
	}

	/**
	 * @param factsCreateData
	 * @param mailData
	 */
	public CustomFieldFactsInsertData(CustomFieldFactsInsert factsCreateData, MailData mailData) {
		super();
		this.factsCreateData = factsCreateData;
		this.mailData = mailData;
	}

	public CustomFieldFactsInsert getFactsCreateData() {
		return factsCreateData;
	}

	public void setFactsCreateData(CustomFieldFactsInsert factsCreateData) {
		this.factsCreateData = factsCreateData;
	}

	public MailData getMailData() {
		return mailData;
	}

	public void setMailData(MailData mailData) {
		this.mailData = mailData;
	}

}
