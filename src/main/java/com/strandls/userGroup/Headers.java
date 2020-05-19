/**
 * 
 */
package com.strandls.userGroup;

import javax.ws.rs.core.HttpHeaders;

import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.user.controller.UserServiceApi;

/**
 * @author Abhishek Rudra
 *
 */
public class Headers {

	public ActivitySerivceApi addActivityHeader(ActivitySerivceApi activityService, String token) {
		activityService.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, token);
		return activityService;
	}

	public UserServiceApi addUserHeader(UserServiceApi userService, String token) {
		userService.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, token);
		return userService;
	}

}
