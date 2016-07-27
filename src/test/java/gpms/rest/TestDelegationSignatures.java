package gpms.rest;

import static org.junit.Assert.fail;
import gpms.DAL.MongoDBConnector;
import gpms.accesscontrol.Accesscontrol;
import gpms.dao.ProposalDAO;
import gpms.model.SignatureInfo;
import gpms.model.SignatureUserInfo;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

public class TestDelegationSignatures {
	Accesscontrol ac = null;

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	ProposalDAO proposalDAO = null;

	@Before
	public void setUp() throws Exception {
		ac = new Accesscontrol();

		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@After
	public void tearDown() throws Exception {
		ac = null;
	}

	@Test
	public void test() {
		// senior personal Proposal1094
		String proposalID = "5786972565dbb3f187eb61de";

		// Chemistry Chair
		String id = "5745f29ebcbb29192ce0d42f";

		ObjectId userProfileId = new ObjectId(id);

		List<SignatureInfo> delegatedChair = proposalDAO
				.findDelegatedUsersForAUser(userProfileId, proposalID,
						"Science", "Chemistry", "Administrator",
						"Department Chair");

		List<SignatureUserInfo> delegatedChairInfo = proposalDAO
				.findDelegatedUsersInfoForAUser(userProfileId, proposalID,
						"Science", "Chemistry", "Administrator",
						"Department Chair");

		fail("Not yet implemented");

		// assertEquals("Permit", AbstractResult.DECISIONS[intDecision]);
	}
}
