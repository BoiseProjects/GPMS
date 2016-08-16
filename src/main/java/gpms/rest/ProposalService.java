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
import gpms.model.Recovery;
import gpms.model.SignatureByAllUsers;
import gpms.model.SignatureInfo;
import gpms.model.SignatureUserInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.Status;
import gpms.model.SubmitType;
import gpms.model.TypeOfRequest;
import gpms.model.UniversityCommitments;
import gpms.model.UserAccount;
import gpms.model.UserProfile;
import gpms.model.WithdrawType;
import gpms.utils.EmailUtil;
import gpms.utils.SerializationHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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
import org.wso2.balana.ctx.Attribute;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.ctx.xacml3.Result;
import org.wso2.balana.xacml3.Advice;
import org.wso2.balana.xacml3.Attributes;

import com.ebay.xcelite.Xcelite;
import com.ebay.xcelite.sheet.XceliteSheet;
import com.ebay.xcelite.writer.SheetWriter;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.MongoClient;

@Path("/proposals")
@Api(value = "/proposals", description = "Manage Proposals")
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
	// private AbstractResult ar;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static final Logger log = Logger.getLogger(ProposalService.class
			.getName());

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
	@ApiOperation(value = "Test Proposal Service", notes = "This API tests whether the service is working or not")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Hello World! }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response testService() {
		try {
			log.info("ProposalService::testService started");

			return Response.status(Response.Status.OK).entity("Hello World!")
					.build();
		} catch (Exception e) {
			log.error("Could not connect the Proposal Service error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Proposal Service\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetProposalStatusList")
	@ApiOperation(value = "Get all Proposal Status", notes = "This API gets all Proposal Status for dropdown")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Proposal Status Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getProposalStatusList() {
		try {
			log.info("ProposalService::getProposalStatusList started");

			ObjectMapper mapper = new ObjectMapper();
			List<ProposalStatusInfo> proposalStatusList = new ArrayList<ProposalStatusInfo>();
			for (Status status : Status.values()) {
				ProposalStatusInfo proposalStatus = new ProposalStatusInfo();
				proposalStatus.setStatusKey(status.name());
				proposalStatus.setStatusValue(status.toString());
				proposalStatusList.add(proposalStatus);
			}

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(proposalStatusList)).build();

		} catch (Exception e) {
			log.error("Could not find all Proposal Status error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Proposal Status\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/GetProposalsList")
	@ApiOperation(value = "Get all Proposals", notes = "This API gets all Proposals")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Proposal Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceProposalsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::produceProposalsJSON started");

			List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

			int offset = 0, limit = 0;
			String projectTitle = new String();
			String usernameBy = new String();
			Double totalCostsFrom = 0.0;
			Double totalCostsTo = 0.0;
			String submittedOnFrom = new String();
			String submittedOnTo = new String();
			String proposalStatus = new String();

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
					submittedOnTo = proposalObj.get("SubmittedOnTo")
							.textValue();
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
					proposalStatus = proposalObj.get("ProposalStatus")
							.textValue();
				}
			}

			proposals = proposalDAO.findAllForProposalGrid(offset, limit,
					projectTitle, usernameBy, submittedOnFrom, submittedOnTo,
					totalCostsFrom, totalCostsTo, proposalStatus);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(proposals)).build();

		} catch (Exception e) {
			log.error("Could not find all Proposals error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Proposals\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/GetUserProposalsList")
	@ApiOperation(value = "Get all Users", notes = "This API get all Proposals of a User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Proposal Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceUserProposalsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::produceUserProposalsJSON started");

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
					submittedOnTo = proposalObj.get("SubmittedOnTo")
							.textValue();
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
					proposalStatus = proposalObj.get("ProposalStatus")
							.textValue();
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

			proposals = proposalDAO.findUserProposalGrid(offset, limit,
					projectTitle, usernameBy, submittedOnFrom, submittedOnTo,
					totalCostsFrom, totalCostsTo, proposalStatus, userRole,
					userProfileID, userCollege, userDepartment,
					userPositionType, userPositionTitle);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(proposals)).build();

		} catch (Exception e) {
			log.error("Could not find all Proposals of a User error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Proposals Of A User\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/AllProposalsExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(value = "Export all Proposals in a grid", notes = "This API exports all Proposals shown in a grid For Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportAllProposalsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::exportAllProposalsJSON started");

			List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

			String projectTitle = new String();
			String usernameBy = new String();
			Double totalCostsFrom = 0.0;
			Double totalCostsTo = 0.0;
			String submittedOnFrom = new String();
			String submittedOnTo = new String();
			String proposalStatus = new String();

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
					submittedOnTo = proposalObj.get("SubmittedOnTo")
							.textValue();
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
					proposalStatus = proposalObj.get("ProposalStatus")
							.textValue();
				}
			}

			proposals = proposalDAO.findAllProposals(projectTitle, usernameBy,
					submittedOnFrom, submittedOnTo, totalCostsFrom,
					totalCostsTo, proposalStatus);

			String filename = new String();
			if (proposals.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("Proposals");
				SheetWriter<ProposalInfo> writer = sheet
						.getBeanWriter(ProposalInfo.class);

				writer.write(proposals);

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
			log.error("Could not export Proposal list error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Export Proposal List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/ProposalsExportToExcel")
	@Produces(MediaType.TEXT_HTML)
	@ApiOperation(value = "Export all Proposals in a grid", notes = "This API exports all Proposals shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportProposalsJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::exportProposalsJSON started");

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
					submittedOnTo = proposalObj.get("SubmittedOnTo")
							.textValue();
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
					proposalStatus = proposalObj.get("ProposalStatus")
							.textValue();
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

			proposals = proposalDAO.findAllUserProposals(projectTitle,
					usernameBy, submittedOnFrom, submittedOnTo, totalCostsFrom,
					totalCostsTo, proposalStatus, userRole, userProfileID,
					userCollege, userDepartment, userPositionType,
					userPositionTitle);

			String filename = new String();
			if (proposals.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("Proposals");
				SheetWriter<ProposalInfo> writer = sheet
						.getBeanWriter(ProposalInfo.class);

				writer.write(proposals);

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
			log.error("Could not export all Proposals error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Export All Proposals\", \"status\": \"FAIL\"}")
				.build();
	}

	// Delete
	@POST
	@Path("/DeleteProposalByAdmin")
	@ApiOperation(value = "Delete Proposal by Admin", notes = "This API deletes Proposal by Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response deleteProposalByAdmin(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::deleteProposalByAdmin started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			String proposalId = new String();

			if (root != null && root.has("proposalId")) {
				proposalId = root.get("proposalId").textValue();
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

			ObjectId id = new ObjectId(proposalId);
			Proposal existingProposal = proposalDAO
					.findProposalByProposalID(id);

			ObjectId authorId = new ObjectId(userProfileID);
			UserProfile authorProfile = userProfileDAO
					.findUserDetailsByProfileID(authorId);
			// String authorFullName = authorProfile.getFullName();
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();

			boolean isDeleted = proposalDAO.deleteProposalByAdmin(
					existingProposal, authorProfile);
			if (isDeleted) {
				EmailUtil emailUtil = new EmailUtil();
				String emailSubject = new String();
				String emailBody = new String();
				String piEmail = new String();
				List<String> emaillist = new ArrayList<String>();

				emailSubject = "The proposal has been deleted by: "
						+ authorUserName;
				emailBody = "Hello User,<br/><br/>The proposal has been deleted by Admin.<br/><br/>Thank you, <br/> GPMS Team";
				piEmail = existingProposal.getInvestigatorInfo().getPi()
						.getUserRef().getWorkEmails().get(0);

				for (InvestigatorRefAndPosition copis : existingProposal
						.getInvestigatorInfo().getCo_pi()) {
					emaillist.add(copis.getUserRef().getWorkEmails().get(0));
				}

				for (InvestigatorRefAndPosition seniors : existingProposal
						.getInvestigatorInfo().getSeniorPersonnel()) {
					emaillist.add(seniors.getUserRef().getWorkEmails().get(0));
				}

				List<SignatureUserInfo> signatures = proposalDAO
						.findSignaturesExceptInvestigator(id,
								existingProposal.isIrbApprovalRequired());

				for (SignatureUserInfo signatureInfo : signatures) {
					emaillist.add(signatureInfo.getEmail());
				}

				emailUtil.sendMailMultipleUsersWithoutAuth(piEmail, emaillist,
						emailSubject, emailBody);

				String notificationMessage = "Deleted by " + authorUserName
						+ ".";

				broadCastNotification(existingProposal.getId().toString(),
						existingProposal.getProjectInfo().getProjectTitle(),
						notificationMessage, "Proposal", true, true, true,
						true, true, true, true, true, true, true);

				return Response
						.status(Response.Status.OK)
						.entity(mapper.writerWithDefaultPrettyPrinter()
								.writeValueAsString(true)).build();
			}
		} catch (Exception e) {
			log.error("Could not delete Proposal error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delete Proposal\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/DeleteProposalByProposalID")
	@ApiOperation(value = "Delete Proposal by ProposalId", notes = "This API deletes Proposal by ProposalId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response deleteProposalByProposalID(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::deleteProposalByProposalID started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			StringBuffer contentProfile = new StringBuffer();

			if (root != null && root.has("policyInfo")) {
				JsonNode policyInfo = root.get("policyInfo");
				if (policyInfo != null && policyInfo.isArray()
						&& policyInfo.size() > 0) {

					String proposalId = new String();
					String proposalRoles = new String();
					String proposalUserTitle = new String();

					if (root != null && root.has("proposalId")) {
						proposalId = root.get("proposalId").textValue();
					}

					if (root != null && root.has("proposalRoles")) {
						proposalRoles = root.get("proposalRoles").textValue();
					}

					if (root != null && root.has("proposalUserTitle")) {
						proposalUserTitle = root.get("proposalUserTitle")
								.textValue();
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

					ObjectId id = new ObjectId(proposalId);
					Proposal existingProposal = proposalDAO
							.findProposalByProposalID(id);

					List<SignatureUserInfo> signatures = proposalDAO
							.findSignaturesExceptInvestigator(id,
									existingProposal.isIrbApprovalRequired());

					ObjectId authorId = new ObjectId(userProfileID);
					UserProfile authorProfile = userProfileDAO
							.findUserDetailsByProfileID(authorId);
					String authorFullName = authorProfile.getFullName();
					String authorUserName = authorProfile.getUserAccount()
							.getUserName();

					contentProfile.append("<Content>");
					contentProfile
							.append("<ak:record xmlns:ak=\"http://akpower.org\">");
					contentProfile.append("<ak:proposal>");

					contentProfile.append("<ak:proposalid>");
					contentProfile.append(proposalId);
					contentProfile.append("</ak:proposalid>");

					contentProfile.append("<ak:proposaltitle>");
					contentProfile.append(existingProposal.getProjectInfo()
							.getProjectTitle());
					contentProfile.append("</ak:proposaltitle>");

					contentProfile.append("<ak:irbApprovalRequired>");
					contentProfile.append(existingProposal
							.isIrbApprovalRequired());
					contentProfile.append("</ak:irbApprovalRequired>");

					contentProfile.append("<ak:submittedbypi>");
					contentProfile.append(existingProposal.getSubmittedByPI()
							.name());
					contentProfile.append("</ak:submittedbypi>");

					contentProfile.append("<ak:readyforsubmissionbypi>");
					contentProfile.append(existingProposal
							.isReadyForSubmissionByPI());
					contentProfile.append("</ak:readyforsubmissionbypi>");

					contentProfile.append("<ak:deletedbypi>");
					contentProfile.append(existingProposal.getDeletedByPI()
							.name());
					contentProfile.append("</ak:deletedbypi>");

					contentProfile.append("<ak:approvedbydepartmentchair>");
					contentProfile.append(existingProposal.getChairApproval()
							.name());
					contentProfile.append("</ak:approvedbydepartmentchair>");

					contentProfile.append("<ak:approvedbybusinessmanager>");
					contentProfile.append(existingProposal
							.getBusinessManagerApproval().name());
					contentProfile.append("</ak:approvedbybusinessmanager>");

					contentProfile.append("<ak:approvedbyirb>");
					contentProfile.append(existingProposal.getIrbApproval()
							.name());
					contentProfile.append("</ak:approvedbyirb>");

					contentProfile.append("<ak:approvedbydean>");
					contentProfile.append(existingProposal.getDeanApproval()
							.name());
					contentProfile.append("</ak:approvedbydean>");

					contentProfile
							.append("<ak:approvedbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorApproval().name());
					contentProfile
							.append("</ak:approvedbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:withdrawnbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorWithdraw().name());
					contentProfile
							.append("</ak:withdrawnbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:submittedbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorSubmission().name());
					contentProfile
							.append("</ak:submittedbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:approvedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorDeletion().name());
					contentProfile
							.append("</ak:approvedbyuniversityresearchdirector>");

					contentProfile
							.append("<ak:deletedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorDeletion().name());
					contentProfile
							.append("</ak:deletedbyuniversityresearchdirector>");

					contentProfile
							.append("<ak:archivedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorArchived().name());
					contentProfile
							.append("</ak:archivedbyuniversityresearchdirector>");

					contentProfile.append("<ak:authorprofile>");
					// contentProfile.append("<ak:firstname>");
					// contentProfile.append(authorProfile.getFirstName());
					// contentProfile.append("</ak:firstname>");
					// contentProfile.append("<ak:middlename>");
					// contentProfile
					// .append(authorProfile.getMiddleName());
					// contentProfile.append("</ak:middlename>");
					//
					// contentProfile.append("<ak:lastname>");
					// contentProfile.append(authorProfile.getLastName());
					// contentProfile.append("</ak:lastname>");

					contentProfile.append("<ak:fullname>");
					contentProfile.append(authorFullName);
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:userid>");
					contentProfile.append(authorProfile.getId().toString());
					contentProfile.append("</ak:userid>");

					contentProfile.append("</ak:authorprofile>");

					contentProfile.append("<ak:pi>");
					contentProfile.append("<ak:fullname>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserRef()
							.getFullName());
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:workemail>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserRef()
							.getWorkEmails().get(0));
					contentProfile.append("</ak:workemail>");

					contentProfile.append("<ak:userid>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserProfileId());
					contentProfile.append("</ak:userid>");
					contentProfile.append("</ak:pi>");

					for (InvestigatorRefAndPosition copis : existingProposal
							.getInvestigatorInfo().getCo_pi()) {
						contentProfile.append("<ak:copi>");
						contentProfile.append("<ak:fullname>");
						contentProfile.append(copis.getUserRef().getFullName());
						contentProfile.append("</ak:fullname>");

						contentProfile.append("<ak:workemail>");
						contentProfile.append(copis.getUserRef()
								.getWorkEmails().get(0));
						contentProfile.append("</ak:workemail>");

						contentProfile.append("<ak:userid>");
						contentProfile.append(copis.getUserProfileId());
						contentProfile.append("</ak:userid>");
						contentProfile.append("</ak:copi>");
					}

					for (InvestigatorRefAndPosition seniors : existingProposal
							.getInvestigatorInfo().getSeniorPersonnel()) {
						contentProfile.append("<ak:senior>");
						contentProfile.append("<ak:fullname>");
						contentProfile.append(seniors.getUserRef()
								.getFullName());
						contentProfile.append("</ak:fullname>");

						contentProfile.append("<ak:workemail>");
						contentProfile.append(seniors.getUserRef()
								.getWorkEmails().get(0));
						contentProfile.append("</ak:workemail>");

						contentProfile.append("<ak:userid>");
						contentProfile.append(seniors.getUserProfileId());
						contentProfile.append("</ak:userid>");
						contentProfile.append("</ak:senior>");
					}

					for (SignatureUserInfo signatureInfo : signatures) {
						switch (signatureInfo.getPositionTitle()) {
						case "Department Chair":
							contentProfile.append("<ak:chair>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:chair>");

							break;
						case "Business Manager":
							contentProfile.append("<ak:manager>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:manager>");
							break;
						case "Dean":
							contentProfile.append("<ak:dean>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:dean>");
							break;
						case "IRB":
							contentProfile.append("<ak:irb>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:irb>");
							break;
						case "University Research Administrator":
							contentProfile.append("<ak:administrator>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:administrator>");
							break;
						case "University Research Director":
							contentProfile.append("<ak:director>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:director>");
							break;
						}
					}

					contentProfile.append("</ak:proposal>");
					contentProfile.append("</ak:record>");
					contentProfile.append("</Content>");

					contentProfile
							.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
					contentProfile
							.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
					contentProfile.append("</Attribute>");

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
					// network.type

					// Device type
					// device.type

					Set<AbstractResult> set = ac
							.getXACMLdecisionWithObligations(attrMap,
									contentProfile);
					Iterator<AbstractResult> it = set.iterator();
					int intDecision = 3;
					while (it.hasNext()) {
						AbstractResult ar = it.next();
						intDecision = ar.getDecision();

						if (intDecision >= 4 && intDecision <= 6) {
							intDecision = 2;
						}
						System.out.println("Decision:" + intDecision
								+ " that is: "
								+ AbstractResult.DECISIONS[intDecision]);

						if (AbstractResult.DECISIONS[intDecision]
								.equals("Permit")) {
							List<ObligationResult> obligations = ar
									.getObligations();

							EmailUtil emailUtil = new EmailUtil();
							String emailSubject = new String();
							String emailBody = new String();
							String authorName = new String();
							String piEmail = new String();
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
											if (assignment
													.getAttributeId()
													.toString()
													.equalsIgnoreCase(
															"obligationType")) {
												obligationType = assignment
														.getContent();
												break;
											}
										}

										if (obligationType
												.equals("preobligation")) {
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

												switch (assignment
														.getAttributeId()
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

												switch (assignment
														.getAttributeId()
														.toString()) {
												// case "obligationType":
												// obligationType =
												// assignment.getContent();
												// break;
												case "authorName":
													authorName = assignment
															.getContent();
													break;
												case "emailSubject":
													emailSubject = assignment
															.getContent();
													break;
												case "emailBody":
													emailBody = assignment
															.getContent();
													break;
												case "piEmail":
													piEmail = assignment
															.getContent();
													break;
												case "copisEmail":
												case "seniorsEmail":
												case "chairsEmail":
												case "managersEmail":
												case "deansEmail":
												case "irbsEmail":
												case "administratorsEmail":
												case "directorsEmail":
													if (!assignment
															.getContent()
															.equals("")) {
														emaillist
																.add(assignment
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

							boolean isDeleted = proposalDAO.deleteProposal(
									existingProposal, proposalRoles,
									proposalUserTitle, authorProfile);
							if (isDeleted) {
								if (!emailSubject.equals("")) {
									emailUtil.sendMailMultipleUsersWithoutAuth(
											piEmail, emaillist, emailSubject
													+ authorName, emailBody);
								}

								String notificationMessage = "Deleted by "
										+ authorUserName + ".";

								if (proposalRoles.equals("PI")
										&& !proposalUserTitle
												.equals("University Research Director")) {
									broadCastNotification(existingProposal
											.getId().toString(),
											existingProposal.getProjectInfo()
													.getProjectTitle(),
											notificationMessage, "Proposal",
											true, true, true, true, false,
											false, false, false, false, false);
								} else if (!proposalRoles.equals("PI")
										&& proposalUserTitle
												.equals("University Research Director")) {
									broadCastNotification(existingProposal
											.getId().toString(),
											existingProposal.getProjectInfo()
													.getProjectTitle(),
											notificationMessage, "Proposal",
											true, true, true, true, true, true,
											true, true, true, true);
								}

								return Response
										.status(200)
										.type(MediaType.APPLICATION_JSON)
										.entity(mapper
												.writerWithDefaultPrettyPrinter()
												.writeValueAsString(true))
										.build();
								// return
								// Response.status(200).entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(true)).build();
							} else {
								return Response
										.status(200)
										.type(MediaType.APPLICATION_JSON)
										.entity(mapper
												.writerWithDefaultPrettyPrinter()
												.writeValueAsString(true))
										.build();
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
				} else {
					return Response.status(403)
							.type(MediaType.APPLICATION_JSON)
							.entity("No User Permission Attributes are send!")
							.build();
				}
			}
		} catch (Exception e) {
			log.error("Could not delete the selected Proposal error e=", e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"No User Permission Attributes are send!\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/DeleteMultipleProposalsByAdmin")
	@ApiOperation(value = "Delete Multiple Proposals by Admin", notes = "This API deletes multiple Proposals by Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response deleteMultipleProposalsByAdmin(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::deleteMultipleProposalsByAdmin started");

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
			// String authorFullName = authorProfile.getFullName();
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();

			for (String proposalId : proposals) {
				ObjectId id = new ObjectId(proposalId);
				Proposal existingProposal = proposalDAO
						.findProposalByProposalID(id);

				boolean isDeleted = proposalDAO.deleteProposalByAdmin(
						existingProposal, authorProfile);

				if (isDeleted) {
					EmailUtil emailUtil = new EmailUtil();
					String emailSubject = new String();
					String emailBody = new String();
					String piEmail = new String();
					List<String> emaillist = new ArrayList<String>();

					emailSubject = "The proposal has been deleted by: "
							+ authorUserName;
					emailBody = "Hello User,<br/><br/>The proposal has been deleted by Admin.<br/><br/>Thank you, <br/> GPMS Team";
					piEmail = existingProposal.getInvestigatorInfo().getPi()
							.getUserRef().getWorkEmails().get(0);

					for (InvestigatorRefAndPosition copis : existingProposal
							.getInvestigatorInfo().getCo_pi()) {
						emaillist
								.add(copis.getUserRef().getWorkEmails().get(0));
					}

					for (InvestigatorRefAndPosition seniors : existingProposal
							.getInvestigatorInfo().getSeniorPersonnel()) {
						emaillist.add(seniors.getUserRef().getWorkEmails()
								.get(0));
					}

					List<SignatureUserInfo> signatures = proposalDAO
							.findSignaturesExceptInvestigator(id,
									existingProposal.isIrbApprovalRequired());

					for (SignatureUserInfo signatureInfo : signatures) {
						emaillist.add(signatureInfo.getEmail());
					}

					emailUtil.sendMailMultipleUsersWithoutAuth(piEmail,
							emaillist, emailSubject, emailBody);

					String projectTitle = existingProposal.getProjectInfo()
							.getProjectTitle();
					String notificationMessage = "Deleted by " + authorUserName
							+ ".";

					broadCastNotification(proposalId, projectTitle,
							notificationMessage, "Proposal", true, true, true,
							true, true, true, true, true, true, true);
				}
			}

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(true)).build();

		} catch (Exception e) {
			log.error("Could not delete Multiple Proposals error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Delete Multiple Proposals\", \"status\": \"FAIL\"}")
				.build();

	}

	@POST
	@Path("/GetAvailableActionsByProposalId")
	@ApiOperation(value = "Get Proposal Details by ProposalId", notes = "This API gets Proposal Details by ProposalId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Proposal }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceAvailableActionsByProposalId(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::produceAvailableActionsByProposalId started");

			String proposalId = new String();
			String proposalRoles = new String();

			HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

			Multimap<String, String> subjectMap = ArrayListMultimap.create();
			Multimap<String, String> resourceMap = ArrayListMultimap.create();
			Multimap<String, String> actionMap = ArrayListMultimap.create();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("proposalId")) {
				proposalId = root.get("proposalId").textValue();
			}

			if (root != null && root.has("proposalRoles")) {
				proposalRoles = root.get("proposalRoles").textValue();
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

					if (proposalRoles.equals("") && proposalRoles.isEmpty()) {
						subjectMap.put("position.title", userPositionTitle);
						attrMap.put("Subject", subjectMap);
					} else {
						// String[] currentProposalRoles =
						// proposalRoles.split(", ");
						subjectMap.put("proposal.role", proposalRoles);
						attrMap.put("Subject", subjectMap);
					}
				}
			}

			ObjectId id = new ObjectId(proposalId);
			Proposal existingProposal = proposalDAO
					.findProposalByProposalID(id);

			StringBuffer contentProfile = new StringBuffer();

			contentProfile.append("<Content>");
			contentProfile
					.append("<ak:record xmlns:ak=\"http://akpower.org\">");
			contentProfile.append("<ak:proposal>");

			contentProfile.append("<ak:proposalid>");
			contentProfile.append(proposalId);
			contentProfile.append("</ak:proposalid>");

			contentProfile.append("<ak:proposaltitle>");
			contentProfile.append(existingProposal.getProjectInfo()
					.getProjectTitle());
			contentProfile.append("</ak:proposaltitle>");

			contentProfile.append("<ak:irbApprovalRequired>");
			contentProfile.append(existingProposal.isIrbApprovalRequired());
			contentProfile.append("</ak:irbApprovalRequired>");

			contentProfile.append("<ak:submittedbypi>");
			contentProfile.append(existingProposal.getSubmittedByPI().name());
			contentProfile.append("</ak:submittedbypi>");

			contentProfile.append("<ak:readyforsubmissionbypi>");
			contentProfile.append(existingProposal.isReadyForSubmissionByPI());
			contentProfile.append("</ak:readyforsubmissionbypi>");

			contentProfile.append("<ak:deletedbypi>");
			contentProfile.append(existingProposal.getDeletedByPI().name());
			contentProfile.append("</ak:deletedbypi>");

			contentProfile.append("<ak:approvedbydepartmentchair>");
			contentProfile.append(existingProposal.getChairApproval().name());
			contentProfile.append("</ak:approvedbydepartmentchair>");

			contentProfile.append("<ak:approvedbybusinessmanager>");
			contentProfile.append(existingProposal.getBusinessManagerApproval()
					.name());
			contentProfile.append("</ak:approvedbybusinessmanager>");

			contentProfile.append("<ak:approvedbyirb>");
			contentProfile.append(existingProposal.getIrbApproval().name());
			contentProfile.append("</ak:approvedbyirb>");

			contentProfile.append("<ak:approvedbydean>");
			contentProfile.append(existingProposal.getDeanApproval().name());
			contentProfile.append("</ak:approvedbydean>");

			contentProfile
					.append("<ak:approvedbyuniversityresearchadministrator>");
			contentProfile.append(existingProposal
					.getResearchAdministratorApproval().name());
			contentProfile
					.append("</ak:approvedbyuniversityresearchadministrator>");

			contentProfile
					.append("<ak:withdrawnbyuniversityresearchadministrator>");
			contentProfile.append(existingProposal
					.getResearchAdministratorWithdraw().name());
			contentProfile
					.append("</ak:withdrawnbyuniversityresearchadministrator>");

			contentProfile
					.append("<ak:submittedbyuniversityresearchadministrator>");
			contentProfile.append(existingProposal
					.getResearchAdministratorSubmission().name());
			contentProfile
					.append("</ak:submittedbyuniversityresearchadministrator>");

			contentProfile.append("<ak:approvedbyuniversityresearchdirector>");
			contentProfile.append(existingProposal
					.getResearchDirectorDeletion().name());
			contentProfile.append("</ak:approvedbyuniversityresearchdirector>");

			contentProfile.append("<ak:deletedbyuniversityresearchdirector>");
			contentProfile.append(existingProposal
					.getResearchDirectorDeletion().name());
			contentProfile.append("</ak:deletedbyuniversityresearchdirector>");

			contentProfile.append("<ak:archivedbyuniversityresearchdirector>");
			contentProfile.append(existingProposal
					.getResearchDirectorArchived().name());
			contentProfile.append("</ak:archivedbyuniversityresearchdirector>");

			contentProfile.append("<ak:authorprofile>");
			contentProfile.append("<ak:userid>");
			contentProfile.append(userProfileID);
			contentProfile.append("</ak:userid>");
			contentProfile.append("</ak:authorprofile>");

			DateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ssXXX");

			contentProfile.append("<ak:currentdatetime>");
			contentProfile.append(dateFormat.format(new Date()));
			contentProfile.append("</ak:currentdatetime>");

			contentProfile.append("</ak:proposal>");
			contentProfile.append("</ak:record>");
			contentProfile.append("</Content>");

			contentProfile
					.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
			contentProfile
					.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
			contentProfile.append("</Attribute>");

			if (attrMap.get("Resource") == null) {
				attrMap.put("Resource", resourceMap);
			}

			Accesscontrol ac = new Accesscontrol();

			// TODO :: Get these static actions from the Dictionary we setup in
			// "XACMLDatasheet.xls" //Add, Add Co-PI, Add Senior Personnel,
			// Save, Submit, Approve, Disapprove, Withdraw, Archive, Delete,
			// View, Edit, Revoke

			List<String> attributeValue = Arrays.asList("Save", "Submit",
					"Approve", "Disapprove", "Withdraw", "Archive", "Delete");

			for (String action : attributeValue) {
				actionMap.put("proposal.action", action);
				attrMap.put("Action", actionMap);
			}

			Set<AbstractResult> results = ac.getXACMLdecisionForMDPWithProfile(
					attrMap, contentProfile);

			List<String> actions = new ArrayList<String>();
			for (AbstractResult result : results) {
				if (AbstractResult.DECISION_PERMIT == result.getDecision()) {
					Set<Attributes> attributesSet = ((Result) result)
							.getAttributes();
					for (Attributes attributes : attributesSet) {
						for (Attribute attribute : attributes.getAttributes()) {
							actions.add(attribute.getValue().encode());
						}
					}
				}
			}

			// Collections.sort(actions);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.setDateFormat(formatter)
							.writerWithDefaultPrettyPrinter()
							.writeValueAsString(actions)).build();

		} catch (Exception e) {
			log.error("Could not find Proposal Details by ProposalId error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Proposal Details By ProposalId\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetProposalDetailsByProposalId")
	@ApiOperation(value = "Get Proposal Details by ProposalId", notes = "This API gets Proposal Details by ProposalId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Proposal }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceProposalDetailsByProposalId(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::produceProposalDetailsByProposalId started");

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

			// mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,
			// false);

			return Response
					.status(Response.Status.OK)
					.entity(mapper.setDateFormat(formatter)
							.writerWithDefaultPrettyPrinter()
							.writeValueAsString(proposal)).build();

		} catch (Exception e) {
			log.error("Could not find Proposal Details by ProposalId error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Proposal Details By ProposalId\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetProposalAuditLogList")
	@ApiOperation(value = "Get Proposal Audit Log List", notes = "This API gets Proposal Audit Log List")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { AuditLog Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response produceProposalAuditLogJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::produceProposalAuditLogJSON started");

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

				if (auditLogBindObj != null
						&& auditLogBindObj.has("ActivityOnTo")) {
					activityOnTo = auditLogBindObj.get("ActivityOnTo")
							.textValue();
				}
			}

			ObjectId id = new ObjectId(proposalId);

			proposalAuditLogs = proposalDAO.findAllForProposalAuditLogGrid(
					offset, limit, id, action, auditedBy, activityOnFrom,
					activityOnTo);

			// users = (ArrayList<UserInfo>)
			// userProfileDAO.findAllForUserGrid();
			// response = JSONTansformer.ConvertToJSON(users);

			;
			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(proposalAuditLogs)).build();

		} catch (Exception e) {
			log.error("Could not find Proposal Audit Log List error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find Proposal Audit Log List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/ProposalLogsExportToExcel")
	@ApiOperation(value = "Export all Proposal Logs in a grid", notes = "This API exports all Proposal Logs shown in a grid")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Excel Filename/ No Record}"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response exportProposalAuditLogJSON(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::exportProposalAuditLogJSON started");

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

				if (auditLogBindObj != null
						&& auditLogBindObj.has("ActivityOnTo")) {
					activityOnTo = auditLogBindObj.get("ActivityOnTo")
							.textValue();
				}
			}

			ObjectId id = new ObjectId(proposalId);

			proposalAuditLogs = proposalDAO.findAllUserProposalAuditLogs(id,
					action, auditedBy, activityOnFrom, activityOnTo);

			// users = (ArrayList<UserInfo>)
			// userProfileDAO.findAllForUserGrid();
			// response = JSONTansformer.ConvertToJSON(users);
			String filename = new String();
			if (proposalAuditLogs.size() > 0) {
				Xcelite xcelite = new Xcelite();
				XceliteSheet sheet = xcelite.createSheet("AuditLogs");
				SheetWriter<AuditLogInfo> writer = sheet
						.getBeanWriter(AuditLogInfo.class);

				writer.write(proposalAuditLogs);

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
			log.error("Could not export Proposal Logs error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Proposal Logs List\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/CheckUniqueProjectTitle")
	@ApiOperation(value = "Check for Unique Project Title", notes = "This API checks if provided Project Title is Unique or not")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True/ False }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response checkUniqueProjectTitle(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::checkUniqueProjectTitle started");

			String proposalID = new String();
			String newProjectTitle = new String();
			String response = new String();

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			if (root != null && root.has("proposalUniqueObj")) {
				JsonNode proposalUniqueObj = root.get("proposalUniqueObj");
				if (proposalUniqueObj != null
						&& proposalUniqueObj.has("ProposalID")) {
					proposalID = proposalUniqueObj.get("ProposalID")
							.textValue();
				}

				if (proposalUniqueObj != null
						&& proposalUniqueObj.has("NewProjectTitle")) {
					String projectTitle = proposalUniqueObj
							.get("NewProjectTitle").textValue().trim()
							.replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(projectTitle)) {
						newProjectTitle = projectTitle;
					} else {
						return Response.status(403)
								.entity("The Project Title can not be Empty")
								.build();
					}
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

			return Response.status(Response.Status.OK).entity(response).build();

		} catch (Exception e) {
			log.error("Could not check for unique Project Title error e=", e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Check For Unique Project Title\", \"status\": \"FAIL\"}")
				.build();
	}

	@POST
	@Path("/GetAllSignatureForAProposal")
	@ApiOperation(value = "Get all Signatures For A Proposal", notes = "This API gets all Signatures for a Proposal")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { Signature Info }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response getAllSignatureForAProposal(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::getAllSignatureForAProposal started");

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
			// // TODO : get all delegated User Info for this PI user and bind
			// it
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

			return Response
					.status(Response.Status.OK)
					.entity(mapper.writerWithDefaultPrettyPrinter()
							.writeValueAsString(signatures)).build();

		} catch (Exception e) {
			log.error("Could not find all Signatures for a Proposal error e=",
					e);
		}

		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"error\": \"Could Not Find All Signatures For A Proposal\", \"status\": \"FAIL\"}")
				.build();
	}

	// Withdraw and Archive
	@POST
	@Path("/UpdateProposalStatus")
	@ApiOperation(value = "Update Proposal Status", notes = "This API updates Proposal Status")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 400, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response updateProposalStatus(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::updateProposalStatus started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

			StringBuffer contentProfile = new StringBuffer();

			if (root != null && root.has("policyInfo")) {
				JsonNode policyInfo = root.get("policyInfo");
				if (policyInfo != null && policyInfo.isArray()
						&& policyInfo.size() > 0) {

					String proposalId = new String();
					String proposalUserTitle = new String();

					if (root != null && root.has("proposalId")) {
						proposalId = root.get("proposalId").textValue();
					}

					if (root != null && root.has("proposalUserTitle")) {
						proposalUserTitle = root.get("proposalUserTitle")
								.textValue();
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

					ObjectId id = new ObjectId(proposalId);
					Proposal existingProposal = proposalDAO
							.findProposalByProposalID(id);

					List<SignatureUserInfo> signatures = proposalDAO
							.findSignaturesExceptInvestigator(id,
									existingProposal.isIrbApprovalRequired());

					ObjectId authorId = new ObjectId(userProfileID);
					UserProfile authorProfile = userProfileDAO
							.findUserDetailsByProfileID(authorId);
					String authorFullName = authorProfile.getFullName();
					String authorUserName = authorProfile.getUserAccount()
							.getUserName();

					contentProfile.append("<Content>");
					contentProfile
							.append("<ak:record xmlns:ak=\"http://akpower.org\">");
					contentProfile.append("<ak:proposal>");

					contentProfile.append("<ak:proposalid>");
					contentProfile.append(proposalId);
					contentProfile.append("</ak:proposalid>");

					contentProfile.append("<ak:proposaltitle>");
					contentProfile.append(existingProposal.getProjectInfo()
							.getProjectTitle());
					contentProfile.append("</ak:proposaltitle>");

					contentProfile.append("<ak:irbApprovalRequired>");
					contentProfile.append(existingProposal
							.isIrbApprovalRequired());
					contentProfile.append("</ak:irbApprovalRequired>");

					contentProfile.append("<ak:submittedbypi>");
					contentProfile.append(existingProposal.getSubmittedByPI()
							.name());
					contentProfile.append("</ak:submittedbypi>");

					contentProfile.append("<ak:readyforsubmissionbypi>");
					contentProfile.append(existingProposal
							.isReadyForSubmissionByPI());
					contentProfile.append("</ak:readyforsubmissionbypi>");

					contentProfile.append("<ak:deletedbypi>");
					contentProfile.append(existingProposal.getDeletedByPI()
							.name());
					contentProfile.append("</ak:deletedbypi>");

					contentProfile.append("<ak:approvedbydepartmentchair>");
					contentProfile.append(existingProposal.getChairApproval()
							.name());
					contentProfile.append("</ak:approvedbydepartmentchair>");

					contentProfile.append("<ak:approvedbybusinessmanager>");
					contentProfile.append(existingProposal
							.getBusinessManagerApproval().name());
					contentProfile.append("</ak:approvedbybusinessmanager>");

					contentProfile.append("<ak:approvedbyirb>");
					contentProfile.append(existingProposal.getIrbApproval()
							.name());
					contentProfile.append("</ak:approvedbyirb>");

					contentProfile.append("<ak:approvedbydean>");
					contentProfile.append(existingProposal.getDeanApproval()
							.name());
					contentProfile.append("</ak:approvedbydean>");

					contentProfile
							.append("<ak:approvedbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorApproval().name());
					contentProfile
							.append("</ak:approvedbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:withdrawnbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorWithdraw().name());
					contentProfile
							.append("</ak:withdrawnbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:submittedbyuniversityresearchadministrator>");
					contentProfile.append(existingProposal
							.getResearchAdministratorSubmission().name());
					contentProfile
							.append("</ak:submittedbyuniversityresearchadministrator>");

					contentProfile
							.append("<ak:approvedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorDeletion().name());
					contentProfile
							.append("</ak:approvedbyuniversityresearchdirector>");

					contentProfile
							.append("<ak:deletedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorDeletion().name());
					contentProfile
							.append("</ak:deletedbyuniversityresearchdirector>");

					contentProfile
							.append("<ak:archivedbyuniversityresearchdirector>");
					contentProfile.append(existingProposal
							.getResearchDirectorArchived().name());
					contentProfile
							.append("</ak:archivedbyuniversityresearchdirector>");

					contentProfile.append("<ak:authorprofile>");
					// contentProfile.append("<ak:firstname>");
					// contentProfile.append(authorProfile.getFirstName());
					// contentProfile.append("</ak:firstname>");
					// contentProfile.append("<ak:middlename>");
					// contentProfile
					// .append(authorProfile.getMiddleName());
					// contentProfile.append("</ak:middlename>");
					//
					// contentProfile.append("<ak:lastname>");
					// contentProfile.append(authorProfile.getLastName());
					// contentProfile.append("</ak:lastname>");

					contentProfile.append("<ak:fullname>");
					contentProfile.append(authorFullName);
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:userid>");
					contentProfile.append(authorProfile.getId().toString());
					contentProfile.append("</ak:userid>");

					contentProfile.append("</ak:authorprofile>");

					contentProfile.append("<ak:pi>");
					contentProfile.append("<ak:fullname>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserRef()
							.getFullName());
					contentProfile.append("</ak:fullname>");

					contentProfile.append("<ak:workemail>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserRef()
							.getWorkEmails().get(0));
					contentProfile.append("</ak:workemail>");

					contentProfile.append("<ak:userid>");
					contentProfile.append(existingProposal
							.getInvestigatorInfo().getPi().getUserProfileId());
					contentProfile.append("</ak:userid>");
					contentProfile.append("</ak:pi>");

					for (InvestigatorRefAndPosition copis : existingProposal
							.getInvestigatorInfo().getCo_pi()) {
						contentProfile.append("<ak:copi>");
						contentProfile.append("<ak:fullname>");
						contentProfile.append(copis.getUserRef().getFullName());
						contentProfile.append("</ak:fullname>");

						contentProfile.append("<ak:workemail>");
						contentProfile.append(copis.getUserRef()
								.getWorkEmails().get(0));
						contentProfile.append("</ak:workemail>");

						contentProfile.append("<ak:userid>");
						contentProfile.append(copis.getUserProfileId());
						contentProfile.append("</ak:userid>");
						contentProfile.append("</ak:copi>");
					}

					for (InvestigatorRefAndPosition seniors : existingProposal
							.getInvestigatorInfo().getSeniorPersonnel()) {
						contentProfile.append("<ak:senior>");
						contentProfile.append("<ak:fullname>");
						contentProfile.append(seniors.getUserRef()
								.getFullName());
						contentProfile.append("</ak:fullname>");

						contentProfile.append("<ak:workemail>");
						contentProfile.append(seniors.getUserRef()
								.getWorkEmails().get(0));
						contentProfile.append("</ak:workemail>");

						contentProfile.append("<ak:userid>");
						contentProfile.append(seniors.getUserProfileId());
						contentProfile.append("</ak:userid>");
						contentProfile.append("</ak:senior>");
					}

					for (SignatureUserInfo signatureInfo : signatures) {
						switch (signatureInfo.getPositionTitle()) {
						case "Department Chair":
							contentProfile.append("<ak:chair>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:chair>");

							break;
						case "Business Manager":
							contentProfile.append("<ak:manager>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:manager>");
							break;
						case "Dean":
							contentProfile.append("<ak:dean>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:dean>");
							break;
						case "IRB":
							contentProfile.append("<ak:irb>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:irb>");
							break;
						case "University Research Administrator":
							contentProfile.append("<ak:administrator>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:administrator>");
							break;
						case "University Research Director":
							contentProfile.append("<ak:director>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(signatureInfo.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(signatureInfo.getEmail());
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(signatureInfo
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:director>");
							break;
						}
					}

					contentProfile.append("</ak:proposal>");
					contentProfile.append("</ak:record>");
					contentProfile.append("</Content>");

					contentProfile
							.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
					contentProfile
							.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
					contentProfile.append("</Attribute>");

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
					// network.type

					// Device type
					// device.type

					Set<AbstractResult> set = ac
							.getXACMLdecisionWithObligations(attrMap,
									contentProfile);
					Iterator<AbstractResult> it = set.iterator();
					int intDecision = 3;
					while (it.hasNext()) {
						AbstractResult ar = it.next();
						intDecision = ar.getDecision();

						if (intDecision >= 4 && intDecision <= 6) {
							intDecision = 2;
						}
						System.out.println("Decision:" + intDecision
								+ " that is: "
								+ AbstractResult.DECISIONS[intDecision]);

						if (AbstractResult.DECISIONS[intDecision]
								.equals("Permit")) {
							List<ObligationResult> obligations = ar
									.getObligations();

							EmailUtil emailUtil = new EmailUtil();
							String emailSubject = new String();
							String emailBody = new String();
							String authorName = new String();
							String piEmail = new String();
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
											if (assignment
													.getAttributeId()
													.toString()
													.equalsIgnoreCase(
															"obligationType")) {
												obligationType = assignment
														.getContent();
												break;
											}
										}

										if (obligationType
												.equals("preobligation")) {
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

												switch (assignment
														.getAttributeId()
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

												switch (assignment
														.getAttributeId()
														.toString()) {
												// case "obligationType":
												// obligationType =
												// assignment.getContent();
												// break;
												case "authorName":
													authorName = assignment
															.getContent();
													break;
												case "emailSubject":
													emailSubject = assignment
															.getContent();
													break;
												case "emailBody":
													emailBody = assignment
															.getContent();
													break;
												case "piEmail":
													piEmail = assignment
															.getContent();
													break;
												case "copisEmail":
												case "seniorsEmail":
												case "chairsEmail":
												case "managersEmail":
												case "deansEmail":
												case "irbsEmail":
												case "administratorsEmail":
												case "directorsEmail":
													if (!assignment
															.getContent()
															.equals("")) {
														emaillist
																.add(assignment
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

							if (root != null && root.has("buttonType")) {
								JsonNode buttonType = root.get("buttonType");

								if (buttonType != null) {
									switch (buttonType.textValue()) {
									case "Withdraw":
										if (!proposalId.equals("0")) {
											if (existingProposal
													.getResearchAdministratorWithdraw() == WithdrawType.NOTWITHDRAWN
													&& existingProposal
															.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
													&& proposalUserTitle
															.equals("University Research Administrator")) {
												existingProposal
														.setResearchAdministratorWithdraw(WithdrawType.WITHDRAWN);
												existingProposal
														.setResearchAdministratorApproval(ApprovalType.NOTREADYFORAPPROVAL);

												// Proposal Status
												existingProposal
														.getProposalStatus()
														.clear();
												existingProposal
														.getProposalStatus()
														.add(Status.WITHDRAWBYRESEARCHADMIN);

												return updateProposalStatus(
														existingProposal,
														authorProfile,
														authorUserName,
														emailUtil,
														emailSubject,
														emailBody, authorName,
														piEmail, emaillist,
														"Withdrawn");
											}
										}
										break;

									case "Archive":
										if (!proposalId.equals("0")) {
											if (existingProposal
													.getResearchDirectorArchived() == ArchiveType.NOTARCHIVED
													&& existingProposal
															.getResearchAdministratorSubmission() == SubmitType.SUBMITTED
													&& proposalUserTitle
															.equals("University Research Director")) {
												existingProposal
														.setResearchDirectorArchived(ArchiveType.ARCHIVED);
												existingProposal
														.setResearchDirectorApproval(ApprovalType.NOTREADYFORAPPROVAL);

												// Proposal Status
												existingProposal
														.getProposalStatus()
														.clear();
												existingProposal
														.getProposalStatus()
														.add(Status.ARCHIVEDBYRESEARCHDIRECTOR);

												return updateProposalStatus(
														existingProposal,
														authorProfile,
														authorUserName,
														emailUtil,
														emailSubject,
														emailBody, authorName,
														piEmail, emaillist,
														"Archived");
											}
										}
										break;

									default:

										break;
									}
								}
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
				} else {
					return Response.status(403)
							.type(MediaType.APPLICATION_JSON)
							.entity("No User Permission Attributes are send!")
							.build();
				}
			}
		} catch (Exception e) {
			log.error("Could not update Proposal Status error e=", e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"No User Permission Attributes are send!\", \"status\": \"FAIL\"}")
				.build();
	}

	private Response updateProposalStatus(Proposal existingProposal,
			UserProfile authorProfile, String authorUserName,
			EmailUtil emailUtil, String emailSubject, String emailBody,
			String authorName, String piEmail, List<String> emaillist,
			String changeDone) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		boolean isStatusUpdated = proposalDAO.updateProposalStatus(
				existingProposal, authorProfile);

		if (isStatusUpdated) {
			if (!emailSubject.equals("")) {
				emailUtil.sendMailMultipleUsersWithoutAuth(piEmail, emaillist,
						emailSubject + authorName, emailBody);
			}

			String notificationMessage = changeDone + " by " + authorUserName
					+ ".";

			if (changeDone.equals("Withdrawn")) {
				broadCastNotification(existingProposal.getId().toString(),
						existingProposal.getProjectInfo().getProjectTitle(),
						notificationMessage, "Proposal", true, true, true,
						true, true, true, true, true, true, false);
			} else if (changeDone.equals("Archived")) {
				broadCastNotification(existingProposal.getId().toString(),
						existingProposal.getProjectInfo().getProjectTitle(),
						notificationMessage, "Proposal", true, true, true,
						true, false, false, false, false, false, false);
			}

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
	}

	// Save By Admin
	@POST
	@Path("/SaveUpdateProposalByAdmin")
	@ApiOperation(value = "Save a New User or Update an existing Proposal by Admin", notes = "This API saves a New User or updates an existing Proposal by Admin")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response saveUpdateProposalByAdmin(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::saveUpdateProposalByAdmin started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

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
			// String authorFullName = authorProfile.getFullName();
			String authorUserName = authorProfile.getUserAccount()
					.getUserName();

			String proposalID = new String();
			Proposal existingProposal = new Proposal();
			Proposal oldProposal = new Proposal();

			if (root != null && root.has("proposalInfo")) {
				JsonNode proposalInfo = root.get("proposalInfo");

				if (proposalInfo != null && proposalInfo.has("ProposalID")) {
					proposalID = proposalInfo.get("ProposalID").textValue();
					if (!proposalID.equals("0")) {
						ObjectId proposalId = new ObjectId(proposalID);
						existingProposal = proposalDAO
								.findProposalByProposalID(proposalId);
						// using our serializable method for cloning
						oldProposal = SerializationHelper
								.cloneThroughSerialize(existingProposal);
					}
				}

				// Appendix Info
				if (proposalInfo != null && proposalInfo.has("AppendixInfo")) {
					List<Appendix> appendixInfo = Arrays.asList(mapper
							.readValue(proposalInfo.get("AppendixInfo")
									.toString(), Appendix[].class));
					if (appendixInfo.size() != 0) {

						String UPLOAD_PATH = new String();
						try {
							UPLOAD_PATH = this.getClass()
									.getResource("/uploads").toURI().getPath();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}

						List<String> existingFiles = new ArrayList<String>();
						if (!proposalID.equals("0")) {
							boolean alreadyExist = false;
							for (Appendix appendix : oldProposal
									.getAppendices()) {
								for (Appendix appendixObj : appendixInfo) {
									if (appendix.getFilename()
											.equalsIgnoreCase(
													appendixObj.getFilename())
											&& appendix
													.getTitle()
													.equalsIgnoreCase(
															appendixObj
																	.getTitle()
																	.trim()
																	.replaceAll(
																			"\\<[^>]*>",
																			""))) {
										alreadyExist = true;
										existingFiles.add(appendixObj
												.getFilename());
										break;
									}
								}
								if (!alreadyExist) {
									existingProposal.getAppendices().remove(
											appendix);
								}
							}

							for (Appendix uploadFile : appendixInfo) {
								String fileName = uploadFile.getFilename();
								if (!existingFiles.contains(fileName)) {
									File file = new File(UPLOAD_PATH + fileName);

									String extension = "";
									int i = fileName.lastIndexOf('.');
									if (i > 0) {
										extension = fileName.substring(i + 1);

										if (verifyValidFileExtension(extension)) {
											uploadFile.setExtension(extension);
										} else {
											return Response
													.status(403)
													.entity(extension
															+ " is not allowed. Allowed extensions: jpg,png,gif,jpeg,bmp,png,pdf,doc,docx,xls,xlsx,txt")
													.build();
										}
									}

									long fileSize = file.length();
									if (verifyValidFileSize(fileSize)) {
										uploadFile.setFilesize(fileSize);
									} else {
										return Response
												.status(403)
												.entity("The uploaded file is larger than 5MB")
												.build();
									}
									uploadFile.setFilepath("/uploads/"
											+ fileName);

									String fileTitle = uploadFile.getTitle()
											.trim().replaceAll("\\<[^>]*>", "");

									if (validateNotEmptyValue(fileTitle)) {
										uploadFile.setTitle(fileTitle);
									} else {
										return Response
												.status(403)
												.entity("The Uploaded File's Title can not be Empty")
												.build();
									}

									existingProposal.getAppendices().add(
											uploadFile);
								}
							}
						} else {
							for (Appendix uploadFile : appendixInfo) {
								String fileName = uploadFile.getFilename();
								File file = new File(UPLOAD_PATH + fileName);

								String extension = "";
								int i = fileName.lastIndexOf('.');
								if (i > 0) {
									extension = fileName.substring(i + 1);
									if (verifyValidFileExtension(extension)) {
										uploadFile.setExtension(extension);
									} else {
										return Response
												.status(403)
												.entity(extension
														+ " is not allowed. Allowed extensions: jpg,png,gif,jpeg,bmp,png,pdf,doc,docx,xls,xlsx,txt")
												.build();
									}
								}

								long fileSize = file.length();
								if (verifyValidFileSize(fileSize)) {
									uploadFile.setFilesize(fileSize);
								} else {
									return Response
											.status(403)
											.entity("The uploaded file is larger than 5MB")
											.build();
								}
								uploadFile.setFilesize(fileSize);
								uploadFile.setFilepath("/uploads/" + fileName);

								String fileTitle = uploadFile.getTitle().trim()
										.replaceAll("\\<[^>]*>", "");
								if (validateNotEmptyValue(fileTitle)) {
									uploadFile.setTitle(fileTitle);
								} else {
									return Response
											.status(403)
											.entity("The Uploaded File's Title can not be Empty")
											.build();
								}

								existingProposal.getAppendices()
										.add(uploadFile);
							}
						}
					}
				}

				// InvestigatorInfo
				// To hold all new Investigators list to get notified
				InvestigatorInfo addedInvestigators = new InvestigatorInfo();
				InvestigatorInfo existingInvestigators = new InvestigatorInfo();
				InvestigatorInfo deletedInvestigators = new InvestigatorInfo();

				if (proposalInfo != null
						&& proposalInfo.has("InvestigatorInfo")) {

					if (!proposalID.equals("0")) {
						// MUST Clear all co-PI and Senior Personnel
						existingProposal.getInvestigatorInfo().getCo_pi()
								.clear();
						existingProposal.getInvestigatorInfo()
								.getSeniorPersonnel().clear();
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
								// .equals(investigatorRefAndPosition))
								// {
								// existingProposal.getInvestigatorInfo().setPi(
								// investigatorRefAndPosition);
								// if
								// (!addedInvestigators.getPi().equals(
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

									if (!addedInvestigators.getCo_pi()
											.contains(
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

									if (!addedInvestigators
											.getSeniorPersonnel().contains(
													investigatorRefAndPosition)) {
										addedInvestigators
												.getSeniorPersonnel()
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
						existingProposal
								.setInvestigatorInfo(newInvestigatorInfo);
						addedInvestigators = newInvestigatorInfo;
					} else {

						// TO see the deleted from addedInvestigators vs
						// existingInvestigators
						// Existing Investigator Info to compare
						existingInvestigators = oldProposal
								.getInvestigatorInfo();

						for (InvestigatorRefAndPosition coPI : existingInvestigators
								.getCo_pi()) {
							if (!existingProposal.getInvestigatorInfo()
									.getCo_pi().contains(coPI)) {
								if (!deletedInvestigators.getCo_pi().contains(
										coPI)) {
									deletedInvestigators.getCo_pi().add(coPI);
									existingProposal.getInvestigatorInfo()
											.getCo_pi().remove(coPI);
								}
							} else {
								addedInvestigators.getCo_pi().remove(coPI);
							}
						}

						for (InvestigatorRefAndPosition senior : existingInvestigators
								.getSeniorPersonnel()) {
							if (!existingProposal.getInvestigatorInfo()
									.getSeniorPersonnel().contains(senior)) {
								if (!deletedInvestigators.getSeniorPersonnel()
										.contains(senior)) {
									deletedInvestigators.getSeniorPersonnel()
											.add(senior);
									existingProposal.getInvestigatorInfo()
											.getSeniorPersonnel()
											.remove(senior);
								}
							} else {
								addedInvestigators.getSeniorPersonnel().remove(
										senior);
							}
						}

						// Remove Signatures FOR Deleted Investigators
						for (InvestigatorRefAndPosition coPI : deletedInvestigators
								.getCo_pi()) {
							for (SignatureInfo sign : oldProposal
									.getSignatureInfo()) {
								if (coPI.getUserProfileId().equalsIgnoreCase(
										sign.getUserProfileId())) {
									existingProposal.getSignatureInfo().remove(
											sign);
								}
							}
						}
					}
				}

				ProjectInfo newProjectInfo = new ProjectInfo();

				if (proposalInfo != null && proposalInfo.has("ProjectInfo")) {
					JsonNode projectInfo = proposalInfo.get("ProjectInfo");
					if (projectInfo != null && projectInfo.has("ProjectTitle")) {
						final String proposalTitle = projectInfo
								.get("ProjectTitle").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(proposalTitle)) {
							if (!proposalID.equals("0")) {
								if (!existingProposal.getProjectInfo()
										.getProjectTitle()
										.equals(proposalTitle)) {
									existingProposal.getProjectInfo()
											.setProjectTitle(proposalTitle);
								}
							} else {
								newProjectInfo.setProjectTitle(proposalTitle);
							}
						} else {
							return Response
									.status(403)
									.entity("The Proposal Title can not be Empty")
									.build();
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
							if (!existingProposal.getProjectInfo()
									.getProjectType().equals(projectType)) {
								existingProposal.getProjectInfo()
										.setProjectType(projectType);
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
								existingProposal.getProjectInfo()
										.setTypeOfRequest(typeOfRequest);
							}
						} else {
							newProjectInfo.setTypeOfRequest(typeOfRequest);
						}
					}

					if (projectInfo != null
							&& projectInfo.has("ProjectLocation")) {
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
									.getProjectLocation()
									.equals(projectLocation)) {
								existingProposal.getProjectInfo()
										.setProjectLocation(projectLocation);
							}
						} else {
							newProjectInfo.setProjectLocation(projectLocation);
						}
					}

					if (projectInfo != null && projectInfo.has("DueDate")) {
						Date dueDate = formatter.parse(projectInfo
								.get("DueDate").textValue().trim()
								.replaceAll("\\<[^>]*>", ""));

						if (validateNotEmptyValue(dueDate.toString())) {
							if (!proposalID.equals("0")) {
								if (!existingProposal.getProjectInfo()
										.getDueDate().equals(dueDate)) {
									existingProposal.getProjectInfo()
											.setDueDate(dueDate);
								}
							} else {
								newProjectInfo.setDueDate(dueDate);
							}
						} else {
							return Response.status(403)
									.entity("The Due Date can not be Empty")
									.build();
						}
					}

					ProjectPeriod projectPeriod = new ProjectPeriod();

					if (projectInfo != null
							&& projectInfo.has("ProjectPeriodFrom")) {
						Date periodFrom = formatter.parse(projectInfo
								.get("ProjectPeriodFrom").textValue().trim()
								.replaceAll("\\<[^>]*>", ""));
						if (validateNotEmptyValue(periodFrom.toString())) {
							projectPeriod.setFrom(periodFrom);
						} else {
							return Response
									.status(403)
									.entity("The Project Period From can not be Empty")
									.build();
						}
					}

					if (projectInfo != null
							&& projectInfo.has("ProjectPeriodTo")) {
						Date periodTo = formatter.parse(projectInfo
								.get("ProjectPeriodTo").textValue().trim()
								.replaceAll("\\<[^>]*>", ""));
						if (validateNotEmptyValue(periodTo.toString())) {
							projectPeriod.setTo(periodTo);
						} else {
							return Response
									.status(403)
									.entity("The Project Period To can not be Empty")
									.build();
						}
					}
					if (!proposalID.equals("0")) {
						if (!existingProposal.getProjectInfo()
								.getProjectPeriod().equals(projectPeriod)) {
							existingProposal.getProjectInfo().setProjectPeriod(
									projectPeriod);
						}
					} else {
						newProjectInfo.setProjectPeriod(projectPeriod);
					}
				}

				// ProjectInfo
				if (proposalID.equals("0")) {
					existingProposal.setProjectInfo(newProjectInfo);
				}

				SponsorAndBudgetInfo newSponsorAndBudgetInfo = new SponsorAndBudgetInfo();
				if (proposalInfo != null
						&& proposalInfo.has("SponsorAndBudgetInfo")) {
					JsonNode sponsorAndBudgetInfo = proposalInfo
							.get("SponsorAndBudgetInfo");
					if (sponsorAndBudgetInfo != null
							&& sponsorAndBudgetInfo.has("GrantingAgency")) {
						for (String grantingAgency : sponsorAndBudgetInfo
								.get("GrantingAgency").textValue().trim()
								.replaceAll("\\<[^>]*>", "").split(", ")) {
							if (validateNotEmptyValue(grantingAgency)) {
								newSponsorAndBudgetInfo.getGrantingAgency()
										.add(grantingAgency);
							} else {
								return Response
										.status(403)
										.entity("The Granting Agency can not be Empty")
										.build();
							}
						}
					}

					if (sponsorAndBudgetInfo != null
							&& sponsorAndBudgetInfo.has("DirectCosts")) {
						final String directCost = sponsorAndBudgetInfo
								.get("DirectCosts").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(directCost)) {
							newSponsorAndBudgetInfo.setDirectCosts(Double
									.parseDouble(directCost));
						} else {
							return Response
									.status(403)
									.entity("The Direct Costs can not be Empty")
									.build();
						}
					}

					if (sponsorAndBudgetInfo != null
							&& sponsorAndBudgetInfo.has("FACosts")) {
						final String FACosts = sponsorAndBudgetInfo
								.get("FACosts").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(FACosts)) {
							newSponsorAndBudgetInfo.setFaCosts(Double
									.parseDouble(FACosts));
						} else {
							return Response.status(403)
									.entity("The FA Costs can not be Empty")
									.build();
						}
					}

					if (sponsorAndBudgetInfo != null
							&& sponsorAndBudgetInfo.has("TotalCosts")) {
						final String totalCosts = sponsorAndBudgetInfo
								.get("TotalCosts").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(totalCosts)) {
							newSponsorAndBudgetInfo.setTotalCosts(Double
									.parseDouble(totalCosts));
						} else {
							return Response.status(403)
									.entity("The Total Costs can not be Empty")
									.build();
						}
					}

					if (sponsorAndBudgetInfo != null
							&& sponsorAndBudgetInfo.has("FARate")) {
						final String FARate = sponsorAndBudgetInfo
								.get("FARate").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(FARate)) {
							newSponsorAndBudgetInfo.setFaRate(Double
									.parseDouble(FARate));
						} else {
							throw new Exception("The FA Rate can not be Empty");
						}
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
					existingProposal
							.setSponsorAndBudgetInfo(newSponsorAndBudgetInfo);
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
					existingProposal.setCostShareInfo(newCostShareInfo);
				}

				UniversityCommitments newUnivCommitments = new UniversityCommitments();
				if (proposalInfo != null && proposalInfo.has("UnivCommitments")) {
					JsonNode univCommitments = proposalInfo
							.get("UnivCommitments");
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
					existingProposal
							.setUniversityCommitments(newUnivCommitments);
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
							&& conflicOfInterestInfo
									.has("DisclosureFormChange")) {
						switch (conflicOfInterestInfo.get(
								"DisclosureFormChange").textValue()) {
						case "1":
							newConflictOfInterest.setDisclosureFormChange(true);
							break;
						case "2":
							newConflictOfInterest
									.setDisclosureFormChange(false);
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
					existingProposal
							.setConflicOfInterest(newConflictOfInterest);
				}

				AdditionalInfo newAdditionalInfo = new AdditionalInfo();
				if (proposalInfo != null && proposalInfo.has("AdditionalInfo")) {
					JsonNode additionalInfo = proposalInfo
							.get("AdditionalInfo");
					if (additionalInfo != null
							&& additionalInfo
									.has("AnticipatesForeignNationalsPayment")) {
						switch (additionalInfo.get(
								"AnticipatesForeignNationalsPayment")
								.textValue()) {
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
							&& additionalInfo
									.has("AnticipatesCourseReleaseTime")) {
						switch (additionalInfo.get(
								"AnticipatesCourseReleaseTime").textValue()) {
						case "1":
							newAdditionalInfo
									.setAnticipatesCourseReleaseTime(true);
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
					existingProposal.setAdditionalInfo(newAdditionalInfo);
				}

				CollaborationInfo newCollaborationInfo = new CollaborationInfo();
				if (proposalInfo != null
						&& proposalInfo.has("CollaborationInfo")) {
					JsonNode collaborationInfo = proposalInfo
							.get("CollaborationInfo");
					if (collaborationInfo != null
							&& collaborationInfo.has("InvolveNonFundedCollab")) {
						switch (collaborationInfo.get("InvolveNonFundedCollab")
								.textValue()) {
						case "1":
							newCollaborationInfo
									.setInvolveNonFundedCollab(true);
							if (collaborationInfo != null
									&& collaborationInfo.has("Collaborators")) {
								final String collabarationName = collaborationInfo
										.get("Collaborators").textValue()
										.trim().replaceAll("\\<[^>]*>", "");
								if (validateNotEmptyValue(collabarationName)) {
									newCollaborationInfo
											.setInvolvedCollaborators(collabarationName);
								} else {
									return Response
											.status(403)
											.entity("Collaborators can not be Empty")
											.build();
								}
							}
							break;
						case "2":
							newCollaborationInfo
									.setInvolveNonFundedCollab(false);
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
						existingProposal
								.setCollaborationInfo(newCollaborationInfo);
					}
				} else {
					existingProposal.setCollaborationInfo(newCollaborationInfo);
				}

				ConfidentialInfo newConfidentialInfo = new ConfidentialInfo();
				if (proposalInfo != null
						&& proposalInfo.has("ConfidentialInfo")) {
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
								final String onPages = confidentialInfo
										.get("OnPages").textValue().trim()
										.replaceAll("\\<[^>]*>", "");
								if (validateNotEmptyValue(onPages)) {
									newConfidentialInfo.setOnPages(onPages);
								} else {
									return Response
											.status(403)
											.entity("The Pages can not be Empty")
											.build();
								}
							}
							if (confidentialInfo != null
									&& confidentialInfo.has("Patentable")) {
								newConfidentialInfo
										.setPatentable(confidentialInfo.get(
												"Patentable").booleanValue());
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
							&& confidentialInfo
									.has("InvolveIntellectualProperty")) {
						switch (confidentialInfo.get(
								"InvolveIntellectualProperty").textValue()) {
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
						existingProposal
								.setConfidentialInfo(newConfidentialInfo);
					}
				} else {
					existingProposal.setConfidentialInfo(newConfidentialInfo);
				}

				ComplianceInfo newComplianceInfo = new ComplianceInfo();
				Boolean irbApprovalRequired = false;
				if (proposalInfo != null && proposalInfo.has("ComplianceInfo")) {
					JsonNode complianceInfo = proposalInfo
							.get("ComplianceInfo");
					if (complianceInfo != null
							&& complianceInfo.has("InvolveUseOfHumanSubjects")) {
						switch (complianceInfo.get("InvolveUseOfHumanSubjects")
								.textValue()) {
						case "1":
							newComplianceInfo
									.setInvolveUseOfHumanSubjects(true);
							irbApprovalRequired = true;
							if (complianceInfo != null
									&& complianceInfo.has("IRBPending")) {
								switch (complianceInfo.get("IRBPending")
										.textValue()) {
								case "1":
									newComplianceInfo.setIrbPending(false);
									if (complianceInfo != null
											&& complianceInfo.has("IRB")) {
										final String IRBNo = complianceInfo
												.get("IRB").textValue().trim()
												.replaceAll("\\<[^>]*>", "");
										if (validateNotEmptyValue(IRBNo)) {
											newComplianceInfo.setIrb(IRBNo);
										} else {
											return Response
													.status(403)
													.entity("The IRB # can not be Empty")
													.build();
										}
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
							newComplianceInfo
									.setInvolveUseOfHumanSubjects(false);
							break;
						default:
							break;
						}
					}

					if (complianceInfo != null
							&& complianceInfo
									.has("InvolveUseOfVertebrateAnimals")) {
						switch (complianceInfo.get(
								"InvolveUseOfVertebrateAnimals").textValue()) {
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
										final String IACUCNo = complianceInfo
												.get("IACUC").textValue()
												.trim()
												.replaceAll("\\<[^>]*>", "");
										if (validateNotEmptyValue(IACUCNo)) {
											newComplianceInfo.setIacuc(IACUCNo);
										} else {
											return Response
													.status(403)
													.entity("The IACUC # can not be Empty")
													.build();
										}
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
										final String IBCNo = complianceInfo
												.get("IBC").textValue().trim()
												.replaceAll("\\<[^>]*>", "");

										if (validateNotEmptyValue(IBCNo)) {
											newComplianceInfo.setIbc(IBCNo);
										} else {
											return Response
													.status(403)
													.entity("The IBC # can not be Empty")
													.build();
										}
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
							newComplianceInfo
									.setInvolveBiosafetyConcerns(false);
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
					existingProposal.setComplianceInfo(newComplianceInfo);
					existingProposal
							.setIrbApprovalRequired(irbApprovalRequired);
				}

				String notificationMessage = new String();

				JsonNode buttonType = root.get("buttonType");

				// For Proposal Status
				if (buttonType != null) {
					switch (buttonType.textValue()) {
					case "Save":
						if (proposalID.equals("0")) {
							notificationMessage = "Created by "
									+ authorUserName + ".";
						} else if (!proposalID.equals("0")) {
							notificationMessage = "Updated by "
									+ authorUserName + ".";
						}
						break;

					default:

						break;
					}
				}

				String emailSubject = new String();
				String emailBody = new String();
				List<String> emaillist = new ArrayList<String>();

				boolean proposalIsChanged = false;

				if (!proposalID.equals("0")) {
					if (!existingProposal.equals(oldProposal)) {
						proposalDAO.updateProposal(existingProposal,
								authorProfile);
						proposalIsChanged = true;
						emailSubject = "The proposal has been updated by: "
								+ authorUserName;
						emailBody = "Hello User,<br/><br/>The proposal has been updated by Admin.<br/><br/>Thank you, <br/> GPMS Team";

						// ObjectId id = new ObjectId(proposalID);

						// List<SignatureUserInfo> signatures = proposalDAO
						// .findSignaturesExceptInvestigator(id,
						// existingProposal.isIrbApprovalRequired());
						//
						// for (SignatureUserInfo signatureInfo : signatures) {
						// emaillist.add(signatureInfo.getEmail());
						// }
					}
				} else {
					proposalDAO.saveProposal(existingProposal, authorProfile);
					proposalIsChanged = true;
					emailSubject = "The proposal has been created by: "
							+ authorUserName;
					emailBody = "Hello User,<br/><br/>The proposal has been created by Admin.<br/><br/>Thank you, <br/> GPMS Team";
				}

				if (proposalIsChanged) {
					NotifyAllExistingInvestigators(existingProposal.getId()
							.toString(), existingProposal.getProjectInfo()
							.getProjectTitle(), existingProposal,
							notificationMessage, "Proposal", true);

					EmailUtil emailUtil = new EmailUtil();

					String piEmail = existingProposal.getInvestigatorInfo()
							.getPi().getUserRef().getWorkEmails().get(0);

					for (InvestigatorRefAndPosition copis : existingProposal
							.getInvestigatorInfo().getCo_pi()) {
						emaillist
								.add(copis.getUserRef().getWorkEmails().get(0));
					}

					for (InvestigatorRefAndPosition seniors : existingProposal
							.getInvestigatorInfo().getSeniorPersonnel()) {
						emaillist.add(seniors.getUserRef().getWorkEmails()
								.get(0));
					}

					emailUtil.sendMailMultipleUsersWithoutAuth(piEmail,
							emaillist, emailSubject, emailBody);

					// broadCastNotification(existingProposal.getId().toString(),
					// existingProposal.getProjectInfo().getProjectTitle(),
					// notificationMessage, "Proposal", true, true, true,
					// true, true, true, true, true, true, true);

					return Response
							.status(200)
							.type(MediaType.APPLICATION_JSON)
							.entity(mapper.writerWithDefaultPrettyPrinter()
									.writeValueAsString(true)).build();
				} else {
					return Response
							.status(200)
							.type(MediaType.APPLICATION_JSON)
							.entity(mapper.writerWithDefaultPrettyPrinter()
									.writeValueAsString(true)).build();
				}
			} else {
				return Response.status(Response.Status.BAD_REQUEST)
						.type(MediaType.APPLICATION_JSON)
						.entity("No Proposal Info is send!").build();
			}
		} catch (Exception e) {
			log.error(
					"Could not save a New User or update an existing Proposal error e=",
					e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"Could Not Save A New User OR Update AN Existing Proposal\", \"status\": \"FAIL\"}")
				.build();
	}

	// Save Submit Approve Disapprove
	@POST
	@Path("/SaveUpdateProposal")
	@ApiOperation(value = "Save a New Proposal or Update an existing Proposal", notes = "This API saves a New Proposal or updates an existing Proposal")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response saveUpdateProposal(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::saveUpdateProposal started");

			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = mapper.readTree(message);

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
			String authorFullName = authorProfile.getFullName();

			String proposalID = new String();
			Proposal existingProposal = new Proposal();
			Proposal oldProposal = new Proposal();

			boolean signedByCurrentUser = false;

			StringBuffer contentProfile = new StringBuffer();
			Accesscontrol ac = new Accesscontrol();
			HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

			if (root != null && root.has("proposalInfo")) {
				JsonNode proposalInfo = root.get("proposalInfo");

				if (proposalInfo != null && proposalInfo.has("ProposalID")) {
					proposalID = proposalInfo.get("ProposalID").textValue();
					if (!proposalID.equals("0")) {
						ObjectId proposalId = new ObjectId(proposalID);
						existingProposal = proposalDAO
								.findProposalByProposalID(proposalId);
						// using our serializable method for cloning
						oldProposal = SerializationHelper
								.cloneThroughSerialize(existingProposal);
					}
				}

				// Appendix Info
				if (proposalInfo != null && proposalInfo.has("AppendixInfo")) {
					List<Appendix> appendixInfo = Arrays.asList(mapper
							.readValue(proposalInfo.get("AppendixInfo")
									.toString(), Appendix[].class));
					if (appendixInfo.size() != 0) {

						String UPLOAD_PATH = new String();
						try {
							UPLOAD_PATH = this.getClass()
									.getResource("/uploads").toURI().getPath();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}

						List<String> existingFiles = new ArrayList<String>();
						if (!proposalID.equals("0")) {
							boolean alreadyExist = false;
							for (Appendix appendix : oldProposal
									.getAppendices()) {
								for (Appendix appendixObj : appendixInfo) {
									if (appendix.getFilename()
											.equalsIgnoreCase(
													appendixObj.getFilename())
											&& appendix
													.getTitle()
													.equalsIgnoreCase(
															appendixObj
																	.getTitle()
																	.trim()
																	.replaceAll(
																			"\\<[^>]*>",
																			""))) {
										alreadyExist = true;
										existingFiles.add(appendixObj
												.getFilename());
										break;
									}
								}
								if (!alreadyExist) {
									existingProposal.getAppendices().remove(
											appendix);
								}
							}

							for (Appendix uploadFile : appendixInfo) {
								String fileName = uploadFile.getFilename();
								if (!existingFiles.contains(fileName)) {
									File file = new File(UPLOAD_PATH + fileName);

									String extension = "";
									int i = fileName.lastIndexOf('.');
									if (i > 0) {
										extension = fileName.substring(i + 1);

										if (verifyValidFileExtension(extension)) {
											uploadFile.setExtension(extension);
										} else {
											return Response
													.status(403)
													.entity(extension
															+ " is not allowed. Allowed extensions: jpg,png,gif,jpeg,bmp,png,pdf,doc,docx,xls,xlsx,txt")
													.build();
										}
									}

									long fileSize = file.length();
									if (verifyValidFileSize(fileSize)) {
										uploadFile.setFilesize(fileSize);
									} else {
										return Response
												.status(403)
												.entity("The uploaded file is larger than 5MB")
												.build();
									}
									uploadFile.setFilepath("/uploads/"
											+ fileName);
									String fileTitle = uploadFile.getTitle()
											.trim().replaceAll("\\<[^>]*>", "");

									if (validateNotEmptyValue(fileTitle)) {
										uploadFile.setTitle(fileTitle);
									} else {
										return Response
												.status(403)
												.entity("The Uploaded File's Title can not be Empty")
												.build();
									}

									existingProposal.getAppendices().add(
											uploadFile);
								}
							}
						} else {
							for (Appendix uploadFile : appendixInfo) {
								String fileName = uploadFile.getFilename();
								File file = new File(UPLOAD_PATH + fileName);

								String extension = "";
								int i = fileName.lastIndexOf('.');
								if (i > 0) {
									extension = fileName.substring(i + 1);
									if (verifyValidFileExtension(extension)) {
										uploadFile.setExtension(extension);
									} else {
										return Response
												.status(403)
												.entity(extension
														+ " is not allowed. Allowed extensions: jpg,png,gif,jpeg,bmp,png,pdf,doc,docx,xls,xlsx,txt")
												.build();
									}
								}

								long fileSize = file.length();
								if (verifyValidFileSize(fileSize)) {
									uploadFile.setFilesize(fileSize);
								} else {
									return Response
											.status(403)
											.entity("The uploaded file is larger than 5MB")
											.build();
								}
								uploadFile.setFilesize(fileSize);
								uploadFile.setFilepath("/uploads/" + fileName);
								String fileTitle = uploadFile.getTitle().trim()
										.replaceAll("\\<[^>]*>", "");
								if (validateNotEmptyValue(fileTitle)) {
									uploadFile.setTitle(fileTitle);
								} else {
									return Response
											.status(403)
											.entity("The Uploaded File's Title can not be Empty")
											.build();
								}

								existingProposal.getAppendices()
										.add(uploadFile);
							}
						}
					}
				}

				// InvestigatorInfo
				// To hold all new Investigators list to get notified
				InvestigatorInfo addedInvestigators = new InvestigatorInfo();
				InvestigatorInfo existingInvestigators = new InvestigatorInfo();
				InvestigatorInfo deletedInvestigators = new InvestigatorInfo();

				if (proposalInfo != null
						&& proposalInfo.has("InvestigatorInfo")) {

					if (!proposalID.equals("0")) {
						// MUST Clear all co-PI and Senior Personnel
						existingProposal.getInvestigatorInfo().getCo_pi()
								.clear();
						existingProposal.getInvestigatorInfo()
								.getSeniorPersonnel().clear();
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
								// .equals(investigatorRefAndPosition))
								// {
								// existingProposal.getInvestigatorInfo().setPi(
								// investigatorRefAndPosition);
								// if
								// (!addedInvestigators.getPi().equals(
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

									if (!addedInvestigators.getCo_pi()
											.contains(
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

									if (!addedInvestigators
											.getSeniorPersonnel().contains(
													investigatorRefAndPosition)) {
										addedInvestigators
												.getSeniorPersonnel()
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
						existingProposal
								.setInvestigatorInfo(newInvestigatorInfo);
						addedInvestigators = newInvestigatorInfo;
					} else {

						// TO see the deleted from addedInvestigators vs
						// existingInvestigators
						// Existing Investigator Info to compare
						existingInvestigators = oldProposal
								.getInvestigatorInfo();

						for (InvestigatorRefAndPosition coPI : existingInvestigators
								.getCo_pi()) {
							if (!existingProposal.getInvestigatorInfo()
									.getCo_pi().contains(coPI)) {
								if (!deletedInvestigators.getCo_pi().contains(
										coPI)) {
									deletedInvestigators.getCo_pi().add(coPI);
									existingProposal.getInvestigatorInfo()
											.getCo_pi().remove(coPI);
								}
							} else {
								addedInvestigators.getCo_pi().remove(coPI);
							}
						}

						for (InvestigatorRefAndPosition senior : existingInvestigators
								.getSeniorPersonnel()) {
							if (!existingProposal.getInvestigatorInfo()
									.getSeniorPersonnel().contains(senior)) {
								if (!deletedInvestigators.getSeniorPersonnel()
										.contains(senior)) {
									deletedInvestigators.getSeniorPersonnel()
											.add(senior);
									existingProposal.getInvestigatorInfo()
											.getSeniorPersonnel()
											.remove(senior);
								}
							} else {
								addedInvestigators.getSeniorPersonnel().remove(
										senior);
							}
						}

						// Remove Signatures FOR Deleted Investigators
						for (InvestigatorRefAndPosition coPI : deletedInvestigators
								.getCo_pi()) {
							for (SignatureInfo sign : oldProposal
									.getSignatureInfo()) {
								if (coPI.getUserProfileId().equalsIgnoreCase(
										sign.getUserProfileId())) {
									existingProposal.getSignatureInfo().remove(
											sign);
								}
							}
						}
					}
				}

				// Signature
				// To hold all new Investigators list to get notified
				if (proposalInfo != null && proposalInfo.has("SignatureInfo")) {
					String[] rows = proposalInfo.get("SignatureInfo")
							.textValue().split("#!#");

					List<SignatureInfo> newSignatureInfo = new ArrayList<SignatureInfo>();
					List<SignatureInfo> allSignatureInfo = new ArrayList<SignatureInfo>();
					List<SignatureInfo> removeSignatureInfo = new ArrayList<SignatureInfo>();
					// UserProfileID!#!Signature!#!SignedDate!#!Note!#!FullName!#!PositionTitle!#!Delegated#!#
					DateFormat format = new SimpleDateFormat(
							"yyyy/MM/dd hh:mm:ss a");

					for (String col : rows) {
						String[] cols = col.split("!#!");
						SignatureInfo signatureInfo = new SignatureInfo();
						signatureInfo.setUserProfileId(cols[0]);
						final String signatureText = cols[1].replaceAll(
								"\\<[^>]*>", "");
						if (validateNotEmptyValue(signatureText)) {
							signatureInfo.setSignature(signatureText);
						} else {
							return Response.status(403)
									.entity("The Signature can not be Empty")
									.build();
						}

						final String signedDate = cols[2].trim().replaceAll(
								"\\<[^>]*>", "");
						if (validateNotEmptyValue(signedDate)) {
							signatureInfo.setSignedDate(format
									.parse(signedDate));
						} else {
							return Response.status(403)
									.entity("The Signed Date can not be Empty")
									.build();
						}

						final String noteText = cols[3].replaceAll("\\<[^>]*>",
								"");
						if (validateNotEmptyValue(noteText)) {
							signatureInfo.setNote(noteText);
						} else {
							return Response.status(403)
									.entity("The Note can not be Empty")
									.build();
						}

						signatureInfo.setFullName(cols[4]);
						signatureInfo.setPositionTitle(cols[5]);
						signatureInfo.setDelegated(Boolean
								.parseBoolean(cols[6]));

						allSignatureInfo.add(signatureInfo);

						if (!proposalID.equals("0")) {
							boolean alreadyExist = false;
							for (SignatureInfo sign : existingProposal
									.getSignatureInfo()) {
								if (sign.equals(signatureInfo)) {
									alreadyExist = true;
									break;
								} else {
									if (sign.getUserProfileId().equals(cols[0])) {
										removeSignatureInfo.add(sign);
									}

								}
							}
							if (!alreadyExist) {
								newSignatureInfo.add(signatureInfo);
							}
						}

						for (SignatureInfo removeSign : removeSignatureInfo) {
							existingProposal.getSignatureInfo().remove(
									removeSign);
						}
					}
					// SignatureInfo
					// addedSignatures = newSignatureInfo;
					if (!proposalID.equals("0")) {
						if (!existingProposal.getSignatureInfo().equals(
								allSignatureInfo)) {
							for (SignatureInfo signatureInfo : newSignatureInfo) {
								existingProposal.getSignatureInfo().add(
										signatureInfo);
							}
						}
					} else {
						existingProposal.setSignatureInfo(allSignatureInfo);
					}

					for (SignatureInfo sign : existingProposal
							.getSignatureInfo()) {
						if (sign.getUserProfileId().equals(userProfileID)
								&& !sign.getSignature().trim().equals("")) {
							signedByCurrentUser = true;
							break;
						}
					}
				}

				ComplianceInfo newComplianceInfo = new ComplianceInfo();
				Boolean irbApprovalRequired = false;
				if (proposalInfo != null && proposalInfo.has("ComplianceInfo")) {
					JsonNode complianceInfo = proposalInfo
							.get("ComplianceInfo");
					if (complianceInfo != null
							&& complianceInfo.has("InvolveUseOfHumanSubjects")) {
						switch (complianceInfo.get("InvolveUseOfHumanSubjects")
								.textValue()) {
						case "1":
							newComplianceInfo
									.setInvolveUseOfHumanSubjects(true);
							irbApprovalRequired = true;
							if (complianceInfo != null
									&& complianceInfo.has("IRBPending")) {
								switch (complianceInfo.get("IRBPending")
										.textValue()) {
								case "1":
									newComplianceInfo.setIrbPending(false);
									if (complianceInfo != null
											&& complianceInfo.has("IRB")) {
										final String IRBNo = complianceInfo
												.get("IRB").textValue().trim()
												.replaceAll("\\<[^>]*>", "");
										if (validateNotEmptyValue(IRBNo)) {
											newComplianceInfo.setIrb(IRBNo);
										} else {
											return Response
													.status(403)
													.entity("The IRB # can not be Empty")
													.build();
										}
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
							newComplianceInfo
									.setInvolveUseOfHumanSubjects(false);
							break;
						default:
							break;
						}
					}

					if (complianceInfo != null
							&& complianceInfo
									.has("InvolveUseOfVertebrateAnimals")) {
						switch (complianceInfo.get(
								"InvolveUseOfVertebrateAnimals").textValue()) {
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
										final String IACUCNo = complianceInfo
												.get("IACUC").textValue()
												.trim()
												.replaceAll("\\<[^>]*>", "");
										if (validateNotEmptyValue(IACUCNo)) {
											newComplianceInfo.setIacuc(IACUCNo);
										} else {
											return Response
													.status(403)
													.entity("The IACUC # can not be Empty")
													.build();
										}
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
										final String IBCNo = complianceInfo
												.get("IBC").textValue().trim()
												.replaceAll("\\<[^>]*>", "");

										if (validateNotEmptyValue(IBCNo)) {
											newComplianceInfo.setIbc(IBCNo);
										} else {
											return Response
													.status(403)
													.entity("The IBC # can not be Empty")
													.build();
										}
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
							newComplianceInfo
									.setInvolveBiosafetyConcerns(false);
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
					existingProposal.setComplianceInfo(newComplianceInfo);
					existingProposal
							.setIrbApprovalRequired(irbApprovalRequired);
				}

				if (root != null && root.has("policyInfo")) {
					JsonNode policyInfo = root.get("policyInfo");
					if (policyInfo != null && policyInfo.isArray()
							&& policyInfo.size() > 0) {

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
								environmentMap.put(attributeName,
										attributeValue);
								attrMap.put("Environment", environmentMap);
								break;
							default:
								break;
							}
						}

						// TODO only check this for required not all XACML

						// Need to add Environment to detect the Campus or
						// outside
						// network
						// network.type

						// Device type
						// device.type

						List<SignatureUserInfo> signatures = new ArrayList<SignatureUserInfo>();
						SignatureByAllUsers signByAllUsersInfo = new SignatureByAllUsers();

						if (!proposalID.equals("0")) {
							ObjectId id = new ObjectId(proposalID);

							signatures = proposalDAO
									.findSignaturesExceptInvestigator(id,
											irbApprovalRequired);

							contentProfile.append("<Content>");
							contentProfile
									.append("<ak:record xmlns:ak=\"http://akpower.org\">");
							contentProfile.append("<ak:proposal>");

							contentProfile.append("<ak:proposalid>");
							contentProfile.append(proposalID);
							contentProfile.append("</ak:proposalid>");

							contentProfile.append("<ak:proposaltitle>");
							contentProfile.append(existingProposal
									.getProjectInfo().getProjectTitle());
							contentProfile.append("</ak:proposaltitle>");

							contentProfile.append("<ak:irbApprovalRequired>");
							contentProfile.append(existingProposal
									.isIrbApprovalRequired());
							contentProfile.append("</ak:irbApprovalRequired>");

							contentProfile.append("<ak:submittedbypi>");
							contentProfile.append(existingProposal
									.getSubmittedByPI().name());
							contentProfile.append("</ak:submittedbypi>");

							contentProfile
									.append("<ak:readyforsubmissionbypi>");
							contentProfile.append(existingProposal
									.isReadyForSubmissionByPI());
							contentProfile
									.append("</ak:readyforsubmissionbypi>");

							contentProfile.append("<ak:deletedbypi>");
							contentProfile.append(existingProposal
									.getDeletedByPI().name());
							contentProfile.append("</ak:deletedbypi>");

							contentProfile
									.append("<ak:approvedbydepartmentchair>");
							contentProfile.append(existingProposal
									.getChairApproval().name());
							contentProfile
									.append("</ak:approvedbydepartmentchair>");

							contentProfile
									.append("<ak:approvedbybusinessmanager>");
							contentProfile.append(existingProposal
									.getBusinessManagerApproval().name());
							contentProfile
									.append("</ak:approvedbybusinessmanager>");

							contentProfile.append("<ak:approvedbyirb>");
							contentProfile.append(existingProposal
									.getIrbApproval().name());
							contentProfile.append("</ak:approvedbyirb>");

							contentProfile.append("<ak:approvedbydean>");
							contentProfile.append(existingProposal
									.getDeanApproval().name());
							contentProfile.append("</ak:approvedbydean>");

							contentProfile
									.append("<ak:approvedbyuniversityresearchadministrator>");
							contentProfile.append(existingProposal
									.getResearchAdministratorApproval().name());
							contentProfile
									.append("</ak:approvedbyuniversityresearchadministrator>");

							contentProfile
									.append("<ak:withdrawnbyuniversityresearchadministrator>");
							contentProfile.append(existingProposal
									.getResearchAdministratorWithdraw().name());
							contentProfile
									.append("</ak:withdrawnbyuniversityresearchadministrator>");

							contentProfile
									.append("<ak:submittedbyuniversityresearchadministrator>");
							contentProfile.append(existingProposal
									.getResearchAdministratorSubmission()
									.name());
							contentProfile
									.append("</ak:submittedbyuniversityresearchadministrator>");

							contentProfile
									.append("<ak:approvedbyuniversityresearchdirector>");
							contentProfile.append(existingProposal
									.getResearchDirectorDeletion().name());
							contentProfile
									.append("</ak:approvedbyuniversityresearchdirector>");

							contentProfile
									.append("<ak:deletedbyuniversityresearchdirector>");
							contentProfile.append(existingProposal
									.getResearchDirectorDeletion().name());
							contentProfile
									.append("</ak:deletedbyuniversityresearchdirector>");

							contentProfile
									.append("<ak:archivedbyuniversityresearchdirector>");
							contentProfile.append(existingProposal
									.getResearchDirectorArchived().name());
							contentProfile
									.append("</ak:archivedbyuniversityresearchdirector>");

							contentProfile.append("<ak:authorprofile>");
							// contentProfile.append("<ak:firstname>");
							// contentProfile.append(authorProfile.getFirstName());
							// contentProfile.append("</ak:firstname>");
							// contentProfile.append("<ak:middlename>");
							// contentProfile
							// .append(authorProfile.getMiddleName());
							// contentProfile.append("</ak:middlename>");
							//
							// contentProfile.append("<ak:lastname>");
							// contentProfile.append(authorProfile.getLastName());
							// contentProfile.append("</ak:lastname>");

							contentProfile.append("<ak:fullname>");
							contentProfile.append(authorFullName);
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(authorProfile.getId()
									.toString());
							contentProfile.append("</ak:userid>");

							contentProfile.append("</ak:authorprofile>");

							DateFormat dateFormat = new SimpleDateFormat(
									"yyyy-MM-dd'T'HH:mm:ssXXX");

							contentProfile.append("<ak:currentdatetime>");
							contentProfile
									.append(dateFormat.format(new Date()));
							contentProfile.append("</ak:currentdatetime>");

							boolean signedByPI = false;
							boolean signedByAllCoPIs = false;
							boolean signedByAllChairs = false;
							boolean signedByAllBusinessManagers = false;
							boolean signedByAllDeans = false;
							boolean signedByAllIRBs = false;
							boolean signedByAllResearchAdmins = false;
							boolean signedByAllResearchDirectors = false;

							List<String> requiredPISign = new ArrayList<String>();
							List<String> existingPISign = new ArrayList<String>();
							List<String> requiredCoPISigns = new ArrayList<String>();
							List<String> existingCoPISigns = new ArrayList<String>();
							List<String> requiredChairSigns = new ArrayList<String>();
							List<String> existingChairSigns = new ArrayList<String>();
							List<String> requiredBusinessManagerSigns = new ArrayList<String>();
							List<String> existingBusinessManagerSigns = new ArrayList<String>();
							List<String> requiredDeanSigns = new ArrayList<String>();
							List<String> existingDeanSigns = new ArrayList<String>();
							List<String> requiredIRBSigns = new ArrayList<String>();
							List<String> existingIRBSigns = new ArrayList<String>();
							List<String> requiredResearchAdminSigns = new ArrayList<String>();
							List<String> existingResearchAdminSigns = new ArrayList<String>();
							List<String> requiredResearchDirectorSigns = new ArrayList<String>();
							List<String> existingResearchDirectorSigns = new ArrayList<String>();

							if (!existingProposal.getInvestigatorInfo().getPi()
									.getUserRef().isDeleted()) {
								contentProfile.append("<ak:pi>");
								contentProfile.append("<ak:fullname>");
								contentProfile.append(existingProposal
										.getInvestigatorInfo().getPi()
										.getUserRef().getFullName());
								contentProfile.append("</ak:fullname>");

								contentProfile.append("<ak:workemail>");
								contentProfile.append(existingProposal
										.getInvestigatorInfo().getPi()
										.getUserRef().getWorkEmails().get(0));
								contentProfile.append("</ak:workemail>");

								contentProfile.append("<ak:userid>");
								contentProfile.append(existingProposal
										.getInvestigatorInfo().getPi()
										.getUserProfileId());
								contentProfile.append("</ak:userid>");
								contentProfile.append("</ak:pi>");

								requiredPISign.add(existingProposal
										.getInvestigatorInfo().getPi()
										.getUserProfileId());
							}

							for (InvestigatorRefAndPosition copis : existingProposal
									.getInvestigatorInfo().getCo_pi()) {
								if (!copis.getUserRef().isDeleted()) {
									contentProfile.append("<ak:copi>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(copis.getUserRef()
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(copis.getUserRef()
											.getWorkEmails().get(0));
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(copis
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:copi>");

									requiredCoPISigns.add(copis
											.getUserProfileId());
								}
							}

							for (InvestigatorRefAndPosition seniors : existingProposal
									.getInvestigatorInfo().getSeniorPersonnel()) {
								if (!seniors.getUserRef().isDeleted()) {
									contentProfile.append("<ak:senior>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(seniors.getUserRef()
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(seniors.getUserRef()
											.getWorkEmails().get(0));
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(seniors
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:senior>");
								}
							}

							for (SignatureInfo sign : existingProposal
									.getSignatureInfo()) {
								if (sign.getPositionTitle().equals("PI")) {
									existingPISign.add(sign.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"Co-PI")) {
									existingCoPISigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"Department Chair")) {
									existingChairSigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"Business Manager")) {
									existingBusinessManagerSigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"Dean")) {
									existingDeanSigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle()
										.equals("IRB")) {
									existingIRBSigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"University Research Administrator")) {
									existingResearchAdminSigns.add(sign
											.getUserProfileId());
								} else if (sign.getPositionTitle().equals(
										"University Research Director")) {
									existingResearchDirectorSigns.add(sign
											.getUserProfileId());
								}
							}

							for (SignatureUserInfo signatureInfo : signatures) {
								switch (signatureInfo.getPositionTitle()) {
								case "Department Chair":
									contentProfile.append("<ak:chair>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:chair>");

									requiredChairSigns.add(signatureInfo
											.getUserProfileId());

									break;
								case "Business Manager":
									contentProfile.append("<ak:manager>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:manager>");

									requiredBusinessManagerSigns
											.add(signatureInfo
													.getUserProfileId());

									break;
								case "Dean":
									contentProfile.append("<ak:dean>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:dean>");

									requiredDeanSigns.add(signatureInfo
											.getUserProfileId());

									break;
								case "IRB":
									contentProfile.append("<ak:irb>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:irb>");

									requiredIRBSigns.add(signatureInfo
											.getUserProfileId());

									break;
								case "University Research Administrator":
									contentProfile.append("<ak:administrator>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile
											.append("</ak:administrator>");

									requiredResearchAdminSigns
											.add(signatureInfo
													.getUserProfileId());

									break;
								case "University Research Director":
									contentProfile.append("<ak:director>");
									contentProfile.append("<ak:fullname>");
									contentProfile.append(signatureInfo
											.getFullName());
									contentProfile.append("</ak:fullname>");

									contentProfile.append("<ak:workemail>");
									contentProfile.append(signatureInfo
											.getEmail());
									contentProfile.append("</ak:workemail>");

									contentProfile.append("<ak:userid>");
									contentProfile.append(signatureInfo
											.getUserProfileId());
									contentProfile.append("</ak:userid>");
									contentProfile.append("</ak:director>");

									requiredResearchDirectorSigns
											.add(signatureInfo
													.getUserProfileId());

									break;
								}
							}

							signedByPI = existingPISign
									.containsAll(requiredPISign);

							signedByAllCoPIs = existingCoPISigns
									.containsAll(requiredCoPISigns);

							signedByAllChairs = existingChairSigns
									.containsAll(requiredChairSigns);

							signedByAllBusinessManagers = existingBusinessManagerSigns
									.containsAll(requiredBusinessManagerSigns);

							signedByAllDeans = existingDeanSigns
									.containsAll(requiredDeanSigns);

							signedByAllIRBs = existingIRBSigns
									.containsAll(requiredIRBSigns);

							signedByAllResearchAdmins = existingResearchAdminSigns
									.containsAll(requiredResearchAdminSigns);

							signedByAllResearchDirectors = existingResearchDirectorSigns
									.containsAll(requiredResearchDirectorSigns);

							requiredPISign.clear();
							existingPISign.clear();
							requiredCoPISigns.clear();
							existingCoPISigns.clear();
							requiredChairSigns.clear();
							existingChairSigns.clear();
							requiredBusinessManagerSigns.clear();
							existingBusinessManagerSigns.clear();
							requiredDeanSigns.clear();
							existingDeanSigns.clear();
							requiredIRBSigns.clear();
							existingIRBSigns.clear();
							requiredResearchAdminSigns.clear();
							existingResearchAdminSigns.clear();
							requiredResearchDirectorSigns.clear();
							existingResearchDirectorSigns.clear();

							contentProfile.append("<ak:signedByCurrentUser>");
							contentProfile.append(signedByCurrentUser);
							contentProfile.append("</ak:signedByCurrentUser>");

							signByAllUsersInfo.setSignedByPI(signedByPI);
							contentProfile.append("<ak:signedByPI>");
							contentProfile.append(signedByPI);
							contentProfile.append("</ak:signedByPI>");

							signByAllUsersInfo
									.setSignedByAllCoPIs(signedByAllCoPIs);
							contentProfile.append("<ak:signedByAllCoPIs>");
							contentProfile.append(signedByAllCoPIs);
							contentProfile.append("</ak:signedByAllCoPIs>");

							signByAllUsersInfo
									.setSignedByAllChairs(signedByAllChairs);
							contentProfile.append("<ak:signedByAllChairs>");
							contentProfile.append(signedByAllChairs);
							contentProfile.append("</ak:signedByAllChairs>");

							signByAllUsersInfo
									.setSignedByAllBusinessManagers(signedByAllBusinessManagers);
							contentProfile
									.append("<ak:signedByAllBusinessManagers>");
							contentProfile.append(signedByAllBusinessManagers);
							contentProfile
									.append("</ak:signedByAllBusinessManagers>");

							signByAllUsersInfo
									.setSignedByAllDeans(signedByAllDeans);
							contentProfile.append("<ak:signedByAllDeans>");
							contentProfile.append(signedByAllDeans);
							contentProfile.append("</ak:signedByAllDeans>");

							signByAllUsersInfo
									.setSignedByAllIRBs(signedByAllIRBs);
							contentProfile.append("<ak:signedByAllIRBs>");
							contentProfile.append(signedByAllIRBs);
							contentProfile.append("</ak:signedByAllIRBs>");

							signByAllUsersInfo
									.setSignedByAllResearchAdmins(signedByAllResearchAdmins);
							contentProfile
									.append("<ak:signedByAllResearchAdmins>");
							contentProfile.append(signedByAllResearchAdmins);
							contentProfile
									.append("</ak:signedByAllResearchAdmins>");

							signByAllUsersInfo
									.setSignedByAllResearchDirectors(signedByAllResearchDirectors);
							contentProfile
									.append("<ak:signedByAllResearchDirectors>");
							contentProfile.append(signedByAllResearchDirectors);
							contentProfile
									.append("</ak:signedByAllResearchDirectors>");

							contentProfile.append("</ak:proposal>");
							contentProfile.append("</ak:record>");
							contentProfile.append("</Content>");
						} else {
							contentProfile.append("<Content>");
							contentProfile
									.append("<ak:record xmlns:ak=\"http://akpower.org\">");
							contentProfile.append("<ak:proposal>");

							contentProfile.append("<ak:proposalid>");
							contentProfile.append(proposalID);
							contentProfile.append("</ak:proposalid>");

							contentProfile.append("<ak:proposaltitle>");
							contentProfile.append(existingProposal
									.getProjectInfo().getProjectTitle());
							contentProfile.append("</ak:proposaltitle>");

							contentProfile.append("<ak:irbApprovalRequired>");
							contentProfile.append(existingProposal
									.isIrbApprovalRequired());
							contentProfile.append("</ak:irbApprovalRequired>");

							contentProfile.append("<ak:authorprofile>");
							// contentProfile.append("<ak:firstname>");
							// contentProfile.append(authorProfile.getFirstName());
							// contentProfile.append("</ak:firstname>");
							// contentProfile.append("<ak:middlename>");
							// contentProfile
							// .append(authorProfile.getMiddleName());
							// contentProfile.append("</ak:middlename>");
							//
							// contentProfile.append("<ak:lastname>");
							// contentProfile.append(authorProfile.getLastName());
							// contentProfile.append("</ak:lastname>");

							contentProfile.append("<ak:fullname>");
							contentProfile.append(authorFullName);
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(authorProfile.getId()
									.toString());
							contentProfile.append("</ak:userid>");

							contentProfile.append("</ak:authorprofile>");

							contentProfile.append("<ak:pi>");
							contentProfile.append("<ak:fullname>");
							contentProfile.append(existingProposal
									.getInvestigatorInfo().getPi().getUserRef()
									.getFullName());
							contentProfile.append("</ak:fullname>");

							contentProfile.append("<ak:workemail>");
							contentProfile.append(existingProposal
									.getInvestigatorInfo().getPi().getUserRef()
									.getWorkEmails().get(0));
							contentProfile.append("</ak:workemail>");

							contentProfile.append("<ak:userid>");
							contentProfile.append(existingProposal
									.getInvestigatorInfo().getPi()
									.getUserProfileId());
							contentProfile.append("</ak:userid>");
							contentProfile.append("</ak:pi>");

							for (InvestigatorRefAndPosition copis : existingProposal
									.getInvestigatorInfo().getCo_pi()) {
								contentProfile.append("<ak:copi>");
								contentProfile.append("<ak:fullname>");
								contentProfile.append(copis.getUserRef()
										.getFullName());
								contentProfile.append("</ak:fullname>");

								contentProfile.append("<ak:workemail>");
								contentProfile.append(copis.getUserRef()
										.getWorkEmails().get(0));
								contentProfile.append("</ak:workemail>");

								contentProfile.append("<ak:userid>");
								contentProfile.append(copis.getUserProfileId());
								contentProfile.append("</ak:userid>");
								contentProfile.append("</ak:copi>");
							}

							for (InvestigatorRefAndPosition seniors : existingProposal
									.getInvestigatorInfo().getSeniorPersonnel()) {
								contentProfile.append("<ak:senior>");
								contentProfile.append("<ak:fullname>");
								contentProfile.append(seniors.getUserRef()
										.getFullName());
								contentProfile.append("</ak:fullname>");

								contentProfile.append("<ak:workemail>");
								contentProfile.append(seniors.getUserRef()
										.getWorkEmails().get(0));
								contentProfile.append("</ak:workemail>");

								contentProfile.append("<ak:userid>");
								contentProfile.append(seniors
										.getUserProfileId());
								contentProfile.append("</ak:userid>");
								contentProfile.append("</ak:senior>");
							}

							contentProfile.append("<ak:signedByCurrentUser>");
							contentProfile.append(signedByCurrentUser);
							contentProfile.append("</ak:signedByCurrentUser>");

							contentProfile.append("</ak:proposal>");
							contentProfile.append("</ak:record>");
							contentProfile.append("</Content>");

						}

						contentProfile
								.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
						contentProfile
								.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
						contentProfile.append("</Attribute>");

						Set<AbstractResult> set = ac
								.getXACMLdecisionWithObligations(attrMap,
										contentProfile);
						Iterator<AbstractResult> it = set.iterator();
						int intDecision = 3;
						while (it.hasNext()) {
							AbstractResult ar = it.next();
							intDecision = ar.getDecision();

							if (intDecision >= 4 && intDecision <= 6) {
								intDecision = 2;
							}
							System.out.println("Decision:" + intDecision
									+ " that is: "
									+ AbstractResult.DECISIONS[intDecision]);

							if (AbstractResult.DECISIONS[intDecision]
									.equals("Permit")) {
								List<ObligationResult> obligations = ar
										.getObligations();

								EmailUtil emailUtil = new EmailUtil();
								String emailSubject = new String();
								String emailBody = new String();
								String authorName = new String();
								String piEmail = new String();
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
												if (assignment
														.getAttributeId()
														.toString()
														.equalsIgnoreCase(
																"obligationType")) {
													obligationType = assignment
															.getContent();
													break;
												}
											}

											if (obligationType
													.equals("preobligation")) {
												preObligations.add(obligation);
												System.out
														.println(obligationType
																+ " is FOUND");
											} else if (obligationType
													.equals("postobligation")) {
												postObligations.add(obligation);
												System.out
														.println(obligationType
																+ " is FOUND");
											} else {
												ongoingObligations
														.add(obligation);
												System.out
														.println(obligationType
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
													// + assignment.getContent()
													// +
													// "\n");

													switch (assignment
															.getAttributeId()
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
												System.out
														.println(obligationType
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
													// + assignment.getContent()
													// +
													// "\n");

													switch (assignment
															.getAttributeId()
															.toString()) {
													// case "obligationType":
													// obligationType =
													// assignment.getContent();
													// break;
													case "authorName":
														authorName = assignment
																.getContent();
														break;
													case "emailSubject":
														emailSubject = assignment
																.getContent();
														break;
													case "emailBody":
														emailBody = assignment
																.getContent();
														break;
													case "piEmail":
														piEmail = assignment
																.getContent();
														break;
													case "copisEmail":
													case "seniorsEmail":
													case "chairsEmail":
													case "managersEmail":
													case "deansEmail":
													case "irbsEmail":
													case "administratorsEmail":
													case "directorsEmail":
														if (!assignment
																.getContent()
																.equals("")) {
															emaillist
																	.add(assignment
																			.getContent());
														}
														break;

													}
												}

												System.out
														.println(obligationType
																+ " is RUNNING");

											}
										}
										System.out
												.println("===========================================================");
										System.out
												.println("\n======================== Printing Advices ====================");
										List<Advice> advices = ar.getAdvices();
										for (Advice advice : advices) {
											if (advice instanceof org.wso2.balana.xacml3.Advice) {
												List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Advice) advice)
														.getAssignments();
												for (AttributeAssignment assignment : assignments) {
													System.out
															.println("Advice :  "
																	+ assignment
																			.getContent()
																	+ "\n");
												}
											}
										}

									} else {
										return Response
												.status(403)
												.type(MediaType.APPLICATION_JSON)
												.entity(alertMessage).build();
									}
								}

								boolean proposalIsChanged = false;
								try {
									if (proposalID.equals("0")) {
										proposalIsChanged = saveProposal(
												message, existingProposal,
												null, authorProfile,
												proposalID, null,
												irbApprovalRequired, null);

									} else {
										proposalIsChanged = saveProposal(
												message, existingProposal,
												oldProposal, authorProfile,
												proposalID, signatures,
												irbApprovalRequired,
												signByAllUsersInfo);
									}
								} catch (Exception e) {
									return Response.status(403)
											.type(MediaType.APPLICATION_JSON)
											.entity(e.getMessage()).build();
								}

								if (proposalIsChanged) {
									if (!emailSubject.equals("")) {
										emailUtil
												.sendMailMultipleUsersWithoutAuth(
														piEmail, emaillist,
														emailSubject
																+ authorName,
														emailBody);
									}

									return Response
											.status(200)
											.type(MediaType.APPLICATION_JSON)
											.entity(mapper
													.writerWithDefaultPrettyPrinter()
													.writeValueAsString(true))
											.build();
									// return
									// Response.status(200).entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(true)).build();
								} else {
									return Response
											.status(200)
											.type(MediaType.APPLICATION_JSON)
											.entity(mapper
													.writerWithDefaultPrettyPrinter()
													.writeValueAsString(true))
											.build();
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
					}
					// else {
					// if (proposalID.equals("0")) {
					// saveProposal(message, existingProposal, null,
					// authorProfile, proposalID, null,
					// irbApprovalRequired);
					// return Response.status(200)
					// .type(MediaType.APPLICATION_JSON).entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(true))
					// .build();
					// // return
					// Response.status(200).entity(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(true)).build();
					// }
					// }
				}
			} else {
				return Response.status(403).type(MediaType.APPLICATION_JSON)
						.entity("No Proposal Info is send!").build();
			}
			return Response.status(403).type(MediaType.APPLICATION_JSON)
					.entity("No User Permission Attributes are send!").build();
		} catch (Exception e) {
			log.error(
					"Could not save a New Proposal or update an existing Proposal error e=",
					e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"Could Not Save A New Proposal OR Update AN Existing Proposal\", \"status\": \"FAIL\"}")
				.build();
	}

	private boolean saveProposal(String message, Proposal existingProposal,
			Proposal oldProposal, UserProfile authorProfile, String proposalID,
			List<SignatureUserInfo> signatures, boolean irbApprovalRequired,
			SignatureByAllUsers signByAllUsersInfo)
			throws UnknownHostException, Exception, ParseException,
			IOException, JsonParseException, JsonMappingException {

		String authorUserName = authorProfile.getFullName();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(message);

		JsonNode proposalInfo = null;

		boolean proposalIsChanged = false;

		if (root != null && root.has("proposalInfo")) {
			proposalInfo = root.get("proposalInfo");

			ProjectInfo newProjectInfo = new ProjectInfo();

			if (proposalInfo != null && proposalInfo.has("ProjectInfo")) {
				JsonNode projectInfo = proposalInfo.get("ProjectInfo");
				if (projectInfo != null && projectInfo.has("ProjectTitle")) {
					final String proposalTitle = projectInfo
							.get("ProjectTitle").textValue().trim()
							.replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(proposalTitle)) {
						if (!proposalID.equals("0")) {
							if (!existingProposal.getProjectInfo()
									.getProjectTitle().equals(proposalTitle)) {
								existingProposal.getProjectInfo()
										.setProjectTitle(proposalTitle);
							}
						} else {
							newProjectInfo.setProjectTitle(proposalTitle);
						}
					} else {
						throw new Exception(
								"The Proposal Title can not be Empty");
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
							.textValue().trim().replaceAll("\\<[^>]*>", ""));
					if (validateNotEmptyValue(dueDate.toString())) {
						if (!proposalID.equals("0")) {
							if (!existingProposal.getProjectInfo().getDueDate()
									.equals(dueDate)) {
								existingProposal.getProjectInfo().setDueDate(
										dueDate);
							}
						} else {
							newProjectInfo.setDueDate(dueDate);
						}
					} else {
						throw new Exception("The Due Date can not be Empty");
					}
				}

				ProjectPeriod projectPeriod = new ProjectPeriod();

				if (projectInfo != null && projectInfo.has("ProjectPeriodFrom")) {
					Date periodFrom = formatter.parse(projectInfo
							.get("ProjectPeriodFrom").textValue().trim()
							.replaceAll("\\<[^>]*>", ""));
					if (validateNotEmptyValue(periodFrom.toString())) {
						projectPeriod.setFrom(periodFrom);
					} else {
						throw new Exception(
								"The Project Period From can not be Empty");
					}
				}

				if (projectInfo != null && projectInfo.has("ProjectPeriodTo")) {
					Date periodTo = formatter.parse(projectInfo
							.get("ProjectPeriodTo").textValue().trim()
							.replaceAll("\\<[^>]*>", ""));
					if (validateNotEmptyValue(periodTo.toString())) {
						projectPeriod.setTo(periodTo);
					} else {
						throw new Exception(
								"The Project Period To can not be Empty");
					}
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
				existingProposal.setProjectInfo(newProjectInfo);
			}

			SponsorAndBudgetInfo newSponsorAndBudgetInfo = new SponsorAndBudgetInfo();
			if (proposalInfo != null
					&& proposalInfo.has("SponsorAndBudgetInfo")) {
				JsonNode sponsorAndBudgetInfo = proposalInfo
						.get("SponsorAndBudgetInfo");
				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("GrantingAgency")) {
					for (String grantingAgency : sponsorAndBudgetInfo
							.get("GrantingAgency").textValue().trim()
							.replaceAll("\\<[^>]*>", "").split(", ")) {
						if (validateNotEmptyValue(grantingAgency)) {
							newSponsorAndBudgetInfo.getGrantingAgency().add(
									grantingAgency);
						} else {
							throw new Exception(
									"The Granting Agency can not be Empty");
						}
					}
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("DirectCosts")) {
					final String directCost = sponsorAndBudgetInfo
							.get("DirectCosts").textValue().trim()
							.replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(directCost)) {
						newSponsorAndBudgetInfo.setDirectCosts(Double
								.parseDouble(directCost));
					} else {
						throw new Exception("The Direct Costs can not be Empty");
					}
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("FACosts")) {
					final String FACosts = sponsorAndBudgetInfo.get("FACosts")
							.textValue().trim().replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(FACosts)) {
						newSponsorAndBudgetInfo.setFaCosts(Double
								.parseDouble(FACosts));
					} else {
						throw new Exception("The FA Costs can not be Empty");
					}
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("TotalCosts")) {
					final String totalCosts = sponsorAndBudgetInfo
							.get("TotalCosts").textValue().trim()
							.replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(totalCosts)) {
						newSponsorAndBudgetInfo.setTotalCosts(Double
								.parseDouble(totalCosts));
					} else {
						throw new Exception("The Total Costs can not be Empty");
					}
				}

				if (sponsorAndBudgetInfo != null
						&& sponsorAndBudgetInfo.has("FARate")) {
					final String FARate = sponsorAndBudgetInfo.get("FARate")
							.textValue().trim().replaceAll("\\<[^>]*>", "");
					if (validateNotEmptyValue(FARate)) {
						newSponsorAndBudgetInfo.setFaRate(Double
								.parseDouble(FARate));
					} else {
						throw new Exception("The FA Rate can not be Empty");
					}
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
				existingProposal
						.setSponsorAndBudgetInfo(newSponsorAndBudgetInfo);
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
				existingProposal.setCostShareInfo(newCostShareInfo);
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
				existingProposal.setUniversityCommitments(newUnivCommitments);
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
				existingProposal.setConflicOfInterest(newConflictOfInterest);
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
				existingProposal.setAdditionalInfo(newAdditionalInfo);
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
							final String collabarationName = collaborationInfo
									.get("Collaborators").textValue().trim()
									.replaceAll("\\<[^>]*>", "");
							if (validateNotEmptyValue(collabarationName)) {
								newCollaborationInfo
										.setInvolvedCollaborators(collabarationName);
							} else {
								throw new Exception(
										"Collaborators can not be Empty");
							}
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
				existingProposal.setCollaborationInfo(newCollaborationInfo);
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
							final String onPages = confidentialInfo
									.get("OnPages").textValue().trim()
									.replaceAll("\\<[^>]*>", "");
							if (validateNotEmptyValue(onPages)) {
								newConfidentialInfo.setOnPages(onPages);
							} else {
								throw new Exception(
										"The Pages can not be Empty");
							}
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
				existingProposal.setConfidentialInfo(newConfidentialInfo);
			}

			// For Proposal User Title : for Dean, Chair and Manager
			JsonNode proposalUserTitle = root.get("proposalUserTitle");

			if ((proposalUserTitle.textValue().equals(
					"University Research Administrator") || proposalUserTitle
					.textValue().equals("University Research Director"))
					&& !proposalID.equals("0")) {
				// OSP Section Info Only for University Research Administrator
				// or University Research Director
				OSPSectionInfo newOSPSectionInfo = new OSPSectionInfo();
				if (proposalInfo != null && proposalInfo.has("OSPSectionInfo")) {
					JsonNode oSPSectionInfo = proposalInfo
							.get("OSPSectionInfo");

					// List Agency
					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("ListAgency")) {
						String agencies = oSPSectionInfo.get("ListAgency")
								.textValue().trim().replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(agencies)) {
							if (!existingProposal.getOspSectionInfo()
									.getListAgency().equals(agencies)) {
								existingProposal.getOspSectionInfo()
										.setListAgency(agencies);
							}
						} else {
							throw new Exception(
									"The Agency List can not be Empty");
						}
					}

					FundingSource newFundingSource = new FundingSource();
					if (oSPSectionInfo != null && oSPSectionInfo.has("Federal")) {
						newFundingSource.setFederal(oSPSectionInfo.get(
								"Federal").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("FederalFlowThrough")) {
						newFundingSource.setFederalFlowThrough(oSPSectionInfo
								.get("FederalFlowThrough").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("StateOfIdahoEntity")) {
						newFundingSource.setStateOfIdahoEntity(oSPSectionInfo
								.get("StateOfIdahoEntity").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("PrivateForProfit")) {
						newFundingSource.setPrivateForProfit(oSPSectionInfo
								.get("PrivateForProfit").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("NonProfitOrganization")) {
						newFundingSource
								.setNonProfitOrganization(oSPSectionInfo.get(
										"NonProfitOrganization").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("NonIdahoStateEntity")) {
						newFundingSource.setNonIdahoStateEntity(oSPSectionInfo
								.get("NonIdahoStateEntity").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("CollegeOrUniversity")) {
						newFundingSource.setCollegeOrUniversity(oSPSectionInfo
								.get("CollegeOrUniversity").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("LocalEntity")) {
						newFundingSource.setLocalEntity(oSPSectionInfo.get(
								"LocalEntity").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("NonIdahoLocalEntity")) {
						newFundingSource.setNonIdahoLocalEntity(oSPSectionInfo
								.get("NonIdahoLocalEntity").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("TirbalGovernment")) {
						newFundingSource.setTirbalGovernment(oSPSectionInfo
								.get("TirbalGovernment").booleanValue());
					}

					if (oSPSectionInfo != null && oSPSectionInfo.has("Foreign")) {
						newFundingSource.setForeign(oSPSectionInfo.get(
								"Foreign").booleanValue());
					}

					// Funding Source
					if (!existingProposal.getOspSectionInfo()
							.getFundingSource().equals(newFundingSource)) {
						existingProposal.getOspSectionInfo().setFundingSource(
								newFundingSource);
					}

					// CFDA No
					if (oSPSectionInfo != null && oSPSectionInfo.has("CFDANo")) {
						String CFDANo = oSPSectionInfo.get("CFDANo")
								.textValue().trim().replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(CFDANo)) {
							if (!existingProposal.getOspSectionInfo()
									.getCfdaNo().equals(CFDANo)) {
								existingProposal.getOspSectionInfo().setCfdaNo(
										CFDANo);
							}
						} else {
							throw new Exception("The CFDA No can not be Empty");
						}
					}

					// Program No
					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("ProgramNo")) {
						String programNo = oSPSectionInfo.get("ProgramNo")
								.textValue().trim().replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(programNo)) {
							if (!existingProposal.getOspSectionInfo()
									.getProgramNo().equals(programNo)) {
								existingProposal.getOspSectionInfo()
										.setProgramNo(programNo);
							}
						} else {
							throw new Exception(
									"The Program No can not be Empty");
						}
					}

					// Program Title
					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("ProgramTitle")) {
						String programTitle = oSPSectionInfo
								.get("ProgramTitle").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(programTitle)) {
							if (!existingProposal.getOspSectionInfo()
									.getProgramTitle().equals(programTitle)) {
								existingProposal.getOspSectionInfo()
										.setProgramTitle(programTitle);
							}
						} else {
							throw new Exception(
									"The Program Title can not be Empty");
						}
					}

					Recovery newRecovery = new Recovery();
					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("FullRecovery")) {
						newRecovery.setFullRecovery(oSPSectionInfo.get(
								"FullRecovery").booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo
									.has("NoRecoveryNormalSponsorPolicy")) {
						newRecovery
								.setNoRecoveryNormalSponsorPolicy(oSPSectionInfo
										.get("NoRecoveryNormalSponsorPolicy")
										.booleanValue());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo
									.has("NoRecoveryInstitutionalWaiver")) {
						newRecovery
								.setNoRecoveryInstitutionalWaiver(oSPSectionInfo
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
					if (!existingProposal.getOspSectionInfo().getRecovery()
							.equals(newRecovery)) {
						existingProposal.getOspSectionInfo().setRecovery(
								newRecovery);
					}

					BaseInfo newBaseInfo = new BaseInfo();
					if (oSPSectionInfo != null && oSPSectionInfo.has("MTDC")) {
						newBaseInfo.setMtdc(oSPSectionInfo.get("MTDC")
								.booleanValue());
					}

					if (oSPSectionInfo != null && oSPSectionInfo.has("TDC")) {
						newBaseInfo.setTdc(oSPSectionInfo.get("TDC")
								.booleanValue());
					}

					if (oSPSectionInfo != null && oSPSectionInfo.has("TC")) {
						newBaseInfo.setTc(oSPSectionInfo.get("TC")
								.booleanValue());
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
					if (!existingProposal.getOspSectionInfo().getBaseInfo()
							.equals(newBaseInfo)) {
						existingProposal.getOspSectionInfo().setBaseInfo(
								newBaseInfo);
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
					if (existingProposal.getOspSectionInfo()
							.isPiSalaryIncluded() != newOSPSectionInfo
							.isPiSalaryIncluded()) {
						existingProposal.getOspSectionInfo()
								.setPiSalaryIncluded(
										newOSPSectionInfo.isPiSalaryIncluded());
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("PISalary")) {
						// PI Salary
						String PISalary = oSPSectionInfo.get("PISalary")
								.textValue().trim().replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(PISalary)) {
							if (existingProposal.getOspSectionInfo()
									.getPiSalary() != Double
									.parseDouble(PISalary)) {
								existingProposal.getOspSectionInfo()
										.setPiSalary(
												Double.parseDouble(PISalary));
							}
						} else {
							throw new Exception(
									"The PI Salary can not be Empty");
						}
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("PIFringe")) {
						// PI Fringe
						String PiFringe = oSPSectionInfo.get("PIFringe")
								.textValue().trim().replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(PiFringe)) {
							if (existingProposal.getOspSectionInfo()
									.getPiFringe() != Double
									.parseDouble(PiFringe)) {
								existingProposal.getOspSectionInfo()
										.setPiFringe(
												Double.parseDouble(PiFringe));
							}
						} else {
							throw new Exception(
									"The PI Fringe can not be Empty");
						}
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("DepartmentId")) {
						// Department Id
						String departmentId = oSPSectionInfo
								.get("DepartmentId").textValue().trim()
								.replaceAll("\\<[^>]*>", "");
						if (validateNotEmptyValue(departmentId)) {
							if (!existingProposal.getOspSectionInfo()
									.getDepartmentId().equals(departmentId)) {
								existingProposal.getOspSectionInfo()
										.setDepartmentId(departmentId);
							}
						} else {
							throw new Exception(
									"The Department Id can not be Empty");
						}
					}

					BaseOptions newBaseOptions = new BaseOptions();

					if (oSPSectionInfo != null
							&& oSPSectionInfo
									.has("InstitutionalCostDocumented")) {
						switch (oSPSectionInfo.get(
								"InstitutionalCostDocumented").textValue()) {
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
					if (!existingProposal.getOspSectionInfo()
							.getInstitutionalCostDocumented()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setInstitutionalCostDocumented(newBaseOptions);
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
					if (!existingProposal.getOspSectionInfo()
							.getThirdPartyCostDocumented()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setThirdPartyCostDocumented(newBaseOptions);
					}

					if (oSPSectionInfo != null
							&& oSPSectionInfo.has("IsAnticipatedSubRecipients")) {
						switch (oSPSectionInfo
								.get("IsAnticipatedSubRecipients").textValue()) {
						case "1":
							newOSPSectionInfo.setAnticipatedSubRecipients(true);
							if (oSPSectionInfo != null
									&& oSPSectionInfo
											.has("AnticipatedSubRecipientsNames")) {
								final String anticipatedSubRecipients = oSPSectionInfo
										.get("AnticipatedSubRecipientsNames")
										.textValue().trim()
										.replaceAll("\\<[^>]*>", "");
								if (validateNotEmptyValue(anticipatedSubRecipients)) {
									newOSPSectionInfo
											.setAnticipatedSubRecipientsNames(anticipatedSubRecipients);
								} else {
									throw new Exception(
											"The Anticipated SubRecipients Names can not be Empty");
								}
							}
							break;
						case "2":
							newOSPSectionInfo
									.setAnticipatedSubRecipients(false);
							break;
						default:
							break;
						}
					}

					// Is Anticipated SubRecipients
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
							newBasePIEligibilityOptions
									.setThisProposalOnly(true);
							break;
						case "5":
							newBasePIEligibilityOptions.setBlanket(true);
							break;
						default:
							break;
						}
					}

					// Base PI Eligibility Options
					if (!existingProposal.getOspSectionInfo()
							.getPiEligibilityWaiver()
							.equals(newBasePIEligibilityOptions)) {
						existingProposal.getOspSectionInfo()
								.setPiEligibilityWaiver(
										newBasePIEligibilityOptions);
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
					if (!existingProposal.getOspSectionInfo()
							.getConflictOfInterestForms()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setConflictOfInterestForms(newBaseOptions);
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
					if (!existingProposal.getOspSectionInfo()
							.getExcludedPartyListChecked()
							.equals(newBaseOptions)) {
						existingProposal.getOspSectionInfo()
								.setExcludedPartyListChecked(newBaseOptions);
					}
				}
			} else {
				if (!proposalID.equals("0")) {
					existingProposal.setOspSectionInfo(oldProposal
							.getOspSectionInfo());
				}
			}

			String notificationMessage = new String();
			boolean isCritical = false;
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
					NotificationLog notification = new NotificationLog();

					switch (buttonType.textValue()) {
					case "Save":
						// Change status to ready to submitted by PI
						if (proposalID.equals("0")) {
							notificationMessage = "Saved by " + authorUserName
									+ ".";
							if (existingProposal.getInvestigatorInfo()
									.getCo_pi().size() == 0) {
								existingProposal
										.setReadyForSubmissionByPI(true);

								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.READYFORSUBMITBYPI);
							}
						} else if (!proposalID.equals("0")
								&& currentProposalRoles != null) {
							if ((currentProposalRoles.contains("PI") || (currentProposalRoles
									.contains("Co-PI") && !existingProposal
									.isReadyForSubmissionByPI()))
									&& existingProposal.getSubmittedByPI() == SubmitType.NOTSUBMITTED) {

								if (signByAllUsersInfo.isSignedByPI()
										&& signByAllUsersInfo
												.isSignedByAllCoPIs()) {
									existingProposal
											.setReadyForSubmissionByPI(true);

									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.READYFORSUBMITBYPI);
								} else {
									existingProposal
											.setReadyForSubmissionByPI(false);

									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.NOTSUBMITTEDBYPI);
								}

								notificationMessage = "Updated by "
										+ authorUserName + ".";
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
											.isReadyForSubmissionByPI()
									&& existingProposal.getDeletedByPI() == DeleteType.NOTDELETED
									&& currentProposalRoles.contains("PI")
									&& !proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {

								if (signByAllUsersInfo.isSignedByPI()
										&& signByAllUsersInfo
												.isSignedByAllCoPIs()) {
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

									for (SignatureUserInfo chair : signatures) {
										if (chair.getPositionTitle().equals(
												"Department Chair")) {
											notification = new NotificationLog();
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Ready for Approval");
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification.setUserProfileId(chair
													.getUserProfileId());
											notification.setUsername(chair
													.getUserName());
											notification.setCollege(chair
													.getCollege());
											notification.setDepartment(chair
													.getDepartment());
											notification.setPositionType(chair
													.getPositionType());
											notification.setPositionTitle(chair
													.getPositionTitle());

											notificationDAO.save(notification);
										}
									}

								} else {
									existingProposal
											.setReadyForSubmissionByPI(false);

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

								for (SignatureUserInfo allUsers : signatures) {
									notification = new NotificationLog();
									notification.setCritical(true);
									notification.setType("Proposal");
									notification
											.setAction("The Proposal is Submitted");
									notification.setProposalId(proposalID);
									notification
											.setProposalTitle(existingProposal
													.getProjectInfo()
													.getProjectTitle());
									notification.setUserProfileId(allUsers
											.getUserProfileId());
									notification.setUsername(allUsers
											.getUserName());
									notification.setCollege(allUsers
											.getCollege());
									notification.setDepartment(allUsers
											.getDepartment());
									notification.setPositionType(allUsers
											.getPositionType());
									notification.setPositionTitle(allUsers
											.getPositionTitle());

									notificationDAO.save(notification);
								}

							} else {
								// This user is both PI and Research
								// Administrator
							}
						}

						notificationMessage = "Submitted" + " by "
								+ authorUserName + ".";

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

							notificationMessage = "Approved" + " by "
									+ authorUserName + ".";

							if (existingProposal.getChairApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Department Chair") || proposalUserTitle
											.textValue().equals(
													"Associate Chair"))) {
								if (signByAllUsersInfo.isSignedByAllChairs()) {
									// Ready for Review by Business
									// Manager
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

									for (SignatureUserInfo businessManager : signatures) {
										if (businessManager.getPositionTitle()
												.equals("Business Manager")) {
											notification = new NotificationLog();
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Ready for Reviewal");
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(businessManager
															.getUserProfileId());
											notification
													.setUsername(businessManager
															.getUserName());
											notification
													.setCollege(businessManager
															.getCollege());
											notification
													.setDepartment(businessManager
															.getDepartment());
											notification
													.setPositionType(businessManager
															.getPositionType());
											notification
													.setPositionTitle(businessManager
															.getPositionTitle());

											notificationDAO.save(notification);
										}
									}

									if (irbApprovalRequired) {
										existingProposal
												.setIrbApproval(ApprovalType.READYFORAPPROVAL);

										// Proposal Status
										existingProposal
												.getProposalStatus()
												.add(Status.READYFORREVIEWBYIRB);

										for (SignatureUserInfo irb : signatures) {
											if (irb.getPositionTitle().equals(
													"IRB")) {
												notification = new NotificationLog();
												notification.setCritical(true);
												notification
														.setType("Proposal");
												notification
														.setAction("Ready for Reviewal");
												notification
														.setProposalId(proposalID);
												notification
														.setProposalTitle(existingProposal
																.getProjectInfo()
																.getProjectTitle());
												notification
														.setUserProfileId(irb
																.getUserProfileId());
												notification.setUsername(irb
														.getUserName());
												notification.setCollege(irb
														.getCollege());
												notification.setDepartment(irb
														.getDepartment());
												notification
														.setPositionType(irb
																.getPositionType());
												notification
														.setPositionTitle(irb
																.getPositionTitle());

												notificationDAO
														.save(notification);
											}
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify.getPositionTitle().equals(
											"Department Chair")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else if (existingProposal
									.getBusinessManagerApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Business Manager") || proposalUserTitle
											.textValue()
											.equals("Department Administrative Assistant"))) {
								if (signByAllUsersInfo
										.isSignedByAllBusinessManagers()) {
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

									for (SignatureUserInfo dean : signatures) {
										if (dean.getPositionTitle().equals(
												"Dean")) {
											notification = new NotificationLog();
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Ready for Approval");
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification.setUserProfileId(dean
													.getUserProfileId());
											notification.setUsername(dean
													.getUserName());
											notification.setCollege(dean
													.getCollege());
											notification.setDepartment(dean
													.getDepartment());
											notification.setPositionType(dean
													.getPositionType());
											notification.setPositionTitle(dean
													.getPositionTitle());

											notificationDAO.save(notification);
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify.getPositionTitle().equals(
											"Business Manager")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else if (existingProposal.getDeanApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Dean") || proposalUserTitle
											.textValue().equals(
													"Associate Dean"))) {
								if (signByAllUsersInfo.isSignedByAllDeans()) {
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

										for (SignatureUserInfo researchadmin : signatures) {
											if (researchadmin
													.getPositionTitle()
													.equals("University Research Administrator")) {
												notification = new NotificationLog();
												notification.setCritical(true);
												notification
														.setType("Proposal");
												notification
														.setAction("Ready for Approval");
												notification
														.setProposalId(proposalID);
												notification
														.setProposalTitle(existingProposal
																.getProjectInfo()
																.getProjectTitle());
												notification
														.setUserProfileId(researchadmin
																.getUserProfileId());
												notification
														.setUsername(researchadmin
																.getUserName());
												notification
														.setCollege(researchadmin
																.getCollege());
												notification
														.setDepartment(researchadmin
																.getDepartment());
												notification
														.setPositionType(researchadmin
																.getPositionType());
												notification
														.setPositionTitle(researchadmin
																.getPositionTitle());

												notificationDAO
														.save(notification);
											}
										}
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

											for (SignatureUserInfo researchadmin : signatures) {
												if (researchadmin
														.getPositionTitle()
														.equals("University Research Administrator")) {
													notification = new NotificationLog();
													notification
															.setCritical(true);
													notification
															.setType("Proposal");
													notification
															.setAction("Ready for Approval");
													notification
															.setProposalId(proposalID);
													notification
															.setProposalTitle(existingProposal
																	.getProjectInfo()
																	.getProjectTitle());
													notification
															.setUserProfileId(researchadmin
																	.getUserProfileId());
													notification
															.setUsername(researchadmin
																	.getUserName());
													notification
															.setCollege(researchadmin
																	.getCollege());
													notification
															.setDepartment(researchadmin
																	.getDepartment());
													notification
															.setPositionType(researchadmin
																	.getPositionType());
													notification
															.setPositionTitle(researchadmin
																	.getPositionTitle());

													notificationDAO
															.save(notification);
												}
											}
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify.getPositionTitle().equals(
											"Dean")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else if (existingProposal.getIrbApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"IRB") && irbApprovalRequired) {
								if (signByAllUsersInfo.isSignedByAllIRBs()) {
									// Approved by IRB
									existingProposal
											.setIrbApproval(ApprovalType.APPROVED);

									// Proposal Status
									existingProposal.getProposalStatus()
											.remove(Status.READYFORREVIEWBYIRB);
									existingProposal.getProposalStatus().add(
											Status.REVIEWEDBYIRB);

									if (existingProposal.getDeanApproval() == ApprovalType.APPROVED
											&& existingProposal
													.getBusinessManagerApproval() == ApprovalType.APPROVED) {
										existingProposal
												.setResearchAdministratorApproval(ApprovalType.READYFORAPPROVAL);

										// Proposal Status
										existingProposal.getProposalStatus()
												.clear();
										existingProposal
												.getProposalStatus()
												.add(Status.WAITINGFORRESEARCHADMINAPPROVAL);

										for (SignatureUserInfo researchadmin : signatures) {
											if (researchadmin
													.getPositionTitle()
													.equals("University Research Administrator")) {
												notification = new NotificationLog();
												notification.setCritical(true);
												notification
														.setType("Proposal");
												notification
														.setAction("Ready for Approval");
												notification
														.setProposalId(proposalID);
												notification
														.setProposalTitle(existingProposal
																.getProjectInfo()
																.getProjectTitle());
												notification
														.setUserProfileId(researchadmin
																.getUserProfileId());
												notification
														.setUsername(researchadmin
																.getUserName());
												notification
														.setCollege(researchadmin
																.getCollege());
												notification
														.setDepartment(researchadmin
																.getDepartment());
												notification
														.setPositionType(researchadmin
																.getPositionType());
												notification
														.setPositionTitle(researchadmin
																.getPositionTitle());

												notificationDAO
														.save(notification);
											}
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify.getPositionTitle().equals(
											"IRB")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else if (existingProposal
									.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {
								if (signByAllUsersInfo
										.isSignedByAllResearchAdmins()) {
									// Submitted to Research
									// Director
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

									for (SignatureUserInfo researchdirector : signatures) {
										if (researchdirector
												.getPositionTitle()
												.equals("University Research Director")) {
											notification = new NotificationLog();
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Ready for Approval");
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(researchdirector
															.getUserProfileId());
											notification
													.setUsername(researchdirector
															.getUserName());
											notification
													.setCollege(researchdirector
															.getCollege());
											notification
													.setDepartment(researchdirector
															.getDepartment());
											notification
													.setPositionType(researchdirector
															.getPositionType());
											notification
													.setPositionTitle(researchdirector
															.getPositionTitle());

											notificationDAO.save(notification);
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify
											.getPositionTitle()
											.equals("University Research Administrator")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else if (existingProposal
									.getResearchDirectorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"University Research Director")) {
								if (signByAllUsersInfo
										.isSignedByAllResearchDirectors()) {
									// Ready for submission
									existingProposal
											.setResearchDirectorApproval(ApprovalType.APPROVED);

									// Proposal Status
									existingProposal.getProposalStatus()
											.clear();
									existingProposal.getProposalStatus().add(
											Status.READYFORSUBMISSION);

									for (SignatureUserInfo researchadmin : signatures) {
										if (researchadmin
												.getPositionTitle()
												.equals("University Research Administrator")) {
											notification = new NotificationLog();
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Ready for Submission");
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(researchadmin
															.getUserProfileId());
											notification
													.setUsername(researchadmin
															.getUserName());
											notification
													.setCollege(researchadmin
															.getCollege());
											notification
													.setDepartment(researchadmin
															.getDepartment());
											notification
													.setPositionType(researchadmin
															.getPositionType());
											notification
													.setPositionTitle(researchadmin
															.getPositionTitle());

											notificationDAO.save(notification);
										}
									}
								}

								for (SignatureUserInfo userToNotify : signatures) {
									if (userToNotify.getPositionTitle().equals(
											"University Research Director")) {
										notification = new NotificationLog();
										notification.setCritical(false);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());

										notificationDAO.save(notification);
									}
								}
							} else {
								// You are not allowed to APPROVE
								// the
								// Proposal
								return false;
							}
						}

						break;

					case "Disapprove":
						if (!proposalID.equals("0")
								&& currentProposalRoles != null) {

							notificationMessage = "Disapproved" + " by "
									+ authorUserName + ".";

							isCritical = true;

							int coPICount = existingProposal
									.getInvestigatorInfo().getCo_pi().size();

							// int seniorCount = existingProposal
							// .getInvestigatorInfo().getSeniorPersonnel()
							// .size();

							if (existingProposal.getChairApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Department Chair") || proposalUserTitle
											.textValue().equals(
													"Associate Chair"))) {
								// Returned by Chair
								existingProposal
										.setChairApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.RETURNEDBYCHAIR);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify.getPositionTitle().equals(
											"Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									}
								}
							} else if (existingProposal
									.getBusinessManagerApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Business Manager") || proposalUserTitle
											.textValue()
											.equals("Department Administrative Assistant"))) {
								// Disapproved by Business Manager
								existingProposal
										.setBusinessManagerApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);
								existingProposal
										.setIrbApproval(ApprovalType.NOTREADYFORAPPROVAL);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYBUSINESSMANAGER);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify.getPositionTitle().equals(
											"Business Manager")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("IRB")) {
										if (existingProposal.getIrbApproval() == ApprovalType.APPROVED) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction(notificationMessage);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									}

									notificationDAO.save(notification);
								}
							} else if (existingProposal.getDeanApproval() == ApprovalType.READYFORAPPROVAL
									&& (proposalUserTitle.textValue().equals(
											"Dean") || proposalUserTitle
											.textValue().equals(
													"Associate Dean"))) {
								// Returned by Dean
								existingProposal
										.setDeanApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.RETURNEDBYDEAN);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify.getPositionTitle().equals(
											"Dean")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Business Manager")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("IRB")) {
										if (existingProposal.getIrbApproval() == ApprovalType.APPROVED) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction(notificationMessage);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									}
									notificationDAO.save(notification);
								}

							} else if (existingProposal.getIrbApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"IRB")) {
								// Disapproved by IRB
								existingProposal
										.setIrbApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYIRB);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify.getPositionTitle().equals(
											"IRB")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Business Manager")) {
										if (existingProposal
												.getBusinessManagerApproval() == ApprovalType.APPROVED) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction(notificationMessage);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									} else if (userToNotify.getPositionTitle()
											.equals("Dean")) {
										if (existingProposal.getDeanApproval() == ApprovalType.APPROVED) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction(notificationMessage);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									}

									notificationDAO.save(notification);
								}
							} else if (existingProposal
									.getResearchAdministratorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle
											.textValue()
											.equals("University Research Administrator")) {
								// Disapproved by Research
								// Administrator
								existingProposal
										.setResearchAdministratorApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYRESEARCHADMIN);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify
											.getPositionTitle()
											.equals("University Research Administrator")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Dean")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Business Manager")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("IRB")) {
										if (irbApprovalRequired) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction(notificationMessage);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									}
									notificationDAO.save(notification);
								}

							} else if (existingProposal
									.getResearchDirectorApproval() == ApprovalType.READYFORAPPROVAL
									&& proposalUserTitle.textValue().equals(
											"University Research Director")) {
								// Disapproved by University
								// Research
								// Director
								existingProposal
										.setResearchDirectorApproval(ApprovalType.DISAPPROVED);

								existingProposal
										.setSubmittedByPI(SubmitType.NOTSUBMITTED);

								if (coPICount > 0) {
									existingProposal
											.setReadyForSubmissionByPI(false);
								}

								existingProposal.getSignatureInfo().clear();

								// Proposal Status
								existingProposal.getProposalStatus().clear();
								existingProposal.getProposalStatus().add(
										Status.DISAPPROVEDBYRESEARCHDIRECTOR);

								for (SignatureUserInfo userToNotify : signatures) {
									notification = new NotificationLog();
									if (userToNotify.getPositionTitle().equals(
											"University Research Director")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify
											.getPositionTitle()
											.equals("University Research Administrator")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction(notificationMessage);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Dean")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction("Disapproved by "
														+ authorUserName);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Business Manager")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction("Disapproved by "
														+ authorUserName);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("Department Chair")) {
										notification.setCritical(true);
										notification.setType("Proposal");
										notification
												.setAction("Disapproved by "
														+ authorUserName);
										notification.setProposalId(proposalID);
										notification
												.setProposalTitle(existingProposal
														.getProjectInfo()
														.getProjectTitle());
										notification
												.setUserProfileId(userToNotify
														.getUserProfileId());
										notification.setUsername(userToNotify
												.getUserName());
										notification.setCollege(userToNotify
												.getCollege());
										notification.setDepartment(userToNotify
												.getDepartment());
										notification
												.setPositionType(userToNotify
														.getPositionType());
										notification
												.setPositionTitle(userToNotify
														.getPositionTitle());
									} else if (userToNotify.getPositionTitle()
											.equals("IRB")) {
										if (irbApprovalRequired) {
											notification.setCritical(true);
											notification.setType("Proposal");
											notification
													.setAction("Disapproved by "
															+ authorUserName);
											notification
													.setProposalId(proposalID);
											notification
													.setProposalTitle(existingProposal
															.getProjectInfo()
															.getProjectTitle());
											notification
													.setUserProfileId(userToNotify
															.getUserProfileId());
											notification
													.setUsername(userToNotify
															.getUserName());
											notification
													.setCollege(userToNotify
															.getCollege());
											notification
													.setDepartment(userToNotify
															.getDepartment());
											notification
													.setPositionType(userToNotify
															.getPositionType());
											notification
													.setPositionTitle(userToNotify
															.getPositionTitle());
										}
									}
									notificationDAO.save(notification);
								}

							} else {
								// You are not allowed to DISAPPROVE
								// the
								// Proposal
							}
						}

						break;

					default:

						break;
					}
				}
			}

			if (!proposalID.equals("0")) {
				if (!existingProposal.equals(oldProposal)) {
					proposalDAO.updateProposal(existingProposal, authorProfile);
					proposalIsChanged = true;
				}
			} else {
				proposalDAO.saveProposal(existingProposal, authorProfile);
				proposalIsChanged = true;
			}

			if (proposalIsChanged) {
				NotifyAllExistingInvestigators(existingProposal.getId()
						.toString(), existingProposal.getProjectInfo()
						.getProjectTitle(), existingProposal,
						notificationMessage, "Proposal", isCritical);
			}
		}
		return proposalIsChanged;
	}

	// Only 5MB is allowed from client to upload
	private boolean verifyValidFileSize(long fileSize) {
		if (fileSize <= 5 * 1024 * 1024) {
			return true;
		} else {
			return false;
		}
	}

	// Allowed extensions: jpg,png,gif,jpeg,bmp,png,pdf,doc,docx,xls,xlsx,txt
	private boolean verifyValidFileExtension(String extension) {
		List<String> list = Arrays.asList("jpg", "png", "gif", "jpeg", "bmp",
				"png", "pdf", "doc", "docx", "xls", "xlsx", "txt");
		if (list.contains(extension)) {
			return true;
		} else {
			return false;
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
		notification.setForAdmin(true);
		notificationDAO.save(notification);

		InvestigatorRefAndPosition newPI = existingProposal
				.getInvestigatorInfo().getPi();
		if (newPI.getUserProfileId() != null) {
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

	private void broadCastNotification(String proposalID, String projectTitle,
			String notificationMessage, String notificationType,
			boolean isCritical, Boolean needPI, Boolean needCoPI,
			Boolean needSenior, Boolean needChair, Boolean needManager,
			Boolean needDean, Boolean needIrb, Boolean needResearchadmin,
			Boolean needDirector) {

		ObjectId id = new ObjectId(proposalID);

		List<SignatureUserInfo> signatures = proposalDAO
				.findUsersExceptInvestigatorForAproposal(id, needPI, needCoPI,
						needSenior, needChair, needManager, needDean, needIrb,
						needResearchadmin, needDirector);

		NotificationLog notification = new NotificationLog();
		if (isCritical) {
			notification.setCritical(true);
		}

		notification.setType(notificationType);
		notification.setAction(notificationMessage);
		notification.setProposalId(proposalID);
		notification.setProposalTitle(projectTitle);
		notification.setForAdmin(true);
		notificationDAO.save(notification);

		for (SignatureUserInfo notifyUsers : signatures) {
			notification = new NotificationLog();
			if (isCritical) {
				notification.setCritical(true);
			}
			notification.setType(notificationType);
			notification.setAction(notificationMessage);
			notification.setProposalId(proposalID);
			notification.setProposalTitle(projectTitle);
			notification.setUserProfileId(notifyUsers.getUserProfileId());
			notification.setUsername(notifyUsers.getUserName());
			notification.setCollege(notifyUsers.getCollege());
			notification.setDepartment(notifyUsers.getDepartment());
			notification.setPositionType(notifyUsers.getPositionType());
			notification.setPositionTitle(notifyUsers.getPositionTitle());

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
	@ApiOperation(value = "Check permission for a Proposal", notes = "This API checks permission for a Proposal")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Success: { True }"),
			@ApiResponse(code = 403, message = "Failed: { \"error\":\"error description\", \"status\": \"FAIL\" }") })
	public Response checkUserPermissionForAProposal(
			@ApiParam(value = "Message", required = true, defaultValue = "", allowableValues = "", allowMultiple = false) String message) {
		try {
			log.info("ProposalService::checkUserPermissionForAProposal started");

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
					// network.type

					// Device type
					// device.type

					String decision = ac.getXACMLdecision(attrMap);
					if (decision.equals("Permit")) {
						return Response
								.status(200)
								.type(MediaType.APPLICATION_JSON)
								.entity(mapper.writerWithDefaultPrettyPrinter()
										.writeValueAsString(true)).build();
					} else {
						return Response.status(403)
								.type(MediaType.APPLICATION_JSON)
								.entity("Your permission is: " + decision)
								.build();
					}
				} else {
					return Response.status(403)
							.type(MediaType.APPLICATION_JSON)
							.entity("No User Permission Attributes are send!")
							.build();
				}
			}
		} catch (Exception e) {
			log.error("Could not Check Permission for a Proposal error e=", e);
		}

		return Response
				.status(403)
				.entity("{\"error\": \"No User Permission Attributes are send!\", \"status\": \"FAIL\"}")
				.build();
	}

	/**
	 * This method will check the signatures for the proposal. It will first
	 * find all the supervisory personnel that SHOULD be signing the proposal
	 * (based on PI, COPI, Senior Personnel -their supervisory personnel-) Then
	 * it will find out if the appropriate number has signed ie: if between the
	 * Pi, CoPi, and SP, there are 4 department chairs, we need to know that 4
	 * department chairs have signed.
	 * 
	 * @param id
	 *            the ID of the proposal we need to query for
	 * @param posTitle
	 *            the position title we want to check
	 * @return true if all required signatures exist
	 * @throws UnknownHostException
	 */
	public boolean getSignedStatusForAProposal(ObjectId id, String posTitle)
			throws UnknownHostException {
		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		Proposal checkProposal = proposalDAO.findProposalByProposalID(id);

		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		List<InvestigatorRefAndPosition> investigatorList = new ArrayList<InvestigatorRefAndPosition>();

		// For now I'm going to handle this boolean here...
		boolean isAdmin = false;
		// The getSupervisory method we'll call wants a boolean "isAdmin" this
		// is just used to define
		// whether or not someone is in an administrative position.
		// For example: when we want a department chair, we need their college
		// and their department that
		// they are from, but if we want a dean, we just need their college,
		// because they're the dean
		// of the college, and not part of a department under that college
		// The boolean tells the getSuper method which search call it needs to
		// make, for now
		// this is done for simplicity
		if (posTitle.equals("Dean")) {
			isAdmin = true;
		}

		investigatorList.add(checkProposal.getInvestigatorInfo().getPi());

		if (!checkProposal.getInvestigatorInfo().getCo_pi().isEmpty()) {
			for (InvestigatorRefAndPosition coPi : checkProposal
					.getInvestigatorInfo().getCo_pi()) {
				investigatorList.add(coPi);
			}
		}
		// for (InvestigatorRefAndPosition senior : checkProposal
		// .getInvestigatorInfo().getSeniorPersonnel()) {
		// investigatorList.add(senior);
		// } //Apparently we do not need the supers for senior personnel

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

		// 2nd Find out all of their supervisory personnel
		for (InvestigatorRefAndPosition investigator : investigatorList) {
			List<UserProfile> tempList = userProfileDAO
					.getSupervisoryPersonnels(investigator.getCollege(),
							investigator.getDepartment(), posTitle, isAdmin);
			for (UserProfile profs : tempList) {
				if (!supervisorsList.contains(profs)) {
					supervisorsList.add(profs);
				}
			}
		}

		// 3rd Evaluate if these personnel have "signed" the proposal
		boolean isOnList = true;
		ArrayList<String> sigids = new ArrayList<String>();

		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		for (UserProfile superProfs : supervisorsList) {
			if (!sigids.contains(superProfs.getId().toString())) {
				isOnList = false;
			}
		}

		return isOnList;
	}

	/**
	 * Use this method to find out if all PI's have signed the proposal (should
	 * just be one pi, but will check all of them
	 * 
	 * @param id
	 *            the id of the proposal we want to check
	 * @param investigatorType
	 *            pi to check pi, copi to check copi
	 * @return true if all investigators have signed
	 * @throws UnknownHostException
	 */
	public boolean getPISignedStatusForProposal(ObjectId id)
			throws UnknownHostException {
		Proposal checkProposal = proposalDAO.findProposalByProposalID(id);
		ArrayList<String> sigids = new ArrayList<String>();
		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		if (sigids.contains(checkProposal.getInvestigatorInfo().getPi()
				.getUserProfileId())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method will verify that all CoPi's have signed the proposal
	 * 
	 * @param id
	 *            ID of the proposal to check
	 * @return true if all CoPI's have signed
	 * @throws UnknownHostException
	 */
	public boolean getCoPiSignedStatusForProposal(ObjectId id)
			throws UnknownHostException {

		boolean allCoPiSigned = true;

		Proposal checkProposal = proposalDAO.findProposalByProposalID(id);
		ArrayList<String> sigids = new ArrayList<String>();
		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		for (InvestigatorRefAndPosition profs : checkProposal
				.getInvestigatorInfo().getCo_pi()) {
			if (!sigids.contains(profs.getUserProfileId())) {
				allCoPiSigned = false;
			}
		}

		return allCoPiSigned;
	}

	private boolean validateNotEmptyValue(String value) {
		if (!value.equalsIgnoreCase("")) {
			return true;
		} else {
			return false;
		}
	}

}