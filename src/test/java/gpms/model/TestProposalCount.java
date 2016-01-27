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

public class TestProposalCount {

	MongoClient mongoClient;
	Morphia morphia;
	String dbName = "db_gpms";
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
	ProposalDAO newProposalDAO;
	final int MAXIMUM_PROFILES = 10; // Adjust this to make more or less
	// profiles
	// with the generator.

	@Before
	public void initiate() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newUserAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		newUserProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	/**
	 * This is just going to pull the first user from the list of users then try and get a count
	 * of all of that users proposals by role
	 * @throws UnknownHostException
	 */
	@Test
	public void testCount() throws UnknownHostException
	{
		int total, pi, copi, senior;
		List<UserProfile> userList = newUserProfileDAO.findAll();
		ProposalCount newCounter = new ProposalCount();
		UserProfile temp = userList.get(0);
		String college = userList.get(0).getDetails(0).getCollege();
		String department = userList.get(0).getDetails(0).getDepartment();
		String positionTitle = userList.get(0).getDetails(0).getPositionTitle();
		String positionType = userList.get(0).getDetails(0).getPositionType();
		
		total = newUserProfileDAO.countByRoleTotalProposal(temp, college, department, positionType, positionTitle);
		pi = newUserProfileDAO.countByRolePIProposal(temp, college, department, positionType, positionTitle);
		copi = newUserProfileDAO.countByRoleCoPIProposal(temp, college, department, positionType, positionTitle);
		senior =  newUserProfileDAO.countByRoleSeniorProposal(temp, college, department, positionType, positionTitle);
		System.out.println("The selected user is: " + temp.getFirstName() +" " + temp.getLastName());
		System.out.println("The user id is: " + temp.getId().toString());
		System.out.println("The total proposal number is: " + total);
		System.out.println("The pi proposal number is: " + pi);
		System.out.println("The copi proposal number is: " + copi);
		System.out.println("The senior proposal number is: " + senior);
	}
}