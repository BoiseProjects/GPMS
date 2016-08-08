package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.accesscontrol.Accesscontrol;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.AuditLogInfo;
import gpms.model.Delegation;
import gpms.model.DelegationInfo;
import gpms.model.NotificationLog;
import gpms.model.UserAccount;
import gpms.model.UserDetail;
import gpms.model.UserProfile;
import gpms.utils.EmailUtil;
import gpms.utils.SerializationHelper;
import gpms.utils.WriteXMLUtil;
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
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.mongodb.morphia.Morphia;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.xacml3.Advice;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.MongoClient;

@Path("/delegations")
@Api(value = "/delegations", description = "Manage Delegations")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN, MediaType.TEXT_HTML })
public class DelegationService {
	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	DelegationDAO delegationDAO = null;
	NotificationDAO notificationDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	private static String policyLocation = new String();

	private static final Logger log = Logger.getLogger(DelegationService.class
			.getName());

	public DelegationService() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		delegationDAO = new DelegationDAO(mongoClient, morphia, dbName);
		delegationDAO = new DelegationDAO(mongoClient, morphia, dbName);
		notificationDAO = new NotificationDAO(mongoClient, morphia, dbName);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@ApiOperation(value = "Test Delegation Service", notes = "This API tests whether the service is working or not")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Hello World! }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response testService() {
		try {
			log.info("DelegationService::testService started");

			return Response.status(Response.Status.OK).entity("Hello World!")
					.build();
		} catch (Exception e) {
			log.error("Could not connect the Delegation Service error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Delegation Service\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetDelegationsList")
	@ApiOperation(value = "Get all Delegations", notes = "This API gets all Delegations")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Delegation Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUserDelegationsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::produceDelegationsJSON started");

			List<DelegationInfo> delegations = new ArrayList<DelegationInfo>();

			int offset = 0, limit = 0;
			String delegatee = new String();
			String delegatedFrom = new String();
			String delegatedTo = new String();
			String delegatedAction = new String();
			Boolean isRevoked = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("offset")) {
				offset = root.get("offset").intValue();
			}

			if (root != null && root.has("limit")) {
				limit = root.get("limit").intValue();
			}

			if (root != null && root.has("delegationBindObj")) {
				JsonNode delegationObj = root.get("delegationBindObj");
				if (delegationObj != null && delegationObj.has("delegatee")) {
					delegatee = delegationObj.get("delegatee").textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedFrom")) {
					delegatedFrom = delegationObj.get("delegatedFrom")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedTo")) {
					delegatedTo = delegationObj.get("delegatedTo").textValue();
				}

				if (delegationObj != null
						&& delegationObj.has("delegatedAction")) {
					delegatedAction = delegationObj.get("delegatedAction")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("isRevoked")) {
					if (!delegationObj.get("isRevoked").isNull()) {
						isRevoked = delegationObj.get("isRevoked")
								.booleanValue();
					} else {
						isRevoked = null;
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

			delegations = delegationDAO.findAllForUserDelegationGrid(offset,
					limit, delegatee, delegatedFrom, delegatedTo,
					delegatedAction, isRevoked, userProfileID);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(delegations)).build();

		} catch (Exception e) {
			log.error("Could not find all Delegations of a User error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Delegations Of A User\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/DelegationsExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(value = "Export all Delegations in a grid", notes = "This API exports all Delegations shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportDelegationsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::exportDelegationsJSON started");

			List<DelegationInfo> delegations = new ArrayList<DelegationInfo>();

			String delegatee = new String();
			String delegatedFrom = new String();
			String delegatedTo = new String();
			String delegatedAction = new String();
			Boolean isRevoked = null;

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("delegationBindObj")) {
				JsonNode delegationObj = root.get("delegationBindObj");
				if (delegationObj != null && delegationObj.has("delegatee")) {
					delegatee = delegationObj.get("delegatee").textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedFrom")) {
					delegatedFrom = delegationObj.get("delegatedFrom")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedTo")) {
					delegatedTo = delegationObj.get("delegatedTo").textValue();
				}

				if (delegationObj != null
						&& delegationObj.has("delegatedAction")) {
					delegatedAction = delegationObj.get("delegatedAction")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("isRevoked")) {
					if (!delegationObj.get("isRevoked").isNull()) {
						isRevoked = delegationObj.get("isRevoked")
								.booleanValue();
					} else {
						isRevoked = null;
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
			if (root != null && root.has("delegationBindObj")) {
				JsonNode delegationObj = root.get("delegationBindObj");
				if (delegationObj != null && delegationObj.has("delegatee")) {
					delegatee = delegationObj.get("delegatee").textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedFrom")) {
					delegatedFrom = delegationObj.get("delegatedFrom")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("delegatedTo")) {
					delegatedTo = delegationObj.get("delegatedTo").textValue();
				}

				if (delegationObj != null
						&& delegationObj.has("delegatedAction")) {
					delegatedAction = delegationObj.get("delegatedAction")
							.textValue();
				}

				if (delegationObj != null && delegationObj.has("isRevoked")) {
					if (!delegationObj.get("isRevoked").isNull()) {
						isRevoked = delegationObj.get("isRevoked")
								.booleanValue();
					} else {
						isRevoked = null;
					}
				}
			}

			delegations = delegationDAO.findAllUserDelegations(delegatee,
					delegatedFrom, delegatedTo, delegatedAction, isRevoked,
					userProfileID);

			String filename = new String();
			if (delegations.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("Delegations");
				SheetWriter<DelegationInfo> writer = sheet
						.getBeanWriter(DelegationInfo.class);

				writer.write(delegations);

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
			log.error("Could not export all Delegations error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Export All Delegations\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetDelegationAuditLogList")
	@ApiOperation(value = "Get Delegation Audit Log List", notes = "This API gets Delegation Audit Log List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { AuditLog Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceDelegationAuditLogJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::produceDelegationAuditLogJSON started");

			List<AuditLogInfo> delegationAuditLogs = new ArrayList<AuditLogInfo>();

			int offset = 0, limit = 0;
			String delegationId = new String();
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

			if (root != null && root.has("delegationId")) {
				delegationId = root.get("delegationId").textValue();
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

			ObjectId id = new ObjectId(delegationId);

			delegationAuditLogs = delegationDAO
					.findAllForDelegationAuditLogGrid(offset, limit, id,
							action, auditedBy, activityOnFrom, activityOnTo);

			// users = (ArrayList<UserInfo>)
			// userProfileDAO.findAllForUserGrid();
			// response = JSONTansformer.ConvertToJSON(users);

			;
			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(delegationAuditLogs)).build();

		} catch (Exception e) {
			log.error("Could not find Delegation Audit Log List error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Delegation Audit Log List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/DelegationLogsExportToExcel")
	@ApiOperation(value = "Export all Delegation Logs in a grid", notes = "This API exports all Delegation Logs shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportDelegationAuditLogJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::exportDelegationAuditLogJSON started");

			List<AuditLogInfo> delegationAuditLogs = new ArrayList<AuditLogInfo>();

			String delegationId = new String();
			String action = new String();
			String auditedBy = new String();
			String activityOnFrom = new String();
			String activityOnTo = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("delegationId")) {
				delegationId = root.get("delegationId").textValue();
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

			ObjectId id = new ObjectId(delegationId);

			delegationAuditLogs = delegationDAO.findAllUserDelegationAuditLogs(
					id, action, auditedBy, activityOnFrom, activityOnTo);

			// users = (ArrayList<UserInfo>)
			// userProfileDAO.findAllForUserGrid();
			// response = JSONTansformer.ConvertToJSON(users);
			String filename = new String();
			if (delegationAuditLogs.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("AuditLogs");
				SheetWriter<AuditLogInfo> writer = sheet
						.getBeanWriter(AuditLogInfo.class);

				writer.write(delegationAuditLogs);

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
			log.error("Could not export Delegation Logs error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delegation Logs List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetDelegationDetailsByDelegationId")
	@ApiOperation(value = "Get Delegation Details by DelegationId", notes = "This API gets Delegation Details by DelegationId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Delegation }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceDelegationDetailsByDelegationId(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::produceDelegationDetailsByDelegationId started");

			Delegation delegation = new Delegation();
			String delegationId = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("delegationId")) {
				delegationId = root.get("delegationId").textValue();
			}

			ObjectId id = new ObjectId(delegationId);
			delegation = delegationDAO.findDelegationByDelegationID(id);

			// Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
			// .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
			// .create();
			// return gson.toJson(delegation, Delegation.class);

			// mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,
			// false);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.setDateFormat(formatter)
							.writerWithDefaultPrettyPrinter()
							.writeValueAsString(delegation)).build();

		} catch (Exception e) {
			log.error(
					"Could not find Delegation Details by DelegationId error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Delegation Details By DelegationId\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetDelegableUsersForAction")
	@ApiOperation(value = "Get Delegable Multiple Response For supplied Action of a User", notes = "This API gets Delegable Multiple Response For supplied Action for a User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { User Detail for Dropdown }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceDelegableUsersForAction(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::produceDelegableUsersForAction started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("policyInfo")) {
				JsonNode policyInfo = root.get("policyInfo");
				if (policyInfo != null && policyInfo.isArray()
						&& policyInfo.size() > 0) {
					Accesscontrol ac = new Accesscontrol();
					HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

					Multimap<String, String> subjectMap = ArrayListMultimap
							.create();
					Multimap<String, String> resourceMap = ArrayListMultimap
							.create();
					Multimap<String, String> actionMap = ArrayListMultimap
							.create();
					Multimap<String, String> environmentMap = ArrayListMultimap
							.create();
					for (JsonNode node : policyInfo) {
						String attributeName = node.path("attributeName")
								.asText();
						String attributeValue = node.path("attributeValue")
								.asText();
						String attributeType = node.path("attributeType")
								.asText();
						switch (attributeType) {
						case "Subject":
							subjectMap.put(attributeName, attributeValue);
							attrMap.put("Subject", subjectMap);
							break;
						case "Resource":
							resourceMap.put(attributeName, attributeValue);
							attrMap.put("Resource", resourceMap);
							break;
						case "Action":
							actionMap.put(attributeName, attributeValue);
							attrMap.put("Action", actionMap);
							break;
						case "Environment":
							environmentMap.put(attributeName, attributeValue);
							attrMap.put("Environment", environmentMap);
							break;
						default:
							break;
						}
					}

					if (attrMap.get("Resource") == null) {
						attrMap.put("Resource", resourceMap);
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
							userProfileID = commonObj.get("UserProfileID")
									.textValue();
						}
						if (commonObj != null && commonObj.has("UserName")) {
							userName = commonObj.get("UserName").textValue();
						}
						if (commonObj != null && commonObj.has("UserIsAdmin")) {
							userIsAdmin = Boolean.parseBoolean(commonObj.get(
									"UserIsAdmin").textValue());
						}
						if (commonObj != null && commonObj.has("UserCollege")) {
							userCollege = commonObj.get("UserCollege")
									.textValue();
						}
						if (commonObj != null
								&& commonObj.has("UserDepartment")) {
							userDepartment = commonObj.get("UserDepartment")
									.textValue();
						}
						if (commonObj != null
								&& commonObj.has("UserPositionType")) {
							userPositionType = commonObj
									.get("UserPositionType").textValue();
						}
						if (commonObj != null
								&& commonObj.has("UserPositionTitle")) {
							userPositionTitle = commonObj.get(
									"UserPositionTitle").textValue();
						}
					}

					StringBuffer contentProfile = new StringBuffer();

					contentProfile.append("<Content>");
					contentProfile
							.append("<ak:record xmlns:ak=\"http://akpower.org\">");

					contentProfile.append("<ak:user>");

					contentProfile.append("<ak:userid>");
					contentProfile.append("578918b9bcbb29090c4278e7");
					contentProfile.append("</ak:userid>");

					contentProfile.append("<ak:fullname>");
					contentProfile.append("Computer Science Associate Chair");
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:email>");
					contentProfile.append("csassociatechair@gmail.com");
					contentProfile.append("</ak:email>");

					contentProfile.append("<ak:college>");
					contentProfile.append("Engineering");
					contentProfile.append("</ak:college>");

					contentProfile.append("<ak:department>");
					contentProfile.append("Computer Science");
					contentProfile.append("</ak:department>");

					contentProfile.append("<ak:positionType>");
					contentProfile.append("Administrator");
					contentProfile.append("</ak:positionType>");

					contentProfile.append("<ak:positiontitle>");
					contentProfile.append("Associate Chair");
					contentProfile.append("</ak:positiontitle>");

					contentProfile.append("</ak:user>");

					contentProfile.append("<ak:user>");

					contentProfile.append("<ak:userid>");
					contentProfile.append("578918b9bcbb29090c4278e1");
					contentProfile.append("</ak:userid>");

					contentProfile.append("<ak:fullname>");
					contentProfile
							.append("Computer Engineering Associate Chair");
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:email>");
					contentProfile.append("ceassociatechair@gmail.com");
					contentProfile.append("</ak:email>");

					contentProfile.append("<ak:college>");
					contentProfile.append("Engineering");
					contentProfile.append("</ak:college>");

					contentProfile.append("<ak:department>");
					contentProfile.append("Computer Engineering");
					contentProfile.append("</ak:department>");

					contentProfile.append("<ak:positionType>");
					contentProfile.append("Administrator");
					contentProfile.append("</ak:positionType>");

					contentProfile.append("<ak:positiontitle>");
					contentProfile.append("Associate Chair");
					contentProfile.append("</ak:positiontitle>");

					contentProfile.append("</ak:user>");

					contentProfile.append("<ak:user>");

					contentProfile.append("<ak:userid>");
					contentProfile.append("578918b9bcbb29090c4278e2");
					contentProfile.append("</ak:userid>");

					contentProfile.append("<ak:fullname>");
					contentProfile
							.append("Computer Science Department Administrative Assistant");
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:email>");
					contentProfile.append("csdeptadminasst@gmail.com");
					contentProfile.append("</ak:email>");

					contentProfile.append("<ak:college>");
					contentProfile.append("Engineering");
					contentProfile.append("</ak:college>");

					contentProfile.append("<ak:department>");
					contentProfile.append("Computer Science");
					contentProfile.append("</ak:department>");

					contentProfile.append("<ak:positionType>");
					contentProfile.append("Administrator");
					contentProfile.append("</ak:positionType>");

					contentProfile.append("<ak:positiontitle>");
					contentProfile
							.append("Department Administrative Assistant");
					contentProfile.append("</ak:positiontitle>");

					contentProfile.append("</ak:user>");

					contentProfile.append("<ak:user>");

					contentProfile.append("<ak:userid>");
					contentProfile.append("578918b9bcbb29090c4278e3");
					contentProfile.append("</ak:userid>");

					contentProfile.append("<ak:fullname>");
					contentProfile.append("Computer Science Dean");
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:email>");
					contentProfile.append("cedean@gmail.com");
					contentProfile.append("</ak:email>");

					contentProfile.append("<ak:college>");
					contentProfile.append("Engineering");
					contentProfile.append("</ak:college>");

					contentProfile.append("<ak:department>");
					contentProfile.append("Computer Science");
					contentProfile.append("</ak:department>");

					contentProfile.append("<ak:positionType>");
					contentProfile.append("Administrator");
					contentProfile.append("</ak:positionType>");

					contentProfile.append("<ak:positiontitle>");
					contentProfile.append("Dean");
					contentProfile.append("</ak:positiontitle>");

					contentProfile.append("</ak:user>");

					contentProfile.append("<ak:user>");

					contentProfile.append("<ak:userid>");
					contentProfile.append("578918b9bcbb29090c4278e4");
					contentProfile.append("</ak:userid>");

					contentProfile.append("<ak:fullname>");
					contentProfile.append("Computer Science Associate Dean");
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:email>");
					contentProfile.append("csassociatedean@gmail.com");
					contentProfile.append("</ak:email>");

					contentProfile.append("<ak:college>");
					contentProfile.append("Engineering");
					contentProfile.append("</ak:college>");

					contentProfile.append("<ak:department>");
					contentProfile.append("Computer Science");
					contentProfile.append("</ak:department>");

					contentProfile.append("<ak:positionType>");
					contentProfile.append("Administrator");
					contentProfile.append("</ak:positionType>");

					contentProfile.append("<ak:positiontitle>");
					contentProfile.append("Associate Dean");
					contentProfile.append("</ak:positiontitle>");

					contentProfile.append("</ak:user>");

					contentProfile.append("</ak:record>");
					contentProfile.append("</Content>");

					contentProfile
							.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\" IncludeInResult=\"false\">");
					contentProfile
							.append("<AttributeValue DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\" XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\">//ak:record//ak:user</AttributeValue>");
					contentProfile.append("</Attribute>");

					Set<AbstractResult> results = ac
							.getXACMLdecisionWithObligations(attrMap,
									contentProfile);

					List<UserDetail> userDetails = new ArrayList<UserDetail>();
					for (AbstractResult result : results) {
						if (AbstractResult.DECISION_PERMIT == result
								.getDecision()) {
							System.out
									.println("===========================================================");
							System.out
									.println("\n======================== Printing Advices ====================");
							List<Advice> advices = result.getAdvices();
							for (Advice advice : advices) {
								if (advice instanceof org.wso2.balana.xacml3.Advice) {
									UserDetail userDeatil = new UserDetail();

									List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Advice) advice)
											.getAssignments();
									for (AttributeAssignment assignment : assignments) {
										switch (assignment.getAttributeId()
												.toString()) {
										case "userId":
											userDeatil
													.setUserProfileId(assignment
															.getContent());
											break;
										case "userfullName":
											userDeatil.setFullName(assignment
													.getContent());
											break;
										case "userEmail":
											userDeatil.setEmail(assignment
													.getContent());
											break;
										case "userCollege":
											userDeatil.setCollege(assignment
													.getContent());
											break;
										case "userDepartment":
											userDeatil.setDepartment(assignment
													.getContent());
											break;
										case "userPositionType":
											userDeatil
													.setPositionType(assignment
															.getContent());
											break;
										case "userPositionTitle":
											userDeatil
													.setPositionTitle(assignment
															.getContent());
											break;
										default:
											break;
										}
									}
									userDetails.add(userDeatil);
								}
							}
						}
					}
					return Response
							.status(Response.Status.OK)
							.entity(mapper.setDateFormat(formatter)
									.writerWithDefaultPrettyPrinter()
									.writeValueAsString(userDetails)).build();
				} else {
					return Response.status(403)
							.type(MediaType.APPLICATION_JSON)
							.entity("No User Permission Attributes are send!")
							.build();
				}
			}
		} catch (Exception e) {
			log.error(
					"Could not find User Detail for supplied Action of a User error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find User Detail for supplied Action of a User\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/SaveUpdateDelegation")
	@ApiOperation(value = "Save a New Delegation or Update an existing Delegation", notes = "This API saves a New Delegation or updates an existing Delegation")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response saveUpdateDelegation(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::saveUpdateDelegation started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			String userProfileID = new String();
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

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);
			String delegatorName = authorProfile.getFullName();

			UserProfile delegateeProfile = new UserProfile();

			String delegationID = new String();
			Delegation newDelegation = new Delegation();
			Delegation existingDelegation = new Delegation();
			Delegation oldDelegation = new Delegation();

			try {
				policyLocation = this.getClass().getResource("/policy").toURI()
						.getPath();
			} catch (Exception ex) {
				throw new Exception("The Policy folder can not be Found!");
			}

			String policyFileName = new String();
			boolean delegationIsChanged = false;

			if (root != null && root.has("delegationInfo")) {
				JsonNode delegationInfo = root.get("delegationInfo");

				if (delegationInfo != null
						&& delegationInfo.has("DelegationId")) {
					delegationID = delegationInfo.get("DelegationId")
							.textValue();
					if (!delegationID.equals("0")) {
						ObjectId delegationId = new ObjectId(delegationID);
						existingDelegation = delegationDAO
								.findDelegationByDelegationID(delegationId);
						// using our serializable method for cloning
						oldDelegation = SerializationHelper
								.cloneThroughSerialize(existingDelegation);
					}
				}

				if (delegationID.equals("0")) {
					newDelegation.setUserProfile(authorProfile);
					newDelegation.setDelegatorId(userProfileID);
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegatedAction")) {
					if (delegationID.equals("0")) {
						newDelegation.setAction(delegationInfo.get(
								"DelegatedAction").textValue());
					}
				}

				if (delegationInfo != null && delegationInfo.has("DelegateeId")) {
					String delegateeId = delegationInfo.get("DelegateeId")
							.textValue();
					// ObjectId id = new ObjectId(delegateeId);
					// delegateeProfile = userProfileDAO
					// .findUserDetailsByProfileID(id);

					if (delegationID.equals("0")) {
						newDelegation.setDelegateeId(delegateeId);
					}
				}

				if (delegationInfo != null && delegationInfo.has("Delegateee")) {
					final String delegatee = delegationInfo.get("Delegateee")
							.textValue().trim().replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(delegatee)
							&& delegationID.equals("0")) {
						newDelegation.setDelegatee(delegatee);
					} else {
						throw new Exception("The Delegatee can not be Empty");
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegateeCollege")) {
					if (delegationID.equals("0")) {
						newDelegation.setCollege(delegationInfo.get(
								"DelegateeCollege").textValue());
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegateeDepartment")) {
					if (delegationID.equals("0")) {
						newDelegation.setDepartment(delegationInfo.get(
								"DelegateeDepartment").textValue());
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegateePositionType")) {
					if (delegationID.equals("0")) {
						newDelegation.setPositionType(delegationInfo.get(
								"DelegateePositionType").textValue());
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegateePositionTitle")) {
					if (delegationID.equals("0")) {
						newDelegation.setPositionTitle(delegationInfo.get(
								"DelegateePositionTitle").textValue());
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegationFrom")) {
					Date fromDate = formatter.parse(delegationInfo
							.get("DelegationFrom").textValue().trim()
							.replaceAll("\\<[^>]*>", ""));
					if (validateNotEmptyValue(fromDate.toString())) {
						if (!delegationID.equals("0")) {
							if (!existingDelegation.getFrom().equals(
									delegationInfo.get("DelegationFrom")
											.textValue())) {
								existingDelegation.setFrom(fromDate);
							}
						} else {
							newDelegation.setFrom(fromDate);
						}
					} else {
						throw new Exception(
								"The Delegation Start From Date can not be Empty");
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegationTo")) {
					Date toDate = formatter.parse(delegationInfo
							.get("DelegationTo").textValue().trim()
							.replaceAll("\\<[^>]*>", ""));
					if (validateNotEmptyValue(toDate.toString())) {
						if (!delegationID.equals("0")) {
							if (!existingDelegation.getTo().equals(
									delegationInfo.get("DelegationTo")
											.textValue())) {
								existingDelegation.setTo(toDate);
							}
						} else {
							newDelegation.setTo(toDate);
						}
					} else {
						throw new Exception(
								"The Delegation End Date can not be Empty");
					}
				}

				if (delegationInfo != null
						&& delegationInfo.has("DelegationReason")) {
					final String reason = delegationInfo
							.get("DelegationReason").textValue().trim()
							.replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(reason)) {
						if (!delegationID.equals("0")) {
							if (!existingDelegation.getReason().equals(
									delegationInfo.get("DelegationReason")
											.textValue())) {
								existingDelegation.setReason(reason);
							}
						} else {
							newDelegation.setReason(reason);
						}
					} else {
						throw new Exception(
								"The Delegation Reason can not be Empty");
					}
				}

				String notificationMessage = new String();

				if (!delegationID.equals("0")) {
					if (!existingDelegation.equals(oldDelegation)) {
						// Delete the Delegation Dynamic Policy File here
						try {
							File file = new File(
									policyLocation
											+ "/"
											+ existingDelegation
													.getDelegationFileName());
							if (file.delete()) {
								System.out.println(file.getName()
										+ " is deleted!");

								// Create New policy File
								policyFileName = createDynamicPolicy(
										userProfileID, delegatorName,
										policyLocation, existingDelegation);
								existingDelegation
										.setDelegationFileName(policyFileName);

								delegationDAO.updateDelegation(
										existingDelegation, authorProfile);
								delegationIsChanged = true;
								notificationMessage = "Delegation Updated by "
										+ userName + ".";
							} else {
								return Response.status(403)
										.type(MediaType.APPLICATION_JSON)
										.entity("Delete operation is failed.")
										.build();
							}

						} catch (Exception e) {
							return Response
									.status(403)
									.type(MediaType.APPLICATION_JSON)
									.entity("File delete permission is not enabled!")
									.build();

						}
					}
				} else {
					policyFileName = createDynamicPolicy(userProfileID,
							delegatorName, policyLocation, existingDelegation);

					existingDelegation.setDelegationFileName(policyFileName);
					delegationDAO.saveDelegation(existingDelegation,
							authorProfile);
					delegationIsChanged = true;
					notificationMessage = "Delegation Added by " + userName
							+ ".";
				}

				if (delegationIsChanged) {
					// Update the current Delegator bit to true
					authorProfile.setDelegator(true);
					userProfileDAO.save(authorProfile);

					sendNotification(existingDelegation, delegateeProfile
							.getUserAccount().getUserName(), userProfileID,
							userName, userCollege, userDepartment,
							userPositionType, userPositionTitle,
							notificationMessage, "Delegation", false);
				}
			} else {
				return Response.status(403).type(MediaType.APPLICATION_JSON)
						.entity("No Delegation Info is send!").build();
			}
		} catch (Exception e) {
			log.error(
					"Could not save a New Delegation or update an existing Delegation error e=",
					e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"Could Not Save A New Delegation OR Update AN Existing Delegation\", \"status\": \"FAIL\"}")
				.build();
	}

	private String createDynamicPolicy(String delegatorId,
			String delegatorName, String policyLocation,
			Delegation existingDelegation) {
		return WriteXMLUtil.saveDelegationPolicy(delegatorId, delegatorName,
				policyLocation, existingDelegation);
	}

	private void sendNotification(Delegation existingDelegation,
			String delegateeUserName, String userProfileID, String userName,
			String userCollege, String userDepartment, String userPositionType,
			String userPositionTitle, String notificationMessage,
			String notificationType, boolean isCritical) {

		NotificationLog notification = new NotificationLog();

		// For Admin
		notification.setType(notificationType);
		notification.setAction(notificationMessage);
		notification.setUserProfileId(existingDelegation.getDelegateeId());
		notification.setUsername(delegateeUserName);
		notification.setForAdmin(true);
		notification.setCritical(isCritical);
		// notification.isViewedByUser(true);
		notificationDAO.save(notification);

		// For Delegator
		notification = new NotificationLog();
		notification.setType(notificationType);
		notification.setAction(notificationMessage);
		notification.setUserProfileId(userProfileID);
		notification.setUsername(userName);
		notification.setCollege(userCollege);
		notification.setDepartment(userDepartment);
		notification.setPositionType(userPositionType);
		notification.setPositionTitle(userPositionTitle);
		notification.setCritical(isCritical);
		// notification.isViewedByUser(true);
		notificationDAO.save(notification);

		// For Delegatee
		notification = new NotificationLog();
		notification.setType(notificationType);
		notification.setAction(notificationMessage);
		notification.setUserProfileId(existingDelegation.getDelegateeId());
		notification.setUsername(delegateeUserName);
		notification.setCollege(existingDelegation.getCollege());
		notification.setDepartment(existingDelegation.getDepartment());
		notification.setPositionType(existingDelegation.getPositionType());
		notification.setPositionTitle(existingDelegation.getPositionTitle());
		notification.setCritical(isCritical);
		// notification.isViewedByUser(true);
		notificationDAO.save(notification);

		// Broadcasting SSE

		OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		OutboundEvent event = eventBuilder.name("notification")
				.mediaType(MediaType.TEXT_PLAIN_TYPE).data(String.class, "1")
				.build();

		NotificationService.BROADCASTER.broadcast(event);
	}

	private boolean validateNotEmptyValue(String value) {
		if (!value.equalsIgnoreCase("")) {
			return true;
		} else {
			return false;
		}
	}

	@POST
	@Path("/RevokeDelegationByDelegationID")
	@ApiOperation(value = "Revoke Delegation by DelegationId", notes = "This API deletes Delegation by DelegationId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response revokeDelegationByDelegationID(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("DelegationService::revokeDelegationByDelegationID started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			StringBuffer contentProfile = new StringBuffer();

			String delegationId = new String();
			if (root != null && root.has("delegationId")) {
				delegationId = root.get("delegationId").textValue();
			}

			String userProfileID = new String();
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

			ObjectId id = new ObjectId(delegationId);
			Delegation existingDelegation = delegationDAO
					.findDelegationByDelegationID(id);

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);
			String authorFullName = authorProfile.getFullName();
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();

			String delegateeIdStr = existingDelegation.getDelegateeId();
			ObjectId delegateeId = new ObjectId(delegateeIdStr);
			UserProfile delegateeUserProfile = userProfileDAO
					.findUserDetailsByProfileID(delegateeId);
			String delegateeUserName = delegateeUserProfile.getUserAccount()
					.getUserName();
			String delegateeEmail = delegateeUserProfile.getWorkEmails().get(0);

			contentProfile.append("<Content>");
			contentProfile
					.append("<ak:record xmlns:ak=\"http://akpower.org\">");
			contentProfile.append("<ak:delegation>");

			contentProfile.append("<ak:delegationid>");
			contentProfile.append(delegationId);
			contentProfile.append("</ak:delegationid>");

			contentProfile.append("<ak:delegationfilename>");
			final String delegationFileName = existingDelegation
					.getDelegationFileName();
			contentProfile.append(delegationFileName);
			contentProfile.append("</ak:delegationfilename>");

			contentProfile.append("<ak:revoked>");
			contentProfile.append(existingDelegation.isRevoked());
			contentProfile.append("</ak:revoked>");

			contentProfile.append("<ak:delegator>");
			contentProfile.append("<ak:id>");
			contentProfile.append(authorProfile.getId().toString());
			contentProfile.append("</ak:id>");
			contentProfile.append("<ak:fullname>");
			contentProfile.append(authorFullName);
			contentProfile.append("</ak:fullname>");
			contentProfile.append("<ak:email>");
			contentProfile.append(authorProfile.getWorkEmails().get(0));
			contentProfile.append("</ak:email>");
			contentProfile.append("</ak:delegator>");

			contentProfile.append("<ak:delegatee>");
			contentProfile.append("<ak:id>");
			contentProfile.append(existingDelegation.getDelegateeId());
			contentProfile.append("</ak:id>");
			contentProfile.append("<ak:fullname>");
			contentProfile.append(existingDelegation.getDelegatee());
			contentProfile.append("</ak:fullname>");
			contentProfile.append("<ak:email>");
			contentProfile.append(delegateeEmail);
			contentProfile.append("</ak:email>");
			contentProfile.append("</ak:delegatee>");

			contentProfile.append("</ak:delegation>");
			contentProfile.append("</ak:record>");
			contentProfile.append("</Content>");

			contentProfile
					.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
			contentProfile
					.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:delegation</AttributeValue>");
			contentProfile.append("</Attribute>");

			Accesscontrol ac = new Accesscontrol();
			HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();
			Multimap<String, String> resourceMap = ArrayListMultimap.create();

			if (attrMap.get("Resource") == null) {
				attrMap.put("Resource", resourceMap);
			}

			Multimap<String, String> actionMap = ArrayListMultimap.create();

			actionMap.put("proposal.action", "Revoke");
			attrMap.put("Action", actionMap);

			Set<AbstractResult> set = ac.getXACMLdecisionWithObligations(
					attrMap, contentProfile);
			Iterator<AbstractResult> it = set.iterator();
			int intDecision = 3;
			while (it.hasNext()) {
				AbstractResult ar = it.next();
				intDecision = ar.getDecision();

				if (intDecision >= 4 && intDecision <= 6) {
					intDecision = 2;
				}
				System.out.println("Decision:" + intDecision + " that is: "
						+ AbstractResult.DECISIONS[intDecision]);

				if (AbstractResult.DECISIONS[intDecision].equals("Permit")) {
					List<ObligationResult> obligations = ar.getObligations();

					EmailUtil emailUtil = new EmailUtil();
					String emailSubject = new String();
					String emailBody = new String();
					String delegatorName = new String();
					String delegatorEmail = new String();
					String delegateeName = new String();
					List<String> emaillist = new ArrayList<String>();

					if (obligations.size() > 0) {
						List<ObligationResult> preObligations = new ArrayList<ObligationResult>();
						List<ObligationResult> postObligations = new ArrayList<ObligationResult>();
						List<ObligationResult> ongoingObligations = new ArrayList<ObligationResult>();

						for (ObligationResult obligation : obligations) {
							if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
								List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
										.getAssignments();

								String obligationType = "postobligation";

								for (AttributeAssignment assignment : assignments) {
									if (assignment.getAttributeId().toString()
											.equalsIgnoreCase("obligationType")) {
										obligationType = assignment
												.getContent();
										break;
									}
								}

								if (obligationType.equals("preobligation")) {
									preObligations.add(obligation);
									System.out.println(obligationType
											+ " is FOUND");
								} else if (obligationType
										.equals("postobligation")) {
									postObligations.add(obligation);
									System.out.println(obligationType
											+ " is FOUND");
								} else {
									ongoingObligations.add(obligation);
									System.out.println(obligationType
											+ " is FOUND");
								}

							}
						}

						Boolean preCondition = true;
						String alertMessage = new String();
						if (preObligations.size() != 0) {
							preCondition = false;

							System.out
									.println("\n======================== Printing Obligations ====================");

							for (ObligationResult obligation : preObligations) {
								if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
									List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
											.getAssignments();

									String obligationType = "preobligation";

									for (AttributeAssignment assignment : assignments) {

										// System.out.println("Obligation :  "
										// + assignment.getContent() +
										// "\n");

										switch (assignment.getAttributeId()
												.toString()) {
										// case "obligationType":
										// obligationType =
										// assignment.getContent();
										// break;

										case "signedByCurrentUser":
											preCondition = Boolean
													.parseBoolean(assignment
															.getContent());
											break;
										case "alertMessage":
											alertMessage = assignment
													.getContent();
											break;

										}
									}
									System.out.println(obligationType
											+ " is RUNNING");
									if (!preCondition) {
										break;
									}
								}
							}
						}

						if (preCondition) {
							for (ObligationResult obligation : postObligations) {
								if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
									List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
											.getAssignments();

									String obligationType = "postobligation";

									for (AttributeAssignment assignment : assignments) {

										// System.out.println("Obligation :  "
										// + assignment.getContent() +
										// "\n");

										switch (assignment.getAttributeId()
												.toString()) {
										// case "obligationType":
										// obligationType =
										// assignment.getContent();
										// break;
										case "emailSubject":
											emailSubject = assignment
													.getContent();
											break;
										case "emailBody":
											emailBody = assignment.getContent();
											break;
										case "delegatorName":
											delegatorName = assignment
													.getContent();
											break;
										case "delegatorEmail":
											delegatorEmail = assignment
													.getContent();
											break;
										case "delegateeName":
											delegateeName = assignment
													.getContent();
											break;
										case "delegateeEmail":
											if (!assignment.getContent()
													.equals("")) {
												emaillist.add(assignment
														.getContent());
											}
											break;
										}
									}

									System.out.println(obligationType
											+ " is RUNNING");
								}
							}
						} else {
							return Response.status(403)
									.type(MediaType.APPLICATION_JSON)
									.entity(alertMessage).build();
						}
					}

					boolean isRevoked = delegationDAO.revokeDelegation(
							existingDelegation, authorProfile);

					if (isRevoked) {
						// Update the current Delegator bit to false
						authorProfile.setDelegator(false);
						userProfileDAO.save(authorProfile);

						// Delete the Delegation Dynamic Policy File here
						try {
							policyLocation = this.getClass()
									.getResource("/policy").toURI().getPath();
							File file = new File(policyLocation + "/"
									+ delegationFileName);
							if (!file.exists()) {
								return Response
										.status(Response.Status.NOT_FOUND)
										.entity("FILE NOT FOUND: "
												+ delegationFileName)
										.type("text/plain").build();
							}
							if (file.delete()) {
								System.out.println(file.getName()
										+ " is deleted!");
							} else {
								return Response.status(403)
										.type(MediaType.APPLICATION_JSON)
										.entity("Delete operation is failed.")
										.build();
							}

						} catch (Exception e) {
							return Response
									.status(403)
									.type(MediaType.APPLICATION_JSON)
									.entity("File delete permission is not enabled!")
									.build();

						}
						if (!emailSubject.equals("")) {
							emailUtil.sendMailMultipleUsersWithoutAuth(
									delegatorEmail, emaillist, emailSubject
											+ delegatorName, emailBody);
						}

						String notificationMessage = "Delegation Revoked by "
								+ authorUserName + ".";

						sendNotification(existingDelegation, delegateeUserName,
								userProfileID, userName, userCollege,
								userDepartment, userPositionType,
								userPositionTitle, notificationMessage,
								"Delegation", true);

						return Response
								.status(200)
								.type(MediaType.APPLICATION_JSON)
								.entity(mapper.writerWithDefaultPrettyPrinter()
										.writeValueAsString(true)).build();
						// return
						// Response.status(200).entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(true)).build();
					} else {
						return Response
								.status(200)
								.type(MediaType.APPLICATION_JSON)
								.entity(mapper.writerWithDefaultPrettyPrinter()
										.writeValueAsString(true)).build();
					}
				} else {
					return Response
							.status(403)
							.type(MediaType.APPLICATION_JSON)
							.entity("Your permission is: "
									+ AbstractResult.DECISIONS[intDecision])
							.build();
				}
			}
		} catch (Exception e) {
			log.error("Could not revoke the selected Delegation error e=", e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"Could not revoke the selected Delegation\", \"status\": \"FAIL\"}")
				.build();
	}

}
