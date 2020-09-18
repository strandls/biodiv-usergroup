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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.taxonomy.controllers.TaxonomyServicesApi;
import com.strandls.taxonomy.pojo.BreadCrumb;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.userGroup.TokenGenerator;
import com.strandls.userGroup.dao.UserGroupCreatedOnDateRuleDao;
import com.strandls.userGroup.dao.UserGroupFilterRuleDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.dao.UserGroupObservedOnDateRuleDao;
import com.strandls.userGroup.dao.UserGroupSpatialDataDao;
import com.strandls.userGroup.dao.UserGroupTaxonomicRuleDao;
import com.strandls.userGroup.pojo.ShowFilterRule;
import com.strandls.userGroup.pojo.UserGroupCreatedOnDateRule;
import com.strandls.userGroup.pojo.UserGroupFilterDate;
import com.strandls.userGroup.pojo.UserGroupFilterEnable;
import com.strandls.userGroup.pojo.UserGroupFilterRemove;
import com.strandls.userGroup.pojo.UserGroupFilterRule;
import com.strandls.userGroup.pojo.UserGroupFilterRuleInputData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupObservedonDateRule;
import com.strandls.userGroup.pojo.UserGroupObvFilterData;
import com.strandls.userGroup.pojo.UserGroupSpatialData;
import com.strandls.userGroup.pojo.UserGroupTaxonomicRule;
import com.strandls.userGroup.service.UserGroupFilterService;
import com.strandls.userGroup.service.UserGroupMemberService;
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
public class UserGroupFilterServiceImpl implements UserGroupFilterService {

	private final Logger logger = LoggerFactory.getLogger(UserGroupFilterServiceImpl.class);

	@Inject
	private UserGroupFilterRuleDao ugFilterRuleDao;

	@Inject
	private UserGroupObservedOnDateRuleDao ugObservedDateDao;

	@Inject
	private UserGroupCreatedOnDateRuleDao ugCreatedDateDao;

	@Inject
	private UserGroupTaxonomicRuleDao ugtaxonomicDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private UserGroupSpatialDataDao ugSpatialDao;

	@Inject
	private UserGroupSerivce ugService;

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private UserGroupObservationDao ugObvDao;

	@Inject
	private TaxonomyServicesApi taxonomyService;

	@Inject
	private TokenGenerator tokenGenerator;

	@Inject
	private LogActivities logActivity;

	@Inject
	private UserGroupMemberService ugMemberSerivce;

//	check observed on date filter
	private Boolean checkObservedOnDateFilter(Long userGroupId, Date observedOnDate) {
		List<UserGroupObservedonDateRule> observedDateData = ugObservedDateDao.findByUserGroupIdIsEnabled(userGroupId);
		if (observedDateData != null && !observedDateData.isEmpty()) {
			for (UserGroupObservedonDateRule observedDateRange : observedDateData) {
				if (observedDateRange.getToDate() == null) {
					if (observedOnDate.compareTo(observedDateRange.getFromDate()) > 0)
						return true;
				} else if (observedDateRange.getFromDate() == null) {
					if (observedOnDate.compareTo(observedDateRange.getToDate()) < 0)
						return true;
				} else {
//					both toDate and fromDate are not null
					if (observedOnDate.compareTo(observedDateRange.getFromDate()) > 0
							&& observedOnDate.compareTo(observedDateRange.getToDate()) < 0)
						return true;
				}
			}
		}
		return false;
	}

//	check created on date filter
	private Boolean checkCreatedOnDateFilter(Long userGroupId, Date createdOnDate) {
		List<UserGroupCreatedOnDateRule> createdDataList = ugCreatedDateDao.findByUserGroupIdIsEnabled(userGroupId);
		if (createdDataList != null && !createdDataList.isEmpty()) {
			for (UserGroupCreatedOnDateRule createdDateRange : createdDataList) {
				if (createdDateRange.getToDate() == null) {
					if (createdOnDate.compareTo(createdDateRange.getFromDate()) > 0)
						return true;
				} else if (createdDateRange.getFromDate() == null) {
					if (createdOnDate.compareTo(createdDateRange.getToDate()) < 1)
						return true;
				} else {
//					both toDate and fromDate are not null
					if (createdOnDate.compareTo(createdDateRange.getFromDate()) > 0
							&& createdOnDate.compareTo(createdDateRange.getToDate()) < 1)
						return true;
				}
			}
		}
		return false;
	}

//	check user rule filter
	private Boolean checkUserRule(Long userGroupId, Long userId) {
		try {
			Boolean result = ugMemberSerivce.checkUserGroupMember(userId, userGroupId);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

//	check taxonomic rule
	private Boolean checkTaxonomicRule(Long userGroupId, Long taxonomyId) {
		try {

			if (taxonomyId == null)
				return true;

			List<UserGroupTaxonomicRule> taxonomicRule = ugtaxonomicDao.findByUserGroupIdIsEnabled(userGroupId);
			if (taxonomicRule != null && !taxonomicRule.isEmpty()) {
				List<BreadCrumb> breadCrumbs = taxonomyService.getTaxonomyBreadCrumb(taxonomyId.toString());
				List<Long> taxonomyPath = new ArrayList<Long>();
				for (BreadCrumb breadCrumb : breadCrumbs) {
					taxonomyPath.add(breadCrumb.getId());
				}
				for (UserGroupTaxonomicRule ugTaxonomy : taxonomicRule) {
					if (taxonomyPath.contains(ugTaxonomy.getTaxonomyId()))
						return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

//	check spartial rule
	private Boolean checkSpatialRule(Long userGroupId, Double lat, Double lon) {
		try {
			List<UserGroupSpatialData> spatialList = ugSpatialDao.findByUserGroupIdIsEnabled(userGroupId);
			if (spatialList != null && !spatialList.isEmpty()) {
				GeometryFactory geofactory = new GeometryFactory(new PrecisionModel(), 4326);
				WKTReader reader = new WKTReader(geofactory);
				Coordinate c = new Coordinate(lon, lat);
				Geometry point = geofactory.createPoint(c);
				for (UserGroupSpatialData ugSpatialData : spatialList) {
					Geometry groupBoundries = reader.read(ugSpatialData.getSpatialData());
					if (groupBoundries.intersects(point))
						return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public void bgFiltureRule(HttpServletRequest request, UserGroupObvFilterData ugObvFilterData) {
		bgPostingUG(request, ugObvFilterData);
		bgUnPostingUG(request, ugObvFilterData);

	}

//	for posting observation to usergroup
	@Override
	public void bgPostingUG(HttpServletRequest request, UserGroupObvFilterData ugFilterData) {

		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			List<UserGroupFilterRule> ugFilterList = ugFilterRuleDao.findAll();
			List<UserGroupObservation> ugObservation = ugObvDao.findByObservationId(ugFilterData.getObservationId());
			List<Long> ugIdFilterList = new ArrayList<Long>();
			for (UserGroupFilterRule ugFilter : ugFilterList)
				ugIdFilterList.add(ugFilter.getUserGroupId());
			List<Long> ugIdObvList = new ArrayList<Long>();
			for (UserGroupObservation ugObv : ugObservation)
				ugIdObvList.add(ugObv.getUserGroupId());

			for (Long ugid : ugIdFilterList) {
				if (!ugIdObvList.contains(ugid)) {
					Boolean isEligible = checkUserGroupEligiblity(ugid, userId, ugFilterData);
					if (isEligible) {
						UserGroupObservation ugObv = new UserGroupObservation(ugid, ugFilterData.getObservationId());
						ugObv = ugObvDao.save(ugObv);
						if (ugObv != null) {

							InputStream in = Thread.currentThread().getContextClassLoader()
									.getResourceAsStream("config.properties");
							Properties properties = new Properties();
							try {
								properties.load(in);
							} catch (IOException e) {
								e.printStackTrace();
							}

							String adminId = properties.getProperty("portalAdminId");

							in.close();

							String token = tokenGenerator.generate(userService.getUser(adminId));

							UserGroupActivity ugActivity = new UserGroupActivity();
							UserGroupIbp ugIbp = ugService.fetchByGroupIdIbp(ugid);
							String description = null;
							ugActivity.setFeatured(null);
							ugActivity.setUserGroupId(ugIbp.getId());
							ugActivity.setUserGroupName(ugIbp.getName());
							ugActivity.setWebAddress(ugIbp.getWebAddress());
							ugActivity.setReason("Added Through Filter Rules");
							try {
								description = objectMapper.writeValueAsString(ugActivity);
							} catch (Exception e) {
								logger.error(e.getMessage());
							}

							logActivity.LogActivity(token, description, ugFilterData.getObservationId(),
									ugFilterData.getObservationId(), "observation", ugid, "Posted resource", null);

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

//	for  unposting observation from a userGroup
	@Override
	public void bgUnPostingUG(HttpServletRequest request, UserGroupObvFilterData ugObvFilterData) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());

			List<UserGroupFilterRule> ugFilterList = ugFilterRuleDao.findAll();
			List<UserGroupObservation> ugObservation = ugObvDao.findByObservationId(ugObvFilterData.getObservationId());
			List<Long> ugIdFilterList = new ArrayList<Long>();
			for (UserGroupFilterRule ugFilter : ugFilterList)
				ugIdFilterList.add(ugFilter.getUserGroupId());
			List<Long> ugIdObvList = new ArrayList<Long>();
			for (UserGroupObservation ugObv : ugObservation)
				ugIdObvList.add(ugObv.getUserGroupId());

			for (Long ugid : ugIdObvList) {
				if (ugIdFilterList.contains(ugid)) {

					Boolean isEligible = checkUserGroupEligiblity(ugid, userId, ugObvFilterData);
					if (!isEligible) {
						UserGroupObservation ugObvMapping = ugObvDao
								.checkObservationUGMApping(ugObvFilterData.getObservationId(), ugid);
						ugObvDao.delete(ugObvMapping);

						InputStream in = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream("config.properties");
						Properties properties = new Properties();
						try {
							properties.load(in);
						} catch (IOException e) {
							e.printStackTrace();
						}

						String adminId = properties.getProperty("portalAdminId");

						in.close();

						String token = tokenGenerator.generate(userService.getUser(adminId));

						UserGroupActivity ugActivity = new UserGroupActivity();
						UserGroupIbp ugIbp = ugService.fetchByGroupIdIbp(ugid);
						String description = null;
						ugActivity.setFeatured(null);
						ugActivity.setUserGroupId(ugIbp.getId());
						ugActivity.setUserGroupName(ugIbp.getName());
						ugActivity.setWebAddress(ugIbp.getWebAddress());
						ugActivity
								.setReason("Removed Through Filter Rules" + findReason(ugid, userId, ugObvFilterData));
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						logActivity.LogActivity(token, description, ugObvFilterData.getObservationId(),
								ugObvFilterData.getObservationId(), "observation", ugid, "Removed resoruce", null);

					}

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public Boolean checkUserGroupEligiblity(Long userGroupId, Long userId, UserGroupObvFilterData ugFilterData) {
		try {
			UserGroupFilterRule ugFilter = ugFilterRuleDao.findByUserGroupId(userGroupId);
			Boolean isSpartial = false;
			Boolean isTaxo = false;
			Boolean isUser = false;
			Boolean isCreatedOn = false;
			Boolean isObservedOn = false;

			Boolean result = false;

			if (ugFilter != null) {
				if (ugFilter.getHasSpatialRule()) {
					isSpartial = checkSpatialRule(userGroupId, ugFilterData.getLatitude(), ugFilterData.getLongitude());
					if (isSpartial)
						result = true;
					else
						return false;
				}
				if (ugFilter.getHasUserRule()) {
					isUser = checkUserRule(userGroupId, userId);
					if (isUser)
						result = true;
					else
						return false;

				}
				if (ugFilter.getHasCreatedOnDateRule()) {
					isCreatedOn = checkCreatedOnDateFilter(userGroupId, ugFilterData.getCreatedOnDate());
					if (isCreatedOn)
						result = true;
					else
						return false;
				}
				if (ugFilter.getHasObservedOnDateRule()) {
					isObservedOn = checkObservedOnDateFilter(userGroupId, ugFilterData.getObservedOnDate());
					if (isObservedOn)
						result = true;
					else
						return false;
				}

				if (ugFilter.getHasTaxonomicRule()) {
					if (ugFilterData.getTaxonomyId() != null) {
						isTaxo = checkTaxonomicRule(userGroupId, ugFilterData.getTaxonomyId());
						if (isTaxo)
							result = true;
						else
							return false;
					}

				}
				if (ugFilter.getHasSpatialRule() == false && ugFilter.getHasTaxonomicRule() == false
						&& ugFilter.getHasUserRule() == false && ugFilter.getHasCreatedOnDateRule() == false
						&& ugFilter.getHasObservedOnDateRule() == false)
					return false;

			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	private String findReason(Long userGroupId, Long userId, UserGroupObvFilterData ugObvFilterData) {

		try {

			String reason = "";
			UserGroupFilterRule ugFilter = ugFilterRuleDao.findByUserGroupId(userGroupId);
			if (ugFilter != null) {
				if (ugFilter.getHasSpatialRule()) {
					Boolean isSpartial = checkSpatialRule(userGroupId, ugObvFilterData.getLatitude(),
							ugObvFilterData.getLatitude());
					if (!isSpartial)
						reason = reason + " Spartial Rule ,";
				}
				if (ugFilter.getHasTaxonomicRule()) {
					Boolean isTaxo = checkTaxonomicRule(userGroupId, ugObvFilterData.getTaxonomyId());
					if (!isTaxo)
						reason = reason + " taxonomic Rule ,";
				}
				if (ugFilter.getHasUserRule()) {
					Boolean isUser = checkUserRule(userGroupId, userId);
					if (!isUser)
						reason = reason + " User Rule ,";
				}
				if (ugFilter.getHasCreatedOnDateRule()) {
					Boolean isCreatedOn = checkCreatedOnDateFilter(userGroupId, ugObvFilterData.getCreatedOnDate());
					if (!isCreatedOn)
						reason = reason + " CreatedOn Date Rule ,";
				}
				if (ugFilter.getHasObservedOnDateRule()) {
					Boolean isObservedOn = checkObservedOnDateFilter(userGroupId, ugObvFilterData.getObservedOnDate());
					if (!isObservedOn)
						reason = reason + " ObservedOn Date Rule ,";
				}
			}

			if (reason.length() > 0) {
				reason = reason.substring(0, reason.length() - 1);
			}

			return reason;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	@Override
	public ShowFilterRule showAllFilter(Long userGroupId) {

		ShowFilterRule showFilter = new ShowFilterRule(false, null, false, null, false, false, null, false, null);

		UserGroupFilterRule ugFilter = ugFilterRuleDao.findByUserGroupId(userGroupId);
		if (ugFilter != null) {
			if (ugFilter.getHasSpatialRule()) {
				showFilter.setHasSpatialRule(true);
				showFilter.setSpartialRuleList(ugSpatialDao.findAllByUserGroupId(userGroupId));
			}
			if (ugFilter.getHasUserRule()) {
				showFilter.setHasUserRule(true);
			}
			if (ugFilter.getHasTaxonomicRule()) {
				showFilter.setHasTaxonomicRule(true);
				showFilter.setTaxonomicRuleList(ugtaxonomicDao.findAllByUserGroupId(userGroupId));
			}
			if (ugFilter.getHasCreatedOnDateRule()) {
				showFilter.setHasCreatedOnDateRule(true);
				showFilter.setCreatedOnDateRuleList(ugCreatedDateDao.findAllByUserGroupId(userGroupId));
			}
			if (ugFilter.getHasObservedOnDateRule()) {
				showFilter.setHasObservedOnDateRule(true);
				showFilter.setObservedOnDateRule(ugObservedDateDao.findAllByUserGroupId(userGroupId));
			}
		}
		return showFilter;

	}

	@Override
	public ShowFilterRule deleteUGFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterRemove ugFilterRemove) {
		try {

			UserGroupFilterRule ugFilter = ugFilterRuleDao.findByUserGroupId(userGroupId);

			if (ugFilterRemove.getFilterName().equalsIgnoreCase("userRule")) {
				if (ugFilter.getHasUserRule()) {
					ugFilter.setHasUserRule(false);
					ugFilterRuleDao.update(ugFilter);
					String desc = "User Rule removed";
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilter.getId(), "Removed Filter Rule");
				}
			} else if (ugFilterRemove.getFilterName().equalsIgnoreCase("taxonomicRule")) {
				if (ugFilter.getHasTaxonomicRule()) {
					UserGroupTaxonomicRule taxonomicRule = ugtaxonomicDao.findById(ugFilterRemove.getFilterId());
					ugtaxonomicDao.delete(taxonomicRule);
					String desc = "Taxonomic Rule removed: taxonomyId " + taxonomicRule.getTaxonomyId();
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilter.getId(), "Removed Filter Rule");
				}
			} else if (ugFilterRemove.getFilterName().equalsIgnoreCase("spatialRule")) {
				if (ugFilter.getHasSpatialRule()) {
					UserGroupSpatialData spatialData = ugSpatialDao.findById(ugFilterRemove.getFilterId());
					ugSpatialDao.delete(spatialData);
					String desc = "Spatial Rule removed";
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilter.getId(), "Removed Filter Rule");
				}
			} else if (ugFilterRemove.getFilterName().equalsIgnoreCase("observedOnDateRule")) {
				if (ugFilter.getHasObservedOnDateRule()) {
					UserGroupObservedonDateRule observedOnDate = ugObservedDateDao
							.findById(ugFilterRemove.getFilterId());
					ugObservedDateDao.delete(observedOnDate);
					String fromDate = observedOnDate.getFromDate() != null ? observedOnDate.getFromDate().toString()
							: new Date(0).toString();
					String toDate = observedOnDate.getToDate() != null ? observedOnDate.getToDate().toString()
							: "Presently";
					String desc = "Observed Date Rule Added :" + fromDate + " to " + toDate;
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilter.getId(), "Removed Filter Rule");
				}
			} else if (ugFilterRemove.getFilterName().equalsIgnoreCase("createdOnDateRule")) {
				if (ugFilter.getHasCreatedOnDateRule()) {

					UserGroupCreatedOnDateRule createdOnDate = ugCreatedDateDao.findById(ugFilterRemove.getFilterId());
					ugCreatedDateDao.delete(createdOnDate);
					String fromDate = createdOnDate.getFromDate() != null ? createdOnDate.getFromDate().toString()
							: new Date(0).toString();
					String toDate = createdOnDate.getToDate() != null ? createdOnDate.getToDate().toString()
							: "Presently";
					String desc = "CreatedOn Date Rule Added :" + fromDate + " to " + toDate;
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilter.getId(), "Removed Filter Rule");
				}
			}

			updateUGFilter(userGroupId);
			return showAllFilter(userGroupId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public ShowFilterRule enableDisableUGFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterEnable ugFilterEnable) {
		try {
			UserGroupFilterRule ugFilterRule = ugFilterRuleDao.findByUserGroupId(userGroupId);
			if (ugFilterEnable.getFilterType().equalsIgnoreCase("taxonomicRule")) {
				UserGroupTaxonomicRule taxonomicRule = ugtaxonomicDao.findById(ugFilterEnable.getFilterId());
				if (taxonomicRule != null && !taxonomicRule.getIsEnabled().equals(ugFilterEnable.getIsEnabled())) {
					taxonomicRule.setIsEnabled(ugFilterEnable.getIsEnabled());
					taxonomicRule = ugtaxonomicDao.update(taxonomicRule);
					String activityType = taxonomicRule.getIsEnabled() ? "Enabled Filter Rule" : "Disabled Filter Rule";
					String desc = "Taxonomy Rule : taxonomy id " + taxonomicRule.getTaxonomyId() + " is "
							+ (taxonomicRule.getIsEnabled() ? "Enabled" : "disabled");
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), activityType);

				}
			} else if (ugFilterEnable.getFilterType().equalsIgnoreCase("spatialRule")) {
				UserGroupSpatialData spatialData = ugSpatialDao.findById(ugFilterEnable.getFilterId());
				if (spatialData != null && !spatialData.getIsEnabled().equals(ugFilterEnable.getIsEnabled())) {
					spatialData.setIsEnabled(ugFilterEnable.getIsEnabled());
					ugSpatialDao.update(spatialData);
					String activityType = spatialData.getIsEnabled() ? "Enabled Filter Rule" : "Disabled Filter Rule";
					String desc = "Spatial rule " + (spatialData.getIsEnabled() ? "Enabled" : "Disabled");
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), activityType);

				}
			} else if (ugFilterEnable.getFilterType().equalsIgnoreCase("observedOnDateRule")) {
				UserGroupObservedonDateRule observerdRule = ugObservedDateDao.findById(ugFilterEnable.getFilterId());
				if (observerdRule != null && !observerdRule.getIsEnabled().equals(ugFilterEnable.getIsEnabled())) {
					observerdRule.setIsEnabled(ugFilterEnable.getIsEnabled());
					ugObservedDateDao.update(observerdRule);

					String activityType = observerdRule.getIsEnabled() ? "Enabled Filter Rule" : "Disabled Filter Rule";

					String fromDate = observerdRule.getFromDate() != null ? observerdRule.getFromDate().toString()
							: new Date(0).toString();
					String toDate = observerdRule.getToDate() != null ? observerdRule.getToDate().toString()
							: "Presently";
					String desc = "observerdOn Date Rule Added :" + fromDate + " to " + toDate + " is "
							+ (observerdRule.getIsEnabled() ? "Enabled" : "Disabled");

					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), activityType);

				}
			} else if (ugFilterEnable.getFilterType().equalsIgnoreCase("createdOnDateRule")) {
				UserGroupCreatedOnDateRule createdOnRule = ugCreatedDateDao.findById(ugFilterEnable.getFilterId());
				if (createdOnRule != null && !createdOnRule.getIsEnabled().equals(ugFilterEnable.getIsEnabled())) {
					createdOnRule.setIsEnabled(ugFilterEnable.getIsEnabled());
					ugCreatedDateDao.update(createdOnRule);

					String activityType = createdOnRule.getIsEnabled() ? "Enabled Filter Rule" : "Disabled Filter Rule";

					String fromDate = createdOnRule.getFromDate() != null ? createdOnRule.getFromDate().toString()
							: new Date(0).toString();
					String toDate = createdOnRule.getToDate() != null ? createdOnRule.getToDate().toString()
							: "Presently";
					String desc = "createdOn Date Rule Added :" + fromDate + " to " + toDate + " is "
							+ (createdOnRule.getIsEnabled() ? "Enabled" : "Disabled");

					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), activityType);
				}
			}

			return showAllFilter(userGroupId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	private void updateUGFilter(Long userGroupId) {
		try {
			UserGroupFilterRule ugFilter = ugFilterRuleDao.findByUserGroupId(userGroupId);
			List<UserGroupSpatialData> spartialData = ugSpatialDao.findAllByUserGroupId(userGroupId);
			List<UserGroupTaxonomicRule> taxonomicData = ugtaxonomicDao.findAllByUserGroupId(userGroupId);
			List<UserGroupObservedonDateRule> observedOnData = ugObservedDateDao.findAllByUserGroupId(userGroupId);
			List<UserGroupCreatedOnDateRule> createdOnData = ugCreatedDateDao.findAllByUserGroupId(userGroupId);
			ugFilter.setHasSpatialRule(false);
			if (spartialData != null && !spartialData.isEmpty())
				ugFilter.setHasSpatialRule(true);

			ugFilter.setHasTaxonomicRule(false);
			if (taxonomicData != null && !taxonomicData.isEmpty())
				ugFilter.setHasTaxonomicRule(true);

			ugFilter.setHasObservedOnDateRule(false);
			if (observedOnData != null && !observedOnData.isEmpty())
				ugFilter.setHasObservedOnDateRule(true);

			ugFilter.setHasCreatedOnDateRule(false);
			if (createdOnData != null && !createdOnData.isEmpty())
				ugFilter.setHasCreatedOnDateRule(true);
			ugFilterRuleDao.update(ugFilter);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public ShowFilterRule changeUgFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterRuleInputData ugFilterInputData) {

		try {
			UserGroupFilterRule ugFilterRule = ugFilterRuleDao.findByUserGroupId(userGroupId);
			if (ugFilterRule == null) {
				ugFilterRule = new UserGroupFilterRule(null, userGroupId, false, false, false, false, false);
				ugFilterRule = ugFilterRuleDao.save(ugFilterRule);
			}

			if (ugFilterInputData.getHasUserRule() != null) {
				ugFilterRule.setHasUserRule(ugFilterInputData.getHasUserRule());
				ugFilterRuleDao.update(ugFilterRule);
				String desc = "User Rule :Added";
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
						userGroupId, "userGroup", ugFilterRule.getId(), "Added Filter Rule");
			}

			if (ugFilterInputData.getSpartialDataList() != null && !ugFilterInputData.getSpartialDataList().isEmpty()) {
				for (String spatialData : ugFilterInputData.getSpartialDataList()) {
					UserGroupSpatialData ugSpatialData = new UserGroupSpatialData(null, userGroupId, spatialData, true);
					ugSpatialDao.save(ugSpatialData);
					String desc = "Spatial Rule :Added";
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), "Added Filter Rule");
				}
			}
			if (ugFilterInputData.getTaxonomicIdList() != null && !ugFilterInputData.getTaxonomicIdList().isEmpty()) {
				for (Long taxonomyId : ugFilterInputData.getTaxonomicIdList()) {
					UserGroupTaxonomicRule ugTaxonomicRule = new UserGroupTaxonomicRule(null, userGroupId, taxonomyId,
							true);
					ugtaxonomicDao.save(ugTaxonomicRule);

					String desc = "Taxonomic Rule Added : TaxonId " + taxonomyId;
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), "Added Filter Rule");
				}
			}
			if (ugFilterInputData.getCreatedOnDateList() != null
					&& !ugFilterInputData.getCreatedOnDateList().isEmpty()) {
				for (UserGroupFilterDate ugFilterDate : ugFilterInputData.getCreatedOnDateList()) {

					UserGroupCreatedOnDateRule ugCreatedOnRule = new UserGroupCreatedOnDateRule(null, userGroupId,
							ugFilterDate.getFromDate(), ugFilterDate.getToDate(), true);
					ugCreatedDateDao.save(ugCreatedOnRule);
					String fromDate = ugFilterDate.getFromDate() != null ? ugFilterDate.getFromDate().toString()
							: new Date(0).toString();
					String toDate = ugFilterDate.getToDate() != null ? ugFilterDate.getToDate().toString()
							: "Presently";
					String desc = "CreatedOn Date Rule Added :" + fromDate + " to " + toDate;
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), "Added Filter Rule");
				}
			}
			if (ugFilterInputData.getObservedOnDateList() != null
					&& !ugFilterInputData.getObservedOnDateList().isEmpty()) {
				for (UserGroupFilterDate ugFilterDate : ugFilterInputData.getObservedOnDateList()) {
					UserGroupObservedonDateRule ugObservedOnRule = new UserGroupObservedonDateRule(null, userGroupId,
							ugFilterDate.getFromDate(), ugFilterDate.getToDate(), true);
					ugObservedDateDao.save(ugObservedOnRule);
					String fromDate = ugFilterDate.getFromDate() != null ? ugFilterDate.getFromDate().toString()
							: new Date(0).toString();
					String toDate = ugFilterDate.getToDate() != null ? ugFilterDate.getToDate().toString()
							: "Presently";
					String desc = "ObservedOn Date Rule Added:" + fromDate + " to " + toDate;
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", ugFilterRule.getId(), "Added Filter Rule");
				}
			}

			updateUGFilter(userGroupId);
			return showAllFilter(userGroupId);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public void bulkFilteringIn(HttpServletRequest request, Long userGroupId,
			List<UserGroupObvFilterData> ugObvFilterDataList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			for (UserGroupObvFilterData ugFilterData : ugObvFilterDataList) {
				UserGroupObservation ugObvMapping = ugObvDao.checkObservationUGMApping(ugFilterData.getObservationId(),
						userGroupId);
				if (ugObvMapping == null) {
					Boolean isEligible = checkUserGroupEligiblity(userGroupId, userId, ugFilterData);
					if (isEligible) {
						UserGroupObservation ugObv = new UserGroupObservation(userGroupId,
								ugFilterData.getObservationId());
						ugObvDao.save(ugObv);

						InputStream in = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream("config.properties");
						Properties properties = new Properties();
						try {
							properties.load(in);
						} catch (IOException e) {
							e.printStackTrace();
						}

						String adminId = properties.getProperty("portalAdminId");

						in.close();

						String token = tokenGenerator.generate(userService.getUser(adminId));

						UserGroupActivity ugActivity = new UserGroupActivity();
						UserGroupIbp ugIbp = ugService.fetchByGroupIdIbp(userGroupId);
						String description = null;
						ugActivity.setFeatured(null);
						ugActivity.setUserGroupId(ugIbp.getId());
						ugActivity.setUserGroupName(ugIbp.getName());
						ugActivity.setWebAddress(ugIbp.getWebAddress());
						ugActivity.setReason("Added Through Filter Rules");
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						logActivity.LogActivity(token, description, ugFilterData.getObservationId(),
								ugFilterData.getObservationId(), "observation", userGroupId, "Posted resource", null);

					}
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public void bulkFilteringOut(HttpServletRequest request, Long userGroupId,
			List<UserGroupObvFilterData> ugObvFilterDataList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());

			for (UserGroupObvFilterData ugFilterData : ugObvFilterDataList) {

				UserGroupObservation ugObvMapping = ugObvDao.checkObservationUGMApping(ugFilterData.getObservationId(),
						userGroupId);
				if (ugObvMapping != null) {
					Boolean isEligible = checkUserGroupEligiblity(userGroupId, userId, ugFilterData);
					if (!isEligible) {
						ugObvDao.delete(ugObvMapping);

						InputStream in = Thread.currentThread().getContextClassLoader()
								.getResourceAsStream("config.properties");
						Properties properties = new Properties();
						try {
							properties.load(in);
						} catch (IOException e) {
							e.printStackTrace();
						}

						String adminId = properties.getProperty("portalAdminId");

						in.close();

						String token = tokenGenerator.generate(userService.getUser(adminId));

						UserGroupActivity ugActivity = new UserGroupActivity();
						UserGroupIbp ugIbp = ugService.fetchByGroupIdIbp(userGroupId);
						String description = null;
						ugActivity.setFeatured(null);
						ugActivity.setUserGroupId(ugIbp.getId());
						ugActivity.setUserGroupName(ugIbp.getName());
						ugActivity.setWebAddress(ugIbp.getWebAddress());
						ugActivity.setReason(
								"Removed Through Filter Rules " + findReason(userGroupId, userId, ugFilterData));
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						logActivity.LogActivity(token, description, ugFilterData.getObservationId(),
								ugFilterData.getObservationId(), "observation", userGroupId, "Removed resoruce", null);

					}

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
