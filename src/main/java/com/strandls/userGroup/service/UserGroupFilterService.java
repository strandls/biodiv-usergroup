/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.strandls.userGroup.pojo.ShowFilterRule;
import com.strandls.userGroup.pojo.UserGroupFilterEnable;
import com.strandls.userGroup.pojo.UserGroupFilterRemove;
import com.strandls.userGroup.pojo.UserGroupFilterRuleInputData;
import com.strandls.userGroup.pojo.UserGroupObvFilterData;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserGroupFilterService {

	public Boolean checkUserGroupEligiblity(Long userGroupId, Long userId, UserGroupObvFilterData ugFilterData);

	public void bgFiltureRule(HttpServletRequest request, UserGroupObvFilterData ugObvFilterData);

	public void bgPostingUG(HttpServletRequest request, UserGroupObvFilterData ugFilterData);

	public void bgUnPostingUG(HttpServletRequest request, UserGroupObvFilterData ugObvFilterData);

	public void bulkFilteringIn(HttpServletRequest request, Long userGroupId,
			List<UserGroupObvFilterData> ugObvFilterDataList);

	public void bulkFilteringOut(HttpServletRequest request, Long userGroupId,
			List<UserGroupObvFilterData> ugObvFilterDataList);

	public ShowFilterRule showAllFilter(Long userGroupId);

	public ShowFilterRule changeUgFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterRuleInputData ugFilterInputData);

	public ShowFilterRule deleteUGFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterRemove ugFilterRemove);

	public ShowFilterRule enableDisableUGFilter(HttpServletRequest request, Long userGroupId,
			UserGroupFilterEnable ugFilterEnable);

}
