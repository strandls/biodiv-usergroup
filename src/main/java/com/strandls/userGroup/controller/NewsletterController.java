package com.strandls.userGroup.controller;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.user.ApiException;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.userGroup.ApiConstants;
import com.strandls.userGroup.pojo.Newsletter;
import com.strandls.userGroup.pojo.NewsletterWithParentChildRelationship;
import com.strandls.userGroup.service.NewsletterSerivce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.minidev.json.JSONArray;

@Api("Newsletter Serivce")
@Path(ApiConstants.V1 + ApiConstants.NEWSLETTER)
public class NewsletterController {

	private static final String ENGLISH_LANGAUAGE_ID = "205";
	@Inject
	private NewsletterSerivce newsletterSerivce;
	
	@Inject
	private UserServiceApi userServiceApi;

	@GET
	@Path("ping")
	@Produces(MediaType.TEXT_PLAIN)
	public String ping() {
		return "pong";
	}

	@GET
	@Path("{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find Newsletter by ID", notes = "Returns Newsletter details", response = Newsletter.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Newsletter not found", response = String.class) })
	public Response getNewsletter(@PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			Newsletter newsletter = newsletterSerivce.findById(id);
			return Response.status(Status.OK).entity(newsletter).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("group")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find Newsletter by ID", notes = "Returns Newsletter details", response = Newsletter.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Newsletter not found", response = String.class) })
	public Response getNewslettersByGroup(@Context HttpServletRequest request,
			@QueryParam("userGroupId") Long userGroupId,
			@QueryParam("languageId") @DefaultValue(ENGLISH_LANGAUAGE_ID) Long languageId) throws ApiException {
		// validate request

		String authorizationHeader = ((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION);
		Boolean showSticky = false;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			if (profile == null)
				return Response.status(Status.UNAUTHORIZED).entity("Invalid JWT token").build();
			JSONArray roles = (JSONArray) profile.getAttribute("roles");

			userServiceApi.getApiClient().addDefaultHeader(HttpHeaders.AUTHORIZATION, authorizationHeader);
			Boolean isFounder = userServiceApi.checkFounderRole(userGroupId+"");
			Boolean isModerator = userServiceApi.checkModeratorRole(userGroupId+"");
			
			if(roles.contains("ROLE_ADMIN") || isFounder || isModerator) {
				showSticky = true;
			}
		}
		// Validation end
		try {
			List<NewsletterWithParentChildRelationship> newsletter = newsletterSerivce
					.getByUserGroupAndLanguage(userGroupId, languageId, showSticky);
			return Response.status(Status.OK).entity(newsletter).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
