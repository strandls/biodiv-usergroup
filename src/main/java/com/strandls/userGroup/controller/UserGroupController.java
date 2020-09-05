/**
 * 
 */
package com.strandls.userGroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.userGroup.ApiConstants;
import com.strandls.userGroup.dto.AuthenticationDTO;
import com.strandls.userGroup.pojo.AdministrationList;
import com.strandls.userGroup.pojo.BulkGroupPostingData;
import com.strandls.userGroup.pojo.BulkGroupUnPostingData;
import com.strandls.userGroup.pojo.EncryptionKey;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.ShowFilterRule;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupDocCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupFilterEnable;
import com.strandls.userGroup.pojo.UserGroupFilterRemove;
import com.strandls.userGroup.pojo.UserGroupFilterRuleInputData;
import com.strandls.userGroup.pojo.UserGroupHomePage;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupObvFilterData;
import com.strandls.userGroup.pojo.UserGroupPermissions;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UserGroupWKT;
import com.strandls.userGroup.service.UserGroupFilterService;
import com.strandls.userGroup.service.UserGroupMemberService;
import com.strandls.userGroup.service.UserGroupSerivce;
import com.strandls.userGroup.util.AppUtil;

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

@Api("UserGroup Serivce")
@Path(ApiConstants.V1 + ApiConstants.GROUP)
public class UserGroupController {

	@Inject
	private UserGroupSerivce ugServices;

	@Inject
	private UserGroupFilterService ugFilterService;

	@Inject
	private UserGroupMemberService ugMemberService;

	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)

	public Response pong() {
//		ugFilterService.checkUserRule(1L, 1L);
		return Response.status(Status.OK).entity("PONG").build();
	}

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
	@Path(ApiConstants.ALL + ApiConstants.OBSERVATION + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find all observation related to a userGroup", notes = "Return list of observation associated with a userGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Observation list not found", response = String.class) })
	public Response getAllObservation(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			List<Long> result = ugServices.findAllObservation(userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPLIST)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find list of UserGroup based on List of UserGroupId", notes = "Return UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class) })

	public Response getUserGroupList(
			@ApiParam(name = "userGroupMember") @QueryParam("userGroupMember") String userGroupMember) {
		try {
			String[] userGroupRole = userGroupMember.split(",");
			List<Long> memberList = new ArrayList<Long>();
			for (String s : userGroupRole)
				memberList.add(Long.parseLong(s.trim()));

			List<UserGroupIbp> userGroupList = ugServices.fetchByUserGroupDetails(memberList);
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
			@PathParam("obsId") String obsId,
			@ApiParam(name = "userGroupData") UserGroupMappingCreateData userGroupData) {
		try {

			Long observationId = Long.parseLong(obsId);
			List<Long> result = ugServices.createUserGroupObservationMapping(request, observationId, userGroupData);
			if (result == null)
				return Response.status(Status.CONFLICT).entity("Error occured in transaction").build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UPDATE + ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Update the UserGroup Observation Mapping", notes = "Returns the List of UserGroup Linked", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to Update the UserGroup Observation Mapping", response = String.class) })

	public Response updateUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("observationId") String observationId,
			@ApiParam(name = "userGroups") UserGroupMappingCreateData userGroup) {
		try {
			Long obvId = Long.parseLong(observationId);

			List<UserGroupIbp> result = ugServices.updateUserGroupObservationMapping(request, obvId, userGroup);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ALL)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find all the UserGroups", notes = "Returns all the UserGroups", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to fetch the UserGroups", response = String.class) })

	public Response getAllUserGroup() {
		try {
			List<UserGroupIbp> result = ugServices.fetchAllUserGroup();
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.FEATURED + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Find Featured", notes = "Return list Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Featured not Found", response = String.class) })

	public Response getAllFeatured(@PathParam("objectType") String objectType, @PathParam("objectId") String objectId) {

		try {
			Long id = Long.parseLong(objectId);
			List<Featured> featuredList = ugServices.fetchFeatured(objectType, id);
			return Response.status(Status.OK).entity(featuredList).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.FEATURED)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	@ApiOperation(value = "Posting of Featured to a Group", notes = "Returns the Details of Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to Feature in a Group", response = String.class) })
	public Response createFeatured(@Context HttpServletRequest request,
			@ApiParam(name = "featuredCreate") FeaturedCreateData featuredCreate) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			List<Featured> result = ugServices.createFeatured(request, userId, featuredCreate);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@PUT
	@Path(ApiConstants.UNFEATURED + "/{objectType}/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "UnFeatures a Object from a UserGroup", notes = "Returns the Current Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Unable to Unfeature", response = String.class) })
	public Response unFeatured(@Context HttpServletRequest request, @PathParam("objectType") String objectType,
			@PathParam("objectId") String objectId,
			@ApiParam("userGroupList") UserGroupMappingCreateData userGroupList) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long objId = Long.parseLong(objectId);
			List<Featured> result = ugServices.removeFeatured(request, userId, objectType, objId, userGroupList);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Checks the post creation rule", notes = "Add the observation Based on rules", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to set the filter Rule", response = String.class) })

	public Response getFilterRule(@Context HttpServletRequest request,
			@ApiParam(name = "ugObvFilterData") UserGroupObvFilterData ugObvFilterData) {
		try {
			ugFilterService.bgFiltureRule(request, ugObvFilterData);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE + ApiConstants.BULK + ApiConstants.POSTING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Checks the post creation rule in Bulk to post", notes = "Add the observation Based on rules in Bulk", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to set the filter Rule", response = String.class) })

	public Response bulkFilterRulePosting(@Context HttpServletRequest request, @PathParam("userGroupId") String groupId,
			@ApiParam(name = "ugObvFilterDataList") List<UserGroupObvFilterData> ugObvFilterDataList) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			ugFilterService.bulkFilteringIn(request, userGroupId, ugObvFilterDataList);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity("Not Allowed").build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE + ApiConstants.BULK + ApiConstants.REMOVING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Checks the post creation rule in Bulk to remove", notes = "remove the observation Based on rules in Bulk", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to set the filter Rule", response = String.class) })

	public Response bulkFilterRuleRemoving(@Context HttpServletRequest request,
			@PathParam("userGroupId") String groupId,
			@ApiParam(name = "ugObvFilterDataList") List<UserGroupObvFilterData> ugObvFilterDataList) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			ugFilterService.bulkFilteringOut(request, userGroupId, ugObvFilterDataList);
			return Response.status(Status.OK).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UPDATE + ApiConstants.FILTERRULE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	public Response updateUserGroupFilterRule(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId, @ApiParam(name = "userGroupWKT") UserGroupWKT userGroupWKT) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray userRole = (JSONArray) profile.getAttribute("roles");
			if (userRole.contains("ROLE_ADMIN")) {
				Long userGroup = Long.parseLong(userGroupId);
				String result = ugServices.updateUserGroupFilter(userGroup, userGroupWKT);
				return Response.status(Status.OK).entity(result).build();
			}
			return Response.status(Status.NOT_ACCEPTABLE).entity("USER NOT ALLOWED TO PERFORM THE TASK").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.SPECIESGROUP + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Get the species Group for a userGroup", notes = "Returns the species Group for a userGroup", response = UserGroupSpeciesGroup.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to retireve the data", response = String.class) })

	public Response getUserGroupSGroup(@PathParam("userGroupId") String userGroupId) {
		try {
			Long ugId = Long.parseLong(userGroupId);
			List<UserGroupSpeciesGroup> result = ugServices.getUserGroupSpeciesGroup(ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.MEMBERS)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Sends out invitaions for founder and moedrators", notes = "Returns the success and failur", response = String.class)
	@ApiResponses(value = {

			@ApiResponse(code = 400, message = "Unable to send the invitaions", response = String.class) })

	public Response addUserGroupMember(@Context HttpServletRequest request,
			@ApiParam(name = "userGroupInvitations") UserGroupInvitationData userGroupInvitations) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.addMemberRoleInvitaions(request, profile, userGroupInvitations);
			if (result)
				return Response.status(Status.OK).entity("Sent out Invitations to all").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User not allowed to send invitations").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.ADMINSTRATION + ApiConstants.MEMBERS + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "find the founder and moderator list", notes = "Return the founder and moderator list", response = AdministrationList.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to find the data", response = String.class) })

	public Response getAdminstrationMember(@PathParam("userGroupId") String groupId) {
		try {
			AdministrationList result = ugServices.getAdminMembers(groupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.VALIDATE + ApiConstants.MEMBERS)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Validate the inivation request", notes = "validates the invitaion request", response = UserGroupIbp.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to validate the invitation", response = String.class) })

	public Response validateUserGroupMemberInvite(@Context HttpServletRequest request,
			@ApiParam(name = "encryptionKey") EncryptionKey encryptionKey) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			UserGroupIbp result = ugServices.validateMember(request, userId, encryptionKey.getToken());
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("user Not allowed to join the group").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.VALIDATE + ApiConstants.REQUEST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "validate the join request for closed groups", notes = "In success returns the usergroup data", response = UserGroupIbp.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to validate the request", response = String.class) })

	public Response validateJoinRequest(@Context HttpServletRequest request,
			@ApiParam(name = "encryptionKey") EncryptionKey encryptionKey) {
		try {
			UserGroupIbp result = ugServices.validateJoinRequest(request, encryptionKey.getToken());
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.METHOD_NOT_ALLOWED).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.REMOVE + ApiConstants.MEMBERS)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser

	@ApiOperation(value = "remove a existing user from the group", notes = "remove existing user", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to remove the user", response = String.class) })

	public Response removeUserUG(@Context HttpServletRequest request, @QueryParam("userId") String userId,
			@QueryParam("userGroupId") String userGroupId) {
		try {
			Boolean result = ugServices.removeUser(request, userGroupId, userId);
			if (result)
				return Response.status(Status.OK).entity("Removed user").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User Not removed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@DELETE
	@Path(ApiConstants.LEAVE + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser

	@ApiOperation(value = "endpoint to leave a group", notes = "leave group", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to leave the group", response = String.class) })

	public Response leaveUserGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Boolean result = ugServices.leaveGroup(request, userId, userGroupId);
			if (result)
				return Response.status(Status.OK).entity("User left the group").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("NOt able to leave the group").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@GET
	@Path(ApiConstants.JOIN + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)

	@ValidateUser

	@ApiOperation(value = "endpoint to join open group", notes = "User can join open group without invitation", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to join the userGroup", response = String.class) })

	public Response joinUserGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Boolean result = ugServices.joinGroup(request, userId, userGroupId);
			if (result)
				return Response.status(Status.OK).entity("User joined the Group").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User not able to join the Group").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.SEND + ApiConstants.INVITES + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Send invites for Role Member in UserGroup", notes = "Sends Invitation mails for joining group as Member role", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to send invites", response = String.class) })

	public Response sendInvitesForMemberRole(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId, @ApiParam(name = "userList") List<Long> userList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = ugServices.sendInvitesForMemberRole(request, profile, ugId, userList);
			if (result != null)
				return Response.status(Status.OK).entity("Invitaion Sent out").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Invitation Sending caused Problem").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.BULK + ApiConstants.POSTING)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Bulk Posting of observation in a UserGroup", notes = "Returns the success failuer result", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to do Bulk Posting", response = String.class) })

	public Response bulkPostingObservationUG(@Context HttpServletRequest request,
			@ApiParam(name = "bulkGroupPosting") BulkGroupPostingData bulkGroupPostingData) {

		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.bulkPosting(request, profile, bulkGroupPostingData);
			if (result)
				return Response.status(Status.OK).entity("Bulk Posting completed").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Bulk posting failed").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.BULK + ApiConstants.REMOVING)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Bulk removing of observation in a UserGroup", notes = "Returns the success failuer result", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to do Bulk removing", response = String.class) })

	public Response bulkRemovingObservation(@Context HttpServletRequest request,
			@ApiParam(name = "bulkgroupUnPosting") BulkGroupUnPostingData bulkGroupUnPostingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.bulkRemoving(request, profile, bulkGroupUnPostingData);
			if (result)
				return Response.status(Status.OK).entity("Bulk Removing Completed").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Bulking Removing Failed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.DIRECT + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Adds the user directly to usergroup", notes = "Add all the user", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to add the user", response = String.class) })

	public Response addMembersDirectly(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId,
			@ApiParam(name = "memberList") UserGroupAddMemebr memberList) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				Long ugId = Long.parseLong(userGroupId);
				Boolean result = ugServices.addMemberDirectly(request, ugId, memberList);
				if (result)
					return Response.status(Status.OK).entity("Added all user").build();
				return Response.status(Status.NOT_ACCEPTABLE).entity("Not Able to add the user").build();
			}
			return Response.status(Status.FORBIDDEN).entity("User not allowed to do the operation").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Create the userGroup", notes = "Returns the userGroupIBP data", response = UserGroupIbp.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to create the group", response = String.class) })

	public Response createUserGroup(@Context HttpServletRequest request,
			@ApiParam(name = "userGroupCreateData") UserGroupCreateData ugCreateDate) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				UserGroupIbp result = ugServices.createUserGroup(request, profile, ugCreateDate);
				if (result != null)
					return Response.status(Status.OK).entity(result).build();
				return Response.status(Status.NOT_ACCEPTABLE).entity("Wrong set of data").build();
			}
			return Response.status(Status.FORBIDDEN).entity("User not allowed to create User group").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.EDIT + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "find the userGroup edit data", notes = "Returns the edit data of userGroup", response = UserGroupEditData.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to read the data", response = String.class) })

	public Response getEditData(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			UserGroupEditData result = ugServices.getUGEditData(request, profile, ugId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.FORBIDDEN).entity("User Not allowed to Edit the page").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.EDIT + ApiConstants.SAVE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Save the editied data of UserGroup", notes = "Saves the edit of UserGroup", response = UserGroupIbp.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to edit the userGroup", response = String.class) })

	public Response saveEdit(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId,
			@ApiParam("ugEditData") UserGroupEditData ugEditData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			UserGroupIbp result = ugServices.saveUGEdit(request, profile, ugId, ugEditData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.FORBIDDEN).entity("User not allowed to edit").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.HOMEPAGE + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "fetches the stats of usergroup", notes = "Returns the userGroupStats", response = UserGroupHomePage.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable the fetch the stats", response = String.class) })

	public Response getUserGroupHomePageData(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			UserGroupHomePage result = ugServices.getUserGroupHomePageData(userGroupId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.FILTERRULE + ApiConstants.SHOW + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "Show all the filter rules attached to a group", notes = "Returns all the filter rule", response = ShowFilterRule.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the rule", response = String.class) })

	public Response showAllFilterRules(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			ShowFilterRule result = ugFilterService.showAllFilter(userGroupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE + ApiConstants.REMOVE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Remove a filter rules attached to a group", notes = "Returns all the filter rule", response = ShowFilterRule.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the rule", response = String.class) })

	public Response deleteFilterRule(@Context HttpServletRequest request, @PathParam("userGroupId") String groupId,
			@ApiParam(name = "ugFilterRemove") UserGroupFilterRemove ugFilterRemove) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			ShowFilterRule result = ugFilterService.deleteUGFilter(request, userGroupId, ugFilterRemove);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE + ApiConstants.ENABLE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Enable disable the filter rules attached to a group", notes = "Returns all the filter rule", response = ShowFilterRule.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the rule", response = String.class) })

	public Response enableDisableFilter(@Context HttpServletRequest request, @PathParam("userGroupId") String groupId,
			@ApiParam(name = "ugFilterEnable") UserGroupFilterEnable ugFilterEnable) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			ShowFilterRule result = ugFilterService.enableDisableUGFilter(request, userGroupId, ugFilterEnable);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.FILTERRULE + ApiConstants.ADD + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Create filter rules for a group", notes = "Returns all the filter rule", response = ShowFilterRule.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to fetch the rule", response = String.class) })

	public Response addFilterRule(@Context HttpServletRequest request, @PathParam("userGroupId") String groupId,
			@ApiParam(name = "ugFilterInputData") UserGroupFilterRuleInputData ugFilterInputData) {
		try {

			Long userGroupId = Long.parseLong(groupId);
			ShowFilterRule result = ugFilterService.changeUgFilter(request, userGroupId, ugFilterInputData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.REGISTER)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(@Context HttpServletRequest request, AuthenticationDTO authDTO) {
		try {
			Map<String, Object> data = ugServices.registerUserProxy(request, authDTO);
			ResponseBuilder response = Response.ok().entity(data);
			if (Boolean.parseBoolean(data.get("status").toString())
					&& !Boolean.parseBoolean(data.get("verificationRequired").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOGIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginProxy(@Context HttpServletRequest request, @FormParam("username") String userEmail,
			@FormParam("password") String password, @FormParam("mode") String mode) {
		try {
			Map<String, Object> data = ugServices.signupProxy(request, userEmail, password, mode);
			ResponseBuilder response = Response.ok().entity(data);
			if (Boolean.parseBoolean(data.get("status").toString())
					&& !Boolean.parseBoolean(data.get("verificationRequired").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.DOCUMENT + "/{documentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ApiOperation(value = "finds all the usergroup for a document", notes = "returns the usergroup in which the document is posted", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to find the groups", response = String.class) })

	public Response getUserGroupByDocId(@PathParam("documentId") String documentId) {
		try {
			Long docId = Long.parseLong(documentId);
			List<UserGroupIbp> result = ugServices.fetchByDocumentId(docId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.VERIFY_USER)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyUserOTPProxy(@Context HttpServletRequest request, @FormParam("id") Long id,
			@FormParam("otp") String otp) {
		try {
			Map<String, Object> data = ugServices.verifyOTPProxy(request, id, otp);
			ResponseBuilder response = Response.ok();
			if (Boolean.parseBoolean(data.get("status").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.entity(data).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.DOCUMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "create usegroup to doc mapping", notes = "returns all the group in which document is posted", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to create the mapping", response = String.class) })

	public Response createUGDocMapping(@Context HttpServletRequest request,
			@ApiParam(name = "groupDocCreateData") UserGroupDocCreateData groupDocCreateData) {
		try {
			List<UserGroupIbp> result = ugServices.createUGDocMapping(request, groupDocCreateData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + ApiConstants.OBSERVATION)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "get usergroup observation permission", notes = "returns the usergroup for each user", response = UserGroupPermissions.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to get the usergroup", response = String.class) })

	public Response getUserGroupObservationPermission(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			UserGroupPermissions result = ugMemberService.getUserGroupObservationPermissions(userId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
