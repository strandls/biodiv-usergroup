/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import com.strandls.userGroup.pojo.Newsletter;
import com.strandls.userGroup.pojo.NewsletterWithParentChildRelationship;

/**
 * 
 * @author vilay
 *
 */
public interface NewsletterSerivce {
	
	public Newsletter findById(Long id);
	
	public List<NewsletterWithParentChildRelationship> getByUserGroupAndLanguage(Long userGroupId, Long languageId);
}
