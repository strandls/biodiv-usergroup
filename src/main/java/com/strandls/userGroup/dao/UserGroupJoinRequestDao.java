/**
 * 
 */
package com.strandls.userGroup.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupJoinRequest;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupJoinRequestDao extends AbstractDAO<UserGroupJoinRequest, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupJoinRequestDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupJoinRequestDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupJoinRequest findById(Long id) {
		UserGroupJoinRequest result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupJoinRequest.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public UserGroupJoinRequest findByuserIdUGId(Long userId, Long userGroupId) {
		UserGroupJoinRequest result = null;
		String qry = "from UserGroupJoinRequest where userId = :userId and userGroupId = :ugId";
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupJoinRequest> query = session.createQuery(qry);
			query.setParameter("userId", userId);
			query.setParameter("ugId", userGroupId);
			result = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}

		return result;
	}

}
