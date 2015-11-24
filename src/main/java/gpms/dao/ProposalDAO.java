package gpms.dao;

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
import org.mongodb.morphia.query.Query;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.GPMSCommonInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.Proposal;
import gpms.model.SimplePersonnelData;
import gpms.model.Status;
import gpms.model.UserProfile;

public class ProposalDAO  extends BasicDAO<Proposal, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "proposal";

	private static Morphia morphia;
	private static Datastore ds;
	private AuditLog audit = new AuditLog();
//	DelegationDAO delegationDAO = null;
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public ProposalDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	public Proposal findProposalByProposalID(ObjectId id)
			throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).field("_id").equal(id).get();
	}
	
	public void deleteProposal(Proposal proposal, UserProfile authorProfile,
			GPMSCommonInfo gpmsCommonObj) {
		Datastore ds = getDatastore();
		proposal.setProposalStatus(Status.DELETED);
		AuditLog entry = new AuditLog(authorProfile, "Deleted Proposal for "
				+ proposal.getProjectInfo().getProjectTitle(), new Date());
		proposal.addEntryToAuditLog(entry);
		ds.save(proposal);
	}
	
	public Proposal findProposalDetailsByProposalID(ObjectId id) {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class).field("_id").equal(id).get();
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
					if (poposalAudit.getUserProfileId().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfileId().getFirstName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfileId().getMiddleName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (poposalAudit.getUserProfileId().getLastName()
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
					proposalAuditLog.setUserName(poposalAudit
							.getUserProfileId().getUserAccount().getUserName());
					proposalAuditLog.setUserFullName(poposalAudit
							.getUserProfileId().getFullName());
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
	
	public List<SimplePersonnelData> PersonnelQuery(ObjectId id,
			String searchQuery) {
		ArrayList<SimplePersonnelData> spdList = new ArrayList<SimplePersonnelData>();
		SimplePersonnelData newEntry;
		Proposal queryProposal = null;
		try {
			queryProposal = findProposalByProposalID(id);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InvestigatorRefAndPosition pi = queryProposal.getInvestigatorInfo()
				.getPi();
		ArrayList<String> collegeSearch = new ArrayList<String>();
		collegeSearch.add(pi.getCollege());

		List<InvestigatorRefAndPosition> copi = queryProposal
				.getInvestigatorInfo().getCo_pi();
		List<String> copicollegeSearch = new ArrayList<String>();

		for (int b = 0; b < copi.size(); b++) {
			if (!collegeSearch.contains(copi.get(b).getCollege())) {
				collegeSearch.add(copi.get(b).getCollege());
			}
		}

		List<InvestigatorRefAndPosition> seniorPersonnel = queryProposal
				.getInvestigatorInfo().getSeniorPersonnel();

		for (int c = 0; c < seniorPersonnel.size(); c++) {
			if (!collegeSearch.contains(seniorPersonnel.get(c).getCollege())) {
				collegeSearch.add(seniorPersonnel.get(c).getCollege());
			}
		}

		Datastore ds = getDatastore();



		String checkName = "";
		ArrayList<String> checkList = new ArrayList<String>();

		for (int d = 0; d < collegeSearch.size(); d++) {
			String CollegeQuery = collegeSearch.get(d);
			Query<UserProfile> r = ds.createQuery(UserProfile.class);
			r.and(r.criteria("details.college").equal(CollegeQuery), r
					.criteria("details.position title").equal(searchQuery));
			List<UserProfile> queryProfileList = r.asList();
			for (int a = 0; a < queryProfileList.size(); a++) {
				checkName = queryProfileList.get(a).getFirstName() + " "
						+ queryProfileList.get(a).getLastName();
				if (!checkList.contains(checkName)) {
					newEntry = new SimplePersonnelData(queryProfileList.get(a));
					spdList.add(newEntry);
					checkList.add(checkName);
				}
			}
		}

		return spdList;
	}
	
}
