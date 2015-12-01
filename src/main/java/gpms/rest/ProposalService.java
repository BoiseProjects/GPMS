package gpms.rest;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.AdditionalInfo;
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
import gpms.model.GPMSCommonInfo;
import gpms.model.InvestigatorInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.OSPSectionInfo;
import gpms.model.ProjectInfo;
import gpms.model.ProjectLocation;
import gpms.model.ProjectPeriod;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.ProposalInfo;
import gpms.model.Recovery;
import gpms.model.ResearchAdministrator;
import gpms.model.SignatureInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.Status;
import gpms.model.TypeOfRequest;
import gpms.model.UniversityCommitments;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.io.IOException;
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

@Path("/proposals")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
public class ProposalService {
	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public ProposalService() {
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
	@Path("/GetProposalStatusList")
	public ArrayList<Status> getProposalStatusList()
			throws JsonProcessingException, IOException {
		return new ArrayList<Status>(Arrays.asList(Status.values()));
	}

	@POST
	@Path("/GetProposalsList")
	public List<ProposalInfo> produceProposalsJSON(String message)
			throws JsonGenerationException, JsonMappingException, IOException,
			ParseException {
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		int offset = 0, limit = 0;
		String projectTitle = new String();
		String proposedBy = new String();
		Double totalCostsFrom = 0.0;
		Double totalCostsTo = 0.0;
		String receivedOnFrom = new String();
		String receivedOnTo = new String();
		String proposalStatus = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("offset")) {
			offset = root.get("offset").getIntValue();
		}

		if (root != null && root.has("limit")) {
			limit = root.get("limit").getIntValue();
		}

		JsonNode proposalObj = root.get("proposalBindObj");
		if (proposalObj != null && proposalObj.has("ProjectTitle")) {
			projectTitle = proposalObj.get("ProjectTitle").getTextValue();
		}

		if (proposalObj != null && proposalObj.has("ProposedBy")) {
			proposedBy = proposalObj.get("ProposedBy").getTextValue();
		}

		if (proposalObj != null && proposalObj.has("ReceivedOnFrom")) {
			receivedOnFrom = proposalObj.get("ReceivedOnFrom").getTextValue();
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
				totalCostsTo = Double.valueOf(proposalObj.get("TotalCostsTo")
						.getTextValue());
			}
		}

		if (proposalObj != null && proposalObj.has("ProposalStatus")) {
			proposalStatus = proposalObj.get("ProposalStatus").getTextValue();
		}

		proposals = proposalDAO.findAllForProposalGrid(offset, limit,
				projectTitle, proposedBy, receivedOnFrom, receivedOnTo,
				totalCostsFrom, totalCostsTo, proposalStatus);

		return proposals;
	}

	@POST
	@Path("/DeleteProposalByProposalID")
	public String deleteUserByProposalID(String message)
			throws JsonProcessingException, IOException {

		// I just have this here as an example of multimapping, hashmaps within
		// hashmaps etc.
		// Hashtable<String, Hashtable<String, Hashtable<String,
		// ArrayList<String>>>> ht = new Hashtable<String, Hashtable<String,
		// Hashtable<String, ArrayList<String>>>>();
		HashMap AttributesMap = new HashMap<String, HashMap<String, String>>();

		UserProfile user = new UserProfile();
		String response = new String();

		String proposalId = new String();
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalId")) {
			proposalId = root.get("proposalId").getTextValue();
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

		/*
		 * TODO CheckXACMLPOLICY Call the accesscontrol with the
		 * 
		 * if this person is the PI, then String isPI = PI if not then String
		 * isPI = NOT policyEval(isPI, Proposal, Delete)
		 * 
		 * getPIname, getPIId, getProposalID, callPolicyMethod(PINAME, PIID,
		 * PROPOSALID) if policy returns permit, continue if policy returns deny
		 * do not continue
		 */

		// TODO : login set this session value
		// FOr Testing I am using HardCoded UserProfileID
		// userProfileID = "55b9225454ffd82dc052a32a";

		ObjectId id = new ObjectId(proposalId);
		ObjectId authorId = new ObjectId(userProfileID);

		GPMSCommonInfo gpmsCommonObj = new GPMSCommonInfo();
		gpmsCommonObj.setUserName(userName);
		gpmsCommonObj.setUserProfileID(userProfileID);
		gpmsCommonObj.setCultureName(cultureName);

		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);
		Proposal proposal = proposalDAO.findProposalByProposalID(id);

		proposalDAO.deleteProposal(proposal, authorProfile, gpmsCommonObj);

		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");

		return response;
	}

	@POST
	@Path("/DeleteMultipleProposalsByProposalID")
	public String deleteMultipleProposalsByProposalID(String message)
			throws JsonProcessingException, IOException {
		UserProfile user = new UserProfile();
		String response = new String();

		String proposalIds = new String();
		String proposals[] = new String[0];
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		if (root != null && root.has("proposalIds")) {
			proposalIds = root.get("proposalIds").getTextValue();
			proposals = proposalIds.split(",");
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

		for (String proposalId : proposals) {
			ObjectId id = new ObjectId(proposalId);

			Proposal proposal = proposalDAO.findProposalByProposalID(id);

			proposalDAO.deleteProposal(proposal, authorProfile, gpmsCommonObj);
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

		ObjectId id = new ObjectId(proposalId);

		proposalAuditLogs = proposalDAO.findAllForProposalAuditLogGrid(offset,
				limit, id, action, auditedBy, activityOnFrom, activityOnTo);

		// users = (ArrayList<UserInfo>) userProfileDAO.findAllForUserGrid();
		// response = JSONTansformer.ConvertToJSON(users);

		return proposalAuditLogs;
	}

	@POST
	@Path("/CheckUniqueProjectTitle")
	public String checkUniqueProjectTitle(String message)
			throws JsonProcessingException, IOException {
		String proposalID = new String();
		String newProjectTitle = new String();
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		String response = new String();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode proposalUniqueObj = root.get("proposalUniqueObj");
		if (proposalUniqueObj != null && proposalUniqueObj.has("ProposalID")) {
			proposalID = proposalUniqueObj.get("ProposalID").getTextValue();
		}

		if (proposalUniqueObj != null
				&& proposalUniqueObj.has("NewProjectTitle")) {
			newProjectTitle = proposalUniqueObj.get("NewProjectTitle")
					.getTextValue();
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
	@Path("/SaveUpdateProposal")
	public String saveUpdateProposal(String message) throws Exception {
		String userName = new String();
		String userProfileID = new String();
		String cultureName = new String();

		String proposalID = new String();

		Proposal newProposal = new Proposal();
		Proposal existingProposal = new Proposal();

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

		JsonNode proposalInfo = root.get("proposalInfo");
		if (proposalInfo != null && proposalInfo.has("ProposalID")) {
			proposalID = proposalInfo.get("ProposalID").getTextValue();
			if (!proposalID.equals("0")) {
				ObjectId id = new ObjectId(proposalID);
				existingProposal = proposalDAO.findProposalByProposalID(id);
			}
		}

		ProjectInfo newProjectInfo = new ProjectInfo();

		if (proposalInfo != null && proposalInfo.has("ProjectInfo")) {
			JsonNode projectInfo = proposalInfo.get("ProjectInfo");
			if (projectInfo != null && projectInfo.has("ProjectTitle")) {
				if (proposalID.equals("0")) {
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

		if (proposalInfo != null && proposalInfo.has("InvestigatorInfo")) {
			if (!proposalID.equals("0")) {
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
						}
					} else {
						newInvestigatorInfo.setPi(investigatorRefAndPosition);
					}
					break;
				case "1":
					if (!proposalID.equals("0")) {
						existingProposal.getInvestigatorInfo().getCo_pi()
								.add(investigatorRefAndPosition);
					} else {
						newInvestigatorInfo.getCo_pi().add(
								investigatorRefAndPosition);
					}
					break;
				case "2":
					if (!proposalID.equals("0")) {
						existingProposal.getInvestigatorInfo()
								.getSeniorPersonnel()
								.add(investigatorRefAndPosition);
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
			}
		}

		if (proposalInfo != null && proposalInfo.has("SignatureInfo")) {
			String[] rows = proposalInfo.get("SignatureInfo").getTextValue()
					.split("#!#");

			List<SignatureInfo> newSignatureInfo = new ArrayList<SignatureInfo>();
			// UserProfileID!#!Signature!#!SignedDate!#!FullName!#!PositionTitle!#!Delegated#!#
			DateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss a");

			for (String col : rows) {
				String[] cols = col.split("!#!");
				SignatureInfo signatureInfo = new SignatureInfo();
				signatureInfo.setUserProfileId(cols[0]);
				signatureInfo.setSignature(cols[1]);
				signatureInfo.setSignedDate(format.parse(cols[2]));
				signatureInfo.setFullName(cols[3]);
				signatureInfo.setPositionTitle(cols[4]);
				signatureInfo.setDelegated(Boolean.parseBoolean(cols[5]));

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

			if (oSPSectionInfo != null && oSPSectionInfo.has("proposalNotes")) {
				if (!proposalID.equals("0")) {
					if (!existingProposal
							.getoSPSectionInfo()
							.getProposalNotes()
							.equals(oSPSectionInfo.get("proposalNotes")
									.getTextValue())) {
						existingProposal.getoSPSectionInfo().setProposalNotes(
								oSPSectionInfo.get("proposalNotes")
										.getTextValue());
					}
				}
			}

			ResearchAdministrator newResearchAdministrator = new ResearchAdministrator();

			if (oSPSectionInfo != null && oSPSectionInfo.has("DF")) {
				newResearchAdministrator.setDF(oSPSectionInfo.get("DF")
						.getBooleanValue());
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("LG")) {
				newResearchAdministrator.setLG(oSPSectionInfo.get("LG")
						.getBooleanValue());
			}

			if (oSPSectionInfo != null && oSPSectionInfo.has("LN")) {
				newResearchAdministrator.setLN(oSPSectionInfo.get("LN")
						.getBooleanValue());
			}
			if (!proposalID.equals("0")) {
				if (!existingProposal.getoSPSectionInfo()
						.getResearchAdministrator()
						.equals(newResearchAdministrator)) {
					existingProposal.getoSPSectionInfo()
							.setResearchAdministrator(newResearchAdministrator);
				}
			}
		}

		if (proposalInfo != null && !proposalInfo.has("ProposalNo")
				&& proposalID.equals("0")) {
			newProposal.setProposalNo(proposalDAO.findLatestProposalNo() + 1);
		}

		if (proposalInfo != null && !proposalInfo.has("ReceivedDate")
				&& proposalID.equals("0")) {
			newProposal.setDateReceived(new Date());
		}

		if (proposalInfo != null && proposalInfo.has("ProposalStatus")) {
			if (!proposalID.equals("0")) {
				if (!existingProposal.getProposalStatus().equals(
						Status.valueOf(proposalInfo.get("ProposalStatus")
								.getTextValue()))) {
					existingProposal.setProposalStatus(Status
							.valueOf(proposalInfo.get("ProposalStatus")
									.getTextValue()));
				}
			}
		} else {
			newProposal.setProposalStatus(Status.NEW);
		}

		// Need to Compare Equals before saving existingUserProfile and
		// newProfile

		// Save the Proposal
		if (!proposalID.equals("0")) {
			proposalDAO.save(existingProposal);
		} else {
			proposalDAO.save(newProposal);
		}

		response = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(
				"Success");

		return response;
	}
}
