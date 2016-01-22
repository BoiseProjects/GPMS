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
import gpms.model.PasswordHash;
import gpms.model.PositionDetails;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
import gpms.model.UserProfile;
import gpms.utils.MultimapAdapter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
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

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Morphia;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

@Path("/users")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.APPLICATION_FORM_URLENCODED })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN })
public class UserService {
	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;
	NotificationDAO notificationDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

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
	public String returnString() {
		return "Hello World!";
	}

	@POST
	@Path("/GetUserPositionDetailsForAProposal")
	public String getUserPositionDetailsForAProposal(String message)
			throws UnknownHostException, JsonProcessingException, IOException {
		String profileIds = new String();
		String profiles[] = new String[0];
		List<ObjectId> userIds = new ArrayList<ObjectId>();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(message);
		if (root != null && root.has("userIds")) {
			profileIds = root.get("userIds").getTextValue();
			profiles = profileIds.split(", ");
		}

		for (String profile : profiles) {
			ObjectId id = new ObjectId(profile);
			userIds.add(id);
		}
		final MultimapAdapter multimapAdapter = new MultimapAdapter();
		final Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(Multimap.class, multimapAdapter).create();

		final String userPositions = gson.toJson(userProfileDAO
				.findUserPositionDetailsForAProposal(userIds));
		return userPositions;
	}

	@POST
	@Path("/GetUsersList")
	public List<UserInfo> produceUsersJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException {
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
			offset = root.get("offset").getIntValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").getIntValue();
		}

		JsonNode userObj = root.get("userBindObj");
		if (userObj != null && userObj.has("UserName")) {
			userName = userObj.get("UserName").getTextValue();
		}

		if (userObj != null && userObj.has("College")) {
			college = userObj.get("College").getTextValue();
		}

		if (userObj != null && userObj.has("Department")) {
			department = userObj.get("Department").getTextValue();
		}

		if (userObj != null && userObj.has("PositionType")) {
			positionType = userObj.get("PositionType").getTextValue();
		}

		if (userObj != null && userObj.has("PositionTitle")) {
			positionTitle = userObj.get("PositionTitle").getTextValue();
		}

		if (userObj != null && userObj.has("IsActive")) {
			if (!userObj.get("IsActive").isNull()) {
				isActive = userObj.get("IsActive").getBooleanValue();
			} else {
				isActive = null;
			}
		}

		users = userProfileDAO.findAllForUserGrid(offset, limit, userName,
				college, department, positionType, positionTitle, isActive);
		return users;
	}

	@POST
	@Path("/GetUserDetailsByProfileId")
	public String produceUserDetailsByProfileId(String message)
			throws JsonProcessingException, IOException {
		UserProfile user = new UserProfile();
		String response = new String();
		String profileId = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
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

		ObjectId id = new ObjectId(profileId);

		// System.out.println("Profile ID String: " + profileId
		// + ", Profile ID with ObjectId: " + id + ", User Name: "
		// + userName + ", User Profile ID: " + userProfileID
		// + ", Culture Name: " + cultureName);

		user = userProfileDAO.findUserDetailsByProfileID(id);

		// Gson gson = new Gson();
		// .setDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz").create();
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
				.create();
		response = gson.toJson(user, UserProfile.class);

		// response = gson.toJson(user);

		// response =
		// mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
		// user);

		return response;
	}

	@POST
	@Path("/GetUserAuditLogList")
	public List<AuditLogInfo> produceUserAuditLogJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException {
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
			offset = root.get("offset").getIntValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").getIntValue();
		}

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		JsonNode auditLogBindObj = root.get("auditLogBindObj");
		if (auditLogBindObj != null && auditLogBindObj.has("Action")) {
			action = auditLogBindObj.get("Action").getTextValue();
		}

		if (auditLogBindObj != null && auditLogBindObj.has("AuditedBy")) {
			auditedBy = auditLogBindObj.get("AuditedBy").getTextValue();
		}

		if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnFrom")) {
			activityOnFrom = auditLogBindObj.get("ActivityOnFrom")
					.getTextValue();
		}

		if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnTo")) {
			activityOnTo = auditLogBindObj.get("ActivityOnTo").getTextValue();
		}

		ObjectId userId = new ObjectId(profileId);

		userAuditLogs = userProfileDAO.findAllForUserAuditLogGrid(offset,
				limit, userId, action, auditedBy, activityOnFrom, activityOnTo);

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);

		return userAuditLogs;
	}

	@POST
	@Path("/GetPositionDetailsHash")
	public HashMap<String, HashMap<String, HashMap<String, ArrayList<String>>>> producePositionDetailsHash()
			throws JsonProcessingException, IOException {
		DepartmentsPositionsCollection dpc = new DepartmentsPositionsCollection();
		return dpc.getAvailableDepartmentsAndPositions();
	}

	@POST
	@Path("/DeleteUserByUserID")
	public String deleteUserByUserID(String message)
			throws JsonProcessingException, IOException {
		String response = new String();
		String profileId = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		ObjectId id = new ObjectId(profileId);
		UserProfile userProfile = userProfileDAO.findUserDetailsByProfileID(id);
		// userProfileDAO.deleteUserProfileByUserID(userProfile, authorProfile);

		userProfile.setDeleted(true);
		userProfileDAO.save(userProfile);

		UserAccount userAccount = userAccountDAO.findByID(userProfile
				.getUserAccount().getId());
		userAccountDAO.deleteUserAccountByUserID(userAccount, authorProfile);

		NotificationLog notification = new NotificationLog();
		notification.setType("User");
		notification.setAction("Deleted user account and profile of "
				+ userAccount.getUserName());
		// notification.setUserProfileId(userProfile.getId().toString());
		// notification.isViewedByUser(true);
		notificationDAO.save(notification);

		// response.setContentType("text/html;charset=UTF-8");
		// response.getWriter().write("Success Data");

		// Gson gson = new GsonBuilder().setPrettyPrinting().create();
		// response = gson.toJson("Success", String.class);

		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;
	}

	@POST
	@Path("/DeleteMultipleUsersByUserID")
	public String deleteMultipleUsersByUserID(String message)
			throws JsonProcessingException, IOException {
		String response = new String();

		String profileIds = new String();
		String profiles[] = new String[0];

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userIds")) {
			profileIds = root.get("userIds").getTextValue();
			profiles = profileIds.split(",");
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		for (String profile : profiles) {
			ObjectId id = new ObjectId(profile);
			UserProfile userProfile = userProfileDAO
					.findUserDetailsByProfileID(id);
			// userProfileDAO.deleteUserProfileByUserID(userProfile,
			// authorProfile);
			userProfile.setDeleted(true);
			userProfileDAO.save(userProfile);

			UserAccount userAccount = userAccountDAO.findByID(userProfile
					.getUserAccount().getId());
			userAccountDAO
					.deleteUserAccountByUserID(userAccount, authorProfile);

			NotificationLog notification = new NotificationLog();
			notification.setType("User");
			notification.setAction("Deleted user account and profile of "
					+ userAccount.getUserName());
			// notification.setUserProfileId(userProfile.getId().toString());
			// notification.isViewedByUser(true);
			notificationDAO.save(notification);
		}
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;
	}

	@POST
	@Path("/UpdateUserIsActiveByUserID")
	public String updateUserIsActiveByUserID(String message)
			throws JsonProcessingException, IOException {
		String response = new String();
		String profileId = new String();
		Boolean isActive = true;

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		if (root != null && root.has("isActive")) {
			isActive = root.get("isActive").getBooleanValue();
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		ObjectId id = new ObjectId(profileId);
		UserProfile userProfile = userProfileDAO.findUserDetailsByProfileID(id);

		userProfile.setDeleted(!isActive);
		userProfileDAO.save(userProfile);

		UserAccount userAccount = userProfile.getUserAccount();
		userAccountDAO.activateUserAccountByUserID(userAccount, authorProfile,
				isActive);

		NotificationLog notification = new NotificationLog();

		String notificationMessage = new String();
		if (isActive) {
			notificationMessage = "Activated user account and profile of "
					+ userAccount.getUserName();
		} else {
			notificationMessage = "Deactivated user account and profile of "
					+ userAccount.getUserName();
		}

		// To Admin
		notification.setType("User");
		notificationDAO.save(notification);

		// To All User Roles based on positions
		if (isActive) {
			notificationMessage = "Activated user account and profile of "
					+ userAccount.getUserName();

			for (PositionDetails positions : userProfile.getDetails()) {
				notification = new NotificationLog();
				notification.setType("User");
				notification.setUserProfileId(userProfile.getId().toString());
				notification.setCollege(positions.getCollege());
				notification.setDepartment(positions.getDepartment());
				notification.setPositionType(positions.getPositionType());
				notification.setPositionTitle(positions.getPositionTitle());
				notificationDAO.save(notification);
			}

		} else {
			notificationMessage = "Deactivated user account and profile of "
					+ userAccount.getUserName();
		}

		// return Response.ok("Success", MediaType.APPLICATION_JSON).build();

		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;
	}

	@POST
	@Path("/CheckUniqueUserName")
	public String checkUniqueUserName(String message)
			throws JsonProcessingException, IOException {
		String userID = new String();
		String newUserName = new String();
		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode userUniqueObj = root.get("userUniqueObj");
		if (userUniqueObj != null && userUniqueObj.has("UserID")) {
			userID = userUniqueObj.get("UserID").getTextValue();
		}

		if (userUniqueObj != null && userUniqueObj.has("NewUserName")) {
			newUserName = userUniqueObj.get("NewUserName").getTextValue();
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
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
		return response;
	}

	@POST
	@Path("/CheckUniqueEmail")
	public String checkUniqueEmail(String message)
			throws JsonProcessingException, IOException {
		String userID = new String();
		String newEmail = new String();
		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode userUniqueObj = root.get("userUniqueObj");
		if (userUniqueObj != null && userUniqueObj.has("UserID")) {
			userID = userUniqueObj.get("UserID").getTextValue();
		}

		if (userUniqueObj != null && userUniqueObj.has("NewEmail")) {
			newEmail = userUniqueObj.get("NewEmail").getTextValue();
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
		}

		ObjectId id = new ObjectId();
		UserProfile userProfile = new UserProfile();
		if (!userID.equals("0")) {
			id = new ObjectId(userID);
			userProfile = userProfileDAO
					.findNextUserWithSameEmail(id, newEmail);
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
		return response;
	}

	@POST
	@Path("/SaveUpdateUser")
	public String saveUpdateUser(String message) throws Exception {
		String userID = new String();
		UserAccount newAccount = new UserAccount();
		UserProfile newProfile = new UserProfile();

		UserAccount existingUserAccount = new UserAccount();
		UserProfile existingUserProfile = new UserProfile();
		UserProfile oldUserProfile = new UserProfile();

		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode userInfo = root.get("userInfo");

		if (userInfo != null && userInfo.has("UserID")) {
			userID = userInfo.get("UserID").getTextValue();
			if (!userID.equals("0")) {
				ObjectId id = new ObjectId(userID);
				existingUserProfile = userProfileDAO
						.findUserDetailsByProfileID(id);
				oldUserProfile = cloneThroughSerialize(existingUserProfile);
			} else {
				newAccount.setAddedOn(new Date());
			}
		}

		if (userInfo != null && userInfo.has("UserName")) {
			String userNameOf = userInfo.get("UserName").getTextValue();
			if (!userID.equals("0") && existingUserProfile != null) {
				existingUserAccount = existingUserProfile.getUserAccount();
				if (!existingUserAccount.getUserName().equals(userNameOf)) {
					existingUserAccount = null;
				}
			} else {
				newAccount.setUserName(userNameOf);
			}
		}

		if (userInfo != null && userInfo.has("Password")) {
			if (!userID.equals("0")) {
				if (!existingUserAccount.getPassword().equals(
						userInfo.get("Password").getTextValue())) {
					existingUserAccount.setPassword(userInfo.get("Password")
							.getTextValue());
				}
			} else {
				newAccount.setPassword(userInfo.get("Password").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("IsActive")) {
			if (!userID.equals("0")) {
				if (existingUserAccount.isActive() != userInfo.get("IsActive")
						.getBooleanValue()) {
					existingUserAccount.setActive(userInfo.get("IsActive")
							.getBooleanValue());
				}
			} else {
				newAccount
						.setActive(userInfo.get("IsActive").getBooleanValue());
			}
			if (!userID.equals("0")) {
				if (existingUserAccount.isDeleted() != !userInfo
						.get("IsActive").getBooleanValue()) {
					existingUserAccount.setDeleted(!userInfo.get("IsActive")
							.getBooleanValue());
				}
			} else {
				newAccount.setDeleted(!userInfo.get("IsActive")
						.getBooleanValue());
			}

			// TODO: Check the old ways to do this
			// if (userInfo != null && userInfo.has("IsActive")) {
			// newAccount.setActive(Boolean.parseBoolean(userInfo.get(
			// "IsActive").getTextValue()));
			// newAccount.setDeleted(!Boolean.parseBoolean(userInfo.get(
			// "IsActive").getTextValue()));
			// newProfile.setDeleted(!Boolean.parseBoolean(userInfo.get(
			// "IsActive").getTextValue()));
			// }

			if (!userID.equals("0")) {
				if (existingUserProfile.isDeleted() != !userInfo
						.get("IsActive").getBooleanValue()) {
					existingUserProfile.setDeleted(!userInfo.get("IsActive")
							.getBooleanValue());
				}
			} else {
				newProfile.setDeleted(!userInfo.get("IsActive")
						.getBooleanValue());
			}
		}

		if (userID.equals("0")) {
			newProfile.setUserAccount(newAccount);
		}

		if (userInfo != null && userInfo.has("FirstName")) {
			if (!userID.equals("0")) {
				if (!existingUserProfile.getFirstName().equals(
						userInfo.get("FirstName").getTextValue())) {
					existingUserProfile.setFirstName(userInfo.get("FirstName")
							.getTextValue());
				}
			} else {
				newProfile.setFirstName(userInfo.get("FirstName")
						.getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("MiddleName")) {
			if (!userID.equals("0")) {
				if (!existingUserProfile.getMiddleName().equals(
						userInfo.get("MiddleName").getTextValue())) {
					existingUserProfile.setMiddleName(userInfo
							.get("MiddleName").getTextValue());
				}
			} else {
				newProfile.setMiddleName(userInfo.get("MiddleName")
						.getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("LastName")) {
			if (!userID.equals("0")) {
				if (!existingUserProfile.getLastName().equals(
						userInfo.get("LastName").getTextValue())) {
					existingUserProfile.setLastName(userInfo.get("LastName")
							.getTextValue());
				}
			} else {
				newProfile.setLastName(userInfo.get("LastName").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("DOB")) {
			Date dob = formatter.parse(userInfo.get("DOB").getTextValue());
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
						userInfo.get("Gender").getTextValue())) {
					existingUserProfile.setGender(userInfo.get("Gender")
							.getTextValue());
				}
			} else {
				newProfile.setGender(userInfo.get("Gender").getTextValue());
			}
		}

		Address newAddress = new Address();

		if (userInfo != null && userInfo.has("Street")) {
			newAddress.setStreet(userInfo.get("Street").getTextValue());
		}
		if (userInfo != null && userInfo.has("Apt")) {
			newAddress.setApt(userInfo.get("Apt").getTextValue());
		}
		if (userInfo != null && userInfo.has("City")) {
			newAddress.setCity(userInfo.get("City").getTextValue());
		}
		if (userInfo != null && userInfo.has("State")) {
			newAddress.setState(userInfo.get("State").getTextValue());
		}
		if (userInfo != null && userInfo.has("Zip")) {
			newAddress.setZipcode(userInfo.get("Zip").getTextValue());
		}
		if (userInfo != null && userInfo.has("Country")) {
			newAddress.setCountry(userInfo.get("Country").getTextValue());
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
				for (String officeNo : existingUserProfile.getOfficeNumbers()) {
					if (officeNo.equals(userInfo.get("OfficeNumber")
							.getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getOfficeNumbers().clear();
					existingUserProfile.getOfficeNumbers().add(
							userInfo.get("OfficeNumber").getTextValue());
				}
			} else {
				newProfile.getOfficeNumbers().add(
						userInfo.get("OfficeNumber").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("MobileNumber")) {
			if (!userID.equals("0")) {
				boolean alreadyExist = false;
				for (String mobileNo : existingUserProfile.getMobileNumbers()) {
					if (mobileNo.equals(userInfo.get("MobileNumber")
							.getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getMobileNumbers().clear();
					existingUserProfile.getMobileNumbers().add(
							userInfo.get("MobileNumber").getTextValue());
				}
			} else {
				newProfile.getMobileNumbers().add(
						userInfo.get("MobileNumber").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("HomeNumber")) {
			if (!userID.equals("0")) {
				boolean alreadyExist = false;
				for (String homeNo : existingUserProfile.getHomeNumbers()) {
					if (homeNo
							.equals(userInfo.get("HomeNumber").getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getHomeNumbers().clear();
					existingUserProfile.getHomeNumbers().add(
							userInfo.get("HomeNumber").getTextValue());
				}
			} else {
				newProfile.getHomeNumbers().add(
						userInfo.get("HomeNumber").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("OtherNumber")) {
			if (!userID.equals("0")) {
				boolean alreadyExist = false;
				for (String otherNo : existingUserProfile.getOtherNumbers()) {
					if (otherNo.equals(userInfo.get("OtherNumber")
							.getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getOtherNumbers().clear();
					existingUserProfile.getOtherNumbers().add(
							userInfo.get("OtherNumber").getTextValue());
				}
			} else {
				newProfile.getOtherNumbers().add(
						userInfo.get("OtherNumber").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("WorkEmail")) {
			if (!userID.equals("0")) {
				boolean alreadyExist = false;
				for (String workEmail : existingUserProfile.getWorkEmails()) {
					if (workEmail.equals(userInfo.get("WorkEmail")
							.getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getWorkEmails().clear();
					existingUserProfile.getWorkEmails().add(
							userInfo.get("WorkEmail").getTextValue());
				}
			} else {
				newProfile.getWorkEmails().add(
						userInfo.get("WorkEmail").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("PersonalEmail")) {
			if (!userID.equals("0")) {
				boolean alreadyExist = false;
				for (String personalEmail : existingUserProfile
						.getPersonalEmails()) {
					if (personalEmail.equals(userInfo.get("PersonalEmail")
							.getTextValue())) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					existingUserProfile.getPersonalEmails().clear();
					existingUserProfile.getPersonalEmails().add(
							userInfo.get("PersonalEmail").getTextValue());
				}
			} else {
				newProfile.getPersonalEmails().add(
						userInfo.get("PersonalEmail").getTextValue());
			}
		}

		if (userInfo != null && userInfo.has("SaveOptions")) {
			if (!userID.equals("0")) {
				existingUserProfile.getDetails().clear();
			}

			String[] rows = userInfo.get("SaveOptions").getTextValue()
					.split("#!#");

			for (String col : rows) {
				String[] cols = col.split("!#!");
				PositionDetails newDetails = new PositionDetails();
				newDetails.setCollege(cols[0]);
				newDetails.setDepartment(cols[1]);
				newDetails.setPositionType(cols[2]);
				newDetails.setPositionTitle(cols[3]);
				newDetails.setDefault(Boolean.parseBoolean(cols[4]));
				if (!userID.equals("0")) {
					existingUserProfile.getDetails().add(newDetails);
				} else {
					newProfile.getDetails().add(newDetails);
				}
			}
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = commonObj.get("UserIsAdmin").getBooleanValue();
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").getTextValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle")
					.getTextValue();
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		// Save the User Account
		if (!userID.equals("0")) {
			userAccountDAO.save(existingUserAccount);
		} else {
			userAccountDAO.save(newAccount);
		}

		// Save the User Profile
		NotificationLog notification = new NotificationLog();
		if (!userID.equals("0")) {
			if (!oldUserProfile.equals(existingUserProfile)) {
				userProfileDAO.updateUser(existingUserProfile, authorProfile);

				// For Admin
				notification = new NotificationLog();
				notification.setType("User");
				notification.setAction("Updated user account and profile of "
						+ existingUserProfile.getUserAccount().getUserName());
				notificationDAO.save(notification);

				// For all Roles of the User
				for (PositionDetails positions : existingUserProfile
						.getDetails()) {
					notification = new NotificationLog();
					notification.setType("User");
					notification
							.setAction("Updated user account and profile of "
									+ existingUserProfile.getUserAccount()
											.getUserName());

					notification.setUserProfileId(existingUserProfile.getId()
							.toString());
					notification.setCollege(positions.getCollege());
					notification.setDepartment(positions.getDepartment());
					notification.setPositionType(positions.getPositionType());
					notification.setPositionTitle(positions.getPositionTitle());
					notificationDAO.save(notification);
				}
			}
		} else {
			userProfileDAO.saveUser(newProfile, authorProfile);

			// For Admin
			notification = new NotificationLog();
			notification.setType("User");
			notification.setAction("Created user account and profile of "
					+ newProfile.getUserAccount().getUserName());
			notificationDAO.save(notification);

			// For Roles of the user notify
			for (PositionDetails positions : newProfile.getDetails()) {
				notification = new NotificationLog();
				notification.setType("User");
				notification.setAction("Created user account and profile of "
						+ newProfile.getUserAccount().getUserName());
				notification.setUserProfileId(newProfile.getId().toString());
				notification.setCollege(positions.getCollege());
				notification.setDepartment(positions.getDepartment());
				notification.setPositionType(positions.getPositionType());
				notification.setPositionTitle(positions.getPositionTitle());
				notificationDAO.save(notification);
			}
		}

		// UserProfile user = userProfileDAO.findByUserAccount(newAccount);
		// System.out.println(user);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;

	}

	@POST
	@Path("/signup")
	public String signUpUser(String message) throws JsonProcessingException,
			IOException, ParseException {

		String userID = new String();

		UserAccount newAccount = new UserAccount();
		UserProfile newProfile = new UserProfile();

		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode userInfo = root.get("userInfo");

		if (userInfo != null && userInfo.has("UserID")) {
			userID = userInfo.get("UserID").getTextValue();
		}

		if (userID.equals("0")) {

			newAccount.setAddedOn(new Date());

			if (userInfo != null && userInfo.has("UserName")) {
				String loginUserName = userInfo.get("UserName").getTextValue();
				newAccount.setUserName(loginUserName);
			}

			if (userInfo != null && userInfo.has("Password")) {
				newAccount.setPassword(userInfo.get("Password").getTextValue());
			}

			newProfile.setUserAccount(newAccount);

			if (userInfo != null && userInfo.has("FirstName")) {
				newProfile.setFirstName(userInfo.get("FirstName")
						.getTextValue());
			}

			if (userInfo != null && userInfo.has("MiddleName")) {
				newProfile.setMiddleName(userInfo.get("MiddleName")
						.getTextValue());
			}

			if (userInfo != null && userInfo.has("LastName")) {
				newProfile.setLastName(userInfo.get("LastName").getTextValue());
			}

			if (userInfo != null && userInfo.has("DOB")) {
				Date dob = formatter.parse(userInfo.get("DOB").getTextValue());
				newProfile.setDateOfBirth(dob);
			}

			if (userInfo != null && userInfo.has("Gender")) {
				newProfile.setGender(userInfo.get("Gender").getTextValue());
			}

			Address newAddress = new Address();

			if (userInfo != null && userInfo.has("Street")) {
				newAddress.setStreet(userInfo.get("Street").getTextValue());
			}
			if (userInfo != null && userInfo.has("Apt")) {
				newAddress.setApt(userInfo.get("Apt").getTextValue());
			}
			if (userInfo != null && userInfo.has("City")) {
				newAddress.setCity(userInfo.get("City").getTextValue());
			}
			if (userInfo != null && userInfo.has("State")) {
				newAddress.setState(userInfo.get("State").getTextValue());
			}
			if (userInfo != null && userInfo.has("Zip")) {
				newAddress.setZipcode(userInfo.get("Zip").getTextValue());
			}
			if (userInfo != null && userInfo.has("Country")) {
				newAddress.setCountry(userInfo.get("Country").getTextValue());
			}

			newProfile.getAddresses().add(newAddress);

			if (userInfo != null && userInfo.has("MobileNumber")) {
				newProfile.getMobileNumbers().add(
						userInfo.get("MobileNumber").getTextValue());
			}

			if (userInfo != null && userInfo.has("WorkEmail")) {
				newProfile.getWorkEmails().add(
						userInfo.get("WorkEmail").getTextValue());
			}

			// Save the User Account
			userAccountDAO.save(newAccount);

			// Save the User Profile
			userProfileDAO.save(newProfile);

			NotificationLog notification = new NotificationLog();
			notification.setType("User");
			notification.setAction("Signed Up by " + newAccount.getUserName());
			// notification.setUserProfileId(newProfile.getId().toString());
			// notification.setViewedByUser(true);
			notificationDAO.save(notification);
		}

		// UserProfile user = userProfileDAO.findByUserAccount(newAccount);
		// System.out.println(user);
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;

	}

	@POST
	@Path("/login")
	public Response login(@FormParam("username") String email,
			@FormParam("password") String password,
			@Context HttpServletRequest req) {
		try {
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
			System.out.println("error");
		}
		// return
		// Response.status(403).type("text/plain").entity(message).build();
		return null;
	}

	@POST
	@Path("/SetUserViewSession")
	public void setUserViewSession(@Context HttpServletRequest req,
			String message) throws JsonGenerationException,
			JsonMappingException, IOException {

		deleteAllSession(req);

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId") && root.has("userName")
				&& root.has("isAdminUser")) {
			String profileId = root.get("userId").getTextValue();
			ObjectId id = new ObjectId(profileId);

			String userName = new String();
			Boolean isAdminUser = false;
			String college = new String();
			String department = new String();
			String positionType = new String();
			String positionTitle = new String();
			userName = root.get("userName").getTextValue();
			isAdminUser = root.get("isAdminUser").getBooleanValue();

			if (root != null && root.has("college")) {
				college = root.get("college").getTextValue();
			}

			if (root != null && root.has("department")) {
				department = root.get("department").getTextValue();
			}

			if (root != null && root.has("positionType")) {
				positionType = root.get("positionType").getTextValue();
			}

			if (root != null && root.has("positionTitle")) {
				positionTitle = root.get("positionTitle").getTextValue();
			}

			UserProfile user = userProfileDAO.findMatchedUserDetails(id,
					userName, isAdminUser, college, department, positionType,
					positionTitle);
			if (user != null) {
				setUserCurrentSession(req, userName, isAdminUser, profileId,
						college, department, positionType, positionTitle);
			}
		}
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
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logout(@Context HttpServletRequest req)
			throws URISyntaxException {
		if (req == null) {
			System.out.println("Null request in context");
		}
		deleteAllSession(req);
		return Response.seeOther(new java.net.URI("../Login.jsp")).build();
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
				if (userDetails.isDefault()) {
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

		if (session.getAttribute("userPositionType") == null) {
			return (String) session.getAttribute("userPositionType");
		}

		if (session.getAttribute("userPositionTitle") == null) {
			return (String) session.getAttribute("userPositionTitle");
		}

		if (session.getAttribute("userDepartment") == null) {
			return (String) session.getAttribute("userDepartment");
		}

		if (session.getAttribute("userCollege") == null) {
			return (String) session.getAttribute("userCollege");
		}

		if (session.getAttribute("isAdmin") == null) {
			return (String) session.getAttribute("isAdmin");
		}
		return null;
	}

	@POST
	@Path("/GetAllUserDropdown")
	public HashMap<String, String> getAllUsers() throws UnknownHostException {
		HashMap<String, String> users = new HashMap<String, String>();
		// List<UserProfile> userprofiles = userProfileDAO.findAllActiveUsers();
		List<UserProfile> userprofiles = userProfileDAO
				.findAllUsersWithPosition();
		for (UserProfile userProfile : userprofiles) {
			users.put(userProfile.getId().toString(), userProfile.getFullName());
		}
		return users;
	}

	@POST
	@Path("/GetAllPositionDetailsForAUser")
	public String getAllPositionDetailsForAUser(String message)
			throws UnknownHostException, JsonProcessingException, IOException {
		String userId = new String();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(message);
		if (root != null && root.has("userId")) {
			userId = root.get("userId").getTextValue();
		}

		ObjectId id = new ObjectId(userId);

		final MultimapAdapter multimapAdapter = new MultimapAdapter();
		final Gson gson = new GsonBuilder().setPrettyPrinting()
				.registerTypeAdapter(Multimap.class, multimapAdapter).create();

		final String userPositions = gson.toJson(userProfileDAO
				.findAllPositionDetailsForAUser(id));
		return userPositions;
	}

	@SuppressWarnings("unchecked")
	public static <T> T cloneThroughSerialize(T t) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		serializeToOutputStream((Serializable) t, bos);
		byte[] bytes = bos.toByteArray();
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
				bytes));
		return (T) ois.readObject();
	}

	private static void serializeToOutputStream(Serializable ser,
			OutputStream os) throws IOException {
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(os);
			oos.writeObject(ser);
			oos.flush();
		} finally {
			oos.close();
		}
	}
}
