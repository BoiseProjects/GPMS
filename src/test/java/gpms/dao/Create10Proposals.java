package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.model.InvestigatorInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.PositionDetails;
import gpms.model.ProjectInfo;
import gpms.model.ProjectLocation;
import gpms.model.ProjectPeriod;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.SignatureInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.TypeOfRequest;
import gpms.model.UserAccount;
import gpms.model.UserProfile;
import gpms.utils.SerializationHelper;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;

import com.mongodb.MongoClient;

//This class should either be run as part of the suite SUITECreateXUsersAndProposals.java
//or after the db has been populated.

public class Create10Proposals {
	MongoClient mongoClient;
	Morphia morphia;
	UserAccountDAO newUserAccountDAO;
	UserProfileDAO newUserProfileDAO;
	ProposalDAO newProposalDAO;
	String dbName = "db_gpms";
	final int MAXIMUM_PROPOSALS = 1000; // Adjust this to make more or less
										// profiles with the generator.

	private int SIGNATURE_MODE = 1;

	// This switch can be used to determine the signature creation mode you
	// would like to use.
	// SIGNATURE_MODE=1 PI's will sign, CoPI's will sign.
	// SIGNATURE_MODE=2 Only Deans will be pulled to populate the signature list
	// SIGNATURE_MODE=3 Only Department Chairs will be used to populate
	// signatures
	// SIGNATURE_MODE=5 Pi's will sign, CoPi's will sign, Deans will sign, and
	// Department Chairs will sign *//Unimplemented

	@Before
	public void initiate() {
		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		newUserAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		newUserProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		newProposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@Test
	public void creationTest() throws Exception {
		// We'll make 100 proposals. Each user will be made to have one
		// proposal.
		// We'll have up to 4 co-pi's added, and up to 2 senior personnel added
		List<UserProfile> masterList = newUserProfileDAO
				.findAllUserForInvestigator();
		int propNumb = 1;

		while (!masterList.isEmpty()) {
			// Remove a user from the list
			Random rand = new Random();
			int choice = rand.nextInt(masterList.size());
			UserProfile propProfile = masterList.get(choice);
			masterList.remove(choice);

			List<UserProfile> dupMasterList = masterList;

			// dupMasterList.remove(choice);
			// This will populate the investigator info position with details
			// Currently this is set up to just use the first pos det item from
			// the list of them from each user
			Proposal newProposal = new Proposal();

			InvestigatorInfo newInfo = new InvestigatorInfo();

			InvestigatorRefAndPosition newInvPos = new InvestigatorRefAndPosition();

			for (PositionDetails details : propProfile.getDetails()) {
				if (details.getPositionType().equals(
						"Tenured/tenure-track faculty")
						|| details.getPositionType().equals(
								"Non-tenure-track research faculty")) {
					newInvPos.setCollege(details.getCollege());
					newInvPos.setDepartment(details.getDepartment());
					newInvPos.setPositionType(details.getPositionType());
					newInvPos.setPositionTitle(details.getPositionTitle());
					newInvPos.setUserProfileId(propProfile.getId().toString());
					newInvPos.setUserRef(propProfile);

					newInfo.setPi(newInvPos);
					break;
				}
			}

			int totalCops = 0;
			if (dupMasterList.size() > 0) {
				totalCops = rand.nextInt(5);
			}
			for (int a = 0; a < totalCops; a++) {
				int coPIChoice = rand.nextInt(dupMasterList.size());
				UserProfile coPIProfile = dupMasterList.get(coPIChoice);
				dupMasterList.remove(coPIChoice);
				newInvPos = new InvestigatorRefAndPosition();

				for (PositionDetails details : coPIProfile.getDetails()) {
					if (details.getPositionType().equals(
							"Tenured/tenure-track faculty")
							|| details.getPositionType().equals(
									"Non-tenure-track research faculty")
							|| details.getPositionType().equals(
									"Teaching faculty")) {
						newInvPos.setCollege(details.getCollege());
						newInvPos.setDepartment(details.getDepartment());
						newInvPos.setPositionType(details.getPositionType());
						newInvPos.setPositionTitle(details.getPositionTitle());
						newInvPos.setUserProfileId(coPIProfile.getId()
								.toString());
						newInvPos.setUserRef(coPIProfile);

						newInfo.getCo_pi().add(newInvPos);
						break;
					}
				}
			}

			int totalSeniors = 0;
			if (dupMasterList.size() > 0) {
				totalSeniors = rand.nextInt(3);
			}
			for (int b = 0; b < totalSeniors; b++) {
				int seniorChoice = rand.nextInt(dupMasterList.size());
				UserProfile seniorProfile = dupMasterList.get(seniorChoice);
				dupMasterList.remove(seniorChoice);
				newInvPos = new InvestigatorRefAndPosition();

				for (PositionDetails details : seniorProfile.getDetails()) {
					if (details.getPositionType().equals(
							"Tenured/tenure-track faculty")
							|| details.getPositionType().equals(
									"Non-tenure-track research faculty")
							|| details.getPositionType().equals(
									"Teaching faculty")) {
						newInvPos.setCollege(details.getCollege());
						newInvPos.setDepartment(details.getDepartment());
						newInvPos.setPositionType(details.getPositionType());
						newInvPos.setPositionTitle(details.getPositionTitle());
						newInvPos.setUserProfileId(seniorProfile.getId()
								.toString());
						newInvPos.setUserRef(seniorProfile);

						newInfo.getSeniorPersonnel().add(newInvPos);
						break;
					}
				}
			}

			newProposal.setInvestigatorInfo(newInfo);

			// SignatureInfo newSignInfo = new SignatureInfo();
			// newProposal.getSignatureInfo().add(newSignInfo);

			// Begin Signature Info Section

			// TODO : first add all investigators signatures then only others
			// can sign it

			// I have an idea here...
			// On Odd number proposals I'm going to try and fill out the
			// Signature fields with
			// appropriate signees, so the right members of each department sign
			// it, and try
			// and fill the fields completely so hopefully my signature check
			// tests pass if
			// all systems are go with this
			//

			// //////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////
			// ///INTRODUCING THE NEW SIMPLE WAY TO DO THIS: ONE AT A TIME
			// //////
			// ///SEE THE DEBUG SWITCH AT THE TOP OF THE CLASS TO CHOOSE
			// THE/////
			// ///METHOD OF CREATION FOR THE SIGNATURE INFO OBJECT. //////
			// ///THIS WILL BE USED WITH THE TESTSIGNATURECHECKING CLASS //////
			// ///DUPLICATE KEY ERRORS THAT COME FROM NOT FILLING OUT ALL THE
			// ///
			// ///FIELDS WHEN SOMETHING IS CREATED IN THE MONGO DB
			// //////////////
			// //////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////
			SignatureInfo newSignInfo = new SignatureInfo();
			if (SIGNATURE_MODE == 1) {
				// This mode will populate the signatures with all
				// of the PI's and CoPI's on the proposal

				// PI Signs proposal
				newProposal.getInvestigatorInfo().getPi();
				newSignInfo = new SignatureInfo();
				newSignInfo.setUserProfileId(newProposal.getInvestigatorInfo()
						.getPi().getUserProfileId());
				newSignInfo.setFullName(newProposal.getInvestigatorInfo()
						.getPi().getUserRef().getFullName());
				newSignInfo.setSignature(newProposal.getInvestigatorInfo()
						.getPi().getUserRef().getFullName());
				newSignInfo.setPositionTitle("PI");
				newSignInfo.setSignedDate(new Date());
				newSignInfo.setNote("This is Note from PI");
				newProposal.getSignatureInfo().add(newSignInfo);

				// CoPI's sign the proposal
				for (InvestigatorRefAndPosition cops : newProposal
						.getInvestigatorInfo().getCo_pi()) {
					newSignInfo = new SignatureInfo();
					newSignInfo.setUserProfileId(cops.getUserProfileId());
					newSignInfo.setFullName(cops.getUserRef().getFullName());
					newSignInfo.setSignature(cops.getUserRef().getFullName());
					newSignInfo.setPositionTitle("Co-PI");
					newSignInfo.setSignedDate(new Date());
					newSignInfo.setNote("This is Note from Co-PI");
					newProposal.getSignatureInfo().add(newSignInfo);
				}
			}

			if (SIGNATURE_MODE == 2) {
				// This will populate the signatures with Deans only

				List<String> collegeList = new ArrayList<String>();
				List<UserProfile> deanList = new ArrayList<UserProfile>();

				collegeList.add(newInfo.getPi().getCollege());
				for (InvestigatorRefAndPosition copi : newInfo.getCo_pi()) {
					if (!collegeList.contains(copi.getCollege())) {
						collegeList.add(copi.getCollege());
					}
				}

				for (String college : collegeList) {
					deanList.addAll(newUserProfileDAO.getSupervisoryPersonnels(
							college, "", "Dean", true));
				}

				if (deanList.size() > 0) {
					System.out.println("deanList not empty");
					for (UserProfile dean : deanList) {
						newSignInfo = new SignatureInfo();
						newSignInfo.setFullName(dean.getFullName());
						newSignInfo.setPositionTitle("Dean");
						newSignInfo.setUserProfileId(dean.getId().toString());
						newSignInfo.setSignature(dean.getFullName());
						newProposal.getSignatureInfo().add(newSignInfo);
					}

				} else {
					System.out.println("Dean list is empty");
				}
			}

			if (SIGNATURE_MODE == 3) {
				// This will populate the signatures with Department Chairs only

				List<String> collegeList = new ArrayList<String>();
				List<String> departmentList = new ArrayList<String>();
				List<UserProfile> chairList = new ArrayList<UserProfile>();
				List<UserProfile> coChairList = new ArrayList<UserProfile>();

				String college, department;

				college = newProposal.getInvestigatorInfo().getPi()
						.getCollege();
				department = newProposal.getInvestigatorInfo().getPi()
						.getDepartment();

				chairList.addAll(newUserProfileDAO.getSupervisoryPersonnels(
						college, department, "Department Chair", false));

				for (InvestigatorRefAndPosition copi : newProposal
						.getInvestigatorInfo().getCo_pi()) {
					coChairList = newUserProfileDAO.getSupervisoryPersonnels(
							copi.getCollege(), copi.getDepartment(),
							"Department Chair", false);

					for (UserProfile prof : coChairList) {
						if (!chairList.contains(prof)) {
							chairList.add(prof);
						}
					}

				}

				if (chairList.size() > 0) {
					System.out.println("chairList not empty");
					for (UserProfile chair : chairList) {
						newSignInfo = new SignatureInfo();
						newSignInfo.setFullName(chair.getFullName());
						newSignInfo.setPositionTitle("Department Chair");
						newSignInfo.setUserProfileId(chair.getId().toString());
						newSignInfo.setSignature(chair.getFullName());
						newProposal.getSignatureInfo().add(newSignInfo);
					}

				} else {
					System.out.println("Chair list is empty");
				}

			}

			// //////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////
			// ///FOR NOW I AM DEACTIVATING THIS, I WILL IMPLEMENT A SIMPLER
			// ////
			// ///WAY OF CREATING SIGNATURES WITH A PROPOSAL USING A DEBUG
			// //////
			// ///TYPE SWITCH, THIS SHOULD MAKE IT EASIER, SO I DON'T HAVE
			// //////
			// ///TO WORRY ABOUT CREATING A PROPOSAL AND AVOIDING ALL OF THE
			// ////
			// ///DUPLICATE KEY ERRORS THAT COME FROM NOT FILLING OUT ALL THE
			// ///
			// ///FIELDS WHEN SOMETHING IS CREATED IN THE MONGO DB
			// //////////////
			// //////////////////////////////////////////////////////////////////
			// //////////////////////////////////////////////////////////////////

			//
			// List<String> deptsList = new ArrayList<String>();
			// List<String> collegeList = new ArrayList<String>();
			// List<UserProfile> deanList = new ArrayList<UserProfile>();
			// List<UserProfile> deptChairList = new ArrayList<UserProfile>();
			// List<UserProfile> IRBList = new ArrayList<UserProfile>();
			//
			// deptsList.add(newInfo.getPi().getDepartment());
			// for (InvestigatorRefAndPosition copi : newInfo.getCo_pi()) {
			// if (!deptsList.contains(copi.getDepartment())) {
			// deptsList.add(copi.getDepartment());
			// }
			// }
			//
			// collegeList.add(newInfo.getPi().getCollege());
			// for (InvestigatorRefAndPosition copi : newInfo.getCo_pi()) {
			// if (!collegeList.contains(copi.getCollege())) {
			// collegeList.add(copi.getCollege());
			// }
			// }
			//
			// // for (InvestigatorRefAndPosition seniorp : newInfo
			// // .getSeniorPersonnel()) {
			// // if (!deptsList.contains(seniorp.getDepartment())) {
			// // deptsList.add(seniorp.getDepartment());
			// // }
			// // }
			//
			// for(String college : collegeList)
			// {
			// deanList.addAll(newUserProfileDAO.getSupervisoryPersonnels(college,
			// "Dean", "", true));
			// for (String dept : deptsList)
			// {
			// deptChairList.addAll(newUserProfileDAO.getSupervisoryPersonnels(
			// college, dept, "Department Chair", false));
			// IRBList.addAll(newUserProfileDAO.getSupervisoryPersonnels(college,
			// dept, "IRB", false));
			// }
			// }
			//
			//
			// if (!(propNumb % 2 == 0)) {
			// if (deanList.size() > 0) {
			// System.out.println("deanList not empty");
			// for (UserProfile dean : deanList) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(dean.getFullName());
			// newSignInfo.setPositionTitle("Dean");
			// newSignInfo.setUserProfileId(dean.getId().toString());
			// newSignInfo.setSignature(dean.getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// } else {
			// System.out.println("No deans in database");
			// }
			// //
			// if (deptChairList.size() > 0) {
			// System.out.println("chair list not empty");
			// for (UserProfile dChair : deptChairList) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(dChair.getFullName());
			// newSignInfo.setPositionTitle("Department Chair");
			// newSignInfo.setUserProfileId(dChair.getId().toString());
			// newSignInfo.setSignature(dChair.getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// } else {
			// System.out.println("no chairs in database");
			// }
			// //
			// if (IRBList.size() > 0) {
			// for (UserProfile irb : IRBList) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(irb.getFullName());
			// newSignInfo.setPositionTitle("IRB");
			// newSignInfo.setUserProfileId(irb.getId().toString());
			// newSignInfo.setSignature(irb.getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// }
			// } // this was here,
			// //
			// // //On even number proposals, I'm going to purposefully screw
			// // this
			// // up, so that I can see
			// // //failed tests when I'm running a signature check
			// // //Meaning I want inadequate numbers of signatures, not
			// // exactly
			// // the wrong people signing
			// if ((propNumb % 2) == 0) {
			// Random rDean, rChair, rirb;
			// rDean = new Random();
			// rChair = new Random();
			// rirb = new Random();
			// //
			// //
			// if (deanList.size() > 0) {
			// int rando = rDean.nextInt(deanList.size());
			// for (int a = 0; a < rando; a++) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(deanList.get(a).getFullName());
			// newSignInfo.setPositionTitle("Dean");
			// newSignInfo.setUserProfileId(deanList.get(a).getId()
			// .toString());
			// newSignInfo.setSignature(deanList.get(a).getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// }
			// //
			// if (deptChairList.size() > 0) {
			// int rando = rChair.nextInt(deptChairList.size());
			// for (int b = 0; b < rando; b++) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(deptChairList.get(b)
			// .getFullName());
			// newSignInfo.setPositionTitle("Department Chair");
			// newSignInfo.setUserProfileId(deptChairList.get(b)
			// .getId().toString());
			// newSignInfo.setSignature(deptChairList.get(b)
			// .getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// }
			// //
			// if (IRBList.size() > 0) {
			// int rando = rirb.nextInt(IRBList.size());
			// for (int c = 0; c < rando; c++) {
			// newSignInfo = new SignatureInfo();
			// newSignInfo.setFullName(IRBList.get(c).getFullName());
			// newSignInfo.setPositionTitle("IRB");
			// newSignInfo.setUserProfileId(IRBList.get(c).getId()
			// .toString());
			// newSignInfo.setSignature(IRBList.get(c).getFullName());
			// newProposal.getSignatureInfo().add(newSignInfo);
			// }
			// }
			// }

			// End Signature Info Section

			SponsorAndBudgetInfo newSandBud = new SponsorAndBudgetInfo();

			int sponsorChoice = rand.nextInt(2);

			if (sponsorChoice == 0) {
				newSandBud.getGrantingAgency().add("NSF");
				newSandBud.getGrantingAgency().add("NASA");
			}
			if (sponsorChoice == 1) {
				newSandBud.getGrantingAgency().add("Idaho STEM Grant");
				newSandBud.getGrantingAgency().add("BSU");
			}

			newSandBud.setDirectCosts(1000.00);
			newSandBud.setTotalCosts(21000.00);
			newSandBud.setFaCosts(1000.00);
			newSandBud.setFaRate(10.00);

			newProposal.setSponsorAndBudgetInfo(newSandBud);

			newProposal.setProposalNo(propNumb);

			ProjectInfo newProjInf = new ProjectInfo();

			String nameString = "Proposal" + propNumb;
			newProjInf.setProjectTitle(nameString);

			// Set Proposal Details here
			ProjectLocation newPL = new ProjectLocation();
			newPL.setOffCampus(true);

			newProjInf.setProjectLocation(newPL);

			ProjectPeriod newPP = new ProjectPeriod();
			newPP.setFrom(new Date());

			// SimpleDateFormat sdf = new SimpleDateFormat(
			// "yyyy-MM-dd'T'HH:mm:ssX");
			Calendar c = Calendar.getInstance();
			c.setTime(new Date()); // Now use today date.
			c.add(Calendar.DATE, 5); // Adding 5 days
			newPP.setTo(c.getTime());

			newProjInf.setProjectPeriod(newPP);

			TypeOfRequest newTR = new TypeOfRequest();
			newTR.setContinuation(true);
			newProjInf.setTypeOfRequest(newTR);

			ProjectType newPT = new ProjectType();
			newPT.setResearchApplied(true);
			newProjInf.setProjectType(newPT);

			c.add(Calendar.DATE, 60); // Adding 60 days

			newProjInf.setDueDate(c.getTime());

			newProposal.setProjectInfo(newProjInf);

			newProposal.setDateCreated(new Date());

			// newProposal.getProposalStatus().add(Status.NOTSUBMITTEDBYPI);

			newProposalDAO.save(newProposal);

			propNumb++;

		}

	}
}
