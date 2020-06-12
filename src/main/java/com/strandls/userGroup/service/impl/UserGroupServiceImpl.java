/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.pojo.UserGroupMailData;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.user.controller.AuthenticationServiceApi;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.GroupAddMember;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserDTO;
import com.strandls.user.pojo.UserGroupMembersCount;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.Headers;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.StatsDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupHabitatDao;
import com.strandls.userGroup.dao.UserGroupInvitaionDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.dao.UserGroupSpeciesGroupDao;
import com.strandls.userGroup.dao.UserGroupUserRequestDAO;
import com.strandls.userGroup.dto.AuthenticationDTO;
import com.strandls.userGroup.filter.MutableHttpServletRequest;
import com.strandls.userGroup.pojo.BulkGroupPostingData;
import com.strandls.userGroup.pojo.BulkGroupUnPostingData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.InvitaionMailData;
import com.strandls.userGroup.pojo.Stats;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupHabitat;
import com.strandls.userGroup.pojo.UserGroupHomePage;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitation;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupObvFilterData;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UserGroupUserJoinRequest;
import com.strandls.userGroup.pojo.UserGroupWKT;
import com.strandls.userGroup.service.UserGroupFilterService;
import com.strandls.userGroup.service.UserGroupSerivce;

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
	private AuthenticationServiceApi authenticationApi;

	@Inject
	private UserGroupInvitaionDao ugInvitationDao;

	@Inject
	private EncryptionUtils encryptionUtils;

	@Inject
	private Headers headers;

	@Inject
	private UserGroupFilterService ugFilterService;

	@Inject
	private StatsDao statsDao;

	@Inject
	private UserGroupHabitatDao ugHabitatDao;

	@Inject
	private UserGroupUserRequestDAO userGroupUserRequestDao;

	@Inject
	private MailUtils mailUtils;

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

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		List<Long> resultList = new ArrayList<Long>();
		for (Long userGroup : userGroups.getUserGroups()) {

			Boolean isEligible = ugFilterService.checkUserGroupEligiblity(userGroup, userId,
					userGroups.getUgFilterData());
			if (isEligible) {
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
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
							observationId, "observation", result.getUserGroupId(), "Posted resource", mailData);
				}
			}

		}
		return resultList;
	}

	@Override
	public List<UserGroupIbp> updateUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGorups) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());

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
				logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
						observationId, "observation", ug.getUserGroupId(), "Removed resoruce", mailData);
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups.getUserGroups()) {
			if (!(previousUserGroup.contains(userGroupId))) {

				Boolean isEligible = ugFilterService.checkUserGroupEligiblity(userGroupId, userId,
						userGorups.getUgFilterData());
				if (isEligible) {
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
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
							observationId, "observation", userGroupId, "Posted resource", mailData);
				}
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
				logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						featuredCreate.getObjectId(), featuredCreate.getObjectId(), "observation", activityId,
						"Featured", mailData);

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
						logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, objectId,
								objectId, "observation", activityId, "UnFeatured", mailData);

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
	public Boolean addMemberRoleInvitaions(HttpServletRequest request, CommonProfile profile,
			UserGroupInvitationData userGroupInvitations) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String serverUrl = properties.getProperty("serverUrl");
			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			List<InvitaionMailData> inviteData = new ArrayList<InvitaionMailData>();
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean isFounder = userService.checkFounderRole(userGroupInvitations.getUserGroupId().toString());
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupInvitations.getUserGroupId());
				if (!userGroupInvitations.getFounderIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getFounderIds()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", null, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getModeratorsIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getModeratorsIds()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", null, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getFounderEmail().isEmpty()) {
					for (String email : userGroupInvitations.getFounderEmail()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, null,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", email, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getModeratorsEmail().isEmpty()) {
					for (String email : userGroupInvitations.getModeratorsEmail()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, null,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", email, userGroupIbp);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				mailUtils.sendInvites(inviteData, serverUrl);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;

	}

	private InvitaionMailData getInvitationMailData(HttpServletRequest request, Long inviterId, Long inviteeId,
			Long userGroupId, Long roleId, String role, String email, UserGroupIbp userGroupIbp) {
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
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
						userGroupId, "userGroup", inviteeId, "Invitation Sent");
			}
//			create mail invitation data
			String ugInviteStr = objectMapper.writeValueAsString(ugInvite);
			UserIbp inviterObject = userService.getUserIbp(inviterId.toString());
			String inviteeName = "";
			String inviteeEmail = "";
			if (inviteeId != null) {
				User inviteeObject = userService.getUser(inviteeId.toString());
				inviteeName = inviteeObject.getName();
				inviteeEmail = inviteeObject.getEmail();
			} else if (email != null) {
				inviteeName = email.split("@")[0];
			}
			if (inviteeEmail != null && inviteeEmail.length() > 0) {
				InvitaionMailData mailData = new InvitaionMailData(inviterObject, inviteeName, inviteeEmail,
						userGroupIbp, role, encryptionUtils.encrypt(ugInviteStr));

				return mailData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean validateMember(HttpServletRequest request, Long userId, String token) {
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
					userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
					Boolean isMember = userService.checkGroupMemberByUserId(ugInviteDB.getUserGroupId().toString(),
							userId.toString());
					if (!isMember) {
						userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
						userService.addMemberRoleUG(ugInviteDB.getUserGroupId().toString(),
								ugInviteDB.getRoleId().toString());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "Joined Group with Role:" + role;
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								ugInviteDB.getUserGroupId(), ugInviteDB.getUserGroupId(), "userGroup", userId,
								"Joined group");

						return true;
					} else {
//						code for role update

						String previousRole = "Member";
						if (ugInviteDB.getRoleId().equals(founderId)) {
							userService = headers.addUserHeader(userService,
									request.getHeader(HttpHeaders.AUTHORIZATION));
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (isFounder)
								return null;
							userService = headers.addUserHeader(userService,
									request.getHeader(HttpHeaders.AUTHORIZATION));
							Boolean isModerator = userService
									.checkModeratorRole(ugInviteDB.getUserGroupId().toString());
							if (isModerator)
								previousRole = "Moderator";
						} else if (ugInviteDB.getRoleId().equals(moderatorId)) {
							userService = headers.addUserHeader(userService,
									request.getHeader(HttpHeaders.AUTHORIZATION));
							Boolean isModerator = userService
									.checkModeratorRole(ugInviteDB.getUserGroupId().toString());
							if (isModerator)
								return null;
							userService = headers.addUserHeader(userService,
									request.getHeader(HttpHeaders.AUTHORIZATION));
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (isFounder)
								previousRole = "Founder";
						} else {
							userService = headers.addUserHeader(userService,
									request.getHeader(HttpHeaders.AUTHORIZATION));
							Boolean isFounder = userService.checkFounderRole(ugInviteDB.getUserGroupId().toString());
							if (!isFounder) {
								userService = headers.addUserHeader(userService,
										request.getHeader(HttpHeaders.AUTHORIZATION));
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
						userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
						userService.removeGroupMember(ugInviteDB.getInviteeId().toString(),
								ugInviteDB.getUserGroupId().toString());

						userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
						userService.addMemberRoleUG(ugInviteDB.getUserGroupId().toString(),
								ugInviteDB.getRoleId().toString());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "User Role Updated from ROLE " + previousRole + " to ROLE " + role;
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								ugInviteDB.getUserGroupId(), ugInviteDB.getUserGroupId(), "userGroup", userId,
								"Role updated");

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
	public Boolean removeUser(HttpServletRequest request, String userGroupId, String userId) {
		try {
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean result = userService.removeGroupMember(userId, userGroupId);
			if (result) {
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null,
						Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", Long.parseLong(userId),
						"Removed user");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean leaveGroup(HttpServletRequest request, Long userId, String userGroupId) {
		try {
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean result = userService.leaveGroup(userGroupId);
			if (result) {
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null,
						Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", userId, "Left Group");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean joinGroup(HttpServletRequest request, Long userId, String userGroupId) {
		try {
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean result = userService.joinGroup(userGroupId);
			if (result) {
				String desc = "Joined Group with Role: Member";
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
						Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", userId, "Joined group");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean sendInvitesForMemberRole(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			List<Long> inviteeList) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			String serverUrl = properties.getProperty("serverUrl");
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			UserGroup userGroup = userGroupDao.findById(userGroupId);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupId);
			List<InvitaionMailData> iniviteData = new ArrayList<InvitaionMailData>();

//			open group any body can send the invitation
			if (userGroup.getAllowUserToJoin().equals(true)) {
				for (Long inviteeId : inviteeList) {
					InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId, userGroupId,
							memberId, "Member", null, userGroupIbp);
					if (mailData != null) {
						iniviteData.add(mailData);
						String desc = "Sent invitation for Role: Member";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								userGroupId, userGroupId, "userGroup", inviteeId, "Invitation Sent");
					}

				}

			} else {
//			closer group check for founder , moderator and admin
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				Boolean isModerator = userService.checkModeratorRole(userGroupId.toString());
				if (roles.contains("ROLE_ADMIN") || isFounder || isModerator) {

					for (Long inviteeId : inviteeList) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId, userGroupId,
								memberId, "Member", null, userGroupIbp);
						if (mailData != null) {
							iniviteData.add(mailData);
							String desc = "Sent invitation for Role: Member";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									userGroupId, userGroupId, "userGroup", inviteeId, "Invitation Sent");
						}
					}
				}
			}
			mailUtils.sendInvites(iniviteData, serverUrl);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean bulkPosting(HttpServletRequest request, CommonProfile profile,
			BulkGroupPostingData bulkGroupPosting) {
		try {

			List<Long> userGroupList = bulkGroupPosting.getUserGroupList();
			List<UserGroupObvFilterData> ugObservationFilterList = bulkGroupPosting.getUgObvFilterDataList();

			if (userGroupList == null || userGroupList.isEmpty() || ugObservationFilterList == null
					|| ugObservationFilterList.isEmpty())
				return false;

			for (Long userGroupId : userGroupList) {

				JSONArray roles = (JSONArray) profile.getAttribute("roles");
				Long userId = Long.parseLong(profile.getId());
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				Boolean isModerator = userService.checkModeratorRole(userGroupId.toString());
				int counter = 0;

				if (roles.contains("ROLE_ADMIN") || isFounder || isModerator) {

					UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
					UserGroupObservation result;
					for (UserGroupObvFilterData ugObvData : ugObservationFilterList) {

						UserGroupObservation isAlreadyMapped = userGroupObvDao
								.checkObservationUGMApping(ugObvData.getObservationId(), userGroupId);
						if (isAlreadyMapped != null)
							continue;

						Boolean isEligible = ugFilterService.checkUserGroupEligiblity(userGroupId, userId, ugObvData);

						if (isEligible) {

							UserGroupObservation ugObv = new UserGroupObservation(userGroupId,
									ugObvData.getObservationId());
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
								logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
										ugObv.getObservationId(), ugObvData.getObservationId(), "observation",
										result.getUserGroupId(), "Posted resource", null);
							}
						}
					}

					if (counter > 0) {
						String description = "Posted " + counter + " Observations to group";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								userGroupId, userGroupId, "userGroup", userGroupId, "Posted resource");
					}
				}
			}
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean bulkRemoving(HttpServletRequest request, CommonProfile profile,
			BulkGroupUnPostingData bulkGroupUnPosting) {
		try {

			List<Long> userGroupList = bulkGroupUnPosting.getUserGroupList();
			List<Long> observationList = bulkGroupUnPosting.getObservationList();

			if (userGroupList == null || userGroupList.isEmpty() || observationList == null
					|| observationList.isEmpty())
				return false;

			for (Long userGroupId : userGroupList) {

				JSONArray roles = (JSONArray) profile.getAttribute("roles");
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
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
							logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, obvId,
									obvId, "observation", result.getUserGroupId(), "Removed resoruce", null);
						}
					}
					if (counter > 0) {
						String description = "Removed " + counter + " Observations from group";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								userGroupId, userGroupId, "userGroup", userGroupId, "Removed resoruce");
					}
				}
			}

			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public UserGroupIbp createUserGroup(HttpServletRequest request, CommonProfile profile,
			UserGroupCreateData ugCreateData) {
		try {
			String webAddress = ugCreateData.getName().replace(" ", "_");
			Long userId = Long.parseLong(profile.getId());

			UserGroup userGroup = new UserGroup(null, true, true, true, ugCreateData.getAllowUserToJoin(),
					ugCreateData.getDescription(), ugCreateData.getDomainName(), new Date(), ugCreateData.getHomePage(),
					ugCreateData.getIcon(), false, ugCreateData.getName(), ugCreateData.getNeLatitude(),
					ugCreateData.getNeLongitude(), ugCreateData.getSwLatitude(), ugCreateData.getSwLongitude(),
					ugCreateData.getTheme(), 1L, webAddress, ugCreateData.getLanguageId(),
					ugCreateData.getSendDigestMail(), new Date(), null, ugCreateData.getNewFilterRule(), true, true,
					true, true, true, true);

			userGroup = userGroupDao.save(userGroup);

			if (ugCreateData.getSpeciesGroup() != null && !ugCreateData.getSpeciesGroup().isEmpty()) {
				for (Long speciesGroupId : ugCreateData.getSpeciesGroup()) {
					UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroup.getId(), speciesGroupId);
					ugSGroupDao.save(ugSpeciesGroup);
				}
			}

			if (ugCreateData.getHabitatId() != null && !ugCreateData.getHabitatId().isEmpty()) {
				for (Long habitatId : ugCreateData.getHabitatId()) {
					UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroup.getId());
					ugHabitatDao.save(ugHabitat);
				}
			}

			if (ugCreateData.getInvitationData() != null) {
				UserGroupInvitationData userGroupInvitations = ugCreateData.getInvitationData();
				userGroupInvitations.setUserGroupId(userGroup.getId());
				if (!userGroupInvitations.getFounderIds().contains(userId))
					userGroupInvitations.getFounderIds().add(userId);
				addMemberRoleInvitaions(request, profile, userGroupInvitations);
			} else {
				UserGroupAddMemebr memberList = new UserGroupAddMemebr(
						new ArrayList<Long>(Arrays.asList(Long.parseLong(profile.getId()))), null, null);
				addMemberDirectly(request, userGroup.getId(), memberList);
			}

			logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null, userGroup.getId(),
					userGroup.getId(), "userGroup", null, "Group created");

			return fetchByGroupIdIbp(userGroup.getId());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserGroupEditData getUGEditData(HttpServletRequest request, CommonProfile profile, Long userGroupId) {
		try {
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				UserGroup userGroup = userGroupDao.findById(userGroupId);
				if (userGroup != null) {
					List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(userGroupId);
					List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(userGroupId);
					List<Long> speciesGroupId = new ArrayList<Long>();
					List<Long> habitatId = new ArrayList<Long>();
					for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
						speciesGroupId.add(ugSpeciesGroup.getSpeciesGroupId());
					}
					for (UserGroupHabitat ugHabitat : ugHabitats) {
						habitatId.add(ugHabitat.getHabitatId());
					}
					UserGroupEditData ugEditData = new UserGroupEditData(userGroup.getAllowUserToJoin(),
							userGroup.getDescription(), userGroup.getHomePage(), userGroup.getIcon(),
							userGroup.getDomianName(), userGroup.getName(), userGroup.getNeLatitude(),
							userGroup.getNeLongitude(), userGroup.getSwLatitude(), userGroup.getSwLongitude(),
							userGroup.getTheme(), userGroup.getLanguageId(), userGroup.getSendDigestMail(),
							userGroup.getNewFilterRule(), speciesGroupId, habitatId);
					return ugEditData;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public UserGroupIbp saveUGEdit(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			UserGroupEditData ugEditData) {
		try {

			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
			Boolean isFounder = userService.checkFounderRole(userGroupId.toString());

			if (roles.contains("ROLE_ADMIN") || isFounder) {

				String webAddress = ugEditData.getName().replace(" ", "_");
				UserGroup ug = userGroupDao.findById(userGroupId);
				UserGroup userGroup = new UserGroup(ug.getId(), true, true, true, ugEditData.getAllowUserToJoin(),
						ugEditData.getDescription(), ugEditData.getDomainName(), new Date(), ugEditData.getHomePage(),
						ugEditData.getIcon(), false, ugEditData.getName(), ugEditData.getNeLatitude(),
						ugEditData.getNeLongitude(), ugEditData.getSwLatitude(), ugEditData.getSwLongitude(),
						ugEditData.getTheme(), ug.getVisitCount(), webAddress, ugEditData.getLanguageId(),
						ugEditData.getSendDigestMail(), new Date(), null, ugEditData.getNewFilterRule(), true, true,
						true, true, true, true);

				userGroup = userGroupDao.update(userGroup);

				List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(userGroupId);
				List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(userGroupId);
				List<Long> speciesGroupList = new ArrayList<Long>();
				List<Long> habitatList = new ArrayList<Long>();
				if (ugSpeciesGroups != null && !ugSpeciesGroups.isEmpty()) {
					for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
						speciesGroupList.add(ugSpeciesGroup.getSpeciesGroupId());
					}
				}
				if (ugHabitats != null && !ugHabitats.isEmpty()) {
					for (UserGroupHabitat ugHabitat : ugHabitats) {
						habitatList.add(ugHabitat.getHabitatId());
					}
				}

				for (Long speciesGroupId : ugEditData.getSpeciesGroupId()) {
					if (!speciesGroupList.contains(speciesGroupId)) {
						UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroup.getId(),
								speciesGroupId);
						ugSGroupDao.save(ugSpeciesGroup);
					}

				}
				for (Long sGroupid : speciesGroupList) {
					if (!ugEditData.getSpeciesGroupId().contains(sGroupid)) {
						UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroup.getId(), sGroupid);
						ugSGroupDao.delete(ugSpeciesGroup);
					}
				}

				for (Long habitatId : ugEditData.getHabitatId()) {
					if (!habitatList.contains(habitatId)) {
						UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroup.getId());
						ugHabitatDao.save(ugHabitat);
					}
				}
				for (long habitatId : habitatList) {
					if (!ugEditData.getHabitatId().contains(habitatId)) {
						UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroupId);
						ugHabitatDao.delete(ugHabitat);
					}
				}

				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null,
						userGroup.getId(), userGroup.getId(), "userGroup", null, "Group updated");

				return fetchByGroupIdIbp(userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean addMemberDirectly(HttpServletRequest request, Long userGroupId, UserGroupAddMemebr memberList) {
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
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Founder";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								userGroupId, userGroupId, "userGroup", userId, "Joined group");
					}
				}
			}
			if (!memberList.getModeratorList().isEmpty()) {
				GroupAddMember groupAddMember = new GroupAddMember();
				groupAddMember.setMemberList(memberList.getModeratorList());
				groupAddMember.setRoleId(moderatorId);
				groupAddMember.setUserGroupId(userGroupId);
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Moderator";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								userGroupId, userGroupId, "userGroup", userId, "Joined group");
					}
				}
			}
			if (!memberList.getMemberList().isEmpty()) {
				GroupAddMember groupAddMember = new GroupAddMember();
				groupAddMember.setMemberList(memberList.getMemberList());
				groupAddMember.setRoleId(memberId);
				groupAddMember.setUserGroupId(userGroupId);
				userService = headers.addUserHeader(userService, request.getHeader(HttpHeaders.AUTHORIZATION));
				List<Long> addedUser = userService.addGroupMemberDirectly(groupAddMember);
				if (addedUser != null && !addedUser.isEmpty()) {
					for (Long userId : addedUser) {
						String desc = "Admin Added user with Role: Member";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								userGroupId, userGroupId, "userGroup", userId, "Joined group");
					}
				}
			}

			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public List<Long> findAllObservation(Long userGroupId) {
		List<Long> observationList = new ArrayList<Long>();
		List<UserGroupObservation> ugObvMappingList = userGroupObvDao.findByUserGroupId(userGroupId);
		for (UserGroupObservation ugObv : ugObvMappingList) {
			observationList.add(ugObv.getObservationId());
		}
		return observationList;
	}

	@Override
	public UserGroupHomePage getUserGroupHomePageData(Long userGroupId) {
		UserGroup userGroup = userGroupDao.findById(userGroupId);
		Stats stats = statsDao.fetchStats(userGroupId);
		UserGroupHomePage result = new UserGroupHomePage(userGroup.getShowGallery(), userGroup.getShowStats(),
				userGroup.getShowRecentObservations(), userGroup.getShowGridMap(), userGroup.getShowPartners(), stats);
		return result;
	}

	@Override
	public Map<String, Object> registerUserProxy(HttpServletRequest request, AuthenticationDTO authDTO) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.signUp(authDTO.getCredentials());
			Long groupId = (authDTO.getGroupId() == null || authDTO.getGroupId().toString().isEmpty())
					? Long.parseLong(authDTO.getGroupId().toString())
					: null;
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				boolean verificationRequired = Boolean.parseBoolean(userData.get("verificationRequired").toString());
				if (!verificationRequired) {
					MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
					mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
							"Bearer " + userData.get("access_token").toString());
					CommonProfile profile = AuthUtil.getProfileFromRequest(request);
					Long user = Long.parseLong(profile.getId());
					joinGroup(mutableRequest, user, String.valueOf(groupId));
				} else {
					Long userId = null;
					if (userData.containsKey("user")) {
						userId = Long.parseLong(((Map<String, Object>) userData.get("user")).get("id").toString());
					}
					if (userId != null) {
						UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao
								.checkExistingGroupJoinRequest(userId, groupId);
						if (joinRequest == null) {
							System.out.println("\n\n**** Inside join request  ****\n\n");
							joinRequest = new UserGroupUserJoinRequest(groupId, userId);
							joinRequest = userGroupUserRequestDao.save(joinRequest);
							System.out.println("\n\n**** Join Request Id: " + joinRequest + "  ****\n\n");							
						}
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		return userData;
	}

	@Override
	public Map<String, Object> signupProxy(HttpServletRequest request, String userName, String password, String mode) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.authenticate(userName, password, mode);
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				boolean verificationRequired = Boolean.parseBoolean(userData.get("verificationRequired").toString());
				if (!verificationRequired) {
					MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
					mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
							"Bearer " + userData.get("access_token").toString());
					CommonProfile profile = AuthUtil.getProfileFromRequest(request);
					Long userId = Long.parseLong(profile.getId());
					UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao.getGroupJoinRequestByUser(userId);
					if (joinRequest != null) {
						joinGroup(mutableRequest, userId, String.valueOf(joinRequest.getUserGroupId()));
						userGroupUserRequestDao.delete(joinRequest);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		return userData;
	}

	@Override
	public Map<String, Object> verifyOTPProxy(HttpServletRequest request, Long id, String otp) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.validateAccount(id, otp);
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
				mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
						"Bearer " + userData.get("access_token").toString());
				CommonProfile profile = AuthUtil.getProfileFromRequest(request);
				Long userId = Long.parseLong(profile.getId());
				UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao.getGroupJoinRequestByUser(userId);
				if (joinRequest != null) {
					joinGroup(mutableRequest, userId, String.valueOf(joinRequest.getUserGroupId()));
					userGroupUserRequestDao.delete(joinRequest);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		return userData;
	}

}
