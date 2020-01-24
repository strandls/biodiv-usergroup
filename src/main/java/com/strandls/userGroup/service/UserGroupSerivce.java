/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserGroupSerivce {

	public UserGroup fetchByGroupId(Long id);

	public UserGroupIbp fetchByGroupIdIbp(Long id);

	public List<UserGroupIbp> fetchByObservationId(Long id);

	public List<UserGroupIbp> fetchByUserGroupDetails(List<Long> userGroupMember);

	public List<Long> createUserGroupObservationMapping(Long observationId, List<Long> userGroups);

	public List<UserGroupIbp> updateUserGroupObservationMapping(Long observationId, List<Long> userGorups);

	public List<UserGroupIbp> fetchAllUserGroup();

	public List<Featured> fetchFeatured(String objectType, Long id);

	public List<Featured> createFeatured(Long userId, FeaturedCreate featuredCreate);

	public List<Featured> removeFeatured(Long userId, String objectType, Long objectId, List<Long> userGroupList);

	public void filterRule(ObservationLatLon latlon);

	public void bulkFilterRule(String userGroupIds, List<ObservationLatLon> latlonList);

}
