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

import com.strandls.userGroup.pojo.UserGroupDocument;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDocumentDao extends AbstractDAO<UserGroupDocument, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupDocumentDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupDocumentDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupDocument findById(Long id) {
		UserGroupDocument result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupDocument.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupDocument> findByDocumentId(Long documentId) {
		Session session = sessionFactory.openSession();
		String qry = "from UserGroupDocument where documentId = :docId";
		List<UserGroupDocument> result = null;
		try {
			Query<UserGroupDocument> query = session.createQuery(qry);
			query.setParameter("docId", documentId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
