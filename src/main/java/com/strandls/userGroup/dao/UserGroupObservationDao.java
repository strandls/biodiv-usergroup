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

import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupObservationDao extends AbstractDAO<UserGroupObservation, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupObservationDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupObservationDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupObservation findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupObservation entity = null;
		try {
			entity = session.get(UserGroupObservation.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupObservation> findByObservationId(Long id) {
		String qry = "from UserGroupObservation where observationId = :id";
		List<UserGroupObservation> entity = null;
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupObservation> query = session.createQuery(qry);
			query.setParameter("id", id);
			entity = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public UserGroupObservation checkObservationUGMApping(Long obvId, Long userGroupId) {
		String qry = "from UserGroupObservation where observationId = :obvId and userGroupId = :ugId";
		UserGroupObservation result = null;
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupObservation> query = session.createQuery(qry);
			query.setParameter("obvId", obvId);
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
