/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.pojo.MailData;
import com.strandls.authentication_utility.util.AuthUtil;
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
import com.strandls.userGroup.pojo.CustomFieldFactsInsertData;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldPermission;
import com.strandls.userGroup.pojo.CustomFieldReordering;
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
import com.strandls.userGroup.service.UserGroupMemberService;
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
	private LogActivities logActivity;

	@Inject
	private UserGroupMemberService ugMemberService;

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
				Long authorId = userGroupDao.getObservationAuthor(cf18Data.getObservationId().toString());
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
				Long authorId = userGroupDao.getObservationAuthor(cf37Data.getObservationId().toString());
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
	public List<CustomFieldValues> getCustomFieldOptions(HttpServletRequest request, CommonProfile profile,
			String observationId, Long userGroupId, Long customFieldId) throws Exception {

		List<CustomFieldValues> cfValues = new ArrayList<CustomFieldValues>();
		try {
			Boolean isAllowed = checkCustomFieldPermissions(request, profile, observationId, customFieldId,
					userGroupId);
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

	private Boolean checkCustomFieldPermissions(HttpServletRequest request, CommonProfile profile, String observationId,
			Long customFieldId, Long userGroupId) throws Exception {

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
					Boolean isMember = ugMemberService.checkUserGroupMember(userId, userGroupId);
					return isMember;
				}

			} else {
//				participation is not allowed
				Long authorId = userGroupDao.getObservationAuthor(observationId);
				if (userGroup.getAllowUserToJoin()) {
//					open group
					if (authorId.equals(userId)) {
						return true;
					}
				} else {
//					closer group
					Boolean isMember = ugMemberService.checkUserGroupMember(userId, userGroupId);
					return isMember;
				}
			}
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	@Override
	public List<CustomFieldDetails> createCustomFields(HttpServletRequest request, CommonProfile profile,
			CustomFieldCreateData customFieldCreateData) {
		try {

			List<String> fieldTypesList = new ArrayList<String>(
					Arrays.asList("SINGLE CATEGORICAL", "MULTIPLE CATEGORICAL", "FIELD TEXT", "RANGE"));
			List<String> dataTypeList = new ArrayList<String>(Arrays.asList("STRING", "INTEGER", "DECIMAL", "DATE"));

			if (fieldTypesList.contains(customFieldCreateData.getFieldType())
					&& dataTypeList.contains(customFieldCreateData.getDataType())) {

//				single and multiple categorical should be allowed only for string
				if (customFieldCreateData.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")
						|| customFieldCreateData.getFieldType().equals("MULTIPLE CATEGORICAL")) {
					if (!customFieldCreateData.getDataType().equals("STRING"))
						return null;
				}

//				Range field type should not have String data type
				if (customFieldCreateData.getFieldType().equalsIgnoreCase("RANGE")) {
					if (customFieldCreateData.getDataType().equals("STRING"))
						return null;
				}

				JSONArray roles = (JSONArray) profile.getAttribute("roles");
				Long userId = Long.parseLong(profile.getId());
				Boolean isFounder = ugMemberService.checkFounderRole(userId, customFieldCreateData.getUserGroupId());
				if (roles.contains("ROLE_ADMIN") || isFounder) {

					Long authorId = Long.parseLong(profile.getId());
//					create custom field
					CustomFields customFields = new CustomFields(null, authorId, customFieldCreateData.getName(),
							customFieldCreateData.getDataType(), customFieldCreateData.getFieldType(),
							customFieldCreateData.getUnits(), customFieldCreateData.getIconURL(),
							customFieldCreateData.getNotes());

					customFields = cfsDao.save(customFields);

//					create custom Field values if any
					List<CustomFieldValues> cfValueList = new ArrayList<CustomFieldValues>();
					CustomFieldValues cfValues = null;
					if (customFields.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")
							|| customFields.getFieldType().equals("MULTIPLE CATEGORICAL")) {

						for (CustomFieldValuesCreateData cfValuesCreate : customFieldCreateData.getValues()) {
							cfValues = new CustomFieldValues(null, customFields.getId(), cfValuesCreate.getValue(),
									authorId, cfValuesCreate.getIconURL(), cfValuesCreate.getNotes());
							cfValues = cfValueDao.save(cfValues);
							cfValueList.add(cfValues);

						}

					}
					if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {
						cfValues = new CustomFieldValues(null, customFields.getId(), "MIN", authorId, null, null);
						cfValues = cfValueDao.save(cfValues);
						cfValueList.add(cfValues);
						cfValues = new CustomFieldValues(null, customFields.getId(), "MAX", authorId, null, null);
						cfValues = cfValueDao.save(cfValues);
						cfValueList.add(cfValues);
					}

					List<CustomFieldUGData> customFieldUGDataList = new ArrayList<CustomFieldUGData>();
					customFieldUGDataList.add(new CustomFieldUGData(customFields.getId(),
							customFieldCreateData.getDefaultValue(), customFieldCreateData.getDisplayOrder(),
							customFieldCreateData.getIsMandatory(), customFieldCreateData.getAllowedParticipation()));
					List<CustomFieldDetails> result = addCustomFieldUG(request, profile, authorId,
							customFieldCreateData.getUserGroupId(), customFieldUGDataList);
					return result;

				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public List<CustomFieldObservationData> insertUpdateCustomFieldData(HttpServletRequest request,
			CommonProfile profile, CustomFieldFactsInsertData factsInsertData) throws Exception {
		try {
			CustomFieldFactsInsert factsCreateData = factsInsertData.getFactsCreateData();
			Boolean isAllowed = checkCustomFieldPermissions(request, profile,
					factsCreateData.getObservationId().toString(), factsCreateData.getCustomFieldId(),
					factsCreateData.getUserGroupId());
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

//						logging activity for signle categorical

						CustomFieldValues cfValue = cfValueDao.findById(factsCreateData.getSingleCategorical());

						String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName() + " : "
								+ cfValue.getValues();
						MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
								factsInsertData.getMailData());
						logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								factsCreateData.getObservationId(), factsCreateData.getObservationId(), "observation",
								factsCreateData.getObservationId(), "Custom field edited", mailData);

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

//								Logging activity for multiple categorical
								String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName()
										+ " : " + cfValueDao.findById(valueId).getValues();

								MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
										factsInsertData.getMailData());

								logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
										factsCreateData.getObservationId(), factsCreateData.getObservationId(),
										"observation", factsCreateData.getObservationId(), "Custom field edited",
										mailData);
							}
						}

					} else if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {
//						has to be 2 result but overWrite both
//						validation
						if (factsCreateData.getMinValue() != null && factsCreateData.getMaxValue() != null) {

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
//									new entry for Range
									ObservationCustomField obvCF = new ObservationCustomField(null, authorId,
											factsCreateData.getObservationId(), factsCreateData.getUserGroupId(),
											factsCreateData.getCustomFieldId(), null, new Date(), new Date(), null,
											null, null);
									obvCF = populateRange(customFields, cfValue, obvCF, factsCreateData);
									observationCFDao.save(obvCF);
								}
							}

//							logging activity for range max type
							String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName()
									+ " (Range) : " + factsCreateData.getMinValue() + " - "
									+ factsCreateData.getMaxValue();
							MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
									factsInsertData.getMailData());

							logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
									factsCreateData.getObservationId(), factsCreateData.getObservationId(),
									"observation", factsCreateData.getObservationId(), "Custom field edited", mailData);

						}

					} else {
//						field text box single result always
						observationCF.get(0).setAuthorId(authorId);
						observationCF.get(0).setLastModified(new Date());
						observationCFDao.update(
								populateFieldTextBox(request, customFields, observationCF.get(0), factsInsertData));
					}

				} else {
//				insert custom field data
					ObservationCustomField obvCF = new ObservationCustomField(null, authorId,
							factsCreateData.getObservationId(), factsCreateData.getUserGroupId(),
							factsCreateData.getCustomFieldId(), null, new Date(), new Date(), null, null, null);

					if (customFields.getFieldType().equalsIgnoreCase("SINGLE CATEGORICAL")) {
						obvCF.setCustomFieldValueId(factsCreateData.getSingleCategorical());
						observationCFDao.save(obvCF);

//						logging activity for multiple categorical
						CustomFieldValues cfValue = cfValueDao.findById(factsCreateData.getSingleCategorical());
						String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName() + " : "
								+ cfValue.getValues();
						MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
								factsInsertData.getMailData());
						logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								factsCreateData.getObservationId(), factsCreateData.getObservationId(), "observation",
								factsCreateData.getObservationId(), "Custom field edited", mailData);

					} else if (customFields.getFieldType().equalsIgnoreCase("MULTIPLE CATEGORICAL")) {
						for (Long cfValuesId : factsCreateData.getMultipleCategorical()) {
							obvCF.setCustomFieldValueId(cfValuesId);
							observationCFDao.save(obvCF);

//							loggig activity for multiple categorical
							String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName() + " : "
									+ cfValueDao.findById(cfValuesId).getValues();
							MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
									factsInsertData.getMailData());
							logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
									factsCreateData.getObservationId(), factsCreateData.getObservationId(),
									"observation", factsCreateData.getObservationId(), "Custom field edited", mailData);
						}
					} else if (customFields.getFieldType().equalsIgnoreCase("RANGE")) {

						if (factsCreateData.getMinValue() != null && factsCreateData.getMaxValue() != null) {
							List<CustomFieldValues> cfValues = cfValueDao
									.findByCustomFieldId(factsCreateData.getCustomFieldId());

							for (CustomFieldValues cfValue : cfValues) {
								obvCF = populateRange(customFields, cfValue, obvCF, factsCreateData);
								observationCFDao.save(obvCF);
							}
//							logging activity for range max type
							String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName()
									+ " (Range) : " + factsCreateData.getMinValue() + " - "
									+ factsCreateData.getMaxValue();
							MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
									factsInsertData.getMailData());
							logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
									factsCreateData.getObservationId(), factsCreateData.getObservationId(),
									"observation", factsCreateData.getObservationId(), "Custom field edited", mailData);

						}

					} else {
//						Field Text Box case
						obvCF = populateFieldTextBox(request, customFields, obvCF, factsInsertData);
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

	private ObservationCustomField populateFieldTextBox(HttpServletRequest request, CustomFields customFields,
			ObservationCustomField obvCF, CustomFieldFactsInsertData factsInsertData) {

		try {
			CustomFieldFactsInsert factsCreateData = factsInsertData.getFactsCreateData();
			if (customFields.getDataType().equalsIgnoreCase("String"))
				obvCF.setValueString(factsCreateData.getTextBoxValue());
			else if (customFields.getDataType().equalsIgnoreCase("Integer")
					|| customFields.getDataType().equalsIgnoreCase("Decimal"))
				obvCF.setValueNumeric(Double.parseDouble(factsCreateData.getTextBoxValue()));
			else
				obvCF.setValueDate(
						new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ").parse(factsCreateData.getTextBoxValue()));

//			logging activity for Text box type
			String description = cfsDao.findById(factsCreateData.getCustomFieldId()).getName() + " : "
					+ factsCreateData.getTextBoxValue();
			MailData mailData = ugService.updateMailData(factsCreateData.getObservationId(),
					factsInsertData.getMailData());
			logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
					factsCreateData.getObservationId(), factsCreateData.getObservationId(), "observation",
					factsCreateData.getObservationId(), "Custom field edited", mailData);

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
	public List<CustomFieldPermission> getCustomFieldPermisison(HttpServletRequest request, CommonProfile profile,
			String observationId) throws Exception {

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
					Boolean isAllowed = checkCustomFieldPermissions(request, profile, observationId,
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
	public List<CustomFieldDetails> getCustomField(HttpServletRequest request, CommonProfile profile,
			Long userGroupId) {
		try {

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
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<CustomFieldDetails> removeCustomField(HttpServletRequest request, CommonProfile profile,
			Long userGroupId, Long customFieldId) {
		try {

			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				UserGroupCustomFieldMapping ugCFMapping = ugCFMappingDao.findByUserGroupCustomFieldId(userGroupId,
						customFieldId);

				if (ugCFMapping == null)
					return null;
				ugCFMappingDao.delete(ugCFMapping);
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null, userGroupId,
						userGroupId, "userGroup", customFieldId, "Removed Custom Field");

				return getCustomField(request, profile, userGroupId);

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
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
	public List<CustomFieldDetails> addCustomFieldUG(HttpServletRequest request, CommonProfile profile, Long userId,
			Long userGroupId, List<CustomFieldUGData> customFieldUGDataList) {
		try {

			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				for (CustomFieldUGData customFieldUGData : customFieldUGDataList) {
					UserGroupCustomFieldMapping ugCFMapping = new UserGroupCustomFieldMapping(null, userId, userGroupId,
							customFieldUGData.getCustomFieldId(), customFieldUGData.getDefaultValue(),
							customFieldUGData.getDisplayOrder(), customFieldUGData.getIsMandatory(),
							customFieldUGData.getAllowedParticipation());
					ugCFMappingDao.save(ugCFMapping);
					String desc = "Custom Field : " + cfsDao.findById(customFieldUGData.getCustomFieldId()).getName()
							+ " to Group : " + userGroupDao.findById(userGroupId).getName();
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
							userGroupId, "userGroup", customFieldUGData.getCustomFieldId(), "Added Custom Field");
				}
				return getCustomField(request, profile, userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<CustomFieldDetails> reorderingCustomFields(HttpServletRequest request, Long userGroupId,
			List<CustomFieldReordering> customFieldReorderings) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				List<UserGroupCustomFieldMapping> ugCFMappings = ugCFMappingDao.findByUserGroupId(userGroupId);
				Map<Long, Long> displayOrder = new HashMap<Long, Long>();
				List<Long> associatedCF = new ArrayList<Long>();
				for (UserGroupCustomFieldMapping ugCFMapping : ugCFMappings) {
					associatedCF.add(ugCFMapping.getCustomFieldId());
				}

				for (CustomFieldReordering cfReordering : customFieldReorderings) {
					if (associatedCF.contains(cfReordering.getCfId())) {
						if (!displayOrder.containsKey(cfReordering.getDisplayOrder())
								&& !displayOrder.containsValue(cfReordering.getCfId())) {
							displayOrder.put(cfReordering.getDisplayOrder(), cfReordering.getCfId());
						} else {
//							duplicate element either display order or cfid
							return null;
						}
					}
				}

				for (Entry<Long, Long> entry : displayOrder.entrySet()) {
					UserGroupCustomFieldMapping ugCfMapping = ugCFMappingDao.findByUserGroupCustomFieldId(userGroupId,
							entry.getValue());
					ugCfMapping.setDisplayOrder(Integer.parseInt(entry.getKey().toString()));
					ugCFMappingDao.update(ugCfMapping);
				}

				return getCustomField(request, profile, userGroupId);

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<CustomFieldDetails> addCustomFieldValues(HttpServletRequest request, Long customFieldId,
			Long userGroupId, CustomFieldValuesCreateData cfVCreateData) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains("ROLE_ADMIN") || isFounder) {
				Long authorId = Long.parseLong(profile.getId());
				CustomFieldValues cfValues = new CustomFieldValues(null, customFieldId, cfVCreateData.getValue(),
						authorId, cfVCreateData.getIconURL(), cfVCreateData.getNotes());
				cfValues = cfValueDao.save(cfValues);
				if (cfValues.getId() != null)
					return getCustomField(request, profile, userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

}
