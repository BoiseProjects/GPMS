package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.NotificationLog;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.media.sse.EventOutput;
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.glassfish.jersey.media.sse.SseBroadcaster;
import org.glassfish.jersey.media.sse.SseFeature;
import org.mongodb.morphia.Morphia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;

@Singleton
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

	static final SseBroadcaster BROADCASTER = new SseBroadcaster();

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
	public String notificationGetAllCountForAUser(String message)
			throws JsonProcessingException, IOException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		String userProfileID = new String();
		@SuppressWarnings("unused")
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		JsonNode commonObj = root.get("gpmsCommonObj");
		if (commonObj != null && commonObj.has("UserProfileID")) {
			userProfileID = commonObj.get("UserProfileID").textValue();
		}
		if (commonObj != null && commonObj.has("UserName")) {
			userName = commonObj.get("UserName").textValue();
		}
		if (commonObj != null && commonObj.has("UserIsAdmin")) {
			userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
					.textValue());
		}
		if (commonObj != null && commonObj.has("UserCollege")) {
			userCollege = commonObj.get("UserCollege").textValue();
		}
		if (commonObj != null && commonObj.has("UserDepartment")) {
			userDepartment = commonObj.get("UserDepartment").textValue();
		}
		if (commonObj != null && commonObj.has("UserPositionType")) {
			userPositionType = commonObj.get("UserPositionType").textValue();
		}
		if (commonObj != null && commonObj.has("UserPositionTitle")) {
			userPositionTitle = commonObj.get("UserPositionTitle").textValue();
		}

		return Long.toString(notificationDAO.findAllNotificationCountAUser(
				userProfileID, userCollege, userDepartment, userPositionType,
				userPositionTitle, userIsAdmin));

	}

	@GET
	@Path("/NotificationGetRealTimeCount")
	// @Produces("text/event-stream")
	@Produces(SseFeature.SERVER_SENT_EVENTS)
	public EventOutput notificationGetRealTimeCountForAUser(
			@Context HttpServletRequest request,
			@Context HttpServletResponse response)
			throws JsonProcessingException, IOException, ParseException {
		// response.setContentType("text/event-stream, charset=UTF-8");
		// PrintWriter out = response.getWriter();

		EventOutput eventOutput = new EventOutput();

		HttpSession session = request.getSession();
		String userProfileID = new String();
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();
		Boolean userIsAdmin = false;

		if (session.getAttribute("userProfileId") != null) {
			userProfileID = (String) session.getAttribute("userProfileId");
		}
		// if (session.getAttribute("gpmsUserName") != null) {
		// userName = (String) session.getAttribute("gpmsUserName");
		// }

		if (session.getAttribute("userCollege") != null) {
			userCollege = (String) session.getAttribute("userCollege");
		}

		if (session.getAttribute("userDepartment") != null) {
			userDepartment = (String) session.getAttribute("userDepartment");
		}

		if (session.getAttribute("userPositionType") != null) {
			userPositionType = (String) session
					.getAttribute("userPositionType");
		}

		if (session.getAttribute("userPositionTitle") != null) {
			userPositionTitle = (String) session
					.getAttribute("userPositionTitle");
		}

		if (session.getAttribute("isAdmin") != null) {
			userIsAdmin = (Boolean) session.getAttribute("isAdmin");
		}

		long notificationCount = notificationDAO.findAllNotificationCountAUser(
				userProfileID, userCollege, userDepartment, userPositionType,
				userPositionTitle, userIsAdmin);

		OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		eventBuilder.name("notification");
		eventBuilder.data(String.class, Long.toString(notificationCount));
		OutboundEvent event = eventBuilder.build();
		eventOutput.write(event);

		BROADCASTER.add(eventOutput);

		return eventOutput;
	}

	@POST
	@Path("/NotificationGetAll")
	public List<NotificationLog> notificationGetAllForAUser(String message)
			throws JsonProcessingException, IOException, ParseException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		String userProfileID = new String();
		@SuppressWarnings("unused")
		String userName = new String();
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
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.textValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").textValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").textValue();
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

		// limit: 10, offset: 1
		List<NotificationLog> notifications = notificationDAO
				.findAllNotificationForAUser(1, 10, userProfileID, userCollege,
						userDepartment, userPositionType, userPositionTitle,
						userIsAdmin);

		return notifications;

	}
}
