/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupSpecies;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 * 
 */
public class UserGroupSpeciesDao extends AbstractDAO<UserGroupSpecies, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupSpeciesDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupSpecies findById(Long id) {
		UserGroupSpecies result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupSpecies.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupSpecies> findBySpeciesId(Long speciesId) {
		String qry = "from  UserGroupSpecies where speciesId = :speciesId";
		Session session = sessionFactory.openSession();
		List<UserGroupSpecies> result = null;
		try {
			Query<UserGroupSpecies> query = session.createQuery(qry);
			query.setParameter("speciesId", speciesId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
