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

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDao extends AbstractDAO<UserGroup, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserGroup findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroup entity = null;
		try {
			entity = session.get(UserGroup.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findFilterRule() {
		Session session = sessionFactory.openSession();
		List<UserGroup> result = null;
		String qry = "from UserGroup where newFilterRule is not null";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findFilterRuleGroupWise(String groupIds) {
		Session session = sessionFactory.openSession();
		List<UserGroup> result = null;
		String qry = "from UserGroup where id in (" + groupIds + ") newFilterRule is not null";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Long getObservationAuthor(String observationId) {
		String qry = "SELECT author_id from observation where id =" + observationId;
		Session session = sessionFactory.openSession();
		try {
			Query<Object> query = session.createNativeQuery(qry);
			Object resultObject = query.getSingleResult();
			Long authorId = Long.parseLong(resultObject.toString());
			return authorId;
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}

}
