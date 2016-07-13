package gpms.model;

import static org.junit.Assert.assertTrue;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

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
	@Ignore
	public void testAllPISigned() throws UnknownHostException {
		boolean allSigned = true;
		List<Proposal> propList = newProposalDAO.findAllProposals();
		for (Proposal prop : propList) {
			if (!getPISignedStatusForProposal(prop.getId())) {
				allSigned = false;
			}
		}

		assertTrue(allSigned);
	}

	@Test
	@Ignore
	public void testAllCoPiSigned() throws UnknownHostException {
		boolean allSigned = true;
		List<Proposal> propList = newProposalDAO.findAllProposals();
		for (Proposal prop : propList) {
			if (!getCoPiSignedStatusForProposal(prop.getId())) {
				allSigned = false;
			}
		}

		assertTrue(allSigned);
	}

	@Test
	@Ignore
	public void testAllDeanSigned() throws UnknownHostException {
		boolean allSigned = true;
		List<Proposal> propList = newProposalDAO.findAllProposals();
		for (Proposal prop : propList) {
			if (!getSignedStatusForAProposal(prop.getId(), "Dean")) {
				allSigned = false;
			}
		}

		assertTrue(allSigned);
	}

	@Test
	@Ignore
	public void testAllDepartmentChairSigned() throws UnknownHostException {
		boolean allSigned = true;
		List<Proposal> propList = newProposalDAO.findAllProposals();
		for (Proposal prop : propList) {
			if (!getSignedStatusForAProposal(prop.getId(), "Department Chair")) {
				allSigned = false;
			}
		}

		assertTrue(allSigned);
	}

	@Test
	public void verifyNoDuplicatesExist() throws UnknownHostException {
		boolean noDupes = true;
		List<Proposal> propList = newProposalDAO.findAllProposals();
		for (Proposal prop : propList) {

			for (SignatureInfo sigs : prop.getSignatureInfo()) {
				int unique = 0;
				for (SignatureInfo otherSigs : prop.getSignatureInfo()) {
					if (sigs.equals(otherSigs)) {
						unique++;
					}
					if (unique > 1) {
						noDupes = false;
					}
				}
			}
		}
		assertTrue(noDupes);
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
	 * @param id
	 *            the ID of the proposal we need to query for
	 * @param posTitle
	 *            the position title we want to check
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
		boolean isOnList = true;
		ArrayList<String> sigids = new ArrayList<String>();

		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		for (UserProfile superProfs : supervisorsList) {
			if (!sigids.contains(superProfs.getId().toString())) {
				isOnList = false;
			}
		}

		return isOnList;
	}

	/**
	 * Use this method to find out if all PI's have signed the proposal (should
	 * just be one pi, but will check all of them
	 * 
	 * @param id
	 *            the id of the proposal we want to check
	 * @param investigatorType
	 *            pi to check pi, copi to check copi
	 * @return true if all investigators have signed
	 * @throws UnknownHostException
	 */
	public boolean getPISignedStatusForProposal(ObjectId id)
			throws UnknownHostException {
		Proposal checkProposal = newProposalDAO.findProposalByProposalID(id);
		ArrayList<String> sigids = new ArrayList<String>();
		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		if (sigids.contains(checkProposal.getInvestigatorInfo().getPi()
				.getUserProfileId())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This method will verify that all CoPi's have signed the proposal
	 * 
	 * @param id
	 *            ID of the proposal to check
	 * @return true if all CoPI's have signed
	 * @throws UnknownHostException
	 */
	public boolean getCoPiSignedStatusForProposal(ObjectId id)
			throws UnknownHostException {

		boolean allCoPiSigned = true;

		Proposal checkProposal = newProposalDAO.findProposalByProposalID(id);
		ArrayList<String> sigids = new ArrayList<String>();
		for (SignatureInfo sigInfo : checkProposal.getSignatureInfo()) {
			sigids.add(sigInfo.getUserProfileId());
		}

		for (InvestigatorRefAndPosition profs : checkProposal
				.getInvestigatorInfo().getCo_pi()) {
			if (!sigids.contains(profs.getUserProfileId())) {
				allCoPiSigned = false;
			}
		}

		return allCoPiSigned;
	}

}
