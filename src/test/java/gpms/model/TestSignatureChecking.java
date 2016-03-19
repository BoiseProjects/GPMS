package gpms.model;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;

/**
 * This Junit test is meant to check if the signature verification method is
 * working correctly Unfortunately, the automatic proposal creation suite we
 * have is a bit too bloated for effective testing So within this class I will
 * create only the essentials that are needed to verify the signature checking
 * method is working correctly.
 * 
 * @author Tommy
 *
 */
public class TestSignatureChecking {
	MongoClient mongoClient;
	Morphia morphia;
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
	ProposalDAO newProposalDAO;
	String dbName = "db_gpms";

	@Before
	public void initiate() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newUserAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		newUserProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);

	}

	// Supervisory personnel are queried by College, Department, Position Title.
	// This is all we'll need to create to test this, we need this info for any
	// PI, CoPI that we make
	// As well as the same info for any person we add into the database, so that
	// we can find them.

	/**
	 * Test only one PI and only one Dean
	 * 
	 * @throws UnknownHostException
	 */
	@Test
	public void testOneDeanWithOnlyPI() throws UnknownHostException {
		Proposal testProposal;
		InvestigatorInfo testInfo;
		testProposal = new Proposal();
		testInfo = new InvestigatorInfo();

		// Set Proposal Number
		testProposal.setProposalNo(1);
		// Set PI Info
		InvestigatorRefAndPosition pi = new InvestigatorRefAndPosition();
		UserProfile profile1 = new UserProfile();
		// Create the PI
		profile1.setFirstName("Donald");
		profile1.setLastName("Trump");
		UserAccount account1 = new UserAccount();
		account1.setUserName("dTrump");
		profile1.setUserAccount(account1);
		newUserAccountDAO.save(account1);
		newUserProfileDAO.save(profile1);

		pi.setUserRef(profile1);
		pi.setCollege("Science");
		pi.setDepartment("Chemistry");

		testInfo.setPi(pi);
		testProposal.setInvestigatorInfo(testInfo);

		// Create his Dean
		UserProfile deanProfile = new UserProfile();
		deanProfile.setFirstName("Bruce");
		deanProfile.setLastName("Jenner");
		UserAccount deanAccount = new UserAccount();
		deanAccount.setUserName("bJenner");
		deanProfile.setUserAccount(deanAccount);
		newUserAccountDAO.save(deanAccount);
		newUserProfileDAO.save(deanProfile);

		// Sign the Proposal
		SignatureInfo newSig = new SignatureInfo();
		newSig.setFullName(deanProfile.getFullName());
		newSig.setUserProfileId(deanProfile.getId().toString());
		newSig.setPositionTitle("Dean");
		List<SignatureInfo> sigList = new ArrayList<SignatureInfo>();
		sigList.add(newSig);
		testProposal.setSignatureInfo(sigList);

		// Save the proposal
		newProposalDAO.save(testProposal);
		assertTrue(getSignedStatusForAProposal(testProposal.getId(), "Dean"));

	}

	// ///////////////////////////////////////////
	// THIS IS THE EXACT COPY OF THE METHOD FROM//
	// PROPOSALSERVICE.JAVA, IF THIS WORKS, THAT//
	// WORKS AS WELL, HERE ARE MORE WORDS ////////
	// ///////////////////////////////////////////

	/**
	 * This method will check the signatures for the proposal. It will first
	 * find all the supervisory personnel that SHOULD be signing the proposal
	 * (based on PI, COPI, Senior Personnel -their supervisory personnel-) Then
	 * it will find out if the appropriate number has signed ie: if between the
	 * Pi, CoPi, and SP, there are 4 department chairs, we need to know that 4
	 * department chairs have signed.
	 * 
	 * @param1 the ID of the proposal we need to query for
	 * @param2 the position title we want to check
	 * @return true if all required signatures exist
	 * @throws UnknownHostException
	 */
	public boolean getSignedStatusForAProposal(ObjectId id, String posTitle)
			throws UnknownHostException {
		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		Proposal checkProposal = newProposalDAO.findProposalByProposalID(id);

		// 1st Get the Proposal, then get the Pi, CoPi and SP attached to it
		List<InvestigatorRefAndPosition> investigatorList = new ArrayList<InvestigatorRefAndPosition>();

		// For now I'm going to handle this boolean here...
		boolean isAdmin = false;
		// The getSupervisory method we'll call wants a boolean "isAdmin" this
		// is just used to define
		// whether or not someone is in an administrative position.
		// For example: when we want a department chair, we need their college
		// and their department that
		// they are from, but if we want a dean, we just need their college,
		// because they're the dean
		// of the college, and not part of a department under that college
		// The boolean tells the getSuper method which search call it needs to
		// make, for now
		// this is done for simplicity
		if (posTitle.equals("Dean")) {
			isAdmin = true;
		}

		investigatorList.add(checkProposal.getInvestigatorInfo().getPi());

		if (!checkProposal.getInvestigatorInfo().getCo_pi().isEmpty()) {
			for (InvestigatorRefAndPosition coPi : checkProposal
					.getInvestigatorInfo().getCo_pi()) {
				investigatorList.add(coPi);
			}
		}
		// for (InvestigatorRefAndPosition senior : checkProposal
		// .getInvestigatorInfo().getSeniorPersonnel()) {
		// investigatorList.add(senior);
		// } //Apparently we do not need the supers for senior personnel

		ArrayList<UserProfile> supervisorsList = new ArrayList<UserProfile>();
		// For each person on this list, get their supervisory personnel, and
		// add them to a list,
		// but avoid duplicate entries.

		// For each investigator (pi, copi, sp) in the list of them...
		// get their department, then from that department, get the desired
		// position title (chair, dean, etc...)
		// and add those supervisors to the list
		// This may result in duplicate entries being added to the list but we
		// will handle this with a nest for loop
		// Hopefully this does not result in a giant run time

		// 2nd Find out all of their supervisory personnel
		for (InvestigatorRefAndPosition investigator : investigatorList) {
			List<UserProfile> tempList = newUserProfileDAO
					.getSupervisoryPersonnels(investigator.getCollege(),
							investigator.getDepartment(), posTitle, isAdmin);
			for (UserProfile profs : tempList) {
				if (!supervisorsList.contains(profs)) {
					supervisorsList.add(profs);
				}
			}
		}

		// 3rd Evaluate if these personnel have "signed" the proposal
		List<SignatureInfo> checkSigs = checkProposal.getSignatureInfo();
		boolean isOnList = false;
		int sigsCount = 0;
		for (UserProfile superProfs : supervisorsList) {
			for (SignatureInfo sigsProfs : checkSigs) {
				if (sigsProfs.getFullName().equals(superProfs.getFullName())) {
					if (sigsProfs.getPositionTitle().equals(posTitle)) {
						isOnList = true;
					}
				}

			}
			if (isOnList) {
				sigsCount++;
				isOnList = false;
			}
		}

		if (sigsCount == supervisorsList.size()) {
			return true;
		} else {
			return false;
		}
	}

}
