/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import com.strandls.userGroup.pojo.Newsletter;

/**
 * 
 * @author vilay
 *
 */
public interface NewsletterSerivce {
	
	public Newsletter findById(Long id);
	
	public List<Newsletter> getByUserGroupAndLanguage(Long userGroupId, Long languageId);
}
