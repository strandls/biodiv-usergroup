/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.userGroup.pojo.CustomFieldCreateData;
import com.strandls.userGroup.pojo.CustomFieldFactsInsert;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldValues;

/**
 * @author Abhishek Rudra
 *
 */
public interface CustomFieldServices {

	public void migrateCustomField();

	public List<CustomFieldObservationData> getObservationCustomFields(Long observationId);

	public List<CustomFieldValues> getCustomFieldOptions(CommonProfile profile, String observationId, Long userGroupId,
			Long customFieldId) throws Exception;

	public List<CustomFieldObservationData> insertUpdateCustomFieldData(CommonProfile profile,
			CustomFieldFactsInsert factsCreateData) throws Exception;

	public void createCustomFields(CommonProfile profile, CustomFieldCreateData customFieldCreateData);

}
