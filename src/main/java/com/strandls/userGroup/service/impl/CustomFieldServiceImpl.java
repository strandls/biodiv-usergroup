/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.observation.controller.ObservationServiceApi;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.userGroup.dao.CustomFieldDao;
import com.strandls.userGroup.dao.CustomFieldUG18Dao;
import com.strandls.userGroup.dao.CustomFieldUG37Dao;
import com.strandls.userGroup.dao.CustomFieldValuesDao;
import com.strandls.userGroup.dao.CustomFieldsDao;
import com.strandls.userGroup.dao.ObservationCustomFieldDao;
import com.strandls.userGroup.dao.UserGroupCustomFieldMappingDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.pojo.CustomField;
import com.strandls.userGroup.pojo.CustomFieldCreateData;
import com.strandls.userGroup.pojo.CustomFieldData;
import com.strandls.userGroup.pojo.CustomFieldDetails;
import com.strandls.userGroup.pojo.CustomFieldFactsInsert;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldPermission;
import com.strandls.userGroup.pojo.CustomFieldUG18;
import com.strandls.userGroup.pojo.CustomFieldUG37;
import com.strandls.userGroup.pojo.CustomFieldUGData;
import com.strandls.userGroup.pojo.CustomFieldValues;
import com.strandls.userGroup.pojo.CustomFieldValuesCreateData;
import com.strandls.userGroup.pojo.CustomFieldValuesData;
import com.strandls.userGroup.pojo.CustomFields;
import com.strandls.userGroup.pojo.ObservationCustomField;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupCustomFieldMapping;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.service.CustomFieldServices;
import com.strandls.userGroup.service.UserGroupSerivce;

import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 *
 */
public class CustomFieldServiceImpl implements CustomFieldServices {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldServiceImpl.class);

	@Inject
	private UserGroupSerivce ugService;

	@Inject
	private UserGroupDao userGroupDao;

	@Inject
	private UserGroupObservationDao userGroupObvDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private CustomFieldDao cfDao;

	@Inject
	private CustomFieldsDao cfsDao;

	@Inject
	private CustomFieldValuesDao cfValueDao;

	@Inject
	private UserGroupCustomFieldMappingDao ugCFMappingDao;

	@Inject
	private CustomFieldUG18Dao cf18Dao;

	@Inject
	private CustomFieldUG37Dao cf37Dao;

	@Inject
	private ObservationCustomFieldDao observationCFDao;

	@Inject
	private ObservationServiceApi observationService;

	@Override
	public void migrateCustomField() {
		try {

			Map<Long, Long> previousToNew = new HashMap<Long, Long>();
			List<CustomField> cfList = cfDao.findAll();
			for (CustomField customField : cfList) {

				String dataType = "";
				if (customField.getDataType().equalsIgnoreCase("PARAGRAPH_TEXT")
						|| customField.getDataType().equalsIgnoreCase("TEXT"))
					dataType = "STRING";
				else if (customField.getDataType().equalsIgnoreCase("Integer"))
					dataType = "INTEGER";
				else if (customField.getDataType().equalsIgnoreCase("decimal"))
					dataType = "DECIMAL";
				else if (customField.getDataType().equalsIgnoreCase("date"))
					dataType = "DATE";

				String fieldType = "";
				if (customField.getDataType().equalsIgnoreCase("PARAGRAPH_TEXT")
						|| customField.getDataType().equalsIgnoreCase("TEXT"))
					fieldType = "FIELD TEXT";

				if (customField.getOptions() != null && customField.getOptions().trim().length() != 0) {
					if (customField.getAllowedMultiple())
						fieldType = "MULTIPLE CATEGORICAL";
					else
						fieldType = "SINGLE CATEGORICAL";
				}

				CustomFields cfs = new CustomFields(null, customField.getAuthorId(), customField.getName(), dataType,
						fieldType, null, null, customField.getNotes());
				cfs = cfsDao.save(cfs);
				previousToNew.put(customField.getId(), cfs.getId());

				if (customField.getOptions() != null) {
					CustomFieldValues cfValues = null;
					String options[] = customField.getOptions().split(",");
					for (String option : options) {
						cfValues = new CustomFieldValues(null, cfs.getId(), option.trim(), cfs.getAuthorId(), null,
								null);
						cfValues = cfValueDao.save(cfValues);
					}
				}

				UserGroupCustomFieldMapping ugCFMapping = new UserGroupCustomFieldMapping(null,
						customField.getAuthorId(), customField.getUserGroupId(), cfs.getId(),
						customField.getDefaultValue(), customField.getDisplayOrder(), customField.getIsMandatory(),
						customField.getAllowedPaticipation());

				ugCFMappingDao.save(ugCFMapping);
			}
			observationCustomFieldDataMigration(previousToNew);

			System.out.println("-------!!!!!!Custom field Migration Completed!!!!!!!!!!!!-----------");

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	private void observationCustomFieldDataMigration(Map<Long, Long> preciousToNew) {
		try {

			List<CustomFieldUG18> cf18DataList = cf18Dao.findAll();
			ObservationCustomField observationCF = null;
			Date date = new Date(0);
			for (CustomFieldUG18 cf18Data : cf18DataList) {
				Long authorId = Long
						.parseLong(observationService.getObservationAuthor(cf18Data.getObservationId().toString()));
				if (cf18Data.getCf5() != null && cf18Data.getCf5().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf18Data.getObservationId(), 18L,
							preciousToNew.get(5L), null, date, date, cf18Data.getCf5(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf18Data.getCf6() != null && cf18Data.getCf6().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf18Data.getObservationId(), 18L,
							preciousToNew.get(6L), null, date, date, cf18Data.getCf6(), null, null);
					observationCFDao.save(observationCF);
				}
			}

			List<CustomFieldUG37> cf37DataList = cf37Dao.findAll();
			for (CustomFieldUG37 cf37Data : cf37DataList) {
				Long authorId = Long
						.parseLong(observationService.getObservationAuthor(cf37Data.getObservationId().toString()));
				if (cf37Data.getCf_14501638() != null && cf37Data.getCf_14501638().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501638L), null, date, date, cf37Data.getCf_14501638(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501655() != null && cf37Data.getCf_14501655().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501655L), null, date, date, cf37Data.getCf_14501655(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501656() != null && cf37Data.getCf_14501656().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501656L), null, date, date, cf37Data.getCf_14501656(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501657() != null && cf37Data.getCf_14501657().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501657L), null, date, date, cf37Data.getCf_14501657(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501658() != null && cf37Data.getCf_14501658().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501658L), null, date, date, cf37Data.getCf_14501658(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501659() != null && cf37Data.getCf_14501659().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501659L), null, date, date, cf37Data.getCf_14501659(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501660() != null && cf37Data.getCf_14501660().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501660L), null, date, date, cf37Data.getCf_14501660(), null, null);
					observationCFDao.save(observationCF);
				}
				if (cf37Data.getCf_14501661() != null && cf37Data.getCf_14501661().trim().length() != 0) {
					observationCF = new ObservationCustomField(null, authorId, cf37Data.getObservationId(), 37L,
							preciousToNew.get(14501661L), null, date, date, cf37Data.getCf_14501661(), null, null);
					observationCFDao.save(observationCF);
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public List<CustomFieldObservationData> getObservationCustomFields(Long observationId) {

		List<CustomFieldObservationData> result = new ArrayList<CustomFieldObservationData>();
		List<UserGroupObservation> userGroupObservation = userGroupObvDao.findByObservationId(observationId);

		for (UserGroupObservation ugObservation : userGroupObservation) {
			List<UserGroupCustomFieldMapping> ugCFMappingList = ugCFMappingDao
					.findByUserGroupId(ugObservation.getUserGroupId());
			if (!(ugCFMappingList.isEmpty())) {

				List<CustomFieldData> customFieldDataList = new ArrayList<CustomFieldData>();

				for (UserGroupCustomFieldMapping ugCFMapping : ugCFMappingList) {
					CustomFields customFields = cfsDao.findById(ugCFMapping.getCustomFieldId());

					List<ObservationCustomField> observationCFData = observationCFDao.findByObservationIdUGidCFId(
							observationId, customFields.getId(), ugCFMapping.getUserGroupId());

					CustomFieldValuesData valuesData = null;

					if (!(observationCFData.isEmpty())) {

						if (customFields.getFieldType().equals("FIELD TEXT")) {
							String fieldTextData = "";
							if (customFields.getDataType().equalsIgnoreCase("String"))
								fieldTextData = observationCFData.get(0).getValueString();
							else if (customFields.getDataType().equalsIgnoreCase("Integer")
									|| customFields.getDataType().equalsIgnoreCase("Decimal"))
								fieldTextData = observationCFData.get(0).getValueNumeric().toString();
							else
								fieldTextData = observationCFData.get(0).getValueDate().toString();

							valuesData = new CustomFieldValuesData(fieldTextData, null, null, null, null);

						} else if (customFields.getFieldType().equals("SINGLE CATEGORICAL")) {

							CustomFieldValues singleCat = cfValueDao
									.findById(observationCFData.get(0).getCustomFieldValueId());

							valuesData = new CustomFieldValuesData(null, singleCat, null, null, null);

						} else if (customFields.getFieldType().equals("MULTIPLE CATEGORICAL")) {
							List<CustomFieldValues> multipleCat = new ArrayList<CustomFieldValues>();
							for (ObservationCustomField obvCF : observationCFData)
								multipleCat.add(cfValueDao.findById(obvCF.getCustomFieldValueId()));

							valuesData = new CustomFieldValuesData(null, null, multipleCat, null, null);

						} else if (customFields.getFieldType().equals("RANGE")) {

							String min = "";
							String max = "";

							for (ObservationCustomField obvCF : observationCFData) {
								CustomFieldValues rangeValue = cfValueDao.findById(obvCF.getCustomFieldValueId());
								if (customFields.getDataType().equalsIgnoreCase("String")) {
									if (rangeValue.getValues().equalsIgnoreCase("min"))
										min = obvCF.getValueString();
									if (rangeValue.getValues().equalsIgnoreCase("max"))
										max = obvCF.getValueString();
								} else if (customFields.getDataType().equalsIgnoreCase("Integer")
										|| customFields.getDataType().equalsIgnoreCase("Decimal")) {
									if (rangeValue.getValues().equalsIgnoreCase("min"))
										min = obvCF.getValueNumeric().toString();
									if (rangeValue.getValues().equalsIgnoreCase("max"))
										max = obvCF.getValueNumeric().toString();
								} else {
									if (rangeValue.getValues().equalsIgnoreCase("min"))
										min = obvCF.getValueDate().toString();
									if (rangeValue.getValues().equalsIgnoreCase("max"))
										max = obvCF.getValueDate().toString();

								}
							}

							valuesData = new CustomFieldValuesData(null, null, null, min, max);

						}

					}

					CustomFieldData customFieldData = new CustomFieldData(customFields.getId(), customFields.getName(),
							customFields.getDataType(), customFields.getFieldType(), customFields.getIconURL(),
							customFields.getNotes(), ugCFMapping.getDefaultValue(), customFields.getUnits(),
							ugCFMapping.getDisplayOrder(), ugCFMapping.getAllowedParticipation(), valuesData);

					customFieldDataList.add(customFieldData);

				}
				result.add(new CustomFieldObservationData(ugObservation.getUserGroupId(), customFieldDataList));

			}

		}

		return result;
	}

	@Override
	public List<CustomFieldValues> getCustomFieldOptions(CommonProfile profile, String observationId, Long userGroupId,
			Long customFieldId) throws Exception {

		List<CustomFieldValues> cfValues = new ArrayList<CustomFieldValues>();
		try {
			Boolean isAllowed = checkCustomFieldPermissions(profile, observationId, customFieldId, userGroupId);
			if (isAllowed) {
				cfValues = cfValueDao.findByCustomFieldId(customFieldId);
			} else {
				throw new Exception("User Not allowed to ADD/UPDATE the custom Field");
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}
		return cfValues;
	}

	private Boolean checkCustomFieldPermissions(CommonProfile profile, String observationId, Long customFieldId,
			Long userGroupId) throws Exception {

		try {
			Long userId = Long.parseLong(profile.getId());
			JSONArray userRoles = (JSONArray) profile.getAttribute("roles");
			UserGroup userGroup = userGroupDao.findById(userGroupId);
			UserGroupCustomFieldMapping ugCFMapping = ugCFMappingDao.findByUserGroupCustomFieldId(userGroupId,
					customFieldId);
			if (userRoles.contains("ROLE_ADMIN"))
				return true;

			if (ugCFMapping.getAllowedParticipation()) {
//				participation is allowed
				if (userGroup.getAllowUserToJoin()) {
//					open group .. so allow all user
					return true;
				} else {
//					close group
					Boolean isMember = userService.checkMemberRoleUG(userGroupId.toString());
					return isMember;
				}

			} else {
//				participation is not allowed
				Long authorId = Long.parseLong(observationService.getObservationAuthor(observationId));
				if (userGroup.getAllowUserToJoin()) {
//					open group
					if (authorId.equals(userId)) {
						return true;
					}
				} else {
//					closer group
					Boolean isMember = userService.checkMemberRoleUG(userGroupId.toString());
					return isMember;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	@Override
	public void createCustomFields(CommonProfile profile, CustomFieldCreateData customFieldCreateData) {
		try {

			Long authorId = Long.parseLong(profile.getId());

//			create custom field
			CustomFields customFields = new CustomFields(null, authorId, customFieldCreateData.getName(),
					customFieldCreateData.getDataType(), customFieldCreateData.getFieldType(),
					customFieldCreateData.getUnits(), customFieldCreateData.getIconURL(),
					customFieldCreateData.getNotes());

			customFields = cfsDao.save(customFields);

//			create custom Field values if any
			if (customFields.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")
					|| customFields.getFieldType().equals("MULTIPLE CATEGORICAL")) {

				for (CustomFieldValuesCreateData cfValuesCreate : customFieldCreateData.getValues()) {
					CustomFieldValues cfValues = new CustomFieldValues(null, customFields.getId(),
							cfValuesCreate.getValue(), authorId, cfValuesCreate.getIconURL(),
							cfValuesCreate.getNotes());
					cfValueDao.save(cfValues);

				}

			}
			if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {
				CustomFieldValues cfValues = new CustomFieldValues(null, customFields.getId(), "MIN", authorId, null,
						null);
				cfValueDao.save(cfValues);
				cfValues = new CustomFieldValues(null, customFields.getId(), "MAX", authorId, null, null);
				cfValueDao.save(cfValues);
			}

//			create custom field userGroup mapping and user Group specific properties

			UserGroupCustomFieldMapping ugCustomFieldMapping = new UserGroupCustomFieldMapping(null, authorId,
					customFieldCreateData.getUserGroupId(), customFields.getId(),
					customFieldCreateData.getDefaultValue(), customFieldCreateData.getDisplayOrder(),
					customFieldCreateData.getIsMandatory(), customFieldCreateData.getAllowedParticipation());

			ugCustomFieldMapping = ugCFMappingDao.save(ugCustomFieldMapping);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	@Override
	public List<CustomFieldObservationData> insertUpdateCustomFieldData(CommonProfile profile,
			CustomFieldFactsInsert factsCreateData) throws Exception {
		try {
			Boolean isAllowed = checkCustomFieldPermissions(profile, factsCreateData.getObservationId().toString(),
					factsCreateData.getCustomFieldId(), factsCreateData.getUserGroupId());
			if (isAllowed) {
				List<ObservationCustomField> observationCF = observationCFDao.findByObservationIdUGidCFId(
						factsCreateData.getObservationId(), factsCreateData.getCustomFieldId(),
						factsCreateData.getUserGroupId());

				CustomFields customFields = cfsDao.findById(factsCreateData.getCustomFieldId());
				Long authorId = Long.parseLong(profile.getId());

				if (observationCF != null && !(observationCF.isEmpty())) {
//				update of the custom Field data
					if (customFields.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")) {
//						single result always
						observationCF.get(0).setAuthorId(authorId);
						observationCF.get(0).setLastModified(new Date());
						observationCF.get(0).setCustomFieldValueId(factsCreateData.getSingleCategorical());
						observationCFDao.update(observationCF.get(0));
					} else if (customFields.getFieldType().equalsIgnoreCase("MULTIPLE CATEGORICAL")) {

//						can have multiple result
						List<Long> previousCFValueList = new ArrayList<Long>();
						for (ObservationCustomField obvCF : observationCF) {
							previousCFValueList.add(obvCF.getCustomFieldValueId());
//							delete the facts which are not assigned
							if (!(factsCreateData.getMultipleCategorical().contains(obvCF.getCustomFieldValueId()))) {
								observationCFDao.delete(obvCF);
							}
						}
						for (Long valueId : factsCreateData.getMultipleCategorical()) {
//							Add the new facts
							if (!(previousCFValueList.contains(valueId))) {
								ObservationCustomField obvCF = new ObservationCustomField(null, authorId,
										factsCreateData.getObservationId(), factsCreateData.getUserGroupId(),
										factsCreateData.getCustomFieldId(), valueId, new Date(), new Date(), null, null,
										null);
								observationCFDao.save(obvCF);
							}
						}

					} else if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {
//						can have at max 2 result but overWrite both
						List<CustomFieldValues> cfValues = cfValueDao
								.findByCustomFieldId(factsCreateData.getCustomFieldId());
						for (CustomFieldValues cfValue : cfValues) {
							int overWrite = 0;
							for (ObservationCustomField obvCF : observationCF) {
								if (obvCF.getCustomFieldId().equals(cfValue.getId())) {
									obvCF.setAuthorId(authorId);
									obvCF.setLastModified(new Date());
									obvCF = populateRange(customFields, cfValue, obvCF, factsCreateData);
									observationCFDao.update(obvCF);
									overWrite = 1;
								}
							}
							if (overWrite == 0) {
//								new entry for Range
								ObservationCustomField obvCF = new ObservationCustomField(null, authorId,
										factsCreateData.getObservationId(), factsCreateData.getUserGroupId(),
										factsCreateData.getCustomFieldId(), null, new Date(), new Date(), null, null,
										null);
								obvCF = populateRange(customFields, cfValue, obvCF, factsCreateData);
								observationCFDao.save(obvCF);
							}
						}

					} else {
//						field text box single result always
						observationCF.get(0).setAuthorId(authorId);
						observationCF.get(0).setLastModified(new Date());
						observationCFDao
								.update(populateFieldTextBox(customFields, observationCF.get(0), factsCreateData));
					}

				} else {
//				insert custom field data
					ObservationCustomField obvCF = new ObservationCustomField(null, authorId,
							factsCreateData.getObservationId(), factsCreateData.getUserGroupId(),
							factsCreateData.getCustomFieldId(), null, new Date(), new Date(), null, null, null);

					if (customFields.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")) {
						obvCF.setCustomFieldValueId(factsCreateData.getSingleCategorical());
						observationCFDao.save(obvCF);
					} else if (customFields.getFieldType().equalsIgnoreCase("MULTIPLE CATEGORICAL")) {
						for (Long cfValuesId : factsCreateData.getMultipleCategorical()) {
							obvCF.setCustomFieldValueId(cfValuesId);
							observationCFDao.save(obvCF);
						}
					} else if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {
						List<CustomFieldValues> cfValues = cfValueDao
								.findByCustomFieldId(factsCreateData.getCustomFieldId());
						for (CustomFieldValues cfValue : cfValues) {
							obvCF = populateRange(customFields, cfValue, obvCF, factsCreateData);
							observationCFDao.save(obvCF);
						}
					} else {
//						Field Text Box case
						obvCF = populateFieldTextBox(customFields, obvCF, factsCreateData);
						observationCFDao.save(obvCF);

					}
				}

				return getObservationCustomFields(factsCreateData.getObservationId());

			} else {
				throw new Exception("User not allowed to ADD/UPDATE the custom field");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw e;
		}

	}

	private ObservationCustomField populateFieldTextBox(CustomFields customFields, ObservationCustomField obvCF,
			CustomFieldFactsInsert factsCreateData) {

		try {
			if (customFields.getDataType().equalsIgnoreCase("String"))
				obvCF.setValueString(factsCreateData.getTextBoxValue());
			else if (customFields.getDataType().equalsIgnoreCase("Integer")
					|| customFields.getDataType().equalsIgnoreCase("Decimal"))
				obvCF.setValueNumeric(Double.parseDouble(factsCreateData.getTextBoxValue()));
			else
				obvCF.setValueDate(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(factsCreateData.getTextBoxValue()));

			return obvCF;
		} catch (Exception e) {
			logger.error("Error inside populate Field TextBox");
		}
		return null;

	}

	private ObservationCustomField populateRange(CustomFields customFields, CustomFieldValues cfValue,
			ObservationCustomField obvCF, CustomFieldFactsInsert factsCreateData) {
		try {
			if (cfValue.getValues().equalsIgnoreCase("min")) {
				obvCF.setCustomFieldValueId(cfValue.getId());
				if (customFields.getDataType().equalsIgnoreCase("String"))
					obvCF.setValueString(factsCreateData.getMinValue());
				else if (customFields.getDataType().equalsIgnoreCase("Integer")
						|| customFields.getDataType().equalsIgnoreCase("Decimal"))
					obvCF.setValueNumeric(Double.parseDouble(factsCreateData.getMinValue()));
				else
					obvCF.setValueDate(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(factsCreateData.getMinValue()));

			}
			if (cfValue.getValues().equalsIgnoreCase("max")) {
				obvCF.setCustomFieldValueId(cfValue.getId());
				if (customFields.getDataType().equalsIgnoreCase("String"))
					obvCF.setValueString(factsCreateData.getMaxValue());
				else if (customFields.getDataType().equalsIgnoreCase("Integer")
						|| customFields.getDataType().equalsIgnoreCase("Decimal"))
					obvCF.setValueNumeric(Double.parseDouble(factsCreateData.getMaxValue()));
				else
					obvCF.setValueDate(
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(factsCreateData.getMaxValue()));

			}
			return obvCF;

		} catch (Exception e) {
			logger.error("Error inside Populate Range");
		}
		return null;
	}

	@Override
	public List<CustomFieldPermission> getCustomFieldPermisison(CommonProfile profile, String observationId)
			throws Exception {

		List<CustomFieldPermission> cfPermissionList = new ArrayList<CustomFieldPermission>();
		List<Long> allowedCFId = null;

		List<UserGroupIbp> userGroupListIbp = ugService.fetchByObservationId(Long.parseLong(observationId));
		List<Long> userGroupList = new ArrayList<Long>();
		for (UserGroupIbp ugIBP : userGroupListIbp)
			userGroupList.add(ugIBP.getId());

		for (Long userGroupId : userGroupList) {
			List<UserGroupCustomFieldMapping> ugcfMappingList = ugCFMappingDao.findByUserGroupId(userGroupId);
			if (!ugcfMappingList.isEmpty()) {
				allowedCFId = new ArrayList<Long>();
				for (UserGroupCustomFieldMapping ugCFMapping : ugcfMappingList) {
					Boolean isAllowed = checkCustomFieldPermissions(profile, observationId,
							ugCFMapping.getCustomFieldId(), userGroupId);
					if (isAllowed)
						allowedCFId.add(ugCFMapping.getCustomFieldId());
				}
				cfPermissionList.add(new CustomFieldPermission(userGroupId, allowedCFId));
			}
		}
		return cfPermissionList;

	}

	@Override
	public List<CustomFieldDetails> getCustomField(Long userGroupId) {
		List<CustomFieldDetails> result = new ArrayList<CustomFieldDetails>();
		List<CustomFieldValues> cfValues = new ArrayList<CustomFieldValues>();
		List<UserGroupCustomFieldMapping> ugCFMappingList = ugCFMappingDao.findByUserGroupId(userGroupId);
		for (UserGroupCustomFieldMapping ugCFMapping : ugCFMappingList) {
			CustomFields customField = cfsDao.findById(ugCFMapping.getCustomFieldId());
			if (customField.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")
					|| customField.getFieldType().equalsIgnoreCase("MULTIPLE CATEGORICAL")) {
				cfValues = cfValueDao.findByCustomFieldId(customField.getId());

			}

			result.add(new CustomFieldDetails(customField, cfValues, ugCFMapping.getDefaultValue(),
					ugCFMapping.getDisplayOrder(), ugCFMapping.getIsMandatory(),
					ugCFMapping.getAllowedParticipation()));

		}

		return result;

	}

	@Override
	public List<CustomFieldDetails> removeCustomField(Long userGroupId, Long customFieldId) {
		UserGroupCustomFieldMapping ugCFMapping = ugCFMappingDao.findByUserGroupCustomFieldId(userGroupId,
				customFieldId);
		ugCFMappingDao.delete(ugCFMapping);
		return getCustomField(userGroupId);
	}

	@Override
	public List<CustomFieldDetails> getAllCustomField() {

		List<CustomFieldDetails> result = new ArrayList<CustomFieldDetails>();

		List<CustomFieldValues> cfValues = null;

		List<CustomFields> customFieldList = cfsDao.findAll();

		for (CustomFields customField : customFieldList) {

			if (customField.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")
					|| customField.getFieldType().equalsIgnoreCase("MULTIPLE CATEGORICAL")) {
				cfValues = cfValueDao.findByCustomFieldId(customField.getId());

			}
			result.add(new CustomFieldDetails(customField, cfValues, null, null, null, null));

		}
		return result;
	}

	@Override
	public List<CustomFieldDetails> addCustomFieldUG(Long userId, CustomFieldUGData customFieldUGData) {
		UserGroupCustomFieldMapping ugCFMapping = new UserGroupCustomFieldMapping(null, userId,
				customFieldUGData.getUserGroupId(), customFieldUGData.getCustomFieldId(),
				customFieldUGData.getDefaultValue(), customFieldUGData.getDisplayOrder(),
				customFieldUGData.getIsMandatory(), customFieldUGData.getAllowedParticipation());
		ugCFMappingDao.save(ugCFMapping);

		return getCustomField(customFieldUGData.getUserGroupId());
	}

}
