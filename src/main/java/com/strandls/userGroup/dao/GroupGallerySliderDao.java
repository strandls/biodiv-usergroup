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

import com.strandls.userGroup.pojo.GroupGallerySlider;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class GroupGallerySliderDao extends AbstractDAO<GroupGallerySlider, Long> {

	private final Logger logger = LoggerFactory.getLogger(GroupGallerySlider.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected GroupGallerySliderDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public GroupGallerySlider findById(Long id) {
		GroupGallerySlider result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(GroupGallerySlider.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<GroupGallerySlider> findByUsergroupId(Long userGroupId) {
		String qry = "from GroupGallerySlider where ugId = :userGroupId";
		Session session = sessionFactory.openSession();
		List<GroupGallerySlider> result = null;
		try {
			Query<GroupGallerySlider> query = session.createQuery(qry);
			query.setParameter("userGroupId", userGroupId);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;

	}

}
