/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

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
}
