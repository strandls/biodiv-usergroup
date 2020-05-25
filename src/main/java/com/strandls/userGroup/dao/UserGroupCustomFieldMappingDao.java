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

import com.strandls.userGroup.pojo.UserGroupCustomFieldMapping;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupCustomFieldMappingDao extends AbstractDAO<UserGroupCustomFieldMapping, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupCustomFieldMappingDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupCustomFieldMappingDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupCustomFieldMapping findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupCustomFieldMapping result = null;
		try {
			result = session.get(UserGroupCustomFieldMapping.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupCustomFieldMapping> findByUserGroupId(Long userGroupId) {
		Session session = sessionFactory.openSession();
		List<UserGroupCustomFieldMapping> result = null;
		String qry = "from UserGroupCustomFieldMapping where userGroupId = :id";
		try {
			Query<UserGroupCustomFieldMapping> query = session.createQuery(qry);
			query.setParameter("id", userGroupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public UserGroupCustomFieldMapping findByUserGroupCustomFieldId(Long userGroupId, Long customFieldId) {
		Session session = sessionFactory.openSession();
		UserGroupCustomFieldMapping result = null;
		String qry = "from UserGroupCustomFieldMapping where userGroupId = :id and customFieldId = :cfId";
		try {
			Query<UserGroupCustomFieldMapping> query = session.createQuery(qry);
			query.setParameter("id", userGroupId);
			query.setParameter("cfId", customFieldId);
			result = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
