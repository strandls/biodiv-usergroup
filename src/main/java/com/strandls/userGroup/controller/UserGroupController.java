/**
 * 
 */
package com.strandls.userGroup.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.pac4j.core.profile.CommonProfile;

import com.google.inject.Inject;
import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.userGroup.ApiConstants;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.service.UserGroupSerivce;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author Abhishek Rudra
 *
 */

@Api("UserGroup Serivce")
@Path(ApiConstants.V1 + ApiConstants.GROUP)
public class UserGroupController {

	@Inject
	private UserGroupSerivce ugServices;

	@GET
	@Path("/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find UserGroup by ID", notes = "Returns UserGroup details", response = UserGroup.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })
	public Response getUserGroup(@PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			UserGroup userGroup = ugServices.fetchByGroupId(id);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.IBP + "/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find UserGroup by ID", notes = "Returns UserGroup details for IBP", response = UserGroupIbp.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })
	public Response getIbpData(@PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			UserGroupIbp ibp = ugServices.fetchByGroupIdIbp(id);
			return Response.status(Status.OK).entity(ibp).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find UserGroup by observation ID", notes = "Returns UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })

	public Response getObservationUserGroup(@PathParam("observationId") String observationId) {
		try {
			Long id = Long.parseLong(observationId);
			List<UserGroupIbp> userGroup = ugServices.fetchByObservationId(id);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	@GET
	@Path(ApiConstants.GROUPLIST)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Find list of UserGroup based on UserId", notes = "Return UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class) })

	public Response getUserGroupList(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long sUserId = Long.parseLong(profile.getId());
			List<UserGroupIbp> userGroupList = ugServices.fetchByUserId(sUserId);
			return Response.status(Status.OK).entity(userGroupList).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}

	}

	@POST
	@Path(ApiConstants.CREATE + "/{obsId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns List of UserGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

	public Response createObservationUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId, @ApiParam(name = "userGroups") List<Long> userGroups) {
		try {

			Long observationId = Long.parseLong(obsId);
			List<Long> result = ugServices.createUserGroupObservationMapping(observationId, userGroups);
			if (result == null)
				return Response.status(Status.CONFLICT).entity("Error occured in transaction").build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
