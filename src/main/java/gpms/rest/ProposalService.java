package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.accesscontrol.Accesscontrol;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.AdditionalInfo;
import gpms.model.Appendix;
import gpms.model.ApprovalType;
import gpms.model.ArchiveType;
import gpms.model.AuditLogInfo;
import gpms.model.BaseInfo;
import gpms.model.BaseOptions;
import gpms.model.BasePIEligibilityOptions;
import gpms.model.CollaborationInfo;
import gpms.model.ComplianceInfo;
import gpms.model.ConfidentialInfo;
import gpms.model.ConflictOfInterest;
import gpms.model.CostShareInfo;
import gpms.model.DeleteType;
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
import gpms.model.ReadyType;
import gpms.model.Recovery;
import gpms.model.SignatureInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.Status;
import gpms.model.SubmitType;
import gpms.model.TypeOfRequest;
import gpms.model.UniversityCommitments;
import gpms.model.UserAccount;
import gpms.model.UserProfile;
import gpms.model.WithdrawType;
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
import org.glassfish.jersey.media.sse.OutboundEvent;
import org.mongodb.morphia.Morphia;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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
			offset = root.get("offset").intValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").intValue();
		}

		if (root != null && root.has("proposalBindObj")) {

			JsonNode proposalObj = root.get("proposalBindObj");
			if (proposalObj != null && proposalObj.has("ProjectTitle")) {
				projectTitle = proposalObj.get("ProjectTitle").textValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").textValue();
			}

			if (proposalObj != null && proposalObj.has("ReceivedOnFrom")) {
				receivedOnFrom = proposalObj.get("ReceivedOnFrom").textValue();
			}

			if (proposalObj != null && proposalObj.has("ReceivedOnTo")) {
				receivedOnTo = proposalObj.get("ReceivedOnTo").textValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").textValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").textValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus").textValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").textValue();
			}
		}

		proposals = proposalDAO.findAllForProposalGrid(offset, limit,
				projectTitle, usernameBy, receivedOnFrom, receivedOnTo,
				totalCostsFrom, totalCostsTo, proposalStatus, userRole);

		return proposals;
	}

	@POST
	@Path("/GetUserProposalsList")
	public String produceUserProposalsJSON(String message)
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
			offset = root.get("offset").intValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").intValue();
		}

		if (root != null && root.has("proposalBindObj")) {
			JsonNode proposalObj = root.get("proposalBindObj");
			if (proposalObj != null && proposalObj.has("ProjectTitle")) {
				projectTitle = proposalObj.get("ProjectTitle").textValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").textValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnFrom")) {
				submittedOnFrom = proposalObj.get("SubmittedOnFrom")
						.textValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnTo")) {
				submittedOnTo = proposalObj.get("SubmittedOnTo").textValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").textValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").textValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus").textValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").textValue();
			}
		}

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

		proposals = proposalDAO.findUserProposalGrid(offset, limit,
				projectTitle, usernameBy, submittedOnFrom, submittedOnTo,
				totalCostsFrom, totalCostsTo, proposalStatus, userRole,
				userProfileID, userCollege, userDepartment, userPositionType,
				userPositionTitle);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				proposals);
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
				projectTitle = proposalObj.get("ProjectTitle").textValue();
			}

			if (proposalObj != null && proposalObj.has("UsernameBy")) {
				usernameBy = proposalObj.get("UsernameBy").textValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnFrom")) {
				submittedOnFrom = proposalObj.get("SubmittedOnFrom")
						.textValue();
			}

			if (proposalObj != null && proposalObj.has("SubmittedOnTo")) {
				submittedOnTo = proposalObj.get("SubmittedOnTo").textValue();
			}

			if (proposalObj != null && proposalObj.has("TotalCostsFrom")) {
				if (proposalObj.get("TotalCostsFrom").textValue() != null) {
					totalCostsFrom = Double.valueOf(proposalObj.get(
							"TotalCostsFrom").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("TotalCostsTo")) {
				if (proposalObj.get("TotalCostsTo").textValue() != null) {
					totalCostsTo = Double.valueOf(proposalObj.get(
							"TotalCostsTo").textValue());
				}
			}

			if (proposalObj != null && proposalObj.has("ProposalStatus")) {
				proposalStatus = proposalObj.get("ProposalStatus").textValue();
			}

			if (proposalObj != null && proposalObj.has("UserRole")) {
				userRole = proposalObj.get("UserRole").textValue();
			}
		}

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
			proposalId = root.get("proposalId").textValue();
		}

		if (root != null && root.has("proposalRoles")) {
			proposalRoles = root.get("proposalRoles").textValue();
		}

		if (root != null && root.has("proposalUserTitle")) {
			proposalUserTitle = root.get("proposalUserTitle").textValue();
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

			return Response.status(200).entity("true").build();
		} else {
			return Response.status(403).entity("DENY").build();
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
			proposalIds = root.get("proposalIds").textValue();
			proposals = proposalIds.split(",");
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
				true);
		return response;
	}

	@POST
	@Path("/GetProposalDetailsByProposalId")
	public String produceProposalDetailsByProposalId(String message)
			throws JsonProcessingException, IOException {
		Proposal proposal = new Proposal();
		String proposalId = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").textValue();
		}

		ObjectId id = new ObjectId(proposalId);
		proposal = proposalDAO.findProposalByProposalID(id);

		// Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd")
		// .excludeFieldsWithoutExposeAnnotation().setPrettyPrinting()
		// .create();
		// return gson.toJson(proposal, Proposal.class);

		// mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

		return mapper.setDateFormat(formatter).writerWithDefaultPrettyPrinter()
				.writeValueAsString(proposal);
	}

	@POST
	@Path("/GetProposalAuditLogList")
	public String produceProposalAuditLogJSON(String message)
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
			offset = root.get("offset").intValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").intValue();
		}

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").textValue();
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

			if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnTo")) {
				activityOnTo = auditLogBindObj.get("ActivityOnTo").textValue();
			}
		}

		ObjectId id = new ObjectId(proposalId);

		proposalAuditLogs = proposalDAO.findAllForProposalAuditLogGrid(offset,
				limit, id, action, auditedBy, activityOnFrom, activityOnTo);

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);

		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				proposalAuditLogs);
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
			proposalId = root.get("proposalId").textValue();
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

			if (auditLogBindObj != null && auditLogBindObj.has("ActivityOnTo")) {
				activityOnTo = auditLogBindObj.get("ActivityOnTo").textValue();
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
				proposalID = proposalUniqueObj.get("ProposalID").textValue();
			}

			if (proposalUniqueObj != null
					&& proposalUniqueObj.has("NewProjectTitle")) {
				newProjectTitle = proposalUniqueObj.get("NewProjectTitle")
						.textValue();
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

		Boolean irbApprovalRequired = false;

		// String response = new String();

		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(message);
		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").textValue();
		}
		if (root != null && root.has("irbApprovalRequired")) {
			irbApprovalRequired = Boolean.parseBoolean(root.get(
					"irbApprovalRequired").textValue());
		}

		ObjectId id = new ObjectId(proposalId);

		List<SignatureInfo> signatures = proposalDAO
				.findAllSignatureForAProposal(id, irbApprovalRequired);

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
				proposalID = proposalId.textValue();
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

									switch (buttonType.textValue()) {
									case "Withdraw":
										if (!proposalID.equals("0")) {
											if (existingProposal
													.getResearchAdministratorWithdraw() == WithdrawType.NOTWITHDRAWN
													&& existingProposal
															.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
													&& proposalUserTitle
															.textValue()
															.equals("University Research Administrator")) {
												existingProposal
														.setResearchAdministratorWithdraw(WithdrawType.WITHDRAWN);

												// Proposal Status
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
													.getResearchDirectorArchived() == ArchiveType.NOTARCHIVED
													&& existingProposal
															.getResearchAdministratorSubmission() == SubmitType.SUBMITTED
													&& proposalUserTitle
															.textValue()
															.equals("University Research Director")) {
												existingProposal
														.setResearchDirectorArchived(ArchiveType.ARCHIVED);

												// Proposal Status
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

									if (root != null
											&& root.has("gpmsCommonObj")) {
										JsonNode commonObj = root
												.get("gpmsCommonObj");
										if (commonObj != null
												&& commonObj
														.has("UserProfileID")) {
											userProfileID = commonObj.get(
													"UserProfileID")
													.textValue();
										}
										if (commonObj != null
												&& commonObj.has("UserName")) {
											userName = commonObj
													.get("UserName")
													.textValue();
										}
										if (commonObj != null
												&& commonObj.has("UserIsAdmin")) {
											userIsAdmin = Boolean
													.parseBoolean(commonObj
															.get("UserIsAdmin")
															.textValue());
										}
										if (commonObj != null
												&& commonObj.has("UserCollege")) {
											userCollege = commonObj.get(
													"UserCollege").textValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserDepartment")) {
											userDepartment = commonObj.get(
													"UserDepartment")
													.textValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserPositionType")) {
											userPositionType = commonObj.get(
													"UserPositionType")
													.textValue();
										}
										if (commonObj != null
												&& commonObj
														.has("UserPositionTitle")) {
											userPositionTitle = commonObj.get(
													"UserPositionTitle")
													.textValue();
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
				proposalID = proposalInfo.get("ProposalID").textValue();
				if (!proposalID.equals("0")) {
					proposalId = new ObjectId(proposalID);
					existingProposal = proposalDAO
							.findProposalByProposalID(proposalId);
					// using our serializable method for cloning
					oldProposal = SerializationHelper
							.cloneThroughSerialize(existingProposal);
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
										.textValue())) {
							existingProposal.getProjectInfo()
									.setProjectTitle(
											projectInfo.get("ProjectTitle")
													.textValue());
						}
					} else {
						newProjectInfo.setProjectTitle(projectInfo.get(
								"ProjectTitle").textValue());
					}
				}

				if (projectInfo != null && projectInfo.has("ProjectType")) {
					ProjectType projectType = new ProjectType();
					switch (projectInfo.get("ProjectType").textValue()) {
					case "1":
						projectType.setResearchBasic(true);
						break;
					case "2":
						projectType.setResearchApplied(true);
						break;
					case "3":
						projectType.setResearchDevelopment(true);
						break;
					case "4":
						projectType.setInstruction(true);
						break;
					case "5":
						projectType.setOtherSponsoredActivity(true);
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
					switch (projectInfo.get("TypeOfRequest").textValue()) {
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
						if (!existingProposal.getProjectInfo()
								.getTypeOfRequest().equals(typeOfRequest)) {
							existingProposal.getProjectInfo().setTypeOfRequest(
									typeOfRequest);
						}
					} else {
						newProjectInfo.setTypeOfRequest(typeOfRequest);
					}
				}

				if (projectInfo != null && projectInfo.has("ProjectLocation")) {
					ProjectLocation projectLocation = new ProjectLocation();
					switch (projectInfo.get("ProjectLocation").textValue()) {
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
						if (!existingProposal.getProjectInfo()
								.getProjectLocation().equals(projectLocation)) {
							existingProposal.getProjectInfo()
									.setProjectLocation(projectLocation);
						}
					} else {
						newProjectInfo.setProjectLocation(projectLocation);
					}
				}

				if (projectInfo != null && projectInfo.has("DueDate")) {
					Date dueDate = formatter.parse(projectInfo.get("DueDate")
							.textValue());
					if (!proposalID.equals("0")) {
						if (!existingProposal.getProjectInfo().getDueDate()
								.equals(dueDate)) {
							existingProposal.getProjectInfo().setDueDate(
									dueDate);
						}
					} else {
						newProjectInfo.setDueDate(dueDate);
					}
				}

				ProjectPeriod projectPeriod = new ProjectPeriod();

				if (projectInfo != null && projectInfo.has("ProjectPeriodFrom")) {
					Date periodFrom = formatter.parse(projectInfo.get(
							"ProjectPeriodFrom").textValue());
					projectPeriod.setFrom(periodFrom);
				}

				if (projectInfo != null && projectInfo.has("ProjectPeriodTo")) {
					Date periodTo = formatter.parse(projectInfo.get(
							"ProjectPeriodTo").textValue());
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
			if (proposalInfo != null
					&& proposalInfo.has("SponsorAndBudgetInfo")) {
				JsonNode sponsorAndBudgetInfo = proposalInfo
						.get("SponsorAndBudgetInfo");
				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("GrantingAgency")) {
					for (String grantingAgency : sponsorAndBudgetInfo
							.get("GrantingAgency").textValue().split(", ")) {
						newSponsorAndBudgetInfo.getGrantingAgency().add(
								grantingAgency);
					}
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("DirectCosts")) {
					newSponsorAndBudgetInfo.setDirectCosts(Double
							.parseDouble(sponsorAndBudgetInfo
									.get("DirectCosts").textValue()));
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("FACosts")) {
					newSponsorAndBudgetInfo.setFaCosts(Double
							.parseDouble(sponsorAndBudgetInfo.get("FACosts")
									.textValue()));
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("TotalCosts")) {
					newSponsorAndBudgetInfo.setTotalCosts(Double
							.parseDouble(sponsorAndBudgetInfo.get("TotalCosts")
									.textValue()));
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("FARate")) {
					newSponsorAndBudgetInfo.setFaRate(Double
							.parseDouble(sponsorAndBudgetInfo.get("FARate")
									.textValue()));
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
							.textValue()) {
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
					switch (costShareInfo.get("ThirdPartyCommitted")
							.textValue()) {
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
				if (!existingProposal.getCostShareInfo().equals(
						newCostShareInfo)) {
					existingProposal.setCostShareInfo(newCostShareInfo);
				}
			} else {
				newProposal.setCostShareInfo(newCostShareInfo);
			}

			UniversityCommitments newUnivCommitments = new UniversityCommitments();
			if (proposalInfo != null && proposalInfo.has("UnivCommitments")) {
				JsonNode univCommitments = proposalInfo.get("UnivCommitments");
				if (univCommitments != null
						&& univCommitments
								.has("NewRenovatedFacilitiesRequired")) {
					switch (univCommitments.get(
							"NewRenovatedFacilitiesRequired").textValue()) {
					case "1":
						newUnivCommitments
								.setNewRenovatedFacilitiesRequired(true);
						break;
					case "2":
						newUnivCommitments
								.setNewRenovatedFacilitiesRequired(false);
						break;
					default:
						break;
					}
				}

				if (univCommitments != null
						&& univCommitments.has("RentalSpaceRequired")) {
					switch (univCommitments.get("RentalSpaceRequired")
							.textValue()) {
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
						&& univCommitments
								.has("InstitutionalCommitmentRequired")) {
					switch (univCommitments.get(
							"InstitutionalCommitmentRequired").textValue()) {
					case "1":
						newUnivCommitments
								.setInstitutionalCommitmentRequired(true);
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
					existingProposal
							.setUniversityCommitments(newUnivCommitments);
				}
			} else {
				newProposal.setUniversityCommitments(newUnivCommitments);
			}

			ConflictOfInterest newConflictOfInterest = new ConflictOfInterest();
			if (proposalInfo != null
					&& proposalInfo.has("ConflicOfInterestInfo")) {
				JsonNode conflicOfInterestInfo = proposalInfo
						.get("ConflicOfInterestInfo");
				if (conflicOfInterestInfo != null
						&& conflicOfInterestInfo.has("FinancialCOI")) {
					switch (conflicOfInterestInfo.get("FinancialCOI")
							.textValue()) {
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
							.textValue()) {
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
							.textValue()) {
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
					existingProposal
							.setConflicOfInterest(newConflictOfInterest);
				}
			} else {
				newProposal.setConflicOfInterest(newConflictOfInterest);
			}

			ComplianceInfo newComplianceInfo = new ComplianceInfo();
			Boolean irbApprovalRequired = false;
			if (proposalInfo != null && proposalInfo.has("ComplianceInfo")) {
				JsonNode complianceInfo = proposalInfo.get("ComplianceInfo");
				if (complianceInfo != null
						&& complianceInfo.has("InvolveUseOfHumanSubjects")) {
					switch (complianceInfo.get("InvolveUseOfHumanSubjects")
							.textValue()) {
					case "1":
						newComplianceInfo.setInvolveUseOfHumanSubjects(true);
						irbApprovalRequired = true;
						if (complianceInfo != null
								&& complianceInfo.has("IRBPending")) {
							switch (complianceInfo.get("IRBPending")
									.textValue()) {
							case "1":
								newComplianceInfo.setIrbPending(false);
								if (complianceInfo != null
										&& complianceInfo.has("IRB")) {
									newComplianceInfo.setIrb(complianceInfo
											.get("IRB").textValue());
								}
								break;
							case "2":
								newComplianceInfo.setIrbPending(true);
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
							.textValue()) {
					case "1":
						newComplianceInfo
								.setInvolveUseOfVertebrateAnimals(true);
						irbApprovalRequired = true;
						if (complianceInfo != null
								&& complianceInfo.has("IACUCPending")) {
							switch (complianceInfo.get("IACUCPending")
									.textValue()) {
							case "1":
								newComplianceInfo.setIacucPending(false);
								if (complianceInfo != null
										&& complianceInfo.has("IACUC")) {
									newComplianceInfo.setIacuc(complianceInfo
											.get("IACUC").textValue());
								}
								break;
							case "2":
								newComplianceInfo.setIacucPending(true);
								break;
							default:
								break;
							}
						}
						break;
					case "2":
						newComplianceInfo
								.setInvolveUseOfVertebrateAnimals(false);
						break;
					default:
						break;
					}
				}

				if (complianceInfo != null
						&& complianceInfo.has("InvolveBiosafetyConcerns")) {
					switch (complianceInfo.get("InvolveBiosafetyConcerns")
							.textValue()) {
					case "1":
						newComplianceInfo.setInvolveBiosafetyConcerns(true);
						irbApprovalRequired = true;
						if (complianceInfo != null
								&& complianceInfo.has("IBCPending")) {
							switch (complianceInfo.get("IBCPending")
									.textValue()) {
							case "1":
								newComplianceInfo.setIbcPending(false);
								if (complianceInfo != null
										&& complianceInfo.has("IBC")) {
									newComplianceInfo.setIbc(complianceInfo
											.get("IBC").textValue());
								}
								break;
							case "2":
								newComplianceInfo.setIbcPending(true);
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
							.textValue()) {
					case "1":
						newComplianceInfo
								.setInvolveEnvironmentalHealthAndSafetyConcerns(true);
						irbApprovalRequired = true;
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
				if (!existingProposal.getComplianceInfo().equals(
						newComplianceInfo)) {
					existingProposal.setComplianceInfo(newComplianceInfo);
					existingProposal
							.setIrbApprovalRequired(irbApprovalRequired);
				}
			} else {
				newProposal.setComplianceInfo(newComplianceInfo);
				newProposal.setIrbApprovalRequired(irbApprovalRequired);
			}

			AdditionalInfo newAdditionalInfo = new AdditionalInfo();
			if (proposalInfo != null && proposalInfo.has("AdditionalInfo")) {
				JsonNode additionalInfo = proposalInfo.get("AdditionalInfo");
				if (additionalInfo != null
						&& additionalInfo
								.has("AnticipatesForeignNationalsPayment")) {
					switch (additionalInfo.get(
							"AnticipatesForeignNationalsPayment").textValue()) {
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
							.textValue()) {
					case "1":
						newAdditionalInfo.setAnticipatesCourseReleaseTime(true);
						break;
					case "2":
						newAdditionalInfo
								.setAnticipatesCourseReleaseTime(false);
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
							.textValue()) {
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
				if (!existingProposal.getAdditionalInfo().equals(
						newAdditionalInfo)) {
					existingProposal.setAdditionalInfo(newAdditionalInfo);
				}
			} else {
				newProposal.setAdditionalInfo(newAdditionalInfo);
			}

			CollaborationInfo newCollaborationInfo = new CollaborationInfo();
			if (proposalInfo != null && proposalInfo.has("CollaborationInfo")) {
				JsonNode collaborationInfo = proposalInfo
						.get("CollaborationInfo");
				if (collaborationInfo != null
						&& collaborationInfo.has("InvolveNonFundedCollab")) {
					switch (collaborationInfo.get("InvolveNonFundedCollab")
							.textValue()) {
					case "1":
						newCollaborationInfo.setInvolveNonFundedCollab(true);
						if (collaborationInfo != null
								&& collaborationInfo.has("Collaborators")) {
							newCollaborationInfo
									.setInvolvedCollaborators(collaborationInfo
											.get("Collaborators").textValue());
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
				JsonNode confidentialInfo = proposalInfo
						.get("ConfidentialInfo");
				if (confidentialInfo != null
						&& confidentialInfo
								.has("ContainConfidentialInformation")) {
					switch (confidentialInfo.get(
							"ContainConfidentialInformation").textValue()) {
					case "1":
						newConfidentialInfo
								.setContainConfidentialInformation(true);
						if (confidentialInfo != null
								&& confidentialInfo.has("OnPages")) {
							newConfidentialInfo.setOnPages(confidentialInfo
									.get("OnPages").textValue());
						}
						if (confidentialInfo != null
								&& confidentialInfo.has("Patentable")) {
							newConfidentialInfo.setPatentable(confidentialInfo
									.get("Patentable").booleanValue());
						}
						if (confidentialInfo != null
								&& confidentialInfo.has("Copyrightable")) {
							newConfidentialInfo
									.setCopyrightable(confidentialInfo.get(
											"Copyrightable").booleanValue());
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
							.textValue()) {
					case "1":
						newConfidentialInfo
								.setInvolveIntellectualProperty(true);
						break;
					case "2":
						newConfidentialInfo
								.setInvolveIntellectualProperty(false);
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

			// OSP Section Info
			OSPSectionInfo newOSPSectionInfo = new OSPSectionInfo();
			if (proposalInfo != null && proposalInfo.has("OSPSectionInfo")) {
				JsonNode oSPSectionInfo = proposalInfo.get("OSPSectionInfo");

				// List Agency
				if (oSPSectionInfo != null && oSPSectionInfo.has("ListAgency")) {
					if (!proposalID.equals("0")) {
						if (!existingProposal
								.getOspSectionInfo()
								.getListAgency()
								.equals(oSPSectionInfo.get("ListAgency")
										.textValue())) {
							existingProposal.getOspSectionInfo().setListAgency(
									oSPSectionInfo.get("ListAgency")
											.textValue());
						}
					}
				}

				FundingSource newFundingSource = new FundingSource();
				if (oSPSectionInfo != null && oSPSectionInfo.has("Federal")) {
					newFundingSource.setFederal(oSPSectionInfo.get("Federal")
							.booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("FederalFlowThrough")) {
					newFundingSource.setFederalFlowThrough(oSPSectionInfo.get(
							"FederalFlowThrough").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("StateOfIdahoEntity")) {
					newFundingSource.setStateOfIdahoEntity(oSPSectionInfo.get(
							"StateOfIdahoEntity").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("PrivateForProfit")) {
					newFundingSource.setPrivateForProfit(oSPSectionInfo.get(
							"PrivateForProfit").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NonProfitOrganization")) {
					newFundingSource.setNonProfitOrganization(oSPSectionInfo
							.get("NonProfitOrganization").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NonIdahoStateEntity")) {
					newFundingSource.setNonIdahoStateEntity(oSPSectionInfo.get(
							"NonIdahoStateEntity").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("CollegeOrUniversity")) {
					newFundingSource.setCollegeOrUniversity(oSPSectionInfo.get(
							"CollegeOrUniversity").booleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("LocalEntity")) {
					newFundingSource.setLocalEntity(oSPSectionInfo.get(
							"LocalEntity").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NonIdahoLocalEntity")) {
					newFundingSource.setNonIdahoLocalEntity(oSPSectionInfo.get(
							"NonIdahoLocalEntity").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("TirbalGovernment")) {
					newFundingSource.setTirbalGovernment(oSPSectionInfo.get(
							"TirbalGovernment").booleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("Foreign")) {
					newFundingSource.setForeign(oSPSectionInfo.get("Foreign")
							.booleanValue());
				}

				// Funding Source
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getFundingSource().equals(newFundingSource)) {
						existingProposal.getOspSectionInfo().setFundingSource(
								newFundingSource);
					}
				}

				// CFDA No
				if (oSPSectionInfo != null && oSPSectionInfo.has("CFDANo")) {
					if (!proposalID.equals("0")) {
						if (!existingProposal
								.getOspSectionInfo()
								.getCfdaNo()
								.equals(oSPSectionInfo.get("CFDANo")
										.textValue())) {
							existingProposal.getOspSectionInfo().setCfdaNo(
									oSPSectionInfo.get("CFDANo").textValue());
						}
					}
				}

				// Program No
				if (oSPSectionInfo != null && oSPSectionInfo.has("ProgramNo")) {
					if (!proposalID.equals("0")) {
						if (!existingProposal
								.getOspSectionInfo()
								.getProgramNo()
								.equals(oSPSectionInfo.get("ProgramNo")
										.textValue())) {
							existingProposal.getOspSectionInfo()
									.setProgramNo(
											oSPSectionInfo.get("ProgramNo")
													.textValue());
						}
					}
				}

				// Program Title
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("ProgramTitle")) {
					if (!proposalID.equals("0")) {
						if (!existingProposal
								.getOspSectionInfo()
								.getProgramTitle()
								.equals(oSPSectionInfo.get("ProgramTitle")
										.textValue())) {
							existingProposal.getOspSectionInfo()
									.setProgramTitle(
											oSPSectionInfo.get("ProgramTitle")
													.textValue());
						}
					}
				}

				Recovery newRecovery = new Recovery();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("FullRecovery")) {
					newRecovery.setFullRecovery(oSPSectionInfo.get(
							"FullRecovery").booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NoRecoveryNormalSponsorPolicy")) {
					newRecovery.setNoRecoveryNormalSponsorPolicy(oSPSectionInfo
							.get("NoRecoveryNormalSponsorPolicy")
							.booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NoRecoveryInstitutionalWaiver")) {
					newRecovery.setNoRecoveryInstitutionalWaiver(oSPSectionInfo
							.get("NoRecoveryInstitutionalWaiver")
							.booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo
								.has("LimitedRecoveryNormalSponsorPolicy")) {
					newRecovery
							.setLimitedRecoveryNormalSponsorPolicy(oSPSectionInfo
									.get("LimitedRecoveryNormalSponsorPolicy")
									.booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo
								.has("LimitedRecoveryInstitutionalWaiver")) {
					newRecovery
							.setLimitedRecoveryInstitutionalWaiver(oSPSectionInfo
									.get("LimitedRecoveryInstitutionalWaiver")
									.booleanValue());
				}
				// Recovery
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo().getRecovery()
							.equals(newRecovery)) {
						existingProposal.getOspSectionInfo().setRecovery(
								newRecovery);
					}
				}

				BaseInfo newBaseInfo = new BaseInfo();
				if (oSPSectionInfo != null && oSPSectionInfo.has("MTDC")) {
					newBaseInfo.setMtdc(oSPSectionInfo.get("MTDC")
							.booleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("TDC")) {
					newBaseInfo
							.setTdc(oSPSectionInfo.get("TDC").booleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("TC")) {
					newBaseInfo.setTc(oSPSectionInfo.get("TC").booleanValue());
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("Other")) {
					newBaseInfo.setOther(oSPSectionInfo.get("Other")
							.booleanValue());
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("NotApplicable")) {
					newBaseInfo.setNotApplicable(oSPSectionInfo.get(
							"NotApplicable").booleanValue());
				}

				// Base Info
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo().getBaseInfo()
							.equals(newBaseInfo)) {
						existingProposal.getOspSectionInfo().setBaseInfo(
								newBaseInfo);
					}
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("IsPISalaryIncluded")) {
					switch (oSPSectionInfo.get("IsPISalaryIncluded")
							.textValue()) {
					case "1":
						newOSPSectionInfo.setPiSalaryIncluded(true);
						break;
					case "2":
						newOSPSectionInfo.setPiSalaryIncluded(false);
						break;
					default:
						break;
					}
				}

				// PI Salary Included
				if (!proposalID.equals("0")) {
					if (existingProposal.getOspSectionInfo()
							.isPiSalaryIncluded() != newOSPSectionInfo
							.isPiSalaryIncluded()) {
						existingProposal.getOspSectionInfo()
								.setPiSalaryIncluded(
										newOSPSectionInfo.isPiSalaryIncluded());
					}
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("PISalary")) {
					// PI Salary
					if (!proposalID.equals("0")) {
						if (existingProposal.getOspSectionInfo().getPiSalary() != Double
								.parseDouble(oSPSectionInfo.get("PISalary")
										.textValue())) {
							existingProposal.getOspSectionInfo().setPiSalary(
									Double.parseDouble(oSPSectionInfo.get(
											"PISalary").textValue()));
						}
					}
				}

				if (oSPSectionInfo != null && oSPSectionInfo.has("PIFringe")) {
					// PI Fringe
					if (!proposalID.equals("0")) {
						if (existingProposal.getOspSectionInfo().getPiFringe() != Double
								.parseDouble(oSPSectionInfo.get("PIFringe")
										.textValue())) {
							existingProposal.getOspSectionInfo().setPiFringe(
									Double.parseDouble(oSPSectionInfo.get(
											"PIFringe").textValue()));
						}
					}
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("DepartmentId")) {
					// Department Id
					if (!proposalID.equals("0")) {
						if (!existingProposal
								.getOspSectionInfo()
								.getDepartmentId()
								.equals(oSPSectionInfo.get("DepartmentId")
										.textValue())) {
							existingProposal.getOspSectionInfo()
									.setDepartmentId(
											oSPSectionInfo.get("DepartmentId")
													.textValue());
						}
					}
				}

				BaseOptions newBaseOptions = new BaseOptions();

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("InstitutionalCostDocumented")) {
					switch (oSPSectionInfo.get("InstitutionalCostDocumented")
							.textValue()) {
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
				// Institutional Cost Documented
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getInstitutionalCostDocumented()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setInstitutionalCostDocumented(newBaseOptions);
					}
				}

				newBaseOptions = new BaseOptions();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("ThirdPartyCostDocumented")) {
					switch (oSPSectionInfo.get("ThirdPartyCostDocumented")
							.textValue()) {
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

				// Third Party Cost Documented
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getThirdPartyCostDocumented()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setThirdPartyCostDocumented(newBaseOptions);
					}
				}

				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("IsAnticipatedSubRecipients")) {
					switch (oSPSectionInfo.get("IsAnticipatedSubRecipients")
							.textValue()) {
					case "1":
						newOSPSectionInfo.setAnticipatedSubRecipients(true);
						if (oSPSectionInfo != null
								&& oSPSectionInfo
										.has("AnticipatedSubRecipientsNames")) {
							newOSPSectionInfo
									.setAnticipatedSubRecipientsNames(oSPSectionInfo
											.get("AnticipatedSubRecipientsNames")
											.textValue());
						}
						break;
					case "2":
						newOSPSectionInfo.setAnticipatedSubRecipients(false);
						break;
					default:
						break;
					}
				}

				// Is Anticipated SubRecipients
				if (!proposalID.equals("0")) {
					if (existingProposal.getOspSectionInfo()
							.isAnticipatedSubRecipients() != newOSPSectionInfo
							.isAnticipatedSubRecipients()) {
						existingProposal.getOspSectionInfo()
								.setAnticipatedSubRecipients(
										newOSPSectionInfo
												.isAnticipatedSubRecipients());
					}

					// Anticipated SubRecipients Names
					if (existingProposal.getOspSectionInfo()
							.getAnticipatedSubRecipientsNames() != null) {
						if (!existingProposal
								.getOspSectionInfo()
								.getAnticipatedSubRecipientsNames()
								.equals(newOSPSectionInfo
										.getAnticipatedSubRecipientsNames())) {
							existingProposal
									.getOspSectionInfo()
									.setAnticipatedSubRecipientsNames(
											newOSPSectionInfo
													.getAnticipatedSubRecipientsNames());
						}
					} else {
						existingProposal
								.getOspSectionInfo()
								.setAnticipatedSubRecipientsNames(
										newOSPSectionInfo
												.getAnticipatedSubRecipientsNames());
					}
				}

				BasePIEligibilityOptions newBasePIEligibilityOptions = new BasePIEligibilityOptions();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("PIEligibilityWaiver")) {
					switch (oSPSectionInfo.get("PIEligibilityWaiver")
							.textValue()) {
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

				// Base PI Eligibility Options
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getPiEligibilityWaiver()
							.equals(newBasePIEligibilityOptions)) {
						existingProposal.getOspSectionInfo()
								.setPiEligibilityWaiver(
										newBasePIEligibilityOptions);
					}
				}

				newBaseOptions = new BaseOptions();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("ConflictOfInterestForms")) {
					switch (oSPSectionInfo.get("ConflictOfInterestForms")
							.textValue()) {
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

				// Conflict Of Interest Forms
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getConflictOfInterestForms()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setConflictOfInterestForms(newBaseOptions);
					}
				}

				newBaseOptions = new BaseOptions();
				if (oSPSectionInfo != null
						&& oSPSectionInfo.has("ExcludedPartyListChecked")) {
					switch (oSPSectionInfo.get("ExcludedPartyListChecked")
							.textValue()) {
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

				// Excluded Party List Checked
				if (!proposalID.equals("0")) {
					if (!existingProposal.getOspSectionInfo()
							.getExcludedPartyListChecked()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setExcludedPartyListChecked(newBaseOptions);
					}
				}
			}

			// Appendix Info
			if (proposalInfo != null && proposalInfo.has("AppendixInfo")) {
				List<Appendix> appendixInfo = Arrays.asList(mapper.readValue(
						proposalInfo.get("AppendixInfo").toString(),
						Appendix[].class));
				if (appendixInfo.size() != 0) {

					String UPLOAD_PATH = new String();
					try {
						UPLOAD_PATH = this.getClass().getResource("/uploads")
								.toURI().getPath();
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}

					if (!proposalID.equals("0")) {
						// boolean alreadyExist = false;
						// for (Appendix appendix :
						// existingProposal.getAppendices()) {
						// if (appendix.equals(newAppendix)) {
						// alreadyExist = true;
						// break;
						// }
						// }
						// if (!alreadyExist) {
						if (!existingProposal.getAppendices().equals(
								appendixInfo)) {
							existingProposal.getAppendices().clear();

							for (Appendix uploadFile : appendixInfo) {
								String fileName = uploadFile.getFilename();
								File file = new File(UPLOAD_PATH + fileName);

								String extension = "";
								int i = fileName.lastIndexOf('.');
								if (i > 0) {
									extension = fileName.substring(i + 1);
									uploadFile.setExtension(extension);
								}
								uploadFile.setFilesize(file.length());
								uploadFile.setFilepath("/uploads/" + fileName);

								existingProposal.getAppendices()
										.add(uploadFile);
							}
						}
						// }
					} else {
						for (Appendix uploadFile : appendixInfo) {
							String fileName = uploadFile.getFilename();
							File file = new File(UPLOAD_PATH + fileName);

							String extension = "";
							int i = fileName.lastIndexOf('.');
							if (i > 0) {
								extension = fileName.substring(i + 1);
								uploadFile.setExtension(extension);
							}
							uploadFile.setFilesize(file.length());
							uploadFile.setFilepath("/uploads/" + fileName);

							newProposal.getAppendices().add(uploadFile);
						}
					}
				}
			}

			// Proposal No
			if (proposalInfo != null && !proposalInfo.has("ProposalNo")
					&& proposalID.equals("0")) {
				newProposal
						.setProposalNo(proposalDAO.findLatestProposalNo() + 1);
			}

			// START

			// Signature
			// To hold all new Investigators list to get notified
			List<SignatureInfo> addedSignatures = new ArrayList<SignatureInfo>();

			if (proposalInfo != null && proposalInfo.has("SignatureInfo")) {
				String[] rows = proposalInfo.get("SignatureInfo").textValue()
						.split("#!#");

				List<SignatureInfo> newSignatureInfo = new ArrayList<SignatureInfo>();
				List<SignatureInfo> allSignatureInfo = new ArrayList<SignatureInfo>();
				// UserProfileID!#!Signature!#!SignedDate!#!Note!#!FullName!#!PositionTitle!#!Delegated#!#
				DateFormat format = new SimpleDateFormat(
						"yyyy/MM/dd hh:mm:ss a");

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

					allSignatureInfo.add(signatureInfo);

					if (!proposalID.equals("0")) {
						boolean alreadyExist = false;
						for (SignatureInfo sign : existingProposal
								.getSignatureInfo()) {
							if (sign.equals(signatureInfo)) {
								alreadyExist = true;
								break;
							}
						}
						if (!alreadyExist) {
							newSignatureInfo.add(signatureInfo);
						}
					}
				}
				// SignatureInfo
				addedSignatures = newSignatureInfo;
				if (!proposalID.equals("0")) {
					if (!existingProposal.getSignatureInfo().equals(
							allSignatureInfo)) {
						for (SignatureInfo signatureInfo : newSignatureInfo) {
							existingProposal.getSignatureInfo().add(
									signatureInfo);
						}
					}
				} else {
					newProposal.setSignatureInfo(allSignatureInfo);
				}
			}

			// InvestigatorInfo
			// To hold all new Investigators list to get notified
			InvestigatorInfo addedInvestigators = new InvestigatorInfo();
			InvestigatorInfo existingInvestigators = new InvestigatorInfo();
			InvestigatorInfo deletedInvestigators = new InvestigatorInfo();

			if (proposalInfo != null && proposalInfo.has("InvestigatorInfo")) {

				if (!proposalID.equals("0")) {
					// MUST Clear all co-PI and Seniors
					existingProposal.getInvestigatorInfo().getCo_pi().clear();
					existingProposal.getInvestigatorInfo().getSeniorPersonnel()
							.clear();
				}

				String[] rows = proposalInfo.get("InvestigatorInfo")
						.textValue().split("#!#");

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
							// if
							// (!existingProposal.getInvestigatorInfo().getPi()
							// .equals(investigatorRefAndPosition)) {
							// existingProposal.getInvestigatorInfo().setPi(
							// investigatorRefAndPosition);
							// if (!addedInvestigators.getPi().equals(
							// investigatorRefAndPosition)) {
							// addedInvestigators
							// .setPi(investigatorRefAndPosition);
							// }
							// }
						} else {
							newInvestigatorInfo
									.setPi(investigatorRefAndPosition);
						}
						break;
					case "1":
						if (!proposalID.equals("0")) {
							if (!existingProposal.getInvestigatorInfo()
									.getCo_pi()
									.contains(investigatorRefAndPosition)) {
								existingProposal.getInvestigatorInfo()
										.getCo_pi()
										.add(investigatorRefAndPosition);

								if (!addedInvestigators.getCo_pi().contains(
										investigatorRefAndPosition)) {
									addedInvestigators.getCo_pi().add(
											investigatorRefAndPosition);
								}
							}
							// if (!existingInvestigators.getCo_pi().contains(
							// investigatorRefAndPosition)) {
							// }
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

								if (!addedInvestigators.getSeniorPersonnel()
										.contains(investigatorRefAndPosition)) {
									addedInvestigators.getSeniorPersonnel()
											.add(investigatorRefAndPosition);
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
					// Existing Investigator Info to compare
					existingInvestigators = oldProposal.getInvestigatorInfo();

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
							deletedInvestigators.getSeniorPersonnel().add(
									senior);
						}
					}
				}
			}

			// For Proposal User Title : for Dean, Chair and Manager
			JsonNode proposalUserTitle = root.get("proposalUserTitle");
			if (proposalUserTitle != null) {
				// For Proposal Roles : PI, Co-PI, Senior
				JsonNode proposalRoles = root.get("proposalRoles");

				List<String> currentProposalRoles = new ArrayList<String>();

				if (!proposalRoles.asText().equals("")) {
					currentProposalRoles = Arrays.asList(proposalRoles
							.textValue().split(", "));
				}

				JsonNode buttonType = root.get("buttonType");

				// For Proposal Status
				if (buttonType != null) {
					switch (buttonType.textValue()) {
					case "Create":
						// This is Hack
						// TODO check for if no CO-PI/ Seniors in first create
						// case
						// Change status to ready to submitted by PI
						if (proposalID.equals("0")) {
							if ((newProposal.getInvestigatorInfo().getCo_pi()
									.size() == 0)
									&& (newProposal.getInvestigatorInfo()
											.getSeniorPersonnel().size() == 0)) {
								newProposal
										.setReadyForSubmissionByPI(ReadyType.READYFORSUBMIT);

								newProposal.getProposalStatus().add(
										Status.READYFORSUBMITBYPI);
							}
						}
						break;

					case "Submit":
						// var canSubmitRoles = [ "PI" ];
						// var canSubmitTitles = [
						// "University Research Administrator"
						// ];
						if (!proposalID.equals("0")
								&& currentProposalRoles != null) {
							if (existingProposal.getSubmittedByPI() == SubmitType.NOTSUBMITTED
									&& existingProposal
											.getReadyForSubmissionByPI() == ReadyType.READYFORSUBMIT
									&& existingProposal.getDeletedByPI() == DeleteType.NOTDELETED
									&& currentProposalRoles.contains("PI")
									&& !proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {

								boolean foundCoPI = false;
								boolean foundSenior = false;
								// Check if all PI/ Co-PI/ Senior/
								boolean signedByPI = false;
								boolean signedByAllCoPIs = false;
								boolean signedByAllSeniors = false;

								for (SignatureInfo sign : existingProposal
										.getSignatureInfo()) {
									if (existingProposal.getInvestigatorInfo()
											.getPi().getUserProfileId()
											.equals(sign.getUserProfileId())
											&& sign.getPositionTitle().equals(
													"PI")) {
										signedByPI = true;
										break;
									}
								}

								int coPICount = existingProposal
										.getInvestigatorInfo().getCo_pi()
										.size();
								if (coPICount > 0) {
									for (InvestigatorRefAndPosition copi : existingProposal
											.getInvestigatorInfo().getCo_pi()) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (copi.getUserProfileId().equals(
													sign.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Co-PI")) {
												foundCoPI = true;
												break;
											}
										}
										if (!foundCoPI) {
											signedByAllCoPIs = false;
											break;
										} else {
											signedByAllCoPIs = true;
										}
										foundCoPI = false;
									}
								} else {
									signedByAllCoPIs = true;
								}

								int seniorCount = existingProposal
										.getInvestigatorInfo()
										.getSeniorPersonnel().size();
								if (seniorCount > 0) {
									for (InvestigatorRefAndPosition senior : existingProposal
											.getInvestigatorInfo()
											.getSeniorPersonnel()) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (senior
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Senior")) {
												foundSenior = true;
												break;
											}
										}
										if (!foundSenior) {
											signedByAllSeniors = false;
											break;
										} else {
											signedByAllSeniors = true;
										}
										foundSenior = false;
									}
								} else {
									signedByAllSeniors = true;
								}

								if (signedByPI && signedByAllCoPIs
										&& signedByAllSeniors) {
									existingProposal
											.setDateSubmitted(new Date());

									existingProposal
											.setSubmittedByPI(SubmitType.SUBMITTED);
									existingProposal
											.setChairApproval(ApprovalType.READYFORAPPROVAL);

									// Proposal Status
									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.WAITINGFORCHAIRAPPROVAL);
								} else {
									existingProposal
											.setReadyForSubmissionByPI(ReadyType.NOTREADYFORSUBMIT);

									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.NOTSUBMITTEDBYPI);
								}

							} else if (existingProposal
									.getResearchAdministratorSubmission() == SubmitType.NOTSUBMITTED
									&& existingProposal
											.getResearchDirectorApproval() == ApprovalType.APPROVED
									&& !currentProposalRoles.contains("PI")
									&& proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {
								existingProposal
										.setResearchAdministratorSubmission(SubmitType.SUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.SUBMITTEDBYRESEARCHADMIN);

							} else {
								// This user is both PI and Research
								// Administrator
							}
						}

						break;

					case "Update":
						if (!proposalID.equals("0")
								&& currentProposalRoles != null) {
							if ((currentProposalRoles.contains("PI")
									|| (currentProposalRoles.contains("CO-PI") && existingProposal
											.getReadyForSubmissionByPI() == ReadyType.NOTREADYFORSUBMIT) || (currentProposalRoles
									.contains("Senior") && existingProposal
									.getReadyForSubmissionByPI() == ReadyType.NOTREADYFORSUBMIT))
									&& existingProposal.getSubmittedByPI() == SubmitType.NOTSUBMITTED) {
								// TODO : check all pi/copi/seniors
								// signed the proposal
								// existingProposal.getInvestigatorInfo().getCo_pi()
								// existingProposal.getSignatureInfo()

								boolean foundCoPI = false;
								boolean foundSenior = false;
								// Check if all PI/ Co-PI/ Senior/
								boolean signedByPI = false;
								boolean signedByAllCoPIs = false;
								boolean signedByAllSeniors = false;

								for (SignatureInfo sign : existingProposal
										.getSignatureInfo()) {
									if (existingProposal.getInvestigatorInfo()
											.getPi().getUserProfileId()
											.equals(sign.getUserProfileId())
											&& sign.getPositionTitle().equals(
													"PI")) {
										signedByPI = true;
										break;
									}
								}

								int coPICount = existingProposal
										.getInvestigatorInfo().getCo_pi()
										.size();
								if (coPICount > 0) {
									for (InvestigatorRefAndPosition copi : existingProposal
											.getInvestigatorInfo().getCo_pi()) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (copi.getUserProfileId().equals(
													sign.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Co-PI")) {
												foundCoPI = true;
												break;
											}
										}
										if (!foundCoPI) {
											signedByAllCoPIs = false;
											break;
										} else {
											signedByAllCoPIs = true;
										}
										foundCoPI = false;
									}
								} else {
									signedByAllCoPIs = true;
								}

								int seniorCount = existingProposal
										.getInvestigatorInfo()
										.getSeniorPersonnel().size();
								if (seniorCount > 0) {
									for (InvestigatorRefAndPosition senior : existingProposal
											.getInvestigatorInfo()
											.getSeniorPersonnel()) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (senior
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Senior")) {
												foundSenior = true;
												break;
											}
										}
										if (!foundSenior) {
											signedByAllSeniors = false;
											break;
										} else {
											signedByAllSeniors = true;
										}
										foundSenior = false;
									}
								} else {
									signedByAllSeniors = true;
								}

								if (signedByPI && signedByAllCoPIs
										&& signedByAllSeniors) {
									existingProposal
											.setReadyForSubmissionByPI(ReadyType.READYFORSUBMIT);

									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.READYFORSUBMITBYPI);
								} else {
									existingProposal
											.setReadyForSubmissionByPI(ReadyType.NOTREADYFORSUBMIT);

									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.NOTSUBMITTEDBYPI);
								}
							}
						}
						break;

					case "Approve":
						if (!proposalID.equals("0")
								&& currentProposalRoles != null) {
							// var canApproveTitles = [
							// "Department Chair",
							// "Business Manager",
							// "IRB", "Dean",
							// "University Research Administrator",
							// "University Research Director" ];

							boolean foundChair = false;
							boolean foundBusinessManager = false;
							boolean foundDean = false;
							boolean foundIRB = false;
							boolean foundResearchAdmin = false;
							boolean foundResearchDirector = false;

							boolean signedByAllChairs = false;
							boolean signedByAllBusinessManagers = false;
							boolean signedByAllDeans = false;
							boolean signedByAllIRBs = false;
							boolean signedByAllResearchAdmins = false;
							boolean signedByAllResearchDirectors = false;

							ObjectId id = new ObjectId(proposalID);

							List<SignatureInfo> signatures = proposalDAO
									.findSignaturesExceptInvestigator(id,
											irbApprovalRequired);

							if (existingProposal.getChairApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Department Chair")) {
								for (SignatureInfo chairSign : signatures) {
									if (chairSign.getPositionTitle().equals(
											"Department Chair")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (chairSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Department Chair")) {
												foundChair = true;
												break;
											}
										}
										if (!foundChair) {
											signedByAllChairs = false;
											break;
										} else {
											signedByAllChairs = true;
										}
										foundChair = false;
									}
								}

								if (signedByAllChairs) {
									// Ready for Review by Business Manager
									existingProposal
											.setChairApproval(ApprovalType.APPROVED);
									existingProposal
											.setBusinessManagerApproval(ApprovalType.READYFORAPPROVAL);

									// Proposal Status
									existingProposal.getProposalStatus()
											.clear();
									existingProposal
											.getProposalStatus()
											.add(Status.READYFORREVIEWBYBUSINESSMANAGER);

									// TODO check the compliance and IRB
									// approvable needed or not?
									if (irbApprovalRequired) {
										existingProposal
												.setIrbApproval(ApprovalType.READYFORAPPROVAL);

										// Proposal Status
										existingProposal
												.getProposalStatus()
												.add(Status.READYFORREVIEWBYIRB);
									}
								}
							} else if (existingProposal
									.getBusinessManagerApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Business Manager")) {
								for (SignatureInfo businessManagerSign : signatures) {
									if (businessManagerSign.getPositionTitle()
											.equals("Business Manager")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (businessManagerSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Business Manager")) {
												foundBusinessManager = true;
												break;
											}
										}
										if (!foundBusinessManager) {
											signedByAllBusinessManagers = false;
											break;
										} else {
											signedByAllBusinessManagers = true;
										}
										foundBusinessManager = false;
									}
								}
								if (signedByAllBusinessManagers) {
									// Reviewed by Business Manager
									existingProposal
											.setBusinessManagerApproval(ApprovalType.APPROVED);
									existingProposal
											.setDeanApproval(ApprovalType.READYFORAPPROVAL);

									// Proposal Status
									existingProposal
											.getProposalStatus()
											.remove(Status.READYFORREVIEWBYBUSINESSMANAGER);
									existingProposal.getProposalStatus().add(
											Status.WAITINGFORDEANAPPROVAL);
								}
							} else if (existingProposal.getIrbApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"IRB") && irbApprovalRequired) {
								for (SignatureInfo irbSign : signatures) {
									if (irbSign.getPositionTitle()
											.equals("IRB")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (irbSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("IRB")) {
												foundIRB = true;
												break;
											}
										}
										if (!foundIRB) {
											signedByAllIRBs = false;
											break;
										} else {
											signedByAllIRBs = true;
										}
										foundIRB = false;
									}
								}
								if (signedByAllIRBs) {
									// Approved by IRB
									existingProposal
											.setIrbApproval(ApprovalType.APPROVED);

									// Proposal Status
									existingProposal.getProposalStatus()
											.remove(Status.READYFORREVIEWBYIRB);
									existingProposal.getProposalStatus().add(
											Status.REVIEWEDBYIRB);

									if (existingProposal.getDeanApproval() == ApprovalType.APPROVED) {
										existingProposal
												.setResearchAdministratorApproval(ApprovalType.READYFORAPPROVAL);

										// Proposal Status
										existingProposal.getProposalStatus()
												.clear();
										existingProposal
												.getProposalStatus()
												.add(Status.WAITINGFORRESEARCHADMINAPPROVAL);
									}
								}
							} else if (existingProposal.getDeanApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Dean")) {
								for (SignatureInfo deanSign : signatures) {
									if (deanSign.getPositionTitle().equals(
											"Dean")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (deanSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("Dean")) {
												foundDean = true;
												break;
											}
										}
										if (!foundDean) {
											signedByAllDeans = false;
											break;
										} else {
											signedByAllDeans = true;
										}
										foundDean = false;
									}
								}
								if (signedByAllDeans) {
									// Approved by Dean
									existingProposal
											.setDeanApproval(ApprovalType.APPROVED);

									// Proposal Status
									existingProposal
											.getProposalStatus()
											.remove(Status.WAITINGFORDEANAPPROVAL);
									existingProposal.getProposalStatus().add(
											Status.APPROVEDBYDEAN);

									if (!irbApprovalRequired) {
										existingProposal
												.setResearchAdministratorApproval(ApprovalType.READYFORAPPROVAL);

										// Proposal Status
										existingProposal.getProposalStatus()
												.clear();
										existingProposal
												.getProposalStatus()
												.add(Status.WAITINGFORRESEARCHADMINAPPROVAL);
									} else {
										if (existingProposal.getIrbApproval() == ApprovalType.APPROVED) {
											existingProposal
													.setResearchAdministratorApproval(ApprovalType.READYFORAPPROVAL);

											// Proposal Status
											existingProposal
													.getProposalStatus()
													.clear();
											existingProposal
													.getProposalStatus()
													.add(Status.WAITINGFORRESEARCHADMINAPPROVAL);
										}
									}
								}
							} else if (existingProposal
									.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {
								for (SignatureInfo researchAdminSign : signatures) {
									if (researchAdminSign
											.getPositionTitle()
											.equals("University Research Administrator")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (researchAdminSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("University Research Administrator")) {
												foundResearchAdmin = true;
												break;
											}
										}
										if (!foundResearchAdmin) {
											signedByAllResearchAdmins = false;
											break;
										} else {
											signedByAllResearchAdmins = true;
										}
										foundResearchAdmin = false;
									}
								}
								if (signedByAllResearchAdmins) {
									// Submitted to Research Director
									existingProposal
											.setResearchAdministratorApproval(ApprovalType.APPROVED);
									existingProposal
											.setResearchDirectorApproval(ApprovalType.READYFORAPPROVAL);

									// Proposal Status
									existingProposal.getProposalStatus()
											.clear();
									existingProposal
											.getProposalStatus()
											.add(Status.WAITINGFORRESEARCHDIRECTORAPPROVAL);
								}
							} else if (existingProposal
									.getResearchDirectorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"University Research Director")) {
								for (SignatureInfo researchDirectorSign : signatures) {
									if (researchDirectorSign
											.getPositionTitle()
											.equals("University Research Director")) {
										for (SignatureInfo sign : existingProposal
												.getSignatureInfo()) {
											if (researchDirectorSign
													.getUserProfileId()
													.equals(sign
															.getUserProfileId())
													&& sign.getPositionTitle()
															.equals("University Research Director")) {
												foundResearchDirector = true;
												break;
											}
										}
										if (!foundResearchDirector) {
											signedByAllResearchDirectors = false;
											break;
										} else {
											signedByAllResearchDirectors = true;
										}
										foundResearchDirector = false;
									}
								}
								if (signedByAllResearchDirectors) {
									// Ready for submission
									existingProposal
											.setResearchDirectorApproval(ApprovalType.APPROVED);

									// Proposal Status
									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.READYFORSUBMISSION);
								}
							} else {
								// You are not allowed to APPROVE the
								// Proposal
							}
						}

						break;

					case "Disapprove":
						if (!proposalID.equals("0")
								&& currentProposalRoles != null) {

							if (existingProposal.getChairApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Department Chair")) {
								// Returned by Chair
								existingProposal
										.setChairApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.RETURNEDBYCHAIR);

							} else if (existingProposal
									.getBusinessManagerApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Business Manager")) {
								// Disapproved by Business Manager
								existingProposal
										.setBusinessManagerApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);
								existingProposal
										.setIrbApproval(ApprovalType.NOTREADYFORAPPROVAL);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYBUSINESSMANAGER);

							} else if (existingProposal.getIrbApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"IRB")) {
								// Disapproved by IRB
								existingProposal
										.setIrbApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYIRB);

							} else if (existingProposal.getDeanApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"Dean")) {
								// Returned by Dean
								existingProposal
										.setDeanApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.RETURNEDBYDEAN);

							} else if (existingProposal
									.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {
								// Disapproved by Research Administrator
								existingProposal
										.setResearchAdministratorApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYRESEARCHADMIN);

							} else if (existingProposal
									.getResearchDirectorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"University Research Director")) {
								// Disapproved by University Research
								// Director
								existingProposal
										.setResearchDirectorApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYRESEARCHDIRECTOR);

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
			// END

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

			// Save the Proposal
			Proposal currentProposal = new Proposal();
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();
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
				notification
						.setAction("Added as PI by " + authorUserName + ".");
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

			for (InvestigatorRefAndPosition copi : addedInvestigators
					.getCo_pi()) {
				notification = new NotificationLog();
				notification.setType("Investigator");
				notification.setAction("Added as CO-PI by " + authorUserName
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
				notification.setAction("Deleted as PI by " + authorUserName
						+ ".");
				notification.setProposalId(currentProposal.getId().toString());
				notification.setProposalTitle(projectTitle);
				notification.setUserProfileId(deletedPI.getUserProfileId());
				notification.setUsername(deletedPI.getUserRef()
						.getUserAccount().getUserName());
				notification.setCollege(deletedPI.getCollege());
				notification.setDepartment(deletedPI.getDepartment());
				notification.setPositionType(deletedPI.getPositionType());
				notification.setPositionTitle(deletedPI.getPositionTitle());
				notification.setCritical(true);
				notificationDAO.save(notification);
			}

			for (InvestigatorRefAndPosition copi : deletedInvestigators
					.getCo_pi()) {
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
					// Date signedDate = signatureInfo.getSignedDate();
					notification = new NotificationLog();
					notification.setType("Signature");
					notification.setAction("Signed by " + signFullName + " as "
							+ position + ".");

					notification.setProposalId(currentProposal.getId()
							.toString());
					notification.setProposalTitle(projectTitle);
					notification.setForAdmin(true);
					notificationDAO.save(notification);

					notification = new NotificationLog();
					notification.setType("Signature");
					notification.setAction("Signed by " + signFullName + " as "
							+ position + ".");
					notification.setProposalId(currentProposal.getId()
							.toString());
					notification.setProposalTitle(projectTitle);
					notification.setUserProfileId(currentProposal
							.getInvestigatorInfo().getPi().getUserProfileId());
					notification.setUsername(currentProposal
							.getInvestigatorInfo().getPi().getUserRef()
							.getUserAccount().getUserName());
					notification.setCollege(currentProposal
							.getInvestigatorInfo().getPi().getCollege());
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
						notification.setAction("Signed by " + signFullName
								+ " as " + position + ".");
						notification.setProposalId(currentProposal.getId()
								.toString());
						notification.setProposalTitle(projectTitle);
						notification.setUserProfileId(copi.getUserProfileId());
						notification.setUsername(copi.getUserRef()
								.getUserAccount().getUserName());
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
						notification.setAction("Signed by " + signFullName
								+ " as " + position + ".");
						notification.setProposalId(currentProposal.getId()
								.toString());
						notification.setProposalTitle(projectTitle);
						notification
								.setUserProfileId(senior.getUserProfileId());
						notification.setUsername(senior.getUserRef()
								.getUserAccount().getUserName());
						notification.setCollege(senior.getCollege());
						notification.setDepartment(senior.getDepartment());
						notification.setPositionType(senior.getPositionType());
						notification
								.setPositionTitle(senior.getPositionTitle());
						notificationDAO.save(notification);
					}
				}
			}
			return Response.status(200).entity(true).build();
		} else {
			return Response.status(403)
					.entity("NOT ALLOWED TO UPDATE/ SAVE A PROPOSAL").build();
		}
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

		// Broadcasting SSE

		OutboundEvent.Builder eventBuilder = new OutboundEvent.Builder();
		OutboundEvent event = eventBuilder.name("notification")
				.mediaType(MediaType.TEXT_PLAIN_TYPE).data(String.class, "1")
				.build();

		NotificationService.BROADCASTER.broadcast(event);
	}

	@POST
	@Path("/CheckPermissionForAProposal")
	@Produces(MediaType.APPLICATION_JSON)
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

				// // TODO only check this for required not all XACML call
				// if (root != null && root.has("proposalId")) {
				// String proposalId = new String();
				// JsonNode proposal_Id = root.get("proposalId");
				// proposalId = proposal_Id.textValue();
				// if (!proposalId.equals("")) {
				// ObjectId id = new ObjectId(proposalId);
				// Proposal proposal = proposalDAO
				// .findProposalByProposalID(id);
				// resourceMap.put("status", proposal.getProposalStatus()
				// .toString());
				// attrMap.put("Resource", resourceMap);
				// }
				// }

				// Need to add Environment to detect the Campus or outside
				// network
				// network-type

				// Device type
				// device-type

				// String decision = ac.getXACMLdecision(attrMap);
				// if (decision.equals("Permit")) {
				return Response.status(200).type(MediaType.APPLICATION_JSON)
						.entity("true").build();
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

	/**
	 * This method will check the signatures for the proposal. It will first
	 * find all the supervisory personnel that SHOULD be signing the proposal
	 * (based on PI, COPI, Senior Personnel -their supervisory personnel-) Then
	 * it will find out if the appropriate number has signed ie: if between the
	 * Pi, CoPi, and SP, there are 4 department chairs, we need to know that 4
	 * department chairs have signed.
	 * 
	 * @param1 the ID of the proposal we need to query for
	 * @param2 the position title we want to check
	 * @return true if all required signatures exist
	 * @throws UnknownHostException
	 */
	public boolean getSignedStatusForAProposal(ObjectId id, String posTitle)
			throws UnknownHostException {
		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		Proposal checkProposal = proposalDAO.findProposalByProposalID(id);

		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		List<InvestigatorRefAndPosition> investigatorList = new ArrayList<InvestigatorRefAndPosition>();

		investigatorList.add(checkProposal.getInvestigatorInfo().getPi());
		for (InvestigatorRefAndPosition coPi : checkProposal
				.getInvestigatorInfo().getCo_pi()) {
			investigatorList.add(coPi);
		}
		for (InvestigatorRefAndPosition senior : checkProposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			investigatorList.add(senior);
		}

		ArrayList<UserProfile> supervisorsList = new ArrayList<UserProfile>();
		// For each person on this list, get their supervisory personnel, and
		// add them to a list,
		// but avoid duplicate entries.

		// For each investigator (pi, copi, sp) in the list of them...
		// get their department, then from that department, get the desired
		// position title (chair, dean, etc...)
		// and add those supervisors to the list
		// This may result in duplicate entries being added to the list but we
		// will handle this with a nest for loop
		// Hopefully this does not result in a giant run time

		for (InvestigatorRefAndPosition investigator : investigatorList) {
			List<UserProfile> tempList = userProfileDAO
					.getSupervisoryPersonnels(investigator.getDepartment(),
							posTitle);
			for (UserProfile profs : tempList) {
				if (!supervisorsList.contains(profs)) {
					supervisorsList.add(profs);
				}
			}
		}

		int sigCount = 0;
		boolean isSigned = true;
		// For all the supervisors on the list, we need to get their status as
		// signed or not signed.

		// 2nd Find out all of their supervisory personnel
		for (UserProfile supervisor : supervisorsList) {
			for (SignatureInfo signature : checkProposal.getSignatureInfo()) {
				if (!signature.getUserProfileId().toString()
						.equals(supervisor.getId().toString())
						&& !signature.getPositionTitle().equals(posTitle)) {
					isSigned = false;
				}

				if (isSigned) {
					sigCount++;
				}
			}

		}

		// 3rd Evaluate if these personnel have "signed" the proposal
		if (sigCount == supervisorsList.size()) {
			return true;
		} else {
			return false;
		}
	}
}
