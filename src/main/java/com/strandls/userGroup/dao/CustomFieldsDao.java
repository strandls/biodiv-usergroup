/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.CustomFields;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldsDao extends AbstractDAO<CustomFields, Long> {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldsDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CustomFieldsDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CustomFields findById(Long id) {
		Session session = sessionFactory.openSession();
		CustomFields result = null;
		try {
			result = session.get(CustomFields.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
