/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.List;

import com.google.inject.Inject;
import com.strandls.userGroup.dao.NewsletterDao;
import com.strandls.userGroup.pojo.Newsletter;
import com.strandls.userGroup.service.NewsletterSerivce;
import com.strandls.userGroup.util.AbstractService;

/**
 * 
 * @author vilay
 *
 */
public class NewsletterServiceImpl extends AbstractService<Newsletter> implements NewsletterSerivce {

	@Inject
	public NewsletterServiceImpl(NewsletterDao dao) {
		super(dao);
	}

	@Override
	public List<Newsletter> getByUserGroupAndLanguage(Long userGroupId, Long languageId) {
		return ((NewsletterDao) dao).getByUserGroupAndLanguage(userGroupId, languageId);
	}

}
