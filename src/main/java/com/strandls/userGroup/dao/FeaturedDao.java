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
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class FeaturedDao extends AbstractDAO<Featured, Long> {

	private final Logger logger = LoggerFactory.getLogger(FeaturedDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected FeaturedDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Featured findById(Long id) {
		Session session = sessionFactory.openSession();
		Featured entity = null;
		try {
			entity = session.get(Featured.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<Featured> fetchAllFeatured(String objectType, Long objectId) {
		List<Featured> result = new ArrayList<Featured>();
		Session session = sessionFactory.openSession();
		String qry = "from Featured where objectType = :objectType and objectId = :objectId";
		try {
			Query<Featured> query = session.createQuery(qry);
			query.setParameter("objectType", objectType);
			query.setParameter("objectId", objectId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
