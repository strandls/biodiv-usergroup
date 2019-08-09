/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDao extends AbstractDAO<UserGroup, Long>{

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
		Session session =sessionFactory.openSession();
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
	

}
