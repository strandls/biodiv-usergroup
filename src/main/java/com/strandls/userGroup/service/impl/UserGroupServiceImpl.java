/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupWKT;
import com.strandls.userGroup.service.UserGroupSerivce;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceImpl implements UserGroupSerivce {

	private final Logger logger = LoggerFactory.getLogger(UserGroupServiceImpl.class);

	@Inject
	private ObjectMapper objectMapper;

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
				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroup);
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				logActivity.LogActivity(description, observationId, observationId, "observation",
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

				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(ug.getUserGroupId());
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				logActivity.LogActivity(description, observationId, observationId, "observation", ug.getUserGroupId(),
						"Removed resoruce");
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups) {
			if (!(previousUserGroup.contains(userGroupId))) {
				UserGroupObservation userGroupMapping = new UserGroupObservation(userGroupId, observationId);
				userGroupObvDao.save(userGroupMapping);

				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				logActivity.LogActivity(description, observationId, observationId, "observation", userGroupId,
						"Posted resource");
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

				UserGroupActivity ugActivity = new UserGroupActivity();
				if (userGroupId != null) {
					UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);

					ugActivity.setUserGroupId(ugIbp.getId());
					ugActivity.setUserGroupName(ugIbp.getName());
					ugActivity.setWebAddress(ugIbp.getWebAddress());
				} else {
					InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("config.properties");

					Properties properties = new Properties();
					try {
						properties.load(in);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					String portalName = properties.getProperty("portalName");
					String portalWebAddress = properties.getProperty("portalAddress");
					in.close();
					ugActivity.setUserGroupId(null);
					ugActivity.setUserGroupName(portalName);
					ugActivity.setWebAddress(portalWebAddress);

				}
				ugActivity.setFeatured(featuredCreate.getNotes());

				String description = null;

				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				logActivity.LogActivity(description, featuredCreate.getObjectId(), featuredCreate.getObjectId(),
						"observation", activityId, "Featured");

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

						UserGroupActivity ugActivity = new UserGroupActivity();
						if (userGroupId != null) {
							UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);

							ugActivity.setUserGroupId(ugIbp.getId());
							ugActivity.setUserGroupName(ugIbp.getName());
							ugActivity.setWebAddress(ugIbp.getWebAddress());
						} else {
							InputStream in = Thread.currentThread().getContextClassLoader()
									.getResourceAsStream("config.properties");

							Properties properties = new Properties();
							try {
								properties.load(in);
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
							String portalName = properties.getProperty("portalName");
							String portalWebAddress = properties.getProperty("portalAddress");
							in.close();
							ugActivity.setUserGroupId(null);
							ugActivity.setUserGroupName(portalName);
							ugActivity.setWebAddress(portalWebAddress);

						}
						ugActivity.setFeatured(featured.getNotes());

						String description = null;
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}

						logActivity.LogActivity(description, objectId, objectId, "observation", activityId,
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

	@Override
	public void filterRule(ObservationLatLon latlon) {
		try {
			List<UserGroup> userGroupList = userGroupDao.findFilterRule();
			GeometryFactory geofactory = new GeometryFactory(new PrecisionModel(), 4326);
			WKTReader reader = new WKTReader(geofactory);
			Coordinate c = new Coordinate(latlon.getLongitude(), latlon.getLatitude());
			Geometry point = geofactory.createPoint(c);

			List<Long> userGroupId = new ArrayList<Long>();

			List<UserGroupObservation> usergroupObv = userGroupObvDao.findByObservationId(latlon.getObservationId());
			if (usergroupObv != null) {
				for (UserGroupObservation ugObv : usergroupObv) {
					userGroupId.add(ugObv.getUserGroupId());
				}
			}
			int previousSize = userGroupId.size();
			for (UserGroup userGroup : userGroupList) {
				UserGroupWKT wkt = objectMapper.readValue(userGroup.getNewFilterRule(), UserGroupWKT.class);
				Geometry groupBoundries = reader.read(wkt.getWkt());
				if (groupBoundries.intersects(point)) {
					if (!(userGroupId.contains(userGroup.getId()))) {
						userGroupId.add(userGroup.getId());
					}
				}
			}
			if (previousSize < userGroupId.size())
				updateUserGroupObservationMapping(latlon.getObservationId(), userGroupId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public void bulkFilterRule(String userGroupIds, List<ObservationLatLon> latlonList) {
		List<UserGroup> userGroupList = null;
		if (userGroupIds == null) {
			userGroupList = userGroupDao.findFilterRule();
		} else {
			userGroupList = userGroupDao.findFilterRuleGroupWise(userGroupIds);
		}
		bulkFilter(userGroupList, latlonList);
	}

	private void bulkFilter(List<UserGroup> userGroupList, List<ObservationLatLon> latlonList) {

		try {
			GeometryFactory geofactory = new GeometryFactory(new PrecisionModel(), 4326);
			WKTReader reader = new WKTReader(geofactory);

			for (ObservationLatLon latlon : latlonList) {
				Coordinate c = new Coordinate(latlon.getLongitude(), latlon.getLatitude());
				Geometry point = geofactory.createPoint(c);
				List<Long> userGroupId = new ArrayList<Long>();

				List<UserGroupObservation> usergroupObv = userGroupObvDao
						.findByObservationId(latlon.getObservationId());
				if (usergroupObv != null) {
					for (UserGroupObservation ugObv : usergroupObv) {
						userGroupId.add(ugObv.getUserGroupId());
					}
				}
				int previousSize = userGroupId.size();
				for (UserGroup userGroup : userGroupList) {
					UserGroupWKT wkt = objectMapper.readValue(userGroup.getNewFilterRule(), UserGroupWKT.class);
					Geometry groupBoundries = reader.read(wkt.getWkt());
					if (groupBoundries.intersects(point)) {
						if (!(userGroupId.contains(userGroup.getId()))) {
							userGroupId.add(userGroup.getId());
						}
					}
				}
				if (previousSize < userGroupId.size())
					updateUserGroupObservationMapping(latlon.getObservationId(), userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public String updateUserGroupFilter(Long userGroupId, UserGroupWKT userGroupWKT) {
		try {
			UserGroup userGroup = userGroupDao.findById(userGroupId);
			String filterRule = objectMapper.writeValueAsString(userGroupWKT);
			userGroup.setNewFilterRule(filterRule);
			userGroupDao.update(userGroup);
			return "User Group Updated with WKT filter";
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

}
