package gpms.rest;

import gpms.model.UserAccount;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.platform.win32.Netapi32Util.User;

@Path("/v1/users")
@Api(value = "/users", description = "Manage Users")
public class UsersResource {

	private static final Logger log = Logger.getLogger(UsersResource.class
			.getName());

	@GET
	@Path("/xx")
	@Produces(MediaType.TEXT_PLAIN)
	public String xx() {
		return "welcome to xx";
	}

	@GET
	@Path("/{userId}")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find User", notes = "This API retrieves the public information for the user (Private info is returned if this is the auth user)"
			+ "<p><u>Input Parameters</u><ul><li><b>userId</b> is required</li></ul>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { user profile }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getUserById(
			@ApiParam(value = "userId", required = true, defaultValue = "23456", allowableValues = "", allowMultiple = false) @PathParam("userId") String userId) {

		log.info("UsersResource::getUserById started userId=" + userId);

		if (userId == null) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\": \"Empty userId\", \"status\": \"FAIL\"}")
					.build();
		}

		try {
			// User user = BusinessManager.getInstance().findUser(userId);
			UserAccount user = new UserAccount();

			return Response.status(Response.Status.OK).entity(user).build();
		} catch (Exception e) {

		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find User\", \"status\": \"FAIL\"}")
				.build();
	}

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find All Users", notes = "This API retrieves  all users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { user profile }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getUsers() {

		log.info("UsersResource::getUsers started");

		try {
			List<UserAccount> users = new ArrayList<UserAccount>(); // BusinessManager.getInstance().findUsers();

			return Response.status(Response.Status.OK).entity(users).build();
		} catch (Exception e) {

		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find User\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create a new User", notes = "This API creates a new user if the username does not exist"
			+ "<p><u>Input Parameters</u><ul><li><b>new user object</b> us required</li></ul></p>")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Success: { user profile }"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\", \"status\" : \"FAIL\" }") })
	public Response createUser(
			@ApiParam(value = "New User", required = true, defaultValue = "\"{\"name\"=\"Milstein Munakami\"}\"", allowableValues = "", allowMultiple = false) User user) {

		try {
			UserAccount newUser = new UserAccount();

			return Response.status(Response.Status.CREATED).entity(newUser)
					.build();
		} catch (Exception e) {

		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Create User\", \"status\": \"FAIL\"}")
				.build();

	}

	@PUT
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update User", notes = "This API updates the user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Success: { user profile }"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\", \"status\" : \"FAIL\" }") })
	public Response updateUser(
			@ApiParam(value = "userId", required = true, defaultValue = "23456", allowableValues = "", allowMultiple = false) @PathParam("userId") String userId,
			String jsonString) {

		String name = new String();

		try {

			// Object obj = JSONValue.parse(jsonString);
			//
			// JSONObject jsonObject = (JSONObject) obj;
			// name = (String) jsonObject.get("name");

			ObjectMapper mapper = new ObjectMapper();

			JsonNode root = mapper.readTree(jsonString);

			if (root != null && root.has("name")) {
				name = root.get("name").textValue();
			}
		} catch (Exception e) {
			return Response
					.status(Response.Status.BAD_REQUEST)
					.entity("{\"error\": \"Invalid or Missing fields error\", \"status\": \"FAIL\"}")
					.build();
		}

		try {
			UserAccount updateduser = new UserAccount();
			return Response.status(Response.Status.CREATED).entity(updateduser)
					.build();

		} catch (Exception e) {

		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Update User\", \"status\": \"FAIL\"}")
				.build();

	}

	@DELETE
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Delete User", notes = "This API deletes the user")
	@ApiResponses(value = {
			@ApiResponse(code = 201, message = "Success: { }"),
			@ApiResponse(code = 400, message = "Failed: {\"error\":\"error description\", \"status\" : \"FAIL\" }") })
	public Response deleteUser(
			@ApiParam(value = "userId", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) @PathParam("userId") String userId) {

		try {
			// BusinessManager.getInstance().deleteUser(userId);

			return Response.status(Response.Status.OK).entity("{}").build();

		} catch (Exception e) {

		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delete User\", \"status\": \"FAIL\"}")
				.build();

	}

}
