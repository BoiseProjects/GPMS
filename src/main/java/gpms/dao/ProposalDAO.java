package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.ApprovalType;
import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.CollegeDepartmentInfo;
import gpms.model.DeleteType;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.PositionDetails;
import gpms.model.ProjectLocation;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.ProposalInfo;
import gpms.model.SignatureInfo;
import gpms.model.SignatureUserInfo;
import gpms.model.Status;
import gpms.model.SubmitType;
import gpms.model.TypeOfRequest;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class ProposalDAO extends BasicDAO<Proposal, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "proposal";

	private static Morphia morphia;
	private static Datastore ds;
	private AuditLog audit = new AuditLog();
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(Proposal.class);
		}
		return morphia;
	}

	@Override
	public Datastore getDatastore() {
		if (ds == null) {
			try {
				ds = getMorphia().createDatastore(MongoDBConnector.getMongo(),
						DBNAME);
			} catch (UnknownHostException | MongoException e) {
				e.printStackTrace();
			}
		}
		return ds;
	}

	public ProposalDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	/**
	 * Get all the proposals in the database Used mostly in testing
	 * 
	 * @return a list of all proposals in the database
	 */
	public List<Proposal> findAllProposals() {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).asList();
	}

	public Proposal findProposalByProposalID(ObjectId id)
			throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).field("_id").equal(id).get();
	}

	public Proposal findProposalByProposalIDExceptAuditLog(ObjectId proposalId) {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class)
				.retrievedFields(false, "_id", "audit log", "version")
				.field("_id").equal(proposalId).get();
	}

	public void saveProposal(Proposal newProposal, UserProfile authorProfile) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Created proposal by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		newProposal.getAuditLog().add(audit);
		ds.save(newProposal);
	}

	public void updateProposal(Proposal existingProposal,
			UserProfile authorProfile) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Updated proposal by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		existingProposal.getAuditLog().add(audit);
		ds.save(existingProposal);
	}

	public boolean updateProposalStatus(Proposal existingProposal,
			UserProfile authorProfile) {
		Datastore ds = getDatastore();

		boolean isStatusUpdated = false;

		audit = new AuditLog(authorProfile, "Updated proposal by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		existingProposal.getAuditLog().add(audit);
		ds.save(existingProposal);
		isStatusUpdated = true;
		return isStatusUpdated;
	}

	public boolean deleteProposal(Proposal proposal, String proposalRoles,
			String proposalUserTitle, UserProfile authorProfile) {
		Datastore ds = getDatastore();

		boolean isDeleted = false;

		if (proposal.getSubmittedByPI() == SubmitType.NOTSUBMITTED
				&& proposal.getDeletedByPI() == DeleteType.NOTDELETED
				&& proposalRoles.equals("PI")
				&& !proposalUserTitle.equals("University Research Director")) {

			proposal.setDeletedByPI(DeleteType.DELETED);

			// Proposal Status
			proposal.getProposalStatus().clear();
			proposal.getProposalStatus().add(Status.DELETEDBYPI);

			AuditLog entry = new AuditLog(authorProfile, "Deleted Proposal by "
					+ authorProfile.getUserAccount().getUserName(), new Date());
			proposal.getAuditLog().add(entry);
			ds.save(proposal);
			isDeleted = true;
		} else if (proposal.getResearchDirectorDeletion() == DeleteType.NOTDELETED
				&& proposal.getResearchDirectorApproval() == ApprovalType.READYFORAPPROVAL
				&& !proposalRoles.equals("PI")
				&& proposalUserTitle.equals("University Research Director")) {

			proposal.setResearchDirectorDeletion(DeleteType.DELETED);
			proposal.setResearchDirectorApproval(ApprovalType.NOTREADYFORAPPROVAL);

			// Proposal Status
			proposal.getProposalStatus().clear();
			proposal.getProposalStatus().add(Status.DELETEDBYRESEARCHDIRECTOR);

			AuditLog entry = new AuditLog(authorProfile, "Deleted Proposal by "
					+ authorProfile.getUserAccount().getUserName(), new Date());
			proposal.getAuditLog().add(entry);
			ds.save(proposal);
			isDeleted = true;
		} else {
			// This user is both PI and University
			// Research Director
		}
		return isDeleted;
	}

	public List<AuditLogInfo> findAllForProposalAuditLogGrid(int offset,
			int limit, ObjectId id, String action, String auditedBy,
			String activityOnFrom, String activityOnTo) throws ParseException {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		Proposal q = proposalQuery.field("_id").equal(id).get();

		List<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();
		int rowTotal = 0;
		if (q.getAuditLog() != null && q.getAuditLog().size() != 0) {
			for (AuditLog poposalAudit : q.getAuditLog()) {
				AuditLogInfo proposalAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (poposalAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (poposalAudit.getUserProfile().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getFirstName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getMiddleName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getLastName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (poposalAudit.getActivityDate()
							.compareTo(activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					proposalAuditLog.setUserName(poposalAudit.getUserProfile()
							.getUserAccount().getUserName());
					proposalAuditLog.setUserFullName(poposalAudit
							.getUserProfile().getFullName());
					proposalAuditLog.setAction(poposalAudit.getAction());
					proposalAuditLog.setActivityDate(poposalAudit
							.getActivityDate());

					allAuditLogs.add(proposalAuditLog);
				}
			}
		}

		Collections.sort(allAuditLogs);

		rowTotal = allAuditLogs.size();
		if (rowTotal > 0) {
			for (AuditLogInfo t : allAuditLogs) {
				t.setRowTotal(rowTotal);
			}
		}

		if (rowTotal >= (offset + limit - 1)) {
			return allAuditLogs.subList(offset - 1, offset + limit - 1);
		} else {
			return allAuditLogs.subList(offset - 1, rowTotal);
		}
	}

	public List<AuditLogInfo> findAllUserProposalAuditLogs(ObjectId id,
			String action, String auditedBy, String activityOnFrom,
			String activityOnTo) throws ParseException {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		Proposal q = proposalQuery.field("_id").equal(id).get();

		List<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();

		if (q.getAuditLog() != null && q.getAuditLog().size() != 0) {
			for (AuditLog poposalAudit : q.getAuditLog()) {
				AuditLogInfo proposalAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (poposalAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (poposalAudit.getUserProfile().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getFirstName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getMiddleName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfile().getLastName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (poposalAudit.getActivityDate()
							.compareTo(activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (poposalAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					proposalAuditLog.setUserName(poposalAudit.getUserProfile()
							.getUserAccount().getUserName());
					proposalAuditLog.setUserFullName(poposalAudit
							.getUserProfile().getFullName());
					proposalAuditLog.setAction(poposalAudit.getAction());
					proposalAuditLog.setActivityDate(poposalAudit
							.getActivityDate());

					allAuditLogs.add(proposalAuditLog);
				}
			}
		}

		Collections.sort(allAuditLogs);
		return allAuditLogs;
	}

	public Proposal findNextProposalWithSameProjectTitle(ObjectId id,
			String newProjectTitle) {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		// Pattern pattern = Pattern.compile("^" + newProjectTitle + "$",
		// Pattern.CASE_INSENSITIVE);

		// Updated with new version of Morphia
		proposalQuery.and(proposalQuery.criteria("_id").notEqual(id),
				proposalQuery.criteria("project info.project title")
						.equalIgnoreCase(newProjectTitle));
		return proposalQuery.get();
	}

	public Proposal findAnyProposalWithSameProjectTitle(String newProjectTitle) {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		// Pattern pattern = Pattern.compile("^" + newProjectTitle + "$",
		// Pattern.CASE_INSENSITIVE);
		// proposalQuery.criteria("project info.project title")
		// .containsIgnoreCase(pattern.pattern());

		// Updated with new version of Morphia
		proposalQuery.criteria("project info.project title").equalIgnoreCase(
				newProjectTitle);
		return proposalQuery.get();
	}

	public List<ProposalInfo> findAllForProposalGrid(int offset, int limit,
			String projectTitle, String usernameBy, String submittedOnFrom,
			String submittedOnTo, Double totalCostsFrom, Double totalCostsTo,
			String proposalStatus, String userRole) throws ParseException {
		Datastore ds = getDatastore();
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);
		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		if (projectTitle != null) {
			proposalQuery.field("project info.project title")
					.containsIgnoreCase(projectTitle);
		}

		if (submittedOnFrom != null && !submittedOnFrom.isEmpty()) {
			Date receivedOnF = formatter.parse(submittedOnFrom);
			proposalQuery.field("date submitted").greaterThanOrEq(receivedOnF);
		}
		if (submittedOnTo != null && !submittedOnTo.isEmpty()) {
			Date receivedOnT = formatter.parse(submittedOnTo);
			proposalQuery.field("date submitted").lessThanOrEq(receivedOnT);
		}

		if (totalCostsFrom != null && totalCostsFrom != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.greaterThanOrEq(totalCostsFrom);
		}
		if (totalCostsTo != null && totalCostsTo != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.lessThanOrEq(totalCostsTo);
		}

		if (proposalStatus != null) {
			proposalQuery.field("proposal status").contains(proposalStatus);
		}

		if (usernameBy != null) {
			// accountQuery.field("username").containsIgnoreCase(usernameBy);
			// profileQuery.criteria("user id").in(accountQuery.asKeyList());
			// proposalQuery.criteria("investigator info.pi.user profile").in(
			// profileQuery.asKeyList());
			profileQuery.or(
					profileQuery.criteria("first name").containsIgnoreCase(
							usernameBy),
					profileQuery.criteria("middle name").containsIgnoreCase(
							usernameBy), profileQuery.criteria("last name")
							.containsIgnoreCase(usernameBy));
			if (userRole != null) {
				switch (userRole) {
				case "PI":
					proposalQuery.criteria("investigator info.pi.user profile")
							.in(profileQuery.asKeyList());
					break;
				case "Co-PI":
					proposalQuery.criteria(
							"investigator info.co_pi.user profile").in(
							profileQuery.asKeyList());
					break;

				case "Senior Personnel":
					proposalQuery.criteria(
							"investigator info.senior personnel.user profile")
							.in(profileQuery.asKeyList());
					break;

				default:
					break;
				}
			} else {
				proposalQuery
						.or(proposalQuery.criteria(
								"investigator info.pi.user profile").in(
								profileQuery.asKeyList()),
								proposalQuery.criteria(
										"investigator info.co_pi.user profile")
										.in(profileQuery.asKeyList()),
								proposalQuery
										.criteria(
												"investigator info.senior personnel.user profile")
										.in(profileQuery.asKeyList()));
			}
		}

		int rowTotal = proposalQuery.asList().size();
		List<Proposal> allProposals = proposalQuery.offset(offset - 1)
				.limit(limit).order("-audit log.activity on").asList();

		for (Proposal userProposal : allProposals) {
			ProposalInfo proposal = new ProposalInfo();

			// Proposal
			proposal.setRowTotal(rowTotal);
			proposal.setId(userProposal.getId().toString());

			// ProjectInfo
			proposal.setProjectTitle(userProposal.getProjectInfo()
					.getProjectTitle());

			ProjectType pt = userProposal.getProjectInfo().getProjectType();
			if (pt.isResearchBasic()) {
				proposal.setProjectType("Research-basic");
			} else if (pt.isResearchApplied()) {
				proposal.setProjectType("Research-applied");
			} else if (pt.isResearchDevelopment()) {
				proposal.setProjectType("Research-development");
			} else if (pt.isInstruction()) {
				proposal.setProjectType("Instruction");
			} else if (pt.isOtherSponsoredActivity()) {
				proposal.setProjectType("Other sponsored activity");
			}

			TypeOfRequest tor = userProposal.getProjectInfo()
					.getTypeOfRequest();
			if (tor.isPreProposal()) {
				proposal.getTypeOfRequest().add("Pre-proposal");
			} else if (tor.isNewProposal()) {
				proposal.getTypeOfRequest().add("New proposal");
			} else if (tor.isContinuation()) {
				proposal.getTypeOfRequest().add("Continuation");
			} else if (tor.isSupplement()) {
				proposal.getTypeOfRequest().add("Supplement");
			}

			ProjectLocation pl = userProposal.getProjectInfo()
					.getProjectLocation();
			if (pl.isOffCampus()) {
				proposal.setProjectLocation("Off-campus");
			} else if (pl.isOnCampus()) {
				proposal.setProjectLocation("On-campus");
			}

			// SponsorAndBudgetInfo
			proposal.setGrantingAgencies(userProposal.getSponsorAndBudgetInfo()
					.getGrantingAgency());
			proposal.setDirectCosts(userProposal.getSponsorAndBudgetInfo()
					.getDirectCosts());
			proposal.setFaCosts(userProposal.getSponsorAndBudgetInfo()
					.getFaCosts());
			proposal.setTotalCosts(userProposal.getSponsorAndBudgetInfo()
					.getTotalCosts());
			proposal.setFaRate(userProposal.getSponsorAndBudgetInfo()
					.getFaRate());

			proposal.setDateCreated(userProposal.getDateCreated());
			proposal.setDateSubmitted(userProposal.getDateSubmitted());

			proposal.setDueDate(userProposal.getProjectInfo().getDueDate());
			proposal.setProjectPeriodFrom(userProposal.getProjectInfo()
					.getProjectPeriod().getFrom());
			proposal.setProjectPeriodTo(userProposal.getProjectInfo()
					.getProjectPeriod().getTo());

			// Proposal Status
			for (Status status : userProposal.getProposalStatus()) {
				proposal.getProposalStatus().add(status.toString());
			}

			// PI
			proposal.setSubmittedByPI(userProposal.getSubmittedByPI());
			proposal.setReadyForSubmissionByPI(userProposal
					.isReadyForSubmissionByPI());
			proposal.setDeletedByPI(userProposal.getDeletedByPI());

			// Chair
			proposal.setChairApproval(userProposal.getChairApproval());

			// Business Manager
			proposal.setBusinessManagerApproval(userProposal
					.getBusinessManagerApproval());

			// IRB
			proposal.setIrbApproval(userProposal.getIrbApproval());

			// Dean
			proposal.setDeanApproval(userProposal.getDeanApproval());

			// University Research Administrator
			proposal.setResearchAdministratorApproval(userProposal
					.getResearchAdministratorApproval());

			proposal.setResearchAdministratorWithdraw(userProposal
					.getResearchAdministratorWithdraw());

			proposal.setResearchAdministratorSubmission(userProposal
					.getResearchAdministratorSubmission());

			// University Research Director
			proposal.setResearchDirectorApproval(userProposal
					.getResearchDirectorApproval());

			proposal.setResearchDirectorDeletion(userProposal
					.getResearchDirectorDeletion());

			proposal.setResearchDirectorArchived(userProposal
					.getResearchDirectorArchived());

			proposal.setIrbApprovalRequired(userProposal
					.isIrbApprovalRequired());

			if (userProposal.getDeletedByPI().equals(DeleteType.DELETED)
					|| userProposal.getResearchDirectorDeletion().equals(
							DeleteType.DELETED)) {
				proposal.setDeleted(true);
			}

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();

			int auditLogCount = userProposal.getAuditLog().size();
			if (userProposal.getAuditLog() != null && auditLogCount != 0) {
				AuditLog auditLog = userProposal.getAuditLog().get(
						auditLogCount - 1);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserProfile().getFullName();
				lastAuditAction = auditLog.getAction();
			}

			proposal.setLastAudited(lastAudited);
			proposal.setLastAuditedBy(lastAuditedBy);
			proposal.setLastAuditAction(lastAuditAction);

			String piUserId = userProposal.getInvestigatorInfo().getPi()
					.getUserRef().getId().toString();
			proposal.setPiUser(piUserId);
			if (!proposal.getAllUsers().contains(piUserId)) {
				proposal.getAllUsers().add(piUserId);
			}

			List<InvestigatorRefAndPosition> allCoPI = userProposal
					.getInvestigatorInfo().getCo_pi();
			for (InvestigatorRefAndPosition coPI : allCoPI) {
				String coPIUser = coPI.getUserRef().getId().toString();
				proposal.getCopiUsers().add(coPIUser);
				if (!proposal.getAllUsers().contains(coPIUser)) {
					proposal.getAllUsers().add(coPIUser);
				}
			}

			List<InvestigatorRefAndPosition> allSeniors = userProposal
					.getInvestigatorInfo().getSeniorPersonnel();
			for (InvestigatorRefAndPosition senior : allSeniors) {
				String seniorUser = senior.getUserRef().getId().toString();
				proposal.getSeniorUsers().add(seniorUser);
				if (!proposal.getAllUsers().contains(seniorUser)) {
					proposal.getAllUsers().add(seniorUser);
				}
			}

			proposals.add(proposal);
		}
		// Collections.sort(proposals);
		return proposals;
	}

	public List<ProposalInfo> findUserProposalGrid(int offset, int limit,
			String projectTitle, String usernameBy, String submittedOnFrom,
			String submittedOnTo, Double totalCostsFrom, Double totalCostsTo,
			String proposalStatus, String userRole, String userId,
			String college, String department, String positionType,
			String positionTitle) throws ParseException {
		Datastore ds = getDatastore();
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);
		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		if (projectTitle != null) {
			proposalQuery.field("project info.project title")
					.containsIgnoreCase(projectTitle);
		}

		if (submittedOnFrom != null && !submittedOnFrom.isEmpty()) {
			Date receivedOnF = formatter.parse(submittedOnFrom);
			proposalQuery.field("date submitted").greaterThanOrEq(receivedOnF);
		}
		if (submittedOnTo != null && !submittedOnTo.isEmpty()) {
			Date receivedOnT = formatter.parse(submittedOnTo);
			proposalQuery.field("date submitted").lessThanOrEq(receivedOnT);
		}

		if (totalCostsFrom != null && totalCostsFrom != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.greaterThanOrEq(totalCostsFrom);
		}
		if (totalCostsTo != null && totalCostsTo != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.lessThanOrEq(totalCostsTo);
		}

		if (proposalStatus != null) {
			proposalQuery.field("proposal status").contains(proposalStatus);
		}

		// High Level Users University Level --> University Research
		// Administrator,University Research Director
		// College Level --> Dean,Associate Dean
		// Department Level --> Business Manager, IRB, Department Administrative
		// Assistant,Department Chair,Associate Chair

		if (!positionTitle.equals("University Research Administrator")
				&& !positionTitle.equals("University Research Director")) {
			if (positionTitle.equals("Dean")
					|| positionTitle.equals("Associate Dean")) {
				proposalQuery.or(
						proposalQuery.criteria("investigator info.pi.college")
								.equal(college),
						proposalQuery.criteria(
								"investigator info.co_pi.college").equal(
								college),
						proposalQuery.criteria(
								"investigator info.senior personnel.college")
								.equal(college));
			} else if (positionTitle.equals("Business Manager")
					|| positionTitle
							.equals("Department Administrative Assistant")
					|| positionTitle.equals("Department Chair")
					|| positionTitle.equals("Associate Chair")) {
				proposalQuery
						.and(proposalQuery
								.or(proposalQuery.criteria(
										"investigator info.pi.college").equal(
										college),
										proposalQuery
												.criteria(
														"investigator info.co_pi.college")
												.equal(college),
										proposalQuery
												.criteria(
														"investigator info.senior personnel.college")
												.equal(college)),
								proposalQuery
										.or(proposalQuery
												.criteria(
														"investigator info.pi.department")
												.equal(department),
												proposalQuery
														.criteria(
																"investigator info.co_pi.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department)));
			} else if (positionTitle.equals("IRB")) {
				proposalQuery
						.and(proposalQuery
								.or(proposalQuery.criteria(
										"investigator info.pi.college").equal(
										college),
										proposalQuery
												.criteria(
														"investigator info.co_pi.college")
												.equal(college),
										proposalQuery
												.criteria(
														"investigator info.senior personnel.college")
												.equal(college)),
								proposalQuery
										.or(proposalQuery
												.criteria(
														"investigator info.pi.department")
												.equal(department),
												proposalQuery
														.criteria(
																"investigator info.co_pi.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department)),
								proposalQuery.criteria("irb approval required")
										.equal(true));
			} else {
				proposalQuery
						.or(proposalQuery.and(
								proposalQuery.criteria(
										"investigator info.pi.user profile id")
										.equal(userId),
								proposalQuery.criteria(
										"investigator info.pi.college").equal(
										college),
								proposalQuery.criteria(
										"investigator info.pi.department")
										.equal(department),
								proposalQuery.criteria(
										"investigator info.pi.position type")
										.equal(positionType),
								proposalQuery.criteria(
										"investigator info.pi.position title")
										.equal(positionTitle)),
								proposalQuery
										.and(proposalQuery
												.criteria(
														"investigator info.co_pi.user profile id")
												.equal(userId),
												proposalQuery
														.criteria(
																"investigator info.co_pi.college")
														.equal(college),
												proposalQuery
														.criteria(
																"investigator info.co_pi.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.co_pi.position type")
														.equal(positionType),
												proposalQuery
														.criteria(
																"investigator info.co_pi.position title")
														.equal(positionTitle)),
								proposalQuery
										.and(proposalQuery
												.criteria(
														"investigator info.senior personnel.user profile id")
												.equal(userId),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.college")
														.equal(college),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.position type")
														.equal(positionType),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.position title")
														.equal(positionTitle)));
			}
		}

		if (usernameBy != null) {
			// accountQuery.field("username").containsIgnoreCase(usernameBy);
			// profileQuery.criteria("user id").in(accountQuery.asKeyList());
			profileQuery.or(
					profileQuery.criteria("first name").containsIgnoreCase(
							usernameBy),
					profileQuery.criteria("middle name").containsIgnoreCase(
							usernameBy), profileQuery.criteria("last name")
							.containsIgnoreCase(usernameBy));
			if (userRole != null) {
				switch (userRole) {
				case "PI":
					proposalQuery.criteria("investigator info.pi.user profile")
							.in(profileQuery.asKeyList());
					break;
				case "Co-PI":
					proposalQuery.criteria(
							"investigator info.co_pi.user profile").in(
							profileQuery.asKeyList());
					break;

				case "Senior Personnel":
					proposalQuery.criteria(
							"investigator info.senior personnel.user profile")
							.in(profileQuery.asKeyList());
					break;

				default:
					break;
				}
			} else {
				proposalQuery
						.or(proposalQuery.criteria(
								"investigator info.pi.user profile").in(
								profileQuery.asKeyList()),
								proposalQuery.criteria(
										"investigator info.co_pi.user profile")
										.in(profileQuery.asKeyList()),
								proposalQuery
										.criteria(
												"investigator info.senior personnel.user profile")
										.in(profileQuery.asKeyList()));
			}
		} else if (usernameBy == null && userRole != null) {
			switch (userRole) {
			case "PI":
				proposalQuery.criteria("investigator info.pi.user profile id")
						.equal(userId);
				break;
			case "Co-PI":
				proposalQuery.criteria(
						"investigator info.co_pi.user profile id")
						.equal(userId);
				break;

			case "Senior Personnel":
				proposalQuery.criteria(
						"investigator info.senior personnel.user profile id")
						.equal(userId);
				break;

			default:
				break;
			}
		}

		int rowTotal = proposalQuery.asList().size();
		List<Proposal> allProposals = proposalQuery.offset(offset - 1)
				.limit(limit).order("-audit log.activity on").asList();

		for (Proposal userProposal : allProposals) {
			ProposalInfo proposal = new ProposalInfo();

			// Proposal
			proposal.setRowTotal(rowTotal);
			proposal.setId(userProposal.getId().toString());

			// ProjectInfo
			proposal.setProjectTitle(userProposal.getProjectInfo()
					.getProjectTitle());

			ProjectType pt = userProposal.getProjectInfo().getProjectType();
			if (pt.isResearchBasic()) {
				proposal.setProjectType("Research-basic");
			} else if (pt.isResearchApplied()) {
				proposal.setProjectType("Research-applied");
			} else if (pt.isResearchDevelopment()) {
				proposal.setProjectType("Research-development");
			} else if (pt.isInstruction()) {
				proposal.setProjectType("Instruction");
			} else if (pt.isOtherSponsoredActivity()) {
				proposal.setProjectType("Other sponsored activity");
			}

			TypeOfRequest tor = userProposal.getProjectInfo()
					.getTypeOfRequest();
			if (tor.isPreProposal()) {
				proposal.getTypeOfRequest().add("Pre-proposal");
			} else if (tor.isNewProposal()) {
				proposal.getTypeOfRequest().add("New proposal");
			} else if (tor.isContinuation()) {
				proposal.getTypeOfRequest().add("Continuation");
			} else if (tor.isSupplement()) {
				proposal.getTypeOfRequest().add("Supplement");
			}

			ProjectLocation pl = userProposal.getProjectInfo()
					.getProjectLocation();
			if (pl.isOffCampus()) {
				proposal.setProjectLocation("Off-campus");
			} else if (pl.isOnCampus()) {
				proposal.setProjectLocation("On-campus");
			}

			// SponsorAndBudgetInfo
			proposal.setGrantingAgencies(userProposal.getSponsorAndBudgetInfo()
					.getGrantingAgency());
			proposal.setDirectCosts(userProposal.getSponsorAndBudgetInfo()
					.getDirectCosts());
			proposal.setFaCosts(userProposal.getSponsorAndBudgetInfo()
					.getFaCosts());
			proposal.setTotalCosts(userProposal.getSponsorAndBudgetInfo()
					.getTotalCosts());
			proposal.setFaRate(userProposal.getSponsorAndBudgetInfo()
					.getFaRate());

			proposal.setDateCreated(userProposal.getDateCreated());
			proposal.setDateSubmitted(userProposal.getDateSubmitted());

			proposal.setDueDate(userProposal.getProjectInfo().getDueDate());
			proposal.setProjectPeriodFrom(userProposal.getProjectInfo()
					.getProjectPeriod().getFrom());
			proposal.setProjectPeriodTo(userProposal.getProjectInfo()
					.getProjectPeriod().getTo());

			// Proposal Status
			for (Status status : userProposal.getProposalStatus()) {
				proposal.getProposalStatus().add(status.toString());
			}

			// PI
			proposal.setSubmittedByPI(userProposal.getSubmittedByPI());
			proposal.setReadyForSubmissionByPI(userProposal
					.isReadyForSubmissionByPI());
			proposal.setDeletedByPI(userProposal.getDeletedByPI());

			// Chair
			proposal.setChairApproval(userProposal.getChairApproval());

			// Business Manager
			proposal.setBusinessManagerApproval(userProposal
					.getBusinessManagerApproval());

			// IRB
			proposal.setIrbApproval(userProposal.getIrbApproval());

			// Dean
			proposal.setDeanApproval(userProposal.getDeanApproval());

			// University Research Administrator
			proposal.setResearchAdministratorApproval(userProposal
					.getResearchAdministratorApproval());

			proposal.setResearchAdministratorWithdraw(userProposal
					.getResearchAdministratorWithdraw());

			proposal.setResearchAdministratorSubmission(userProposal
					.getResearchAdministratorSubmission());

			// University Research Director
			proposal.setResearchDirectorApproval(userProposal
					.getResearchDirectorApproval());

			proposal.setResearchDirectorDeletion(userProposal
					.getResearchDirectorDeletion());

			proposal.setResearchDirectorArchived(userProposal
					.getResearchDirectorArchived());

			proposal.setIrbApprovalRequired(userProposal
					.isIrbApprovalRequired());

			if (userProposal.getDeletedByPI().equals(DeleteType.DELETED)
					|| userProposal.getResearchDirectorDeletion().equals(
							DeleteType.DELETED)) {
				proposal.setDeleted(true);
			}

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();

			int auditLogCount = userProposal.getAuditLog().size();
			if (userProposal.getAuditLog() != null && auditLogCount != 0) {
				AuditLog auditLog = userProposal.getAuditLog().get(
						auditLogCount - 1);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserProfile().getFullName();
				lastAuditAction = auditLog.getAction();
			}

			proposal.setLastAudited(lastAudited);
			proposal.setLastAuditedBy(lastAuditedBy);
			proposal.setLastAuditAction(lastAuditAction);

			String piUserId = userProposal.getInvestigatorInfo().getPi()
					.getUserProfileId();
			String piUserCollege = userProposal.getInvestigatorInfo().getPi()
					.getCollege();
			String piUserDepartment = userProposal.getInvestigatorInfo()
					.getPi().getDepartment();
			String piUserPositionType = userProposal.getInvestigatorInfo()
					.getPi().getPositionType();
			String piUserPositionTitle = userProposal.getInvestigatorInfo()
					.getPi().getPositionTitle();

			proposal.setPiUser(piUserId);
			if (!proposal.getAllUsers().contains(piUserId)) {
				proposal.getAllUsers().add(piUserId);
			}

			if (piUserId.equals(userId) && piUserCollege.equals(college)
					&& piUserDepartment.equals(department)
					&& piUserPositionType.equals(positionType)
					&& piUserPositionTitle.equals(positionTitle)) {
				proposal.getCurrentuserProposalRoles().add("PI");
			}

			List<InvestigatorRefAndPosition> allCoPI = userProposal
					.getInvestigatorInfo().getCo_pi();
			for (InvestigatorRefAndPosition coPI : allCoPI) {
				String coPIUser = coPI.getUserProfileId();
				String coPIUserCollege = coPI.getCollege();
				String coPIUserDepartment = coPI.getDepartment();
				String coPIUserPositionType = coPI.getPositionType();
				String coPIUserPositionTitle = coPI.getPositionTitle();

				proposal.getCopiUsers().add(coPIUser);
				if (!proposal.getAllUsers().contains(coPIUser)) {
					proposal.getAllUsers().add(coPIUser);
				}

				if (coPIUser.equals(userId) && coPIUserCollege.equals(college)
						&& coPIUserDepartment.equals(department)
						&& coPIUserPositionType.equals(positionType)
						&& coPIUserPositionTitle.equals(positionTitle)) {
					proposal.getCurrentuserProposalRoles().add("Co-PI");
				}
			}

			List<InvestigatorRefAndPosition> allSeniors = userProposal
					.getInvestigatorInfo().getSeniorPersonnel();
			for (InvestigatorRefAndPosition senior : allSeniors) {
				String seniorUser = senior.getUserProfileId();
				String seniorUserCollege = senior.getCollege();
				String seniorUserDepartment = senior.getDepartment();
				String seniorUserPositionType = senior.getPositionType();
				String seniorUserPositionTitle = senior.getPositionTitle();

				proposal.getSeniorUsers().add(seniorUser);
				if (!proposal.getAllUsers().contains(seniorUser)) {
					proposal.getAllUsers().add(seniorUser);
				}

				if (seniorUser.equals(userId)
						&& seniorUserCollege.equals(college)
						&& seniorUserDepartment.equals(department)
						&& seniorUserPositionType.equals(positionType)
						&& seniorUserPositionTitle.equals(positionTitle)) {
					proposal.getCurrentuserProposalRoles().add(
							"Senior Personnel");
				}
			}

			proposals.add(proposal);
		}
		// Collections.sort(proposals);
		return proposals;
	}

	public List<ProposalInfo> findAllUserProposals(String projectTitle,
			String usernameBy, String submittedOnFrom, String submittedOnTo,
			Double totalCostsFrom, Double totalCostsTo, String proposalStatus,
			String userRole, String userId, String college, String department,
			String positionType, String positionTitle) throws ParseException {
		Datastore ds = getDatastore();
		List<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);
		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		if (projectTitle != null) {
			proposalQuery.field("project info.project title")
					.containsIgnoreCase(projectTitle);
		}

		if (submittedOnFrom != null && !submittedOnFrom.isEmpty()) {
			Date receivedOnF = formatter.parse(submittedOnFrom);
			proposalQuery.field("date submitted").greaterThanOrEq(receivedOnF);
		}
		if (submittedOnTo != null && !submittedOnTo.isEmpty()) {
			Date receivedOnT = formatter.parse(submittedOnTo);
			proposalQuery.field("date submitted").lessThanOrEq(receivedOnT);
		}

		if (totalCostsFrom != null && totalCostsFrom != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.greaterThanOrEq(totalCostsFrom);
		}
		if (totalCostsTo != null && totalCostsTo != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.lessThanOrEq(totalCostsTo);
		}

		if (proposalStatus != null) {
			proposalQuery.field("proposal status").contains(proposalStatus);
		}

		// High Level Users University Level --> University Research
		// Administrator, University Research Director
		// College Level --> Dean,Associate Dean
		// Department Level --> Business Manager,Department Administrative
		// Assistant,Department Chair,Associate Chair

		if (!positionTitle.equals("University Research Administrator")
				&& !positionTitle.equals("University Research Director")) {
			if (positionTitle.equals("Dean")
					|| positionTitle.equals("Associate Dean")) {
				proposalQuery.or(
						proposalQuery.criteria("investigator info.pi.college")
								.equal(college),
						proposalQuery.criteria(
								"investigator info.co_pi.college").equal(
								college),
						proposalQuery.criteria(
								"investigator info.senior personnel.college")
								.equal(college));
			} else if (positionTitle.equals("Business Manager")
					|| positionTitle
							.equals("Department Administrative Assistant")
					|| positionTitle.equals("Department Chair")
					|| positionTitle.equals("Associate Chair")) {
				proposalQuery
						.and(proposalQuery
								.or(proposalQuery.criteria(
										"investigator info.pi.college").equal(
										college),
										proposalQuery
												.criteria(
														"investigator info.co_pi.college")
												.equal(college),
										proposalQuery
												.criteria(
														"investigator info.senior personnel.college")
												.equal(college)),
								proposalQuery
										.or(proposalQuery
												.criteria(
														"investigator info.pi.department")
												.equal(department),
												proposalQuery
														.criteria(
																"investigator info.co_pi.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department)));
			} else {
				proposalQuery
						.or(proposalQuery.and(
								proposalQuery.criteria(
										"investigator info.pi.user profile id")
										.equal(userId),
								proposalQuery.criteria(
										"investigator info.pi.college").equal(
										college),
								proposalQuery.criteria(
										"investigator info.pi.department")
										.equal(department),
								proposalQuery.criteria(
										"investigator info.pi.position type")
										.equal(positionType),
								proposalQuery.criteria(
										"investigator info.pi.position title")
										.equal(positionTitle)),
								proposalQuery
										.and(proposalQuery
												.criteria(
														"investigator info.co_pi.user profile id")
												.equal(userId),
												proposalQuery
														.criteria(
																"investigator info.co_pi.college")
														.equal(college),
												proposalQuery
														.criteria(
																"investigator info.co_pi.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.co_pi.position type")
														.equal(positionType),
												proposalQuery
														.criteria(
																"investigator info.co_pi.position title")
														.equal(positionTitle)),
								proposalQuery
										.and(proposalQuery
												.criteria(
														"investigator info.senior personnel.user profile id")
												.equal(userId),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.college")
														.equal(college),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.position type")
														.equal(positionType),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.position title")
														.equal(positionTitle)));
			}
		}

		if (usernameBy != null) {
			// accountQuery.field("username").containsIgnoreCase(usernameBy);
			// profileQuery.criteria("user id").in(accountQuery.asKeyList());
			profileQuery.or(
					profileQuery.criteria("first name").containsIgnoreCase(
							usernameBy),
					profileQuery.criteria("middle name").containsIgnoreCase(
							usernameBy), profileQuery.criteria("last name")
							.containsIgnoreCase(usernameBy));
			if (userRole != null) {
				switch (userRole) {
				case "PI":
					proposalQuery.criteria("investigator info.pi.user profile")
							.in(profileQuery.asKeyList());
					break;
				case "Co-PI":
					proposalQuery.criteria(
							"investigator info.co_pi.user profile").in(
							profileQuery.asKeyList());
					break;

				case "Senior Personnel":
					proposalQuery.criteria(
							"investigator info.senior personnel.user profile")
							.in(profileQuery.asKeyList());
					break;

				default:
					break;
				}
			} else {
				proposalQuery
						.or(proposalQuery.criteria(
								"investigator info.pi.user profile").in(
								profileQuery.asKeyList()),
								proposalQuery.criteria(
										"investigator info.co_pi.user profile")
										.in(profileQuery.asKeyList()),
								proposalQuery
										.criteria(
												"investigator info.senior personnel.user profile")
										.in(profileQuery.asKeyList()));
			}
		} else if (usernameBy == null && userRole != null) {
			switch (userRole) {
			case "PI":
				proposalQuery.criteria("investigator info.pi.user profile id")
						.equal(userId);
				break;
			case "Co-PI":
				proposalQuery.criteria(
						"investigator info.co_pi.user profile id")
						.equal(userId);
				break;

			case "Senior Personnel":
				proposalQuery.criteria(
						"investigator info.senior personnel.user profile id")
						.equal(userId);
				break;

			default:
				break;
			}
		}

		int rowTotal = proposalQuery.asList().size();
		List<Proposal> allProposals = proposalQuery.order(
				"-audit log.activity on").asList();

		for (Proposal userProposal : allProposals) {
			ProposalInfo proposal = new ProposalInfo();

			// Proposal
			proposal.setRowTotal(rowTotal);
			proposal.setId(userProposal.getId().toString());

			// ProjectInfo
			proposal.setProjectTitle(userProposal.getProjectInfo()
					.getProjectTitle());

			ProjectType pt = userProposal.getProjectInfo().getProjectType();
			if (pt.isResearchBasic()) {
				proposal.setProjectType("Research-basic");
			} else if (pt.isResearchApplied()) {
				proposal.setProjectType("Research-applied");
			} else if (pt.isResearchDevelopment()) {
				proposal.setProjectType("Research-development");
			} else if (pt.isInstruction()) {
				proposal.setProjectType("Instruction");
			} else if (pt.isOtherSponsoredActivity()) {
				proposal.setProjectType("Other sponsored activity");
			}

			TypeOfRequest tor = userProposal.getProjectInfo()
					.getTypeOfRequest();
			if (tor.isPreProposal()) {
				proposal.getTypeOfRequest().add("Pre-proposal");
			} else if (tor.isNewProposal()) {
				proposal.getTypeOfRequest().add("New proposal");
			} else if (tor.isContinuation()) {
				proposal.getTypeOfRequest().add("Continuation");
			} else if (tor.isSupplement()) {
				proposal.getTypeOfRequest().add("Supplement");
			}

			ProjectLocation pl = userProposal.getProjectInfo()
					.getProjectLocation();
			if (pl.isOffCampus()) {
				proposal.setProjectLocation("Off-campus");
			} else if (pl.isOnCampus()) {
				proposal.setProjectLocation("On-campus");
			}

			// SponsorAndBudgetInfo
			proposal.setGrantingAgencies(userProposal.getSponsorAndBudgetInfo()
					.getGrantingAgency());
			proposal.setDirectCosts(userProposal.getSponsorAndBudgetInfo()
					.getDirectCosts());
			proposal.setFaCosts(userProposal.getSponsorAndBudgetInfo()
					.getFaCosts());
			proposal.setTotalCosts(userProposal.getSponsorAndBudgetInfo()
					.getTotalCosts());
			proposal.setFaRate(userProposal.getSponsorAndBudgetInfo()
					.getFaRate());

			proposal.setDateCreated(userProposal.getDateCreated());
			proposal.setDateSubmitted(userProposal.getDateSubmitted());

			proposal.setDueDate(userProposal.getProjectInfo().getDueDate());
			proposal.setProjectPeriodFrom(userProposal.getProjectInfo()
					.getProjectPeriod().getFrom());
			proposal.setProjectPeriodTo(userProposal.getProjectInfo()
					.getProjectPeriod().getTo());

			// Proposal Status
			for (Status status : userProposal.getProposalStatus()) {
				proposal.getProposalStatus().add(status.toString());
			}

			// PI
			proposal.setSubmittedByPI(userProposal.getSubmittedByPI());
			proposal.setReadyForSubmissionByPI(userProposal
					.isReadyForSubmissionByPI());
			proposal.setDeletedByPI(userProposal.getDeletedByPI());

			// Chair
			proposal.setChairApproval(userProposal.getChairApproval());

			// Business Manager
			proposal.setBusinessManagerApproval(userProposal
					.getBusinessManagerApproval());

			// IRB
			proposal.setIrbApproval(userProposal.getIrbApproval());

			// Dean
			proposal.setDeanApproval(userProposal.getDeanApproval());

			// University Research Administrator
			proposal.setResearchAdministratorApproval(userProposal
					.getResearchAdministratorApproval());

			proposal.setResearchAdministratorWithdraw(userProposal
					.getResearchAdministratorWithdraw());

			proposal.setResearchAdministratorSubmission(userProposal
					.getResearchAdministratorSubmission());

			// University Research Director
			proposal.setResearchDirectorApproval(userProposal
					.getResearchDirectorApproval());

			proposal.setResearchDirectorDeletion(userProposal
					.getResearchDirectorDeletion());

			proposal.setResearchDirectorArchived(userProposal
					.getResearchDirectorArchived());

			proposal.setIrbApprovalRequired(userProposal
					.isIrbApprovalRequired());

			if (userProposal.getDeletedByPI().equals(DeleteType.DELETED)
					|| userProposal.getResearchDirectorDeletion().equals(
							DeleteType.DELETED)) {
				proposal.setDeleted(true);
			}

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();

			int auditLogCount = userProposal.getAuditLog().size();
			if (userProposal.getAuditLog() != null && auditLogCount != 0) {
				AuditLog auditLog = userProposal.getAuditLog().get(
						auditLogCount - 1);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserProfile().getFullName();
				lastAuditAction = auditLog.getAction();
			}

			proposal.setLastAudited(lastAudited);
			proposal.setLastAuditedBy(lastAuditedBy);
			proposal.setLastAuditAction(lastAuditAction);

			String piUserId = userProposal.getInvestigatorInfo().getPi()
					.getUserRef().getId().toString();
			proposal.setPiUser(piUserId);
			if (!proposal.getAllUsers().contains(piUserId)) {
				proposal.getAllUsers().add(piUserId);
			}

			if (piUserId.equals(userId)) {
				proposal.getCurrentuserProposalRoles().add("PI");
			}

			List<InvestigatorRefAndPosition> allCoPI = userProposal
					.getInvestigatorInfo().getCo_pi();
			for (InvestigatorRefAndPosition coPI : allCoPI) {
				String coPIUser = coPI.getUserRef().getId().toString();
				proposal.getCopiUsers().add(coPIUser);
				if (!proposal.getAllUsers().contains(coPIUser)) {
					proposal.getAllUsers().add(coPIUser);
				}

				if (coPI.getUserProfileId().equals(userId)) {
					proposal.getCurrentuserProposalRoles().add("Co-PI");
				}
			}

			List<InvestigatorRefAndPosition> allSeniors = userProposal
					.getInvestigatorInfo().getSeniorPersonnel();
			for (InvestigatorRefAndPosition senior : allSeniors) {
				String seniorUser = senior.getUserRef().getId().toString();
				proposal.getSeniorUsers().add(seniorUser);
				if (!proposal.getAllUsers().contains(seniorUser)) {
					proposal.getAllUsers().add(seniorUser);
				}

				if (senior.getUserProfileId().equals(userId)) {
					proposal.getCurrentuserProposalRoles().add(
							"Senior Personnel");
				}
			}

			proposals.add(proposal);
		}
		// Collections.sort(proposals);
		return proposals;
	}

	public List<SignatureInfo> findAllSignatureForAProposal(ObjectId id,
			boolean irbApprovalRequired) throws ParseException {
		Datastore ds = getDatastore();
		List<SignatureInfo> signatures = new ArrayList<SignatureInfo>();
		List<CollegeDepartmentInfo> investigators = new ArrayList<CollegeDepartmentInfo>();

		Query<Proposal> q1 = ds
				.createQuery(Proposal.class)
				.field("_id")
				.equal(id)
				.retrievedFields(true, "_id", "investigator info",
						"signature info");
		Proposal proposal = q1.get();

		// Adding PI
		SignatureInfo piSign = new SignatureInfo();
		InvestigatorRefAndPosition PI = proposal.getInvestigatorInfo().getPi();

		boolean piAlreadySigned = false;
		for (SignatureInfo signature : proposal.getSignatureInfo()) {
			if (PI.getUserRef().getId().toString()
					.equals(signature.getUserProfileId())
					&& !PI.getUserRef().isDeleted()
					&& signature.getPositionTitle().equals("PI")) {
				piSign.setUserProfileId(signature.getUserProfileId());
				piSign.setFullName(signature.getFullName());
				piSign.setSignature(signature.getSignature());
				piSign.setSignedDate(signature.getSignedDate());
				piSign.setNote(signature.getNote());
				piSign.setPositionTitle(signature.getPositionTitle());
				piSign.setDelegated(signature.isDelegated());
				boolean piAlreadyExist = false;
				for (SignatureInfo sign : signatures) {
					if (sign.getUserProfileId().equalsIgnoreCase(
							piSign.getUserProfileId())) {
						piAlreadyExist = true;
						break;
					}
				}
				if (!piAlreadyExist) {
					signatures.add(piSign);
				}
				piAlreadySigned = true;
			}
		}

		if (!piAlreadySigned && !PI.getUserRef().isDeleted()) {
			piSign.setUserProfileId(PI.getUserRef().getId().toString());
			piSign.setFullName(PI.getUserRef().getFullName());
			piSign.setSignature("");
			piSign.setNote("");
			piSign.setPositionTitle("PI");
			piSign.setDelegated(false);
			boolean piAlreadyExist = false;
			for (SignatureInfo sign : signatures) {
				if (sign.getUserProfileId().equalsIgnoreCase(
						piSign.getUserProfileId())) {
					piAlreadyExist = true;
					break;
				}
			}
			if (!piAlreadyExist) {
				signatures.add(piSign);
			}
		}

		CollegeDepartmentInfo investRef = new CollegeDepartmentInfo();
		if (!PI.getUserRef().isDeleted()) {
			investRef.setCollege(PI.getCollege());
			investRef.setDepartment(PI.getDepartment());
			investigators.add(investRef);
		}

		for (InvestigatorRefAndPosition coPIs : proposal.getInvestigatorInfo()
				.getCo_pi()) {
			// Adding Co-PIs
			SignatureInfo coPISign = new SignatureInfo();

			boolean coPIAlreadySigned = false;
			for (SignatureInfo signature : proposal.getSignatureInfo()) {
				if (coPIs.getUserRef().getId().toString()
						.equals(signature.getUserProfileId())
						&& !coPIs.getUserRef().isDeleted()
						&& signature.getPositionTitle().equals("Co-PI")) {
					coPISign.setUserProfileId(signature.getUserProfileId());
					coPISign.setFullName(signature.getFullName());
					coPISign.setSignature(signature.getSignature());
					coPISign.setSignedDate(signature.getSignedDate());
					coPISign.setNote(signature.getNote());
					coPISign.setPositionTitle(signature.getPositionTitle());
					coPISign.setDelegated(signature.isDelegated());
					boolean coPIAlreadyExist = false;
					for (SignatureInfo sign : signatures) {
						if (sign.getUserProfileId().equalsIgnoreCase(
								coPISign.getUserProfileId())) {
							coPIAlreadyExist = true;
							break;
						}
					}
					if (!coPIAlreadyExist) {
						signatures.add(coPISign);
					}
					coPIAlreadySigned = true;
				}
			}

			if (!coPIAlreadySigned && !coPIs.getUserRef().isDeleted()) {
				coPISign.setUserProfileId(coPIs.getUserRef().getId().toString());
				coPISign.setFullName(coPIs.getUserRef().getFullName());

				coPISign.setSignature("");
				coPISign.setNote("");
				coPISign.setPositionTitle("Co-PI");
				coPISign.setDelegated(false);
				boolean coPIAlreadyExist = false;
				for (SignatureInfo sign : signatures) {
					if (sign.getUserProfileId().equalsIgnoreCase(
							coPISign.getUserProfileId())) {
						coPIAlreadyExist = true;
						break;
					}
				}
				if (!coPIAlreadyExist) {
					signatures.add(coPISign);
				}
			}

			if (!coPIs.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(coPIs.getCollege());
				investRef.setDepartment(coPIs.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}
			}
		}

		// for (InvestigatorRefAndPosition seniors : proposal
		// .getInvestigatorInfo().getSeniorPersonnel()) {
		// // Adding Seniors
		// SignatureInfo seniorSign = new SignatureInfo();
		//
		// boolean seniorAlreadySigned = false;
		// for (SignatureInfo signature : proposal.getSignatureInfo()) {
		// if (seniors.getUserRef().getId().toString()
		// .equals(signature.getUserProfileId()) &&
		// !seniors.getUserRef().isDeleted()
		// && signature.getPositionTitle().equals(
		// "Senior Personnel")) {
		// seniorSign.setUserProfileId(signature.getUserProfileId());
		// seniorSign.setFullName(signature.getFullName());
		// seniorSign.setSignature(signature.getSignature());
		// seniorSign.setSignedDate(signature.getSignedDate());
		// seniorSign.setNote(signature.getNote());
		// seniorSign.setPositionTitle(signature.getPositionTitle());
		// seniorSign.setDelegated(signature.isDelegated());
		// if (!signatures.contains(seniorSign)) {
		// signatures.add(seniorSign);
		// }
		// boolean seniorAlreadyExist = false;
		// for (SignatureInfo sign : signatures) {
		// if (sign.getUserProfileId().equalsIgnoreCase(
		// seniorSign.getUserProfileId())) {
		// seniorAlreadyExist = true;
		// break;
		// }
		// }
		// if (!seniorAlreadyExist) {
		// signatures.add(seniorSign);
		// }
		// seniorAlreadySigned = true;
		// }
		// }
		//
		// if (!seniorAlreadySigned && !seniors.getUserRef().isDeleted()) {
		// seniorSign.setUserProfileId(seniors.getUserRef().getId()
		// .toString());
		// seniorSign.setFullName(seniors.getUserRef().getFullName());
		// seniorSign.setSignature("");
		// seniorSign.setNote("");
		// seniorSign.setPositionTitle("Senior Personnel");
		// seniorSign.setDelegated(false);
		// boolean seniorAlreadyExist = false;
		// for (SignatureInfo sign : signatures) {
		// if (sign.getUserProfileId().equalsIgnoreCase(
		// seniorSign.getUserProfileId())) {
		// seniorAlreadyExist = true;
		// break;
		// }
		// }
		// if (!seniorAlreadyExist) {
		// signatures.add(seniorSign);
		// }
		// }
		// if (!seniors.getUserRef().isDeleted()) {
		// investRef = new CollegeDepartmentInfo();
		// investRef.setCollege(seniors.getCollege());
		// investRef.setDepartment(seniors.getDepartment());
		// if (!investigators.contains(investRef)) {
		// investigators.add(investRef);
		// }
		// }
		// }

		// 2. Get all Users filter using College in<> and Department in <> and
		// Position Title equal <>
		// Department Chair
		// Business Manager
		// IRB
		// Dean
		// University Research Director
		// Research Administrator
		List<String> positions = new ArrayList<String>();
		positions.add("Department Chair");
		positions.add("Business Manager");
		positions.add("Dean");
		positions.add("University Research Administrator");
		positions.add("University Research Director");

		if (irbApprovalRequired) {
			positions.add("IRB");
		}

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details");

		profileQuery.and(profileQuery.criteria("deleted").equal(false),
				profileQuery.criteria("details.position title").in(positions));
		List<UserProfile> userProfile = profileQuery.asList();

		for (UserProfile user : userProfile) {
			for (PositionDetails posDetails : user.getDetails()) {
				for (CollegeDepartmentInfo colDeptInfo : investigators) {
					if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Department Chair")) {
						SignatureInfo signDeptChair = new SignatureInfo();

						boolean departmentChairAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Department Chair")) {
								signDeptChair.setUserProfileId(signature
										.getUserProfileId());
								signDeptChair.setFullName(signature
										.getFullName());
								signDeptChair.setSignature(signature
										.getSignature());
								signDeptChair.setSignedDate(signature
										.getSignedDate());
								signDeptChair.setNote(signature.getNote());
								signDeptChair.setPositionTitle(signature
										.getPositionTitle());
								signDeptChair.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signDeptChair)) {
									signatures.add(signDeptChair);
								}
								departmentChairAlreadySigned = true;
							}
						}

						if (!departmentChairAlreadySigned) {
							signDeptChair.setUserProfileId(user.getId()
									.toString());
							signDeptChair.setFullName(user.getFullName());
							signDeptChair.setSignature("");
							signDeptChair.setNote("");
							signDeptChair.setPositionTitle("Department Chair");
							signDeptChair.setDelegated(false);
							if (!signatures.contains(signDeptChair)) {
								signatures.add(signDeptChair);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Business Manager")) {
						SignatureInfo signBusinessMgr = new SignatureInfo();

						boolean businessManagerAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Business Manager")) {
								signBusinessMgr.setUserProfileId(signature
										.getUserProfileId());
								signBusinessMgr.setFullName(signature
										.getFullName());
								signBusinessMgr.setSignature(signature
										.getSignature());
								signBusinessMgr.setSignedDate(signature
										.getSignedDate());
								signBusinessMgr.setNote(signature.getNote());
								signBusinessMgr.setPositionTitle(signature
										.getPositionTitle());
								signBusinessMgr.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signBusinessMgr)) {
									signatures.add(signBusinessMgr);
								}
								businessManagerAlreadySigned = true;
							}
						}

						if (!businessManagerAlreadySigned) {
							signBusinessMgr.setUserProfileId(user.getId()
									.toString());
							signBusinessMgr.setFullName(user.getFullName());
							signBusinessMgr.setSignature("");
							signBusinessMgr.setNote("");
							signBusinessMgr
									.setPositionTitle("Business Manager");
							signBusinessMgr.setDelegated(false);
							if (!signatures.contains(signBusinessMgr)) {
								signatures.add(signBusinessMgr);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Dean")) {
						SignatureInfo signDean = new SignatureInfo();

						boolean deanAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Dean")) {
								signDean.setUserProfileId(signature
										.getUserProfileId());
								signDean.setFullName(signature.getFullName());
								signDean.setSignature(signature.getSignature());
								signDean.setSignedDate(signature
										.getSignedDate());
								signDean.setNote(signature.getNote());
								signDean.setPositionTitle(signature
										.getPositionTitle());
								signDean.setDelegated(signature.isDelegated());
								if (!signatures.contains(signDean)) {
									signatures.add(signDean);
								}
								deanAlreadySigned = true;
							}
						}

						if (!deanAlreadySigned) {
							signDean.setUserProfileId(user.getId().toString());
							signDean.setFullName(user.getFullName());
							signDean.setSignature("");
							signDean.setNote("");
							signDean.setPositionTitle("Dean");
							signDean.setDelegated(false);
							if (!signatures.contains(signDean)) {
								signatures.add(signDean);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"IRB") && irbApprovalRequired) {
						SignatureInfo signBusinessMgr = new SignatureInfo();

						boolean irbAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"IRB")) {
								signBusinessMgr.setUserProfileId(signature
										.getUserProfileId());
								signBusinessMgr.setFullName(signature
										.getFullName());
								signBusinessMgr.setSignature(signature
										.getSignature());
								signBusinessMgr.setSignedDate(signature
										.getSignedDate());
								signBusinessMgr.setNote(signature.getNote());
								signBusinessMgr.setPositionTitle(signature
										.getPositionTitle());
								signBusinessMgr.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signBusinessMgr)) {
									signatures.add(signBusinessMgr);
								}
								irbAlreadySigned = true;
							}
						}

						if (!irbAlreadySigned) {
							signBusinessMgr.setUserProfileId(user.getId()
									.toString());
							signBusinessMgr.setFullName(user.getFullName());
							signBusinessMgr.setSignature("");
							signBusinessMgr.setNote("");
							signBusinessMgr.setPositionTitle("IRB");
							signBusinessMgr.setDelegated(false);
							if (!signatures.contains(signBusinessMgr)) {
								signatures.add(signBusinessMgr);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Administrator")) {
						SignatureInfo signAdmin = new SignatureInfo();

						boolean adminAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature
											.getPositionTitle()
											.equals("University Research Administrator")) {
								signAdmin.setUserProfileId(signature
										.getUserProfileId());
								signAdmin.setFullName(user.getFullName());
								signAdmin
										.setSignature(signature.getSignature());
								signAdmin.setSignedDate(signature
										.getSignedDate());
								signAdmin.setNote(signature.getNote());
								signAdmin
										.setPositionTitle("University Research Administrator");
								signAdmin.setDelegated(signature.isDelegated());
								if (!signatures.contains(signAdmin)) {
									signatures.add(signAdmin);
								}
								adminAlreadySigned = true;
							}
						}

						if (!adminAlreadySigned) {
							signAdmin.setUserProfileId(user.getId().toString());
							signAdmin.setFullName(user.getFullName());
							signAdmin.setSignature("");
							signAdmin.setNote("");
							signAdmin
									.setPositionTitle("University Research Administrator");
							signAdmin.setDelegated(false);
							if (!signatures.contains(signAdmin)) {
								signatures.add(signAdmin);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Director")) {
						SignatureInfo signDirector = new SignatureInfo();

						boolean directorAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"University Research Director")) {
								signDirector.setUserProfileId(signature
										.getUserProfileId());
								signDirector.setFullName(signature
										.getFullName());
								signDirector.setSignature(signature
										.getSignature());
								signDirector.setSignedDate(signature
										.getSignedDate());
								signDirector.setNote(signature.getNote());
								signDirector
										.setPositionTitle("University Research Director");
								signDirector.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signDirector)) {
									signatures.add(signDirector);
								}
								directorAlreadySigned = true;
							}
						}

						if (!directorAlreadySigned) {
							signDirector.setUserProfileId(user.getId()
									.toString());
							signDirector.setFullName(user.getFullName());
							signDirector.setSignature("");
							signDirector.setNote("");
							signDirector
									.setPositionTitle("University Research Director");
							signDirector.setDelegated(false);
							if (!signatures.contains(signDirector)) {
								signatures.add(signDirector);
							}
						}
					}
				}
			}
		}
		return signatures;
	}

	public List<SignatureUserInfo> findSignaturesExceptInvestigator(
			ObjectId id, Boolean irbApprovalRequired) {
		Datastore ds = getDatastore();
		List<SignatureUserInfo> signatures = new ArrayList<SignatureUserInfo>();
		List<CollegeDepartmentInfo> investigators = new ArrayList<CollegeDepartmentInfo>();

		Query<Proposal> q1 = ds
				.createQuery(Proposal.class)
				.field("_id")
				.equal(id)
				.retrievedFields(true, "_id", "investigator info",
						"signature info");
		Proposal proposal = q1.get();

		// Adding PI
		InvestigatorRefAndPosition PI = proposal.getInvestigatorInfo().getPi();

		CollegeDepartmentInfo investRef = new CollegeDepartmentInfo();

		if (!PI.getUserRef().isDeleted()) {
			investRef.setCollege(PI.getCollege());
			investRef.setDepartment(PI.getDepartment());
			investigators.add(investRef);
		}

		for (InvestigatorRefAndPosition coPIs : proposal.getInvestigatorInfo()
				.getCo_pi()) {
			// Adding Co-PIs
			if (!coPIs.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(coPIs.getCollege());
				investRef.setDepartment(coPIs.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}
			}
		}

		for (InvestigatorRefAndPosition seniors : proposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			// Adding Seniors
			if (!seniors.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(seniors.getCollege());
				investRef.setDepartment(seniors.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}
			}
		}
		// }
		// 2. Get all Users filter using College in<> and Department in <> and
		// Position Title equal <>
		// Department Chair
		// Business Manager
		// Dean
		// IRB
		// University Research Director
		// Research Administrator

		List<String> positions = new ArrayList<String>();
		positions.add("Department Chair");
		positions.add("Business Manager");
		positions.add("Dean");
		positions.add("University Research Administrator");
		positions.add("University Research Director");

		if (irbApprovalRequired) {
			positions.add("IRB");
		}

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "work email");

		profileQuery.and(profileQuery.criteria("deleted").equal(false),
				profileQuery.criteria("details.position title").in(positions));
		List<UserProfile> userProfile = profileQuery.asList();

		for (UserProfile user : userProfile) {
			for (PositionDetails posDetails : user.getDetails()) {
				for (CollegeDepartmentInfo colDeptInfo : investigators) {
					if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Department Chair")) {
						SignatureUserInfo signDeptChair = new SignatureUserInfo();

						signDeptChair.setUserProfileId(user.getId().toString());
						signDeptChair.setFullName(user.getFullName());
						signDeptChair.setUserName(user.getUserAccount()
								.getUserName());
						signDeptChair.setEmail(user.getWorkEmails().get(0));
						signDeptChair.setCollege(posDetails.getCollege());
						signDeptChair.setDepartment(posDetails.getDepartment());
						signDeptChair.setPositionType(posDetails
								.getPositionType());
						signDeptChair.setPositionTitle(posDetails
								.getPositionTitle());
						signDeptChair.setSignature("");
						signDeptChair.setNote("");
						signDeptChair.setPositionTitle("Department Chair");
						signDeptChair.setDelegated(false);
						if (!signatures.contains(signDeptChair)) {
							signatures.add(signDeptChair);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Business Manager")) {
						SignatureUserInfo signBusinessMgr = new SignatureUserInfo();
						signBusinessMgr.setUserProfileId(user.getId()
								.toString());
						signBusinessMgr.setFullName(user.getFullName());
						signBusinessMgr.setUserName(user.getUserAccount()
								.getUserName());
						signBusinessMgr.setEmail(user.getWorkEmails().get(0));
						signBusinessMgr.setCollege(posDetails.getCollege());
						signBusinessMgr.setDepartment(posDetails
								.getDepartment());
						signBusinessMgr.setPositionType(posDetails
								.getPositionType());
						signBusinessMgr.setPositionTitle(posDetails
								.getPositionTitle());
						signBusinessMgr.setSignature("");
						signBusinessMgr.setNote("");
						signBusinessMgr.setPositionTitle("Business Manager");
						signBusinessMgr.setDelegated(false);
						if (!signatures.contains(signBusinessMgr)) {
							signatures.add(signBusinessMgr);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Dean")) {
						SignatureUserInfo signDean = new SignatureUserInfo();
						signDean.setUserProfileId(user.getId().toString());
						signDean.setFullName(user.getFullName());
						signDean.setUserName(user.getUserAccount()
								.getUserName());
						signDean.setEmail(user.getWorkEmails().get(0));
						signDean.setCollege(posDetails.getCollege());
						signDean.setDepartment(posDetails.getDepartment());
						signDean.setPositionType(posDetails.getPositionType());
						signDean.setPositionTitle(posDetails.getPositionTitle());
						signDean.setSignature("");
						signDean.setNote("");
						signDean.setPositionTitle("Dean");
						signDean.setDelegated(false);
						if (!signatures.contains(signDean)) {
							signatures.add(signDean);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"IRB") && irbApprovalRequired) {
						SignatureUserInfo signBusinessMgr = new SignatureUserInfo();
						signBusinessMgr.setUserProfileId(user.getId()
								.toString());
						signBusinessMgr.setFullName(user.getFullName());
						signBusinessMgr.setUserName(user.getUserAccount()
								.getUserName());
						signBusinessMgr.setEmail(user.getWorkEmails().get(0));
						signBusinessMgr.setCollege(posDetails.getCollege());
						signBusinessMgr.setDepartment(posDetails
								.getDepartment());
						signBusinessMgr.setPositionType(posDetails
								.getPositionType());
						signBusinessMgr.setPositionTitle(posDetails
								.getPositionTitle());
						signBusinessMgr.setSignature("");
						signBusinessMgr.setNote("");
						signBusinessMgr.setPositionTitle("IRB");
						signBusinessMgr.setDelegated(false);
						if (!signatures.contains(signBusinessMgr)) {
							signatures.add(signBusinessMgr);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Administrator")) {
						SignatureUserInfo signAdmin = new SignatureUserInfo();
						signAdmin.setUserProfileId(user.getId().toString());
						signAdmin.setFullName(user.getFullName());
						signAdmin.setUserName(user.getUserAccount()
								.getUserName());
						signAdmin.setEmail(user.getWorkEmails().get(0));
						signAdmin.setCollege(posDetails.getCollege());
						signAdmin.setDepartment(posDetails.getDepartment());
						signAdmin.setPositionType(posDetails.getPositionType());
						signAdmin.setPositionTitle(posDetails
								.getPositionTitle());
						signAdmin.setSignature("");
						signAdmin.setNote("");
						signAdmin
								.setPositionTitle("University Research Administrator");
						signAdmin.setDelegated(false);
						if (!signatures.contains(signAdmin)) {
							signatures.add(signAdmin);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Director")) {
						SignatureUserInfo signDirector = new SignatureUserInfo();
						signDirector.setUserProfileId(user.getId().toString());
						signDirector.setFullName(user.getFullName());
						signDirector.setUserName(user.getUserAccount()
								.getUserName());
						signDirector.setEmail(user.getWorkEmails().get(0));
						signDirector.setCollege(posDetails.getCollege());
						signDirector.setDepartment(posDetails.getDepartment());
						signDirector.setPositionType(posDetails
								.getPositionType());
						signDirector.setPositionTitle(posDetails
								.getPositionTitle());
						signDirector.setSignature("");
						signDirector.setNote("");
						signDirector
								.setPositionTitle("University Research Director");
						signDirector.setDelegated(false);
						if (!signatures.contains(signDirector)) {
							signatures.add(signDirector);
						}
					}
				}
			}
		}
		return signatures;
	}

	public List<SignatureUserInfo> findUsersExceptInvestigatorForAproposal(
			ObjectId id, Boolean needPI, Boolean needCoPI, Boolean needSenior,
			Boolean needChair, Boolean needManager, Boolean needDean,
			Boolean needIrb, Boolean needResearchadmin, Boolean needDirector) {
		Datastore ds = getDatastore();
		List<SignatureUserInfo> signatures = new ArrayList<SignatureUserInfo>();
		List<CollegeDepartmentInfo> investigators = new ArrayList<CollegeDepartmentInfo>();

		Query<Proposal> q1 = ds
				.createQuery(Proposal.class)
				.field("_id")
				.equal(id)
				.retrievedFields(true, "_id", "irb approval required",
						"investigator info", "signature info");
		Proposal proposal = q1.get();

		Boolean irbApprovalRequired = proposal.isIrbApprovalRequired();

		SignatureUserInfo signUser = new SignatureUserInfo();

		// Adding PI
		InvestigatorRefAndPosition PI = proposal.getInvestigatorInfo().getPi();

		CollegeDepartmentInfo investRef = new CollegeDepartmentInfo();

		if (!PI.getUserRef().isDeleted()) {
			investRef.setCollege(PI.getCollege());
			investRef.setDepartment(PI.getDepartment());
			investigators.add(investRef);
			if (needPI) {
				signUser = new SignatureUserInfo();
				UserProfile user = PI.getUserRef();
				signUser.setUserProfileId(user.getId().toString());
				signUser.setFullName(user.getFullName());
				signUser.setUserName(user.getUserAccount().getUserName());
				signUser.setEmail(user.getWorkEmails().get(0));
				signUser.setCollege(PI.getCollege());
				signUser.setDepartment(PI.getDepartment());
				signUser.setPositionType(PI.getPositionType());
				signUser.setPositionTitle(PI.getPositionTitle());
				signUser.setSignature("");
				signUser.setNote("");
				//signUser.setPositionTitle("PI");
				signUser.setDelegated(false);
				if (!signatures.contains(signUser)) {
					signatures.add(signUser);
				}
			}
		}

		for (InvestigatorRefAndPosition coPIs : proposal.getInvestigatorInfo()
				.getCo_pi()) {
			// Adding Co-PIs
			if (!coPIs.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(coPIs.getCollege());
				investRef.setDepartment(coPIs.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}

				if (needCoPI) {
					signUser = new SignatureUserInfo();
					UserProfile user = coPIs.getUserRef();
					signUser.setUserProfileId(user.getId().toString());
					signUser.setFullName(user.getFullName());
					signUser.setUserName(user.getUserAccount().getUserName());
					signUser.setEmail(user.getWorkEmails().get(0));
					signUser.setCollege(coPIs.getCollege());
					signUser.setDepartment(coPIs.getDepartment());
					signUser.setPositionType(coPIs.getPositionType());
					signUser.setPositionTitle(coPIs.getPositionTitle());
					signUser.setSignature("");
					signUser.setNote("");
					//signUser.setPositionTitle("Co-PI");
					signUser.setDelegated(false);
					if (!signatures.contains(signUser)) {
						signatures.add(signUser);
					}
				}
			}
		}

		for (InvestigatorRefAndPosition seniors : proposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			// Adding Seniors
			if (!seniors.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(seniors.getCollege());
				investRef.setDepartment(seniors.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}

				if (needSenior) {
					signUser = new SignatureUserInfo();
					UserProfile user = seniors.getUserRef();
					signUser.setUserProfileId(user.getId().toString());
					signUser.setFullName(user.getFullName());
					signUser.setUserName(user.getUserAccount().getUserName());
					signUser.setEmail(user.getWorkEmails().get(0));
					signUser.setCollege(seniors.getCollege());
					signUser.setDepartment(seniors.getDepartment());
					signUser.setPositionType(seniors.getPositionType());
					signUser.setPositionTitle(seniors.getPositionTitle());
					signUser.setSignature("");
					signUser.setNote("");
					//signUser.setPositionTitle("Senior Personnel");
					signUser.setDelegated(false);
					if (!signatures.contains(signUser)) {
						signatures.add(signUser);
					}
				}
			}
		}
		// }
		// 2. Get all Users filter using College in<> and Department in <> and
		// Position Title equal <>
		// Department Chair
		// Business Manager
		// Dean
		// IRB
		// University Research Director
		// Research Administrator

		List<String> positions = new ArrayList<String>();
		positions.add("Department Chair");
		positions.add("Business Manager");
		positions.add("Dean");
		positions.add("University Research Administrator");
		positions.add("University Research Director");

		if (irbApprovalRequired) {
			positions.add("IRB");
		}

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "work email");

		profileQuery.and(profileQuery.criteria("deleted").equal(false),
				profileQuery.criteria("details.position title").in(positions));
		List<UserProfile> userProfile = profileQuery.asList();

		for (UserProfile user : userProfile) {
			for (PositionDetails posDetails : user.getDetails()) {
				for (CollegeDepartmentInfo colDeptInfo : investigators) {
					if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Department Chair") && needChair) {
						SignatureUserInfo signDeptChair = new SignatureUserInfo();

						signDeptChair.setUserProfileId(user.getId().toString());
						signDeptChair.setFullName(user.getFullName());
						signDeptChair.setUserName(user.getUserAccount()
								.getUserName());
						signDeptChair.setEmail(user.getWorkEmails().get(0));
						signDeptChair.setCollege(posDetails.getCollege());
						signDeptChair.setDepartment(posDetails.getDepartment());
						signDeptChair.setPositionType(posDetails
								.getPositionType());
						signDeptChair.setPositionTitle(posDetails
								.getPositionTitle());
						signDeptChair.setSignature("");
						signDeptChair.setNote("");
						//signDeptChair.setPositionTitle("Department Chair");
						signDeptChair.setDelegated(false);
						if (!signatures.contains(signDeptChair)) {
							signatures.add(signDeptChair);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Business Manager") && needManager) {
						SignatureUserInfo signBusinessMgr = new SignatureUserInfo();
						signBusinessMgr.setUserProfileId(user.getId()
								.toString());
						signBusinessMgr.setFullName(user.getFullName());
						signBusinessMgr.setUserName(user.getUserAccount()
								.getUserName());
						signBusinessMgr.setEmail(user.getWorkEmails().get(0));
						signBusinessMgr.setCollege(posDetails.getCollege());
						signBusinessMgr.setDepartment(posDetails
								.getDepartment());
						signBusinessMgr.setPositionType(posDetails
								.getPositionType());
						signBusinessMgr.setPositionTitle(posDetails
								.getPositionTitle());
						signBusinessMgr.setSignature("");
						signBusinessMgr.setNote("");
						//signBusinessMgr.setPositionTitle("Business Manager");
						signBusinessMgr.setDelegated(false);
						if (!signatures.contains(signBusinessMgr)) {
							signatures.add(signBusinessMgr);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Dean") && needDean) {
						SignatureUserInfo signDean = new SignatureUserInfo();
						signDean.setUserProfileId(user.getId().toString());
						signDean.setFullName(user.getFullName());
						signDean.setUserName(user.getUserAccount()
								.getUserName());
						signDean.setEmail(user.getWorkEmails().get(0));
						signDean.setCollege(posDetails.getCollege());
						signDean.setDepartment(posDetails.getDepartment());
						signDean.setPositionType(posDetails.getPositionType());
						signDean.setPositionTitle(posDetails.getPositionTitle());
						signDean.setSignature("");
						signDean.setNote("");
						//signDean.setPositionTitle("Dean");
						signDean.setDelegated(false);
						if (!signatures.contains(signDean)) {
							signatures.add(signDean);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"IRB") && irbApprovalRequired && needIrb) {
						SignatureUserInfo signBusinessMgr = new SignatureUserInfo();
						signBusinessMgr.setUserProfileId(user.getId()
								.toString());
						signBusinessMgr.setFullName(user.getFullName());
						signBusinessMgr.setUserName(user.getUserAccount()
								.getUserName());
						signBusinessMgr.setEmail(user.getWorkEmails().get(0));
						signBusinessMgr.setCollege(posDetails.getCollege());
						signBusinessMgr.setDepartment(posDetails
								.getDepartment());
						signBusinessMgr.setPositionType(posDetails
								.getPositionType());
						signBusinessMgr.setPositionTitle(posDetails
								.getPositionTitle());
						signBusinessMgr.setSignature("");
						signBusinessMgr.setNote("");
						//signBusinessMgr.setPositionTitle("IRB");
						signBusinessMgr.setDelegated(false);
						if (!signatures.contains(signBusinessMgr)) {
							signatures.add(signBusinessMgr);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Administrator")
							&& needResearchadmin) {
						SignatureUserInfo signAdmin = new SignatureUserInfo();
						signAdmin.setUserProfileId(user.getId().toString());
						signAdmin.setFullName(user.getFullName());
						signAdmin.setUserName(user.getUserAccount()
								.getUserName());
						signAdmin.setEmail(user.getWorkEmails().get(0));
						signAdmin.setCollege(posDetails.getCollege());
						signAdmin.setDepartment(posDetails.getDepartment());
						signAdmin.setPositionType(posDetails.getPositionType());
						signAdmin.setPositionTitle(posDetails
								.getPositionTitle());
						signAdmin.setSignature("");
						signAdmin.setNote("");
						// signAdmin
						// .setPositionTitle("University Research Administrator");
						signAdmin.setDelegated(false);
						if (!signatures.contains(signAdmin)) {
							signatures.add(signAdmin);
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Director")
							&& needDirector) {
						SignatureUserInfo signDirector = new SignatureUserInfo();
						signDirector.setUserProfileId(user.getId().toString());
						signDirector.setFullName(user.getFullName());
						signDirector.setUserName(user.getUserAccount()
								.getUserName());
						signDirector.setEmail(user.getWorkEmails().get(0));
						signDirector.setCollege(posDetails.getCollege());
						signDirector.setDepartment(posDetails.getDepartment());
						signDirector.setPositionType(posDetails
								.getPositionType());
						signDirector.setPositionTitle(posDetails
								.getPositionTitle());
						signDirector.setSignature("");
						signDirector.setNote("");
						// signDirector
						// .setPositionTitle("University Research Director");
						signDirector.setDelegated(false);
						if (!signatures.contains(signDirector)) {
							signatures.add(signDirector);
						}
					}
				}
			}
		}
		return signatures;
	}

	public List<SignatureInfo> findUsersInProposal(ObjectId id,
			boolean irbApprovalRequired) {

		Datastore ds = getDatastore();
		List<SignatureInfo> signatures = new ArrayList<SignatureInfo>();
		List<CollegeDepartmentInfo> investigators = new ArrayList<CollegeDepartmentInfo>();

		Query<Proposal> q1 = ds
				.createQuery(Proposal.class)
				.field("_id")
				.equal(id)
				.retrievedFields(true, "_id", "irb approval required",
						"investigator info", "signature info");
		Proposal proposal = q1.get();

		// Adding PI
		SignatureInfo piSign = new SignatureInfo();
		InvestigatorRefAndPosition PI = proposal.getInvestigatorInfo().getPi();

		boolean piAlreadySigned = false;
		for (SignatureInfo signature : proposal.getSignatureInfo()) {
			if (PI.getUserRef().getId().toString()
					.equals(signature.getUserProfileId())
					&& !PI.getUserRef().isDeleted()
					&& signature.getPositionTitle().equals("PI")) {
				piSign.setUserProfileId(signature.getUserProfileId());
				piSign.setFullName(signature.getFullName());
				// piSign.setEmail(PI.getUserRef().getWorkEmails().get(0));
				piSign.setSignature(signature.getSignature());
				piSign.setSignedDate(signature.getSignedDate());
				piSign.setNote(signature.getNote());
				piSign.setPositionTitle(signature.getPositionTitle());
				piSign.setDelegated(signature.isDelegated());
				boolean piAlreadyExist = false;
				for (SignatureInfo sign : signatures) {
					if (sign.getUserProfileId().equalsIgnoreCase(
							piSign.getUserProfileId())) {
						piAlreadyExist = true;
						break;
					}
				}
				if (!piAlreadyExist) {
					signatures.add(piSign);
				}
				piAlreadySigned = true;
			}
		}

		if (!piAlreadySigned && !PI.getUserRef().isDeleted()) {
			piSign.setUserProfileId(PI.getUserRef().getId().toString());
			piSign.setFullName(PI.getUserRef().getFullName());
			// piSign.setEmail(PI.getUserRef().getWorkEmails().get(0));
			piSign.setSignature("");
			piSign.setNote("");
			piSign.setPositionTitle("PI");
			piSign.setDelegated(false);
			boolean piAlreadyExist = false;
			for (SignatureInfo sign : signatures) {
				if (sign.getUserProfileId().equalsIgnoreCase(
						piSign.getUserProfileId())) {
					piAlreadyExist = true;
					break;
				}
			}
			if (!piAlreadyExist) {
				signatures.add(piSign);
			}
		}

		CollegeDepartmentInfo investRef = new CollegeDepartmentInfo();
		if (!PI.getUserRef().isDeleted()) {
			investRef.setCollege(PI.getCollege());
			investRef.setDepartment(PI.getDepartment());
			investigators.add(investRef);
		}

		for (InvestigatorRefAndPosition coPIs : proposal.getInvestigatorInfo()
				.getCo_pi()) {
			// Adding Co-PIs
			SignatureInfo coPISign = new SignatureInfo();

			boolean coPIAlreadySigned = false;
			for (SignatureInfo signature : proposal.getSignatureInfo()) {
				if (coPIs.getUserRef().getId().toString()
						.equals(signature.getUserProfileId())
						&& !coPIs.getUserRef().isDeleted()
						&& signature.getPositionTitle().equals("Co-PI")) {
					coPISign.setUserProfileId(signature.getUserProfileId());
					coPISign.setFullName(signature.getFullName());
					// coPISign.setEmail(coPIs.getUserRef().getWorkEmails().get(0));
					coPISign.setSignature(signature.getSignature());
					coPISign.setSignedDate(signature.getSignedDate());
					coPISign.setNote(signature.getNote());
					coPISign.setPositionTitle(signature.getPositionTitle());
					coPISign.setDelegated(signature.isDelegated());
					boolean coPIAlreadyExist = false;
					for (SignatureInfo sign : signatures) {
						if (sign.getUserProfileId().equalsIgnoreCase(
								coPISign.getUserProfileId())) {
							coPIAlreadyExist = true;
							break;
						}
					}
					if (!coPIAlreadyExist) {
						signatures.add(coPISign);
					}
					coPIAlreadySigned = true;
				}
			}

			if (!coPIAlreadySigned && !coPIs.getUserRef().isDeleted()) {
				coPISign.setUserProfileId(coPIs.getUserRef().getId().toString());
				coPISign.setFullName(coPIs.getUserRef().getFullName());
				// coPISign.setEmail(coPIs.getUserRef().getWorkEmails().get(0));
				coPISign.setSignature("");
				coPISign.setNote("");
				coPISign.setPositionTitle("Co-PI");
				coPISign.setDelegated(false);
				boolean coPIAlreadyExist = false;
				for (SignatureInfo sign : signatures) {
					if (sign.getUserProfileId().equalsIgnoreCase(
							coPISign.getUserProfileId())) {
						coPIAlreadyExist = true;
						break;
					}
				}
				if (!coPIAlreadyExist) {
					signatures.add(coPISign);
				}
			}

			if (!coPIs.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(coPIs.getCollege());
				investRef.setDepartment(coPIs.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}
			}
		}

		for (InvestigatorRefAndPosition seniors : proposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			// Adding Seniors
			SignatureInfo seniorSign = new SignatureInfo();

			boolean seniorAlreadySigned = false;
			for (SignatureInfo signature : proposal.getSignatureInfo()) {
				if (seniors.getUserRef().getId().toString()
						.equals(signature.getUserProfileId())
						&& !seniors.getUserRef().isDeleted()
						&& signature.getPositionTitle().equals(
								"Senior Personnel")) {
					seniorSign.setUserProfileId(signature.getUserProfileId());
					seniorSign.setFullName(signature.getFullName());
					// seniorSign.setEmail(seniors.getUserRef().getWorkEmails().get(0));
					seniorSign.setSignature(signature.getSignature());
					seniorSign.setSignedDate(signature.getSignedDate());
					seniorSign.setNote(signature.getNote());
					seniorSign.setPositionTitle(signature.getPositionTitle());
					seniorSign.setDelegated(signature.isDelegated());
					if (!signatures.contains(seniorSign)) {
						signatures.add(seniorSign);
					}
					boolean seniorAlreadyExist = false;
					for (SignatureInfo sign : signatures) {
						if (sign.getUserProfileId().equalsIgnoreCase(
								seniorSign.getUserProfileId())) {
							seniorAlreadyExist = true;
							break;
						}
					}
					if (!seniorAlreadyExist) {
						signatures.add(seniorSign);
					}
					seniorAlreadySigned = true;
				}
			}

			if (!seniorAlreadySigned && !seniors.getUserRef().isDeleted()) {
				seniorSign.setUserProfileId(seniors.getUserRef().getId()
						.toString());
				seniorSign.setFullName(seniors.getUserRef().getFullName());
				// seniorSign
				// .setEmail(seniors.getUserRef().getWorkEmails().get(0));
				seniorSign.setSignature("");
				seniorSign.setNote("");
				seniorSign.setPositionTitle("Senior Personnel");
				seniorSign.setDelegated(false);
				boolean seniorAlreadyExist = false;
				for (SignatureInfo sign : signatures) {
					if (sign.getUserProfileId().equalsIgnoreCase(
							seniorSign.getUserProfileId())) {
						seniorAlreadyExist = true;
						break;
					}
				}
				if (!seniorAlreadyExist) {
					signatures.add(seniorSign);
				}
			}

			if (!seniors.getUserRef().isDeleted()) {
				investRef = new CollegeDepartmentInfo();
				investRef.setCollege(seniors.getCollege());
				investRef.setDepartment(seniors.getDepartment());
				if (!investigators.contains(investRef)) {
					investigators.add(investRef);
				}
			}
		}

		// 2. Get all Users filter using College in<> and Department in <> and
		// Position Title equal <>
		// Department Chair
		// Business Manager
		// IRB
		// Dean
		// University Research Director
		// Research Administrator
		List<String> positions = new ArrayList<String>();
		positions.add("Department Chair");
		positions.add("Business Manager");
		positions.add("Dean");
		positions.add("University Research Administrator");
		positions.add("University Research Director");

		if (irbApprovalRequired) {
			positions.add("IRB");
		}

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "work email");

		profileQuery.and(profileQuery.criteria("deleted").equal(false),
				profileQuery.criteria("details.position title").in(positions));
		List<UserProfile> userProfile = profileQuery.asList();

		for (UserProfile user : userProfile) {
			for (PositionDetails posDetails : user.getDetails()) {
				for (CollegeDepartmentInfo colDeptInfo : investigators) {
					if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Department Chair")) {
						SignatureInfo signDeptChair = new SignatureInfo();

						boolean departmentChairAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Department Chair")) {
								signDeptChair.setUserProfileId(signature
										.getUserProfileId());
								signDeptChair.setFullName(signature
										.getFullName());
								// signDeptChair.setEmail(user.getWorkEmails()
								// .get(0));
								signDeptChair.setSignature(signature
										.getSignature());
								signDeptChair.setSignedDate(signature
										.getSignedDate());
								signDeptChair.setNote(signature.getNote());
								signDeptChair.setPositionTitle(signature
										.getPositionTitle());
								signDeptChair.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signDeptChair)) {
									signatures.add(signDeptChair);
								}
								departmentChairAlreadySigned = true;
							}
						}

						if (!departmentChairAlreadySigned) {
							signDeptChair.setUserProfileId(user.getId()
									.toString());
							signDeptChair.setFullName(user.getFullName());
							// signDeptChair.setEmail(user.getWorkEmails().get(0));
							signDeptChair.setSignature("");
							signDeptChair.setNote("");
							signDeptChair.setPositionTitle("Department Chair");
							signDeptChair.setDelegated(false);
							if (!signatures.contains(signDeptChair)) {
								signatures.add(signDeptChair);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Business Manager")) {
						SignatureInfo signBusinessMgr = new SignatureInfo();

						boolean businessManagerAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Business Manager")) {
								signBusinessMgr.setUserProfileId(signature
										.getUserProfileId());
								signBusinessMgr.setFullName(signature
										.getFullName());
								// signBusinessMgr.setEmail(user.getWorkEmails()
								// .get(0));
								signBusinessMgr.setSignature(signature
										.getSignature());
								signBusinessMgr.setSignedDate(signature
										.getSignedDate());
								signBusinessMgr.setNote(signature.getNote());
								signBusinessMgr.setPositionTitle(signature
										.getPositionTitle());
								signBusinessMgr.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signBusinessMgr)) {
									signatures.add(signBusinessMgr);
								}
								businessManagerAlreadySigned = true;
							}
						}

						if (!businessManagerAlreadySigned) {
							signBusinessMgr.setUserProfileId(user.getId()
									.toString());
							signBusinessMgr.setFullName(user.getFullName());
							// signBusinessMgr.setEmail(user.getWorkEmails()
							// .get(0));
							signBusinessMgr.setSignature("");
							signBusinessMgr.setNote("");
							signBusinessMgr
									.setPositionTitle("Business Manager");
							signBusinessMgr.setDelegated(false);
							if (!signatures.contains(signBusinessMgr)) {
								signatures.add(signBusinessMgr);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"Dean")) {
						SignatureInfo signDean = new SignatureInfo();

						boolean deanAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"Dean")) {
								signDean.setUserProfileId(signature
										.getUserProfileId());
								signDean.setFullName(signature.getFullName());
								// signDean.setEmail(user.getWorkEmails().get(0));
								signDean.setSignature(signature.getSignature());
								signDean.setSignedDate(signature
										.getSignedDate());
								signDean.setNote(signature.getNote());
								signDean.setPositionTitle(signature
										.getPositionTitle());
								signDean.setDelegated(signature.isDelegated());
								if (!signatures.contains(signDean)) {
									signatures.add(signDean);
								}
								deanAlreadySigned = true;
							}
						}

						if (!deanAlreadySigned) {
							signDean.setUserProfileId(user.getId().toString());
							signDean.setFullName(user.getFullName());
							// signDean.setEmail(user.getWorkEmails().get(0));
							signDean.setSignature("");
							signDean.setNote("");
							signDean.setPositionTitle("Dean");
							signDean.setDelegated(false);
							if (!signatures.contains(signDean)) {
								signatures.add(signDean);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"IRB") && irbApprovalRequired) {
						SignatureInfo signBusinessMgr = new SignatureInfo();

						boolean irbAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"IRB")) {
								signBusinessMgr.setUserProfileId(signature
										.getUserProfileId());
								signBusinessMgr.setFullName(signature
										.getFullName());
								// signBusinessMgr.setEmail(user.getWorkEmails()
								// .get(0));
								signBusinessMgr.setSignature(signature
										.getSignature());
								signBusinessMgr.setSignedDate(signature
										.getSignedDate());
								signBusinessMgr.setNote(signature.getNote());
								signBusinessMgr.setPositionTitle(signature
										.getPositionTitle());
								signBusinessMgr.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signBusinessMgr)) {
									signatures.add(signBusinessMgr);
								}
								irbAlreadySigned = true;
							}
						}

						if (!irbAlreadySigned) {
							signBusinessMgr.setUserProfileId(user.getId()
									.toString());
							signBusinessMgr.setFullName(user.getFullName());
							// signBusinessMgr.setEmail(user.getWorkEmails()
							// .get(0));
							signBusinessMgr.setSignature("");
							signBusinessMgr.setNote("");
							signBusinessMgr.setPositionTitle("IRB");
							signBusinessMgr.setDelegated(false);
							if (!signatures.contains(signBusinessMgr)) {
								signatures.add(signBusinessMgr);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Administrator")) {
						SignatureInfo signAdmin = new SignatureInfo();

						boolean adminAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature
											.getPositionTitle()
											.equals("University Research Administrator")) {
								signAdmin.setUserProfileId(signature
										.getUserProfileId());
								signAdmin.setFullName(user.getFullName());
								// signAdmin.setEmail(user.getWorkEmails().get(0));
								signAdmin
										.setSignature(signature.getSignature());
								signAdmin.setSignedDate(signature
										.getSignedDate());
								signAdmin.setNote(signature.getNote());
								signAdmin
										.setPositionTitle("University Research Administrator");
								signAdmin.setDelegated(signature.isDelegated());
								if (!signatures.contains(signAdmin)) {
									signatures.add(signAdmin);
								}
								adminAlreadySigned = true;
							}
						}

						if (!adminAlreadySigned) {
							signAdmin.setUserProfileId(user.getId().toString());
							signAdmin.setFullName(user.getFullName());
							// signAdmin.setEmail(user.getWorkEmails().get(0));
							signAdmin.setSignature("");
							signAdmin.setNote("");
							signAdmin
									.setPositionTitle("University Research Administrator");
							signAdmin.setDelegated(false);
							if (!signatures.contains(signAdmin)) {
								signatures.add(signAdmin);
							}
						}
					} else if (posDetails.getCollege().equalsIgnoreCase(
							colDeptInfo.getCollege())
							&& posDetails.getDepartment().equalsIgnoreCase(
									colDeptInfo.getDepartment())
							&& posDetails.getPositionTitle().equalsIgnoreCase(
									"University Research Director")) {
						SignatureInfo signDirector = new SignatureInfo();

						boolean directorAlreadySigned = false;
						for (SignatureInfo signature : proposal
								.getSignatureInfo()) {
							if (user.getId().toString()
									.equals(signature.getUserProfileId())
									&& signature.getPositionTitle().equals(
											"University Research Director")) {
								signDirector.setUserProfileId(signature
										.getUserProfileId());
								signDirector.setFullName(signature
										.getFullName());
								// signDirector.setEmail(user.getWorkEmails().get(
								// 0));
								signDirector.setSignature(signature
										.getSignature());
								signDirector.setSignedDate(signature
										.getSignedDate());
								signDirector.setNote(signature.getNote());
								signDirector
										.setPositionTitle("University Research Director");
								signDirector.setDelegated(signature
										.isDelegated());
								if (!signatures.contains(signDirector)) {
									signatures.add(signDirector);
								}
								directorAlreadySigned = true;
							}
						}

						if (!directorAlreadySigned) {
							signDirector.setUserProfileId(user.getId()
									.toString());
							signDirector.setFullName(user.getFullName());
							// signDirector.setEmail(user.getWorkEmails().get(0));
							signDirector.setSignature("");
							signDirector.setNote("");
							signDirector
									.setPositionTitle("University Research Director");
							signDirector.setDelegated(false);
							if (!signatures.contains(signDirector)) {
								signatures.add(signDirector);
							}
						}
					}
				}
			}
		}
		return signatures;
	}

}
