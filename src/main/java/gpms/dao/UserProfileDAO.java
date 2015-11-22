package gpms.dao;

/**
 * @author Thomas Volz
 * 
 * @author Milson Munakami
 */

import gpms.DAL.MongoDBConnector;
import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.GPMSCommonInfo;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
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

public class UserProfileDAO extends BasicDAO<UserProfile, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "userprofile";

	private static Morphia morphia;
	private static Datastore ds;
	private AuditLog audit = new AuditLog();

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(UserProfile.class).map(
					UserAccount.class);
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

	public UserProfileDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	/*
	 * This is example format for grid Info object bind that is customized to
	 * bind in grid
	 */
	public List<UserInfo> findAllForUserGrid(int offset, int limit,
			String userName, String college, String department,
			String positionType, String positionTitle, Boolean isActive)
			throws UnknownHostException {
		Datastore ds = getDatastore();
		ArrayList<UserInfo> users = new ArrayList<UserInfo>();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);
		Query<UserAccount> accountQuery = ds.createQuery(UserAccount.class);

		if (userName != null) {
			accountQuery.criteria("username").containsIgnoreCase(userName);
		}

		if (isActive != null) {
			accountQuery.criteria("is active").equal(isActive);
		}

		profileQuery.criteria("user id").in(accountQuery.asKeyList());

		if (college != null) {
			profileQuery.criteria("details.college").equal(college);
		}
		if (department != null) {
			profileQuery.criteria("details.department").equal(department);
		}
		if (positionType != null) {
			profileQuery.criteria("details.position type").equal(positionType);
		}
		if (positionTitle != null) {
			profileQuery.criteria("details.position title")
					.equal(positionTitle);
		}

		int rowTotal = profileQuery.asList().size();
		// profileQuery.and(profileQuery.criteria("_id").notEqual(id)
		List<UserProfile> userProfiles = profileQuery.offset(offset - 1)
				.limit(limit).asList();

		for (UserProfile userProfile : userProfiles) {
			UserInfo user = new UserInfo();
			user.setRowTotal(rowTotal);
			user.setId(userProfile.getId().toString());
			user.setUserName(userProfile.getUserAccount().getUserName());
			user.setFullName(userProfile.getFullName());

			user.setNoOfPIedProposal(countPIProposal(userProfile));
			user.setNoOfCoPIedProposal(countCoPIProposal(userProfile));
			user.setNoOfSenioredProposal(countSeniorPersonnel(userProfile));

			user.setAddedOn(userProfile.getUserAccount().getAddedOn());

			ArrayList<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();

			if (userProfile.getAuditLog() != null
					&& userProfile.getAuditLog().size() != 0) {
				if (userProfile.getUserAccount().getAuditLog() != null
						&& userProfile.getUserAccount().getAuditLog().size() != 0) {
					for (AuditLog userAccountAudit : userProfile
							.getUserAccount().getAuditLog()) {
						AuditLogInfo userAuditLog = new AuditLogInfo();

						userAuditLog.setActivityDate(userAccountAudit
								.getActivityDate());
						userAuditLog.setUserFullName(userAccountAudit
								.getUserProfileId().getFullName());
						userAuditLog.setAction(userAccountAudit.getAction());

						allAuditLogs.add(userAuditLog);
					}

				}

				for (AuditLog userProfileAudit : userProfile.getAuditLog()) {
					AuditLogInfo userAuditLog = new AuditLogInfo();
					userAuditLog.setActivityDate(userProfileAudit
							.getActivityDate());
					userAuditLog.setUserFullName(userProfileAudit
							.getUserProfileId().getFullName());
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

			user.setLastAudited(lastAudited);
			user.setLastAuditedBy(lastAuditedBy);
			user.setLastAuditAction(lastAuditAction);

			user.setDeleted(userProfile.getUserAccount().isDeleted());
			user.setActive(userProfile.getUserAccount().isActive());
			users.add(user);
		}
		Collections.sort(users);
		return users;
	}

	public List<AuditLogInfo> findAllForUserAuditLogGrid(int offset, int limit,
			ObjectId userId, String action, String auditedBy,
			String activityOnFrom, String activityOnTo) throws ParseException,
			UnknownHostException {

		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		UserProfile q = profileQuery.field("_id").equal(userId).get();

		ArrayList<AuditLogInfo> allAuditLogs = new ArrayList<AuditLogInfo>();
		int rowTotal = 0;
		if (q.getAuditLog() != null && q.getAuditLog().size() != 0) {
			for (AuditLog userProfileAudit : q.getAuditLog()) {
				AuditLogInfo userAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (userProfileAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (userProfileAudit.getUserProfileId().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userProfileAudit.getUserProfileId()
							.getFirstName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userProfileAudit.getUserProfileId()
							.getMiddleName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userProfileAudit.getUserProfileId()
							.getLastName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (userProfileAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (userProfileAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (userProfileAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (userProfileAudit.getActivityDate().compareTo(
							activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (userProfileAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (userProfileAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					userAuditLog.setUserName(userProfileAudit
							.getUserProfileId().getUserAccount().getUserName());
					userAuditLog.setUserFullName(userProfileAudit
							.getUserProfileId().getFullName());
					userAuditLog.setAction(userProfileAudit.getAction());
					userAuditLog.setActivityDate(userProfileAudit
							.getActivityDate());

					allAuditLogs.add(userAuditLog);
				}
			}
		}

		if (q.getUserAccount().getAuditLog() != null
				&& q.getUserAccount().getAuditLog().size() != 0) {
			for (AuditLog userAccountAudit : q.getUserAccount().getAuditLog()) {
				AuditLogInfo userAuditLog = new AuditLogInfo();
				boolean isActionMatch = false;
				boolean isAuditedByMatch = false;
				boolean isActivityDateFromMatch = false;
				boolean isActivityDateToMatch = false;

				if (action != null) {
					if (userAccountAudit.getAction().toLowerCase()
							.contains(action.toLowerCase())) {
						isActionMatch = true;
					}
				} else {
					isActionMatch = true;
				}

				if (auditedBy != null) {
					if (userAccountAudit.getUserProfileId().getUserAccount()
							.getUserName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userAccountAudit.getUserProfileId()
							.getFirstName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userAccountAudit.getUserProfileId()
							.getMiddleName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					} else if (userAccountAudit.getUserProfileId()
							.getLastName().toLowerCase()
							.contains(auditedBy.toLowerCase())) {
						isAuditedByMatch = true;
					}
				} else {
					isAuditedByMatch = true;
				}

				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				if (activityOnFrom != null) {
					Date activityDateFrom = formatter.parse(activityOnFrom);
					if (userAccountAudit.getActivityDate().compareTo(
							activityDateFrom) > 0) {
						isActivityDateFromMatch = true;
					} else if (userAccountAudit.getActivityDate().compareTo(
							activityDateFrom) < 0) {
						isActivityDateFromMatch = false;
					} else if (userAccountAudit.getActivityDate().compareTo(
							activityDateFrom) == 0) {
						isActivityDateFromMatch = true;
					}
				} else {
					isActivityDateFromMatch = true;
				}

				if (activityOnTo != null) {
					Date activityDateTo = formatter.parse(activityOnTo);
					if (userAccountAudit.getActivityDate().compareTo(
							activityDateTo) > 0) {
						isActivityDateToMatch = false;
					} else if (userAccountAudit.getActivityDate().compareTo(
							activityDateTo) < 0) {
						isActivityDateToMatch = true;
					} else if (userAccountAudit.getActivityDate().compareTo(
							activityDateTo) == 0) {
						isActivityDateToMatch = true;
					}
				} else {
					isActivityDateToMatch = true;
				}

				if (isActionMatch && isAuditedByMatch
						&& isActivityDateFromMatch && isActivityDateToMatch) {
					userAuditLog.setUserName(userAccountAudit
							.getUserProfileId().getUserAccount().getUserName());
					userAuditLog.setUserFullName(userAccountAudit
							.getUserProfileId().getFullName());
					userAuditLog.setAction(userAccountAudit.getAction());
					userAuditLog.setActivityDate(userAccountAudit
							.getActivityDate());

					allAuditLogs.add(userAuditLog);
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

	private int countPIProposal(UserProfile userProfile) {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class)
				.field("investigator info.PI.user profile").equal(userProfile)
				.asList().size();
	}

	private int countCoPIProposal(UserProfile userProfile) {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class)
				.field("investigator info.CO-PI.user profile")
				.equal(userProfile).asList().size();
	}

	private int countSeniorPersonnel(UserProfile userProfile) {
		Datastore ds = getDatastore();
		return ds.createQuery(Proposal.class)
				.field("investigator info.senior personnel.user profile")
				.equal(userProfile).asList().size();
	}

	public UserProfile findUserDetailsByProfileID(ObjectId id) {
		Datastore ds = getDatastore();
		return ds.createQuery(UserProfile.class).field("_id").equal(id).get();
	}

	public void activateUserProfileByUserID(UserProfile userProfile,
			UserProfile authorProfile, GPMSCommonInfo gpmsCommonObj,
			Boolean isActive) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Deleted user "
				+ userProfile.getFullName(), new Date());
		userProfile.addEntryToAuditLog(audit);

		userProfile.setDeleted(!isActive);
		ds.save(userProfile);

	}
}
