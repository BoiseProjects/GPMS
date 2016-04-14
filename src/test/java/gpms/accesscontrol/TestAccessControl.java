package gpms.accesscontrol;

import static org.junit.Assert.assertEquals;
import gpms.DAL.MongoDBConnector;
import gpms.dao.ProposalDAO;
import gpms.model.Proposal;
import gpms.model.UserAccount;
import gpms.model.UserProfile;

import java.net.UnknownHostException;
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

public class TestAccessControl {

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
	public void test() throws UnknownHostException {
		// ac.getXACMLdecision("Faculty", "Proposal", "Create");
		HashMap<String, Multimap<String, String>> attrMap = new HashMap<String, Multimap<String, String>>();

		Multimap<String, String> subjectMap = ArrayListMultimap.create();
		subjectMap.put("position.type", "Tenured/tenure-track faculty");
		// subjectMap.put("Proposal Role", "PI");

		// subjectMap.put("position.title", "Department Chair");
		attrMap.put("Subject", subjectMap);

		Multimap<String, String> resourceMap = ArrayListMultimap.create();
		resourceMap.put("proposal.section", "Whole Proposal");
		// resourceMap.put("status", "Withdraw by Research Office");

		// resourceMap.put("ApprovedByDepartmentChair", "READYFORAPPROVAL");
		attrMap.put("Resource", resourceMap);

		Multimap<String, String> actionMap = ArrayListMultimap.create();
		actionMap.put("proposal.action", "Add");

		// actionMap.put("proposal.action", "Approve");
		attrMap.put("Action", actionMap);

		// Multimap<String, String> environmentMap = ArrayListMultimap.create();
		// environmentMap.put("device-type", "Android Device");
		// attrMap.put("Environment", environmentMap);

		// ac.getXACMLdecision("Tenured", "Proposal", "Create");
		// ac.getXACMLdecision("Research Staff", "Proposal", "Create");
		// ac.getXACMLdecision("Tenured", "Proposal", "Delete");
		// ac.getXACMLdecision("PI", "Proposal", "Delete");
		// ac.getXACMLdecision("PI", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("PI", "Co-PI-List", "View");
		// ac.getXACMLdecision("Co-PI", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("Co-PI", "Co-PI-List", "View");
		// ac.getXACMLdecision("Senior-Personnel", "Co-PI-List", "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Co-PI-List", "View");
		// ac.getXACMLdecision("PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("Co-PI", "Senior-Personnel-List", "Edit");
		// ac.getXACMLdecision("Co-PI", "Senior-Personnel-List", "View");
		// ac.getXACMLdecision("Senior-Personnel", "Senior-Personnel-List",
		// "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Senior-Personnel-List",
		// "View");
		// ac.getXACMLdecision("PI", "Project-Info", "View");
		// ac.getXACMLdecision("PI", "Project-Info", "Edit");
		// ac.getXACMLdecision("Co-PI", "Project-Info", "View");
		// ac.getXACMLdecision("Co-PI", "Project-Info", "Edit");
		// ac.getXACMLdecision("Senior-Personnel", "Project-Info", "View");
		// ac.getXACMLdecision("Research-Administrator", "OSP-Section", "Edit");

		// String decision = ac.getXACMLdecision("PI", "Project-Info", "View");

		String proposalID = "56fee06865dbb35ce580c907";

		ObjectId proposalId = new ObjectId(proposalID);

		Proposal existingProposal = proposalDAO
				.findProposalByProposalID(proposalId);

		StringBuffer contentProfile = new StringBuffer();

		contentProfile.append("<Content>");
		contentProfile.append("<ak:record xmlns:ak='http://akpower.org'>");
		contentProfile.append("<ak:proposal>");

		contentProfile.append("<ak:proposalid>");
		contentProfile.append(proposalID);
		contentProfile.append("</ak:proposalid>");

		contentProfile.append("<ak:proposaltitle>");
		contentProfile.append(existingProposal.getProjectInfo()
				.getProjectTitle());
		contentProfile.append("</ak:proposaltitle>");

		contentProfile.append("<ak:submittedbypi>");
		contentProfile.append(existingProposal.getSubmittedByPI());
		contentProfile.append("</ak:submittedbypi>");

		contentProfile.append("<ak:readyforsubmissionbypi>");
		contentProfile.append(existingProposal.isReadyForSubmissionByPI());
		contentProfile.append("</ak:readyforsubmissionbypi>");

		contentProfile.append("<ak:deletedbypi>");
		contentProfile.append(existingProposal.getDeletedByPI());
		contentProfile.append("</ak:deletedbypi>");

		contentProfile.append("<ak:approvedbydepartmentchair>");
		contentProfile.append(existingProposal.getChairApproval());
		contentProfile.append("</ak:approvedbydepartmentchair>");

		contentProfile.append("<ak:approvedbybusinessmanager>");
		contentProfile.append(existingProposal.getBusinessManagerApproval());
		contentProfile.append("</ak:approvedbybusinessmanager>");

		contentProfile.append("<ak:approvedbyirb>");
		contentProfile.append(existingProposal.getIrbApproval());
		contentProfile.append("</ak:approvedbyirb>");

		contentProfile.append("<ak:approvedbydean>");
		contentProfile.append(existingProposal.getDeanApproval());
		contentProfile.append("</ak:approvedbydean>");

		contentProfile.append("<ak:approvedbyuniversityresearchadministrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorApproval());
		contentProfile
				.append("</ak:approvedbyuniversityresearchadministrator>");

		contentProfile.append("<ak:withdwarnbyuniversityresearchadmisntrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorWithdraw());
		contentProfile
				.append("</ak:withdwarnbyuniversityresearchadmisntrator>");

		contentProfile.append("<ak:submittedbyuniversityresearchadminstrator>");
		contentProfile.append(existingProposal
				.getResearchAdministratorSubmission());
		contentProfile
				.append("</ak:submittedbyuniversityresearchadminstrator>");

		contentProfile.append("<ak:approvedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorDeletion());
		contentProfile.append("</ak:approvedbyuniversityresearchdirector>");

		contentProfile.append("<ak:deletedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorDeletion());
		contentProfile.append("</ak:deletedbyuniversityresearchdirector>");

		contentProfile.append("<ak:archivedbyuniversityresearchdirector>");
		contentProfile.append(existingProposal.getResearchDirectorArchived());
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
		contentProfile.append("Milson Munakami");
		contentProfile.append("</ak:fullname>");
		contentProfile.append("</ak:authorprofile>");

		contentProfile.append("<ak:pi>");
		contentProfile.append("<ak:fullname>");
		contentProfile.append(existingProposal.getInvestigatorInfo().getPi()
				.getUserRef().getFullName());
		contentProfile.append("</ak:fullname>");

		contentProfile.append("<ak:workemail>");
		contentProfile.append(existingProposal.getInvestigatorInfo().getPi()
				.getUserRef().getWorkEmails().get(0));
		contentProfile.append("</ak:workemail>");

		contentProfile.append("<ak:userid>");
		contentProfile.append(existingProposal.getInvestigatorInfo().getPi()
				.getUserProfileId());
		contentProfile.append("</ak:userid>");
		contentProfile.append("</ak:pi>");

		contentProfile.append("</ak:proposal>");
		contentProfile.append("</ak:record>");
		contentProfile.append("</Content>");
		contentProfile
				.append("<Attribute IncludeInResult=\"false\" AttributeId=\"urn:oasis:names:tc:xacml:3.0:profile:multiple:content-selector\">");
		contentProfile
				.append("<AttributeValue XPathCategory=\"urn:oasis:names:tc:xacml:3.0:attribute-category:resource\" DataType=\"urn:oasis:names:tc:xacml:3.0:data-type:xpathExpression\">/ak:record/ak:proposal</AttributeValue>");
		contentProfile.append("</Attribute>");

		Set<AbstractResult> set = ac.getXACMLdecisionWithObligations(attrMap,
				contentProfile);

		Iterator<AbstractResult> it = set.iterator();
		int intDecision = 3;
		while (it.hasNext()) {
			AbstractResult ar = it.next();
			intDecision = ar.getDecision();

			System.out
					.println("\n======================== Printing Obligations ====================");
			List<ObligationResult> obligations = ar.getObligations();
			for (ObligationResult obligation : obligations) {
				if (obligation instanceof org.wso2.balana.xacml3.Obligation) {
					List<AttributeAssignment> assignments = ((org.wso2.balana.xacml3.Obligation) obligation)
							.getAssignments();
					for (AttributeAssignment assignment : assignments) {
						System.out.println("Obligation :  "
								+ assignment.getContent() + "\n");
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
			System.out
					.println("===========================================================");
			if (intDecision >= 4 && intDecision <= 6) {
				intDecision = 2;
			}
			System.out.println("Decision:" + intDecision + " that is: "
					+ AbstractResult.DECISIONS[intDecision]);

			assertEquals("Permit", AbstractResult.DECISIONS[intDecision]);
		}

		// String decision = ac.getXACMLdecision(attrMap);
		//
		// assertEquals("Permit", decision);
	}

}
