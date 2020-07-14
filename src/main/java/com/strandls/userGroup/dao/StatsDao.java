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

		String obvQry = "select count(distinct(o.id)) from observation o join user_group_observations ugo on o.id = ugo.observation_id where o.is_deleted = false and ugo.user_group_id = "
				+ userGroupId;
		String docQry = "select count(distinct(d.id)) from document d join user_group_documents ugd on d.id = ugd.document_id where d.is_deleted = false and ugd.user_group_id = "
				+ userGroupId;
		String speciesQry = "select count(distinct(s.id)) from species s join user_group_species ugs on s.id = ugs.species_id where s.is_deleted = false and ugs.user_group_id = "
				+ userGroupId;
		String discussionQry = "select count(distinct(d.id)) from discussion d join user_group_discussions ugd on d.id = ugd.discussion_id where d.is_deleted = false and ugd.user_group_id = "
				+ userGroupId;
		String actUserQry = "select count(distinct(ugmr.s_user_id)) from user_group ug join user_group_member_role ugmr on ug.id = ugmr.user_group_id where ug.is_deleted = false and ugmr.user_group_id = "
				+ userGroupId;
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
