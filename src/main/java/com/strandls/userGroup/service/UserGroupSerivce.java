/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.strandls.activity.pojo.MailData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupHomePage;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UserGroupWKT;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserGroupSerivce {

	public UserGroup fetchByGroupId(Long id);

	public UserGroupIbp fetchByGroupIdIbp(Long id);

	public List<UserGroupIbp> fetchByObservationId(Long id);

	public List<UserGroupIbp> fetchByUserGroupDetails(List<Long> userGroupMember);

	public List<Long> createUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGroups);

	public List<UserGroupIbp> updateUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGorups);

	public List<UserGroupIbp> fetchAllUserGroup();

	public List<Featured> fetchFeatured(String objectType, Long id);

	public List<Featured> createFeatured(HttpServletRequest request, Long userId, FeaturedCreateData featuredCreate);

	public List<Featured> removeFeatured(HttpServletRequest request, Long userId, String objectType, Long objectId,
			UserGroupMappingCreateData userGroupList);

	public void filterRule(HttpServletRequest request, ObservationLatLon latlon);

	public void bulkFilterRule(HttpServletRequest request, String userGroupIds, List<ObservationLatLon> latlonList);

	public String updateUserGroupFilter(Long userGroupId, UserGroupWKT userGroupWKT);

	public List<UserGroupSpeciesGroup> getUserGroupSpeciesGroup(Long ugId);

	public MailData updateMailData(Long observationId, MailData mailData);

	public UserGroupHomePage getUserGroupHomePageData(Long userGroupId);

}
