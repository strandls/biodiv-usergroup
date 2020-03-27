/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import com.strandls.activity.pojo.MailData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
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

	public List<Long> createUserGroupObservationMapping(Long observationId, UserGroupMappingCreateData userGroups);

	public List<UserGroupIbp> updateUserGroupObservationMapping(Long observationId,
			UserGroupMappingCreateData userGorups);

	public List<UserGroupIbp> fetchAllUserGroup();

	public List<Featured> fetchFeatured(String objectType, Long id);

	public List<Featured> createFeatured(Long userId, FeaturedCreateData featuredCreate);

	public List<Featured> removeFeatured(Long userId, String objectType, Long objectId,
			UserGroupMappingCreateData userGroupList);

	public void filterRule(ObservationLatLon latlon);

	public void bulkFilterRule(String userGroupIds, List<ObservationLatLon> latlonList);

	public String updateUserGroupFilter(Long userGroupId, UserGroupWKT userGroupWKT);

	public List<UserGroupSpeciesGroup> getUserGroupSpeciesGroup(Long ugId);

	public MailData updateMailData(Long observationId, MailData mailData);

}
