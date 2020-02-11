/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

}
