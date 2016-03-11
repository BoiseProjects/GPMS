package gpms.dao;

import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.InvestigatorInfo;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.ProjectInfo;
import gpms.model.ProjectLocation;
import gpms.model.ProjectPeriod;
import gpms.model.ProjectType;
import gpms.model.Proposal;
import gpms.model.SignatureInfo;
import gpms.model.SponsorAndBudgetInfo;
import gpms.model.Status;
import gpms.model.TypeOfRequest;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

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
	//final int MAXIMUM_PROPOSALS = 10; // Adjust this to make more or less
										// profiles with the generator.
										//Currently unused, atm uses a list of users.

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
	public void creationTest() throws UnknownHostException {
		// We'll make 100 proposals. Each user will be made to have one
		// proposal.
		// We'll have up to 4 co-pi's added, and up to 2 senior personnel added
		List<UserProfile> masterList = newUserProfileDAO.findAll();
		List<UserProfile> pullList = newUserProfileDAO.findAll();
		int propNumb = 1;

		while (!pullList.isEmpty()) {
			// Remove a user from the list
			Random rand = new Random();
			int choice = rand.nextInt(pullList.size());

			UserProfile propProfile = pullList.remove(choice);

			// This will populate the investigator info position with details
			// Currently this is set up to just use the first pos det item from
			// the list of them from each user
			Proposal newProposal = new Proposal();

			// ProjectInfo newPI = new ProjectInfo();
			//
			// // Set Proposal Details here
			// ProjectLocation newPL = new ProjectLocation();
			// newPL.setOffCampus(true);
			//
			// newPI.setProjectLocation(newPL);
			//
			// ProjectPeriod newPP = new ProjectPeriod();
			// newPP.setFrom(new Date());
			//
			// // SimpleDateFormat sdf = new SimpleDateFormat(
			// // "yyyy-MM-dd'T'HH:mm:ssX");
			// Calendar c = Calendar.getInstance();
			// c.setTime(new Date()); // Now use today date.
			// c.add(Calendar.DATE, 5); // Adding 5 days
			// newPP.setTo(c.getTime());
			//
			// newPI.setProjectPeriod(newPP);
			//
			// TypeOfRequest newTR = new TypeOfRequest();
			// newTR.setContinuation(true);
			// newPI.setTypeOfRequest(newTR);
			//
			// ProjectType newPT = new ProjectType();
			// newPT.setIsResearchApplied(true);
			// newPI.setProjectType(newPT);
			//
			// c.add(Calendar.DATE, 60); // Adding 60 days
			//
			// newPI.setDueDate(c.getTime());
			//
			// newProposal.setProjectInfo(newPI);

			// --------------------------------------------------------
			// newProposalDAO.save(newProposal);


			
			

			InvestigatorInfo newInfo = new InvestigatorInfo();

			InvestigatorRefAndPosition newInvPos = new InvestigatorRefAndPosition();
			Random randomizer = new Random();
			int posChoice = randomizer.nextInt(propProfile.getDetails().size());

			newInvPos
					.setCollege(propProfile.getDetails(posChoice).getCollege());
			newInvPos.setDepartment(propProfile.getDetails(posChoice)
					.getDepartment());
			newInvPos.setPositionType(propProfile.getDetails(posChoice)
					.getPositionType());
			newInvPos.setPositionTitle(propProfile.getDetails(posChoice)
					.getPositionTitle());
			newInvPos.setUserProfileId(propProfile.getId().toString());
			newInvPos.setUserRef(propProfile);

			newInfo.setPi(newInvPos);

			int totalCops = rand.nextInt(5);
			for (int a = 0; a < totalCops; a++) {
				newInfo.getCo_pi().add(makeCoPI(masterList, newInfo));

			}

			int totalSeniors = rand.nextInt(2) + 1;
			for (int b = 0; b < totalSeniors; b++) {
				newInfo.getSeniorPersonnel().add(
						makeSenior(masterList, newInfo));
			}

			newProposal.setInvestigatorInfo(newInfo);
			
			
			//Begin Signature Info Section


			//I have an idea here...
			//On Odd number proposals I'm going to try and fill out the Signature fields with 
			//appropriate signees, so the right members of each department sign it, and try 
			//and fill the fields completely so hopefully my signature check tests pass if
			//all systems are go with this
//
			SignatureInfo newSignInfo;
			
			ArrayList<String> deptsList = new ArrayList<String>();
			deptsList.add(newInfo.getPi().getDepartment());
			for(InvestigatorRefAndPosition copi : newInfo.getCo_pi())
			{
				if(!deptsList.contains(copi.getDepartment()))
				{
					deptsList.add(copi.getDepartment());
				}
			}

			for(InvestigatorRefAndPosition seniorp : newInfo.getSeniorPersonnel())
			{
				if(!deptsList.contains(seniorp.getDepartment()))
				{
					deptsList.add(seniorp.getDepartment());
				}
			}

			List<UserProfile> deanList=null;
			List<UserProfile> deptChairList=null;
			List<UserProfile> IRBList=null;

			for(String dept : deptsList)
			{
				deanList = newUserProfileDAO.getSupervisoryPersonnelByTitle("Dean", dept);
				deptChairList = newUserProfileDAO.getSupervisoryPersonnelByTitle("Department Chair", dept);
				IRBList = newUserProfileDAO.getSupervisoryPersonnelByTitle("IRB", dept);
			}

			if(!(propNumb%2==0))
			{
				if(deanList.size()>0){
					System.out.println("deanList not empty");
				for(UserProfile dean : deanList)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(dean.getFullName());
					newSignInfo.setPositionTitle("Dean");
					newSignInfo.setUserProfileId(dean.getId().toString());
					newSignInfo.setSignature(dean.getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}else{System.out.println("No deans in database");}
//				
				if(deptChairList.size()>0){
					System.out.println("chair list not empty");
				for(UserProfile dChair : deptChairList)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(dChair.getFullName());
					newSignInfo.setPositionTitle("Department Chair");
					newSignInfo.setUserProfileId(dChair.getId().toString());
					newSignInfo.setSignature(dChair.getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}else{System.out.println("no chairs in database");}
//				
				if(IRBList.size()>0){
				for(UserProfile irb : IRBList)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(irb.getFullName());
					newSignInfo.setPositionTitle("IRB");
					newSignInfo.setUserProfileId(irb.getId().toString());
					newSignInfo.setSignature(irb.getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}
			} //this was here,
//
//			//On even number proposals, I'm going to purposefully screw this up, so that I can see
//			//failed tests when I'm running a signature check
//			//Meaning I want inadequate numbers of signatures, not exactly the wrong people signing
			if((propNumb%2)==0)
			{
				Random rDean, rChair, rirb;
				rDean = new Random();
				rChair = new Random();
				rirb = new Random();
//				
//				
				if(deanList.size()>0){
					int rando = rDean.nextInt(deanList.size());
				for(int a = 0; a < rando; a++)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(deanList.get(a).getFullName());
					newSignInfo.setPositionTitle("Dean");
					newSignInfo.setUserProfileId(deanList.get(a).getId().toString());
					newSignInfo.setSignature(deanList.get(a).getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}
//				
				if(deptChairList.size()>0){
					int rando = rChair.nextInt(deptChairList.size());
				for(int b = 0; b < rando; b++)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(deptChairList.get(b).getFullName());
					newSignInfo.setPositionTitle("Department Chair");
					newSignInfo.setUserProfileId(deptChairList.get(b).getId().toString());
					newSignInfo.setSignature(deptChairList.get(b).getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}
//				
				if(IRBList.size()>0){
					int rando = rirb.nextInt(IRBList.size());
				for(int c = 0; c < rando; c++)
				{
					newSignInfo = new SignatureInfo();
					newSignInfo.setFullName(IRBList.get(c).getFullName());
					newSignInfo.setPositionTitle("IRB");
					newSignInfo.setUserProfileId(IRBList.get(c).getId().toString());
					newSignInfo.setSignature(IRBList.get(c).getFullName());
					newProposal.getSignatureInfo().add(newSignInfo);
				}}
			}

			//End Signature Info Section

			SponsorAndBudgetInfo newSandBud = new SponsorAndBudgetInfo();

			int sponsorChoice = rand.nextInt(2);

			if (sponsorChoice == 0) {
				newSandBud.addGrantingAgency("NSF");
				newSandBud.addGrantingAgency("NASA");
			}
			if (sponsorChoice == 1) {
				newSandBud.addGrantingAgency("Idaho STEM Grant");
				newSandBud.addGrantingAgency("BSU");
			}

			newSandBud.setDirectCosts(1000.00);
			newSandBud.setTotalCosts(21000.00);
			newSandBud.setFACosts(1000.00);
			newSandBud.setFARate(10.00);

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
			newPT.setIsResearchApplied(true);
			newProjInf.setProjectType(newPT);

			c.add(Calendar.DATE, 60); // Adding 60 days

			newProjInf.setDueDate(c.getTime());

			newProposal.setProjectInfo(newProjInf);

			newProposal.setDateCreated(new Date());

			//newProposal.getProposalStatus().add(Status.NOTSUBMITTEDBYPI);

			newProposalDAO.save(newProposal);

			propNumb++;

		}

	}

	public InvestigatorRefAndPosition makeCoPI(List<UserProfile> theMasterList,
			InvestigatorInfo theInfo) {
		Random rand = new Random();
		int coChoice = rand.nextInt(theMasterList.size());
		UserProfile copProfile = theMasterList.get(coChoice);
		InvestigatorRefAndPosition newInvPos = new InvestigatorRefAndPosition();

		newInvPos.setCollege(copProfile.getDetails(0).getCollege());
		newInvPos.setDepartment(copProfile.getDetails(0).getDepartment());
		newInvPos.setPositionType(copProfile.getDetails(0).getPositionType());
		newInvPos.setPositionTitle(copProfile.getDetails(0).getPositionTitle());
		newInvPos.setUserProfileId(copProfile.getId().toString());
		newInvPos.setUserRef(copProfile);

		return newInvPos;
	}

	public InvestigatorRefAndPosition makeSenior(
			List<UserProfile> theMasterList, InvestigatorInfo theInfo) {
		Random rand = new Random();
		int coChoice = rand.nextInt(theMasterList.size());
		UserProfile copProfile = theMasterList.get(coChoice);
		InvestigatorRefAndPosition newInvPos = new InvestigatorRefAndPosition();

		newInvPos.setCollege(copProfile.getDetails(0).getCollege());
		newInvPos.setDepartment(copProfile.getDetails(0).getDepartment());
		newInvPos.setPositionType(copProfile.getDetails(0).getPositionType());
		newInvPos.setPositionTitle(copProfile.getDetails(0).getPositionTitle());
		newInvPos.setUserProfileId(copProfile.getId().toString());
		newInvPos.setUserRef(copProfile);

		return newInvPos;
	}
}
