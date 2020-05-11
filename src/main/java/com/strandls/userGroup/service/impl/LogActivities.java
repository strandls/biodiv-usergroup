/**
 * 
 */
package com.strandls.userGroup.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.activity.pojo.ActivityLoggingData;
import com.strandls.activity.pojo.MailData;
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

	public void LogActivity(HttpServletRequest request, String activityDescription, Long rootObjectId,
			Long subRootObjectId, String rootObjectType, Long activityId, String activityType, MailData mailData) {

		try {
			ActivityLoggingData activityLogging = new ActivityLoggingData();
			activityLogging.setActivityDescription(activityDescription);
			activityLogging.setActivityId(activityId);
			activityLogging.setActivityType(activityType);
			activityLogging.setRootObjectId(rootObjectId);
			activityLogging.setRootObjectType(rootObjectType);
			activityLogging.setSubRootObjectId(subRootObjectId);
			activityLogging.setMailData(mailData);
			activityService = headers.addActivityHeader(activityService, request);
			activityService.logActivity(activityLogging);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

	public void logUserGroupActivities(HttpServletRequest request, String activityDescription, Long rootObjectId,
			Long subRootObjectId, String rootObjectType, Long activityId, String activityType) {
		try {
			UserGroupActivityLogging ugActivityLogging = new UserGroupActivityLogging();
			ugActivityLogging.setActivityDescription(activityDescription);
			ugActivityLogging.setActivityId(activityId);
			ugActivityLogging.setActivityType(activityType);
			ugActivityLogging.setRootObjectId(rootObjectId);
			ugActivityLogging.setRootObjectType(rootObjectType);
			ugActivityLogging.setSubRootObjectId(subRootObjectId);

			activityService = headers.addActivityHeader(activityService, request);
			activityService.logUserGroupActivity(ugActivityLogging);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

	}

}
