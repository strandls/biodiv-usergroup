/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.activity.pojo.MailData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.ObservationLatLon;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
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

	public Boolean addMemberRoleInvitaions(CommonProfile profile, UserGroupInvitationData userGroupInvitations);

	public Boolean validateMember(Long userId, String token);

	public Boolean removeUser(String userGroupId, String userId);

	public Boolean leaveGroup(Long userId, String userGroupId);

	public Boolean joinGroup(Long userId, String userGroupId);

	public Boolean sendInvitesForMemberRole(CommonProfile profile, Long userGroupId, List<Long> inviteeList);

	public Boolean bulkPosting(CommonProfile profile, Long userGroupId, List<Long> observationId);

	public Boolean bulkRemoving(CommonProfile profile, Long userGroupId, List<Long> observationId);

	public UserGroupIbp createUserGroup(CommonProfile profile, UserGroupCreateData ugCreateData);

	public UserGroupEditData getUGEditData(CommonProfile profile, Long userGroupId);

	public UserGroupIbp saveUGEdit(CommonProfile profile, Long userGroupId, UserGroupEditData ugEditData);

	public Boolean addMemberDirectly(Long userGroupId, UserGroupAddMemebr memberList);

}
