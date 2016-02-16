package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.accesscontrol.Accesscontrol;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.AdditionalInfo;
import gpms.model.Address;
import gpms.model.Appendix;
import gpms.model.AuditLogInfo;
import gpms.model.BaseInfo;
import gpms.model.BaseOptions;
import gpms.model.BasePIEligibilityOptions;
import gpms.model.CollaborationInfo;
import gpms.model.ComplianceInfo;
import gpms.model.ConfidentialInfo;
import gpms.model.ConflictOfInterest;
import gpms.model.CostShareInfo;
import gpms.model.FundingSource;
import gpms.model.InvestigatorInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.NotificationLog;
import gpms.model.OSPSectionInfo;
import gpms.model.ProjectInfo;
import gpms.model.ProjectLocation;
import gpms.model.ProjectPeriod;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.ProposalInfo;
import gpms.model.ProposalStatusInfo;
import gpms.model.Recovery;
import gpms.model.SignatureInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.Status;
import gpms.model.TypeOfRequest;
import gpms.model.UniversityCommitments;
import gpms.model.UserAccount;
import gpms.model.UserProfile;
import gpms.utils.SerializationHelper;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.mongodb.morphia.Morphia;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.MongoClient;

@Path("/proposals")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.APPLICATION_FORM_URLENCODED, MediaType.TEXT_PLAIN,
		MediaType.TEXT_HTML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
		MediaType.TEXT_PLAIN, MediaType.TEXT_HTML })
public class ProposalService {
	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;
	DelegationDAO delegationDAO = null;
	NotificationDAO notificationDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public ProposalService() {
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
	@Path("/GetProposalStatusList")
	public List<ProposalStatusInfo> getProposalStatusList()
			throws JsonProcessingException, IOException {
		List<ProposalStatusInfo> proposalStatusList = new ArrayList<ProposalStatusInfo>();
		for (Status status : Status.values()) {
			ProposalStatusInfo proposalStatus = new ProposalStatusInfo();
			proposalStatus.setStatusKey(status.name());
			proposalStatus.setStatusValue(status.toString());
			proposalStatusList.add(proposalStatus);
		}

		return proposalStatusList;
	}

	@POST
	@Path("/GetProposalsList")
	public List<ProposalInfo> produceProposalsJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException {
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		int offset = 0, limit = 0;
		String projectTitle = new String();
		String usernameBy = new String();
		Double totalCostsFrom = 0.0;
		Double totalCostsTo = 0.0;
		String receivedOnFrom = new String();
		String receivedOnTo = new String();
		String proposalStatus = new String();
		String userRole = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("offset")) {
			offset = root.get("offset").getIntValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").getIntValue();
		}

		if (root != null && root.has("proposalBindObj")) {

			JsonNode proposalObj = root.get("proposalBindObj");
			if (proposalObj != null && proposalObj.has("ProjectTitle")) {
				projectTitle = proposalObj.get("ProjectTitle").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("ReceivedOnFrom")) {
				receivedOnFrom = proposalObj.get("ReceivedOnFrom")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("ReceivedOnTo")) {
				receivedOnTo = proposalObj.get("ReceivedOnTo").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").getTextValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").getTextValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").getTextValue();
			}
		}

		proposals = proposalDAO.findAllForProposalGrid(offset, limit,
				projectTitle, usernameBy, receivedOnFrom, receivedOnTo,
				totalCostsFrom, totalCostsTo, proposalStatus, userRole);

		return proposals;
	}

	@POST
	@Path("/GetUserProposalsList")
	public List<ProposalInfo> produceUserProposalsJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException {
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		int offset = 0, limit = 0;
		String projectTitle = new String();
		String usernameBy = new String();
		Double totalCostsFrom = 0.0;
		Double totalCostsTo = 0.0;
		String submittedOnFrom = new String();
		String submittedOnTo = new String();
		String proposalStatus = new String();
		String userRole = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("offset")) {
			offset = root.get("offset").getIntValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").getIntValue();
		}

		if (root != null && root.has("proposalBindObj")) {
			JsonNode proposalObj = root.get("proposalBindObj");
			if (proposalObj != null && proposalObj.has("ProjectTitle")) {
				projectTitle = proposalObj.get("ProjectTitle").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnFrom")) {
				submittedOnFrom = proposalObj.get("SubmittedOnFrom")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnTo")) {
				submittedOnTo = proposalObj.get("SubmittedOnTo").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").getTextValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").getTextValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").getTextValue();
			}
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		if (root != null && root.has("gpmsCommonObj")) {
			JsonNode commonObj = root.get("gpmsCommonObj");
			if (commonObj != null && commonObj.has("UserProfileID")) {
				userProfileID = commonObj.get("UserProfileID").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserName")) {
				userName = commonObj.get("UserName").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserIsAdmin")) {
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.getTextValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionType")) {
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		proposals = proposalDAO.findUserProposalGrid(offset, limit,
				projectTitle, usernameBy, submittedOnFrom, submittedOnTo,
				totalCostsFrom, totalCostsTo, proposalStatus, userRole,
				userProfileID, userCollege, userDepartment, userPositionType,
				userPositionTitle);

		return proposals;
	}

	@POST
	@Path("/ProposalsExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	public String exportProposalsJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException, URISyntaxException {
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		String projectTitle = new String();
		String usernameBy = new String();
		Double totalCostsFrom = 0.0;
		Double totalCostsTo = 0.0;
		String submittedOnFrom = new String();
		String submittedOnTo = new String();
		String proposalStatus = new String();
		String userRole = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalBindObj")) {
			JsonNode proposalObj = root.get("proposalBindObj");
			if (proposalObj != null && proposalObj.has("ProjectTitle")) {
				projectTitle = proposalObj.get("ProjectTitle").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnFrom")) {
				submittedOnFrom = proposalObj.get("SubmittedOnFrom")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnTo")) {
				submittedOnTo = proposalObj.get("SubmittedOnTo").getTextValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").getTextValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").getTextValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").getTextValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus")
						.getTextValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").getTextValue();
			}
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		if (root != null && root.has("gpmsCommonObj")) {
			JsonNode commonObj = root.get("gpmsCommonObj");
			if (commonObj != null && commonObj.has("UserProfileID")) {
				userProfileID = commonObj.get("UserProfileID").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserName")) {
				userName = commonObj.get("UserName").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserIsAdmin")) {
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.getTextValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionType")) {
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		proposals = proposalDAO.findAllUserProposals(projectTitle, usernameBy,
				submittedOnFrom, submittedOnTo, totalCostsFrom, totalCostsTo,
				proposalStatus, userRole, userProfileID, userCollege,
				userDepartment, userPositionType, userPositionTitle);

		if (proposals.size() > 0) {
			Xcelite xcelite = new Xcelite();
			XceliteSheet sheet = xcelite.createSheet("Proposals");
			SheetWriter<ProposalInfo> writer = sheet
					.getBeanWriter(ProposalInfo.class);

			writer.write(proposals);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			Date date = new Date();
			System.out.println(); // 2016/02/10 16:16:39

			String fileName = String.format(
					"%s.%s",
					RandomStringUtils.randomAlphanumeric(8) + "_"
							+ dateFormat.format(date), "xlsx");

			// File file = new File(request.getServletContext().getAttribute(
			// "FILES_DIR")
			// + File.separator + filename);
			// System.out.println("Absolute Path at server=" +
			// file.getAbsolutePath());
			String policyLocation = this.getClass().getResource("/tmpfiles")
					.toURI().getPath();

			xcelite.write(new File(policyLocation + fileName));

			// xcelite.write(new File(request.getServletContext().getAttribute(
			// "FILES_DIR")
			// + File.separator + fileName));

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					fileName);
		} else {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					"No Record");
		}
	}

	@POST
	@Path("/DeleteProposalByProposalID")
	public Response deleteUserByProposalID(String message) throws Exception {
		String proposalId = new String();
		String proposalRoles = new String();
		String proposalUserTitle = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
		}

		if (root != null && root.has("proposalRoles")) {
			proposalRoles = root.get("proposalRoles").getTextValue();
		}

		if (root != null && root.has("proposalUserTitle")) {
			proposalUserTitle = root.get("proposalUserTitle").getTextValue();
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		if (root != null && root.has("gpmsCommonObj")) {
			JsonNode commonObj = root.get("gpmsCommonObj");
			if (commonObj != null && commonObj.has("UserProfileID")) {
				userProfileID = commonObj.get("UserProfileID").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserName")) {
				userName = commonObj.get("UserName").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserIsAdmin")) {
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.getTextValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionType")) {
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		ObjectId id = new ObjectId(proposalId);

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		Proposal proposal = proposalDAO.findProposalByProposalID(id);

		boolean isDeleted = proposalDAO.deleteProposal(proposal, proposalRoles,
				proposalUserTitle, authorProfile);

		if (isDeleted) {
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();
			String projectTitle = proposal.getProjectInfo().getProjectTitle();
			String notificationMessage = "Deleted by " + authorUserName + ".";
			NotifyAllExistingInvestigators(proposalId, projectTitle, proposal,
					notificationMessage, "Proposal", true);

			return Response.status(200).type(MediaType.APPLICATION_JSON)
					.entity(true).build();
		} else {
			return Response.status(403).type(MediaType.APPLICATION_JSON)
					.entity("DENY").build();
		}

	}

	@POST
	@Path("/DeleteMultipleProposalsByProposalID")
	public String deleteMultipleProposalsByProposalID(String message)
			throws JsonProcessingException, IOException {
		String response = new String();
		String proposalIds = new String();
		String proposals[] = new String[0];

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalIds")) {
			proposalIds = root.get("proposalIds").getTextValue();
			proposals = proposalIds.split(",");
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		if (root != null && root.has("gpmsCommonObj")) {
			JsonNode commonObj = root.get("gpmsCommonObj");
			if (commonObj != null && commonObj.has("UserProfileID")) {
				userProfileID = commonObj.get("UserProfileID").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserName")) {
				userName = commonObj.get("UserName").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserIsAdmin")) {
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.getTextValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionType")) {
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		for (String proposalId : proposals) {
			ObjectId id = new ObjectId(proposalId);
			Proposal proposal = proposalDAO.findProposalByProposalID(id);
			proposalDAO.deleteProposal(proposal, "", userPositionTitle,
					authorProfile);

			String authorUserName = authorProfile.getUserAccount()
					.getUserName();
			String projectTitle = proposal.getProjectInfo().getProjectTitle();
			String notificationMessage = "Deleted by " + authorUserName + ".";
			NotifyAllExistingInvestigators(proposalId, projectTitle, proposal,
					notificationMessage, "Proposal", true);
		}
		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");
		return response;
	}

	@POST
	@Path("/GetProposalDetailsByProposalId")
	public String produceProposalDetailsByProposalId(String message)
			throws JsonProcessingException, IOException {
		Proposal proposal = new Proposal();
		String response = new String();

		String proposalId = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
		}

		ObjectId id = new ObjectId(proposalId);
		proposal = proposalDAO.findProposalByProposalID(id);

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
				.excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
				.create();
		response = gson.toJson(proposal, Proposal.class);

		return response;
	}

	@POST
	@Path("/GetProposalAuditLogList")
	public List<AuditLogInfo> produceProposalAuditLogJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException {
		List<AuditLogInfo> proposalAuditLogs = new ArrayList<AuditLogInfo>();

		int offset = 0, limit = 0;
		String proposalId = new String();
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

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
		}

		if (root != null && root.has("auditLogBindObj")) {
			JsonNode auditLogBindObj = root.get("auditLogBindObj");
			if (auditLogBindObj != null && auditLogBindObj.has("Action")) {
				action = auditLogBindObj.get("Action").getTextValue();
			}

			if (auditLogBindObj != null && auditLogBindObj.has("AuditedBy")) {
				auditedBy = auditLogBindObj.get("AuditedBy").getTextValue();
			}

			if (auditLogBindObj != null
					&& auditLogBindObj.has("ActivityOnFrom")) {
				activityOnFrom = auditLogBindObj.get("ActivityOnFrom")
						.getTextValue();
			}

			if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnTo")) {
				activityOnTo = auditLogBindObj.get("ActivityOnTo")
						.getTextValue();
			}
		}

		ObjectId id = new ObjectId(proposalId);

		proposalAuditLogs = proposalDAO.findAllForProposalAuditLogGrid(offset,
				limit, id, action, auditedBy, activityOnFrom, activityOnTo);

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);

		return proposalAuditLogs;
	}

	@POST
	@Path("/ProposalLogsExportToExcel")
	public String exportProposalAuditLogJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException, URISyntaxException {
		List<AuditLogInfo> proposalAuditLogs = new ArrayList<AuditLogInfo>();

		String proposalId = new String();
		String action = new String();
		String auditedBy = new String();
		String activityOnFrom = new String();
		String activityOnTo = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
		}

		if (root != null && root.has("auditLogBindObj")) {
			JsonNode auditLogBindObj = root.get("auditLogBindObj");
			if (auditLogBindObj != null && auditLogBindObj.has("Action")) {
				action = auditLogBindObj.get("Action").getTextValue();
			}

			if (auditLogBindObj != null && auditLogBindObj.has("AuditedBy")) {
				auditedBy = auditLogBindObj.get("AuditedBy").getTextValue();
			}

			if (auditLogBindObj != null
					&& auditLogBindObj.has("ActivityOnFrom")) {
				activityOnFrom = auditLogBindObj.get("ActivityOnFrom")
						.getTextValue();
			}

			if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnTo")) {
				activityOnTo = auditLogBindObj.get("ActivityOnTo")
						.getTextValue();
			}
		}

		ObjectId id = new ObjectId(proposalId);

		proposalAuditLogs = proposalDAO.findAllUserProposalAuditLogs(id,
				action, auditedBy, activityOnFrom, activityOnTo);

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);
		if (proposalAuditLogs.size() > 0) {
			Xcelite xcelite = new Xcelite();
			XceliteSheet sheet = xcelite.createSheet("AuditLogs");
			SheetWriter<AuditLogInfo> writer = sheet
					.getBeanWriter(AuditLogInfo.class);

			writer.write(proposalAuditLogs);

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
			Date date = new Date();
			System.out.println(); // 2016/02/10 16:16:39

			String fileName = String.format(
					"%s.%s",
					RandomStringUtils.randomAlphanumeric(8) + "_"
							+ dateFormat.format(date), "xlsx");

			// File file = new File(request.getServletContext().getAttribute(
			// "FILES_DIR")
			// + File.separator + filename);
			// System.out.println("Absolute Path at server=" +
			// file.getAbsolutePath());
			String policyLocation = this.getClass().getResource("/tmpfiles")
					.toURI().getPath();

			xcelite.write(new File(policyLocation + fileName));

			// xcelite.write(new File(request.getServletContext().getAttribute(
			// "FILES_DIR")
			// + File.separator + fileName));

			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					fileName);
		} else {
			return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
					"No Record");
		}
	}

	@POST
	@Path("/CheckUniqueProjectTitle")
	public String checkUniqueProjectTitle(String message)
			throws JsonProcessingException, IOException {
		String proposalID = new String();
		String newProjectTitle = new String();
		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalUniqueObj")) {
			JsonNode proposalUniqueObj = root.get("proposalUniqueObj");
			if (proposalUniqueObj != null
					&& proposalUniqueObj.has("ProposalID")) {
				proposalID = proposalUniqueObj.get("ProposalID").getTextValue();
			}

			if (proposalUniqueObj != null
					&& proposalUniqueObj.has("NewProjectTitle")) {
				newProjectTitle = proposalUniqueObj.get("NewProjectTitle")
						.getTextValue();
			}
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();

		if (root != null && root.has("gpmsCommonObj")) {
			JsonNode commonObj = root.get("gpmsCommonObj");
			if (commonObj != null && commonObj.has("UserProfileID")) {
				userProfileID = commonObj.get("UserProfileID").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserName")) {
				userName = commonObj.get("UserName").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserIsAdmin")) {
				userIsAdmin = Boolean.parseBoolean(commonObj.get("UserIsAdmin")
						.getTextValue());
			}
			if (commonObj != null && commonObj.has("UserCollege")) {
				userCollege = commonObj.get("UserCollege").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserDepartment")) {
				userDepartment = commonObj.get("UserDepartment").getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionType")) {
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		Proposal proposal = new Proposal();
		if (!proposalID.equals("0")) {
			ObjectId id = new ObjectId(proposalID);
			proposal = proposalDAO.findNextProposalWithSameProjectTitle(id,
					newProjectTitle);
		} else {
			proposal = proposalDAO
					.findAnyProposalWithSameProjectTitle(newProjectTitle);
		}

		if (proposal != null) {
			response = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString("false");
		} else {
			response = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString("true");
		}
		return response;
	}

	@POST
	@Path("/GetAllSignatureForAProposal")
	public List<SignatureInfo> getAllSignatureForAProposal(String message)
			throws UnknownHostException, JsonProcessingException, IOException,
			ParseException {
		String proposalId = new String();
		// String response = new String();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(message);
		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
		}

		ObjectId id = new ObjectId(proposalId);

		List<SignatureInfo> signatures = proposalDAO
				.findAllSignatureForAProposal(id);

		// Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
		// .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
		// .create();
		// response = gson.toJson(signatures, SignatureInfo.class);

		// for (SignatureInfo signatureInfo : signatures) {
		// // TODO : get all delegated User Info for this PI user and bind it
		// // into signature Object
		//
		// // Check if the proposal Id is exact to this proposal id
		//
		// // TODO : find all the delegated User for this Proposal Id
		// ObjectId userId = new ObjectId(signatureInfo.getUserProfileId());
		// List<SignatureInfo> delegatedUsers = delegationDAO
		// .findDelegatedUsersForAUser(userId,
		// signatureInfo.getPositionTitle(), proposalId);
		//
		// for (SignatureInfo delegatedUser : delegatedUsers) {
		// signatures.add(delegatedUser);
		// }
		//
		// }

		return signatures;
	}

	@POST
	@Path("/UpdateProposalStatus")
	public Response updateProposalStatus(String message) throws Exception {
		String proposalID = new String();
		Proposal existingProposal = new Proposal();

		ObjectId id = new ObjectId();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			JsonNode proposalId = root.get("proposalId");

			if (proposalId != null) {
				proposalID = proposalId.getTextValue();
				if (!proposalID.equals("0") && !proposalID.equals("")) {
					id = new ObjectId(proposalID);
					existingProposal = proposalDAO.findProposalByProposalID(id);

					// For Proposal User Title : for Research Administrator and
					// University Research Director
					if (root != null && root.has("proposalUserTitle")) {
						JsonNode proposalUserTitle = root
								.get("proposalUserTitle");
						if (proposalUserTitle != null) {
							// For Proposal Status
							if (root != null && root.has("buttonType")) {
								JsonNode buttonType = root.get("buttonType");

								if (buttonType != null) {
									String changeDone = new String();

									switch (buttonType.getTextValue()) {
									case "Withdraw":
										if (!proposalID.equals("0")) {
											if (existingProposal
													.getProposalStatus()
													.contains(
															Status.APPROVEDBYDEAN)
													&& proposalUserTitle
															.getTextValue()
															.equals("Research Administrator")
													&& !existingProposal
															.getProposalStatus()
															.contains(
																	Status.WITHDRAWBYRESEARCHADMIN)) {
												existingProposal
														.getProposalStatus()
														.clear();
												existingProposal
														.getProposalStatus()
														.add(Status.WITHDRAWBYRESEARCHADMIN);
												changeDone = "Withdrawn";
											}
										}
										break;

									case "Archive":
										if (!proposalID.equals("0")) {
											if (existingProposal
													.getProposalStatus()
													.contains(
															Status.SUBMITTEDBYRESEARCHADMIN)
													&& proposalUserTitle
															.getTextValue()
															.equals("University Research Director")
													&& !existingProposal
															.getProposalStatus()
															.contains(
																	Status.ARCHIVEDBYRESEARCHDIRECTOR)) {
												existingProposal
														.getProposalStatus()
														.clear();
												existingProposal
														.getProposalStatus()
														.add(Status.ARCHIVEDBYRESEARCHDIRECTOR);
												changeDone = "Archived";
											}
										}
										break;

									default:

										break;
									}

									String userProfileID = new String();
									String userName = new String();
									Boolean userIsAdmin = false;
									String userCollege = new String();
									String userDepartment = new String();
									String userPositionType = new String();
									String userPositionTitle = new String();

									if (root != null
											&& root.has("gpmsCommonObj")) {
										JsonNode commonObj = root
												.get("gpmsCommonObj");
										if (commonObj != null
												&& commonObj
														.has("UserProfileID")) {
											userProfileID = commonObj.get(
													"UserProfileID")
													.getTextValue();
										}
										if (commonObj != null
												&& commonObj.has("UserName")) {
											userName = commonObj
													.get("UserName")
													.getTextValue();
										}
										if (commonObj != null
												&& commonObj.has("UserIsAdmin")) {
											userIsAdmin = commonObj.get(
													"UserIsAdmin")
													.getBooleanValue();
										}
										if (commonObj != null
												&& commonObj.has("UserCollege")) {
											userCollege = commonObj.get(
													"UserCollege")
													.getTextValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserDepartment")) {
											userDepartment = commonObj.get(
													"UserDepartment")
													.getTextValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserPositionType")) {
											userPositionType = commonObj.get(
													"UserPositionType")
													.getTextValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserPositionTitle")) {
											userPositionTitle = commonObj.get(
													"UserPositionTitle")
													.getTextValue();
										}
									}

									ObjectId authorId = new ObjectId(
											userProfileID);
									UserProfile authorProfile = userProfileDAO
											.findUserDetailsByProfileID(authorId);

									// Save the Proposal
									String authorUserName = authorProfile
											.getUserAccount().getUserName();

									proposalDAO.updateProposalStatus(
											existingProposal, authorProfile);

									String notificationMessage = changeDone
											+ " by " + authorUserName + ".";

									NotifyAllExistingInvestigators(
											existingProposal.getId().toString(),
											existingProposal.getProjectInfo()
													.getProjectTitle(),
											existingProposal,
											notificationMessage, "Proposal",
											false);
								}
							}
						}
					}
				}
			}
		}
		return null;
	}

	@POST
	@Path("/SaveUpdateProposal")
	public Response saveUpdateProposal(String message) throws Exception {
		String proposalID = new String();
		Proposal newProposal = new Proposal();
		Proposal existingProposal = new Proposal();
		Proposal oldProposal = new Proposal();

		ObjectId proposalId = new ObjectId();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);
		JsonNode proposalInfo = null;

		if (root != null && root.has("proposalInfo")) {
			proposalInfo = root.get("proposalInfo");
			if (proposalInfo != null && proposalInfo.has("ProposalID")) {
				proposalID = proposalInfo.get("ProposalID").getTextValue();
				if (!proposalID.equals("0")) {
					proposalId = new ObjectId(proposalID);
					existingProposal = proposalDAO
							.findProposalByProposalID(proposalId);
					// using our serializable method for cloning
					oldProposal = SerializationHelper
							.cloneThroughSerialize(existingProposal);
				}

				// For Proposal User Title : for Dean, Chair and Manager
				JsonNode proposalUserTitle = root.get("proposalUserTitle");
				if (proposalUserTitle != null) {
					// For Proposal Roles : PI, Co-PI, Senior
					JsonNode proposalRoles = root.get("proposalRoles");
					if (proposalRoles != null) {
						// TODO
						List<String> currentProposalRoles = Arrays
								.asList(proposalRoles.getTextValue()
										.split(", "));

						// For Proposal Status
						JsonNode buttonType = root.get("buttonType");

						if (buttonType != null) {
							switch (buttonType.getTextValue()) {
							case "Save As Draft":
								// This is Hack
								break;

							case "Submit":
								// var canSubmitRoles = [ "PI" ];
								// var canSubmitTitles = [
								// "Research Administrator"
								// ];
								if (!proposalID.equals("0")) {

									List<Status> canPISubmitStatus = Arrays
											.asList(Status.NOTSUBMITTEDBYPI,
													Status.RETURNEDBYCHAIR,
													Status.DISAPPROVEDBYBUSINESSMANAGER,
													Status.DISAPPROVEDBYIRB,
													Status.RETURNEDBYDEAN,
													Status.DISAPPROVEDBYRESEARCHADMIN,
													Status.DISAPPROVEDBYRESEARCHDIRECTOR);
									if (canPISubmitStatus
											.contains(existingProposal
													.getProposalStatus().get(0))
											&& currentProposalRoles
													.contains("PI")
											&& !proposalUserTitle
													.getTextValue()
													.equals("Research Administrator")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.WAITINGFORCHAIRAPPROVAL)) {
										existingProposal.getProposalStatus()
												.clear();
										existingProposal
												.getProposalStatus()
												.add(Status.WAITINGFORCHAIRAPPROVAL);
									} else if (existingProposal
											.getProposalStatus().contains(
													Status.READYFORSUBMISSION)
											&& !currentProposalRoles
													.contains("PI")
											&& proposalUserTitle
													.getTextValue()
													.equals("Research Administrator")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.SUBMITTEDBYRESEARCHADMIN)) {
										existingProposal.getProposalStatus()
												.clear();
										existingProposal
												.getProposalStatus()
												.add(Status.SUBMITTEDBYRESEARCHADMIN);
									} else {
										// This user is both PI and Research
										// Administrator
									}
								}

								break;

							case "Update":
								// This is also Hack

								break;

							case "Approve":
								if (!proposalID.equals("0")) {
									// var canApproveTitles = [
									// "Department Chair",
									// "Business Manager",
									// "IRB", "Dean", "Research Administrator",
									// "University Research Director" ];
									if (existingProposal
											.getProposalStatus()
											.contains(
													Status.WAITINGFORCHAIRAPPROVAL)
											&& proposalUserTitle.getTextValue()
													.equals("Department Chair")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.READYFORREVIEW)) {
										existingProposal.getProposalStatus()
												.clear();
										// Ready for Review
										existingProposal.getProposalStatus()
												.add(Status.READYFORREVIEW);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW) || existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYIRB))
											&& proposalUserTitle.getTextValue()
													.equals("Business Manager")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.REVIEWEDBYBUSINESSMANAGER)) {
										existingProposal.getProposalStatus()
												.clear();
										// Reviewed by Business Manager
										existingProposal
												.getProposalStatus()
												.add(Status.REVIEWEDBYBUSINESSMANAGER);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW) || existingProposal
											.getProposalStatus()
											.contains(
													Status.REVIEWEDBYBUSINESSMANAGER))
											&& proposalUserTitle.getTextValue()
													.equals("IRB")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.APPROVEDBYIRB)) {
										existingProposal.getProposalStatus()
												.clear();
										// Approved by IRB
										existingProposal.getProposalStatus()
												.add(Status.APPROVEDBYIRB);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW)
											|| existingProposal
													.getProposalStatus()
													.contains(
															Status.REVIEWEDBYBUSINESSMANAGER) || existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYIRB))
											&& proposalUserTitle.getTextValue()
													.equals("Dean")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.APPROVEDBYDEAN)) {
										existingProposal.getProposalStatus()
												.clear();
										// Approved by Dean
										existingProposal.getProposalStatus()
												.add(Status.APPROVEDBYDEAN);

									} else if (existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYDEAN)
											&& proposalUserTitle
													.getTextValue()
													.equals("Research Administrator")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.SUBMITTEDTORESEARCHDIRECTOR)) {
										existingProposal.getProposalStatus()
												.clear();
										// Submitted to Research Director
										existingProposal
												.getProposalStatus()
												.add(Status.SUBMITTEDTORESEARCHDIRECTOR);

									} else if (existingProposal
											.getProposalStatus()
											.contains(
													Status.SUBMITTEDTORESEARCHDIRECTOR)
											&& proposalUserTitle
													.getTextValue()
													.equals("University Research Director")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.READYFORSUBMISSION)) {
										existingProposal.getProposalStatus()
												.clear();
										// Ready for submission
										existingProposal.getProposalStatus()
												.add(Status.READYFORSUBMISSION);
									} else {
										// You are not allowed to APPROVE the
										// Proposal
									}
								}

								break;

							case "Disapprove":
								if (!proposalID.equals("0")) {

									if (existingProposal
											.getProposalStatus()
											.contains(
													Status.WAITINGFORCHAIRAPPROVAL)
											&& proposalUserTitle.getTextValue()
													.equals("Department Chair")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.RETURNEDBYCHAIR)) {
										existingProposal.getProposalStatus()
												.clear();
										// Returned by Chair
										existingProposal.getProposalStatus()
												.add(Status.RETURNEDBYCHAIR);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW) || existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYIRB))
											&& proposalUserTitle.getTextValue()
													.equals("Business Manager")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.DISAPPROVEDBYBUSINESSMANAGER)) {
										existingProposal.getProposalStatus()
												.clear();
										// Disapproved by Business Manager
										existingProposal
												.getProposalStatus()
												.add(Status.DISAPPROVEDBYBUSINESSMANAGER);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW) || existingProposal
											.getProposalStatus()
											.contains(
													Status.REVIEWEDBYBUSINESSMANAGER))
											&& proposalUserTitle.getTextValue()
													.equals("IRB")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.DISAPPROVEDBYIRB)) {
										existingProposal.getProposalStatus()
												.clear();
										// Disapproved by IRB
										existingProposal.getProposalStatus()
												.add(Status.DISAPPROVEDBYIRB);

									} else if ((existingProposal
											.getProposalStatus().contains(
													Status.READYFORREVIEW)
											|| existingProposal
													.getProposalStatus()
													.contains(
															Status.REVIEWEDBYBUSINESSMANAGER) || existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYIRB))
											&& proposalUserTitle.getTextValue()
													.equals("Dean")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.RETURNEDBYDEAN)) {
										existingProposal.getProposalStatus()
												.clear();
										// Returned by Dean
										existingProposal.getProposalStatus()
												.add(Status.RETURNEDBYDEAN);

									} else if (existingProposal
											.getProposalStatus().contains(
													Status.APPROVEDBYDEAN)
											&& proposalUserTitle
													.getTextValue()
													.equals("Research Administrator")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.DISAPPROVEDBYRESEARCHADMIN)) {
										existingProposal.getProposalStatus()
												.clear();
										// Disapproved by Research Administrator
										existingProposal
												.getProposalStatus()
												.add(Status.DISAPPROVEDBYRESEARCHADMIN);

									} else if (existingProposal
											.getProposalStatus()
											.contains(
													Status.SUBMITTEDTORESEARCHDIRECTOR)
											&& proposalUserTitle
													.getTextValue()
													.equals("University Research Director")
											&& !existingProposal
													.getProposalStatus()
													.contains(
															Status.DISAPPROVEDBYRESEARCHDIRECTOR)) {
										existingProposal.getProposalStatus()
												.clear();
										// Disapproved by University Research
										// Director
										existingProposal
												.getProposalStatus()
												.add(Status.DISAPPROVEDBYRESEARCHDIRECTOR);
									} else {
										// You are not allowed to DISAPPROVE the
										// Proposal
									}
								}
								break;

							default:

								break;
							}
						}
					}

				}
			}
		}

		ProjectInfo newProjectInfo = new ProjectInfo();

		if (proposalInfo != null && proposalInfo.has("ProjectInfo")) {
			JsonNode projectInfo = proposalInfo.get("ProjectInfo");
			if (projectInfo != null && projectInfo.has("ProjectTitle")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getProjectInfo()
							.getProjectTitle()
							.equals(projectInfo.get("ProjectTitle")
									.getTextValue())) {
						existingProposal.getProjectInfo().setProjectTitle(
								projectInfo.get("ProjectTitle").getTextValue());
					}
				} else {
					newProjectInfo.setProjectTitle(projectInfo.get(
							"ProjectTitle").getTextValue());
				}
			}

			if (projectInfo != null && projectInfo.has("ProjectType")) {
				ProjectType projectType = new ProjectType();
				switch (projectInfo.get("ProjectType").getTextValue()) {
				case "1":
					projectType.setIsResearchBasic(true);
					break;
				case "2":
					projectType.setIsResearchApplied(true);
					break;
				case "3":
					projectType.setIsResearchDevelopment(true);
					break;
				case "4":
					projectType.setIsInstruction(true);
					break;
				case "5":
					projectType.setIsOtherSponsoredActivity(true);
					break;
				default:
					break;
				}

				if (!proposalID.equals("0")) {
					if (!existingProposal.getProjectInfo().getProjectType()
							.equals(projectType)) {
						existingProposal.getProjectInfo().setProjectType(
								projectType);
					}
				} else {
					newProjectInfo.setProjectType(projectType);
				}
			}

			if (projectInfo != null && projectInfo.has("TypeOfRequest")) {
				TypeOfRequest typeOfRequest = new TypeOfRequest();
				switch (projectInfo.get("TypeOfRequest").getTextValue()) {
				case "1":
					typeOfRequest.setPreProposal(true);
					break;
				case "2":
					typeOfRequest.setNewProposal(true);
					break;
				case "3":
					typeOfRequest.setContinuation(true);
					break;
				case "4":
					typeOfRequest.setSupplement(true);
					break;
				default:
					break;
				}
				if (!proposalID.equals("0")) {
					if (!existingProposal.getProjectInfo().getTypeOfRequest()
							.equals(typeOfRequest)) {
						existingProposal.getProjectInfo().setTypeOfRequest(
								typeOfRequest);
					}
				} else {
					newProjectInfo.setTypeOfRequest(typeOfRequest);
				}
			}

			if (projectInfo != null && projectInfo.has("ProjectLocation")) {
				ProjectLocation projectLocation = new ProjectLocation();
				switch (projectInfo.get("ProjectLocation").getTextValue()) {
				case "1":
					projectLocation.setOffCampus(true);
					break;
				case "2":
					projectLocation.setOnCampus(true);
					break;
				default:
					break;
				}
				if (!proposalID.equals("0")) {
					if (!existingProposal.getProjectInfo().getProjectLocation()
							.equals(projectLocation)) {
						existingProposal.getProjectInfo().setProjectLocation(
								projectLocation);
					}
				} else {
					newProjectInfo.setProjectLocation(projectLocation);
				}
			}

			if (projectInfo != null && projectInfo.has("DueDate")) {
				Date dueDate = formatter.parse(projectInfo.get("DueDate")
						.getTextValue());
				if (!proposalID.equals("0")) {
					if (!existingProposal.getProjectInfo().getDueDate()
							.equals(dueDate)) {
						existingProposal.getProjectInfo().setDueDate(dueDate);
					}
				} else {
					newProjectInfo.setDueDate(dueDate);
				}
			}

			ProjectPeriod projectPeriod = new ProjectPeriod();

			if (projectInfo != null && projectInfo.has("ProjectPeriodFrom")) {
				Date periodFrom = formatter.parse(projectInfo.get(
						"ProjectPeriodFrom").getTextValue());
				projectPeriod.setFrom(periodFrom);
			}

			if (projectInfo != null && projectInfo.has("ProjectPeriodTo")) {
				Date periodTo = formatter.parse(projectInfo.get(
						"ProjectPeriodTo").getTextValue());
				projectPeriod.setTo(periodTo);
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getProjectInfo().getProjectPeriod()
						.equals(projectPeriod)) {
					existingProposal.getProjectInfo().setProjectPeriod(
							projectPeriod);
				}
			} else {
				newProjectInfo.setProjectPeriod(projectPeriod);
			}
		}

		// ProjectInfo
		if (proposalID.equals("0")) {
			newProposal.setProjectInfo(newProjectInfo);
		}

		SponsorAndBudgetInfo newSponsorAndBudgetInfo = new SponsorAndBudgetInfo();
		if (proposalInfo != null && proposalInfo.has("SponsorAndBudgetInfo")) {
			JsonNode sponsorAndBudgetInfo = proposalInfo
					.get("SponsorAndBudgetInfo");
			if (sponsorAndBudgetInfo != null
					&& sponsorAndBudgetInfo.has("GrantingAgency")) {
				for (String grantingAgency : sponsorAndBudgetInfo
						.get("GrantingAgency").getTextValue().split(", ")) {
					newSponsorAndBudgetInfo.getGrantingAgency().add(
							grantingAgency);
				}
			}

			if (sponsorAndBudgetInfo != null
					&& sponsorAndBudgetInfo.has("DirectCosts")) {
				newSponsorAndBudgetInfo.setDirectCosts(Double
						.parseDouble(sponsorAndBudgetInfo.get("DirectCosts")
								.getTextValue()));
			}

			if (sponsorAndBudgetInfo != null
					&& sponsorAndBudgetInfo.has("FACosts")) {
				newSponsorAndBudgetInfo.setFACosts(Double
						.parseDouble(sponsorAndBudgetInfo.get("FACosts")
								.getTextValue()));
			}

			if (sponsorAndBudgetInfo != null
					&& sponsorAndBudgetInfo.has("TotalCosts")) {
				newSponsorAndBudgetInfo.setTotalCosts(Double
						.parseDouble(sponsorAndBudgetInfo.get("TotalCosts")
								.getTextValue()));
			}

			if (sponsorAndBudgetInfo != null
					&& sponsorAndBudgetInfo.has("FARate")) {
				newSponsorAndBudgetInfo.setFARate(Double
						.parseDouble(sponsorAndBudgetInfo.get("FARate")
								.getTextValue()));
			}
		}

		// SponsorAndBudgetInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getSponsorAndBudgetInfo().equals(
					newSponsorAndBudgetInfo)) {
				existingProposal
						.setSponsorAndBudgetInfo(newSponsorAndBudgetInfo);
			}
		} else {
			newProposal.setSponsorAndBudgetInfo(newSponsorAndBudgetInfo);
		}

		CostShareInfo newCostShareInfo = new CostShareInfo();
		if (proposalInfo != null && proposalInfo.has("CostShareInfo")) {
			JsonNode costShareInfo = proposalInfo.get("CostShareInfo");
			if (costShareInfo != null
					&& costShareInfo.has("InstitutionalCommitted")) {
				switch (costShareInfo.get("InstitutionalCommitted")
						.getTextValue()) {
				case "1":
					newCostShareInfo.setInstitutionalCommitted(true);
					break;
				case "2":
					newCostShareInfo.setInstitutionalCommitted(false);
					break;
				default:
					break;
				}
			}

			if (costShareInfo != null
					&& costShareInfo.has("ThirdPartyCommitted")) {
				switch (costShareInfo.get("ThirdPartyCommitted").getTextValue()) {
				case "1":
					newCostShareInfo.setThirdPartyCommitted(true);
					break;
				case "2":
					newCostShareInfo.setThirdPartyCommitted(false);
					break;
				default:
					break;
				}
			}
		}
		// CostShareInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getCostShareInfo().equals(newCostShareInfo)) {
				existingProposal.setCostShareInfo(newCostShareInfo);
			}
		} else {
			newProposal.setCostShareInfo(newCostShareInfo);
		}

		UniversityCommitments newUnivCommitments = new UniversityCommitments();
		if (proposalInfo != null && proposalInfo.has("UnivCommitments")) {
			JsonNode univCommitments = proposalInfo.get("UnivCommitments");
			if (univCommitments != null
					&& univCommitments.has("NewRenovatedFacilitiesRequired")) {
				switch (univCommitments.get("NewRenovatedFacilitiesRequired")
						.getTextValue()) {
				case "1":
					newUnivCommitments.setNewRenovatedFacilitiesRequired(true);
					break;
				case "2":
					newUnivCommitments.setNewRenovatedFacilitiesRequired(false);
					break;
				default:
					break;
				}
			}

			if (univCommitments != null
					&& univCommitments.has("RentalSpaceRequired")) {
				switch (univCommitments.get("RentalSpaceRequired")
						.getTextValue()) {
				case "1":
					newUnivCommitments.setRentalSpaceRequired(true);
					break;
				case "2":
					newUnivCommitments.setRentalSpaceRequired(false);
					break;
				default:
					break;
				}
			}

			if (univCommitments != null
					&& univCommitments.has("InstitutionalCommitmentRequired")) {
				switch (univCommitments.get("InstitutionalCommitmentRequired")
						.getTextValue()) {
				case "1":
					newUnivCommitments.setInstitutionalCommitmentRequired(true);
					break;
				case "2":
					newUnivCommitments
							.setInstitutionalCommitmentRequired(false);
					break;
				default:
					break;
				}
			}
		}
		// UnivCommitments
		if (!proposalID.equals("0")) {
			if (!existingProposal.getUniversityCommitments().equals(
					newUnivCommitments)) {
				existingProposal.setUniversityCommitments(newUnivCommitments);
			}
		} else {
			newProposal.setUniversityCommitments(newUnivCommitments);
		}

		ConflictOfInterest newConflictOfInterest = new ConflictOfInterest();
		if (proposalInfo != null && proposalInfo.has("ConflicOfInterestInfo")) {
			JsonNode conflicOfInterestInfo = proposalInfo
					.get("ConflicOfInterestInfo");
			if (conflicOfInterestInfo != null
					&& conflicOfInterestInfo.has("FinancialCOI")) {
				switch (conflicOfInterestInfo.get("FinancialCOI")
						.getTextValue()) {
				case "1":
					newConflictOfInterest.setFinancialCOI(true);
					break;
				case "2":
					newConflictOfInterest.setFinancialCOI(false);
					break;
				default:
					break;
				}
			}

			if (conflicOfInterestInfo != null
					&& conflicOfInterestInfo.has("ConflictDisclosed")) {
				switch (conflicOfInterestInfo.get("ConflictDisclosed")
						.getTextValue()) {
				case "1":
					newConflictOfInterest.setConflictDisclosed(true);
					break;
				case "2":
					newConflictOfInterest.setConflictDisclosed(false);
					break;
				default:
					break;
				}
			}

			if (conflicOfInterestInfo != null
					&& conflicOfInterestInfo.has("DisclosureFormChange")) {
				switch (conflicOfInterestInfo.get("DisclosureFormChange")
						.getTextValue()) {
				case "1":
					newConflictOfInterest.setDisclosureFormChange(true);
					break;
				case "2":
					newConflictOfInterest.setDisclosureFormChange(false);
					break;
				default:
					break;
				}
			}
		}
		// ConflicOfInterestInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getConflicOfInterest().equals(
					newConflictOfInterest)) {
				existingProposal.setConflicOfInterest(newConflictOfInterest);
			}
		} else {
			newProposal.setConflicOfInterest(newConflictOfInterest);
		}

		ComplianceInfo newComplianceInfo = new ComplianceInfo();
		if (proposalInfo != null && proposalInfo.has("ComplianceInfo")) {
			JsonNode complianceInfo = proposalInfo.get("ComplianceInfo");
			if (complianceInfo != null
					&& complianceInfo.has("InvolveUseOfHumanSubjects")) {
				switch (complianceInfo.get("InvolveUseOfHumanSubjects")
						.getTextValue()) {
				case "1":
					newComplianceInfo.setInvolveUseOfHumanSubjects(true);
					if (complianceInfo != null
							&& complianceInfo.has("IRBPending")) {
						switch (complianceInfo.get("IRBPending").getTextValue()) {
						case "1":
							newComplianceInfo.setIRBPending(false);
							if (complianceInfo != null
									&& complianceInfo.has("IRB")) {
								newComplianceInfo.setIRB(complianceInfo.get(
										"IRB").getTextValue());
							}
							break;
						case "2":
							newComplianceInfo.setIRBPending(true);
							break;
						default:
							break;
						}
					}
					break;
				case "2":
					newComplianceInfo.setInvolveUseOfHumanSubjects(false);
					break;
				default:
					break;
				}
			}

			if (complianceInfo != null
					&& complianceInfo.has("InvolveUseOfVertebrateAnimals")) {
				switch (complianceInfo.get("InvolveUseOfVertebrateAnimals")
						.getTextValue()) {
				case "1":
					newComplianceInfo.setInvolveUseOfVertebrateAnimals(true);
					if (complianceInfo != null
							&& complianceInfo.has("IACUCPending")) {
						switch (complianceInfo.get("IACUCPending")
								.getTextValue()) {
						case "1":
							newComplianceInfo.setIACUCPending(false);
							if (complianceInfo != null
									&& complianceInfo.has("IACUC")) {
								newComplianceInfo.setIACUC(complianceInfo.get(
										"IACUC").getTextValue());
							}
							break;
						case "2":
							newComplianceInfo.setIACUCPending(true);
							break;
						default:
							break;
						}
					}
					break;
				case "2":
					newComplianceInfo.setInvolveUseOfVertebrateAnimals(false);
					break;
				default:
					break;
				}
			}

			if (complianceInfo != null
					&& complianceInfo.has("InvolveBiosafetyConcerns")) {
				switch (complianceInfo.get("InvolveBiosafetyConcerns")
						.getTextValue()) {
				case "1":
					newComplianceInfo.setInvolveBiosafetyConcerns(true);
					if (complianceInfo != null
							&& complianceInfo.has("IBCPending")) {
						switch (complianceInfo.get("IBCPending").getTextValue()) {
						case "1":
							newComplianceInfo.setIBCPending(false);
							if (complianceInfo != null
									&& complianceInfo.has("IBC")) {
								newComplianceInfo.setIBC(complianceInfo.get(
										"IBC").getTextValue());
							}
							break;
						case "2":
							newComplianceInfo.setIBCPending(true);
							break;
						default:
							break;
						}
					}
					break;
				case "2":
					newComplianceInfo.setInvolveBiosafetyConcerns(false);
					break;
				default:
					break;
				}
			}

			if (complianceInfo != null
					&& complianceInfo
							.has("InvolveEnvironmentalHealthAndSafetyConcerns")) {
				switch (complianceInfo.get(
						"InvolveEnvironmentalHealthAndSafetyConcerns")
						.getTextValue()) {
				case "1":
					newComplianceInfo
							.setInvolveEnvironmentalHealthAndSafetyConcerns(true);
					break;
				case "2":
					newComplianceInfo
							.setInvolveEnvironmentalHealthAndSafetyConcerns(false);
					break;
				default:
					break;
				}
			}
		}
		// ComplianceInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getComplianceInfo().equals(newComplianceInfo)) {
				existingProposal.setComplianceInfo(newComplianceInfo);
			}
		} else {
			newProposal.setComplianceInfo(newComplianceInfo);
		}

		AdditionalInfo newAdditionalInfo = new AdditionalInfo();
		if (proposalInfo != null && proposalInfo.has("AdditionalInfo")) {
			JsonNode additionalInfo = proposalInfo.get("AdditionalInfo");
			if (additionalInfo != null
					&& additionalInfo.has("AnticipatesForeignNationalsPayment")) {
				switch (additionalInfo
						.get("AnticipatesForeignNationalsPayment")
						.getTextValue()) {
				case "1":
					newAdditionalInfo
							.setAnticipatesForeignNationalsPayment(true);
					break;
				case "2":
					newAdditionalInfo
							.setAnticipatesForeignNationalsPayment(false);
					break;
				default:
					break;
				}
			}

			if (additionalInfo != null
					&& additionalInfo.has("AnticipatesCourseReleaseTime")) {
				switch (additionalInfo.get("AnticipatesCourseReleaseTime")
						.getTextValue()) {
				case "1":
					newAdditionalInfo.setAnticipatesCourseReleaseTime(true);
					break;
				case "2":
					newAdditionalInfo.setAnticipatesCourseReleaseTime(false);
					break;
				default:
					break;
				}
			}

			if (additionalInfo != null
					&& additionalInfo
							.has("RelatedToCenterForAdvancedEnergyStudies")) {
				switch (additionalInfo.get(
						"RelatedToCenterForAdvancedEnergyStudies")
						.getTextValue()) {
				case "1":
					newAdditionalInfo
							.setRelatedToCenterForAdvancedEnergyStudies(true);
					break;
				case "2":
					newAdditionalInfo
							.setRelatedToCenterForAdvancedEnergyStudies(false);
					break;
				default:
					break;
				}
			}
		}
		// AdditionalInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getAdditionalInfo().equals(newAdditionalInfo)) {
				existingProposal.setAdditionalInfo(newAdditionalInfo);
			}
		} else {
			newProposal.setAdditionalInfo(newAdditionalInfo);
		}

		CollaborationInfo newCollaborationInfo = new CollaborationInfo();
		if (proposalInfo != null && proposalInfo.has("CollaborationInfo")) {
			JsonNode collaborationInfo = proposalInfo.get("CollaborationInfo");
			if (collaborationInfo != null
					&& collaborationInfo.has("InvolveNonFundedCollab")) {
				switch (collaborationInfo.get("InvolveNonFundedCollab")
						.getTextValue()) {
				case "1":
					newCollaborationInfo.setInvolveNonFundedCollab(true);
					if (collaborationInfo != null
							&& collaborationInfo.has("Collaborators")) {
						newCollaborationInfo
								.setInvolvedCollaborators(collaborationInfo
										.get("Collaborators").getTextValue());
					}
					break;
				case "2":
					newCollaborationInfo.setInvolveNonFundedCollab(false);
					break;
				default:
					break;
				}
			}
		}
		// CollaborationInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getCollaborationInfo().equals(
					newCollaborationInfo)) {
				existingProposal.setCollaborationInfo(newCollaborationInfo);
			}
		} else {
			newProposal.setCollaborationInfo(newCollaborationInfo);
		}

		ConfidentialInfo newConfidentialInfo = new ConfidentialInfo();
		if (proposalInfo != null && proposalInfo.has("ConfidentialInfo")) {
			JsonNode confidentialInfo = proposalInfo.get("ConfidentialInfo");
			if (confidentialInfo != null
					&& confidentialInfo.has("ContainConfidentialInformation")) {
				switch (confidentialInfo.get("ContainConfidentialInformation")
						.getTextValue()) {
				case "1":
					newConfidentialInfo.setContainConfidentialInformation(true);
					if (confidentialInfo != null
							&& confidentialInfo.has("OnPages")) {
						newConfidentialInfo.setOnPages(confidentialInfo.get(
								"OnPages").getTextValue());
					}
					if (confidentialInfo != null
							&& confidentialInfo.has("Patentable")) {
						newConfidentialInfo.setPatentable(confidentialInfo.get(
								"Patentable").getBooleanValue());
					}
					if (confidentialInfo != null
							&& confidentialInfo.has("Copyrightable")) {
						newConfidentialInfo.setCopyrightable(confidentialInfo
								.get("Copyrightable").getBooleanValue());
					}
					break;
				case "2":
					newConfidentialInfo
							.setContainConfidentialInformation(false);
					break;
				default:
					break;
				}
			}

			if (confidentialInfo != null
					&& confidentialInfo.has("InvolveIntellectualProperty")) {
				switch (confidentialInfo.get("InvolveIntellectualProperty")
						.getTextValue()) {
				case "1":
					newConfidentialInfo.setInvolveIntellectualProperty(true);
					break;
				case "2":
					newConfidentialInfo.setInvolveIntellectualProperty(false);
					break;
				default:
					break;
				}
			}
		}
		// ConfidentialInfo
		if (!proposalID.equals("0")) {
			if (!existingProposal.getConfidentialInfo().equals(
					newConfidentialInfo)) {
				existingProposal.setConfidentialInfo(newConfidentialInfo);
			}
		} else {
			newProposal.setConfidentialInfo(newConfidentialInfo);
		}

		// To hold all new Investigators list to get notified
		InvestigatorInfo addedInvestigators = new InvestigatorInfo();
		InvestigatorInfo existingInvestigators = new InvestigatorInfo();
		InvestigatorInfo deletedInvestigators = new InvestigatorInfo();

		if (proposalInfo != null && proposalInfo.has("InvestigatorInfo")) {
			if (!proposalID.equals("0")) {
				// Existing Investigator Info to compare
				existingInvestigators = oldProposal.getInvestigatorInfo();

				// MUST Clear all co-PI and Seniors
				existingProposal.getInvestigatorInfo().getCo_pi().clear();
				existingProposal.getInvestigatorInfo().getSeniorPersonnel()
						.clear();
			}

			String[] rows = proposalInfo.get("InvestigatorInfo").getTextValue()
					.split("#!#");

			InvestigatorInfo newInvestigatorInfo = new InvestigatorInfo();

			for (String col : rows) {
				String[] cols = col.split("!#!");
				InvestigatorRefAndPosition investigatorRefAndPosition = new InvestigatorRefAndPosition();
				ObjectId id = new ObjectId(cols[1]);
				UserProfile userRef = userProfileDAO
						.findUserDetailsByProfileID(id);
				investigatorRefAndPosition.setUserRef(userRef);
				investigatorRefAndPosition.setUserProfileId(cols[1]);
				investigatorRefAndPosition.setCollege(cols[2]);
				investigatorRefAndPosition.setDepartment(cols[3]);
				investigatorRefAndPosition.setPositionType(cols[4]);
				investigatorRefAndPosition.setPositionTitle(cols[5]);
				switch (cols[0]) {
				case "0":
					if (!proposalID.equals("0")) {
						if (!existingProposal.getInvestigatorInfo().getPi()
								.equals(investigatorRefAndPosition)) {
							existingProposal.getInvestigatorInfo().setPi(
									investigatorRefAndPosition);
							if (!addedInvestigators.getPi().equals(
									investigatorRefAndPosition)) {
								addedInvestigators
										.setPi(investigatorRefAndPosition);
							}
						}
					} else {
						newInvestigatorInfo.setPi(investigatorRefAndPosition);
					}
					break;
				case "1":
					if (!proposalID.equals("0")) {
						if (!existingProposal.getInvestigatorInfo().getCo_pi()
								.contains(investigatorRefAndPosition)) {
							existingProposal.getInvestigatorInfo().getCo_pi()
									.add(investigatorRefAndPosition);
						}
						if (!existingInvestigators.getCo_pi().contains(
								investigatorRefAndPosition)) {
							if (!addedInvestigators.getCo_pi().contains(
									investigatorRefAndPosition)) {
								addedInvestigators.getCo_pi().add(
										investigatorRefAndPosition);
							}
						}
					} else {
						newInvestigatorInfo.getCo_pi().add(
								investigatorRefAndPosition);
					}
					break;
				case "2":
					if (!proposalID.equals("0")) {
						if (!existingProposal.getInvestigatorInfo()
								.getSeniorPersonnel()
								.contains(investigatorRefAndPosition)) {
							existingProposal.getInvestigatorInfo()
									.getSeniorPersonnel()
									.add(investigatorRefAndPosition);
						}
						if (!existingInvestigators.getSeniorPersonnel()
								.contains(investigatorRefAndPosition)) {
							if (!addedInvestigators.getSeniorPersonnel()
									.contains(investigatorRefAndPosition)) {
								addedInvestigators.getSeniorPersonnel().add(
										investigatorRefAndPosition);
							}
						}
					} else {
						newInvestigatorInfo.getSeniorPersonnel().add(
								investigatorRefAndPosition);
					}
					break;
				default:
					break;
				}
			}

			// InvestigatorInfo
			if (proposalID.equals("0")) {
				newProposal.setInvestigatorInfo(newInvestigatorInfo);
				addedInvestigators = newInvestigatorInfo;
			} else {
				// TO see the deleted from addedInvestigators vs
				// existingInvestigators

				if (!existingProposal.getInvestigatorInfo().getPi()
						.equals(existingInvestigators.getPi())) {
					if (!deletedInvestigators.getPi().equals(
							existingInvestigators.getPi())) {
						deletedInvestigators.setPi(existingInvestigators
								.getPi());
					}
				}

				for (InvestigatorRefAndPosition coPI : existingInvestigators
						.getCo_pi()) {
					if (!existingProposal.getInvestigatorInfo().getCo_pi()
							.contains(coPI)) {
						deletedInvestigators.getCo_pi().add(coPI);
					}
				}

				for (InvestigatorRefAndPosition senior : existingInvestigators
						.getSeniorPersonnel()) {
					if (!existingProposal.getInvestigatorInfo()
							.getSeniorPersonnel().contains(senior)) {
						deletedInvestigators.getSeniorPersonnel().add(senior);
					}
				}
			}
		}

		// To hold all new Investigators list to get notified
		List<SignatureInfo> addedSignatures = new ArrayList<SignatureInfo>();

		if (proposalInfo != null && proposalInfo.has("SignatureInfo")) {
			String[] rows = proposalInfo.get("SignatureInfo").getTextValue()
					.split("#!#");

			List<SignatureInfo> newSignatureInfo = new ArrayList<SignatureInfo>();
			// UserProfileID!#!Signature!#!SignedDate!#!Note!#!FullName!#!PositionTitle!#!Delegated#!#
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");

			for (String col : rows) {
				String[] cols = col.split("!#!");
				SignatureInfo signatureInfo = new SignatureInfo();
				signatureInfo.setUserProfileId(cols[0]);
				signatureInfo.setSignature(cols[1]);
				signatureInfo.setSignedDate(format.parse(cols[2]));
				signatureInfo.setNote(cols[3]);
				signatureInfo.setFullName(cols[4]);
				signatureInfo.setPositionTitle(cols[5]);
				signatureInfo.setDelegated(Boolean.parseBoolean(cols[6]));

				boolean alreadyExist = false;
				for (SignatureInfo sign : existingProposal.getSignatureInfo()) {
					if (sign.equals(signatureInfo)) {
						alreadyExist = true;
						break;
					}
				}
				if (!alreadyExist) {
					newSignatureInfo.add(signatureInfo);
				}
			}
			// SignatureInfo
			addedSignatures = newSignatureInfo;
			if (!proposalID.equals("0")) {
				for (SignatureInfo signatureInfo : newSignatureInfo) {
					existingProposal.getSignatureInfo().add(signatureInfo);
				}
			} else {
				newProposal.setSignatureInfo(newSignatureInfo);
			}
		}

		OSPSectionInfo newOSPSectionInfo = new OSPSectionInfo();
		if (proposalInfo != null && proposalInfo.has("OSPSectionInfo")) {
			JsonNode oSPSectionInfo = proposalInfo.get("OSPSectionInfo");

			if (oSPSectionInfo != null && oSPSectionInfo.has("ListAgency")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getListAgency()
							.equals(oSPSectionInfo.get("ListAgency")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo()
								.setListAgency(
										oSPSectionInfo.get("ListAgency")
												.getTextValue());
					}
				}
			}

			FundingSource newFundingSource = new FundingSource();
			if (oSPSectionInfo != null && oSPSectionInfo.has("Federal")) {
				newFundingSource.setFederal(oSPSectionInfo.get("Federal")
						.getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("FederalFlowThrough")) {
				newFundingSource.setFederalFlowThrough(oSPSectionInfo.get(
						"FederalFlowThrough").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("StateOfIdahoEntity")) {
				newFundingSource.setStateOfIdahoEntity(oSPSectionInfo.get(
						"StateOfIdahoEntity").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("PrivateForProfit")) {
				newFundingSource.setPrivateForProfit(oSPSectionInfo.get(
						"PrivateForProfit").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("NonProfitOrganization")) {
				newFundingSource.setNonProfitOrganization(oSPSectionInfo.get(
						"NonProfitOrganization").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("NonIdahoStateEntity")) {
				newFundingSource.setNonIdahoStateEntity(oSPSectionInfo.get(
						"NonIdahoStateEntity").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("CollegeOrUniversity")) {
				newFundingSource.setCollegeOrUniversity(oSPSectionInfo.get(
						"CollegeOrUniversity").getBooleanValue());
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("LocalEntity")) {
				newFundingSource.setLocalEntity(oSPSectionInfo.get(
						"LocalEntity").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("NonIdahoLocalEntity")) {
				newFundingSource.setNonIdahoLocalEntity(oSPSectionInfo.get(
						"NonIdahoLocalEntity").getBooleanValue());
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("TirbalGovernment")) {
				newFundingSource.setTirbalGovernment(oSPSectionInfo.get(
						"TirbalGovernment").getBooleanValue());
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("Foreign")) {
				newFundingSource.setForeign(oSPSectionInfo.get("Foreign")
						.getBooleanValue());
			}

			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo().getFundingSource()
						.equals(newFundingSource)) {
					existingProposal.getoSPSectionInfo().setFundingSource(
							newFundingSource);
				}
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("CFDANo")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getCFDANo()
							.equals(oSPSectionInfo.get("CFDANo").getTextValue())) {
						existingProposal.getoSPSectionInfo().setCFDANo(
								oSPSectionInfo.get("CFDANo").getTextValue());
					}
				}
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("ProgramNo")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getProgramNo()
							.equals(oSPSectionInfo.get("ProgramNo")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo().setProgramNo(
								oSPSectionInfo.get("ProgramNo").getTextValue());
					}
				}
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("ProgramTitle")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getProgramTitle()
							.equals(oSPSectionInfo.get("ProgramTitle")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo().setProgramTitle(
								oSPSectionInfo.get("ProgramTitle")
										.getTextValue());
					}
				}

				Recovery newRecovery = new Recovery();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("FullRecovery")) {
					newRecovery.setFullRecovery(oSPSectionInfo.get(
							"FullRecovery").getBooleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NoRecoveryNormalSponsorPolicy")) {
					newRecovery.setNoRecoveryNormalSponsorPolicy(oSPSectionInfo
							.get("NoRecoveryNormalSponsorPolicy")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NoRecoveryInstitutionalWaiver")) {
					newRecovery.setNoRecoveryInstitutionalWaiver(oSPSectionInfo
							.get("NoRecoveryInstitutionalWaiver")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo
								.has("LimitedRecoveryNormalSponsorPolicy")) {
					newRecovery
							.setLimitedRecoveryNormalSponsorPolicy(oSPSectionInfo
									.get("LimitedRecoveryNormalSponsorPolicy")
									.getBooleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo
								.has("LimitedRecoveryInstitutionalWaiver")) {
					newRecovery
							.setLimitedRecoveryInstitutionalWaiver(oSPSectionInfo
									.get("LimitedRecoveryInstitutionalWaiver")
									.getBooleanValue());
				}
				if (!proposalID.equals("0")) {
					if (!existingProposal.getoSPSectionInfo().getRecovery()
							.equals(newRecovery)) {
						existingProposal.getoSPSectionInfo().setRecovery(
								newRecovery);
					}
				}

				BaseInfo newBaseInfo = new BaseInfo();
				if (oSPSectionInfo != null && oSPSectionInfo.has("MTDC")) {
					newBaseInfo.setMTDC(oSPSectionInfo.get("MTDC")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("TDC")) {
					newBaseInfo.setTDC(oSPSectionInfo.get("TDC")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("TC")) {
					newBaseInfo.setTC(oSPSectionInfo.get("TC")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("Other")) {
					newBaseInfo.setOther(oSPSectionInfo.get("Other")
							.getBooleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NotApplicable")) {
					newBaseInfo.setNotApplicable(oSPSectionInfo.get(
							"NotApplicable").getBooleanValue());
				}
				if (!proposalID.equals("0")) {
					if (!existingProposal.getoSPSectionInfo().getBaseInfo()
							.equals(newBaseInfo)) {
						existingProposal.getoSPSectionInfo().setBaseInfo(
								newBaseInfo);
					}
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("IsPISalaryIncluded")) {
					switch (oSPSectionInfo.get("IsPISalaryIncluded")
							.getTextValue()) {
					case "1":
						newOSPSectionInfo.setPISalaryIncluded(true);
						break;
					case "2":
						newOSPSectionInfo.setPISalaryIncluded(false);
						break;
					default:
						break;
					}
				}

				if (!proposalID.equals("0")) {
					if (existingProposal.getoSPSectionInfo()
							.isPISalaryIncluded() != newOSPSectionInfo
							.isPISalaryIncluded()) {
						existingProposal.getoSPSectionInfo()
								.setPISalaryIncluded(
										newOSPSectionInfo.isPISalaryIncluded());
					}
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("PISalary")) {
					if (!proposalID.equals("0")) {
						if (existingProposal.getoSPSectionInfo().getPISalary() != Double
								.parseDouble(oSPSectionInfo.get("PISalary")
										.getTextValue())) {
							existingProposal.getoSPSectionInfo().setPISalary(
									Double.parseDouble(oSPSectionInfo.get(
											"PISalary").getTextValue()));
						}
					}
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("PIFringe")) {
					if (existingProposal.getoSPSectionInfo().getPIFringe() != Double
							.parseDouble(oSPSectionInfo.get("PIFringe")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo().setPIFringe(
								Double.parseDouble(oSPSectionInfo.get(
										"PIFringe").getTextValue()));
					}
				} else {
					newOSPSectionInfo.setPIFringe(Double
							.parseDouble(oSPSectionInfo.get("PIFringe")
									.getTextValue()));
				}
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("DepartmentId")) {
				if (oSPSectionInfo != null && oSPSectionInfo.has("PIFringe")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getDepartmentId()
							.equals(oSPSectionInfo.get("DepartmentId")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo().setDepartmentId(
								oSPSectionInfo.get("DepartmentId")
										.getTextValue());
					}
				} else {
					newOSPSectionInfo.setDepartmentId(oSPSectionInfo.get(
							"DepartmentId").getTextValue());
				}
			}

			BaseOptions newBaseOptions = new BaseOptions();

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("InstitutionalCostDocumented")) {
				switch (oSPSectionInfo.get("InstitutionalCostDocumented")
						.getTextValue()) {
				case "1":
					newBaseOptions.setYes(true);
					break;
				case "2":
					newBaseOptions.setNo(true);
					break;
				case "3":
					newBaseOptions.setNotApplicable(true);
					break;
				default:
					break;
				}
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getInstitutionalCostDocumented()
						.equals(newBaseOptions)) {
					existingProposal.getoSPSectionInfo()
							.setInstitutionalCostDocumented(newBaseOptions);
				}
			}

			newBaseOptions = new BaseOptions();
			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("ThirdPartyCostDocumented")) {
				switch (oSPSectionInfo.get("ThirdPartyCostDocumented")
						.getTextValue()) {
				case "1":
					newBaseOptions.setYes(true);
					break;
				case "2":
					newBaseOptions.setNo(true);
					break;
				case "3":
					newBaseOptions.setNotApplicable(true);
					break;
				default:
					break;
				}
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getThirdPartyCostDocumented().equals(newBaseOptions)) {
					existingProposal.getoSPSectionInfo()
							.setThirdPartyCostDocumented(newBaseOptions);
				}
			}

			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("IsAnticipatedSubRecipients")) {
				switch (oSPSectionInfo.get("IsAnticipatedSubRecipients")
						.getTextValue()) {
				case "1":
					newOSPSectionInfo.setAnticipatedSubRecipients(true);
					if (oSPSectionInfo != null
							&& oSPSectionInfo
									.has("AnticipatedSubRecipientsNames")) {
						newOSPSectionInfo
								.setAnticipatedSubRecipientsNames(oSPSectionInfo
										.get("AnticipatedSubRecipientsNames")
										.getTextValue());
					}
					break;
				case "2":
					newOSPSectionInfo.setAnticipatedSubRecipients(false);
					break;
				default:
					break;
				}
			}

			if (!proposalID.equals("0")) {
				if (existingProposal.getoSPSectionInfo()
						.isAnticipatedSubRecipients() != newOSPSectionInfo
						.isAnticipatedSubRecipients()) {
					existingProposal.getoSPSectionInfo()
							.setAnticipatedSubRecipients(
									newOSPSectionInfo
											.isAnticipatedSubRecipients());
				}

				if (existingProposal.getoSPSectionInfo()
						.getAnticipatedSubRecipientsNames() != null) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getAnticipatedSubRecipientsNames()
							.equals(newOSPSectionInfo
									.getAnticipatedSubRecipientsNames())) {
						existingProposal
								.getoSPSectionInfo()
								.setAnticipatedSubRecipientsNames(
										newOSPSectionInfo
												.getAnticipatedSubRecipientsNames());
					}
				} else {
					existingProposal
							.getoSPSectionInfo()
							.setAnticipatedSubRecipientsNames(
									newOSPSectionInfo
											.getAnticipatedSubRecipientsNames());
				}
			}

			BasePIEligibilityOptions newBasePIEligibilityOptions = new BasePIEligibilityOptions();
			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("PIEligibilityWaiver")) {
				switch (oSPSectionInfo.get("PIEligibilityWaiver")
						.getTextValue()) {
				case "1":
					newBasePIEligibilityOptions.setYes(true);
					break;
				case "2":
					newBasePIEligibilityOptions.setNo(true);
					break;
				case "3":
					newBasePIEligibilityOptions.setNotApplicable(true);
					break;
				case "4":
					newBasePIEligibilityOptions.setThisProposalOnly(true);
					break;
				case "5":
					newBasePIEligibilityOptions.setBlanket(true);
					break;
				default:
					break;
				}
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getPIEligibilityWaiver()
						.equals(newBasePIEligibilityOptions)) {
					existingProposal
							.getoSPSectionInfo()
							.setPIEligibilityWaiver(newBasePIEligibilityOptions);
				}
			}

			newBaseOptions = new BaseOptions();
			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("ConflictOfInterestForms")) {
				switch (oSPSectionInfo.get("ConflictOfInterestForms")
						.getTextValue()) {
				case "1":
					newBaseOptions.setYes(true);
					break;
				case "2":
					newBaseOptions.setNo(true);
					break;
				case "3":
					newBaseOptions.setNotApplicable(true);
					break;
				default:
					break;
				}
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getConflictOfInterestForms().equals(newBaseOptions)) {
					existingProposal.getoSPSectionInfo()
							.setConflictOfInterestForms(newBaseOptions);
				}
			}

			newBaseOptions = new BaseOptions();
			if (oSPSectionInfo != null
					&& oSPSectionInfo.has("ExcludedPartyListChecked")) {
				switch (oSPSectionInfo.get("ExcludedPartyListChecked")
						.getTextValue()) {
				case "1":
					newBaseOptions.setYes(true);
					break;
				case "2":
					newBaseOptions.setNo(true);
					break;
				case "3":
					newBaseOptions.setNotApplicable(true);
					break;
				default:
					break;
				}
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getExcludedPartyListChecked().equals(newBaseOptions)) {
					existingProposal.getoSPSectionInfo()
							.setExcludedPartyListChecked(newBaseOptions);
				}
			}
		}

		if (proposalInfo != null && proposalInfo.has("AppendixInfo")) {

			List<Appendix> appendixInfo = Arrays.asList(mapper.readValue(
					proposalInfo.get("AppendixInfo"), Appendix[].class));

			if (!proposalID.equals("0")) {
				// boolean alreadyExist = false;
				// for (Appendix appendix : existingProposal.getAppendices()) {
				// if (appendix.equals(newAppendix)) {
				// alreadyExist = true;
				// break;
				// }
				// }
				// if (!alreadyExist) {
				existingProposal.getAppendices().clear();
				for (Appendix appendix : appendixInfo) {
					Appendix newAppendix = new Appendix();
					newAppendix.setFilename(appendix.getFilename());
					newAppendix.setFilesize(appendix.getFilesize());

					existingProposal.getAppendices().add(newAppendix);
				}

				// }
			} else {
				for (Appendix appendix : appendixInfo) {
					Appendix newAppendix = new Appendix();
					newAppendix.setFilename(appendix.getFilename());
					newAppendix.setFilesize(appendix.getFilesize());

					newProposal.getAppendices().add(newAppendix);
				}
			}
		}

		if (proposalInfo != null && !proposalInfo.has("ProposalNo")
				&& proposalID.equals("0")) {
			newProposal.setProposalNo(proposalDAO.findLatestProposalNo() + 1);
		}

		String userProfileID = new String();
		String userName = new String();
		Boolean userIsAdmin = false;
		String userCollege = new String();
		String userDepartment = new String();
		String userPositionType = new String();
		String userPositionTitle = new String();
		if (root != null && root.has("gpmsCommonObj")) {
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
				userPositionType = commonObj.get("UserPositionType")
						.getTextValue();
			}
			if (commonObj != null && commonObj.has("UserPositionTitle")) {
				userPositionTitle = commonObj.get("UserPositionTitle")
						.getTextValue();
			}
		}

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);

		// Save the Proposal
		Proposal currentProposal = new Proposal();
		String authorUserName = authorProfile.getUserAccount().getUserName();
		String notificationMessage = new String();

		if (!proposalID.equals("0")) {
			// TODO get clone rather again calling db
			// Proposal oldProposal = proposalDAO
			// .findProposalByProposalID(proposalId);

			if (!existingProposal.equals(oldProposal)) {
				proposalDAO.updateProposal(existingProposal, authorProfile);
				currentProposal = existingProposal;

				// TODO update notification for all users but
				// need to check the duplicate notification cause there
				// going to be update as well as added as PI, Co-PI,
				// Senior?
				notificationMessage = "Updated by " + authorUserName + ".";
				NotifyAllExistingInvestigators(existingProposal.getId()
						.toString(), existingProposal.getProjectInfo()
						.getProjectTitle(), existingProposal,
						notificationMessage, "Proposal", false);
			}
		} else {
			proposalDAO.saveProposal(newProposal, authorProfile);
			currentProposal = newProposal;

			// TODO create notification for all users
			notificationMessage = "Created by " + authorUserName + ".";
			NotifyAllExistingInvestigators(newProposal.getId().toString(),
					newProposal.getProjectInfo().getProjectTitle(),
					newProposal, notificationMessage, "Proposal", false);
		}

		// Added Investigators Notify
		NotificationLog notification = new NotificationLog();
		InvestigatorRefAndPosition addedPI = addedInvestigators.getPi();
		String projectTitle = currentProposal.getProjectInfo()
				.getProjectTitle();
		if (addedPI.getUserRef().getId() != null) {
			notification = new NotificationLog();

			notification.setType("Investigator");
			notification.setAction("Added as PI by " + authorUserName + ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(addedPI.getUserProfileId());
			notification.setUsername(addedPI.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(addedPI.getCollege());
			notification.setDepartment(addedPI.getDepartment());
			notification.setPositionType(addedPI.getPositionType());
			notification.setPositionTitle(addedPI.getPositionTitle());
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition copi : addedInvestigators.getCo_pi()) {
			notification = new NotificationLog();
			notification.setType("Investigator");
			notification.setAction("Added as CO-PI by " + authorUserName + ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(copi.getUserProfileId());
			notification.setUsername(copi.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(copi.getCollege());
			notification.setDepartment(copi.getDepartment());
			notification.setPositionType(copi.getPositionType());
			notification.setPositionTitle(copi.getPositionTitle());
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition senior : addedInvestigators
				.getSeniorPersonnel()) {
			notification = new NotificationLog();
			notification.setType("Investigator");
			notification.setAction("Added as Senior Personnel by "
					+ authorUserName + ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(senior.getUserProfileId());
			notification.setUsername(senior.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(senior.getCollege());
			notification.setDepartment(senior.getDepartment());
			notification.setPositionType(senior.getPositionType());
			notification.setPositionTitle(senior.getPositionTitle());
			notificationDAO.save(notification);
		}

		// Deleted Users Notify
		InvestigatorRefAndPosition deletedPI = deletedInvestigators.getPi();
		if (!deletedPI.getUserProfileId().equals("")) {
			notification = new NotificationLog();

			notification.setType("Investigator");
			notification.setAction("Deleted as PI by " + authorUserName + ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(deletedPI.getUserProfileId());
			notification.setUsername(deletedPI.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(deletedPI.getCollege());
			notification.setDepartment(deletedPI.getDepartment());
			notification.setPositionType(deletedPI.getPositionType());
			notification.setPositionTitle(deletedPI.getPositionTitle());
			notification.setCritical(true);
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition copi : deletedInvestigators.getCo_pi()) {
			notification = new NotificationLog();
			notification.setType("Investigator");
			notification.setAction("Deleted as CO-PI by " + authorUserName
					+ ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(copi.getUserProfileId());
			notification.setUsername(copi.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(copi.getCollege());
			notification.setDepartment(copi.getDepartment());
			notification.setPositionType(copi.getPositionType());
			notification.setPositionTitle(copi.getPositionTitle());
			notification.setCritical(true);
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition senior : deletedInvestigators
				.getSeniorPersonnel()) {
			notification = new NotificationLog();
			notification.setType("Investigator");
			notification.setAction("Deleted as Senior Personnel by "
					+ authorUserName + ".");
			notification.setProposalId(currentProposal.getId().toString());
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(senior.getUserProfileId());
			notification.setUsername(senior.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(senior.getCollege());
			notification.setDepartment(senior.getDepartment());
			notification.setPositionType(senior.getPositionType());
			notification.setPositionTitle(senior.getPositionTitle());
			notification.setCritical(true);
			notificationDAO.save(notification);
		}

		// New Signatures Notify
		if (addedSignatures.size() != 0) {
			for (SignatureInfo signatureInfo : addedSignatures) {
				String signFullName = signatureInfo.getFullName();
				String position = signatureInfo.getPositionTitle();
				Date signedDate = signatureInfo.getSignedDate();
				notification = new NotificationLog();
				notification.setType("Signature");
				notification.setAction("Signed by " + signFullName + " as "
						+ position + ".");

				notification.setProposalId(currentProposal.getId().toString());
				notification.setProposalTitle(projectTitle);
				notification.setForAdmin(true);
				notificationDAO.save(notification);

				notification = new NotificationLog();
				notification.setType("Signature");
				notification.setAction("Signed by " + signFullName + " as "
						+ position + ".");
				notification.setProposalId(currentProposal.getId().toString());
				notification.setProposalTitle(projectTitle);
				notification.setUserProfileId(currentProposal
						.getInvestigatorInfo().getPi().getUserProfileId());
				notification.setUsername(currentProposal.getInvestigatorInfo()
						.getPi().getUserRef().getUserAccount().getUserName());
				notification.setCollege(currentProposal.getInvestigatorInfo()
						.getPi().getCollege());
				notification.setDepartment(currentProposal
						.getInvestigatorInfo().getPi().getDepartment());
				notification.setPositionType(currentProposal
						.getInvestigatorInfo().getPi().getPositionType());
				notification.setPositionTitle(currentProposal
						.getInvestigatorInfo().getPi().getPositionTitle());
				notificationDAO.save(notification);

				for (InvestigatorRefAndPosition copi : currentProposal
						.getInvestigatorInfo().getCo_pi()) {
					notification = new NotificationLog();
					notification.setType("Signature");
					notification.setAction("Signed by " + signFullName + " as "
							+ position + ".");
					notification.setProposalId(currentProposal.getId()
							.toString());
					notification.setProposalTitle(projectTitle);
					notification.setUserProfileId(copi.getUserProfileId());
					notification.setUsername(copi.getUserRef().getUserAccount()
							.getUserName());
					notification.setCollege(copi.getCollege());
					notification.setDepartment(copi.getDepartment());
					notification.setPositionType(copi.getPositionType());
					notification.setPositionTitle(copi.getPositionTitle());
					notificationDAO.save(notification);
				}

				for (InvestigatorRefAndPosition senior : currentProposal
						.getInvestigatorInfo().getSeniorPersonnel()) {
					notification = new NotificationLog();
					notification.setType("Signature");
					notification.setAction("Signed by " + signFullName + " as "
							+ position + ".");
					notification.setProposalId(currentProposal.getId()
							.toString());
					notification.setProposalTitle(projectTitle);
					notification.setUserProfileId(senior.getUserProfileId());
					notification.setUsername(senior.getUserRef()
							.getUserAccount().getUserName());
					notification.setCollege(senior.getCollege());
					notification.setDepartment(senior.getDepartment());
					notification.setPositionType(senior.getPositionType());
					notification.setPositionTitle(senior.getPositionTitle());
					notificationDAO.save(notification);
				}
			}
		}
		return Response.status(200).type(MediaType.APPLICATION_JSON)
				.entity(true).build();
	}

	private void NotifyAllExistingInvestigators(String proposalID,
			String projectTitle, Proposal existingProposal,
			String notificationMessage, String notificationType,
			boolean isCritical) {
		NotificationLog notification = new NotificationLog();
		if (isCritical) {
			notification.setCritical(true);
		}

		notification.setType(notificationType);
		notification.setAction(notificationMessage);
		notification.setProposalId(proposalID);
		notification.setProposalTitle(projectTitle);
		notificationDAO.save(notification);

		InvestigatorRefAndPosition newPI = existingProposal
				.getInvestigatorInfo().getPi();
		if (newPI.getUserRef().getId() != null) {
			notification = new NotificationLog();
			if (isCritical) {
				notification.setCritical(true);
			}
			notification.setType(notificationType);
			notification.setAction(notificationMessage);
			notification.setProposalId(proposalID);
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(newPI.getUserProfileId());
			notification.setUsername(newPI.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(newPI.getCollege());
			notification.setDepartment(newPI.getDepartment());
			notification.setPositionType(newPI.getPositionType());
			notification.setPositionTitle(newPI.getPositionTitle());
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition copi : existingProposal
				.getInvestigatorInfo().getCo_pi()) {
			notification = new NotificationLog();
			if (isCritical) {
				notification.setCritical(true);
			}
			notification.setType(notificationType);
			notification.setAction(notificationMessage);
			notification.setProposalId(proposalID);
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(copi.getUserProfileId());
			notification.setUsername(copi.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(copi.getCollege());
			notification.setDepartment(copi.getDepartment());
			notification.setPositionType(copi.getPositionType());
			notification.setPositionTitle(copi.getPositionTitle());
			notificationDAO.save(notification);
			notificationDAO.save(notification);
		}

		for (InvestigatorRefAndPosition senior : existingProposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			notification = new NotificationLog();
			if (isCritical) {
				notification.setCritical(true);
			}
			notification.setType(notificationType);
			notification.setAction(notificationMessage);
			notification.setProposalId(proposalID);
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(senior.getUserProfileId());
			notification.setUsername(senior.getUserRef().getUserAccount()
					.getUserName());
			notification.setCollege(senior.getCollege());
			notification.setDepartment(senior.getDepartment());
			notification.setPositionType(senior.getPositionType());
			notification.setPositionTitle(senior.getPositionTitle());
			notificationDAO.save(notification);
		}
	}

	@POST
	@Path("/CheckPermissionForAProposal")
	public Response checkUserPermissionForAProposal(String message)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("policyInfo")) {
			JsonNode policyInfo = root.get("policyInfo");
			if (policyInfo != null && policyInfo.isArray()) {
				Accesscontrol ac = new Accesscontrol();
				HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

				Multimap<String, String> subjectMap = ArrayListMultimap
						.create();
				Multimap<String, String> resourceMap = ArrayListMultimap
						.create();
				Multimap<String, String> actionMap = ArrayListMultimap.create();
				Multimap<String, String> environmentMap = ArrayListMultimap
						.create();
				for (JsonNode node : policyInfo) {
					String attributeName = node.path("attributeName").asText();
					String attributeValue = node.path("attributeValue")
							.asText();
					String attributeType = node.path("attributeType").asText();
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

				String userProfileID = new String();
				String userName = new String();
				Boolean userIsAdmin = false;
				String userCollege = new String();
				String userDepartment = new String();
				String userPositionType = new String();
				String userPositionTitle = new String();

				if (root != null && root.has("gpmsCommonObj")) {
					JsonNode commonObj = root.get("gpmsCommonObj");
					if (commonObj != null && commonObj.has("UserProfileID")) {
						userProfileID = commonObj.get("UserProfileID")
								.getTextValue();
					}
					if (commonObj != null && commonObj.has("UserName")) {
						userName = commonObj.get("UserName").getTextValue();
					}
					if (commonObj != null && commonObj.has("UserIsAdmin")) {
						userIsAdmin = commonObj.get("UserIsAdmin")
								.getBooleanValue();
					}
					if (commonObj != null && commonObj.has("UserCollege")) {
						userCollege = commonObj.get("UserCollege")
								.getTextValue();
					}
					if (commonObj != null && commonObj.has("UserDepartment")) {
						userDepartment = commonObj.get("UserDepartment")
								.getTextValue();
					}
					if (commonObj != null && commonObj.has("UserPositionType")) {
						userPositionType = commonObj.get("UserPositionType")
								.getTextValue();
					}
					if (commonObj != null && commonObj.has("UserPositionTitle")) {
						userPositionTitle = commonObj.get("UserPositionTitle")
								.getTextValue();
					}
				}

				// TODO only check this for required not all XACML call
				if (root != null && root.has("proposalId")) {
					String proposalId = new String();
					JsonNode proposal_Id = root.get("proposalId");
					proposalId = proposal_Id.getTextValue();
					if (!proposalId.equals("")) {
						ObjectId id = new ObjectId(proposalId);
						Proposal proposal = proposalDAO
								.findProposalByProposalID(id);
						resourceMap.put("status", proposal.getProposalStatus()
								.get(0).toString());
						attrMap.put("Resource", resourceMap);
					}
				}

				// Need to add Environment to detect the Campus or outside
				// network
				// network-type

				// Device type
				// device-type

				// String decision = ac.getXACMLdecision(attrMap);
				// if (decision.equals("Permit")) {
				return Response.status(200).type(MediaType.APPLICATION_JSON)
						.entity(true).build();
				// } else {
				// return Response.status(403)
				// .type(MediaType.APPLICATION_JSON)
				// .entity("Your permission is: " + decision).build();
				// }
			} else {
				return Response.status(403).type(MediaType.APPLICATION_JSON)
						.entity("No User Permission Attributes are send!")
						.build();
			}
		} else {
			return Response.status(403).type(MediaType.APPLICATION_JSON)
					.entity("No User Permission Attributes are send!").build();
		}
	}
}
