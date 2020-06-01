/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.CustomFieldUG37;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldUG37Dao extends AbstractDAO<CustomFieldUG37, Long> {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldUG37Dao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CustomFieldUG37Dao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CustomFieldUG37 findById(Long id) {
		Session session = sessionFactory.openSession();
		CustomFieldUG37 result = null;
		try {
			result = session.get(CustomFieldUG37.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
