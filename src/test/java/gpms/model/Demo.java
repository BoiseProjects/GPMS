package gpms.model;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
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
		ProposalDAO proposalDAO = null;
		UserProfileDAO userProfileDAO = null;
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);

		// User Starts here
		String userID = "56e459c1af68c71ea4248ed8";
		ObjectId id = new ObjectId(userID);

		UserProfile existingUserProfile = userProfileDAO
				.findUserDetailsByProfileID(id);

		// deep copy
		UserProfile ceDeepClone = SerializationUtils.clone(existingUserProfile);

		// ce.setNum(200);

		if (existingUserProfile.equals(ceDeepClone)) {
			System.out.println("Equals! using SerializationUtils");
		}

		UserProfile ceDeepCopy = (UserProfile) (ObjectCloner
				.deepCopy(existingUserProfile));

		if (existingUserProfile.equals(ceDeepCopy)) {
			System.out.println("Equals! using ObjectCloner");
		}

		UserProfile ceDeepClone2 = SerializationHelper
				.cloneThroughSerialize(existingUserProfile);

		if (existingUserProfile.equals(ceDeepClone2)) {
			System.out.println("Equals! using SerializationHelper");
		}

		UserProfile normalCopy = existingUserProfile;

		// normalCopy.setNum(100);

		if (normalCopy.equals(existingUserProfile)) {
			System.out.println("Equals");
		}

		// // Proposal Starts here
		// String proposalID = "56e84538d87e7b1a989d7df2";
		// ObjectId id = new ObjectId(proposalID);
		// Proposal existingProposal = proposalDAO.findProposalByProposalID(id);
		//
		// // deep copy
		// Proposal ceDeepClone = SerializationUtils.clone(existingProposal);
		//
		// // ce.setNum(200);
		//
		// if (existingProposal.equals(ceDeepClone)) {
		// System.out.println("Equals! using SerializationUtils");
		// }
		//
		// Proposal ceDeepCopy = (Proposal) (ObjectCloner
		// .deepCopy(existingProposal));
		//
		// if (existingProposal.equals(ceDeepCopy)) {
		// System.out.println("Equals! using ObjectCloner");
		// }
		//
		// Proposal ceDeepClone2 = SerializationHelper
		// .cloneThroughSerialize(existingProposal);
		//
		// if (existingProposal.equals(ceDeepClone2)) {
		// System.out.println("Equals! using SerializationHelper");
		// }
		//
		// Proposal normalCopy = existingProposal;
		//
		// // normalCopy.setNum(100);
		//
		// if (normalCopy.equals(existingProposal)) {
		// System.out.println("Equals");
		// }

	}
}
