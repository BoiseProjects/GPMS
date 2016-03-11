package gpms.model;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;

public class TestSignatureCheck {

	MongoClient mongoClient;
	Morphia morphia;
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
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
	public void test() throws UnknownHostException {

		List<Proposal> allPropsList = newProposalDAO.findAllProposals();
		
		//Need to check for Deans, IRB, Department Chairs  ...  
		
		for(Proposal prop : allPropsList)
		{
			
			System.out.println("Checking Proposal " + prop.getProposalNo() + "for position: Dean");
			System.out.println("For Proposal + " + prop.getProposalNo() + "AllSigned is: " + prop.getSignedStatus("Dean"));
			
			System.out.println("Checking Proposal " + prop.getProposalNo() + "for position: Department Chair");
			System.out.println("For Proposal + " + prop.getProposalNo() + "AllSigned is: " + prop.getSignedStatus("Department Chair"));
			
			System.out.println("Checking Proposal " + prop.getProposalNo() + "for position: IRB");
			System.out.println("For Proposal + " + prop.getProposalNo() + "AllSigned is: " + prop.getSignedStatus("IRB"));
			
		}
	}

}
