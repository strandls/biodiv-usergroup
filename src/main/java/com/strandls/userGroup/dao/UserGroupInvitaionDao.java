package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroupInvitation;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupInvitaionDao extends AbstractDAO<UserGroupInvitation, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupInvitaionDao.class);

	@Inject
	protected UserGroupInvitaionDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupInvitation findById(Long id) {
		UserGroupInvitation result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupInvitation.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public UserGroupInvitation findByUserIdUGId(Long userId, Long userGroupId) {
		String qry = "from UserGroupInvitaion where inviteeId = :userId and userGroupId = :ugId";
		UserGroupInvitation result = null;
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupInvitation> query = session.createQuery(qry);
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

	@SuppressWarnings("unchecked")
	public UserGroupInvitation findByUGIdEmailId(Long ugId, String emailId) {
		String qry = "from UserGroupInvitation where userGroupId = :ugId and email = :emailId";
		Session session = sessionFactory.openSession();
		UserGroupInvitation result = null;
		try {
			Query<UserGroupInvitation> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			query.setParameter("emailId", emailId);
			result = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
