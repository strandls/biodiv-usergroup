/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.userGroup.pojo.CustomFieldCreateData;
import com.strandls.userGroup.pojo.CustomFieldDetails;
import com.strandls.userGroup.pojo.CustomFieldFactsInsertData;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldPermission;
import com.strandls.userGroup.pojo.CustomFieldUGData;
import com.strandls.userGroup.pojo.CustomFieldValues;

/**
 * @author Abhishek Rudra
 *
 */
public interface CustomFieldServices {

	public void migrateCustomField();

	public List<CustomFieldObservationData> getObservationCustomFields(Long observationId);

	public List<CustomFieldValues> getCustomFieldOptions(HttpServletRequest request, CommonProfile profile,
			String observationId, Long userGroupId, Long customFieldId) throws Exception;

	public List<CustomFieldObservationData> insertUpdateCustomFieldData(HttpServletRequest request,
			CommonProfile profile, CustomFieldFactsInsertData factsCreateData) throws Exception;

	public CustomFieldDetails createCustomFields(CommonProfile profile, CustomFieldCreateData customFieldCreateData);

	public List<CustomFieldPermission> getCustomFieldPermisison(HttpServletRequest request, CommonProfile profile,
			String observationId) throws Exception;

	public List<CustomFieldDetails> getAllCustomField();

	public List<CustomFieldDetails> getCustomField(CommonProfile profile, Long userGroupId);

	public List<CustomFieldDetails> addCustomFieldUG(CommonProfile profile, Long userId, Long userGroupId,
			List<CustomFieldUGData> customFieldUGDataList);

	public List<CustomFieldDetails> removeCustomField(CommonProfile profile, Long userGroupId, Long customFieldId);

}
