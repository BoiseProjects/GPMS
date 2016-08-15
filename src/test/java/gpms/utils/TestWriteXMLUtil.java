package gpms.utils;

import gpms.DAL.MongoDBConnector;
import gpms.dao.DelegationDAO;
import gpms.dao.NotificationDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.Delegation;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.io.File;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class TestWriteXMLUtil {

	private static String policyLocation = new String();
	public static final String RESOURCE_PATH = "src" + File.separator + "main"
			+ File.separator + "resources";

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	private static UserProfileDAO userProfileDAO = null;
	private static DelegationDAO delegationDAO = null;
	NotificationDAO notificationDAO = null;

	public TestWriteXMLUtil() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		delegationDAO = new DelegationDAO(mongoClient, morphia, dbName);
		delegationDAO = new DelegationDAO(mongoClient, morphia, dbName);
		notificationDAO = new NotificationDAO(mongoClient, morphia, dbName);
	}

	public static void main(String[] args) throws Exception {
		// Create New policy File

		try {
			// policyLocation = this.getClass().getResource("/policy").toURI()
			// .getPath();

			policyLocation = (new File(".")).getCanonicalPath()
					+ File.separator + RESOURCE_PATH + File.separator
					+ "policy";

			String userProfileID = "5745f0f7bcbb29192ce0d405";
			String delegationID = "0";

			// ObjectId authorId = new ObjectId(userProfileID);
			// UserProfile authorProfile = userProfileDAO
			// .findUserDetailsByProfileID(authorId);
			String delegatorName = "Computer Science Department Chair";// authorProfile.getFullName();

			Delegation existingDelegation = new Delegation();

			if (delegationID != "0") {
				ObjectId delegationId = new ObjectId(delegationID);
				existingDelegation = delegationDAO
						.findDelegationByDelegationID(delegationId);
			}

			String policyId = WriteXMLUtil.saveDelegationPolicy(userProfileID,
					delegatorName, policyLocation, existingDelegation);

			System.out.println(policyId);

		} catch (Exception ex) {
			throw new Exception("The Policy folder can not be Found!");
		}
	}
}
