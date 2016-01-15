package gpms.rest;

import gpms.DAL.DepartmentsPositionsCollection;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.Address;
import gpms.model.AuditLogInfo;
import gpms.model.GPMSCommonInfo;
import gpms.model.PasswordHash;
import gpms.model.PositionDetails;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
import gpms.model.UserProfile;
import gpms.utils.MultimapAdapter;

import java.io.IOException;
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

	@POST
	@Path("/DeleteUserByUserID")
	public String deleteUserByUserID(String message)
			throws JsonProcessingException, IOException {
		UserProfile user = new UserProfile();
		String response = new String();

		String profileId = new String();
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
		}

		// TODO : login set this session value
		// FOr Testing I am using HardCoded UserProfileID
		// userProfileID = "55b9225454ffd82dc052a32a";

		ObjectId id = new ObjectId(profileId);
		ObjectId authorId = new ObjectId(userProfileID);

		GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		gpmsCommonObj.setUserName(userName);
		gpmsCommonObj.setUserProfileID(userProfileID);
		gpmsCommonObj.setCultureName(cultureName);

		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		UserProfile userProfile = userProfileDAO.findUserDetailsByProfileID(id);
		// userProfileDAO.deleteUserProfileByUserID(userProfile, authorProfile,
		// gpmsCommonObj);

		userProfile.setDeleted(true);
		userProfileDAO.save(userProfile);

		UserAccount userAccount = userAccountDAO.findByID(userProfile
				.getUserAccount().getId());
		userAccountDAO.deleteUserAccountByUserID(userAccount, authorProfile,
				gpmsCommonObj);

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
		UserProfile user = new UserProfile();
		String response = new String();

		String profileIds = new String();
		String profiles[] = new String[0];
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userIds")) {
			profileIds = root.get("userIds").getTextValue();
			profiles = profileIds.split(",");
		}

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
		}

		ObjectId authorId = new ObjectId(userProfileID);

		GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		gpmsCommonObj.setUserName(userName);
		gpmsCommonObj.setUserProfileID(userProfileID);
		gpmsCommonObj.setCultureName(cultureName);

		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		for (String profile : profiles) {
			ObjectId id = new ObjectId(profile);

			UserProfile userProfile = userProfileDAO
					.findUserDetailsByProfileID(id);
			// userProfileDAO.deleteUserProfileByUserID(userProfile,
			// authorProfile, gpmsCommonObj);
			userProfile.setDeleted(true);
			userProfileDAO.save(userProfile);

			UserAccount userAccount = userAccountDAO.findByID(userProfile
					.getUserAccount().getId());
			userAccountDAO.deleteUserAccountByUserID(userAccount,
					authorProfile, gpmsCommonObj);
		}
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;
	}

	@POST
	@Path("/UpdateUserIsActiveByUserID")
	public String updateUserIsActiveByUserID(String message)
			throws JsonProcessingException, IOException {
		UserProfile user = new UserProfile();
		String response = new String();

		String profileId = new String();
		Boolean isActive = true;
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("userId")) {
			profileId = root.get("userId").getTextValue();
		}

		if (root != null && root.has("isActive")) {
			isActive = root.get("isActive").getBooleanValue();
		}

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
		}

		ObjectId id = new ObjectId(profileId);
		ObjectId authorId = new ObjectId(userProfileID);

		GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		gpmsCommonObj.setUserName(userName);
		gpmsCommonObj.setUserProfileID(userProfileID);
		gpmsCommonObj.setCultureName(cultureName);

		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		UserProfile userProfile = userProfileDAO.findUserDetailsByProfileID(id);

		// userProfileDAO.activateUserProfileByUserID(userProfile,
		// authorProfile,
		// gpmsCommonObj, isActive);

		userProfile.setDeleted(!isActive);
		userProfileDAO.save(userProfile);

		UserAccount userAccount = userProfile.getUserAccount();
		userAccountDAO.activateUserAccountByUserID(userAccount, authorProfile,
				gpmsCommonObj, isActive);
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
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

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

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
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
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

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

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
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
	public String saveUpdateUser(String message)
			throws JsonProcessingException, IOException, ParseException {

		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		String userID = new String();

		UserAccount newAccount = new UserAccount();
		UserProfile newProfile = new UserProfile();

		UserAccount existingUserAccount = new UserAccount();
		UserProfile existingUserProfile = new UserProfile();

		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").getTextValue();
		}

		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").getTextValue();
		}

		if (commonObj != null && commonObj.has("CultureName")) {
			cultureName = commonObj.get("CultureName").getTextValue();
		}

		JsonNode userInfo = root.get("userInfo");

		if (userInfo != null && userInfo.has("UserID")) {
			userID = userInfo.get("UserID").getTextValue();
			if (!userID.equals("0")) {
				ObjectId id = new ObjectId(userID);
				existingUserProfile = userProfileDAO
						.findUserDetailsByProfileID(id);
			} else {
				newAccount.setAddedOn(new Date());
			}
		}

		if (userInfo != null && userInfo.has("UserName")) {
			String loginUserName = userInfo.get("UserName").getTextValue();
			if (!userID.equals("0")) {
				existingUserAccount = userAccountDAO
						.findByUserName(loginUserName);
			} else {
				newAccount.setUserName(loginUserName);
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
			newProfile.setUserId(newAccount);
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

		// Need to Compare Equals before saving existingUserProfile and
		// newProfile
		ObjectId authorId = new ObjectId(userProfileID);
		GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		gpmsCommonObj.setUserName(userName);
		gpmsCommonObj.setUserProfileID(userProfileID);
		gpmsCommonObj.setCultureName(cultureName);

		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		// Save the User Account
		if (!userID.equals("0")) {
			userAccountDAO.save(existingUserAccount);
		} else {
			userAccountDAO.save(newAccount);
		}

		// Save the User Profile
		if (!userID.equals("0")) {
			userProfileDAO.updateUserByUserID(existingUserProfile,
					authorProfile, gpmsCommonObj);
		} else {
			userProfileDAO.saveUserByUserID(newProfile, authorProfile,
					gpmsCommonObj);
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
			newAccount.setAddedOn(new Date());
		}

		if (userID.equals("0")) {
			if (userInfo != null && userInfo.has("UserName")) {
				String loginUserName = userInfo.get("UserName").getTextValue();
				newAccount.setUserName(loginUserName);
			}

			if (userInfo != null && userInfo.has("Password")) {
				newAccount.setPassword(userInfo.get("Password").getTextValue());
			}

			newProfile.setUserId(newAccount);

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

	@GET
	@Path("/logout")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response logout(@Context HttpServletRequest req) {
		if (req == null) {
			System.out.println("Null request in context");
		}
		HttpSession session = req.getSession();

		// if (session.getAttribute("gpmsUserName") != null) {
		session.removeAttribute("gpmsUserName");
		// session.invalidate();
		// }

		// if (session.getAttribute("userPositionType") == null) {
		session.removeAttribute("userPositionType");
		// session.invalidate();
		// }

		// if (session.getAttribute("userPositionTitle") == null) {
		session.removeAttribute("userPositionTitle");
		// session.invalidate();
		// }

		// if (session.getAttribute("userDepartment") == null) {
		session.removeAttribute("userDepartment");
		// session.invalidate();
		// }

		// if (session.getAttribute("userCollege") == null) {
		session.removeAttribute("userCollege");
		// session.invalidate();
		// }

		// if (session.getAttribute("isAdmin") == null) {
		session.removeAttribute("isAdmin");
		// session.invalidate();
		// }

		if (session.getAttribute("userProfileId") != null) {
			// session.setAttribute("userProfileId", null);
			session.removeAttribute("userProfileId");
			session.invalidate();
			java.net.URI location = null;
			try {
				location = new java.net.URI("../Login.jsp");
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			return Response.seeOther(location).build();
		}
		return null;
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
}
