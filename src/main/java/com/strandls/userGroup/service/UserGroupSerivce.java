/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.activity.pojo.MailData;
import com.strandls.userGroup.dto.AuthenticationDTO;
import com.strandls.userGroup.pojo.AdministrationList;
import com.strandls.userGroup.pojo.BulkGroupPostingData;
import com.strandls.userGroup.pojo.BulkGroupUnPostingData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupDocCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupHomePage;
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

	public List<Long> findAllObservation(Long userGroupId);

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

	public String updateUserGroupFilter(Long userGroupId, UserGroupWKT userGroupWKT);

	public List<UserGroupSpeciesGroup> getUserGroupSpeciesGroup(Long ugId);

	public MailData updateMailData(Long observationId, MailData mailData);

	public Boolean addMemberRoleInvitaions(HttpServletRequest request, CommonProfile profile,
			UserGroupInvitationData userGroupInvitations);

	public UserGroupIbp validateMember(HttpServletRequest request, Long userId, String token);

	public Boolean removeUser(HttpServletRequest request, String userGroupId, String userId);

	public Boolean leaveGroup(HttpServletRequest request, Long userId, String userGroupId);

	public Boolean joinGroup(HttpServletRequest request, Long userId, String userGroupId);

	public Boolean sendInvitesForMemberRole(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			List<Long> inviteeList);

	public Boolean bulkPosting(HttpServletRequest request, CommonProfile profile, BulkGroupPostingData bulkPosting);

	public Boolean bulkRemoving(HttpServletRequest request, CommonProfile profile,
			BulkGroupUnPostingData bulkUnPosting);

	public UserGroupIbp createUserGroup(HttpServletRequest request, CommonProfile profile,
			UserGroupCreateData ugCreateData);

	public UserGroupEditData getUGEditData(HttpServletRequest request, CommonProfile profile, Long userGroupId);

	public UserGroupIbp saveUGEdit(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			UserGroupEditData ugEditData);

	public Boolean addMemberDirectly(HttpServletRequest request, Long userGroupId, UserGroupAddMemebr memberList);

	public UserGroupHomePage getUserGroupHomePageData(Long userGroupId);

	public UserGroupIbp validateJoinRequest(HttpServletRequest request, String token);

	public AdministrationList getAdminMembers(String userGroupId);

	public Map<String, Object> registerUserProxy(HttpServletRequest request, AuthenticationDTO authDTO);

	public Map<String, Object> signupProxy(HttpServletRequest request, String userName, String password, String mode);

	public Map<String, Object> verifyOTPProxy(HttpServletRequest request, Long id, String otp);

	public List<UserGroupIbp> fetchByDocumentId(Long documentId);

	public List<UserGroupIbp> createUGDocMapping(HttpServletRequest request,UserGroupDocCreateData ugDocCreate);

}
