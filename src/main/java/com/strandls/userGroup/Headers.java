/**
 * 
 */
package com.strandls.userGroup;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import com.strandls.activity.controller.ActivitySerivceApi;
import com.strandls.user.controller.UserServiceApi;

/**
 * @author Abhishek Rudra
 *
 */
public class Headers {

	public ActivitySerivceApi addActivityHeader(ActivitySerivceApi activityService, HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		activityService.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, authHeader);
		return activityService;
	}

	public UserServiceApi addUserHeader(UserServiceApi userService, HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		userService.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, authHeader);
		return userService;
	}

}
