package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupDataTable;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupDataTableDao   extends AbstractDAO<UserGroupDataTable, Long> {
	
	private final Logger logger = LoggerFactory.getLogger(UserGroupHabitatDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupDataTableDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	@Override
	public UserGroupDataTable findById(Long id) {
		UserGroupDataTable result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupDataTable.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserGroupDataTable> findByDataTableId(Long dataTableId) {
		String qry = "from UserGroupDataTable where dataTableId = :dtId";
		Session session = sessionFactory.openSession();
		List<UserGroupDataTable> result = new ArrayList<UserGroupDataTable>();
		try {
			Query<UserGroupDataTable> query = session.createQuery(qry);
			query.setParameter("dtId", dataTableId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	

}
