/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class FilterRule {
	private String fieldName;
	private String ruleName;
	private String ruleValues;

	/**
	 * 
	 */
	public FilterRule() {
		super();
	}

	/**
	 * @param fieldName
	 * @param ruleName
	 * @param ruleValues
	 */
	public FilterRule(String fieldName, String ruleName, String ruleValues) {
		super();
		this.fieldName = fieldName;
		this.ruleName = ruleName;
		this.ruleValues = ruleValues;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	public String getRuleValues() {
		return ruleValues;
	}

	public void setRuleValues(String ruleValues) {
		this.ruleValues = ruleValues;
	}

}
