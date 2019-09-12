/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupMemberRoleDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceImpl implements UserGroupSerivce {

	@Inject
	private UserGroupDao userGroupDao;

	@Inject
	private UserGroupObservationDao userGroupObvDao;

	@Inject
	private UserGroupMemberRoleDao userGroupMemberRoleDao;

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
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getDomianName());
		else {
			String webAddress = "https://indiabiodiversity.org/group/" + ug.getWebAddress() + "/show";
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), webAddress);
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
	public List<UserGroupIbp> fetchByUserId(Long sUserId) {
		List<UserGroupMemberRole> result = userGroupMemberRoleDao.getUserGroup(sUserId);

		List<UserGroupIbp> userGroupList = new ArrayList<UserGroupIbp>();
		for (UserGroupMemberRole userGroup : result) {
			userGroupList.add(fetchByGroupIdIbp(userGroup.getUserGroupId()));
		}
		return userGroupList;
	}

}
