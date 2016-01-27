package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.PositionDetails;
import gpms.model.ProjectLocation;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.ProposalInfo;
import gpms.model.SignatureInfo;
import gpms.model.Status;
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
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.CriteriaContainer;
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

	public Proposal findProposalByProposalID(ObjectId id)
			throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).field("_id").equal(id).get();
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

	public void deleteProposal(Proposal proposal, String proposalRoles,
			String proposalUserTitle, UserProfile authorProfile) {
		Datastore ds = getDatastore();
		// TODO
		if (proposalRoles.equalsIgnoreCase("PI")
				&& !proposal.getProposalStatus().contains(Status.DELETEDBYPI)) {
			proposal.getProposalStatus().clear();
			proposal.getProposalStatus().add(Status.DELETEDBYPI);
		}
		AuditLog entry = new AuditLog(authorProfile, "Deleted Proposal by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		proposal.getAuditLog().add(entry);
		ds.save(proposal);
	}

	public List<AuditLogInfo> findAllForProposalAuditLogGrid(int offset,
			int limit, ObjectId id, String action, String auditedBy,
			String activityOnFrom, String activityOnTo) throws ParseException {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		Proposal q = proposalQuery.field("_id").equal(id).get();

		ArrayList<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();
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

	public Proposal findNextProposalWithSameProjectTitle(ObjectId id,
			String newProjectTitle) {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		Pattern pattern = Pattern.compile("^" + newProjectTitle + "$",
				Pattern.CASE_INSENSITIVE);

		proposalQuery.and(proposalQuery.criteria("_id").notEqual(id),
				proposalQuery.criteria("project info.project title")
						.containsIgnoreCase(pattern.pattern()));
		return proposalQuery.get();
	}

	public Proposal findAnyProposalWithSameProjectTitle(String newProjectTitle) {
		Datastore ds = getDatastore();

		Query<Proposal> proposalQuery = ds.createQuery(Proposal.class);

		Pattern pattern = Pattern.compile("^" + newProjectTitle + "$",
				Pattern.CASE_INSENSITIVE);

		proposalQuery.criteria("project info.project title")
				.containsIgnoreCase(pattern.pattern());
		return proposalQuery.get();
	}

	public int findLatestProposalNo() {
		Datastore ds = getDatastore();
		List<Proposal> q1 = ds.createQuery(Proposal.class)
				.retrievedFields(true, "proposal no").asList();
		if (q1.size() == 0) {
			return 0;
		} else {
			return q1.get(q1.size() - 1).getProposalNo();
		}
	}

	public List<ProposalInfo> findAllForProposalGrid(int offset, int limit,
			String projectTitle, String usernameBy, String submittedOnFrom,
			String submittedOnTo, Double totalCostsFrom, Double totalCostsTo,
			String proposalStatus, String userRole) throws ParseException {
		Datastore ds = getDatastore();
		ArrayList<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

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

		// TODO for Date Submitted

		if (totalCostsFrom != null && totalCostsFrom != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.greaterThanOrEq(totalCostsFrom);
		}
		if (totalCostsTo != null && totalCostsTo != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.lessThanOrEq(totalCostsTo);
		}

		// TODO
		if (proposalStatus != null) {
			proposalQuery.field("proposal status").contains(proposalStatus);
		}

		if (usernameBy != null) {
			// accountQuery.field("username").containsIgnoreCase(usernameBy);
			// profileQuery.criteria("user id").in(accountQuery.asKeyList());
			// proposalQuery.criteria("investigator info.PI.user profile").in(
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
					proposalQuery.criteria("investigator info.PI.user profile")
							.in(profileQuery.asKeyList());
					break;
				case "CO-PI":
					proposalQuery.criteria(
							"investigator info.CO-PI.user profile").in(
							profileQuery.asKeyList());
					break;

				case "Senior":
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
								"investigator info.PI.user profile").in(
								profileQuery.asKeyList()),
								proposalQuery.criteria(
										"investigator info.CO-PI.user profile")
										.in(profileQuery.asKeyList()),
								proposalQuery
										.criteria(
												"investigator info.senior personnel.user profile")
										.in(profileQuery.asKeyList()));
			}
		}

		int rowTotal = proposalQuery.asList().size();
		List<Proposal> allProposals = proposalQuery.offset(offset - 1)
				.limit(limit).asList();

		for (Proposal userProposal : allProposals) {
			ProposalInfo proposal = new ProposalInfo();

			// Proposal
			proposal.setRowTotal(rowTotal);
			proposal.setId(userProposal.getId().toString());
			proposal.setProposalNo(userProposal.getProposalNo());

			// ProjectInfo
			proposal.setProjectTitle(userProposal.getProjectInfo()
					.getProjectTitle());

			ProjectType pt = userProposal.getProjectInfo().getProjectType();
			if (pt.getIsResearchBasic()) {
				proposal.setProjectType("Research-basic");
			} else if (pt.getIsResearchApplied()) {
				proposal.setProjectType("Research-applied");
			} else if (pt.getIsResearchDevelopment()) {
				proposal.setProjectType("Research-development");
			} else if (pt.getIsInstruction()) {
				proposal.setProjectType("Instruction");
			} else if (pt.getIsOtherSponsoredActivity()) {
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
					.getFACosts());
			proposal.setTotalCosts(userProposal.getSponsorAndBudgetInfo()
					.getTotalCosts());
			proposal.setFaRate(userProposal.getSponsorAndBudgetInfo()
					.getFARate());

			proposal.setDateCreated(userProposal.getDateCreated());
			proposal.setDateSubmitted(userProposal.getDateSubmitted());

			proposal.setDueDate(userProposal.getProjectInfo().getDueDate());
			proposal.setProjectPeriodFrom(userProposal.getProjectInfo()
					.getProjectPeriod().getFrom());
			proposal.setProjectPeriodTo(userProposal.getProjectInfo()
					.getProjectPeriod().getTo());

			for (Status status : userProposal.getProposalStatus()) {
				proposal.getProposalStatus().add(status.toString());
			}

			// TODO
			if (userProposal.getProposalStatus().equals(Status.DELETEDBYPI)) {
				proposal.setDeleted(true);
			}

			ArrayList<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();

			if (userProposal.getAuditLog() != null
					&& userProposal.getAuditLog().size() != 0) {
				for (AuditLog userProfileAudit : userProposal.getAuditLog()) {
					AuditLogInfo userAuditLog = new AuditLogInfo();
					userAuditLog.setActivityDate(userProfileAudit
							.getActivityDate());
					userAuditLog.setUserFullName(userProfileAudit
							.getUserProfile().getFullName());
					userAuditLog.setAction(userProfileAudit.getAction());

					allAuditLogs.add(userAuditLog);
				}
			}
			Collections.sort(allAuditLogs);

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();
			if (allAuditLogs.size() > 0) {
				AuditLogInfo auditLog = allAuditLogs.get(0);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserFullName();
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
		Collections.sort(proposals);
		return proposals;
	}

	public List<ProposalInfo> findUserProposalGrid(int offset, int limit,
			String projectTitle, String usernameBy, String submittedOnFrom,
			String submittedOnTo, Double totalCostsFrom, Double totalCostsTo,
			String proposalStatus, String userRole, String userId,
			String college, String department, String positionType,
			String positionTitle) throws ParseException {
		Datastore ds = getDatastore();
		ArrayList<ProposalInfo> proposals = new ArrayList<ProposalInfo>();

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

		// TODO for Date Submitted

		if (totalCostsFrom != null && totalCostsFrom != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.greaterThanOrEq(totalCostsFrom);
		}
		if (totalCostsTo != null && totalCostsTo != 0.0) {
			proposalQuery.field("sponsor and budget info.total costs")
					.lessThanOrEq(totalCostsTo);
		}

		// TODO
		if (proposalStatus != null) {
			proposalQuery.field("proposal status").contains(proposalStatus);
		}

		// High Level Users University Level --> University Research
		// Administrator,Research Administrator,University Research Director
		// College Level --> Dean,Associate Dean
		// Department Level --> Business Manager,Department Administrative
		// Assistant,Department Chair,Associate Chair

		if (!positionTitle.equals("University Research Administrator")
				&& !positionTitle.equals("Research Administrator")
				&& !positionTitle.equals("University Research Director")) {
			if (positionTitle.equals("Dean")
					|| positionTitle.equals("Associate Dean")) {
				proposalQuery.or(
						proposalQuery.criteria("investigator info.PI.college")
								.equal(college),
						proposalQuery.criteria(
								"investigator info.CO-PI.college").equal(
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
										"investigator info.PI.college").equal(
										college),
										proposalQuery
												.criteria(
														"investigator info.CO-PI.college")
												.equal(college),
										proposalQuery
												.criteria(
														"investigator info.senior personnel.college")
												.equal(college)),
								proposalQuery
										.or(proposalQuery
												.criteria(
														"investigator info.PI.department")
												.equal(department),
												proposalQuery
														.criteria(
																"investigator info.CO-PI.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.senior personnel.department")
														.equal(department)));
			} else {
				proposalQuery
						.or(proposalQuery.and(
								proposalQuery.criteria(
										"investigator info.PI.user profile id")
										.equal(userId),
								proposalQuery.criteria(
										"investigator info.PI.college").equal(
										college),
								proposalQuery.criteria(
										"investigator info.PI.department")
										.equal(department),
								proposalQuery.criteria(
										"investigator info.PI.position type")
										.equal(positionType),
								proposalQuery.criteria(
										"investigator info.PI.position title")
										.equal(positionTitle)),
								proposalQuery
										.and(proposalQuery
												.criteria(
														"investigator info.CO-PI.user profile id")
												.equal(userId),
												proposalQuery
														.criteria(
																"investigator info.CO-PI.college")
														.equal(college),
												proposalQuery
														.criteria(
																"investigator info.CO-PI.department")
														.equal(department),
												proposalQuery
														.criteria(
																"investigator info.CO-PI.position type")
														.equal(positionType),
												proposalQuery
														.criteria(
																"investigator info.CO-PI.position title")
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
					proposalQuery.criteria("investigator info.PI.user profile")
							.in(profileQuery.asKeyList());
					break;
				case "CO-PI":
					proposalQuery.criteria(
							"investigator info.CO-PI.user profile").in(
							profileQuery.asKeyList());
					break;

				case "Senior":
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
								"investigator info.PI.user profile").in(
								profileQuery.asKeyList()),
								proposalQuery.criteria(
										"investigator info.CO-PI.user profile")
										.in(profileQuery.asKeyList()),
								proposalQuery
										.criteria(
												"investigator info.senior personnel.user profile")
										.in(profileQuery.asKeyList()));
			}
		} else if (usernameBy == null && userRole != null) {
			switch (userRole) {
			case "PI":
				proposalQuery.criteria("investigator info.PI.user profile id")
						.equal(userId);
				break;
			case "CO-PI":
				proposalQuery.criteria(
						"investigator info.CO-PI.user profile id")
						.equal(userId);
				break;

			case "Senior":
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
				.limit(limit).asList();

		for (Proposal userProposal : allProposals) {
			ProposalInfo proposal = new ProposalInfo();

			// Proposal
			proposal.setRowTotal(rowTotal);
			proposal.setId(userProposal.getId().toString());
			proposal.setProposalNo(userProposal.getProposalNo());

			// ProjectInfo
			proposal.setProjectTitle(userProposal.getProjectInfo()
					.getProjectTitle());

			ProjectType pt = userProposal.getProjectInfo().getProjectType();
			if (pt.getIsResearchBasic()) {
				proposal.setProjectType("Research-basic");
			} else if (pt.getIsResearchApplied()) {
				proposal.setProjectType("Research-applied");
			} else if (pt.getIsResearchDevelopment()) {
				proposal.setProjectType("Research-development");
			} else if (pt.getIsInstruction()) {
				proposal.setProjectType("Instruction");
			} else if (pt.getIsOtherSponsoredActivity()) {
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
					.getFACosts());
			proposal.setTotalCosts(userProposal.getSponsorAndBudgetInfo()
					.getTotalCosts());
			proposal.setFaRate(userProposal.getSponsorAndBudgetInfo()
					.getFARate());

			proposal.setDateCreated(userProposal.getDateCreated());
			proposal.setDateSubmitted(userProposal.getDateSubmitted());

			proposal.setDueDate(userProposal.getProjectInfo().getDueDate());
			proposal.setProjectPeriodFrom(userProposal.getProjectInfo()
					.getProjectPeriod().getFrom());
			proposal.setProjectPeriodTo(userProposal.getProjectInfo()
					.getProjectPeriod().getTo());

			for (Status status : userProposal.getProposalStatus()) {
				proposal.getProposalStatus().add(status.toString());
			}

			// TODO
			if (userProposal.getProposalStatus().equals(Status.DELETEDBYPI)) {
				proposal.setDeleted(true);
			}

			ArrayList<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();

			if (userProposal.getAuditLog() != null
					&& userProposal.getAuditLog().size() != 0) {
				for (AuditLog userProfileAudit : userProposal.getAuditLog()) {
					AuditLogInfo userAuditLog = new AuditLogInfo();
					userAuditLog.setActivityDate(userProfileAudit
							.getActivityDate());
					userAuditLog.setUserFullName(userProfileAudit
							.getUserProfile().getFullName());
					userAuditLog.setAction(userProfileAudit.getAction());

					allAuditLogs.add(userAuditLog);
				}
			}
			Collections.sort(allAuditLogs);

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();
			if (allAuditLogs.size() > 0) {
				AuditLogInfo auditLog = allAuditLogs.get(0);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserFullName();
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
					proposal.getCurrentuserProposalRoles().add("CO-PI");
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

				// TODO Bind Proposal Roles for the User
				if (senior.getUserProfileId().equals(userId)) {
					proposal.getCurrentuserProposalRoles().add("Senior");
				}
			}

			proposals.add(proposal);
		}
		Collections.sort(proposals);
		return proposals;
	}

	public List<SignatureInfo> findAllSignatureForAProposal(ObjectId id)
			throws ParseException {
		Datastore ds = getDatastore();
		List<SignatureInfo> signatures = new ArrayList<SignatureInfo>();
		List<String> colleges = new ArrayList<String>();
		List<String> departments = new ArrayList<String>();

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

		if (!piAlreadySigned) {
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

		if (!colleges.contains(PI.getCollege())) {
			colleges.add(PI.getCollege());
		}
		if (!departments.contains(PI.getDepartment())) {
			departments.add(PI.getDepartment());
		}

		for (InvestigatorRefAndPosition coPIs : proposal.getInvestigatorInfo()
				.getCo_pi()) {
			// Adding Co-PIs
			SignatureInfo coPISign = new SignatureInfo();

			boolean coPIAlreadySigned = false;
			for (SignatureInfo signature : proposal.getSignatureInfo()) {
				if (coPIs.getUserRef().getId().toString()
						.equals(signature.getUserProfileId())
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

			if (!coPIAlreadySigned) {
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

			if (!colleges.contains(coPIs.getCollege())) {
				colleges.add(coPIs.getCollege());
			}
			if (!departments.contains(coPIs.getDepartment())) {
				departments.add(coPIs.getDepartment());
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
						&& signature.getPositionTitle().equals("Senior")) {
					seniorSign.setUserProfileId(signature.getUserProfileId());
					seniorSign.setFullName(signature.getFullName());
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

			if (!seniorAlreadySigned) {
				seniorSign.setUserProfileId(seniors.getUserRef().getId()
						.toString());
				seniorSign.setFullName(seniors.getUserRef().getFullName());
				seniorSign.setSignature("");
				seniorSign.setNote("");
				seniorSign.setPositionTitle("Senior");
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

			if (!colleges.contains(seniors.getCollege())) {
				colleges.add(seniors.getCollege());
			}
			if (!departments.contains(seniors.getDepartment())) {
				departments.add(seniors.getDepartment());
			}
		}
		// }
		// 2. Get all Users filter using College in<> and Department in <> and
		// Position Title equal <>
		// Department Chair
		// Business Manager
		// Dean
		// University Research Director
		// Research Administrator

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		List<String> positions = new ArrayList<String>();
		// positions.add("Department Chair");
		positions.add("Business Manager");
		positions.add("Dean");
		positions.add("Research Administrator");
		positions.add("University Research Director");

		final CriteriaContainer container = profileQuery.or();
		if (colleges != null && !colleges.isEmpty()) {
			container.add(container.and(
					profileQuery.criteria("details.position title").in(
							positions), profileQuery
							.criteria("details.college").in(colleges)));
			if (departments != null && !departments.isEmpty()) {
				container.add(container.and(
						profileQuery.criteria("details.position title").equal(
								"Department Chair"),
						profileQuery.criteria("details.department").in(
								departments)));
			}
		}

		List<UserProfile> userProfile = profileQuery.asList();

		for (UserProfile user : userProfile) {
			for (PositionDetails posDetails : user.getDetails()) {
				if (posDetails.getPositionTitle().equalsIgnoreCase(
						"Research Administrator")) {
					SignatureInfo signAdmin = new SignatureInfo();

					boolean adminAlreadySigned = false;
					for (SignatureInfo signature : proposal.getSignatureInfo()) {
						if (user.getId().toString()
								.equals(signature.getUserProfileId())
								&& signature.getPositionTitle().equals(
										"Research Administrator")) {
							signAdmin.setUserProfileId(signature
									.getUserProfileId());
							signAdmin.setFullName(signature.getFullName());
							signAdmin.setSignature(signature.getSignature());
							signAdmin.setSignedDate(signature.getSignedDate());
							signAdmin.setNote(signature.getNote());
							signAdmin
									.setPositionTitle("Research Administrator");
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
						signAdmin.setPositionTitle("Research Administrator");
						signAdmin.setDelegated(false);
						if (!signatures.contains(signAdmin)) {
							signatures.add(signAdmin);
						}
					}
				} else if (posDetails.getPositionTitle().equalsIgnoreCase(
						"University Research Director")) {
					SignatureInfo signDirector = new SignatureInfo();

					boolean directorAlreadySigned = false;
					for (SignatureInfo signature : proposal.getSignatureInfo()) {
						if (user.getId().toString()
								.equals(signature.getUserProfileId())
								&& signature.getPositionTitle().equals(
										"University Research Director")) {
							signDirector.setUserProfileId(signature
									.getUserProfileId());
							signDirector.setFullName(signature.getFullName());
							signDirector.setSignature(signature.getSignature());
							signDirector.setSignedDate(signature
									.getSignedDate());
							signDirector.setNote(signature.getNote());
							signDirector
									.setPositionTitle("University Research Director");
							signDirector.setDelegated(signature.isDelegated());
							if (!signatures.contains(signDirector)) {
								signatures.add(signDirector);
							}
							directorAlreadySigned = true;
						}
					}

					if (!directorAlreadySigned) {
						signDirector.setUserProfileId(user.getId().toString());
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
				} else if (posDetails.getPositionTitle().equalsIgnoreCase(
						"Dean")) {
					SignatureInfo signDean = new SignatureInfo();

					boolean deanAlreadySigned = false;
					for (SignatureInfo signature : proposal.getSignatureInfo()) {
						if (user.getId().toString()
								.equals(signature.getUserProfileId())
								&& signature.getPositionTitle().equals("Dean")) {
							signDean.setUserProfileId(signature
									.getUserProfileId());
							signDean.setFullName(signature.getFullName());
							signDean.setSignature(signature.getSignature());
							signDean.setSignedDate(signature.getSignedDate());
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
				} else if (posDetails.getPositionTitle().equalsIgnoreCase(
						"Business Manager")) {
					SignatureInfo signBusinessMgr = new SignatureInfo();

					boolean businessManagerAlreadySigned = false;
					for (SignatureInfo signature : proposal.getSignatureInfo()) {
						if (user.getId().toString()
								.equals(signature.getUserProfileId())
								&& signature.getPositionTitle().equals(
										"Business Manager")) {
							signBusinessMgr.setUserProfileId(signature
									.getUserProfileId());
							signBusinessMgr
									.setFullName(signature.getFullName());
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
						signBusinessMgr.setPositionTitle("Business Manager");
						signBusinessMgr.setDelegated(false);
						if (!signatures.contains(signBusinessMgr)) {
							signatures.add(signBusinessMgr);
						}
					}
				} else if (posDetails.getPositionTitle().equalsIgnoreCase(
						"Department Chair")) {
					SignatureInfo signDeptChair = new SignatureInfo();

					boolean departmentChairAlreadySigned = false;
					for (SignatureInfo signature : proposal.getSignatureInfo()) {
						if (user.getId().toString()
								.equals(signature.getUserProfileId())
								&& signature.getPositionTitle().equals(
										"Department Chair")) {
							signDeptChair.setUserProfileId(signature
									.getUserProfileId());
							signDeptChair.setFullName(signature.getFullName());
							signDeptChair
									.setSignature(signature.getSignature());
							signDeptChair.setSignedDate(signature
									.getSignedDate());
							signDeptChair.setNote(signature.getNote());
							signDeptChair.setPositionTitle(signature
									.getPositionTitle());
							signDeptChair.setDelegated(signature.isDelegated());
							if (!signatures.contains(signDeptChair)) {
								signatures.add(signDeptChair);
							}
							departmentChairAlreadySigned = true;
						}
					}

					if (!departmentChairAlreadySigned) {
						signDeptChair.setUserProfileId(user.getId().toString());
						signDeptChair.setFullName(user.getFullName());
						signDeptChair.setSignature("");
						signDeptChair.setNote("");
						signDeptChair.setPositionTitle("Department Chair");
						signDeptChair.setDelegated(false);
						if (!signatures.contains(signDeptChair)) {
							signatures.add(signDeptChair);
						}
					}
				}
			}
		}
		return signatures;
	}

}
