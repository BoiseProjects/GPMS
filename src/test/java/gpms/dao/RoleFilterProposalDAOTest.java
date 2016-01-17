package gpms.dao;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import gpms.DAL.MongoDBConnector;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

public class RoleFilterProposalDAOTest {

	MongoClient mongoClient;
	Morphia morphia;
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
	ProposalDAO newProposalDAO;
	String dbName = "db_gpms";
	String college, department, positionType, positionTitle;

	@Before
	public void initialize() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newUserAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		newUserProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@Test
	public void test() throws UnknownHostException {
		ArrayList<Proposal> newList = new ArrayList<Proposal>();
		// positionTitle = "Department Chair";
		UserProfile tempUser = newUserProfileDAO.findAll().get(0); // First User
		college = tempUser.getDetails().get(0).getCollege();
		// college = "Science";
		department = tempUser.getDetails().get(0).getDepartment();
		// department = "Physics";
		positionType = tempUser.getDetails().get(0).getPositionType();
		positionTitle = tempUser.getDetails().get(0).getPositionTitle();

		// newList = (ArrayList<Proposal>)
		// newProposalDAO.filteredProposalList(tempUser.getId().toString(),
		// college, department, positionType, positionTitle);

		newList = (ArrayList<Proposal>) newProposalDAO.filteredProposalList(
				"56999b2458019e1b2c9aa379", "Engineering", "Computer Science",
				"Non-tenure-track research faculty",
				"Assistant Research Professor");

		System.out.println("Returned list size is: " + newList.size());
		for (int list = 0; list < newList.size(); list++) {
			System.out.println("Proposal Number: "
					+ newList.get(list).getProposalNo());
		}
		System.out.println("Using ID: " + tempUser.getId().toString());
	}

}
