package gpms.accesscontrol;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

public class TestProposalDAO {

	MongoClient mongoClient;
	Morphia morphia;
	ProposalDAO newProposalDAO;
	String dbName = "db_gpms";
	final int MAXIMUM_PROPOSALS = 10; // Adjust this to make more or less
										// profiles with the generator.

	@Before
	public void initiate() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}
	
	@Test
	public void createPropObject()
	{
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	
	}
}
