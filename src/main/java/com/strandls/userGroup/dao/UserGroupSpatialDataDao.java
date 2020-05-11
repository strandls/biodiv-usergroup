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
import com.strandls.userGroup.pojo.UserGroupSpatialData;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupSpatialDataDao extends AbstractDAO<UserGroupSpatialData, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupFilterRuleDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupSpatialDataDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupSpatialData findById(Long id) {
		UserGroupSpatialData result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupSpatialData.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupSpatialData> findByUserGroupId(Long userGroupId) {
		String qry = "from UserGroupSpatialData where userGroupId = :ugId and isEnabled = true";
		Session session = sessionFactory.openSession();
		List<UserGroupSpatialData> result = null;
		try {
			Query<UserGroupSpatialData> query = session.createQuery(qry);
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
