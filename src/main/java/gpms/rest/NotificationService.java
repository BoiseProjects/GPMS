package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.NotificationLog;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

@Path("/notifications")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class NotificationService {

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;
	DelegationDAO delegationDAO = null;
	NotificationDAO notificationDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public NotificationService() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
		delegationDAO = new DelegationDAO(mongoClient, morphia, dbName);
		notificationDAO = new NotificationDAO(mongoClient, morphia, dbName);
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String returnString() {
		return "Hello World!";
	}

	@POST
	@Path("/NotificationGetAllCount")
	public long notificationGetAllCountForAUser(String message)
			throws JsonProcessingException, IOException, ParseException {
		String response = new String();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

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

		return notificationDAO.findAllNotificationCountAUser(userProfileID,
				userIsAdmin);

	}

	@POST
	@Path("/NotificationGetAll")
	public String notificationGetAllForAUser(String message)
			throws JsonProcessingException, IOException, ParseException {
		String response = new String();
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

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

		// limit: 10, offset: 1
		List<NotificationLog> notifications = notificationDAO
				.findAllNotificationForAUser(1, 10, userProfileID, userIsAdmin);

		return response;

	}
}
