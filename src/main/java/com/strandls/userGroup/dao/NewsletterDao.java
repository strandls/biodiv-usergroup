/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.Newsletter;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * 
 * @author vilay
 *
 */
public class NewsletterDao extends AbstractDAO<Newsletter, Long> {

	private final Logger logger = LoggerFactory.getLogger(NewsletterDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected NewsletterDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Newsletter findById(Long id) {
		Session session = sessionFactory.openSession();
		Newsletter entity = null;
		try {
			entity = session.get(Newsletter.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<Newsletter> getByUserGroupAndLanguage(Long userGroupId, Long languageId) {
		String queryStr = "from Newsletter t  where ((t.userGroupId is null and :userGroupId is null) or t.userGroupId = :userGroupId) and t.languageId = :languageId and sticky = true order by displayOrder";

		Session session = sessionFactory.openSession();
		Query<Newsletter> query = session.createQuery(queryStr, Newsletter.class);
		query.setParameter("userGroupId", userGroupId);
		query.setParameter("languageId", languageId);

		List<Newsletter> resultList;
		try {
			resultList = query.getResultList();
		} catch (NoResultException e) {
			throw e;
		}

		session.close();
		return resultList;
	}

}
