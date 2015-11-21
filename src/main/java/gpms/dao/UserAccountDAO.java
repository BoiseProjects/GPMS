package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class UserAccountDAO extends BasicDAO<UserAccount, String> {
	private static final String DBNAME = "db_gpms";
	public static final String COLLECTION_NAME = "useraccount";
	private AuditLog audit;
	private static Morphia morphia;
	private static Datastore ds;

	private static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(UserAccount.class);
		}
		return morphia;
	}

	@Override
	public Datastore getDatastore() {
		if (ds == null) {
			try {
				ds = getMorphia().createDatastore(MongoDBConnector.getMongo(),
						DBNAME);
				ds.ensureIndexes();
			} catch (UnknownHostException | MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return ds;
	}

	public UserAccountDAO(MongoClient mongo, Morphia morphia, String dbName) {
		super(mongo, morphia, dbName);
	}

	public List<UserAccount> findAll() throws UnknownHostException {
		// if decision permit = 0
		// do
		// else don't
		Datastore ds = getDatastore();
		return ds.createQuery(UserAccount.class).asList();
	}

	public UserAccount findByID(ObjectId id) {
		Datastore ds = getDatastore();
		return ds.createQuery(UserAccount.class).field("_id").equal(id).get();
	}

	/**
	 * This method will return a user account object that matches the username
	 * searched for
	 * 
	 * @param userName
	 *            the user account name to search for
	 * @return the username if it exists or null if it does not
	 */
	public UserAccount findByUserName(String userName) {
		Datastore ds = getDatastore();
		return ds.createQuery(UserAccount.class).field("username")
				.equal(userName).get();
	}

	/**
	 * Method to set account name
	 * 
	 * @param author
	 *            profile making the change
	 * @param target
	 *            profile being changed
	 * @param newName
	 *            the new name for the user
	 */
	public void setAccountName(UserProfile author, UserAccount target,
			String newName) {
		Datastore ds = getDatastore();
		if (!target.getUserName().equals(newName)) {
			audit = new AuditLog(author, "Edited user name", new Date());
			target.addEntryToAuditLog(audit);
			target.setUserName(newName);
			ds.save(target);
		}
	}

	/**
	 * Method to set the password
	 * 
	 * @param author
	 *            profile making the change
	 * @param target
	 *            profile the change will be made to
	 * @param newPassword
	 *            the new password
	 */
	public void setPassword(UserProfile author, UserAccount target,
			String newPassword) {
		Datastore ds = getDatastore();
		if (!target.getPassword().equals(newPassword)) {
			audit = new AuditLog(author, "Edited password", new Date());
			target.addEntryToAuditLog(audit);
			target.setPassword(newPassword);
			ds.save(target);
		}
	}

	public void deleteUserAccountByUserID(UserAccount userAccount,
			UserProfile authorProfile, GPMSCommonInfo gpmsCommonObj) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Deleted user account for "
				+ userAccount.getUserName(), new Date());
		userAccount.addEntryToAuditLog(audit);

		userAccount.setDeleted(true);
		userAccount.setActive(false);
		ds.save(userAccount);
	}

	public void activateUserAccountByUserID(UserAccount userAccount,
			UserProfile authorProfile, GPMSCommonInfo gpmsCommonObj,
			Boolean isActive) {
		Datastore ds = getDatastore();
		audit = new AuditLog(authorProfile, "Activated user account for "
				+ userAccount.getUserName(), new Date());
		userAccount.addEntryToAuditLog(audit);

		userAccount.setDeleted(!isActive);
		userAccount.setActive(isActive);
		ds.save(userAccount);

	}
}