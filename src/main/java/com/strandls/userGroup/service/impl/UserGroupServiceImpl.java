/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.pojo.UserGroupMailData;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.GroupAddMember;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupInvitaionDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.dao.UserGroupSpeciesGroupDao;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.InvitaionMailData;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitation;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UserGroupWKT;
import com.strandls.userGroup.service.CustomFieldServices;
import com.strandls.userGroup.service.UserGroupSerivce;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

import net.minidev.json.JSONArray;

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

	@Inject
	private RabbitMQProducer produce;

	@Inject
	private UserGroupSpeciesGroupDao ugSGroupDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private UserGroupInvitaionDao ugInvitationDao;

	@Inject
	private EncryptionUtils encryptionUtils;

	@Inject
	private CustomFieldServices cfServices;

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
		try {
			for (Long userGroupId : userGroupMember) {
				userGroupList.add(fetchByGroupIdIbp(userGroupId));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return userGroupList;
	}

	@Override
	public List<Long> createUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGroups) {

		List<Long> resultList = new ArrayList<Long>();
		for (Long userGroup : userGroups.getUserGroups()) {
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
				MailData mailData = null;
				if (userGroups.getMailData() != null) {
					mailData = updateMailData(observationId, userGroups.getMailData());
				}
				logActivity.LogActivity(request, description, observationId, observationId, "observation",
						result.getUserGroupId(), "Posted resource", mailData);
			}
		}
		return resultList;
	}

	@Override
	public List<UserGroupIbp> updateUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGorups) {

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupObservation> previousMapping = userGroupObvDao.findByObservationId(observationId);
		for (UserGroupObservation ug : previousMapping) {
			if (!(userGorups.getUserGroups().contains(ug.getUserGroupId()))) {
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

				MailData mailData = updateMailData(observationId, userGorups.getMailData());
				logActivity.LogActivity(request, description, observationId, observationId, "observation",
						ug.getUserGroupId(), "Removed resoruce", mailData);
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups.getUserGroups()) {
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

				MailData mailData = updateMailData(observationId, userGorups.getMailData());
				logActivity.LogActivity(request, description, observationId, observationId, "observation", userGroupId,
						"Posted resource", mailData);
			}
		}
		try {
			produce.setMessage("esmodule", observationId.toString(), "User Groups");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		List<UserGroupIbp> result = fetchByObservationId(observationId);

		return result;
	}

	@Override
	public List<UserGroupIbp> fetchAllUserGroup() {
		List<UserGroupIbp> result = new ArrayList<UserGroupIbp>();
		try {
			List<UserGroup> userGroupList = userGroupDao.findAll();
			List<UserGroupMembersCount> count = userService.getMemberCounts();
			Map<Long, UserGroupIbp> ugMap = new HashMap<Long, UserGroupIbp>();
			UserGroupIbp ibp = null;
			for (UserGroup userGroup : userGroupList) {

				if (userGroup.getDomianName() != null)
					ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(),
							userGroup.getDomianName());
				else {
					String webAddress = "/group/" + userGroup.getWebAddress();
					ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(), webAddress);
				}
				ugMap.put(userGroup.getId(), ibp);
			}
			for (UserGroupMembersCount ugm : count) {
				result.add(ugMap.get(ugm.getUserGroupId()));
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Featured> fetchFeatured(String objectType, Long id) {
		List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, id);
		return featuredList;
	}

	@Override
	public List<Featured> createFeatured(HttpServletRequest request, Long userId,
			FeaturedCreateData featuredCreateData) {

		List<Featured> result = new ArrayList<Featured>();
		try {

			FeaturedCreate featuredCreate = featuredCreateData.getFeaturedCreate();
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

				MailData mailData = updateMailData(featuredCreate.getObjectId(), featuredCreateData.getMailData());
				logActivity.LogActivity(request, description, featuredCreate.getObjectId(),
						featuredCreate.getObjectId(), "observation", activityId, "Featured", mailData);

			}

			result = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(), featuredCreate.getObjectId());
		} catch (

		Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Featured> removeFeatured(HttpServletRequest request, Long userId, String objectType, Long objectId,
			UserGroupMappingCreateData userGroupList) {

		List<Featured> resultList = null;
		try {
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";
			List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, objectId);

			for (Long userGroupId : userGroupList.getUserGroups()) {

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

						MailData mailData = updateMailData(objectId, userGroupList.getMailData());
						logActivity.LogActivity(request, description, objectId, objectId, "observation", activityId,
								"UnFeatured", mailData);

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
	public void filterRule(HttpServletRequest request, ObservationLatLon latlon) {
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
			UserGroupMappingCreateData usergroupMapping = null;
			if (previousSize < userGroupId.size()) {
				usergroupMapping = new UserGroupMappingCreateData(latlon.getMailData(), userGroupId);
				updateUserGroupObservationMapping(request, latlon.getObservationId(), usergroupMapping);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public void bulkFilterRule(HttpServletRequest request, String userGroupIds, List<ObservationLatLon> latlonList) {
		List<UserGroup> userGroupList = null;
		if (userGroupIds == null) {
			userGroupList = userGroupDao.findFilterRule();
		} else {
			userGroupList = userGroupDao.findFilterRuleGroupWise(userGroupIds);
		}
		bulkFilter(request, userGroupList, latlonList);
	}

	private void bulkFilter(HttpServletRequest request, List<UserGroup> userGroupList,
			List<ObservationLatLon> latlonList) {

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
				UserGroupMappingCreateData usergroupMapping = null;
				if (previousSize < userGroupId.size())
					usergroupMapping = new UserGroupMappingCreateData(latlon.getMailData(), userGroupId);
				updateUserGroupObservationMapping(request, latlon.getObservationId(), usergroupMapping);
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

	@Override
	public List<UserGroupSpeciesGroup> getUserGroupSpeciesGroup(Long ugId) {
		List<UserGroupSpeciesGroup> result = ugSGroupDao.findByUserGroupId(ugId);
		return result;
	}

	@Override
	public MailData updateMailData(Long observationId, MailData mailData) {
		List<UserGroupMailData> userGroup = new ArrayList<UserGroupMailData>();
		List<UserGroupIbp> updatedUG = fetchByObservationId(observationId);

		for (UserGroupIbp ug : updatedUG) {
			UserGroupMailData ugMail = new UserGroupMailData();
			ugMail.setIcon(ug.getIcon());
			ugMail.setId(ug.getId());
			ugMail.setName(ug.getName());
			ugMail.setWebAddress(ug.getWebAddress());

			userGroup.add(ugMail);
		}

		mailData.setUserGroupData(userGroup);
		return mailData;
	}

	@Override
	public Boolean addMemberRoleInvitaions(CommonProfile profile, UserGroupInvitationData userGroupInvitations) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			List<InvitaionMailData> inviteData = new ArrayList<InvitaionMailData>();
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = userService.checkFounderRole(userGroupInvitations.getUserGroupId().toString());
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupInvitations.getUserGroupId());
				if (!userGroupInvitations.getFounderIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getFounderIds()) {
						InvitaionMailData mailData = getInvitationMailData(inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", null, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getModeratorsIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getModeratorsIds()) {
						InvitaionMailData mailData = getInvitationMailData(inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", null, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getFounderEmail().isEmpty()) {
					for (String email : userGroupInvitations.getFounderEmail()) {
						InvitaionMailData mailData = getInvitationMailData(inviterId, null,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", email, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getModeratorsEmail().isEmpty()) {
					for (String email : userGroupInvitations.getModeratorsEmail()) {
						InvitaionMailData mailData = getInvitationMailData(inviterId, null,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", email, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
//				LAST STEP
//			TODO	forward the data to send out mails
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;

	}

	private InvitaionMailData getInvitationMailData(Long inviterId, Long inviteeId, Long userGroupId, Long roleId,
			String role, String email, UserGroupIbp userGroupIbp) {
		try {
			if (inviteeId != null) {
				UserGroupInvitation ugInvitee = ugInvitationDao.findByUserIdUGId(inviteeId, userGroupId);
				if (ugInvitee != null)
					ugInvitationDao.delete(ugInvitee);
			}
			if (email != null) {
				UserGroupInvitation ugEmailInvitees = ugInvitationDao.findByUGIdEmailId(userGroupId, email);
				if (ugEmailInvitees != null)
					ugInvitationDao.delete(ugEmailInvitees);
			}
			UserGroupInvitation ugInvite = new UserGroupInvitation(null, inviterId, inviteeId, userGroupId, roleId,
					email);
			ugInvite = ugInvitationDao.save(ugInvite);

			if (ugInvite != null) {
				String desc = "Sent invitation for Role: " + role;
				if (inviteeId == null) {
					String emailsplit[] = email.split("@");
					desc = "Sent Invitaion to a NON-registred user : " + emailsplit[0] + " for role : " + role;
				}
				logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", inviteeId,
						"Invitation Sent");
			}
//			create mail invitation data
			String ugInviteStr = objectMapper.writeValueAsString(ugInvite);
			UserIbp userIbp = null;
			if (inviteeId != null)
				userIbp = userService.getUserIbp(inviteeId.toString());
			InvitaionMailData mailData = new InvitaionMailData(userIbp, email, userGroupIbp, role,
					encryptionUtils.encrypt(ugInviteStr));
			return mailData;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean validateMember(Long userId, String token) {
		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			in.close();

			String plaintext = encryptionUtils.decrypt(token);
			UserGroupInvitation ugInvte = objectMapper.readValue(plaintext, UserGroupInvitation.class);
			if (userId.equals(ugInvte.getInviteeId())) {
				UserGroupInvitation ugInviteDB = ugInvitationDao.findByUserIdUGId(userId, ugInvte.getUserGroupId());
				if (ugInviteDB.equals(ugInvte)) {
					Boolean isMember = userService.checkGroupMemberByUserId(ugInviteDB.getUserGroupId().toString(),
							userId.toString());
					if (!isMember) {
						userService.addMemberRoleUG(ugInviteDB.getUserGroupId().toString(),
								ugInviteDB.getRoleId().toString());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "Joined Group with Role:" + role;
						logActivity.logUserGroupActivities(desc, ugInviteDB.getUserGroupId(),
								ugInviteDB.getUserGroupId(), "userGroup", userId, "Joined group");

						return true;
					} else {
//						code for role update

						String previousRole = "Member";
						if (ugInviteDB.getRoleId().equals(founderId)) {
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (isFounder)
								return null;
							Boolean isModerator = userService
									.checkModeratorRole(ugInviteDB.getUserGroupId().toString());
							if (isModerator)
								previousRole = "Moderator";
						} else if (ugInviteDB.getRoleId().equals(moderatorId)) {
							Boolean isModerator = userService
									.checkModeratorRole(ugInviteDB.getUserGroupId().toString());
							if (isModerator)
								return null;
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (isFounder)
								previousRole = "Founder";
						} else {
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (!isFounder) {
								Boolean isModerator = userService
										.checkModeratorRole(ugInviteDB.getUserGroupId().toString());
								if (!isModerator)
									return null;
								else
									previousRole = "Moderator";

							} else {
								previousRole = "Founder";
							}

						}
						userService.removeGroupMember(ugInviteDB.getInviteeId().toString(),
								ugInviteDB.getUserGroupId().toString());

						userService.addMemberRoleUG(ugInviteDB.getUserGroupId().toString(),
								ugInviteDB.getRoleId().toString());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "User Role Updated from ROLE " + previousRole + " to ROLE " + role;
						logActivity.logUserGroupActivities(desc, ugInviteDB.getUserGroupId(),
								ugInviteDB.getUserGroupId(), "userGroup", userId, "Role updated");

						return true;

					}

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public Boolean removeUser(String userGroupId, String userId) {
		try {
			Boolean result = userService.removeGroupMember(userId, userGroupId);
			if (result) {
				logActivity.logUserGroupActivities(null, Long.parseLong(userGroupId), Long.parseLong(userGroupId),
						"userGroup", Long.parseLong(userId), "Removed user");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean leaveGroup(Long userId, String userGroupId) {
		try {
			Boolean result = userService.leaveGroup(userGroupId);
			if (result) {
				logActivity.logUserGroupActivities(null, Long.parseLong(userGroupId), Long.parseLong(userGroupId),
						"userGroup", userId, "Left Group");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean joinGroup(Long userId, String userGroupId) {
		try {
			Boolean result = userService.joinGroup(userGroupId);
			if (result) {
				String desc = "Joined Group with Role: Member";
				logActivity.logUserGroupActivities(desc, Long.parseLong(userGroupId), Long.parseLong(userGroupId),
						"userGroup", userId, "Joined group");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean sendInvitesForMemberRole(CommonProfile profile, Long userGroupId, List<Long> inviteeList) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			UserGroup userGroup = userGroupDao.findById(userGroupId);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupId);
			List<InvitaionMailData> iniviteData = new ArrayList<InvitaionMailData>();

//			open group any body can send the invitation
			if (userGroup.getAllowUserToJoin().equals(true)) {
				for (Long inviteeId : inviteeList) {
					InvitaionMailData mailData = getInvitationMailData(inviterId, inviteeId, userGroupId, memberId,
							"Member", null, userGroupIbp);
					if (mailData != null) {
						iniviteData.add(mailData);
						String desc = "Sent invitation for Role: Member";
						logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", inviteeId,
								"Invitation Sent");
					}

				}

			} else {
//			closer group check for founder , moderator and admin
				Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
				Boolean isModerator = userService.checkModeratorRole(userGroupId.toString());
				if (roles.contains("ROLE_ADMIN") || isFounder || isModerator) {

					for (Long inviteeId : inviteeList) {
						InvitaionMailData mailData = getInvitationMailData(inviterId, inviteeId, userGroupId, memberId,
								"Member", null, userGroupIbp);
						if (mailData != null) {
							iniviteData.add(mailData);
							String desc = "Sent invitation for Role: Member";
							logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", inviteeId,
									"Invitation Sent");
						}
					}
				}
			}

//			TODO  send mail for invitation

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean bulkPosting(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			List<Long> observationList) {
		try {
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
			Boolean isModerator = userService.checkModeratorRole(userGroupId.toString());
			int counter = 0;
			if (roles.contains("ROLE_ADMIN") || isFounder || isModerator) {

				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
				UserGroupObservation result;
				for (Long obvId : observationList) {
					UserGroupObservation isAlreadyMapped = userGroupObvDao.checkObservationUGMApping(obvId,
							userGroupId);
					if (isAlreadyMapped != null)
						continue;

					UserGroupObservation ugObv = new UserGroupObservation(userGroupId, obvId);
					result = userGroupObvDao.save(ugObv);
					if (result != null) {
						counter++;
						UserGroupActivity ugActivity = new UserGroupActivity();
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
						logActivity.LogActivity(request, description, obvId, obvId, "observation",
								result.getUserGroupId(), "Posted resource", null);
					}
				}

				if (counter > 0) {
					String description = "Posted " + counter + " Observations to group";
					logActivity.logUserGroupActivities(description, userGroupId, userGroupId, "userGroup", userGroupId,
							"Posted resource");
				}
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean bulkRemoving(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			List<Long> observationList) {
		try {
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
			Boolean isModerator = userService.checkModeratorRole(userGroupId.toString());
			int counter = 0;
			if (roles.contains("ROLE_ADMIN") || isFounder || isModerator) {
				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
				UserGroupObservation result;
				for (Long obvId : observationList) {
					UserGroupObservation isAlreadyMapped = userGroupObvDao.checkObservationUGMApping(obvId,
							userGroupId);
					if (isAlreadyMapped == null)
						continue;
					result = userGroupObvDao.delete(isAlreadyMapped);
					if (result != null) {
						counter++;
						UserGroupActivity ugActivity = new UserGroupActivity();
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
						logActivity.LogActivity(request, description, obvId, obvId, "observation",
								result.getUserGroupId(), "Removed resoruce", null);
					}

				}
				if (counter > 0) {
					String description = "Removed " + counter + " Observations from group";
					logActivity.logUserGroupActivities(description, userGroupId, userGroupId, "userGroup", userGroupId,
							"Removed resoruce");
				}
				return true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public UserGroupIbp createUserGroup(CommonProfile profile, UserGroupCreateData ugCreateData) {
		try {
			String webAddress = ugCreateData.getName().replace(" ", "_");
			Long userId = Long.parseLong(profile.getId());

			UserGroup userGroup = new UserGroup(null, ugCreateData.getAllowUserToJoin(), ugCreateData.getDescription(),
					ugCreateData.getDomainName(), new Date(), ugCreateData.getHomePage(), ugCreateData.getIcon(), false,
					ugCreateData.getName(), ugCreateData.getNeLatitude(), ugCreateData.getNeLongitude(),
					ugCreateData.getSwLatitude(), ugCreateData.getSwLongitude(), ugCreateData.getTheme(), 1L,
					webAddress, ugCreateData.getLanguageId(), ugCreateData.getSendDigestMail(), new Date(), null,
					ugCreateData.getNewFilterRule());

			userGroup = userGroupDao.save(userGroup);

			if (ugCreateData.getInvitationData() != null) {
				UserGroupInvitationData userGroupInvitations = ugCreateData.getInvitationData();
				userGroupInvitations.setUserGroupId(userGroup.getId());
				if (!userGroupInvitations.getFounderIds().contains(userId))
					userGroupInvitations.getFounderIds().add(userId);
				addMemberRoleInvitaions(profile, userGroupInvitations);
			}

			if (ugCreateData.getCfUGMappingData() != null)
				cfServices.addCustomFieldUG(profile, userId, userGroup.getId(), ugCreateData.getCfUGMappingData());

			logActivity.logUserGroupActivities(null, userGroup.getId(), userGroup.getId(), "userGroup", null,
					"Group created");

			return fetchByGroupIdIbp(userGroup.getId());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserGroupEditData getUGEditData(CommonProfile profile, Long userGroupId) {
		try {
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				UserGroup userGroup = userGroupDao.findById(userGroupId);
				if (userGroup != null) {
					UserGroupEditData ugEditData = new UserGroupEditData(userGroup.getAllowUserToJoin(),
							userGroup.getDescription(), userGroup.getHomePage(), userGroup.getIcon(),
							userGroup.getDomianName(), userGroup.getName(), userGroup.getNeLatitude(),
							userGroup.getNeLongitude(), userGroup.getSwLatitude(), userGroup.getSwLongitude(),
							userGroup.getTheme(), userGroup.getLanguageId(), userGroup.getSendDigestMail(),
							userGroup.getNewFilterRule());
					return ugEditData;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public UserGroupIbp saveUGEdit(CommonProfile profile, Long userGroupId, UserGroupEditData ugEditData) {
		try {

			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());

			if (roles.contains("ROLE_ADMIN") || isFounder) {

				String webAddress = ugEditData.getName().replace(" ", "_");
				UserGroup userGroup = new UserGroup(null, ugEditData.getAllowUserToJoin(), ugEditData.getDescription(),
						ugEditData.getDomainName(), new Date(), ugEditData.getHomePage(), ugEditData.getIcon(), false,
						ugEditData.getName(), ugEditData.getNeLatitude(), ugEditData.getNeLongitude(),
						ugEditData.getSwLatitude(), ugEditData.getSwLongitude(), ugEditData.getTheme(), 1L, webAddress,
						ugEditData.getLanguageId(), ugEditData.getSendDigestMail(), new Date(), null,
						ugEditData.getNewFilterRule());

				userGroup = userGroupDao.update(userGroup);

				logActivity.logUserGroupActivities(null, userGroup.getId(), userGroup.getId(), "userGroup", null,
						"Group updated");

				return fetchByGroupIdIbp(userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean addMemberDirectly(Long userGroupId, UserGroupAddMemebr memberList) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			in.close();

			if (!memberList.getFounderList().isEmpty()) {
				GroupAddMember groupAddMember = new GroupAddMember();
				groupAddMember.setMemberList(memberList.getFounderList());
				groupAddMember.setRoleId(founderId);
				groupAddMember.setUserGroupId(userGroupId);
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Founder";
						logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", userId,
								"Joined group");
					}
				}
			}
			if (!memberList.getModeratorList().isEmpty()) {
				GroupAddMember groupAddMember = new GroupAddMember();
				groupAddMember.setMemberList(memberList.getModeratorList());
				groupAddMember.setRoleId(moderatorId);
				groupAddMember.setUserGroupId(userGroupId);
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Moderator";
						logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", userId,
								"Joined group");
					}
				}
			}
			if (!memberList.getMemberList().isEmpty()) {
				GroupAddMember groupAddMember = new GroupAddMember();
				groupAddMember.setMemberList(memberList.getMemberList());
				groupAddMember.setRoleId(memberId);
				groupAddMember.setUserGroupId(userGroupId);
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Member";
						logActivity.logUserGroupActivities(desc, userGroupId, userGroupId, "userGroup", userId,
								"Joined group");
					}
				}
			}

			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

}
