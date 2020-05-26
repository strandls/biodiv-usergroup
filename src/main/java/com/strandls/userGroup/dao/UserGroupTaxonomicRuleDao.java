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

import com.strandls.userGroup.pojo.UserGroupTaxonomicRule;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupTaxonomicRuleDao extends AbstractDAO<UserGroupTaxonomicRule, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupTaxonomicRuleDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupTaxonomicRuleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupTaxonomicRule findById(Long id) {
		UserGroupTaxonomicRule result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupTaxonomicRule.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupTaxonomicRule> findByUserGroupIdIsEnabled(Long userGroupId) {
		String qry = "from UserGroupTaxonomicRule where userGroupId = :ugId and isEnabled = true";
		Session session = sessionFactory.openSession();
		List<UserGroupTaxonomicRule> result = null;
		try {
			Query<UserGroupTaxonomicRule> query = session.createQuery(qry);
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
	public List<UserGroupTaxonomicRule> findAllByUserGroupId(Long userGroupId) {
		String qry = "from UserGroupTaxonomicRule where userGroupId = :ugId";
		Session session = sessionFactory.openSession();
		List<UserGroupTaxonomicRule> result = null;
		try {
			Query<UserGroupTaxonomicRule> query = session.createQuery(qry);
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
