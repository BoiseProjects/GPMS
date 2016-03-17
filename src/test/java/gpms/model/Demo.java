package gpms.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import gpms.DAL.MongoDBConnector;
import gpms.dao.NotificationDAO;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.utils.ObjectCloner;
import gpms.utils.SerializationHelper;

import org.apache.commons.lang3.SerializationUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class Demo {

	public static void main(String[] args) throws Exception {

		MongoClient mongoClient = null;
		Morphia morphia = null;
		String dbName = "db_gpms";
		UserAccountDAO userAccountDAO = null;
		UserProfileDAO userProfileDAO = null;
		ProposalDAO proposalDAO = null;
		NotificationDAO notificationDAO = null;

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
		notificationDAO = new NotificationDAO(mongoClient, morphia, dbName);

		String userID = "56e459c1af68c71ea4248ed8";
		ObjectId id = new ObjectId(userID);
		UserProfile existingUserProfile = userProfileDAO
				.findUserDetailsByProfileID(id);

		// deep copy
		UserProfile oldUserProfile = (UserProfile) (ObjectCloner
				.deepCopy(existingUserProfile));

		UserProfile oldUserProfile2 = SerializationUtils
				.clone(existingUserProfile);

		UserProfile oldUserProfile3 = SerializationHelper
				.cloneThroughSerialize(existingUserProfile);
		if (existingUserProfile.equals(oldUserProfile)) {
			System.out.println("Equals! using ObjectCloner");
		}

		if (existingUserProfile.equals(oldUserProfile2)) {
			System.out.println("Equals! using SerializationUtils");
		}

		if (existingUserProfile.equals(oldUserProfile3)) {
			System.out.println("Equals!");
		}

		// CloneExample ce = new CloneExample();
		// AuditLog newLog = new AuditLog();
		// newLog.setAction("Edited!");
		// newLog.setActivityDate(new Date());
		// ce.getAuditLog().add(newLog);
		// ce.setNum(3);
		// ce.setThing(new Thing("Fred"));
		// System.out.println("Before cloning");
		// System.out.println("ce:" + ce);
		//
		// CloneExample ceShallowClone = ce.clone();
		// CloneExample cdDeepClone = SerializationHelper
		// .cloneThroughSerialize(ce);
		//
		// CloneExample normalCopy = ce;
		//
		// // System.out.println("\nAfter cloning, setting ce num to 5");
		// // ce.setNum(5);
		// //
		// System.out.println("After cloning, setting ce thing name to Barney");
		// // Thing thing = ce.getThing();
		// // thing.setName("Barney");
		//
		// System.out.println("ce:" + ce);
		// System.out.println("ceShallowClone:" + ceShallowClone);
		// System.out.println("cdDeepClone:" + cdDeepClone);
		//
		// // ce.setNum(500);
		// if (cdDeepClone.equals(ce)) {
		// System.out.println("Equals");
		//
		// }
		//
		// if (ceShallowClone.equals(ce)) {
		// System.out.println("Equals");
		//
		// }
		//
		// normalCopy.setNum(100);
		//
		// if (normalCopy.equals(ce)) {
		// System.out.println("Equals");
		// }

	}
}
