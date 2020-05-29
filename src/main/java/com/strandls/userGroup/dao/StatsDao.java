package com.strandls.userGroup.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.Stats;

public class StatsDao {

	private final Logger logger = LoggerFactory.getLogger(StatsDao.class);

	@Inject
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	public Stats fetchStats(Long userGroupId) {
		Stats stats = new Stats();
		Session session = sessionFactory.openSession();

		String obvQry = "select count(*) from user_group_observations where user_group_id = " + userGroupId;
		String docQry = "select count(*) from user_group_documents where user_group_id = " + userGroupId;
		String speciesQry = "SELECT count(*) from public.user_group_species where user_group_id = " + userGroupId;
		String discussionQry = "select count(*) from user_group_discussions where user_group_id = " + userGroupId;
		String actUserQry = "select count(*) from user_group_member_role where user_group_id = " + userGroupId;
		try {
			Query<Object> obvquery = session.createNativeQuery(obvQry);
			Query<Object> docQuery = session.createNativeQuery(docQry);
			Query<Object> speciesQuery = session.createNativeQuery(speciesQry);
			Query<Object> disQuery = session.createNativeQuery(discussionQry);
			Query<Object> actUserQuery = session.createNativeQuery(actUserQry);

			stats.setObservation(Long.parseLong(obvquery.getSingleResult().toString()));
			stats.setDocuments(Long.parseLong(docQuery.getSingleResult().toString()));
			stats.setSpecies(Long.parseLong(speciesQuery.getSingleResult().toString()));
			stats.setDiscussions(Long.parseLong(disQuery.getSingleResult().toString()));
			stats.setActiveUser(Long.parseLong(actUserQuery.getSingleResult().toString()));
			stats.setMaps(203L);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}

		return stats;
	}

}
