/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.Date;

import com.strandls.userGroup.pojo.UserGroupFilterData;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserGroupFilterService {

	public void bgCheckForRule(UserGroupFilterData ugFilterData);

	public Boolean checkObservedOnDateFilter(Long observationId, Date observedOnDate);

	public Boolean checkCreatedOnDateFilter(Long observationdId, Date createdOnDate);

	public Boolean checkUserRule(Long observationId, Long userId);

	public Boolean checkTaxonomicRule(Long observationId, Long taxonomyId);

	public Boolean checkSpatialRule(Long userGroupId, Double lat, Double lon);

}
