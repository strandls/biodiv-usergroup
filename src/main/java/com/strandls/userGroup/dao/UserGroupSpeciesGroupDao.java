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
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupSpeciesGroupDao extends AbstractDAO<UserGroupSpeciesGroup, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesGroupDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupSpeciesGroupDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupSpeciesGroup findById(Long id) {
		UserGroupSpeciesGroup result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupSpeciesGroup.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupSpeciesGroup> findByUserGroupId(Long userGroupId) {

		List<UserGroupSpeciesGroup> result = new ArrayList<UserGroupSpeciesGroup>();
		String qry = "from UserGroupSpeciesGroup where userGroupId = :ugId";
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupSpeciesGroup> query = session.createQuery(qry);
			query.setParameter("ugIf", userGroupId);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
