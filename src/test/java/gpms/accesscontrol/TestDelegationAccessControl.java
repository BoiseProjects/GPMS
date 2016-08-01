package gpms.accesscontrol;

import static org.junit.Assert.assertEquals;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.dao.UserAccountDAO;
import gpms.dao.UserProfileDAO;
import gpms.model.InvestigatorRefAndPosition;
import gpms.model.Proposal;
import gpms.model.SignatureByAllUsers;
import gpms.model.SignatureUserInfo;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mongodb.morphia.Morphia;
import org.wso2.balana.ObligationResult;
import org.wso2.balana.ctx.AbstractResult;
import org.wso2.balana.ctx.AttributeAssignment;
import org.wso2.balana.xacml3.Advice;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.mongodb.MongoClient;

public class TestDelegationAccessControl {

	Accesscontrol ac = null;

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "db_gpms";
	UserAccountDAO userAccountDAO = null;
	UserProfileDAO userProfileDAO = null;
	ProposalDAO proposalDAO = null;

	@Before
	public void setUp() throws Exception {
		ac = new Accesscontrol();

		mongoClient = MongoDBConnector.getMongo();
		morphia = new Morphia();
		morphia.map(UserProfile.class).map(UserAccount.class);
		userAccountDAO = new UserAccountDAO(mongoClient, morphia, dbName);
		userProfileDAO = new UserProfileDAO(mongoClient, morphia, dbName);
		proposalDAO = new ProposalDAO(mongoClient, morphia, dbName);
	}

	@After
	public void tearDown() throws Exception {
		ac = null;
	}

	@Test
	public void test() throws UnknownHostException {
		// ac.getXACMLdecision("Faculty", "Proposal", "Create");
		HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

		Multimap<String, String> subjectMap = ArrayListMultimap.create();
		subjectMap.put("position.title", "Department Chair");
		subjectMap.put("group", "Computer Science");
		// subjectMap.put("group", "Computer Engineering");

		attrMap.put("Subject", subjectMap);

		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		resourceMap.put("proposal.section", "Whole Proposal");
		resourceMap.put("ApprovedByDepartmentChair", "READYFORAPPROVAL");

		// resourceMap.put("proposal.section", "Certification/Signatures");
		// resourceMap.put("ApprovedByDepartmentChair", "READYFORAPPROVAL");
		// resourceMap.put("DeletedByPI", "NOTDELETED");

		attrMap.put("Resource", resourceMap);

		Multimap<String, String> actionMap = ArrayListMultimap.create();
		// actionMap.put("proposal.action", "Add");
		// actionMap.put("proposal.action", "Save");

		actionMap.put("proposal.action", "Approve");

		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// attrMap.put("Environment", environmentMap);

		String proposalID = "579c22109da462251c2b034f";
		String userProfileID = "5745f29ebcbb29192ce0d42f";

		// ObjectId proposalId = new ObjectId(proposalID);

		ObjectId id = new ObjectId(proposalID);
		Proposal existingProposal = proposalDAO.findProposalByProposalID(id);
		Boolean irbApprovalRequired = existingProposal.isIrbApprovalRequired();

		List<SignatureUserInfo> signatures = proposalDAO
				.findSignaturesExceptInvestigator(id, irbApprovalRequired);

		ObjectId authorId = new ObjectId(userProfileID);
		UserProfile authorProfile = userProfileDAO
				.findUserDetailsByProfileID(authorId);
		String authorFullName = authorProfile.getFullName();
		String authorUserName = authorProfile.getUserAccount().getUserName();

		signatures = proposalDAO.findSignaturesExceptInvestigator(id,
				irbApprovalRequired);

		StringBuffer contentProfile = new StringBuffer();

		contentProfile.append("<Content>");
		contentProfile.append("<ak:record xmlns:ak=\"http://akpower.org\">");
		contentProfile.append("<ak:proposal>");

		contentProfile.append("<ak:proposalid>");
		contentProfile.append(proposalID);
		contentProfile.append("</ak:proposalid>");

		contentProfile.append("<ak:proposaltitle>");
		contentProfile.append(existingProposal.getProjectInfo()
				.getProjectTitle());
		contentProfile.append("</ak:proposaltitle>");

		contentProfile.append("<ak:irbApprovalRequired>");
		contentProfile.append(existingProposal.isIrbApprovalRequired());
		contentProfile.append("</ak:irbApprovalRequired>");

		contentProfile.append("<ak:submittedbypi>");
		contentProfile.append(existingProposal.getSubmittedByPI().name());
		contentProfile.append("</ak:submittedbypi>");

		contentProfile.append("<ak:readyforsubmissionbypi>");
		contentProfile.append(existingProposal.isReadyForSubmissionByPI());
		contentProfile.append("</ak:readyforsubmissionbypi>");

		contentProfile.append("<ak:deletedbypi>");
		contentProfile.append(existingProposal.getDeletedByPI().name());
		contentProfile.append("</ak:deletedbypi>");

		contentProfile.append("<ak:approvedbydepartmentchair>");
		contentProfile.append(existingProposal.getChairApproval().name());
		contentProfile.append("</ak:approvedbydepartmentchair>");

		contentProfile.append("<ak:approvedbybusinessmanager>");
		contentProfile.append(existingProposal.getBusinessManagerApproval()
				.name());
		contentProfile.append("</ak:approvedbybusinessmanager>");

		contentProfile.append("<ak:approvedbyirb>");
		contentProfile.append(existingProposal.getIrbApproval().name());
		contentProfile.append("</ak:approvedbyirb>");

		contentProfile.append("<ak:approvedbydean>");
		contentProfile.append(existingProposal.getDeanApproval().name());
		contentProfile.append("</ak:approvedbydean>");

		contentProfile.append("<ak:approvedbyuniversityresearchadministrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorApproval().name());
		contentProfile
				.append("</ak:approvedbyuniversityresearchadministrator>");

		contentProfile
				.append("<ak:withdrawnbyuniversityresearchadministrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorWithdraw().name());
		contentProfile
				.append("</ak:withdrawnbyuniversityresearchadministrator>");

		contentProfile
				.append("<ak:submittedbyuniversityresearchadministrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorSubmission().name());
		contentProfile
				.append("</ak:submittedbyuniversityresearchadministrator>");

		contentProfile.append("<ak:approvedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorDeletion()
				.name());
		contentProfile.append("</ak:approvedbyuniversityresearchdirector>");

		contentProfile.append("<ak:deletedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorDeletion()
				.name());
		contentProfile.append("</ak:deletedbyuniversityresearchdirector>");

		contentProfile.append("<ak:archivedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorArchived()
				.name());
		contentProfile.append("</ak:archivedbyuniversityresearchdirector>");

		contentProfile.append("<ak:authorprofile>");
		// contentProfile.append("<ak:firstname>");
		// contentProfile.append(authorProfile.getFirstName());
		// contentProfile.append("</ak:firstname>");
		// contentProfile.append("<ak:middlename>");
		// contentProfile
		// .append(authorProfile.getMiddleName());
		// contentProfile.append("</ak:middlename>");
		//
		// contentProfile.append("<ak:lastname>");
		// contentProfile.append(authorProfile.getLastName());
		// contentProfile.append("</ak:lastname>");

		contentProfile.append("<ak:fullname>");
		contentProfile.append(authorFullName);
		contentProfile.append("</ak:fullname>");

		contentProfile.append("<ak:userid>");
		contentProfile.append(authorProfile.getId().toString());
		contentProfile.append("</ak:userid>");

		contentProfile.append("</ak:authorprofile>");

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

		contentProfile.append("<ak:currentdatetime>");
		contentProfile.append(dateFormat.format(new Date()));
		contentProfile.append("</ak:currentdatetime>");

		boolean signedByPI = false;
		boolean signedByAllCoPIs = false;
		boolean signedByAllChairs = false;
		boolean signedByAllBusinessManagers = false;
		boolean signedByAllDeans = false;
		boolean signedByAllIRBs = false;
		boolean signedByAllResearchAdmins = false;
		boolean signedByAllResearchDirectors = false;

		List<String> requiredPISign = new ArrayList<String>();
		List<String> existingPISign = new ArrayList<String>();
		List<String> requiredCoPISigns = new ArrayList<String>();
		List<String> existingCoPISigns = new ArrayList<String>();
		List<String> requiredChairSigns = new ArrayList<String>();
		List<String> existingChairSigns = new ArrayList<String>();
		List<String> requiredBusinessManagerSigns = new ArrayList<String>();
		List<String> existingBusinessManagerSigns = new ArrayList<String>();
		List<String> requiredDeanSigns = new ArrayList<String>();
		List<String> existingDeanSigns = new ArrayList<String>();
		List<String> requiredIRBSigns = new ArrayList<String>();
		List<String> existingIRBSigns = new ArrayList<String>();
		List<String> requiredResearchAdminSigns = new ArrayList<String>();
		List<String> existingResearchAdminSigns = new ArrayList<String>();
		List<String> requiredResearchDirectorSigns = new ArrayList<String>();
		List<String> existingResearchDirectorSigns = new ArrayList<String>();

		if (!existingProposal.getInvestigatorInfo().getPi().getUserRef()
				.isDeleted()) {
			contentProfile.append("<ak:pi>");
			contentProfile.append("<ak:fullname>");
			contentProfile.append(existingProposal.getInvestigatorInfo()
					.getPi().getUserRef().getFullName());
			contentProfile.append("</ak:fullname>");

			contentProfile.append("<ak:workemail>");
			contentProfile.append(existingProposal.getInvestigatorInfo()
					.getPi().getUserRef().getWorkEmails().get(0));
			contentProfile.append("</ak:workemail>");

			contentProfile.append("<ak:userid>");
			contentProfile.append(existingProposal.getInvestigatorInfo()
					.getPi().getUserProfileId());
			contentProfile.append("</ak:userid>");
			contentProfile.append("</ak:pi>");

			requiredPISign.add(existingProposal.getInvestigatorInfo().getPi()
					.getUserProfileId());
		}

		for (InvestigatorRefAndPosition copis : existingProposal
				.getInvestigatorInfo().getCo_pi()) {
			if (!copis.getUserRef().isDeleted()) {
				contentProfile.append("<ak:copi>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(copis.getUserRef().getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile
						.append(copis.getUserRef().getWorkEmails().get(0));
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(copis.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:copi>");

				requiredCoPISigns.add(copis.getUserProfileId());
			}
		}

		for (InvestigatorRefAndPosition seniors : existingProposal
				.getInvestigatorInfo().getSeniorPersonnel()) {
			if (!seniors.getUserRef().isDeleted()) {
				contentProfile.append("<ak:senior>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(seniors.getUserRef().getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(seniors.getUserRef().getWorkEmails()
						.get(0));
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(seniors.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:senior>");
			}
		}

		for (SignatureUserInfo signatureInfo : signatures) {
			switch (signatureInfo.getPositionTitle()) {
			case "Department Chair":
				contentProfile.append("<ak:chair>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:chair>");

				requiredChairSigns.add(signatureInfo.getUserProfileId());

				break;
			case "Business Manager":
				contentProfile.append("<ak:manager>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:manager>");

				requiredBusinessManagerSigns.add(signatureInfo
						.getUserProfileId());

				break;
			case "Dean":
				contentProfile.append("<ak:dean>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:dean>");

				requiredDeanSigns.add(signatureInfo.getUserProfileId());

				break;
			case "IRB":
				contentProfile.append("<ak:irb>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:irb>");

				requiredIRBSigns.add(signatureInfo.getUserProfileId());

				break;
			case "University Research Administrator":
				contentProfile.append("<ak:administrator>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:administrator>");

				requiredResearchAdminSigns
						.add(signatureInfo.getUserProfileId());

				break;
			case "University Research Director":
				contentProfile.append("<ak:director>");
				contentProfile.append("<ak:fullname>");
				contentProfile.append(signatureInfo.getFullName());
				contentProfile.append("</ak:fullname>");

				contentProfile.append("<ak:workemail>");
				contentProfile.append(signatureInfo.getEmail());
				contentProfile.append("</ak:workemail>");

				contentProfile.append("<ak:userid>");
				contentProfile.append(signatureInfo.getUserProfileId());
				contentProfile.append("</ak:userid>");
				contentProfile.append("</ak:director>");

				requiredResearchDirectorSigns.add(signatureInfo
						.getUserProfileId());

				break;
			}
		}

		signedByPI = existingPISign.containsAll(requiredPISign);

		signedByAllCoPIs = existingCoPISigns.containsAll(requiredCoPISigns);

		signedByAllChairs = existingChairSigns.containsAll(requiredChairSigns);

		signedByAllBusinessManagers = existingBusinessManagerSigns
				.containsAll(requiredBusinessManagerSigns);

		signedByAllDeans = existingDeanSigns.containsAll(requiredDeanSigns);

		signedByAllIRBs = existingIRBSigns.containsAll(requiredIRBSigns);

		signedByAllResearchAdmins = existingResearchAdminSigns
				.containsAll(requiredResearchAdminSigns);

		signedByAllResearchDirectors = existingResearchDirectorSigns
				.containsAll(requiredResearchDirectorSigns);

		requiredPISign.clear();
		existingPISign.clear();
		requiredCoPISigns.clear();
		existingCoPISigns.clear();
		requiredChairSigns.clear();
		existingChairSigns.clear();
		requiredBusinessManagerSigns.clear();
		existingBusinessManagerSigns.clear();
		requiredDeanSigns.clear();
		existingDeanSigns.clear();
		requiredIRBSigns.clear();
		existingIRBSigns.clear();
		requiredResearchAdminSigns.clear();
		existingResearchAdminSigns.clear();
		requiredResearchDirectorSigns.clear();
		existingResearchDirectorSigns.clear();

		contentProfile.append("<ak:signedByCurrentUser>");
		contentProfile.append(true);
		contentProfile.append("</ak:signedByCurrentUser>");

		contentProfile.append("<ak:signedByPI>");
		contentProfile.append(signedByPI);
		contentProfile.append("</ak:signedByPI>");

		contentProfile.append("<ak:signedByAllCoPIs>");
		contentProfile.append(signedByAllCoPIs);
		contentProfile.append("</ak:signedByAllCoPIs>");

		contentProfile.append("<ak:signedByAllChairs>");
		contentProfile.append(signedByAllChairs);
		contentProfile.append("</ak:signedByAllChairs>");

		contentProfile.append("<ak:signedByAllBusinessManagers>");
		contentProfile.append(signedByAllBusinessManagers);
		contentProfile.append("</ak:signedByAllBusinessManagers>");

		contentProfile.append("<ak:signedByAllDeans>");
		contentProfile.append(signedByAllDeans);
		contentProfile.append("</ak:signedByAllDeans>");

		contentProfile.append("<ak:signedByAllIRBs>");
		contentProfile.append(signedByAllIRBs);
		contentProfile.append("</ak:signedByAllIRBs>");

		contentProfile.append("<ak:signedByAllResearchAdmins>");
		contentProfile.append(signedByAllResearchAdmins);
		contentProfile.append("</ak:signedByAllResearchAdmins>");

		contentProfile.append("<ak:signedByAllResearchDirectors>");
		contentProfile.append(signedByAllResearchDirectors);
		contentProfile.append("</ak:signedByAllResearchDirectors>");

		contentProfile.append("</ak:proposal>");
		contentProfile.append("</ak:record>");
		contentProfile.append("</Content>");

		contentProfile
				.append("<Attribute AttributeId=\"urn:oasis:names:tc:xacml:3.0:content-selector\" IncludeInResult=\"false\">");
		contentProfile
				.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">//ak:record/ak:proposal</AttributeValue>");
		contentProfile.append("</Attribute>");

		Set<AbstractResult> set = ac.getXACMLdecisionWithObligations(attrMap,
				contentProfile);

		Iterator<AbstractResult> it = set.iterator();
		int intDecision = 3;
		while (it.hasNext()) {
			AbstractResult ar = it.next();
			intDecision = ar.getDecision();

			System.out
					.println("===========================================================");
			if (intDecision >= 4 && intDecision <= 6) {
				intDecision = 2;
			}
			System.out.println("Decision:" + intDecision + " that is: "
					+ AbstractResult.DECISIONS[intDecision]);

			List<ObligationResult> obligations = ar.getObligations();

			List<ObligationResult> preObligations = new ArrayList<ObligationResult>();
			List<ObligationResult> postObligations = new ArrayList<ObligationResult>();
			List<ObligationResult> ongoingObligations = new ArrayList<ObligationResult>();

			for (ObligationResult obligation : obligations) {
				if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
					List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
							.getAssignments();

					String obligationType = "postobligation";

					for (AttributeAssignment assignment : assignments) {
						if (assignment.getAttributeId().toString()
								.equalsIgnoreCase("obligationType")) {
							obligationType = assignment.getContent();
							break;
						}
					}

					if (obligationType.equals("preobligation")) {
						preObligations.add(obligation);
						System.out.println(obligationType + " is FOUND");
					} else if (obligationType.equals("postobligation")) {
						postObligations.add(obligation);
						System.out.println(obligationType + " is FOUND");
					} else {
						ongoingObligations.add(obligation);
						System.out.println(obligationType + " is FOUND");
					}

				}
			}

			Boolean preCondition = false;

			System.out
					.println("\n======================== Printing Obligations ====================");

			for (ObligationResult obligation : preObligations) {
				if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
					List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
							.getAssignments();

					String obligationType = "preobligation";

					for (AttributeAssignment assignment : assignments) {

						// System.out.println("Obligation :  "
						// + assignment.getContent() +
						// "\n");

						switch (assignment.getAttributeId().toString()) {
						// case "obligationType":
						// obligationType = assignment.getContent();
						// break;

						case "signedByCurrentUser":
							preCondition = Boolean.parseBoolean(assignment
									.getContent());
							break;

						}
					}
					System.out.println(obligationType + " is RUNNING");
					if (!preCondition) {
						break;
					}
				}
			}

			if (preCondition) {
				for (ObligationResult obligation : postObligations) {
					if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
						// System.out
						// .println(((org.wso2.balana.xacml3.Obligation)
						// obligation)
						// .getObligationId());

						List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
								.getAssignments();

						String obligationType = "postobligation";

						String emailSubject = new String();
						String emailBody = new String();
						String authorName = new String();
						String piEmail = new String();
						List<String> emaillist = new ArrayList<String>();
						List<String> userlist = new ArrayList<String>();

						for (AttributeAssignment assignment : assignments) {

							// System.out.println("Obligation :  "
							// + assignment.getContent() +
							// "\n");

							switch (assignment.getAttributeId().toString()) {
							// case "obligationType":
							// obligationType = assignment.getContent();
							// break;
							case "emailBody":
								emailBody = assignment.getContent();
								break;
							case "emailSubject":
								emailSubject = assignment.getContent();
								break;
							case "authorName":
								authorName = assignment.getContent();
								break;
							case "piEmail":
								piEmail = assignment.getContent();
								break;
							case "copisEmail":
							case "seniorsEmail":
							case "chairsEmail":
							case "managersEmail":
							case "deansEmail":
							case "irbsEmail":
							case "administratorsEmail":
							case "directorsEmail":
								emaillist.add(assignment.getContent());
								break;

							case "piFullName":
							case "copisFullName":
							case "seniorsFullName":
							case "chairsFullName":
							case "managersFullName":
							case "deansFullName":
							case "irbsFullName":
							case "administratorsFullName":
							case "directorsFullName":
								userlist.add(assignment.getContent());
								break;

							}
						}

						System.out.println(obligationType + " is RUNNING");

						boolean proposalIsChanged = true;
						if (proposalIsChanged) {
							System.out.println(piEmail + ":::" + emaillist
									+ ":::" + userlist + ":::" + emailSubject
									+ authorName + ":::" + emailBody);
						}
					}
				}
				System.out
						.println("===========================================================");
				System.out
						.println("\n======================== Printing Advices ====================");
				List<Advice> advices = ar.getAdvices();
				for (Advice advice : advices) {
					if (advice instanceof org.wso2.balana.xacml3.Advice) {
						List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Advice) advice)
								.getAssignments();
						for (AttributeAssignment assignment : assignments) {
							System.out.println("Advice :  "
									+ assignment.getContent() + "\n");
						}
					}
				}
			}

			assertEquals("Permit", AbstractResult.DECISIONS[intDecision]);
		}
	}
}
