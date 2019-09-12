/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberRoleDao extends AbstractDAO<UserGroupMemberRole, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupMemberRole.class);

	/**
	 * @param sessionFactory
	 */

	@Inject
	protected UserGroupMemberRoleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupMemberRole findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupMemberRole entity = null;
		try {
			entity = session.get(UserGroupMemberRole.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMemberRole> getUserGroup(Long sUserId) {

		String qry = "from UserGroupMemberRole where sUserId = :sUserId";
		Session session = sessionFactory.openSession();
		List<UserGroupMemberRole> result = new ArrayList<UserGroupMemberRole>();
		try {
			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("sUserId", sUserId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
