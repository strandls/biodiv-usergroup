/**
 * 
 */
package com.strandls.userGroup.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
import com.strandls.userGroup.pojo.CustomFieldCreateData;
import com.strandls.userGroup.pojo.CustomFieldDetails;
import com.strandls.userGroup.pojo.CustomFieldFactsInsert;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldPermission;
import com.strandls.userGroup.pojo.CustomFieldUGData;
import com.strandls.userGroup.pojo.CustomFieldValues;
import com.strandls.userGroup.service.CustomFieldServices;
import com.strandls.userGroup.service.impl.CustomFieldMigrationThread;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 *
 */

@Api("CustomField Service")
@Path(ApiConstants.V1 + ApiConstants.CUSTOMFIELD)
public class CustomFieldController {

	@Inject
	private CustomFieldMigrationThread cfMigrationThread;

	@Inject
	private CustomFieldServices cfService;

	@POST
	@Path(ApiConstants.MIGRATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	public Response migrateCustomField() {
		try {
			Thread customFieldMigrationThread = new Thread(cfMigrationThread);
			customFieldMigrationThread.start();
			return Response.status(Status.OK).entity("Custom Field migration has started").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Finds the Custom fields for the specified Observaiton", notes = "Return all the Custom field associated with a observation", response = CustomFieldObservationData.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to retrieve the data", response = String.class) })
	public Response getObservationCustomFields(@PathParam("observationId") String observationId) {
		try {
			Long obvId = Long.parseLong(observationId);
			List<CustomFieldObservationData> result = cfService.getObservationCustomFields(obvId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.OPTIONS + "/{observationId}/{userGroupId}/{cfId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Finds the set of Values for a Custom Field", notes = "Returns the Set of Values of Custom Field", response = CustomFieldValues.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to get the value list", response = String.class) })

	public Response getCustomFieldOptions(@Context HttpServletRequest request,
			@PathParam("observationId") String observationId, @PathParam("userGroupId") String userGroupId,
			@PathParam("cfId") String cfId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long customFieldId = Long.parseLong(cfId);
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldValues> result = cfService.getCustomFieldOptions(profile, observationId, ugId,
					customFieldId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.INSERT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Insert/Update custom field Data", notes = "Return a complete customField Data for the Observaiton", response = CustomFieldObservationData.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to add/Update the data", response = String.class) })

	public Response addUpdateCustomFieldData(@Context HttpServletRequest request,
			@ApiParam(name = "factsCreateData") CustomFieldFactsInsert factsCreateData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			List<CustomFieldObservationData> result = cfService.insertUpdateCustomFieldData(profile, factsCreateData);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Adds a new Custom Field", notes = "Adds a new Custom Field", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to create a new CustomField", response = String.class) })

	public Response addNewCustomField(@Context HttpServletRequest request,
			@ApiParam("customFieldData") CustomFieldCreateData customFieldCreateData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userRoles = (JSONArray) profile.getAttribute("roles");
			if (userRoles.contains("ROLE_ADMIN")) {
				cfService.createCustomFields(profile, customFieldCreateData);
				return Response.status(Status.OK).entity("New CustomField Created").build();
			} else {
				return Response.status(Status.NOT_ACCEPTABLE).entity("ONLY ADMIN CAN PERFORM THIS TASK").build();
			}

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + "/{observationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Checks the current user permission for custom Field", notes = "Returns the list of cfid group wises", response = CustomFieldPermission.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to get the permission", response = String.class) })

	public Response getCustomFieldPermission(@Context HttpServletRequest request,
			@PathParam("observationId") String observationId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			List<CustomFieldPermission> result = cfService.getCustomFieldPermisison(profile, observationId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.GROUP + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Find all the custom field related with a userGroup", notes = "Returns all the customField related with a userGroup", response = CustomFieldDetails.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to retrive the data", response = String.class) })

	public Response getUserGroupCustomFields(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId) {
		try {
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldDetails> customField = cfService.getCustomField(ugId);
			return Response.status(Status.OK).entity(customField).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Find all the custom field", notes = "Returns all the customField", response = CustomFieldDetails.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to retrive the data", response = String.class) })

	public Response getAllCustomField(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userProfiles = (JSONArray) profile.getAttribute("roles");
			if (userProfiles.contains("ROLE_ADMIN")) {
				List<CustomFieldDetails> result = cfService.getAllCustomField();
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("USER NOT ALLOWED TO REMOVE CUSTOM FIELD").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Add a already existing customField to a UserGroup", notes = "Returns all the customField related with a userGroup", response = CustomFieldDetails.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to retrive the data", response = String.class) })

	public Response addCustomField(@Context HttpServletRequest request,
			@ApiParam(name = "CustomFieldUserGroupData") CustomFieldUGData customFieldUGData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userRole = (JSONArray) profile.getAttribute("roles");
			if (userRole.contains("ROLE_ADMIN")) {
				Long userId = Long.parseLong(profile.getId());
				List<CustomFieldDetails> result = cfService.addCustomFieldUG(userId, customFieldUGData);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("user not allowed to add custom field").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.REMOVE + "/{userGroupId}/{customFieldId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Remove a custom field related with a userGroup", notes = "Returns all the customField related with a userGroup", response = CustomFieldDetails.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to retrive the data", response = String.class) })

	public Response removeCustomField(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId,
			@PathParam("customFieldId") String customFieldId) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userProfiles = (JSONArray) profile.getAttribute("roles");
			if (userProfiles.contains("ROLE_ADMIN")) {
				Long cfId = Long.parseLong(customFieldId);
				Long ugId = Long.parseLong(userGroupId);
				List<CustomFieldDetails> result = cfService.removeCustomField(ugId, cfId);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("USER NOT ALLOWED TO REMOVE CUSTOM FIELD").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
