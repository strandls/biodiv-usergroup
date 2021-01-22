/**
 * 
 */
package com.strandls.userGroup.service.impl;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.DocumentActivityLogging;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.SpeciesActivityLogging;
import com.strandls.activity.pojo.UserGroupActivityLogging;
import com.strandls.userGroup.Headers;

/**
 * @author Abhishek Rudra
 *
 */
public class LogActivities {

	private final Logger logger = LoggerFactory.getLogger(LogActivities.class);

	@Inject
	private ActivitySerivceApi activityService;

	@Inject
	private Headers headers;

	public void LogActivity(String authHeader, String activityDescription, Long rootObjectId, Long subRootObjectId,
			String rootObjectType, Long activityId, String activityType, MailData mailData) {

		try {
			ActivityLoggingData activityLogging = new ActivityLoggingData();
			activityLogging.setActivityDescription(activityDescription);
			activityLogging.setActivityId(activityId);
			activityLogging.setActivityType(activityType);
			activityLogging.setRootObjectId(rootObjectId);
			activityLogging.setRootObjectType(rootObjectType);
			activityLogging.setSubRootObjectId(subRootObjectId);
			activityLogging.setMailData(mailData);
			activityService = headers.addActivityHeader(activityService, authHeader);
			activityService.logActivity(activityLogging);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void logUserGroupActivities(String authHeader, String activityDescription, Long rootObjectId,
			Long subRootObjectId, String rootObjectType, Long activityId, String activityType) {
		try {
			UserGroupActivityLogging ugActivityLogging = new UserGroupActivityLogging();
			ugActivityLogging.setActivityDescription(activityDescription);
			ugActivityLogging.setActivityId(activityId);
			ugActivityLogging.setActivityType(activityType);
			ugActivityLogging.setRootObjectId(rootObjectId);
			ugActivityLogging.setRootObjectType(rootObjectType);
			ugActivityLogging.setSubRootObjectId(subRootObjectId);

			activityService = headers.addActivityHeader(activityService, authHeader);
			activityService.logUserGroupActivity(ugActivityLogging);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void LogDocumentActivities(String authHeader, String activityDescription, Long rootObjectId,
			Long subRootObjectId, String rootObjectType, Long activityId, String activityType, MailData mailData) {
		try {

			DocumentActivityLogging loggingData = new DocumentActivityLogging();
			loggingData.setActivityDescription(activityDescription);
			loggingData.setActivityId(activityId);
			loggingData.setActivityType(activityType);
			loggingData.setRootObjectId(rootObjectId);
			loggingData.setRootObjectType(rootObjectType);
			loggingData.setSubRootObjectId(subRootObjectId);
			loggingData.setMailData(mailData);

			activityService = headers.addActivityHeader(activityService, authHeader);
			activityService.logDocumentActivity(loggingData);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void logSpeciesActivities(String authHeader, String activityDescription, Long rootObjectId,
			Long subRootObjectId, String rootObjectType, Long activityId, String activityType, MailData mailData) {
		try {
			SpeciesActivityLogging speciesActivity = new SpeciesActivityLogging();
			speciesActivity.setActivityDescription(activityDescription);
			speciesActivity.setActivityId(activityId);
			speciesActivity.setActivityType(activityType);
			speciesActivity.setMailData(mailData);
			speciesActivity.setRootObjectId(rootObjectId);
			speciesActivity.setRootObjectType(rootObjectType);
			speciesActivity.setSubRootObjectId(subRootObjectId);

			activityService = headers.addActivityHeader(activityService, authHeader);
			activityService.logSpeciesActivities(speciesActivity);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
