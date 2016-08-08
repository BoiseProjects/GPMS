package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.Delegation;
import gpms.model.DelegationInfo;
import gpms.model.Proposal;
import gpms.model.UserAccount;
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

public class DelegationDAO extends BasicDAO<Delegation, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "delegation";

	private static Morphia morphia;
	private static Datastore ds;
	private AuditLog audit = new AuditLog();
	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(Delegation.class)
					.map(UserAccount.class);
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
		ds.ensureIndexes();
		return ds;
	}

	public DelegationDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	public List<DelegationInfo> findAllForUserDelegationGrid(int offset,
			int limit, String delegatee, String delegatedFrom,
			String delegatedTo, String delegatedAction, Boolean isRevoked,
			String delegatorID) throws ParseException {
		Datastore ds = getDatastore();
		List<DelegationInfo> delegations = new ArrayList<DelegationInfo>();

		Query<Delegation> delegationQuery = ds.createQuery(Delegation.class);

		if (delegatorID != null) {
			delegationQuery.criteria("delegator user id").equal(delegatorID);
		}

		if (delegatee != null) {
			delegationQuery.criteria("delegatee").containsIgnoreCase(delegatee);
		}

		if (delegatedFrom != null && !delegatedFrom.isEmpty()) {
			Date delegatedStartsFrom = formatter.parse(delegatedFrom);
			delegationQuery.criteria("from").greaterThanOrEq(
					delegatedStartsFrom);
		}
		if (delegatedTo != null && !delegatedTo.isEmpty()) {
			Date delegatedStartsTo = formatter.parse(delegatedTo);
			delegationQuery.criteria("to").lessThanOrEq(delegatedStartsTo);
		}

		if (delegatedAction != null) {
			delegationQuery.criteria("action").equal(delegatedAction);
		}

		if (isRevoked != null) {
			delegationQuery.criteria("revoked").equal(isRevoked);
		}

		int rowTotal = delegationQuery.asList().size();
		List<Delegation> allDelegations = delegationQuery.offset(offset - 1)
				.limit(limit).order("-audit log.activity on").asList();

		for (Delegation userDelegation : allDelegations) {
			DelegationInfo delegation = new DelegationInfo();

			delegation.setRowTotal(rowTotal);
			delegation.setId(userDelegation.getId().toString());
			delegation.setDelegatee(userDelegation.getDelegatee());
			delegation.setDelegateeDepartment(userDelegation.getDepartment());
			delegation.setDelegateePositionTitle(userDelegation
					.getPositionTitle());
			delegation.setDelegatedAction(userDelegation.getAction());
			delegation.setDelegationReason(userDelegation.getReason());
			delegation.setDateCreated(userDelegation.getCreatedOn());
			delegation.setDelegatedFrom(userDelegation.getFrom());
			delegation.setDelegatedTo(userDelegation.getTo());

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();

			int auditLogCount = userDelegation.getAuditLog().size();
			if (userDelegation.getAuditLog() != null && auditLogCount != 0) {
				AuditLog auditLog = userDelegation.getAuditLog().get(
						auditLogCount - 1);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserProfile().getFullName();
				lastAuditAction = auditLog.getAction();
			}

			delegation.setLastAudited(lastAudited);
			delegation.setLastAuditedBy(lastAuditedBy);
			delegation.setLastAuditAction(lastAuditAction);

			delegation.setRevoked(userDelegation.isRevoked());

			delegations.add(delegation);
		}
		// Collections.sort(delegations);
		return delegations;
	}

	public List<DelegationInfo> findAllUserDelegations(String delegatee,
			String delegatedFrom, String delegatedTo, String delegatedAction,
			Boolean isRevoked, String delegatorID) throws ParseException {
		Datastore ds = getDatastore();
		List<DelegationInfo> delegations = new ArrayList<DelegationInfo>();

		Query<Delegation> delegationQuery = ds.createQuery(Delegation.class);

		if (delegatorID != null) {
			delegationQuery.criteria("delegator user id").equal(delegatorID);
		}

		if (delegatee != null) {
			delegationQuery.criteria("delegatee").containsIgnoreCase(delegatee);
		}

		if (delegatedFrom != null && !delegatedFrom.isEmpty()) {
			Date delegatedStartsFrom = formatter.parse(delegatedFrom);
			delegationQuery.criteria("from").greaterThanOrEq(
					delegatedStartsFrom);
		}
		if (delegatedTo != null && !delegatedTo.isEmpty()) {
			Date delegatedStartsTo = formatter.parse(delegatedTo);
			delegationQuery.criteria("to").lessThanOrEq(delegatedStartsTo);
		}

		if (delegatedAction != null) {
			delegationQuery.criteria("action").equal(delegatedAction);
		}

		if (isRevoked != null) {
			delegationQuery.criteria("revoked").equal(isRevoked);
		}

		int rowTotal = delegationQuery.asList().size();
		List<Delegation> allDelegations = delegationQuery.order(
				"-audit log.activity on").asList();

		for (Delegation userDelegation : allDelegations) {
			DelegationInfo delegation = new DelegationInfo();

			delegation.setRowTotal(rowTotal);
			delegation.setId(userDelegation.getId().toString());
			delegation.setDelegatee(userDelegation.getDelegatee());
			delegation.setDelegateeDepartment(userDelegation.getDepartment());
			delegation.setDelegateePositionTitle(userDelegation
					.getPositionTitle());
			delegation.setDelegatedAction(userDelegation.getAction());
			delegation.setDelegationReason(userDelegation.getReason());
			delegation.setDateCreated(userDelegation.getCreatedOn());
			delegation.setDelegatedFrom(userDelegation.getFrom());
			delegation.setDelegatedTo(userDelegation.getTo());

			Date lastAudited = null;
			String lastAuditedBy = new String();
			String lastAuditAction = new String();

			int auditLogCount = userDelegation.getAuditLog().size();
			if (userDelegation.getAuditLog() != null && auditLogCount != 0) {
				AuditLog auditLog = userDelegation.getAuditLog().get(
						auditLogCount - 1);
				lastAudited = auditLog.getActivityDate();
				lastAuditedBy = auditLog.getUserProfile().getFullName();
				lastAuditAction = auditLog.getAction();
			}

			delegation.setLastAudited(lastAudited);
			delegation.setLastAuditedBy(lastAuditedBy);
			delegation.setLastAuditAction(lastAuditAction);

			delegation.setRevoked(userDelegation.isRevoked());

			delegations.add(delegation);
		}
		// Collections.sort(delegations);
		return delegations;
	}

	public Delegation findDelegationByDelegationID(ObjectId id) {
		Datastore ds = getDatastore();
		return ds.createQuery(Delegation.class).field("_id").equal(id).get();
		// .retrievedFields(false, "userProfile")
	}

	public List<AuditLogInfo> findAllForDelegationAuditLogGrid(int offset,
			int limit, ObjectId id, String action, String auditedBy,
			String activityOnFrom, String activityOnTo) throws ParseException {
		Datastore ds = getDatastore();

		Query<Delegation> delegationQuery = ds.createQuery(Delegation.class);

		Delegation q = delegationQuery.field("_id").equal(id).get();

		List<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();
		int rowTotal = 0;
		if (q.getAuditLog() != null && q.getAuditLog().size() != 0) {
			for (AuditLog delegationAudit : q.getAuditLog()) {
				AuditLogInfo delegationAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (delegationAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (delegationAudit.getUserProfile().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getFirstName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getMiddleName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getLastName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					delegationAuditLog.setUserName(delegationAudit
							.getUserProfile().getUserAccount().getUserName());
					delegationAuditLog.setUserFullName(delegationAudit
							.getUserProfile().getFullName());
					delegationAuditLog.setAction(delegationAudit.getAction());
					delegationAuditLog.setActivityDate(delegationAudit
							.getActivityDate());

					allAuditLogs.add(delegationAuditLog);
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

	public List<AuditLogInfo> findAllUserDelegationAuditLogs(ObjectId id,
			String action, String auditedBy, String activityOnFrom,
			String activityOnTo) throws ParseException {
		Datastore ds = getDatastore();

		Query<Delegation> delegationQuery = ds.createQuery(Delegation.class);

		Delegation q = delegationQuery.field("_id").equal(id).get();

		List<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();

		if (q.getAuditLog() != null && q.getAuditLog().size() != 0) {
			for (AuditLog delegationAudit : q.getAuditLog()) {
				AuditLogInfo delegationAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (delegationAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (delegationAudit.getUserProfile().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getFirstName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getMiddleName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (delegationAudit.getUserProfile().getLastName()
							.toLowerCase().contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (delegationAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					delegationAuditLog.setUserName(delegationAudit
							.getUserProfile().getUserAccount().getUserName());
					delegationAuditLog.setUserFullName(delegationAudit
							.getUserProfile().getFullName());
					delegationAuditLog.setAction(delegationAudit.getAction());
					delegationAuditLog.setActivityDate(delegationAudit
							.getActivityDate());

					allAuditLogs.add(delegationAuditLog);
				}
			}
		}

		Collections.sort(allAuditLogs);
		return allAuditLogs;
	}

	public void saveDelegation(Delegation newDelegation,
			UserProfile authorProfile) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Created delegation by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		newDelegation.getAuditLog().add(audit);
		ds.save(newDelegation);

	}

	public void updateDelegation(Delegation existingDelegation,
			UserProfile authorProfile) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Updated delegation by "
				+ authorProfile.getUserAccount().getUserName(), new Date());
		existingDelegation.getAuditLog().add(audit);
		ds.save(existingDelegation);
	}

	public boolean revokeDelegation(Delegation existingDelegation,
			UserProfile authorProfile) {
		try {
			Datastore ds = getDatastore();
			audit = new AuditLog(authorProfile, "Revoked delegation by "
					+ authorProfile.getUserAccount().getUserName(), new Date());
			existingDelegation.getAuditLog().add(audit);
			existingDelegation.setRevoked(true);
			ds.save(existingDelegation);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
