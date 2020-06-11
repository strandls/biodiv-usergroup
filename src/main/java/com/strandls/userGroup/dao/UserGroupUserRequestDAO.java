package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupUserJoinRequest;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupUserRequestDAO extends AbstractDAO<UserGroupUserJoinRequest, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupUserRequestDAO.class);
	
	@Inject
	protected UserGroupUserRequestDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupUserJoinRequest findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupUserJoinRequest entity = null;
		try {
			entity = session.get(UserGroupUserJoinRequest.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}
	
	public UserGroupUserJoinRequest checkExistingGroupJoinRequest(Long userId, Long groupId) {
		Session session = sessionFactory.openSession();
		UserGroupUserJoinRequest entity = null;
		String hql = "from UserGroupJoinRequest u where u.userGroupId = :group and u.userId = :user";
		try {
			Query<UserGroupUserJoinRequest> q = session.createQuery(hql);
			q.setParameter("group", groupId);
			q.setParameter("user", userId);
			entity = q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}
	
	public UserGroupUserJoinRequest getGroupJoinRequestByUser(Long userId) {
		Session session = sessionFactory.openSession();
		UserGroupUserJoinRequest entity = null;
		String hql = "from UserGroupJoinRequest u where u.userId = :user";
		try {
			Query<UserGroupUserJoinRequest> q = session.createQuery(hql);
			q.setParameter("user", userId);
			entity = q.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

}
