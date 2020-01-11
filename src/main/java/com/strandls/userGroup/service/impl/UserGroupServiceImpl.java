/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceImpl implements UserGroupSerivce {

	private final Logger logger = LoggerFactory.getLogger(UserGroupServiceImpl.class);

	@Inject
	private LogActivities logActivity;

	@Inject
	private UserGroupDao userGroupDao;

	@Inject
	private UserGroupObservationDao userGroupObvDao;

	@Inject
	private FeaturedDao featuredDao;

	@Override
	public UserGroup fetchByGroupId(Long id) {
		UserGroup userGroup = userGroupDao.findById(id);
		return userGroup;
	}

	@Override
	public UserGroupIbp fetchByGroupIdIbp(Long id) {
		UserGroup ug = userGroupDao.findById(id);
		UserGroupIbp ibp;
		if (ug.getDomianName() != null)
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), ug.getDomianName());
		else {
			String webAddress = "/group/" + ug.getWebAddress();
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), webAddress);
		}
		return ibp;
	}

	@Override
	public List<UserGroupIbp> fetchByObservationId(Long id) {
		List<UserGroupObservation> userGroupObv = userGroupObvDao.findByObservationId(id);
		List<UserGroupIbp> userGroup = new ArrayList<UserGroupIbp>();
		for (UserGroupObservation ugObv : userGroupObv) {
			userGroup.add(fetchByGroupIdIbp(ugObv.getUserGroupId()));
		}
		return userGroup;
	}

	@Override
	public List<UserGroupIbp> fetchByUserGroupDetails(List<Long> userGroupMember) {
		List<UserGroupIbp> userGroupList = new ArrayList<UserGroupIbp>();
		for (Long userGroupId : userGroupMember) {
			userGroupList.add(fetchByGroupIdIbp(userGroupId));
		}
		return userGroupList;
	}

	@Override
	public List<Long> createUserGroupObservationMapping(Long observationId, List<Long> userGroups) {

		List<Long> resultList = new ArrayList<Long>();

		for (Long userGroup : userGroups) {
			UserGroupObservation userGroupObs = new UserGroupObservation(userGroup, observationId);
			UserGroupObservation result = userGroupObvDao.save(userGroupObs);
			if (result != null) {
				resultList.add(result.getUserGroupId());
				logActivity.LogActivity("Posted observation to group", observationId, observationId, "observation",
						result.getUserGroupId(), "Posted resource");
			}
		}
		return resultList;
	}

	@Override
	public List<UserGroupIbp> updateUserGroupObservationMapping(Long observationId, List<Long> userGorups) {

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupObservation> previousMapping = userGroupObvDao.findByObservationId(observationId);
		for (UserGroupObservation ug : previousMapping) {
			if (!(userGorups.contains(ug.getUserGroupId()))) {
				userGroupObvDao.delete(ug);
				logActivity.LogActivity("Removed observation from group", observationId, observationId, "observation",
						ug.getUserGroupId(), "Removed resoruce");
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups) {
			if (!(previousUserGroup.contains(userGroupId))) {
				UserGroupObservation userGroupMapping = new UserGroupObservation(userGroupId, observationId);
				userGroupObvDao.save(userGroupMapping);
				logActivity.LogActivity("Posted observation to group", observationId, observationId, "observation",
						userGroupId, "Posted resource");
			}
		}

		List<UserGroupIbp> result = fetchByObservationId(observationId);

		return result;
	}

	@Override
	public List<UserGroupIbp> fetchAllUserGroup() {
		List<UserGroup> userGroupList = userGroupDao.findAll();
		List<UserGroupIbp> result = new ArrayList<UserGroupIbp>();
		UserGroupIbp ibp = null;
		for (UserGroup userGroup : userGroupList) {

			if (userGroup.getDomianName() != null)
				ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(),
						userGroup.getDomianName());
			else {
				String webAddress = "/group/" + userGroup.getWebAddress();
				ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(), webAddress);
			}
			result.add(ibp);
		}
		return result;
	}

	@Override
	public List<Featured> fetchFeatured(String objectType, Long id) {
		List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, id);
		return featuredList;
	}

	@Override
	public List<Featured> createFeatured(Long userId, FeaturedCreate featuredCreate) {

		List<Featured> result = new ArrayList<Featured>();
		try {

			Featured featured;
			if (featuredCreate.getObjectType().equalsIgnoreCase("observation"))
				featuredCreate.setObjectType("species.participation.Observation");

			List<Featured> featuredList = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(),
					featuredCreate.getObjectId());

			for (Long userGroupId : featuredCreate.getUserGroup()) {

				int flag = 0;
				for (Featured alreadyFeatured : featuredList) {
					if (alreadyFeatured.getUserGroup() == userGroupId) {
						alreadyFeatured.setCreatedOn(new Date());
						alreadyFeatured.setNotes(featuredCreate.getNotes());
						alreadyFeatured.setAuthorId(userId);
						featuredDao.update(alreadyFeatured);
						flag = 1;
					}
				}

				if (flag == 0) {
					featured = new Featured(null, 0L, userId, new Date(), featuredCreate.getNotes(),
							featuredCreate.getObjectId(), featuredCreate.getObjectType(), userGroupId, 205L, null);
					featured = featuredDao.save(featured);

				}

				Long activityId = userGroupId;
				if (userGroupId == null)
					activityId = featuredCreate.getObjectId();

				logActivity.LogActivity(featuredCreate.getNotes(), featuredCreate.getObjectId(),
						featuredCreate.getObjectId(), "observation", activityId, "Featured");

			}

			result = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(), featuredCreate.getObjectId());
		} catch (

		Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Featured> removeFeatured(Long userId, String objectType, Long objectId, List<Long> userGroupList) {

		List<Featured> resultList = null;
		try {
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";
			List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, objectId);

			for (Long userGroupId : userGroupList) {

				for (Featured featured : featuredList) {
					if (featured.getUserGroup() == userGroupId) {
						featuredDao.delete(featured);
						Long activityId = userGroupId;
						if (userGroupId == null)
							activityId = objectId;
						logActivity.LogActivity(featured.getNotes(), objectId, objectId, "observation", activityId,
								"UnFeatured");

						break;
					}
				}
			}

			resultList = featuredDao.fetchAllFeatured(objectType, objectId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}

}
