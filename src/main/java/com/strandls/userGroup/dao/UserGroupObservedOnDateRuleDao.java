/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.UserGroupObservedonDateRule;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupObservedOnDateRuleDao extends AbstractDAO<UserGroupObservedonDateRule, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupObservedOnDateRuleDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupObservedOnDateRuleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupObservedonDateRule findById(Long id) {
		UserGroupObservedonDateRule result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupObservedonDateRule.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupObservedonDateRule> findByUserGroupIdIsEnabled(Long userGroupId) {
		String qry = "from UserGroupObservedonDateRule where userGroupId = :ugId and isEnabled = true";
		Session session = sessionFactory.openSession();
		List<UserGroupObservedonDateRule> result = null;
		try {
			Query<UserGroupObservedonDateRule> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupObservedonDateRule> findAllByUserGroupId(Long userGroupId) {
		String qry = "from UserGroupObservedonDateRule where userGroupId = :ugId";
		Session session = sessionFactory.openSession();
		List<UserGroupObservedonDateRule> result = null;
		try {
			Query<UserGroupObservedonDateRule> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
