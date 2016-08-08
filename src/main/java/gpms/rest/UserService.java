package gpms.rest;

import gpms.DAL.DepartmentsPositionsCollection;
import gpms.DAL.MongoDBConnector;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.Address;
import gpms.model.AuditLogInfo;
import gpms.model.NotificationLog;
import gpms.model.PositionDetails;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
import gpms.model.UserProfile;
import gpms.model.UserProposalCount;
import gpms.utils.EmailUtil;
import gpms.utils.MultimapAdapter;
import gpms.utils.PasswordHash;
import gpms.utils.SerializationHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.mongodb.morphia.Morphia;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

@Path("/users")
@Api(value = "/users", description = "Manage Users")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN, MediaType.TEXT_HTML })
public class UserService {
	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;
	NotificationDAO notificationDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static final Logger log = Logger.getLogger(UserService.class
			.getName());

	public UserService() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
		notificationDAO = new NotificationDAO(mongoClient, morphia, dbName);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Test User Service", notes = "This API tests whether the service is working or not")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Hello World! }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response testService() {
		try {
			// log.info("UsersResource::getUserById started userId=" + userId);
			log.info("UserService::testService started");

			return Response.status(Response.Status.OK).entity("Hello World!")
					.build();
		} catch (Exception e) {
			log.error("Could not connect the User Service error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find User Service\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetUserPositionDetailsForAProposal")
	@ApiOperation(value = "Get all User Position Details for a Proposal", notes = "This API gets all Investigator User Position Details of a Proposal")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Investigator Users And Positions }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getUserPositionDetailsForAProposal(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::getUserPositionDetailsForAProposal started");

			String profileIds = new String();
			String profiles[] = new String[0];
			List<ObjectId> userIds = new ArrayList<ObjectId>();

			ObjectMapper mapper = new ObjectMapper();

			JsonNode root = mapper.readTree(message);
			if (root != null && root.has("userIds")) {
				profileIds = root.get("userIds").textValue();
				profiles = profileIds.split(", ");
			}

			for (String profile : profiles) {
				ObjectId id = new ObjectId(profile);
				userIds.add(id);
			}
			final MultimapAdapter multimapAdapter = new MultimapAdapter();
			final Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(Multimap.class, multimapAdapter)
					.create();

			return Response
					.status(Response.Status.OK)
					.entity(gson.toJson(userProfileDAO
							.findUserPositionDetailsForAProposal(userIds)))
					.build();
		} catch (Exception e) {
			log.error(
					"Could not find Investigator User Details for the proposal error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Investigator User Details for the proposal\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetUsersList")
	@ApiOperation(value = "Get all Users", notes = "This API gets all active Users")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUsersJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::produceUsersJSON started");
			List<UserInfo> users = new ArrayList<UserInfo>();

			int offset = 0, limit = 0;
			String userName = new String();
			String college = new String();
			String department = new String();
			String positionType = new String();
			String positionTitle = new String();
			Boolean isActive = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("offset")) {
				offset = root.get("offset").intValue();
			}

			if (root != null && root.has("limit")) {
				limit = root.get("limit").intValue();
			}

			if (root != null && root.has("userBindObj")) {
				JsonNode userObj = root.get("userBindObj");
				if (userObj != null && userObj.has("UserName")) {
					userName = userObj.get("UserName").textValue();
				}

				if (userObj != null && userObj.has("College")) {
					college = userObj.get("College").textValue();
				}

				if (userObj != null && userObj.has("Department")) {
					department = userObj.get("Department").textValue();
				}

				if (userObj != null && userObj.has("PositionType")) {
					positionType = userObj.get("PositionType").textValue();
				}

				if (userObj != null && userObj.has("PositionTitle")) {
					positionTitle = userObj.get("PositionTitle").textValue();
				}

				if (userObj != null && userObj.has("IsActive")) {
					if (!userObj.get("IsActive").isNull()) {
						isActive = userObj.get("IsActive").booleanValue();
					} else {
						isActive = null;
					}
				}
			}

			users = userProfileDAO.findAllForUserGrid(offset, limit, userName,
					college, department, positionType, positionTitle, isActive);

			// final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// return gson.toJson(users);

			// return
			// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
			// users);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(users)).build();
		} catch (Exception e) {
			log.error("Could not find all Users error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Users\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetAdminUsersList")
	@ApiOperation(value = "Get Admin Users", notes = "This API gets all admin Users")
	public Response produceAdminUsersJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::produceAdminUsersJSON started");
			List<UserInfo> users = new ArrayList<UserInfo>();

			int offset = 0, limit = 0;
			String userName = new String();
			String positionTitle = new String();
			Boolean isActive = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("offset")) {
				offset = root.get("offset").intValue();
			}

			if (root != null && root.has("limit")) {
				limit = root.get("limit").intValue();
			}

			if (root != null && root.has("userBindObj")) {
				JsonNode userObj = root.get("userBindObj");
				if (userObj != null && userObj.has("UserName")) {
					userName = userObj.get("UserName").textValue();
				}

				if (userObj != null && userObj.has("PositionTitle")) {
					positionTitle = userObj.get("PositionTitle").textValue();
				}

				if (userObj != null && userObj.has("IsActive")) {
					if (!userObj.get("IsActive").isNull()) {
						isActive = userObj.get("IsActive").booleanValue();
					} else {
						isActive = null;
					}
				}
			}

			users = userProfileDAO.findAllForAdminUserGrid(offset, limit,
					userName, positionTitle, isActive);

			// final Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// return gson.toJson(users);

			// return
			// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
			// users);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(users)).build();
		} catch (Exception e) {
			log.error("Could not find all Admin Users error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Admin Users\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/UsersExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(value = "Export all Users in a grid", notes = "This API exports all Users shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportUsersJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::exportUsersJSON started");

			List<UserInfo> users = new ArrayList<UserInfo>();
			String userName = new String();
			String college = new String();
			String department = new String();
			String positionType = new String();
			String positionTitle = new String();
			Boolean isActive = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userBindObj")) {
				JsonNode userObj = root.get("userBindObj");
				if (userObj != null && userObj.has("UserName")) {
					userName = userObj.get("UserName").textValue();
				}

				if (userObj != null && userObj.has("College")) {
					college = userObj.get("College").textValue();
				}

				if (userObj != null && userObj.has("Department")) {
					department = userObj.get("Department").textValue();
				}

				if (userObj != null && userObj.has("PositionType")) {
					positionType = userObj.get("PositionType").textValue();
				}

				if (userObj != null && userObj.has("PositionTitle")) {
					positionTitle = userObj.get("PositionTitle").textValue();
				}

				if (userObj != null && userObj.has("IsActive")) {
					if (!userObj.get("IsActive").isNull()) {
						isActive = userObj.get("IsActive").booleanValue();
					} else {
						isActive = null;
					}
				}
			}
			users = userProfileDAO.findAllUsers(userName, college, department,
					positionType, positionTitle, isActive);

			String filename = new String();
			if (users.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("Users");
				SheetWriter<UserInfo> writer = sheet
						.getBeanWriter(UserInfo.class);

				writer.write(users);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
				Date date = new Date();

				String fileName = String.format("%s.%s",
						RandomStringUtils.randomAlphanumeric(8) + "_"
								+ dateFormat.format(date), "xlsx");

				// File file = new
				// File(request.getServletContext().getAttribute(
				// "FILES_DIR")
				// + File.separator + filename);
				// System.out.println("Absolute Path at server=" +
				// file.getAbsolutePath());
				String downloadLocation = this.getClass()
						.getResource("/uploads").toURI().getPath();

				xcelite.write(new File(downloadLocation + fileName));

				// xcelite.write(new
				// File(request.getServletContext().getAttribute(
				// "FILES_DIR")
				// + File.separator + fileName));

				filename = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(fileName);
			} else {
				filename = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("No Record");
			}

			return Response.status(Response.Status.OK).entity(filename).build();
		} catch (Exception e) {
			log.error("Could not export User list error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Export User List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/AdminUsersExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(value = "Export all Admin Users in a grid", notes = "This API exports all Admin Users shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportAdminUsersJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::exportAdminUsersJSON started");

			List<UserInfo> users = new ArrayList<UserInfo>();
			String userName = new String();
			String college = new String();
			String department = new String();
			String positionType = new String();
			String positionTitle = new String();
			Boolean isActive = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userBindObj")) {
				JsonNode userObj = root.get("userBindObj");
				if (userObj != null && userObj.has("UserName")) {
					userName = userObj.get("UserName").textValue();
				}

				if (userObj != null && userObj.has("College")) {
					college = userObj.get("College").textValue();
				}

				if (userObj != null && userObj.has("Department")) {
					department = userObj.get("Department").textValue();
				}

				if (userObj != null && userObj.has("PositionType")) {
					positionType = userObj.get("PositionType").textValue();
				}

				if (userObj != null && userObj.has("PositionTitle")) {
					positionTitle = userObj.get("PositionTitle").textValue();
				}

				if (userObj != null && userObj.has("IsActive")) {
					if (!userObj.get("IsActive").isNull()) {
						isActive = userObj.get("IsActive").booleanValue();
					} else {
						isActive = null;
					}
				}
			}
			users = userProfileDAO.findAllAdminUsers(userName, college,
					department, positionType, positionTitle, isActive);

			String filename = new String();
			if (users.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("Users");
				SheetWriter<UserInfo> writer = sheet
						.getBeanWriter(UserInfo.class);

				writer.write(users);

				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
				Date date = new Date();

				String fileName = String.format("%s.%s",
						RandomStringUtils.randomAlphanumeric(8) + "_"
								+ dateFormat.format(date), "xlsx");

				// File file = new
				// File(request.getServletContext().getAttribute(
				// "FILES_DIR")
				// + File.separator + filename);
				// System.out.println("Absolute Path at server=" +
				// file.getAbsolutePath());
				String downloadLocation = this.getClass()
						.getResource("/uploads").toURI().getPath();

				xcelite.write(new File(downloadLocation + fileName));

				// xcelite.write(new
				// File(request.getServletContext().getAttribute(
				// "FILES_DIR")
				// + File.separator + fileName));

				filename = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString(fileName);
			} else {
				filename = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("No Record");
			}

			return Response.status(Response.Status.OK).entity(filename).build();
		} catch (Exception e) {
			log.error("Could not export Admin User list error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Export Admin User List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetUserDetailsByProfileId")
	@ApiOperation(value = "Get User Details By ProfileId", notes = "This API gets User Detail Information by ProfileId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Profile }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUserDetailsByProfileId(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::produceUserDetailsByProfileId started");
			UserProfile user = new UserProfile();
			String profileId = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userId")) {
				profileId = root.get("userId").textValue();
			}

			// // build a JSON object using org.JSON
			// JSONObject obj = new JSONObject(message);
			//
			// // get the first result
			// String profileId = obj.getString("userId");

			// Alternatively
			// // Embedded Object
			// JSONObject commonObj = obj.getJSONObject("gpmsCommonObj");
			// String userName = commonObj.getString("UserName");
			// String userProfileID = commonObj.getString("UserProfileID");
			// String cultureName = commonObj.getString("CultureName");

			if (profileId != null) {
				ObjectId id = new ObjectId(profileId);

				// System.out.println("Profile ID String: " + profileId
				// + ", Profile ID with ObjectId: " + id + ", User Name: "
				// + userName + ", User Profile ID: " + userProfileID
				// + ", Culture Name: " + cultureName);

				user = userProfileDAO.findUserDetailsByProfileID(id);

				// Gson gson = new Gson();
				// .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
				// Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				// .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
				// .create();
				// return gson.toJson(user, UserProfile.class);

				// response = gson.toJson(user);

				return Response
						.status(Response.Status.OK)
						.entity(mapper.setDateFormat(formatter)
								.writerWithDefaultPrettyPrinter()
								.writeValueAsString(user)).build();
			}
		} catch (Exception e) {
			log.error(
					"Could not get User Detail Information by ProfileId error e=",
					e);
		}
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get User Details By ProfileId\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetUserInfoByProfileId")
	@ApiOperation(value = "Get User Information By ProfileId", notes = "This API gets User Information by ProfileId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Profile }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUserInfoByProfileId(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::produceUserInfoByProfileId started");
			UserProfile user = new UserProfile();
			String profileId = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userId")) {
				profileId = root.get("userId").textValue();
			}

			ObjectId id = new ObjectId(profileId);

			user = userProfileDAO.findUserInfoByProfileID(id);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(
									user.getUserAccount().getPassword()))
					.build();

		} catch (Exception e) {
			log.error("Could not get User Information By ProfileId error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get User Information By ProfileId\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetUserAuditLogList")
	@ApiOperation(value = "Get User Audit Log Information", notes = "This API gets Audit Log information for a User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Audit Log Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUserAuditLogJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::produceUserAuditLogJSON started");
			List<AuditLogInfo> userAuditLogs = new ArrayList<AuditLogInfo>();

			int offset = 0, limit = 0;
			String profileId = new String();
			String action = new String();
			String auditedBy = new String();
			String activityOnFrom = new String();
			String activityOnTo = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("offset")) {
				offset = root.get("offset").intValue();
			}

			if (root != null && root.has("limit")) {
				limit = root.get("limit").intValue();
			}

			if (root != null && root.has("userId")) {
				profileId = root.get("userId").textValue();
			}

			if (root != null && root.has("auditLogBindObj")) {
				JsonNode auditLogBindObj = root.get("auditLogBindObj");
				if (auditLogBindObj != null && auditLogBindObj.has("Action")) {
					action = auditLogBindObj.get("Action").textValue();
				}

				if (auditLogBindObj != null && auditLogBindObj.has("AuditedBy")) {
					auditedBy = auditLogBindObj.get("AuditedBy").textValue();
				}

				if (auditLogBindObj != null
						&& auditLogBindObj.has("ActivityOnFrom")) {
					activityOnFrom = auditLogBindObj.get("ActivityOnFrom")
							.textValue();
				}

				if (auditLogBindObj != null
						&& auditLogBindObj.has("ActivityOnTo")) {
					activityOnTo = auditLogBindObj.get("ActivityOnTo")
							.textValue();
				}
			}

			ObjectId userId = new ObjectId(profileId);

			userAuditLogs = userProfileDAO.findAllForUserAuditLogGrid(offset,
					limit, userId, action, auditedBy, activityOnFrom,
					activityOnTo);

			// users = (ArrayList<UserInfo>)
			// userProfileDAO.findAllForUserGrid();
			// response = JSONTansformer.ConvertToJSON(users);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(userAuditLogs)).build();

		} catch (Exception e) {
			log.error("Could not get User Audit Log information error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get User Audit Log Information\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetPositionDetailsHash")
	@ApiOperation(value = "Get All Available User Position Details as Database", notes = "This API gets all Available User Position Details as Database stored in DepartmentsPositionsCollection class")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Audit Log Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response producePositionDetailsHash() {
		try {
			log.info("UserService::producePositionDetailsHash started");
			ObjectMapper mapper = new ObjectMapper();
			DepartmentsPositionsCollection dpc = new DepartmentsPositionsCollection();
			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(
									dpc.getAvailableDepartmentsAndPositions()))
					.build();

		} catch (Exception e) {
			log.error("Could not load all User Position Details error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Load All User Position Details\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/DeleteUserByUserID")
	@ApiOperation(value = "Delete User by UserID", notes = "This API deletes User by UserID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response deleteUserByUserID(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::deleteUserByUserID started");

			String profileId = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userId")) {
				profileId = root.get("userId").textValue();
			}

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);

			ObjectId id = new ObjectId(profileId);
			UserProfile userProfile = userProfileDAO
					.findUserDetailsByProfileID(id);

			userProfileDAO
					.deleteUserProfileByUserID(userProfile, authorProfile);
			// userProfile.setDeleted(true);
			// userProfileDAO.save(userProfile);

			UserAccount userAccount = userAccountDAO.findByID(userProfile
					.getUserAccount().getId());

			// userAccountDAO.deleteUserAccountByUserID(userAccount,
			// authorProfile);
			userAccount.setDeleted(true);
			userAccount.setActive(false);
			userAccountDAO.save(userAccount);

			String messageBody = new String();
			EmailUtil emailUtil = new EmailUtil();
			if (userProfile.isDeleted()) {
				messageBody = "Hello "
						+ userProfile.getFullName()
						+ ",<br/> Your account has been deleted to reactivate you can contact administrator: <a href='http://seal.boisestate.edu:8080/GPMS/ContactUs.jsp' title='GPMS Contact Us' target='_blank'>Contact Us</a><br/><br/>Thank you, <br/> GPMS Team";
				emailUtil.sendMailWithoutAuth(userProfile.getWorkEmails()
						.get(0),
						"You have been deleted " + userProfile.getFullName(),
						messageBody);
			}

			NotificationLog notification = new NotificationLog();
			notification.setType("User");
			notification.setAction("Account is deleted.");
			notification.setUserProfileId(userProfile.getId().toString());
			notification.setUsername(userAccount.getUserName());
			notification.setForAdmin(true);
			notification.setCritical(true);
			// notification.isViewedByUser(true);
			notificationDAO.save(notification);

			// Broadcasting SSE

			OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			OutboundEvent event = eventBuilder.name("notification")
					.mediaType(MediaType.TEXT_PLAIN_TYPE)
					.data(String.class, "1").build();

			NotificationService.BROADCASTER.broadcast(event);

			// response.setContentType("text/html;charset=UTF-8");
			// response.getWriter().write("Success Data");

			// Gson gson = new GsonBuilder().setPrettyPrinting().create();
			// response = gson.toJson("Success", String.class);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error("Could not delete User by UserID error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delete User by UserID\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/DeleteMultipleUsersByUserID")
	@ApiOperation(value = "Delete Multiple Users at once", notes = "This API deletes Multiple Users at once")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response deleteMultipleUsersByUserID(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::deleteMultipleUsersByUserID started");

			String profileIds = new String();
			String profiles[] = new String[0];

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userIds")) {
				profileIds = root.get("userIds").textValue();
				profiles = profileIds.split(",");
			}

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);

			for (String profile : profiles) {
				ObjectId id = new ObjectId(profile);
				UserProfile userProfile = userProfileDAO
						.findUserDetailsByProfileID(id);

				userProfileDAO.deleteUserProfileByUserID(userProfile,
						authorProfile);
				// userProfile.setDeleted(true);
				// userProfileDAO.save(userProfile);

				UserAccount userAccount = userAccountDAO.findByID(userProfile
						.getUserAccount().getId());

				// userAccountDAO.deleteUserAccountByUserID(userAccount,
				// authorProfile);
				userAccount.setDeleted(true);
				userAccount.setActive(false);
				userAccountDAO.save(userAccount);

				NotificationLog notification = new NotificationLog();
				notification.setType("User");
				notification.setAction("Account is deleted.");
				notification.setUserProfileId(userProfile.getId().toString());
				notification.setUsername(userAccount.getUserName());
				notification.setForAdmin(true);
				notification.setCritical(true);
				notificationDAO.save(notification);
			}

			// Broadcasting SSE

			OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			OutboundEvent event = eventBuilder.name("notification")
					.mediaType(MediaType.TEXT_PLAIN_TYPE)
					.data(String.class, "1").build();

			NotificationService.BROADCASTER.broadcast(event);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error("Could not delete all selected Users error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delete All Selected Users\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/UpdateUserIsActiveByUserID")
	@ApiOperation(value = "Update User's IsActive By UserID", notes = "This API updates User's IsActive field By UserID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response updateUserIsActiveByUserID(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::updateUserIsActiveByUserID started");

			String profileId = new String();
			Boolean isActive = true;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userId")) {
				profileId = root.get("userId").textValue();
			}

			if (root != null && root.has("isActive")) {
				isActive = root.get("isActive").booleanValue();
			}

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);

			ObjectId id = new ObjectId(profileId);
			UserProfile userProfile = userProfileDAO
					.findUserDetailsByProfileID(id);

			userProfileDAO.activateUserProfileByUserID(userProfile,
					authorProfile, isActive);
			// userProfile.setDeleted(!isActive);
			// userProfileDAO.save(userProfile);

			UserAccount userAccount = userProfile.getUserAccount();
			// userAccountDAO.activateUserAccountByUserID(userAccount,
			// authorProfile,
			// isActive);
			userAccount.setDeleted(!isActive);
			userAccount.setActive(isActive);
			userAccountDAO.save(userAccount);

			String messageBody = new String();
			EmailUtil emailUtil = new EmailUtil();

			NotificationLog notification = new NotificationLog();

			String notificationMessage = new String();
			boolean isCritical = false;
			if (isActive) {
				notificationMessage = "Account is activated.";

				// Send Email
				messageBody = "Hello "
						+ userProfile.getFullName()
						+ ",<br/><br/> Your account has been activated and you can login now using your credential: <a href='http://seal.boisestate.edu:8080/GPMS/Login.jsp' title='GPMS Login' target='_blank'>Login Here</a><br/><br/>Thank you, <br/> GPMS Team";
				emailUtil.sendMailWithoutAuth(userProfile.getWorkEmails()
						.get(0), "Successfully Activated your account "
						+ userProfile.getFullName(), messageBody);
			} else {
				notificationMessage = "Account is deactivated.";
				isCritical = true;

				messageBody = "Hello "
						+ userProfile.getFullName()
						+ ",<br/> Your account has been deactivated to reactivate you can contact administrator: <a href='http://seal.boisestate.edu:8080/GPMS/ContactUs.jsp' title='GPMS Contact Us' target='_blank'>Contact Us</a><br/><br/>Thank you, <br/> GPMS Team";
				emailUtil.sendMailWithoutAuth(
						userProfile.getWorkEmails().get(0),
						"You have been Deactivated "
								+ userProfile.getFullName(), messageBody);
			}

			// To Admin
			notification.setType("User");
			notification.setAction(notificationMessage);
			notification.setUserProfileId(userProfile.getId().toString());
			notification.setUsername(userAccount.getUserName());
			notification.setForAdmin(true);
			notification.setCritical(isCritical);
			notificationDAO.save(notification);

			// To All User Roles based on positions
			for (PositionDetails positions : userProfile.getDetails()) {
				notification = new NotificationLog();
				notification.setType("User");
				notification.setAction(notificationMessage);
				notification.setUserProfileId(userProfile.getId().toString());
				notification.setUsername(userAccount.getUserName());
				notification.setCollege(positions.getCollege());
				notification.setDepartment(positions.getDepartment());
				notification.setPositionType(positions.getPositionType());
				notification.setPositionTitle(positions.getPositionTitle());
				notification.setCritical(isCritical);
				notificationDAO.save(notification);
			}

			// Broadcasting SSE

			OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			OutboundEvent event = eventBuilder.name("notification")
					.mediaType(MediaType.TEXT_PLAIN_TYPE)
					.data(String.class, "1").build();

			NotificationService.BROADCASTER.broadcast(event);

			// return Response.ok("Success",
			// MediaType.APPLICATION_JSON).build();

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error("Could not update User's IsActive field error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Update User's IsActive Field\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/CheckUniqueUserName")
	@ApiOperation(value = "Check for Unique Username", notes = "This API checks if provided Username is Unique or not")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True/ False }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response checkUniqueUserName(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::checkUniqueUserName started");

			String userID = new String();
			String newUserName = new String();
			String response = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userUniqueObj")) {
				JsonNode userUniqueObj = root.get("userUniqueObj");
				if (userUniqueObj != null && userUniqueObj.has("UserID")) {
					userID = userUniqueObj.get("UserID").textValue();
				}

				if (userUniqueObj != null && userUniqueObj.has("NewUserName")) {
					newUserName = userUniqueObj.get("NewUserName").textValue();
				}
			}

			@SuppressWarnings("unused")
			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId id = new ObjectId();
			UserProfile userProfile = new UserProfile();
			if (!userID.equals("0")) {
				id = new ObjectId(userID);
				userProfile = userProfileDAO.findNextUserWithSameUserName(id,
						newUserName);
			} else {
				userProfile = userProfileDAO
						.findAnyUserWithSameUserName(newUserName);
			}

			if (userProfile != null) {
				response = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("false");
			} else {
				response = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("true");
			}
			return Response.status(Response.Status.OK).entity(response).build();

		} catch (Exception e) {
			log.error("Could not check for unique Username error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Check For Unique Username\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/CheckUniqueEmail")
	@ApiOperation(value = "Check for Unique Email Address", notes = "This API checks for unique Email Address")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True/ False }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response checkUniqueEmail(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::checkUniqueEmail started");

			String userID = new String();
			String newEmail = new String();
			String response = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userUniqueObj")) {
				JsonNode userUniqueObj = root.get("userUniqueObj");
				if (userUniqueObj != null && userUniqueObj.has("UserID")) {
					userID = userUniqueObj.get("UserID").textValue();
				}

				if (userUniqueObj != null && userUniqueObj.has("NewEmail")) {
					newEmail = userUniqueObj.get("NewEmail").textValue();
				}
			}

			@SuppressWarnings("unused")
			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId id = new ObjectId();
			UserProfile userProfile = new UserProfile();
			if (!userID.equals("0")) {
				id = new ObjectId(userID);
				userProfile = userProfileDAO.findNextUserWithSameEmail(id,
						newEmail);
			} else {
				userProfile = userProfileDAO.findAnyUserWithSameEmail(newEmail);
			}

			if (userProfile != null) {
				response = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("false");
			} else {
				response = mapper.writerWithDefaultPrettyPrinter()
						.writeValueAsString("true");
			}
			return Response.status(Response.Status.OK).entity(response).build();

		} catch (Exception e) {
			log.error("Could not check Unique Email Address error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Check for Unique Email Address\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/SaveUpdateUser")
	@ApiOperation(value = "Save a New User or Update an existing User", notes = "This API saves a New User or updates an existing User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response saveUpdateUser(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::saveUpdateUser started");

			String userID = new String();
			UserAccount newAccount = new UserAccount();
			UserProfile newProfile = new UserProfile();

			UserAccount existingUserAccount = new UserAccount();
			UserProfile existingUserProfile = new UserProfile();
			UserProfile oldUserProfile = new UserProfile();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			boolean isActiveUser = false;

			if (root != null && root.has("userInfo")) {
				JsonNode userInfo = root.get("userInfo");

				if (userInfo != null && userInfo.has("UserID")) {
					userID = userInfo.get("UserID").textValue();
					if (!userID.equals("0")) {
						ObjectId id = new ObjectId(userID);
						existingUserProfile = userProfileDAO
								.findUserDetailsByProfileID(id);
						oldUserProfile = SerializationHelper
								.cloneThroughSerialize(existingUserProfile);
					} else {
						newAccount.setAddedOn(new Date());
					}
				}

				if (userInfo != null && userInfo.has("UserName")) {
					String userNameOf = userInfo.get("UserName").textValue();
					if (!userID.equals("0") && existingUserProfile != null) {
						existingUserAccount = existingUserProfile
								.getUserAccount();
						if (!existingUserAccount.getUserName().equals(
								userNameOf)) {
							existingUserAccount = null;
						}
					} else {
						newAccount.setUserName(userNameOf);
					}
				}

				if (userInfo != null && userInfo.has("Password")) {
					if (!userID.equals("0")) {
						if (!existingUserAccount.getPassword().equals(
								userInfo.get("Password").textValue())) {
							existingUserAccount.setPassword(userInfo.get(
									"Password").textValue());
						}
					} else {
						newAccount.setPassword(userInfo.get("Password")
								.textValue());
					}
				}

				if (userInfo != null && userInfo.has("IsActive")) {
					if (!userID.equals("0")) {
						if (existingUserAccount.isActive() != userInfo.get(
								"IsActive").booleanValue()) {
							existingUserAccount.setActive(userInfo.get(
									"IsActive").booleanValue());

							isActiveUser = userInfo.get("IsActive")
									.booleanValue();
						}
					} else {
						newAccount.setActive(userInfo.get("IsActive")
								.booleanValue());
					}
					if (!userID.equals("0")) {
						if (existingUserAccount.isDeleted() != !userInfo.get(
								"IsActive").booleanValue()) {
							existingUserAccount.setDeleted(!userInfo.get(
									"IsActive").booleanValue());
						}
					} else {
						newAccount.setDeleted(!userInfo.get("IsActive")
								.booleanValue());
					}

					// TODO: Check the old ways to do this
					// if (userInfo != null && userInfo.has("IsActive")) {
					// newAccount.setActive(userInfo.get(
					// "IsActive").getBooleanValue());
					// newAccount.setDeleted(!userInfo.get(
					// "IsActive").getBooleanValue());
					// newProfile.setDeleted(!userInfo.get(
					// "IsActive").getBooleanValue());
					// }

					if (!userID.equals("0")) {
						if (existingUserProfile.isDeleted() != !userInfo.get(
								"IsActive").booleanValue()) {
							existingUserProfile.setDeleted(!userInfo.get(
									"IsActive").booleanValue());
						}
					} else {
						newProfile.setDeleted(!userInfo.get("IsActive")
								.booleanValue());
					}
				}

				if (userID.equals("0")) {
					newProfile.setUserAccount(newAccount);
				}

				if (userInfo != null && userInfo.has("FirstName")) {
					if (!userID.equals("0")) {
						if (!existingUserProfile.getFirstName().equals(
								userInfo.get("FirstName").textValue())) {
							existingUserProfile.setFirstName(userInfo.get(
									"FirstName").textValue());
						}
					} else {
						newProfile.setFirstName(userInfo.get("FirstName")
								.textValue());
					}
				}

				if (userInfo != null && userInfo.has("MiddleName")) {
					if (!userID.equals("0")) {
						if (!existingUserProfile.getMiddleName().equals(
								userInfo.get("MiddleName").textValue())) {
							existingUserProfile.setMiddleName(userInfo.get(
									"MiddleName").textValue());
						}
					} else {
						newProfile.setMiddleName(userInfo.get("MiddleName")
								.textValue());
					}
				}

				if (userInfo != null && userInfo.has("LastName")) {
					if (!userID.equals("0")) {
						if (!existingUserProfile.getLastName().equals(
								userInfo.get("LastName").textValue())) {
							existingUserProfile.setLastName(userInfo.get(
									"LastName").textValue());
						}
					} else {
						newProfile.setLastName(userInfo.get("LastName")
								.textValue());
					}
				}

				if (userInfo != null && userInfo.has("DOB")) {
					Date dob = formatter.parse(userInfo.get("DOB").textValue());
					if (!userID.equals("0")) {
						if (!existingUserProfile.getDateOfBirth().equals(dob)) {
							existingUserProfile.setDateOfBirth(dob);
						}
					} else {
						newProfile.setDateOfBirth(dob);
					}
				}

				if (userInfo != null && userInfo.has("Gender")) {
					if (!userID.equals("0")) {
						if (!existingUserProfile.getGender().equals(
								userInfo.get("Gender").textValue())) {
							existingUserProfile.setGender(userInfo
									.get("Gender").textValue());
						}
					} else {
						newProfile
								.setGender(userInfo.get("Gender").textValue());
					}
				}

				Address newAddress = new Address();

				if (userInfo != null && userInfo.has("Street")) {
					newAddress.setStreet(userInfo.get("Street").textValue());
				}
				if (userInfo != null && userInfo.has("Apt")) {
					newAddress.setApt(userInfo.get("Apt").textValue());
				}
				if (userInfo != null && userInfo.has("City")) {
					newAddress.setCity(userInfo.get("City").textValue());
				}
				if (userInfo != null && userInfo.has("State")) {
					newAddress.setState(userInfo.get("State").textValue());
				}
				if (userInfo != null && userInfo.has("Zip")) {
					newAddress.setZipcode(userInfo.get("Zip").textValue());
				}
				if (userInfo != null && userInfo.has("Country")) {
					newAddress.setCountry(userInfo.get("Country").textValue());
				}

				if (!userID.equals("0")) {
					boolean alreadyExist = false;
					for (Address address : existingUserProfile.getAddresses()) {
						if (address.equals(newAddress)) {
							alreadyExist = true;
							break;
						}
					}
					if (!alreadyExist) {
						existingUserProfile.getAddresses().clear();
						existingUserProfile.getAddresses().add(newAddress);
					}
				} else {
					newProfile.getAddresses().add(newAddress);
				}

				if (userInfo != null && userInfo.has("OfficeNumber")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String officeNo : existingUserProfile
								.getOfficeNumbers()) {
							if (officeNo.equals(userInfo.get("OfficeNumber")
									.textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getOfficeNumbers().clear();
							existingUserProfile.getOfficeNumbers().add(
									userInfo.get("OfficeNumber").textValue());
						}
					} else {
						newProfile.getOfficeNumbers().add(
								userInfo.get("OfficeNumber").textValue());
					}
				}

				if (userInfo != null && userInfo.has("MobileNumber")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String mobileNo : existingUserProfile
								.getMobileNumbers()) {
							if (mobileNo.equals(userInfo.get("MobileNumber")
									.textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getMobileNumbers().clear();
							existingUserProfile.getMobileNumbers().add(
									userInfo.get("MobileNumber").textValue());
						}
					} else {
						newProfile.getMobileNumbers().add(
								userInfo.get("MobileNumber").textValue());
					}
				}

				if (userInfo != null && userInfo.has("HomeNumber")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String homeNo : existingUserProfile
								.getHomeNumbers()) {
							if (homeNo.equals(userInfo.get("HomeNumber")
									.textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getHomeNumbers().clear();
							existingUserProfile.getHomeNumbers().add(
									userInfo.get("HomeNumber").textValue());
						}
					} else {
						newProfile.getHomeNumbers().add(
								userInfo.get("HomeNumber").textValue());
					}
				}

				if (userInfo != null && userInfo.has("OtherNumber")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String otherNo : existingUserProfile
								.getOtherNumbers()) {
							if (otherNo.equals(userInfo.get("OtherNumber")
									.textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getOtherNumbers().clear();
							existingUserProfile.getOtherNumbers().add(
									userInfo.get("OtherNumber").textValue());
						}
					} else {
						newProfile.getOtherNumbers().add(
								userInfo.get("OtherNumber").textValue());
					}
				}

				if (userInfo != null && userInfo.has("WorkEmail")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String workEmail : existingUserProfile
								.getWorkEmails()) {
							if (workEmail.equals(userInfo.get("WorkEmail")
									.textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getWorkEmails().clear();
							existingUserProfile.getWorkEmails().add(
									userInfo.get("WorkEmail").textValue());
						}
					} else {
						newProfile.getWorkEmails().add(
								userInfo.get("WorkEmail").textValue());
					}
				}

				if (userInfo != null && userInfo.has("PersonalEmail")) {
					if (!userID.equals("0")) {
						boolean alreadyExist = false;
						for (String personalEmail : existingUserProfile
								.getPersonalEmails()) {
							if (personalEmail.equals(userInfo.get(
									"PersonalEmail").textValue())) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							existingUserProfile.getPersonalEmails().clear();
							existingUserProfile.getPersonalEmails().add(
									userInfo.get("PersonalEmail").textValue());
						}
					} else {
						newProfile.getPersonalEmails().add(
								userInfo.get("PersonalEmail").textValue());
					}
				}

				if (userInfo != null && userInfo.has("SaveOptions")) {
					if (!userID.equals("0")) {
						existingUserProfile.getDetails().clear();
					}

					String[] rows = userInfo.get("SaveOptions").textValue()
							.split("#!#");

					for (String col : rows) {
						String[] cols = col.split("!#!");
						PositionDetails newDetails = new PositionDetails();
						newDetails.setCollege(cols[0]);
						newDetails.setDepartment(cols[1]);
						newDetails.setPositionType(cols[2]);
						newDetails.setPositionTitle(cols[3]);
						newDetails.setAsDefault(Boolean.parseBoolean(cols[4]));
						if (!userID.equals("0")) {
							existingUserProfile.getDetails().add(newDetails);
						} else {
							newProfile.getDetails().add(newDetails);
						}
					}
				} else if (userInfo != null && userInfo.has("positionTitle")) {
					PositionDetails newDetails = new PositionDetails();
					newDetails.setPositionType("University administrator");
					newDetails.setPositionTitle(userInfo.get("positionTitle")
							.textValue());
					newDetails.setAsDefault(true);
					if (!userID.equals("0")) {
						existingUserProfile.getDetails().clear();
						existingUserProfile.getDetails().add(newDetails);
					} else {
						newProfile.getDetails().add(newDetails);
					}
				}
			}

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			@SuppressWarnings("unused")
			String userCollege = new String();
			@SuppressWarnings("unused")
			String userDepartment = new String();
			@SuppressWarnings("unused")
			String userPositionType = new String();
			@SuppressWarnings("unused")
			String userPositionTitle = new String();
			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);

			// Save the User Profile
			NotificationLog notification = new NotificationLog();
			if (!userID.equals("0")) {
				if (!oldUserProfile.equals(existingUserProfile)) {
					// Save the User Account
					if (!oldUserProfile.getUserAccount().equals(
							existingUserAccount)) {
						userAccountDAO.save(existingUserAccount);
					}

					userProfileDAO.updateUser(existingUserProfile,
							authorProfile);

					// For Admin
					notification = new NotificationLog();
					notification.setType("User");
					if (isActiveUser) {
						notification.setAction("Account is activated.");
					} else {
						notification.setAction("Account is updated.");
					}
					notification.setUserProfileId(existingUserProfile.getId()
							.toString());
					notification.setUsername(existingUserProfile
							.getUserAccount().getUserName());
					notification.setForAdmin(true);
					notificationDAO.save(notification);

					// For all Roles of the User
					for (PositionDetails positions : existingUserProfile
							.getDetails()) {
						notification = new NotificationLog();
						notification.setType("User");
						notification.setAction("Account is updated.");

						notification.setUserProfileId(existingUserProfile
								.getId().toString());
						notification.setUsername(existingUserProfile
								.getUserAccount().getUserName());
						notification.setCollege(positions.getCollege());
						notification.setDepartment(positions.getDepartment());
						notification.setPositionType(positions
								.getPositionType());
						notification.setPositionTitle(positions
								.getPositionTitle());
						notificationDAO.save(notification);
					}
				}
			} else {
				userAccountDAO.save(newAccount);

				userProfileDAO.saveUser(newProfile, authorProfile);

				// For Admin
				notification = new NotificationLog();
				notification.setType("User");
				notification.setAction("Account is created.");
				notification.setUserProfileId(newProfile.getId().toString());
				notification.setUsername(newProfile.getUserAccount()
						.getUserName());
				notification.setForAdmin(true);
				notificationDAO.save(notification);

				// For Roles of the user notify
				for (PositionDetails positions : newProfile.getDetails()) {
					notification = new NotificationLog();
					notification.setType("User");
					notification.setAction("Account is created.");
					notification
							.setUserProfileId(newProfile.getId().toString());
					notification.setUsername(newProfile.getUserAccount()
							.getUserName());
					notification.setCollege(positions.getCollege());
					notification.setDepartment(positions.getDepartment());
					notification.setPositionType(positions.getPositionType());
					notification.setPositionTitle(positions.getPositionTitle());
					notificationDAO.save(notification);
				}
			}

			// Broadcasting SSE

			OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			OutboundEvent event = eventBuilder.name("notification")
					.mediaType(MediaType.TEXT_PLAIN_TYPE)
					.data(String.class, "1").build();

			NotificationService.BROADCASTER.broadcast(event);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error(
					"Could not save a New User or update an existing User error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Save A New User OR Update AN Existing User\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/signup")
	@ApiOperation(value = "Registering a New user", notes = "This API signups a new user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response signUpUser(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::signUpUser started");

			String userID = new String();
			String userEmail = new String();

			UserAccount newAccount = new UserAccount();
			UserProfile newProfile = new UserProfile();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userInfo")) {
				JsonNode userInfo = root.get("userInfo");

				if (userInfo != null && userInfo.has("UserID")) {
					userID = userInfo.get("UserID").textValue();
				}

				if (userID.equals("0")) {

					newAccount.setAddedOn(new Date());

					if (userInfo != null && userInfo.has("UserName")) {
						String loginUserName = userInfo.get("UserName")
								.textValue();
						newAccount.setUserName(loginUserName);
					}

					if (userInfo != null && userInfo.has("Password")) {
						newAccount.setPassword(userInfo.get("Password")
								.textValue());
					}

					newProfile.setUserAccount(newAccount);

					if (userInfo != null && userInfo.has("FirstName")) {
						newProfile.setFirstName(userInfo.get("FirstName")
								.textValue());
					}

					if (userInfo != null && userInfo.has("MiddleName")) {
						newProfile.setMiddleName(userInfo.get("MiddleName")
								.textValue());
					}

					if (userInfo != null && userInfo.has("LastName")) {
						newProfile.setLastName(userInfo.get("LastName")
								.textValue());
					}

					if (userInfo != null && userInfo.has("DOB")) {
						Date dob = formatter.parse(userInfo.get("DOB")
								.textValue());
						newProfile.setDateOfBirth(dob);
					}

					if (userInfo != null && userInfo.has("Gender")) {
						newProfile
								.setGender(userInfo.get("Gender").textValue());
					}

					Address newAddress = new Address();

					if (userInfo != null && userInfo.has("Street")) {
						newAddress
								.setStreet(userInfo.get("Street").textValue());
					}
					if (userInfo != null && userInfo.has("Apt")) {
						newAddress.setApt(userInfo.get("Apt").textValue());
					}
					if (userInfo != null && userInfo.has("City")) {
						newAddress.setCity(userInfo.get("City").textValue());
					}
					if (userInfo != null && userInfo.has("State")) {
						newAddress.setState(userInfo.get("State").textValue());
					}
					if (userInfo != null && userInfo.has("Zip")) {
						newAddress.setZipcode(userInfo.get("Zip").textValue());
					}
					if (userInfo != null && userInfo.has("Country")) {
						newAddress.setCountry(userInfo.get("Country")
								.textValue());
					}

					newProfile.getAddresses().add(newAddress);

					if (userInfo != null && userInfo.has("MobileNumber")) {
						newProfile.getMobileNumbers().add(
								userInfo.get("MobileNumber").textValue());
					}

					if (userInfo != null && userInfo.has("WorkEmail")) {
						userEmail = userInfo.get("WorkEmail").textValue();

						newProfile.getWorkEmails().add(userEmail);
					}
				}

				// Save the User Account
				userAccountDAO.save(newAccount);

				// Save the User Profile
				userProfileDAO.signUpUser(newProfile);

				NotificationLog notification = new NotificationLog();
				notification.setType("User");
				notification.setAction("Signed up.");
				notification.setUserProfileId(newAccount.getId().toString());
				notification.setUsername(newAccount.getUserName());
				notification.setForAdmin(true);
				notificationDAO.save(notification);
			}

			// Broadcasting SSE

			OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
			OutboundEvent event = eventBuilder.name("notification")
					.mediaType(MediaType.TEXT_PLAIN_TYPE)
					.data(String.class, "1").build();

			NotificationService.BROADCASTER.broadcast(event);

			// UserProfile user = userProfileDAO.findByUserAccount(newAccount);
			// System.out.println(user);
			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error("Could not register a new user error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Register A New User\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/login")
	@ApiOperation(value = "login user with valid username and password", notes = "This API allows to login a valid user with authorized username and password"
			+ "<p><u>Form Parameters</u><ul><li><b>username</b> is required</li><li><b>password</b> is required</li></ul>")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Redirect to Dashboard page }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response login(
			@ApiParam(value = "username", required = true, defaultValue = "test", allowableValues = "", allowMultiple = false) @FormParam("username") String email,
			@ApiParam(value = "password", required = true, defaultValue = "password", allowableValues = "", allowMultiple = false) @FormParam("password") String password,
			@Context HttpServletRequest req) {
		try {
			if (req == null) {
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("{\"error\": \"Could Not Find The User\", \"status\": \"FAIL\"}")
						.build();
			}

			List<UserProfile> userList = userProfileDAO.findAll();
			boolean isFound = false;
			if (userList.size() != 0) {
				for (UserProfile user : userList) {
					if (user.getUserAccount().getUserName().equals(email)
							|| user.getWorkEmails().contains(email)) {
						if (PasswordHash.validatePassword(password, user
								.getUserAccount().getPassword())
								&& !user.isDeleted()
								&& user.getUserAccount().isActive()
								&& !user.getUserAccount().isDeleted()) {
							isFound = true;

							setMySessionID(req, user.getId().toString());
							java.net.URI location = new java.net.URI(
									"../Home.jsp");
							if (user.getUserAccount().isAdmin()) {
								location = new java.net.URI("../Dashboard.jsp");
							}
							return Response.seeOther(location).build();
						} else {
							isFound = false;
						}
					}
				}
			} else {
				isFound = false;
			}
			if (!isFound) {
				java.net.URI location = new java.net.URI(
						"../Login.jsp?msg=error");
				return Response.seeOther(location).build();
			}
		} catch (Exception e) {
			log.error("Could not find the User error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find The User\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/SetUserViewSession")
	@ApiOperation(value = "Set User Session based on selected position detail", notes = "This API sets the selected position detail as a session")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response setUserViewSession(
			@Context HttpServletRequest req,
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::setUserViewSession started");

			deleteAllSession(req);

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("userId") && root.has("userName")
					&& root.has("isAdminUser")) {
				String profileId = root.get("userId").textValue();
				ObjectId id = new ObjectId(profileId);

				String userName = new String();
				Boolean isAdminUser = false;
				String college = new String();
				String department = new String();
				String positionType = new String();
				String positionTitle = new String();

				userName = root.get("userName").textValue();
				isAdminUser = Boolean.parseBoolean(root.get("isAdminUser")
						.textValue());

				if (root != null && root.has("college")) {
					college = root.get("college").textValue();
				}

				if (root != null && root.has("department")) {
					department = root.get("department").textValue();
				}

				if (root != null && root.has("positionType")) {
					positionType = root.get("positionType").textValue();
				}

				if (root != null && root.has("positionTitle")) {
					positionTitle = root.get("positionTitle").textValue();
				}

				UserProfile user = userProfileDAO.findMatchedUserDetails(id,
						userName, isAdminUser, college, department,
						positionType, positionTitle);
				if (user != null) {
					setUserCurrentSession(req, userName, isAdminUser,
							profileId, college, department, positionType,
							positionTitle);
				}

				return Response
						.status(Response.Status.OK)
						.entity(mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(true)).build();

			}
		} catch (Exception e) {
			log.error(
					"Could not set User Session based on selected position detail error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Set User Session Based On Selected Position Detail\", \"status\": \"FAIL\"}")
				.build();

	}

	private void setUserCurrentSession(HttpServletRequest req, String userName,
			boolean admin, String userId, String college, String department,
			String positionType, String positionTitle) {

		HttpSession session = req.getSession();
		if (session.getAttribute("userProfileId") == null) {
			session.setAttribute("userProfileId", userId);
		}

		if (session.getAttribute("gpmsUserName") == null) {
			session.setAttribute("gpmsUserName", userName);
		}

		if (session.getAttribute("isAdmin") == null) {
			session.setAttribute("isAdmin", admin);
		}

		if (session.getAttribute("userCollege") == null) {
			session.setAttribute("userCollege", college);
		}

		if (session.getAttribute("userDepartment") == null) {
			session.setAttribute("userDepartment", department);
		}

		if (session.getAttribute("userPositionType") == null) {
			session.setAttribute("userPositionType", positionType);
		}

		if (session.getAttribute("userPositionTitle") == null) {
			session.setAttribute("userPositionTitle", positionTitle);
		}
	}

	@GET
	@Path("/logout")
	@ApiOperation(value = "Logout the User", notes = "This API logouts the user")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Redirect to Login page }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logout(@Context HttpServletRequest req) {
		try {
			log.info("UserService::logout started");

			if (req == null) {
				log.error("Null request in context");
				return Response
						.status(Response.Status.BAD_REQUEST)
						.entity("{\"error\": \"Could Not Logout the User\", \"status\": \"FAIL\"}")
						.build();
			}
			deleteAllSession(req);
			return Response.seeOther(new java.net.URI("../Login.jsp")).build();

		} catch (Exception e) {
			log.error("Could not logout the user error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Logout the User\", \"status\": \"FAIL\"}")
				.build();
	}

	private void deleteAllSession(@Context HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.removeAttribute("userProfileId");
		session.removeAttribute("gpmsUserName");
		session.removeAttribute("isAdmin");
		session.removeAttribute("userCollege");
		session.removeAttribute("userDepartment");
		session.removeAttribute("userPositionType");
		session.removeAttribute("userPositionTitle");
		session.invalidate();
	}

	private void setMySessionID(@Context HttpServletRequest req,
			String sessionValue) {
		try {
			if (req == null) {
				System.out.println("Null request in context");
			}
			HttpSession session = req.getSession();

			if (session.getAttribute("userProfileId") == null) {
				// id = System.currentTimeMillis();
				session.setAttribute("userProfileId", sessionValue);
			}

			UserProfile existingUserProfile = new UserProfile();
			if (!sessionValue.equals("null")) {
				ObjectId id = new ObjectId(sessionValue);
				existingUserProfile = userProfileDAO
						.findUserDetailsByProfileID(id);
			}

			if (session.getAttribute("gpmsUserName") == null) {
				// id = System.currentTimeMillis();
				session.setAttribute("gpmsUserName", existingUserProfile
						.getUserAccount().getUserName());
			}

			if (session.getAttribute("isAdmin") == null) {
				session.setAttribute("isAdmin", existingUserProfile
						.getUserAccount().isAdmin());
			}

			for (PositionDetails userDetails : existingUserProfile.getDetails()) {
				if (userDetails.isAsDefault()) {
					if (session.getAttribute("userPositionType") == null) {
						session.setAttribute("userPositionType",
								userDetails.getPositionType());
					}

					if (session.getAttribute("userPositionTitle") == null) {
						session.setAttribute("userPositionTitle",
								userDetails.getPositionTitle());
					}

					if (session.getAttribute("userDepartment") == null) {
						session.setAttribute("userDepartment",
								userDetails.getDepartment());
					}

					if (session.getAttribute("userCollege") == null) {
						session.setAttribute("userCollege",
								userDetails.getCollege());
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String getMySessionId(@Context HttpServletRequest req) {
		HttpSession session = req.getSession();
		if (session.getAttribute("userProfileId") != null) {
			return (String) session.getAttribute("userProfileId");
		}
		if (session.getAttribute("gpmsUserName") != null) {
			return (String) session.getAttribute("gpmsUserName");
		}

		if (session.getAttribute("userPositionType") != null) {
			return (String) session.getAttribute("userPositionType");
		}

		if (session.getAttribute("userPositionTitle") != null) {
			return (String) session.getAttribute("userPositionTitle");
		}

		if (session.getAttribute("userDepartment") != null) {
			return (String) session.getAttribute("userDepartment");
		}

		if (session.getAttribute("userCollege") != null) {
			return (String) session.getAttribute("userCollege");
		}

		if (session.getAttribute("isAdmin") != null) {
			return (String) session.getAttribute("isAdmin");
		}
		return null;
	}

	@POST
	@Path("/GetAllUserDropdown")
	@ApiOperation(value = "Get All Users", notes = "This API gets all active Users to bind in dropdowns")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getAllUsers() {
		try {
			log.info("UserService::getAllUsers started");

			HashMap<String, String> users = new HashMap<String, String>();
			// List<UserProfile> userprofiles =
			// userProfileDAO.findAllActiveUsers();
			List<UserProfile> userprofiles = userProfileDAO
					.findAllUsersWithPosition();
			for (UserProfile userProfile : userprofiles) {
				users.put(userProfile.getId().toString(),
						userProfile.getFullName());
			}

			ObjectMapper mapper = new ObjectMapper();

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(users)).build();

		} catch (Exception e) {
			log.error("Could not get all Users error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get All Users\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetCurrentPositionDetailsForPI")
	@ApiOperation(value = "Get Current Position Details For PI", notes = "This API gets current Position Details for PI")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Investigator Users And Positions }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getCurrentPositionDetailsForPI(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::getCurrentPositionDetailsForPI started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			String userCollege = new String();
			String userDepartment = new String();
			String userPositionType = new String();
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			ObjectId id = new ObjectId(userProfileID);

			final MultimapAdapter multimapAdapter = new MultimapAdapter();
			final Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(Multimap.class, multimapAdapter)
					.create();

			return Response
					.status(Response.Status.OK)
					.entity(gson.toJson(userProfileDAO
							.findCurrentPositionDetailsForPI(id, userCollege,
									userDepartment, userPositionType,
									userPositionTitle))).build();

		} catch (Exception e) {
			log.error("Could not current Position Details for PI error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Current Position Details For PI\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetAllPositionDetailsForAUser")
	@ApiOperation(value = "Get All Position Details For A User", notes = "This API gets all Position Details for a User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getAllPositionDetailsForAUser(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::getAllPositionDetailsForAUser started");

			String userId = new String();

			ObjectMapper mapper = new ObjectMapper();

			JsonNode root = mapper.readTree(message);
			if (root != null && root.has("userId")) {
				userId = root.get("userId").textValue();
			}

			ObjectId id = new ObjectId(userId);

			final MultimapAdapter multimapAdapter = new MultimapAdapter();
			final Gson gson = new GsonBuilder().setPrettyPrinting()
					.registerTypeAdapter(Multimap.class, multimapAdapter)
					.create();

			return Response
					.status(Response.Status.OK)
					.entity(gson.toJson(userProfileDAO
							.findAllPositionDetailsForAUser(id))).build();

		} catch (Exception e) {
			log.error("Could not get all Position Details for a User error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get All Position Details For A User\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetAllProposalCountForAUser")
	@ApiOperation(value = "Get All Proposal Count For A User", notes = "This API gets all proposal count for a User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getAllProposalCountForAUser(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("UserService::getAllProposalCountForAUser started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			String userProfileID = new String();
			@SuppressWarnings("unused")
			String userName = new String();
			@SuppressWarnings("unused")
			Boolean userIsAdmin = false;
			String userCollege = new String();
			String userDepartment = new String();
			String userPositionType = new String();
			String userPositionTitle = new String();

			if (root != null && root.has("gpmsCommonObj")) {
				JsonNode commonObj = root.get("gpmsCommonObj");
				if (commonObj != null && commonObj.has("UserProfileID")) {
					userProfileID = commonObj.get("UserProfileID").textValue();
				}
				if (commonObj != null && commonObj.has("UserName")) {
					userName = commonObj.get("UserName").textValue();
				}
				if (commonObj != null && commonObj.has("UserIsAdmin")) {
					userIsAdmin = Boolean.parseBoolean(commonObj.get(
							"UserIsAdmin").textValue());
				}
				if (commonObj != null && commonObj.has("UserCollege")) {
					userCollege = commonObj.get("UserCollege").textValue();
				}
				if (commonObj != null && commonObj.has("UserDepartment")) {
					userDepartment = commonObj.get("UserDepartment")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionType")) {
					userPositionType = commonObj.get("UserPositionType")
							.textValue();
				}
				if (commonObj != null && commonObj.has("UserPositionTitle")) {
					userPositionTitle = commonObj.get("UserPositionTitle")
							.textValue();
				}
			}

			UserProposalCount count = userProfileDAO.getUserProposalCounts(
					userProfileID, userCollege, userDepartment,
					userPositionType, userPositionTitle);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(count)).build();

		} catch (Exception e) {
			log.error("Could not get all proposal count for a User error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Get All Proposal Count For A User\", \"status\": \"FAIL\"}")
				.build();
	}
}
