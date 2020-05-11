/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.strandls.taxonomy.controllers.TaxonomyServicesApi;
import com.strandls.taxonomy.pojo.BreadCrumb;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.userGroup.dao.UserGroupCreatedOnDateRuleDao;
import com.strandls.userGroup.dao.UserGroupFilterRuleDao;
import com.strandls.userGroup.dao.UserGroupObservedOnDateRuleDao;
import com.strandls.userGroup.dao.UserGroupSpatialDataDao;
import com.strandls.userGroup.dao.UserGroupTaxonomicRuleDao;
import com.strandls.userGroup.pojo.UserGroupCreatedOnDateRule;
import com.strandls.userGroup.pojo.UserGroupFilterData;
import com.strandls.userGroup.pojo.UserGroupObservedonDateRule;
import com.strandls.userGroup.pojo.UserGroupSpatialData;
import com.strandls.userGroup.pojo.UserGroupTaxonomicRule;
import com.strandls.userGroup.pojo.UserGroupWKT;
import com.strandls.userGroup.service.UserGroupFilterService;
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
	private ObjectMapper objectMapper;

	@Inject
	private TaxonomyServicesApi taxonomyService;

	@Override
	public Boolean checkObservedOnDateFilter(Long userGroupId, Date observedOnDate) {
		List<UserGroupObservedonDateRule> observedDateData = ugObservedDateDao.findByUserGroupId(userGroupId);
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

	@Override
	public Boolean checkCreatedOnDateFilter(Long userGroupId, Date createdOnDate) {
		List<UserGroupCreatedOnDateRule> createdDataList = ugCreatedDateDao.findByUserGroupId(userGroupId);
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

	@Override
	public Boolean checkUserRule(Long userGroupId, Long userId) {
		try {
			Boolean result = userService.checkGroupMemberByUserId(userGroupId.toString(), userId.toString());
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean checkTaxonomicRule(Long userGroupId, Long taxonomyId) {
		try {

			List<UserGroupTaxonomicRule> taxonomicRule = ugtaxonomicDao.findByUserGroupId(userGroupId);
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

	@Override
	public Boolean checkSpatialRule(Long userGroupId, Double lat, Double lon) {
		try {
			List<UserGroupSpatialData> spatialList = ugSpatialDao.findByUserGroupId(userGroupId);
			if (spatialList != null && !spatialList.isEmpty()) {
				GeometryFactory geofactory = new GeometryFactory(new PrecisionModel(), 4326);
				WKTReader reader = new WKTReader(geofactory);
				Coordinate c = new Coordinate(lon, lat);
				Geometry point = geofactory.createPoint(c);
				for (UserGroupSpatialData ugSpatialData : spatialList) {
					UserGroupWKT wkt = objectMapper.readValue(ugSpatialData.getSpatialData(), UserGroupWKT.class);
					Geometry groupBoundries = reader.read(wkt.getWkt());
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
	public void bgCheckForRule(UserGroupFilterData ugFilterData) {
		// TODO Auto-generated method stub

	}

}
