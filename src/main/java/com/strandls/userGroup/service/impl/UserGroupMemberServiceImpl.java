/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.dao.UserGroupMemberRoleDao;
import com.strandls.userGroup.pojo.GroupAddMember;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupMembersCount;
import com.strandls.userGroup.pojo.UserGroupPermissions;
import com.strandls.userGroup.service.UserGroupMemberService;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberServiceImpl implements UserGroupMemberService {

	private final Logger logger = LoggerFactory.getLogger(UserGroupMemberServiceImpl.class);

	@Inject
	private UserGroupMemberRoleDao userGroupMemberDao;

	@Inject
	private UserServiceApi userService;

	@Override
	public Boolean checkUserGroupMember(Long userId, Long userGroupId) {
		UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
		if (result != null)
			return true;
		return false;
	}

	@Override
	public List<UserGroupMembersCount> getUserGroupMemberCount() {
		List<UserGroupMembersCount> result = userGroupMemberDao.fetchMemberCountUserGroup();
		return result;
	}

	@Override
	public Boolean checkFounderRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupFounder");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result != null && result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean checkModeratorRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupExpert");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result != null && result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	@Override
	public UserGroupMemberRole addMemberUG(Long userId, Long roleId, Long userGroupId) {
		UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(userGroupId, roleId, userId);
		ugMemberRole = userGroupMemberDao.save(ugMemberRole);
		return ugMemberRole;
	}

	@Override
	public Boolean removeGroupMember(Long userId, Long userGroupId) {
		try {
			UserGroupMemberRole ugMember = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (ugMember != null) {
				userGroupMemberDao.delete(ugMember);
				List<UserGroupMemberRole> members = userGroupMemberDao.fetchByUserGroupIdRole(userGroupId);
				if (members == null || members.isEmpty()) {
					InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("config.properties");
					Properties properties = new Properties();
					try {
						properties.load(in);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
					Long portalAmdinId = Long.parseLong(properties.getProperty("portalAdminId"));
					in.close();
					ugMember = new UserGroupMemberRole(userGroupId, founderId, portalAmdinId);
					userGroupMemberDao.save(ugMember);
				}
				return true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean joinGroup(Long userId, Long userGroupId) {
		try {
			Boolean isOpenGroup = userGroupMemberDao.checksGroupType(userGroupId.toString());
			if (isOpenGroup) {
				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
				in.close();
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, memberId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMember = new UserGroupMemberRole(userGroupId, memberId, userId);
					userGroupMemberDao.save(ugMember);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Long> addMemberDirectly(GroupAddMember addMember) {
		try {
			Long roleId = addMember.getRoleId();
			Long userGroupId = addMember.getRoleId();
			List<Long> mappedUser = new ArrayList<Long>();
			for (Long userId : addMember.getMemberList()) {
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, roleId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(addMember.getUserGroupId(),
							addMember.getRoleId(), userId);
					userGroupMemberDao.save(ugMemberRole);
					mappedUser.add(userId);
				}

			}
			return mappedUser;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<User> getFounderModerator(Long userGroupId) {
		try {
			List<UserGroupMemberRole> ugMemberRoleList = userGroupMemberDao.findFounderModerator(userGroupId);
			List<User> userList = new ArrayList<User>();
			for (UserGroupMemberRole ugMemberRole : ugMemberRoleList) {
				userList.add(userService.getUser(ugMemberRole.getsUserId().toString()));
			}
			return userList;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public List<UserIbp> getFounderList(Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupFounder");
			in.close();
			List<UserGroupMemberRole> ugMemberList = userGroupMemberDao.findMemberListByRoleId(userGroupId,
					Long.parseLong(founder));

			List<UserIbp> result = new ArrayList<UserIbp>();
			for (UserGroupMemberRole ugMember : ugMemberList) {
				result.add(userService.getUserIbp(ugMember.getsUserId().toString()));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<UserIbp> getModeratorList(Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String expert = properties.getProperty("userGroupExpert");
			in.close();
			List<UserGroupMemberRole> ugMemberList = userGroupMemberDao.findMemberListByRoleId(userGroupId,
					Long.parseLong(expert));

			List<UserIbp> result = new ArrayList<UserIbp>();
			for (UserGroupMemberRole ugMember : ugMemberList) {
				result.add(userService.getUserIbp(ugMember.getsUserId().toString()));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public UserGroupPermissions getUserGroupObservationPermissions(Long userId) {
		List<UserGroupMemberRole> userMemberRole = userGroupMemberDao.getUserGroup(userId);
		List<UserGroupMemberRole> userFeatureRole = userGroupMemberDao.findUserGroupbyUserIdRole(userId);
		UserGroupPermissions ugPermission = new UserGroupPermissions(userMemberRole, userFeatureRole);
		return ugPermission;
	}
}
