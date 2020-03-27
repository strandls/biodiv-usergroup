/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.CustomFieldUG18;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldUG18Dao extends AbstractDAO<CustomFieldUG18, Long> {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldUG18Dao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CustomFieldUG18Dao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CustomFieldUG18 findById(Long id) {
		Session session = sessionFactory.openSession();
		CustomFieldUG18 result = null;
		try {
			result = session.get(CustomFieldUG18.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
