package gpms.rest;

import gpms.DAL.DepartmentsPositionsCollection;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.AuditLogInfo;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
import gpms.model.UserProfile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Morphia;

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

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public UserService() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String returnString() {
		return "Hello World!";
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

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);

		return users;
	}

	@POST
	@Path("/GetUserDetailsByProfileId")
	public String produceUserDetailsByProfileId(String message)
			throws JsonProcessingException, IOException {
		UserProfile user = new UserProfile();
		String response = new String();

		String profileId = new String();
		// String userName = new String();
		// String userProfileID = new String();
		// String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		// JsonNode commonObj = root.get("gpmsCommonObj");
		// if (commonObj != null && commonObj.has("UserName")) {
		// userName = commonObj.get("UserName").getTextValue();
		// }
		//
		// if (commonObj != null && commonObj.has("UserProfileID")) {
		// userProfileID = commonObj.get("UserProfileID").getTextValue();
		// }
		//
		// if (commonObj != null && commonObj.has("CultureName")) {
		// cultureName = commonObj.get("CultureName").getTextValue();
		// }

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

		// GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		// gpmsCommonObj.setUserName(userName);
		// gpmsCommonObj.setUserProfileID(userProfileID);
		// gpmsCommonObj.setCultureName(cultureName);

		user = userProfileDAO.findUserDetailsByProfileID(id);
		// user.getUserAccount();
		// user.getUserAccount().getUserName();
		// user.getUserAccount().getPassword();

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
}
