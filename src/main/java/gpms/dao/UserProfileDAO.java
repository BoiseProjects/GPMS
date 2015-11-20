package gpms.dao;

/**
 * @author Thomas Volz
 * 
 * @author Milson Munakami
 */

import gpms.DAL.MongoDBConnector;
import gpms.model.Address;
import gpms.model.AuditLog;
import gpms.model.AuditLogInfo;
import gpms.model.GPMSCommonInfo;
import gpms.model.PositionDetails;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserInfo;
import gpms.model.UserProfile;
import gpms.rest.InvestigatorUsersAndPositions;

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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
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

	// public UserProfile getUserProfile(ObjectId id) {
	// return UserProfile;
	// }

	/**
	 * 
	 * @return list of all users in the ds
	 * @throws UnknownHostException
	 */
	public List<UserProfile> findAll() throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(UserProfile.class).asList();
	}

	public List<UserProfile> findAllUsersWithPosition() throws UnknownHostException {
		Datastore ds = getDatastore();
		return ds.createQuery(UserProfile.class).field("details")
				.notEqual(null).asList();
	}

	public List<UserProfile> findAllActiveUsers() throws UnknownHostException {
		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);
		Query<UserAccount> accountQuery = ds.createQuery(UserAccount.class);

		accountQuery.and(accountQuery.criteria("is deleted").equal(false),
				accountQuery.criteria("is active").equal(true));
		profileQuery.and(
				profileQuery.criteria("details").notEqual(null),
				profileQuery.and(profileQuery.criteria("user id").in(
						accountQuery.asKeyList())),
				profileQuery.criteria("is deleted").equal(false));

		return profileQuery.retrievedFields(true, "_id", "first name",
				"middle name", "last name").asList();
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

	public UserProfile findByUserAccount(UserAccount userAccount) {
		Datastore ds = getDatastore();

		// UserProfile temp = query.field("user id.$id").equal(id).get();
		// UserProfile tempUser = ds.createQuery(UserProfile.class);
		// .field("user id.id").equal(id).get();
		return ds.createQuery(UserProfile.class).field("user id")
				.equal(userAccount).get();
	}

	public void deleteUserProfileByUserID(UserProfile userProfile,
			UserProfile authorProfile, GPMSCommonInfo gpmsCommonObj) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Deleted user "
				+ userProfile.getFullName(), new Date());
		userProfile.addEntryToAuditLog(audit);

		userProfile.setDeleted(true);
		ds.save(userProfile);
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

	/**
	 * 
	 * @param firstName
	 *            first name to search by
	 * @return list of users matching the first name
	 */
	public List<UserProfile> findByFirstName(String firstName) {
		// Could not make this work with .find methods. It threw errors every
		// time.
		// Had to use a query and createQuery method, and to search by field,
		// seems stable this way
		Datastore ds = getDatastore();
		Query<UserProfile> q = ds.createQuery(UserProfile.class)
				.field("first name").equal(firstName);
		return q.asList();
	}

	public List<UserProfile> findByFirstNameIgnoreCase(String firstName) {
		// This may be the go-to method for searching by name.
		// Still needs more testing, I believe it may actually look for any
		// phrase that
		// contains the given search query, ie: a search of "rIck" would return
		// both a "RICK" and a "Brick".
		// But these "similarities" may be preferred
		Datastore ds = getDatastore();
		Query<UserProfile> query = ds.createQuery(UserProfile.class);
		query.criteria("first name").containsIgnoreCase(firstName);
		// Query<UserProfile> q =
		// ds.createQuery(UserProfile.class).criteria("first name").containsIgnoreCase(firstName);
		return query.asList();
	}

	// /**
	// * Returns list sorted by First Name
	// * @param firstName
	// * @return
	// */
	// public List<UserProfile> sortAllByFirstName(String firstName)
	// {
	// //Could not make this work with .find methods. It threw errors every
	// time.
	// //Had to use a query and createQuery method, and to search by field,
	// seems stable this way
	// Datastore ds = getDatastore();
	// Query<UserProfile> q = ds.find(UserProfile.class).
	// return q.asList();
	// }

	/**
	 * Name search for specified name at param
	 * 
	 * @param middleName
	 *            middle name to search for
	 * @return list of all users in the ds with middleName
	 */
	public List<UserProfile> findByMiddleName(String middleName) {
		// Could not make this work with .find methods. It threw errors every
		// time.
		// Had to use a query and createQuery method, and to search by field,
		// seems stable this way
		Datastore ds = getDatastore();
		Query<UserProfile> q = ds.createQuery(UserProfile.class)
				.field("middle name").equal(middleName);
		return q.asList();
	}

	/**
	 * Name search for specified name at param
	 * 
	 * @param lastName
	 *            last name to search for
	 * @return list of all users in the ds with lastName
	 */
	public List<UserProfile> findByLastName(String lastName) {
		Datastore ds = getDatastore();
		Query<UserProfile> q = ds.createQuery(UserProfile.class)
				.field("last name").equal(lastName);
		return q.asList();
	}

	/**
	 * 
	 * @param email
	 * @return
	 */
	public UserProfile findByEmail(String email) {
		// May be the
		Datastore ds = getDatastore();
		UserProfile res = ds.find(UserProfile.class).filter("email = ", email)
				.get();
		return res;
	}

	/**
	 * Sets the first name of a user profile
	 * 
	 * @param profile
	 *            the user profile to change
	 * @param newName
	 *            the new name to set
	 */
	public void setFirstName(UserProfile author, UserProfile target,
			String newName) {
		// Accesscontrol ac = new Accesscontrol();
		// ac.getXACMLdecision(userName, resource, action);
		// Datastore ds = getDatastore();
		if (!target.getFirstName().equals(newName)) {
			audit = new AuditLog(author, "Edited first name", new Date());
			target.addEntryToAuditLog(audit);
			target.setFirstName(newName);
			ds.save(target);
		}
	}

	/**
	 * Sets the middle name of a user profile
	 * 
	 * @param profile
	 *            the user profile to change
	 * @param newName
	 *            the new name to set
	 */
	public void setMiddleName(UserProfile author, UserProfile target,
			String newName) {
		Datastore ds = getDatastore();
		if (!target.getMiddleName().equals(newName)) {
			audit = new AuditLog(author, "Edited middle name", new Date());
			target.addEntryToAuditLog(audit);
			target.setMiddleName(newName);
			ds.save(target);
		}
	}

	/**
	 * Sets the last name of a user profile
	 * 
	 * @param profile
	 *            the user profile to change
	 * @param newName
	 *            the new name to set
	 */
	public void setLastName(UserProfile author, UserProfile target,
			String newName) {
		Datastore ds = getDatastore();
		if (!target.getLastName().equals(newName)) {
			audit = new AuditLog(author, "Edited last name", new Date());
			target.addEntryToAuditLog(audit);
			target.setLastName(newName);
			ds.save(target);
		}
	}

	public void setDateOfBirth(UserProfile author, UserProfile target,
			Date newDOB) {
		Datastore ds = getDatastore();
		if (!target.getDateOfBirth().equals(newDOB)) {
			audit = new AuditLog(author, "Edited Date Of Birth", new Date());
			target.addEntryToAuditLog(audit);
			target.setDateOfBirth(newDOB);
			ds.save(target);
		}
	}

	public void setGender(UserProfile author, UserProfile target,
			String newGender) {
		Datastore ds = getDatastore();
		if (!target.getGender().equals(newGender)) {
			audit = new AuditLog(author, "Edited Gender", new Date());
			target.addEntryToAuditLog(audit);
			target.setGender(newGender);
			ds.save(target);
		}
	}

	// /**
	// * Returns a list of the details lists
	// * @param profile
	// * @return
	// */
	// public List getDetailsList(UserProfile profile)
	// {
	// List<PositionDetails> list = profile.getDetailsList();
	//
	// return list;
	// }

	/**
	 * Delete a specific "details" entry from a user profile
	 * 
	 * @param profile
	 *            the profile to use
	 * @param details
	 *            the details to delete
	 */
	public void removeDetails(UserProfile author, UserProfile target,
			PositionDetails details) {
		Datastore ds = getDatastore();
		audit = new AuditLog(author, "Removed details", new Date());
		target.addEntryToAuditLog(audit);
		target.getDetails().remove(target.getDetails().indexOf(details));
		ds.save(target);
	}

	/**
	 * Add a details object to the details of the user
	 * 
	 * @param profile
	 *            the profile to use
	 * @param details
	 *            the details to add
	 */
	public void addDetails(UserProfile author, UserProfile target,
			PositionDetails details) {
		Datastore ds = getDatastore();
		audit = new AuditLog(author, "Added details", new Date());
		target.addEntryToAuditLog(audit);
		target.getDetails().add(details);
		ds.save(target);
	}

	public void addOfficeNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (!target.getOfficeNumbers().contains(number)) {
			audit = new AuditLog(author, "Added office number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getOfficeNumbers().add(number);
			ds.save(target);
		}
	}

	public void deleteOfficeNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (target.getOfficeNumbers().contains(number)) {
			audit = new AuditLog(author, "Deleted office number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getOfficeNumbers().remove(
					target.getOfficeNumbers().indexOf(number));
			ds.save(target);
		}
	}

	public void addHomeNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (!target.getHomeNumbers().contains(number)) {
			audit = new AuditLog(author, "Added home number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getHomeNumbers().add(number);
			ds.save(target);
		}
	}

	public void deleteHomeNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (target.getHomeNumbers().contains(number)) {
			audit = new AuditLog(author, "Deleted home number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getHomeNumbers().remove(
					target.getHomeNumbers().indexOf(number));
			ds.save(target);
		}
	}

	public void addMobileNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (!target.getMobileNumbers().contains(number)) {
			audit = new AuditLog(author, "Added mobile number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getMobileNumbers().add(number);
			ds.save(target);
		}
	}

	public void deleteMobileNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (target.getMobileNumbers().contains(number)) {
			audit = new AuditLog(author, "Deleted mobile number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getMobileNumbers().remove(
					target.getMobileNumbers().indexOf(number));
			ds.save(target);
		}
	}

	public void addOtherNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (!target.getOtherNumbers().contains(number)) {
			audit = new AuditLog(author, "Added other number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getOtherNumbers().add(number);
			ds.save(target);
		}
	}

	public void deleteOtherNumber(UserProfile author, UserProfile target,
			String number) {
		Datastore ds = getDatastore();
		if (target.getOtherNumbers().contains(number)) {
			audit = new AuditLog(author, "Deleted other number " + number,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getOtherNumbers().remove(
					target.getOtherNumbers().indexOf(number));
			ds.save(target);
		}
	}

	public void addAddress(UserProfile author, UserProfile target,
			Address address) {
		Datastore ds = getDatastore();
		if (!target.getAddresses().contains(address)) {
			audit = new AuditLog(author,
					"Edited address " + address.toString(), new Date());
			target.addEntryToAuditLog(audit);
			target.getAddresses().add(address);
			ds.save(target);
		}
	}

	public void addWorkEmail(UserProfile author, UserProfile target,
			String email) {
		Datastore ds = getDatastore();
		if (!target.getWorkEmails().contains(email)) {
			audit = new AuditLog(author, "Added work email " + email,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getWorkEmails().add(email);
			ds.save(target);
		}
	}

	public void deleteWorkEmail(UserProfile author, UserProfile target,
			String email) {
		Datastore ds = getDatastore();
		if (target.getWorkEmails().contains(email)) {
			audit = new AuditLog(author, "Deleted work email " + email,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getWorkEmails()
					.remove(target.getWorkEmails().indexOf(email));
			ds.save(target);
		}
	}

	public void addPersonalEmail(UserProfile author, UserProfile target,
			String email) {
		Datastore ds = getDatastore();
		if (!target.getPersonalEmails().contains(email)) {
			audit = new AuditLog(author, "Added personal email " + email,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getPersonalEmails().add(email);
			ds.save(target);
		}
	}

	public void deletePersonalEmail(UserProfile author, UserProfile target,
			String email) {
		Datastore ds = getDatastore();
		if (target.getPersonalEmails().contains(email)) {
			audit = new AuditLog(author, "Deleted personal email " + email,
					new Date());
			target.addEntryToAuditLog(audit);
			target.getPersonalEmails().remove(
					target.getPersonalEmails().indexOf(email));
			ds.save(target);
		}
	}

	/**
	 * Dangerous method, will erase all entries. When it works Used only for
	 * testing, will be removed later
	 */
	public void deleteAll() {
		Datastore ds = getDatastore();
		ds.delete(ds.createQuery(UserProfile.class));
	}

	public UserProfile findNextUserWithSameUserName(ObjectId id, String userName) {
		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);
		Query<UserAccount> accountQuery = ds.createQuery(UserAccount.class);

		// CriteriaContainer or3 =
		// accountQuery.and(accountQuery.criteria("username").equal(userName));
		// CriteriaBuilder c = new CriteriaBuilder();
		// c.lower(x);
		//

		Pattern pattern = Pattern.compile("^" + userName + "$",
				Pattern.CASE_INSENSITIVE);

		accountQuery.criteria("username").containsIgnoreCase(pattern.pattern());

		profileQuery.and(profileQuery.criteria("_id").notEqual(id),
				profileQuery.criteria("user id").in(accountQuery.asKeyList()));
		return profileQuery.get();
	}

	public UserProfile findAnyUserWithSameUserName(String newUserName) {
		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);
		Query<UserAccount> accountQuery = ds.createQuery(UserAccount.class);

		Pattern pattern = Pattern.compile("^" + newUserName + "$",
				Pattern.CASE_INSENSITIVE);

		accountQuery.criteria("username").containsIgnoreCase(pattern.pattern());
		profileQuery.criteria("user id").in(accountQuery.asKeyList());
		return profileQuery.get();
	}

	public UserProfile findNextUserWithSameEmail(ObjectId id, String newEmail) {
		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);
		profileQuery.and(
				profileQuery.criteria("_id").notEqual(id),
				profileQuery.or(
						profileQuery.criteria("work email").hasThisOne(
								newEmail.toString()),
						profileQuery.criteria("personal email").hasThisOne(
								newEmail.toString())));
		return profileQuery.get();
	}

	public UserProfile findAnyUserWithSameEmail(String newEmail) {
		Datastore ds = getDatastore();

		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		// Pattern pattern = Pattern.compile("^" + newEmail + "$",
		// Pattern.CASE_INSENSITIVE);
		profileQuery.or(
				profileQuery.criteria("work email").hasThisOne(
						newEmail.toString()),
				profileQuery.criteria("personal email").hasThisOne(
						newEmail.toString()));
		return profileQuery.get();
	}

	// public List<InvestigatorUsersAndPositions> findAllUsers() {
	// Datastore ds = getDatastore();
	// List<InvestigatorUsersAndPositions> users = new
	// ArrayList<InvestigatorUsersAndPositions>();
	//
	// Query<UserProfile> q = ds.createQuery(UserProfile.class)
	// .retrievedFields(true, "_id", "first name", "middle name",
	// "last name", "mobile number");
	// List<UserProfile> userProfile = q.asList();
	//
	// for (UserProfile user : userProfile) {
	// InvestigatorUsersAndPositions userInfo = new
	// InvestigatorUsersAndPositions();
	// userInfo.setId(user.getId().toString());
	// userInfo.setFullName(user.getFullName());
	// userInfo.setMobileNumber(user.getMobileNumbers().get(0));
	// users.add(userInfo);
	// }
	// return users;
	// }

	public ArrayList<InvestigatorUsersAndPositions> findAllUsersAndPositions() {
		Datastore ds = getDatastore();
		ArrayList<InvestigatorUsersAndPositions> userPositions = new ArrayList<InvestigatorUsersAndPositions>();

		Query<UserProfile> q = ds.createQuery(UserProfile.class)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "mobile number");
		List<UserProfile> userProfile = q.asList();

		for (UserProfile user : userProfile) {
			Multimap<String, Object> htUser = ArrayListMultimap.create();

			InvestigatorUsersAndPositions userPosition = new InvestigatorUsersAndPositions();
			userPosition.setId(user.getId().toString());
			userPosition.setFullName(user.getFullName());
			userPosition.setMobileNumber(user.getMobileNumbers().get(0));

			for (PositionDetails userDetails : user.getDetails()) {
				// Multimap<String, String> mapTypeTitle = new
				// ArrayListMultimap.create();

				Multimap<String, Object> mapTypeTitle = ArrayListMultimap
						.create();
				Multimap<String, Object> mapDeptType = ArrayListMultimap
						.create();

				mapTypeTitle.put(userDetails.getPositionType(),
						userDetails.getPositionTitle());
				mapDeptType.put(userDetails.getDepartment(),
						mapTypeTitle.asMap());
				// ht.put(userDetails.getCollege(), mapTypeTitle);

				htUser.put(userDetails.getCollege(), mapDeptType.asMap());
				userPosition.setPositions(htUser);
			}
			userPositions.add(userPosition);
		}
		return userPositions;
	}

	public List<InvestigatorUsersAndPositions> findAllPositionDetailsForAUser(
			ObjectId id) {
		Datastore ds = getDatastore();
		ArrayList<InvestigatorUsersAndPositions> userPositions = new ArrayList<InvestigatorUsersAndPositions>();

		Query<UserProfile> q = ds
				.createQuery(UserProfile.class)
				.field("_id")
				.equal(id)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "mobile number");
		List<UserProfile> userProfile = q.asList();

		for (UserProfile user : userProfile) {
			Multimap<String, Object> htUser = ArrayListMultimap.create();

			InvestigatorUsersAndPositions userPosition = new InvestigatorUsersAndPositions();
			userPosition.setId(user.getId().toString());
			userPosition.setFullName(user.getFullName());
			userPosition.setMobileNumber(user.getMobileNumbers().get(0));

			for (PositionDetails userDetails : user.getDetails()) {
				Multimap<String, Object> mapTypeTitle = ArrayListMultimap
						.create();
				Multimap<String, Object> mapDeptType = ArrayListMultimap
						.create();

				mapTypeTitle.put(userDetails.getPositionType(),
						userDetails.getPositionTitle());
				mapDeptType.put(userDetails.getDepartment(),
						mapTypeTitle.asMap());

				htUser.put(userDetails.getCollege(), mapDeptType.asMap());
				userPosition.setPositions(htUser);
			}
			userPositions.add(userPosition);
		}
		return userPositions;
	}

	public List<InvestigatorUsersAndPositions> findUserPositionDetailsForAProposal(
			List<ObjectId> userIds) {
		Datastore ds = getDatastore();
		ArrayList<InvestigatorUsersAndPositions> userPositions = new ArrayList<InvestigatorUsersAndPositions>();

		Query<UserProfile> q = ds
				.createQuery(UserProfile.class)
				.field("_id")
				.in(userIds)
				.retrievedFields(true, "_id", "first name", "middle name",
						"last name", "details", "mobile number");
		List<UserProfile> userProfile = q.asList();

		for (UserProfile user : userProfile) {
			Multimap<String, Object> htUser = ArrayListMultimap.create();

			InvestigatorUsersAndPositions userPosition = new InvestigatorUsersAndPositions();
			userPosition.setId(user.getId().toString());
			userPosition.setFullName(user.getFullName());
			userPosition.setMobileNumber(user.getMobileNumbers().get(0));

			for (PositionDetails userDetails : user.getDetails()) {
				Multimap<String, Object> mapTypeTitle = ArrayListMultimap
						.create();
				Multimap<String, Object> mapDeptType = ArrayListMultimap
						.create();

				mapTypeTitle.put(userDetails.getPositionType(),
						userDetails.getPositionTitle());
				mapDeptType.put(userDetails.getDepartment(),
						mapTypeTitle.asMap());

				htUser.put(userDetails.getCollege(), mapDeptType.asMap());
				userPosition.setPositions(htUser);
			}
			userPositions.add(userPosition);
		}
		return userPositions;
	}

	public String findMobileNoForAUser(ObjectId id) {
		Datastore ds = getDatastore();
		Query<UserProfile> profileQuery = ds.createQuery(UserProfile.class);

		UserProfile q = profileQuery.field("_id").equal(id)
				.retrievedFields(true, "mobile number").get();
		return q.getMobileNumbers().get(0).toString();
	}

	public List<String> findCollegesForAUser(ObjectId id) {
		Datastore ds = getDatastore();
		List<String> userColleges = new ArrayList<String>();

		Query<UserProfile> q = ds.createQuery(UserProfile.class).field("_id")
				.equal(id).retrievedFields(true, "details");
		List<UserProfile> userProfile = q.asList();
		for (UserProfile user : userProfile) {
			for (PositionDetails userDetails : user.getDetails()) {
				if (!userColleges.contains(userDetails.getCollege())) {
					userColleges.add(userDetails.getCollege());
				}
			}
		}
		return userColleges;
	}

	public List<String> findDepartmentsForAUser(ObjectId id, String college) {
		Datastore ds = getDatastore();
		List<String> userDepartments = new ArrayList<String>();

		Query<UserProfile> q = ds.createQuery(UserProfile.class).field("_id")
				.equal(id).retrievedFields(true, "details");
		List<UserProfile> userProfile = q.asList();
		for (UserProfile user : userProfile) {
			for (PositionDetails userDetails : user.getDetails()) {
				if (userDetails.getCollege().equalsIgnoreCase(college)
						&& !userDepartments.contains(userDetails
								.getDepartment())) {
					userDepartments.add(userDetails.getDepartment());
				}
			}
		}
		return userDepartments;
	}

	public List<String> findPositionTypesForAUser(ObjectId id, String college,
			String department) {
		Datastore ds = getDatastore();
		List<String> userPositionTypes = new ArrayList<String>();

		Query<UserProfile> q = ds.createQuery(UserProfile.class).field("_id")
				.equal(id).retrievedFields(true, "details");
		List<UserProfile> userProfile = q.asList();
		for (UserProfile user : userProfile) {
			for (PositionDetails userDetails : user.getDetails()) {
				if (userDetails.getCollege().equalsIgnoreCase(college)
						&& userDetails.getDepartment().equalsIgnoreCase(
								department)
						&& !userPositionTypes.contains(userDetails
								.getPositionType())) {
					userPositionTypes.add(userDetails.getPositionType());
				}
			}
		}
		return userPositionTypes;
	}

	public List<String> findPositionTitlesForAUser(ObjectId id, String college,
			String department, String positionType) {
		Datastore ds = getDatastore();
		List<String> userPositionTitles = new ArrayList<String>();

		Query<UserProfile> q = ds.createQuery(UserProfile.class).field("_id")
				.equal(id).retrievedFields(true, "details");
		List<UserProfile> userProfile = q.asList();
		for (UserProfile user : userProfile) {
			for (PositionDetails userDetails : user.getDetails()) {
				if (userDetails.getCollege().equalsIgnoreCase(college)
						&& userDetails.getDepartment().equalsIgnoreCase(
								department)
						&& userDetails.getPositionType().equalsIgnoreCase(
								positionType)
						&& !userPositionTitles.contains(userDetails
								.getPositionTitle())) {
					userPositionTitles.add(userDetails.getPositionTitle());
				}
			}
		}
		return userPositionTitles;
	}

}
