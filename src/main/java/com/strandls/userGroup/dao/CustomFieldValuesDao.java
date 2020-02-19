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
import com.strandls.userGroup.pojo.CustomFieldValues;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldValuesDao extends AbstractDAO<CustomFieldValues, Long> {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldValuesDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CustomFieldValuesDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CustomFieldValues findById(Long id) {
		Session session = sessionFactory.openSession();
		CustomFieldValues result = null;
		try {
			result = session.get(CustomFieldValues.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<CustomFieldValues> findByCustomFieldId(Long customFieldId) {
		Session session = sessionFactory.openSession();
		List<CustomFieldValues> result = new ArrayList<CustomFieldValues>();
		String qry = "from CustomFieldValues where customFieldId = :cfId";
		try {
			Query<CustomFieldValues> query = session.createQuery(qry);
			query.setParameter("cfId", customFieldId);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
